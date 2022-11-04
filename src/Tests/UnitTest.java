package Tests;

import Game.Board;
import Game.Tiles.Units.Empty;
import Game.Tiles.Units.Enemies.*;
import Game.Tiles.Units.Player.*;
import Game.Tiles.Units.Wall;
import Game.Utils.Position;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest {
    private Player player;
    private Enemy enemy;
    private Empty empty;
    private Wall wall;

    @Before
    public void setUp() throws Exception {
        enemy = new Monster("Lannister Solider", 's', -1, 8, 3, 3, 25);
        player = new Warrior("Jon Snow", 300, 30, 4, 3);
        player.setPosition(new Position(2,3));
        empty = new Empty(new Position(3,3));
        wall = new Wall(new Position(2,2));
    }

    @After
    public void tearDown() throws Exception {
        // not needed
    }

    @Test
    public void testIsDead1() {
        assertTrue("Enemy should not be dead", enemy.isDead());
    }

    @Test
    public void testIsDead2() {
        assertFalse("Player should not be dead", player.isDead());
    }

    @Test
    public void testVisit1() {
        empty.accept(player);
        assertTrue("Player should switch positions with empty space", Board.samePos(player.getPosition(),new Position(3,3)));
    }

    @Test
    public void testVisit2() {
        wall.accept(player);
        assertFalse("Player should not switch positions with wall", Board.samePos(player.getPosition(),wall.getPosition()));
    }
}