package dungeon.model.characters;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Monster class and its subclasses.
 * Uses Skeleton as the primary concrete Monster for testing.
 */
public class MonsterTest {

    /** Helper to create a clean Skeleton for testing. */
    private Skeleton createSkeleton() {
        return new Skeleton();
    }

    // ------------------------------------------------------------
    // HEALING TESTS
    // ------------------------------------------------------------

    @Test
    public void testHealDoesNotExceedMaxHP() {
        Skeleton s = createSkeleton();

        s.myHealChance = 1.0;
        s.myMinHeal = 50;
        s.myMaxHeal = 50;

        s.takeDamage(10);
        s.heal();

        assertEquals(80, s.getHP());
    }

    @Test
    public void testHealRestoresHP() {
        Skeleton s = createSkeleton();

        s.myHealChance = 1.0;
        s.myMinHeal = 20;
        s.myMaxHeal = 20;

        s.takeDamage(40);
        s.heal();

        assertEquals(60, s.getHP());
    }

    @Test
    public void testHealDoesNothingAtFullHP() {
        Skeleton s = createSkeleton();

        s.myHealChance = 1.0;
        s.myMinHeal = 50;
        s.myMaxHeal = 50;

        int before = s.getHP();
        s.heal();

        assertEquals(before, s.getHP());
    }

    @Test
    public void testHealFailsWhenChanceIsZero() {
        Skeleton s = createSkeleton();

        s.myHealChance = 0.0;
        s.myMinHeal = 50;
        s.myMaxHeal = 50;

        s.takeDamage(30);
        s.heal();

        assertEquals(50, s.getHP());
    }

    // ------------------------------------------------------------
    // BLEED TESTS
    // ------------------------------------------------------------

    @Test
    public void testApplyBleedSetsTimer() {
        Skeleton s = createSkeleton();

        s.applyBleed(3);

        assertEquals(3, s.getMyBleedTimer());
    }

    @Test
    public void testProcessBleedDealsDamage() {
        Skeleton s = createSkeleton();

        s.applyBleed(3);
        int before = s.getHP();

        s.processBleed();

        assertEquals(before - 5, s.getHP());
        assertEquals(2, s.getMyBleedTimer());
    }

    @Test
    public void testBleedStopsAtZero() {
        Skeleton s = createSkeleton();

        s.applyBleed(1);
        s.processBleed();
        s.processBleed();

        assertEquals(0, s.getMyBleedTimer());
    }

    @Test
    public void testBleedCanKillMonster() {
        Skeleton s = createSkeleton();

        s.applyBleed(10);
        s.takeDamage(95);

        s.processBleed();

        assertEquals(0, s.getHP());
        assertFalse(s.isAlive());
    }

    // ------------------------------------------------------------
    // HEAL + BLEED INTERACTION
    // ------------------------------------------------------------

    @Test
    public void testMonsterCannotHealWhenDead() {
        Skeleton s = createSkeleton();

        s.takeDamage(200);
        s.myHealChance = 1.0;

        s.heal();

        assertEquals(0, s.getHP());
    }

    @Test
    public void testBleedDoesNotTriggerHeal() {
        Skeleton s = createSkeleton();

        s.applyBleed(1);
        s.myHealChance = 1.0;
        s.myMinHeal = 50;
        s.myMaxHeal = 50;

        int before = s.getHP();
        s.processBleed();

        assertEquals(before - 5, s.getHP());
    }
}
