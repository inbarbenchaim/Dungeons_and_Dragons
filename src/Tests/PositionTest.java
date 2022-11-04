package Tests;

import Game.Utils.Position;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    private Position pos1;
    private Position pos2;
    private Position pos3;

    @Before
    public void setUp() throws Exception {
        pos1 = new Position(1,1);
        pos2 = new Position(1,1);
        pos3 = new Position(3,4);
    }

    @After
    public void tearDown() throws Exception {
        // not needed
    }

    @Test
    public void testIsInRange1() {
        assertTrue("pos1 and pos2 should be in range", pos1.isInRange(pos2, 2));
    }

    @Test
    public void testIsInRange2() {
        assertFalse("pos1 and pos3 should not be in range", pos1.isInRange(pos3, 2));
    }
}