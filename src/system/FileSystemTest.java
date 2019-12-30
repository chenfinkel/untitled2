package system;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.LeafStub;
import test.SpaceStub;

import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FileSystemTest {

    private FileSystem fs;
    private String[] name = {"root","name1"};

    @Before
    public void setUp() throws Exception {
        fs = new FileSystem(5);
    }

    @After
    public void tearDown() throws Exception {
        fs.fileStorage = null;
        fs = null;
    }

    @Test
    public void createDir() throws BadFileNameException {
        fs.dir(name);
        assertNotNull(fs.DirExists(name));
    }

    @Test (expected = BadFileNameException.class)
    public void createDirNotFromRoot() throws BadFileNameException {
        String[] dir = {"notroot","name1"};
        fs.dir(dir);
    }


    @Test
    public void disk() throws BadFileNameException, OutOfSpaceException {/**
     String[] name2 = {"root","name2"};
     String[] name3 = {"root","name3"};
     String[] name4 = {"root","name4"};
     String[][] names = {name, name2, name3, name4};
     for (String[] n: names) {
     fs.file(n,1);
     }
     String[][] res = fs.disk();
     for (String[] a: res
     ) {
     System.out.println(a);
     }
     assertTrue(Arrays.deepEquals(names, res));*/
        Space storage = fs.fileStorage;
        FileSystem.fileStorage = new SpaceStub();
        String[][] disk = fs.disk();
        String[] disk0 = {"root", "parent", "leaf1"};
        String[] disk3 = {"root", "parent", "leaf2"};
        assertArrayEquals(disk[0], disk0);
        assertNull(disk[1]);
        assertNull(disk[2]);
        assertArrayEquals(disk[3], disk3);
        assertNull(disk[4]);

    }

    @Test (expected = BadFileNameException.class)
    public void badFileName() throws BadFileNameException, OutOfSpaceException {
        String[] name = {"name","name1"};
        fs.file(name, 1);
    }

    @Test (expected = OutOfSpaceException.class)
    public void fileOutOfSpace() throws BadFileNameException, OutOfSpaceException {
        fs.file(name, 6);
    }

    @Test
    public void fileShouldReplaceOlder() throws BadFileNameException, OutOfSpaceException {
        fs.file(name, 2);
        fs.file(name, 3);
        Leaf file = fs.FileExists(name);
        assertEquals(3,file.allocations.length);
    }

    @Test
    public void fileTooLargeButExists() throws BadFileNameException, OutOfSpaceException {
        fs.file(name, 2);
        fs.file(name, 4);
        Leaf file = fs.FileExists(name);
        assertEquals(4,file.allocations.length);
    }

    @Test (expected = BadFileNameException.class)
    public void createFileWithSameNameAsFolder() throws BadFileNameException, OutOfSpaceException {
        fs.dir(name);
        fs.file(name, 4);
    }

    //The lsdir method supposed to return only files, therefore i created files
    //but if there is a directory inside the name1 directory it will print it too
    @Test
    public void lsdir() throws BadFileNameException, OutOfSpaceException {
        /*String[] name2 = {"root","name1","name2"};
        String[] name3 = {"root","name1","name3"};
        String[] name4 = {"root","name1","name4"};
        String[][] names = {name2, name3, name4};*/
        fs.dir(name);
        Tree file = fs.DirExists(name);
        Node child1 = new LeafStub("name");
        file.children.put("name1", child1);
        file.children.put("name2", child1);
        file.children.put("name3", child1);
        /*for (String[] n : names) {
            fs.file(n, 1);
        }*/
        String[] res = fs.lsdir(name);
        String[] expected = {"name1", "name2", "name3"};
        assertTrue(Arrays.deepEquals(res,expected));
    }

    @Test
    public void rmfile() throws OutOfSpaceException, BadFileNameException {
        fs.file(name, 2);
        fs.rmfile(name);
        assertNull(fs.FileExists(name));
    }

    @Test
    public void rmdir() throws BadFileNameException, DirectoryNotEmptyException {
        fs.dir(name);
        fs.rmdir(name);
        assertNull(fs.DirExists(name));
    }

    @Test (expected = DirectoryNotEmptyException.class)
    public void rmdirNotEmpty() throws BadFileNameException, DirectoryNotEmptyException {
        String[] name1 = {"root", "name1", "name"};
        fs.dir(name);
        fs.dir(name1);
        fs.rmdir(name);
    }

    @Test
    public void fileExists() throws OutOfSpaceException, BadFileNameException {
        int k = 2;
        fs.file(name, k);
        Leaf leaf = fs.FileExists(name);
        assertNotNull(leaf);
        assertTrue(Arrays.deepEquals(leaf.getPath(),name));
    }

    @Test
    public void fileDoesntExist() {
        assertNull(fs.FileExists(name));
    }

    @Test
    public void dirExists() throws BadFileNameException {
        fs.dir(name);
        assertTrue(fs.DirExists(name) != null);
    }

    @Test
    public void dirDoesntExist() throws BadFileNameException {
        String[] name = {"root","first"};
        String[] name2 = {"root","second"};
        fs.dir(name);
        assertNull(fs.DirExists(name2));
    }


}