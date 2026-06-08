package dungeon.model.dungeon;

import dungeon.model.Dungeon;
import dungeon.model.Room;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DungeonTest {

    @Test
    public void testAllRoomExists() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        for(int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertNotNull(dungeon.getRoom(r, c));
            }
        }
    }

    @Test
    public void testEntranceExists() {
        for (int i = 0; i < 10; i++) {
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            int count = 0;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (dungeon.getRoom(r, c).isEntrance()) {
                        count++;
                    }
                }
            }
            assertEquals("Failed on iteration " + i, 1, count);
        }
    }

    @Test
    public void testExitExist() {
        for (int i = 0; i < 10; i++) {
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            int count = 0;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (dungeon.getRoom(r, c).isExit()) {
                        count++;
                    }
                }
            }
            assertEquals("Failed on iteration " + i, 1, count);
        }
    }

    @Test
    public void testHeroStartsAtEntrance() {
        for (int i = 0; i < 10; i++) {
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            assertTrue("Failed on iteration " + i, dungeon.getCurrentRoom().isEntrance());
        }
    }

    @Test
    public void testAllPillarsExist() {
        for (int i = 0; i < 10; i++) {
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            int count = 0;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (dungeon.getRoom(r, c).hasPillar()) count++;
                }
            }
            assertEquals("Failed on iteration " + i, 4, count);
        }
    }

    @Test
    public void testMoveHeroBlockedByWall() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room =  dungeon.getCurrentRoom();
        room.setNorthDoor(false);
        assertFalse(dungeon.moveHero("NORTH"));
    }

    @Test
    public void testMoveHeroWhenDoorExists() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room = dungeon.getCurrentRoom();

        // Force all possible doors
        room.setNorthDoor(true);
        room.setSouthDoor(true);
        room.setEastDoor(true);
        room.setWestDoor(true);

        // Try all directions because of dungeon randomness
        boolean moved =
                dungeon.moveHero("NORTH") ||
                        dungeon.moveHero("SOUTH") ||
                        dungeon.moveHero("EAST")  ||
                        dungeon.moveHero("WEST");

        assertTrue(moved);
    }

    @Test
    public void testValidDirectionsMatchDoors() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        Room room =  dungeon.getCurrentRoom();
        List<String> directions = dungeon.getValidDirections();

        if (room.hasNorthDoor()) assertTrue(directions.contains("NORTH"));
        if (room.hasSouthDoor()) assertTrue(directions.contains("SOUTH"));
        if (room.hasWestDoor()) assertTrue(directions.contains("WEST"));
        if (room.hasEastDoor()) assertTrue(directions.contains("EAST"));
    }

    @Test
    public void testIsExitReached() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        assertFalse(dungeon.isExitReached());
        dungeon.setHeroPosition(dungeon.getMyExitRow(), dungeon.getMyExitCol());
        assertTrue(dungeon.isExitReached());
    }

    @Test
    public void testRevealSurroundingRooms() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        dungeon.revealSurroundingRooms();
        boolean oneRevealed = false;

        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++){
                if (dungeon.getRoom(r, c).isRevealed()) {
                    oneRevealed = true;
                }
            }
        }
        assertTrue(oneRevealed);
    }

    @Test
    public void testPillarRoomsHaveGuardians() {
        for (int i = 0; i < 10; i++){ //loop multiple times because of randomness
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    Room room = dungeon.getRoom(r, c);
                    if (dungeon.getRoom(r, c).hasPillar()) {
                        assertTrue(dungeon.getRoom(r, c).hasMonster());
                    }
                }
            }
        }
    }

    @Test
    public void testMonsterCountDoesNotExceedMax() {
        for (int i = 0; i < 10; i++){
            Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
            int monsters = 0;
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    Room room = dungeon.getRoom(r, c);
                    if (room.hasMonster() && !room.hasPillar()) {
                        monsters++;
                    }
                }
            }
            int maxMonsters = (int) Math.ceil(5 * 5 * 0.25);
            assertTrue(monsters <= maxMonsters);
        }
    }

    @Test
    public void testTeleportMovesHero() {
        Dungeon dungeon = new Dungeon(5, 5, "The Easy Dungeon", false);
        int startRow = dungeon.getHeroRow();
        int startCol = dungeon.getHeroCol();
        dungeon.teleportHeroRandom();
        assertFalse(dungeon.getHeroRow() == startRow
                    && dungeon.getHeroCol() == startCol);
    }
}
