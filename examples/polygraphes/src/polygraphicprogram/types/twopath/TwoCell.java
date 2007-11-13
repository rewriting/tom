
package polygraphicprogram.types.twopath;


public final class TwoCell extends polygraphicprogram.types.TwoPath implements tom.library.sl.Visitable  {

  private TwoCell() {}

  private int hashCode;
  private static TwoCell proto = new TwoCell();
  private String _Name;
  private polygraphicprogram.types.OnePath _Source;
  private polygraphicprogram.types.OnePath _Target;
  private polygraphicprogram.types.CellType _Type;

    /* static constructor */

  public static TwoCell make(String _Name, polygraphicprogram.types.OnePath _Source, polygraphicprogram.types.OnePath _Target, polygraphicprogram.types.CellType _Type) {
    proto.initHashCode( _Name,  _Source,  _Target,  _Type);
    return (TwoCell) factory.build(proto);
  }
  
  private void init(String _Name, polygraphicprogram.types.OnePath _Source, polygraphicprogram.types.OnePath _Target, polygraphicprogram.types.CellType _Type, int hashCode) {
    this._Name = _Name.intern();
    this._Source = _Source;
    this._Target = _Target;
    this._Type = _Type;

    this.hashCode = hashCode;
  }

  private void initHashCode(String _Name, polygraphicprogram.types.OnePath _Source, polygraphicprogram.types.OnePath _Target, polygraphicprogram.types.CellType _Type) {
    this._Name = _Name.intern();
    this._Source = _Source;
    this._Target = _Target;
    this._Type = _Type;

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "TwoCell";
  }

  private int getArity() {
    return 4;
  }

  public shared.SharedObject duplicate() {
    TwoCell clone = new TwoCell();
    clone.init( _Name,  _Source,  _Target,  _Type, hashCode);
    return clone;
  }


  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("TwoCell(");
    buffer.append('"');
            for (int i = 0; i < _Name.length(); i++) {
              char c = _Name.charAt(i);
              switch (c) {
                case '\n':
                  buffer.append('\\');
                  buffer.append('n');
                  break;
                case '\t':
                  buffer.append('\\');
                  buffer.append('t');
                  break;
                case '\b':
                  buffer.append('\\');
                  buffer.append('b');
                  break;
                case '\r':
                  buffer.append('\\');
                  buffer.append('r');
                  break;
                case '\f':
                  buffer.append('\\');
                  buffer.append('f');
                  break;
                case '\\':
                  buffer.append('\\');
                  buffer.append('\\');
                  break;
                case '\'':
                  buffer.append('\\');
                  buffer.append('\'');
                  break;
                case '\"':
                  buffer.append('\\');
                  buffer.append('\"');
                  break;
                case '!':
                case '@':
                case '#':
                case '$':
                case '%':
                case '^':
                case '&':
                case '*':
                case '(':
                case ')':
                case '-':
                case '_':
                case '+':
                case '=':
                case '|':
                case '~':
                case '{':
                case '}':
                case '[':
                case ']':
                case ';':
                case ':':
                case '<':
                case '>':
                case ',':
                case '.':
                case '?':
                case ' ':
                case '/':
                  buffer.append(c);
                  break;

                default:
                  if (java.lang.Character.isLetterOrDigit(c)) {
                    buffer.append(c);
                  } else {
                    buffer.append('\\');
                    buffer.append((char) ('0' + c / 64));
                    c = (char) (c % 64);
                    buffer.append((char) ('0' + c / 8));
                    c = (char) (c % 8);
                    buffer.append((char) ('0' + c));
                  }
              }
            }
            buffer.append('"');
buffer.append(",");
    _Source.toStringBuffer(buffer);
buffer.append(",");
    _Target.toStringBuffer(buffer);
buffer.append(",");
    _Type.toStringBuffer(buffer);

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
    polygraphicprogram.PolygraphicProgramAbstractType ao = (polygraphicprogram.PolygraphicProgramAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    TwoCell tco = (TwoCell) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0)
      return _NameCmp;
             

    int _SourceCmp = (this._Source).compareToLPO(tco._Source);
    if(_SourceCmp != 0)
      return _SourceCmp;

    int _TargetCmp = (this._Target).compareToLPO(tco._Target);
    if(_TargetCmp != 0)
      return _TargetCmp;

    int _TypeCmp = (this._Type).compareToLPO(tco._Type);
    if(_TypeCmp != 0)
      return _TypeCmp;

    throw new RuntimeException("Unable to compare");
  }

  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    polygraphicprogram.PolygraphicProgramAbstractType ao = (polygraphicprogram.PolygraphicProgramAbstractType) o;
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
    TwoCell tco = (TwoCell) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0)
      return _NameCmp;
             

    int _SourceCmp = (this._Source).compareTo(tco._Source);
    if(_SourceCmp != 0)
      return _SourceCmp;

    int _TargetCmp = (this._Target).compareTo(tco._Target);
    if(_TargetCmp != 0)
      return _TargetCmp;

    int _TypeCmp = (this._Type).compareTo(tco._Type);
    if(_TypeCmp != 0)
      return _TypeCmp;

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof TwoCell) {

      TwoCell peer = (TwoCell) obj;
      return _Name==peer._Name && _Source==peer._Source && _Target==peer._Target && _Type==peer._Type && true;
    }
    return false;
  }

  /* TwoPath interface */
  @Override
  public boolean isTwoCell() {
    return true;
  }

  @Override
  public String getName() {
    return _Name;
  }
      
  @Override
  public polygraphicprogram.types.TwoPath setName(String set_arg) {
    return make(set_arg, _Source, _Target, _Type);
  }
  @Override
  public polygraphicprogram.types.OnePath getSource() {
    return _Source;
  }
      
  @Override
  public polygraphicprogram.types.TwoPath setSource(polygraphicprogram.types.OnePath set_arg) {
    return make(_Name, set_arg, _Target, _Type);
  }
  @Override
  public polygraphicprogram.types.OnePath getTarget() {
    return _Target;
  }
      
  @Override
  public polygraphicprogram.types.TwoPath setTarget(polygraphicprogram.types.OnePath set_arg) {
    return make(_Name, _Source, set_arg, _Type);
  }
  @Override
  public polygraphicprogram.types.CellType getType() {
    return _Type;
  }
      
  @Override
  public polygraphicprogram.types.TwoPath setType(polygraphicprogram.types.CellType set_arg) {
    return make(_Name, _Source, _Target, set_arg);
  }
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) atermFactory.makeAppl(atermFactory.makeAFun(getName() ,0 , true)), getSource().toATerm(), getTarget().toATerm(), getType().toATerm()});
  }

  public static polygraphicprogram.types.TwoPath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
(String) ((aterm.ATermAppl)appl.getArgument(0)).getAFun().getName(), polygraphicprogram.types.OnePath.fromTerm(appl.getArgument(1)), polygraphicprogram.types.OnePath.fromTerm(appl.getArgument(2)), polygraphicprogram.types.CellType.fromTerm(appl.getArgument(3))
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 3;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _Source;
      case 1: return _Target;
      case 2: return _Type;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make(getName(), (polygraphicprogram.types.OnePath) v, _Target, _Type);
      case 1: return make(getName(), _Source, (polygraphicprogram.types.OnePath) v, _Type);
      case 2: return make(getName(), _Source, _Target, (polygraphicprogram.types.CellType) v);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 3) {
      return make(getName(), (polygraphicprogram.types.OnePath) childs[0], (polygraphicprogram.types.OnePath) childs[1], (polygraphicprogram.types.CellType) childs[2]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  _Source,  _Target,  _Type };
  }

    /* internal use */
  protected  int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (15024392<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_Name, 3) << 24);
    a += (_Source.hashCode() << 16);
    a += (_Target.hashCode() << 8);
    a += (_Type.hashCode());

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
