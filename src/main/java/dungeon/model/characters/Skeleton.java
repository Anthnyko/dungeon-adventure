package dungeon.model.characters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Skeleton class represents a balanced Monster type in the dungeon.
 * Skeletons are moderately durable and deal consistent physical damage.
 * They also possess a small chance to heal themselves during combat.
 *
 * <p>This class defines only the Skeleton's constructor for Iteration 1.
 * All combat behavior, healing logic, and bleed effects are implemented
 * in later iterations.</p>
 *
 * <p>Skeleton Stats:</p>
 * <ul>
 *     <li>HP: 100</li>
 *     <li>Damage: 30–50</li>
 *     <li>Attack Speed: 3</li>
 *     <li>Hit Chance: 0.8</li>
 *     <li>Heal Chance: 0.3</li>
 *     <li>Heal Amount: 30–50</li>
 * </ul>
 *
 * This class extends {@link Monster} and provides the appropriate
 * stat values through its no‑argument constructor.
 *
 * @version 1.0
 */
public class Skeleton extends Monster {

    /**
     * Constructs a new Skeleton by loading its stats from the database.
     * This is the primary constructor used during normal dungeon generation.
     *
     * @param rs the ResultSet from the Monsters table containing this Skeleton's stats
     * @throws SQLException if any of the expected columns are missing or cannot be read from the ResultSet
     */
    public Skeleton(ResultSet rs) throws SQLException {
        super(rs.getString("Name"),
                rs.getInt("HP"),
                rs.getInt("MaxHP"),
                rs.getInt("MinDamage"),
                rs.getInt("MaxDamage"),
                rs.getInt("AttackSpeed"),
                rs.getInt("HitChance"),
                rs.getInt("HealChance"),
                rs.getInt("MinHeal"),
                rs.getInt("MaxHeal")
        );
    }

    /**
     * Constructs a new Skeleton with hardcoded standard stats.
     * All values are based on the standard Dungeon Adventure monster stats.
     * This constructor is intended for testing purposes only.
     */
    public Skeleton() {
        super("Skeleton",
                100, // HP
                100, // max HP
                30,  // min damage
                50,  // max damage
                3,   // attack speed
                0.8, // hit chance
                0.3, // heal chance
                30,  // min heal
                50); // max heal
    }
}
