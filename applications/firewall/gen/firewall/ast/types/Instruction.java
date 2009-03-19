
package firewall.ast.types;        

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
//import firewall.ast.types.instruction.*;
//import firewall.ast.*;
public abstract class Instruction extends firewall.ast.AstAbstractType  {



  public boolean isIns() {
    return false;
  }

  public firewall.ast.types.Opts getopt() {
    throw new UnsupportedOperationException("This Instruction has no opt");
  }

  public Instruction setopt(firewall.ast.types.Opts _arg) {
    throw new UnsupportedOperationException("This Instruction has no opt");
  }

  public firewall.ast.types.Target gettarget() {
    throw new UnsupportedOperationException("This Instruction has no target");
  }

  public Instruction settarget(firewall.ast.types.Target _arg) {
    throw new UnsupportedOperationException("This Instruction has no target");
  }

  public firewall.ast.types.Communication getsource() {
    throw new UnsupportedOperationException("This Instruction has no source");
  }

  public Instruction setsource(firewall.ast.types.Communication _arg) {
    throw new UnsupportedOperationException("This Instruction has no source");
  }

  public firewall.ast.types.Options getoptions() {
    throw new UnsupportedOperationException("This Instruction has no options");
  }

  public Instruction setoptions(firewall.ast.types.Options _arg) {
    throw new UnsupportedOperationException("This Instruction has no options");
  }

  public firewall.ast.types.Protocol getprot() {
    throw new UnsupportedOperationException("This Instruction has no prot");
  }

  public Instruction setprot(firewall.ast.types.Protocol _arg) {
    throw new UnsupportedOperationException("This Instruction has no prot");
  }

  public firewall.ast.types.Communication getdestination() {
    throw new UnsupportedOperationException("This Instruction has no destination");
  }

  public Instruction setdestination(firewall.ast.types.Communication _arg) {
    throw new UnsupportedOperationException("This Instruction has no destination");
  }


  public static IdConverter idConv = new IdConverter();

  public aterm.ATerm toATerm() {
    // returns null to indicates sub-classes that they have to work
    return null;
  }

  public static firewall.ast.types.Instruction fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  public static firewall.ast.types.Instruction fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  public static firewall.ast.types.Instruction fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  public static firewall.ast.types.Instruction fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    ArrayList<firewall.ast.types.Instruction> tmp = new ArrayList<firewall.ast.types.Instruction>();
    ArrayList<firewall.ast.types.Instruction> table = new ArrayList<firewall.ast.types.Instruction>();
    aterm.ATerm convertedTerm = atConv.convert(trm);
    int nbr = 0;
    firewall.ast.types.Instruction res = null;

    tmp.add(firewall.ast.types.instruction.Ins.fromTerm(convertedTerm,atConv));


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
        throw new IllegalArgumentException("This is not a Instruction " + trm);
      case 1:
        return res;
      default:
        Logger.getLogger("Instruction").log(Level.WARNING,"There were many possibilities ({0}) in {1} but the first one was chosen: {2}",new Object[] {table.toString(), "firewall.ast.types.Instruction", res.toString()});
        break;
    }
    return res;
  }

  public static firewall.ast.types.Instruction fromString(String s, ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  public static firewall.ast.types.Instruction fromStream(java.io.InputStream stream, ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
  }

  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public firewall.ast.types.Instruction reverse() {
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
