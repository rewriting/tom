/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

package jtom;

import java.io.*;
import java.text.*;
import java.util.*;

import jtom.tools.*;
import jtom.exception.*;

import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.*;
import jtom.adt.options.types.*;

import aterm.*;
import aterm.pure.*;

public class TomEnvironment
{
    private SymbolTable symbolTable;
    private TomAlertList errors;
    private TomAlertList warnings;

    /**
     * list of import paths
     */
    private List userImportList;
    /**
     * absolute path where file are generated  
     */ 
    private File destDir;
    /**
     * absolute name of the input file (with extension) 
     */
    private File inputFile;
    /**
     * absolute name of the output file (with extension) 
     */
    private File outputFile;
    /**
     * absolute name of the output file (given in command line) 
     */
    private File userOutputFile;
    /**
     * relative path which corresponds to the package defined in the input file (empty by default) 
     */
    private String packagePath;
    /**
     * Eclipse mode for error management
     */
    private boolean eclipseMode; 
    private String inputSuffix;
    private String outputSuffix;

    private Collection importsToDiscard;

    /**
     * 
     */
    private ASTFactory astFactory;

    /**
     * 
     */
    private TomSignatureFactory tomSignatureFactory;

    /**
     * 
     */
    private OptionsFactory optionsFactory;

    /**
     * An accessor method.
     * 
     * @return an ASTFactory
     */
    public ASTFactory getASTFactory() { return astFactory; }
    
    /**
     * An accessor method.
     * 
     * @return a TomSignatureFactory
     */
    public TomSignatureFactory getTomSignatureFactory() { return tomSignatureFactory; }

  /**
   * An accessor method.
   * 
   * @return an OptionsFactory
   */
  public OptionsFactory getOptionsFactory() { return optionsFactory; }

  /**
   * Part of the Singleton pattern. The unique instance of the TomServer.
   */
  private static TomEnvironment instance = null;
    
  /**
   * Part of the Singleton pattern. A protected constructor method, that exists to defeat instantiation.
   */
  protected TomEnvironment(){}
    
  /**
   * Part of the Singleton pattern. Returns the instance of the TomServer if it has been initialized before,
   * otherwise it throws a TomRuntimeException.
   * 
   * @return the instance of the TomServer
   * @throws TomRuntimeException if the TomServer hasn't been initialized before the call
   */
  public static TomEnvironment getInstance() {
    if(instance == null) {
      throw new TomRuntimeException(TomMessage.getString("GetInitializedTomServerInstance"));
    }
    return instance;
  }

  /**
   * Part of the Singleton pattern. Initializes the TomEnvironment in case it hasn't been done before,
   * otherwise it reinitializes it.
   * 
   * @return the instance of the TomEnvironment
   */
  public static TomEnvironment create() {
    if(instance == null) {
      instance = new TomEnvironment();

      instance.tomSignatureFactory = TomSignatureFactory.getInstance(SingletonFactory.getInstance());
      instance.astFactory = new ASTFactory(instance.tomSignatureFactory);
      instance.optionsFactory = OptionsFactory.getInstance(SingletonFactory.getInstance());
		
      instance.symbolTable = new SymbolTable(instance.astFactory);

      instance.errors = instance.tomSignatureFactory.makeTomAlertList();
      instance.warnings = instance.tomSignatureFactory.makeTomAlertList();

      instance.inputSuffix = ".t";
      instance.outputSuffix = ".java";
      instance.userOutputFile = null;
      instance.eclipseMode = false;

      instance.importsToDiscard = new HashSet();
      instance.importsToDiscard.add("string.tom");
      instance.importsToDiscard.add("int.tom");
      instance.importsToDiscard.add("double.tom");
      instance.importsToDiscard.add("aterm.tom");
      instance.importsToDiscard.add("atermlist.tom");
  
      return instance;
    } else {
      TomEnvironment.clear();
      return instance;
    }
  }

  /**
   * Reinitializes the TomEnvironment instance.
   */
  public static void clear() {
    instance.symbolTable.init();
    instance.errors = instance.tomSignatureFactory.makeTomAlertList();
    instance.warnings = instance.tomSignatureFactory.makeTomAlertList();
		instance.destDir = null;
		instance.inputFile = null;
		instance.outputFile = null;
		instance.userOutputFile = null;
		instance.packagePath = null;
		instance.eclipseMode = false;
		instance.inputSuffix = ".t";
		instance.outputSuffix = ".java";
  }

    public void updateEnvironment(String localInputFileName) // updateInputOutputFiles + init
    {
	symbolTable.init();
	getTomSignatureFactory().makeTomAlertList();
	warnings = getTomSignatureFactory().makeTomAlertList();
	
	// compute inputFile:
	//  - add a suffix if necessary
	if(!localInputFileName.endsWith(getInputSuffix())) {
	    localInputFileName += getInputSuffix();
	}
	setInputFile(localInputFileName);
    	
	// compute outputFile:
	//  - either use the given UserOutputFileName
	//  - either concatenate
	//    the outputDir
	//    [the packagePath] will be updated by the parser
	//    and reuse the inputFileName with a good suffix
	if(isUserOutputFile()) {
	    setOutputFile(getUserOutputFile().getPath());
	} else {
	    String child = new File(getInputFileNameWithoutSuffix() + getOutputSuffix()).getName();
	    File out = new File(getDestDir(),child).getAbsoluteFile();
	    setOutputFile(out.getPath());
	}
    }

    public void initInputFromArgs() // must find a more appropriate name
    {
	List localUserImportList = new ArrayList();
	String localDestDir = null;

	// computes the input and output suffixes
	// well, it would be better in the future if we let the generator append the output suffix itself
	// so that's only temporary
	if ( getServer().getOptionBooleanValue("jCode") )
	    {
		inputSuffix = ".t";
		outputSuffix = ".java";
	    }
	else if ( getServer().getOptionBooleanValue("cCode") )
	    {
		inputSuffix = ".t";
		outputSuffix = ".tom.c";
	    }
	else if ( getServer().getOptionBooleanValue("camlCode") )
	    {
		inputSuffix = ".t";
		outputSuffix = ".tom.ml";
	    }
	else if ( getServer().getOptionBooleanValue("eCode") )
	    {
		inputSuffix = ".t";
		outputSuffix = ".e";
	    }
	else // we should never ever be here...
	    {
		inputSuffix = ".t";
		outputSuffix = ".java";
	    }

	// fills the local user import list
	String imports = getServer().getOptionStringValue("import");
	StringTokenizer st = new StringTokenizer(imports, ":"); // paths are separated by ':'
	while( st.hasMoreTokens() )
	    {
		String next = st.nextToken();
		localUserImportList.add(new File(next).getAbsoluteFile());
	    }
	// Setting importList
	setUserImportList(localUserImportList);

	// for Eclipse...
	if ( getServer().getOptionBooleanValue("eclipse") )
	    setEclipseMode(true);

	// computes destdir
	localDestDir = getServer().getOptionStringValue("destdir");
	setDestDir(localDestDir);
            
	String userOutputFile = getServer().getOptionStringValue("output");
	if ( userOutputFile.length() > 0 ) {
	    setUserOutputFile( userOutputFile );
	}
    }

    public SymbolTable getSymbolTable() {
	return symbolTable;
    }

    public TomServer getServer()
    {
	return TomServer.getInstance();
    }

    public TomAlertList getErrors() {
	return errors;
    }

    public TomAlertList getWarnings() {
	return warnings;
    }

    public void clearErrors() {
  	errors = getTomSignatureFactory().makeTomAlertList();
    }
  
    public void clearWarnings() {
  	warnings = getTomSignatureFactory().makeTomAlertList();
    }
  
    private void setErrors(TomAlertList list) {
	errors = list;
    }

    public void setWarnings(TomAlertList list) {
	warnings = list;
    }

    public boolean hasError() {
	return getErrors().getLength()>0;
    }

    public boolean hasWarning() {
	return getWarnings().getLength()>0;
    }

    public void printErrorMessage() {
	if(!isEclipseMode()) {
	    TomAlertList errorList = getErrors();
	    while(!errorList.isEmpty()) {
		TomAlert error = errorList.getHead();
		System.out.println(MessageFormat.format(TomMessage.getString("MainErrorMessage"), new Object[]{error.getFile(), new Integer(error.getLine()), error.getMessage()}));
		errorList = errorList.getTail();
	    }
	}
    }

    public void printWarningMessage() {
	if(!isEclipseMode() && !((Boolean)getServer().getOptionValue("noWarning")).booleanValue()) {
	    TomAlertList warningList = getWarnings();
	    while(!warningList.isEmpty()) {
		TomAlert warning = warningList.getHead();
		System.out.println(MessageFormat.format(TomMessage.getString("MainWarningMessage"), new Object[]{warning.getFile(), new Integer(warning.getLine()), warning.getMessage()}));
		warningList= warningList.getTail();
	    }
	}
    }

    public void printAlertMessage(String taskName) {
	if(!isEclipseMode()) {
	    printErrorMessage();
	    printWarningMessage();
	    if(hasError()) {
		System.out.println(MessageFormat.format(TomMessage.getString("TaskErrorMessage"),
							new Object[]{taskName, new Integer(getErrors().getLength()), new Integer(getWarnings().getLength())}));
	    } else if(hasWarning()) {
		System.out.println(MessageFormat.format(TomMessage.getString("TaskWarningMessage"),
							new Object[]{taskName, new Integer(getWarnings().getLength())}));
	    }
	}
    }

    public void messageError(int errorLine,
			     String fileName,
			     String structInfo,
			     int structInfoLine,
			     String msg,
			     Object[] msgArg) {
	String formatedMessage = 
	    MessageFormat.format(
				 TomMessage.getString("DetailErrorMessage"), 
				 new Object[]{
				     structInfo, 
				     new Integer(structInfoLine), 
				     MessageFormat.format(msg, msgArg)
				 });
	messageError(formatedMessage,fileName, errorLine);
    }
         
    
    public void messageError(String msg, Object[] args, String fileName, int errorLine) {
	String formatedMessage = MessageFormat.format(msg, args);
	messageError(formatedMessage,fileName, errorLine);
    }
  
    public void messageError(String formatedMessage, String file, int line) {
	TomAlert err = getTomSignatureFactory().makeTomAlert_Error(formatedMessage,file,line);
	setErrors(getTomSignatureFactory().makeTomAlertList(err, getErrors()));
    }

    public void messageWarning(int warningLine,
			       String fileName,
			       String structInfo,
			       int structInfoLine,
			       String msg,
			       Object[] msgArg) {
	String formatedMessage = 
	    MessageFormat.format(TomMessage.getString("DetailWarningMessage"), 
				 new Object[]{
				     structInfo, 
				     new Integer(structInfoLine), 
				     MessageFormat.format(msg, msgArg)
				 });
	messageWarning(formatedMessage,fileName, warningLine);
    }

    public void messageWarning(String msg, Object[] args, String fileName, int errorLine) {
	String formatedMessage = MessageFormat.format(msg, args);
	messageWarning(formatedMessage,fileName, errorLine);
    }

    public void messageWarning(String formatedMessage, String file, int line) {
	TomAlert err = getTomSignatureFactory().makeTomAlert_Warning(formatedMessage,file,line);
	setWarnings(getTomSignatureFactory().makeTomAlertList(err, getWarnings()));
    }

//////////////////// THIS IS WHERE THE OLD TOMTASKINPUT BEGINS /////////////////////////////////

    public String getOutputSuffix() {
	return outputSuffix;
    }
  
    public void setOutputSuffix(String string) {
	outputSuffix = string;
    }

    public boolean isEclipseMode() {
	return eclipseMode;
    }
    public void setEclipseMode(boolean b) {
	eclipseMode = b;
    }

    public void setUserImportList(List list) {
	userImportList = list;
    }

    public List getUserImportList() {
	return userImportList;
    }

    /**
     * dynamically compute the list of imported files:
     *  - user defined imports
     *  - destDir/packagePath
     *  - inputFile.getParent
     *  - TOM_HOME/share/jtom
     */
    public List getImportList() {
	List importList = new ArrayList(getUserImportList().size()+3);
	for(Iterator it=getUserImportList().iterator() ; it.hasNext() ;) {
	    importList.add(it.next());
	}
	try {
      
	    importList.add(new File(getDestDir(),getPackagePath()).getCanonicalFile());
      
	    importList.add(getInputFile().getParentFile().getCanonicalFile());
	    String tom_home = System.getProperty("tom.home");
	    if(tom_home != null) {
		File file = new File(new File(tom_home,"jtom"),"share");
		importList.add(file.getCanonicalFile());
		//System.out.println(" extend import list with: " + file.getPath());
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
	try {
	    this.inputFile = new File(sInputFile).getCanonicalFile();
	} catch (IOException e) {
	    System.out.println("IO Exception using file `" + sInputFile + "`");
	    e.printStackTrace();
	}

    }
  
    public File getInputFile() {
	return inputFile;
    }

    public String getInputFileNameWithoutSuffix() {
	String inputFileName = getInputFile().getPath();
	String res = inputFileName.substring(0, inputFileName.length() - getInputSuffix().length());
	return res;
    }
  
    public String getOutputFileNameWithoutSuffix() {
	String outputFileName = getOutputFile().getPath();
	String res = outputFileName.substring(0, outputFileName.length() - getOutputSuffix().length());
	return res;
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
    public void updateOutputFile() {
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
  	String inputFileName = getInputFile().getName();
	String res = inputFileName.substring(0, inputFileName.length() - getInputSuffix().length());
	return res;
    }

	public boolean isSilentDiscardImport(String fileName) {
		return importsToDiscard.contains(fileName);
	}

}
