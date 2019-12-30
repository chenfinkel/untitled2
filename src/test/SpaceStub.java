package test;

import system.Leaf;
import system.OutOfSpaceException;
import system.Space;
import test.LeafStub;

public class SpaceStub extends Space {

    public boolean allocated = false;

    public SpaceStub(){
        super(5);
    }


    public void Alloc(int size, Leaf file) throws OutOfSpaceException {
        if (size > 5)
            throw new OutOfSpaceException();
        else
            allocated = true;
    }

    public Leaf[] getAlloc(){
        Leaf[] blocks = new Leaf[5];
        try {
            Leaf leaf1 = new LeafStub("leaf1");
            blocks[0] = leaf1;

            Leaf leaf2 = new LeafStub("leaf2");
            blocks[3] = leaf2;

        } catch (OutOfSpaceException e){ e.printStackTrace(); }
        return blocks;
    }
}
