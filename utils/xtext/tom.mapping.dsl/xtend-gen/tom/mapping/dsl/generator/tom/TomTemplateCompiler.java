package tom.mapping.dsl.generator.tom;

import com.google.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.TerminalsCompiler;
import tom.mapping.model.Mapping;
import tom.mapping.model.Module;

@SuppressWarnings("all")
public class TomTemplateCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  @Inject
  private TerminalsCompiler terminals;
  
  private String prefix = "tom/";
  
  public void compile(final Mapping m, final IFileSystemAccess fsa) {
      String _operator_plus = StringExtensions.operator_plus(this.prefix, "common.tom");
      CharSequence _main = this.main(m);
      fsa.generateFile(_operator_plus, _main);
      String _name = m.getName();
      String _operator_plus_1 = StringExtensions.operator_plus(this.prefix, _name);
      String _operator_plus_2 = StringExtensions.operator_plus(_operator_plus_1, "_terminals.tom");
      CharSequence _terminals = this.terminals(m);
      fsa.generateFile(_operator_plus_2, _terminals);
      String _name_1 = m.getName();
      String _operator_plus_3 = StringExtensions.operator_plus(this.prefix, _name_1);
      String _operator_plus_4 = StringExtensions.operator_plus(_operator_plus_3, "_operators.tom");
      CharSequence _operators = this.operators(m);
      fsa.generateFile(_operator_plus_4, _operators);
      String _name_2 = m.getName();
      String _operator_plus_5 = StringExtensions.operator_plus(this.prefix, _name_2);
      String _operator_plus_6 = StringExtensions.operator_plus(_operator_plus_5, "_defaultOperators.tom");
      CharSequence _defaultOperators = this.defaultOperators(m);
      fsa.generateFile(_operator_plus_6, _defaultOperators);
      EList<Module> _modules = m.getModules();
      for (final Module module : _modules) {
        String _name_3 = m.getName();
        String _operator_plus_7 = StringExtensions.operator_plus(this.prefix, _name_3);
        String _operator_plus_8 = StringExtensions.operator_plus(_operator_plus_7, "_");
        String _name_4 = module.getName();
        String _operator_plus_9 = StringExtensions.operator_plus(_operator_plus_8, _name_4);
        String _operator_plus_10 = StringExtensions.operator_plus(_operator_plus_9, ".tom");
        CharSequence _module = this.module(module);
        fsa.generateFile(_operator_plus_10, _module);
      }
  }
  
  public CharSequence main(final Mapping m) {
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
  
  public CharSequence terminals(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Primitive terminals (enum and data types)");
    _builder.newLine();
    _builder.append("\u00AC\u00B4FOR p: m.getAllRootPackages()\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4terminals.primitiveTerminal(p)\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4ENDFOR\u00AC\u00AA    ");
    _builder.newLine();
    _builder.append("// Terminals");
    _builder.newLine();
    _builder.append("\u00AC\u00B4FOR t: m.terminals\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4terminals.terminal(m,t)\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4ENDFOR\u00AC\u00AA");
    _builder.newLine();
    _builder.append("// List Terminals");
    _builder.newLine();
    _builder.append("\u00AC\u00B4FOR lt:m.allListTerminals\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4terminals.listTerminal(m,lt)\u00AC\u00AA");
    _builder.newLine();
    _builder.append("\u00AC\u00B4ENDFOR\u00AC\u00AA");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence operators(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence defaultOperators(final Mapping m) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence module(final Module m) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
}
