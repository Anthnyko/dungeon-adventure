package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

public class PoisonEvent implements RoomEvent {
    @Override
    public String trigger(final Hero theHero) {
        return "";
    }
}
