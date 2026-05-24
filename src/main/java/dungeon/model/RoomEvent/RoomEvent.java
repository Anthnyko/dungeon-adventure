package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

public interface RoomEvent {
    String trigger(Hero theHero);
}
