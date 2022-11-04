package Game.Tiles.Units.Player;

import Game.Handelers.TargetHandler;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;

import java.util.List;

public class Hunter extends Player {

    private int arrowCount = 0;
    private int tickCount = 0;
    private final int ARROW_COST = 1;
    private final String ABILITY_NAME="Fan of Knives";
    private int ABILITY_RANGE;

    public Hunter(String name, int health, int attack, int defense, int range) {
        super(name, health, attack, defense);
        this.ABILITY_RANGE = range;
        this.arrowCount = 10 * getLevel();
    }

    // the Hunter cast his ability
    @Override
    public void castAbility() {
        List<Enemy> potenTarget = TargetHandler.candidateTarget(this, this.getPosition(), this.ABILITY_RANGE);
        if (potenTarget.size() > 0) {
            Enemy closetEnemy = potenTarget.get(0);
            for (Enemy target : potenTarget) {
                if (this.distance(target) < this.distance(closetEnemy))
                    closetEnemy = target;
            }
            messageCallback.send(String.format("%s fired an arrow at %s.", this.getName(), closetEnemy.getName()));
            this.castAbility(closetEnemy, this.attack);
            this.arrowCount -= ARROW_COST;
        } else
            messageCallback.send(String.format("%s tried to shoot an arrow but there were no enemies in range.", this.getName()));
    }

    // return the distance of this Hunter from another Unit
    public double distance(Unit unit) {
        return this.getPosition().distance(unit.getPosition());
    }

    // the Hunter try to cast his ability
    public void tryCastAbility() {
        boolean cast = super.tryCastAbility(this.arrowCount, ARROW_COST);
        if (!cast)
            messageCallback.send(String.format("%s tried to shoot an arrow but there were no enemies in range.", this.getName()));
    }

    @Override
    public void levelUp() {
        super.levelUp();
        this.arrowCount += 10 * this.getLevel();
        this.attack = (this.getAttack() + this.getLevel() * 2);
        this.defense = (this.getDefense() + getLevel());
    }

    // this Mage makes a turn
    @Override
    public void turn(int turnCount) {
        super.turn(turnCount);
        if (tickCount == 10) {
            arrowCount += getLevel();
            tickCount = 0;
        } else
            tickCount += 1;
    }

    // the Hunter describe
    public String describe() {
        return super.describe() + "\t\tArrows: " + this.arrowCount;
    }

    // a copy of the Hunter
    @Override
    public Unit copy() {
        return new Hunter(name, this.getHealth().getAmount(), this.getAttack()
                ,this.getDefense(), this.ABILITY_RANGE);
    }

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

    public void visit(Tile tile) {
        tile.accept(this);
    }
}
