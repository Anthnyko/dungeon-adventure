package dungeon.model.characters;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Abstract base class representing any character in the dungeon.
 * Both heroes and monsters share these core combat attributes.
 * <p>
 * This class defines the common structure for all character types
 * but does not implement combat logic in Iteration 1.
 *
 * @author Anthony
 * @version 1.0
 */
public abstract class DungeonCharacter {

    /** The character's name. */
    protected final String myCharName;

    /** Max hit points of the character. */
    protected final int myMaxHP;

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

    /** Last damage value used. */
    protected int myLastDamageDealt;

    /** Last heal value used. */
    protected int myLastHeal;

    /** Rolls random value for abilities. */
    protected Random rng = new Random();

    protected Consumer<String> myAttackLogger;


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
        myLastDamageDealt = 0;
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

            // 🔹 Hook: Monster can override this to add blockChance
            if (!beforeHit(theTarget)) {
                continue; // skip this swing
            }

            if (rng.nextDouble() < myHitChance) {
                final int damage = myMinDamage +
                        (int)(rng.nextDouble() * (myMaxDamage - myMinDamage + 1));

                myLastDamageDealt = damage;
                theTarget.takeDamage(damage);

                if (myAttackLogger != null) {
                    myAttackLogger.accept(myCharName + " hits " +
                            theTarget.getCharName() + " (-" + damage + ")");
                }

            } else {
                myLastDamageDealt = 0;

                System.out.println(myCharName + " misses " + theTarget.getCharName() + "...");

                if (myAttackLogger != null) {
                    myAttackLogger.accept(myCharName + " misses " + theTarget.getCharName());
                }
            }

            if (!theTarget.isAlive()) break;
        }
    }

    /**
     * Hook for subclasses to modify behavior before a hit.
     * Return false to cancel this attack (e.g., Hero blocks).
     */
    protected boolean beforeHit(DungeonCharacter theTarget) {
        return true;
    }


    /**
     * Performs a single basic attack against the specified target.
     * This method applies hit chance and damage calculation but does
     * not use attack‑speed–based multi‑attack logic. It is intended
     * for hero abilities that require exactly one attack.
     *
     * @param theTarget the character being attacked
     */
    public void singleAttack(DungeonCharacter theTarget) {
        if (!isAlive() || theTarget == null || !theTarget.isAlive()) {
            return;
        }

        if (rng.nextDouble() < myHitChance) {
            int dmg = myMinDamage + rng.nextInt(myMaxDamage - myMinDamage + 1);
            myLastDamageDealt = dmg;
            theTarget.takeDamage(dmg);
        } else {
            myLastDamageDealt = 0;
            System.out.println(myCharName + " misses " + theTarget.getCharName() + "...");
        }
    }

    /**
     * Determines whether the character is still alive.
     *
     * @return true if HP is above zero, false otherwise
     */
    public boolean isAlive() {
        return myHP > 0;
    }

    /**
     * Applies damage to the character.
     *
     * @param damage the amount of damage taken
     */
    public void takeDamage(int damage) {
        myHP -= damage;
        if (myHP <= 0) {
            myHP = 0;
        }
        System.out.println(myCharName + " takes " + damage + " damage! (HP: " + myHP + ")");
    }

    /**
     * Applies bleed damage to the Monster.
     *
     * @param damage the amount of damage taken
     */
    public void takeBleedDamage(int damage) {
        myHP -= damage;
        if (myHP <= 0) {
            myHP = 0;
        }
        System.out.println(myCharName + " bleeds " + damage + " damage! (HP: " + myHP + ")");
    }

    public void applyHeal(int theHealAmount) {
        myHP += theHealAmount;
        if (myHP >= myMaxHP) {
            myHP = myMaxHP;
        }
        System.out.println(myCharName + " heals " + theHealAmount + " health! (HP: " + myHP + ")");
    }

    /**
     * Returns last damage dealt by the character.
     * <p>
     * Used for combat logging.
     * @return last damage dealt
     */
    public int getLastDamageDealt() {
        return myLastDamageDealt;
    }

    public int getLastHeal() {
        return myLastHeal;
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

    public void setAttackLogger(Consumer<String> logger) {
        myAttackLogger = logger;
    }
}
