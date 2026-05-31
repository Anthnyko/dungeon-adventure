package dungeon.model.items;

public class VisionPotion implements Item{
    /**
     * Gets the symbol representing this vision potion on the dungeon map.
     *
     * @return the character 'V' representing a vision potion
     */
    @Override
    public char getSymbol() {
        return 'V';
    }
}
