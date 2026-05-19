package dungeon.model.characters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Ogre class represents a specific type of Monster in the dungeon.
 * Ogres are slow but extremely durable enemies capable of dealing
 * heavy damage. They also possess a small chance to heal themselves
 * during combat.
 *
 * <p>This class defines only the Ogre's constructor for Iteration 1.
 * All combat behavior, healing logic, and bleed effects are implemented
 * in later iterations.</p>
 *
 * <p>Ogre Stats:</p>
 * <ul>
 *     <li>HP: 200</li>
 *     <li>Damage: 30–60</li>
 *     <li>Attack Speed: 2</li>
 *     <li>Hit Chance: 0.6</li>
 *     <li>Heal Chance: 0.1</li>
 *     <li>Heal Amount: 30–60</li>
 * </ul>
 *
 * This class extends {@link Monster} and provides the appropriate
 * stat values through its no‑argument constructor.
 *
 * @author Anthony
 * @version 1.0
 */
public class Ogre extends Monster {

    /**
     * Constructs a new Ogre by loading its stats from the database.
     * This is the primary constructor used during normal dungeon generation.
     *
     * @param rs the ResultSet from the Monsters table containing this Ogre's stats
     * @throws SQLException if any of the expected columns are missing or cannot be read from the ResultSet
     */
    public Ogre(ResultSet rs) throws SQLException {
        super(rs.getString("Name"),
                rs.getInt("HP"),
                rs.getInt("MaxHP"),
                rs.getInt("MinDamage"),
                rs.getInt("MaxDamage"),
                rs.getInt("AttackSpeed"),
                rs.getDouble("HitChance"),
                rs.getDouble("HealChance"),
                rs.getInt("MinHeal"),
                rs.getInt("MaxHeal")
        );
    }

    /**
     * Constructs a new Ogre with hardcoded standard stats.
     * All values are based on the standard Dungeon Adventure monster stats.
     * This constructor is intended for testing purposes only.
     */
    public Ogre() {
        super("Ogre",
                200,  // HP
                200,  // max HP
                30,   // min damage
                60,   // max damage
                2,    // attack speed
                0.6,  // hit chance
                0.1,  // heal chance
                30,   // min heal
                60);  // max heal
    }
}
