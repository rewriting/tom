
package firewall.ast.strategy.instruction;

public class Make_Ins implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _target;
  private tom.library.sl.Strategy _prot;
  private tom.library.sl.Strategy _opt;
  private tom.library.sl.Strategy _source;
  private tom.library.sl.Strategy _destination;
  private tom.library.sl.Strategy _options;


  public int getChildCount() {
    return 6;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _target;
      case 1: return _prot;
      case 2: return _opt;
      case 3: return _source;
      case 4: return _destination;
      case 5: return _options;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _target = (tom.library.sl.Strategy) child; return this;
      case 1: _prot = (tom.library.sl.Strategy) child; return this;
      case 2: _opt = (tom.library.sl.Strategy) child; return this;
      case 3: _source = (tom.library.sl.Strategy) child; return this;
      case 4: _destination = (tom.library.sl.Strategy) child; return this;
      case 5: _options = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_target, _prot, _opt, _source, _destination, _options};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._target = (tom.library.sl.Strategy)children[0];
    this._prot = (tom.library.sl.Strategy)children[1];
    this._opt = (tom.library.sl.Strategy)children[2];
    this._source = (tom.library.sl.Strategy)children[3];
    this._destination = (tom.library.sl.Strategy)children[4];
    this._options = (tom.library.sl.Strategy)children[5];

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

  public Make_Ins(tom.library.sl.Strategy _target, tom.library.sl.Strategy _prot, tom.library.sl.Strategy _opt, tom.library.sl.Strategy _source, tom.library.sl.Strategy _destination, tom.library.sl.Strategy _options) {
    this._target = _target;
    this._prot = _prot;
    this._opt = _opt;
    this._source = _source;
    this._destination = _destination;
    this._options = _options;

  }

  /**
    * Builds a new Ins
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public Object visitLight(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    firewall.ast.types.Target new_target = (firewall.ast.types.Target) _target.visit(any,i);

    firewall.ast.types.Protocol new_prot = (firewall.ast.types.Protocol) _prot.visit(any,i);

    firewall.ast.types.Opts new_opt = (firewall.ast.types.Opts) _opt.visit(any,i);

    firewall.ast.types.Communication new_source = (firewall.ast.types.Communication) _source.visit(any,i);

    firewall.ast.types.Communication new_destination = (firewall.ast.types.Communication) _destination.visit(any,i);

    firewall.ast.types.Options new_options = (firewall.ast.types.Options) _options.visit(any,i);

    return firewall.ast.types.instruction.Ins.make( new_target,  new_prot,  new_opt,  new_source,  new_destination,  new_options);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_target).visit(i);
    firewall.ast.types.Target new_target = (firewall.ast.types.Target) getEnvironment().getSubject();

    (_prot).visit(i);
    firewall.ast.types.Protocol new_prot = (firewall.ast.types.Protocol) getEnvironment().getSubject();

    (_opt).visit(i);
    firewall.ast.types.Opts new_opt = (firewall.ast.types.Opts) getEnvironment().getSubject();

    (_source).visit(i);
    firewall.ast.types.Communication new_source = (firewall.ast.types.Communication) getEnvironment().getSubject();

    (_destination).visit(i);
    firewall.ast.types.Communication new_destination = (firewall.ast.types.Communication) getEnvironment().getSubject();

    (_options).visit(i);
    firewall.ast.types.Options new_options = (firewall.ast.types.Options) getEnvironment().getSubject();

    getEnvironment().setSubject(firewall.ast.types.instruction.Ins.make( new_target,  new_prot,  new_opt,  new_source,  new_destination,  new_options));
    return tom.library.sl.Environment.SUCCESS;
  }
}
