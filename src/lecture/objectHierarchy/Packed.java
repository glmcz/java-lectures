package lecture.objectHierarchy;

public class Packed {
    private String name;

    public Packed(String name) {
        this.name = name;
    }
    public String getString(){
        return name;
    }

    @Override
    public String toString(){
        return getString();
    }
}
