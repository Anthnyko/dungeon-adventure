package dungeon.model.items;

import dungeon.characters.Hero;

/**
 * Abstract item class
 *
 * @author Ibrahim Mohamud
 * @version 1.0
 */
public abstract class Item {

    /**
     * Applies the effect of the item onto the hero
     */
    public abstract void applyEffect();

    /**
     * Gets the symbol of the item
     *
     * @return the character symbol of the item
     */
    public abstract char getSymbol();
}
