package Game.Tiles.Units.Player;

import Game.Callbacks.MessageCallback;
import Game.Handelers.MoveHandler;
import Game.Tiles.Units.*;
import Game.Handelers.InputHandler;
import View.Input.InputProvider;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

public abstract class Player extends Unit implements HeroicUnit {

    public static final char playerTile = '@';
    protected static final int REQ_EXP = 50;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;
    protected static final int HEALTH_BONUS = 10;
    public String abilityName="";

    protected int level;
    protected int experience;

    protected MessageCallback messageCallback;

    protected Player(String name, int healthCapacity, int attack, int defense) {
        super(name, playerTile, healthCapacity, attack, defense);
        this.level = 1;
        this.experience = 0;
    }

    // initialize the position and messageCallback and inputProvider for this player
    public void initialize(Position position, MessageCallback messageCallback) {
        super.initialize(position, messageCallback);
        this.messageCallback = messageCallback;
    }

    public void accept(Unit unit){
        unit.visit(this);
    }

    // if the player visit an Enemy, it makes a combat
    public void visit(Enemy e) {
        super.combat(e);
        if (e.isDead()) {
            swapPosition(e);
            onKill(e);
        }
    }

    // when a player meet a player on the board
    public void visit(Player p){}

    // the damage the ability has done
    protected void abilityDamage(Enemy e, int abilityDamage) {
        int damageDone = Math.max(abilityDamage - e.getDefense(), 0);
        e.getHealth().reduceAmount(damageDone);
        messageCallback.send(String.format("%s hit %s for %d ability damage.", getName(), e.getName(), damageDone));
        if (e.isDead()) {
            onKill(e);
        }
    }

    // a player kills an enemy
    protected void onKill(Enemy e) {
        int experienceGained = e.getExprienceValue();
        messageCallback.send(String.format("%s died. %s gained %d experience.", e.getName(), getName(), experienceGained));
        addExperience(experienceGained);
        e.onDeath();
    }

    // the player is dead
    public void onDeath() {
        messageCallback.send("You lost.");
    }

    // add experience when level up
    protected void addExperience(int experienceGained) {
        this.experience += experienceGained;
        int nextLevelReq = levelUpRequirement();
        while (experience >= nextLevelReq) {
            levelUp();
            experience -= nextLevelReq;
            nextLevelReq = levelUpRequirement();
        }
    }

    // level up for this player
    protected void levelUp() {
        level++;
        getHealth().setPool(getHealth().getPool() + (HEALTH_BONUS * level));
        int healthGained = gainHealth();
        int attackGained = gainAttack();
        int defenseGained = gainDefense();
        health.addCapacity(healthGained);
        attack += attackGained;
        defense += defenseGained;
        messageCallback.send(String.format("%s reached level %d: %d New Health, +%d Attack, +%d Defense.", getName(), getLevel(), healthGained, attackGained, defenseGained));
    }

    public String toString() {
        return !isDead() ? super.toString() : "x";
    }

    // when leveling up, returns the new amount of HEALTH
    // The new amount of health is defined as the new health pool
    // The new health pool is defined as the previous health pool + ( 10 X level )
    protected int gainHealth() {
        return health.getPool();
        // return level * HEALTH_BONUS;
    }

    // when leveling up, returns the new amount of ATTACK
    protected int gainAttack() {
        return level * ATTACK_BONUS;
    }

    // when leveling up, returns the new amount of DEFENSE
    protected int gainDefense() {
        return level * DEFENSE_BONUS;
    }

    // return the requirement amount to level up
    protected int levelUpRequirement() {
        return REQ_EXP * level;
    }

    // return the level of the player
    public int getLevel(){
        return level;
    }

    // return the experience of the player
    public int getExperienceValue() {
        return experience;
    }

    // return a string that describe the player
    public String describe() {
        return String.format("%s\t\tLevel: %d\t\tExperience: %d/%d", super.describe(), getLevel(), getExperienceValue(), levelUpRequirement());
    }

    // the player try to cast his ability
    public Boolean tryCastAbility(int resource, int cost) {
        if (resource - cost >= 0) {
            castAbility();
            return true;
        }
        return false;
    }

    public abstract void castAbility();

    // cast the ability of the player, and send a message callback
    public void castAbility(Enemy defender,int attackPoints){
        int [] combatInfo = defender.defense(attackPoints);
        messageCallback.send(String.format("%s rolled : %d defense points.", defender.getName(), combatInfo[0]));
        messageCallback.send(String.format("%s hit %s: for %d ability damage.", getName(), defender.getName(), combatInfo[1]));
        if(defender.isDead()) {
            messageCallback.send(String.format("%s died. %s gained %d experience.", defender.getName(), this.getName(), defender.experienceValue));
            experience = (experience+ defender.experienceValue);
        }
    }

    // the turn of the player
    public void turn(int turnCount) {
        InputProvider input = InputHandler.inputPlayerGame();
        if (input == InputProvider.CastAbility)
            this.tryCastAbility();
        else
            move(input);
    }

    // abstract method
    public abstract void tryCastAbility();

    // make a move for the player
    public void move(InputProvider moveDir) {
        Tile newPosTile = MoveHandler.move(moveDir, this);
        newPosTile.accept(this);
    }
}
