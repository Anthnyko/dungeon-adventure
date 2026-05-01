package dungeon.characters;

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
     * Constructs a new Skeleton with predefined combat and healing attributes.
     * All values are based on the standard Dungeon Adventure monster stats.
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
