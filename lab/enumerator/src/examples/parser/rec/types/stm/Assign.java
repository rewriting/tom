
package examples.parser.rec.types.stm;



public final class Assign extends examples.parser.rec.types.Stm implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Assign";


  private Assign() {}
  private int hashCode;
  private static Assign gomProto = new Assign();
    private String _Name;
  private examples.parser.rec.types.Exp _Exp;

  /**
   * Constructor that builds a term rooted by Assign
   *
   * @return a term rooted by Assign
   */

  public static Assign make(String _Name, examples.parser.rec.types.Exp _Exp) {

    // use the proto as a model
    gomProto.initHashCode( _Name,  _Exp);
    return (Assign) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _Exp
   * @param hashCode hashCode of Assign
   */
  private void init(String _Name, examples.parser.rec.types.Exp _Exp, int hashCode) {
    this._Name = _Name.intern();
    this._Exp = _Exp;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _Exp
   */
  private void initHashCode(String _Name, examples.parser.rec.types.Exp _Exp) {
    this._Name = _Name.intern();
    this._Exp = _Exp;

    this.hashCode = hashFunction();
  }

  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @return the name of the symbol
   */
  @Override
  public String symbolName() {
    return "Assign";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 2;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    Assign clone = new Assign();
    clone.init( _Name,  _Exp, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Assign(");
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
    _Exp.toStringBuilder(buffer);

    buffer.append(")");
  }


  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    Assign tco = (Assign) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _ExpCmp = (this._Exp).compareToLPO(tco._Exp);
    if(_ExpCmp != 0) {
      return _ExpCmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 /**
   * Compares two terms. This functions implements a total order.
   *
   * @param o object to which this term is compared
   * @return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @throws ClassCastException in case of invalid arguments
   * @throws RuntimeException if unable to compare children
   */
  @Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    examples.parser.rec.RecAbstractType ao = (examples.parser.rec.RecAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    Assign tco = (Assign) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _ExpCmp = (this._Exp).compareTo(tco._Exp);
    if(_ExpCmp != 0) {
      return _ExpCmp;
    }

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  /**
   * Returns hashCode
   *
   * @return hashCode
   */
  @Override
  public final int hashCode() {
    return hashCode;
  }

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @param obj SharedObject to test
   * @return true if obj is a Assign and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Assign) {

      Assign peer = (Assign) obj;
      return _Name==peer._Name && _Exp==peer._Exp && true;
    }
    return false;
  }


   //Stm interface
  /**
   * Returns true if the term is rooted by the symbol Assign
   *
   * @return true, because this is rooted by Assign
   */
  @Override
  public boolean isAssign() {
    return true;
  }
  
  /**
   * Returns the attribute String
   *
   * @return the attribute String
   */
  @Override
  public String getName() {
    return _Name;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute String which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm setName(String set_arg) {
    return make(set_arg, _Exp);
  }
  
  /**
   * Returns the attribute parser.rec.types.Exp
   *
   * @return the attribute parser.rec.types.Exp
   */
  @Override
  public examples.parser.rec.types.Exp getExp() {
    return _Exp;
  }

  /**
   * Sets and returns the attribute parser.rec.types.Stm
   *
   * @param set_arg the argument to set
   * @return the attribute parser.rec.types.Exp which just has been set
   */
  @Override
  public examples.parser.rec.types.Stm setExp(examples.parser.rec.types.Exp set_arg) {
    return make(_Name, set_arg);
  }
  
  /* AbstractType */
  /**
   * Returns an ATerm representation of this term.
   *
   * @return an ATerm representation of this term.
   */
  @Override
  public aterm.ATerm toATerm() {
    aterm.ATerm res = super.toATerm();
    if(res != null) {
      // the super class has produced an ATerm (may be a variadic operator)
      return res;
    }
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {(aterm.ATerm) atermFactory.makeAppl(atermFactory.makeAFun(getName() ,0 , true)), getExp().toATerm()});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a parser.rec.types.Stm from it
   *
   * @param trm ATerm to convert into a Gom term
   * @param atConv ATerm Converter used to convert the ATerm
   * @return the Gom term
   */
  public static examples.parser.rec.types.Stm fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
convertATermToString(appl.getArgument(0), atConv), examples.parser.rec.types.Exp.fromTerm(appl.getArgument(1),atConv)
        );
      }
    }
    return null;
  }

  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return 2;
  }

  /**
   * Returns the child at the specified index
   *
   * @param index index of the child to return; must be
             nonnegative and less than the childCount
   * @return the child at the specified index
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable getChildAt(int index) {
        switch(index) {
      case 0: return new tom.library.sl.VisitableBuiltin<String>(_Name);
      case 1: return _Exp;
      default: throw new IndexOutOfBoundsException();
 }
 }

  /**
   * Set the child at the specified index
   *
   * @param index index of the child to set; must be
             nonnegative and less than the childCount
   * @param v child to set at the specified index
   * @return the child which was just set
   * @throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
        switch(index) {
      case 0: return make(getName(), _Exp);
      case 1: return make(getName(), (examples.parser.rec.types.Exp) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 2
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof tom.library.sl.VisitableBuiltin && children[1] instanceof examples.parser.rec.types.Exp) {
      return make(((tom.library.sl.VisitableBuiltin<String>)children[0]).getBuiltin(), (examples.parser.rec.types.Exp) children[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Returns the whole children of the term
   *
   * @return the children of the term
   */
  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] { new tom.library.sl.VisitableBuiltin<String>(_Name),  _Exp};
  }

    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @return a hash value
     */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (1212502667<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_Name, 1) << 8);
    a += (_Exp.hashCode());

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

  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>> apply(final tom.library.enumerator.Enumeration<String> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp>,tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.Enumeration<examples.parser.rec.types.Stm> apply(final tom.library.enumerator.Enumeration<examples.parser.rec.types.Exp> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<String,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Stm>>) 
        new tom.library.enumerator.F<String,tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Stm>>() {
          public tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Stm> apply(final String t1) {
            return 
        new tom.library.enumerator.F<examples.parser.rec.types.Exp,examples.parser.rec.types.Stm>() {
          public examples.parser.rec.types.Stm apply(final examples.parser.rec.types.Exp t2) {
            return make(t1,t2);
          }
        };
          }
        }),t1),t2).pay();
          }
        };
          }
        };
  }

}
