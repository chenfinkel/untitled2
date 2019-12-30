package test;


import system.Leaf;
import system.OutOfSpaceException;
import system.Tree;

public class LeafStub extends Leaf {

    //public int depth;

    public String name;

    public LeafStub(String name) throws OutOfSpaceException {
        super(name,0);
        this.name = name;
        this.parent = new Tree("parent");
    }

    public String[] getPath(){
        String[] path = {"root", "parent", name};
        return path;
    }


}
