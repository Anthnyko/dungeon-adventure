package dungeon.model.Saving;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages saving and loading game state using SQLite database.
 * 
 * @author Jackson Steger
 * @version 1.0
 */
public class SaveGameManager {
    private final String myDbUrl;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a SaveGameManager and initializes the SQLite database
     * with the standard save database (should be used for the main game).
     * Creates the saves table if it does not already exist.
     */
    public SaveGameManager() {
        this.myDbUrl = "jdbc:sqlite:dungeon_saves.db";
        initializeDatabase();
    }

    /**
     * Constructs a SaveGameManager and initializes the SQLite database 
     * with a custom database url.
     * Creates the saves table if it does not already exist.
     */
    public SaveGameManager(final String dbUrl) {
        this.myDbUrl = dbUrl;
        initializeDatabase();
    }

    /**
     * Initializes the SQLite database and creates the saves table if it doesn't exist.
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             Statement stmt = conn.createStatement()) {
            
            final String sql = "CREATE TABLE IF NOT EXISTS saves ("   + System.lineSeparator() 
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"   + System.lineSeparator() 
                    + "save_name TEXT UNIQUE NOT NULL,"         + System.lineSeparator() 
                    + "save_time TEXT NOT NULL,"                + System.lineSeparator() 
                    + "hero_name TEXT NOT NULL,"                + System.lineSeparator() 
                    + "hero_class TEXT NOT NULL,"               + System.lineSeparator() 
                    + "hero_hp INTEGER NOT NULL,"               + System.lineSeparator() 
                    + "healing_potions INTEGER NOT NULL,"       + System.lineSeparator() 
                    + "vision_potions INTEGER NOT NULL,"        + System.lineSeparator() 
                    + "pillars_found TEXT,"                     + System.lineSeparator() 
                    + "skill_timer INTEGER NOT NULL,"           + System.lineSeparator() 
                    + "cd_timer INTEGER NOT NULL,"              + System.lineSeparator() 
                    + "dungeon_width INTEGER NOT NULL,"         + System.lineSeparator() 
                    + "dungeon_height INTEGER NOT NULL,"        + System.lineSeparator()  
                    + "dungeon_name TEXT NOT NULL,"             + System.lineSeparator() 
                    + "hero_row INTEGER NOT NULL,"              + System.lineSeparator() 
                    + "hero_col INTEGER NOT NULL,"              + System.lineSeparator() 
                    + "entrance_row INTEGER NOT NULL,"          + System.lineSeparator() 
                    + "entrance_col INTEGER NOT NULL,"          + System.lineSeparator() 
                    + "exit_row INTEGER NOT NULL,"              + System.lineSeparator() 
                    + "exit_col INTEGER NOT NULL,"              + System.lineSeparator() 
                    + "room_data LONGTEXT"                      + System.lineSeparator() 
                    + ");";
            
            stmt.execute(sql);
        } catch (final SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Saves the current game state to the SQLite database.
     * 
     * @param theState the GameState to save
     * @param theSaveName the name for this save file
     * @return true if save was successful, false otherwise
     */
    public boolean saveGame(final GameState theState, final String theSaveName) {
        try (Connection conn = DriverManager.getConnection(myDbUrl)) {
            // Check if save with this name already exists
            final String checkSql = "SELECT id FROM saves WHERE save_name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, theSaveName);
                final ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    // Update existing save
                    return updateSave(conn, theState, theSaveName);
                } else {
                    // Insert new save
                    return insertSave(conn, theState, theSaveName);
                }
            }
        } catch (final SQLException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts a new save record into the database.
     */
    private boolean insertSave(final Connection theConn, final GameState theState, final String theSaveName) throws SQLException {
        final String sql = "INSERT INTO saves (save_name, save_time, hero_name, hero_class, hero_hp, "
                + "healing_potions, vision_potions, pillars_found, skill_timer, cd_timer, dungeon_width, "
                + "dungeon_height, dungeon_name, hero_row, hero_col, entrance_row, entrance_col, exit_row, exit_col, "
                + "room_data)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = theConn.prepareStatement(sql)) {
            pstmt.setString(1, theSaveName);
            pstmt.setString(2, LocalDateTime.now().format(DATE_FORMATTER));
            pstmt.setString(3, theState.myHeroName);
            pstmt.setString(4, theState.myHeroClass);
            pstmt.setInt(5, theState.myHeroHP);
            pstmt.setInt(6, theState.myHealingPotions);
            pstmt.setInt(7, theState.myVisionPotions);
            pstmt.setString(8, serializePillars(theState.myPillarsFound));
            pstmt.setInt(9, theState.mySkillTimer);
            pstmt.setInt(10, theState.myCDTimer);
            pstmt.setInt(11, theState.myDungeonWidth);
            pstmt.setInt(12, theState.myDungeonHeight);
            pstmt.setString(13, theState.myDungeonName);
            pstmt.setInt(14, theState.myHeroRow);
            pstmt.setInt(15, theState.myHeroCol);
            pstmt.setInt(16, theState.myEntranceRow);
            pstmt.setInt(17, theState.myEntranceCol);
            pstmt.setInt(18, theState.myExitRow);
            pstmt.setInt(19, theState.myExitCol);
            pstmt.setString(20, serializeRoomData(theState.myRoomData));
            
            pstmt.executeUpdate();
            System.out.println("Game saved successfully: " + theSaveName);
            return true;
        }
    }

    /**
     * Updates an existing save record in the database.
     */
    private boolean updateSave(final Connection theConn, final GameState theState, final String theSaveName) throws SQLException {
        final String sql = "UPDATE saves SET save_time = ?, hero_hp = ?, healing_potions = ?, "
                + "vision_potions = ?, pillars_found = ?, skill_timer = ?, cd_timer = ?, hero_row = ?, "
                + "hero_col = ?, room_data = ? WHERE save_name = ?";
        
        try (PreparedStatement pstmt = theConn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDateTime.now().format(DATE_FORMATTER));
            pstmt.setInt(2, theState.myHeroHP);
            pstmt.setInt(3, theState.myHealingPotions);
            pstmt.setInt(4, theState.myVisionPotions);
            pstmt.setString(5, serializePillars(theState.myPillarsFound));
            pstmt.setInt(6, theState.mySkillTimer);
            pstmt.setInt(7, theState.myCDTimer);
            pstmt.setInt(8, theState.myHeroRow);
            pstmt.setInt(9, theState.myHeroCol);
            pstmt.setString(10, serializeRoomData(theState.myRoomData));
            pstmt.setString(11, theSaveName);
            
            pstmt.executeUpdate();
            System.out.println("Game updated successfully: " + theSaveName);
            return true;
        }
    }

    /**
     * Loads a game state from the database by save name.
     * 
     * @param theSaveName the name of the save to load
     * @return the GameState object, or null if not found
     */
    public GameState loadGame(final String theSaveName) {
        final String sql = "SELECT * FROM saves WHERE save_name = ?";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, theSaveName);
            final ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                final GameState state = new GameState();
                state.myHeroName = rs.getString("hero_name");
                state.myHeroClass = rs.getString("hero_class");
                state.myHeroHP = rs.getInt("hero_hp");
                state.myHealingPotions = rs.getInt("healing_potions");
                state.myVisionPotions = rs.getInt("vision_potions");
                state.myPillarsFound = deserializePillars(rs.getString("pillars_found"));
                state.mySkillTimer = rs.getInt("skill_timer");
                state.myCDTimer = rs.getInt("cd_timer");
                state.myDungeonWidth = rs.getInt("dungeon_width");
                state.myDungeonHeight = rs.getInt("dungeon_height");
                state.myDungeonName = rs.getString("dungeon_name");
                state.myHeroRow = rs.getInt("hero_row");
                state.myHeroCol = rs.getInt("hero_col");
                state.myRoomData = deserializeRoomData(rs.getString("room_data"));
                state.mySaveTime = rs.getLong("save_time");
                
                System.out.println("Game loaded successfully: " + theSaveName);
                return state;
            } else {
                System.out.println("Save file not found: " + theSaveName);
                return null;
            }
        } catch (final SQLException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a list of all available save names in the database.
     * 
     * @return list of save names with timestamps
     */
    public List<String> listSaves() {
        final List<String> saves = new ArrayList<>();
        final String sql = "SELECT save_name, save_time, hero_name, hero_class FROM saves ORDER BY save_time DESC";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                final String info = String.format("%s | %s (%s - %s)", 
                        rs.getString("save_name"),
                        rs.getString("save_time"),
                        rs.getString("hero_name"),
                        rs.getString("hero_class"));
                saves.add(info);
            }
        } catch (final SQLException e) {
            System.err.println("Error listing saves: " + e.getMessage());
        }
        
        return saves;
    }

    /**
     * Deletes a save from the database.
     * 
     * @param theSaveName the name of the save to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSave(final String theSaveName) {
        final String sql = "DELETE FROM saves WHERE save_name = ?";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, theSaveName);
            final int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Save deleted successfully: " + theSaveName);
                return true;
            }
        } catch (final SQLException e) {
            System.err.println("Error deleting save: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Converts a list of pillar characters to a comma-separated string for database storage.
     * Each pillar is represented by a single character (A, E, I, P for the four pillars of OO).
     * 
     * @param thePillars the list of pillar characters to serialize
     * @return a comma-separated string of pillars, or an empty string if the list is empty or null
     */
    private String serializePillars(final List<Character> thePillars) {
        if (thePillars == null || thePillars.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < thePillars.size(); i++) {
            sb.append(thePillars.get(i));
            if (i < thePillars.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Converts a comma-separated string of pillars back to a list of pillar characters.
     * Reverses the serialization performed by serializePillars().
     * 
     * @param thePillarsStr the comma-separated string of pillar characters
     * @return a list of pillar characters, or an empty list if the string is empty or null
     */
    private List<Character> deserializePillars(final String thePillarsStr) {
        final List<Character> pillars = new ArrayList<>();
        if (thePillarsStr == null || thePillarsStr.isEmpty()) {
            return pillars;
        }
        for (final String s : thePillarsStr.split(",")) {
            if (!s.isEmpty()) {
                pillars.add(s.charAt(0));
            }
        }
        return pillars;
    }

    /**
     * Serializes a 2D array of room data strings into a single database-storable string.
     * Uses semicolons (;) to separate rows and tildes (~) to separate columns.
     * 
     * @param theRoomData the 2D array of room data to serialize
     * @return a serialized string representation of the room data, or an empty string if null
     */
    private String serializeRoomData(final String[][] theRoomData) {
        if (theRoomData == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < theRoomData.length; i++) {
            for (int j = 0; j < theRoomData[i].length; j++) {
                sb.append(theRoomData[i][j] != null ? theRoomData[i][j] : "");
                if (j < theRoomData[i].length - 1) sb.append("~");
            }
            if (i < theRoomData.length - 1) sb.append(";");
        }
        return sb.toString();
    }

    /**
     * Deserializes a database-stored string back into a 2D array of room data.
     * Reverses the serialization performed by serializeRoomData().
     * Semicolons separate rows and tildes separate columns.
     * 
     * @param theData the serialized room data string from the database
     * @return a 2D array of room data strings, or an empty array if the string is null or empty
     */
    private String[][] deserializeRoomData(final String theData) {
        if (theData == null || theData.isEmpty()) {
            return new String[0][0];
        }
        String[] rows = theData.split(";", -1);
        rows = Arrays.stream(rows).filter(r -> !r.isEmpty()).toArray(String[]::new);
        final String[][] roomData = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            roomData[i] = rows[i].split("~");
        }
        return roomData;
    }
}
