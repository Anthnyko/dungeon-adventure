package dungeon.model.characters;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Unit tests for the Warrior hero class.
 */
public class WarriorTest {

    /** Creates a Warrior with a fixed name. */
    private Warrior createWarrior() {
        return new Warrior("TestWarrior", 0, 0);
    }

    /** Creates a Skeleton target. */
    private Skeleton createSkeleton() {
        return new Skeleton();
    }

    /** Injects a fixed RNG into the Warrior for deterministic behavior. */
    private void setFixedRNG(Warrior w, double value) {
        w.rng = new Random() {
            @Override
            public double nextDouble() {
                return value;
            }
        };
    }

    /**
     * A deterministic Random implementation that returns a predefined sequence
     * of double values. Each call to nextDouble() returns the next value in the
     * sequence, allowing unit tests to control multiple RNG calls in order.
     */
    private static class SequenceRNG extends Random {
        private final double[] values;
        private int index = 0;

        SequenceRNG(double... values) {
            this.values = values;
        }

        @Override
        public double nextDouble() {
            return values[index++];
        }

        @Override
        public int nextInt(int bound) {
            return (int )values[index++];
        }
    }

    /** Simple high‑HP dummy target for testing. */
    private static class DummyTarget extends Monster {
        public DummyTarget() {
            super("Dummy", 1000, 1000, 0, 0,
                    4, 1.0, 0.0, 1000,
                    1000);
        }

        @Override
        public void attack(DungeonCharacter theTarget) {
            // Dummy does nothing
        }
    }

    // ------------------------------------------------------------
    // CRUSHING BLOW TESTS
    // ------------------------------------------------------------

    @Test
    public void testCrushingBlowHitsWhenRollBelow40Percent() {
        Warrior w = createWarrior();
        Skeleton s = createSkeleton();

        // Force success branch (roll < 0.40)
        setFixedRNG(w, 0.10);

        int before = s.getHP();
        w.specialSkill(s);

        assertTrue(s.getHP() < before);
    }

    @Test
    public void testCrushingBlowMissesWhenRollAbove40Percent() {
        Warrior w = createWarrior();
        Skeleton s = createSkeleton();

        // Force failure branch (roll >= 0.40)
        setFixedRNG(w, 0.90);

        int before = s.getHP();
        w.specialSkill(s);

        assertEquals(before, s.getHP());
    }

    @Test
    public void testCrushingBlowDealsDamageWithinRange() {
        Warrior w = createWarrior();
        Skeleton s = createSkeleton();

        w.rng = new SequenceRNG(0.10, 0.00);

        int before = s.getHP();
        w.specialSkill(s);

        int dmg = before - s.getHP();
        assertEquals(75, dmg); // minCrushDamage
    }

    // ------------------------------------------------------------
    // ENRAGE TESTS
    // ------------------------------------------------------------

    @Test
    public void testEnrageSetsTimers() {
        Warrior w = createWarrior();

        w.bigCooldown(null);

        assertEquals(2, w.getMyUltimateDuration());
    }

    @Test
    public void testEnrageBoostsAttackDamage() {
        Warrior w = createWarrior();
        Skeleton s = createSkeleton();

        // Force hit
        w.myHitChance = 1.0;

        // Force deterministic damage
        w.rng = new Random() {
            @Override
            public double nextDouble() { return 0.0; }
            @Override
            public int nextInt(int bound) { return 0; }
        };

        // Normal attack damage = minDamage = 35
        int before = s.getHP();
        w.attack(s);
        assertEquals(before - 35, s.getHP());

        // Activate Enrage
        w.bigCooldown(null);

        // Enraged attack damage = 35 + 10 = 45
        before = s.getHP();
        w.attack(s);
        assertEquals(before - 42, s.getHP());
    }

    @Test
    public void testEnrageDamageReturnsToNormalAfterTimerExpires() {
        Warrior w = createWarrior();
        DummyTarget d = new DummyTarget();

        w.myHitChance = 1.0;

        // Deterministic damage
        w.rng = new Random() {
            @Override
            public double nextDouble() { return 0.0; }
            @Override
            public int nextInt(int bound) { return 0; }
        };

        // Activate Enrage
        w.bigCooldown(null);

        // Use up all 3 turns
        w.attack(d);
        w.attack(d);
        w.attack(d);

        // Now EnrageTimer should be 0
        assertEquals(0, w.getMyUltimateDuration());

        // Damage should return to normal (35)
        int before = d.getHP();
        w.attack(d);
        assertEquals(before - 35, d.getHP());
    }
}
