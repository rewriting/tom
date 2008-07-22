import java.util.Hashtable;
import systemf.types.*;

public class TestSystemF {

  %include { systemf/SystemF.tom }

  public static void testRawCompilation() {
    RawLTerm t = `RawTAbs(RawTypeTermAbs("T",
          RawLAbs(RawTermTermAbs("x","T",RawLVar("x")))));

  }

  public static boolean testRename() {
    LTerm t1 = `TAbs(TypeTermAbs(TypeVar(0,"T"),
          LAbs(TermTermAbs(TermVar(1,"x"),TypeVar(0,"T"),LVar(TermVar(1,"x"))))));
    LTerm t2 = `TAbs(TypeTermAbs(TypeVar(0,"T"),
          LAbs(TermTermAbs(TermVar(2,"x"),TypeVar(0,"T"),LVar(TermVar(2,"x"))))));

    Hashtable<TermVar,TermVar> map = new Hashtable<TermVar,TermVar>();
    map.put(`TermVar(1,"x"),`TermVar(2,"x"));
    return t2 == t1.renameTermVar(map);
  }

  public static boolean testRefresh() {
    TypeTermAbs t1 = `TypeTermAbs(TypeVar(0,"T"),
        LAbs(TermTermAbs(TermVar(1,"x"),TypeVar(0,"T"),LVar(TermVar(1,"x")))));

    TypeTermAbs t2 = `TypeTermAbs(TypeVar(1,"T"),
        LAbs(TermTermAbs(TermVar(1,"x"),TypeVar(1,"T"),LVar(TermVar(1,"x")))));

    return t2 == t1.refresh();
  }


  public static void main(String[] args) {
    System.out.println("testRename: " +  testRename());
    System.out.println("testRefresh: " +  testRefresh());
  }
}
