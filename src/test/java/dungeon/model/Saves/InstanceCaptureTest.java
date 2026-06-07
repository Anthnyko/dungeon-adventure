package dungeon.model.Saves;

import dungeon.model.Dungeon;
import dungeon.model.Saving.GameState;
import dungeon.model.Saving.InstanceCapture;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Warrior;
import dungeon.model.characters.Priest;
import dungeon.model.characters.Thief;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for InstanceCapture game state serialization.
 * Verifies that the InstanceCapture class correctly captures and serializes
 * the complete game state including dungeon, rooms, and hero information.
 */
public class InstanceCaptureTest {
    private Hero hero;
    private Dungeon dungeon;

    @Before
    public void setUp() {
        dungeon = new Dungeon(5, 5, "Test Dungeon", true);
        hero = new Warrior("TestHero", 5, 3);
    }

    @Test
    public void testCaptureGameStateNotNull() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);
        assertNotNull("GameState should not be null", state);
    }

    @Test
    public void testCaptureDungeonDimensions() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertEquals("Dungeon width should be captured", 5, state.myDungeonWidth);
        assertEquals("Dungeon height should be captured", 5, state.myDungeonHeight);
        assertEquals("Dungeon name should be captured", "Test Dungeon", state.myDungeonName);
    }

    @Test
    public void testCaptureDungeonPositions() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertNotNull("Entrance row should be captured", state.myEntranceRow);
        assertNotNull("Entrance column should be captured", state.myEntranceCol);
        assertNotNull("Exit row should be captured", state.myExitRow);
        assertNotNull("Exit column should be captured", state.myExitCol);
    }

    @Test
    public void testCaptureHeroInformation() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertEquals("Hero name should be captured", "TestHero", state.myHeroName);
        assertEquals("Hero class should be captured", "Warrior", state.myHeroClass);
        assertEquals("Hero HP should be captured", hero.getHP(), state.myHeroHP);
    }

    @Test
    public void testCaptureHeroInventory() {
        hero.gainHealingPotion();
        hero.gainVisionPotion();

        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertEquals("Healing potions should be captured", 6, state.myHealingPotions);
        assertEquals("Vision potions should be captured", 4, state.myVisionPotions);
    }

    @Test
    public void testCapturePillars() {
        hero.gainPillar('A');
        hero.gainPillar('E');

        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertNotNull("Pillars should be captured", state.myPillarsFound);
        assertTrue("Should capture pillar A", state.myPillarsFound.contains('A'));
        assertTrue("Should capture pillar E", state.myPillarsFound.contains('E'));
    }

    @Test
    public void testCaptureHeroPosition() {
        dungeon.moveHero("EAST");

        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertEquals("Hero row should be captured", dungeon.getHeroRow(), state.myHeroRow);
        assertEquals("Hero column should be captured", dungeon.getHeroCol(), state.myHeroCol);
    }

    @Test
    public void testCaptureRoomData() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertNotNull("Room data should not be null", state.myRoomData);
        assertEquals("Should have correct number of rows", 5, state.myRoomData.length);
        assertEquals("Should have correct number of columns", 5, state.myRoomData[0].length);
    }

    @Test
    public void testCaptureRoomDataFormat() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        // Each room should have format: "N|S|E|W|Pillar|Monster|Items|Revealed|Events"
        String[] parts = state.myRoomData[0][0].split("\\|");
        assertEquals("Room data should have 9 pipe-separated fields", 9, parts.length);
    }

    @Test
    public void testCaptureEmptyItems() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        // Most rooms should have "none" for items initially
        String roomData = state.myRoomData[0][0];
        assertTrue("Items should be properly serialized",
                roomData.contains("none") || roomData.contains("H") || roomData.contains("V"));
    }

    @Test
    public void testCaptureWithDifferentHeroClasses() {
        // Test with Priest
        Hero priest = new Priest("PriestHero", 4, 2);
        GameState priestState = InstanceCapture.captureGameState(priest, dungeon);
        assertEquals("Should capture Priest class", "Priest", priestState.myHeroClass);

        // Test with Thief
        Hero thief = new Thief("ThiefHero", 3, 5);
        GameState thiefState = InstanceCapture.captureGameState(thief, dungeon);
        assertEquals("Should capture Thief class", "Thief", thiefState.myHeroClass);
    }

    @Test
    public void testCaptureSaveTimestamp() {
        long beforeCapture = System.currentTimeMillis();
        GameState state = InstanceCapture.captureGameState(hero, dungeon);
        long afterCapture = System.currentTimeMillis();

        assertNotNull("Save time should be captured", state.mySaveTime);
        assertTrue("Save time should be within capture window",
                state.mySaveTime >= beforeCapture && state.mySaveTime <= afterCapture);
    }

    @Test
    public void testCaptureMultipleTimes() {
        GameState state1 = InstanceCapture.captureGameState(hero, dungeon);
        hero.gainPillar('I');
        GameState state2 = InstanceCapture.captureGameState(hero, dungeon);

        // state1 should have fewer pillars than state2
        assertTrue("Second capture should reflect hero changes",
                state2.myPillarsFound.size() > state1.myPillarsFound.size());
    }

    @Test
    public void testCapturePreservesHeroStats() {
        int originalHP = hero.getHP();
        int originalHealing = hero.getHealingPotion();

        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertEquals("Hero HP should be preserved", originalHP, state.myHeroHP);
        assertEquals("Healing potions should be preserved", originalHealing, state.myHealingPotions);
    }

    @Test
    public void testCaptureSpecialTimers() {
        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        assertNotNull("Skill timer should be captured", state.mySkillTimer);
        assertNotNull("CD timer should be captured", state.myCDTimer);
    }

    @Test
    public void testCaptureCompleteGameState() {
        // Set up complex game state
        hero.gainPillar('A');
        hero.gainPillar('E');
        hero.gainHealingPotion();
        hero.gainVisionPotion();

        GameState state = InstanceCapture.captureGameState(hero, dungeon);

        // Verify comprehensive capture
        assertNotNull(state.myDungeonWidth);
        assertNotNull(state.myDungeonHeight);
        assertNotNull(state.myDungeonName);
        assertNotNull(state.myHeroName);
        assertNotNull(state.myHeroClass);
        assertNotNull(state.myHeroHP);
        assertNotNull(state.myRoomData);
        assertNotNull(state.myPillarsFound);
        assertTrue(state.mySaveTime > 0);
    }
}
