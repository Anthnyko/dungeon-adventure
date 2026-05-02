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
 * @version 1.0
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

    private Random myRandom;

    /**
     * Constructs a new dungeon given the dimensions and name then
     * generates a valid maze.
     *
     * @param width the number of columns in the dungeon
     * @param height the number of rows in the dungeon
     * @param name the name of the dungeon
     */
    public Dungeon(int width, int height, String name) {
        myRandom = new Random();
        myWidth = width;
        myHeight = height;
        myName = name;

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

            //Pick exit
            do {
                myExitRow = myRandom.nextInt(myHeight);
                myExitCol = myRandom.nextInt(myWidth);
            } while (myExitRow == myEntranceRow && myExitCol == myEntranceCol);
            myRooms[myExitRow][myExitCol].setExit();

            addRandomPaths();

        } while (!isValidMaze());

        placePillars();
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

    private void placePillars(){
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

    private boolean isSpecialRoom(int row, int col) {
        Room room = myRooms[row][col];
        return room.isEntrance() || room.isExit() || room.hasPillar();
    }

    // to prevent door mismatches between rooms
    private void connectSouth(int row, int col) {
        if (row >= myHeight - 1) return;
        myRooms[row][col].setSouthDoor(true);
        myRooms[row+1][col].setNorthDoor(true);
    }
    private void connectEast(int row, int col) {
        if (col >= myWidth - 1) return;
        myRooms[row][col].setEastDoor(true);
        myRooms[row][col+1].setWestDoor(true);
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
                if (!getCurrentRoom().hasNorthDoor()) return false;
                myHeroRow--;
                break;
            case "SOUTH":
                if (!getCurrentRoom().hasSouthDoor()) return false;
                myHeroRow++;
                break;
            case "EAST":
                if (!getCurrentRoom().hasEastDoor()) return false;
                myHeroCol++;
                break;
            case "WEST":
                if (!getCurrentRoom().hasWestDoor()) return false;
                myHeroCol--;
                break;
            default:
                return false;
        }
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
     * Returns a string representation of the entire dungeon.
     *
     * @return the full dungeon as a formatted string
     */
    @Override
    public String toString() {
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

}
