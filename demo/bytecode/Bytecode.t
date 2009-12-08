import tom.library.adt.bytecode.types.*;

public class Bytecode {
  %include { adt/bytecode/Bytecode.tom }

  public static void main(String[] args) {
    TInstruction i = `Iload(3);
    i = updateIndex(i,3,5);
    System.out.println("new i = " + i);
 
    
    TInstructionList p = `InstructionList(Istore(3),Istore(2),Iload(3));
    boolean b= hasIstore(p);
    System.out.println("b = " + b); 
  }

  static boolean hasIstore(TInstructionList l) {
    %match(l) {    
      InstructionList(a*,Istore(index),b*) -> { return true; }
    }
    return false;
  }

  static TInstruction updateIndex(TInstruction i, int oldI, int newI) {
    %match(i) {
      Istore(x) -> { if(`x==oldI) return `Istore(newI); } 
      Iload(x)  -> { if(`x==oldI) return `Iload(newI);  }
    }
    return i;
  }

}
