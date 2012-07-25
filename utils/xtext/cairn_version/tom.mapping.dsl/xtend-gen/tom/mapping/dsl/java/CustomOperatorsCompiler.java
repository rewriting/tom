package tom.mapping.dsl.java;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.ParametersCompiler;
import tom.mapping.model.Accessor;
import tom.mapping.model.Mapping;
import tom.mapping.model.Operator;
import tom.mapping.model.Parameter;
import tom.mapping.model.Terminal;
import tom.mapping.model.UserOperator;

@SuppressWarnings("all")
public class CustomOperatorsCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  @Inject
  private ParametersCompiler injpa;
  
  private String prefix = "tom";
  
  public void compile(final Mapping m, final IFileSystemAccess fsa) {
    String _operator_plus = StringExtensions.operator_plus(this.prefix, "/");
    String _customOperatorsClass = this._tomMappingExtensions.getCustomOperatorsClass(m);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _customOperatorsClass);
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, ".java");
    CharSequence _main = this.main(m);
    fsa.generateFile(_operator_plus_2, _main);
  }
  
  public CharSequence main(final Mapping map) {
    CharSequence _xifexpression = null;
    EList<Operator> _operators = map.getOperators();
    final Function1<Operator,Boolean> _function = new Function1<Operator,Boolean>() {
        public Boolean apply(final Operator e) {
          return Boolean.valueOf((e instanceof UserOperator));
        }
      };
    Iterable<Operator> _filter = IterableExtensions.<Operator>filter(_operators, _function);
    int _size = IterableExtensions.size(_filter);
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    if (_operator_greaterThan) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public class ");
      String _customOperatorsClass = this._tomMappingExtensions.getCustomOperatorsClass(map);
      _builder.append(_customOperatorsClass, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      {
        EList<Operator> _operators_1 = map.getOperators();
        for(final Operator op : _operators_1) {
          _builder.append("\t");
          Object _operator = this.operator(map, op);
          _builder.append(_operator, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  public Object operator(final Mapping map, final Operator op) {
    return null;
  }
  
  public CharSequence operator(final Mapping map, final UserOperator usop) {
    CharSequence _xblockexpression = null;
    {
      EList<Accessor> _accessors = usop.getAccessors();
      for (final Accessor a : _accessors) {
        this.accessor(usop, a);
      }
      this.test(usop);
      CharSequence _make = this.make(usop);
      _xblockexpression = (_make);
    }
    return _xblockexpression;
  }
  
  public CharSequence accessor(final UserOperator op, final Accessor acc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static ");
    Parameter _slot = acc.getSlot();
    Terminal _type = _slot.getType();
    CharSequence _javaTerminalType = this.injpa.javaTerminalType(_type);
    _builder.append(_javaTerminalType, "");
    _builder.newLineIfNotEmpty();
    String _customOperatorSlotAccessorName = this._tomMappingExtensions.getCustomOperatorSlotAccessorName(acc);
    _builder.append(_customOperatorSlotAccessorName, "");
    _builder.newLineIfNotEmpty();
    _builder.append("(");
    Terminal _type_1 = op.getType();
    CharSequence _javaTerminalType_1 = this.injpa.javaTerminalType(_type_1);
    _builder.append(_javaTerminalType_1, "");
    _builder.append(" t) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return ");
    String _java = acc.getJava();
    _builder.append(_java, "	");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence test(final UserOperator usop) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static boolean is");
    String _name = usop.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    _builder.append(_firstUpper, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("(");
    Terminal _type = usop.getType();
    CharSequence _javaTerminalType = this.injpa.javaTerminalType(_type);
    _builder.append(_javaTerminalType, "	");
    _builder.append(" t) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return ");
    CharSequence _test = this.test(usop);
    _builder.append(_test, "	");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence make(final UserOperator usop) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static ");
    Terminal _type = usop.getType();
    CharSequence _javaTerminalType = this.injpa.javaTerminalType(_type);
    _builder.append(_javaTerminalType, "");
    _builder.append(" make ");
    String _name = usop.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    _builder.append(_firstUpper, "");
    _builder.newLineIfNotEmpty();
    _builder.append("(");
    {
      EList<Accessor> _accessors = usop.getAccessors();
      boolean _hasElements = false;
      for(final Accessor acc : _accessors) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.newLineIfNotEmpty();
        Parameter _slot = acc.getSlot();
        CharSequence _javaParameter = this.injpa.javaParameter(_slot);
        _builder.append(_javaParameter, "");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
      }
    }
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return ");
    CharSequence _make = this.make(usop);
    _builder.append(_make, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
