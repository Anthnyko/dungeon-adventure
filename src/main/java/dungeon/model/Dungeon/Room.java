package dungeon.model.Dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.RoomEvent.RoomEvent;
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

    /** List of all events in this room */
    private final List<RoomEvent> myEvents = new ArrayList<>();

    /** Tracks if this room has a pillar */
    private boolean myPillar;

    /** The Pillar of OO this room contains */
    private char myPillarType; //put the letter of the pillar 'A', 'E', 'I', 'P'

    /** The list of items this room contains */
    private final List<Item> myItems;

    /** The chance that each item has to be placed */
    private static final int SPAWN_CHANCE = 10;

    /** Random object for random generation */
    private final Random myRandom;

    /**
     * Constructs a new room with randomly generated items.
     * Each item has an independent chance of being placed.
     */
    public Room() {
        myRandom = new Random();
        myItems = new ArrayList<>();

        generateRandomItems();

        myEntrance = false;
        myExit = false;
        myPillar = false;
    }

    /**
     * Constructs a new room with no items.
     */
    public Room(final boolean isEmpty) {
        myRandom = new Random();
        myItems = new ArrayList<>();
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
     * Add a room event to this room.
     * @param theEvent the event to add (e.g. PitEvent, FountainEvent)
     */
    public void addEvent(final RoomEvent theEvent) {
        myEvents.add(theEvent);
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
    public boolean hasPillar() {
        return myPillar;
    }

    /**
     * @return true if the room has a pit
     */
    public boolean hasPit() {
        for (final RoomEvent event : myEvents) {
            if (event instanceof PitEvent) return true;
        }
        return false;
    }

    /**
     * @return true if the room has a pit
     */
    public boolean hasPoison() {
        for (final RoomEvent event : myEvents) {
            if (event instanceof PoisonEvent) return true;
        }
        return false;
    }

    /**
     * @return true if the room has an alarm
     */
    public boolean hasAlarm() {
        for (final RoomEvent event : myEvents) {
            if (event instanceof AlarmEvent) return true;
        }
        return false;
    }

    /**
     * @return true if the room has a fountain
     */
    public boolean hasFountain() {
        for (final RoomEvent event : myEvents) {
            if (event instanceof FountainEvent) return true;
        }
        return false;
    }

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
     * @return a list of all the room event objects in this room
     */
    public List<RoomEvent> getEvents() {return myEvents;}

    /**
     * @return the monster in this room, or null if non exists
     */
    public Monster getMonster() {
        return myMonster;
    }

    /**
     * Removes the list of items in the room and returns them
     */
    public void pickUpItems(final Hero theHero) {
        for (Item myItem : myItems) {
            if (myItem instanceof HealingPotion) {
                theHero.gainHealingPotion();
            } else if (myItem instanceof VisionPotion) {
                theHero.gainVisionPotion();
            }
        }  
        myItems.clear();
    }

    public List<Item> getMyItems() {
        return myItems;
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
     * Returns the type of pillar in this room.
     * @return the pillar character ('A', 'E', 'I', 'P') or '.' if no pillar
     */
    public char getPillarType() {
        if (myPillar) return myPillarType;
        return '.';
    }

    /**
     * Triggers a specific room event by type and applies its effect to the hero.
     * If no matching event is found, nothing happens.
     * <p>
     *  * Valid event types:
     *  * <ul>
     *  *     <li>"PIT" ? triggers the pit trap, dealing damage to the hero</li>
     *  *     <li>"FOUNTAIN" ? triggers the fountain, healing the hero</li>
     *        <li>"POISON" ? triggers the fountain, healing the hero</li>
     *        <li>"ALARM" ? triggers the fountain, healing the hero</li>
     *  * </ul>
     *
     * The event is removed from the room after it has been triggered.
     *
     * @param theEventType the type of event to trigger ("PIT", "FOUNTAIN")
     * @param theHero the hero to apply the event on
     * @return a string of the event type triggered ("PIT", "FOUNTAIN")
     */
    public String triggerEvent(final String theEventType, final Hero theHero, final Dungeon theDungeon) {
        for (final RoomEvent event : new ArrayList<>(myEvents)) {
            if (event instanceof PitEvent && theEventType.equals("PIT")) {
                myEvents.remove(event);
                return event.trigger(theHero, theDungeon);
            } else if (event instanceof FountainEvent && theEventType.equals("FOUNTAIN")) {
                myEvents.remove(event);
                return event.trigger(theHero, theDungeon);
            } else if (event instanceof PoisonEvent && theEventType.equals("POISON")) {
                myEvents.remove(event);
                return event.trigger(theHero, theDungeon);
            } else if (event instanceof AlarmEvent && theEventType.equals("ALARM")) {
                myEvents.remove(event);
                return event.trigger(theHero, theDungeon);
            }
        }
        return null;
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
     * Returns a string representation of the room.
     * Walls are represented with '*' and doors with '-' or '|'.
     * The center character represents the content of the room.
     *
     * @return the string representation
     */
    @Override
    public String toString() {

        // Top row

        return (myNorthDoor ? "*-*" : "***") + "\n" +

                // Middle row
                (myWestDoor ? "|" : "*") +
                getRoomSymbol() +
                (myEastDoor ? "|" : "*") + "\n" +

                // Bottom row
                (mySouthDoor ? "*-*" : "***");
    }

    // Private helpers
    private char getRoomSymbol() {
        if (myEntrance) return 'i';
        if (myExit) return 'O';
        if (myPillar) return myPillarType;

        int count = 0;
        if (hasPit()) count++;
        if (hasAlarm()) count++;
        if (hasMonster()) count++;
        if (hasFountain()) count++;
        if (hasPoison()) count++;
        count += myItems.size();

        if (count > 1) return 'M';

        if (hasFountain()) return '+';
        if (hasPit()) return 'X';
        if (hasPoison()) return '~';
        if (hasAlarm()) return '?';
        if (hasMonster()) return '!';
        if(myItems.size() == 1) return myItems.getFirst().getSymbol();

        return ' ';
    }

    private void clearRoom() {
        myItems.clear();
        myEvents.clear();
    }

    private void generateRandomItems() {
        if (myRandom.nextInt(100) < SPAWN_CHANCE) myItems.add(new HealingPotion());
        if (myRandom.nextInt(100) < SPAWN_CHANCE) myItems.add(new VisionPotion());
    }
}
