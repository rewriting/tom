package tom.library.sl;

public interface GraphStrategy extends tom.library.strategy.mutraveler.MuStrategy {


  public void setEnvironment(Environment p);

  public Environment getEnvironment();

  public jjtraveler.Visitable gapply(jjtraveler.Visitable any);

  public jjtraveler.Visitable getRoot();

}

