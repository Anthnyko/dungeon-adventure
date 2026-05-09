package dungeon.model;

import dungeon.items.Item;
import dungeon.items.HealingPotion;
import dungeon.items.VisionPotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a single room in the dungeon
 *
 * A room contains doors that connect it to adjacent rooms in the North,
 * South, East, and West. The room is responsible to storing items and behaviors
 * and applies item effects once the adventurer enters it.
 *
 * Room content is randomly generated while special rooms (entrance, exit, pillars)
 * are decided by the Dungeon class.
 *
 * @author Ibrahim Mohamud
 * @version 1.0
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

    /** Tracks if this room has a pit */
    private boolean hasPit;

    /** Tracks if this room has a pillar */
    private boolean hasPillar;
    /** The Pillar of OO this room contains */
    private char pillarType; //put the letter of the pillar 'A', 'E', 'I', 'P'

    /** The list of items this room contains */
    private List<Item> myItems;

    /** The chance that each item has to be placed */
    public int myChance = 10; //Players luck maybe?
    private final Random myRandom;

    /**
     * Constructs a new room with randomly generated items.
     * Each item has an independent chance of being placed.
     */
    public Room() {
        myRandom = new Random();
        myItems = new ArrayList<Item>();

        generateRandomItems();

        hasPit = myRandom.nextInt(100) < myChance;

        myEntrance = false;
        myExit = false;
        hasPillar = false;
    }

    //---SETTERS---\\

    /**
     * Sets whether the room has a door to the north
     * @param value true if there is a door, false if not
     */
    public void setNorthDoor(boolean value) { myNorthDoor = value;}

    /**
     * Sets whether the room has a door to the east
     * @param value true if there is a door, false if not
     */
    public void setEastDoor(boolean value) { myEastDoor = value;}

    /**
     * Sets whether the room has a door to the west
     * @param value true if there is a door, false if not
     */
    public void setWestDoor(boolean value) { myWestDoor = value;}

    /**
     * Sets whether the room has a door to the south
     * @param value true if there is a door, false if not
     */
    public void setSouthDoor(boolean value) { mySouthDoor = value;}

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
     * Places a pillar in this room.
     * @param thePillarType the type of the pillar ('A', 'E', 'I', 'P')
     */
    public void setPillar(char thePillarType) {
        hasPillar = true;
        pillarType = thePillarType;
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
     * @return true if the room is the entrance
     */
    public boolean isEntrance() {return myEntrance;}

    /**
     * @return true if the room is the exit
     */
    public boolean isExit() {return myExit;}

    /**
     * @return true if the room has a pillar
     */
    public boolean hasPillar() {return hasPillar;}

    /**
     * @return true if the room has a pit
     */
    public boolean hasPit() {return hasPit;}

    /**
     * @return true if the room has items
     */
    public boolean hasItems() {
        return !myItems.isEmpty();
    }

    /**
     * Removes the list of items in the room and returns them
     * @return A list of all the items in the room
     */
    public List<Item> pickUpItems() {
        List<Item> items = new ArrayList<>(myItems);
        myItems.clear();
        return items;
    }

    /**
     * Removes the pillar in the room and returns it
     * @return the pillar in the room
     */
    public char pickUpPillar() {
        if (!hasPillar) return ' ';
        hasPillar = false;
        char temp = pillarType;
        pillarType = ' '; //So a pillar is not shown in the room anymore
        return temp;
    }

    /**
     * Gives the amount of damage this rooms pit will do
     *
     * @return the amount of damage the pit will do
     */
    public int triggerPitDamage() {
        if (!hasPit) return 0;

        hasPit = false;
        return myRandom.nextInt(20) +1;
    }

    /**
     * Adds an item to the room
     */
    public void addItem(Item theItem) {
        myItems.add(theItem);
    }

    /**
     * Sets if this room has a pit or not
     */
    public void setPit(boolean value) {
        hasPit = value;
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
        StringBuilder sb = new StringBuilder();

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
        if (hasPit) count++;
        if (hasPillar) count++;
        count += myItems.size();

        if (count > 1) return 'M';

        if (hasPillar) return pillarType;
        if (hasPit) return 'X';
        if(myItems.size() == 1) return myItems.getFirst().getSymbol();

        return ' ';
    }

    private void clearRoom() {
        myItems.clear();
        hasPit = false;
    }

    private void generateRandomItems() {
        if (myRandom.nextInt(100) < myChance) myItems.add(new HealingPotion());
        if (myRandom.nextInt(100) < myChance) myItems.add(new VisionPotion());
    }

}
