package jtom.runtime.set.jgtreeset;

abstract public class JGTreeSet_BranchImpl
extends JGTreeSet
{
  static private aterm.ATerm pattern = null;

  protected aterm.ATerm getPattern() {
    return pattern;
  }
  private static int index_left = 0;
  private static int index_right = 1;
  public shared.SharedObject duplicate() {
    JGTreeSet_Branch clone = new JGTreeSet_Branch();
     clone.init(hashCode(), getAnnotations(), getAFun(), getArgumentArray());
    return clone;
  }

  protected aterm.ATermAppl make(aterm.AFun fun, aterm.ATerm[] i_args, aterm.ATermList annos) {
    return getSetFactory().makeJGTreeSet_Branch(fun, i_args, annos);
  }
  static public void initializePattern()
  {
    pattern = getStaticFactory().parse("branch(<term>,<term>)");
  }

  static public JGTreeSet fromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(pattern);

    if (children != null) {
      JGTreeSet tmp = getStaticSetFactory().makeJGTreeSet_Branch(JGTreeSet.fromTerm( (aterm.ATerm) children.get(0)), JGTreeSet.fromTerm( (aterm.ATerm) children.get(1)));
      tmp.setTerm(trm);
      return tmp;
    }
    else {
      return null;
    }
  }
  public boolean isBranch()
  {
    return true;
  }

  public boolean hasLeft()
  {
    return true;
  }

  public boolean hasRight()
  {
    return true;
  }

  public JGTreeSet getLeft()
  {
    return (JGTreeSet) this.getArgument(index_left) ;
  }

  public JGTreeSet setLeft(JGTreeSet _left)
  {
    return (JGTreeSet) super.setArgument(_left, index_left);
  }

  public JGTreeSet getRight()
  {
    return (JGTreeSet) this.getArgument(index_right) ;
  }

  public JGTreeSet setRight(JGTreeSet _right)
  {
    return (JGTreeSet) super.setArgument(_right, index_right);
  }

  public aterm.ATermAppl setArgument(aterm.ATerm arg, int i) {
    switch(i) {
      case 0:
        if (! (arg instanceof JGTreeSet)) { 
          throw new RuntimeException("Argument 0 of a JGTreeSet_Branch should have type JGTreeSet");
        }
        break;
      case 1:
        if (! (arg instanceof JGTreeSet)) { 
          throw new RuntimeException("Argument 1 of a JGTreeSet_Branch should have type JGTreeSet");
        }
        break;
      default: throw new RuntimeException("JGTreeSet_Branch does not have an argument at " + i );
    }
    return super.setArgument(arg, i);
  }
  protected int hashFunction() {
    int c = 0 + (getAnnotations().hashCode()<<8);
    int a = 0x9e3779b9;
    int b = (getAFun().hashCode()<<8);
    a += (getArgument(1).hashCode() << 8);
    a += (getArgument(0).hashCode() << 0);

    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);

    return c;
  }
}
