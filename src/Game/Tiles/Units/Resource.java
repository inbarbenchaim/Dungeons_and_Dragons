package Game.Tiles.Units;

public class Resource {
    private String Name;
    private int Pool;
    private int Amount;

    public Resource(String name, int pool, int amount) {
        this.Name = name;
        this.Pool = pool;
        this.Amount = amount;
    }

    // return the amount of the player
    public int getAmount() {
        return Amount;
    }

    // set the amount of the player
    public void setAmount(int amount) {
        Amount = amount;
    }

    // set the pool of the player
    public void setPool(int pool) {
        Pool = pool;
    }

    // return the pool of the player
    public int getPool() {
        return Pool;
    }

    // reduce the amount points by the damage that done
    public void reduceAmount(int damageDone) {
    }

    // add the health gained to this player health
    // Check that health gained isn't over the max health
    public void addCapacity(int healthGained) {
        if (healthGained <= getPool()) {
            setAmount(healthGained);
        }
        else
            setAmount(getPool());
    }

    // check if the player is dead
    public boolean isDead() {
        return getAmount() <= 0;
    }
}