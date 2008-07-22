import systemf.types.*;

public class TestSystemF {

  %include { systemf/SystemF.tom }

  public static void testRawCompilation() {
    RawLTerm t = `RawTAbs(RawTypeTermAbs("T",
          RawLAbs(RawTermTermAbs("x",RawLVar("x")))));

  }

  public static boolean testRename() {
    LTerm t1 = `TAbs(TypeTermAbs(TypeVar(0,"T"),
          LAbs(TermTermAbs(TermVar(1,"x"),LVar(TermVar(1,"x"))))));
    LTerm t2 = `TAbs(TypeTermAbs(TypeVar(0,"T"),
          LAbs(TermTermAbs(TermVar(2,"x"),LVar(TermVar(2,"x"))))));

    return t2 == t1.renameTermVar(`TermVar(1,"x"),`TermVar(2,"x"));
  }

  public static void main(String[] args) {
    System.out.println("testRename: " +  testRename());
  }
}
