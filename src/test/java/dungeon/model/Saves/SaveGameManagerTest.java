package dungeon.model.Saves;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dungeon.model.Saving.GameState;
import dungeon.model.Saving.SaveGameManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for SaveGameManager database operations.
 * Tests saving, loading, and managing game saves in the SQLite database.
 */
public class SaveGameManagerTest {
    private SaveGameManager saveGameManager;
    private static final String TEST_DB = "test_dungeon_saves.db";

    @Before
    public void setUp() {
        // Clean up test database if it exists
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }

        saveGameManager = new SaveGameManager("jdbc:sqlite:" + TEST_DB);
    }

    @After
    public void tearDown() {
        // Clean up test database after each test
        File testDb = new File(TEST_DB);
        if (testDb.exists()) {
            testDb.delete();
        }
    }

    @Test
    public void testSaveGameManagerInitialization() {
        assertNotNull("SaveGameManager should be initialized", saveGameManager);
    }

    @Test
    public void testCreateGameState() {
        GameState state = new GameState();
        state.myHeroName = "TestHero";
        state.myHeroClass = "Warrior";
        state.myHeroHP = 100;
        state.myHealingPotions = 5;
        state.myVisionPotions = 3;
        state.myDungeonWidth = 5;
        state.myDungeonHeight = 5;
        state.myDungeonName = "Test Dungeon";
        state.myHeroRow = 0;
        state.myHeroCol = 0;
        state.myEntranceRow = 0;
        state.myEntranceCol = 0;
        state.myExitRow = 4;
        state.myExitCol = 4;
        state.myRoomData = new String[5][5];
        state.myPillarsFound = new ArrayList<>();
        state.mySkillTimer = 0;
        state.myCDTimer = 0;

        assertNotNull("GameState should be created", state);
        assertEquals("TestHero", state.myHeroName);
    }

    @Test
    public void testSaveGame() {
        GameState state = createTestGameState("SaveTest1");

        boolean success = saveGameManager.saveGame(state, "SaveTest1");
        assertTrue("Game should be saved successfully", success);
    }

    @Test
    public void testLoadGame() {
        GameState originalState = createTestGameState("TestHero");
        saveGameManager.saveGame(originalState, "LoadTest1");

        GameState loadedState = saveGameManager.loadGame("LoadTest1");
        assertNotNull("Loaded state should not be null", loadedState);
        assertEquals("Hero name should match", "TestHero", loadedState.myHeroName);
        assertEquals("Hero class should match", "Warrior", loadedState.myHeroClass);
    }

    @Test
    public void testLoadNonExistentGame() {
        GameState loadedState = saveGameManager.loadGame("NonExistent");
        assertNull("Should return null for non-existent save", loadedState);
    }

    @Test
    public void testUpdateExistingSave() {
        GameState state = createTestGameState("UpdateTest");
        saveGameManager.saveGame(state, "UpdateTest");

        // Update the state
        GameState updatedState = createTestGameState("UpdateTest");
        updatedState.myHeroHP = 50;
        updatedState.myHealingPotions = 10;

        boolean success = saveGameManager.saveGame(updatedState, "UpdateTest");
        assertTrue("Game save should be updated successfully", success);

        GameState loaded = saveGameManager.loadGame("UpdateTest");
        assertEquals("Updated HP should be persisted", 50, loaded.myHeroHP);
        assertEquals("Updated potions should be persisted", 10, loaded.myHealingPotions);
    }

    @Test
    public void testListSaves() {
        createAndSaveMultipleSaves();

        List<String> saves = saveGameManager.listSaves();
        assertNotNull("List of saves should not be null", saves);
        assertTrue("Should list at least 2 saves", saves.size() >= 2);
    }

    @Test
    public void testDeleteSave() {
        GameState state = createTestGameState("DeleteTest");
        saveGameManager.saveGame(state, "DeleteTest");

        boolean success = saveGameManager.deleteSave("DeleteTest");
        assertTrue("Save should be deleted successfully", success);

        GameState loaded = saveGameManager.loadGame("DeleteTest");
        assertNull("Deleted save should not be loadable", loaded);
    }

    @Test
    public void testDeleteNonExistentSave() {
        boolean success = saveGameManager.deleteSave("NonExistentSave");
        assertFalse("Deleting non-existent save should return false", success);
    }

    @Test
    public void testSaveWithPillars() {
        GameState state = createTestGameState("PillarTest");
        List<Character> pillars = new ArrayList<>();
        pillars.add('A');
        pillars.add('E');
        pillars.add('I');
        state.myPillarsFound = pillars;

        saveGameManager.saveGame(state, "PillarTest");
        GameState loaded = saveGameManager.loadGame("PillarTest");

        assertNotNull("Pillars should be loaded", loaded.myPillarsFound);
        assertEquals("Should load all pillars", 3, loaded.myPillarsFound.size());
    }

    @Test
    public void testSaveWithHighHP() {
        GameState state = createTestGameState("HighHPTest");
        state.myHeroHP = 200;

        saveGameManager.saveGame(state, "HighHPTest");
        GameState loaded = saveGameManager.loadGame("HighHPTest");

        assertEquals("High HP should be persisted", 200, loaded.myHeroHP);
    }

    @Test
    public void testSaveWithZeroHP() {
        GameState state = createTestGameState("ZeroHPTest");
        state.myHeroHP = 0;

        saveGameManager.saveGame(state, "ZeroHPTest");
        GameState loaded = saveGameManager.loadGame("ZeroHPTest");

        assertEquals("Zero HP should be persisted", 0, loaded.myHeroHP);
    }

    @Test
    public void testSaveMultipleGames() {
        createAndSaveMultipleSaves();

        GameState save1 = saveGameManager.loadGame("MultiTest1");
        GameState save2 = saveGameManager.loadGame("MultiTest2");

        assertNotNull("First save should be loadable", save1);
        assertNotNull("Second save should be loadable", save2);
        assertEquals("First save should have correct hero", "Hero1", save1.myHeroName);
        assertEquals("Second save should have correct hero", "Hero2", save2.myHeroName);
    }

    @Test
    public void testSaveDifferentHeroClasses() {
        // Save Warrior
        GameState warrior = createTestGameState("WarriorSave");
        warrior.myHeroClass = "Warrior";
        saveGameManager.saveGame(warrior, "WarriorSave");

        // Save Priest
        GameState priest = createTestGameState("PriestSave");
        priest.myHeroClass = "Priest";
        saveGameManager.saveGame(priest, "PriestSave");

        // Load and verify
        GameState loadedWarrior = saveGameManager.loadGame("WarriorSave");
        GameState loadedPriest = saveGameManager.loadGame("PriestSave");

        assertEquals("Warrior class should be saved", "Warrior", loadedWarrior.myHeroClass);
        assertEquals("Priest class should be saved", "Priest", loadedPriest.myHeroClass);
    }

    @Test
    public void testSaveWithLargeInventory() {
        GameState state = createTestGameState("LargeInventoryTest");
        state.myHealingPotions = 20;
        state.myVisionPotions = 15;

        saveGameManager.saveGame(state, "LargeInventoryTest");
        GameState loaded = saveGameManager.loadGame("LargeInventoryTest");

        assertEquals("Large healing potion count should persist", 20, loaded.myHealingPotions);
        assertEquals("Large vision potion count should persist", 15, loaded.myVisionPotions);
    }

    @Test
    public void testSaveGameNamePreservation() {
        String saveName = "NamePreservationTest";
        GameState state = createTestGameState(saveName);

        saveGameManager.saveGame(state, saveName);
        GameState loaded = saveGameManager.loadGame(saveName);

        assertNotNull("Save with specific name should be loadable", loaded);
    }

    @Test
    public void testSaveLargerDungeonDimensions() {
        GameState state = createTestGameState("LargeDungeonTest");
        state.myDungeonWidth = 20;
        state.myDungeonHeight = 20;

        saveGameManager.saveGame(state, "LargeDungeonTest");
        GameState loaded = saveGameManager.loadGame("LargeDungeonTest");

        assertEquals("Large dungeon width should persist", 20, loaded.myDungeonWidth);
        assertEquals("Large dungeon height should persist", 20, loaded.myDungeonHeight);
    }

    @Test
    public void testSaveHeroPositionInDungeon() {
        GameState state = createTestGameState("HeroPositionTest");
        state.myHeroRow = 10;
        state.myHeroCol = 15;

        saveGameManager.saveGame(state, "HeroPositionTest");
        GameState loaded = saveGameManager.loadGame("HeroPositionTest");

        assertEquals("Hero row position should persist", 10, loaded.myHeroRow);
        assertEquals("Hero column position should persist", 15, loaded.myHeroCol);
    }

    @Test
    public void testDuplicateSaveName() {
        GameState state1 = createTestGameState("DuplicateTest");
        state1.myHeroHP = 100;
        saveGameManager.saveGame(state1, "DuplicateTest");

        GameState state2 = createTestGameState("DuplicateTest");
        state2.myHeroHP = 50;
        boolean success = saveGameManager.saveGame(state2, "DuplicateTest");

        // The second save should update the first
        assertTrue("Update of existing save should succeed", success);
        GameState loaded = saveGameManager.loadGame("DuplicateTest");
        assertEquals("Updated save should have new HP value", 50, loaded.myHeroHP);
    }

    @Test
    public void testSaveAllHeroStats() {
        GameState state = createTestGameState("AllStatsTest");
        state.myHeroName = "CompleteHero";
        state.myHeroClass = "Thief";
        state.myHeroHP = 85;
        state.myHealingPotions = 6;
        state.myVisionPotions = 4;
        state.mySkillTimer = 2;
        state.myCDTimer = 5;

        saveGameManager.saveGame(state, "AllStatsTest");
        GameState loaded = saveGameManager.loadGame("AllStatsTest");

        assertEquals("CompleteHero", loaded.myHeroName);
        assertEquals("Thief", loaded.myHeroClass);
        assertEquals(85, loaded.myHeroHP);
        assertEquals(6, loaded.myHealingPotions);
        assertEquals(4, loaded.myVisionPotions);
    }

    // Helper method to create a test GameState
    private GameState createTestGameState(String heroName) {
        GameState state = new GameState();
        state.myHeroName = heroName;
        state.myHeroClass = "Warrior";
        state.myHeroHP = 100;
        state.myHealingPotions = 5;
        state.myVisionPotions = 3;
        state.myDungeonWidth = 5;
        state.myDungeonHeight = 5;
        state.myDungeonName = "Test Dungeon";
        state.myHeroRow = 0;
        state.myHeroCol = 0;
        state.myEntranceRow = 0;
        state.myEntranceCol = 0;
        state.myExitRow = 4;
        state.myExitCol = 4;
        state.myRoomData = new String[5][5];
        state.myPillarsFound = new ArrayList<>();
        state.mySkillTimer = 0;
        state.myCDTimer = 0;
        state.mySaveTime = System.currentTimeMillis();
        return state;
    }

    // Helper method to create and save multiple games
    private void createAndSaveMultipleSaves() {
        GameState state1 = createTestGameState("Hero1");
        GameState state2 = createTestGameState("Hero2");

        saveGameManager.saveGame(state1, "MultiTest1");
        saveGameManager.saveGame(state2, "MultiTest2");
    }
}
