package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

/**
 * Represents a healing fountain event in a dungeon room.
 * When triggered, the hero is healed upon drinking from the fountain.
 *
 * @author Ibrahim
 * @version 1.0
 */
public class FountainEvent implements RoomEvent {

    /**
     * Triggers the fountain, healing the hero for 40 hit points.
     *
     * @param theHero the hero to apply the healing on
     * @return "FOUNTAIN" to identify this event type to the controller
     */
    @Override
<<<<<<< HEAD
    public String trigger(final Hero theHero) {
=======
    public void trigger(Hero theHero) {
>>>>>>> anthony/character-system
        theHero.applyHeal(40);
    }
}
