package jtom.verifier;

import aterm.*;

import jtom.tools.*;
import java.util.*;
import jtom.TomEnvironment;
import jtom.adt.tomsignature.types.*;
import jtom.adt.tomsignature.*;
import jtom.exception.*;

public class TomVerifierExtract extends TomTask {

	// ------------------------------------------------------------
	%include { ../../adt/TomSignature.tom }
	// ------------------------------------------------------------

	public TomVerifierExtract(TomEnvironment tomEnvironment) { 
		super("Tom verifier", tomEnvironment);
	}
	
  public TomVerifierExtract(String name, TomEnvironment tomEnvironment) {
    super(name, tomEnvironment);
  }

	protected void process() {
		try {
			long startChrono = 0;
			boolean verbose = getInput().isVerbose();
			if(verbose) { startChrono = System.currentTimeMillis(); }
			// I may use my own datatype
			TomTerm extractTerm = `emptyTerm();
			// here the extraction stuff


			if(verbose) {
				System.out.println("TOM Verifier first extraction phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
			}
			// put extrated data in a file
			Tools.generateOutput(
				getInput().getBaseInputFileName() + TomTaskInput.verifExtractionSuffix, 
				extractTerm);
		} catch (Exception e) {
			addError("Exception occured in TomVerifierExtract: "+ e.getMessage(), 
							 getInput().getInputFileName(), 0, 0);
			e.printStackTrace();
			return;
		}
	}

}