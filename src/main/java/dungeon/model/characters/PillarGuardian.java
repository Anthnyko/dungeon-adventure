package dungeon.characters;

/**
 * Represents the special mini‑boss monster that guards each Pillar
 * in the dungeon. The Pillar Guardian is significantly stronger than
 * standard monsters and must be defeated before the hero can collect
 * a Pillar of OO.
 *
 * This monster does not heal, has high durability, and deals heavy
 * damage, making it a mid‑game challenge intended to test the player's
 * resource management and combat strategy.
 *
 * Pillar Guardians appear only in pillar rooms and are not encountered
 * elsewhere in the dungeon.
 *
 * @author Anthony
 * @version 1.0
 */
public class PillarGuardian extends Monster {

    /**
     * Constructs a new Pillar Guardian with predefined combat stats.
     * These stats are intentionally higher than those of regular
     * monsters to create a mini‑boss encounter.
     *
     * Stats:
     * - HP: 250
     * - Max HP: 250
     * - Damage: 35–60
     * - Attack Speed: 2
     * - Hit Chance: 0.75
     * - Heal Chance: 0 (cannot heal)
     *
     */
    public PillarGuardian() {
        super("Pillar Guardian",
                250,  // HP
                250,  // max HP
                35,   // min damage
                60,   // max damage
                2,    // attack speed
                0.75, // hit chance
                0,    // heal chance
                0,    // min heal
                0);   // max heal
    }
}
