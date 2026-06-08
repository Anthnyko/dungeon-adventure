package dungeon.model.Saving;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates the complete game state for saving and loading game progress.
 * This class serializes all necessary information about the dungeon layout,
 * hero status, inventory, and position to enable persistent game saves.
 * <p>
 * GameState objects are serialized to an SQLite database for later
 * retrieval, allowing players to resume their adventure from a saved checkpoint.
 * All fields are public to facilitate easy serialization and deserialization.
 *
 * @author Jackson Steger
 * @version 1.0
 */
public class GameState implements Serializable {
    /** Serial version UID for serialization compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new GameState with default values.
     * All fields must be initialized by the caller after construction.
     */
    public GameState() {
        // Default constructor for serialization
    }

    /** Width (columns) of the dungeon. */
    public int myDungeonWidth;
    
    /** Height (rows) of the dungeon. */
    public int myDungeonHeight;
    
    /** Name of the dungeon. */
    public String myDungeonName;
    
    /** Current row position of the hero in the dungeon. */
    public int myHeroRow;
    
    /** Current column position of the hero in the dungeon. */
    public int myHeroCol;
    
    /** Row position of the dungeon entrance. */
    public int myEntranceRow;
    
    /** Column position of the dungeon entrance. */
    public int myEntranceCol;
    
    /** Row position of the dungeon exit. */
    public int myExitRow;
    
    /** Column position of the dungeon exit. */
    public int myExitCol;
    
    /** 2D array containing serialized room data. */
    public String[][] myRoomData;

    /** Name of the hero. */
    public String myHeroName;
    
    /** Class of the hero (e.g., Warrior, Priest, Thief). */
    public String myHeroClass;
    
    /** Current hit points of the hero. */
    public int myHeroHP;
    
    /** Number of healing potions in hero's inventory. */
    public int myHealingPotions;
    
    /** Number of vision potions in hero's inventory. */
    public int myVisionPotions;
    
    /** List of pillar characters collected by the hero. */
    public List<Character> myPillarsFound;
    
    /** Cooldown timer for the hero's special skill. */
    public int mySkillTimer;
    
    /** Cooldown timer for the hero's ultimate ability. */
    public int myCDTimer;

    /** Timestamp of when the game was saved. */
    public long mySaveTime;

}