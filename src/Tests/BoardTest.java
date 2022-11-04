package Tests;

import Game.Board;
import Game.Utils.Position;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    private Position pos1;
    private Position pos2;
    private Position pos3;

    @Before
    public void setUp() throws Exception {
        pos1 = new Position(1, 1);
        pos2 = new Position(1, 1);
        pos3 = new Position(2, 3);
    }

    @After
    public void tearDown() throws Exception {
        // not needed
    }

    @Test
    public void testSamePos1() {
        assertTrue("The positions are the same Position", Board.samePos(pos1, pos2));
    }

    @Test
    public void testSamePos2() {
        assertFalse("The positions should be different positions", Board.samePos(pos1, pos3));
    }
}