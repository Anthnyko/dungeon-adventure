package dungeon.characters;

public abstract class DungeonCharacter {

    protected String myCharName;

    protected int myHP;

    protected int myMinDamage;

    protected int myMaxDamage;

    protected int myAttackSpeed;

    protected double myHitChance;

    protected double myDodgeChance;

    protected int myMinHeal;

    protected int myMaxHeal;

    protected DungeonCharacter(String theName, int theHP, int theMinDamage, int theMaxDamage, int theAttackSpeed,
                               double theHitChance, double theDodgeChance, int theMinHeal, int theMaxHeal) {
        myCharName = theName;
        myHP = theHP;
        myMinDamage = theMinDamage;
        myMaxDamage = theMaxDamage;
        myAttackSpeed = theAttackSpeed;
        myHitChance = theHitChance;
        myDodgeChance = theDodgeChance;
        myMinHeal = theMinHeal;
        myMaxHeal = theMaxHeal;
    }

    public void attack(DungeonCharacter target) {
        // TODO: implement in later iteration.
    }

    public boolean isAlive() {
        // TODO: implement in later iteration.
        return false;
    }

    public void takeDamage(int damage) {
        // TODO: implement in later iteration.
    }


    public String getCharName() {
        return myCharName;
    }

    public int getMyHP() {
        return myHP;
    }

    public int getMyMinDamage() {
        return myMinDamage;
    }

    public int getMyMaxDamage() {
        return myMaxDamage;
    }

    public int getMyAttackSpeed() {
        return myAttackSpeed;
    }

    public double getMyHitChance() {
        return myHitChance;
    }

    public double getMyDodgeChance() {
        return myDodgeChance;
    }

    public int getMyMinHeal() {
        return myMinHeal;
    }

    public int getMyMaxHeal() {
        return myMaxHeal;
    }
}
