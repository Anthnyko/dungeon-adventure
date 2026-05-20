package dungeon.model.characters;

/**
 * The Monster class represents all enemy characters encountered in the dungeon.
 * Monsters inherit core combat attributes from {@link DungeonCharacter} and add
 * additional mechanics unique to monster behavior, such as healing and status
 * effects. Specific monster types (e.g., Ogre, Gremlin, Skeleton) extend this
 * class and define their own stats.
 *
 * <p>Each Monster has:
 * <ul>
 *     <li>a chance to heal after taking damage</li>
 *     <li>a minimum and maximum heal amount</li>
 *     <li>a bleed timer used for damage-over-time effects (implemented later)</li>
 * </ul>
 *
 * <p>Combat logic, healing behavior, and bleed effects are implemented in later
 * iterations. This class provides the structural foundation required for
 * polymorphic monster behavior.</p>
 *
 * @author Anthony
 * @version 1.0
 */
public abstract class Monster extends DungeonCharacter {

    /** The probability that the monster successfully heals. */
    protected double myHealChance;

    /** The minimum amount of HP restored when healing succeeds. */
    protected int myMinHeal;

    /** The maximum amount of HP restored when healing succeeds. */
    protected int myMaxHeal;

    /** Tracks the number of turns remaining for bleed damage effects. */
    protected int myBleedTimer;

    /** Amount of bleed damage per tick. */
    protected final int myBleedDamage = 5;

    /** Checks if monster died from bleed damage. */
    protected boolean myDiedFromBleed;

    /**
     * Constructs a Monster with predefined combat and healing attributes.
     *
     * @param theName        the name of the monster
     * @param theHP          the monster's starting hit points
     * @param theMaxHP       the monster's max hit points
     * @param theMinDamage   the minimum damage the monster can deal
     * @param theMaxDamage   the maximum damage the monster can deal
     * @param theAttackSpeed the monster's attack speed
     * @param theHitChance   the probability the monster successfully hits
     * @param theHealChance  the probability the monster successfully heals
     * @param theMinHeal     the minimum amount of HP restored when healing
     * @param theMaxHeal     the maximum amount of HP restored when healing
     */
    public Monster(String theName, int theHP, int theMaxHP,int theMinDamage, int theMaxDamage,
                   int theAttackSpeed, double theHitChance,
                   double theHealChance, int theMinHeal, int theMaxHeal) {

        super(theName, theHP, theMaxHP, theMinDamage, theMaxDamage, theAttackSpeed, theHitChance);

        myHealChance = theHealChance;
        myMinHeal = theMinHeal;
        myMaxHeal = theMaxHeal;
        myBleedTimer = 0;
        myDiedFromBleed = false;
    }

    /**
     * Attempts to heal the monster.
     * This method will be called during combat
     * after the monster takes damage.
     */
    public void heal() {
        if (!isAlive() || myHP == myMaxHP) {
            return;
        }
        myLastHeal = 0;
        if (rng.nextDouble() < myHealChance) {
            final int heal = myMinHeal + (int)(rng.nextDouble() * (myMaxHeal - myMinHeal + 1));
            if (heal + myHP > myMaxHP) {
                myLastHeal = myMaxHP - myHP;
                myHP = myMaxHP;
            } else {
                myHP += heal;
                myLastHeal = heal;
            }
            System.out.println(myCharName + " regenerates " + heal + " health!");
        }
    }

    /**
     * Applies a bleed effect to this monster for the given number of turns.
     *
     * @param theTimer number of turns the bleed should last
     */
    public void applyBleed(final int theTimer) {
        myBleedTimer = theTimer;
        System.out.println(myCharName + " starts bleeding!");
    }

    /**
     * Processes bleed damage at the start of the monster's turn.
     * Deals damage and reduces the bleed timer.
     */
    public int processBleed() {
        if (myBleedTimer > 0) {
            takeDamage(myBleedDamage);
            myBleedTimer--;

            System.out.println(myCharName + " takes " + myBleedDamage + " bleed damage!");

            if (!isAlive()) {
                myDiedFromBleed = true;
                System.out.println(myCharName + " dies from bleeding!");
            }
            return myBleedDamage;
        }
        return 0;
    }

    /**
     * Displays the monster's current combat status, including HP.
     */
    @Override
    public void displayStatus() {
        System.out.println("=== MONSTER STATUS ===");
        System.out.println("Name: " + myCharName);
        System.out.println("HP: " + myHP + "/" + myMaxHP);
        System.out.println("====================");
    }

    /**
     * @return the number of turns remaining for bleed damage effects
     */
    public int getMyBleedTimer() {
        return myBleedTimer;
    }

    /**
     * @return if monster dies from bleed
     */
    public boolean diedFromBleed() {
        return myDiedFromBleed;
    }
}
