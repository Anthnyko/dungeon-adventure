package dungeon.model.Saving;

import java.util.List;

import dungeon.model.Dungeon.Dungeon;
import dungeon.model.Dungeon.Room;
import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.RoomEvent.RoomEvent;
import dungeon.model.characters.Hero;
import dungeon.model.items.HealingPotion;
import dungeon.model.items.Item;
import dungeon.model.items.VisionPotion;

/**
 * Captures the current game state for saving by serializing the dungeon layout,
 * room data, and hero information into a {@link GameState} object.
 * Room data is serialized using the format "N|S|E|W|Pillar|Monster|Items|Revealed|Events"
 * with rooms separated by '~' and rows separated by ';'.
 * Items are encoded as single characters (H=HealingPotion, V=VisionPotion)
 * and events are encoded as short codes (P=Pit, F=Fountain, Po=Poison, Al=Alarm).
 *
 * @author Jackson Steger
 * @version 1.0
 * @see GameState
 * @see RestoreSave
 */
public class InstanceCapture {
    
   /**
     * Captures the complete current game state including dungeon layout,
     * room data, and hero information for saving.
     * 
     * @return a GameState object containing all necessary game information
     */
    public static GameState captureGameState(Hero theHero, Dungeon theDungeon) {
        final GameState state = new GameState();
        
        // Dungeon info
        state.myDungeonHeight = theDungeon.getDungeonHeight();
        state.myDungeonWidth = theDungeon.getDungeonWidth();
        state.myDungeonName = theDungeon.getMyName();
        state.myHeroRow = theDungeon.getHeroRow();
        state.myHeroCol = theDungeon.getHeroCol();
        state.myEntranceRow = theDungeon.getMyEntranceRow();
        state.myEntranceCol = theDungeon.getMyEntranceCol();
        state.myExitRow = theDungeon.getMyExitRow();
        state.myExitCol = theDungeon.getMyExitCol();
        
        // room data
        state.myRoomData = captureRoomData(theDungeon);
        
        // Hero info
        state.myHeroName = theHero.getCharName();
        state.myHeroClass = theHero.getClassName();   
        state.myHeroHP = theHero.getHP();
        state.myHealingPotions = theHero.getHealingPotion();
        state.myVisionPotions = theHero.getVisionPotion();
        state.myPillarsFound = theHero.getMyPillars();
        state.mySkillTimer = theHero.getSkillTimer();
        state.myCDTimer = theHero.getCDTimer();
        
        state.mySaveTime = System.currentTimeMillis();
        
        return state;
    }

    /**
     * Captures the state of all rooms in the dungeon for saving.
     * Returns a 2D array of serialized room data strings.
     * 
     * Format per room: "N|S|E|W|Pillar|Monster|Items|Revealed|Events"
     */
    private static String[][] captureRoomData(Dungeon theDungeon) {
        final int width = theDungeon.getDungeonWidth();
        final int height = theDungeon.getDungeonHeight();
        final String[][] roomData = new String[height][width];
        
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                final Room room = theDungeon.getRoom(row, col);
                
                // Build serialized room string
                final StringBuilder roomInfo = new StringBuilder();
                roomInfo.append(room.hasNorthDoor() ? "1" : "0").append("|");
                roomInfo.append(room.hasSouthDoor() ? "1" : "0").append("|");
                roomInfo.append(room.hasEastDoor() ? "1" : "0").append("|");
                roomInfo.append(room.hasWestDoor() ? "1" : "0").append("|");
                roomInfo.append(room.isEntrance() ? "i" : (room.isExit() ? "O" : (room.hasPillar() ? room.getPillarType() : "."))).append("|");
                roomInfo.append(room.hasMonster() ? room.getMonster().getCharName() : "-").append("|");
                roomInfo.append(serializeItems(room.getMyItems())).append("|");
                roomInfo.append(room.isRevealed() ? "1" : "0").append("|");
                roomInfo.append(serializeEvents(room.getEvents()));
                
                roomData[row][col] = roomInfo.toString();
            }
        }
        
        return roomData;
    }

    /**
     * Serializes a list of items into a comma-separated string for database storage.
     * Each item type is represented by a single character (H=HealingPotion, V=VisionPotion).
     * 
     * @param items the list of items to serialize
     * @return a serialized string representation of the items, or "none" if empty
     */
    private static String serializeItems(final List<Item> items) {
        if (items == null || items.isEmpty()) {
            return "none";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            final Item item = items.get(i);
            if (item instanceof HealingPotion) {
                sb.append("H");
            } else if (item instanceof VisionPotion) {
                sb.append("V");
            }
            if (i < items.size() - 1) sb.append(",");
        }
        return sb.toString();
    }

    /**
     * Serializes a list of room events into a comma-separated string for database storage.
     * Each event type is represented by a code (P=Pit, F=Fountain, Po=Poison, A=Alarm).
     * 
     * @param events the list of room events to serialize
     * @return a serialized string representation of the events, or "none" if empty
     */
    private static String serializeEvents(final List<RoomEvent> events) {
        if (events == null || events.isEmpty()) {
            return "none";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < events.size(); i++) {
            final RoomEvent event = events.get(i);
            if (event instanceof PitEvent) {
                sb.append("P");
            } else if (event instanceof FountainEvent) {
                sb.append("F");
            } else if (event instanceof PoisonEvent) {
                sb.append("Po");
            } else if (event instanceof AlarmEvent) {
                sb.append("Al");
            }
            if (i < events.size() - 1) sb.append(",");
        }
        return sb.toString();
    }
}
