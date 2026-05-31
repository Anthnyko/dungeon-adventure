package dungeon.model.RoomEvent;

import dungeon.model.Dungeon;
import dungeon.model.characters.Hero;

public class AlarmEvent implements RoomEvent {

    /**
     * Triggers an alarm event that makes the closest monster chase
     * the hero.
     *
     * @param theHero the hero to apply the event effect on
     * @param theDungeon the dungeon instance
     * @return "ALARM" to identify this event type to the controller
     */
    @Override
    public String trigger(final Hero theHero, final Dungeon theDungeon) {
        theDungeon.triggerAlarm();
        return "ALARM";
    }
}
