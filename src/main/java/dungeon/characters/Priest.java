package dungeon.characters;

public class Priest extends Hero {

    public Priest(String theName) {
        super(theName, 75, 25, 45, 5, 0.7, 0.3);
    }

    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        heal(theTarget);
    }

    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        smite(theTarget);
    }

    private void heal(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }

    private void smite(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }
}
