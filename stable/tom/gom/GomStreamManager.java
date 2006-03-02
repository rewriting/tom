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
 
  /** Absolute name of the input file (with extension). */
  private File inputFile;

  /** Absolute name of the output file (with extension). */
  private File outputFile;

  /** 
   * Relative path which corresponds to the package where to generate the java
   * classes (empty by default).
   * */
  private String packagePath; 

  /** the module name */
  private String moduleName;
  
  /** Suffixes */
  private String inputSuffix;
  private String outputSuffix;
  
  public GomStreamManager() {
    clear();
  }
  
  /** Reinitializes the GomEnvironment instance. */
  public void clear() {
    userImportList = new ArrayList();
    destDirFile = null;
    inputFile = null;
    outputFile = null;
    packagePath = "";
    moduleName = "";
    inputSuffix = ".gom";
    outputSuffix = ".java";
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
  
  /*
   * update the outputFile by inserting the packagePath
   * between the destDirFile and the fileName
   */
  public void prepareForInputFile(String localInputFileName) {
    // update Input/Output files
    // compute inputFile : add a suffix if necessary
    if(!localInputFileName.endsWith(getInputSuffix())) {
      localInputFileName += getInputSuffix();
    }
    setInputFile(localInputFileName);
    
    String child = new File(getInputFileNameWithoutSuffix() + getOutputSuffix()).getName();
    File out = new File(getDestDir(), getPackagePath());
    out = new File(out, child);
    setOutputFile(out.getPath());
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
      importList.add(getInputFile().getParentFile().getCanonicalFile());
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
    String inputFileName = getInputFile().getPath();
    String res = inputFileName.substring(0, inputFileName.length() - getInputSuffix().length());
    return res;
  }

  public File getInputFile() {
    return inputFile;
  }  
  public void setInputFile(String sInputFile) {
    try {
      this.inputFile = new File(sInputFile).getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "IOExceptionManipulation",
                      new Object[]{sInputFile, e.getMessage()});
    }
  }

  public File getOutputFile() {
    return outputFile;
  }  
  public void setOutputFile(String sOutputFile) {
    try {
      this.outputFile = new File(sOutputFile).getCanonicalFile();
    } catch (IOException e) {
      getLogger().log(Level.SEVERE, "IOExceptionManipulation",
                       new Object[]{sOutputFile, e.getMessage()});
    }
  }

  public String getInputSuffix() {
    return inputSuffix;
  }
  public void setInputSuffix(String inputSuffix) {
    this.inputSuffix = inputSuffix;
  }

  public String getOutputSuffix() {
    return outputSuffix;
  }
  public void setOutputSuffix(String string) {
    outputSuffix = string;
  }

  /*
   * update the outputFile by inserting the packagePath
   * between the destDirFile and the fileName
   */
  public void updateOutputFile() {
    File out = new File(getOutputFile().getParentFile(),getPackagePath());
    setOutputFile(new File(out, getOutputFile().getName()).getPath());
  }
  
  public String getPackagePath() {
    return packagePath;
  }  
  public void setPackagePath(String packagePath) {
    this.packagePath = packagePath.replace('.',File.separatorChar);
  }

  public String getModuleName() {
    return moduleName;
  }
  public void setModuleName(String smoduleName) {
    this.moduleName = smoduleName;
    uponModuleNameChanged(smoduleName);
  }
  
  public void uponModuleNameChanged(String smoduleName) {
    String child;
    child = new File(smoduleName + getOutputSuffix()).getName();
    File out = new File(getDestDir(),child).getAbsoluteFile();
    setOutputFile(out.getPath());
    // take into account the package information 
    updateOutputFile();
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

  public void cleanOutput() {
    File foutputFile = getOutputFile();
    // try to delete previously generated code
    if(foutputFile.exists()) {
      foutputFile.delete();
    }
    File topAPIGenerationDir = new File(getDestDir().getPath());
    File tomGeneratedFile;
    //delete destDir+ package
    topAPIGenerationDir = new File(topAPIGenerationDir, getPackagePath());
    // delete destDir + apiname
    topAPIGenerationDir = new File(topAPIGenerationDir, getModuleName());
    tomGeneratedFile = new File(getDestDir(), getModuleName()+".tom");
    if(topAPIGenerationDir.exists()) {
    	topAPIGenerationDir.delete();
    }
    if(tomGeneratedFile.exists()) {
      tomGeneratedFile.delete();
    }
  }
  
  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
