package Game;

import Game.Tiles.Units.Empty;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Tile;
import Game.Tiles.Units.Unit;
import Game.Tiles.Units.Wall;
import Game.Utils.Position;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Board {

    private Player player;
    public int height;
    public int width;
    public List<Enemy> enemies = new ArrayList<Enemy>();
    public HashMap<Position, Wall> walls = new HashMap<Position, Wall>();
    private String[][] board;

    // return the player
    public Player getPlayer() {
        return player;
    }

    // set the player in the game board
    public void setPlayer(Player player) {
        this.player = player;
    }

    // build the board this the map file of the game board
    public void buildBoard(File mapFile, Player player) {
        this.player = player;
        buildBoard(mapFile);
    }

    // build the game board
    public void buildBoard(File mapFile) {
        walls.clear();
        try {
            Scanner mapReader = new Scanner(mapFile);
            int y = 0, x = 0;
            Position position = new Position(x, y);
            Unit enemy;
            String data;
            while (mapReader.hasNextLine()) {
                data = mapReader.nextLine();

                x = 0;
                for (char ch : data.toCharArray()) {
                    if (ch == '@') {
                        position = new Position(x, y);
                        player.setPosition(position);
                    }
                    if (ch == '#') {
                        position = new Position(x, y);
                        walls.put(position, new Wall(position));
                    }
                    if (DatabaseUnits.enemyPool.containsKey(ch + "")) {
                        enemy = DatabaseUnits.enemyPool.get(ch + "").copy();
                        position = new Position(x, y);
                        enemy.setPosition(position);
                        enemies.add((Enemy) enemy);
                    }
                    x++;
                }
                y++;
            }
            this.width = x;
            this.height = y;
            this.board = new String[y][x];
        } catch (Exception e) {
        }
    }

    // build the board in arrays
    public void buildArray() {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                board[y][x] = ".";
        board[player.getPosition().y][player.getPosition().x] = player.toString();
        for (Unit enemy : this.enemies) {
            board[enemy.getPosition().y][enemy.getPosition().x] = enemy.toString();
        }
        for (Position wallPos : this.walls.keySet()) {
            board[wallPos.y][wallPos.x] = this.walls.get(wallPos).toString();
        }
    }

    // convert the array to a string
    public String arrayToString() {
        StringBuilder currBoard = new StringBuilder();
        for (String[] line : board) {
            for (String block : line) {
                currBoard.append(block);
            }
            currBoard.append("\n");
        }
        return currBoard.toString();
    }

    // return the board in a string format
    public String toString() {
        buildArray();
        return arrayToString();
    }

    public Tile getTile(Position pos) {
        if (player.getPosition().equals(pos))
            return player;
        for (Enemy e : enemies) {
            if (samePos(e.getPosition(),pos))
                return e;
        }
        for (var w : walls.entrySet()) {
            if (samePos(w.getKey(), pos))
                return w.getValue();
        }
        return new Empty(pos);
    }

    public static boolean samePos(Position pos1, Position pos2){
        return (pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY());
    }

    public void removeEnemy(Enemy e){
        enemies.remove(e);
    }
}
