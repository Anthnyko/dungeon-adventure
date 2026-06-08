package dungeon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.characters.Monster;

/**
 * Represents a randomly generated dungeon maze.
 * <p>
 * The dungeon is a 2D grid of room objects connected by doors.
 * The dungeon class is responsible for:
 * Generating the maze,
 * Placing the entrance, exit and four pillars,
 * Keeping track of the hero
 *
 * @author Ibrahim Mohamud
 * @version 1.3
 */
public class Dungeon {
    /** The 2D grid of rooms that make up the dungeon */
    private Room[][] myRooms;

    /** The number of columns in the dungeon */
    private final int myWidth;

    /** The number of rows in the dungeon */
    private final int myHeight;

    /** The row index of the entrance room */
    private int myEntranceRow;

    /** The column index of the entrance room */
    private int myEntranceCol;

    /** The row index of the exit room */
    private int myExitRow;
    /** The column index of the exit room */
    private int myExitCol;

    /** The row index of the room the hero is in */
    private int myHeroRow;

    /** The column index of the room the hero is in */
    private int myHeroCol;

    /** The maximum number of monsters that can be placed in the dungeon */
    private final int myMaxMonsters;

    /** the row of the alerted monster for the alarm event, -1 if no monster is alerted */
    private int myAlertedMonsterRow = -1;

    /** the column of the alerted monster for the alarm event, -1 if no monster is alerted */
    private int myAlertedMonsterCol = -1;

    /** The name of the dungeon */
    private final String myName;

    /** For monster generation in the dungeon */
    private final MonsterGenerator myMonsterGenerator;

    /** The chance that each event has to be placed */
    private static final int EVENT_CHANCE = 10;

    /** Random object for random generation */
    private final Random myRandom;

    /**
     * Constructs a new dungeon given the dimensions and name then
     * generates a valid maze.
     *
     * @param theWidth the number of columns in the dungeon
     * @param theHeight the number of rows in the dungeon
     * @param theName the name of the dungeon
     */
    public Dungeon(final int theWidth, final int theHeight, final String theName, final boolean forLoad) {
        myRandom = new Random();
        myMonsterGenerator = new MonsterGenerator();
        myWidth = theWidth;
        myHeight = theHeight;
        myName = theName;
        myMaxMonsters = switch(theName) {
            case "The Easy Dungeon" -> (int) Math.ceil(myWidth * myHeight * 0.25);
            case "The Medium Dungeon" -> (int) Math.ceil(myWidth * myHeight * 0.3);
            case "The Hard Dungeon" -> (int) Math.ceil(myWidth * myHeight * 0.35);
            default -> (int) Math.ceil(myWidth * myHeight * 0.3);
        };
        if (forLoad) {
            initializeRoomsForLoad();
        } else {
            mazeGeneration();
        }
    }

    /**
     * Generates a valid dungeon maze by repeatedly initializing rooms,
     * placing the entrance and exit, and adding random paths until a valid
     * traversable maze is made. Then places all dungeon contents such as events,
     * monsters, and pillars.
     */
    private void mazeGeneration() {
        do {
            initializeRooms();

            //Pick entrance
            myEntranceRow = myRandom.nextInt(myHeight);
            myEntranceCol = myRandom.nextInt(myWidth);
            myHeroRow = myEntranceRow;
            myHeroCol = myEntranceCol;
            myRooms[myEntranceRow][myEntranceCol].setEntrance();
            myRooms[myEntranceRow][myEntranceCol].setRevealed(true); //sets the entrance room as revealed

            //Pick exit
            do {
                myExitRow = myRandom.nextInt(myHeight);
                myExitCol = myRandom.nextInt(myWidth);
            } while (myExitRow == myEntranceRow && myExitCol == myEntranceCol);
            myRooms[myExitRow][myExitCol].setExit();

            addRandomPaths();

        } while (!isValidMaze());

        placePillars();
        placePits();
        placePoisons();
        placeAlarms();
        placeFountains();
        placeMonsters();
    }

    /**
     * Initializes the 2D grid of rooms with empty rooms.
     */
    private void initializeRooms() {
        myRooms = new Room[myHeight][myWidth];
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                myRooms[row][col] = new Room();
            }
        }
    }

    /**
     * Initializes rooms with no items
     */
    private void initializeRoomsForLoad() {
        myRooms = new Room[myHeight][myWidth];
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                myRooms[row][col] = new Room(true);
            }
        }
    }

    /**
    * Randomly connects rooms with doors in the east and south directions
    * to create a maze of paths.
    */
    private void addRandomPaths() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (col < myWidth - 1 && myRandom.nextBoolean()) {
                    connectEast(row, col);
                }
                if (row < myHeight - 1 && myRandom.nextBoolean()) {
                    connectSouth(row, col);
                }
            }
        }
    }

    /**
     * Checks if the maze is valid by verifying that every room is reachable
     * from the entrance using BFS traversal.
     *
     * @return true if all rooms are reachable, false otherwise
     */
    private boolean isValidMaze() {
        final boolean[][] visited = new boolean[myHeight][myWidth];
        final Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{myEntranceRow, myEntranceCol});
        visited[myEntranceRow][myEntranceCol] = true;

        //Check for valid path from entrance to exit
        while (!queue.isEmpty()) {
            final int[] current = queue.poll();
            final int r = current[0],  c = current[1];
            final Room room = myRooms[r][c];

            if (room.hasNorthDoor() && r > 0          && !visited[r-1][c]) { visited[r-1][c] = true; queue.add(new int[]{r-1, c}); }
            if (room.hasSouthDoor() && r < myHeight-1 && !visited[r+1][c]) { visited[r+1][c] = true; queue.add(new int[]{r+1, c}); }
            if (room.hasEastDoor()  && c < myWidth-1  && !visited[r][c+1]) { visited[r][c+1] = true; queue.add(new int[]{r, c+1}); }
            if (room.hasWestDoor()  && c > 0          && !visited[r][c-1]) { visited[r][c-1] = true; queue.add(new int[]{r, c-1}); }
        }

        //Check that every room can be reached
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (!visited[row][col]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Places the four pillars of OO (A, E, I, P) in the dungeon.
     */
    private void placePillars() {
        final char[] pillars = {'A', 'E', 'I', 'P'};
        final int minDistance = (myWidth + myHeight) / 4; //pillars minimum distance from entrance based on dungeon size

        for (final char pillar : pillars) {
            int row, col;
            do {
                row = myRandom.nextInt(myHeight);
                col = myRandom.nextInt(myWidth);
            } while (isSpecialRoom(row, col) ||
                    Math.abs(row - myEntranceRow) + Math.abs(col - myEntranceCol) <= minDistance ||
                    hasAdjacentPillar(row, col));

            myRooms[row][col].setPillar(pillar);
        }
    }

    /**
     * Checks if the room at the given position has an adjacent room with a pillar
     * connected through an open door.
     * Helper for placePillars().
     *
     * @param theRow the row of the room to check
     * @param theCol the column of the room to check
     * @return true if an adjacent connected room has a pillar, false otherwise
     */
    private boolean hasAdjacentPillar(final int theRow, final int theCol) {
        final Room room = myRooms[theRow][theCol];
        return (isInBounds(theRow - 1, theCol) && myRooms[theRow - 1][theCol].hasPillar() && room.hasNorthDoor()) || // north
                (isInBounds(theRow + 1, theCol) && myRooms[theRow + 1][theCol].hasPillar() && room.hasSouthDoor()) || // south
                (isInBounds(theRow, theCol + 1) && myRooms[theRow][theCol + 1].hasPillar() && room.hasEastDoor())  || // east
                (isInBounds(theRow, theCol - 1) && myRooms[theRow][theCol - 1].hasPillar() && room.hasWestDoor());   // west
    }

    /**
     * Randomly places pit trap events throughout the dungeon.
     */
    private void placePits() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                final Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new PitEvent());
                }
            }
        }
    }

    /**
     * Randomly places poison trap events throughout the dungeon.
     */
    private void placePoisons() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                final Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new PoisonEvent());
                }
            }
        }
    }

    /**
     * Randomly places alarm trap events throughout the dungeon.
     */
    private void placeAlarms() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                final Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new AlarmEvent());
                }
            }
        }
    }

    /**
     * Places fountain events throughout the dungeon based on dungeon size.
     * Approximately one fountain is placed per 10 rooms.
     */
    private void placeFountains() {
        final int count = (myWidth * myHeight) / 10; // around 1 fountain per 10 rooms

        for (int i = 0; i < count; i++) {
            int row, col;
            do {
                row = myRandom.nextInt(myHeight);
                col = myRandom.nextInt(myWidth);
            } while (isSpecialRoom(row, col));

            myRooms[row][col].addEvent(new FountainEvent());
        }
    }

    /**
     * Places monsters throughout the dungeon up to the maximum monster count
     * which is dependent on difficulty.
     * Pillar rooms receive a Pillar Guardian monster.
     * Regular rooms have a 30% chance to receive a random monster.
     */
    private void placeMonsters() {
        int monstersPlaced = 0;

        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {

                final Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (room.hasPillar()) {
                    room.setMonster(myMonsterGenerator.createMonster("Pillar Guardian"));
                    continue;
                }

                if (monstersPlaced < myMaxMonsters && myRandom.nextInt(100) < 30) { // 30% chance to spawn monster in room
                    room.setMonster(myMonsterGenerator.createMonster(getRandomMonsterType(row, col)));
                    monstersPlaced++;
                }
            }
        }
    }

    /**
     * Returns the type of monster to spawn at the given position.
     * Rooms close to the entrance only spawn Gremlins or Skeletons.
     * Rooms far from the entrance can also spawn Ogres.
     *
     * @param theRow the row of the room
     * @param theCol the column of the room
     * @return the name of the monster type to spawn
     */
    private String getRandomMonsterType(final int theRow, final int theCol) {
        final int entranceDistance = Math.abs(theRow - myEntranceRow) + Math.abs(theCol - myEntranceCol);
        final int minDistance = (myWidth + myHeight) / 4; //ogre minimum distance from entrance based on dungeon size

        if (entranceDistance <= minDistance) {
            final String[] types = {"Gremlin", "Skeleton"};
            return types[myRandom.nextInt(types.length)];
        }

        final String[] types = {"Ogre", "Gremlin", "Skeleton"};
        return types[myRandom.nextInt(types.length)];
    }

    /**
     * Checks if the room at the given position is a special room.
     * Special rooms include the entrance, exit, pillar rooms, and fountain rooms.
     * This is to check for these rooms so that certain content should not be placed
     * in them.
     *
     * @param theRow the row of the room
     * @param theCol the column of the room
     * @return true if the room is a special room, false otherwise
     */
    private boolean isSpecialRoom(final int theRow, final int theCol) {
        final Room room = myRooms[theRow][theCol];
        return room.isEntrance() || room.isExit() || room.hasPillar() || room.hasFountain();
    }

    /**
     * Connects the room at the given position to the room directly south of it
     * by setting their respective doors.
     *
     * @param theRow the row of the northern room
     * @param theCol the column of the northern room
     */
    private void connectSouth(final int theRow, final int theCol) {
        if (theRow >= myHeight - 1) return;
        myRooms[theRow][theCol].setSouthDoor(true);
        myRooms[theRow+1][theCol].setNorthDoor(true);
    }

    /**
     * Connects the room at the given position to the room directly east of it
     * by setting their respective doors.
     *
     * @param theRow the row of the western room
     * @param theCol the column of the western room
     */
    private void connectEast(final int theRow, final int theCol) {
        if (theCol >= myWidth - 1) return;
        myRooms[theRow][theCol].setEastDoor(true);
        myRooms[theRow][theCol+1].setWestDoor(true);
    }

    /**
     * Spawns a random monster in the hero's current room.
     * Used by fountain effects that have a chance to summon a monster.
     */
    public void spawnMonsterAtHero() {
        if (!myRooms[myHeroRow][myHeroCol].hasMonster()) {
            myRooms[myHeroRow][myHeroCol].setMonster(
                    myMonsterGenerator.createMonster(getRandomMonsterType(myHeroRow, myHeroCol)));
        }
    }

    /**
     * Attempts to move the hero in the given direction.
     *
     * @param theDirection the direction to move ("NORTH", "SOUTH", "EAST", "WEST")
     * @return true if the move was successful, false if no door exists that way
     */
    public boolean moveHero(final String theDirection){
        switch (theDirection.toUpperCase()){
            case "NORTH":
                if (!getCurrentRoom().hasNorthDoor() || myHeroRow <= 0) return false;
                myHeroRow--;
                break;
            case "SOUTH":
                if (!getCurrentRoom().hasSouthDoor() || myHeroRow >= myHeight - 1) return false;
                myHeroRow++;
                break;
            case "EAST":
                if (!getCurrentRoom().hasEastDoor() || myHeroCol >= myWidth - 1) return false;
                myHeroCol++;
                break;
            case "WEST":
                if (!getCurrentRoom().hasWestDoor() || myHeroCol <= 0) return false;
                myHeroCol--;
                break;
            default:
                return false;
        }
        getCurrentRoom().setRevealed(true);
        return true;
    }

    /**
     * Teleports the hero to a random room in the dungeon.
     * The hero cannot be teleported to their current room.
     */
    public void teleportHeroRandom() {
        int row;
        int col;
        do {
            row = myRandom.nextInt(myHeight);
            col = myRandom.nextInt(myWidth);
        } while (row == myHeroRow && col == myHeroCol);
        myHeroRow = row;
        myHeroCol = col;
        getCurrentRoom().setRevealed(true);
    }

    /**
     * Returns a list of directions the hero can currently move.
     * Only directions wih a door are included.
     *
     * @return a list of valid direction strings from the current room
     */
    public List<String> getValidDirections(){
        final List<String> validDirections = new ArrayList<>();
        final Room current = getCurrentRoom();

        if (current.hasNorthDoor()) validDirections.add("NORTH");
        if (current.hasSouthDoor()) validDirections.add("SOUTH");
        if (current.hasEastDoor()) validDirections.add("EAST");
        if (current.hasWestDoor()) validDirections.add("WEST");

        return validDirections;
    }

    /**
     * Triggers the alarm event by locking onto the nearest non-pillar monster
     * and moving it toward the hero each turn.
     * If no monster exists one will be spawned in an adjacent room.
     */
    public void triggerAlarm() {
        if (myAlertedMonsterRow == -1) {
            final int[] nearest = findNearestMonster();
            if (nearest != null) {
                myAlertedMonsterRow = nearest[0];
                myAlertedMonsterCol = nearest[1];
            } else {
                // no monster was found, spawn one in an adjacent room
                final int[][] neighbors = {
                        {myHeroRow - 1, myHeroCol},
                        {myHeroRow + 1, myHeroCol},
                        {myHeroRow, myHeroCol + 1},
                        {myHeroRow, myHeroCol - 1},
                };
                for (final int[] neighbor : neighbors) {
                    final int nr = neighbor[0];
                    final int nc = neighbor[1];
                    if (isInBounds(nr, nc) && !myRooms[nr][nc].hasMonster()
                            && !myRooms[nr][nc].isEntrance() && !myRooms[nr][nc].isExit()
                            && !myRooms[nr][nc].hasPillar()) {
                        myRooms[nr][nc].setMonster(
                                myMonsterGenerator.createMonster(getRandomMonsterType(nr, nc))
                        );
                        myAlertedMonsterRow = nr;
                        myAlertedMonsterCol = nc;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Reveals the location of a random pillar on the dungeon map
     * that has not yet been collected by the hero.
     * Does nothing if no pillars remain.
     */
    public void revealRandomPillar() {
        final List<int[]> pillarRooms = new ArrayList<>();
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (myRooms[row][col].hasPillar()) {
                    pillarRooms.add(new int[]{row, col});
                }
            }
        }
        if (!pillarRooms.isEmpty()) {
            final int[] pillar = pillarRooms.get(myRandom.nextInt(pillarRooms.size()));
            myRooms[pillar[0]][pillar[1]].setRevealed(true);
        }
    }

    /**
     * Find the nearest non-pillar monster to the hero.
     * Helper for triggerAlarm().
     *
     * @return the row and column of the nearest monster as an int array
     *         or null if no monsters exist.
     */
    private int[] findNearestMonster() {
        int[] nearest = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (myRooms[row][col].hasMonster() && !myRooms[row][col].hasPillar()) {
                    final int distance = Math.abs(row - myHeroRow) + Math.abs(col - myHeroCol);
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        nearest = new int[]{row, col};
                    }
                }
            }
        }
        return nearest;
    }

    /**
     * Moves the alerted monster one step closer to the hero each turn
     * using BFS to pathfind through open doors.
     */
    public void moveAlarmMonsterCloser() {
        if (myAlertedMonsterRow == -1) return;

        // don't move if the monster is already in the hero's room
        if (myAlertedMonsterRow == myHeroRow &&  myAlertedMonsterCol == myHeroCol) return;

        // check if monster was defeated
        if (!myRooms[myAlertedMonsterRow][myAlertedMonsterCol].hasMonster()) {
            myAlertedMonsterRow = -1;
            myAlertedMonsterCol = -1;
            return;
        }

        final int[] nextStep = getNextStepTowardHero(myAlertedMonsterRow, myAlertedMonsterCol);
        if (nextStep == null) return;

        if (!myRooms[nextStep[0]][nextStep[1]].hasMonster()) {
            final Monster monster = myRooms[myAlertedMonsterRow][myAlertedMonsterCol].getMonster();
            myRooms[myAlertedMonsterRow][myAlertedMonsterCol].removeMonster();
            myRooms[nextStep[0]][nextStep[1]].setMonster(monster);
            myAlertedMonsterRow = nextStep[0];
            myAlertedMonsterCol = nextStep[1];
        }
    }

    /**
     * Uses BFS to find the next step the alarm monster should take toward the hero
     * following only rooms connected by open doors.
     * Helper for moveAlarmMonsterCloser().
     *
     * @param theMonsterRow the row of the monster
     * @param theMonsterCol the column of the monster
     * @return the next step toward the hero as an int array
     *         or null if no path exists
     */
    private int[] getNextStepTowardHero(final int theMonsterRow, final int theMonsterCol) {
        if (theMonsterRow == myHeroRow && theMonsterCol == myHeroCol) return null;

        final boolean[][] visited = new boolean[myHeight][myWidth];
        final Map<String, int[]> parent = new HashMap<>();
        final Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{theMonsterRow, theMonsterCol});
        visited[theMonsterRow][theMonsterCol] = true;

        while(!queue.isEmpty()) {
            final int[] current = queue.poll();
            final int r = current[0];
            final int c = current[1];
            final Room room =  myRooms[r][c];

            final int[][] neighbors = {
                    {r-1, c}, // north
                    {r+1, c}, // south
                    {r, c+1}, // east
                    {r, c-1} // west
            };

            final boolean[] doors = {
                    room.hasNorthDoor(),
                    room.hasSouthDoor(),
                    room.hasEastDoor(),
                    room.hasWestDoor()
            };

            for (int i = 0; i < 4; i++) {
                final int nr = neighbors[i][0];
                final int nc = neighbors[i][1];

                if (isInBounds(nr, nc) && !visited[nr][nc] && doors[i]) {
                    visited[nr][nc] = true;
                    parent.put(nr + "," + nc, new int[]{r, c});

                    if (nr == myHeroRow && nc == myHeroCol) {
                        // trace back to find first step
                        int[] step = new int[]{nr, nc};
                        while (true) {
                            final int[] prev = parent.get(step[0] + "," + step[1]);
                            if (prev[0] == theMonsterRow && prev[1] == theMonsterCol) {
                                return step;
                            }
                            step = prev;
                        }
                    }
                    queue.add(new int[]{nr, nc});
                }
            }
        }
        return null;
    }

    /**
     * Returns the room the hero is currently in.
     *
     * @return the current room
     */
    public Room getCurrentRoom() {
        return myRooms[myHeroRow][myHeroCol];
    }

    /**
     * Returns the row index of the hero's current position.
     *
     * @return the hero's row index
     */
    public int getHeroRow() {
        return myHeroRow;
    }

    /**
     * Returns the column index of the hero's current position.
     *
     * @return the hero's column index
     */
    public int getHeroCol() {
        return myHeroCol;
    }

    /**
     * Returns the row index of the alerted monster's current position.
     * Returns -1 if no monster is currently alerted.
     *
     * @return the alerted monster's row index, or -1 if none
     */
    public int getAlertedMonsterRow() {
        return myAlertedMonsterRow;
    }

    /**
     * Returns the column index of the alerted monster's current position.
     * Returns -1 if no monster is currently alerted.
     *
     * @return the alerted monster's column index, or -1 if none
     */
    public int getAlertedMonsterCol() {
        return myAlertedMonsterCol;
    }

    /**
     * Returns the row index of the dungeon entrance.
     *
     * @return the entrance row index
     */
    public int getMyEntranceRow() {
        return myEntranceRow;
    }

    /**
     * Returns the column index of the dungeon entrance.
     *
     * @return the entrance column index
     */
    public int getMyEntranceCol() {
        return myEntranceCol;
    }

    /**
     * Returns the row index of the dungeon exit.
     *
     * @return the exit row index
     */
    public int getMyExitRow() {
        return myExitRow;
    }

    /**
     * Returns the column index of the dungeon exit.
     *
     * @return the exit column index
     */
    public int getMyExitCol() {
        return myExitCol;
    }

    /**
     * Returns the number of columns in the dungeon.
     *
     * @return the dungeon width
     */
    public int getDungeonWidth() {
        return myWidth;
    }

    /**
     * Returns the number of rows in the dungeon.
     *
     * @return the dungeon height
     */
    public int getDungeonHeight() {
        return myHeight;
    }

    /**
     * Sets the hero's position in the dungeon.
     * 
     * @param theRow the new row position
     * @param theCol the new column position
     */
    public void setHeroPosition(final int theRow, final int theCol) {
        if (theRow >= 0 && theRow < myHeight && theCol >= 0 && theCol < myWidth) {
            myHeroRow = theRow;
            myHeroCol = theCol;
        }
    }

    /**
     * Returns true or false if the hero is in the exit room or not.
     *
     * @return true if the hero is in the exit room, false otherwise
     */
    public boolean isExitReached() {
        return getCurrentRoom().isExit();
    }

    /**
     * Returns the room at the given row and column.
     *
     * @return the room at the given row and column
     */
    public Room getRoom(final int theRow, final int theCol) {
        return myRooms[theRow][theCol];
    }

    /**
     * Reveals the contents of the rooms surrounding the current room.
     * Sets the surrounding rooms revealed as true.
     */
    public void revealSurroundingRooms() {
        for (int row = myHeroRow - 1; row <= myHeroRow + 1; row++) {
            for (int col = myHeroCol - 1; col <= myHeroCol + 1; col++) {
                if (isInBounds(row, col)) {
                    myRooms[row][col].setRevealed(true);
                }
            }
        }
    }

    /**
     * returns whether the given position is within the bounds of the dungeon.
     *
     * @param theRow the row to check
     * @param theCol the column to check
     * @return true if the position is within bounds, false otherwise
     */
    private boolean isInBounds(final int theRow, final int theCol) {
        return theRow >= 0 && theRow < myHeight && theCol >= 0 && theCol < myWidth;
    }

    /**
     * Returns a string representation of the dungeon, hiding unrevealed rooms.
     *
     * @return the dungeon as a formatted string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int row = 0; row < myHeight; row++) {

            final StringBuilder top = new StringBuilder();
            final StringBuilder mid = new StringBuilder();
            final StringBuilder bot = new StringBuilder();

            for (int col = 0; col < myWidth; col++) {

                if (!myRooms[row][col].isRevealed()) {
                    top.append("??? ");
                    mid.append("??? ");
                    bot.append("??? ");
                    continue;
                }

                final String[] parts = myRooms[row][col].toString().split("\n");

                if (row == myHeroRow && col == myHeroCol) {
                    parts[1] = parts[1].charAt(0) + "@" + parts[1].charAt(2);
                }

                top.append(parts[0]).append(" ");
                mid.append(parts[1]).append(" ");
                bot.append(parts[2]).append(" ");
            }

            sb.append(top).append("\n");
            sb.append(mid).append("\n");
            sb.append(bot).append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns a string representation of the entire dungeon revealed.
     *
     * @return the fully revealed dungeon as a formatted string
     */
    public String toStringFullDungeon() {
        final StringBuilder sb = new StringBuilder();

        for (int row = 0; row < myHeight; row++) {

            final StringBuilder top = new StringBuilder();
            final StringBuilder mid = new StringBuilder();
            final StringBuilder bot = new StringBuilder();

            for (int col = 0; col < myWidth; col++) {
                final String[] parts = myRooms[row][col].toString().split("\n");

                top.append(parts[0]).append(" ");
                mid.append(parts[1]).append(" ");
                bot.append(parts[2]).append(" ");
            }

            sb.append(top).append("\n");
            sb.append(mid).append("\n");
            sb.append(bot).append("\n");
        }

        return sb.toString();
    }

    /**
     * Returns the name of the dungeon.
     *
     * @return the dungeon name
     */
    public String getMyName() {
        return myName;
    }

    /**
     * Sets where the entrance is in the dungeon.
     * 
     * @param theRow the row index
     * @param theCol the column index
     */
    public void setEntrancePosition(final int theRow, final int theCol) {
        myEntranceRow = theRow;
        myEntranceCol = theCol;
    }

    /**
     * Sets where the exit is in the dungeon.
     * 
     * @param theRow the row index
     * @param theCol the column index
     */
    public void setExitPosition(final int theRow, final int theCol) {
        myExitRow = theRow;
        myExitCol = theCol;
    }

}
