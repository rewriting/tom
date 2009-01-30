package strategycompiler;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;


/**
 * Collector for method related stuff.
 * This collector contains a list of local variables, a list of try/catch blocks
 * and an instruction list.
 * All these informations are used to generate a method term in the strategy
 * compiler.
 */
public class MethodCollector {

  %include { adt/bytecode/Bytecode.tom }
  
  private TLocalVariableList varList;
  private TTryCatchBlockList tcbList;
  private TInstructionList insList;

  public MethodCollector() {
    varList = `LocalVariableList();
    tcbList = `TryCatchBlockList();
    insList = `InstructionList();
  }

  public void addVarList(TLocalVariableList varL) {
    varList = `LocalVariableList(varL*, varList*);
  }

  public TLocalVariableList getVariableList() {
    return varList;
  }

  public void addTryCatchBlockList(TTryCatchBlockList tcbL) {
    tcbList = `TryCatchBlockList(tcbL*, tcbList*);
  }

  public TTryCatchBlockList getTryCatchBlockList() {
    return tcbList;
  }

  public void addInstruction(TInstruction ins) {
    insList = `InstructionList(ins, insList*);
  }

  public TInstructionList getInstructionList() {
    // The list is reversed because instruction order is important, and
    // instructions are added in the beginning of the list.
    return insList.reverse();
  }
}

