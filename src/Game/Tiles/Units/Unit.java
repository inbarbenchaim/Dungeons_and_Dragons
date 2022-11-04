package Game.Tiles.Units;

import Game.Callbacks.MessageCallback;
import Game.GameManager;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Player.Player;
import Game.Utils.Position;

import java.util.Random;

public abstract class Unit extends Tile {

    protected static Random rnd = new Random(123);
    public static MessageCallback messageCallback;
    protected String name;
    public Health health;
    protected int attack;
    protected int defense;
    public static GameManager gameManager;

    // return the unit name
    public String getName() {
        return name;
    }

    // return the health of the unit
    public Resource getHealth() {
        return health;
    }

    // return the attack points of the unit
    public int getAttack() {
        return attack;
    }

    // return the defense points of the unit
    public int getDefense() {
        return defense;
    }

    public Unit(String name, char tile, int healthCapacity, int attack, int defense) {
        super(tile);
        this.name = name;
        this.health = new Health(healthCapacity, healthCapacity);
        this.attack = attack;
        this.defense = defense;
    }

    public Unit(char tile, String name, int healthCapacity, int attack, int defense, Position position) {
        super(tile);
        this.name = name;
        this.health = new Health(healthCapacity, healthCapacity);
        this.attack = attack;
        this.defense = defense;
    }

    // initialize the position of this Tile
    protected void initialize(Position position, MessageCallback messageCallback) {
        super.initialize(position);
        Unit.messageCallback = messageCallback;
    }

    /*
    // make a combat between this Unit and Unit defender
    public void attack(Unit defender) {
        this.attack(defender, this.getAttackPoints());
    }

    // return the attack points of this Unit
    public int getAttackPoints() {
        return attack;
    }

    // make a combat between this Unit and Unit defender
    public void attack(Unit defender, int attackPoints) {
        combat(defender);
        int result = rnd.nextInt(attackPoints + 1);
        messageCallback.send(getName() + " rolled " + result + " attack points.");
        int [] combatInfo = defender.defense(result);
        messageCallback.send(defender.getName() + " rolled " + combatInfo[0] + " defense points.");
        messageCallback.send(getName() + " dealt " + combatInfo[1] + " damage to " + defender.getName() + ".");
    }
     */

    public int[] defense(int ar) {
        int[] combatInfo = new int[2];
        combatInfo[0] = rnd.nextInt(getDefense());
        combatInfo[1] = Math.max(ar - combatInfo[0], 0);
        this.getHealth().setAmount(getHealth().getAmount() - combatInfo[1]);
        return combatInfo;
    }

    // print the combat between this Unit and Unit u
    public void combat(Unit u) {
        messageCallback.send(String.format("\n"));
        messageCallback.send(String.format("%s engaged in combat with %s.\n%s\n%s", getName(), u.getName(), describe(), u.describe()));
        int damageDone = Math.max(attack - u.defense, 0);
        u.health.reduceAmount(damageDone);
        int result = rnd.nextInt(this.attack + 1);
        messageCallback.send(getName() + " rolled " + result + " attack points.");
        int [] combatInfo = u.defense(result);
        messageCallback.send(u.getName() + " rolled " + combatInfo[0] + " defense points.");
        messageCallback.send(getName() + " dealt " + combatInfo[1] + " damage to " + u.getName() + ".");
        messageCallback.send(String.format("\n"));
    }

    // if the player visit an Enemy, it makes a combat
    public abstract void visit(Enemy e);

    // if the unit is an empty - it is swap the positions
    public void visit(Empty e){
        swapPosition(e);
    }

    // when a player meet a player on the board
    public abstract void visit(Player p);

    // abstract method
    public abstract void onDeath();

    // return a string that describe the unit
    public String describe() {
        return String.format("%s\t\tHealth: %s\t\tAttack: %d\t\tDefense: %d", getName(), getHealth().toString(), getAttack(), getDefense());
    }

    // return if the unit is dead
    public boolean isDead() {
        return health.isDead();
    }

    // swap the position between this Unit and an empty Tile
    protected void swapPosition(Empty t) {
        Position p = t.getPosition();
        t.setPosition(this.getPosition());
        this.setPosition(p);
    }

    // abstract method
    public abstract Unit copy();

    // set the char of this Tile
    public void setTile(char tile) {
        super.tile = tile;
    }

    // abstract method of a turn
    public abstract void turn(int tickCount);

    public static void setMessageCallback(MessageCallback m){
        messageCallback = m;
    }
}