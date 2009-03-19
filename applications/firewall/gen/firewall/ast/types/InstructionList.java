
package firewall.ast.types;        

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
//import firewall.ast.types.instructionlist.*;
//import firewall.ast.*;
public abstract class InstructionList extends firewall.ast.AstAbstractType  {



  public boolean isConsInstructionList() {
    return false;
  }

  public boolean isEmptyInstructionList() {
    return false;
  }

  public firewall.ast.types.Instruction getHeadInstructionList() {
    throw new UnsupportedOperationException("This InstructionList has no HeadInstructionList");
  }

  public InstructionList setHeadInstructionList(firewall.ast.types.Instruction _arg) {
    throw new UnsupportedOperationException("This InstructionList has no HeadInstructionList");
  }

  public firewall.ast.types.InstructionList getTailInstructionList() {
    throw new UnsupportedOperationException("This InstructionList has no TailInstructionList");
  }

  public InstructionList setTailInstructionList(firewall.ast.types.InstructionList _arg) {
    throw new UnsupportedOperationException("This InstructionList has no TailInstructionList");
  }


  public static IdConverter idConv = new IdConverter();

  public aterm.ATerm toATerm() {
    // returns null to indicates sub-classes that they have to work
    return null;
  }

  public static firewall.ast.types.InstructionList fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  public static firewall.ast.types.InstructionList fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  public static firewall.ast.types.InstructionList fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  public static firewall.ast.types.InstructionList fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    ArrayList<firewall.ast.types.InstructionList> tmp = new ArrayList<firewall.ast.types.InstructionList>();
    ArrayList<firewall.ast.types.InstructionList> table = new ArrayList<firewall.ast.types.InstructionList>();
    aterm.ATerm convertedTerm = atConv.convert(trm);
    int nbr = 0;
    firewall.ast.types.InstructionList res = null;

    tmp.add(firewall.ast.types.instructionlist.ConsInstructionList.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.instructionlist.EmptyInstructionList.fromTerm(convertedTerm,atConv));


    tmp.add(firewall.ast.types.instructionlist.InstructionList.fromTerm(convertedTerm,atConv));


    for(int i=0;i<tmp.size();i++) {
      if(tmp.get(i) != null) {
        nbr++;
        table.add(tmp.get(i));
        if (res == null) {
          res = tmp.get(i);
        }
      }
    }
    switch(nbr) {
      case 0:
        throw new IllegalArgumentException("This is not a InstructionList " + trm);
      case 1:
        return res;
      default:
        Logger.getLogger("InstructionList").log(Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {table.toString(), "firewall.ast.types.InstructionList", res.toString()});
        break;
    }
    return res;
  }

  public static firewall.ast.types.InstructionList fromString(String s, ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  public static firewall.ast.types.InstructionList fromStream(java.io.InputStream stream, ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
  }

  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public firewall.ast.types.InstructionList reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Collection
   */
  /*
  public boolean add(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean containsAll(java.util.Collection c) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean contains(Object o) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean equals(Object o) { return this == o; }

  public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return false; }

  public java.util.Iterator iterator() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public int size() { return length(); }

  public Object[] toArray() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public Object[] toArray(Object[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
  */
  
}
