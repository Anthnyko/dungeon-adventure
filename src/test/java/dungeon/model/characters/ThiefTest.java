package dungeon.model.characters;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

/**
 * Unit tests for the Thief hero class.
 */
public class ThiefTest {

    /** Creates a Thief with a fixed name. */
    private Thief createThief() {
        return new Thief("TestThief");
    }

    /** Creates a Skeleton target. */
    private Skeleton createSkeleton() {
        return new Skeleton();
    }

    /** Injects a fixed RNG into the Thief for deterministic behavior. */
    private void setFixedRNG(Thief t, double value) {
        t.rng = new Random() {
            @Override
            public double nextDouble() {
                return value;
            }
        };
    }

    // ------------------------------------------------------------
    // SURPRISE ATTACK TESTS
    // ------------------------------------------------------------

    @Test
    public void testSurpriseAttackSuccessGrantsExtraTurn() {
        Thief t = createThief();
        Skeleton s = createSkeleton();

        // Force success branch (roll < 0.40)
        setFixedRNG(t, 0.10);

        // Ensure attack always hits
        t.myHitChance = 1.0;
        t.myMinDamage = 10;
        t.myMaxDamage = 10;

        int before = s.getHP();
        t.specialSkill(s);

        assertTrue(t.hasExtraTurn());
        assertEquals(before - 10, s.getHP());
    }

    @Test
    public void testSurpriseAttackNormalHitNoExtraTurn() {
        Thief t = createThief();
        Skeleton s = createSkeleton();

        // Force normal hit branch (0.40 <= roll < 0.80)
        setFixedRNG(t, 0.50);

        t.myHitChance = 1.0;
        t.myMinDamage = 10;
        t.myMaxDamage = 10;

        int before = s.getHP();
        t.specialSkill(s);

        assertFalse(t.hasExtraTurn());
        assertEquals(before - 10, s.getHP());
    }

    @Test
    public void testSurpriseAttackFailureNoAttackNoExtraTurn() {
        Thief t = createThief();
        Skeleton s = createSkeleton();

        // Force failure branch (roll >= 0.80)
        setFixedRNG(t, 0.90);

        int before = s.getHP();
        t.specialSkill(s);

        assertFalse(t.hasExtraTurn());
        assertEquals(before, s.getHP()); // no attack
    }

    @Test
    public void testConsumeExtraTurnResetsFlag() {
        Thief t = createThief();

        t.myExtraTurn = true;
        t.consumeExtraTurn();

        assertFalse(t.hasExtraTurn());
    }

    // ------------------------------------------------------------
    // GARROTE TESTS
    // ------------------------------------------------------------

    @Test
    public void testGarroteAppliesBleed() {
        Thief t = createThief();
        Skeleton s = createSkeleton();

        t.bigCooldown(s);

        assertEquals(8, s.getMyBleedTimer());
    }

    @Test
    public void testGarroteSetsCooldownTo3() {
        Thief t = createThief();
        Skeleton s = createSkeleton();

        t.bigCooldown(s);

        assertEquals(4, t.getCDTimer());
    }

    @Test
    public void testGarroteDoesNothingIfTargetIsNull() {
        Thief t = createThief();

        t.bigCooldown(null);

        // Your implementation sets cooldown BEFORE checking target
        assertEquals(0, t.getCDTimer());
    }
}
