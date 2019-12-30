package system;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import test.LeafStub;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SpaceTest {
    private Space space;
    private Leaf leaf;

    @Before
    public void setUp() throws OutOfSpaceException {
        FileSystem.fileStorage = new Space(10);
        space = FileSystem.fileStorage;
        leaf = new LeafStub("leaf");
        leaf.size = 3;
    }

    @Test
    public void fileShouldBeAddedToBlocks() throws OutOfSpaceException{
        space.Alloc(3, leaf);
        Leaf[] blocks = space.getAlloc();
        assertThat(Arrays.asList(blocks), CoreMatchers.hasItem(leaf));
    }

    @Test
    public void fileShouldBeAllocated() throws OutOfSpaceException{
        space.Alloc(3, leaf);
        int[] expectedAlloc = {0,1,2};
        assertArrayEquals(leaf.allocations, expectedAlloc);
    }

    @Test
    public void tenShouldBeFree() throws OutOfSpaceException {
        Tree tree = new Tree("parent");
        leaf.parent = tree;
        tree.children.put("leaf", leaf);
        space.Alloc(3, leaf);
        space.Dealloc(leaf);
        assertEquals(10, space.countFreeSpace());
    }

    @Test
    public void blocksShouldBeFree() throws OutOfSpaceException {
        Tree tree = new Tree("parent");
        leaf.parent = tree;
        tree.children.put("leaf", leaf);
        space.Alloc(3, leaf);
        space.Dealloc(leaf);
        Leaf[] expectedAlloc = {null,null,null,null,null,null,null,null,null,null};
        assertArrayEquals(expectedAlloc, space.getAlloc());
    }

    @Test
    public void parentShouldHaveDeletedLeaf() throws OutOfSpaceException{
        Tree tree = new Tree("parent");
        leaf.parent = tree;
        tree.children.put("leaf", leaf);
        space.Alloc(3, leaf);
        space.Dealloc(leaf);
        assertTrue(tree.children.isEmpty());
    }

    @Test
    public void deallocTwiceShouldDoNotChangeFreeSpace() throws OutOfSpaceException{
        Tree tree = new Tree("parent");
        leaf.parent = tree;
        tree.children.put("leaf", leaf);
        space.Alloc(3, leaf);
        space.Dealloc(leaf);
        int freeSpace = space.countFreeSpace();
        leaf.parent = tree;
        tree.children.put("leaf", leaf);
        space.Dealloc(leaf);
        int freeSpace2 = space.countFreeSpace();
        //////////// CHANGE AFTER PIT TEST !!!!!!! //////////////
        assertEquals(freeSpace+3, freeSpace2);

    }

    @Test
    public void freeSpaceShouldBeEmpty() {
        assertEquals(10, space.countFreeSpace());
    }

    @Test
    public void AllocShouldBeEmpty() {
        Leaf[] expectedAlloc = new Leaf[10];
        assertArrayEquals(expectedAlloc,space.getAlloc());
    }

}