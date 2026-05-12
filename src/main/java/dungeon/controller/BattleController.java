package dungeon.controller;

import dungeon.model.characters.Monster;
import dungeon.view.DungeonView;
import dungeon.model.characters.Hero;
import java.util.Scanner;

/**
 * Controls the flow of a single combat encounter between a Hero and a Monster.
 * This class manages turn order, ability selection, cooldown reduction,
 * bleed processing, and win/lose detection. It acts as the combat subsystem
 * controller and is invoked by the main game controller when combat begins.
 */
public class BattleController {

    /** The hero participating in the battle. */
    private final Hero myHero;

    /** The monster the hero is fighting. */
    private Monster myMonster;

    /** Tracks whether the battle has ended. */
    private boolean myBattleOver;

    /** Tracks round number during combat. */
    private int myRound;

    /**
     * Handles all combat-related text output, including round banners,
     * action prompts, and status displays for both the hero and monster.
     * This view is created at the start of each battle and used throughout
     * the combat loop to present information to the player.
     */
    private DungeonView myDungeonView;

    /**
     * Shared Scanner instance used to read player input during combat.
     */
    private final Scanner myScanner = new Scanner(System.in);


    /**
     * Constructs a GameController to manage a combat encounter between
     * the given hero and monster.
     *
     * @param theHero the hero controlled by the player.
     */
    public BattleController(final Hero theHero) {
        myHero = theHero;
    }

    public int getMyRound() {
        return myRound;
    }

    /**
     * Begins the combat loop and continues until either the hero or the monster
     * is defeated. Handles turn sequencing and end-of-round effects.
     *
     * @param theMonster the monster the hero is fighting
     */
    public void startBattle(final Monster theMonster) {
        myMonster = theMonster;
        myBattleOver = false;
        myRound = 1;
        myDungeonView = new DungeonView();
        System.out.println("A wild " + myMonster.getCharName() + " appears!");

        while (!myBattleOver) {
            myDungeonView.displayCombatRound(myRound);
            heroTurn();
            if (isBattleOver()) break;

            monsterTurn();
            if (isBattleOver()) break;

            processEndOfRoundEffects();
            myRound++;
        }
        myHero.resetStatusEffects();
    }

    /**
     * Returns whether the hero is still alive.
     * This is a convenience wrapper used by external systems
     * (such as DungeonAdventure) to check combat outcome without
     * directly accessing the hero object.
     *
     * @return true if the hero's HP is above zero, false otherwise
     */
    public boolean heroIsAlive() {
        return myHero.isAlive();
    }

    /**
     * Returns whether the monster is still alive.
     * This safely checks for null in case the monster has been removed
     * from the room after combat or was never assigned.
     *
     * @return true if a monster exists and its HP is above zero, false otherwise
     */
    public boolean monsterIsAlive() {
        return myMonster != null && myMonster.isAlive();
    }

    /**
     * Returns a compact summary of the battle outcome.
     * Useful for DungeonAdventure to display after combat ends.
     */
    public String getCombatSummary() {
        if (!myHero.isAlive()) {
            return "Hero defeated by " + myMonster.getCharName();
        }
        return "Hero defeated " + myMonster.getCharName();
    }

    /**
     * Prompts the player to choose a combat action using a Scanner.
     *
     * @return the validated action choice entered by the player
     */
    private int getHeroActionChoice() {
        int choice = -1;

        while (choice < 1 || choice > 4) {
            try {
                myDungeonView.promptActionChoice(myHero);
                final String input = myScanner.nextLine().trim();
                choice = Integer.parseInt(input);

                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid input: Please enter a choice between 1-4.");
                }
            } catch (final NumberFormatException e) {
                System.out.println("Invalid input: Please enter a number between 1-4.");
            }
        }
        return choice;
    }

    /**
     * Executes the hero's turn, including status display, ability selection,
     * ability execution, extra-turn handling (Thief Surprise Attack), and
     * cooldown reduction.
     */
    private void heroTurn() {
        System.out.println("\n--- Hero Turn ---");
        myHero.displayStatus();
        myMonster.displayStatus();

        final int choice = getHeroActionChoice();
        myHero.performAction(choice, myMonster);

        // Extra turn mechanic (Thief only)
        if (myHero.hasExtraTurn()) {
            System.out.println(myHero.getCharName() + " gains an extra turn!");
            myHero.consumeExtraTurn();
            heroTurn();
        }
    }

    /**
     * Executes the monster's turn by performing its attack on the hero.
     */
    private void monsterTurn() {
        System.out.println("\n--- Monster Turn ---");
        myMonster.attack(myHero);
    }

    /**
     * Processes end-of-round effects such as bleed damage on the monster.
     */
    private void processEndOfRoundEffects() {
        myHero.reduceCooldown();
        myMonster.processBleed();
    }

    /**
     * Checks whether the battle has ended due to either the hero or the monster
     * reaching zero HP. Prints the appropriate victory or defeat message.
     *
     * @return true if the battle is over, false otherwise
     */
    private boolean isBattleOver() {
        if (!myHero.isAlive()) {
            System.out.println("You have been defeated...");
            myBattleOver = true;
        } else if (!myMonster.isAlive()) {
            System.out.println("You defeated the " + myMonster.getCharName() + "!");
            myHero.reduceCooldown();
            myBattleOver = true;
        }
        return myBattleOver;
    }
}