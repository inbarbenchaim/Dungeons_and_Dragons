package Game.Handelers;

import Game.Board;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class TargetHandler {

    public static Board gameBoard;

    // return all the enemies that are nearby the player
    public static List<Enemy> candidateTarget(Player player, Position position, int range) {
        List<Enemy> closeEnemy = new ArrayList<>();
        for (Enemy enemy : gameBoard.enemies) {
            if (position.isInRange(enemy.getPosition(), range))
                closeEnemy.add(enemy);
        }
        return closeEnemy;
    }

    // save all the players that are nearby the enemy
    public static List<Player> candidateTarget(Enemy enemy, Position position, int range){
        List<Player> closePlayer=new ArrayList<>();
        if( position.isInRange(gameBoard.getPlayer().getPosition(),range))
            closePlayer.add(gameBoard.getPlayer());
        return closePlayer;
    }
}
