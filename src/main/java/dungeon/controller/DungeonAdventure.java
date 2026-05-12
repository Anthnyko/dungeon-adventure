
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
    private final DungeonView myDungeonView;

    // private field for Hero
    private Hero myHero;

    private final Scanner myScanner = new Scanner(System.in);

    private boolean myActiveGame;
    private boolean myWinCondition;

    public static void main(final String[] args) {
        final DungeonAdventure game = new DungeonAdventure();
        game.mainMenu();
    }

    public DungeonAdventure() {
        myDungeonView = new DungeonView();
        // Default constructor
    }

    /**
     * Starts the game by initializing the game setup and running the main game loop.
     * Orchestrates the overall flow of the dungeon adventure game.
     */
    private final void createGame() {
        gameSetup();
        gameLoop();
        endGame(myWinCondition);
    }

    /**
     * Initializes the game setup including hero creation, dungeon initialization,
     * and any necessary game state preparation.
     */
    private final void gameSetup() {
        dungeonDifficultyChoice();
        setMyHero(classChoice(), nameChoice());
        myDungeonView.displayIntro(myDungeon);
    }

    private final void mainMenu() {
        int playerChoice = -1;
        myDungeonView.displayMainMenu();
        while (playerChoice < 1 || playerChoice > 5) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 5) {
                    System.out.println("Invalid input. Please enter a choice between 1-5.");
                }
            } catch (final NumberFormatException e){
                System.out.println("Invalid input. please enter a number between 1-5");
            }
        }

        switch (playerChoice) {
            case 1 -> createGame();
            case 2 -> loadGame(); // TODO: add load game
            case 3 -> helpPage(); // help menu
            case 4 -> aboutPage(); // about info
            case 5 -> closeGame(); // quit game
            default -> throw new IllegalArgumentException("Invlid choice: " + playerChoice);
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
            evaluateRoomEvents(myDungeon.getCurrentRoom());
            myDungeonView.displayPlayerStatus(myHero);
            myDungeonView.displayRoom(myDungeon.getCurrentRoom());
            myDungeonView.displayDungeon(myDungeon);
            myDungeonView.displayInGameMenu();

            final String userInput = myScanner.nextLine().trim().toUpperCase();

            if(!userInput.isEmpty()) {
                final char userChoice = userInput.charAt(0);
                handlePlayerChoice(userChoice);
            }

            if(checkWinCondidtion() || myHero.getHP() <= 0) {
                myActiveGame = false;
            }
        }
    }

    private final int classChoice() {
        // Validate player class selection
        int playerChoice = -1;
        while (playerChoice < 1 || playerChoice > 3) {
            try {
                myDungeonView.promptHeroSelection();
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 3) {
                    System.out.println("Invalid input: Please enter a choice between 1-3.");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Invalid input: Please enter a number between 1-3.");
            }
        } 
        return playerChoice;
    }

    private final String nameChoice() {
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
        return heroName;
    }

    public void setMyHero(int thePlayerChoice, String theHeroName) {
        myHero = switch (thePlayerChoice) {
            case 1 -> new Warrior(theHeroName);
            case 2 -> new Priest(theHeroName);
            case 3 -> new Thief(theHeroName);
            default -> throw new IllegalArgumentException("Invalid class choice: " + thePlayerChoice);
        }; 
    }

    private final void dungeonDifficultyChoice() {
        int playerChoice = -1;
        while (playerChoice < 1 || playerChoice > 3) {
            try {
                myDungeonView.promptDungeonDifficulty();
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 3) {
                    System.out.println("Invalid input: Please enter a choice between 1-3.");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Invalid input: Please enter a number between 1-3.");
            }
        }
        myDungeon = switch (playerChoice) {
            case 1 -> new Dungeon(5, 5, "The Easy Dungeon");
            case 2 -> new Dungeon(7, 7, "The Medium Dungeon");
            case 3 -> new Dungeon(10, 10, "The Hard Dungeon");
            default -> throw new IllegalArgumentException("Invalid dungeon selection: " + playerChoice);
        };
    }

    /**
     * Evaluates and applies the effects of the current room on the player,
     * such as finding items, encountering traps, or finding a monster. 
     * 
     * @param room the room to evaluate
     */
    private void evaluateRoomEvents(final Room theRoom) { //parameter/s: room
        if (theRoom.hasMonster()) {
            handleCombat(theRoom.getMonster());
        }
        if (theRoom.hasPit()) {
            theRoom.triggerPitDamage(myHero);
        }
        if (theRoom.hasFountain()) {
            theRoom.activateFountainHeal(myHero);
        } 
        if (theRoom.hasItems()) {
            theRoom.pickUpItems();
        } 
        if (theRoom.hasPillar()) {
            theRoom.pickUpPillar();
        } 
    }

    /**
     * Processes the player's input choice and executes the corresponding action.
     * 
     * @param thePlayerChoice the character representing the player's choice
     */
    private final void handlePlayerChoice(final char thePlayerChoice) {

        if(thePlayerChoice == 'W' || thePlayerChoice == 'A' 
        || thePlayerChoice == 'S' || thePlayerChoice == 'D') {
            move(thePlayerChoice);
        } else if(thePlayerChoice == 'H') {
            myHero.useHealingPotion();
        } else if(thePlayerChoice == 'V') {
            myHero.useVisionPotion(myDungeon);
        } else if(thePlayerChoice == 'B') {
            // TODO: implement help menu
        } else if(thePlayerChoice == 'Q') {
            saveGame();
            myActiveGame = false;
        }
        
    }

    /**
     * Handles the player's movement within the dungeon, updating position and
     * evaluating effects of the new room.
     */
    private final void move(final char theDirection) {
        switch (theDirection) {
            case 'W' -> myDungeon.moveHero("NORTH");
            case 'A' -> myDungeon.moveHero("WEST");
            case 'S' -> myDungeon.moveHero("SOUTH");
            case 'D' -> myDungeon.moveHero("EAST");
            default -> throw new IllegalArgumentException("Error: Invalid move input.");
        }
    }

    /**
     * Manages combat between the hero and an encountered monster, including
     * attack resolution and outcome determination.
     * 
     * @param monster the enemy monster to engage in combat
     */
    private final void handleCombat(final Monster theMonster) {
        final BattleController battle = new BattleController(myHero, theMonster);
        battle.startBattle();
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
    private final void endGame(final boolean theResult) {
        if(theResult == true) {
            myDungeonView.displayWin();
        } else {
            myDungeonView.displayLoss();
        }
        myDungeonView.displayPlayerStatus(myHero);
        myDungeonView.displayDungeon(myDungeon);
    }

    private final void aboutPage() {
        // TODO: implement the about page
        System.out.println("Press [1] to return to main menu.");
        int playerChoice = -1;
        while (playerChoice != 1) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);
                if (playerChoice > 1 || playerChoice < 1) {
                    System.out.println("Invalid input. please press 1 to return to the main menu.");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Invalid input. please press 1 to return to the main menu.");
            }
        }
        mainMenu();
    }

    private final void helpPage() {

    }

    private final void closeGame() {
        System.out.println("Closing Game...");
        System.exit(0);
    }

    /**
     * Asks the player if they want to save the game
     */
    private final void saveGame() {

    }

    /**
     * Loads a saved game
     */
    private final void loadGame() {

    }


}
