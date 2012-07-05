package tom.mapping.dsl.generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
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
    String _operator_plus = StringExtensions.operator_plus("get", _firstUpper);
    String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, "Slot");
    Parameter _slot = accessor.getSlot();
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
    String _xifexpression = null;
    boolean _operator_and = false;
    boolean _isMany = t.isMany();
    if (!_isMany) {
      _operator_and = false;
    } else {
      Boolean _isInferedList = this.isInferedList(t, m);
      _operator_and = BooleanExtensions.operator_and(_isMany, (_isInferedList).booleanValue());
    }
    if (_operator_and) {
      String _name = t.getName();
      String _operator_plus = StringExtensions.operator_plus(_name, "List");
      _xifexpression = _operator_plus;
    } else {
      String _name_1 = t.getName();
      _xifexpression = _name_1;
    }
    return _xifexpression;
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
    String _xifexpression = null;
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
      _xifexpression = "";
    } else {
      String _operator_plus = StringExtensions.operator_plus(prefix, ".");
      _xifexpression = _operator_plus;
    }
    return _xifexpression;
  }
  
  private Boolean isInferedList(final Terminal t, final Mapping m) {
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
    return Boolean.valueOf(_operator_and);
  }
}
