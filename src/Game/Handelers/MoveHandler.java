package Game.Handelers;

import Game.Board;
import Game.Callbacks.MessageCallback;
import Game.Tiles.Units.Empty;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;
import Game.Tiles.Units.Wall;
import Game.Utils.Position;
import View.Input.InputProvider;

import java.util.List;

public class MoveHandler {
    public static Board gameBoard;
    public static MessageCallback messageCallback;

    // receive a move and a player and commit the movement
    public static Tile move(InputProvider input, Player player){
        Position moveTo = joyStick(input, player.getPosition());
        return gameBoard.getTile(moveTo);
    }

    // receive a move and an enemy and commit the movement
    public static Tile move(InputProvider input, Enemy enemy){
        Position moveTo = joyStick(input, enemy.getPosition());
        if(isOnEnemy(moveTo))
            return enemy;
        Player target = targetCondidate(moveTo, enemy);
        if (target == null){
            return gameBoard.getTile(moveTo);
        }
        else {
            return target;
        }
    }

    // commit a combat between two units
    public static void combat(Unit unit1, Unit unit2){
        messageCallback.send(unit1.getName() + " engaged in combat with " + unit2.getName() + ".");
        messageCallback.send(unit1.describe());
        messageCallback.send(unit2.describe());
    }

    // update the position of the unit and committed move
    public static Position joyStick(InputProvider input, Position pos) {
        Position newPos = pos.copy();
        switch (input) {
            case Right -> newPos.x++;
            case Left -> newPos.x--;
            case Up -> newPos.y--;
            case Down -> newPos.y++;
        }
        if (!isValidMove(newPos))
            newPos = pos.copy();
        return newPos;
    }

    // check if the move is valid in the game
    private static boolean isValidMove(Position newPos){
        return (newPos.x>= 0 && newPos.x< gameBoard.width && newPos.y>= 0 && newPos.y< gameBoard.height &&
                !gameBoard.walls.containsKey(newPos));
    }

    // check if an enemy move on another enemy
    private static boolean isOnEnemy(Position newPos){
        boolean isSameCor = false;
        int i = 0;
        List<Enemy> enemies = gameBoard.enemies;
        while (!isSameCor && i < enemies.size() ){
            if (enemies.get(i).getPosition().equals(newPos))
                isSameCor = true;
            i++;
        }
        return isSameCor;
    }

    // return the first enemy if exist that close to the player
    private static Enemy targetCondidate(Position newPos, Player player){
        List<Enemy> targets = TargetHandler.candidateTarget(player,newPos, 0);
        return targets.size() == 0 ? null: targets.get(0);
    }

    // return the flayer if exist that close to the enemy
    private static Player targetCondidate(Position newPos, Enemy enemy){
        List<Player> targets = TargetHandler.candidateTarget(enemy,newPos, 0);
        return targets.size() == 0 ? null: targets.get(0);
    }
}
