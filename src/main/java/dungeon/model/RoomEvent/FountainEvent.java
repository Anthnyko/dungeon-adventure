package dungeon.model.RoomEvent;

import dungeon.model.Dungeon;
import dungeon.model.characters.Hero;

import java.util.Random;

/**
 * Represents a healing fountain event in a dungeon room.
 * When triggered, the hero is healed upon drinking from the fountain.
 *
 * @author Ibrahim
 * @version 1.0
 */
public class FountainEvent implements RoomEvent {

    /** The percentage chance a monster spawns after a heal or potion effect */
    private static final int MONSTER_SPAWN_CHANCE = 50;

    /** The amount of damage dealt per turn by the mind effect */
    private static final int MIND_DAMAGE = 2;

    /** The number of turns the mind effect lasts */
    private static final int MIND_DURATION = 6;

    private final Random myRandom = new Random();

    /**
     * Triggers the fountain, healing the hero for 40 hit points.
     *
     * @param theHero the hero to apply the healing on
     * @return "FOUNTAIN" to identify this event type to the controller
     */
    @Override
    public String trigger(final Hero theHero, final Dungeon theDungeon) {
        int effectCount;
        if (theHero.getPillarCount() < 4) {
            effectCount = 4;
        } else {
            effectCount = 3;
        }

        int effect = myRandom.nextInt(effectCount);
        switch (effect) {
            case 0 -> { // give the hero a full heal
                theHero.applyHeal(theHero.getMaxHP());
                if (myRandom.nextInt(100) < MONSTER_SPAWN_CHANCE) {
                    theDungeon.spawnMonsterAtHero();
                }
                return "FOUNTAIN_HEAL";
            }
            case 1 -> { // teleport the hero to a random location in the dungeon
                theDungeon.teleportHeroRandom();
                return "FOUNTAIN_TELEPORT";
            }
            case 2 -> { // grant two random potions
                grantRandomPotions(theHero);
                if (myRandom.nextInt(100) < MONSTER_SPAWN_CHANCE) {
                    theDungeon.spawnMonsterAtHero();
                }
                return "FOUNTAIN_POTION";
            }
            case 3 -> {
                theDungeon.revealRandomPillar();
                theHero.takeTickDamage(MIND_DAMAGE, MIND_DURATION);
                return "FOUNTAIN_MIND";
            }
        }
        return "FOUNTAIN";
    }

    private void grantRandomPotions(final Hero theHero) {
        for (int i = 0; i < 2; i++) {
            if (myRandom.nextBoolean()) {
                theHero.gainHealingPotion();
                //System.out.println("Gained +1 healing potion!");
            } else {
                theHero.gainVisionPotion();
                //System.out.println("Gained +1 vision potion!");
            }
        }
    }
}
