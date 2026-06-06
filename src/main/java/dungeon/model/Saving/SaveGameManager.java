package dungeon.model.Saving;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public SaveGameManager(String dbUrl) {
        this.myDbUrl = dbUrl;
        initializeDatabase();
    }

    /**
     * Initializes the SQLite database and creates the saves table if it doesn't exist.
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS saves ("   + System.lineSeparator() 
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
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Saves the current game state to the SQLite database.
     * 
     * @param state the GameState to save
     * @param saveName the name for this save file
     * @return true if save was successful, false otherwise
     */
    public boolean saveGame(GameState state, String saveName) {
        try (Connection conn = DriverManager.getConnection(myDbUrl)) {
            // Check if save with this name already exists
            String checkSql = "SELECT id FROM saves WHERE save_name = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, saveName);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    // Update existing save
                    return updateSave(conn, state, saveName);
                } else {
                    // Insert new save
                    return insertSave(conn, state, saveName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserts a new save record into the database.
     */
    private boolean insertSave(Connection conn, GameState state, String saveName) throws SQLException {
        String sql = "INSERT INTO saves (save_name, save_time, hero_name, hero_class, hero_hp, "
                + "healing_potions, vision_potions, pillars_found, skill_timer, cd_timer, dungeon_width, "
                + "dungeon_height, dungeon_name, hero_row, hero_col, entrance_row, entrance_col, exit_row, exit_col, "
                + "room_data)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, saveName);
            pstmt.setString(2, LocalDateTime.now().format(DATE_FORMATTER));
            pstmt.setString(3, state.myHeroName);
            pstmt.setString(4, state.myHeroClass);
            pstmt.setInt(5, state.myHeroHP);
            pstmt.setInt(6, state.myHealingPotions);
            pstmt.setInt(7, state.myVisionPotions);
            pstmt.setString(8, serializePillars(state.myPillarsFound));
            pstmt.setInt(9, state.mySkillTimer);
            pstmt.setInt(10, state.myCDTimer);
            pstmt.setInt(11, state.myDungeonWidth);
            pstmt.setInt(12, state.myDungeonHeight);
            pstmt.setString(13, state.myDungeonName);
            pstmt.setInt(14, state.myHeroRow);
            pstmt.setInt(15, state.myHeroCol);
            pstmt.setInt(16, state.myEntranceRow);
            pstmt.setInt(17, state.myEntranceCol);
            pstmt.setInt(18, state.myExitRow);
            pstmt.setInt(19, state.myExitCol);
            pstmt.setString(20, serializeRoomData(state.myRoomData));
            
            pstmt.executeUpdate();
            System.out.println("Game saved successfully: " + saveName);
            return true;
        }
    }

    /**
     * Updates an existing save record in the database.
     */
    private boolean updateSave(Connection conn, GameState state, String saveName) throws SQLException {
        String sql = "UPDATE saves SET save_time = ?, hero_hp = ?, healing_potions = ?, "
                + "vision_potions = ?, pillars_found = ?, skill_timer = ?, cd_timer = ?, hero_row = ?, "
                + "hero_col = ?, room_data = ? WHERE save_name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, LocalDateTime.now().format(DATE_FORMATTER));
            pstmt.setInt(2, state.myHeroHP);
            pstmt.setInt(3, state.myHealingPotions);
            pstmt.setInt(4, state.myVisionPotions);
            pstmt.setString(5, serializePillars(state.myPillarsFound));
            pstmt.setInt(6, state.mySkillTimer);
            pstmt.setInt(7, state.myCDTimer);
            pstmt.setInt(8, state.myHeroRow);
            pstmt.setInt(9, state.myHeroCol);
            pstmt.setString(10, serializeRoomData(state.myRoomData));
            pstmt.setString(11, saveName);
            
            pstmt.executeUpdate();
            System.out.println("Game updated successfully: " + saveName);
            return true;
        }
    }

    /**
     * Loads a game state from the database by save name.
     * 
     * @param saveName the name of the save to load
     * @return the GameState object, or null if not found
     */
    public GameState loadGame(String saveName) {
        String sql = "SELECT * FROM saves WHERE save_name = ?";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, saveName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                GameState state = new GameState();
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
                
                System.out.println("Game loaded successfully: " + saveName);
                return state;
            } else {
                System.out.println("Save file not found: " + saveName);
                return null;
            }
        } catch (SQLException e) {
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
        List<String> saves = new ArrayList<>();
        String sql = "SELECT save_name, save_time, hero_name, hero_class FROM saves ORDER BY save_time DESC";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String info = String.format("%s | %s (%s - %s)", 
                        rs.getString("save_name"),
                        rs.getString("save_time"),
                        rs.getString("hero_name"),
                        rs.getString("hero_class"));
                saves.add(info);
            }
        } catch (SQLException e) {
            System.err.println("Error listing saves: " + e.getMessage());
        }
        
        return saves;
    }

    /**
     * Deletes a save from the database.
     * 
     * @param saveName the name of the save to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteSave(String saveName) {
        String sql = "DELETE FROM saves WHERE save_name = ?";
        
        try (Connection conn = DriverManager.getConnection(myDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, saveName);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("Save deleted successfully: " + saveName);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting save: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Converts a list of pillar characters to a comma-separated string for database storage.
     * Each pillar is represented by a single character (A, E, I, P for the four pillars of OO).
     * 
     * @param pillars the list of pillar characters to serialize
     * @return a comma-separated string of pillars, or an empty string if the list is empty or null
     */
    private String serializePillars(List<Character> pillars) {
        if (pillars == null || pillars.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pillars.size(); i++) {
            sb.append(pillars.get(i));
            if (i < pillars.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Converts a comma-separated string of pillars back to a list of pillar characters.
     * Reverses the serialization performed by serializePillars().
     * 
     * @param pillarsStr the comma-separated string of pillar characters
     * @return a list of pillar characters, or an empty list if the string is empty or null
     */
    private List<Character> deserializePillars(String pillarsStr) {
        List<Character> pillars = new ArrayList<>();
        if (pillarsStr == null || pillarsStr.isEmpty()) {
            return pillars;
        }
        for (String s : pillarsStr.split(",")) {
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
     * @param roomData the 2D array of room data to serialize
     * @return a serialized string representation of the room data, or an empty string if null
     */
    private String serializeRoomData(String[][] roomData) {
        if (roomData == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < roomData.length; i++) {
            for (int j = 0; j < roomData[i].length; j++) {
                sb.append(roomData[i][j] != null ? roomData[i][j] : "");
                if (j < roomData[i].length - 1) sb.append("~");
            }
            if (i < roomData.length - 1) sb.append(";");
        }
        return sb.toString();
    }

    /**
     * Deserializes a database-stored string back into a 2D array of room data.
     * Reverses the serialization performed by serializeRoomData().
     * Semicolons separate rows and tildes separate columns.
     * 
     * @param data the serialized room data string from the database
     * @return a 2D array of room data strings, or an empty array if the string is null or empty
     */
    private String[][] deserializeRoomData(String data) {
        if (data == null || data.isEmpty()) {
            return new String[0][0];
        }
        String[] rows = data.split(";", -1);
        rows = Arrays.stream(rows).filter(r -> !r.isEmpty()).toArray(String[]::new);
        String[][] roomData = new String[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            roomData[i] = rows[i].split("~");
        }
        return roomData;
    }
}
