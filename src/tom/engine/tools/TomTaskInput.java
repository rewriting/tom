/*
  
	TOM - To One Matching Compiler

	Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
				 Nancy, France.

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

	Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom.tools;

import jtom.adt.TomTerm;

public class TomTaskInput {
  private TomTerm trm;
  public String inputFileName = "",
    outputFileName = "",
    outputSuffix    = ".java",
    parsedSuffix    = ".tfix.parsed",
    expandedSuffix  = ".tfix.expanded",
    compiledSuffix  = ".tfix.compiled",
    optimizedSuffix  = ".tfix.optimized",
    parsedTableSuffix = ".tfix.parsed.table",
    expandedTableSuffix = ".tfix.expanded.table",
    debugTableSuffix = ".tfix.debug.table";
  
  private boolean needDebugExpansion = false, 
    verbose = false, // a verbose mode (Show duration for each phase)
    intermediate = false, // generate intermediate files
    debugMode = false, // generate debug primitives
    jCode = true,  // generate Java
    cCode = false, // generate C
    eCode = false, // generate Eiffel
    supportedGoto  = true, // if the target language has gotos
    supportedBlock = true, // if the target language has blocks
    doParse   = true, // parse a *.t file
    doExpand  = true, // expand the AST
    doCompile = true, // compile the AST
    printOutput = true, // print the output file using a TomGenerator instance
    doOnlyCompile = false, // try to start from an intermediate file
    doCheck = true, // verify after parsing (syntax checking) and expansion (type checking)
    strictType = true, // no universal type
    genDecl = true, // generate declarations
    doOptimization = false, // optimize generated code
    warningAll = false, // print warning error messages
    noWarning = false, // print only warning stopping the compilation
    staticFunction = false, // generate static functions
    debugMemory = false, // generate debug primitives
    pretty = false; // Synchronize TL code and source code
  
  
    //public static boolean doOptimization = false; // optimize generated code
  public TomTaskInput() {
  }
  
  public void setTerm(TomTerm trm) {
  	this.trm = trm;
  }
  public TomTerm getTerm() {
  	return trm;
  }
  public boolean needDebugExpansion() {
	 return needDebugExpansion;
   }
   public void needDebugExpansion(boolean need) {
	needDebugExpansion = need;
   }
   
  public String getOutputFileName() {
	return outputFileName;
  }

  public void setOutputFileName(String string) {
	outputFileName = string;
  }

  public boolean isVerbose() {
	return verbose;
  }

  public void setVerbose(boolean b) {
	verbose = b;
  }

  public boolean isIntermediate() {
	return intermediate;
  }

  public void setIntermediate(boolean b) {
	intermediate = b;
  }

  public boolean isDebugMode() {
	return debugMode;
  }


  public void setDebugMode(boolean b) {
	debugMode = b;
  }

  public boolean isPrintOutput() {
	return printOutput;
  }

  public void setPrintOutput(boolean b) {
	printOutput = b;
  }

  public String getOutputSuffix() {
	return outputSuffix;
  }
  
  public void setOutputSuffix(String string) {
	outputSuffix = string;
  }

  public boolean isCCode() {
	return cCode;
  }

  public boolean isECode() {
	return eCode;
  }

  public boolean isJCode() {
	return jCode;
  }

  public void setCCode(boolean b) {
	cCode = b;
  }

  public void setECode(boolean b) {
	eCode = b;
  }

  public void setJCode(boolean b) {
	jCode = b;
  }

  public boolean isSupportedBlock() {
	return supportedBlock;
  }

  public boolean isSupportedGoto() {
	return supportedGoto;
  }

  public void setSupportedBlock(boolean b) {
	supportedBlock = b;
  }

  public void setSupportedGoto(boolean b) {
	supportedGoto = b;
  }


public boolean isDoCheck() {
	return doCheck;
}

public boolean isDoCompile() {
	return doCompile;
}

public boolean isDoExpand() {
	return doExpand;
}

public boolean isDoOnlyCompile() {
	return doOnlyCompile;
}

public boolean isDoParse() {
	return doParse;
}

public boolean isGenDecl() {
	return genDecl;
}


public boolean isStrictType() {
	return strictType;
}


public void setDoCheck(boolean b) {
	doCheck = b;
}


public void setDoCompile(boolean b) {
	doCompile = b;
}

/**
 * @param b
 */
public void setDoExpand(boolean b) {
	doExpand = b;
}

/**
 * @param b
 */
public void setDoOnlyCompile(boolean b) {
	doOnlyCompile = b;
}

public void setDoParse(boolean b) {
	doParse = b;
}


public void setGenDecl(boolean b) {
	genDecl = b;
}

public void setStrictType(boolean b) {
	strictType = b;
}

public boolean isDoOptimization() {
	return doOptimization;
}

public void setDoOptimization(boolean b) {
	doOptimization = b;
}

public boolean isDebugMemory() {
	return debugMemory;
}

public boolean isNoWarning() {
	return noWarning;
}

public boolean isStaticFunction() {
	return staticFunction;
}

public boolean isWarningAll() {
	return warningAll;
}

public void setDebugMemory(boolean b) {
	debugMemory = b;
}

public void setNoWarning(boolean b) {
	noWarning = b;
}

public void setStaticFunction(boolean b) {
	staticFunction = b;
}

public void setWarningAll(boolean b) {
	warningAll = b;
}

public boolean isPretty() {
	return pretty;
}

public void setPretty(boolean b) {
	pretty = b;
}

} // class TomTaskInput
