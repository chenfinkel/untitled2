package system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.SpaceStub;

import static org.junit.Assert.*;

public class LeafTest {

    private Leaf leaf;

    @Before
    public void setUp() throws Exception {
        FileSystem.fileStorage = new SpaceStub();
    }

    @After
    public void tearDown() throws Exception {
        FileSystem.fileStorage = null;
    }

    @Test(expected = OutOfSpaceException.class)
    public void notEnoughSpaceForLeaf() throws OutOfSpaceException{
        leaf = new Leaf("leaf",7);
        assertFalse(((SpaceStub)FileSystem.fileStorage).allocated);
    }

    @Test
    public void leafShouldBeCreated() throws OutOfSpaceException{
        leaf = new Leaf("leaf", 1);
        assertTrue(((SpaceStub)FileSystem.fileStorage).allocated);
    }

}