package dungeon.controller;

import dungeon.characters.Hero;
import dungeon.characters.Monster;

import java.util.Scanner;

/**
 * Controls the flow of a single combat encounter between a Hero and a Monster.
 * This class manages turn order, ability selection, cooldown reduction,
 * bleed processing, and win/lose detection. It acts as the combat subsystem
 * controller and is invoked by the main game controller when combat begins.
 */
public class GameController {

    /** The hero participating in the battle. */
    private final Hero myHero;

    /** The monster the hero is fighting. */
    private final Monster myMonster;

    /** Tracks whether the battle has ended. */
    private boolean myBattleOver;

    /**
     * Constructs a GameController to manage a combat encounter between
     * the given hero and monster.
     *
     * @param theHero the hero controlled by the player
     * @param theMonster the monster the hero is fighting
     */
    public GameController(final Hero theHero, final Monster theMonster) {
        myHero = theHero;
        myMonster = theMonster;
        myBattleOver = false;
    }

    /**
     * Begins the combat loop and continues until either the hero or the monster
     * is defeated. Handles turn sequencing and end-of-round effects.
     */
    public void startBattle() {
        System.out.println("A wild " + myMonster.getCharName() + " appears!");

        while (!myBattleOver) {
            heroTurn();
            if (isBattleOver()) break;

            monsterTurn();
            if (isBattleOver()) break;

            processEndOfRoundEffects();
        }
    }

    /**
     * Prompts the player to choose a combat action using a Scanner.
     *
     * @return the validated action choice entered by the player
     */
    private int getHeroActionChoice() {
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        while (choice < 1 || choice > 4) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Basic Attack");
            System.out.println("2. Special Skill");
            System.out.println("3. Ultimate");
            System.out.print("Enter choice: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                sc.next(); // consume invalid token
                System.out.println("Invalid input. Please enter a number 1–3.");
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

        int choice = getHeroActionChoice();
        myHero.performAction(choice, myMonster);

        // Extra turn mechanic (Thief only)
        if (myHero.hasExtraTurn()) {
            System.out.println(myHero.getCharName() + " gains an extra turn!");
            myHero.consumeExtraTurn();
            heroTurn();
            return;
        }

        myHero.reduceCooldown();
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
            myBattleOver = true;
        }
        return myBattleOver;
    }
}