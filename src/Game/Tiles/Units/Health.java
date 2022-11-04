package Game.Tiles.Units;

public class Health  extends Resource{

    public Health(int healthAmount, int healthPool) {
        super("Health", healthAmount, healthPool);
    }

    // return the Health Amount of the player
    public int getHealthAmount() {
        return super.getAmount();
    }

    // set the Health Amount of the player
    public int getHealthPool() {
        return super.getPool();
    }

    // return the health describes by a string
    public String toString() {
        return "health amount:" + getHealthAmount() + ", health pool:" + getHealthPool();
    }
}
