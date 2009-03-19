
package firewall.ast.strategy.instructionlist;

public class Make_ConsInstructionList implements tom.library.sl.Strategy {

  protected tom.library.sl.Environment environment;
  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  private tom.library.sl.Strategy _HeadInstructionList;
  private tom.library.sl.Strategy _TailInstructionList;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadInstructionList;
      case 1: return _TailInstructionList;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadInstructionList = (tom.library.sl.Strategy) child; return this;
      case 1: _TailInstructionList = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadInstructionList, _TailInstructionList};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadInstructionList = (tom.library.sl.Strategy)children[0];
    this._TailInstructionList = (tom.library.sl.Strategy)children[1];

    return this;
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure{
    return (tom.library.sl.Visitable) visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,envt);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Object visit(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Make_ConsInstructionList(tom.library.sl.Strategy _HeadInstructionList, tom.library.sl.Strategy _TailInstructionList) {
    this._HeadInstructionList = _HeadInstructionList;
    this._TailInstructionList = _TailInstructionList;

  }

  /**
    * Builds a new ConsInstructionList
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public Object visitLight(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    firewall.ast.types.Instruction new_HeadInstructionList = (firewall.ast.types.Instruction) _HeadInstructionList.visit(any,i);

    firewall.ast.types.InstructionList new_TailInstructionList = (firewall.ast.types.InstructionList) _TailInstructionList.visit(any,i);

    return firewall.ast.types.instructionlist.ConsInstructionList.make( new_HeadInstructionList,  new_TailInstructionList);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_HeadInstructionList).visit(i);
    firewall.ast.types.Instruction new_HeadInstructionList = (firewall.ast.types.Instruction) getEnvironment().getSubject();

    (_TailInstructionList).visit(i);
    firewall.ast.types.InstructionList new_TailInstructionList = (firewall.ast.types.InstructionList) getEnvironment().getSubject();

    getEnvironment().setSubject(firewall.ast.types.instructionlist.ConsInstructionList.make( new_HeadInstructionList,  new_TailInstructionList));
    return tom.library.sl.Environment.SUCCESS;
  }
}
