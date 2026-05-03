
package dungeon.view;

import dungeon.model.characters.Hero;

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

    private static final String SEPERATOR_EQUALS = "=============================================";
    private static final String SEPERATOR_STRAIGHT = "----------------------------------------------";
    private static final String NEWLINE = System.lineSeparator();


    public static void main(String[] args) {
        DungeonView test = new DungeonView();
        test.displayMenu();
    }

    DungeonView() {
        // Default constructor
    }

    // ============================== Begin Intro display section ==============================

    public final void displayIntro() {
        System.out.println(
            TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET 
            + NEWLINE
            + "WELCOME TO BETTER NAME PENDING" 
            + NEWLINE
            + TEXT_BLACK + "Collect all 4 pillars and find the exit" + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_YELLOW + SEPERATOR_EQUALS + TEXT_COLOR_RESET 
            + NEWLINE
        );
    }

    public final void displayHeroSelection() {
        System.out.print(
            "Choose your hero:" 
            + NEWLINE 
            + TEXT_BLUE + "[1] Warrior - Special Skill: Crushing Blow, 125 HP" + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_PINK + "[2] Priest - Special Skill: Heal, 75 HP" + TEXT_COLOR_RESET 
            + NEWLINE
            + TEXT_BLACK + "[3] Thief - Special Skill: Suprise Attack, 75 HP" + TEXT_COLOR_RESET 
            + NEWLINE 
            + NEWLINE
            + "> Enter choice: "
        );
    }

    public final void promptName() {
        System.out.print("> Enter the name of your character: ");
    }

    // ============================== End Intro display section ==============================


    // ============================== Begin Game Loop display section ==============================

    public final void displayPlayerStatus(Hero theHero) {
        System.out.println(
            SEPERATOR_STRAIGHT 
            + NEWLINE
            + TEXT_YELLOW + theHero.getCharName() + TEXT_COLOR_RESET + " | " // character name
            + TEXT_GREEN + "HP: " + theHero.getHP() + theHero.getMaxHP() + TEXT_COLOR_RESET + " | " // Character hp count
            + TEXT_PURPLE + "Potions: Hx" + theHero.getHealingPotion() + " " 
            + "Vx" + theHero.getVisionPotion() + TEXT_COLOR_RESET + " | " // number of player hp potions and vision potions
            + TEXT_BLUE + "Pillars: " + TEXT_COLOR_RESET
            + NEWLINE 
            + SEPERATOR_STRAIGHT 
            + NEWLINE
        );
    }

    public final void displayMenu() {
        System.out.print(
            TEXT_BLUE
            + "[W/A/S/D] Move"
            + "[H] Use healing potion"
            + "[V] Use vision potion"
            + "[B] Help"
            + "[Q] Quit"
            + TEXT_COLOR_RESET
        );
    }

    public final void displayRoom() { //parameter/s: room
    
    }

    // ============================== End Game Loop display section ==============================
 

    // ============================== Begin Extra Room display section ==============================

    public final void displayRoomEvent() { // Some parameter that indecates what is in the room

    }

    /**
     * Displays a 3x3 grid of the rooms surrounding the player when they
     * drink a vision potion.
     */
    public final void displayVisionPotionView() { //parameter/s: room

    }

    /**
     * Displays all the rooms in the dungeon for the player to see.
     * This will display when the game end either through losing or 
     * winning the game. Also used for debugging.
     * 
     * 
     */
    public final void displayFullDungeon() { //parameter/s: dungeon (I think)
        System.out.println("Full dungeon map: ");
    }

    // ============================== End Extra Room Display Section ==============================


    // ============================== Begin Combat Display Section ==============================

    public final void displayCombat() { //parameter/s: hero, monster

    }

    public final void displayCombatRound(String theRound) {

    }

    // ==============================   End Combat Display Section   ==============================


    // ============================== Begin End Game Display Section ==============================

    public final void displayDeath() {
        System.out.println(
            TEXT_RED + " You are dead" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_RED + "DEFEAT" + TEXT_COLOR_RESET
            + NEWLINE
        );
        displayFullDungeon();

    }

    public final void displayWin() {
        System.out.println(
            TEXT_GREEN + "You escaped the dungeon!" + TEXT_COLOR_RESET
            + NEWLINE
            + TEXT_YELLOW + "VICTORY" + TEXT_COLOR_RESET
            + NEWLINE
        );
        displayFullDungeon();
    }

    // ==============================  End End Game Display Section  ==============================

    // ==============================  Begin Debug Display Section  ==============================

    // ==============================  End Debug Display Section  ==============================

}
