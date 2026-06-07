package dungeon.model.characters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Gremlin class represents a fast but fragile Monster type in the dungeon.
 * Gremlins rely on speed and unpredictability, attacking quickly but with
 * relatively low durability. They also possess a moderate chance to heal
 * themselves during combat.
 *
 * <p>This class defines only the Gremlin's constructor for Iteration 1.
 * All combat behavior, healing logic, and bleed effects are implemented
 * in later iterations.</p>
 *
 * <p>Gremlin Stats:</p>
 * <ul>
 *     <li>HP: 55</li>
 *     <li>Damage: 10–20</li>
 *     <li>Attack Speed: 5</li>
 *     <li>Hit Chance: 0.8</li>
 *     <li>Heal Chance: 0.25</li>
 *     <li>Heal Amount: 10–20</li>
 * </ul>
 *
 * This class extends {@link Monster} and provides the appropriate
 * stat values through its no‑argument constructor.
 *
 * @version 1.0
 */
public class Gremlin extends Monster {

    /**
     * Constructs a new Gremlin by loading its stats from the database.
     * This is the primary constructor used during normal dungeon generation.
     *
     * @param rs the ResultSet from the Monsters table containing this Gremlin's stats
     * @throws SQLException if any of the expected columns are missing or cannot be read from the ResultSet
     */
    public Gremlin(final ResultSet rs) throws SQLException {
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
     * Constructs a new Gremlin with hardcoded standard stats.
     * All values are based on the standard Dungeon Adventure monster stats.
     * This constructor is intended for testing purposes only.
     */
    public Gremlin() {
        super("Gremlin",
                55,   // HP
                55,   // max HP
                10,   // min damage
                20,   // max damage
                5,    // attack speed
                0.8,  // hit chance
                0.25,  // heal chance
                10,   // min heal
                20);  // max heal
    }
}
