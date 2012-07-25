package tom.mapping.dsl.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.model.Accessor;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.FeatureException;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.Parameter;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.Terminal;
import tom.mapping.model.UserOperator;

@SuppressWarnings("all")
public class TomMappingExtensions {
  public String getCustomOperatorSlotAccessorName(final Accessor a) {
    String _name = ((UserOperator) a).getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus("get", _firstUpper);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, "Slot");
    Parameter _slot = a.getSlot();
    String _name_1 = _slot.getName();
    String _upperCase = _name_1.toUpperCase();
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, _upperCase);
    return _operator_plus_2;
  }
  
  public String getCustomOperatorsClass(final Mapping mapping) {
    String _name = mapping.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus(_firstUpper, "CustomAccessors");
    return _operator_plus;
  }
  
  public String getChildrenGetterName(final EPackage p) {
    String _name = p.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus(_firstUpper, "ChildrenGetter");
    return _operator_plus;
  }
  
  public String getChildrenSetterName(final EPackage p) {
    String _name = p.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus(_firstUpper, "ChildrenSetter");
    return _operator_plus;
  }
  
  public String name(final Terminal t, final Mapping m) {
    boolean _operator_and = false;
    boolean _isMany = t.isMany();
    if (!_isMany) {
      _operator_and = false;
    } else {
      boolean _isInferedList = this.isInferedList(t, m);
      _operator_and = BooleanExtensions.operator_and(_isMany, _isInferedList);
    }
    if (_operator_and) {
      String _name = t.getName();
      String _operator_plus = StringExtensions.operator_plus(_name, "List");
      return _operator_plus;
    } else {
      String _name_1 = t.getName();
      return _name_1;
    }
  }
  
  public boolean isInferedList(final Terminal t, final Mapping m) {
    boolean _operator_and = false;
    boolean _isMany = t.isMany();
    if (!_isMany) {
      _operator_and = false;
    } else {
      boolean _operator_or = false;
      EList<Terminal> _terminals = m.getTerminals();
      boolean _contains = _terminals.contains(t);
      if (_contains) {
        _operator_or = true;
      } else {
        EList<Terminal> _externalTerminals = m.getExternalTerminals();
        boolean _contains_1 = _externalTerminals.contains(t);
        _operator_or = BooleanExtensions.operator_or(_contains, _contains_1);
      }
      boolean _operator_not = BooleanExtensions.operator_not(_operator_or);
      _operator_and = BooleanExtensions.operator_and(_isMany, _operator_not);
    }
    return _operator_and;
  }
  
  public String factoryName(final Mapping m) {
    String _name = m.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus(_firstUpper, "UserFactory");
    return _operator_plus;
  }
  
  public String tomFactoryName(final Mapping m) {
    String _name = m.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _operator_plus = StringExtensions.operator_plus(_firstUpper, "TomFactory");
    return _operator_plus;
  }
  
  public String tomFactoryQualifiedName(final Mapping m) {
    String _prefix = m.getPrefix();
    String _operator_plus = StringExtensions.operator_plus(_prefix, ".");
    String _name = m.getName();
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, _name);
    String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, ".");
    String _operator_plus_3 = StringExtensions.operator_plus(_operator_plus_2, "internal");
    String _operator_plus_4 = StringExtensions.operator_plus(_operator_plus_3, ".");
    String _mFactoryName = this.tomFactoryName(m);
    String _operator_plus_5 = StringExtensions.operator_plus(_operator_plus_4, _mFactoryName);
    return _operator_plus_5;
  }
  
  public String getPackagePrefix(final String prefix) {
    boolean _operator_or = false;
    boolean _operator_equals = ObjectExtensions.operator_equals(prefix, null);
    if (_operator_equals) {
      _operator_or = true;
    } else {
      int _compareTo = prefix.compareTo("");
      boolean _operator_equals_1 = IntegerExtensions.operator_equals(_compareTo, 0);
      _operator_or = BooleanExtensions.operator_or(_operator_equals, _operator_equals_1);
    }
    if (_operator_or) {
      return "";
    } else {
      String _operator_plus = StringExtensions.operator_plus(prefix, ".");
      return _operator_plus;
    }
  }
  
  public List<EReference> getDefaultParameters(final EClass c, final Mapping mapping) {
    EList<EReference> _eAllReferences = c.getEAllReferences();
    final Function1<EReference,Boolean> _function = new Function1<EReference,Boolean>() {
        public Boolean apply(final EReference e) {
          boolean _isParameterFeature = TomMappingExtensions.this.isParameterFeature(e, mapping);
          return Boolean.valueOf(_isParameterFeature);
        }
      };
    Iterable<EReference> _filter = IterableExtensions.<EReference>filter(_eAllReferences, _function);
    List<EReference> _list = IterableExtensions.<EReference>toList(_filter);
    return _list;
  }
  
  public Iterable<FeatureParameter> getCustomParameters(final ClassOperator c) {
    EList<FeatureParameter> _parameters = c.getParameters();
    final Function1<FeatureParameter,Boolean> _function = new Function1<FeatureParameter,Boolean>() {
        public Boolean apply(final FeatureParameter e) {
          boolean _isExplicitParameter = TomMappingExtensions.this.isExplicitParameter(e);
          return Boolean.valueOf(_isExplicitParameter);
        }
      };
    Iterable<FeatureParameter> _filter = IterableExtensions.<FeatureParameter>filter(_parameters, _function);
    return _filter;
  }
  
  public List<SettedFeatureParameter> getSettedCustomParameters(final ClassOperator c) {
    EList<FeatureParameter> _parameters = c.getParameters();
    Iterable<SettedFeatureParameter> _filter = IterableExtensions.<SettedFeatureParameter>filter(_parameters, tom.mapping.model.SettedFeatureParameter.class);
    List<SettedFeatureParameter> _list = IterableExtensions.<SettedFeatureParameter>toList(_filter);
    return _list;
  }
  
  private boolean _isParameterFeature(final EStructuralFeature f, final Mapping mapping) {
    return true;
  }
  
  private boolean _isParameterFeature(final EReference f, final Mapping mapping) {
    boolean _operator_and = false;
    boolean _operator_and_1 = false;
    boolean _operator_and_2 = false;
    boolean _isDerived = f.isDerived();
    boolean _operator_equals = BooleanExtensions.operator_equals(_isDerived, false);
    if (!_operator_equals) {
      _operator_and_2 = false;
    } else {
      boolean _isVolatile = f.isVolatile();
      boolean _operator_equals_1 = BooleanExtensions.operator_equals(_isVolatile, false);
      _operator_and_2 = BooleanExtensions.operator_and(_operator_equals, _operator_equals_1);
    }
    if (!_operator_and_2) {
      _operator_and_1 = false;
    } else {
      boolean _operator_or = false;
      boolean _isContainment = f.isContainment();
      if (_isContainment) {
        _operator_or = true;
      } else {
        EReference _eOpposite = f.getEOpposite();
        boolean _operator_equals_2 = ObjectExtensions.operator_equals(_eOpposite, null);
        _operator_or = BooleanExtensions.operator_or(_isContainment, _operator_equals_2);
      }
      _operator_and_1 = BooleanExtensions.operator_and(_operator_and_2, _operator_or);
    }
    if (!_operator_and_1) {
      _operator_and = false;
    } else {
      boolean _operator_or_1 = false;
      EClass _eReferenceType = f.getEReferenceType();
      Terminal _terminal = mapping.getTerminal(_eReferenceType, false);
      boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_terminal, null);
      if (_operator_notEquals) {
        _operator_or_1 = true;
      } else {
        EClass _eReferenceType_1 = f.getEReferenceType();
        Terminal _terminal_1 = mapping.getTerminal(_eReferenceType_1, true);
        boolean _operator_notEquals_1 = ObjectExtensions.operator_notEquals(_terminal_1, null);
        _operator_or_1 = BooleanExtensions.operator_or(_operator_notEquals, _operator_notEquals_1);
      }
      _operator_and = BooleanExtensions.operator_and(_operator_and_1, _operator_or_1);
    }
    return _operator_and;
  }
  
  private boolean _isExplicitParameter(final FeatureParameter pf) {
    return true;
  }
  
  private boolean _isExplicitParameter(final SettedFeatureParameter pf) {
    return false;
  }
  
  private boolean _isExplicitParameter(final FeatureException pf) {
    return false;
  }
  
  private List<EPackage> getAllSubPackages(final EPackage p) {
    TreeIterator<EObject> _eAllContents = p.eAllContents();
    Iterator<EPackage> _filter = IteratorExtensions.<EPackage>filter(_eAllContents, org.eclipse.emf.ecore.EPackage.class);
    List<EPackage> _list = IteratorExtensions.<EPackage>toList(_filter);
    return _list;
  }
  
  public ArrayList<EPackage> getAllPackages(final Mapping m) {
      ArrayList<EPackage> _arrayList = new ArrayList<EPackage>();
      final ArrayList<EPackage> selected = _arrayList;
      EList<EPackage> _metamodelPackages = m.getMetamodelPackages();
      for (final EPackage mp : _metamodelPackages) {
        List<EPackage> _allSubPackages = this.getAllSubPackages(mp);
        final Function1<EPackage,Boolean> _function = new Function1<EPackage,Boolean>() {
            public Boolean apply(final EPackage e) {
              boolean _isSelected = TomMappingExtensions.this.isSelected(e, m);
              return Boolean.valueOf(_isSelected);
            }
          };
        Iterable<EPackage> _filter = IterableExtensions.<EPackage>filter(_allSubPackages, _function);
        CollectionExtensions.<EPackage>addAll(selected, _filter);
      }
      return selected;
  }
  
  public boolean isSelected(final EPackage p, final Mapping m) {
    EList<EClassifier> _eClassifiers = p.getEClassifiers();
    Iterable<EClass> _filter = IterableExtensions.<EClass>filter(_eClassifiers, org.eclipse.emf.ecore.EClass.class);
    final Function1<EClass,Boolean> _function = new Function1<EClass,Boolean>() {
        public Boolean apply(final EClass e) {
          Terminal _terminal = m.getTerminal(((EClass) e), false);
          boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_terminal, null);
          return Boolean.valueOf(_operator_notEquals);
        }
      };
    Iterable<EClass> _filter_1 = IterableExtensions.<EClass>filter(_filter, _function);
    int _size = IterableExtensions.size(_filter_1);
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    return _operator_greaterThan;
  }
  
  public String packageToPath(final String s) {
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(s, null);
    if (_operator_notEquals) {
      String _replaceAll = s.replaceAll("\\.", "/");
      return _replaceAll;
    } else {
      return "";
    }
  }
  
  public EList<EPackage> getAllRootPackages(final Mapping mapping) {
    EList<EPackage> _metamodelPackages = mapping.getMetamodelPackages();
    return _metamodelPackages;
  }
  
  public boolean isPrimitive(final String type) {
    boolean _operator_or = false;
    boolean _operator_or_1 = false;
    boolean _operator_or_2 = false;
    boolean _operator_or_3 = false;
    boolean _operator_or_4 = false;
    int _compareTo = type.compareTo("int");
    boolean _operator_equals = IntegerExtensions.operator_equals(_compareTo, 0);
    if (_operator_equals) {
      _operator_or_4 = true;
    } else {
      int _compareTo_1 = type.compareTo("long");
      boolean _operator_equals_1 = IntegerExtensions.operator_equals(_compareTo_1, 0);
      _operator_or_4 = BooleanExtensions.operator_or(_operator_equals, _operator_equals_1);
    }
    if (_operator_or_4) {
      _operator_or_3 = true;
    } else {
      int _compareTo_2 = type.compareTo("float");
      boolean _operator_equals_2 = IntegerExtensions.operator_equals(_compareTo_2, 0);
      _operator_or_3 = BooleanExtensions.operator_or(_operator_or_4, _operator_equals_2);
    }
    if (_operator_or_3) {
      _operator_or_2 = true;
    } else {
      int _compareTo_3 = type.compareTo("double");
      boolean _operator_equals_3 = IntegerExtensions.operator_equals(_compareTo_3, 0);
      _operator_or_2 = BooleanExtensions.operator_or(_operator_or_3, _operator_equals_3);
    }
    if (_operator_or_2) {
      _operator_or_1 = true;
    } else {
      int _compareTo_4 = type.compareTo("boolean");
      boolean _operator_equals_4 = IntegerExtensions.operator_equals(_compareTo_4, 0);
      _operator_or_1 = BooleanExtensions.operator_or(_operator_or_2, _operator_equals_4);
    }
    if (_operator_or_1) {
      _operator_or = true;
    } else {
      int _compareTo_5 = type.compareTo("char");
      boolean _operator_equals_5 = IntegerExtensions.operator_equals(_compareTo_5, 0);
      _operator_or = BooleanExtensions.operator_or(_operator_or_1, _operator_equals_5);
    }
    return _operator_or;
  }
  
  private boolean isParameterFeature(final EStructuralFeature f, final Mapping mapping) {
    if (f instanceof EReference) {
      return _isParameterFeature((EReference)f, mapping);
    } else if (f != null) {
      return _isParameterFeature(f, mapping);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(f, mapping).toString());
    }
  }
  
  private boolean isExplicitParameter(final FeatureParameter pf) {
    if (pf instanceof FeatureException) {
      return _isExplicitParameter((FeatureException)pf);
    } else if (pf instanceof SettedFeatureParameter) {
      return _isExplicitParameter((SettedFeatureParameter)pf);
    } else if (pf != null) {
      return _isExplicitParameter(pf);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(pf).toString());
    }
  }
}
