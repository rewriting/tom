package tom.mapping.dsl.introspector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.BooleanExtensions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class ChildrenGetterSetter {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  protected CharSequence _getter(final Mapping mapping, final EPackage ep) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private static class ");
    String _childrenGetterName = this._tomMappingExtensions.getChildrenGetterName(ep);
    _builder.append(_childrenGetterName, "");
    _builder.append(" extends ");
    String _name = ep.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    _builder.append(_firstUpper, "");
    _builder.append("Switch<Object[]> implements IChildrenGetter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("public final static ");
    String _childrenGetterName_1 = this._tomMappingExtensions.getChildrenGetterName(ep);
    _builder.append(_childrenGetterName_1, "	");
    _builder.append(" INSTANCE = new ");
    String _childrenGetterName_2 = this._tomMappingExtensions.getChildrenGetterName(ep);
    _builder.append(_childrenGetterName_2, "	");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    String _childrenGetterName_3 = this._tomMappingExtensions.getChildrenGetterName(ep);
    _builder.append(_childrenGetterName_3, "	");
    _builder.append("(){}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public Object[] children(Object i) {\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("Object[] children = doSwitch((EObject) i);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if(children !=null) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return children");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("} else { return new Object[0]; }");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      for(final EClassifier c : _eClassifiers) {
        _builder.append("\t");
        CharSequence _ter = this.getter(mapping, c);
        _builder.append(_ter, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _getter(final Mapping mapping, final EClassifier ecf) {
    return null;
  }
  
  protected CharSequence _getter(final Mapping mapping, final EClass ec) {
    CharSequence _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ec, mapping);
      final List<EReference> parameters = _defaultParameters;
      StringConcatenation _builder = new StringConcatenation();
      {
        int _size = parameters.size();
        boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
        if (_operator_greaterThan) {
          _builder.append("public Object[] case");
          String _name = ec.getName();
          String _firstUpper = StringExtensions.toFirstUpper(_name);
          _builder.append(_firstUpper, "");
          _builder.append("(");
          String _name_1 = ec.getName();
          _builder.append(_name_1, "");
          _builder.append(" o) {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("List<Object> l = new ArrayList<Object>();");
          _builder.newLine();
          {
            for(final EReference param : parameters) {
              _builder.append("\t");
              _builder.append("if(o.get");
              String _name_2 = param.getName();
              String _firstUpper_1 = StringExtensions.toFirstUpper(_name_2);
              _builder.append(_firstUpper_1, "	");
              _builder.append("() != null) { ");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("l.add(o.get");
              String _name_3 = param.getName();
              String _firstUpper_2 = StringExtensions.toFirstUpper(_name_3);
              _builder.append(_firstUpper_2, "		");
              _builder.append("());");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
            }
          }
          _builder.append("\t");
          _builder.append("return l.toArray();");
          _builder.newLine();
          _builder.append("\t\t");
        }
      }
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _setter(final Mapping mapping, final EPackage ep) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private static class ");
    String _childrenSetterName = this._tomMappingExtensions.getChildrenSetterName(ep);
    _builder.append(_childrenSetterName, "");
    _builder.append(" extends ");
    String _name = ep.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    _builder.append(_firstUpper, "");
    _builder.append("Switch<Object[]> implements IChildrenSetter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("public final static ");
    String _childrenSetterName_1 = this._tomMappingExtensions.getChildrenSetterName(ep);
    _builder.append(_childrenSetterName_1, "	");
    _builder.append(" INSTANCE = new ");
    String _childrenSetterName_2 = this._tomMappingExtensions.getChildrenSetterName(ep);
    _builder.append(_childrenSetterName_2, "	");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    String _childrenSetterName_3 = this._tomMappingExtensions.getChildrenSetterName(ep);
    _builder.append(_childrenSetterName_3, "	");
    _builder.append("(){}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public Object set(Object i, Object[] children) {\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ep.children = children;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return doSwitch((EObject) i);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      for(final EClassifier c : _eClassifiers) {
        _builder.append("\t");
        CharSequence _setter = this.setter(mapping, c);
        _builder.append(_setter, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _setter(final Mapping mapping, final EClassifier ecf) {
    return null;
  }
  
  protected CharSequence _setter(final Mapping mapping, final EClass ec) {
    CharSequence _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ec, mapping);
      final Function1<EReference,Boolean> _function = new Function1<EReference,Boolean>() {
          public Boolean apply(final EReference e) {
            boolean _isMany = e.isMany();
            boolean _operator_not = BooleanExtensions.operator_not(_isMany);
            return Boolean.valueOf(_operator_not);
          }
        };
      Iterable<EReference> _filter = IterableExtensions.<EReference>filter(_defaultParameters, _function);
      final ArrayList<EReference> parameters = ((ArrayList<EReference>) _filter);
      StringConcatenation _builder = new StringConcatenation();
      {
        int _size = parameters.size();
        boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
        if (_operator_greaterThan) {
          _builder.append("public Object[] case");
          String _name = ec.getName();
          String _firstUpper = StringExtensions.toFirstUpper(_name);
          _builder.append(_firstUpper, "");
          _builder.append("(");
          String _name_1 = ec.getName();
          _builder.append(_name_1, "");
          _builder.append(" o) { ");
          _builder.newLineIfNotEmpty();
          {
            for(final EReference p : parameters) {
              _builder.append("\t");
              _builder.append("o.set");
              String _name_2 = p.getName();
              String _firstUpper_1 = StringExtensions.toFirstUpper(_name_2);
              _builder.append(_firstUpper_1, "	");
              _builder.append("((");
              EClass _eReferenceType = p.getEReferenceType();
              String _name_3 = _eReferenceType.getName();
              _builder.append(_name_3, "	");
              _builder.append(")children[");
              int _indexOf = parameters.indexOf(p);
              _builder.append(_indexOf, "	");
              _builder.append("]);");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("\t");
          _builder.append("return o;");
          _builder.newLine();
        }
      }
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence getter(final Mapping mapping, final ENamedElement ec) {
    if (ec instanceof EClass) {
      return _getter(mapping, (EClass)ec);
    } else if (ec instanceof EClassifier) {
      return _getter(mapping, (EClassifier)ec);
    } else if (ec instanceof EPackage) {
      return _getter(mapping, (EPackage)ec);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, ec).toString());
    }
  }
  
  public CharSequence setter(final Mapping mapping, final ENamedElement ec) {
    if (ec instanceof EClass) {
      return _setter(mapping, (EClass)ec);
    } else if (ec instanceof EClassifier) {
      return _setter(mapping, (EClassifier)ec);
    } else if (ec instanceof EPackage) {
      return _setter(mapping, (EPackage)ec);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, ec).toString());
    }
  }
}
