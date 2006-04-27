/*
 * Gom
 * 
 * Copyright (c) 2000-2005, INRIA
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
import java.util.List;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.platform.OptionManager;

public class GomStreamManager {

  /** List of import paths. */
  private List userImportList;

  /** Absolute path where the file is generated. */
  private File destDirFile;

  /** The input file name (relative path, or -) */
  private String inputFileName;
  
  /** The input stream */
  private Reader inputReader;

  /** 
   * Relative path which corresponds to the package where to generate the java
   * classes (empty by default).
   * */
  private String packagePath;

  /** Suffixes */
  private String inputSuffix;
  
  public GomStreamManager() {
    clear();
  }

  /** Reinitializes the GomEnvironment instance. */
  public void clear() {
    userImportList = new ArrayList();
    destDirFile = null;
    inputFileName = "";
    inputReader = null;
    packagePath = "";
    inputSuffix = ".gom";
  }

  public void initializeFromOptionManager(OptionManager optionManager) {
    // fills the local user import list
    String imports = (String)optionManager.getOptionValue("import");
    // paths are separated by ':'
    StringTokenizer st = new StringTokenizer(imports, ":");
    while( st.hasMoreTokens() ) {
      String next = st.nextToken();
      userImportList.add(new File(next).getAbsoluteFile());
    }
    // computes destdir
    String dest = (String)optionManager.getOptionValue("destdir");
    if ( dest.length() > 0 ) {
      // note : destdir is `.` by default
      setDestDir(dest);
    }
    // package and name for apigen
    String pack = (String)optionManager.getOptionValue("package");
    if(pack.length() > 0) {
      setPackagePath(pack);
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
  public List getImportList() {
    List importList = new ArrayList(getUserImportList().size()+2);
    for(Iterator it=getUserImportList().iterator() ; it.hasNext() ;) {
      importList.add(it.next());
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

  public List getUserImportList() {
    return userImportList;
  }

  public void setUserImportList(List list) {
    userImportList = list;
  }

  public File getDestDir() {
    return destDirFile;
  }

  public void setDestDir(String destDirName) {
    try {
      this.destDirFile = new File(destDirName).getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "IOExceptionManipulation",
                       new Object[]{destDirFile, e.getMessage()});
    }
  }

  public String getInputFileNameWithoutSuffix() {
    String res = inputFileName.substring(0, inputFileName.length() - getInputSuffix().length());
    return res;
  }

  public String getInputFileName() {
    return inputFileName;
  }

  public void setInputFile(String sInputFile) {
    this.inputFileName = sInputFile;
  }

  public Reader getInputReader() {
    try {
      if (!inputFileName.equals("-")) {
        inputReader = new BufferedReader(
            new FileReader(
              new File(inputFileName).getCanonicalFile()));
      } else {
        getLogger().log(Level.FINER, "gom will use System.in as input");
        inputReader = new BufferedReader(new InputStreamReader(System.in));
      }
    } catch (FileNotFoundException e) {
      getLogger().log(Level.SEVERE, GomMessage.fileNotFound.getMessage(),
          new Object[]{inputFileName});
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "getInputReader:IOExceptionManipulation",
                      new Object[]{inputFileName, e.getMessage()});
    }
    return inputReader;
  }

  public File getInputParent() {
    File parent = null;
    try {
      parent = ((new File(inputFileName)).getParentFile()).getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "getInputParent:IOExceptionManipulation",
                      new Object[]{inputFileName, e.getMessage()});
    } catch (NullPointerException e) {
      // The file was not found
      return null;
    }
    return parent;
  }

  public String getInputSuffix() {
    return inputSuffix;
  }
  public void setInputSuffix(String inputSuffix) {
    this.inputSuffix = inputSuffix;
  }

  public String getPackagePath() {
    return packagePath;
  }

  public void setPackagePath(String packagePath) {
    this.packagePath = packagePath.replace('.',File.separatorChar);
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
