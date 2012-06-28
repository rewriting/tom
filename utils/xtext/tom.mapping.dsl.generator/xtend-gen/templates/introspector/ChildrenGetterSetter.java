package templates.introspector;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import templates.Extensions;
import templates.Naming;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class ChildrenGetterSetter {
  private Naming a;
  
  public CharSequence getter(final EPackage p, final Mapping mapping) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private static class ");
    String _childrenGetterName = Naming.getChildrenGetterName(p);
    _builder.append(_childrenGetterName, "");
    _builder.append(" extends ");
    String _name = p.getName();
    String _firstUpper = StringExtensions.toFirstUpper(_name);
    _builder.append(_firstUpper, "");
    _builder.append("Switch<Object[]> implements IChildrenGetter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("public final static ");
    String _childrenGetterName_1 = Naming.getChildrenGetterName(p);
    _builder.append(_childrenGetterName_1, "		");
    _builder.append(" INSTANCE = new ");
    String _childrenGetterName_2 = Naming.getChildrenGetterName(p);
    _builder.append(_childrenGetterName_2, "		");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("private ");
    String _childrenGetterName_3 = Naming.getChildrenGetterName(p);
    _builder.append(_childrenGetterName_3, "		");
    _builder.append("(){}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public Object[] children(Object i) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("Object[] children = doSwitch((EObject) i);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return children!=null ? children: new Object[0];");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    {
      EList<EClassifier> _eClassifiers = p.getEClassifiers();
      for(final EClassifier c : _eClassifiers) {
        CharSequence _ter = this.getter(c, mapping);
        _builder.append(_ter, "		");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getter(final EClassifier c, final Mapping mapping) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence getter(final EClass c, final Mapping mapping) {
    CharSequence _xblockexpression = null;
    {
      Extensions _extensions = new Extensions();
      final Extensions hack = _extensions;
      Iterable<EReference> _defaultParameters = hack.getDefaultParameters(c, mapping);
      final Iterable<EReference> parameters = _defaultParameters;
      CharSequence _xifexpression = null;
      int _size = IterableExtensions.size(parameters);
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("public Object[] case");
        String _name = c.getName();
        String _firstUpper = StringExtensions.toFirstUpper(_name);
        _builder.append(_firstUpper, "");
        _builder.append("(");
        String _name_1 = c.getName();
        _builder.append(_name_1, "");
        _builder.append(" o){");
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
            _builder.append("() !=null)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("l.add(o.get");
            String _name_3 = param.getName();
            String _firstUpper_2 = StringExtensions.toFirstUpper(_name_3);
            _builder.append(_firstUpper_2, "		");
            _builder.append("());");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("return l.toArray();");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _xifexpression = _builder;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
}
