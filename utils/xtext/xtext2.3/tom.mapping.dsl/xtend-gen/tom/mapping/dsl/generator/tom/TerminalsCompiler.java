package tom.mapping.dsl.generator.tom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
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
      _builder.append("%typeterm ");
      String _name = t.getName();
      _builder.append(_name, "");
      _builder.append(" {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("implement \t\t{");
      EClass _class_ = t.getClass_();
      String _name_1 = _class_.getName();
      _builder.append(_name_1, "	");
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("is_sort(t) \t\t{$t instanceof ");
      EClass _class__1 = t.getClass_();
      String _name_2 = _class__1.getName();
      _builder.append(_name_2, "	");
      _builder.append("}");
      _builder.newLineIfNotEmpty();
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
      _builder.append("%typeterm ");
      _builder.append(name, "");
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      _builder.append("\t ");
      _builder.append("implement { EList<");
      EClass _class_ = t.getClass_();
      String _name_1 = _class_.getName();
      _builder.append(_name_1, "	 ");
      _builder.append("> }");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("is_sort(t) ");
      CharSequence _listTest = this.listTest(t);
      _builder.append(_listTest, "     ");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("equals(l1,l2) \t{($l1!=null && $l1.equals($l2)) || $l1==$l2}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("%oparray ");
      _builder.append(name, "");
      _builder.append(" ");
      _builder.append(name, "");
      _builder.append(" (");
      EClass _class__1 = t.getClass_();
      Terminal _terminal = m.getTerminal(_class__1, false);
      String _name_2 = _terminal.getName();
      _builder.append(_name_2, "");
      _builder.append("*) {");
      _builder.newLineIfNotEmpty();
      _builder.append(" \t ");
      _builder.append("is_fsym(t) ");
      CharSequence _listTest_1 = this.listTest(t);
      _builder.append(_listTest_1, " 	 ");
      _builder.newLineIfNotEmpty();
      _builder.append("     ");
      _builder.append("make_empty(n) { new BasicEList<");
      EClass _class__2 = t.getClass_();
      String _name_3 = _class__2.getName();
      _builder.append(_name_3, "     ");
      _builder.append(">($n) }");
      _builder.newLineIfNotEmpty();
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
    _builder.append("(((EList<");
    EClass _class_ = t.getClass_();
    String _name = _class_.getName();
    _builder.append(_name, "	");
    _builder.append(">)$t).size() == 0 ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("|| (((EList<");
    EClass _class__1 = t.getClass_();
    String _name_1 = _class__1.getName();
    _builder.append(_name_1, "	");
    _builder.append(">)$t).size()>0 && ((EList<");
    EClass _class__2 = t.getClass_();
    String _name_2 = _class__2.getName();
    _builder.append(_name_2, "	");
    _builder.append(">)$t).get(0) instanceof ");
    EClass _class__3 = t.getClass_();
    String _name_3 = _class__3.getName();
    _builder.append(_name_3, "	");
    _builder.append("))} ");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EPackage c) {
    StringConcatenation _builder = new StringConcatenation();
    {
      TreeIterator<EObject> _eAllContents = c.eAllContents();
      Iterator<EClassifier> _filter = IteratorExtensions.<EClassifier>filter(_eAllContents, org.eclipse.emf.ecore.EClassifier.class);
      List<EClassifier> _list = IteratorExtensions.<EClassifier>toList(_filter);
      for(final EClassifier classifier : _list) {
        CharSequence _primitiveTerminal = this.primitiveTerminal(classifier);
        _builder.append(_primitiveTerminal, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EClassifier c) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _primitiveTerminal(final EEnum c) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%typeterm ");
    String _name = c.getName();
    _builder.append(_name, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("implement \t\t{");
    String _name_1 = c.getName();
    _builder.append(_name_1, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("is_sort(t) \t\t{$t instanceof ");
    String _name_2 = c.getName();
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
  
  protected CharSequence _primitiveTerminal(final EDataType c) {
    StringConcatenation _builder = new StringConcatenation();
    String _instanceTypeName = c.getInstanceTypeName();
    boolean _isPrimitive = this._tomMappingExtensions.isPrimitive(_instanceTypeName);
    final boolean primitive = _isPrimitive;
    _builder.newLineIfNotEmpty();
    _builder.append("%typeterm ");
    String _name = c.getName();
    _builder.append(_name, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("implement \t\t{");
    String _instanceTypeName_1 = c.getInstanceTypeName();
    _builder.append(_instanceTypeName_1, "	");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("is_sort(t) \t\t{");
    {
      if (primitive) {
        _builder.append("true");
      } else {
        _builder.append("$t instanceof ");
        String _instanceTypeName_2 = c.getInstanceTypeName();
        _builder.append(_instanceTypeName_2, "	");
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("equals(l1,l2) \t{");
    {
      if (primitive) {
        _builder.append("$l1==$l2");
      } else {
        _builder.append("$l1.equals($l2)");
      }
    }
    _builder.append("}");
    _builder.newLineIfNotEmpty();
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
