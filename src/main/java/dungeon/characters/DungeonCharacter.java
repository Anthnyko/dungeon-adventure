package dungeon.characters;

/**
 * Abstract base class representing any character in the dungeon.
 * Both heroes and monsters share these core combat attributes.
 *
 * This class defines the common structure for all character types
 * but does not implement combat logic in Iteration 1.
 *
 * @author Anthony
 * @version 1.0
 */
public abstract class DungeonCharacter {

    /** The character's name. */
    protected String myCharName;

    /** Max hit points of the character. */
    protected int myMaxHP;

    /** Current hit points of the character. */
    protected int myHP;

    /** Minimum damage the character can deal. */
    protected int myMinDamage;

    /** Maximum damage the character can deal. */
    protected int myMaxDamage;

    /** Number of actions the character can perform per round. */
    protected int myAttackSpeed;

    /** Probability that the character successfully lands an attack. */
    protected double myHitChance;


    /**
     * Constructs a new DungeonCharacter with the given combat attributes.
     *
     * @param theName        the character's name
     * @param theMaxHP       max hit points
     * @param theHP          starting hit points
     * @param theMinDamage   minimum attack damage
     * @param theMaxDamage   maximum attack damage
     * @param theAttackSpeed number of actions per round
     * @param theHitChance   probability of landing an attack
     */
    protected DungeonCharacter(String theName, int theHP, int theMaxHP, int theMinDamage, int theMaxDamage,
                               int theAttackSpeed, double theHitChance) {

        myCharName = theName;
        myMaxHP = theMaxHP;
        myHP = theHP;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myAttackSpeed = theAttackSpeed;
        myHitChance = theHitChance;
    }

    /**
     * Performs an attack on the target character.
     *
     * @param theTarget the character being attacked
     */
    public void attack(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        int numAttacks = myAttackSpeed / theTarget.myAttackSpeed;
        if (numAttacks < 1) numAttacks = 1;

        for (int i = 0; i < numAttacks; i++) {
            if (Math.random() < myHitChance) {
                final int damage = myMinDamage + (int)(Math.random() * (myMaxDamage - myMinDamage + 1));
                theTarget.takeDamage(damage);
                System.out.println(myCharName + " attacks " + theTarget.getCharName() + " for " + damage + " damage!");
            } else {
                System.out.println(myCharName + " misses " + theTarget.myCharName + "...");
            }
            if (!theTarget.isAlive()) {
                break;
            }
        }

    }

    /**
     * Determines whether the character is still alive.
     *
     * @return true if HP is above zero, false otherwise
     */
    public boolean isAlive() {
        // TODO: implement in later iteration.
        return false;
    }

    /**
     * Applies damage to the character.
     *
     * @param damage the amount of damage taken
     */
    public void takeDamage(int damage) {
        // TODO: implement in later iteration.
    }

    /** @return the character's name */
    public String getCharName() {
        return myCharName;
    }

    /** @return the character's current hit points */
    public int getHP() {
        return myHP;
    }

    /** @return the character's max hit points */
    public int getMaxHP() { return myMaxHP; }

    /** @return the minimum damage value */
    public int getMinDamage() {
        return myMinDamage;
    }

    /** @return the maximum damage value */
    public int getMaxDamage() {
        return myMaxDamage;
    }

    /** @return the character's attack speed */
    public int getAttackSpeed() {
        return myAttackSpeed;
    }

    /** @return the probability of landing an attack */
    public double getHitChance() {
        return myHitChance;
    }
}
