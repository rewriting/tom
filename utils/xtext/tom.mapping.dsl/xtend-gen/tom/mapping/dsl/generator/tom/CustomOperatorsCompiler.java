package tom.mapping.dsl.generator.tom;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.dsl.generator.tom.ParametersCompiler;
import tom.mapping.model.Accessor;
import tom.mapping.model.Mapping;
import tom.mapping.model.Operator;
import tom.mapping.model.UserOperator;

@SuppressWarnings("all")
public class CustomOperatorsCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  /**
   * def main(Mapping map) {
   * if(map.operators.filter[e | UserOperator.isInstance(e)].size>0) {
   * var File getCustomOperatorsClass()+".java"; // File ?
   * 
   * '''
   * public class «getCustomOperatorClass()» {
   * «for(Operator op: operators)» {
   * «operator(map, op);
   * }»
   * 
   * }
   * '''
   * }
   * }
   */
  public Object operator(final Mapping map, final Operator op) {
    return null;
  }
  
  public void operator(final Mapping map, final UserOperator usop) {
    EList<Accessor> _accessors = usop.getAccessors();
    for (final Accessor a : _accessors) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\u00AC\u00B4accessor(usop,a);\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\u00AC\u00B4test(usop);\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\u00AC\u00B4make(usop);\u00AC\u00AA");
      _builder.newLine();
    }
  }
  
  public CharSequence accessor(final UserOperator op, final Accessor acc) {
    CharSequence _xblockexpression = null;
    {
      ParametersCompiler paco = null;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public static \u00AC\u00B4paco.javaTerminalType(acc.slot.type)\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\u00AC\u00B4getCustomOperatorSlotAccessorName(acc);\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\u00AC\u00B4(paco.javaTerminalType(op.type)\u00AC\u00AA t) {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("return \u00AC\u00B4java\u00AC\u00AA");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence test(final UserOperator usop) {
    CharSequence _xblockexpression = null;
    {
      ParametersCompiler paco = null;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public static boolean is\u00AC\u00B4name.toFirstUpper()\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("(\u00AC\u00B4paco.javaTerminalType(usop.type)\u00AC\u00AA t) {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("return \u00AC\u00B4test\u00AC\u00AA;");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public CharSequence make(final UserOperator usop) {
    CharSequence _xblockexpression = null;
    {
      ParametersCompiler paco = null;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("public static \u00AC\u00B4paco.javaTerminalType(usop.type)\u00AC\u00AA make \u00AC\u00B4name.toFirstUpper()\u00AC\u00AA");
      _builder.newLine();
      _builder.append("(\u00AC\u00B4for(Accessor as: accessors)\u00AC\u00AA) {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("\u00AC\u00B4paco.javaParameter(acc.slot)\u00AC\u00AA {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("return \u00AC\u00B4make\u00AC\u00AA");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
}
