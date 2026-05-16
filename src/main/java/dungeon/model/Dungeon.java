package dungeon.model;

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

    /** The name of the dungeon */
    private final String myName;

    private final MonsterGenerator myMonsterGenerator;

    private Random myRandom;

    /**
     * Constructs a new dungeon given the dimensions and name then
     * generates a valid maze.
     *
     * @param theWidth the number of columns in the dungeon
     * @param theHeight the number of rows in the dungeon
     * @param theName the name of the dungeon
     */
    public Dungeon(int theWidth, int theHeight, String theName) {
        myRandom = new Random();
        myMonsterGenerator = new MonsterGenerator();
        myWidth = theWidth;
        myHeight = theHeight;
        myName = theName;

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

        for (char pillar : pillars) {
            int row, col;
            do {
                row = myRandom.nextInt(myHeight);
                col = myRandom.nextInt(myWidth);
            } while (isSpecialRoom(row, col));

            myRooms[row][col].setPillar(pillar);
        }
    }

    private void placeFountains() {
        int count = 2; //for now just have 2 fountains in the dungeon

        for (int i = 0; i < count; i++) {
            int row, col;
            do {
                row = myRandom.nextInt(myHeight);
                col = myRandom.nextInt(myWidth);
            } while (isSpecialRoom(row, col));

            myRooms[row][col].setFountain(true);
        }
    }

    private void placeMonsters() {
        for (int row = 0; row < myHeight; row++) {
            for (int col = 0; col < myWidth; col++) {
                Room room = myRooms[row][col];

                if (room.isEntrance() || room.isExit()) continue;

                if (room.hasPillar()) {
                    room.setMonster(myMonsterGenerator.createMonster("Pillar Guardian"));
                    continue;
                }

                if (room.hasFountain()) {
                    if (myRandom.nextInt(100) < 50) { // 50% chance for monster spawn in fountain room
                        room.setMonster(myMonsterGenerator.createMonster(getRandomMonsterType()));
                    }
                }

                if (myRandom.nextInt(100) < 30) { // 30% chance to spawn monster in room
                    room.setMonster(myMonsterGenerator.createMonster(getRandomMonsterType()));
                }
            }
        }
    }

    private String getRandomMonsterType() {
        String[] types = {"Ogre", "Gremlin", "Skeleton"};
        return  types[myRandom.nextInt(types.length)];
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

    /**
     * Returns the room the hero is currently in.
     *
     * @return the current room
     */
    public Room getCurrentRoom() {
        return myRooms[myHeroRow][myHeroCol];
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

    public String getMyName() {
        return myName;
    }
}
