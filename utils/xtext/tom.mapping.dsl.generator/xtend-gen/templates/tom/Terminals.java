package templates.tom;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtend2.lib.StringConcatenation;
import templates.Naming;
import tom.mapping.model.Mapping;
import tom.mapping.model.Terminal;

@SuppressWarnings("all")
public class Terminals {
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
      String _name = Naming.name(t, m);
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
    _builder.append("{ $t instanceof EList<?> &&  ");
    _builder.newLine();
    _builder.append("(((EList<");
    EClass _class_ = t.getClass_();
    String _name = _class_.getName();
    _builder.append(_name, "");
    _builder.append(">)$t).size() == 0 ");
    _builder.newLineIfNotEmpty();
    _builder.append("|| (((EList<");
    EClass _class__1 = t.getClass_();
    String _name_1 = _class__1.getName();
    _builder.append(_name_1, "");
    _builder.append(">)$t).size()>0 && ((EList<");
    EClass _class__2 = t.getClass_();
    String _name_2 = _class__2.getName();
    _builder.append(_name_2, "");
    _builder.append(">)$t).get(0) instanceof ");
    EClass _class__3 = t.getClass_();
    String _name_3 = _class__3.getName();
    _builder.append(_name_3, "");
    _builder.append("))} ");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
