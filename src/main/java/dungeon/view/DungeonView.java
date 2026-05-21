
package dungeon.view;

import dungeon.model.Dungeon;
import dungeon.model.Room;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Monster;
import dungeon.model.items.HealingPotion;
import dungeon.model.items.VisionPotion;

/**
 * The DungeonView class manages all user interface output for the Dungeon Adventure game.
 * It provides methods to display menus, game state, combat information, and various game events.
 * This class handles text formatting with colors and separators to enhance the user experience.
 * 
 * @author Jackson Steger
 * @version 1.0
 */
public class DungeonView {

    // Text Colors
    private static final String TEXT_COLOR_RESET    = "\u001B[0m";
    private static final String TEXT_RED            = "\u001B[31m";
    private static final String TEXT_GREEN          = "\u001B[32m";
    private static final String TEXT_YELLOW         = "\u001B[33m";
    private static final String TEXT_BLUE           = "\u001B[34m";
    private static final String TEXT_PINK           = "\u001B[35m";
    private static final String TEXT_PURPLE         = "\u001B[36m";
    private static final String TEXT_BLACK          = "\u001B[30m";
    private static final String TEXT_DIM            = "\u001B[2m";

    private static final String SEPERATOR_EQUALS        = "==================================================";
    private static final String SEPERATOR_EQUALS_HALF   = "====================";
    private static final String SEPERATOR_EQUALS_SHORT  = "====";
    private static final String SEPERATOR_STRAIGHT      = "--------------------------------------------------";
    private static final String NEWLINE = System.lineSeparator();

    /**
     * Constructs a DungeonView instance.
     */
    public DungeonView() {
        // Default constructor
    }

    // ============================== Begin Main Menu display section ==============================

    /**
     * Displays the main menu with options for starting a new game, loading a saved game,
     * viewing help, viewing about information, or quitting the game.
     */
    public final void displayMainMenu() {
        System.out.print(
            TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
            + "          WELCOME TO DUNGEON ADVENTURE!"
            + NEWLINE
            + TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
            + NEWLINE
            + TEXT_BLUE 
            + "[1] Start a new game "
            + NEWLINE
            + "[2] Load a saved game "
            + NEWLINE
            + "[3] Help menu "
            + NEWLINE
            + "[4] About "
            + NEWLINE
            + "[5] Quit "
            + TEXT_COLOR_RESET
            + NEWLINE
            + "> Enter choice: "
        );
    }

    // ============================== End Main Menu display section ==============================



    // ============================== Begin Intro display section ==============================

    /**
     * Displays the game introduction screen, showing the dungeon name and the objective
     * to collect all 4 pillars and find the exit.
     * 
     * @param myDungeon the dungeon being entered
     */
    public final void displayIntro(final Dungeon myDungeon) {
        System.out.println(
            TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET 
            + NEWLINE
            + "You have entered " + myDungeon.getMyName() + "!" //change to incorporate the dungeon name
            + NEWLINE
            + TEXT_BLACK + "Collect all 4 pillars and find the exit" + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET 
            + NEWLINE
        );
    }

    /**
     * Prompts the player to select the dungeon difficulty level with descriptions
     * of each difficulty option.
     */
    public final void promptDungeonDifficulty() {
        System.out.print(
            "Choose the dungeons' difficulty:" 
            + NEWLINE
            + TEXT_GREEN + "[1] EASY: 5x5" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_YELLOW + "[2] MEDIUM: 7x7" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_RED + "[3] HARD: 10x10" + TEXT_COLOR_RESET
            + NEWLINE
            + "> Enter Choice: "
        );
    }

    /**
     * Displays the hero selection menu with descriptions of each available hero class
     * and their special abilities.
     */
    public final void promptHeroSelection() {
        System.out.print(
            "Choose your hero:" 
            + NEWLINE 
            + TEXT_BLUE + "[1] Warrior (125 HP): " 
            + NEWLINE
            + "    Special Skill: Crushing Blow"
            + NEWLINE 
            + "    Ultimate Skill: Enrage" 
            + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_PINK + "[2] Priest (85 HP): "
            + NEWLINE
            + "    Special Skill: Heal"
            + NEWLINE 
            + "    Ultimate Skill: Smite" 
            + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_BLACK + "[3] Thief (75 HP): "
            + NEWLINE
            + "    Special Skill: Surprise Attack"
            + NEWLINE 
            + "    Ultimate Skill: Garrote"  
            + TEXT_COLOR_RESET 
            + NEWLINE 
            + NEWLINE
            + "> Enter choice: "
        );
    }

    /**
     * Prompts the player to enter the name of their character.
     */
    public final void promptName() {
        System.out.print("> Enter the name of your character [Max 15 characters]: ");
    }

    // ============================== End Intro display section ==============================



    // ============================== Begin Player Stats display section ==============================

    /**
     * Returns a formatted string displaying the player's current health points.
     * 
     * @param theHero the hero whose health to display
     * @return formatted health display string
     */
    public final String displayPlayerHP(final Hero theHero) {
        return
            TEXT_GREEN + "HP: " + TEXT_COLOR_RESET 
            + playerHPColorHelper(theHero) 
            + TEXT_GREEN + "/" + theHero.getMaxHP() + TEXT_COLOR_RESET;
    }

    /**
     * Returns a formatted string displaying the player's name.
     * 
     * @param theHero the hero whose name to display
     * @return formatted name display string
     */
    public final String displayPlayerName(final Hero theHero) {
        return TEXT_YELLOW + theHero.getCharName() + TEXT_COLOR_RESET;
    }

    /**
     * Returns a formatted string displaying the count of healing and vision potions.
     * 
     * @param theHero the hero whose potions to display
     * @return formatted potions display string
     */
    public final String displayPotions(final Hero theHero) {
        return 
            TEXT_PURPLE + "Potions: Hx" + TEXT_COLOR_RESET
            + healthPotionDisplayHelper(theHero)
            + TEXT_PURPLE + " Vx" + TEXT_COLOR_RESET
            + visionPotionDisplayHelper(theHero);
    }

    /**
     * Returns a formatted string displaying the count of healing potions only.
     * 
     * @param theHero the hero whose healing potions to display
     * @return formatted healing potions display string
     */
    public final String displayHealingPotions(final Hero theHero) {
        return TEXT_PURPLE + "HP Potions: Hx" + TEXT_COLOR_RESET + healthPotionDisplayHelper(theHero);
    }

    /**
     * Returns a formatted string displaying the number of pillars collected.
     * 
     * @param theHero the hero whose pillars to display
     * @return formatted pillars display string
     */
    public final String displayPillars(final Hero theHero) {
        return TEXT_BLUE + "Pillars: " + theHero.getMyPillars() + TEXT_COLOR_RESET;
    }

    // ==============================  End Player Stats display section  ==============================



    // ============================== Begin Monster Stats display section ==============================
    
    /**
     * Returns a formatted string displaying the monster's name.
     * 
     * @param theMonster the monster whose name to display
     * @return formatted monster name display string
     */
    public String displayMonsterName(final Monster theMonster) {
        return TEXT_YELLOW + theMonster.getCharName() + TEXT_COLOR_RESET;
    }

    /**
     * Returns a formatted string displaying the monster's current health points.
     * 
     * @param theMonster the monster whose health to display
     * @return formatted health display string
     */
    public final String displayMonsterHP(final Monster theMonster) {
        return
            TEXT_GREEN + "HP: " + TEXT_COLOR_RESET 
            + monsterHpColorHelper(theMonster) 
            + TEXT_GREEN + "/" + theMonster.getMaxHP() + TEXT_COLOR_RESET;
    }

    // ==============================  End Monster Stats display section  ==============================




    // ============================== Begin Game Loop display section ==============================

    /**
     * Displays the player's status including name, health, potions, and pillars.
     * 
     * @param theHero the hero whose status to display
     */
    public final void displayPlayerStatus(final Hero theHero) {
        System.out.println(
            SEPERATOR_STRAIGHT 
            + NEWLINE
            + displayPlayerName(theHero) + " | " // character name
            + displayPlayerHP(theHero) + " | " // Character hp count
            + displayPotions(theHero) + " | " // number of hp potions and vision potions
            + displayPillars(theHero)
            + NEWLINE
            + "Abilities: Basic Attack | " 
            + theHero.getSpecialSkillName() + " | " 
            + cooldownDisplayHelper(theHero)  
            + NEWLINE 
            + SEPERATOR_STRAIGHT 
            + NEWLINE
        );
    }

    /**
     * Displays the in-game menu with available player actions (movement, potions, quit, etc.).
     */
    public final void displayInGameMenu() {
        System.out.print(
            TEXT_BLUE
            + "[W/A/S/D] Move "
            + NEWLINE
            + "[H] Use healing potion "
            + NEWLINE
            + "[V] Use vision potion "
            + NEWLINE
            + "[B] Help "
            + NEWLINE
            + "[Q] Quit "
            + TEXT_COLOR_RESET
            + NEWLINE
            + "> Enter choice: "
        );
    }

    /**
     * Displays information about the current room including its contents and features.
     * 
     * @param theRoom the room to display
     */
    public final void displayRoom(final Room theRoom) {
        System.out.println(
            "Current Room: "
            + NEWLINE
            + theRoom.toString()
            + NEWLINE
        );
    }

    /**
     * Displays rooms in the dungeon for the player to see.
     * Rooms unvisited by the player will be displayed in '?'.
     * 
     * @param theDungeon
     */
    public final void displayDungeon(final Dungeon theDungeon) {
        System.out.println(
            NEWLINE
            + "Dungeon Map: "
            + NEWLINE
            + theDungeon.toString()
            + NEWLINE
        );
    }

    /**
     * Displays a separator banner for the next turn in the game.
     */
    public final void turnSeperator() {
        System.out.println(
            NEWLINE
            + SEPERATOR_EQUALS
            + NEWLINE
            + "                     NEXT TURN"
            + NEWLINE
            + SEPERATOR_EQUALS
            + NEWLINE
        );
    }

    // ============================== End Game Loop display section ==============================

 

    // ============================== Begin Extra Room display section ==============================

    /**
     * Displays a monster encounter message when the player encounters a monster in a room.
     * 
     * @param theRoom the room containing the monster
     */
    public final void DisplayMonsterEncounter(final Room theRoom) {
        if (theRoom.hasMonster()) {
            System.out.println(
                TEXT_RED + "You encountered a " + theRoom.getMonster().getCharName() + TEXT_COLOR_RESET
            );
        }
    }

    /**
     * Displays a message when the player picks up potions from a room.
     * 
     * @param theRoom the room containing the potions
     */
    public final void displayPotionAcquisition(final Room theRoom) {
        for (int i = 0; i < theRoom.getMyItems().size(); i++) {
            if (theRoom.getMyItems().get(i) instanceof HealingPotion) {
                System.out.println(
                    TEXT_GREEN + "You picked up a healing potion!" + TEXT_COLOR_RESET
                );
            } else if (theRoom.getMyItems().get(i) instanceof VisionPotion) {
                System.out.println(
                    TEXT_PURPLE + "You picked up a vision potion!" + TEXT_COLOR_RESET
                );
            }
        }
    }

    /**
     * Displays a message when the player picks up a pillar.
     */
    public final void displayPillarAcquisition() {
        System.out.println(
            TEXT_BLUE + "You picked up a pillar!" + TEXT_COLOR_RESET
        );
    }

    /**
     * Displays a message when the player discovers and interacts with a healing fountain.
     */
    public final void displayFountainInteraction() {
        System.out.println(
            TEXT_PINK + "You discovered a fountain!" + TEXT_COLOR_RESET
        );
    }

    /**
     * Displays a message when the player falls into a pit.
     */
    public final void displayPitInteration() {
        System.out.println(
            TEXT_RED + "You fell into a pit!" + TEXT_COLOR_RESET
        );
    }

    /**
     * Displays a 3x3 grid of the rooms surrounding the player when they
     * drink a vision potion.
     */
    public final void displayVisionPotionView() { 
        System.out.println(
            TEXT_PURPLE+ SEPERATOR_EQUALS + TEXT_COLOR_RESET
        );

    }

    /**
     * Displays all rooms in the dungeon for the user to see.
     * 
     * @param theDungeon
     */
    public final void displayFullDungeon(final Dungeon theDungeon) {
        System.out.println(
            NEWLINE
            + "Full Dungeon Map: "
            + NEWLINE
            + theDungeon.toStringFullDungeon()
            + NEWLINE
        );
    }

    // ============================== End Extra Room Display Section ==============================



    // ============================== Begin Combat Display Section ==============================

    /**
     * Prompts the player to select their combat action (attack, special, ultimate, heal, etc.).
     * 
     * @param theHero the hero choosing their action
     */
    public final void promptActionChoice(final Hero theHero) {
        System.out.print(
            TEXT_BLUE + "Choose an Action: "
            + NEWLINE
            + " [1] Basic Attack"
            + NEWLINE
            + " [2] Special Attack: " + theHero.getSpecialSkillName()
            + NEWLINE
            + " [3] Ultimate Attack: " + cooldownDisplayHelper(theHero) + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_BLUE + " [4] Use Healing Potion: " + TEXT_COLOR_RESET 
            + TEXT_PURPLE + "Hx" + healthPotionDisplayHelper(theHero) + TEXT_COLOR_RESET
            + NEWLINE
            + "> Enter Choice: "
        );
    }

    /**
     * Displays the hero's current status during combat.
     * 
     * @param theHero the hero whose combat status to display
     */
    public final void displayHeroCombatStatus(final Hero theHero) {
        System.out.println(
            TEXT_BLUE + SEPERATOR_EQUALS_SHORT + " Hero's Status " + SEPERATOR_EQUALS_SHORT + TEXT_COLOR_RESET
            + NEWLINE
            + displayPlayerName(theHero)
            + NEWLINE
            + displayPlayerHP(theHero)
            + NEWLINE
            + displayHealingPotions(theHero)
            + NEWLINE
            + TEXT_BLUE + SEPERATOR_EQUALS_HALF + "===" + TEXT_BLUE
            + NEWLINE
        );
    }

    /**
     * Displays the monster's current status during combat.
     * 
     * @param theMonster the monster whose combat status to display
     */
    public final void displayMonsterStatus(final Monster theMonster) {
        System.out.println(
            TEXT_RED + "\\\\\\ Monster's Status ///" + TEXT_COLOR_RESET
            + NEWLINE
            + displayMonsterName(theMonster)
            + NEWLINE
            + displayMonsterHP(theMonster)
            + NEWLINE
            + TEXT_RED + "////////||||||||\\\\\\\\\\\\\\\\" + TEXT_COLOR_RESET
            + NEWLINE
        );
    }

    /**
     * Returns a formatted string for the specified combat round.
     * 
     * @param myRound the round number
     * @return formatted combat round display string
     */
    public final String displayCombatRound(final int myRound) {
        return
            NEWLINE
            + TEXT_PINK + SEPERATOR_EQUALS_HALF + " ROUND " + myRound + " " + SEPERATOR_EQUALS_HALF + TEXT_COLOR_RESET
            + NEWLINE;
    }

    /**
     * Displays a banner indicating the start of the hero's turn in combat.
     */
    public final void displayHeroTurn() {
        System.out.println(
             TEXT_YELLOW + "================== HERO'S TURN ==================" + TEXT_COLOR_RESET
            + NEWLINE
        );
    }

    /**
     * Displays a banner indicating the start of the monster's turn in combat.
     */
    public final void displayMonsterTurn() {
        System.out.println(
            NEWLINE
            + TEXT_RED + "================== ENEMY'S TURN ==================" + TEXT_COLOR_RESET
        );
    }

    /**
     * Displays a combat log header during battle.
     */
    public final void displayCombatLog() {
        System.out.println(
            TEXT_BLUE + SEPERATOR_EQUALS_HALF + " COMBAT LOG " + SEPERATOR_EQUALS_HALF + TEXT_COLOR_RESET
        );
    }

    // ==============================   End Combat Display Section   ==============================



    // ============================== Begin Display Helper Method Section ==============================

    private final String cooldownDisplayHelper(final Hero theHero) {
        if (theHero.getCDTimer() > 0) {
            return TEXT_BLACK + theHero.getUltimateName() + TEXT_COLOR_RESET
            + " (CD: " + theHero.getCDTimer() + " turns left)";
        } else {
            return theHero.getUltimateName();
        }
    }

    private final String healthPotionDisplayHelper(final Hero theHero) {
        if (theHero.getHealingPotion() > 0) {
            return TEXT_PURPLE + theHero.getHealingPotion() + TEXT_COLOR_RESET;
        } else {
            return TEXT_BLACK + theHero.getHealingPotion() + TEXT_COLOR_RESET;
        }
    }

    private final String visionPotionDisplayHelper(final Hero theHero) {
        if (theHero.getVisionPotion() > 0) {
            return TEXT_PURPLE + theHero.getVisionPotion() + TEXT_COLOR_RESET;
        } else {
            return TEXT_BLACK + theHero.getVisionPotion() + TEXT_COLOR_RESET;
        }
    }

    private final String playerHPColorHelper(final Hero theHero) {
        if (theHero.getHP() > theHero.getMaxHP() * 0.75) {
            return TEXT_GREEN + theHero.getHP() + TEXT_COLOR_RESET;
        } else if (theHero.getHP() <= theHero.getMaxHP() * 0.75 && theHero.getHP() >= theHero.getMaxHP() * 0.25) {
            return TEXT_YELLOW + theHero.getHP() + TEXT_COLOR_RESET;
        } else if (theHero.getHP() < theHero.getMaxHP() * 0.25) {
            return TEXT_RED + theHero.getHP() + TEXT_COLOR_RESET;
        }
        return TEXT_BLACK + theHero.getHP() + TEXT_COLOR_RESET;
    }

    private final String monsterHpColorHelper(final Monster theMonster) {
        if (theMonster.getHP() > theMonster.getMaxHP() * 0.75) {
            return TEXT_GREEN + theMonster.getHP() + TEXT_COLOR_RESET;
        } else if (theMonster.getHP() < theMonster.getMaxHP() * 0.75 && theMonster.getHP() > theMonster.getMaxHP() * 0.25) {
            return TEXT_YELLOW + theMonster.getHP() + TEXT_COLOR_RESET;
        } else if (theMonster.getHP() < theMonster.getMaxHP() * 0.25) {
            return TEXT_RED + theMonster.getHP() + TEXT_COLOR_RESET;
        }
        return TEXT_BLACK + theMonster.getHP() + TEXT_COLOR_RESET;
    }

    // ==============================  End Display Helper Method Section  ==============================



    // ==============================  Begin Misc Display Section  ==============================
    
    /**
     * Displays a prompt for the player to enter a choice.
     */
    public final void reprompt() {
        System.out.print("> Enter choice: ");
    }

    /**
     * Prompts the player to confirm whether they want to save the game.
     */
    public final void promptSave() {
        System.out.print(
            "Do you want to save [Y/N]?"
            + NEWLINE
            + "> Enter choice: "
        );
    }

    public final void promptReturnToMenu() {
        System.out.print(
            "Do you want to return to the main menu [Y/N]?"
            + NEWLINE
            + "> Enter choice: "
        );
    }

    public final void promptPostGame() {
        System.out.print(
            "[1] Quit to main menu "
            + NEWLINE
            + "[2] Start a new game "
            + NEWLINE
            + "[3] Close game "
        );
    }

    /**
     * Displays a blue separator line as a section ending marker.
     */
    public final void endSectionBlue() {
        System.out.println(
            NEWLINE
            + TEXT_BLUE + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
        );
    }

    public final void displayHelpSection() {

    }

    // ==============================   End Misc Display Section   ==============================



    // ============================== Begin End Game Display Section ==============================

    /**
     * Displays the game loss screen when the player is defeated.
     */
    public final void displayLoss() {
        System.out.println(
            NEWLINE
            + TEXT_RED + SEPERATOR_EQUALS
            + NEWLINE 
            + "                    GAME OVER"
            + NEWLINE
            + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
        );

    }

    /**
     * Displays the game victory screen when the player successfully escapes the dungeon.
     */
    public final void displayWin() {
        System.out.println(
            NEWLINE
            + TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_GREEN + "             You escaped the dungeon!"
            + NEWLINE
            + "                     VICTORY" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
        );
    }

    // ==============================  End End Game Display Section  ==============================



    // ==============================  Begin Debug Display Section  ==============================

    // ==============================  End Debug Display Section  ==============================

}
