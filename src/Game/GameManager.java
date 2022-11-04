package Game;

import Game.Callbacks.MessageCallback;
import Game.Handelers.InputHandler;
import Game.Handelers.MoveHandler;
import Game.Handelers.TargetHandler;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Player.Player;
import Game.Tiles.Units.Unit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class GameManager {

    private MessageCallback messageCallback;
    public Board board = new Board();
    List<Enemy> enemies;
    private int tickCount=0;
    private List<File> levelsFiles=new ArrayList<File>();
    public List<Unit> listTurn=new ArrayList<Unit>();


    // contracture for game manager
    public GameManager(MessageCallback m){
        this.messageCallback = m;
        DatabaseUnits.buildDictionary();
        TargetHandler.gameBoard=this.board;
        MoveHandler.gameBoard=this.board;
        MoveHandler.messageCallback = m;
        Unit.gameManager=this;
        Unit.setMessageCallback(m);
    }

    // start the game
    public void start(String address) {
        instruction();
        getPlayerMenu();
        createListOfLevel(address);
        for (File level : levelsFiles) {
            Player player = board.getPlayer();
            player.initialize(player.getPosition(), messageCallback);
            if (player.isDead())
                break;
            loadGame(level);
            startLevel();
        }
        if (board.getPlayer().isDead())
            messageCallback.send("Game Over.");
        else
            messageCallback.send("You Won.");
    }

    // load the game to the screen
    public void loadGame(File file){
        board.buildBoard(file);
        listTurn.clear();
        listTurn.add(board.getPlayer());

        for(Unit enemy:board.enemies){
            listTurn.add(enemy);
        }
    }

    // if the unit is dead , remove it from the board
    public void onTick(){
        ListIterator<Unit> iter = listTurn.listIterator();
        while(iter.hasNext()&&!board.getPlayer().isDead()){
            Unit unit= iter.next();
            if(unit.isDead()){
                Enemy enemy = (Enemy) unit;
                iter.remove();
                board.removeEnemy(enemy);
                continue;
            }
            unit.turn(tickCount);
        }
    }

    // create list of files
    public void createListOfLevel(String address){
        File f = new File(address);
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return  name.endsWith("txt");
            }
        });
        assert matchingFiles != null;
        levelsFiles= Arrays.asList(matchingFiles);
        levelsFiles.sort((File f1,File f2)->f1.getName().compareTo(f2.getName()));
    }

    // print the instructions and game control
    public void instruction(){
        messageCallback.send("*!*!*!*!*!*!*!*!*! D&D-Roguelike !*!*!*!*!*!*!*!*!*");
        messageCallback.send("*** Game instructions:\n");
        messageCallback.send("* Game Controls:\n");
        messageCallback.send(
                "-Move up:\tW\n" +
                        "-Move down:\tS\n" +
                        "-Move right:\tD\n" +
                        "-Move left:\tA\n" +
                        "-Wait:\tQ\n" +
                        "-Attack: Steping on an enemy\n" +
                        "-Cast special Attack:\tE\n");
        messageCallback.send("* Map description:\n");
        messageCallback.send("-(.):\t Free space\n" +
                "-(#):\t Wall\n" +
                "-(@):\t Your player\n");
        messageCallback.send("* Enemies list:\n");
        messageCallback.send("-(s):\t Lannister Solider\n" +
                "-(k):\t Lannister Knight\n" +
                "-(q):\t Queen’s Guard\n" +
                "-(z):\t Wright\n" +
                "-(b):\t Bear-Wright\n" +
                "-(g):\t Giant-Wright\n" +
                "-(w):\t White Walker\n" +
                "^ Traps:\n" +
                "-(B):\t Bonus Trap\n" +
                "-(Q):\t Queen’s Trap\n" +
                "-(D):\t Death Trap\n" +
                "! Bosses:\n" +
                "-(M):\t The Mountain\n" +
                "-(C):\t Queen Cersei\n" +
                "-(N):\t Night’s King\n");
    }

    // print the levels
    private void startLevel() {
        tickCount = 0;
        while (!board.getPlayer().isDead() && board.enemies.size() != 0) {
            printLevel();
            tickCount += 1;
            onTick();
        }
        if (board.getPlayer().isDead())
            messageCallback.send("You Lost");
        printLevel();
    }


    // get the player choose
    private void getPlayerMenu(){
        printMenu();
        String choose= String.valueOf(InputHandler.inputMenu());
        board.setPlayer((Player) DatabaseUnits.playerPool.get(choose).copy());
        printChosenPlayer();
    }

    // print the level of the game
    public void printLevel() {
        if (board.enemies.size() > 0)
            messageCallback.send(board.toString());
            messageCallback.send(board.getPlayer().describe());
    }

    // print options of players
    public void printMenu(){
        messageCallback.send("Select player:");
        for(Map.Entry<String, Unit> player : DatabaseUnits.playerPool.entrySet())
            messageCallback.send(player.getKey()+"."+player.getValue().describe());
    }

    // print the chosen player
    public void printChosenPlayer(){
        Player player = board.getPlayer();
        messageCallback.send(String.format("You have selected: %s", player.getName()));
    }
}
