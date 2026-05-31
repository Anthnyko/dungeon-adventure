package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

public interface RoomEvent {
    void trigger(Hero theHero);
}
