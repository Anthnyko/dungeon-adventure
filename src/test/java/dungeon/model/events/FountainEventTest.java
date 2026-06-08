package dungeon.model.events;

import dungeon.model.Dungeon;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.characters.Warrior;
import org.junit.Test;

import static org.junit.Assert.*;

public class FountainEventTest {

    @Test
    public void testAlarmReturnsValidString() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        FountainEvent fountain = new FountainEvent();
        Warrior warrior = new Warrior("test", 0, 0);
        String result = fountain.trigger(warrior, dungeon);
        assertTrue(
                result.equals("FOUNTAIN_HEAL") ||
                        result.equals("FOUNTAIN_TELEPORT") ||
                        result.equals("FOUNTAIN_POTION") ||
                        result.equals("FOUNTAIN_MIND")
        );
    }

    @Test
    public void testFountainMindNeverTriggersWhenNoPillars() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);

        warrior.gainPillar('A');
        warrior.gainPillar('E');
        warrior.gainPillar('I');
        warrior.gainPillar('P');

        for (int i = 0; i < 100; i++) {
            FountainEvent fountain = new FountainEvent();
            String result =  fountain.trigger(warrior, dungeon);
            assertNotEquals("FOUNTAIN_MIND", result);
        }
    }

    @Test
    public void testFountainHealRestoresHP() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        warrior.takeDamage(50);
        int hpBefore = warrior.getHP();
        new FountainEvent(0).trigger(warrior, dungeon);
        assertTrue(warrior.getHP() > hpBefore);
    }

    @Test
    public void testFountainTeleportMovesHero() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        int rowBefore = dungeon.getHeroRow();
        int colBefore = dungeon.getHeroCol();
        new FountainEvent(1).trigger(warrior, dungeon);
        assertFalse(dungeon.getHeroRow() == rowBefore
                && dungeon.getHeroCol() == colBefore);
    }

    @Test
    public void testFountainPotionGrantsPotions() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        int potionsBefore = warrior.getHealingPotion() + warrior.getVisionPotion();
        new FountainEvent(2).trigger(warrior, dungeon);
        assertTrue(warrior.getHealingPotion() + warrior.getVisionPotion() > potionsBefore);
    }

    @Test
    public void testFountainMindAppliesTickDamage() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Warrior warrior = new Warrior("test", 0, 0);
        new FountainEvent(3).trigger(warrior, dungeon);
        assertTrue(warrior.getTickTurnsRemaining() > 0);
    }
}
