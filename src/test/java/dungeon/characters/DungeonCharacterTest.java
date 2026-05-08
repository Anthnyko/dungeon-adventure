package dungeon.characters;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the DungeonCharacter abstract class.
 * Uses Skeleton (a Monster subclass) as the concrete implementation.
 */
public class DungeonCharacterTest {

    /**
     * Creates a simple Skeleton for testing.
     */
    private Skeleton createSkeleton() {
        return new Skeleton(); // 100 HP, 30–50 dmg, etc.
    }

    @Test
    public void testTakeDamageReducesHP() {
        Skeleton s = createSkeleton();
        int before = s.getHP();

        s.takeDamage(20);

        assertEquals(before - 20, s.getHP());
    }

    @Test
    public void testTakeDamageClampsToZero() {
        Skeleton s = createSkeleton();

        s.takeDamage(999);

        assertEquals(0, s.getHP());
        assertFalse(s.isAlive());
    }

    @Test
    public void testIsAlive() {
        Skeleton s = createSkeleton();

        assertTrue(s.isAlive());

        s.takeDamage(200);

        assertFalse(s.isAlive());
    }

    @Test
    public void testAttackDealsDamage() {
        Skeleton attacker = new Skeleton();
        Skeleton target = new Skeleton();

        attacker.myHitChance = 1.0; // force hit
        attacker.myMinDamage = 10;
        attacker.myMaxDamage = 10;

        int before = target.getHP();
        attacker.attack(target);

        assertEquals(before - 10, target.getHP());
    }

    @Test
    public void testAttackSpeedRatio() {
        Skeleton attacker = new Skeleton();
        Skeleton target = new Skeleton();

        attacker.myAttackSpeed = 4;
        target.myAttackSpeed = 2;

        attacker.myHitChance = 1.0;
        attacker.myMinDamage = 5;
        attacker.myMaxDamage = 5;

        int before = target.getHP();
        attacker.attack(target);

        // 4 / 2 = 2 attacks → 5 dmg each → 10 total
        assertEquals(before - 10, target.getHP());
    }

    @Test
    public void testAttackStopsWhenTargetDies() {
        Skeleton attacker = new Skeleton();
        Skeleton target = new Skeleton();

        attacker.myHitChance = 1.0;
        attacker.myMinDamage = 200;
        attacker.myMaxDamage = 200;

        attacker.attack(target);

        assertEquals(0, target.getHP());
        assertFalse(target.isAlive());
    }
}
