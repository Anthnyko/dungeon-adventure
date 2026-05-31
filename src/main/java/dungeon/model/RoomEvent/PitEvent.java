package dungeon.model.RoomEvent;

import dungeon.model.Dungeon;
import dungeon.model.characters.Hero;

import java.util.Random;

/**
 * Represents a pit trap event in a dungeon room.
 * When triggered, the hero falls into the pit and takes damage.
 *
 * @author Ibrahim
 * @version 1.0
 */
public class PitEvent implements RoomEvent{

    /**
     * Triggers the pit trap, dealing 1-20 damage to the hero.
     *
     * @param theHero the hero to apply the pit damage on
     * @return "PIT" to identify this event type to the controller
     */
    @Override
    public String trigger(final Hero theHero) {
        int damage = new Random().nextInt(20) + 1;
        theHero.takeDamage(damage);
        return "PIT";
    }
}