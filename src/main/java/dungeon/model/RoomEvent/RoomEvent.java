package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

/**
 * Represents an event that can occur in a dungeon room.
 * <p>
 * Implementing classes define specific room events triggered
 * by the controller and applies its effects to the hero.
 */
public interface RoomEvent {
    void trigger(Hero theHero);
}
