package tom.mapping.dsl.generator;

import java.util.Arrays;
import java.util.List;
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
import tom.mapping.dsl.generator.NamingCompiler;
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
  
  private NamingCompiler _namingCompiler = new Function0<NamingCompiler>() {
    public NamingCompiler apply() {
      NamingCompiler _namingCompiler = new NamingCompiler();
      return _namingCompiler;
    }
  }.apply();
  
  private NamingCompiler nam;
  
  protected CharSequence _getter(final Mapping mapping, final EPackage ep) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private static class \u00AC\u00B4getChildrenGetterName(ep)\u00AC\u00AA extends \u00AC\u00B4ep.name.toFirstUpper()\u00AC\u00AASwitch<Object[]> implements IChildrenGetter{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public final static \u00AC\u00B4getChildrenGetterName(ep)\u00AC\u00AA INSTANCE = new \u00AC\u00B4getChildrenGetterName(ep)\u00AC\u00AA();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private \u00AC\u00B4getChildrenGetterName(ep)\u00AC\u00AA(){}");
    _builder.newLine();
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
    _builder.append("\t");
    _builder.append("\u00AC\u00B4for(EClassifier c: ep.EClassifiers) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("getter(mapping, c);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\u00AC\u00AA");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getter(final Mapping mapping, final EClassifier ecf) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _getter(final Mapping mapping, final EClass ec) {
    CharSequence _xblockexpression = null;
    {
      List<EReference> _defaultParameters = this._tomMappingExtensions.getDefaultParameters(ec, mapping);
      final List<EReference> parameters = _defaultParameters;
      CharSequence _xifexpression = null;
      int _size = parameters.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("public Object[] case\u00AC\u00B4ec.name.toFirstUpper()\u00AC\u00AA(\u00ACec.name\u00AC\u00AA o) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("List<Object> l = new ArrayList<Object>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\u00AC\u00B4for(EReference param: parameters)\u00AC\u00AA {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if(o.get\u00AC\u00B4param.name.toFirstUpper()\u00AC\u00AA() != null)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("l.add(o.get\u00AC\u00B4param.name.toFirstUpper()\u00AC\u00AA());");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return l.toArray();");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _xifexpression = _builder;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected CharSequence _setter(final Mapping mapping, final EPackage ep) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private static class \u00AC\u00B4getChildrenSetterName(ep)\u00AC\u00AA extends \u00AC\u00B4ep.name.toFirstUpper()\u00AC\u00AASwitch<Object[]> implements IChildrenSetter{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public final static \u00AC\u00B4getChildrenSetterName(ep)\u00AC\u00AA INSTANCE = new \u00AC\u00B4getChildrenSetterName(ep)\u00AC\u00AA();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private \u00AC\u00B4getChildrenSetterName(ep)\u00AC\u00AA(){}");
    _builder.newLine();
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
    _builder.append("\t");
    _builder.append("\u00AC\u00B4for(EClassifier c: ep.EClassifiers) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("setter(mapping, c);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\u00AC\u00AA");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence setter(final Mapping mapping, final EClassifier ecf) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
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
      final Iterable<EReference> parameters = _filter;
      CharSequence _xifexpression = null;
      int _size = IterableExtensions.size(parameters);
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("public Object[] case\u00AC\u00B4ec.name.toFirstUpper()\u00AC\u00AA(\u00ACec.name\u00AC\u00AA o) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\u00AC\u00B4for(EReference p: parameters)\u00AC\u00AA {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("o.set\u00AC\u00B4p.name.toFirstUpper()\u00AC\u00AA((\u00AC\u00B4p.EReferenceType.name\u00AC\u00AA)children[\u00AC\u00B4parameters.indexOf(p)\u00AC\u00AA]);");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return o;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _xifexpression = _builder;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public CharSequence getter(final Mapping mapping, final ENamedElement ec) {
    if (ec instanceof EClass) {
      return _getter(mapping, (EClass)ec);
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
    } else if (ec instanceof EPackage) {
      return _setter(mapping, (EPackage)ec);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(mapping, ec).toString());
    }
  }
}
