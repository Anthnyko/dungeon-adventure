package dungeon.model.characters;

/**
 * The Thief hero class. Thieves specialize in agility-based combat
 * and rely on speed and precision to outmaneuver enemies. This class
 * defines the Thief's base stats and provides stubs for its special
 * abilities, which are implemented in later iterations.
 * <p>
 * The Thief has two unique abilities:
 * - surpriseAttack: the Thief's special skill
 * - garrote: the Thief's ultimate cooldown ability
 *
 * @author Anthony
 * @version 1.0
 */
public class Thief extends Hero {

    /**
     * Constructs a new Thief with predefined combat attributes.
     *
     * @param theName the name of the Thief
     */
    public Thief(String theName) {
        super(theName,
                75,   // HP
                75,   // max HP
                20,   // min damage
                40,   // max damage
                6,    // attack speed
                0.8,  // hit chance
                0.4); // block chance (or dodge if you keep it)
        myExtraTurn = false;
    }

    /**
     * Performs the Thief's special skill.
     * This method delegates to the private surpriseAttack method.
     *
     * @param theTarget the target of the special skill
     */
    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        surpriseAttack(theTarget);
    }

    /**
     * Performs the Thief's ultimate ability.
     * This method delegates to the private garrote method.
     *
     * @param theTarget the target of the ultimate attack
     */
    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        garrote(theTarget);
    }

    /** Returns Thief's special skill name. */
    @Override
    public String getSpecialSkillName() {
        return "Surprise Attack";
    }

    /** Returns Thief's ultimate skill name. */
    @Override
    public String getUltimateName() {
        return "Garrote";
    }

    /** Sets extra turn to false. */
    @Override
    public void consumeExtraTurn() {
        myExtraTurn = false;
    }

    /**
     * Executes the Thief's surprise attack ability.
     *
     * @param theTarget the target of the surprise attack
     */
    private void surpriseAttack(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        final double roll = rng.nextDouble();

        if (roll < 0.40) {
            System.out.println(getCharName() + " performs a Surprise Attack! Extra turn gained!");
            singleAttack(theTarget);
            myLastDamageDealt = theTarget.getLastDamageDealt();
            myExtraTurn = true;

        } else if (roll < 0.85) {
            System.out.println(getCharName() + " attempts a Surprise Attack but only lands a normal hit.");
            singleAttack(theTarget);
            myLastDamageDealt = theTarget.getLastDamageDealt();
            myExtraTurn = false;

        } else {
            System.out.println(getCharName() + " fails the Surprise Attack completely!");
            myLastDamageDealt = 0;
            myExtraTurn = false;
        }
    }


    /**
     * Executes the Thief's garrote ability, used as the ultimate attack.
     *
     * @param theTarget the target of the garrote attack
     */
    private void garrote(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        System.out.println(getCharName() + " uses Garrote!");

        if (theTarget instanceof Monster monster) {
            monster.applyBleed(8);
        }

        myCDTimer = 3;

        myLastDamageDealt = 0;
        myLastHeal = 0;
    }
}