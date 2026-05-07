package dungeon.characters;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Priest hero class.
 */
public class PriestTest {

    /** Helper to create a Priest with a fixed name. */
    private Priest createPriest() {
        return new Priest("TestPriest");
    }

    /** Helper to create a Skeleton target. */
    private Skeleton createSkeleton() {
        return new Skeleton();
    }

    // ------------------------------------------------------------
    // HEAL TESTS (specialSkill)
    // ------------------------------------------------------------

    @Test
    public void testHealRestores20HP() {
        Priest p = createPriest();
        p.takeDamage(30); // HP = 45

        p.specialSkill(null); // heal()

        assertEquals(65, p.getHP());
    }

    @Test
    public void testHealClampsToMaxHP() {
        Priest p = createPriest();
        p.takeDamage(10); // HP = 65

        p.specialSkill(null); // heal 20 → clamp to 75

        assertEquals(75, p.getHP());
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
    public void testSmiteDeals50Damage() {
        Priest p = createPriest();
        Skeleton s = createSkeleton();

        int before = s.getHP();

        p.bigCooldown(s); // smite()

        assertEquals(before - 50, s.getHP());
    }

    @Test
    public void testSmiteSetsCooldownTo3() {
        Priest p = createPriest();
        Skeleton s = createSkeleton();

        p.bigCooldown(s);

        assertEquals(3, p.getCDTimer());
    }

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
