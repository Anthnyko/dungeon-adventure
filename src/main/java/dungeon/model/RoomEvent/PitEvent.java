package dungeon.model.RoomEvent;

import dungeon.model.characters.Hero;

import java.util.Random;

public class PitEvent implements RoomEvent{

    @Override
    public void trigger(Hero theHero) {
        int damage = new Random().nextInt(20) + 1;
        theHero.takeDamage(damage);
    }
}
