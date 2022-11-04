package Game.Tiles.Units.Player;

import Game.Handelers.TargetHandler;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Tile;

import java.util.List;
import java.util.Random;

public class Mage extends Player {

    private int ManaCost;
    private int CurrentMana;
    private int ManaPool;
    private int SpellPower;
    private int HitCount;
    private int AbilityRange;

    public Mage(String name, int health, int attack, int defense, int manaPool, int manaCost, int spellPower, int hitCount, int range){
        super(name, health, attack,defense);
        this.name = "Blizzard";
        this.AbilityRange = range;
        this.ManaPool = manaPool;
        this.CurrentMana = manaPool/4;
        this.ManaCost = manaCost;
        this.SpellPower = spellPower;
        this.HitCount = hitCount;
    }

    // the Mage describe
    @Override
    public String describe(){
        return super.describe() + "\t\tMana: " + this.CurrentMana + "/" + this.ManaPool + "\t\tspellPower: " + this.SpellPower;}

    // level up for this Mage
    @Override
    public void levelUp() {
        super.levelUp();
        this.ManaPool += 25*this.getLevel();
        this.CurrentMana = Math.min(this.CurrentMana + this.ManaPool/4, this.ManaPool);
        this.SpellPower += 10*getLevel();

    }

    public void setExperience(int experience) {
        this.experience = experience;
        while (this.experience>=50* getLevel()) {
            int[] saveState=new int[]{this.getHealth().getPool(),this.getAttack(),this.getDefense(),this.ManaPool, this.SpellPower};
            levelUp();
            messageCallback.send(this.getName() + " reached level " + this.getLevel() + ": +"+
                    (this.getHealth().getPool()-saveState[0])+" Health, +"+(this.getAttack()-saveState[1])+" Attack, +"+(this.getDefense()-saveState[2])+" Defense, +"+
                    (this.ManaPool - saveState[3])+" maximum mana, +"+(this.SpellPower - saveState[4]) +" spell power");
        }
    }

    // this Mage makes a turn
    @Override
    public void turn(int tick){
        super.turn(tick);
        this.CurrentMana = Math.min(CurrentMana + getLevel(), ManaPool);
    }

    // the Mage cast his ability
    @Override
    public void castAbility(){
        List<Enemy> potenTargets = TargetHandler.candidateTarget(this,this.getPosition(), AbilityRange);
        messageCallback.send(this.getName()+ " cast "+ this.abilityName +".");
        int hits = 0;
        Random rnd = new Random();
        this.CurrentMana -= ManaCost;
        while(hits < HitCount && 0 < potenTargets.size()){
            Enemy target = potenTargets.get(rnd.nextInt(potenTargets.size()));
            this.castAbility(target, this.SpellPower);
            if(target.isDead())
                potenTargets.remove(target);
            hits++;
        }
    }

    // the Mage try to cast his ability
    public void tryCastAbility(){
        boolean cast = this.tryCastAbility(CurrentMana , ManaCost);
        if (!cast){
            messageCallback.send(String.format("%s tried to cast %s, but there was not enough mana: %d/%d", this.getName(), this.abilityName,CurrentMana, ManaCost));
        }
    }

    // a copy of the Mage
    @Override
    public Mage copy(){
        return new Mage(this.getName(), this.getHealth().getAmount(), this.getAttack()
                ,this.getDefense(),this.ManaPool, this.ManaCost, this.SpellPower,this.HitCount,this.AbilityRange);
    }

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

}
