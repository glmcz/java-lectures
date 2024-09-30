package lecture.objectHierarchy;

public class Main {
    public static void main(String[] args) {
        Object o = "string";
        String s = (String)o;
        System.out.println(s);

        Object o2 = new Packed("packet name");
        System.out.println(o2);
    }
}
