package dungeon.model.dungeon;

import dungeon.model.Dungeon;
import dungeon.model.Room;
import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.characters.Ogre;
import dungeon.model.characters.Warrior;
import dungeon.model.items.HealingPotion;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void testEntranceHasNothingElse() {
        Room room = new Room();
        room.addItem(new HealingPotion());
        room.addEvent(new PitEvent());
        room.setEntrance();

        assertTrue(room.isEntrance());
        assertFalse(room.hasItems());
        assertFalse(room.hasPit());
    }

    @Test
    public void testExitHasNothingElse() {
        Room room = new Room();
        room.addItem(new HealingPotion());
        room.addEvent(new PitEvent());
        room.setExit();

        assertTrue(room.isExit());
        assertFalse(room.hasItems());
        assertFalse(room.hasPit());
    }

    @Test
    public void testPickUpItemsEmptiesRoom() {
        Room room = new Room();
        Warrior warrior = new Warrior("test", 0, 0);
        room.addItem(new HealingPotion());
        room.pickUpItems(warrior);
        assertFalse(room.hasItems());
    }

    @Test
    public void testPickUpPillar() {
        Room room = new Room();
        room.setPillar('I');
        char pillar = room.pickUpPillar();

        assertEquals('I', pillar);
        assertFalse(room.hasPillar());
    }

    @Test
    public void testTriggerEvent() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room = new Room();
        Warrior warrior = new Warrior("test", 0, 0);
        room.addEvent(new PitEvent());

        int hpBefore = warrior.getHP();
        room.triggerEvent("PIT", warrior, dungeon);
        int hpAfter = warrior.getHP();

        assertTrue(hpAfter < hpBefore);
        assertFalse(room.hasPit()); //check that pit is removed after
    }

    @Test
    public void testHasItems() {
        Room room = new Room();
        Warrior warrior = new Warrior("test", 0, 0);
        room.addItem(new HealingPotion());
        assertTrue(room.hasItems());

        room.pickUpItems(warrior);
        assertFalse(room.hasItems());
    }

    @Test
    public void testHasPit() {
        Room room = new Room();
        room.addEvent(new PitEvent());
        assertTrue(room.hasPit());
    }

    @Test
    public void testHasAlarm() {
        Room room = new Room();
        room.addEvent(new AlarmEvent());
        assertTrue(room.hasAlarm());
    }

    @Test
    public void testHasPoison() {
        Room room = new Room();
        room.addEvent(new PoisonEvent());
        assertTrue(room.hasPoison());
    }

    @Test
    public void testHasFountain() {
        Room room = new Room();
        room.addEvent(new FountainEvent());
        assertTrue(room.hasFountain());
    }

    @Test
    public void testMultipleEventsInRoom() {
        Room room = new Room();
        room.addEvent(new PitEvent());
        room.addEvent(new FountainEvent());
        assertTrue(room.hasPit());
        assertTrue(room.hasFountain());
    }

    @Test
    public void testSetPillarType() {
        Room room = new Room();
        room.setPillar('A');
        assertTrue(room.hasPillar());
        assertEquals('A', room.getPillarType());
    }

    @Test
    public void testRemoveMonster() {
        Room room = new Room();
        room.setMonster(new Ogre());
        assertTrue(room.hasMonster());
        room.removeMonster();
        assertFalse(room.hasMonster());
    }

    @Test
    public void testTriggerEventReturnsCorrectString() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room = new Room();
        Warrior warrior = new Warrior("test", 0, 0);
        room.addEvent(new PitEvent());
        String result = room.triggerEvent("PIT", warrior, dungeon);
        assertEquals("PIT", result);
    }

    @Test
    public void testTriggerEventReturnsNullIfNoMatch() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room = new Room();
        Warrior warrior = new Warrior("test", 0, 0);
        String result =  room.triggerEvent("PIT", warrior, dungeon);
        assertNull(result);
    }

}
