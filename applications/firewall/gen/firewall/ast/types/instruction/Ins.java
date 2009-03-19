
package firewall.ast.types.instruction;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public final class Ins extends firewall.ast.types.Instruction implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Ins";


  private Ins() {}
  private int hashCode;
  private static Ins proto = new Ins();
    private firewall.ast.types.Target _target;
  private firewall.ast.types.Protocol _prot;
  private firewall.ast.types.Opts _opt;
  private firewall.ast.types.Communication _source;
  private firewall.ast.types.Communication _destination;
  private firewall.ast.types.Options _options;

    /* static constructor */

  public static Ins make(firewall.ast.types.Target _target, firewall.ast.types.Protocol _prot, firewall.ast.types.Opts _opt, firewall.ast.types.Communication _source, firewall.ast.types.Communication _destination, firewall.ast.types.Options _options) {

    // use the proto as a model
    proto.initHashCode( _target,  _prot,  _opt,  _source,  _destination,  _options);
    return (Ins) factory.build(proto);

  }

  private void init(firewall.ast.types.Target _target, firewall.ast.types.Protocol _prot, firewall.ast.types.Opts _opt, firewall.ast.types.Communication _source, firewall.ast.types.Communication _destination, firewall.ast.types.Options _options, int hashCode) {
    this._target = _target;
    this._prot = _prot;
    this._opt = _opt;
    this._source = _source;
    this._destination = _destination;
    this._options = _options;

    this.hashCode = hashCode;
  }

  private void initHashCode(firewall.ast.types.Target _target, firewall.ast.types.Protocol _prot, firewall.ast.types.Opts _opt, firewall.ast.types.Communication _source, firewall.ast.types.Communication _destination, firewall.ast.types.Options _options) {
    this._target = _target;
    this._prot = _prot;
    this._opt = _opt;
    this._source = _source;
    this._destination = _destination;
    this._options = _options;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "Ins";
  }

  private int getArity() {
    return 6;
  }

  public shared.SharedObject duplicate() {
    Ins clone = new Ins();
    clone.init( _target,  _prot,  _opt,  _source,  _destination,  _options, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Ins(");
    _target.toStringBuilder(buffer);
buffer.append(",");
    _prot.toStringBuilder(buffer);
buffer.append(",");
    _opt.toStringBuilder(buffer);
buffer.append(",");
    _source.toStringBuilder(buffer);
buffer.append(",");
    _destination.toStringBuilder(buffer);
buffer.append(",");
    _options.toStringBuilder(buffer);

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
    Ins tco = (Ins) ao;
    int _targetCmp = (this._target).compareToLPO(tco._target);
    if(_targetCmp != 0)
      return _targetCmp;

    int _protCmp = (this._prot).compareToLPO(tco._prot);
    if(_protCmp != 0)
      return _protCmp;

    int _optCmp = (this._opt).compareToLPO(tco._opt);
    if(_optCmp != 0)
      return _optCmp;

    int _sourceCmp = (this._source).compareToLPO(tco._source);
    if(_sourceCmp != 0)
      return _sourceCmp;

    int _destinationCmp = (this._destination).compareToLPO(tco._destination);
    if(_destinationCmp != 0)
      return _destinationCmp;

    int _optionsCmp = (this._options).compareToLPO(tco._options);
    if(_optionsCmp != 0)
      return _optionsCmp;

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
    Ins tco = (Ins) ao;
    int _targetCmp = (this._target).compareTo(tco._target);
    if(_targetCmp != 0)
      return _targetCmp;

    int _protCmp = (this._prot).compareTo(tco._prot);
    if(_protCmp != 0)
      return _protCmp;

    int _optCmp = (this._opt).compareTo(tco._opt);
    if(_optCmp != 0)
      return _optCmp;

    int _sourceCmp = (this._source).compareTo(tco._source);
    if(_sourceCmp != 0)
      return _sourceCmp;

    int _destinationCmp = (this._destination).compareTo(tco._destination);
    if(_destinationCmp != 0)
      return _destinationCmp;

    int _optionsCmp = (this._options).compareTo(tco._options);
    if(_optionsCmp != 0)
      return _optionsCmp;

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Ins) {

      Ins peer = (Ins) obj;
      return _target==peer._target && _prot==peer._prot && _opt==peer._opt && _source==peer._source && _destination==peer._destination && _options==peer._options && true;
    }
    return false;
  }


   //Instruction interface
  @Override
  public boolean isIns() {
    return true;
  }
  
  @Override
  public firewall.ast.types.Target gettarget() {
    return _target;
  }
  
  @Override
  public firewall.ast.types.Instruction settarget(firewall.ast.types.Target set_arg) {
    return make(set_arg, _prot, _opt, _source, _destination, _options);
  }
  @Override
  public firewall.ast.types.Protocol getprot() {
    return _prot;
  }
  
  @Override
  public firewall.ast.types.Instruction setprot(firewall.ast.types.Protocol set_arg) {
    return make(_target, set_arg, _opt, _source, _destination, _options);
  }
  @Override
  public firewall.ast.types.Opts getopt() {
    return _opt;
  }
  
  @Override
  public firewall.ast.types.Instruction setopt(firewall.ast.types.Opts set_arg) {
    return make(_target, _prot, set_arg, _source, _destination, _options);
  }
  @Override
  public firewall.ast.types.Communication getsource() {
    return _source;
  }
  
  @Override
  public firewall.ast.types.Instruction setsource(firewall.ast.types.Communication set_arg) {
    return make(_target, _prot, _opt, set_arg, _destination, _options);
  }
  @Override
  public firewall.ast.types.Communication getdestination() {
    return _destination;
  }
  
  @Override
  public firewall.ast.types.Instruction setdestination(firewall.ast.types.Communication set_arg) {
    return make(_target, _prot, _opt, _source, set_arg, _options);
  }
  @Override
  public firewall.ast.types.Options getoptions() {
    return _options;
  }
  
  @Override
  public firewall.ast.types.Instruction setoptions(firewall.ast.types.Options set_arg) {
    return make(_target, _prot, _opt, _source, _destination, set_arg);
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
      new aterm.ATerm[] {gettarget().toATerm(), getprot().toATerm(), getopt().toATerm(), getsource().toATerm(), getdestination().toATerm(), getoptions().toATerm()});
  }

  public static firewall.ast.types.Instruction fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
firewall.ast.types.Target.fromTerm(appl.getArgument(0),atConv), firewall.ast.types.Protocol.fromTerm(appl.getArgument(1),atConv), firewall.ast.types.Opts.fromTerm(appl.getArgument(2),atConv), firewall.ast.types.Communication.fromTerm(appl.getArgument(3),atConv), firewall.ast.types.Communication.fromTerm(appl.getArgument(4),atConv), firewall.ast.types.Options.fromTerm(appl.getArgument(5),atConv)
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 6;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _target;
      case 1: return _prot;
      case 2: return _opt;
      case 3: return _source;
      case 4: return _destination;
      case 5: return _options;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make((firewall.ast.types.Target) v, _prot, _opt, _source, _destination, _options);
      case 1: return make(_target, (firewall.ast.types.Protocol) v, _opt, _source, _destination, _options);
      case 2: return make(_target, _prot, (firewall.ast.types.Opts) v, _source, _destination, _options);
      case 3: return make(_target, _prot, _opt, (firewall.ast.types.Communication) v, _destination, _options);
      case 4: return make(_target, _prot, _opt, _source, (firewall.ast.types.Communication) v, _options);
      case 5: return make(_target, _prot, _opt, _source, _destination, (firewall.ast.types.Options) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 6) {
      return make((firewall.ast.types.Target) childs[0], (firewall.ast.types.Protocol) childs[1], (firewall.ast.types.Opts) childs[2], (firewall.ast.types.Communication) childs[3], (firewall.ast.types.Communication) childs[4], (firewall.ast.types.Options) childs[5]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _target,  _prot,  _opt,  _source,  _destination,  _options };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1646446586<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    b += (_target.hashCode() << 8);
    b += (_prot.hashCode());
    a += (_opt.hashCode() << 24);
    a += (_source.hashCode() << 16);
    a += (_destination.hashCode() << 8);
    a += (_options.hashCode());

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
