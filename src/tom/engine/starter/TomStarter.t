/*
 *
 * The TomStarter plugin.
 *
 */

package jtom.starter;

import java.util.*;

import jtom.*;
import jtom.adt.tnode.*;
import jtom.adt.tnode.types.*;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;

import jtom.runtime.*;
import jtom.runtime.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

public class TomStarter extends TomBase implements TomPlugin, TomStarterInterface
{
    %include{ ../adt/TNode.tom }
    %include{ ../adt/TomSignature.tom }

    private TomTerm term;
    private OptionList myOptions;

    private String[] argumentList;

    public TomStarter()
    {
	myOptions = `emptyOptionList();
    }

    public jtom.adt.tnode.Factory getTNodeFactory()
    {
 	return new jtom.adt.tnode.Factory(new PureFactory());
    }

    public void setInput(TomTerm term)
    {
	this.term = term;
    }

    public TomTerm getOutput()
    {
	return term;
    }

    public void run()
    {
	long startChrono = System.currentTimeMillis();
	OptionList list = `concOption(myOptions*);
	int verbose = ((Integer)getServer().getOptionValue("--verbose")).intValue();

	while(!(list.isEmpty()))
	    {
		Option h = list.getHead();
		%match(Option h)
		    {

		    }

		list = list.getTail();
	    }

	if(verbose == 1)
	    {
		System.out.println("2.1. Execution of the starter... " +(System.currentTimeMillis()-startChrono)+ "ms");
	    }
    }

    public OptionList declareOptions()
    {
	int i = 0;
	OptionList list = `concOption(myOptions*);
	while(!(list.isEmpty()))
	    {
		i++;
		list = list.getTail();
	    }

	System.out.println("1.1. The starter declares " +i+ " options.");

	return myOptions;
    }

    public OptionList requiredOptions()
    {
	return `emptyOptionList();
    }

    public TomServer getServer()
    {
	try
	    {
		return TomServer.getInstance();
	    }
	catch(Exception e)
	    {
		System.out.println(e.getMessage());
	    }
	return null;
    }

    public void setOption(Option option)
    {}

    public void setOption(String optionName, String optionValue)
    {
 	%match(OptionList myOptions)
 	    {
		concOption(av*, OptionBoolean(n, alt, desc, val), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			{
			    myOptions = `concOption(av*, ap*, OptionBoolean(n, alt, desc, Integer.parseInt(optionValue)));
			    // will become Boolean.valueOf() later on
			}
		}
		concOption(av*, OptionInteger(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionInteger(n, alt, desc, Integer.parseInt(optionValue), attr));
		}
		concOption(av*, OptionString(n, alt, desc, val, attr), ap*)
		    -> { if(n.equals(optionName)||alt.equals(optionName))
			myOptions = `concOption(av*, ap*, OptionString(n, alt, desc, optionValue, attr));
		}
	    }
    }

    /*
     * This method takes the arguments given by the user and deduces the options to set, then sets them.
     * @return an array containing the name of the input files
     */
    public String[] processArguments(String argumentList[])
    {
	    Vector inputFiles = new Vector();
	    boolean noMoreOptions = false;
	
	    for(int i = 0; i < argumentList.length; i++)
		{
		    String s = argumentList[i];
		
		    if(s.startsWith("-"))
			{
			    if(s.equals("--help")||s.equals("-h"))
				getServer().displayHelp();
			    if(s.equals("-X"))
				{
				    // if we're here, the TomServer has already handled the "-X" option
				    // and all errors that might occur
				    // just skip it,along with its argument
				    i++;
				    continue;
				}
			    if(noMoreOptions == true)
				// the user messed up and wrote a "lone argument" (see below)
				// in the middle of the options
				{
				    System.out.println("usage : tom [options] input[.t] [... input[.t]]");
				    System.exit(1); // consider creating a TomBadArgumentsException
				}
			    
			    System.out.println("option : " + s);
			    Vector about = getServer().aboutThisOption(s);
			    if(about == null)
				System.out.println("Option " +s+ " not found !"); // will raise an exception later
			    else
				{
				    String type = (String)about.elementAt(0);
				    TomPlugin plugin = (TomPlugin)about.elementAt(1);
				    
				    if (type.equals("boolean"))
					{
					    plugin.setOption(s, "1");
					}
				    else if (type.equals("integer"))
					{
					    if (i+1 >= argumentList.length)// argument expected but no more input
						{
						    System.out.println("Option " +s+ " requires an integer attribute");
						}   
					    else
						{
						    String t = argumentList[i+1];
						    if(t.startsWith("-"))
							{System.out.println("Option " +s+ " requires an integer attribute");}
						    else {plugin.setOption(s, t); i++;}
						}
					}
				    else if (type.equals("string")) 
					{
					    if (i+1 >= argumentList.length)// argument expected but no more input
						{
						    System.out.println("Option " +s+ " requires a string attribute");
						}   
					    else
						{
						    String t = argumentList[i+1];
						    if(t.startsWith("-"))
							{System.out.println("Option " +s+ " requires a string attribute");}
						    else {plugin.setOption(s, t); i++;}
						}
					    
					}
				}			
			}
		    else
			{
			    // a lone argument (not preceded by an option name) can only be an input file
			    // BUT ! input file names can only be specified after options
			    // so there is an error if a lone argument is found in the middle of options
			    inputFiles.add(s);
			    noMoreOptions = true;
			}
		}
	    if(inputFiles.isEmpty())
		System.out.println("No input file(s).");

	    return (String[])inputFiles.toArray(new String[]{});
    }

    public void extractOptionList()
    {
	XmlTools xtools = new XmlTools();
	TNode tnode = (TNode)xtools.convertXMLToATerm("./jtom/starter/Options.xml");
	TNode node = tnode.getDocElem();

	%match(TNode node)
	    {
		ElementNode[childList = c]
		    -> { while(!(c.isEmpty()))
			{
			    TNode h = c.getHead();
			    
			    %match(TNode h){
				<OptionBoolean>
				     name@<name></name>
				     alt@<altName></altName>
				     desc@<description></description>
				     val@<valueB></valueB>
				     </OptionBoolean> -> { extractOptionBoolean(name, alt, desc, val); }
				
				<OptionInteger>
				     name@<name></name>
				     alt@<altName></altName>
				     desc@<description></description>
				     val@<valueI></valueI>
				     attr@<attrName></attrName>
				     </OptionInteger> -> { extractOptionInteger(name, alt, desc, val, attr);}

				<OptionString>
				     name@<name></name>
				     alt@<altName></altName>
				     desc@<description></description>
				     val@<valueS></valueS>
				     attr@<attrName></attrName>
				     </OptionString> -> { extractOptionString(name, alt, desc, val, attr);}
			    }

			    c = c.getTail();
			}
		}
	    }
    }	

    private void extractOptionBoolean(TNode nameNode, TNode altNameNode,
				      TNode descriptionNode, TNode valueBNode)
    {
	String name = "";
	String altName = "";
	String description = "";
	String valueB = "";

	%match(TNode nameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { name = d; } }
			}
		}
	    }

	%match(TNode altNameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { altName = d; } }
			}
		}
	    }

	%match(TNode descriptionNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { description = d; } }
			}
		}
	    }

	%match(TNode valueBNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> {  valueB = d; } }
			}
		}
	    }
	myOptions = `concOption(OptionBoolean(name, altName, description, Integer.parseInt(valueB)), myOptions*);
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueB);
    }

    private void extractOptionInteger(TNode nameNode, TNode altNameNode,
				      TNode descriptionNode, TNode valueINode, TNode attrNameNode)
    {
	String name = "";
	String altName = "";
	String description = "";
	String valueI = "";
	String attrName = "";

	%match(TNode nameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { name = d; } }
			}
		}
	    }

	%match(TNode altNameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { altName = d; } }
			}
		}
	    }

	%match(TNode descriptionNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { description = d; } }
			}
		}
	    }

	%match(TNode valueINode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> {  valueI = d; } }
			}
		}
	    }

	%match(TNode attrNameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { attrName = d; } }
			}
		}
	    }

	myOptions = `concOption(OptionInteger(name, altName, description, Integer.parseInt(valueI), attrName), myOptions*);
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueI + "|" + attrName);
    }

    private void extractOptionString(TNode nameNode, TNode altNameNode,
				     TNode descriptionNode, TNode valueSNode, TNode attrNameNode)
    {
	String name = "";
	String altName = "";
	String description = "";
	String valueS = "";
	String attrName = "";

	%match(TNode nameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { name = d; } }
			}
		}
	    }

	%match(TNode altNameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { altName = d; } }
			}
		}
	    }

	%match(TNode descriptionNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { description = d; } }
			}
		}
	    }

	%match(TNode valueSNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> {  valueS = d; } }
			}
		}
	    }

	%match(TNode attrNameNode)
	    {
	    	ElementNode[childList = c]
		    -> { if(!(c.isEmpty()))
			{
			    TNode text = c.getHead();
			    %match(TNode text){ TextNode[data = d]
						    -> { attrName = d; } }
			}
		}
	    }

	myOptions = `concOption(OptionString(name, altName, description, valueS, attrName), myOptions*);
	//System.out.println(name + "|" + altName + "|" + description + "|" + valueS + "|" + attrName);
    }
}
