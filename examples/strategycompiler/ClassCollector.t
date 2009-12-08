
package strategycompiler;

import java.util.Stack;
import java.util.ArrayList;

import java.util.EmptyStackException;

import tom.library.sl.*;

import tom.library.adt.bytecode.*;
import tom.library.adt.bytecode.types.*;


/**
 * Collector for class related stuff.
 * This collector contains a field list and a method list used to generate the
 * compiled class in the strategy compiler.
 * A subterms list is present too. It is used by the `compile' method of the
 * strategy compiler to instantiate the compiled strategy with `%strategy'
 * related `Strategy'.
 * Finally, a `Strategy' stack is used while inlining is running to store
 * `getArgument' return values.
 */
public class ClassCollector {
  %include { adt/bytecode/Bytecode.tom }

  private FieldList fieldList;
  private MethodList methodList;
  private Stack strategyStack;
  private ArrayList subtermList;

  public ClassCollector() {
    fieldList = `FieldList();
    methodList = `MethodList();
    strategyStack = new Stack();
    subtermList = new ArrayList();
  }

  public void addField(Field field) {
    %match(fieldList, Field field) {
      FieldList(_*, x, _*), x -> {
        return;
      }
    }
    fieldList = `FieldList(field, fieldList*);
  }

  public FieldList getFieldList() {
    return fieldList;
  }

  public void addMethod(Method method) {
    methodList = `MethodList(method, methodList*);
  }

  public MethodList getMethodList() {
    return methodList;
  }

  public void pushStrategy(Strategy s) {
    strategyStack.push(s);
  }

  public Strategy popStrategy() {
    try {
      return (Strategy)strategyStack.pop();
    } catch(EmptyStackException ese) {
      ese.printStackTrace();
    }

    return null;
  }

  public int addSubterm(Strategy subterm) {
    subtermList.add(subterm);
    return subtermList.size() - 1;
  }

  public Strategy[] getSubterms() {
    Strategy[] array = new Strategy[subtermList.size()];
    for(int i = 0; i < array.length; ++i)
      array[i] = (Strategy)subtermList.get(i);
    return array;
  }

}

