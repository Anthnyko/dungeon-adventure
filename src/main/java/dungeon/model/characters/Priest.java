package dungeon.model.characters;

/**
 * The Priest hero class. Priests specialize in supportive magic,
 * focusing on healing and holy damage. This class defines the
 * Priest's base stats and provides stubs for its special abilities,
 * which are implemented in later iterations.
 * <p>
 * The Priest has two unique abilities:
 * - heal: the Priest's special skill (typically self-targeted)
 * - smite: the Priest's ultimate cooldown ability
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
                85,   // HP
                85,   // max HP
                25,   // min damage
                45,   // max damage
                5,    // attack speed
                0.7,  // hit chance
                0.3); // block chance
    }

    /**
     * Performs the Priest's special skill.
     * This method delegates to the private heal method.
     */
    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        heal();
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

    /** Returns Priest's special skill name. */
    @Override
    public String getSpecialSkillName() {
        return "Heal";
    }

    /** Returns Priest's ultimate skill name. */
    @Override
    public String getUltimateName() {
        return "Smite";
    }

    /**
     * Executes the Priest's healing ability.
     */
    private void heal() {
        final int heal = 40;

        if (heal + myHP > myMaxHP) {
            myLastHeal = myMaxHP - myHP;
            myHP = myMaxHP;
        } else {
            myHP += heal;
            myLastHeal = 0;
        }
        myLastDamageDealt = 0;
        System.out.println(myCharName + " casts heal!\n Regenerates " + heal + " health!");
    }

    /**
     * Executes the Priest's smite ability, used as the ultimate attack.
     *
     * @param theTarget the target of the smite attack
     */
    private void smite(DungeonCharacter theTarget) {
        if (theTarget == null || !theTarget.isAlive()) {
            return;
        }

        final int smiteDmg = 70;

        System.out.println(myCharName + " casts smite!");
        myCDTimer = 3;

        myLastDamageDealt = smiteDmg;
        myLastHeal = 0;

        theTarget.takeDamage(smiteDmg);
    }
}
