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
        _builder.append("import ");
        String _packagePrefix = this._tomMappingExtensions.getPackagePrefix(prefix);
        _builder.append(_packagePrefix, "");
        String _name = ep.getName();
        _builder.append(_name, "");
        _builder.append(".*; ");
        _builder.newLineIfNotEmpty();
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        String _packagePrefix_1 = this._tomMappingExtensions.getPackagePrefix(prefix);
        String _name_1 = ep.getName();
        String _operator_plus = StringExtensions.operator_plus(_packagePrefix_1, _name_1);
        this.imports(_operator_plus, p);
      }
  }
  
  public void importsWithUtils(final String prefix, final EPackage ep) {
      EList<EClassifier> _eClassifiers = ep.getEClassifiers();
      int _size = _eClassifiers.size();
      boolean _operator_greaterThan = IntegerExtensions.operator_greaterThan(_size, 0);
      if (_operator_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("import ");
        String _packagePrefix = this._tomMappingExtensions.getPackagePrefix(prefix);
        _builder.append(_packagePrefix, "");
        String _name = ep.getName();
        _builder.append(_name, "");
        _builder.append(".*; ");
        _builder.newLineIfNotEmpty();
        _builder.append("import ");
        String _packagePrefix_1 = this._tomMappingExtensions.getPackagePrefix(prefix);
        _builder.append(_packagePrefix_1, "");
        String _name_1 = ep.getName();
        _builder.append(_name_1, "");
        _builder.append(".util.*; ");
        _builder.newLineIfNotEmpty();
      }
      EList<EPackage> _eSubpackages = ep.getESubpackages();
      for (final EPackage p : _eSubpackages) {
        String _packagePrefix_2 = this._tomMappingExtensions.getPackagePrefix(prefix);
        String _name_2 = ep.getName();
        String _operator_plus = StringExtensions.operator_plus(_packagePrefix_2, _name_2);
        this.importsWithUtils(_operator_plus, p);
      }
  }
}
