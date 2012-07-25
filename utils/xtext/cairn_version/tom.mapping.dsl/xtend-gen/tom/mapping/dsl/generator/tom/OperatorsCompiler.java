package tom.mapping.dsl.generator.tom;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.ParametersCompiler;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.EnumLiteralValue;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.JavaCodeValue;
import tom.mapping.model.Mapping;
import tom.mapping.model.Operator;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.SettedValue;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class OperatorsCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  @Inject
  private ParametersCompiler injpa;
  
  protected CharSequence _operator(final Mapping mapping, final Operator op) {
    return null;
  }
  
  protected CharSequence _operator(final Mapping mapping, final ClassOperator clop) {
    CharSequence _xifexpression = null;
    EList<FeatureParameter> _parameters = clop.getParameters();
    int _size = _parameters.size();
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    if (_operator_greaterThan) {
      CharSequence _classOperatorWithParameters = this.classOperatorWithParameters(mapping, clop);
      _xifexpression = _classOperatorWithParameters;
    } else {
      String _name = clop.getName();
      EClass _class_ = clop.getClass_();
      CharSequence _classOperator = this.classOperator(mapping, _name, _class_);
      _xifexpression = _classOperator;
    }
    return _xifexpression;
  }
  
  public CharSequence classOperator(final Mapping mapping, final String op, final EClass ecl) {
    CharSequence _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
      final List<EReference> parameters = _defaultParameters;
      CharSequence _xifexpression = null;
      Terminal _terminal = mapping.getTerminal(ecl, false);
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_terminal, null);
      if (_operator_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("%op ");
        Terminal _terminal_1 = mapping.getTerminal(ecl, false);
        String _name = _terminal_1.getName();
        _builder.append(_name, "");
        _builder.append(" ");
        _builder.append(op, "");
        _builder.append(" (");
        CharSequence _classAttributes = this.classAttributes(mapping, ecl);
        _builder.append(_classAttributes, "");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        {
          boolean _hasElements = false;
          for(final EReference p : parameters) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            CharSequence _defaultFeatureParameter = this.injpa.defaultFeatureParameter(mapping, p);
            _builder.append(_defaultFeatureParameter, "");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("is_fsym(t) {$t instanceof ");
        String _name_1 = ecl.getName();
        _builder.append(_name_1, "");
        _builder.append("}");
        _builder.newLineIfNotEmpty();
        _builder.newLine();
        {
          EList<EAttribute> _eAllAttributes = ecl.getEAllAttributes();
          for(final EAttribute attribute : _eAllAttributes) {
            _builder.append("get_slot(");
            String _name_2 = attribute.getName();
            _builder.append(_name_2, "");
            _builder.append(",t) {");
            _builder.newLineIfNotEmpty();
            _builder.append("((");
            String _name_3 = ecl.getName();
            _builder.append(_name_3, "");
            _builder.append(")$t).get");
            String _name_4 = attribute.getName();
            String _firstUpper = StringExtensions.toFirstUpper(_name_4);
            _builder.append(_firstUpper, "");
            _builder.append("()}");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        {
          for(final EReference p_1 : parameters) {
            _builder.append("get_slot(");
            String _name_5 = p_1.getName();
            _builder.append(_name_5, "");
            _builder.append(",t) {");
            CharSequence _ter = this.getter(ecl, p_1);
            _builder.append(_ter, "");
            _builder.append("}");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        _builder.append("make(");
        {
          EList<EAttribute> _eAllAttributes_1 = ecl.getEAllAttributes();
          boolean _hasElements_1 = false;
          for(final EAttribute att : _eAllAttributes_1) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(" ");
            _builder.newLineIfNotEmpty();
            String _name_6 = att.getName();
            _builder.append(_name_6, "");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _operator_and = false;
          EList<EAttribute> _eAllAttributes_2 = ecl.getEAllAttributes();
          int _size = _eAllAttributes_2.size();
          boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
          if (!_operator_greaterThan) {
            _operator_and = false;
          } else {
            List<EReference> _defaultParameters_1 = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
            int _size_1 = _defaultParameters_1.size();
            boolean _operator_greaterThan_1 = IntegerExtensions.operator_greaterThan(_size_1, 0);
            _operator_and = BooleanExtensions.operator_and(_operator_greaterThan, _operator_greaterThan_1);
          }
          if (_operator_and) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
        {
          boolean _hasElements_2 = false;
          for(final EReference param : parameters) {
            if (!_hasElements_2) {
              _hasElements_2 = true;
            } else {
              _builder.appendImmediate(",", "	");
            }
            _builder.append("\t");
            String _name_7 = param.getName();
            _builder.append(_name_7, "	");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("{");
        String _mFactoryQualifiedName = this._tomMappingExtensions.tomFactoryQualifiedName(mapping);
        _builder.append(_mFactoryQualifiedName, "	");
        _builder.append(".create");
        String _name_8 = ecl.getName();
        String _firstUpper_1 = StringExtensions.toFirstUpper(_name_8);
        _builder.append(_firstUpper_1, "	");
        _builder.append("(");
        {
          EList<EAttribute> _eAllAttributes_3 = ecl.getEAllAttributes();
          boolean _hasElements_3 = false;
          for(final EAttribute att_1 : _eAllAttributes_3) {
            if (!_hasElements_3) {
              _hasElements_3 = true;
            } else {
              _builder.appendImmediate(",", "	");
            }
            _builder.append("$");
            String _name_9 = att_1.getName();
            _builder.append(_name_9, "	");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        {
          boolean _operator_and_1 = false;
          EList<EAttribute> _eAllAttributes_4 = ecl.getEAllAttributes();
          int _size_2 = _eAllAttributes_4.size();
          boolean _operator_greaterThan_2 = IntegerExtensions.operator_greaterThan(_size_2, 0);
          if (!_operator_greaterThan_2) {
            _operator_and_1 = false;
          } else {
            List<EReference> _defaultParameters_2 = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
            int _size_3 = _defaultParameters_2.size();
            boolean _operator_greaterThan_3 = IntegerExtensions.operator_greaterThan(_size_3, 0);
            _operator_and_1 = BooleanExtensions.operator_and(_operator_greaterThan_2, _operator_greaterThan_3);
          }
          if (_operator_and_1) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        {
          boolean _hasElements_4 = false;
          for(final EReference param_1 : parameters) {
            if (!_hasElements_4) {
              _hasElements_4 = true;
            } else {
              _builder.appendImmediate(",", "			");
            }
            _builder.append(" {$");
            String _name_10 = param_1.getName();
            _builder.append(_name_10, "			");
            _builder.append("}");
          }
        }
        _builder.append(")}");
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public CharSequence classOperatorWithParameters(final Mapping mapping, final ClassOperator clop) {
    CharSequence _xblockexpression = null;
    {
      Iterable<FeatureParameter> _customParameters = this._tomMappingExtensions.getCustomParameters(clop);
      final Iterable<FeatureParameter> parameters = _customParameters;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("%op ");
      EClass _class_ = clop.getClass_();
      Terminal _terminal = mapping.getTerminal(_class_, false);
      String _name = _terminal.getName();
      _builder.append(_name, "");
      _builder.append(" ");
      String _name_1 = clop.getName();
      _builder.append(_name_1, "");
      _builder.append(" ");
      _builder.newLineIfNotEmpty();
      _builder.append("(");
      {
        boolean _hasElements = false;
        for(final FeatureParameter p : parameters) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "");
          }
          _builder.newLineIfNotEmpty();
          CharSequence _featureParameter = this.injpa.featureParameter(mapping, p);
          _builder.append(_featureParameter, "");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("is_fsym(t) {$t instanceof ");
      EClass _class__1 = clop.getClass_();
      String _name_2 = _class__1.getName();
      _builder.append(_name_2, "	");
      _builder.newLineIfNotEmpty();
      {
        EList<FeatureParameter> _parameters = clop.getParameters();
        for(final FeatureParameter p_1 : _parameters) {
          _builder.append("\t");
          EClass _class__2 = clop.getClass_();
          CharSequence _settedParameterTest = this.settedParameterTest(_class__2, p_1);
          _builder.append(_settedParameterTest, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
        }
      }
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.newLine();
      {
        for(final FeatureParameter p_2 : parameters) {
          _builder.append("\t");
          _builder.append("get_slot(");
          EStructuralFeature _feature = p_2.getFeature();
          String _name_3 = _feature.getName();
          _builder.append(_name_3, "	");
          _builder.append(",t) {");
          EClass _class__3 = clop.getClass_();
          EStructuralFeature _feature_1 = p_2.getFeature();
          CharSequence _ter = this.getter(_class__3, _feature_1);
          _builder.append(_ter, "	");
          _builder.append("}");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("make(");
      {
        boolean _hasElements_1 = false;
        for(final FeatureParameter p_3 : parameters) {
          if (!_hasElements_1) {
            _hasElements_1 = true;
          } else {
            _builder.appendImmediate(",", "	");
          }
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("_");
          EStructuralFeature _feature_2 = p_3.getFeature();
          String _name_4 = _feature_2.getName();
          _builder.append(_name_4, "	");
          _builder.newLineIfNotEmpty();
          _builder.append("   \t\t");
        }
      }
      _builder.append(") {");
      String _mFactoryQualifiedName = this._tomMappingExtensions.tomFactoryQualifiedName(mapping);
      _builder.append(_mFactoryQualifiedName, "	");
      _builder.append(".create");
      String _name_5 = clop.getName();
      String _firstUpper = StringExtensions.toFirstUpper(_name_5);
      _builder.append(_firstUpper, "	");
      _builder.append("(");
      {
        boolean _hasElements_2 = false;
        for(final FeatureParameter p_4 : parameters) {
          if (!_hasElements_2) {
            _hasElements_2 = true;
          } else {
            _builder.appendImmediate(",", "	");
          }
          _builder.append("$_");
          EStructuralFeature _feature_3 = p_4.getFeature();
          String _name_6 = _feature_3.getName();
          _builder.append(_name_6, "	");
        }
      }
      _builder.append(")}");
      _builder.newLineIfNotEmpty();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence getter(final EClass c, final EStructuralFeature esf) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isMany = esf.isMany();
      if (_isMany) {
        _builder.append("enforce");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("((");
    String _name = c.getName();
    _builder.append(_name, "	");
    _builder.append(")$t).get");
    String _name_1 = esf.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name_1);
    _builder.append(_firstUpper, "	");
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    {
      boolean _isMany_1 = esf.isMany();
      if (_isMany_1) {
        _builder.append(")");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence classAttributes(final Mapping mapping, final EClass ecl) {
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
        String _name = att.getName();
        _builder.append(_name, "");
        _builder.append(" : ");
        CharSequence _feature = this.injpa.feature(att);
        _builder.append(_feature, "");
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
        CharSequence _feature = this.injpa.feature(att);
        _builder.append(_feature, "");
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
  
  protected CharSequence _settedParameterTest(final EClass c, final FeatureParameter feature) {
    return null;
  }
  
  protected CharSequence _settedParameterTest(final EClass c, final SettedFeatureParameter sfp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("&& ((");
    String _name = c.getName();
    _builder.append(_name, "");
    _builder.append(")$t).get");
    EStructuralFeature _feature = sfp.getFeature();
    String _name_1 = _feature.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name_1);
    _builder.append(_firstUpper, "");
    _builder.append("().equals(");
    EStructuralFeature _feature_1 = sfp.getFeature();
    SettedValue _value = sfp.getValue();
    CharSequence _settedValue = this.settedValue(_feature_1, _value);
    _builder.append(_settedValue, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _settedValue(final EStructuralFeature feature, final SettedValue sv) {
    return null;
  }
  
  protected CharSequence _settedValue(final EStructuralFeature feature, final EnumLiteralValue elv) {
    StringConcatenation _builder = new StringConcatenation();
    EClassifier _eType = feature.getEType();
    String _name = _eType.getName();
    _builder.append(_name, "");
    _builder.append(".");
    EEnumLiteral _literal = elv.getLiteral();
    String _name_1 = _literal.getName();
    _builder.append(_name_1, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _settedValue(final EStructuralFeature feature, final JavaCodeValue jcv) {
    StringConcatenation _builder = new StringConcatenation();
    boolean _operator_and = false;
    EClassifier _eType = feature.getEType();
    String _instanceTypeName = _eType.getInstanceTypeName();
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_instanceTypeName, null);
    if (!_operator_notEquals) {
      _operator_and = false;
    } else {
      EClassifier _eType_1 = feature.getEType();
      String _instanceTypeName_1 = _eType_1.getInstanceTypeName();
      boolean _contains = _instanceTypeName_1.contains("java.lang.String");
      _operator_and = BooleanExtensions.operator_and(_operator_notEquals, _contains);
    }
    final boolean isString = _operator_and;
    _builder.newLineIfNotEmpty();
    {
      if (isString) {
        _builder.append("\"");
      }
    }
    _builder.newLineIfNotEmpty();
    String _java = jcv.getJava();
    _builder.append(_java, "");
    _builder.newLineIfNotEmpty();
    {
      if (isString) {
        _builder.append("\"");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence operator(final Mapping mapping, final Operator clop) {
    if (clop instanceof ClassOperator) {
      return _operator(mapping, (ClassOperator)clop);
    } else if (clop != null) {
      return _operator(mapping, clop);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, clop).toString());
    }
  }
  
  public CharSequence settedParameterTest(final EClass c, final FeatureParameter sfp) {
    if (sfp instanceof SettedFeatureParameter) {
      return _settedParameterTest(c, (SettedFeatureParameter)sfp);
    } else if (sfp != null) {
      return _settedParameterTest(c, sfp);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c, sfp).toString());
    }
  }
  
  public CharSequence settedValue(final EStructuralFeature feature, final SettedValue elv) {
    if (elv instanceof EnumLiteralValue) {
      return _settedValue(feature, (EnumLiteralValue)elv);
    } else if (elv instanceof JavaCodeValue) {
      return _settedValue(feature, (JavaCodeValue)elv);
    } else if (elv != null) {
      return _settedValue(feature, elv);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(feature, elv).toString());
    }
  }
}
