package tom.mapping.dsl.generator;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.ImportsCompiler;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.OperatorsCompiler;
import tom.mapping.dsl.generator.tom.ParametersCompiler;
import tom.mapping.dsl.generator.tom.TomFactoryCompiler;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.Module;
import tom.mapping.model.Operator;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.SettedValue;

@SuppressWarnings("all")
public class FactoryCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  @Inject
  private TomFactoryCompiler tfc;
  
  @Inject
  private ImportsCompiler imc;
  
  @Inject
  private ParametersCompiler injpa;
  
  @Inject
  private OperatorsCompiler injop;
  
  private String prefix = "";
  
  public void compile(final Mapping m, final IFileSystemAccess fsa) {
    String _packageToPath = this._tomMappingExtensions.packageToPath(this.prefix);
    String _operator_plus = StringExtensions.operator_plus(_packageToPath, "/");
    String _name = m.getName();
    String _firstLower = StringExtensions.toFirstLower(_name);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _firstLower);
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, "/");
    String _factoryName = this._tomMappingExtensions.factoryName(m);
    String _operator_plus_3 = StringExtensions.operator_plus(_operator_plus_2, _factoryName);
    String _operator_plus_4 = StringExtensions.operator_plus(_operator_plus_3, ".java");
    CharSequence _main = this.main(m);
    fsa.generateFile(_operator_plus_4, _main);
  }
  
  public ArrayList<EPackage> intersectName(final ArrayList<EPackage> listBase) {
      ArrayList<EPackage> _arrayList = new ArrayList<EPackage>();
      ArrayList<EPackage> listDestination = _arrayList;
      for (final EPackage eltB : listBase) {
        {
          boolean sameName = false;
          for (final EPackage eltD : listDestination) {
            boolean _operator_or = false;
            if (sameName) {
              _operator_or = true;
            } else {
              String _name = eltB.getName();
              String _name_1 = eltD.getName();
              boolean _operator_equals = ObjectExtensions.operator_equals(_name, _name_1);
              _operator_or = BooleanExtensions.operator_or(sameName, _operator_equals);
            }
            sameName = _operator_or;
          }
          boolean _operator_not = BooleanExtensions.operator_not(sameName);
          if (_operator_not) {
            listDestination.add(eltB);
          }
        }
      }
      return listDestination;
  }
  
  public CharSequence main(final Mapping map) {
    CharSequence _xblockexpression = null;
    {
      this.tfc.main(map);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("package ");
      String _packagePrefix = this._tomMappingExtensions.getPackagePrefix(this.prefix);
      _builder.append(_packagePrefix, "");
      String _name = map.getName();
      String _firstLower = StringExtensions.toFirstLower(_name);
      _builder.append(_firstLower, "");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("import java.util.List;");
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.append("// protected imports, you should add here required imports");
      _builder.newLine();
      _builder.append("// that won\'t be removed after regeneration of the mapping code");
      _builder.newLine();
      _builder.newLine();
      this.imc.imports(map);
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.append("/**");
      _builder.newLine();
      _builder.append("* User factory for \"name\"");
      _builder.newLine();
      _builder.append("* -- Generated by TOM mapping EMF generator --");
      _builder.newLine();
      _builder.append("*/");
      _builder.newLine();
      _builder.newLine();
      _builder.append("public class ");
      String _factoryName = this._tomMappingExtensions.factoryName(map);
      _builder.append(_factoryName, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      ArrayList<EPackage> _arrayList = new ArrayList<EPackage>();
      ArrayList<EPackage> packageList = _arrayList;
      _builder.newLineIfNotEmpty();
      {
        EList<Operator> _operators = map.getOperators();
        Iterable<ClassOperator> _filter = IterableExtensions.<ClassOperator>filter(_operators, tom.mapping.model.ClassOperator.class);
        for(final ClassOperator elt : _filter) {
          _builder.append("\t");
          EClass _class_ = elt.getClass_();
          EPackage _ePackage = _class_.getEPackage();
          boolean _add = packageList.add(_ePackage);
          _builder.append(_add, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        ArrayList<EPackage> _intersectName = this.intersectName(packageList);
        for(final EPackage pack : _intersectName) {
          _builder.append("\t");
          _builder.append("public static ");
          String _name_1 = pack.getName();
          String _firstUpper = StringExtensions.toFirstUpper(_name_1);
          _builder.append(_firstUpper, "	");
          _builder.append("Factory ");
          String _name_2 = pack.getName();
          _builder.append(_name_2, "	");
          _builder.append("Factory = ");
          String _name_3 = pack.getName();
          String _firstUpper_1 = StringExtensions.toFirstUpper(_name_3);
          _builder.append(_firstUpper_1, "	");
          _builder.append("Factory.eINSTANCE");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      ArrayList<EPackage> _arrayList_1 = new ArrayList<EPackage>();
      ArrayList<EPackage> packageList2 = _arrayList_1;
      _builder.newLineIfNotEmpty();
      {
        EList<EClass> _allDefaultOperators = map.getAllDefaultOperators();
        for(final EClass elt_1 : _allDefaultOperators) {
          _builder.append("\t");
          EPackage _ePackage_1 = elt_1.getEPackage();
          boolean _add_1 = packageList2.add(_ePackage_1);
          _builder.append(_add_1, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      {
        ArrayList<EPackage> _intersectName_1 = this.intersectName(packageList2);
        for(final EPackage pack_1 : _intersectName_1) {
          _builder.append("\t");
          _builder.append("public static ");
          String _name_4 = pack_1.getName();
          String _firstUpper_2 = StringExtensions.toFirstUpper(_name_4);
          _builder.append(_firstUpper_2, "	");
          _builder.append("Factory ");
          String _name_5 = pack_1.getName();
          _builder.append(_name_5, "	");
          _builder.append("Factory = ");
          String _name_6 = pack_1.getName();
          String _firstUpper_3 = StringExtensions.toFirstUpper(_name_6);
          _builder.append(_firstUpper_3, "	");
          _builder.append("Factory.eINSTANCE");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.append("// User operators ");
      EList<Operator> _operators_1 = map.getOperators();
      _builder.append(_operators_1, "");
      _builder.newLineIfNotEmpty();
      {
        EList<Module> _modules = map.getModules();
        for(final Module module : _modules) {
          _builder.append("{");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("/** Module ");
          String _name_7 = module.getName();
          _builder.append(_name_7, "	");
          _builder.append(" **/");
          _builder.newLineIfNotEmpty();
          {
            EList<Operator> _operators_2 = module.getOperators();
            for(final Operator op : _operators_2) {
              _builder.append("// Operator ");
              String _name_8 = op.getName();
              _builder.append(_name_8, "");
              _builder.newLineIfNotEmpty();
              CharSequence _operator = this.operator(map, op);
              _builder.append(_operator, "");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      _builder.newLine();
      _builder.append("/*");
      _builder.newLine();
      _builder.append("* Default TOM Operators for ");
      String _name_9 = map.getName();
      _builder.append(_name_9, "");
      _builder.append(" mapping. Each class that has a terminal type has also a default factory function.");
      _builder.newLineIfNotEmpty();
      _builder.append("*/");
      _builder.newLine();
      _builder.newLine();
      {
        EList<EClass> _allDefaultOperators_1 = map.getAllDefaultOperators();
        for(final EClass op_1 : _allDefaultOperators_1) {
          String _name_10 = op_1.getName();
          CharSequence _javaFactoryCreateDefaultOperator = this.javaFactoryCreateDefaultOperator(map, _name_10, op_1);
          _builder.append(_javaFactoryCreateDefaultOperator, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      _builder.append("/* PROTECTED REGION ID(map.name+\"_mapping_user_custom_methods\"+map.name) ENABLED START */");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/*");
      _builder.newLine();
      _builder.append("* Custom factory functions that won\'t be removed at regeneration of mapping code");
      _builder.newLine();
      _builder.append("*/");
      _builder.newLine();
      _builder.newLine();
      _builder.append("/* PROTECTED REGION END */");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _operator(final Mapping map, final Operator op) {
    return null;
  }
  
  protected CharSequence _operator(final Mapping map, final ClassOperator clop) {
    CharSequence _xifexpression = null;
    EList<FeatureParameter> _parameters = clop.getParameters();
    int _size = _parameters.size();
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    if (_operator_greaterThan) {
      CharSequence _xblockexpression = null;
      {
        Iterable<FeatureParameter> _customParameters = this._tomMappingExtensions.getCustomParameters(clop);
        final Iterable<FeatureParameter> parameters = _customParameters;
        CharSequence _javaFactoryCreateOperatorWithParameters = this.javaFactoryCreateOperatorWithParameters(parameters, clop);
        _xblockexpression = (_javaFactoryCreateOperatorWithParameters);
      }
      _xifexpression = _xblockexpression;
    } else {
      String _name = clop.getName();
      EClass _class_ = clop.getClass_();
      CharSequence _javaFactoryCreateDefaultOperator = this.javaFactoryCreateDefaultOperator(map, _name, _class_);
      _xifexpression = _javaFactoryCreateDefaultOperator;
    }
    return _xifexpression;
  }
  
  public CharSequence javaFactoryCreateOperatorWithParameters(final Iterable<FeatureParameter> parameters, final ClassOperator clop) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static ");
    EClass _class_ = clop.getClass_();
    String _name = _class_.getName();
    _builder.append(_name, "");
    _builder.append(" ");
    String _name_1 = clop.getName();
    String _firstLower = StringExtensions.toFirstLower(_name_1);
    _builder.append(_firstLower, "");
    _builder.append("(");
    _builder.newLineIfNotEmpty();
    {
      boolean _hasElements = false;
      for(final FeatureParameter p : parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _javaFeatureParameter = this.injpa.javaFeatureParameter(p);
        _builder.append(_javaFeatureParameter, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(") {");
    _builder.newLine();
    _builder.append("\t");
    EClass _class__1 = clop.getClass_();
    String _name_2 = _class__1.getName();
    _builder.append(_name_2, "	");
    _builder.append(" o = ");
    EClass _class__2 = clop.getClass_();
    EPackage _ePackage = _class__2.getEPackage();
    String _name_3 = _ePackage.getName();
    _builder.append(_name_3, "	");
    _builder.append("Factory.create");
    EClass _class__3 = clop.getClass_();
    String _name_4 = _class__3.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name_4);
    _builder.append(_firstUpper, "	");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    {
      for(final FeatureParameter p_1 : parameters) {
        _builder.append("\t");
        EStructuralFeature _feature = p_1.getFeature();
        CharSequence _structureFeatureSetter = this.structureFeatureSetter(_feature);
        _builder.append(_structureFeatureSetter, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<SettedFeatureParameter> _settedCustomParameters = this._tomMappingExtensions.getSettedCustomParameters(clop);
      for(final SettedFeatureParameter p_2 : _settedCustomParameters) {
        _builder.append("\t");
        _builder.append("o.set");
        EStructuralFeature _feature_1 = p_2.getFeature();
        String _name_5 = _feature_1.getName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_5);
        _builder.append(_firstUpper_1, "	");
        _builder.append("(");
        EStructuralFeature _feature_2 = p_2.getFeature();
        SettedValue _value = p_2.getValue();
        CharSequence _settedValue = this.injop.settedValue(_feature_2, _value);
        _builder.append(_settedValue, "	");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return o;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence javaFactoryCreateDefaultOperator(final Mapping mapping, final String name, final EClass ecl) {
    CharSequence _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
      final List<EReference> parameters = _defaultParameters;
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _operator_and = false;
        boolean _isAbstract = ecl.isAbstract();
        boolean _operator_not = BooleanExtensions.operator_not(_isAbstract);
        if (!_operator_not) {
          _operator_and = false;
        } else {
          boolean _isInterface = ecl.isInterface();
          boolean _operator_not_1 = BooleanExtensions.operator_not(_isInterface);
          _operator_and = BooleanExtensions.operator_and(_operator_not, _operator_not_1);
        }
        if (_operator_and) {
          _builder.append("public static ");
          String _name = ecl.getName();
          _builder.append(_name, "");
          _builder.append(" ");
          String _firstLower = StringExtensions.toFirstLower(name);
          _builder.append(_firstLower, "");
          _builder.append("(");
          CharSequence _javaClassAttributes = this.injop.javaClassAttributes(mapping, ecl);
          _builder.append(_javaClassAttributes, "");
          _builder.newLineIfNotEmpty();
          {
            boolean _hasElements = false;
            for(final EReference param : parameters) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder.appendImmediate(",", "");
              }
              CharSequence _defaultJavaFeatureParameter = this.injpa.defaultJavaFeatureParameter(param);
              _builder.append(_defaultJavaFeatureParameter, "");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          String _name_1 = ecl.getName();
          _builder.append(_name_1, "	");
          _builder.append(" o = ");
          EPackage _ePackage = ecl.getEPackage();
          String _name_2 = _ePackage.getName();
          _builder.append(_name_2, "	");
          _builder.append("Factory.create.");
          String _name_3 = ecl.getName();
          String _firstUpper = StringExtensions.toFirstUpper(_name_3);
          _builder.append(_firstUpper, "	");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
          {
            EList<EAttribute> _eAllAttributes = ecl.getEAllAttributes();
            for(final EAttribute attribute : _eAllAttributes) {
              _builder.append("\t");
              CharSequence _structureFeatureSetter = this.structureFeatureSetter(attribute);
              _builder.append(_structureFeatureSetter, "	");
              _builder.newLineIfNotEmpty();
            }
          }
          {
            for(final EReference param_1 : parameters) {
              _builder.append("\t");
              CharSequence _structureFeatureSetter_1 = this.structureFeatureSetter(param_1);
              _builder.append(_structureFeatureSetter_1, "	");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("return o;");
          _builder.newLine();
          _builder.append("}\t");
          _builder.newLine();
        }
      }
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence structureFeatureSetter(final EStructuralFeature esf) {
    CharSequence _xifexpression = null;
    boolean _isMany = esf.isMany();
    if (_isMany) {
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _isUnsettable = esf.isUnsettable();
        boolean _operator_not = BooleanExtensions.operator_not(_isUnsettable);
        if (_operator_not) {
          _builder.append("for(int i = 0 ; i < ");
          String _name = esf.getName();
          _builder.append(_name, "");
          _builder.append(".size() ; ++i) {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("o.get");
          String _name_1 = esf.getName();
          String _firstUpper = StringExtensions.toFirstUpper(_name_1);
          _builder.append(_firstUpper, "	");
          _builder.append("().add(_");
          String _name_2 = esf.getName();
          _builder.append(_name_2, "	");
          _builder.append(".get(i));");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      {
        boolean _isUnsettable_1 = esf.isUnsettable();
        boolean _operator_not_1 = BooleanExtensions.operator_not(_isUnsettable_1);
        if (_operator_not_1) {
          _builder_1.append("o.set");
          String _name_3 = esf.getName();
          String _firstUpper_1 = StringExtensions.toFirstUpper(_name_3);
          _builder_1.append(_firstUpper_1, "");
          _builder_1.append("(_");
          String _name_4 = esf.getName();
          _builder_1.append(_name_4, "");
          _builder_1.append(");");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _xifexpression = _builder_1;
    }
    return _xifexpression;
  }
  
  public CharSequence operator(final Mapping map, final Operator clop) {
    if (clop instanceof ClassOperator) {
      return _operator(map, (ClassOperator)clop);
    } else if (clop != null) {
      return _operator(map, clop);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(map, clop).toString());
    }
  }
}
