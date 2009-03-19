
package firewall.ast.types.block;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public final class Block extends firewall.ast.types.Block implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Block";


  private Block() {}
  private int hashCode;
  private static Block proto = new Block();
    private firewall.ast.types.Rule _rule;
  private firewall.ast.types.InstructionList _instructionList;

    /* static constructor */

  public static Block make(firewall.ast.types.Rule _rule, firewall.ast.types.InstructionList _instructionList) {

    // use the proto as a model
    proto.initHashCode( _rule,  _instructionList);
    return (Block) factory.build(proto);

  }

  private void init(firewall.ast.types.Rule _rule, firewall.ast.types.InstructionList _instructionList, int hashCode) {
    this._rule = _rule;
    this._instructionList = _instructionList;

    this.hashCode = hashCode;
  }

  private void initHashCode(firewall.ast.types.Rule _rule, firewall.ast.types.InstructionList _instructionList) {
    this._rule = _rule;
    this._instructionList = _instructionList;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "Block";
  }

  private int getArity() {
    return 2;
  }

  public shared.SharedObject duplicate() {
    Block clone = new Block();
    clone.init( _rule,  _instructionList, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Block(");
    _rule.toStringBuilder(buffer);
buffer.append(",");
    _instructionList.toStringBuilder(buffer);

    buffer.append(")");
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
    Block tco = (Block) ao;
    int _ruleCmp = (this._rule).compareToLPO(tco._rule);
    if(_ruleCmp != 0)
      return _ruleCmp;

    int _instructionListCmp = (this._instructionList).compareToLPO(tco._instructionList);
    if(_instructionListCmp != 0)
      return _instructionListCmp;

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
    Block tco = (Block) ao;
    int _ruleCmp = (this._rule).compareTo(tco._rule);
    if(_ruleCmp != 0)
      return _ruleCmp;

    int _instructionListCmp = (this._instructionList).compareTo(tco._instructionList);
    if(_instructionListCmp != 0)
      return _instructionListCmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Block) {

      Block peer = (Block) obj;
      return _rule==peer._rule && _instructionList==peer._instructionList && true;
    }
    return false;
  }


   //Block interface
  @Override
  public boolean isBlock() {
    return true;
  }
  
  @Override
  public firewall.ast.types.Rule getrule() {
    return _rule;
  }
  
  @Override
  public firewall.ast.types.Block setrule(firewall.ast.types.Rule set_arg) {
    return make(set_arg, _instructionList);
  }
  @Override
  public firewall.ast.types.InstructionList getinstructionList() {
    return _instructionList;
  }
  
  @Override
  public firewall.ast.types.Block setinstructionList(firewall.ast.types.InstructionList set_arg) {
    return make(_rule, set_arg);
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
      new aterm.ATerm[] {getrule().toATerm(), getinstructionList().toATerm()});
  }

  public static firewall.ast.types.Block fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
firewall.ast.types.Rule.fromTerm(appl.getArgument(0),atConv), firewall.ast.types.InstructionList.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _rule;
      case 1: return _instructionList;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((firewall.ast.types.Rule) v, _instructionList);
      case 1: return make(_rule, (firewall.ast.types.InstructionList) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 2) {
      return make((firewall.ast.types.Rule) childs[0], (firewall.ast.types.InstructionList) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _rule,  _instructionList };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1475859203<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_rule.hashCode() << 8);
    a += (_instructionList.hashCode());

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
