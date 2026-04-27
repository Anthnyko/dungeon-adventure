package dungeon.characters;

public class Thief extends Hero {

    public Thief(String theName) {
        super(theName, 75, 20, 40, 5, 0.7, 0.3);
    }

    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        surpriseAttack(theTarget);
    }

    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        garrote(theTarget);
    }

    private void surpriseAttack(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }

    private void garrote(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }
}
