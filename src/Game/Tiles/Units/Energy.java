package Game.Tiles.Units;

public class Energy extends Resource{
    private int cost;

    public Energy(String name, int pool, int amount) {
        super(name, pool, amount);
    }

    // return the cost amount of the player
    public int getCost() {
        return cost;
    }

    // set the cost amount of the player
    public void setCost(int cost) {
        this.cost = cost;
    }

    // return the health describes by a string
    public String toString(){return "cost:" + getCost();}
}
