package tom.mapping.dsl.generator.tom;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
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
import tom.mapping.model.ClassOperator;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.Module;
import tom.mapping.model.Operator;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.SettedValue;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class TomFactoryCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  private String prefix = "tom";
  
  @Inject
  private OperatorsCompiler injop;
  
  @Inject
  private ImportsCompiler injco;
  
  @Inject
  private ParametersCompiler injpa;
  
  public void compile(final Mapping m, final IFileSystemAccess fsa) {
    String _operator_plus = StringExtensions.operator_plus(this.prefix, "/");
    String _name = m.getName();
    String _firstLower = StringExtensions.toFirstLower(_name);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _firstLower);
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, "/internal/");
    String _mFactoryName = this._tomMappingExtensions.tomFactoryName(m);
    String _operator_plus_3 = StringExtensions.operator_plus(_operator_plus_2, _mFactoryName);
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
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _packagePrefix = this._tomMappingExtensions.getPackagePrefix(this.prefix);
    _builder.append(_packagePrefix, "");
    String _name = map.getName();
    String _firstLower = StringExtensions.toFirstLower(_name);
    _builder.append(_firstLower, "");
    _builder.append(".internal;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/* PROTECTED REGION ID(map.name+\"_tom_factory_imports\") ENABLED START */");
    _builder.newLine();
    _builder.append("// protected imports, you should add here required imports that won\'t be removed after regeneration of the maping code");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("import java.util.List ");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    this.injco.imports(map);
    _builder.newLineIfNotEmpty();
    _builder.append("/* ENDPROTECT */");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append("* Factory used by TOM for ");
    String _name_1 = map.getName();
    _builder.append(_name_1, "");
    _builder.append(" mapping.");
    _builder.newLineIfNotEmpty();
    _builder.append("* It shouldn\'t be visible outside of the plugin");
    _builder.newLine();
    _builder.append("* -- Generated by TOM mapping EMF generator --");
    _builder.newLine();
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class ");
    String _mFactoryName = this._tomMappingExtensions.tomFactoryName(map);
    _builder.append(_mFactoryName, "");
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
        _builder.append("   ");
        EClass _class_ = elt.getClass_();
        EPackage _ePackage = _class_.getEPackage();
        boolean _add = packageList.add(_ePackage);
        _builder.append(_add, "   ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      ArrayList<EPackage> _intersectName = this.intersectName(packageList);
      for(final EPackage pack : _intersectName) {
        _builder.append("\t");
        _builder.append("public static ");
        String _name_2 = pack.getName();
        String _firstUpper = StringExtensions.toFirstUpper(_name_2);
        _builder.append(_firstUpper, "	");
        _builder.append("Factory ");
        String _name_3 = pack.getName();
        _builder.append(_name_3, "	");
        _builder.append("Factory = ");
        String _name_4 = pack.getName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_4);
        _builder.append(_firstUpper_1, "	");
        _builder.append("Factory.eINSTANCE;");
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
        _builder.append("\t\t \t");
        EPackage _ePackage_1 = elt_1.getEPackage();
        boolean _add_1 = packageList2.add(_ePackage_1);
        _builder.append(_add_1, "		 	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    {
      ArrayList<EPackage> _intersectName_1 = this.intersectName(packageList2);
      for(final EPackage pack_1 : _intersectName_1) {
        _builder.append("\t");
        _builder.append("public static ");
        String _name_5 = pack_1.getName();
        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_5);
        _builder.append(_firstUpper_2, "	");
        _builder.append("Factory ");
        String _name_6 = pack_1.getName();
        _builder.append(_name_6, "	");
        _builder.append("Factory = ");
        String _name_7 = pack_1.getName();
        String _firstUpper_3 = StringExtensions.toFirstUpper(_name_7);
        _builder.append(_firstUpper_3, "	");
        _builder.append("Factory.eINSTANCE;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("// User operators ");
    EList<Operator> _operators_1 = map.getOperators();
    _builder.append(_operators_1, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      EList<Module> _modules = map.getModules();
      for(final Module module : _modules) {
        _builder.append("/** Module ");
        String _name_8 = module.getName();
        _builder.append(_name_8, "");
        _builder.append(" **/");
        _builder.newLineIfNotEmpty();
        {
          EList<Operator> _operators_2 = module.getOperators();
          for(final Operator op : _operators_2) {
            _builder.append("// Operator ");
            String _name_9 = op.getName();
            _builder.append(_name_9, "");
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
    _builder.append("* Default TOM operators for ");
    String _name_10 = map.getName();
    _builder.append(_name_10, "");
    _builder.append(" mapping. Each class that has a terminal type has aloso a default create function.");
    _builder.newLineIfNotEmpty();
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    {
      EList<EClass> _allDefaultOperators_1 = map.getAllDefaultOperators();
      for(final EClass op_1 : _allDefaultOperators_1) {
        {
          String _instanceClassName = op_1.getInstanceClassName();
          boolean _contains = _instanceClassName.contains("java.util.Map$Entry");
          boolean _operator_not = BooleanExtensions.operator_not(_contains);
          if (_operator_not) {
            String _name_11 = op_1.getName();
            CharSequence _javaFactoryCreateDefaultOperator = this.javaFactoryCreateDefaultOperator(map, _name_11, op_1);
            _builder.append(_javaFactoryCreateDefaultOperator, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public CharSequence operator(final Mapping mapping, final Operator op) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// ");
    EClass _eClass = op.eClass();
    String _name = _eClass.getName();
    _builder.append(_name, "");
    return _builder;
  }
  
  public CharSequence operator(final Mapping mapping, final ClassOperator clop) {
    CharSequence _xifexpression = null;
    EList<FeatureParameter> _parameters = clop.getParameters();
    int _size = _parameters.size();
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    if (_operator_greaterThan) {
      CharSequence _xblockexpression = null;
      {
        Iterable<FeatureParameter> _customParameters = this._tomMappingExtensions.getCustomParameters(clop);
        final Iterable<FeatureParameter> parameters = _customParameters;
        CharSequence _javaFactoryCreateOperatorWithParameters = this.javaFactoryCreateOperatorWithParameters(parameters, mapping, clop);
        _xblockexpression = (_javaFactoryCreateOperatorWithParameters);
      }
      _xifexpression = _xblockexpression;
    } else {
      String _name = clop.getName();
      EClass _class_ = clop.getClass_();
      CharSequence _javaFactoryCreateDefaultOperator = this.javaFactoryCreateDefaultOperator(mapping, _name, _class_);
      _xifexpression = _javaFactoryCreateDefaultOperator;
    }
    return _xifexpression;
  }
  
  public CharSequence javaFactoryCreateOperatorWithParameters(final Iterable<FeatureParameter> parameters, final Mapping mapping, final ClassOperator clop) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// CreateOperatorWithParameters ");
    String _name = clop.getName();
    _builder.append(_name, "");
    _builder.newLineIfNotEmpty();
    _builder.append("public static ");
    EClass _class_ = clop.getClass_();
    String _name_1 = _class_.getName();
    _builder.append(_name_1, "");
    _builder.append(" create");
    String _name_2 = clop.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name_2);
    _builder.append(_firstUpper, "");
    _builder.append("(");
    {
      boolean _hasElements = false;
      for(final FeatureParameter p : parameters) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        EStructuralFeature _feature = p.getFeature();
        Object _typeOfParameter = this.typeOfParameter(mapping, _feature);
        _builder.append(_typeOfParameter, "");
        _builder.append(" _");
        EStructuralFeature _feature_1 = p.getFeature();
        String _name_3 = _feature_1.getName();
        _builder.append(_name_3, "");
      }
    }
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    EClass _class__1 = clop.getClass_();
    String _name_4 = _class__1.getName();
    _builder.append(_name_4, "	");
    _builder.append(" o = ");
    EClass _class__2 = clop.getClass_();
    EPackage _ePackage = _class__2.getEPackage();
    String _name_5 = _ePackage.getName();
    _builder.append(_name_5, "	");
    _builder.append("Factory.create");
    EClass _class__3 = clop.getClass_();
    String _name_6 = _class__3.getName();
    String _firstUpper_1 = StringExtensions.toFirstUpper(_name_6);
    _builder.append(_firstUpper_1, "	");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    {
      for(final FeatureParameter p_1 : parameters) {
        _builder.append("\t");
        EStructuralFeature _feature_2 = p_1.getFeature();
        CharSequence _structureFeatureSetter = this.structureFeatureSetter(_feature_2);
        _builder.append(_structureFeatureSetter, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<SettedFeatureParameter> _settedCustomParameters = this._tomMappingExtensions.getSettedCustomParameters(clop);
      for(final SettedFeatureParameter p_2 : _settedCustomParameters) {
        _builder.append("\t");
        _builder.append("o.set");
        EStructuralFeature _feature_3 = p_2.getFeature();
        String _name_7 = _feature_3.getName();
        String _firstUpper_2 = StringExtensions.toFirstUpper(_name_7);
        _builder.append(_firstUpper_2, "	");
        _builder.append("(");
        EStructuralFeature _feature_4 = p_2.getFeature();
        SettedValue _value = p_2.getValue();
        CharSequence _settedValue = this.injop.settedValue(_feature_4, _value);
        _builder.append(_settedValue, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return o;");
    _builder.newLine();
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
      _builder.append("// CreateDefaultOperator ");
      String _name = ecl.getName();
      _builder.append(_name, "");
      _builder.newLineIfNotEmpty();
      _builder.append("public static ");
      String _name_1 = ecl.getName();
      _builder.append(_name_1, "");
      _builder.append(" create");
      String _name_2 = ecl.getName();
      String _firstUpper = StringExtensions.toFirstUpper(_name_2);
      _builder.append(_firstUpper, "");
      _builder.append("(");
      CharSequence _javaClassAttributes = this.injop.javaClassAttributes(mapping, ecl);
      _builder.append(_javaClassAttributes, "");
      _builder.newLineIfNotEmpty();
      {
        boolean _hasElements = false;
        for(final EReference p : parameters) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "");
          }
          CharSequence _typeOfParameter = this.typeOfParameter(mapping, p);
          _builder.append(_typeOfParameter, "");
          _builder.append(" _");
          String _name_3 = p.getName();
          _builder.append(_name_3, "");
        }
      }
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      String _name_4 = ecl.getName();
      _builder.append(_name_4, "	");
      _builder.append(" o = ");
      EPackage _ePackage = ecl.getEPackage();
      String _name_5 = _ePackage.getName();
      _builder.append(_name_5, "	");
      _builder.append("Factory create");
      String _name_6 = ecl.getName();
      String _firstUpper_1 = StringExtensions.toFirstUpper(_name_6);
      _builder.append(_firstUpper_1, "	");
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
        for(final EReference param : parameters) {
          CharSequence _structureFeatureSetter_1 = this.structureFeatureSetter(param);
          _builder.append(_structureFeatureSetter_1, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("return o;");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence structureFeatureSetter(final EStructuralFeature esf) {
    CharSequence _xifexpression = null;
    boolean _isMany = esf.isMany();
    if (_isMany) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("for(int i=0; i<_");
      String _name = esf.getName();
      _builder.append(_name, "");
      _builder.append(".size(); i++) {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("o.get");
      String _name_1 = esf.getName();
      String _firstUpper = StringExtensions.toFirstUpper(_name_1);
      _builder.append(_firstUpper, "	");
      _builder.append("().add(");
      CharSequence _featureAccess = this.featureAccess(esf);
      _builder.append(_featureAccess, "	");
      _builder.append(".get(i));");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("o.set");
      String _name_2 = esf.getName();
      String _firstUpper_1 = StringExtensions.toFirstUpper(_name_2);
      _builder_1.append(_firstUpper_1, "");
      _builder_1.append("(");
      CharSequence _featureAccess_1 = this.featureAccess(esf);
      _builder_1.append(_featureAccess_1, "");
      _builder_1.append(");");
      _builder_1.newLineIfNotEmpty();
      _xifexpression = _builder_1;
    }
    return _xifexpression;
  }
  
  public CharSequence featureAccess(final EStructuralFeature esf) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EClassifier _eType = esf.getEType();
      String _instanceTypeName = _eType.getInstanceTypeName();
      boolean _operator_equals = ObjectExtensions.operator_equals(_instanceTypeName, null);
      if (_operator_equals) {
        _builder.append("(");
        EClassifier _eType_1 = esf.getEType();
        String _name = _eType_1.getName();
        _builder.append(_name, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("_");
    String _name_1 = esf.getName();
    _builder.append(_name_1, "	");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public Object typeOfParameter(final Mapping mapping, final EStructuralFeature esf) {
    return null;
  }
  
  public CharSequence typeOfParameter(final Mapping mapping, final EReference eref) {
    boolean _isMany = eref.isMany();
    EClassifier _eType = eref.getEType();
    CharSequence _terminalTypeName = this.terminalTypeName(mapping, _isMany, _eType);
    return _terminalTypeName;
  }
  
  public String typeOfParameter(final Mapping mapping, final EEnum enu) {
    String _name = enu.getName();
    return _name;
  }
  
  public CharSequence typeOfParameters(final Mapping mapping, final EAttribute eat) {
    EDataType _eAttributeType = eat.getEAttributeType();
    CharSequence _primitiveType = this.injpa.primitiveType(_eAttributeType);
    return _primitiveType;
  }
  
  protected CharSequence _terminalTypeName(final Mapping mapping, final boolean many, final EClassifier ecl) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (many) {
        _builder.append("List<");
      }
    }
    String _name = ecl.getName();
    _builder.append(_name, "");
    {
      if (many) {
        _builder.append(">");
      }
    }
    return _builder;
  }
  
  protected CharSequence _terminalTypeName(final Mapping mapping, final boolean many, final EClass ecl) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (many) {
        _builder.append("List<");
      }
    }
    Terminal _terminal = mapping.getTerminal(ecl, false);
    EClass _class_ = _terminal.getClass_();
    String _name = _class_.getName();
    _builder.append(_name, "");
    {
      if (many) {
        _builder.append(">");
      }
    }
    return _builder;
  }
  
  public CharSequence javaClassAttributes(final Mapping mapping, final EClass ecl) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<EAttribute> _eAllAttributes = ecl.getEAllAttributes();
      boolean _hasElements = false;
      for(final EAttribute att : _eAllAttributes) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        Object _typeOfParameter = this.typeOfParameter(mapping, att);
        _builder.append(_typeOfParameter, "");
        _builder.append(" _");
        String _name = att.getName();
        _builder.append(_name, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      boolean _operator_and = false;
      EList<EAttribute> _eAllAttributes_1 = ecl.getEAllAttributes();
      int _size = _eAllAttributes_1.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (!_operator_greaterThan) {
        _operator_and = false;
      } else {
        List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
        int _size_1 = _defaultParameters.size();
        boolean _operator_greaterThan_1 = IntegerExtensions.operator_greaterThan(_size_1, 0);
        _operator_and = BooleanExtensions.operator_and(_operator_greaterThan, _operator_greaterThan_1);
      }
      if (_operator_and) {
        _builder.append(",");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence terminalTypeName(final Mapping mapping, final boolean many, final EClassifier ecl) {
    if (ecl instanceof EClass) {
      return _terminalTypeName(mapping, many, (EClass)ecl);
    } else if (ecl != null) {
      return _terminalTypeName(mapping, many, ecl);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, many, ecl).toString());
    }
  }
}
