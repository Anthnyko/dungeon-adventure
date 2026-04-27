package dungeon.characters;

public class Warrior extends Hero {

    public Warrior(String theName) {
        super(theName, 125, 35, 60, 4, 0.8, 0.2);
    }

    @Override
    public void specialSkill(DungeonCharacter theTarget) {
        crushingBlow(theTarget);
    }

    @Override
    public void bigCooldown(DungeonCharacter theTarget) {
        enrage(theTarget);
    }

    private void crushingBlow(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }

    private void enrage(DungeonCharacter theTarget) {
        // TODO: implement in later iteration.
    }
}
