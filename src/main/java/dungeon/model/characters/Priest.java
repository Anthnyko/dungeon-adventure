package dungeon.model.characters;

/**
 * The Priest hero class. Priests specialize in supportive magic,
 * focusing on healing and holy damage. This class defines the
 * Priest's base stats and provides stubs for its special abilities,
 * which are implemented in later iterations.
 *
 * The Priest has two unique abilities:
 * - heal: the Priest's special skill (typically self-targeted)
 * - smite: the Priest's ultimate cooldown ability
 *
 * Combat logic is added in future iterations.
 *
 * @author Anthony
 * @version 1.0
 */
public class Priest extends Hero {

    /**
     * Constructs a new Priest with predefined combat attributes.
     *
     * @param theName the name of the Priest
     */
    public Priest(String theName) {
        super(theName,
                75,   // HP
                75,   // max HP
                25,   // min damage
                45,   // max damage
                5,    // attack speed
                0.7,  // hit chance
                0.3); // block chance
    }

    /**
     * Performs the Priest's special skill.
     * This method delegates to the private heal method.
     *
     * @param theTarget the target of the special skill
     */
    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        heal(theTarget);
    }

    /**
     * Performs the Priest's ultimate ability.
     * This method delegates to the private smite method.
     *
     * @param theTarget the target of the ultimate attack
     */
    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        smite(theTarget);
    }

    /**
     * Executes the Priest's healing ability.
     * Logic implemented in later iterations.
     *
     * @param theTarget the target of heal
     */
    private void heal(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }

    /**
     * Executes the Priest's smite ability, used as the ultimate attack.
     * Logic implemented in later iterations.
     *
     * @param theTarget the target of the smite attack
     */
    private void smite(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }
}
