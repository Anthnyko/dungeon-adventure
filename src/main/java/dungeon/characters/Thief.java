package dungeon.characters;

/**
 * The Thief hero class. Thieves specialize in agility-based combat
 * and rely on speed and precision to outmaneuver enemies. This class
 * defines the Thief's base stats and provides stubs for its special
 * abilities, which are implemented in later iterations.
 *
 * The Thief has two unique abilities:
 * - surpriseAttack: the Thief's special skill
 * - garrote: the Thief's ultimate cooldown ability
 *
 * Combat logic is added in future iterations.
 *
 * @author Anthony
 * @version 1.0
 */
public class Thief extends Hero {

    /** Track whether thief gains extra turn from special skill. */
    protected boolean myExtraTurn;

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

    /**
     * Executes the Thief's surprise attack ability.
     *
     * @param theTarget the target of the surprise attack
     */
    private void surpriseAttack(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        final double roll = Math.random();

        if (roll < 0.40) {
            // 40%: Surprise success
            System.out.println(getCharName() + " performs a Surprise Attack! Extra turn gained!");
            attack(theTarget); // one normal attack
            myExtraTurn = true;

        } else if (roll < 0.80) {
            // Next 40%: Normal attack, no extra turn
            System.out.println(getCharName() + " attempts a Surprise Attack but only lands a normal hit.");
            attack(theTarget);
            myExtraTurn = false;

        } else {
            // Final 20%: Total failure
            System.out.println(getCharName() + " fails the Surprise Attack completely!");
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
            monster.applyBleed(3);
        }

        myCDTimer = 3;
    }
}