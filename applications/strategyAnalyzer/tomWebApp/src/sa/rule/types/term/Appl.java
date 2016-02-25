
package sa.rule.types.term;



public final class Appl extends sa.rule.types.Term implements tom.library.sl.Visitable  {
  
  private static String symbolName = "Appl";


  private Appl() {}
  private int hashCode;
  private static Appl gomProto = new Appl();
    private String _symbol;
  private sa.rule.types.TermList _args;

  /**
   * Constructor that builds a term rooted by Appl
   *
   * @return a term rooted by Appl
   */

  public static Appl make(String _symbol, sa.rule.types.TermList _args) {

    // use the proto as a model
    gomProto.initHashCode( _symbol,  _args);
    return (Appl) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _symbol
   * @param _args
   * @param hashCode hashCode of Appl
   */
  private void init(String _symbol, sa.rule.types.TermList _args, int hashCode) {
    this._symbol = _symbol.intern();
    this._args = _args;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _symbol
   * @param _args
   */
  private void initHashCode(String _symbol, sa.rule.types.TermList _args) {
    this._symbol = _symbol.intern();
    this._args = _args;

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
    return "Appl";
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
    Appl clone = new Appl();
    clone.init( _symbol,  _args, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("Appl(");
    buffer.append('"');
            for (int i = 0; i < _symbol.length(); i++) {
              char c = _symbol.charAt(i);
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
    _args.toStringBuilder(buffer);

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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    Appl tco = (Appl) ao;
    int _symbolCmp = (this._symbol).compareTo(tco._symbol);
    if(_symbolCmp != 0) {
      return _symbolCmp;
    }


    int _argsCmp = (this._args).compareToLPO(tco._args);
    if(_argsCmp != 0) {
      return _argsCmp;
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
    sa.rule.RuleAbstractType ao = (sa.rule.RuleAbstractType) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    Appl tco = (Appl) ao;
    int _symbolCmp = (this._symbol).compareTo(tco._symbol);
    if(_symbolCmp != 0) {
      return _symbolCmp;
    }


    int _argsCmp = (this._args).compareTo(tco._args);
    if(_argsCmp != 0) {
      return _argsCmp;
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
   * @return true if obj is a Appl and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof Appl) {

      Appl peer = (Appl) obj;
      return _symbol==peer._symbol && _args==peer._args && true;
    }
    return false;
  }


   //Term interface
  /**
   * Returns true if the term is rooted by the symbol Appl
   *
   * @return true, because this is rooted by Appl
   */
  @Override
  public boolean isAppl() {
    return true;
  }
  
  /**
   * Returns the attribute String
   *
   * @return the attribute String
   */
  @Override
  public String getsymbol() {
    return _symbol;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Term
   *
   * @param set_arg the argument to set
   * @return the attribute String which just has been set
   */
  @Override
  public sa.rule.types.Term setsymbol(String set_arg) {
    return make(set_arg, _args);
  }
  
  /**
   * Returns the attribute sa.rule.types.TermList
   *
   * @return the attribute sa.rule.types.TermList
   */
  @Override
  public sa.rule.types.TermList getargs() {
    return _args;
  }

  /**
   * Sets and returns the attribute sa.rule.types.Term
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.TermList which just has been set
   */
  @Override
  public sa.rule.types.Term setargs(sa.rule.types.TermList set_arg) {
    return make(_symbol, set_arg);
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
      case 0: return new tom.library.sl.VisitableBuiltin<String>(_symbol);
      case 1: return _args;
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
      case 0: return make(getsymbol(), _args);
      case 1: return make(getsymbol(), (sa.rule.types.TermList) v);
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
    if (children.length == getChildCount()  && children[0] instanceof tom.library.sl.VisitableBuiltin && children[1] instanceof sa.rule.types.TermList) {
      return make(((tom.library.sl.VisitableBuiltin<String>)children[0]).getBuiltin(), (sa.rule.types.TermList) children[1]);
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
    return new tom.library.sl.Visitable[] { new tom.library.sl.VisitableBuiltin<String>(_symbol),  _args};
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
    b = (-1045111884<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_symbol, 1) << 8);
    a += (_args.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.Term>> apply(final tom.library.enumerator.Enumeration<String> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TermList>,tom.library.enumerator.Enumeration<sa.rule.types.Term>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.Term> apply(final tom.library.enumerator.Enumeration<sa.rule.types.TermList> t2) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.Term>>) 
        new tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.Term>>() {
          public tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.Term> apply(final String t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.TermList,sa.rule.types.Term>() {
          public sa.rule.types.Term apply(final sa.rule.types.TermList t2) {
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
