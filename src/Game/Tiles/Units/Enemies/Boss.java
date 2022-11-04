package Game.Tiles.Units.Enemies;

import Game.Handelers.TargetHandler;
import Game.Tiles.Units.HeroicUnit;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;
import View.Input.InputProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boss extends Enemy {

    private final int visionRange;
    private final int abilityFrequency;
    private int combatTicks = 0;
    private final InputProvider[] rndArrs ={InputProvider.Wait,InputProvider.Right,InputProvider.Left,InputProvider.Up,InputProvider.Down};

    public Boss(String name, char tile, int health, int attack, int defense, int experience, int visionRange , int abilityFrequency) {
        super(name, tile, health, attack, defense, experience);
        this.visionRange = visionRange;
        this.abilityFrequency = abilityFrequency;
    }

    // a copy of the Boss
    @Override
    public Unit copy(){
        return new Boss(this.getName(), this.toString().charAt(0), this.getHealth().getAmount(), this.getAttack(), this.getDefense()
                ,this.experienceValue,this.visionRange, this.abilityFrequency);
    }

    @Override
    public void castAbility(){
    }

    // cast the ability for this Boss
    public void castAbility(Player target) {
        this.attack(target);
        messageCallback.send(String.format("%s cast special ability.",this.getName()));
        int[] combatInfo = target.defense(this.getAttack());
        messageCallback.send(String.format("%s rolled %d defense points.",target.getName(), combatInfo[0]));
        messageCallback.send(String.format("%s hit %d for %d ability damage.",this.getName(),target.getName(), combatInfo[1]));
        if(target.isDead()){
            messageCallback.send(String.format("%s was killed by %d.",target.getName(), this.getName()));
        }
    }

    // this Boss makes a turn
    @Override
    public void turn(int turnCount) {
        //super.turn(turnCount);
        List<Player> closePlayer;
        closePlayer= TargetHandler.candidateTarget(this, getPosition(), visionRange);
        if(closePlayer.size()>0) {
            if (this.combatTicks == abilityFrequency) {
                this.combatTicks = 0;
                this.castAbility(closePlayer.get(0));
            } else {
                this.combatTicks += 1;
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
            }
        }
        else
        {
            this.combatTicks = 0;
            this.move(rndArrs[(new Random().nextInt(5))]) ;
        }
    }

    // this Boss description
    @Override
    public String describe(){return super.describe()+"\t\tVision Range: "+this.visionRange +
            "\t\tAbility load :"+ this.combatTicks +"/"+this.abilityFrequency;
    }

    @Override
    public void onDeath() {
    }

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

    public void visit(Tile tile) {
        tile.accept(this);
    }
}
