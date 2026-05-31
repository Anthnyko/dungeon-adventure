package dungeon.model.RoomEvent;

import dungeon.model.Dungeon;
import dungeon.model.characters.Hero;

/**
 * Represents an event that can occur in a dungeon room.
 * <p>
 * Implementing classes define specific room events triggered
 * by the controller and applies its effects to the hero.
 */
public interface RoomEvent {

    /**
     * Triggers the event and applies its effect to the hero.
     *
     * @param theHero the hero to apply the event effect on
     * @param theDungeon the dungeon instance
     * @return a String identifying the event type to the controller
     */
    String trigger(final Hero theHero, final Dungeon theDungeon);
}
