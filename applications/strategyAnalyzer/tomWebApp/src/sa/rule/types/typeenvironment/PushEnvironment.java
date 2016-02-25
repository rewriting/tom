
package sa.rule.types.typeenvironment;



public final class PushEnvironment extends sa.rule.types.TypeEnvironment implements tom.library.sl.Visitable  {
  
  private static String symbolName = "PushEnvironment";


  private PushEnvironment() {}
  private int hashCode;
  private static PushEnvironment gomProto = new PushEnvironment();
    private String _Name;
  private sa.rule.types.GomType _Type;
  private sa.rule.types.TypeEnvironment _Env;

  /**
   * Constructor that builds a term rooted by PushEnvironment
   *
   * @return a term rooted by PushEnvironment
   */

  public static PushEnvironment make(String _Name, sa.rule.types.GomType _Type, sa.rule.types.TypeEnvironment _Env) {

    // use the proto as a model
    gomProto.initHashCode( _Name,  _Type,  _Env);
    return (PushEnvironment) factory.build(gomProto);

  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _Type
   * @param _Env
   * @param hashCode hashCode of PushEnvironment
   */
  private void init(String _Name, sa.rule.types.GomType _Type, sa.rule.types.TypeEnvironment _Env, int hashCode) {
    this._Name = _Name.intern();
    this._Type = _Type;
    this._Env = _Env;

    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @param  _Name
   * @param _Type
   * @param _Env
   */
  private void initHashCode(String _Name, sa.rule.types.GomType _Type, sa.rule.types.TypeEnvironment _Env) {
    this._Name = _Name.intern();
    this._Type = _Type;
    this._Env = _Env;

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
    return "PushEnvironment";
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
    PushEnvironment clone = new PushEnvironment();
    clone.init( _Name,  _Type,  _Env, hashCode);
    return clone;
  }
  
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @param buffer the buffer to which a string represention of this term is appended.
   */
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("PushEnvironment(");
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
    _Type.toStringBuilder(buffer);
buffer.append(",");
    _Env.toStringBuilder(buffer);

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
    PushEnvironment tco = (PushEnvironment) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _TypeCmp = (this._Type).compareToLPO(tco._Type);
    if(_TypeCmp != 0) {
      return _TypeCmp;
    }

    int _EnvCmp = (this._Env).compareToLPO(tco._Env);
    if(_EnvCmp != 0) {
      return _EnvCmp;
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
    PushEnvironment tco = (PushEnvironment) ao;
    int _NameCmp = (this._Name).compareTo(tco._Name);
    if(_NameCmp != 0) {
      return _NameCmp;
    }


    int _TypeCmp = (this._Type).compareTo(tco._Type);
    if(_TypeCmp != 0) {
      return _TypeCmp;
    }

    int _EnvCmp = (this._Env).compareTo(tco._Env);
    if(_EnvCmp != 0) {
      return _EnvCmp;
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
   * @return true if obj is a PushEnvironment and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof PushEnvironment) {

      PushEnvironment peer = (PushEnvironment) obj;
      return _Name==peer._Name && _Type==peer._Type && _Env==peer._Env && true;
    }
    return false;
  }


   //TypeEnvironment interface
  /**
   * Returns true if the term is rooted by the symbol PushEnvironment
   *
   * @return true, because this is rooted by PushEnvironment
   */
  @Override
  public boolean isPushEnvironment() {
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
   * Sets and returns the attribute sa.rule.types.TypeEnvironment
   *
   * @param set_arg the argument to set
   * @return the attribute String which just has been set
   */
  @Override
  public sa.rule.types.TypeEnvironment setName(String set_arg) {
    return make(set_arg, _Type, _Env);
  }
  
  /**
   * Returns the attribute sa.rule.types.GomType
   *
   * @return the attribute sa.rule.types.GomType
   */
  @Override
  public sa.rule.types.GomType getType() {
    return _Type;
  }

  /**
   * Sets and returns the attribute sa.rule.types.TypeEnvironment
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.GomType which just has been set
   */
  @Override
  public sa.rule.types.TypeEnvironment setType(sa.rule.types.GomType set_arg) {
    return make(_Name, set_arg, _Env);
  }
  
  /**
   * Returns the attribute sa.rule.types.TypeEnvironment
   *
   * @return the attribute sa.rule.types.TypeEnvironment
   */
  @Override
  public sa.rule.types.TypeEnvironment getEnv() {
    return _Env;
  }

  /**
   * Sets and returns the attribute sa.rule.types.TypeEnvironment
   *
   * @param set_arg the argument to set
   * @return the attribute sa.rule.types.TypeEnvironment which just has been set
   */
  @Override
  public sa.rule.types.TypeEnvironment setEnv(sa.rule.types.TypeEnvironment set_arg) {
    return make(_Name, _Type, set_arg);
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
      case 1: return _Type;
      case 2: return _Env;
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
      case 0: return make(getName(), _Type, _Env);
      case 1: return make(getName(), (sa.rule.types.GomType) v, _Env);
      case 2: return make(getName(), _Type, (sa.rule.types.TypeEnvironment) v);
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
    if (children.length == getChildCount()  && children[0] instanceof tom.library.sl.VisitableBuiltin && children[1] instanceof sa.rule.types.GomType && children[2] instanceof sa.rule.types.TypeEnvironment) {
      return make(((tom.library.sl.VisitableBuiltin<String>)children[0]).getBuiltin(), (sa.rule.types.GomType) children[1], (sa.rule.types.TypeEnvironment) children[2]);
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
    return new tom.library.sl.Visitable[] { new tom.library.sl.VisitableBuiltin<String>(_Name),  _Type,  _Env};
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
    b = (1278884122<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_Name, 2) << 16);
    a += (_Type.hashCode() << 8);
    a += (_Env.hashCode());

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
  public static tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>>>> funMake() {
    return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<String>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>>> apply(final tom.library.enumerator.Enumeration<String> t1) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.GomType>,tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>>>() {
          public tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>> apply(final tom.library.enumerator.Enumeration<sa.rule.types.GomType> t2) {
            return 
        new tom.library.enumerator.F<tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>,tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment>>() {
          public tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment> apply(final tom.library.enumerator.Enumeration<sa.rule.types.TypeEnvironment> t3) {
            return tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.apply(tom.library.enumerator.Enumeration.singleton((tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment>>>) 
        new tom.library.enumerator.F<String,tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment>>>() {
          public tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment>> apply(final String t1) {
            return 
        new tom.library.enumerator.F<sa.rule.types.GomType,tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment>>() {
          public tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment> apply(final sa.rule.types.GomType t2) {
            return 
        new tom.library.enumerator.F<sa.rule.types.TypeEnvironment,sa.rule.types.TypeEnvironment>() {
          public sa.rule.types.TypeEnvironment apply(final sa.rule.types.TypeEnvironment t3) {
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
