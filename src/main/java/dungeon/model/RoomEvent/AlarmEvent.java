package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

public class AlarmEvent implements RoomEvent {
    @Override
    public String trigger(final Hero theHero) {
        return "";
    }
}
