package dungeon.controller;

import dungeon.model.characters.Monster;
import dungeon.view.DungeonView;
import dungeon.model.characters.Hero;

import java.util.ArrayList;
import java.util.List;
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
     * Logs all actions during combat allowing for better user readability during combat.
     */
    private final List<String> myCombatLog = new ArrayList<>();

    private static final String NEWLINE = System.lineSeparator();


    /**
     * Constructs a GameController to manage a combat encounter between
     * the given hero and monster.
     *
     * @param theHero the hero controlled by the player.
     */
    public BattleController(final Hero theHero) {
        myHero = theHero;
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
        myHero.setAttackLogger(this::log);
        myMonster.setAttackLogger(this::log);

        int myRound = 1;

        myDungeonView = new DungeonView();
        myCombatLog.clear();

        while (!myBattleOver) {
            System.out.println(myDungeonView.displayCombatRound(myRound));
            log(myDungeonView.displayCombatRound(myRound));
            heroTurn();
            if (isBattleOver()) break;

            monsterTurn();
            if (isBattleOver()) break;

            processEndOfRoundEffects();
            myRound++;
        }
        myHero.resetStatusEffects();
        log(getCombatSummary());
        displayCombatLog();
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
            return myDungeonView.displayPlayerName(myHero) + " is defeated by " + myDungeonView.displayMonsterName(myMonster);
        }
        return myDungeonView.displayPlayerName(myHero) + " defeated " + myDungeonView.displayMonsterName(myMonster);
    }

    /**
     * Prompts the player to choose a combat action using a Scanner.
     *
     * @return the validated action choice entered by the player
     */
    private int getHeroActionChoice() {
        int choice = -1;
        myDungeonView.promptActionChoice(myHero);

        while (choice < 1 || choice > 4) {
            try {
                final String input = myScanner.nextLine().trim();
                choice = Integer.parseInt(input);

                if (choice < 1 || choice > 4) {
                    System.out.print(
                        "Invalid input: Please enter a choice between 1-4."
                        + NEWLINE
                        + "> Enter choice: "
                    );
                    choice = -1;
                } else if (choice == 3 && myHero.getCDTimer() > 0) {
                    System.out.print(
                        "Your ultimate is on cooldown!"
                        + NEWLINE
                        + "> Enter choice: "
                    );
                    choice = -1;
                } else if (choice == 4 && myHero.getHealingPotion() <= 0) {
                    System.out.print(
                        "You have no healing potions!"
                        + NEWLINE
                        + "> Enter choice: "
                    );
                    choice = -1;
                }
            } catch (final NumberFormatException e) {
                System.out.print(
                    "Invalid input: Please enter a number between 1-4."
                    + NEWLINE
                    + "> Enter choice: "
                );
                choice = -1;
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
        myDungeonView.displayHeroTurn();
        myDungeonView.displayHeroCombatStatus(myHero);
        myDungeonView.displayMonsterStatus(myMonster);

        final int choice = getHeroActionChoice();
        myHero.performAction(choice, myMonster);

        switch (choice) {
            case 2:
                logAbility(myHero.getSpecialSkillName());
                break;

            case 3:
                logAbility(myHero.getUltimateName());
                break;

            case 4:
                log(myDungeonView.displayPlayerName(myHero) + " +" + myHero.getLastHeal() + " HP");
                break;
        }

        // Monster healing after taking damage
        if (myHero.getLastDamageDealt() > 0) {
            myMonster.heal();
            int heal = myMonster.getLastHeal();
            if (heal > 0) {
                log(myDungeonView.displayMonsterName(myMonster) + " regenerates +" + heal + " HP");
            }
        }

        // Extra turn mechanic (Thief only)
        if (myHero.hasExtraTurn()) {
            log(myDungeonView.displayPlayerName(myHero) + " gains extra turn");
            System.out.println(myDungeonView.displayPlayerName(myHero) + " gains an extra turn!");
            myHero.consumeExtraTurn();
            heroTurn();
        }

    }

    /**
     * Executes the monster's turn by performing its attack on the hero.
     */
    private void monsterTurn() {
        myDungeonView.displayMonsterTurn();

        // 1. Bleed happens BEFORE monster attacks
        if (bleedPhase()) {
            return;
        }

        myMonster.attack(myHero);
    }

    /**
     * Helper method for logging bleed damage on monsters.
     */
    private boolean bleedPhase() {
        int bleed = myMonster.processBleed();
        if (bleed > 0) {
            log(myDungeonView.displayMonsterName(myMonster) + " • Bleed (-" + bleed + ")");
        }
        return !myMonster.isAlive();
    }

    /**
     * Processes end-of-round effects such as bleed damage on the monster.
     */
    private void processEndOfRoundEffects() {
        myHero.tickCooldowns();
    }

    /**
     * Checks whether the battle has ended due to either the hero or the monster
     * reaching zero HP. Prints the appropriate victory or defeat message.
     *
     * @return true if the battle is over, false otherwise
     */
    private boolean isBattleOver() {
        if (!heroIsAlive()) {
            log(myDungeonView.displayPlayerName(myHero) + " falls in battle");
            System.out.println("You have been defeated...");
            myBattleOver = true;
        } else if (!myMonster.isAlive()) {
            if (myMonster.diedFromBleed()) {
                log(myDungeonView.displayMonsterName(myMonster) + " dies from bleeding");
            } else {
                log(myDungeonView.displayMonsterName(myMonster) + " dies");
            }
            System.out.println("You defeated the " + myDungeonView.displayMonsterName(myMonster) + "!");
            myHero.reduceCooldown();
            myBattleOver = true;
        }
        return myBattleOver;
    }

    /**
     * Logs a preset message from actions.
     *
     * @param theEntry preset log message
     */
    private void log(final String theEntry) {
        myCombatLog.add(theEntry);
    }

    /**
     * Displays entire combat log during battle and when combat ends.
     */
    private void displayCombatLog() {
        myDungeonView.displayCombatLog();
        for (String entry : myCombatLog) {
            System.out.println(entry);
        }
        myDungeonView.endSectionBlue();
    }

    /**
     * Helper method for logging hero abilities, accounts for non-lethal abilities as well.
     *
     * @param abilityName name of the ability
     */
    private void logAbility(String abilityName) {
        int dmg = myHero.getLastDamageDealt();
        int heal = myHero.getLastHeal();

        if (dmg > 0) {
            log(myDungeonView.displayPlayerName(myHero) + " :: " + abilityName + " -> " + myDungeonView.displayMonsterName(myMonster) + " (-" + dmg + ")");
        } else if (heal > 0) {
            log(myDungeonView.displayPlayerName(myHero) + " :: " + abilityName + " (+" + heal + " HP)");
        } else {
            log(myDungeonView.displayPlayerName(myHero) + " :: " + abilityName + " (effect applied)");
        }
    }
}