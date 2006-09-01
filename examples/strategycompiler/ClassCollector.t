
package strategycompiler;

import java.util.Stack;
import java.util.ArrayList;

import java.util.EmptyStackException;

import tom.library.strategy.mutraveler.MuStrategy;

import strategycompiler.classtree.*;
import strategycompiler.classtree.types.*;

/**
 * Collector for class related stuff.
 * This collector contains a field list and a method list used to generate the
 * compiled class in the strategy compiler.
 * A subterms list is present too. It is used by the `compile' method of the
 * strategy compiler to instantiate the compiled strategy with `%strategy'
 * related `MuStrategy'.
 * Finally, a `MuStrategy' stack is used while inlining is running to store
 * `getArgument' return values.
 */
public class ClassCollector {
  %include { classtree/ClassTree.tom }

  private TFieldList fieldList;
  private TMethodList methodList;
  private Stack strategyStack;
  private ArrayList subtermList;

  public ClassCollector() {
    fieldList = `FieldList();
    methodList = `MethodList();
    strategyStack = new Stack();
    subtermList = new ArrayList();
  }

  public void addField(TField field) {
    %match(TFieldList fieldList, TField field) {
      (_*, x, _*), x -> {
        return;
      }
    }
    fieldList = `FieldList(field, fieldList*);
  }

  public TFieldList getFieldList() {
    return fieldList;
  }

  public void addMethod(TMethod method) {
    methodList = `MethodList(method, methodList*);
  }

  public TMethodList getMethodList() {
    return methodList;
  }

  public void pushStrategy(MuStrategy s) {
    strategyStack.push(s);
  }

  public MuStrategy popStrategy() {
    try {
      return (MuStrategy)strategyStack.pop();
    } catch(EmptyStackException ese) {
      ese.printStackTrace();
    }

    return null;
  }

  public int addSubterm(MuStrategy subterm) {
    subtermList.add(subterm);
    return subtermList.size() - 1;
  }

  public MuStrategy[] getSubterms() {
    MuStrategy[] array = new MuStrategy[subtermList.size()];
    for(int i = 0; i < array.length; ++i)
      array[i] = (MuStrategy)subtermList.get(i);
    return array;
  }
}

