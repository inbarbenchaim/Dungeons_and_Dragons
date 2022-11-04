package Game.Tiles.Units.Player;

import Game.Handelers.TargetHandler;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Tile;

import java.util.List;

public class Rogue extends Player {
    private int currentEnergy;
    private int cost;
    private final int MAX_ENERGY=100;
    private final String ABILITY_NAME="Fan of Knives";
    private final int ABILITY_RANGE=2;

    public Rogue(String name, int health,int attack,int defense,int cost){
        super(name,health,attack,defense);
        this.cost=cost;
        currentEnergy=MAX_ENERGY;
    }

    // the Rogue cast his ability
    @Override
    public void castAbility(){
        List<Enemy> potenTarget= TargetHandler.candidateTarget(this,this.getPosition(),this.ABILITY_RANGE);
        messageCallback.send(String.format("%s cast %s.",this.getName(), this.ABILITY_NAME));
        for(Enemy target:potenTarget){
            this.castAbility(target,this.attack);
        }
        this.currentEnergy-=cost;
    }

    // the Rogue try to cast his ability
    public void tryCastAbility(){
        boolean cast=super.tryCastAbility(currentEnergy,cost);
        if(!cast)
            messageCallback.send(String.format("%s tried to cast %s , but there was not enough energy: %d/%d.",this.getName(), this.ABILITY_NAME, currentEnergy, cost));
    }

    // level up for this Rogue
    @Override
    public void levelUp(){
        super.levelUp();
        this.currentEnergy=MAX_ENERGY;
        this.attack = (this.attack+this.getLevel()*3);
    }

    // this Rogue makes a turn
    @Override
    public void turn(int turnCount){
        super.turn(turnCount);
        currentEnergy=Math.min(MAX_ENERGY,currentEnergy+10);
    }

    // the Rogue describe
    public String describe() {
        return super.describe()+"\t\tEnergy: "+currentEnergy+"/"+MAX_ENERGY;
    }

    // a copy of the Rogue
    public Rogue copy()
    {
        return new Rogue(this.getName(), this.getHealth().getAmount(),this.attack,this.getDefense(),this.cost);
    }

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

    public void visit(Tile tile) {
        tile.accept(this);
    }
}
