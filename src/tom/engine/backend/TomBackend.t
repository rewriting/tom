package jtom.backend;

import java.io.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.adt.options.types.*;
import jtom.tools.*;

/**
 * The TomBackend plugin.
 */
public class TomBackend extends TomGenericPlugin //Base implements TomPlugin
{
    %include { ../adt/TomSignature.tom }
    %include{ ../adt/Options.tom }

//     private TomTerm term;
//     private TomOptionList myOptions;

    private final static int defaultDeep = 2;
    private TomAbstractGenerator generator;
    private Writer writer;


    public TomBackend()
    {
	myOptions = `concTomOption(OptionBoolean("noOutput", "", "Do not generate code", False()), // desactivation flag
				OptionBoolean("jCode", "j", "Generate Java code", True()),
				OptionBoolean("cCode", "c", "Generate C code", False()),
				OptionBoolean("eCode", "e", "Generate Eiffel code", False()),
				OptionBoolean("camlCode", "", "Generate Caml code", False()));
    }

//     public void setInput(ATerm term)
//     {
// 	if (term instanceof TomTerm)
// 	    this.term = (TomTerm)term;
// 	else
// 	    environment().messageError(TomMessage.getString("TomTermExpected"),
// 				       "TomParserPlugin", TomMessage.DEFAULT_ERROR_LINE_NUMBER);
//     }

//     public ATerm getOutput()
//     {
// 	return term;
//     }

    public void run()
    {
	if(amIActivated() == true)
	    {
		try
		    {
			long startChrono = System.currentTimeMillis();
			TomOptionList list = `concTomOption(myOptions*);
			boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(environment().getOutputFile())));
			
			OutputCode output = new OutputCode(writer, defaultDeep);
			
			while(!(list.isEmpty()))
			    {
				TomOption h = list.getHead();
				%match(TomOption h)
				    {
					OptionBoolean[name="jCode", valueB=True()] -> 
					    { 
						generator = new TomJavaGenerator(output);
						break;
					    }
					OptionBoolean[name="cCode", valueB=True()] -> 
					    { 
						generator = new TomCGenerator(output);
						break;
					    }
					OptionBoolean[name="eCode", valueB=True()] -> 
					    { 
						generator = new TomEiffelGenerator(output);
						break;
					    }
					OptionBoolean[name="camlCode", valueB=True()] -> 
					    { 
						generator = new TomCamlGenerator(output);
						break;
					    }
				    }
				list = list.getTail();
			    }
			
			generator.generate(defaultDeep, term);
			
			if(verbose)
			    System.out.println("TOM generation phase (" +(System.currentTimeMillis()-startChrono)+ " ms)");

			writer.close();

			environment().printAlertMessage("TomBackend");
			if(!environment().isEclipseMode()) {
			    // remove all warning (in command line only)
			    environment().clearWarnings();
			}
		    }
		catch (Exception e)
		    {
			environment().messageError("Exception occurs in TomBackend Init: "+e.getMessage(), 
						   environment().getInputFile().getName(), TomMessage.DEFAULT_ERROR_LINE_NUMBER);
			e.printStackTrace();
		    }
	    }
	else // backend desactivated
	    {
		boolean verbose = ((Boolean)getServer().getOptionValue("verbose")).booleanValue();
		
		if(verbose)
		    {
			System.out.println("The backend is not activated and thus WILL NOT RUN.");
			System.out.println("No output !");
		    }
	    }
    }

//     public TomOptionList declareOptions()
//     {
// 	return myOptions;
//     }

//     public TomOptionList requiredOptions()
//     {
// 	return `emptyTomOptionList();
//     }

    public void setOption(String optionName, String optionValue)
    {
 	%match(TomOptionList myOptions)
 	    {
		concTomOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{			    
			    %match(String optionValue)
				{
				    ('true') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, True())); }
				    ('false') ->
					{ myOptions = `concTomOption(av*, ap*, OptionBoolean(n, alt, desc, False())); }
				}

			    if(optionValue.equals("true")) // no more than 1 type of code can be activated at a time
				{
				    if(n.equals("jCode"))
					{ 
					    //System.out.println("Java code activated, other codes desactivated");
					    setOption("cCode","false");
					    setOption("eCode","false");
					    setOption("camlCode","false"); 
					}
				    else if(n.equals("cCode"))
					{ 
					    //System.out.println("C code activated, other codes desactivated");
					    setOption("jCode","false");
					    setOption("eCode","false");
					    setOption("camlCode","false"); 
					}
				    else if(n.equals("eCode"))
					{ 
					    //System.out.println("Eiffel code activated, other codes desactivated");
					    setOption("jCode","false");
					    setOption("cCode","false");
					    setOption("camlCode","false"); 
					}
				    else if(n.equals("camlCode"))
					{ 
					    //System.out.println("Caml code activated, other codes desactivated");
					    setOption("jCode","false");
					    setOption("cCode","false");
					    setOption("eCode","false"); 
					}
				}
			}
		}
		concTomOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concTomOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concTomOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
    }


    private boolean amIActivated()
    {
	TomOptionList list = `concTomOption(myOptions*);
	
	while(!(list.isEmpty()))
	    {
		TomOption h = list.getHead();
		%match(TomOption h)
		    {
			OptionBoolean[name="noOutput", valueB=True()] -> 
			    { 
				return false;
			    }
			OptionBoolean[name="noOutput", valueB=False()] -> 
			    { 
				return true;
			    }
		    }
		list = list.getTail();
	    }
	return false; // there's a problem if we're here so I guess it's better not to activate the plugin (maybe raise an error ?)
    }
}
