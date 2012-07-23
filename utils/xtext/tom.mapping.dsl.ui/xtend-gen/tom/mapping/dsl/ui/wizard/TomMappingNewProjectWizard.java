package tom.mapping.dsl.ui.wizard;

import com.google.inject.Inject;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.ui.wizard.IProjectCreator;
import org.eclipse.xtext.ui.wizard.IProjectInfo;
import org.eclipse.xtext.ui.wizard.XtextNewProjectWizard;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import tom.mapping.dsl.ui.wizard.TomMappingProjectInfo;
import tom.mapping.dsl.ui.wizard.Tools;

@SuppressWarnings("all")
public class TomMappingNewProjectWizard extends XtextNewProjectWizard {
  @Inject
  private WizardNewProjectCreationPage mainPage;
  
  @Inject
  public TomMappingNewProjectWizard(final IProjectCreator creator) {
      super(creator);
      this.setWindowTitle("New TomMapping Project");
  }
  
  protected IProjectInfo getProjectInfo() {
      TomMappingProjectInfo _tomMappingProjectInfo = new TomMappingProjectInfo();
      final TomMappingProjectInfo projectInfo = _tomMappingProjectInfo;
      String _projectName = this.mainPage.getProjectName();
      projectInfo.setProjectName(_projectName);
      return projectInfo;
  }
  
  /**
   * Use this method to add pages to the wizard.
   * The one-time generated version of this class will add a default new project page to the wizard.
   */
  public void addPages() {
      WizardNewProjectCreationPage _wizardNewProjectCreationPage = new WizardNewProjectCreationPage("basicNewProjectPage");
      this.mainPage = _wizardNewProjectCreationPage;
      this.mainPage.setTitle("TomMapping Project");
      this.mainPage.setDescription("Create a new TomMapping project.");
      this.addPage(this.mainPage);
  }
  
  private Tools _tools = new Function0<Tools>() {
    public Tools apply() {
      Tools _tools = new Tools();
      return _tools;
    }
  }.apply();
  
  private String prefix = "";
  
  public void compile(final TomMappingProjectInfo tmpi, final IFileSystemAccess fsa) {
      String _operator_plus = StringExtensions.operator_plus(this.prefix, "src/mappings/Model.tmap");
      CharSequence _model = this.model(tmpi);
      fsa.generateFile(_operator_plus, _model);
      String _operator_plus_1 = StringExtensions.operator_plus(this.prefix, "META-INF/MANIFEST.MF");
      CharSequence _manifest = this.manifest(tmpi);
      fsa.generateFile(_operator_plus_1, _manifest);
      String _operator_plus_2 = StringExtensions.operator_plus(this.prefix, "src/");
      String _projectName = tmpi.getProjectName();
      String _path = this._tools.path(_projectName);
      String _operator_plus_3 = StringExtensions.operator_plus(_operator_plus_2, _path);
      String _operator_plus_4 = StringExtensions.operator_plus(_operator_plus_3, "/generator/MappingGenerator.java");
      CharSequence _generator = this.generator(tmpi);
      fsa.generateFile(_operator_plus_4, _generator);
      String _operator_plus_5 = StringExtensions.operator_plus(this.prefix, ".project");
      CharSequence _project = this.project(tmpi);
      fsa.generateFile(_operator_plus_5, _project);
      String _operator_plus_6 = StringExtensions.operator_plus(this.prefix, "src/");
      String _projectName_1 = tmpi.getProjectName();
      String _path_1 = this._tools.path(_projectName_1);
      String _operator_plus_7 = StringExtensions.operator_plus(_operator_plus_6, _path_1);
      String _operator_plus_8 = StringExtensions.operator_plus(_operator_plus_7, "/tom/sources/Example.t");
      CharSequence _mFile = this.tomFile(tmpi);
      fsa.generateFile(_operator_plus_8, _mFile);
  }
  
  public CharSequence main(final TomMappingProjectInfo tmpi) {
    CharSequence _xblockexpression = null;
    {
      this.model(tmpi);
      this.generator(tmpi);
      this.manifest(tmpi);
      this.tomFile(tmpi);
      CharSequence _project = this.project(tmpi);
      _xblockexpression = (_project);
    }
    return _xblockexpression;
  }
  
  public CharSequence model(final TomMappingProjectInfo tmpi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("TomMapping MyMapping {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence manifest(final TomMappingProjectInfo tmpi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Manifest-Version: 1.0");
    _builder.newLine();
    _builder.append("Bundle-ManifestVersion: 2");
    _builder.newLine();
    _builder.append("Bundle-Name: ");
    String _projectName = tmpi.getProjectName();
    _builder.append(_projectName, "");
    _builder.newLineIfNotEmpty();
    _builder.append("undle-Vendor: My Company");
    _builder.newLine();
    _builder.append("Bundle-Version: 1.0.0");
    _builder.newLine();
    _builder.append("Bundle-SymbolicName: ");
    String _projectName_1 = tmpi.getProjectName();
    _builder.append(_projectName_1, "");
    _builder.append("; singleton:=true");
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-ActivationPolicy: lazy");
    _builder.newLine();
    _builder.append("Require-Bundle: com.ibm.icu,");
    _builder.newLine();
    _builder.append("org.eclipse.xtext,");
    _builder.newLine();
    _builder.append("org.eclipse.xtext.generator,");
    _builder.newLine();
    _builder.append("org.eclipse.xtend,");
    _builder.newLine();
    _builder.append("org.eclipse.xtend.typesystem.emf,");
    _builder.newLine();
    _builder.append("org.eclipse.xpand,");
    _builder.newLine();
    _builder.append("de.itemis.xtext.antlr;resolution:=optional,");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("org.eclipse.xtend.util.stdlib,");
    _builder.newLine();
    _builder.append("org.eclipse.emf.mwe2.launch;resolution:=optional,");
    _builder.newLine();
    _builder.append("tom.mapping.dsl.generator,");
    _builder.newLine();
    _builder.append("org.eclipse.jface.text;bundle-version=\"3.6.0\",");
    _builder.newLine();
    _builder.append("org.eclipse.jdt.core;bundle-version=\"3.6.0\",");
    _builder.newLine();
    _builder.append("tom.mapping.model;bundle-version=\"1.0.0\",");
    _builder.newLine();
    _builder.append("org.eclipse.xtext.ecore;bundle-version=\"1.0.0\",");
    _builder.newLine();
    _builder.append("org.eclipse.core.runtime;bundle-version=\"3.6.0\",");
    _builder.newLine();
    _builder.append("fr.irisa.cairn.eclipse.tom;bundle-version=\"1.0.0\"");
    _builder.newLine();
    _builder.append("Import-Package: org.apache.log4j,");
    _builder.newLine();
    _builder.append("org.apache.commons.logging");
    _builder.newLine();
    _builder.append("Bundle-RequiredExecutionEnvironment: J2SE-1.5");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generator(final TomMappingProjectInfo tmpi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _projectName = tmpi.getProjectName();
    _builder.append(_projectName, "");
    _builder.append(".generator;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import tom.mapping.dsl.generator.TomMappingGenerator;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class MappingGenerator {");
    _builder.newLine();
    _builder.append("private final static String MODEL_PATH = \"src/mappings/\";");
    _builder.newLine();
    _builder.append("private final static String TOM_PATH = \"src-tom/\";");
    _builder.newLine();
    _builder.append("private final static String JAVA_PACKAGE = \"internal/\";");
    _builder.newLine();
    _builder.append("private final static String JAVA_PATH = \"src-gen/");
    String _projectName_1 = tmpi.getProjectName();
    String _path = this._tools.path(_projectName_1);
    _builder.append(_path, "");
    _builder.append("/\";");
    _builder.newLineIfNotEmpty();
    _builder.append("private final static String ENCODING = \"UTF-8\";");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public static void main(String[] args) {");
    _builder.newLine();
    _builder.append("MappingGenerator.generate();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public static void generate() {");
    _builder.newLine();
    _builder.append("TomMappingGenerator.generate(MODEL_PATH, JAVA_PATH, JAVA_PACKAGE,");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("TOM_PATH, ENCODING);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence project(final TomMappingProjectInfo tmpi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<projectDescription>");
    _builder.newLine();
    _builder.append("<name>");
    String _projectName = tmpi.getProjectName();
    _builder.append(_projectName, "");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("<comment></comment>");
    _builder.newLine();
    _builder.append("<projects>");
    _builder.newLine();
    _builder.append("</projects>");
    _builder.newLine();
    _builder.append("<buildSpec>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>fr.loria.eclipse.tom.GomBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>fr.loria.eclipse.tom.TomBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>org.eclipse.jdt.core.javabuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>fr.loria.eclipse.tom.TomBuildAnalyser</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>org.eclipse.pde.ManifestBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>org.eclipse.pde.SchemaBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<name>org.eclipse.xtext.ui.shared.xtextBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("</buildSpec>");
    _builder.newLine();
    _builder.append("<natures>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<nature>org.eclipse.jdt.core.javanature</nature>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<nature>org.eclipse.pde.PluginNature</nature>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<nature>org.eclipse.xtext.ui.shared.xtextNature</nature>");
    _builder.newLine();
    _builder.append("</natures>");
    _builder.newLine();
    _builder.append("</projectDescription>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence tomFile(final TomMappingProjectInfo tmpi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    String _projectName = tmpi.getProjectName();
    _builder.append(_projectName, "");
    _builder.append(".tom;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class Example {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
