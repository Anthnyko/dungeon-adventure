package dungeon.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeon.model.characters.Hero;
import dungeon.model.characters.Monster;
import dungeon.model.items.HealingPotion;
import dungeon.model.items.Item;
import dungeon.model.items.VisionPotion;

/**
 * Represents a single room in the dungeon
 * <p>
 * A room contains doors that connect it to adjacent rooms in the North,
 * South, East, and West. The room is responsible to storing items and behaviors
 * and applies item effects once the adventurer enters it.
 * <p>
 * Room content is randomly generated while special rooms (entrance, exit, pillars)
 * are decided by the Dungeon class.
 *
 * @author Ibrahim Mohamud
 * @version 1.1
 */
public class Room {
    /** Tracks if there is a north door or not */
    private boolean myNorthDoor;

    /** Tracks if there is an east door or not */
    private boolean myEastDoor;

    /** Tracks if there is a west door or not */
    private boolean myWestDoor;

    /** Tracks if there is a south door or not */
    private boolean mySouthDoor;

    /** Tracks if this room is the entrance */
    private boolean myEntrance;

    /** Tracks if this room is the Exit */
    private boolean myExit;

    /** Tracks if the room has been revealed or not */
    private boolean myRevealed;

    /** Tracks the monster in this room if it has one **/
    private Monster myMonster;

    /** Tracks if this room has a pit */
    private boolean myPit;

    private boolean myFountain;

    /** Tracks if this room has a pillar */
    private boolean myPillar;

    /** The Pillar of OO this room contains */
    private char myPillarType; //put the letter of the pillar 'A', 'E', 'I', 'P'

    /** The list of items this room contains */
    private final List<Item> myItems;

    /** The chance that each item or event has to be placed */
    public int myChance = 10;//Players luck maybe?

    /** Random object for random generation */
    private final Random myRandom;

    /**
     * Constructs a new room with randomly generated items.
     * Each item has an independent chance of being placed.
     */
    public Room() {
        myRandom = new Random();
        myItems = new ArrayList<Item>();

        generateRandomItems();

        myPit = myRandom.nextInt(100) < myChance;

        myEntrance = false;
        myExit = false;
        myPillar = false;
    }

    //---SETTERS---\\

    /**
     * Sets whether the room has a door to the north
     * @param theValue true if there is a door, false if not
     */
    public void setNorthDoor(final boolean theValue) { myNorthDoor = theValue;}

    /**
     * Sets whether the room has a door to the east
     * @param theValue true if there is a door, false if not
     */
    public void setEastDoor(final boolean theValue) { myEastDoor = theValue;}

    /**
     * Sets whether the room has a door to the west
     * @param theValue true if there is a door, false if not
     */
    public void setWestDoor(final boolean theValue) { myWestDoor = theValue;}

    /**
     * Sets whether the room has a door to the south
     * @param theValue true if there is a door, false if not
     */
    public void setSouthDoor(final boolean theValue) { mySouthDoor = theValue;}

    /**
     * Marks this room as the entrance.
     * Entrance rooms contain no items.
     */
    public void setEntrance() {
        myEntrance = true;
        clearRoom();
    }

    /**
     * Marks this room as the exit.
     * Exit rooms contain no items.
     */
    public void setExit() {
        myExit = true;
        clearRoom();
    }

    /**
     * Marks this room as revealed
     */
    public void setRevealed(final boolean theValue) {
        myRevealed = theValue;
    }

    /**
     * Places a pillar in this room.
     * @param thePillarType the type of the pillar ('A', 'E', 'I', 'P')
     */
    public void setPillar(final char thePillarType) {
        myPillar = true;
        myPillarType = thePillarType;
    }

    /**
     * Places a fountain of chance in this room.
     */
    public void setFountain(final boolean theValue) {
        myFountain = theValue;
    }

    /**
     * Places a monster in this room.
     * @param theMonster monster placed in this room
     */
    public void setMonster(final Monster theMonster) {
        myMonster = theMonster;
    }

    //---GETTERS---\\

    /**
     * @return true if the room has a north door
     */
    public boolean hasNorthDoor() {return myNorthDoor;}

    /**
     * @return true if the room has an east door
     */
    public boolean hasEastDoor() {return myEastDoor;}

    /**
     * @return true if the room has a west door
     */
    public boolean hasWestDoor() {return myWestDoor;}

    /**
     * @return true if the room has a south door
     */
    public boolean hasSouthDoor() {return mySouthDoor;}

    /**
     * @return true if the room has a pillar
     */
    public boolean hasPillar() {return myPillar;}

    /**
     * @return true if the room has a pit
     */
    public boolean hasPit() {return myPit;}

    /**
     * @return true if the room has a fountain
     */
    public boolean hasFountain() {return myFountain;}

    /**
     * @return true if the room has a monster
     */
    public boolean hasMonster() {return myMonster != null;}

    /**
     * @return true if the room has items
     */
    public boolean hasItems() {
        return !myItems.isEmpty();
    }

    /**
     * @return true if the room is the entrance
     */
    public boolean isEntrance() {return myEntrance;}

    /**
     * @return true if the room is the exit
     */
    public boolean isExit() {return myExit;}

    /**
     * @return true if this room has been revealed
     */
    public boolean isRevealed() {return myRevealed;}

    /**
     * @return the monster in this room, or null if non exists
     */
    public Monster getMonster() {
        return myMonster;
    }


    /**
     * Removes the list of items in the room and returns them
     * @return A list of all the items in the room
     */
    public List<Item> pickUpItems() {
        final List<Item> items = new ArrayList<>(myItems);
        myItems.clear();
        return items;
    }

    /**
     * Removes the pillar in the room and returns it
     * @return the pillar in the room
     */
    public char pickUpPillar() {
        if (!myPillar) return ' ';
        myPillar = false;
        final char temp = myPillarType;
        myPillarType = ' '; //So a pillar is not shown in the room anymore
        return temp;
    }

    /**
     * applies the pit damage onto the hero if this room has a pit.
     *
     * @param theHero the hero to apply the pit damage on
     * @return the amount of damage the pit did
     */
    public int triggerPitDamage(final Hero theHero) {
        if (!hasPit()) return 0;

        final int damage = myRandom.nextInt(20) + 1;
        System.out.println(theHero.getCharName() + " has fallen into a pit trap");
        theHero.takeDamage(damage);
        myPit = false;
        return damage;
    }

    /**
     * heals the hero if this room has a fountain.
     *
     * @param theHero the hero to heal
     * @return the amount of health healed
     */
    public int activateFountainHeal(final Hero theHero) {
        if (!hasFountain()) return 0;

        final int healAmount = 40;
        System.out.println(theHero.getCharName() + " has arrived at the Fountain of Chance");
        theHero.applyHeal(healAmount);
        myFountain = false;

        return 40;
    }

    /**
     * Removes the monster from this room.
     * Used for when a monster is defeated.
     */
    public void removeMonster() {
        myMonster = null;
    }

    /**
     * Adds an item to the room
     */
    public void addItem(final Item theItem) {
        myItems.add(theItem);
    }

    /**
     * Sets if this room has a pit or not
     */
    public void setPit(final boolean theValue) {
        myPit = theValue;
    }

    /**
     * Returns a string representation of the room.
     * Walls are represented with '*' and doors with '-' or '|'.
     * The center character represents the content of the room.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        // Top row
        sb.append(myNorthDoor ? "*-*" : "***").append("\n");

        // Middle row
        sb.append(myWestDoor ? "|" : "*");
        sb.append(getRoomSymbol());
        sb.append(myEastDoor ? "|" : "*").append("\n");

        // Bottom row
        sb.append(mySouthDoor ? "*-*" : "***");

        return sb.toString();
    }

    // Private helpers
    private char getRoomSymbol() {
        if (myEntrance) return 'i';
        if (myExit) return 'O';

        int count = 0;
        if (myPit) count++;
        if (myFountain) count++;
        if (myPillar) count++;
        count += myItems.size();

        if (count > 1) return 'M';

        if (myPillar) return myPillarType;
        if (myFountain) return '+';
        if (myPit) return 'X';
        if(myItems.size() == 1) return myItems.getFirst().getSymbol();

        return ' ';
    }

    private void clearRoom() {
        myItems.clear();
        myPit = false;
    }

    private void generateRandomItems() {
        if (myRandom.nextInt(100) < myChance) myItems.add(new HealingPotion());
        if (myRandom.nextInt(100) < myChance) myItems.add(new VisionPotion());
    }
}
