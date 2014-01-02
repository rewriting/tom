/*
 *
 * TOM - To One Matching Compiler
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine;

import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.platform.OptionManager;

/**
 * The TomStreamManager is intended to manage the input and output
 * streams for the tom compiler process.
 * The output file depends on the destdir, the package and the suffix.
 */
public class TomStreamManager {

  /**
   * The symbol table for the current platform execution
   * shouldnt be here but as the VasStreamManager is use for one execution...
   */
  private SymbolTable symbolTable;

  /** List of import paths. */
  private List<File> userImportList;

  /** Absolute path where file are generated. */
  private File destDir;

  /** the input file name */
  private String inputFileName;
  private String encoding;

  /** Absolute name of the output file (with extension). */
  private File outputFile;

  /** Absolute name of the output file (given in command line). */
  private File userOutputFile;

  /** Relative path which corresponds to the package defined in the input file (empty by default) */
  private String packagePath;

  /* in/out suffixes */
  private String inputSuffix;
  private String outputSuffix;

  /* subdir added to the import search path */
	private String importLanguageSubdir;

  /** list of non managed imported file */
  private Collection<String> importsToDiscard;

  public TomStreamManager(){
    symbolTable = new SymbolTable();
    importsToDiscard = new HashSet<String>();
    //importsToDiscard.add("boolean.tom");
    importsToDiscard.add("string.tom");
    importsToDiscard.add("int.tom");
    importsToDiscard.add("double.tom");
    importsToDiscard.add("aterm.tom");
    clear();
  }

  /**
   * Initializes the TomStreamManager.
   */
  private void clear() {
    destDir = null;
    inputFileName = null;
    encoding = null;
    outputFile = null;
    userOutputFile = null;
    packagePath = "";
    inputSuffix = ".t";
    outputSuffix = ".java";
		importLanguageSubdir = "java";
  }

  public void initializeFromOptionManager(OptionManager optionManager) {
    List<File> localUserImportList = new ArrayList<File>();
    String localDestDir = null;

    symbolTable.init(optionManager);
    // computes the input and output suffixes
    // well, it would be better in the future if we let the generator append
    // the output suffix itself
    // so that's only temporary

    if (((Boolean)optionManager.getOptionValue("cCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".tom.c";
			importLanguageSubdir = "c";
    } else if (((Boolean)optionManager.getOptionValue("camlCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".tom.ml";
			importLanguageSubdir = "caml";
    } else if (((Boolean)optionManager.getOptionValue("jCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".java";
			importLanguageSubdir = "java";
    } else if (((Boolean)optionManager.getOptionValue("pCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".py";
			importLanguageSubdir = "java";
    } else if (((Boolean)optionManager.getOptionValue("aCode")).booleanValue()) {
      inputSuffix = ".t";
      outputSuffix = ".adb";
			importLanguageSubdir = "ada";
    } else {
      throw new TomRuntimeException("No code generator selected");
    }

    // fills the local user import list
    String imports = (String)optionManager.getOptionValue("import");
    // paths are separated by File.pathSeparator
    StringTokenizer st = new StringTokenizer(imports, File.pathSeparator);
    try {
      while(st.hasMoreTokens()) {
        String next = st.nextToken();
        localUserImportList.add(new File(next).getCanonicalFile());
      }
    } catch (IOException e) {
      System.out.println("IO Exception when computing importList");
      e.printStackTrace();
    }

    // Setting importList
    setUserImportList(localUserImportList);

    // computes destdir
    localDestDir = (String)optionManager.getOptionValue("destdir");
    setDestDir(localDestDir);

    encoding = (String)optionManager.getOptionValue("encoding");

    String commandLineUserOutputFile = (String)optionManager.getOptionValue("output");
    if(commandLineUserOutputFile.length() > 0) {
      setUserOutputFile(commandLineUserOutputFile);
    }
  }

  public void prepareForInputFile(String localInputFileName) { // updateInputOutputFiles + init
    // compute inputFile:
    //  - add a suffix if necessary
    if((!localInputFileName.endsWith(getInputSuffix())) && (!localInputFileName.equals("-"))) {
      localInputFileName += getInputSuffix();
    }
    setInputFile(localInputFileName);

    // compute outputFile:
    //  - either use the given UserOutputFileName
    //  - either concatenate
    //    the outputDir
    //    [the packagePath] will be updated by the parser
    //    and reuse the inputFileName with a good suffix
    /* if using stdin, --output is mandatory */
    if(!isUserOutputFile() && (localInputFileName.equals("-"))) {
      TomMessage.error(getLogger(), null, 0, TomMessage.expectingOOptionWhenStdin);
      return;
    }
    if(isUserOutputFile()) {
      setOutputFile(getUserOutputFile().getPath());
    } else {
      try {
        String child = new File(getInputFileNameWithoutSuffix() + getOutputSuffix()).getName();
        File out = new File(getDestDir(),child).getCanonicalFile();
        setOutputFile(out.getPath());
      } catch (IOException e) {
        System.out.println("IO Exception when computing outputFile");
        e.printStackTrace();
      }
    }
  }

  /**
   * An accessor method.
   *
   * @return the symbolTable
   */
  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public String getOutputSuffix() {
    return outputSuffix;
  }

  public void setOutputSuffix(String string) {
    outputSuffix = string;
  }

  public void setUserImportList(List<File> list) {
    userImportList = list;
  }

  public List<File> getUserImportList() {
    return userImportList;
  }

  /**
   * dynamically compute the list of imported files:
   *  - user defined imports
   *  - destDir/packagePath
   *  - inputFile.getParent
   *  - TOM_HOME/share/tom
   *  - TOM_HOME/share/tom/importLanguageSubdir (i.e [java|c|caml])
   */
  public List<File> getImportList() {
    List<File> importList = new ArrayList<File>(getUserImportList().size()+3);
    for(File file : getUserImportList()) {
      importList.add(file);
    }
    try {
      File destAndPackage = new File(getDestDir(),getPackagePath());
      importList.add(destAndPackage.getCanonicalFile());
      importList.add(getInputParentFile().getCanonicalFile());
      String tom_home = System.getProperty("tom.home");
      if(tom_home != null) {
        File file = new File(new File(tom_home,"share"),"tom");
        //System.out.println(" extend import list with: " + file.getPath());
        importList.add(file.getCanonicalFile());
        file = new File(file,importLanguageSubdir);
        //System.out.println(" extend import list with: " + file.getPath());
        importList.add(file.getCanonicalFile());
      }
      //System.out.println("importList = " + importList);
    } catch (IOException e) {
      System.out.println("IO Exception when computing importList");
      e.printStackTrace();
    }
    return importList;
  }

  public String getInputSuffix() {
    return inputSuffix;
  }

  public void setInputSuffix(String inputSuffix) {
    this.inputSuffix = inputSuffix;
  }

  public void setPackagePath(String packagePath) {
    this.packagePath = packagePath.replace('.',File.separatorChar);
    updateOutputFileOnPackageChanged();
  }

  public String getPackagePath() {
    return packagePath;
  }

  public void setDestDir(String destDir) {
    try {
      this.destDir = new File(destDir).getCanonicalFile();
    } catch (IOException e) {
      System.out.println("IO Exception using file `" + destDir + "`");
      e.printStackTrace();
    }
  }

  public File getDestDir() {
    return destDir;
  }

  public void setInputFile(String sInputFile) {
    this.inputFileName = sInputFile;
  }

  public String getInputFileName() {
    return inputFileName;
  }

  public Reader getInputReader() {
    try {
      if(!inputFileName.equals("-")) {
        return new BufferedReader(new InputStreamReader(
              new FileInputStream(
                new File(inputFileName).getCanonicalFile()),encoding));
      } else {
        return new BufferedReader(new InputStreamReader(System.in));
      }
    } catch (FileNotFoundException e) {
      TomMessage.error(getLogger(), null, 0, TomMessage.fileNotFound, 
          inputFileName);
    } catch (IOException e) {
      System.out.println("IO Exception using file `" + inputFileName + "`");
      e.printStackTrace();
    }
    throw new TomRuntimeException("Stopped.");
  }

  public File getInputParentFile() {
    File parent = null;
    try {
      parent = (new File(getInputFileName())).getCanonicalFile().getParentFile();
    } catch (IOException e) {
      System.out.println("IO Exception using file `" + getInputFileName() + "`");
      e.printStackTrace();
    }
    return parent;
  }

  public String getInputFileNameWithoutSuffix() {
    String res = inputFileName.substring(0, inputFileName.length() - getInputSuffix().length());
    //System.out.println("IFNWS : " +res);
    return res;
  }

  public String getOutputFileName() {
    return getOutputFile().getPath();
  }

  public void setOutputFile(String sOutputFile) {
    try {
      this.outputFile = new File(sOutputFile).getCanonicalFile();
      this.outputFile.getParentFile().mkdirs();
    } catch (IOException e) {
      System.out.println("IO Exception using file `" + sOutputFile + "`");
      e.printStackTrace();
    }
  }

  public File getOutputFile() {
    return outputFile;
  }

  /**
   * update the outputFile by inserting the packagePath
   * between the destDir and the fileName
   */
  private void updateOutputFileOnPackageChanged() {
    if(!isUserOutputFile()) {
      File out = new File(getOutputFile().getParentFile(),getPackagePath());
      setOutputFile(new File(out, getOutputFile().getName()).getPath());
    }
  }

  public boolean isUserOutputFile() {
    return userOutputFile != null;
  }

  public void setUserOutputFile(String sUserOutputFile) {
    try {
      this.userOutputFile = new File(sUserOutputFile).getCanonicalFile();
    } catch (IOException e) {
      System.out.println("IO Exception using file `" + sUserOutputFile + "`");
      e.printStackTrace();
    }
  }

  public File getUserOutputFile() {
    return userOutputFile;
  }

  public String getRawFileName() {
    String rawInputFileName = new File(getInputFileName()).getName();
    return rawInputFileName.substring(0, rawInputFileName.length() - getInputSuffix().length());
  }

  public boolean isSilentDiscardImport(String fileName) {
    return importsToDiscard.contains(fileName);
  }

  public File findFile(File parent, String fileName) {
    File file = new File(parent, fileName);
    if(file.exists()) {
      return file;
    }
    // Look for importList
    for(int i=0 ; i<getImportList().size() ; i++) {
      file = new File(getImportList().get(i),fileName);
      if(file.exists()) {
        return file;
      }
    }
    return null;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
