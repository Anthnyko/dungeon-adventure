package dungeon.model;

import dungeon.model.characters.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Responsible for creating Monster instances using data retrieved from the
 * SQLite monster database. This ensures monster stats are data driven and can
 * be adjusted without code changes.
 *
 *
 * @author Ibrahim Mohamud
 * @version 1.0
 */

public class MonsterGenerator {

    /** The connection to the SQLite monster database */
    private final Connection myConn;

    /**
     * Constructs a new MonsterGenerator and establishes a connection
     * to the SQLite monster database.
     *
     * @throws RuntimeException if the database connection cannot be established
     */
    public MonsterGenerator() {
        try {
            myConn = DriverManager.getConnection("jdbc:sqlite:MonsterDatabase.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and returns a new Monster of the given type using stats
     * retrieved from the monster database.
     *
     * @param type the name of the monster to create
     * @return a new Monster instance with stats loaded from the database
     * @throws IllegalArgumentException if the given type does not match any known monster
     * @throws RuntimeException if the database query fails
     */
    public Monster createMonster(String type) {
        try {
            ResultSet rs = myConn.createStatement().executeQuery("SELECT * FROM Monsters WHERE Name='" + type + "'");
            return switch (type) {
                case "Ogre" -> new Ogre(rs);
                case "Gremlin" -> new Gremlin(rs);
                case "Skeleton" -> new Skeleton(rs);
                case "Pillar Guardian" -> new PillarGuardian(rs);
                default -> throw new IllegalArgumentException("Unknown monster type");
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
