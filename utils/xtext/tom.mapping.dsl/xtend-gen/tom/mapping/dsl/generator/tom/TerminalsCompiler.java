package tom.mapping.dsl.generator.tom;

import java.util.Arrays;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.model.Mapping;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class TerminalsCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  public CharSequence terminal(final Mapping m, final Terminal t) {
    CharSequence _xifexpression = null;
    boolean _isMany = t.isMany();
    if (_isMany) {
      CharSequence _listTerminal = this.listTerminal(m, t);
      _xifexpression = _listTerminal;
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("%typeterm \u00AC\u00B4t.name\u00AC\u00AA {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("implement \t\t{\u00AC\u00B4t.class_.name\u00AC\u00AA}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("is_sort(t) \t\t{$t instanceof \u00AC\u00B4t.class_.name\u00AC\u00AA}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("equals(l1,l2) \t{($l1!=null && $l1.equals($l2)) || $l1==$l2}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder;
    }
    return _xifexpression;
  }
  
  public CharSequence listTerminal(final Mapping m, final Terminal t) {
    CharSequence _xblockexpression = null;
    {
      String _name = this._tomMappingExtensions.name(t, m);
      final String name = _name;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("%typeterm \u00AC\u00B4name\u00AC\u00AA{");
      _builder.newLine();
      _builder.append("\t ");
      _builder.append("implement { EList<\u00AC\u00B4t.class_.name\u00AC\u00AA> }");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("is_sort(t) \u00AC\u00B4listTest (t)\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("equals(l1,l2) \t{($l1!=null && $l1.equals($l2)) || $l1==$l2}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("%oparray \u00AC\u00B4name\u00AC\u00AA \u00AC\u00B4name\u00AC\u00AA (\u00AC\u00B4m.getTerminal(t.class_,false).name\u00AC\u00AA*) {");
      _builder.newLine();
      _builder.append(" \t ");
      _builder.append("is_fsym(t) \u00AC\u00B4listTest(t)\u00AC\u00AA");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("make_empty(n) { new BasicEList<\u00AC\u00B4t.class_.name\u00AC\u00AA>($n) }");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("make_append(e,l) { append($e,$l) }");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("get_element(l,n) { $l.get($n) }");
      _builder.newLine();
      _builder.append("     ");
      _builder.append("get_size(l)      { $l.size() }");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence listTest(final Terminal t) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{ $t instanceof EList<?> &&");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("(((EList<\u00AC\u00B4t.class_.name\u00AC\u00AA>)$t).size() == 0 ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("|| (((EList<\u00AC\u00B4t.class_.name\u00AC\u00AA>)$t).size()>0 && ((EList<\u00AC\u00B4t.class_.name\u00AC\u00AA>)$t).get(0) instanceof \u00AC\u00B4t.class_.name\u00AC\u00AA))} ");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EPackage c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00AC\u00B4FOR classifier :c.eAllContents.filter(typeof(EClassifier)).toList\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\u00AC\u00B4classifier.primitiveTerminal\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4ENDFOR\u00AC\u00AA");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EClassifier c) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EEnum c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%typeterm \u00AC\u00B4c.name\u00AC\u00AA {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("implement \t\t{\u00AC\u00B4c.name\u00AC\u00AA}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("is_sort(t) \t\t{$t instanceof \u00AC\u00B4c.name\u00AC\u00AA}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("equals(l1,l2) \t{$l1==$l2}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EDataType c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\u00AC\u00B4val primitive = c.instanceTypeName.primitive\u00AC\u00AA");
    _builder.newLine();
    _builder.append("%typeterm \u00AC\u00B4c.name\u00AC\u00AA {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("implement \t\t{\u00AC\u00B4c.instanceTypeName\u00AC\u00AA}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("is_sort(t) \t\t{\u00AC\u00B4IF primitive\u00AC\u00AAtrue\u00AC\u00B4ELSE\u00AC\u00AA$t instanceof \u00AC\u00B4c.instanceTypeName\u00AC\u00AA\u00AC\u00B4ENDIF\u00AC\u00AA}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("equals(l1,l2) \t{\u00AC\u00B4IF primitive\u00AC\u00AA$l1==$l2\u00AC\u00B4ELSE\u00AC\u00AA$l1.equals($l2)\u00AC\u00B4ENDIF\u00AC\u00AA}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence primitiveTerminal(final ENamedElement c) {
    if (c instanceof EEnum) {
      return _primitiveTerminal((EEnum)c);
    } else if (c instanceof EDataType) {
      return _primitiveTerminal((EDataType)c);
    } else if (c instanceof EClassifier) {
      return _primitiveTerminal((EClassifier)c);
    } else if (c instanceof EPackage) {
      return _primitiveTerminal((EPackage)c);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(c).toString());
    }
  }
}
