/*
 * Gom
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles       e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.tools.ant;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import tom.engine.tools.ant.TomRegexpPatternMapper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import org.apache.tools.ant.util.SourceFileScanner;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Environment.Variable;

/**
 * Abstract task to use Gom.
 * Provides support for the different tools based on Gom.
 * Arguments:
 * <ul>
 * <li>config</li>
 * <li>srcdir</li>
 * <li>destdir</li>
 * <li>package</li>
 * <li>options</li>
 * <li>failonerror</li>
 * <li>fork</li>
 * <li>termgraph</li>
 * <li>termpointer</li>
 * <li>withCongruenceStrategies</li>
 * <li>withSeparateCongruenceStrategies</li>
 * <li>fresh</li>
 * <li>multithread</li>
 * <li>nosharing</li>
 * <li></li>
 * </ul>
 * Of these arguments, the <b>srcdir</b> and <b>destdir</b> are
 * required.
 * <b>config</b> has to be set if the Gom.xml file can't be found in
 * <b>tom.home</b>.<p>
 *
 */

public class GomCommonTask extends MatchingTask {

  private Path src;
  private String options;
  private File   destdir;
  private String packagePrefix = null;
  private File configFile = null;
  private File[] compileList = null;
  private boolean verbose = false;
  private boolean failOnError = true;
  private boolean fork = false;
  private boolean termpointer = false;
  private boolean termgraph = false;
  private boolean withCongruenceStrategies = false;
  private boolean withSeparateCongruenceStrategies = false;
  private boolean fresh = false;
  private boolean multithread = false;
  private boolean nosharing = false;

  protected final String protectedFileSeparator = "\\"+File.separatorChar;
  private static final String TOM_HOME = "tom.home";

  protected Java javaRunner;

  public void init() throws BuildException {
    javaRunner = new Java();
    configureTask(javaRunner);
  }

  private void configureTask(Task runner) {
    runner.setProject(getProject());
    runner.setTaskName(getTaskName());
    runner.setOwningTarget(getOwningTarget());
    runner.init();
  }

  public Path createSrc() {
    if (null == src) {
      src = new Path(getProject());
    }
    return src.createPath();
  }

  protected Path recreateSrc() {
    src = null;
    return createSrc();
  }

  public void setSrcdir(Path srcDir) {
    if (null == src) {
      src = srcDir;
    } else {
      src.append(srcDir);
    }
  }

  public Path getSrcdir() {
    return src;
  }

  /**
   * If true, run Gom in a new JVM
   * @param flag if true, executes in a new JVM
   */
  public void setFork(boolean flag) {
    this.fork = flag;
  }

  /**
   * Should Gom run in a new JVM
   * @return true if Gom should run in a new JVM
   */
  public boolean getFork() {
    return fork;
  }

  /**
   * If true, asks the compiler for Gom signature with term pointers.
   * @param termpointer if true, asks the compiler for Gom signature with term pointers
   */
  public void setTermpointer(boolean termpointer) {
    this.termpointer = termpointer;
  }

  /**
   * Gets the termpointer flag.
   * @return the termpointer flag
   */
  public boolean getTermpointer() {
    return termpointer;
  }

  /**
   * If true, asks the compiler for Gom signature with pointers.
   * @param termgraph if true, asks the compiler for Gom signature with pointers
   */
  public void setTermgraph(boolean termgraph) {
    this.termgraph = termgraph;
  }

  /**
   * Gets the termgraph flag.
   * @return the termgraph flag
   */
  public boolean getTermgraph() {
    return termgraph;
  }

  /**
   * If true, generates congruence strategies
   * @param withCongruenceStrategies if true, generates congruence strategies
   */
  public void setWithCongruenceStrategies(boolean flag) {
    this.withCongruenceStrategies = flag;
  }

  /**
   * Gets the withCongruenceStrategies flag.
   * @return the withCongruenceStrategies flag
   */
  public boolean getWithCongruenceStrategies() {
    return withCongruenceStrategies;
  }

  /**
   * If true, generates congruence strategies
   * @param withSeparateCongruenceStrategies if true, generates congruence strategies
   */
  public void setWithSeparateCongruenceStrategies(boolean flag) {
    this.withSeparateCongruenceStrategies = flag;
  }

  /**
   * Gets the withSeparateCongruenceStrategies flag.
   * @return the withSeparateCongruenceStrategies flag
   */
  public boolean getWithSeparateCongruenceStrategies() {
    return withSeparateCongruenceStrategies;
  }

  /**
   * If true, asks the compiler for Gom signature with binders.
   * @param fresh if true, asks the compiler for Gom signature with pointers
   */
  public void setFresh(boolean fresh) {
    this.fresh = fresh;
  }

  /**
   * Gets the fresh flag.
   * @return the fresh flag
   */
  public boolean getFresh() {
    return fresh;
  }

  /**
   * If true, Gom generate data-structure compatible with multi-threading
   * @param flag if true, activate the option
   */
  public void setMultithread(boolean flag) {
    this.multithread = flag;
  }

  /**
   * Gets the multithread flag.
   * @return the multithread flag
   */
  public boolean getMultithread() {
    return multithread;
  }

  /**
   * If true, Gom generate data-structure compatible with no sharing
   * @param flag if true, activate the option
   */
  public void setNosharing(boolean flag) {
    this.nosharing = flag;
  }

  /**
   * Gets the nosharing flag.
   * @return the nosharing flag
   */
  public boolean getNosharing() {
    return nosharing;
  }

  /**
   * If true, asks the compiler for verbose output.
   * @param verbose if true, asks the compiler for verbose output
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets the verbose flag.
   * @return the verbose flag
   */
  public boolean getVerbose() {
    return verbose;
  }

  /**
   * Resets the file list. Make it empty.
   */
  protected void resetFileLists() {
    compileList = new File[0];
  }

  /**
   * Splits a string using spaces, and returns the words in an array
   * @param str the String to split
   * @return the list of arguments
   */
  String[] split(String str) {
    try {
      int begin = 0;
      int end = 0;
      List<String> list = new ArrayList<String>();
      while(end < str.length()) {
        while(end < str.length() && str.charAt(end) != ' ') {
          end++;
        }
        list.add(str.substring(begin, end));
        begin = ++end;
      }
      return list.toArray(new String[0]);
    } catch(Exception x) {
      return new String[0];
    }
  }

  /**
   * Checks that all required attributes are set.
   * @throws BuildException if all required attributes are not set
   */
  protected void checkParameters() throws BuildException {
    if (configFile == null && getProject().getProperty(TOM_HOME) == null) {
      throw new BuildException(
          "config attribute has to be defined, or the tom.home property",
          getLocation());
    }
    if(getSrcdir() == null) {
      throw new BuildException("You must set at least one source directory");
    }
    if(getDestdir() != null && !getDestdir().isDirectory()) {
      throw new BuildException("destination directory \""
          + getDestdir().getAbsolutePath()
          + "\" does not exist "
          + "or is not a directory", getLocation());
    }
  }

  protected String fromPattern() {
    return "((.*?" + protectedFileSeparator + ")?)(\\w+)\\.gom";
  }

  protected String toPattern() {
    throw new RuntimeException("toPattern should be defined");
  }

  protected void scanDir(File srcDir, File destDir, String[] files) {
    TomRegexpPatternMapper m = new TomRegexpPatternMapper();
    m.setFrom(fromPattern());
    m.setTo(toPattern());
    SourceFileScanner sfs = new SourceFileScanner(this);
    File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);

    if (newFiles.length > 0) {
      File[] newCompileList
        = new File[compileList.length + newFiles.length];
      System.arraycopy(compileList, 0, newCompileList, 0,
          compileList.length);
      System.arraycopy(newFiles, 0, newCompileList,
          compileList.length, newFiles.length);
      compileList = newCompileList;
    }
  }

  public void execute() throws BuildException {
    compile();
  }

  protected void compile() throws BuildException {
    try {
      checkParameters();
      resetFileLists();

      /* If "tom.home" is defined in the ant project, pass it to Gom */
      String tom_home = getProject().getProperty(TOM_HOME);
      if (tom_home != null) {
        System.setProperty(TOM_HOME,tom_home);
        Variable var = new Variable();
        var.setKey(TOM_HOME);
        var.setValue(tom_home);
        javaRunner.addSysproperty(var);
      } else {
        log("\"tom.home\" is not defined, Tom hooks may not work");
      }

      // scan sourcedir, build list
      String[] list = src.list();
      for (int i = 0; i < list.length; i++) {

        File srcDir = getProject().resolveFile(list[i]);

        if (!srcDir.exists()) {
          throw new BuildException("srcdir \""
              + srcDir.getPath()
              + "\" does not exist!", getLocation());
        }

        DirectoryScanner ds = this.getDirectoryScanner(srcDir);
        String[] files = ds.getIncludedFiles();

        scanDir(srcDir,(getDestdir()!=null?getDestdir().getCanonicalFile():null),files);
      }
      printCompileList(compileList);
      if(getConfig() != null) {
        javaRunner.createArg().setValue("-X");
        javaRunner.createArg().setFile(getConfig());
      }

      if(getPackage() != null && getPackage().length() > 0) {
        javaRunner.createArg().setValue("--package");
        javaRunner.createArg().setValue(getPackage());
      }

      if(getOptions() != null && getOptions().trim().length() > 0) {
        javaRunner.createArg().setLine(getOptions());
      }

      if(getDestdir() != null) {
        javaRunner.createArg().setValue("--destdir");
        javaRunner.createArg().setFile(getDestdir());
      }
      if(getVerbose()) {
        javaRunner.createArg().setValue("--verbose");
      }
      if(getTermgraph()) {
        javaRunner.createArg().setValue("--termgraph");
      }
      if(getFresh()) {
        javaRunner.createArg().setValue("--fresh");
      }
      if(getMultithread()) {
        javaRunner.createArg().setValue("--multithread");
      }
      if(getNosharing()) {
        javaRunner.createArg().setValue("--nosharing");
      }
      if(getTermpointer()) {
        javaRunner.createArg().setValue("--termpointer");
      }
      if(getWithCongruenceStrategies()) {
        javaRunner.createArg().setValue("--withCongruenceStrategies");
      }
      if(getWithSeparateCongruenceStrategies()) {
        javaRunner.createArg().setValue("--withSeparateCongruenceStrategies");
      }
      processAdditionalOptions(javaRunner);

      for (int i = 0; i < compileList.length; i++) {
        javaRunner.createArg().setValue(compileList[i].getAbsolutePath());
      }

      if(compileList.length > 0) {
        //int err = -1;
        javaRunner.setFork(getFork());
        javaRunner.setClassname("tom.gom.Gom");
        javaRunner.setFailonerror(failOnError);
        javaRunner.execute();
      }
    } catch(Exception e){
      if (e instanceof BuildException) {
        throw (BuildException)e;
      } else {
        e.printStackTrace();
        throw new BuildException("Gom generation failed");
      }
    }
  }

  protected void processAdditionalOptions(Java runner) {
    /* Defaults to nothing */
  }

  public void printCompileList(File[] cl) {
    if (cl.length == 0) {
      return;
    }
    String output = "Compiling:";
    for (int i = 0; i<cl.length; i++) {
      output = output + " " + cl[i].getAbsolutePath();
    }
    System.out.println(output);
  }

  /**
   * Set the configuration file
   * @param  configFile the destination directory
   */
  public void setConfig(File configFile) {
    //System.out.println("CONFIG FILE : " + configFile);
    this.configFile = configFile;
  }

  protected String defaultConfigName() {
    throw new RuntimeException("defaultConfigName should be defined");
  }

  public File getConfig() {
    if (configFile != null) {
      return configFile;
    } else {
      String tom_home = getProject().getProperty(TOM_HOME);
      try {
        return new File(tom_home,File.separator+defaultConfigName()).getCanonicalFile();
      } catch (IOException e) {
        throw new BuildException(
            "Unable to find "+defaultConfigName()+" in "+tom_home,
            getLocation());
      }
    }
  }

  /**
   * Set the package name
   * @param pkgPrefix the package name
   */
  public void setPackage(String pkgPrefix) {
    if(!pkgPrefix.equals("")) {
      this.packagePrefix = pkgPrefix;
    }
  }

  public String getPackage() {
    return packagePrefix;
  }

  public String getPackagePath() {
    if(getPackage() != null) {
      return getPackage().replace('.',File.separatorChar);
    }
    return "";
  }

  /**
   * Set the command line options to pass to the compiler
   * @param options the options to pass
   */
  public void setOptions(String options) {
    this.options = options;
  }

  /**
   * Get the command line options to pass to the compiler
   * @return the options to pass
   */
  public String getOptions() {
    return options;
  }

  /**
   * Set the destination directory where to generate the classes
   * @param destinationDir the destination directory
   */
  public void setDestdir(File destinationDir) {
    destdir = destinationDir;
  }

  /**
   * Get the destination directory where to generate the classes
   * @return the destination directory
   */
  public File getDestdir() {
    return destdir;
  }

  /**
   * Get the destination directory where to generate the classes
   * extended with the package name.
   * @return the extended destination directory
   */
  public File getDestdirWithPackage() {
    File result = new File(getDestdir(),getPackagePath());
    try {
      result = result.getCanonicalFile();
    }
    catch (IOException e) {
      System.out.println("Problem with Package Path " + result);
    }
    return result;
  }

  /* for the nested java process */
  public void setClasspath(Path s) {
    javaRunner.setClasspath(s);
  }

  public Path createClasspath() {
    return javaRunner.createClasspath();
  }

  public void setClasspathRef(Reference r) {
    javaRunner.setClasspathRef(r);
  }
}
