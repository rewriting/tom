package jtom.runtime.set.jgtreeset;

import aterm.*;
import aterm.pure.PureFactory;
public class SetFactory extends PureFactory
{
  private aterm.AFun funJGTreeSet_EmptySet;
  private JGTreeSet protoJGTreeSet_EmptySet;
  private aterm.ATerm patternJGTreeSet_EmptySet;
  private aterm.AFun funJGTreeSet_Singleton;
  private JGTreeSet protoJGTreeSet_Singleton;
  private aterm.ATerm patternJGTreeSet_Singleton;
  private aterm.AFun funJGTreeSet_Pair;
  private JGTreeSet protoJGTreeSet_Pair;
  private aterm.ATerm patternJGTreeSet_Pair;
  private aterm.AFun funJGTreeSet_Branch;
  private JGTreeSet protoJGTreeSet_Branch;
  private aterm.ATerm patternJGTreeSet_Branch;
  public SetFactory()
  {
     super();
     initialize();
  }
  public SetFactory(int logSize)
  {
     super(logSize);
     initialize();
  }
  private void initialize()
  {

    patternJGTreeSet_EmptySet = parse("emptySet");
    funJGTreeSet_EmptySet = makeAFun("_JGTreeSet_emptySet", 0, false);
    protoJGTreeSet_EmptySet = new JGTreeSet_EmptySet(this);

    patternJGTreeSet_Singleton = parse("singleton(<term>)");
    funJGTreeSet_Singleton = makeAFun("_JGTreeSet_singleton", 1, false);
    protoJGTreeSet_Singleton = new JGTreeSet_Singleton(this);

    patternJGTreeSet_Pair = parse("pair(<term>,<int>)");
    funJGTreeSet_Pair = makeAFun("_JGTreeSet_pair", 2, false);
    protoJGTreeSet_Pair = new JGTreeSet_Pair(this);

    patternJGTreeSet_Branch = parse("branch(<term>,<term>)");
    funJGTreeSet_Branch = makeAFun("_JGTreeSet_branch", 2, false);
    protoJGTreeSet_Branch = new JGTreeSet_Branch(this);

  }
  protected JGTreeSet_EmptySet makeJGTreeSet_EmptySet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_EmptySet) {
      protoJGTreeSet_EmptySet.initHashCode(annos,fun,args);
      return (JGTreeSet_EmptySet) build(protoJGTreeSet_EmptySet);
    }
  }

  public JGTreeSet_EmptySet makeJGTreeSet_EmptySet() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeJGTreeSet_EmptySet( funJGTreeSet_EmptySet, args, empty);
  }

  public JGTreeSet JGTreeSet_EmptySetFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternJGTreeSet_EmptySet);

    if (children != null) {
      JGTreeSet tmp = makeJGTreeSet_EmptySet();
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(JGTreeSet_EmptySetImpl arg) {
    java.util.List args = new java.util.LinkedList();
    return make(patternJGTreeSet_EmptySet, args);
  }

  protected JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Singleton) {
      protoJGTreeSet_Singleton.initHashCode(annos,fun,args);
      return (JGTreeSet_Singleton) build(protoJGTreeSet_Singleton);
    }
  }

  public JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.ATerm _value) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value};
    return makeJGTreeSet_Singleton( funJGTreeSet_Singleton, args, empty);
  }

  public JGTreeSet JGTreeSet_SingletonFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternJGTreeSet_Singleton);

    if (children != null) {
      JGTreeSet tmp = makeJGTreeSet_Singleton((aterm.ATerm) children.get(0));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(JGTreeSet_SingletonImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add((aterm.ATerm)arg.getArgument(0));
    return make(patternJGTreeSet_Singleton, args);
  }

  protected JGTreeSet_Pair makeJGTreeSet_Pair(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Pair) {
      protoJGTreeSet_Pair.initHashCode(annos,fun,args);
      return (JGTreeSet_Pair) build(protoJGTreeSet_Pair);
    }
  }

  public JGTreeSet_Pair makeJGTreeSet_Pair(aterm.ATerm _value, int _multiplicity) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value, makeInt(_multiplicity)};
    return makeJGTreeSet_Pair( funJGTreeSet_Pair, args, empty);
  }

  public JGTreeSet JGTreeSet_PairFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternJGTreeSet_Pair);

    if (children != null) {
      JGTreeSet tmp = makeJGTreeSet_Pair((aterm.ATerm) children.get(0), ((Integer) children.get(1)).intValue());
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(JGTreeSet_PairImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add((aterm.ATerm)arg.getArgument(0));
    args.add(new Integer(((aterm.ATermInt)arg.getArgument(1)).getInt()));
    return make(patternJGTreeSet_Pair, args);
  }

  protected JGTreeSet_Branch makeJGTreeSet_Branch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Branch) {
      protoJGTreeSet_Branch.initHashCode(annos,fun,args);
      return (JGTreeSet_Branch) build(protoJGTreeSet_Branch);
    }
  }

  public JGTreeSet_Branch makeJGTreeSet_Branch(JGTreeSet _left, JGTreeSet _right) {
    aterm.ATerm[] args = new aterm.ATerm[] {_left, _right};
    return makeJGTreeSet_Branch( funJGTreeSet_Branch, args, empty);
  }

  public JGTreeSet JGTreeSet_BranchFromTerm(aterm.ATerm trm)
  {
    java.util.List children = trm.match(patternJGTreeSet_Branch);

    if (children != null) {
      JGTreeSet tmp = makeJGTreeSet_Branch(JGTreeSetFromTerm( (aterm.ATerm) children.get(0)), JGTreeSetFromTerm( (aterm.ATerm) children.get(1)));
      return tmp;
    }
    else {
      return null;
    }
  }
  protected aterm.ATerm toTerm(JGTreeSet_BranchImpl arg) {
    java.util.List args = new java.util.LinkedList();
    args.add(((JGTreeSet)arg.getArgument(0)).toTerm());
    args.add(((JGTreeSet)arg.getArgument(1)).toTerm());
    return make(patternJGTreeSet_Branch, args);
  }

  public JGTreeSet JGTreeSetFromTerm(aterm.ATerm trm)
  {
    JGTreeSet tmp;
    tmp = JGTreeSet_EmptySetFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = JGTreeSet_SingletonFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = JGTreeSet_PairFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = JGTreeSet_BranchFromTerm(trm);
    if (tmp != null) {
      return tmp;
    }


    throw new RuntimeException("This is not a JGTreeSet: " + trm);
  }
  public JGTreeSet JGTreeSetFromString(String str)
  {
    aterm.ATerm trm = parse(str);
    return JGTreeSetFromTerm(trm);
  }
  public JGTreeSet JGTreeSetFromFile(java.io.InputStream stream) throws java.io.IOException {
    return JGTreeSetFromTerm(readFromFile(stream));
  }
}
