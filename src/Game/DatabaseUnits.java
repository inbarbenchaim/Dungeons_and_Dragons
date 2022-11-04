package Game;

import Game.Tiles.Units.Enemies.Boss;
import Game.Tiles.Units.Enemies.Monster;
import Game.Tiles.Units.Enemies.Trap;
import Game.Tiles.Units.Player.Hunter;
import Game.Tiles.Units.Player.Mage;
import Game.Tiles.Units.Player.Warrior;
import Game.Tiles.Units.Player.Rogue;
import Game.Tiles.Units.Unit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseUnits {

    public static Map<String, Unit> playerPool = new HashMap<>();
    public static Map<String, Unit> enemyPool = new HashMap<>();
    private final static String dirAddons = "src/addons";

    public DatabaseUnits() {
        buildDictionary();
    }

    // connect the databases to the game
    public static void buildDictionary() {
        List<Unit> players = buildUnit("/addons/dbPlayer");
        for(int i=0; i<players.size(); i++) {
            playerPool.put(String.valueOf(i+1), players.get(i));
        }
        List<Unit> enemies = buildUnit("/addons/dbEnemy");
        for(Unit e : enemies) {
            enemyPool.put(String.valueOf(e.getTile()), e);
        }
    }

    // return the files from Jar to string format
    public static String getFileJar(String address) {
        String fileText = "";
        InputStream in = DatabaseUnits.class.getResourceAsStream(address);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        fileText = reader.lines().collect(Collectors.joining("\n"));
        return fileText;
    }

    // transfer the table to presentation view
    private static List<Unit> buildUnit(String address) {
        List<Unit> result = new ArrayList<>();
        String txtToSplit = getFileJar(address);
        ArrayList<String> enemyUnit = new ArrayList<>(Arrays.asList(txtToSplit.split("\n")));
        for (String unitStr : enemyUnit) {
            ArrayList<String> argumentUnit = new ArrayList<>(Arrays.asList(unitStr.split("\\|")));
            Unit unitObj = factoryUnit(argumentUnit);
            assert unitObj != null;
            result.add(unitObj);
//            map.put(unitObj.toString(), unitObj);
        }
        return result;
    }

    // make the Units with the data from the databases of the Units in the game
    private static Unit factoryUnit(ArrayList<String> typeUnit) {
        String type = typeUnit.get(0);
        String name = typeUnit.get(1);
        char tile = typeUnit.get(2).charAt(0);
        int health = Integer.parseInt(typeUnit.get(3));
        int attack = Integer.parseInt(typeUnit.get(4));
        int defense = Integer.parseInt(typeUnit.get(5));
        if (type.equals("Monster")) {
            int visionRange = Integer.parseInt(typeUnit.get(6));
            int experienceValue = Integer.parseInt(typeUnit.get(7));
            return new Monster(name, tile, health, attack, defense, experienceValue, visionRange);
        }
        if (type.equals("Trap")) {
            int visibilityTime = Integer.parseInt(typeUnit.get(7));
            int invisibilityTime = Integer.parseInt(typeUnit.get(8));
            int experienceValue = Integer.parseInt(typeUnit.get(6));
            return new Trap(name, tile, health, attack, defense, experienceValue, visibilityTime, invisibilityTime);
        }
        if (type.equals("Warrior")) {
            int cooldown = Integer.parseInt(typeUnit.get(6));
            return new Warrior(name, health, attack, defense, cooldown);
        }
        if (type.equals("Rogue")) {
            int cost = Integer.parseInt(typeUnit.get(6));
            return new Rogue(name, health, attack, defense, cost);
        }
        if (type.equals("Mage")) {
            int manaPool = Integer.parseInt(typeUnit.get(6));
            int manaCost = Integer.parseInt(typeUnit.get(7));
            int spellPower = Integer.parseInt(typeUnit.get(8));
            int hitCount = Integer.parseInt(typeUnit.get(9));
            int range = Integer.parseInt(typeUnit.get(10));
            return new Mage(name, health, attack, defense, manaPool, manaCost, spellPower, hitCount, range);
        }
        if (type.equals("Hunter")) {
            int range = Integer.parseInt(typeUnit.get(6));
            return new Hunter(name, health, attack, defense, range);
        }

        if (type.equals("Boss")) {
            int visionRange = Integer.parseInt(typeUnit.get(6));
            int expirenceValue = Integer.parseInt(typeUnit.get(7));
            int abilityFrequency = Integer.parseInt(typeUnit.get(8));
            return new Boss(name, tile, health, attack, defense, expirenceValue, visionRange, abilityFrequency);
        }
        return null;
    }
}
