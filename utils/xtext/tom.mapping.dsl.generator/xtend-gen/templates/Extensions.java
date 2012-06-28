package templates;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.FeatureException;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class Extensions {
  public Iterable<EReference> getDefaultParameters(final EClass c, final Mapping mapping) {
    EList<EReference> _eAllReferences = c.getEAllReferences();
    final Function1<EReference,Boolean> _function = new Function1<EReference,Boolean>() {
        public Boolean apply(final EReference e) {
          boolean _isParameterFeature = Extensions.this.isParameterFeature(e, mapping);
          return Boolean.valueOf(_isParameterFeature);
        }
      };
    Iterable<EReference> _filter = IterableExtensions.<EReference>filter(_eAllReferences, _function);
    return _filter;
  }
  
  public Iterable<FeatureParameter> getCustomParameters(final ClassOperator c) {
    EList<FeatureParameter> _parameters = c.getParameters();
    final Function1<FeatureParameter,Boolean> _function = new Function1<FeatureParameter,Boolean>() {
        public Boolean apply(final FeatureParameter e) {
          boolean _isExplicitParameter = Extensions.this.isExplicitParameter(e);
          return Boolean.valueOf(_isExplicitParameter);
        }
      };
    Iterable<FeatureParameter> _filter = IterableExtensions.<FeatureParameter>filter(_parameters, _function);
    return _filter;
  }
  
  public Iterable<SettedFeatureParameter> getSettedCustomParameters(final ClassOperator c) {
    EList<FeatureParameter> _parameters = c.getParameters();
    final Function1<FeatureParameter,Boolean> _function = new Function1<FeatureParameter,Boolean>() {
        public Boolean apply(final FeatureParameter e) {
          return Boolean.valueOf((e instanceof SettedFeatureParameter));
        }
      };
    Iterable<FeatureParameter> _filter = IterableExtensions.<FeatureParameter>filter(_parameters, _function);
    return ((Iterable<SettedFeatureParameter>) _filter);
  }
  
  public boolean isParameterFeature(final EStructuralFeature f, final Mapping mapping) {
    return true;
  }
  
  public boolean isParameterFeature(final EReference f, final Mapping mapping) {
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
  
  protected boolean _isExplicitParameter(final FeatureParameter pf) {
    return true;
  }
  
  protected boolean _isExplicitParameter(final SettedFeatureParameter pf) {
    return false;
  }
  
  protected boolean _isExplicitParameter(final FeatureException pf) {
    return false;
  }
  
  public Iterable<EPackage> getAllSubPackages(final EPackage p) {
      EList<EPackage> _eSubpackages = p.getESubpackages();
      final Function1<EPackage,Iterable<EPackage>> _function = new Function1<EPackage,Iterable<EPackage>>() {
          public Iterable<EPackage> apply(final EPackage e) {
            Iterable<EPackage> _allSubPackages = Extensions.this.getAllSubPackages(e);
            return _allSubPackages;
          }
        };
      List<Iterable<EPackage>> _map = ListExtensions.<EPackage, Iterable<EPackage>>map(_eSubpackages, _function);
      final List<Iterable<EPackage>> f = _map;
      Iterable<EPackage> _flatten = IterableExtensions.<EPackage>flatten(f);
      return _flatten;
  }
  
  public boolean isSelected(final EPackage p, final Mapping m) {
    EList<EClassifier> _eClassifiers = p.getEClassifiers();
    final Function1<EClassifier,Boolean> _function = new Function1<EClassifier,Boolean>() {
        public Boolean apply(final EClassifier e) {
          return Boolean.valueOf((e instanceof EClass));
        }
      };
    Iterable<EClassifier> _filter = IterableExtensions.<EClassifier>filter(_eClassifiers, _function);
    final Function1<EClassifier,Boolean> _function_1 = new Function1<EClassifier,Boolean>() {
        public Boolean apply(final EClassifier e) {
          Terminal _terminal = m.getTerminal(((EClass) e), false);
          boolean _operator_notEquals = ObjectExtensions.operator_notEquals(_terminal, null);
          return Boolean.valueOf(_operator_notEquals);
        }
      };
    Iterable<EClassifier> _filter_1 = IterableExtensions.<EClassifier>filter(_filter, _function_1);
    int _size = IterableExtensions.size(_filter_1);
    boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
    return _operator_greaterThan;
  }
  
  public Iterable<EPackage> getAllPackages(final Mapping m) {
    EList<EPackage> _metamodelPackages = m.getMetamodelPackages();
    final Function1<EPackage,Iterable<EPackage>> _function = new Function1<EPackage,Iterable<EPackage>>() {
        public Iterable<EPackage> apply(final EPackage e) {
          Iterable<EPackage> _allSubPackages = Extensions.this.getAllSubPackages(e);
          return _allSubPackages;
        }
      };
    List<Iterable<EPackage>> _map = ListExtensions.<EPackage, Iterable<EPackage>>map(_metamodelPackages, _function);
    Iterable<EPackage> _flatten = IterableExtensions.<EPackage>flatten(_map);
    final Function1<EPackage,Boolean> _function_1 = new Function1<EPackage,Boolean>() {
        public Boolean apply(final EPackage e) {
          boolean _isSelected = Extensions.this.isSelected(e, m);
          return Boolean.valueOf(_isSelected);
        }
      };
    Iterable<EPackage> _filter = IterableExtensions.<EPackage>filter(_flatten, _function_1);
    return _filter;
  }
  
  public String packageToPath(final String s) {
    String _xifexpression = null;
    boolean _operator_notEquals = ObjectExtensions.operator_notEquals(s, null);
    if (_operator_notEquals) {
      String _replaceAll = s.replaceAll("\\.", "/");
      _xifexpression = _replaceAll;
    } else {
      _xifexpression = "";
    }
    return _xifexpression;
  }
  
  public List<EPackage> getAllRootPackages(final Mapping mapping) {
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
  
  public boolean isExplicitParameter(final FeatureParameter pf) {
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
