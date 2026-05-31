package dungeon.model;

import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.characters.Hero;
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
        Warrior warrior = new Warrior("test");
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
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon");
        Room room = new Room();
        Warrior warrior = new Warrior("test");
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
        Warrior warrior = new Warrior("test");
        room.addItem(new HealingPotion());
        assertTrue(room.hasItems());

        room.pickUpItems(warrior);
        assertFalse(room.hasItems());
    }

    @Test
    public void testFountainSet() {
        Room room = new Room();
        room.addEvent(new FountainEvent());
        assertTrue(room.hasFountain());
    }
}
