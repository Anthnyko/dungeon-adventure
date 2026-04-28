package dungeon.characters;

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
     * Constructs a new Ogre with predefined combat and healing attributes.
     * All values are based on the standard Dungeon Adventure monster stats.
     */
    public Ogre() {
        super("Ogre",
                200,  // HP
                30,   // min damage
                60,   // max damage
                2,    // attack speed
                0.6,  // hit chance
                0.1,  // heal chance
                30,   // min heal
                60);  // max heal
    }
}
