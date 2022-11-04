package Game.Tiles.Units.Enemies;

import Game.Callbacks.MessageCallback;
import Game.Handelers.InputHandler;
import Game.Handelers.MoveHandler;
import Game.Tiles.Units.*;
import Game.Tiles.Units.Player.Player;
import Game.Utils.Position;
import View.Input.InputProvider;

public abstract class Enemy extends Unit implements HeroicUnit {

    public int experienceValue = 0;

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;
    protected static final int HEALTH_BONUS = 10;

    public Enemy(String name, char tile, int health, int attack, int defense,int experience) {
        this(name, tile, health, attack, defense,experience,new Position());
    }
    public Enemy(String name, char tile, int health, int attack, int defense,int experience,Position position) {
        super(name, tile, health, attack, defense);
        this.experienceValue=experience;
    }

    // the Enemy attack a Player on the board
    public void attack(Player defender) {
        super.combat(defender);
        if (defender.isDead()) {
            messageCallback.send(defender.getName() + " was killed by " + getName() + ".");
        }
    }

    // return the experience value
    public int getExprienceValue(){
        return experienceValue;
    }

    // make a move for this enemy
    public void move(InputProvider moveDir) {
        Tile newPosTile = MoveHandler.move(moveDir, this);
        newPosTile.accept(this);
    }

    // the enemy make a turn
    public void turn(int turnCount) {
    }

    public void accept(Unit unit){
        unit.visit(this);
    }

    public void visit(Player p) {
        combat(p);
    }

    public void visit(Enemy e) {}
}
