package tom.mapping.dsl.generator.tom;

import com.google.inject.Inject;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.OperatorsCompiler;
import tom.mapping.dsl.generator.tom.TerminalsCompiler;
import tom.mapping.model.Mapping;
import tom.mapping.model.Module;
import tom.mapping.model.Operator;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class TomTemplateCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  @Inject
  private TerminalsCompiler terminals;
  
  @Inject
  private OperatorsCompiler injop;
  
  private String prefix = "tom/";
  
  public void compile(final Mapping m, final IFileSystemAccess fsa) {
      String _operator_plus = StringExtensions.operator_plus(this.prefix, "common.tom");
      CharSequence _main = this.main(m);
      fsa.generateFile(_operator_plus, _main);
      String _name = m.getName();
      String _operator_plus_1 = StringExtensions.operator_plus(this.prefix, _name);
      String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, "_terminals.tom");
      CharSequence _terminals = this.terminals(m);
      fsa.generateFile(_operator_plus_2, _terminals);
      String _name_1 = m.getName();
      String _operator_plus_3 = StringExtensions.operator_plus(this.prefix, _name_1);
      String _operator_plus_4 = StringExtensions.operator_plus(_operator_plus_3, "_operators.tom");
      CharSequence _operators = this.operators(m);
      fsa.generateFile(_operator_plus_4, _operators);
      String _name_2 = m.getName();
      String _operator_plus_5 = StringExtensions.operator_plus(this.prefix, _name_2);
      String _operator_plus_6 = StringExtensions.operator_plus(_operator_plus_5, "_defaultOperators.tom");
      CharSequence _defaultOperators = this.defaultOperators(m);
      fsa.generateFile(_operator_plus_6, _defaultOperators);
      EList<Module> _modules = m.getModules();
      for (final Module module : _modules) {
        String _name_3 = m.getName();
        String _operator_plus_7 = StringExtensions.operator_plus(this.prefix, _name_3);
        String _operator_plus_8 = StringExtensions.operator_plus(_operator_plus_7, "_");
        String _name_4 = module.getName();
        String _operator_plus_9 = StringExtensions.operator_plus(_operator_plus_8, _name_4);
        String _operator_plus_10 = StringExtensions.operator_plus(_operator_plus_9, ".tom");
        CharSequence _module = this.module(m, module);
        fsa.generateFile(_operator_plus_10, _module);
      }
  }
  
  public CharSequence main(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%include { string.tom }");
    _builder.newLine();
    _builder.append("%include { boolean.tom }");
    _builder.newLine();
    _builder.append("%include { int.tom }");
    _builder.newLine();
    _builder.append("%include { long.tom }");
    _builder.newLine();
    _builder.append("%include { float.tom }");
    _builder.newLine();
    _builder.append("%include { double.tom }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private static <O> EList<O> enforce(EList l) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return l;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private static <O> EList<O> append(O e,EList<O> l) {");
    _builder.newLine();
    _builder.append("       ");
    _builder.append("l.add(e);");
    _builder.newLine();
    _builder.append("       ");
    _builder.append("return l;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence terminals(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Primitive terminals (enu and data types)");
    _builder.newLine();
    {
      EList<EPackage> _allRootPackages = this._tomMappingExtensions.getAllRootPackages(m);
      for(final EPackage p : _allRootPackages) {
        CharSequence _primitiveTerminal = this.terminals.primitiveTerminal(p);
        _builder.append(_primitiveTerminal, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("// Terminals");
    _builder.newLine();
    {
      EList<Terminal> _terminals = m.getTerminals();
      for(final Terminal t : _terminals) {
        CharSequence _terminal = this.terminals.terminal(m, t);
        _builder.append(_terminal, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("// List Terminals");
    _builder.newLine();
    {
      EList<Terminal> _allListTerminals = m.getAllListTerminals();
      for(final Terminal lt : _allListTerminals) {
        CharSequence _listTerminal = this.terminals.listTerminal(m, lt);
        _builder.append(_listTerminal, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence operators(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// User operators");
    _builder.newLine();
    {
      EList<Operator> _operators = m.getOperators();
      for(final Operator op : _operators) {
        CharSequence _operator = this.injop.operator(m, op);
        _builder.append(_operator, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence defaultOperators(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Default operators");
    _builder.newLine();
    {
      EList<EClass> _allDefaultOperators = m.getAllDefaultOperators();
      for(final EClass op : _allDefaultOperators) {
        String _name = op.getName();
        CharSequence _classOperator = this.injop.classOperator(m, _name, op);
        _builder.append(_classOperator, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("/* PROTECTED REGION ID(op.name+\"_mapping_user\") ENABLED START */");
    _builder.newLine();
    _builder.append("// Protected user region");
    _builder.newLine();
    _builder.append("/* PROTECTED REGION END */");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence module(final Mapping m, final Module mod) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/* PROTECTED REGION ID(module.name+\"_mapping_user\") ENABLED START */");
    _builder.newLine();
    _builder.append("// Protected user region");
    _builder.newLine();
    _builder.append("/* PROTECTED REGION END */");
    _builder.newLine();
    _builder.newLine();
    {
      EList<Operator> _operators = mod.getOperators();
      for(final Operator op : _operators) {
        CharSequence _operator = this.injop.operator(m, op);
        _builder.append(_operator, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public void primitiveTerminals(final EPackage epa) {
      EList<EClassifier> _eClassifiers = epa.getEClassifiers();
      for (final EClassifier c : _eClassifiers) {
        this.primitiveTerminal(c);
      }
      EList<EPackage> _eSubpackages = epa.getESubpackages();
      for (final EPackage subp : _eSubpackages) {
        this.primitiveTerminals(subp);
      }
  }
  
  protected CharSequence _primitiveTerminal(final EClassifier ecl) {
    return null;
  }
  
  protected CharSequence _primitiveTerminal(final EEnum enu) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%typeterm ");
    String _name = enu.getName();
    _builder.append(_name, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("implement {");
    String _name_1 = enu.getName();
    _builder.append(_name_1, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("is_sort(t) {$t instanceof ");
    String _name_2 = enu.getName();
    _builder.append(_name_2, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("equals(l1,l2) {$l1==$l2}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EDataType edaty) {
    CharSequence _xblockexpression = null;
    {
      String _instanceTypeName = edaty.getInstanceTypeName();
      boolean _isPrimitive = this._tomMappingExtensions.isPrimitive(_instanceTypeName);
      final boolean primitive = _isPrimitive;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("%typeterm ");
      String _name = edaty.getName();
      _builder.append(_name, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("implement {");
      String _instanceTypeName_1 = edaty.getInstanceTypeName();
      _builder.append(_instanceTypeName_1, "	");
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("is_sort(t) {");
      {
        if (primitive) {
          _builder.append("true");
        } else {
          _builder.append("{$t instanceof ");
          String _instanceTypeName_2 = edaty.getInstanceTypeName();
          _builder.append(_instanceTypeName_2, "	");
        }
      }
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("equals(l1,l2) {");
      {
        if (primitive) {
          _builder.append("{$l1=$l2}");
        } else {
          _builder.append("{l1.equals($l2)");
        }
      }
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence primitiveTerminal(final EClassifier enu) {
    if (enu instanceof EEnum) {
      return _primitiveTerminal((EEnum)enu);
    } else if (enu instanceof EDataType) {
      return _primitiveTerminal((EDataType)enu);
    } else if (enu != null) {
      return _primitiveTerminal(enu);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(enu).toString());
    }
  }
}
