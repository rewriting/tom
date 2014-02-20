/*
 * Gom
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
 * Antoine Reilles           e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom;

import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import tom.platform.OptionManager;

public class GomStreamManager {

  /** List of import paths. */
  private List<File> userImportList;

  /** Absolute path where the file is generated. */
  private File destDirFile;

  /** The input file name (relative path, or -) */
  private String inputFileName;
  private String intermediateName;

  /** The input stream */
  private Reader inputReader;

  /**
   * Relative path which corresponds to the package where to generate the java
   * classes (empty by default).
   * */
  private String defaultPackagePath = "";
  private HashMap<String,String> packagePathMap = new HashMap<String,String>();

  /** Suffixes */
  private String inputSuffix;

  public GomStreamManager() {
    clear();
  }

  /** Reinitializes the GomEnvironment instance. */
  public void clear() {
    userImportList = new ArrayList<File>();
    destDirFile = null;
    inputFileName = "";
    inputReader = null;
    defaultPackagePath = "";
    inputSuffix = ".gom";
    packagePathMap.clear();
  }

  public void initializeFromOptionManager(OptionManager optionManager) {
    // fills the local user import list
    String imports = (String)optionManager.getOptionValue("import");
    // paths are separated by ':'
    final StringTokenizer st = new StringTokenizer(imports, ":");
    while (st.hasMoreTokens() ) {
      String next = st.nextToken();
      userImportList.add(new File(next).getAbsoluteFile());
    }
    // computes destdir
    String dest = (String)optionManager.getOptionValue("destdir");
    if (dest.length() > 0 ) {
      // note : destdir is `.` by default
      setDestDir(dest);
    }
    // package and name for apigen
    String pack = (String)optionManager.getOptionValue("package");
    if (pack.length() > 0) {
      setDefaultPackagePath(pack);
    }
    // output file name for intermediate
    String intermediateName =
      (String)optionManager.getOptionValue("intermediateName");
    if (intermediateName.length() > 0) {
      this.intermediateName = intermediateName;
    }
  }

  public void prepareForInputFile(String localInputFileName) {
    // update Input file
    if ((!localInputFileName.endsWith(getInputSuffix())) && (!localInputFileName.equals("-"))) {
      localInputFileName += getInputSuffix();
    }
    setInputFile(localInputFileName);
  }

  /**
   * compute the list of imported files:
   *  - user defined imports
   *  - inputFile.getParent
   *  - TOM_HOME/share/gom
   */
  public List<File> getImportList() {
    List<File> importList = new ArrayList<File>(getUserImportList().size()+2);
    for (File file : getUserImportList()) {
      importList.add(file);
    }
    try {
      File inputParent = getInputParent();
      if (inputParent != null) {
        importList.add(inputParent);
      }
      String gomHome = System.getProperty("tom.home");
      if(gomHome != null) {
        File file = new File(gomHome,"share/share");
        importList.add(file.getCanonicalFile());
      }
    } catch (IOException e) {
      System.out.println("IO Exception when computing importList");
      e.printStackTrace();
    }
    return importList;
  }

  public List<File> getUserImportList() {
    return userImportList;
  }

  public void setUserImportList(List<File> list) {
    userImportList = list;
  }

  public File getDestDir() {
    return destDirFile;
  }

  public void setDestDir(String destDirName) {
    try {
      this.destDirFile = new File(destDirName).getCanonicalFile();
    } catch (IOException e) {
      GomMessage.error(getLogger(), null, 0,
          GomMessage.iOExceptionManipulation, destDirFile, e.getMessage());
    }
  }

  public String getOutputFileName() {
    /* if intermediateName is set and we use stdin, use it, otherwise use "-" */
    if (inputFileName.equals("-") && null != intermediateName) {
      return intermediateName;
    } else {
      return inputFileName;
    }
  }

  public String getInputFileName() {
    return inputFileName;
  }

  private void setInputFile(String sInputFile) {
    this.inputFileName = sInputFile;
  }

  public Reader getInputReader() {
    try {
      if (!inputFileName.equals("-")) {
        inputReader = new BufferedReader(
            new FileReader(
              new File(inputFileName).getCanonicalFile()));
      } else {
        GomMessage.finer(getLogger(), null, 0, GomMessage.systemInAsInput);
        inputReader = new BufferedReader(new InputStreamReader(System.in));
      }
    } catch (FileNotFoundException e) {
      GomMessage.error(getLogger(),null,0, GomMessage.fileNotFound,
          inputFileName);
    } catch (IOException e) {
      GomMessage.error(getLogger(), null, 0,
          GomMessage.iOExceptionManipulationInputReader,
          inputFileName, e.getMessage());
    }
    return inputReader;
  }

  public File getInputParent() {
    File parent = null;
    try {
      File rawParent = (new File(inputFileName)).getParentFile();
      if (rawParent != null) {
        parent = rawParent.getCanonicalFile();
      }
    } catch (IOException e) {
      GomMessage.error(getLogger(), null, 0, GomMessage.iOExceptionManipulationInputParent, 
          inputFileName, e.getMessage());
    }
    return parent;
  }

  public String getInputSuffix() {
    return inputSuffix;
  }
  public void setInputSuffix(String inputSuffix) {
    this.inputSuffix = inputSuffix;
  }

  private void setDefaultPackagePath(String packagePath) {
    this.defaultPackagePath = packagePath.replace('.',File.separatorChar);
  }

  public String getDefaultPackagePath() {
    return defaultPackagePath.replace(File.separatorChar,'.');
  }

  public String getPackagePath(String moduleName) {
    if(getDefaultPackagePath().length() > 0) {
      // if a default package exists, return it
      // ignore the prefix of each module
      return getDefaultPackagePath();
    }
    String res = packagePathMap.get(moduleName);
    if(res==null) {
      res = getDefaultPackagePath();
    }
    return res.replace(File.separatorChar,'.');
  }

  public void associatePackagePath(String moduleName,String path) {
    String pkg = path.replace('.',File.separatorChar);
    if(!"".equals(defaultPackagePath)) {
      pkg = this.defaultPackagePath + File.separatorChar + pkg;
    }
    packagePathMap.put(moduleName,pkg);
  }

  public File findModuleFile(String extendedModuleName) {
    // look for import list
    File f = null;
    List imports = getImportList();
    boolean found = false;
    for(int i=0 ; !found && i<imports.size() ; i++) {
      f = new File((File)imports.get(i), extendedModuleName);
      found = f.exists();
    }
    if(!found) {
      return null;
    }
    return f;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
