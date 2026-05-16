
package dungeon.view;

import dungeon.model.Dungeon;
import dungeon.model.Room;
import dungeon.model.characters.Hero;
import dungeon.model.characters.Thief;
import dungeon.model.items.HealingPotion;
import dungeon.model.items.VisionPotion;

/**
 * 
 * @author Jackson Steger
 * @version 1.0
 */
public class DungeonView {

    // Text Colors
    private static final String TEXT_COLOR_RESET = "\u001B[0m";
    private static final String TEXT_RED = "\u001B[31m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_YELLOW = "\u001B[33m";
    private static final String TEXT_BLUE = "\u001B[34m";
    private static final String TEXT_PINK = "\u001B[35m";
    private static final String TEXT_PURPLE = "\u001B[36m";
    private static final String TEXT_BLACK = "\u001B[30m";

    private static final String SEPERATOR_EQUALS = "==================================================";
    private static final String SEPERATOR_EQUALS_HALF = "====================";
    private static final String SEPERATOR_STRAIGHT = "--------------------------------------------------";
    private static final String NEWLINE = System.lineSeparator();

    public DungeonView() {
        // Default constructor
    }

    // ============================== Begin Main Menu display section ==============================

    public final void displayMainMenu() {
        System.out.print(
            TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET
            + NEWLINE
            + "WELCOME TO DUNGEON ADVENTURE!"
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
            + TEXT_PINK + "[2] Priest (75 HP): " 
            + NEWLINE
            + "    Special Skill: Heal"
            + NEWLINE 
            + "    Ultimate Skill: Smite" 
            + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_BLACK + "[3] Thief (75 HP): "
            + NEWLINE
            + "    Special Skill: Suprise Attack"
            + NEWLINE 
            + "    Ultimate Skill: Garrote"  
            + TEXT_COLOR_RESET 
            + NEWLINE 
            + NEWLINE
            + "> Enter choice: "
        );
    }

    public final void promptName() {
        System.out.print("> Enter the name of your character [Max 15 characters]: ");
    }

    // ============================== End Intro display section ==============================



    // ============================== Begin Game Loop display section ==============================

    public final void displayPlayerStatus(final Hero theHero) {
        System.out.println(
            SEPERATOR_STRAIGHT 
            + NEWLINE
            + TEXT_YELLOW + theHero.getCharName() + TEXT_COLOR_RESET + " | " // character name
            + TEXT_GREEN + "HP: " + TEXT_COLOR_RESET 
            + hpColorStatusHelper(theHero) 
            + TEXT_GREEN + "/" + theHero.getMaxHP() + TEXT_COLOR_RESET + " | " // Character hp count
            + TEXT_PURPLE + "Potions: Hx" + healthPotionDisplayHelper(theHero) + " " + TEXT_COLOR_RESET
            + TEXT_PURPLE + "Vx" + visionPotionDisplayHelper(theHero) + TEXT_COLOR_RESET + " | " // number of player hp potions and vision potions
            + TEXT_BLUE + "Pillars: " + TEXT_COLOR_RESET
            + NEWLINE
            + "Abilities: Basic Attack | " 
            + theHero.getSpecialSkillName() + " | " 
            + cooldownDisplayHelper(theHero)  
            + NEWLINE 
            + SEPERATOR_STRAIGHT 
            + NEWLINE
        );
    }

    private final String hpColorStatusHelper(final Hero theHero) {
        if (theHero.getHP() > theHero.getMaxHP() * 0.75) {
            return TEXT_GREEN + theHero.getHP() + TEXT_COLOR_RESET;
        } else if (theHero.getHP() < theHero.getMaxHP() * 0.75 && theHero.getHP() > theHero.getMaxHP() * 0.25) {
            return TEXT_YELLOW + theHero.getHP() + TEXT_COLOR_RESET;
        } else if (theHero.getHP() < theHero.getMaxHP() * 0.25) {
            return TEXT_RED + theHero.getHP() + TEXT_COLOR_RESET;
        }
        return TEXT_BLACK + theHero.getHP() + TEXT_COLOR_RESET;
    }

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

    public final void displayRoom(final Room theRoom) { //parameter/s: room
        System.out.println("Current room: ");
        System.out.println(
            theRoom.toString()
            + NEWLINE
        );
    }

    // ============================== End Game Loop display section ==============================

 

    // ============================== Begin Extra Room display section ==============================

    public final void displayRoomEvent(Room theRoom) { // Some parameter that indecates what is in the room
        if (theRoom.hasMonster()) {
            System.out.println(
                TEXT_RED + "You encountered a " + theRoom.getMonster().getCharName() + TEXT_COLOR_RESET
            );
        }
        if (theRoom.hasPit()) {
            System.out.println(
                TEXT_RED + "You fell into a pit!" + TEXT_COLOR_RESET
            );
        }
        if (theRoom.hasFountain()) {
            System.out.println(
                TEXT_PINK + "You discovered a fountain!" + TEXT_COLOR_RESET
            );
        } 
        if (theRoom.hasItems()) {
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
        if (theRoom.hasPillar()) {
            System.out.println(
                TEXT_BLUE + "You picked up a pillar!" + TEXT_COLOR_RESET
            );
        } 
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
     * Displays all the rooms in the dungeon for the player to see.
     * This will display when the game end either through losing or 
     * winning the game. Also used for debugging.
     * 
     * 
     */
    public final void displayDungeon(final Dungeon theDungeon) {
        System.out.println("Dungeon map: ");
        System.out.println(
            theDungeon.toString()
            + NEWLINE
        );
    }

    // ============================== End Extra Room Display Section ==============================



    // ============================== Begin Combat Display Section ==============================

    public final void promptActionChoice(final Hero theHero) { //parameter/s: hero, monster
        System.out.print(
            "Choose an Action: "
            + NEWLINE
            + " [1] Basic Attack"
            + NEWLINE
            + " [2] Special Attack: " + theHero.getSpecialSkillName()
            + NEWLINE
            + " [3] Ultimate Attack: " + cooldownDisplayHelper(theHero)
            + NEWLINE
            + " [4] Use Healing Potion: Hx" + healthPotionDisplayHelper(theHero)
            + NEWLINE
            + "> Enter Choice: "
        );
    }

    public final void displayCombatRound(final int myRound) {
        System.out.println(
            TEXT_RED + SEPERATOR_EQUALS_HALF + " Round: " + myRound + " " + SEPERATOR_EQUALS_HALF + TEXT_COLOR_RESET
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

    // ==============================  End Display Helper Method Section  ==============================



    // ============================== Begin End Game Display Section ==============================

    public final void displayLoss() {
        System.out.println(
            TEXT_RED + "DEFEAT" + TEXT_COLOR_RESET
            + NEWLINE
        );

    }

    public final void displayWin() {
        System.out.println(
            TEXT_GREEN + "You escaped the dungeon!" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_YELLOW + "VICTORY" + TEXT_COLOR_RESET
            + NEWLINE
        );
    }

    // ==============================  End End Game Display Section  ==============================



    // ==============================  Begin Debug Display Section  ==============================

    // ==============================  End Debug Display Section  ==============================

}
