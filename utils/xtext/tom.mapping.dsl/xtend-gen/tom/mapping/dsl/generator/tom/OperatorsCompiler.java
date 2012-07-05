package tom.mapping.dsl.generator.tom;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
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
        _builder.append("%op \u00AC\u00B4mapping.getTerminal(ecl.class_,false).name\u00AC\u00AA \u00AC\u00B4name\u00AC\u00AA ");
        _builder.newLine();
        _builder.append("\u00AC\u00B4(for(EReference p : parameters) \u00AC\u00AA {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\u00AC\u00B4parameters.featureParameter(mapping, p))");
        _builder.newLine();
        _builder.append("\u00AC\u00AA }");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("is_fsym(t) {$t instanceof \u00AC\u00B4ecl.class_.name\u00AC\u00AA");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\u00AC\u00B4for(EReference p: ecl.parameters)\u00AC\u00AA {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\u00AC\u00B4settedParameterTest(ecl.class_, p)\u00AC\u00AA");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\u00AC\u00B4for(EReference p: parameters)\u00AC\u00AA{");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("get_slot(\u00AC\u00B4p.feature.name\u00AC\u00AA,t) {\u00AC\u00B4getter(ecl.class_, p.feature)\u00AC\u00AA}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("make(\u00AC\u00B4for(EReference p: parameters)\u00AC\u00AA {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\u00AC\u00B4p.feature.name\u00AC\u00AA");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\u00AC\u00B4tomFactoryQualifiedName(mapping\u00AC\u00AA.create\u00AC\u00B4this.name.toFirstUpper()\u00AC\u00AA(\u00ACfor(EReference p:parameters)\u00AC\u00AA{$_\u00AC\u00B4p.feature.name\u00AC\u00AA})");
        _builder.newLine();
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
      _builder.append("%op \u00AC\u00B4mapping.getTerminal(clop.class_,false).name\u00AC\u00AA \u00AC\u00B4name\u00AC\u00AA ");
      _builder.newLine();
      _builder.append("\u00AC\u00B4(for(EReference p : parameters) \u00AC\u00AA {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\u00AC\u00B4parameters.featureParameter(mapping, p))");
      _builder.newLine();
      _builder.append("\u00AC\u00AA }");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("is_fsym(t) {$t instanceof \u00AC\u00B4clop.class_.name\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\u00AC\u00B4for(EReference p: ecl.parameters)\u00AC\u00AA {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("\u00AC\u00B4settedParameterTest(clop.class_, p)\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\u00AC\u00B4for(EReference p: parameters)\u00AC\u00AA{");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("get_slot(\u00AC\u00B4p.feature.name\u00AC\u00AA,t) {\u00AC\u00B4getter(clop.class_, p.feature)\u00AC\u00AA}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("make(\u00AC\u00B4for(EReference p: parameters)\u00AC\u00AA {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("\u00AC\u00B4p.feature.name\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\u00AC\u00B4tomFactoryQualifiedName(mapping\u00AC\u00AA.create\u00AC\u00B4this.name.toFirstUpper()\u00AC\u00AA(\u00ACfor(EReference p:parameters)\u00AC\u00AA{$_\u00AC\u00B4p.feature.name\u00AC\u00AA})");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence getter(final EClass c, final EStructuralFeature esf) {
    CharSequence _xifexpression = null;
    boolean _isMany = esf.isMany();
    if (_isMany) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("enforce(((\u00AC\u00B4c.name\u00AC\u00AA)$t).get\u00AC\u00B4name.toFirstUpper()\u00AC\u00AA()");
      _builder.newLine();
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  public Object classAttibutes(final Mapping mapping, final EClass ecl) {
    Object _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
      final List<EReference> parameters = _defaultParameters;
      EList<EAttribute> _eAllAttributes = ecl.getEAllAttributes();
      for (final EAttribute att : _eAllAttributes) {
      }
      Object _xifexpression = null;
      boolean _operator_and = false;
      EList<EAttribute> _eAllAttributes_1 = ecl.getEAllAttributes();
      int _size = _eAllAttributes_1.size();
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
        _xifexpression = null;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public Object javaClassAttibutes(final Mapping mapping, final EClass ecl) {
    Object _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ecl, mapping);
      final List<EReference> parameters = _defaultParameters;
      EList<EAttribute> _eAllAttributes = ecl.getEAllAttributes();
      for (final EAttribute att : _eAllAttributes) {
      }
      Object _xifexpression = null;
      boolean _operator_and = false;
      EList<EAttribute> _eAllAttributes_1 = ecl.getEAllAttributes();
      int _size = _eAllAttributes_1.size();
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
        _xifexpression = null;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _settedParameterTest(final EClass c, final FeatureParameter feature) {
    return null;
  }
  
  protected CharSequence _settedParameterTest(final EClass c, final SettedFeatureParameter feature) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("((\u00AC\u00B4c.name\u00AC\u00AA)$t).get\u00AC\u00B4feature.name.toFirstUpper()\u00AC\u00AA().equals(\u00AC\u00B4settedValue(feature, sfp.value) \u00AC\u00AA");
    _builder.newLine();
    return _builder;
  }
  
  protected String _settedValue(final EStructuralFeature feature, final SettedValue sv) {
    return null;
  }
  
  protected String _settedValue(final EStructuralFeature feature, final EnumLiteralValue literal) {
    EClassifier _eType = feature.getEType();
    String _name = _eType.getName();
    return _name;
  }
  
  protected String _settedValue(final EStructuralFeature feature, final JavaCodeValue jcv) {
    String _xblockexpression = null;
    {
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
      String _xifexpression = null;
      if (isString) {
        _xifexpression = "java";
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
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
  
  public CharSequence settedParameterTest(final EClass c, final FeatureParameter feature) {
    if (feature instanceof SettedFeatureParameter) {
      return _settedParameterTest(c, (SettedFeatureParameter)feature);
    } else if (feature != null) {
      return _settedParameterTest(c, feature);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c, feature).toString());
    }
  }
  
  public String settedValue(final EStructuralFeature feature, final SettedValue literal) {
    if (literal instanceof EnumLiteralValue) {
      return _settedValue(feature, (EnumLiteralValue)literal);
    } else if (literal instanceof JavaCodeValue) {
      return _settedValue(feature, (JavaCodeValue)literal);
    } else if (literal != null) {
      return _settedValue(feature, literal);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(feature, literal).toString());
    }
  }
}
