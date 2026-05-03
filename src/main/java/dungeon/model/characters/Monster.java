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
    }

    /**
     * Attempts to heal the monster. Actual healing logic is implemented
     * in later iterations. This method will be called during combat
     * after the monster takes damage.
     */
    public void heal() {
        // TODO: implement in later iteration.
    }

    /**
     * @return the probability that the monster successfully heals
     */
    public double getMyHealChance() {
        return myHealChance;
    }

    /**
     * @return the minimum amount of HP the monster can heal
     */
    public int getMyMinHeal() {
        return myMinHeal;
    }

    /**
     * @return the maximum amount of HP the monster can heal
     */
    public int getMyMaxHeal() {
        return myMaxHeal;
    }

    /**
     * @return the number of turns remaining for bleed damage effects
     */
    public int getMyBleedTimer() {
        return myBleedTimer;
    }
}
