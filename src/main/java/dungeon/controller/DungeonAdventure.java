
package dungeon.controller;

import java.util.Scanner;
import java.util.List;

import dungeon.model.Dungeon.Dungeon;
import dungeon.model.Dungeon.Room;
import dungeon.model.Saving.GameState;
import dungeon.model.Saving.InstanceCapture;
import dungeon.model.Saving.RestoreSave;
import dungeon.model.Saving.RestoreSave.RestoreResult;
import dungeon.model.Saving.SaveGameManager;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Monster;
import dungeon.model.characters.Priest;
import dungeon.model.characters.Thief;
import dungeon.model.characters.Warrior;
import dungeon.view.DungeonView;

/**
 * The DungeonAdventure class serves as the main controller for the dungeon adventure game.
 * It manages the game flow, including menu navigation, game initialization, player input,
 * combat encounters, and game state management. This class orchestrates interactions between
 * the game model (Dungeon, Hero, Monsters) and the view (DungeonView).
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

    private final SaveGameManager mySaveGameManager;

    private final Scanner myScanner = new Scanner(System.in);

    private boolean myActiveGame;
    private static final String NEWLINE = System.lineSeparator();

    public static void main(final String[] args) {
        final DungeonAdventure game = new DungeonAdventure();
        game.mainMenu();
    }

    /**
     * Constructs a DungeonAdventure instance and initializes the game view.
     */
    public DungeonAdventure() {
        myDungeonView = new DungeonView();
        mySaveGameManager = new SaveGameManager();
    }

    /**
     * Starts the game by initializing the game setup and running the main game loop.
     * Orchestrates the overall flow of the dungeon adventure game.
     */
    private final void createGame() {
        gameSetup();
        gameLoop();
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

    /**
     * Displays the main menu and handles player navigation through menu options.
     * Allows the player to start a new game, load a saved game, view help, view about info, or quit.
     */
    private final void mainMenu() {
        int playerChoice = -1;
        myDungeonView.displayMainMenu();
        while (playerChoice < 1 || playerChoice > 5) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 5) {
                    System.out.print(
                        "Invalid input. Please enter a choice between 1-5."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } catch (final NumberFormatException e){
                System.out.print(
                    "Invalid input. please enter a number between 1-5"
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        }

        switch (playerChoice) {
            case 1 -> createGame();
            case 2 -> loadMenu(); 
            case 3 -> helpPage(); // help menu
            case 4 -> aboutPage(); // about info
            case 5 -> closeGame(); 
            default -> throw new IllegalArgumentException("Invalid choice: " + playerChoice);
        }
    }

    private enum GameExitReason {
        HERO_DIED,
        WIN_CONDITION_MET,
        PLAYER_QUIT
    }


    /**
     * Executes the main game loop, processing player turns and game events until
     * the game reaches a terminal state (win or loss).
     */
    private final void gameLoop() {

        myActiveGame = true;
        GameExitReason exitReason = null;

        while(myActiveGame && exitReason == null) {

            if (!myHero.isAlive()) {
                exitReason = GameExitReason.HERO_DIED;
                break;
            }

            myDungeonView.turnSeperator();
            if (myDungeon.getCurrentRoom().hasMonster()) {
                evaluateRoomEvents(myDungeon.getCurrentRoom());
                myDungeonView.displayRoom(myDungeon.getCurrentRoom());
            } else {
                myDungeonView.displayRoom(myDungeon.getCurrentRoom());
                evaluateRoomEvents(myDungeon.getCurrentRoom());
            }

            if (!myHero.isAlive()) {
                exitReason = GameExitReason.HERO_DIED;
                break;
            }

            myDungeonView.displayDungeon(myDungeon);
            myDungeonView.displayPlayerStatus(myHero);
            myDungeonView.displayInGameMenu();

            final String userChoice = getValidPlayerChoice();
            exitReason = handlePlayerChoice(userChoice);

            if(checkWinCondidtion() && exitReason == null) {
                exitReason = GameExitReason.WIN_CONDITION_MET;
            }
        }
        endGame(exitReason);
    }

    /**
     * Prompts the player to select a hero class (Warrior, Priest, or Thief).
     * Validates input and returns the selected class choice.
     * 
     * @return the player's hero class selection (1-3)
     */
    private final int classChoice() {
        // Validate player class selection
        int playerChoice = -1;
        myDungeonView.promptHeroSelection();
        while (playerChoice < 1 || playerChoice > 3) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 3) {
                    System.out.print(
                        "Invalid input: Please enter a choice between 1-3."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } catch (final NumberFormatException e) {
                System.out.print(
                    "Invalid input: Please enter a number between 1-3."
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        } 
        return playerChoice;
    }

    /**
     * Prompts the player to enter their hero's name.
     * Validates that the name is not empty and does not exceed 15 characters.
     * 
     * @return the player's chosen hero name
     */
    private final String nameChoice() {
         // Set the player's name with a max 15 characters
        String heroName;
        boolean validName = false;
        do {
            myDungeonView.promptName();
            heroName = myScanner.nextLine();
            
            if(heroName.length() > 15) {
                System.out.println(
                    "Name too long. Please enter a name with a max of 15 characters."
                    + NEWLINE
                    + "> Re-enter name: "
                );
            } else if(heroName.isEmpty()) {
                System.out.print(
                    "Name cannot be empty. Please enter a valid name."
                    + NEWLINE
                    + "> Re-enter name: "
                );
            } else {
                validName = true;
            }
        } while (!validName);
        return heroName;
    }

    /**
     * Creates and initializes the hero based on the selected class and name.
     * 
     * @param thePlayerChoice the selected hero class (1=Warrior, 2=Priest, 3=Thief)
     * @param theHeroName the name given to the hero
     * @throws IllegalArgumentException if thePlayerChoice is not 1, 2, or 3
     */
    private void setMyHero(final int thePlayerChoice, final String theHeroName) {
        myHero = switch (thePlayerChoice) {
            case 1 -> new Warrior(theHeroName, 0, 0);
            case 2 -> new Priest(theHeroName, 0, 0);
            case 3 -> new Thief(theHeroName, 0, 0);
            default -> throw new IllegalArgumentException("Invalid class choice: " + thePlayerChoice);
        }; 
    }

    /**
     * Prompts the player to select the dungeon difficulty level.
     * Creates a dungeon of the appropriate size based on the selection:
     * Easy (5x5), Medium (7x7), or Hard (10x10).
     */
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
            case 1 -> new Dungeon(5, 5, "The Easy Dungeon", false);
            case 2 -> new Dungeon(7, 7, "The Medium Dungeon", false);
            case 3 -> new Dungeon(10, 10, "The Hard Dungeon", false);
            default -> throw new IllegalArgumentException("Invalid dungeon selection: " + playerChoice);
        };
        myDungeon.generateDungeon();
    }

    /**
     * Prompts the player for input and validates it based on available room doors
     * and hero inventory. Continues prompting until valid input is received.
     * Valid inputs include movement commands (W/A/S/D), item usage (H/V),
     * special actions (F=save, Q=quit, B=menu, M=map), or combat (no special key).
     * 
     * @return a validated single-character command from the player
     */
    private String getValidPlayerChoice() {
        String userChoice;
        boolean validInput = false;
        do {
            userChoice = myScanner.nextLine().trim().toUpperCase();
            
            if ((userChoice.equals("W") && myDungeon.getCurrentRoom().hasNorthDoor()) 
                || (userChoice.equals("A") && myDungeon.getCurrentRoom().hasWestDoor())
                || (userChoice.equals("S") && myDungeon.getCurrentRoom().hasSouthDoor())
                || (userChoice.equals("D") && myDungeon.getCurrentRoom().hasEastDoor())
                || (userChoice.equals("H") && myHero.getHealingPotion() > 0  && myHero.getMaxHP() != myHero.getHP()) 
                || (userChoice.equals("V") && myHero.getVisionPotion() > 0)
                || userChoice.equals("F")
                || userChoice.equals("B") 
                || userChoice.equals("Q")
                || userChoice.equals("M")) {
                validInput = true;
            } else if (userChoice.equals("W") || userChoice.equals("A") 
                || userChoice.equals("S") || userChoice.equals("D")) {
                System.out.print(
                    "There is no door there!"
                    + NEWLINE
                    + "> Enter choice: "
                );                            
            } else if (userChoice.equals("H")) {
                if (myHero.getHealingPotion() <= 0) {
                    System.out.print(
                        "You have no healing potions!"
                        + NEWLINE
                        + "> Enter choice: "
                    );
                } else if (myHero.getMaxHP() == myHero.getHP()) {
                    System.out.print(
                        "You are at max health!"
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } else if (userChoice.equals("V")) {
                System.out.print(
                    "You have no vision potions!"
                    + NEWLINE
                    + "> Enter choice: "
                );
            } else {
                System.out.print(
                    "Invalid input"
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        } while (!validInput);
        return userChoice;
    }

    /**
     * Evaluates and applies the effects of the current room on the player,
     * such as finding items, encountering traps, or finding a monster. 
     * 
     * @param room the room to evaluate
     */
    private void evaluateRoomEvents(final Room theRoom) { 
        if (theRoom.hasPit()) {
            myDungeonView.displayPitInteration();
            theRoom.triggerEvent("PIT", myHero);
        }
        if (theRoom.hasFountain()) {
            myDungeonView.displayFountainInteraction();
            theRoom.triggerEvent("FOUNTAIN", myHero);
        } 
        if (theRoom.hasItems()) {
            myDungeonView.displayPotionAcquisition(theRoom);
            theRoom.pickUpItems(myHero);
        } 
        if (theRoom.hasMonster()) {
            myDungeonView.DisplayMonsterEncounter(theRoom);
            handleCombat(theRoom.getMonster(), theRoom);
        }
        if (theRoom.hasPillar() && myHero.isAlive()) {
            myDungeonView.displayPillarAcquisition();
            myHero.gainPillar(theRoom.pickUpPillar());
        } 
    }

    /**
     * Processes the player's input choice and executes the corresponding action.
     * 
     * @param thePlayerChoice the character representing the player's choice
     */
    private final GameExitReason handlePlayerChoice(final String thePlayerChoice) {

        switch (thePlayerChoice) {
            case "W", "A", "S", "D" -> move(thePlayerChoice);
            case "H" -> myHero.useHealingPotion();
            case "V" -> myHero.useVisionPotion(myDungeon);
            case "B" -> {
                // TODO: implement help menu
            }
            case "F" -> {
                saveGame();
            }
            case "Q" -> {
                return returnToMenu();
            }
            case "M" -> {
                myDungeonView.displayFullDungeon(myDungeon);
            }
        }
        return null;
    }

    /**
     * Handles the player's movement within the dungeon, updating position, cooldowns,
     * and evaluating effects of the new room.
     */
    private final void move(final String theDirection) {
        myHero.tickCooldowns();
        switch (theDirection) {
            case "W" -> myDungeon.moveHero("NORTH");
            case "A" -> myDungeon.moveHero("WEST");
            case "S" -> myDungeon.moveHero("SOUTH");
            case "D" -> myDungeon.moveHero("EAST");
            default -> throw new IllegalArgumentException("Error: Invalid move input.");
        }
    }

    /**
     * Manages combat between the hero and an encountered monster, including
     * attack resolution and outcome determination.
     * 
     * @param monster the enemy monster to engage in combat
     */
    private final void handleCombat(final Monster theMonster, final Room theRoom) {
        final BattleController battle = new BattleController(myHero);
        battle.startBattle(theMonster);
        if (!battle.monsterIsAlive()) {
            theRoom.removeMonster();
        }
    }

    /**
     * Checks if the win condition has been met (reaching the exit
     * with all 4 pillars).
     */
    private final boolean checkWinCondidtion() {
        return myHero.getPillarCount() == 4 && myDungeon.isExitReached();
    }

    /**
     * Finalizes the game, displaying the outcome and cleaning up resources.
     * 
     * @param theResult true if the player won, false if the player lost
     */
    private final void endGame(final GameExitReason theExitReason) {
        switch (theExitReason) {
            case WIN_CONDITION_MET -> {
                myDungeonView.displayWin();
                myDungeonView.displayPlayerStatus(myHero);
                myDungeonView.displayFullDungeon(myDungeon);
                postGamePrompt();
                
            }
            case HERO_DIED -> {
                myDungeonView.displayLoss();
                myDungeonView.displayPlayerStatus(myHero);
                myDungeonView.displayFullDungeon(myDungeon);
                postGamePrompt();
            }
            case PLAYER_QUIT -> {
                System.out.println("Returning to Main Menu...");
                mainMenu();
            }
        }
    }

    /**
     * Displays the about page with game information.
     * Currently a placeholder that allows the player to return to the main menu.
     */
    private final void aboutPage() {
        // TODO: implement the about page
        System.out.println("Press [1] to return to main menu.");
        int playerChoice = -1;
        while (playerChoice != 1) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);
                if (playerChoice > 1 || playerChoice < 1) {
                    System.out.print(
                        "Invalid input. please enter 1 to return to the main menu."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } catch (final NumberFormatException e) {
                System.out.print(
                    "Invalid input. please enter 1 to return to the main menu."
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        }
        mainMenu();
    }

    /**
     * Displays the help page with game instructions and controls.
     * Currently a placeholder for future implementation.
     */
    private final void helpPage() {
        //TODO: implement the help page
    }

    /**
     * Closes and exits the game application.
     */
    private final void closeGame() {
        System.out.println("Closing Game...");
        System.exit(0);
    }

    /**
     * Prompts the player whether they want to return to the main menu,
     * offering the option to save the game first if confirmed.
     * 
     * @return GameExitReason.PLAYER_QUIT if returning to menu, or null to continue playing
     */
    private final GameExitReason returnToMenu() {
        boolean validInput = false;
        String userChoice;
        myDungeonView.promptReturnToMenu();;
        do {
            userChoice = myScanner.nextLine().trim().toUpperCase();

            if (userChoice.equals("Y") || userChoice.equals("N")) {
                validInput = true;
            } else {
                System.out.print(
                    "Invalid input"
                    + NEWLINE
                    + "> Enter choice"
                );
            }           
        } while(!validInput);
        switch (userChoice) {
            case "Y":
                saveGame();
                return GameExitReason.PLAYER_QUIT;
            case "N":
                return null;
            default:
                throw new IllegalArgumentException("Invalid input: " + userChoice);
        }
    }

    /**
     * Displays post-game options (return to menu, play again, or exit),
     * validates player input, and executes the selected action.
     */
    private final void postGamePrompt() {
        int playerChoice = -1;
        myDungeonView.promptPostGame();
        
        while (playerChoice < 1 || playerChoice > 3) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);
                if (playerChoice < 1 || playerChoice > 3) {
                    System.out.print(
                        "Invalid input. please enter a choice between 1-3."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } catch (final NumberFormatException e) {
                System.out.print(
                    "Invalid input. please enter a choice between 1-3."
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        }
        switch (playerChoice) {
            case 1 -> mainMenu();
            case 2 -> createGame();
            case 3 -> closeGame();
            default -> throw new IllegalArgumentException("Invalid input: " + playerChoice);
        }
    }

    /**
     * Asks the player if they want to save the game
     */
    private final void saveGame() {
        boolean validInput = false;
        String userChoice;
        myDungeonView.promptSave();
        do {
            userChoice = myScanner.nextLine().trim().toUpperCase();

            if (userChoice.equals("Y") || userChoice.equals("N")) {
                validInput = true;
            } else {
                System.out.print(
                    "Invalid input"
                    + NEWLINE
                    + "> Enter choice"
                );
            }           
        } while(!validInput);

        switch (userChoice) { 
            case "Y" -> {
                final GameState state = InstanceCapture.captureGameState(myHero, myDungeon);
                System.out.print("Enter save name: ");
                final String saveName = myScanner.nextLine().trim();
                if (mySaveGameManager.saveGame(state, saveName)) {
                    System.out.println("Game saved!");
                }
            }
            case "N" -> System.out.println("Canceled Save.");
            default -> throw new IllegalArgumentException("Invalid input: " + userChoice);
        }
    }

    /**
     * Displays a menu for loading or managing saved games.
     * Shows all available saves with timestamps, hero names, and classes.
     * Allows the player to load a save or delete a save file.
     */
    private final void loadMenu() {
        final List<String> saves = mySaveGameManager.listSaves();
        if (saves.isEmpty()) {
            System.out.println("No saves available.");
            mainMenu();
            return;
        }
        
        System.out.println("\nAvailable saves:");
        for (int i = 0; i < saves.size(); i++) {
            System.out.println((i + 1) + ". " + saves.get(i));
        }

        int playerChoice = -1;
        myDungeonView.promptLoad();
        while (playerChoice < 1 || playerChoice > 3) {
            try {
                final String input = myScanner.nextLine().trim();
                playerChoice = Integer.parseInt(input);

                if (playerChoice < 1 || playerChoice > 3) {
                    System.out.print(
                        "Invalid input: Please enter a choice between 1-3."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                }
            } catch (final NumberFormatException e) {
                System.out.print(
                    "Invalid input: Please enter a number between 1-3."
                    + NEWLINE
                    + "> Enter choice: "
                );
            }
        }
        switch (playerChoice) {
            case 1 -> loadGame();
            case 2 -> deleteSave();
        
            default -> throw new IllegalArgumentException("Invalid input: "  + playerChoice);
        }
    }

    /**
     * Prompts the player to enter a save name and loads the corresponding game.
     * Continues prompting until a valid save is found, then restores the game state
     * and resumes the game loop.
     */
    private final void loadGame() {
        System.out.print("> Enter save name to load: ");
        GameState loadedState = null;
        do {
            final String saveName = myScanner.nextLine().trim();
            loadedState = mySaveGameManager.loadGame(saveName);
            if (loadedState == null) {
                System.out.print("> Enter save name: ");
            }
        } while (loadedState == null);

        if (loadedState != null) {
            RestoreSave restore = new RestoreSave();
            RestoreResult result = restore.restoreGameState(loadedState);
            myDungeon = result.myDungeon;
            myHero = result.myHero;
//            restoreGameState(loadedState);
            gameLoop();
        }
    }

    /**
     * Prompts the player to enter a save name and deletes the corresponding save file.
     * If the save is not found, displays an error message.
     * Returns to the load menu after attempting deletion.
     */
    private final void deleteSave() {
        System.out.print("> Enter save name to delete: ");
        boolean isDeleted = false;
        final String saveName = myScanner.nextLine().trim();    
        isDeleted = mySaveGameManager.deleteSave(saveName);    
        if (!isDeleted) {
            System.out.println("Could not find save.");
        }
        loadMenu();
    }
}
