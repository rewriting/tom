// Equivalence scenario for List.gom — Java reference side.
import gom.list.types.List;
import gom.list.types.list.conc;
import gom.list.types.list.Emptyconc;

public class Scenario {
    public static void main(String[] args) {
        List a = conc.fromArray(new int[]{1, 2, 3});
        List b = conc.fromArray(new int[]{1, 2, 3});
        List c = conc.fromArray(new int[]{1, 2, 4});

        System.out.println("a=" + a);
        System.out.println("shared=" + (a == b));
        System.out.println("diff=" + (a != c));

        List empty = Emptyconc.make();
        System.out.println("empty=" + empty);
        System.out.println("shared-empty=" + (empty == Emptyconc.make()));

        // length() walks the cons-list; matches the Go side's slice len.
        System.out.println("len=" + ((conc) a).length());
    }
}
