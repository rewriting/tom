
package firewall.ast.types.file;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public final class ConsBlocks extends firewall.ast.types.file.Blocks implements tom.library.sl.Visitable  {
  
  private static String symbolName = "ConsBlocks";


  private ConsBlocks() {}
  private int hashCode;
  private static ConsBlocks proto = new ConsBlocks();
    private firewall.ast.types.Block _HeadBlocks;
  private firewall.ast.types.File _TailBlocks;

    /* static constructor */

  public static ConsBlocks make(firewall.ast.types.Block _HeadBlocks, firewall.ast.types.File _TailBlocks) {

    // use the proto as a model
    proto.initHashCode( _HeadBlocks,  _TailBlocks);
    return (ConsBlocks) factory.build(proto);

  }

  private void init(firewall.ast.types.Block _HeadBlocks, firewall.ast.types.File _TailBlocks, int hashCode) {
    this._HeadBlocks = _HeadBlocks;
    this._TailBlocks = _TailBlocks;

    this.hashCode = hashCode;
  }

  private void initHashCode(firewall.ast.types.Block _HeadBlocks, firewall.ast.types.File _TailBlocks) {
    this._HeadBlocks = _HeadBlocks;
    this._TailBlocks = _TailBlocks;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "ConsBlocks";
  }

  private int getArity() {
    return 2;
  }

  public shared.SharedObject duplicate() {
    ConsBlocks clone = new ConsBlocks();
    clone.init( _HeadBlocks,  _TailBlocks, hashCode);
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
    ConsBlocks tco = (ConsBlocks) ao;
    int _HeadBlocksCmp = (this._HeadBlocks).compareToLPO(tco._HeadBlocks);
    if(_HeadBlocksCmp != 0)
      return _HeadBlocksCmp;

    int _TailBlocksCmp = (this._TailBlocks).compareToLPO(tco._TailBlocks);
    if(_TailBlocksCmp != 0)
      return _TailBlocksCmp;

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
    ConsBlocks tco = (ConsBlocks) ao;
    int _HeadBlocksCmp = (this._HeadBlocks).compareTo(tco._HeadBlocks);
    if(_HeadBlocksCmp != 0)
      return _HeadBlocksCmp;

    int _TailBlocksCmp = (this._TailBlocks).compareTo(tco._TailBlocks);
    if(_TailBlocksCmp != 0)
      return _TailBlocksCmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof ConsBlocks) {

      ConsBlocks peer = (ConsBlocks) obj;
      return _HeadBlocks==peer._HeadBlocks && _TailBlocks==peer._TailBlocks && true;
    }
    return false;
  }


   //File interface
  @Override
  public boolean isConsBlocks() {
    return true;
  }
  
  @Override
  public firewall.ast.types.Block getHeadBlocks() {
    return _HeadBlocks;
  }
  
  @Override
  public firewall.ast.types.File setHeadBlocks(firewall.ast.types.Block set_arg) {
    return make(set_arg, _TailBlocks);
  }
  @Override
  public firewall.ast.types.File getTailBlocks() {
    return _TailBlocks;
  }
  
  @Override
  public firewall.ast.types.File setTailBlocks(firewall.ast.types.File set_arg) {
    return make(_HeadBlocks, set_arg);
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
      new aterm.ATerm[] {getHeadBlocks().toATerm(), getTailBlocks().toATerm()});
  }

  public static firewall.ast.types.File fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
firewall.ast.types.Block.fromTerm(appl.getArgument(0),atConv), firewall.ast.types.File.fromTerm(appl.getArgument(1),atConv)
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
      case 0: return _HeadBlocks;
      case 1: return _TailBlocks;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((firewall.ast.types.Block) v, _TailBlocks);
      case 1: return make(_HeadBlocks, (firewall.ast.types.File) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 2) {
      return make((firewall.ast.types.Block) childs[0], (firewall.ast.types.File) childs[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _HeadBlocks,  _TailBlocks };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (1390484373<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (_HeadBlocks.hashCode() << 8);
    a += (_TailBlocks.hashCode());

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
