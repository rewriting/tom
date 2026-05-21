// Equivalence scenario for Leaf.gom — Java reference side.
import leaf.types.Leaf;
import leaf.types.leaf.Label;

public class Scenario {
    public static void main(String[] args) {
        Leaf t1 = leaf.types.leaf.Leaf.make();
        Leaf t2 = Label.make("hello");
        Leaf t3 = Label.make("hello");
        Leaf t4 = Label.make("world");

        System.out.println("t1=" + t1);
        System.out.println("t2=" + t2);
        System.out.println("shared-label=" + (t2 == t3));
        System.out.println("diff-labels=" + (t2 != t4));

        Label lab = (Label) t2;
        System.out.println("label-s=" + lab.gets());
    }
}
