
package sa.rule.types.stratdecl;



public final class StratDecl extends sa.rule.types.StratDecl implements tom.library.sl.Visitable  {
  
  private static String symbolName = "StratDecl";


  private StratDecl() {}
  private int hashCode;
  private static StratDecl gomProto = new StratDecl();
    private String _Name;
  private sa.rule.types.ParamList _ParamList;
  private sa.rule.types.Strat _Body;

  /**
   * Constructor that builds a term rooted by StratDecl
   *
   * @return a term rooted by StratDecl
   */

  public static StratDecl make(String _Name, sa.rule.types.ParamList _ParamList, sa.rule.types.Strat _Body) {

    // use the proto as a model
    gomProto.initHashCode( _Name,  _ParamList,  _Body);
    return (StratDecl) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _ParamList
   * @param _Body
   * @param hashCode hashCode of StratDecl
   */
  private void init(String _Name, sa.rule.types.ParamList _ParamList, sa.rule.types.Strat _Body, int hashCode) {
    this._Name = _Name.intern();
    this._ParamList = _ParamList;
    this._Body = _Body;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _ParamList
   * @param _Body
   */
  private void initHashCode(String _Name, sa.rule.types.ParamList _ParamList, sa.rule.types.Strat _Body) {
    this._Name = _Name.intern();
    this._ParamList = _ParamList;
    this._Body = _Body;

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
    return "StratDecl";
  }

  /**
   * Returns the arity of the symbol
   *
   * @return the arity of the symbol
   */
  private int getArity() {
    return 3;
  }

  /**
   * Copy the object and returns the copy
   *
   * @return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    StratDecl clone = new StratDecl();
    clone.init( _Name,  _ParamList,  _Body, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("StratDecl(");
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
    _ParamList.toStringBuilder(buffer);
buffer.append(",");
    _Body.toStringBuilder(buffer);

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
    StratDecl tco = (StratDecl) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _ParamListCmp = (this._ParamList).compareToLPO(tco._ParamList);
    if(_ParamListCmp != 0) {
      return _ParamListCmp;
    }

    int _BodyCmp = (this._Body).compareToLPO(tco._Body);
    if(_BodyCmp != 0) {
      return _BodyCmp;
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
    StratDecl tco = (StratDecl) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _ParamListCmp = (this._ParamList).compareTo(tco._ParamList);
    if(_ParamListCmp != 0) {
      return _ParamListCmp;
    }

    int _BodyCmp = (this._Body).compareTo(tco._Body);
    if(_BodyCmp != 0) {
      return _BodyCmp;
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
   * @return true if obj is a StratDecl and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof StratDecl) {

      StratDecl peer = (StratDecl) obj;
      return _Name==peer._Name && _ParamList==peer._ParamList && _Body==peer._Body && true;
    }
    return false;
  }


   //StratDecl interface
  /**
   * Returns true if the term is rooted by the symbol StratDecl
   *
   * @return true, because this is rooted by StratDecl
   */
  @Override
  public boolean isStratDecl() {
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
   * Sets and returns the attribute sa.rule.types.StratDecl
   *
   * @param set_arg the argument to set
   * @return the attribute String which just has been set
   */
  @Override
  public sa.rule.types.StratDecl setName(String set_arg) {
    return make(set_arg, _ParamList, _Body);
  }
  
  /**
   * Returns the attribute sa.rule.types.ParamList
   *
   * @return the attribute sa.rule.types.ParamList
   */
  @Override
  public sa.rule.types.ParamList getParamList() {
    return _ParamList;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratDecl
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.ParamList which just has been set
   */
  @Override
  public sa.rule.types.StratDecl setParamList(sa.rule.types.ParamList set_arg) {
    return make(_Name, set_arg, _Body);
  }
  
  /**
   * Returns the attribute sa.rule.types.Strat
   *
   * @return the attribute sa.rule.types.Strat
   */
  @Override
  public sa.rule.types.Strat getBody() {
    return _Body;
  }

  /**
   * Sets and returns the attribute sa.rule.types.StratDecl
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.Strat which just has been set
   */
  @Override
  public sa.rule.types.StratDecl setBody(sa.rule.types.Strat set_arg) {
    return make(_Name, _ParamList, set_arg);
  }
  
  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @return the number of children of the term
   */
  public int getChildCount() {
    return 3;
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
      case 1: return _ParamList;
      case 2: return _Body;
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
      case 0: return make(getName(), _ParamList, _Body);
      case 1: return make(getName(), (sa.rule.types.ParamList) v, _Body);
      case 2: return make(getName(), _ParamList, (sa.rule.types.Strat) v);
      default: throw new IndexOutOfBoundsException();
 }
  }

  /**
   * Set children to the term
   *
   * @param children array of children to set
   * @return an array of children which just were set
   * @throws IndexOutOfBoundsException if length of "children" is different than 3
   */
  @SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount()  && children[0] instanceof tom.library.sl.VisitableBuiltin && children[1] instanceof sa.rule.types.ParamList && children[2] instanceof sa.rule.types.Strat) {
      return make(((tom.library.sl.VisitableBuiltin<String>)children[0]).getBuiltin(), (sa.rule.types.ParamList) children[1], (sa.rule.types.Strat) children[2]);
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
    return new tom.library.sl.Visitable[] { new tom.library.sl.VisitableBuiltin<String>(_Name),  _ParamList,  _Body};
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
    b = (-1314848121<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_Name, 2) << 16);
    a += (_ParamList.hashCode() << 8);
    a += (_Body.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>>> apply(final tom.library.enumerator.Enumeration<String> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.ParamList>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.ParamList> t2) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.Strat>,tom.library.enumerator.Enumeration<sa.rule.types.StratDecl>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.StratDecl> apply(final tom.library.enumerator.Enumeration<sa.rule.types.Strat> t3) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.ParamList,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl>>>) 
        new tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.ParamList,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl>>>() {
          public tom.library.enumerator.F<sa.rule.types.ParamList,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl>> apply(final String t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.ParamList,tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl>>() {
          public tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl> apply(final sa.rule.types.ParamList t2) {
            return 
        new tom.library.enumerator.F<sa.rule.types.Strat,sa.rule.types.StratDecl>() {
          public sa.rule.types.StratDecl apply(final sa.rule.types.Strat t3) {
            return make(t1,t2,t3);
          }
        };
          }
        };
          }
        }),t1),t2),t3).pay();
          }
        };
          }
        };
          }
        };
  }

}
