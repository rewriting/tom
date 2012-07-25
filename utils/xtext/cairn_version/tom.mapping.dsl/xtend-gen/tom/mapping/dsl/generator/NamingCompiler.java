package tom.mapping.dsl.generator;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.model.Accessor;
import tom.mapping.model.Mapping;
import tom.mapping.model.Parameter;
import tom.mapping.model.Terminal;
import tom.mapping.model.UserOperator;

@SuppressWarnings("all")
public class NamingCompiler {
  public String getCustomOperatorSlotAccessorName(final Accessor accessor) {
    EObject _eContainer = accessor.eContainer();
    String _name = ((UserOperator) _eContainer).getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = ("get" + _firstUpper);
    String _plus_1 = (_plus + "Slot");
    Parameter _slot = accessor.getSlot();
    String _name_1 = _slot.getName();
    String _upperCase = _name_1.toUpperCase();
    String _plus_2 = (_plus_1 + _upperCase);
    return _plus_2;
  }
  
  public String getCustomOperatorsClass(final Mapping mapping) {
    String _name = mapping.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = (_firstUpper + "CustomAccessors");
    return _plus;
  }
  
  public String getChildrenGetterName(final EPackage p) {
    String _name = p.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = (_firstUpper + "ChildrenGetter");
    return _plus;
  }
  
  public String getChildrenSetterName(final EPackage p) {
    String _name = p.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = (_firstUpper + "ChildrenSetter");
    return _plus;
  }
  
  public String name(final Terminal t, final Mapping m) {
    String _xifexpression = null;
    boolean _and = false;
    boolean _isMany = t.isMany();
    if (!_isMany) {
      _and = false;
    } else {
      Boolean _isInferedList = this.isInferedList(t, m);
      _and = (_isMany && (_isInferedList).booleanValue());
    }
    if (_and) {
      String _name = t.getName();
      String _plus = (_name + "List");
      _xifexpression = _plus;
    } else {
      String _name_1 = t.getName();
      _xifexpression = _name_1;
    }
    return _xifexpression;
  }
  
  public String factoryName(final Mapping m) {
    String _name = m.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = (_firstUpper + "UserFactory");
    return _plus;
  }
  
  public String tomFactoryName(final Mapping m) {
    String _name = m.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    String _plus = (_firstUpper + "TomFactory");
    return _plus;
  }
  
  public String tomFactoryQualifiedName(final Mapping m) {
    String _prefix = m.getPrefix();
    String _plus = (_prefix + ".");
    String _name = m.getName();
    String _plus_1 = (_plus + _name);
    String _plus_2 = (_plus_1 + ".");
    String _plus_3 = (_plus_2 + "internal");
    String _plus_4 = (_plus_3 + ".");
    String _mFactoryName = this.tomFactoryName(m);
    String _plus_5 = (_plus_4 + _mFactoryName);
    return _plus_5;
  }
  
  public String getPackagePrefix(final String prefix) {
    String _xifexpression = null;
    boolean _or = false;
    boolean _equals = Objects.equal(prefix, null);
    if (_equals) {
      _or = true;
    } else {
      int _compareTo = prefix.compareTo("");
      boolean _equals_1 = (_compareTo == 0);
      _or = (_equals || _equals_1);
    }
    if (_or) {
      _xifexpression = "";
    } else {
      String _plus = (prefix + ".");
      _xifexpression = _plus;
    }
    return _xifexpression;
  }
  
  private Boolean isInferedList(final Terminal t, final Mapping m) {
    boolean _and = false;
    boolean _isMany = t.isMany();
    if (!_isMany) {
      _and = false;
    } else {
      boolean _or = false;
      EList<Terminal> _terminals = m.getTerminals();
      boolean _contains = _terminals.contains(t);
      if (_contains) {
        _or = true;
      } else {
        EList<Terminal> _externalTerminals = m.getExternalTerminals();
        boolean _contains_1 = _externalTerminals.contains(t);
        _or = (_contains || _contains_1);
      }
      boolean _not = (!_or);
      _and = (_isMany && _not);
    }
    return Boolean.valueOf(_and);
  }
}
