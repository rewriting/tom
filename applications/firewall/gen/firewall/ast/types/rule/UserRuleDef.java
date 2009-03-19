
package firewall.ast.types.rule;

import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;

public final class UserRuleDef extends firewall.ast.types.Rule implements tom.library.sl.Visitable  {
  
  private static String symbolName = "UserRuleDef";


  private UserRuleDef() {}
  private int hashCode;
  private static UserRuleDef proto = new UserRuleDef();
    private String _user_rule_name;

    /* static constructor */

  public static UserRuleDef make(String _user_rule_name) {

    // use the proto as a model
    proto.initHashCode( _user_rule_name);
    return (UserRuleDef) factory.build(proto);

  }

  private void init(String _user_rule_name, int hashCode) {
    this._user_rule_name = _user_rule_name.intern();

    this.hashCode = hashCode;
  }

  private void initHashCode(String _user_rule_name) {
    this._user_rule_name = _user_rule_name.intern();

  this.hashCode = hashFunction();
  }

  /* name and arity */
  @Override
  public String symbolName() {
    return "UserRuleDef";
  }

  private int getArity() {
    return 1;
  }

  public shared.SharedObject duplicate() {
    UserRuleDef clone = new UserRuleDef();
    clone.init( _user_rule_name, hashCode);
    return clone;
  }
  
  @Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("UserRuleDef(");
    buffer.append('"');
            for (int i = 0; i < _user_rule_name.length(); i++) {
              char c = _user_rule_name.charAt(i);
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
    firewall.ast.AstAbstractType ao = (firewall.ast.AstAbstractType) o;
    /* return 0 for equality */
    if (ao == this)
      return 0;
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0)
      return symbCmp;
    /* compare the childs */
    UserRuleDef tco = (UserRuleDef) ao;
    int _user_rule_nameCmp = (this._user_rule_name).compareTo(tco._user_rule_name);
    if(_user_rule_nameCmp != 0)
      return _user_rule_nameCmp;
             

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
    UserRuleDef tco = (UserRuleDef) ao;
    int _user_rule_nameCmp = (this._user_rule_name).compareTo(tco._user_rule_name);
    if(_user_rule_nameCmp != 0)
      return _user_rule_nameCmp;
             

    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  @Override
  public final int hashCode() {
    return hashCode;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof UserRuleDef) {

      UserRuleDef peer = (UserRuleDef) obj;
      return _user_rule_name==peer._user_rule_name && true;
    }
    return false;
  }


   //Rule interface
  @Override
  public boolean isUserRuleDef() {
    return true;
  }
  
  @Override
  public String getuser_rule_name() {
    return _user_rule_name;
  }
  
  @Override
  public firewall.ast.types.Rule setuser_rule_name(String set_arg) {
    return make(set_arg);
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
      new aterm.ATerm[] {(aterm.ATerm) atermFactory.makeAppl(atermFactory.makeAFun(getuser_rule_name() ,0 , true))});
  }

  public static firewall.ast.types.Rule fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName())) {
        return make(
(String) ((aterm.ATermAppl)appl.getArgument(0)).getAFun().getName()
        );
      }
    }
    return null;
  }


  /* Visitable */
  public int getChildCount() {
    return 1;
  }

  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return new tom.library.sl.VisitableBuiltin<String>(_user_rule_name);

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    switch(index) {
      case 0: return make(getuser_rule_name());

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == 1) {
      return make(((tom.library.sl.VisitableBuiltin<String>)childs[0]).getBuiltin());
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[] {  new tom.library.sl.VisitableBuiltin<String>(_user_rule_name) };
  }

    /* internal use */
  protected int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (-1143014832<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
    a += (shared.HashFunctions.stringHashFunction(_user_rule_name, 0));

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
