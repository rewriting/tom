package jtom.runtime.set.jgtreeset;

import aterm.pure.PureFactory;
public class SetFactory// extends PureFactory
{
  private aterm.AFun funJGTreeSet_EmptySet;
  private JGTreeSet protoJGTreeSet_EmptySet;
  private aterm.AFun funJGTreeSet_Singleton;
  private JGTreeSet protoJGTreeSet_Singleton;
  private aterm.AFun funJGTreeSet_Pair;
  private JGTreeSet protoJGTreeSet_Pair;
  private aterm.AFun funJGTreeSet_Branch;
  private JGTreeSet protoJGTreeSet_Branch;

  private PureFactory factory;

  public SetFactory(PureFactory factory)
  {
    this.factory = factory;
    initialize();
  }

  private void initialize()
  {
    JGTreeSet.initialize(this.factory);
    SetConstructor.initialize(this);

    JGTreeSet_EmptySet.initializePattern();
    funJGTreeSet_EmptySet = factory.makeAFun("_JGTreeSet_emptySet", 0, false);
    protoJGTreeSet_EmptySet = new JGTreeSet_EmptySet();

    JGTreeSet_Singleton.initializePattern();
    funJGTreeSet_Singleton = factory.makeAFun("_JGTreeSet_singleton", 1, false);
    protoJGTreeSet_Singleton = new JGTreeSet_Singleton();

    JGTreeSet_Pair.initializePattern();
    funJGTreeSet_Pair = factory.makeAFun("_JGTreeSet_pair", 2, false);
    protoJGTreeSet_Pair = new JGTreeSet_Pair();

    JGTreeSet_Branch.initializePattern();
    funJGTreeSet_Branch = factory.makeAFun("_JGTreeSet_branch", 2, false);
    protoJGTreeSet_Branch = new JGTreeSet_Branch();

  }

  protected JGTreeSet_EmptySet makeJGTreeSet_EmptySet(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_EmptySet) {
      protoJGTreeSet_EmptySet.initHashCode(annos,fun,args);
      return (JGTreeSet_EmptySet) factory.build(protoJGTreeSet_EmptySet);
    }
  }

  public JGTreeSet_EmptySet makeJGTreeSet_EmptySet() {
    aterm.ATerm[] args = new aterm.ATerm[] {};
    return makeJGTreeSet_EmptySet( funJGTreeSet_EmptySet, args, factory.makeList());
  }

  protected JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Singleton) {
      protoJGTreeSet_Singleton.initHashCode(annos,fun,args);
      return (JGTreeSet_Singleton) factory.build(protoJGTreeSet_Singleton);
    }
  }

  public JGTreeSet_Singleton makeJGTreeSet_Singleton(aterm.ATerm _value) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value};
    return makeJGTreeSet_Singleton( funJGTreeSet_Singleton, args, factory.makeList());
  }

  protected JGTreeSet_Pair makeJGTreeSet_Pair(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Pair) {
      protoJGTreeSet_Pair.initHashCode(annos,fun,args);
      return (JGTreeSet_Pair) factory.build(protoJGTreeSet_Pair);
    }
  }

  public JGTreeSet_Pair makeJGTreeSet_Pair(aterm.ATerm _value, Integer _multiplicity) {
    aterm.ATerm[] args = new aterm.ATerm[] {_value, factory.makeInt(_multiplicity.intValue())};
    return makeJGTreeSet_Pair( funJGTreeSet_Pair, args, factory.makeList());
  }

  protected JGTreeSet_Branch makeJGTreeSet_Branch(aterm.AFun fun, aterm.ATerm[] args, aterm.ATermList annos) {
    synchronized (protoJGTreeSet_Branch) {
      protoJGTreeSet_Branch.initHashCode(annos,fun,args);
      return (JGTreeSet_Branch) factory.build(protoJGTreeSet_Branch);
    }
  }

  public JGTreeSet_Branch makeJGTreeSet_Branch(JGTreeSet _left, JGTreeSet _right) {
    aterm.ATerm[] args = new aterm.ATerm[] {_left, _right};
    return makeJGTreeSet_Branch( funJGTreeSet_Branch, args, factory.makeList());
  }

}
