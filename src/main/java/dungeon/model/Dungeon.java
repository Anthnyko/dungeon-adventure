package dungeon.model;

import dungeon.model.RoomEvent.AlarmEvent;
import dungeon.model.RoomEvent.FountainEvent;
import dungeon.model.RoomEvent.PitEvent;
import dungeon.model.RoomEvent.PoisonEvent;
import dungeon.model.characters.Monster;

import java.util.*;

/**
 * Represents a randomly generated dungeon maze.
 *
 * The dungeon is a 2D grid of room objects connected by doors.
 * The dungeon class is responsible for:
 * Generating the maze,
 * Placing the entrance, exit and four pillars,
 * Keeping track of the hero
 *
 * @author Ibrahim Mohamud
 * @version 1.1
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
    public Dungeon(int theWidth, int theHeight, String theName, final boolean forLoad) {
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
            intiializeRoomsForLoad();
        }
    }

    /**
     * Generates a new random layout of the dungeon.
     */
    public void generateDungeon() {
        mazeGeneration();
    }

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
    private void intiializeRoomsForLoad() {
        myRooms = new Room[myHeight][myWidth];
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                myRooms[row][col] = new Room(true);
            }
        }
    }

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

    private boolean isValidMaze() {
        boolean[][] visited = new boolean[myHeight][myWidth];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{myEntranceRow, myEntranceCol});
        visited[myEntranceRow][myEntranceCol] = true;

        //Check for valid path from entrance to exit
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0],  c = current[1];
            Room room = myRooms[r][c];

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

    private void placePillars() {
        char[] pillars = {'A', 'E', 'I', 'P'};
        final int minDistance = (myWidth + myHeight) / 4; //pillars minimum distance from entrance based on dungeon size

        for (char pillar : pillars) {
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
    //private helper for placePillars()
    private boolean hasAdjacentPillar(int theRow, int theCol) {
        Room room = myRooms[theRow][theCol];
        return (isInBounds(theRow - 1, theCol) && myRooms[theRow - 1][theCol].hasPillar() && room.hasNorthDoor()) || // north
                (isInBounds(theRow + 1, theCol) && myRooms[theRow + 1][theCol].hasPillar() && room.hasSouthDoor()) || // south
                (isInBounds(theRow, theCol + 1) && myRooms[theRow][theCol + 1].hasPillar() && room.hasEastDoor())  || // east
                (isInBounds(theRow, theCol - 1) && myRooms[theRow][theCol - 1].hasPillar() && room.hasWestDoor());   // west
    }

    private void placePits() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new PitEvent());
                }
            }
        }
    }

    private void placePoisons() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new PoisonEvent());
                }
            }
        }
    }

    private void placeAlarms() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (myRandom.nextInt(100) < EVENT_CHANCE) {
                    room.addEvent(new AlarmEvent());
                }
            }
        }
    }



    private void placeFountains() {
        int count = (myWidth * myHeight) / 10; // around 1 fountain per 10 rooms

        for (int i = 0; i < count; i++) {
            int row, col;
            do {
                row = myRandom.nextInt(myHeight);
                col = myRandom.nextInt(myWidth);
            } while (isSpecialRoom(row, col));

            myRooms[row][col].addEvent(new FountainEvent());
        }
    }

    private void placeMonsters() {
        int monstersPlaced = 0;

        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (monstersPlaced >= myMaxMonsters) return;

                Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (room.hasPillar()) {
                    room.setMonster(myMonsterGenerator.createMonster("Pillar Guardian"));
                    continue;
                }

                if (myRandom.nextInt(100) < 30) { // 30% chance to spawn monster in room
                    room.setMonster(myMonsterGenerator.createMonster(getRandomMonsterType(row, col)));
                    monstersPlaced++;
                }
            }
        }
    }

    private String getRandomMonsterType(int theRow, int theCol) {
        int entranceDistance = Math.abs(theRow - myEntranceRow) + Math.abs(theCol - myEntranceCol);
        int minDistance = (myWidth + myHeight) / 4; //ogre minimum distance from entrance based on dungeon size

        if (entranceDistance <= minDistance) {
            String[] types = {"Gremlin", "Skeleton"};
            return types[myRandom.nextInt(types.length)];
        }

        String[] types = {"Ogre", "Gremlin", "Skeleton"};
        return types[myRandom.nextInt(types.length)];
    }

    private boolean isSpecialRoom(int theRow, int theCol) {
        Room room = myRooms[theRow][theCol];
        return room.isEntrance() || room.isExit() || room.hasPillar() || room.hasFountain();
    }

    // to prevent door mismatches between rooms
    private void connectSouth(int theRow, int theCol) {
        if (theRow >= myHeight - 1) return;
        myRooms[theRow][theCol].setSouthDoor(true);
        myRooms[theRow+1][theCol].setNorthDoor(true);
    }
    private void connectEast(int theRow, int theCol) {
        if (theCol >= myWidth - 1) return;
        myRooms[theRow][theCol].setEastDoor(true);
        myRooms[theRow][theCol+1].setWestDoor(true);
    }

    public void spawnMonsterAtHero() {
        if (!myRooms[myHeroRow][myHeroCol].hasMonster()) {
            myRooms[myHeroRow][myHeroCol].setMonster(
                    myMonsterGenerator.createMonster(getRandomMonsterType(myHeroRow, myHeroCol)));;
        }
    }

    /**
     * Attempts to move the hero in the given direction.
     *
     * @param theDirection the direction to move ("NORTH", "SOUTH", "EAST", "WEST")
     * @return true if the move was successful, false if no door exists that way
     */
    public boolean moveHero(String theDirection){
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
        List<String> validDirections = new ArrayList<>();
        Room current = getCurrentRoom();

        if (current.hasNorthDoor()) validDirections.add("NORTH");
        if (current.hasSouthDoor()) validDirections.add("SOUTH");
        if (current.hasEastDoor()) validDirections.add("EAST");
        if (current.hasWestDoor()) validDirections.add("WEST");

        return validDirections;
    }

    public void triggerAlarm() {
        if (myAlertedMonsterRow == -1) {
            int[] nearest = findNearestMonster();
            if (nearest != null) {
                myAlertedMonsterRow = nearest[0];
                myAlertedMonsterCol = nearest[1];
            }
        }
    }

    public void revealRandomPillar() {
        List<int[]> pillarRooms = new ArrayList<>();
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (myRooms[row][col].hasPillar()) {
                    pillarRooms.add(new int[]{row, col});
                }
            }
        }
        if (!pillarRooms.isEmpty()) {
            int[] pillar = pillarRooms.get(myRandom.nextInt(pillarRooms.size()));
            myRooms[pillar[0]][pillar[1]].setRevealed(true);
        }
    }

    private int[] findNearestMonster() {
        int[] nearest = null;
        int shortestDistance = Integer.MAX_VALUE;

        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                if (myRooms[row][col].hasMonster() && !myRooms[row][col].hasPillar()) {
                    int distance = Math.abs(row - myHeroRow) + Math.abs(col - myHeroCol);
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        nearest = new int[]{row, col};
                    }
                }
            }
        }
        return nearest;
    }

    private int[] getNextStepTowardHero(final int theMonsterRow, final int theMonsterCol) {
        if (theMonsterRow == myHeroRow && theMonsterCol == myHeroCol) return null;

        boolean[][] visited = new boolean[myHeight][myWidth];
        Map<String, int[]> parent = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{theMonsterRow, theMonsterCol});
        visited[theMonsterRow][theMonsterCol] = true;

        while(!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            Room room =  myRooms[r][c];

            int[][] neighbors = {
                    {r-1, c}, // north
                    {r+1, c}, // south
                    {r, c+1}, // east
                    {r, c-1} // west
            };

            boolean[] doors = {
                    room.hasNorthDoor(),
                    room.hasSouthDoor(),
                    room.hasEastDoor(),
                    room.hasWestDoor()
            };

            for (int i = 0; i < 4; i++) {
                int nr = neighbors[i][0];
                int nc = neighbors[i][1];

                if (isInBounds(nr, nc) && !visited[nr][nc] && doors[i]) {
                    visited[nr][nc] = true;
                    parent.put(nr + "," + nc, new int[]{r, c});

                    if (nr == myHeroRow && nc == myHeroCol) {
                        // trace back to find first step
                        int[] step = new int[]{nr, nc};
                        while (true) {
                            int[] prev = parent.get(step[0] + "," + step[1]);
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

        int[] nextStep = getNextStepTowardHero(myAlertedMonsterRow, myAlertedMonsterCol);
        if (nextStep == null) return;

        if (!myRooms[nextStep[0]][nextStep[1]].hasMonster()) {
            Monster monster = myRooms[myAlertedMonsterRow][myAlertedMonsterCol].getMonster();
            myRooms[myAlertedMonsterRow][myAlertedMonsterCol].removeMonster();
            myRooms[nextStep[0]][nextStep[1]].setMonster(monster);
            myAlertedMonsterRow = nextStep[0];
            myAlertedMonsterCol = nextStep[1];
        }
    }

    /**
     * Returns the room the hero is currently in.
     *
     * @return the current room
     */
    public Room getCurrentRoom() {
        return myRooms[myHeroRow][myHeroCol];
    }

    public int getHeroRow() {
        return myHeroRow;
    }

    public int getHeroCol() {
        return myHeroCol;
    }

    public int getMyEntranceRow() {
        return myEntranceRow;
    }

    public int getMyEntranceCol() {
        return myEntranceCol;
    }

    public int getMyExitRow() {
        return myExitRow;
    }

    public int getMyExitCol() {
        return myExitCol;
    }

    public int getDungeonWidth() {
        return myWidth;
    }

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
     * Returns true or false if the hero is in a pillar room or not.
     *
     * @return true if the hero is in a pillar room, false otherwise
     */
    public boolean isPillarReached() {
        return getCurrentRoom().hasPillar();
    }

    /**
     * Returns the room at the given row and column.
     *
     * @return the room at the given row and column
     */
    public Room getRoom(int theRow, int theCol) {
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
    //helper for getSurroundingRooms()
    private boolean isInBounds(int theRow, int theCol) {
        return theRow >= 0 && theRow < myHeight && theCol >= 0 && theCol < myWidth;
    }

    /**
     * Returns a string representation of the dungeon, hiding unrevealed rooms.
     *
     * @return the dungeon as a formatted string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < myHeight; row++) {

            StringBuilder top = new StringBuilder();
            StringBuilder mid = new StringBuilder();
            StringBuilder bot = new StringBuilder();

            for (int col = 0; col < myWidth; col++) {

                if (!myRooms[row][col].isRevealed()) {
                    top.append("??? ");
                    mid.append("??? ");
                    bot.append("??? ");
                    continue;
                }

                String[] parts = myRooms[row][col].toString().split("\n");

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
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < myHeight; row++) {

            StringBuilder top = new StringBuilder();
            StringBuilder mid = new StringBuilder();
            StringBuilder bot = new StringBuilder();

            for (int col = 0; col < myWidth; col++) {
                String[] parts = myRooms[row][col].toString().split("\n");

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
     * @return the name of the dungeon.
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
