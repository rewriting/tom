/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2004 INRIA
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

package jtom.tools.ant;

import java.io.File;
import java.util.Vector;
import jtom.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;

/**
 * Compiles Tom source files. This task can take the following
 * arguments:
 * <ul>
 * <li>configfile
 * <li>sourcedir
 * <li>destdir
 * <li>outputfile
 * <li>optimize
 * <li>debug
 * <li>verbose
 * <li>failonerror
 * </ul>
 * Of these arguments, the <b>sourcedir</b> and <b>destdir</b> are required.
 * <p>
 * When this task executes, it will recursively scan the sourcedir and
 * destdir looking for Java source files to compile. This task makes its
 * compile decision based on timestamp.
 */

public class TomTask extends MatchingTask {

  private static final String FAIL_MSG
  = "Compile failed; see the compiler error output for details.";

  private String options;
  private Path src;
  private File destDir;
  private File configFile;
  private File outputFile;
  private Path compileClasspath;
  private Path compileSourcepath;
  private boolean depend = false;
  private boolean verbose = false;
  private Path extdirs;
  private boolean nowarn = false;
  private boolean optimize = false;
  

  protected boolean failOnError = true;
  protected boolean listFiles = false;
  protected File[] compileList = new File[0];

  private File tmpDir;

    /**
     * Set the configuration file
     * @param  configFile the destination directory
     */
  public void setConfig(File configFile) {
    this.configFile = configFile;
  }

  public File getConfig() {
    return configFile;
  }

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
     * Set the destination directory into which the Tom source
     * files should be compiled.
     * @param destDir the destination directory
     */
  public void setOutputFile(File outputFile) {
    this.outputFile = outputFile;
  }

    /**
     * Gets the destination directory into which the Tom source files
     * should be compiled.
     * @return the destination directory
     */
  public File getOutputFile() {
    return outputFile;
  }

    /**
     * Set the sourcepath to be used for this compilation.
     * @param sourcepath the source path
     */
  public void setSourcepath(Path sourcepath) {
    if (compileSourcepath == null) {
      compileSourcepath = sourcepath;
    } else {
      compileSourcepath.append(sourcepath);
    }
  }

    /**
     * Gets the sourcepath to be used for this compilation.
     * @return the source path
     */
  public Path getSourcepath() {
    return compileSourcepath;
  }

    /**
     * Adds a path to sourcepath.
     * @return a sourcepath to be configured
     */
  public Path createSourcepath() {
    if (compileSourcepath == null) {
      compileSourcepath = new Path(getProject());
    }
    return compileSourcepath.createPath();
  }

    /**
     * Adds a reference to a source path defined elsewhere.
     * @param r a reference to a source path
     */
  public void setSourcepathRef(Reference r) {
    createSourcepath().setRefid(r);
  }

    /**
     * Set the classpath to be used for this compilation.
     *
     * @param classpath an Ant Path object containing the compilation classpath.
     */
  public void setClasspath(Path classpath) {
    if (compileClasspath == null) {
      compileClasspath = classpath;
    } else {
      compileClasspath.append(classpath);
    }
  }

    /**
     * Gets the classpath to be used for this compilation.
     * @return the class path
     */
  public Path getClasspath() {
    return compileClasspath;
  }

    /**
     * Adds a path to the classpath.
     * @return a class path to be configured
     */
  public Path createClasspath() {
    if (compileClasspath == null) {
      compileClasspath = new Path(getProject());
    }
    return compileClasspath.createPath();
  }

    /**
     * Adds a reference to a classpath defined elsewhere.
     * @param r a reference to a classpath
     */
  public void setClasspathRef(Reference r) {
    createClasspath().setRefid(r);
  }
  
    /**
     * Sets the extension directories that will be used during the
     * compilation.
     * @param extdirs a path
     */
  public void setExtdirs(Path extdirs) {
    if (this.extdirs == null) {
      this.extdirs = extdirs;
    } else {
      this.extdirs.append(extdirs);
    }
  }

    /**
     * Gets the extension directories that will be used during the
     * compilation.
     * @return the extension directories as a path
     */
  public Path getExtdirs() {
    return extdirs;
  }

    /**
     * Adds a path to extdirs.
     * @return a path to be configured
     */
  public Path createExtdirs() {
    if (extdirs == null) {
      extdirs = new Path(getProject());
    }
    return extdirs.createPath();
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
     * Gets the verbose flag.
     * @return the verbose flag
     */
  public boolean getVerbose() {
    return verbose;
  }

	/**
	 * If true, compiles with optimization enabled.
	 * @param optimize if true compile with optimization enabled
	 */
	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}
	
	public boolean getOptimize() {
		return optimize;
	}
	
	/**
	 * If true, enables the -nowarn option.
	 * @param flag if true, enable the -nowarn option
	 */
  public void setNowarn(boolean flag) {
    this.nowarn = flag;
  }

    /**
     * Should the -nowarn option be used.
     * @return true if the -nowarn option should be used
     */
  public boolean getNowarn() {
    return nowarn;
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
		}
		else {
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
    return compileList;
  }

    /**
     * Check that all required attributes have been set and nothing
     * silly has been entered.
     *
     * @since Ant 1.5
     * @exception BuildException if an error occurs
     */
  protected void checkParameters() throws BuildException {
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

      for (int i = 0; i < compileList.length; i++) {
        String filename = compileList[i].getAbsolutePath();
        if(verbose)
          System.out.println("Compiling " + compileList[i] + "...");
        
        File file = new File(filename);

        String cmd_line = "";
        if (options != null && getOptions().trim().length() > 0) {
          cmd_line = cmd_line.trim() + " " + options;
        }
        if (configFile != null) {
          cmd_line = cmd_line.trim() + " -X " + configFile;
        }
        if (destDir != null) {
          cmd_line = cmd_line.trim() + " -d " + destDir;
        }
        if (outputFile != null) {
          cmd_line = cmd_line.trim() + " -o " + outputFile;
        }
				if (optimize == true) {
          cmd_line = cmd_line.trim() + " --optimize";
				}
				if (nowarn == true) {
          cmd_line = cmd_line.trim() + " --noWarning";
				}
        cmd_line = cmd_line.trim() + " -I " + file.getParent();
        cmd_line = cmd_line.trim() + " " + filename;

        String[] cmd = split(cmd_line);
          //for(int k=0;k<cmd.length;k++) {System.out.println("k: "+cmd[k]);}
        int err = -1;
        err = TomServer.exec(cmd); // before it was Tom.exec(cmd)
        if (err != 0) {
          if (failOnError) {
            throw new BuildException("Tom returned: " + err, getLocation());
          } else {
            log("Tom Result: " + err, Project.MSG_ERR);
          }
          }
      }
    }
  }

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
  
}
