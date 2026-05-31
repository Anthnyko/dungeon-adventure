package dungeon.model.characters;

/**
 * The Warrior hero class. Warriors specialize in high-damage physical
 * combat and rely on strength and resilience to overpower enemies.
 * This class defines the Warrior's base stats and provides stubs
 * for its special abilities, which are implemented in later iterations.
 * <p>
 * The Warrior has two unique abilities:
 * - crushingBlow: the Warrior's special skill
 * - enrage: the Warrior's ultimate cooldown ability
 *
 * @author Anthony
 * @version 1.0
 */
public class Warrior extends Hero {

    /** Minimum damage the character can deal on Crushing Blow. */
    protected final int myMinCrushDamage;

    /** Maximum damage the character can deal on Crushing Blow. */
    protected final int myMaxCrushDamage;

    /** Tracks turn duration of Enrage. */
    protected int myEnrageTimer;

    /**
     * Constructs a new Warrior with predefined combat attributes.
     *
     * @param theName the name of the Warrior
     * @param theHealingPotions the number of healing potions
     * @param theVisionPotions the number of healing potions
     */
    public Warrior(String theName, int theHealingPotions, int theVisionPotions) {
        super(theName,
                125,  // HP
                125,  // max HP
                35,   // min damage
                60,   // max damage
                4,    // attack speed
                0.8,  // hit chance
                0.2, // block chance
                theVisionPotions,
                theVisionPotions); 
        myMinCrushDamage = 75;
        myMaxCrushDamage = 150;
        myEnrageTimer = 0;
    }

    @Override
    public void attack(final DungeonCharacter theTarget) {
        if (myEnrageTimer > 0) {
            // temporarily boost damage
            int originalMin = myMinDamage;
            int originalMax = myMaxDamage;

            myMinDamage += 7;
            myMaxDamage += 7;

            super.attack(theTarget);

            // restore original values
            myMinDamage = originalMin;
            myMaxDamage = originalMax;
            myEnrageTimer--;
            if (myEnrageTimer == 0) {
               System.out.println("Enrage has worn off...");
               myCDTimer = 5;
            }
        } else {
            super.attack(theTarget);
        }
    }

    /**
     * Performs the Warrior's special skill.
     * This method delegates to the private crushingBlow method.
     *
     * @param theTarget the target of the special skill
     */
    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        crushingBlow(theTarget);
    }

    /**
     * Performs the Warrior's ultimate ability.
     * This method delegates to the private enrage method.
     *
     * @param theTarget the target of the ultimate attack
     */
    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        enrage();
    }

    /**
     * Resets temporary combat-only status effects.
     * Called after combat ends.
     */
    @Override
    public void resetStatusEffects() {
        super.resetStatusEffects();
        myEnrageTimer = 0;
    }

    /** Returns Warrior's special skill name. */
    @Override
    public String getSpecialSkillName() {
        return "Crushing Blow";
    }

    /** Returns Warrior's ultimate skill name. */
    @Override
    public String getUltimateName() {
        return "Enrage";
    }

    /**
     * Returns the name of this hero class.
     *
     * @return the class name "Warrior"
     */
    public String getClassName() {
        return "Warrior";
    }

    /** Return Warrior's Enrage timer. */
    public int getMyEnrageTimer() {return myEnrageTimer;}

    /**
     * Executes the Warrior's crushing blow ability.
     * Logic implemented in later iterations.
     *
     * @param theTarget the target of the crushing blow
     */
    private void crushingBlow(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        if (rng.nextDouble() < 0.40) {

            System.out.println(getCharName() + " uses Crushing Blow!");

            final int damage = myMinCrushDamage + (int) (rng.nextDouble() * (myMaxCrushDamage - myMinCrushDamage + 1));
            myLastDamageDealt = damage;
            theTarget.takeDamage(damage);
        } else {
            System.out.println(getCharName() + " failed to use Crushing Blow!");
            myLastDamageDealt = 0;
        }
    }

    /**
     * Executes the Warrior's enrage ability, used as the ultimate move.
     * <p>
     * On successful attack, the enrage buff is decremented by 1.
     */
    private void enrage() {
        System.out.println(getCharName() + " becomes enraged! (+25 HP)");
        myHP += 25;
        myEnrageTimer = 2;

        myLastDamageDealt = 0;
        myLastHeal = 25;
    }
}