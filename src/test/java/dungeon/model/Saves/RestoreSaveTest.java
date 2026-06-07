package dungeon.model.Saves;

import dungeon.model.Dungeon;
import dungeon.model.Saving.GameState;
import dungeon.model.Saving.RestoreSave;
import dungeon.model.characters.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for RestoreSave game state restoration.
 * Verifies that the RestoreSave class correctly deserializes and reconstructs
 * the complete game state from a GameState object.
 */
public class RestoreSaveTest {
    private RestoreSave restoreSave;
    private GameState gameState;

    @Before
    public void setUp() {
        restoreSave = new RestoreSave();
        gameState = new GameState();
    }

    @Test
    public void testRestoreGameStateNotNull() {
        // Create minimal valid game state
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test Dungeon";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 5;
        gameState.myVisionPotions = 3;
        gameState.myRoomData = new String[5][5];
        
        // Populate room data inline
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        assertNotNull("RestoreResult should not be null", result);
    }

    @Test
    public void testRestoreDungeonDimensions() {
        gameState.myDungeonWidth = 7;
        gameState.myDungeonHeight = 7;
        gameState.myDungeonName = "Large Dungeon";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[7][7];
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 6;
        gameState.myExitCol = 6;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Dungeon restoredDungeon = result.myDungeon;

        assertEquals("Dungeon width should be restored", 7, restoredDungeon.getDungeonWidth());
        assertEquals("Dungeon height should be restored", 7, restoredDungeon.getDungeonHeight());
        assertEquals("Dungeon name should be restored", "Large Dungeon", restoredDungeon.getMyName());
    }

    @Test
    public void testRestoreWarriorHero() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "AragonWarrior";
        gameState.myHeroHP = 125;
        gameState.myHealingPotions = 4;
        gameState.myVisionPotions = 2;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Hero name should be restored", "AragonWarrior", hero.getCharName());
        assertEquals("Hero class should be Warrior", "Warrior", hero.getClassName());
        assertEquals("Hero HP should be restored", 125, hero.getHP());
    }

    @Test
    public void testRestorePriestHero() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Priest";
        gameState.myHeroName = "GandalfPriest";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 5;
        gameState.myVisionPotions = 3;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Hero name should be restored", "GandalfPriest", hero.getCharName());
        assertEquals("Hero class should be Priest", "Priest", hero.getClassName());
    }

    @Test
    public void testRestoreThiefHero() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Thief";
        gameState.myHeroName = "LegolasThief";
        gameState.myHeroHP = 75;
        gameState.myHealingPotions = 3;
        gameState.myVisionPotions = 5;
        gameState.myRoomData = new String[5][5];        populateRoomData(gameState.myRoomData, 5, 5);        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Hero name should be restored", "LegolasThief", hero.getCharName());
        assertEquals("Hero class should be Thief", "Thief", hero.getClassName());
    }

    @Test
    public void testRestoreInvalidHeroClass() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "InvalidClass";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];        populateRoomData(gameState.myRoomData, 5, 5);        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        try {
            restoreSave.restoreGameState(gameState);
            fail("Should throw IllegalArgumentException for invalid hero class");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRestoreHeroPosition() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroRow = 2;
        gameState.myHeroCol = 3;
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Dungeon dungeon = result.myDungeon;

        assertEquals("Hero row should be restored", 2, dungeon.getHeroRow());
        assertEquals("Hero column should be restored", 3, dungeon.getHeroCol());
    }

    @Test
    public void testRestoreEntranceAndExit() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Dungeon dungeon = result.myDungeon;

        assertEquals("Entrance row should be restored", 0, dungeon.getMyEntranceRow());
        assertEquals("Entrance column should be restored", 0, dungeon.getMyEntranceCol());
        assertEquals("Exit row should be restored", 4, dungeon.getMyExitRow());
        assertEquals("Exit column should be restored", 4, dungeon.getMyExitCol());
    }

    @Test
    public void testRestoreHeroHP() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 50; // Damaged hero
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Hero HP should be restored to 50", 50, hero.getHP());
    }

    @Test
    public void testRestoreHealingPotions() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 7;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];        populateRoomData(gameState.myRoomData, 5, 5);        populateRoomData(gameState.myRoomData, 5, 5);
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Healing potions should be restored", 7, hero.getHealingPotion());
    }

    @Test
    public void testRestoreVisionPotions() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 5;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Vision potions should be restored", 5, hero.getVisionPotion());
    }

    @Test
    public void testRestorePillars() {
        List<Character> pillars = new ArrayList<>();
        pillars.add('A');
        pillars.add('E');
        pillars.add('I');

        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = pillars;
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        assertEquals("Should restore 3 pillars", 3, hero.getMyPillars().size());
        assertTrue("Should contain pillar A", hero.getMyPillars().contains('A'));
        assertTrue("Should contain pillar E", hero.getMyPillars().contains('E'));
        assertTrue("Should contain pillar I", hero.getMyPillars().contains('I'));
    }

    @Test
    public void testRestoreNullPillars() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = null; // Null pillars
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        // Should not throw exception with null pillars
        restoreSave.restoreGameState(gameState);
    }

    @Test
    public void testRestoreEmptyRoomData() {
        gameState.myDungeonWidth = 2;
        gameState.myDungeonHeight = 2;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 1;
        gameState.myExitCol = 1;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        assertNotNull("Should handle empty room data", result);
    }

    @Test
    public void testRestoreResultStructure() {
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 100;
        gameState.myHealingPotions = 0;
        gameState.myVisionPotions = 0;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        gameState.myPillarsFound = new ArrayList<>();
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);

        assertNotNull("Dungeon should not be null", result.myDungeon);
        assertNotNull("Hero should not be null", result.myHero);
    }

    @Test
    public void testRoundTripSaveRestore() {
        // Create original game state
        gameState.myDungeonWidth = 5;
        gameState.myDungeonHeight = 5;
        gameState.myDungeonName = "Test";
        gameState.myHeroClass = "Warrior";
        gameState.myHeroName = "TestHero";
        gameState.myHeroHP = 75;
        gameState.myHealingPotions = 4;
        gameState.myVisionPotions = 2;
        gameState.myRoomData = new String[5][5];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                gameState.myRoomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
        List<Character> pillars = new ArrayList<>();
        pillars.add('A');
        gameState.myPillarsFound = pillars;
        gameState.myHeroRow = 2;
        gameState.myHeroCol = 2;
        gameState.myEntranceRow = 0;
        gameState.myEntranceCol = 0;
        gameState.myExitRow = 4;
        gameState.myExitCol = 4;

        // Restore
        RestoreSave.RestoreResult result = restoreSave.restoreGameState(gameState);
        Hero hero = result.myHero;

        // Verify round trip
        assertEquals("Hero name should match", "TestHero", hero.getCharName());
        assertEquals("Hero class should match", "Warrior", hero.getClassName());
        assertEquals("Hero HP should match", 75, hero.getHP());
        assertEquals("Healing potions should match", 4, hero.getHealingPotion());
    }

    /**
     * Helper method to populate room data array with valid room strings.
     * Format: "N|S|E|W|Pillar|Monster|Items|Revealed|Events"
     */
    private void populateRoomData(String[][] roomData, int height, int width) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Create a valid room data string: no doors, no pillar, no monster, no items, not revealed, no events
                roomData[row][col] = "0|0|0|0|.|-|none|0|none";
            }
        }
    }
}
