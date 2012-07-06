package tom.mapping.dsl.generator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IntegerExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.generator.NamingCompiler;
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
  
  private NamingCompiler _namingCompiler = new Function0<NamingCompiler>() {
    public NamingCompiler apply() {
      NamingCompiler _namingCompiler = new NamingCompiler();
      return _namingCompiler;
    }
  }.apply();
  
  private String prefix = "";
  
  public void imports(final Mapping map) {
    EList<EPackage> _allRootPackages = this._tomMappingExtensions.getAllRootPackages(map);
    for (final EPackage p : _allRootPackages) {
      this.imports(this.prefix, p);
    }
  }
  
  public void importsWithUtils(final Mapping map) {
    EList<EPackage> _allRootPackages = this._tomMappingExtensions.getAllRootPackages(map);
    for (final EPackage p : _allRootPackages) {
      this.importsWithUtils(this.prefix, p);
    }
  }
  
  public void imports(final String prefix, final EPackage ep) {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      int _size = _eClassifiers.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("import \u00AC\u00B4getPackagePrefix(prefix)\u00AC\u00AA\u00AC\u00B4ep.name\u00AC\u00AA.*; ");
        _builder.newLine();
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        this.imports(prefix, p);
      }
  }
  
  public void importsWithUtils(final String prefix, final EPackage ep) {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      int _size = _eClassifiers.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("import \u00AC\u00B4getPackagePrefix(prefix)\u00AC\u00AA\u00AC\u00B4ep.name\u00AC\u00AA.*; ");
        _builder.newLine();
        _builder.append("import \u00AC\u00B4getPackagePrefix(prefix)\u00AA\u00AC\u00B4ep.name\u00AC\u00AA.util.*; ");
        _builder.newLine();
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        String _packagePrefix = this._namingCompiler.getPackagePrefix(prefix);
        String _name = ep.getName();
        String _operator_plus = StringExtensions.operator_plus(_packagePrefix, _name);
        this.importsWithUtils(_operator_plus, p);
      }
  }
}
