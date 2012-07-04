package tom.mapping.dsl.generator.tom;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.Parameter;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class ParametersCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  public String parameter(final Parameter p) {
    Terminal _type = p.getType();
    String _terminalType = this.terminalType(_type);
    return _terminalType;
  }
  
  public Object javaParameter(final Parameter p) {
    Terminal _type = p.getType();
    Object _javaTerminalType = this.javaTerminalType(_type);
    return _javaTerminalType;
  }
  
  public String terminalType(final Terminal t) {
    String _name = t.getName();
    return _name;
  }
  
  public Object javaTerminalType(final Terminal t) {
    Object _xifexpression = null;
    boolean _isMany = t.isMany();
    if (_isMany) {
      boolean _xblockexpression = false;
      {
        List<String> tnames = null;
        EClass _class_ = t.getClass_();
        String _name = _class_.getName();
        boolean _add = tnames.add(_name);
        _xblockexpression = (_add);
      }
      _xifexpression = _xblockexpression;
    } else {
      EClass _class__1 = t.getClass_();
      String _name_1 = _class__1.getName();
      _xifexpression = _name_1;
    }
    return _xifexpression;
  }
  
  public Object featureParameter(final Mapping mapping, final FeatureParameter fp) {
    EStructuralFeature _feature = fp.getFeature();
    Object _feature_1 = this.feature(mapping, _feature);
    return _feature_1;
  }
  
  public String javaFeatureParameter(final FeatureParameter fp) {
    EStructuralFeature _feature = fp.getFeature();
    String _feature_1 = this.feature(_feature);
    return _feature_1;
  }
  
  public Object defaultFeatureParameter(final Mapping mapping, final EStructuralFeature esf) {
    Object _feature = this.feature(mapping, esf);
    return _feature;
  }
  
  public String defaultJavaFeatureParameter(final EStructuralFeature efp) {
    String _feature = this.feature(efp);
    return _feature;
  }
  
  public String renameEcoreClasses(final EAttribute eat) {
    String _xifexpression = null;
    EDataType _eAttributeType = eat.getEAttributeType();
    String _name = _eAttributeType.getName();
    boolean _operator_equals = ObjectExtensions.operator_equals(_name, "EInt");
    if (_operator_equals) {
      EDataType _eAttributeType_1 = eat.getEAttributeType();
      _eAttributeType_1.setName("int");
    } else {
      String _xifexpression_1 = null;
      EDataType _eAttributeType_2 = eat.getEAttributeType();
      String _name_1 = _eAttributeType_2.getName();
      boolean _operator_equals_1 = ObjectExtensions.operator_equals(_name_1, "EFloat");
      if (_operator_equals_1) {
        EDataType _eAttributeType_3 = eat.getEAttributeType();
        _eAttributeType_3.setName("float");
      } else {
        String _xifexpression_2 = null;
        EDataType _eAttributeType_4 = eat.getEAttributeType();
        String _name_2 = _eAttributeType_4.getName();
        boolean _operator_equals_2 = ObjectExtensions.operator_equals(_name_2, "EDouble");
        if (_operator_equals_2) {
          EDataType _eAttributeType_5 = eat.getEAttributeType();
          _eAttributeType_5.setName("double");
        } else {
          String _xifexpression_3 = null;
          EDataType _eAttributeType_6 = eat.getEAttributeType();
          String _name_3 = _eAttributeType_6.getName();
          boolean _operator_equals_3 = ObjectExtensions.operator_equals(_name_3, "EBoolean");
          if (_operator_equals_3) {
            EDataType _eAttributeType_7 = eat.getEAttributeType();
            _eAttributeType_7.setName("boolean");
          } else {
            String _xifexpression_4 = null;
            EDataType _eAttributeType_8 = eat.getEAttributeType();
            String _name_4 = _eAttributeType_8.getName();
            boolean _operator_equals_4 = ObjectExtensions.operator_equals(_name_4, "EString");
            if (_operator_equals_4) {
              EDataType _eAttributeType_9 = eat.getEAttributeType();
              _eAttributeType_9.setName("String");
            } else {
              String _xifexpression_5 = null;
              EDataType _eAttributeType_10 = eat.getEAttributeType();
              String _instanceTypeName = _eAttributeType_10.getInstanceTypeName();
              boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_instanceTypeName, "null");
              if (_operator_notEquals) {
                EDataType _eAttributeType_11 = eat.getEAttributeType();
                String _instanceClassName = _eAttributeType_11.getInstanceClassName();
                _xifexpression_5 = _instanceClassName;
              } else {
                EDataType _eAttributeType_12 = eat.getEAttributeType();
                String _name_5 = _eAttributeType_12.getName();
                _xifexpression_5 = _name_5;
              }
              _xifexpression_4 = _xifexpression_5;
            }
            _xifexpression_3 = _xifexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  protected Object _feature(final Mapping mapping, final EStructuralFeature esf) {
    return null;
  }
  
  protected Object _feature(final Mapping mapping, final EReference er) {
    EClass _eReferenceType = er.getEReferenceType();
    boolean _isMany = er.isMany();
    Terminal _terminal = mapping.getTerminal(_eReferenceType, _isMany);
    String _name = this._tomMappingExtensions.name(_terminal, mapping);
    return _name;
  }
  
  protected Object _feature(final Mapping mapping, final EAttribute eat) {
    Object _xifexpression = null;
    boolean _isMany = eat.isMany();
    if (_isMany) {
      List<String> _xblockexpression = null;
      {
        List<? extends EAttribute> leat = null;
        final Function1<EAttribute,String> _function = new Function1<EAttribute,String>() {
            public String apply(final EAttribute it) {
              String _renameEcoreClasses = ParametersCompiler.this.renameEcoreClasses(eat);
              return _renameEcoreClasses;
            }
          };
        List<String> _map = ListExtensions.map(leat, _function);
        _xblockexpression = (_map);
      }
      _xifexpression = _xblockexpression;
    } else {
      String _renameEcoreClasses = this.renameEcoreClasses(eat);
      _xifexpression = _renameEcoreClasses;
    }
    return _xifexpression;
  }
  
  protected String _feature(final EStructuralFeature esf) {
    return null;
  }
  
  protected String _feature(final EReference er) {
    String _xblockexpression = null;
    {
      boolean _isMany = er.isMany();
      if (_isMany) {
        {
          List<String> lert = null;
          EClass _eReferenceType = er.getEReferenceType();
          String _name = _eReferenceType.getName();
          lert.add(_name);
        }
      }
      EClass _eReferenceType_1 = er.getEReferenceType();
      String _name_1 = _eReferenceType_1.getName();
      _xblockexpression = (_name_1);
    }
    return _xblockexpression;
  }
  
  protected String _feature(final EEnum ee) {
    String _name = ee.getName();
    return _name;
  }
  
  protected String _feature(final EAttribute eat) {
    EDataType _eAttributeType = eat.getEAttributeType();
    String _primitiveType = this.primitiveType(_eAttributeType);
    return _primitiveType;
  }
  
  protected String _primitiveType(final EDataType edt) {
    String _xifexpression = null;
    String _instanceTypeName = edt.getInstanceTypeName();
    boolean _operator_equals = ObjectExtensions.operator_equals(_instanceTypeName, "java.lang.String");
    if (_operator_equals) {
      _xifexpression = "String";
    } else {
      String _instanceTypeName_1 = edt.getInstanceTypeName();
      _xifexpression = _instanceTypeName_1;
    }
    return _xifexpression;
  }
  
  protected String _primitiveType(final EEnum ee) {
    String _name = ee.getName();
    return _name;
  }
  
  public Object feature(final Mapping mapping, final EStructuralFeature eat) {
    if (eat instanceof EAttribute) {
      return _feature(mapping, (EAttribute)eat);
    } else if (eat instanceof EReference) {
      return _feature(mapping, (EReference)eat);
    } else if (eat != null) {
      return _feature(mapping, eat);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, eat).toString());
    }
  }
  
  public String feature(final ENamedElement eat) {
    if (eat instanceof EAttribute) {
      return _feature((EAttribute)eat);
    } else if (eat instanceof EEnum) {
      return _feature((EEnum)eat);
    } else if (eat instanceof EReference) {
      return _feature((EReference)eat);
    } else if (eat instanceof EStructuralFeature) {
      return _feature((EStructuralFeature)eat);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(eat).toString());
    }
  }
  
  public String primitiveType(final EDataType ee) {
    if (ee instanceof EEnum) {
      return _primitiveType((EEnum)ee);
    } else if (ee != null) {
      return _primitiveType(ee);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ee).toString());
    }
  }
}
