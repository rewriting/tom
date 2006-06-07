/*
 *
 * Pom
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
 * Yoann Toussaint  e-mail: Yoann.Toussaint@loria.fr
 *
 **/

package tom.pom.tools.ant;

import java.io.*;
import java.util.*;
import tom.engine.tools.ant.TomRegexpPatternMapper;
import tom.pom.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.Java;
/**
 * Generates Tom mappings and Token table. This task can take the following
 * arguments:
 * <ul>
 * <li>srcfile</li>
 * <li>destdir</li>
 * <li>package</li>
 * <li>fork</li>
 * </ul>
 * Of these arguments, the <b>srcfile</b>, <b>destdir</b> and <b>package</b> are
 * required.
 */

public class PomTask extends MatchingTask {

  private File srcFile;
  private File destDir;
  private String packagePrefix;
  private boolean fork = false;

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
   * Set the source file
   * @param srcFile the source file
   */
  public void setSrcfile(File srcFile) {
    this.srcFile = srcFile;
  }

  /**
   * Gets the source file
   * @return the source file
   */
  public File getSrcfile() {
    return srcFile;
  }

  /**
   * Set the destination directory
   * @param destDir the destination directory
   */
  public void setDestdir(File destDir) {
    this.destDir = destDir;
  }

  /**
   * Gets the destination directory
   * @return the destination directory
   */
  public File getDestdir() {
    return destDir;
  }

  /**
   * Set the package name
   * @param packagePrefix the package name
   */
  public void setPackage(String name) {
    this.packagePrefix = name;
  }

  /**
   * Gets the package name
   * @return the package name
   */
  public String getPackage() {
    return packagePrefix;
  }

  /**
   * If true, run Pom in a new JVM
   * @param flag if true, executes in a new JVM
   */
  public void setFork(boolean flag) {
    this.fork = flag;
  }

  /**
   * Should Pom run in a new JVM
   * @return true if Pom should run in a new JVM
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

    compile();
  }

  /**
   * Check that all required attributes have been set and nothing
   * silly has been entered.
   *
   * @since Ant 1.5
   * @exception BuildException if an error occurs
   */
  protected void checkParameters() throws BuildException {
    if (srcFile == null) {
      throw new BuildException("srcFile attribute must be set!",
                               getLocation());
    }

    if (destDir != null && !destDir.isDirectory()) {
      throw new BuildException("destination directory \""
                               + destDir
                               + "\" does not exist "
                               + "or is not a directory", getLocation());
    }
    if (packagePrefix == null) {
      throw new BuildException("package attribute must be set!",
                               getLocation());
    }
  }

  /**
   * Perform the compilation.
   */
  protected void compile() {

      /* If "tom.home" is defined in the ant project, pass it to tom */
      String tom_home = getProject().getProperty("tom.home");
      if (tom_home != null) {
        System.setProperty("tom.home",tom_home);
      } else {
        log("\"tom.home\" is not defined, some features may not work");
      }

      if(srcFile != null) {
        javaRunner.createArg().setValue("--srcfile");
        javaRunner.createArg().setFile(getSrcfile());
      }
      if(destDir != null) {
        javaRunner.createArg().setValue("--destdir");
        javaRunner.createArg().setFile(getDestdir());
      }
      if(packagePrefix != null) {
        javaRunner.createArg().setValue("--package");
        javaRunner.createArg().setValue(getPackage());
      }

      int err = -1;
      javaRunner.setFork(getFork());
      javaRunner.setClassname("tom.pom.Pom");
      err = javaRunner.executeJava();
      if(err != 0) {
          throw new BuildException("Pom returned: " + err, getLocation());
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
}
