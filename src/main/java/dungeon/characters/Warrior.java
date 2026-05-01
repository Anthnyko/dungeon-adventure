package dungeon.characters;

/**
 * The Warrior hero class. Warriors specialize in high-damage melee
 * combat and rely on strength and resilience to overpower enemies.
 * This class defines the Warrior's base stats and provides stubs
 * for its special abilities, which are implemented in later iterations.
 *
 * The Warrior has two unique abilities:
 * - crushingBlow: the Warrior's special skill
 * - enrage: the Warrior's ultimate cooldown ability
 *
 * Combat logic is added in future iterations.
 *
 * @author Anthony
 * @version 1.0
 */
public class Warrior extends Hero {

    /**
     * Constructs a new Warrior with predefined combat attributes.
     *
     * @param theName the name of the Warrior
     */
    public Warrior(String theName) {
        super(theName,
                125,  // HP
                125,  // max HP
                35,   // min damage
                60,   // max damage
                4,    // attack speed
                0.8,  // hit chance
                0.2); // block chance (or dodge if you keep it)
    }

    /**
     * Performs the Warrior's special skill.
     * This method delegates to the private crushingBlow method.
     *
     * @param theTarget the target of the special skill
     */
    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        crushingBlow(theTarget);
    }

    /**
     * Performs the Warrior's ultimate ability.
     * This method delegates to the private enrage method.
     *
     * @param theTarget the target of the ultimate attack
     */
    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        enrage(theTarget);
    }

    /**
     * Executes the Warrior's crushing blow ability.
     * Logic implemented in later iterations.
     *
     * @param theTarget the target of the crushing blow
     */
    private void crushingBlow(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }

    /**
     * Executes the Warrior's enrage ability, used as the ultimate attack.
     * Logic implemented in later iterations.
     *
     * @param theTarget the target of the enrage attack
     */
    private void enrage(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }
}