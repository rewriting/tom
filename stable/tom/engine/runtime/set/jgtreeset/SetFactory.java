package jtom.runtime.set.jgtreeset;

import aterm.pure.PureFactory;
public class SetFactory
{
  private PureFactory factory;
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
  public SetFactory(PureFactory factory)
  {
     this.factory = factory;
     initialize();
  }
  public PureFactory getPureFactory()
  {
    return factory;
  }
  private void initialize()
  {

    patternJGTreeSet_EmptySet = factory.parse("emptySet");
    funJGTreeSet_EmptySet = factory.makeAFun("_JGTreeSet_emptySet", 0, false);
    protoJGTreeSet_EmptySet = new JGTreeSet_EmptySet(this);

    patternJGTreeSet_Singleton = factory.parse("singleton(<term>)");
    funJGTreeSet_Singleton = factory.makeAFun("_JGTreeSet_singleton", 1, false);
    protoJGTreeSet_Singleton = new JGTreeSet_Singleton(this);

    patternJGTreeSet_Pair = factory.parse("pair(<term>,<int>)");
    funJGTreeSet_Pair = factory.makeAFun("_JGTreeSet_pair", 2, false);
    protoJGTreeSet_Pair = new JGTreeSet_Pair(this);

    patternJGTreeSet_Branch = factory.parse("branch(<term>,<term>)");
    funJGTreeSet_Branch = factory.makeAFun("_JGTreeSet_branch", 2, false);
    protoJGTreeSet_Branch = new JGTreeSet_Branch(this);

  }
  protected JGTreeSet_EmptySet makeJGTreeSet_EmptySet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_EmptySet) {
      protoJGTreeSet_EmptySet.initHashCode(annos,fun,args);
      return (JGTreeSet_EmptySet) factory.build(protoJGTreeSet_EmptySet);
    }
  }

  public JGTreeSet_EmptySet makeJGTreeSet_EmptySet() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeJGTreeSet_EmptySet(funJGTreeSet_EmptySet, args, factory.getEmpty());
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
    return factory.make(patternJGTreeSet_EmptySet, args);
  }

  protected JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Singleton) {
      protoJGTreeSet_Singleton.initHashCode(annos,fun,args);
      return (JGTreeSet_Singleton) factory.build(protoJGTreeSet_Singleton);
    }
  }

  public JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.ATerm _value) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value};
    return makeJGTreeSet_Singleton(funJGTreeSet_Singleton, args, factory.getEmpty());
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
    args.add((aterm.ATerm)arg.getValue());    return factory.make(patternJGTreeSet_Singleton, args);
  }

  protected JGTreeSet_Pair makeJGTreeSet_Pair(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Pair) {
      protoJGTreeSet_Pair.initHashCode(annos,fun,args);
      return (JGTreeSet_Pair) factory.build(protoJGTreeSet_Pair);
    }
  }

  public JGTreeSet_Pair makeJGTreeSet_Pair(aterm.ATerm _value, int _multiplicity) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value, factory.makeInt(_multiplicity)};
    return makeJGTreeSet_Pair(funJGTreeSet_Pair, args, factory.getEmpty());
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
    args.add((aterm.ATerm)arg.getValue());    args.add(new Integer(arg.getMultiplicity()));    return factory.make(patternJGTreeSet_Pair, args);
  }

  protected JGTreeSet_Branch makeJGTreeSet_Branch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Branch) {
      protoJGTreeSet_Branch.initHashCode(annos,fun,args);
      return (JGTreeSet_Branch) factory.build(protoJGTreeSet_Branch);
    }
  }

  public JGTreeSet_Branch makeJGTreeSet_Branch(JGTreeSet _left, JGTreeSet _right) {
    aterm.ATerm[] args = new aterm.ATerm[] {_left, _right};
    return makeJGTreeSet_Branch(funJGTreeSet_Branch, args, factory.getEmpty());
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
    args.add((arg.getLeft()).toTerm());    args.add((arg.getRight()).toTerm());    return factory.make(patternJGTreeSet_Branch, args);
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
    aterm.ATerm trm = factory.parse(str);
    return JGTreeSetFromTerm(trm);
  }
  public JGTreeSet JGTreeSetFromFile(java.io.InputStream stream) throws java.io.IOException {
    return JGTreeSetFromTerm(factory.readFromFile(stream));
  }
}
