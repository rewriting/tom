
package firewall.ast.types.instructionlist;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public final class ConsInstructionList extends firewall.ast.types.instructionlist.InstructionList implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsInstructionList";


  private ConsInstructionList() {}
  private int hashCode;
  private static ConsInstructionList proto = new ConsInstructionList();
    private firewall.ast.types.Instruction _HeadInstructionList;
  private firewall.ast.types.InstructionList _TailInstructionList;

    /* static constructor */

  public static ConsInstructionList make(firewall.ast.types.Instruction _HeadInstructionList, firewall.ast.types.InstructionList _TailInstructionList) {

    // use the proto as a model
    proto.initHashCode( _HeadInstructionList,  _TailInstructionList);
    return (ConsInstructionList) factory.build(proto);

  }

  private void init(firewall.ast.types.Instruction _HeadInstructionList, firewall.ast.types.InstructionList _TailInstructionList, int hashCode) {
    this._HeadInstructionList = _HeadInstructionList;
    this._TailInstructionList = _TailInstructionList;

    this.hashCode = hashCode;
  }

  private void initHashCode(firewall.ast.types.Instruction _HeadInstructionList, firewall.ast.types.InstructionList _TailInstructionList) {
    this._HeadInstructionList = _HeadInstructionList;
    this._TailInstructionList = _TailInstructionList;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "ConsInstructionList";
  }

  private int getArity() {
    return 2;
  }

  public shared.SharedObject duplicate() {
    ConsInstructionList clone = new ConsInstructionList();
    clone.init( _HeadInstructionList,  _TailInstructionList, hashCode);
    return clone;
  }
  

  /**
    * This method implements a lexicographic order
    */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    firewall.ast.AstAbstractType ao = (firewall.ast.AstAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    ConsInstructionList tco = (ConsInstructionList) ao;
    int _HeadInstructionListCmp = (this._HeadInstructionList).compareToLPO(tco._HeadInstructionList);
    if(_HeadInstructionListCmp != 0)
      return _HeadInstructionListCmp;

    int _TailInstructionListCmp = (this._TailInstructionList).compareToLPO(tco._TailInstructionList);
    if(_TailInstructionListCmp != 0)
      return _TailInstructionListCmp;

    throw new RuntimeException("Unable to compare");
  }

  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    firewall.ast.AstAbstractType ao = (firewall.ast.AstAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* use the hash values to discriminate */
    
    if(hashCode != ao.hashCode())
      return (hashCode < ao.hashCode())?-1:1;

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* last resort: compare the childs */
    ConsInstructionList tco = (ConsInstructionList) ao;
    int _HeadInstructionListCmp = (this._HeadInstructionList).compareTo(tco._HeadInstructionList);
    if(_HeadInstructionListCmp != 0)
      return _HeadInstructionListCmp;

    int _TailInstructionListCmp = (this._TailInstructionList).compareTo(tco._TailInstructionList);
    if(_TailInstructionListCmp != 0)
      return _TailInstructionListCmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsInstructionList) {

      ConsInstructionList peer = (ConsInstructionList) obj;
      return _HeadInstructionList==peer._HeadInstructionList && _TailInstructionList==peer._TailInstructionList && true;
    }
    return false;
  }


   //InstructionList interface
  @Override
  public boolean isConsInstructionList() {
    return true;
  }
  
  @Override
  public firewall.ast.types.Instruction getHeadInstructionList() {
    return _HeadInstructionList;
  }
  
  @Override
  public firewall.ast.types.InstructionList setHeadInstructionList(firewall.ast.types.Instruction set_arg) {
    return make(set_arg, _TailInstructionList);
  }
  @Override
  public firewall.ast.types.InstructionList getTailInstructionList() {
    return _TailInstructionList;
  }
  
  @Override
  public firewall.ast.types.InstructionList setTailInstructionList(firewall.ast.types.InstructionList set_arg) {
    return make(_HeadInstructionList, set_arg);
  }
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    aterm.ATerm res = super.toATerm();
    if(res != null) {
      // the super class has produced an ATerm (may be a variadic operator)
      return res;
    }
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {getHeadInstructionList().toATerm(), getTailInstructionList().toATerm()});
  }

  public static firewall.ast.types.InstructionList fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
firewall.ast.types.Instruction.fromTerm(appl.getArgument(0),atConv), firewall.ast.types.InstructionList.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 2;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadInstructionList;
      case 1: return _TailInstructionList;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((firewall.ast.types.Instruction) v, _TailInstructionList);
      case 1: return make(_HeadInstructionList, (firewall.ast.types.InstructionList) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 2) {
      return make((firewall.ast.types.Instruction) childs[0], (firewall.ast.types.InstructionList) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _HeadInstructionList,  _TailInstructionList };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (1407196604<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadInstructionList.hashCode() << 8);
    a += (_TailInstructionList.hashCode());

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }

}
