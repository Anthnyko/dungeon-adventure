package dungeon.characters;

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
 *     <li>HP: 70</li>
 *     <li>Damage: 15–30</li>
 *     <li>Attack Speed: 5</li>
 *     <li>Hit Chance: 0.8</li>
 *     <li>Heal Chance: 0.4</li>
 *     <li>Heal Amount: 20–40</li>
 * </ul>
 *
 * This class extends {@link Monster} and provides the appropriate
 * stat values through its no‑argument constructor.
 *
 * @version 1.0
 */
public class Gremlin extends Monster {

    /**
     * Constructs a new Gremlin with predefined combat and healing attributes.
     * All values are based on the standard Dungeon Adventure monster stats.
     */
    public Gremlin() {
        super("Gremlin",
                70,   // HP
                70,   // max HP
                15,   // min damage
                30,   // max damage
                5,    // attack speed
                0.8,  // hit chance
                0.4,  // heal chance
                20,   // min heal
                40);  // max heal
    }
}
