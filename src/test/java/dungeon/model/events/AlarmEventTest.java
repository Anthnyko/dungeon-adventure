package dungeon.model.events;

import dungeon.model.Dungeon;
import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.characters.Warrior;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlarmEventTest {

    @Test
    public void testAlarmReturnsCorrectString() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        AlarmEvent alarm = new AlarmEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        assertEquals("ALARM", alarm.trigger(warrior, dungeon));
    }

    @Test
    public void testAlarmTriggersMonsterAlert() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        AlarmEvent alarm = new AlarmEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        alarm.trigger(warrior, dungeon);
        // after alarm triggers, alerted monster position should be set
        assertTrue(dungeon.getAlertedMonsterRow() != -1 ||
                    dungeon.getAlertedMonsterCol() != -1);
    }
}
