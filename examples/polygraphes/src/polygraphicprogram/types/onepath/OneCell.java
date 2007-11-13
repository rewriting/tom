
package polygraphicprogram.types.onepath;


public final class OneCell extends polygraphicprogram.types.OnePath implements tom.library.sl.Visitable  {

  private OneCell() {}

  private int hashCode;
  private static OneCell proto = new OneCell();
  private String _Name;

    /* static constructor */

  public static OneCell make(String _Name) {
    proto.initHashCode( _Name);
    return (OneCell) factory.build(proto);
  }
  
  private void init(String _Name, int hashCode) {
    this._Name = _Name.intern();

    this.hashCode = hashCode;
  }

  private void initHashCode(String _Name) {
    this._Name = _Name.intern();

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "OneCell";
  }

  private int getArity() {
    return 1;
  }

  public shared.SharedObject duplicate() {
    OneCell clone = new OneCell();
    clone.init( _Name, hashCode);
    return clone;
  }


  @Override
  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("OneCell(");
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
    OneCell tco = (OneCell) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0)
      return _NameCmp;
             

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
    OneCell tco = (OneCell) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0)
      return _NameCmp;
             

    throw new RuntimeException("Unable to compare");
  }

  /* shared.SharedObject */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof OneCell) {

      OneCell peer = (OneCell) obj;
      return _Name==peer._Name && true;
    }
    return false;
  }

  /* OnePath interface */
  @Override
  public boolean isOneCell() {
    return true;
  }

  @Override
  public String getName() {
    return _Name;
  }
      
  @Override
  public polygraphicprogram.types.OnePath setName(String set_arg) {
    return make(set_arg);
  }
  /* AbstractType */
  @Override
  public aterm.ATerm toATerm() {
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) atermFactory.makeAppl(atermFactory.makeAFun(getName() ,0 , true))});
  }

  public static polygraphicprogram.types.OnePath fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(proto.symbolName().equals(appl.getName())) {
        return make(
(String) ((aterm.ATermAppl)appl.getArgument(0)).getAFun().getName()
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 0;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 0) {
      return make(getName());
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  };
  }

    /* internal use */
  protected  int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1797595946<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_Name, 0));

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
