package Game.Tiles.Units.Enemies;

import Game.Handelers.TargetHandler;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;
import View.Input.InputProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Monster extends Enemy {

    private int visionRange = 0;
    private InputProvider rndArrs[] = {InputProvider.Wait, InputProvider.Right, InputProvider.Left, InputProvider.Up, InputProvider.Down};

    public Monster(String name, char tile, int health, int attack, int defense, int experience, int visionRange) {
        super(name, tile, health, attack, defense, experience);
        this.visionRange = visionRange;
    }

    // this Monster makes a turn
    @Override
    public void turn(int turnCount) {
        //super.turn(turnCount);
        List<Player> closePlayer = new ArrayList<>();
        closePlayer = TargetHandler.candidateTarget(this, this.getPosition(), visionRange);
        if (closePlayer.size() > 0) {
            int dx = this.getPosition().x - closePlayer.get(0).getPosition().x;
            int dy = this.getPosition().y - closePlayer.get(0).getPosition().y;
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0)
                    this.move(InputProvider.Left);
                else
                    this.move(InputProvider.Right);
            } else {
                if (dy > 0)
                    this.move(InputProvider.Up);
                else
                    this.move(InputProvider.Down);
            }
        } else {
            this.move(rndArrs[(new Random().nextInt(5))]);
        }
    }

    @Override
    public void onDeath() {
    }

    // a copy of the Monster
    public Unit copy() {
        return new Monster(this.getName(), this.toString().charAt(0), this.getHealth().getAmount(), this.attack, this.getDefense()
                , this.experienceValue, this.visionRange);
    }

    // the describe of this Monster
    public String describe() {
        return super.describe() + "\t\tVision Range: " + this.visionRange;
    }
    @Override
    public void castAbility() {}

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

    public void visit(Tile tile) {
        tile.accept(this);
    }
}
