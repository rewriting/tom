package tom.mapping.dsl.generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.TomMappingExtensions;
import tom.mapping.model.Mapping;

@SuppressWarnings("all")
public class ImportsCompiler {
  private TomMappingExtensions _tomMappingExtensions = new Function0<TomMappingExtensions>() {
    public TomMappingExtensions apply() {
      TomMappingExtensions _tomMappingExtensions = new TomMappingExtensions();
      return _tomMappingExtensions;
    }
  }.apply();
  
  public void imports(final Mapping map) {
    EList<EPackage> _allRootPackages = this._tomMappingExtensions.getAllRootPackages(map);
    for (final EPackage p : _allRootPackages) {
      String _nsPrefix = p.getNsPrefix();
      this.imports(_nsPrefix, p);
    }
  }
  
  public void importsWithUtils(final Mapping map) {
    EList<EPackage> _allRootPackages = this._tomMappingExtensions.getAllRootPackages(map);
    for (final EPackage p : _allRootPackages) {
      String _nsPrefix = p.getNsPrefix();
      this.importsWithUtils(_nsPrefix, p);
    }
  }
  
  public void imports(final String prefix, final EPackage ep) {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      int _size = _eClassifiers.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        {
          String _name = ep.getName();
          String _operator_plus = StringExtensions.operator_plus(prefix, _name);
          String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, ".*");
          String aimporter = _operator_plus_1;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("import \u00ACaimporter\u00AC\u00AA; ");
          _builder.newLine();
        }
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        String _nsPrefix = p.getNsPrefix();
        this.imports(_nsPrefix, p);
      }
  }
  
  public void importsWithUtils(final String prefix, final EPackage ep) {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      int _size = _eClassifiers.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        {
          String _name = ep.getName();
          String _operator_plus = StringExtensions.operator_plus(prefix, _name);
          String _operator_plus_1 = StringExtensions.operator_plus(_operator_plus, ".*");
          String import1 = _operator_plus_1;
          String _name_1 = ep.getName();
          String _operator_plus_2 = StringExtensions.operator_plus(prefix, _name_1);
          String _operator_plus_3 = StringExtensions.operator_plus(_operator_plus_2, ".util.*");
          String import2 = _operator_plus_3;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("import \u00ACimport1\u00AC\u00AA; ");
          _builder.newLine();
          _builder.append("import \u00ACimport2\u00AC\u00AA; ");
          _builder.newLine();
        }
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        String _nsPrefix = p.getNsPrefix();
        this.importsWithUtils(_nsPrefix, p);
      }
  }
}
