// Scenario for the Minimal.gom equivalence test, Java reference side.
// Must emit byte-identical stdout to scenario.go.
import minimal.types.Nop;
import minimal.types.nop.EmptyNop;
import minimal.types.nop.UnaryNop;
import minimal.types.nop.BinaryNop;

public class Scenario {
    public static void main(String[] args) {
        Nop t1 = EmptyNop.make();
        Nop t2 = UnaryNop.make(EmptyNop.make());
        Nop t3 = BinaryNop.make(EmptyNop.make(), UnaryNop.make(EmptyNop.make()));

        System.out.println("t1=" + t1);
        System.out.println("t2=" + t2);
        System.out.println("t3=" + t3);

        System.out.println("shared-emptynop=" + (EmptyNop.make() == t1));
        Nop a = BinaryNop.make(EmptyNop.make(), EmptyNop.make());
        Nop b = BinaryNop.make(EmptyNop.make(), EmptyNop.make());
        System.out.println("shared-binarynop=" + (a == b));

        boolean diff = BinaryNop.make(EmptyNop.make(), EmptyNop.make())
            != BinaryNop.make(EmptyNop.make(), UnaryNop.make(EmptyNop.make()));
        System.out.println("different-terms=" + diff);

        BinaryNop bn = (BinaryNop) BinaryNop.make(
            EmptyNop.make(),
            UnaryNop.make(EmptyNop.make()));
        System.out.println("binarynop-ls=" + bn.getls());
        System.out.println("binarynop-rs=" + bn.getrs());
    }
}
