package jtom.backend;

import java.io.*;
import java.util.logging.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import jtom.tools.*;

/**
 * The TomBackend plugin.
 */
public class TomBackend extends TomGenericPlugin {

  %include { adt/TomSignature.tom }
  %include { adt/PlatformOption.tom }

  private final static int defaultDeep = 2;
  private TomAbstractGenerator generator;
  private Writer writer;

  public TomBackend() {
    super("TomBackend");
  }

  public void run() {
    if(isActivated() == true) {
      try {
	int errorsAtStart = getPluginPlatform().getStatusHandler().nbOfErrors();
	int warningsAtStart = getPluginPlatform().getStatusHandler().nbOfWarnings();

	long startChrono = System.currentTimeMillis();
				
	writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(environment().getOutputFile())));
			
	OutputCode output = new OutputCode(writer, defaultDeep);
			
	if( getOptionBooleanValue("jCode") ) {
	  generator = new TomJavaGenerator(output);
	} else if( getOptionBooleanValue("cCode") ) {
	  generator = new TomCGenerator(output);
	} else if( getOptionBooleanValue("eCode") ) {
	  generator = new TomEiffelGenerator(output);
	} else if( getOptionBooleanValue("camlCode") ) {
	  generator = new TomCamlGenerator(output);
	}
			
	generator.generate( defaultDeep, (TomTerm)getArg() );
	
	getLogger().log( Level.INFO,
			 "TomGenerationPhase",
			 new Integer((int)(System.currentTimeMillis()-startChrono)) );
	
	writer.close();

	printAlertMessage(errorsAtStart, warningsAtStart);
      }
      catch (Exception e) {
	getLogger().log( Level.SEVERE,
			 "ExceptionMessage",
			 new Object[]{environment().getInputFile().getName(), "TomBackend", e.getMessage()} );
	
	e.printStackTrace();
      }
    } else { // backend desactivated
	getLogger().log(Level.INFO, "The backend is not activated and thus WILL NOT RUN.\nNo output !");
    }
  }

  public PlatformOptionList declaredOptions() {
    String noOutput = "<OptionBoolean name=\"noOutput\" altName=\"\" description=\"Do not generate code\" value=\"false\"/>";
    String jCode = "<OptionBoolean name=\"jCode\" altName=\"j\" description=\"Generate Java code\" value=\"true\"/>";
    String cCode = "<OptionBoolean name=\"cCode\" altName=\"c\" description=\"Generate C code\" value=\"false\"/>";
    String eCode = "<OptionBoolean name=\"eCode\" altName=\"e\" description=\"Generate Eiffel code\" value=\"false\"/>";
    String camlCode = "<OptionBoolean name=\"camlCode\" altName=\"\" description=\"Generate Caml code\" value=\"false\"/>";
    return TomOptionManager.xmlToOptionList("<options>" +
                                            noOutput + " " +
                                            jCode + " " +
                                            cCode + " " +
                                            eCode + " " +
                                            camlCode + " " +
                                            "</options>");
  }

  public void setOption(String optionName, Object optionValue) {
    setOptionValue(optionName, optionValue);

    if(optionValue.equals(Boolean.TRUE)) // no more than 1 type of code can be activated at a time
	{
	    if( optionName.equals("jCode") || optionName.equals("j") )
		{ 
		    //System.out.println("Java code activated, other codes desactivated");
		    setOptionValue("cCode", Boolean.FALSE);
		    setOptionValue("eCode", Boolean.FALSE);
		    setOptionValue("camlCode", Boolean.FALSE); 
		}
	    else if( optionName.equals("cCode") || optionName.equals("c") )
		{ 
		    //System.out.println("C code activated, other codes desactivated");
		    setOptionValue("jCode", Boolean.FALSE);
		    setOptionValue("eCode", Boolean.FALSE);
		    setOptionValue("camlCode", Boolean.FALSE); 
		}
	    else if( optionName.equals("eCode") || optionName.equals("e") )
		{ 
		    //System.out.println("Eiffel code activated, other codes desactivated");
		    setOptionValue("jCode", Boolean.FALSE);
		    setOptionValue("cCode", Boolean.FALSE);
		    setOptionValue("camlCode", Boolean.FALSE); 
		}
	    else if( optionName.equals("camlCode") )
		{ 
		    //System.out.println("Caml code activated, other codes desactivated");
		    setOptionValue("jCode", Boolean.FALSE);
		    setOptionValue("cCode", Boolean.FALSE);
		    setOptionValue("eCode", Boolean.FALSE); 
		}
	}
    }

  private boolean isActivated() {
    return !getOptionBooleanValue("noOutput");
  }
}
