package dungeon.model.characters;

import dungeon.model.Dungeon;
import org.junit.Test;
import static org.junit.Assert.*;

public class HeroTest {

    private Hero createHero() {
        return new Thief("TestHero");
    }

    @Test
    public void testHeroStartsWithZeroHealingPotions() {
        Hero h = createHero();
        assertEquals(3, h.getHealingPotion());
    }

    @Test
    public void testHealingPotionCannotExceedMaxHP() {
        Hero h = createHero();
        h.myHP = 70;
        h.myHealingPotions = 1;
        h.useHealingPotion();

        assertEquals(75, h.getHP());
    }

    @Test
    public void testUsingHealingPotionReducesPotionCount() {
        Hero h = createHero();
        h.myHealingPotions = 2;
        h.useHealingPotion();

        assertEquals(1, h.getHealingPotion());
    }

    @Test
    public void testCooldownStartsAtZero() {
        Hero h = createHero();
        assertEquals(0, h.getCDTimer());
    }

    @Test
    public void testReduceCooldownDecrementsTimer() {
        Hero h = createHero();
        h.myCDTimer = 3;
        h.reduceCooldown();

        assertEquals(2, h.getCDTimer());
    }

    @Test
    public void testReduceCooldownDoesNotGoBelowZero() {
        Hero h = createHero();
        h.myCDTimer = 0;
        h.reduceCooldown();
        assertEquals(0, h.getCDTimer());
    }

    @Test
    public void testHeroHasNoExtraTurnByDefault() {
        Hero h = createHero();
        assertFalse(h.hasExtraTurn());
    }

    @Test
    public void testConsumeExtraTurnDoesNotCrash() {
        Hero h = createHero();
        h.consumeExtraTurn();
        assertFalse(h.hasExtraTurn());
    }

    @Test
    public void testToStringContainsHeroName() {
        Hero h = createHero();
        String output = h.toString();
        assertTrue(output.contains("TestHero"));
    }
}
