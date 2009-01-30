package strategycompiler;

import java.util.HashMap;

/**
 * The symbol table is used to match old local variable indexes and labels with
 * new ones. It is used into the strategy compiler to prevent name capture while
 * inlining.
 */
public class SymbolTable {
  private static int localVariableCount = 1;  // Starts to 1 to handle aload_0/astore_0
  private static int labelCount = 0;

  private HashMap localVariableTable;
  private HashMap labelTable;

  public SymbolTable() {
    localVariableTable = new HashMap();
    labelTable = new HashMap();
  }

  public int getLocalVariableIndex(int old) {
    int newi = 0;
    if(old != 0) {
      Integer oldi = new Integer(old);
      Object o = localVariableTable.get(oldi);
      if(o == null) {
        newi = localVariableCount++;
        localVariableTable.put(oldi, new Integer(newi));
      } else
        newi = ((Integer)o).intValue();
    }

    return newi;
  }

  public int getFreshLocalVariableIndex() {
    return localVariableCount++;
  }

  public int getLabelIndex(int old) {
    Integer oldi = new Integer(old);
    Object o = labelTable.get(oldi);
    int newi = 0;
    if(o == null) {
      newi = labelCount++;
      labelTable.put(oldi, new Integer(newi));
    } else
      newi = ((Integer)o).intValue();

    return newi;
  }

  public int getFreshLabelIndex() {
    return labelCount++;
  }
}

