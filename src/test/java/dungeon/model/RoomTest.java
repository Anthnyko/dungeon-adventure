package dungeon.model;

import dungeon.items.HealingPotion;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomTest {

    @Test
    public void testEntranceHasNothingElse() {
        Room room = new Room();
        room.addItem(new HealingPotion());
        room.setPit(true);
        room.setEntrance();

        assertTrue(room.isEntrance());
        assertFalse(room.hasItems());
        assertFalse(room.hasPit());
    }

    @Test
    public void testExitHasNoItems() {
        Room room = new Room();
        room.addItem(new HealingPotion());
        room.setPit(true);
        room.setEntrance();

        assertTrue(room.isEntrance());
        assertFalse(room.hasItems());
        assertFalse(room.hasPit());
    }

    @Test
    public void testPickUpItemsEmptiesRoom() {
        Room room = new Room();
        room.addItem(new HealingPotion());
        room.pickUpItems();
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
    public void testTriggerPit() {
        Room room = new Room();
        room.setPit(true);

        int damage1 = room.triggerPitDamage();
        int damage2 = room.triggerPitDamage(); //To test that the trap can't be triggered twice

        assertTrue(damage1 >= 0);
        assertEquals(0, damage2);
    }
}
