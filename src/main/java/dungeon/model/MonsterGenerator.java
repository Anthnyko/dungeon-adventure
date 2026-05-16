package dungeon.model;

import dungeon.model.characters.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MonsterGenerator {
    private Connection myConn;

    public MonsterGenerator() {
        try {
            myConn = DriverManager.getConnection("jdbc:sqlite:MonsterDatabase.db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Monster createMonster(String type) {
        try {
            ResultSet rs = myConn.createStatement().executeQuery("SELECT * FROM Monsters WHERE Name='" + type + "'");
            switch (type) {
                case "Ogre": return new Ogre(rs);
                case "Gremlin": return new Gremlin(rs);
                case "Skeleton": return  new Skeleton(rs);
                case "Pillar Guardian": return new PillarGuardian(rs);
                default: throw new IllegalArgumentException("Unknown monster type");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
