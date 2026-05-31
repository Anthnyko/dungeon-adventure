package dungeon.model.items;



public class HealingPotion implements Item{
    /**
     * Gets the symbol representing this healing potion on the dungeon map.
     *
     * @return the character 'H' representing a healing potion
     */
    @Override
    public char getSymbol() {
        return 'H';
    }
}
