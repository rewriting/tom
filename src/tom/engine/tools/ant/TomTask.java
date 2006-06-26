/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2004-2006 INRIA
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.tools.ant;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.taskdefs.Java;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MatchingTask;
//import org.apache.tools.ant.taskdefs.Java;

/**
 * Compiles Tom source files. This task can take the following
 * arguments:
 * <ul>
 * <li>config</li>
 * <li>logpropertiesfile</li>
 * <li>srcdir</li>
 * <li>destdir</li>
 * <li>outputfile</li>
 * <li>optimize</li>
 * <li>optimize2</li>
 * <li>pretty</li>
 * <li>verbose</li>
 * <li>failonerror</li>
 * <li>stamp</li>
 * <li>nowarn</li>
 * <li>fork</li>
 * </ul>
 * Of these arguments, only <b>sourcedir</b> is required.
 * Either <b>destdir</b> or <b>outputfile</b> have to be set,
 * and <b>config</b> has to be set if the Tom.xml file can't be found in
 * <b>tom.home</b>.<p>
 * When this task executes, it will recursively scan the sourcedir and
 * destdir looking for Java source files to compile. This task makes its
 * compile decision based on timestamp.
 */

public class TomTask extends MatchingTask {

  private static final String FAIL_MSG =
    "Compile failed; see the compiler error output for details.";

  private String options = null;
  private Path src;
  private File destDir;
  private File configFile = null;
  private String logPropertiesFile;
  private File outputFile;
  private boolean verbose = false;
  private boolean stamp = false;
  private boolean nowarn = false;
  private boolean optimize = false;
  private boolean optimize2 = false;
  private boolean pretty = false;
  private boolean protectedFlag = false;

  private boolean failOnError = true;
  private boolean fork = false;
  private boolean listFiles = false;
  private File[] compileList = new File[0];

  private Java javaRunner;

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

  /**
   * Set the configuration file
   * @param  configFile the destination directory
   */
  public void setConfig(File configFile) {
    //System.out.println("CONFIG FILE : " + configFile);
    this.configFile = configFile;
  }

  public File getConfig() {
    if (configFile != null) {
      return configFile;
    } else {
      String tom_home = getProject().getProperty("tom.home");
      try {
        return new File(tom_home,File.separator+"Tom.xml").getCanonicalFile();
      } catch (IOException e) {
        throw new BuildException(
            "Unable to find Tom.xml in "+tom_home,
            getLocation());
      }
    }
  }

  /**
   * Set the log file
   * @param logPropertiesFile the destination file
   */
  public void setLogfile(String logPropertiesFile) {
    System.out.println("LOG FILE : " + logPropertiesFile);
    this.logPropertiesFile = logPropertiesFile;
  }

  public String getLogfile() {
    return logPropertiesFile;
  }

  /**
   * Set command line option to pass to Tom
   * @param options the command line options to use
   */
  public void setOptions(String options) {
    this.options = options;
  }

  public String getOptions() {
    return options;
  }

  /**
   * Adds a path for source compilation.
   *
   * @return a nested src element.
   */
  public Path createSrc() {
    if (src == null) {
      src = new Path(getProject());
    }
    return src.createPath();
  }

  /**
   * Recreate src.
   *
   * @return a nested src element.
   */
  protected Path recreateSrc() {
    src = null;
    return createSrc();
  }

  /**
   * Set the source directories to find the source Java files.
   * @param srcDir the source directories as a path
   */
  public void setSrcdir(Path srcDir) {
    if (src == null) {
      src = srcDir;
    } else {
      src.append(srcDir);
    }
  }

  /**
   * Gets the source dirs to find the source java files.
   * @return the source directories as a path
   */
  public Path getSrcdir() {
    return src;
  }

  /**
   * Set the destination directory into which the Tom source
   * files should be compiled.
   * @param destDir the destination directory
   */
  public void setDestdir(File destDir) {
    this.destDir = destDir;
  }

  /**
   * Gets the destination directory into which the Tom source files
   * should be compiled.
   * @return the destination directory
   */
  public File getDestdir() {
    return destDir;
  }

  /**
   * Set the output file into which the Tom source
   * file should be compiled.
   * @param outputFile the destination file
   */
  public void setOutputFile(File outputFile) {
    this.outputFile = outputFile;
  }

  /**
   * Gets the destination file into which the Tom source file
   * should be compiled.
   * @return the destination file
   */
  public File getOutputFile() {
    return outputFile;
  }

  /**
   * If true, list the source files being handed off to the compiler.
   * @param list if true list the source files
   */
  public void setListfiles(boolean list) {
    listFiles = list;
  }

  /**
   * Get the listfiles flag.
   * @return the listfiles flag
   */
  public boolean getListfiles() {
    return listFiles;
  }

  /**
   * If true, asks the compiler for verbose output.
   * @param verbose if true, asks the compiler for verbose output
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * If true, asks the compiler to generate matching code for abstract
   * data-type.
   * @param stamp if true, asks the compiler to generate code with stamps
   */
  public void setStamp(boolean stamp) {
    this.stamp = stamp;
  }

  public boolean getStamp() {
    return stamp;
  }

  /**
   * Gets the verbose flag.
   * @return the verbose flag
   */
  public boolean getVerbose() {
    return verbose;
  }

  /**
   * If true, compiles with optimization enabled.
   * @param optimize if true compile with optimization level-1 enabled
   */
  public void setOptimize(boolean optimize) {
    this.optimize = optimize;
  }

  public boolean getOptimize() {
    return optimize;
  }

  /**
   * If true, compiles with optimization enabled.
   * @param optimize if true compile with optimization level-2 enabled
   */
  public void setOptimize2(boolean optimize) {
    this.optimize2 = optimize;
  }

  public boolean getOptimize2() {
    return optimize2;
  }

  /**
   * If true, compiles with pretty-printing enabled.
   * @param pretty if true compile with pretty-printing enabled
   */
  public void setPretty(boolean pretty) {
    this.pretty = pretty;
  }

  public boolean getPretty() {
    return pretty;
  }

  /**
   * If true, generates  protected functions instead of private
   * @param flag if true generates  protected functions instead of private
   */
  public void setProtected(boolean flag) {
    this.protectedFlag = flag;
  }

  public boolean getProtected() {
    return protectedFlag;
  }

  /**
   * If true, disable the --wall option.
   * @param flag if true, disable the --wall option
   */
  public void setNowarn(boolean flag) {
    this.nowarn = flag;
  }

  /**
   * Should the --wall option be used.
   * @return true if the --wall option should be used
   */
  public boolean getNowarn() {
    return nowarn;
  }

  /**
   * If true, run Tom in a new JVM
   * @param flag if true, executes in a new JVM
   */
  public void setFork(boolean flag) {
    this.fork = flag;
  }

  /**
   * Should Tom run in a new JVM
   * @return true if Tom should run in a new JVM
   */
  public boolean getFork() {
    return fork;
  }

  /**
   * Executes the task.
   * @exception BuildException if an error occurs
   */
  public void execute() throws BuildException {
    checkParameters();
    resetFileLists();

    // scan source directories and dest directory to build up
    // compile lists
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

      scanDir(srcDir, destDir != null ? destDir : srcDir, files);
    }

    compile();
  }

  /**
   * Clear the list of files to be compiled and copied..
   */
  protected void resetFileLists() {
    compileList = new File[0];
  }

  /**
   * Scans the directory looking for source files to be compiled.
   * The results are returned in the class variable compileList
   *
   * @param srcDir   The source directory
   * @param destDir  The destination directory
   * @param files    An array of filenames
   */
  protected void scanDir(File srcDir, File destDir, String[] files) {
    if ((outputFile != null) && (files.length == 1)) {
      GlobPatternMapper m = new GlobPatternMapper();
      m.setFrom(files[0]);
      m.setTo(outputFile.getPath());
      SourceFileScanner sfs = new SourceFileScanner(this);
      File[] newFiles = sfs.restrictAsFiles(files, srcDir, null, m);

      if (newFiles.length > 0) {
        File[] newCompileList
          = new File[compileList.length + newFiles.length];
        System.arraycopy(compileList, 0, newCompileList, 0,
                         compileList.length);
        System.arraycopy(newFiles, 0, newCompileList,
                         compileList.length, newFiles.length);
        compileList = newCompileList;
      }
    } else {
      GlobPatternMapper m = new GlobPatternMapper();
      m.setFrom("*.t");
      m.setTo("*.java");
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
  }

  /**
   * Gets the list of files to be compiled.
   * @return the list of files as an array
   */
  public File[] getFileList() {
    return (File[]) compileList.clone();
  }

  /**
   * Check that all required attributes have been set and nothing
   * silly has been entered.
   *
   * @since Ant 1.5
   * @throws BuildException if all required attributes are not set
   */
  protected void checkParameters() throws BuildException {
    if (configFile == null && getProject().getProperty("tom.home") == null) {
      throw new BuildException(
          "config attribute has to be defined, or the tom.home property",
          getLocation());
    }
    if (src == null) {
      throw new BuildException("srcdir attribute must be set!",
                               getLocation());
    }
    if (src.size() == 0) {
      throw new BuildException("srcdir attribute must be set!",
                               getLocation());
    }

    if (destDir != null && !destDir.isDirectory()) {
      throw new BuildException("destination directory \""
                               + destDir
                               + "\" does not exist "
                               + "or is not a directory", getLocation());
    }
  }

  /**
   * Perform the compilation.
   */
  protected void compile() {

    if (compileList.length > 0) {
      log("Compiling " + compileList.length + " source file"
          + (compileList.length == 1 ? "" : "s")
          + (destDir != null ? " to " + destDir : ""));
      
      if (logPropertiesFile != null) {
        System.out.println("ANT task : properties = " + System.getProperty("java.util.logging.config.file"));
        System.setProperty("java.util.logging.config.file",logPropertiesFile);
      }

      /* If "tom.home" is defined in the ant project, pass it to tom */
      String tom_home = getProject().getProperty("tom.home");
      if (tom_home != null) {
        System.setProperty("tom.home",tom_home);
      } else {
        log("\"tom.home\" is not defined, some features may not work");
      }

      if(options != null && getOptions().trim().length() > 0) {
        javaRunner.createArg().setLine(options);
      }
      if(getConfig() != null) {
        javaRunner.createArg().setValue("-X");
        javaRunner.createArg().setFile(getConfig());
      }
      if(destDir != null) {
        javaRunner.createArg().setValue("-d");
        javaRunner.createArg().setFile(destDir);
      }
      if(outputFile != null) {
        javaRunner.createArg().setValue("-o");
        javaRunner.createArg().setFile(outputFile);
      }
      if(optimize == true) {
        javaRunner.createArg().setValue("--optimize");
      }
      if(optimize2 == true) {
        javaRunner.createArg().setValue("--optimize2");
      }
      if(pretty == true) {
        javaRunner.createArg().setValue("--pretty");
      }
      if(stamp == true) {
        javaRunner.createArg().setValue("--stamp");
      }
      if(nowarn == false) {
        javaRunner.createArg().setValue("--wall");
      }
      if(verbose == true) {
        javaRunner.createArg().setValue("--verbose");
      }
      for (int i = 0; i < compileList.length; i++) {
        String filename = compileList[i].getAbsolutePath();
        //if(verbose) {
        //  System.out.println("Compiling " + filename + "...");
        //}
        javaRunner.createArg().setValue(filename);
      }

      int err = -1;
      javaRunner.setFork(getFork());
      javaRunner.setFailonerror(failOnError);
      javaRunner.setClassname("tom.engine.Tom");
      /* this call will raise a BuildException if tom
         returns != 0 */
      javaRunner.execute(); 
    }
  }

  /**
   * Splits a string using spaces, and returns the words in an array
   * @param str the String to split
   * @return the list of arguments
   */
  private String[] split(String str) {
    try {
      String res[] = new String[0];
      int begin = 0;
      int end = 0;
      Vector list = new Vector();
      while(end < str.length()) {
        while(end < str.length() && str.charAt(end) != ' ')
          end++;
        list.add(str.substring(begin, end));
        begin = ++end;
      }
      return (String[])list.toArray(res);
    } catch(Exception x) {
      return new String[0];
    }
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
  public Path createBootclasspath() {
    return javaRunner.createBootclasspath();
  }
}
