package tom.mapping.dsl.generator.tom;

import java.util.Arrays;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
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
  
  public CharSequence parameter(final Parameter p) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = p.getName();
    _builder.append(_name, "");
    _builder.append(" : ");
    Terminal _type = p.getType();
    CharSequence _terminalType = this.terminalType(_type);
    _builder.append(_terminalType, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence javaParameter(final Parameter p) {
    StringConcatenation _builder = new StringConcatenation();
    Terminal _type = p.getType();
    CharSequence _javaTerminalType = this.javaTerminalType(_type);
    _builder.append(_javaTerminalType, "");
    _builder.append(" ");
    String _name = p.getName();
    _builder.append(_name, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence terminalType(final Terminal t) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = t.getName();
    _builder.append(_name, "");
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence javaTerminalType(final Terminal t) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isMany = t.isMany();
      if (_isMany) {
        _builder.append("List<");
        EClass _class_ = t.getClass_();
        String _name = _class_.getName();
        _builder.append(_name, "");
        _builder.append(">");
      } else {
        EClass _class__1 = t.getClass_();
        String _name_1 = _class__1.getName();
        _builder.append(_name_1, "");
      }
    }
    return _builder;
  }
  
  public CharSequence featureParameter(final Mapping mapping, final FeatureParameter fp) {
    StringConcatenation _builder = new StringConcatenation();
    EStructuralFeature _feature = fp.getFeature();
    String _name = _feature.getName();
    _builder.append(_name, "");
    _builder.append(" : ");
    EStructuralFeature _feature_1 = fp.getFeature();
    CharSequence _feature_2 = this.feature(mapping, _feature_1);
    _builder.append(_feature_2, "");
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence javaFeatureParameter(final FeatureParameter fp) {
    StringConcatenation _builder = new StringConcatenation();
    EStructuralFeature _feature = fp.getFeature();
    CharSequence _feature_1 = this.feature(_feature);
    _builder.append(_feature_1, "");
    _builder.append(" _");
    EStructuralFeature _feature_2 = fp.getFeature();
    String _name = _feature_2.getName();
    _builder.append(_name, "");
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence defaultFeatureParameter(final Mapping mapping, final EStructuralFeature esf) {
    StringConcatenation _builder = new StringConcatenation();
    String _name = esf.getName();
    _builder.append(_name, "");
    _builder.append(" : ");
    CharSequence _feature = this.feature(mapping, esf);
    _builder.append(_feature, "");
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence defaultJavaFeatureParameter(final EStructuralFeature efp) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _feature = this.feature(efp);
    _builder.append(_feature, "");
    _builder.append(" _");
    String _name = efp.getName();
    _builder.append(_name, "");
    return _builder;
  }
  
  public CharSequence renameEcoreClasses(final EAttribute eat) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EDataType _eAttributeType = eat.getEAttributeType();
      String _name = _eAttributeType.getName();
      boolean _operator_equals = ObjectExtensions.operator_equals(_name, "EInt");
      if (_operator_equals) {
        _builder.append("int");
        _builder.newLineIfNotEmpty();
      } else {
        EDataType _eAttributeType_1 = eat.getEAttributeType();
        String _name_1 = _eAttributeType_1.getName();
        boolean _operator_equals_1 = ObjectExtensions.operator_equals(_name_1, "ELong");
        if (_operator_equals_1) {
          _builder.append("long");
          _builder.newLineIfNotEmpty();
        } else {
          EDataType _eAttributeType_2 = eat.getEAttributeType();
          String _name_2 = _eAttributeType_2.getName();
          boolean _operator_equals_2 = ObjectExtensions.operator_equals(_name_2, "EFloat");
          if (_operator_equals_2) {
            _builder.append("float");
            _builder.newLineIfNotEmpty();
          } else {
            EDataType _eAttributeType_3 = eat.getEAttributeType();
            String _name_3 = _eAttributeType_3.getName();
            boolean _operator_equals_3 = ObjectExtensions.operator_equals(_name_3, "EDouble");
            if (_operator_equals_3) {
              _builder.append("double");
              _builder.newLineIfNotEmpty();
            } else {
              EDataType _eAttributeType_4 = eat.getEAttributeType();
              String _name_4 = _eAttributeType_4.getName();
              boolean _operator_equals_4 = ObjectExtensions.operator_equals(_name_4, "EBoolean");
              if (_operator_equals_4) {
                _builder.append("boolean");
                _builder.newLineIfNotEmpty();
              } else {
                EDataType _eAttributeType_5 = eat.getEAttributeType();
                String _name_5 = _eAttributeType_5.getName();
                boolean _operator_equals_5 = ObjectExtensions.operator_equals(_name_5, "EString");
                if (_operator_equals_5) {
                  _builder.append("String");
                  _builder.newLineIfNotEmpty();
                } else {
                  {
                    EDataType _eAttributeType_6 = eat.getEAttributeType();
                    String _instanceTypeName = _eAttributeType_6.getInstanceTypeName();
                    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_instanceTypeName, null);
                    if (_operator_notEquals) {
                      EDataType _eAttributeType_7 = eat.getEAttributeType();
                      String _instanceClassName = _eAttributeType_7.getInstanceClassName();
                      _builder.append(_instanceClassName, "");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t\t");
                    } else {
                      EDataType _eAttributeType_8 = eat.getEAttributeType();
                      String _name_6 = _eAttributeType_8.getName();
                      _builder.append(_name_6, "");
                    }
                  }
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  protected CharSequence _feature(final Mapping mapping, final EStructuralFeature esf) {
    return null;
  }
  
  protected CharSequence _feature(final Mapping mapping, final EReference er) {
    EClass _eReferenceType = er.getEReferenceType();
    boolean _isMany = er.isMany();
    Terminal _terminal = mapping.getTerminal(_eReferenceType, _isMany);
    String _name = this._tomMappingExtensions.name(_terminal, mapping);
    return _name;
  }
  
  protected CharSequence _feature(final Mapping mapping, final EAttribute eat) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isMany = eat.isMany();
      if (_isMany) {
        _builder.append("List<? extends ");
        CharSequence _renameEcoreClasses = this.renameEcoreClasses(eat);
        _builder.append(_renameEcoreClasses, "");
        _builder.append(">");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
      } else {
        _builder.append("renameEcoreClasses(eat)");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _feature(final EStructuralFeature esf) {
    return null;
  }
  
  protected CharSequence _feature(final EReference er) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isMany = er.isMany();
      if (_isMany) {
        _builder.append("List<? extends ");
        EClass _eReferenceType = er.getEReferenceType();
        String _name = _eReferenceType.getName();
        _builder.append(_name, "");
        _builder.append(">");
      } else {
        _builder.newLineIfNotEmpty();
        _builder.append("er.EReferenceType.name");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _feature(final EEnum ee) {
    String _name = ee.getName();
    return _name;
  }
  
  protected CharSequence _feature(final EAttribute eat) {
    EDataType _eAttributeType = eat.getEAttributeType();
    CharSequence _primitiveType = this.primitiveType(_eAttributeType);
    return _primitiveType;
  }
  
  protected CharSequence _primitiveType(final EDataType edt) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _instanceTypeName = edt.getInstanceTypeName();
      boolean _operator_equals = ObjectExtensions.operator_equals(_instanceTypeName, "java.lang.String");
      if (_operator_equals) {
        _builder.append("\"String\";");
        _builder.newLineIfNotEmpty();
      } else {
        String _instanceTypeName_1 = edt.getInstanceTypeName();
        _builder.append(_instanceTypeName_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _primitiveType(final EEnum ee) {
    String _name = ee.getName();
    return _name;
  }
  
  public CharSequence feature(final Mapping mapping, final EStructuralFeature eat) {
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
  
  public CharSequence feature(final ENamedElement eat) {
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
  
  public CharSequence primitiveType(final EDataType ee) {
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
