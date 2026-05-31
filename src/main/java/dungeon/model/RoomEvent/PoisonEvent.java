package dungeon.model.RoomEvent;

import dungeon.model.Dungeon;
import dungeon.model.characters.Hero;

public class PoisonEvent implements RoomEvent {

    /** The amount of damage dealt per turn */
    private static final int POISON_DAMAGE = 3;

    /** The number of turns the poison lasts */
    private static final int POISON_DURATION = 3;

    /**
     * Triggers the poison trap, applying a poison status effect to the hero
     *
     * @param theHero the hero to apply the event effect on
     * @return "POISON" to identify this event type to the controller
     */
    @Override
    public String trigger(final Hero theHero, final Dungeon theDungeon) {
        theHero.takeTickDamage(POISON_DAMAGE, POISON_DURATION);
        return "POISON";
    }
}
