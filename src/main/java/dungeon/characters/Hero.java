package dungeon.characters;

import java.util.ArrayList;

/**
 * Abstract base class for all hero types in the dungeon game.
 * Heroes share common combat attributes from DungeonCharacter and
 * add blocking and potion-related functionality.
 *
 * This class defines the shared structure for Warrior, Priestess,
 * and Thief, but does not implement combat logic in Iteration 1.
 *
 * @author Anthony
 * @version 1.0
 */

public abstract class Hero extends DungeonCharacter {

    /** Number of healing potions currently held by the hero. */
    protected int myHealingPotions;

    /** Number of vision potions currently held by the hero. */
    protected int myVisionPotions;

    /** Tracker for all Pillar items collected by the Hero. */
    protected List<Pillar> myPillarsFound;

    /** Tracks cooldown time for the Hero's ultimate ability. */
    protected int myCDTimer;

    /** Probability that the character blocks an incoming attack. */
    protected double myBlockChance;

    /**
     * Constructs a new Hero with the given combat attributes.
     *
     * @param theName          the hero's name
     * @param theHP            starting hit points
     * @param theMinDamage     minimum attack damage
     * @param theMaxDamage     maximum attack damage
     * @param theAttackSpeed   number of actions per round
     * @param theHitChance     probability of landing an attack
     * @param theBlockChance   probability of dodging an incoming attack
     */

    public Hero(String theName, int theHP, int theMinDamage, int theMaxDamage, int theAttackSpeed,
                double theHitChance, double theBlockChance) {
        super(theName, theHP, theMinDamage, theMaxDamage, theAttackSpeed, theHitChance);
        myBlockChance = theBlockChance;
        myHealingPotions = 0;
        myVisionPotions = 0;
        myPillarsFound = new ArrayList<Pillar>();
        myCDTimer = 0;
    }

    /**
     * Performs the hero's unique special ability.
     * Each subclass defines its own behavior.
     *
     * @param theTarget the target of the special skill
     */
    public abstract void specialSkill(DungeonCharacter theTarget);

    /**
     * Performs the hero's unique ultimate attack.
     * Each subclass defines its own behavior.
     *
     * @param theTarget the target of the special skill
     */
    public abstract void bigCooldown(DungeonCharacter theTarget);

    /**
     * Uses a healing potion if available.
     */
    public void useHealingPotion() {
        // TODO: implement in later iteration.
    }

    /**
     * Uses a vision potion if available.
     */
    public void useVisionPotion() {
        // TODO: implement in later iteration.
    }

    /**
     * Reduces cooldown for ultimate abilities.
     * Logic added in later iterations.
     */
    public void reduceCooldown() {
        // TODO: implement in later iteration.
    }


    /**
     * Returns the number of healing potions the hero currently holds.
     *
     * @return the count of healing potions
     */
    public int getHealingPotion() {
        return myHealingPotions;
    }

    /**
     * Returns the number of vision potions the hero currently holds.
     *
     * @return the count of vision potions
     */
    public int getVisionPotion() {
        return myVisionPotions;
    }

    /**
     * Returns the remaining cooldown time for the hero's ultimate ability.
     *
     * @return the cooldown value
     */
    public int getCDTimer() {
        return myCDTimer;
    }
}
