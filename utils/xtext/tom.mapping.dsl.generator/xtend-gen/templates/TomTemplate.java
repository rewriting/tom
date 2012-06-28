package templates;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class TomTemplate {
  public static CharSequence main(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%include { string.tom }");
    _builder.newLine();
    _builder.append("%include { boolean.tom }");
    _builder.newLine();
    _builder.append("%include { int.tom }");
    _builder.newLine();
    _builder.append("%include { long.tom }");
    _builder.newLine();
    _builder.append("%include { float.tom }");
    _builder.newLine();
    _builder.append("%include { double.tom }");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private static <O> EList<O> enforce(EList l) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return l;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("private static <O> EList<O> append(O e,EList<O> l) {");
    _builder.newLine();
    _builder.append("       ");
    _builder.append("l.add(e);");
    _builder.newLine();
    _builder.append("       ");
    _builder.append("return l;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected Object _primitiveTerminal(final EEnum e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%typeterm ");
    String _name = e.getName();
    _builder.append(_name, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("implement \t\t{");
    String _name_1 = e.getName();
    _builder.append(_name_1, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("is_sort(t) \t\t{$t instanceof ");
    String _name_2 = e.getName();
    _builder.append(_name_2, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("equals(l1,l2) \t{$l1==$l2}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected Object _primitiveTerminal(final EClassifier e) {
    return null;
  }
  
  protected Object _primitiveTerminal(final EPackage e) {
    List<Object> _xblockexpression = null;
    {
      EList<EClassifier> _eClassifiers = e.getEClassifiers();
      final Function1<EClassifier,Object> _function = new Function1<EClassifier,Object>() {
          public Object apply(final EClassifier c) {
            Object _primitiveTerminal = TomTemplate.this.primitiveTerminal(c);
            return _primitiveTerminal;
          }
        };
      ListExtensions.<EClassifier, Object>map(_eClassifiers, _function);
      EList<EPackage> _eSubpackages = e.getESubpackages();
      final Function1<EPackage,Object> _function_1 = new Function1<EPackage,Object>() {
          public Object apply(final EPackage c) {
            Object _primitiveTerminal = TomTemplate.this.primitiveTerminal(c);
            return _primitiveTerminal;
          }
        };
      List<Object> _map = ListExtensions.<EPackage, Object>map(_eSubpackages, _function_1);
      _xblockexpression = (_map);
    }
    return _xblockexpression;
  }
  
  public CharSequence termFile() {
    CharSequence _xblockexpression = null;
    {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("// Primitive terminals (enum and data types)");
      _builder.newLine();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("// Terminals");
      _builder_1.newLine();
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("// List Terminals");
      _builder_2.newLine();
      _xblockexpression = (_builder_2);
    }
    return _xblockexpression;
  }
  
  public Object primitiveTerminal(final ENamedElement e) {
    if (e instanceof EEnum) {
      return _primitiveTerminal((EEnum)e);
    } else if (e instanceof EClassifier) {
      return _primitiveTerminal((EClassifier)e);
    } else if (e instanceof EPackage) {
      return _primitiveTerminal((EPackage)e);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(e).toString());
    }
  }
}
