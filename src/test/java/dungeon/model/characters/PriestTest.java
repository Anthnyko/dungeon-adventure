package dungeon.model.characters;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Priest hero class.
 */
public class PriestTest {

    /** Helper to create a Priest with a fixed name. */
    private Priest createPriest() {
        return new Priest("TestPriest", 0, 0);
    }

    /** Helper to create a Skeleton target. */
    private Skeleton createSkeleton() {
        return new Skeleton();
    }

    // ------------------------------------------------------------
    // HEAL TESTS (specialSkill)
    // ------------------------------------------------------------

    @Test
    public void testHealClampsToMaxHP() {
        Priest p = createPriest();
        p.takeDamage(10);

        p.specialSkill(null);

        assertEquals(100, p.getHP());
    }

    @Test
    public void testHealDoesNothingIfAlreadyFullHP() {
        Priest p = createPriest();

        int before = p.getHP();
        p.specialSkill(null);

        assertEquals(before, p.getHP());
    }

    // ------------------------------------------------------------
    // SMITE TESTS (bigCooldown)
    // ------------------------------------------------------------

    @Test
    public void testSmiteDoesNothingIfTargetIsNull() {
        Priest p = createPriest();

        p.bigCooldown(null);

        assertEquals(0, p.getCDTimer());
    }

    @Test
    public void testSmiteDoesNothingIfTargetIsDead() {
        Priest p = createPriest();
        Skeleton s = createSkeleton();

        s.takeDamage(999); // dead

        int before = s.getHP();
        p.bigCooldown(s);

        assertEquals(before, s.getHP());
    }
}
