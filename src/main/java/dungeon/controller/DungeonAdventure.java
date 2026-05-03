
package dungeon.controller;

import java.util.Scanner;

import dungeon.model.Dungeon;
import dungeon.model.Room;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Monster;
import dungeon.model.characters.Priest;
import dungeon.model.characters.Thief;
import dungeon.model.characters.Warrior;
import dungeon.view.DungeonView;

/**
 * 
 * @author Jackson Steger
 * @version 1.0
 */
public final class DungeonAdventure {

    // private field for Dungeon
    private Dungeon myDungeon;

    // private field for DungeonView
    private DungeonView myDungeonView;

    // private field for Hero
    private Hero myHero;

    private Scanner myScanner = new Scanner(System.in);

    private boolean myActiveGame;
    private boolean myWinCondition;

    public static void main(String[] args) {
        DungeonAdventure game = new DungeonAdventure();
        game.createGame();
    }

    DungeonAdventure() {
        // Default constructor
    }

    /**
     * Starts the game by initializing the game setup and running the main game loop.
     * Orchestrates the overall flow of the dungeon adventure game.
     */
    private final void createGame() {
        // call gameSetup
        gameSetup();
        // call gameLoop
        gameLoop();
        // call endGame
        endGame(myWinCondition);
    }

    /**
     * Initializes the game setup including hero creation, dungeon initialization,
     * and any necessary game state preparation.
     */
    private final void gameSetup() {
        
        myDungeonView.displayIntro();
        
        // Validate player class selection
        String heroChoice;
        boolean validChoice = false;
        do {
            myDungeonView.displayHeroSelection();
            heroChoice = myScanner.nextLine().trim();
            
            if("1".equals(heroChoice) || "2".equals(heroChoice) || "3".equals(heroChoice)) {
                validChoice = true;
            } else {
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }
        } while (!validChoice);

        // Set the player's name with a max 15 characters
        String heroName;
        boolean validName = false;
        do {
            myDungeonView.promptName();
            heroName = myScanner.nextLine().trim();
            
            if(heroName.length() > 15) {
                System.out.println("Name too long. Please enter a name with a max of 15 characters.");
            } else if(heroName.isEmpty()) {
                System.out.println("Name cannot be empty. Please enter a valid name.");
            } else {
                validName = true;
            }
        } while (!validName);

        // Set the player's class
        if("1".equals(heroChoice)) {
            myHero = new Warrior(heroName);
        } else if("2".equals(heroChoice)) {
            myHero = new Priest(heroName);
        } else if("3".equals(heroChoice)) {
            myHero = new Thief(heroName);
        }
        
    }

    /**
     * Executes the main game loop, processing player turns and game events until
     * the game reaches a terminal state (win or loss).
     */
    private final void gameLoop() {

        myActiveGame = true;
        myWinCondition = false;

        while(myActiveGame) {
            myDungeonView.displayPlayerStatus(myHero);
            myDungeonView.displayRoom(myDungeon.getCurrentRoom());
            evaluateRoomEvents(myDungeon.getCurrentRoom());
            myDungeonView.displayInGameMenu();

            String userInput = myScanner.nextLine().trim().toUpperCase();

            if(!userInput.isEmpty()) {
                char userChoice = userInput.charAt(0);
                handlePlayerChoice(userChoice);
            }

            if(checkWinCondidtion() || myHero.getHP() <= 0) {
                myActiveGame = false;
            }
        }
    }

    /**
     * Evaluates and applies the effects of the current room on the player,
     * such as finding items, encountering traps, or healing. 
     * 
     * @param room the room to evaluate
     */
    private final void evaluateRoomEvents(Room theRoom) { //parameter/s: room

    }

    /**
     * Processes the player's input choice and executes the corresponding action.
     * 
     * @param theUserChoice the character representing the player's choice
     */
    private final void handlePlayerChoice(char theUserChoice) {

        if(theUserChoice == 'W' || theUserChoice == 'A' 
        || theUserChoice == 'S' || theUserChoice == 'D') {
            move(theUserChoice);
        } else if(theUserChoice == 'H') {
            myHero.useHealingPotion();
        } else if(theUserChoice == 'V') {
            myHero.useVisionPotion();
        } else if(theUserChoice == 'B') {
            // TODO: implement help menu
        } else if(theUserChoice == 'Q') {
            saveGame();
            myActiveGame = false;
        }
        
    }

    /**
     * Handles the player's movement within the dungeon, updating position and
     * evaluating effects of the new room.
     */
    private final void move(char theDirection) {
        if(theDirection == 'W') {
            myDungeon.moveHero("NORTH");
        } else if(theDirection == 'A') {
            myDungeon.moveHero("EAST");
        } else if(theDirection == 'S') {
            myDungeon.moveHero("SOUTH");
        } else {
            myDungeon.moveHero("WEST");
        }
    }

    /**
     * Manages combat between the hero and an encountered monster, including
     * attack resolution and outcome determination.
     * 
     * @param monster the enemy monster to engage in combat
     */
    private final void handleCombat(Monster theMonster) {
        // TODO: add combat
    }

    /**
     * Checks if the win condition has been met (reaching the exit
     * with all 4 pillars).
     */
    private final boolean checkWinCondidtion() {
        return myWinCondition;
    }

    /**
     * Finalizes the game, displaying the outcome and cleaning up resources.
     * 
     * @param theResult true if the player won, false if the player lost
     */
    private final void endGame(boolean theResult) {
        if(theResult == true) {
            myDungeonView.displayWin();
        } else {
            myDungeonView.displayLoss();
        }
        myDungeonView.displayPlayerStatus(myHero);
        myDungeonView.displayFullDungeon(myDungeon);
    }

    private final void gameMenu() {

    }

    /**
     * Saves the game
     */
    private final void saveGame() {

    }

    /**
     * Loads a saved game
     */
    private final void loadGame() {

    }


}
