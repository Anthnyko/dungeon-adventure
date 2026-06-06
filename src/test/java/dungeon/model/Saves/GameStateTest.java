package dungeon.model.Saves;

import org.junit.Before;
import org.junit.Test;

import dungeon.model.Saving.GameState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for GameState serialization and data storage.
 * Verifies that GameState correctly stores and retrieves game information.
 */
public class GameStateTest {
    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState();
    }

    @Test
    public void testGameStateDefaultConstructor() {
        assertNotNull("GameState should be instantiated successfully", gameState);
    }

    @Test
    public void testDungeonDimensionsStorage() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test Dungeon";

        assertEquals("Dungeon width should be stored correctly", 5, gameState.myDungeonWidth);
        assertEquals("Dungeon height should be stored correctly", 5, gameState.myDungeonHeight);
        assertEquals("Dungeon name should be stored correctly", "Test Dungeon", gameState.myDungeonName);
    }

    @Test
    public void testHeroPositionStorage() {
        gameState.myHeroRow = 2;
        gameState.myHeroCol = 3;
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        assertEquals("Hero row should be stored correctly", 2, gameState.myHeroRow);
        assertEquals("Hero column should be stored correctly", 3, gameState.myHeroCol);
        assertEquals("Entrance row should be stored correctly", 0, gameState.myEntranceRow);
        assertEquals("Entrance column should be stored correctly", 0, gameState.myEntranceCol);
        assertEquals("Exit row should be stored correctly", 4, gameState.myExitRow);
        assertEquals("Exit column should be stored correctly", 4, gameState.myExitCol);
    }

    @Test
    public void testHeroInformationStorage() {
        gameState.myHeroName = "Hero Name";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 5;
        gameState.myVisionPotions = 3;

        assertEquals("Hero name should be stored correctly", "Hero Name", gameState.myHeroName);
        assertEquals("Hero class should be stored correctly", "Warrior", gameState.myHeroClass);
        assertEquals("Hero HP should be stored correctly", 100, gameState.myHeroHP);
        assertEquals("Healing potions should be stored correctly", 5, gameState.myHealingPotions);
        assertEquals("Vision potions should be stored correctly", 3, gameState.myVisionPotions);
    }

    @Test
    public void testHeroSpecialAbilitiesStorage() {
        gameState.mySkillTimer = 5;
        gameState.myCDTimer = 3;

        assertEquals("Skill timer should be stored correctly", 5, gameState.mySkillTimer);
        assertEquals("Cooldown timer should be stored correctly", 3, gameState.myCDTimer);
    }

    @Test
    public void testPillarsFoundStorage() {
        List<Character> pillars = new ArrayList<>();
        pillars.add('A');
        pillars.add('E');
        gameState.myPillarsFound = pillars;

        assertNotNull("Pillars list should not be null", gameState.myPillarsFound);
        assertEquals("Should store two pillars", 2, gameState.myPillarsFound.size());
        assertTrue("Should contain pillar A", gameState.myPillarsFound.contains('A'));
        assertTrue("Should contain pillar E", gameState.myPillarsFound.contains('E'));
    }

    @Test
    public void testRoomDataStorage() {
        String[][] roomData = new String[2][2];
        roomData[0][0] = "1|1|0|0|.|.|none|1|none";
        roomData[0][1] = "1|0|1|1|A|Ogre|H,V|1|P,F";
        gameState.myRoomData = roomData;

        assertNotNull("Room data should not be null", gameState.myRoomData);
        assertEquals("Should have 2 rows", 2, gameState.myRoomData.length);
        assertEquals("Should have 2 columns", 2, gameState.myRoomData[0].length);
        assertEquals("Room data should be stored correctly", "1|1|0|0|.|.|none|1|none", gameState.myRoomData[0][0]);
    }

    @Test
    public void testSaveTimeStorage() {
        long currentTime = System.currentTimeMillis();
        gameState.mySaveTime = currentTime;

        assertEquals("Save time should be stored correctly", currentTime, gameState.mySaveTime);
    }

    @Test
    public void testCompleteGameStateStorage() {
        // Set all dungeon information
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Main Dungeon";
        gameState.myHeroRow = 0;
        gameState.myHeroCol = 0;
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        // Set hero information
        gameState.myHeroName = "Aragorn";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroHP = 125;
        gameState.myHealingPotions = 4;
        gameState.myVisionPotions = 2;

        // Set abilities
        gameState.mySkillTimer = 0;
        gameState.myCDTimer = 0;

        // Set pillars
        List<Character> pillars = new ArrayList<>();
        pillars.add('A');
        pillars.add('E');
        pillars.add('I');
        pillars.add('P');
        gameState.myPillarsFound = pillars;

        // Set room data
        gameState.myRoomData = new String[5][5];
        gameState.mySaveTime = System.currentTimeMillis();

        // Verify all fields
        assertEquals(5, gameState.myDungeonWidth);
        assertEquals(5, gameState.myDungeonHeight);
        assertEquals("Main Dungeon", gameState.myDungeonName);
        assertEquals("Aragorn", gameState.myHeroName);
        assertEquals("Warrior", gameState.myHeroClass);
        assertEquals(125, gameState.myHeroHP);
        assertEquals(4, gameState.myPillarsFound.size());
    }

    @Test
    public void testNullPillarsHandling() {
        gameState.myPillarsFound = null;
        assertNull("Pillars can be null", gameState.myPillarsFound);
    }

    @Test
    public void testZeroHPStorage() {
        gameState.myHeroHP = 0;
        assertEquals("HP can be zero", 0, gameState.myHeroHP);
    }

    @Test
    public void testEmptyRoomDataArray() {
        gameState.myRoomData = new String[0][0];
        assertEquals("Empty room data should be storable", 0, gameState.myRoomData.length);
    }
}
