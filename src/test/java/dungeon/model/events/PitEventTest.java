package dungeon.model.events;

import dungeon.model.Dungeon;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.characters.Warrior;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PitEventTest {

    @Test
    public void testPitDealsDamage() {
        Dungeon dungeon = new  Dungeon(5, 5, "The Easy Dungeon", false);
        PitEvent pit = new PitEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        int hpBefore = warrior.getHP();
        pit.trigger(warrior, dungeon);
        assertTrue(warrior.getHP() < hpBefore);
    }

    @Test
    public void testPitReturnsCorrectString() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        PitEvent pit = new PitEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        assertEquals("PIT", pit.trigger(warrior, dungeon));
    }

    @Test
    public void testPitDamageIsInRange() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        for (int i = 0; i < 20; i++) {
            int hpBefore = warrior.getHP();
            new PitEvent().trigger(warrior, dungeon);
            int damage = hpBefore - warrior.getHP();
            assertTrue(damage >= 1 && damage <= 20);
            // reset hp for next iteration
            warrior = new Warrior("test", 0, 0);
        }
    }

}
