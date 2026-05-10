package dungeon.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DungeonTest {

    @Test
    public void testAllRoomExists() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        for(int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertNotNull(dungeon.getRoom(r, c));
            }
        }
    }

    @Test
    public void testEntranceExists() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        int count = 0;
        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++){
                if (dungeon.getRoom(r, c).isEntrance()) {
                    count++;
                }
            }
        }
        assertEquals(1, count);
    }

    @Test
    public void testHeroStartsAtEntrance() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        assertTrue(dungeon.getCurrentRoom().isEntrance());
    }

    @Test
    public void testExitExist() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        int count = 0;
        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++){
                if (dungeon.getRoom(r, c).isExit()) {
                    count++;
                }
            }
        }
        assertEquals(1, count);
    }

    @Test
    public void testAllPillarsExist() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        int count = 0;
        for (int r = 0; r < 5; r++){
            for (int c = 0; c < 5; c++){
                if (dungeon.getRoom(r, c).hasPillar()) {
                    count++;
                }
            }
        }
        assertEquals(4,  count);
    }

    @Test
    public void testMoveHeroBlockedByWall() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
        Room room =  dungeon.getCurrentRoom();
        room.setNorthDoor(false);
        assertFalse(dungeon.moveHero("NORTH"));
    }

    @Test
    public void testMoveHeroWhenDoorExists() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
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
        Dungeon dungeon = new Dungeon(5, 5, "test");
        Room room =  dungeon.getCurrentRoom();
        List<String> directions = dungeon.getValidDirections();

        if (room.hasNorthDoor()) assertTrue(directions.contains("NORTH"));
        if (room.hasSouthDoor()) assertTrue(directions.contains("SOUTH"));
        if (room.hasWestDoor()) assertTrue(directions.contains("WEST"));
        if (room.hasEastDoor()) assertTrue(directions.contains("EAST"));
    }

    @Test
    public void testRevealSurroundingRooms() {
        Dungeon dungeon = new Dungeon(5, 5, "test");
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
}
