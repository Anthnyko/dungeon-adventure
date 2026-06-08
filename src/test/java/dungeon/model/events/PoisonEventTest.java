package dungeon.model.events;

import dungeon.model.Dungeon;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.characters.Warrior;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PoisonEventTest {

    @Test
    public void testPoisonAppliesTickDamage() {
        Dungeon dungeon = new  Dungeon(5, 5, "The Easy Dungeon", false);
        PoisonEvent poison = new PoisonEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        poison.trigger(warrior, dungeon);
        assertTrue(warrior.getTickTurnsRemaining() > 0);
    }

    @Test
    public void testPoisonReturnsCorrectString() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        PoisonEvent poison = new PoisonEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        assertEquals("POISON", poison.trigger(warrior, dungeon));
    }

    @Test
    public void testPoisonOverwritesDuration() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        new PoisonEvent().trigger(warrior, dungeon);
        int turnsAfterFirst = warrior.getTickTurnsRemaining();
        new PoisonEvent().trigger(warrior, dungeon);
        assertEquals(turnsAfterFirst, warrior.getTickTurnsRemaining());
    }
}
