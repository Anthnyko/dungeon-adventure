package dungeon.model.characters;

import dungeon.model.Dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all hero types in the dungeon game.
 * Heroes share common combat attributes from DungeonCharacter and
 * add blocking and potion-related functionality.
 * <p>
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
    protected List<Character> myPillarsFound;

    /** Tracks cooldown time for the Hero's special skill. */
    protected int mySkillTimer;

    /** Tracks cooldown time for the Hero's ultimate ability. */
    protected int myCDTimer;

    /** Probability that the character blocks an incoming attack. */
    protected final double myBlockChance;

    /** Track whether thief gains extra turn from special skill. */
    protected boolean myExtraTurn;

    /**
     * Constructs a new Hero with the given combat attributes.
     *
     * @param theName          the hero's name
     * @param theHP            starting hit points
     * @param theMaxHP         max hit points
     * @param theMinDamage     minimum attack damage
     * @param theMaxDamage     maximum attack damage
     * @param theAttackSpeed   number of actions per round
     * @param theHitChance     probability of landing an attack
     * @param theBlockChance   probability of dodging an incoming attack
     */

    public Hero(String theName, int theHP, int theMaxHP, int theMinDamage, int theMaxDamage, int theAttackSpeed,
                double theHitChance, double theBlockChance) {
        super(theName, theHP, theMaxHP,theMinDamage, theMaxDamage, theAttackSpeed, theHitChance);
        myBlockChance = theBlockChance;
        myHealingPotions = 3;
        myVisionPotions = 1;
        myPillarsFound = new ArrayList<>();
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
     *
     * @return amount of HP healed
     */
    public int useHealingPotion() {
        final int healPotionValue = 20;
        if (myHealingPotions <= 0) {
            System.out.println("You have no healing potions!");
            return 0;
        }
        myHealingPotions--;
        if (healPotionValue + myHP > myMaxHP) {
            int healValue = myMaxHP - myHP;
            myHP = myMaxHP;
            return healValue;
        } else {
            myHP += healPotionValue;
        }
        System.out.println("You used healing potion! +20 HP");
        return healPotionValue;
    }

    /**
     * Uses a vision potion if available.
     */
    public void useVisionPotion(Dungeon theDungeon) {
        if (myVisionPotions <= 0) {
            System.out.println("You have no vision potions!");
            return;
        }
        myVisionPotions--;

        System.out.println("You used a vision potion! The surrounding area becomes clear.");
        theDungeon.revealSurroundingRooms();
    }

    /**
     * Reduces cooldown for ultimate abilities.
     */
    public void reduceCooldown() {
        if (myCDTimer > 0) {
            myCDTimer--;
        }
        if (mySkillTimer > 0) {
            mySkillTimer--;
        }
    }

    /**
     * Resets all cooldowns on Hero
     */
    public void resetCooldowns() {
        myCDTimer = 0;
        mySkillTimer = 0;
    }

    /**
     * Reduces all ability cooldowns by 1, to be used outside combat
     * (e.g., when moving between rooms).
     */
    public void tickCooldowns() {
        reduceCooldown();
    }

    /**
     * Resets temporary combat-only status effects.
     * Called after combat ends.
     */
    public void resetStatusEffects() {
        myExtraTurn = false;
    }

    /**
     * Displays the hero's current combat status, including HP, potions,
     * cooldown timers, and any other relevant combat information.
     */
    @Override
    public void displayStatus() {
        System.out.println("=== HERO STATUS ===");
        System.out.println("Name: " + myCharName);
        System.out.println("HP: " + myHP + "/" + myMaxHP);
        System.out.println("Healing Potions: " + myHealingPotions);
        System.out.println("Vision Potions: " + myVisionPotions);
        System.out.println("Ultimate Cooldown: " + myCDTimer);
        System.out.println("====================");
        System.out.println("Abilities:");
        System.out.println("1. Basic Attack");
        System.out.println("2. " + getSpecialSkillName());
        System.out.println("3. " + getUltimateName() + " (CD: " + myCDTimer + ")");
        System.out.println("====================");
    }

    /** Displays actions for the user to choose from.
     *
     * @param choice action chosen
     * @param theTarget targeted monster of action
     */
    public void performAction(final int choice, final Monster theTarget) {
        switch (choice) {
            case 1:
                attack(theTarget);
                break;
            case 2:
                specialSkill(theTarget);
                break;
            case 3:
                bigCooldown(theTarget);
                break;
            case 4:
                myLastHeal = useHealingPotion();
        }
    }

    /** Returns if Thief skill gained extra turn. */
    public boolean hasExtraTurn() {
        return myExtraTurn;
    }

    /** Sets extra turn to false. Logic is in Thief class. */
    public void consumeExtraTurn() {}

    /** @return the probability of blocking an attack */
    public double getBlockChance() {
        return myBlockChance;
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
     * Returns the characters of the collected pillars
     * 
     * @return the pillars collected (ex. [A, I])
     */
    public List<Character> getMyPillars() {
        return myPillarsFound;
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
     * Gains +1 to the total number of healing potions.
     */
    public void gainHealingPotion() {
        myHealingPotions += 1;
    }

    /**
     * Gains +1 to the total number of vision potions.
     */
    public void gainVisionPotion() {
        myVisionPotions += 1;
    }

    /**
     * Adds a pillar to the inventory.
     * 
     * @param thePillar
     */
    public void gainPillar(final char thePillar) {
        myPillarsFound.add(thePillar);
    }

    /**
     * Returns the total number of pillars this hero has collected.
     * 
     * @return the number of pillars collected
     */
    public int getPillarCount() {
        return myPillarsFound.size();
    }

    /**
     * Returns the remaining cooldown time for the hero's skill.
     *
     * @return the cooldown value
     */
    public int getSkillTimer() {
        return mySkillTimer;
    }

    /**
     * Returns the remaining cooldown time for the hero's ultimate ability.
     *
     * @return the cooldown value
     */
    public int getCDTimer() {
        return myCDTimer;
    }

    /** @return the name of special skill */
    public abstract String getSpecialSkillName();

    /** @return the name of ultimate skill */
    public abstract String getUltimateName();

    @Override
    public String toString() {
        return "Hero {" +
                "Name='" + myCharName + '\'' +
                ", HP=" + myHP + "/" + myMaxHP +
                ", HealingPotions=" + myHealingPotions +
                ", VisionPotions=" + myVisionPotions +
                ", UltimateCooldown=" + myCDTimer +
                '}';
    }
}
