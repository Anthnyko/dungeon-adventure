package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

public class FountainEvent implements RoomEvent {

    @Override
    public String trigger(Hero theHero) {
        theHero.applyHeal(40);
        return "FOUNTAIN";
    }
}
