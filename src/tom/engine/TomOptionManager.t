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
 * Gregory Andrien
 *
 **/

package jtom;

import java.util.*;
import java.util.logging.*;
import java.io.*;

import jtom.*;

import tom.platform.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;

import jtom.exception.*;

import tom.library.xml.*;

import jtom.tools.*;

import aterm.*;
import aterm.pure.*;

public class TomOptionManager implements OptionManager, OptionOwner {
  
  %include{ adt/TNode.tom }
  %include{ adt/PlatformOption.tom }

  private final static String[] NULL_STRING_ARRAY = new String[0];

  /**
   * The global options.
   */    
  private PlatformOptionList globalOptions;

  /**
   * map the name of an option to the plugin which defines this option
   */
  private Map mapNameToOptionOwner;

  /**
   * map the name of an option to the option itself
   */
  private Map mapNameToOption;

  /**
   * map a shortname of an option to its full name 
   */
  private Map mapShortNameToName;


  /**
   *
   */
  private static Logger logger;

  /**
   *
   *
   * @return
   */
  private TNodeFactory getTNodeFactory() {
    return TNodeFactory.getInstance(SingletonFactory.getInstance());
  }

  private PlatformOptionFactory getPlatformOptionFactory() {
    return PlatformOptionFactory.getInstance(SingletonFactory.getInstance());
  }

  public TomOptionManager() {
    mapNameToOptionOwner = new HashMap();
    mapNameToOption = new HashMap();
    mapShortNameToName = new HashMap();
    logger = Logger.getLogger(getClass().getName());
  }

  /**
   * This method does the following :
   * <ul>
   * <li>a first call to declaredOptions() on the PluginPlatform and each plugin, in order to determine
   * which options exist and their default values ;</li>
   * <li>a call to processArguments() in order to read the command line and set the options to
   * their actual values ;</li>
   * <li>a second call to declaredOptions() in order to collect the options' real value ;</li>
   * <li>it then tells the environment to set some values right ;</li>
   * <li>eventually, prerequisites are checked.</li>
   * </ul>
   * 
   * @param argumentList the command line
   * @return an array of String containing the names of the files to compile
   */
  public String[] initOptionManagement(List plugins, String[] argumentList) {
    List optionOwnerList = new ArrayList(plugins);
    optionOwnerList.add(this);

    // collects the options/services provided by each plugin
    for(Iterator it = optionOwnerList.iterator(); it.hasNext() ; ) {
      OptionOwner plugin = (OptionOwner)it.next();
      for(PlatformOptionList list = plugin.declaredOptions(); !list.isEmpty(); list = list.getTail()) {
        PlatformOption option = list.getHead();
        %match(PlatformOption option) {
          PluginOption[name=n, altName=an] -> {
            setOptionOwnerFromName(n, plugin);
            setOptionFromName(n, option);
            if( an.length() > 0 ) {
              mapShortNameToName.put(an,n);
            }
          }
        }
      }
    }
    
    // set options accordingly to the arguments given in input
    String[] inputFiles = processArguments(argumentList);

    // checks if every plugin's needs are fulfilled
    for(Iterator it = optionOwnerList.iterator(); it.hasNext() ; ) {
      OptionOwner plugin = (OptionOwner)it.next();
      if(!checkOptionDependency(plugin.requiredOptions())) {
        logger.log(Level.SEVERE, "PrerequisitesIssue", plugin.getClass().getName());
      }
    }
    return inputFiles;
  }

  private String getCanonicalName(String name) {
    if(mapShortNameToName.containsKey(name)) {
      return (String)mapShortNameToName.get(name);
    }
    return name;
  }

  private PlatformOption getOptionFromName(String name) {
    PlatformOption option = (PlatformOption)mapNameToOption.get(getCanonicalName(name));
    if(option == null) {
      logger.log(Level.SEVERE,"OptionNotFound",getCanonicalName(name));
      throw new RuntimeException();
    }
    return option;
  }

  private PlatformOption setOptionFromName(String name, PlatformOption option) {
    return (PlatformOption)mapNameToOption.put(getCanonicalName(name),option);
  }

  private OptionOwner getOptionOwnerFromName(String name) {
    OptionOwner plugin = (OptionOwner)mapNameToOptionOwner.get(getCanonicalName(name));
    if(plugin == null) {
      logger.log(Level.SEVERE,"OptionNotFound",getCanonicalName(name));
      throw new RuntimeException();
    }
    return plugin;
  }

  private OptionOwner setOptionOwnerFromName(String name, OptionOwner plugin) {
    return (OptionOwner)mapNameToOptionOwner.put(getCanonicalName(name),plugin);
  }

  private void setOptionPlatformValue(String name, PlatformValue value) {
    PlatformOption option = getOptionFromName(name);
    if(option != null) {
      PlatformOption newOption = option.setValue(value);
      Object replaced = setOptionFromName(name, newOption);
      logger.log(Level.FINER,"SetValue",new Object[]{name,value,replaced});
    } else {
      throw new RuntimeException();
    }
  }


  /**
   * Sets an option to the desired value.
   * 
   * @param optionName the option's name
   * @param optionValue the option's desired value
   */
  public void setOption(String optionName, Object optionValue) {
    // trampoline to implement OptionOwner
    setOptionValue(optionName,optionValue);
  }
  public void setOptionValue(String optionName, Object optionValue) {
    // to implement OptionManager
    PlatformBoolean bool = null;
    if(optionValue instanceof Boolean) {
      bool = ((Boolean)optionValue).booleanValue()?`True():`False();
      setOptionPlatformValue(optionName, `BooleanValue(bool));
    } else if(optionValue instanceof Integer) {
      Integer v = (Integer) optionValue;
      setOptionPlatformValue(optionName, `IntegerValue(v.intValue()));
    } else if(optionValue instanceof String) {
      String v = (String) optionValue;
      setOptionPlatformValue(optionName, `StringValue(v));
    } else {
      throw new RuntimeException("unknown optionValue type: " + optionValue);
    }
    
    if(optionName.equals("verbose") && bool == `True()) {
      Tom.changeLogLevel(Level.INFO);
    } else if(optionName.equals("noWarning") && bool == `True()) {
      Tom.changeLogLevel(Level.SEVERE);
    }
  }


  /**
   * Returns the value of an option. Returns an Object which is a Boolean, a String or an Integer
   * depending on what the option type is.
   * 
   * @param optionName the name of the option whose value is seeked
   * @return an Object containing the option's value
   */
  public Object getOptionValue(String name) {
    PlatformOption option = getOptionFromName(name);
    %match(PlatformOption option) {
      PluginOption[value=BooleanValue(True())]  -> { return new Boolean(true); }
      PluginOption[value=BooleanValue(False())] -> { return new Boolean(false); }
      PluginOption[value=IntegerValue(value)]   -> { return new Integer(value); }
      PluginOption[value=StringValue(value)]    -> { return value; }
    }
    
    logger.log(Level.SEVERE,"OptionNotFound",name);
    throw new RuntimeException();
  }

  /**
   * Self-explanatory. Displays an help message indicating how to use the compiler.
   */
  private void displayHelp() {
    String beginning = "usage :"
	    + "\n\ttom [options] input[.t] [... input[.t]]"
	    + "\noptions :";
    StringBuffer buffer = new StringBuffer(beginning);

    buffer.append("\n\t-X <file>:\tDefines an alternate XML configuration file\n");

    for(Iterator it = mapNameToOption.values().iterator(); it.hasNext() ; ) {
      PlatformOption h = (PlatformOption)it.next();
      %match(PlatformOption h) {
        PluginOption[name=name, altName=altName, description=description, attrName=attrName] -> {
          buffer.append("\t--" + name);
          if(attrName.length() > 0) {
            buffer.append(" <" + attrName + ">");
          }
          if(altName.length() > 0) {
            buffer.append(" | -" + altName);
          }
          buffer.append(":\t" + description);
          buffer.append("\n");
        }
      }			
    }
	
    System.out.println(buffer.toString());
  }

  /**
   * Self-explanatory. Displays the current version of the TOM compiler.
   */
  public void displayVersion() {
    System.out.println("jtom " + Tom.VERSION + "\n\n"
                       + "Copyright (C) 2000-2004 INRIA, Nancy, France.\n");
  }

  /**
   * Checks if all the options a plugin needs are here.
   * 
   * @param list a list of options that must be found with the right value
   * @return true if every option was found with the right value, false otherwise
   */
  private boolean checkOptionDependency(PlatformOptionList requiredOptions) {
    %match(PlatformOptionList requiredOptions) {
      concPlatformOption() -> {
        return true;
      }

      concPlatformOption(PluginOption[name=name,value=value],tail*) -> {
        PlatformOption option = getOptionFromName(name);
        PlatformValue localValue = option.getValue();
        if(value != localValue) {
          logger.log(Level.SEVERE, "IncorrectOptionValue", new Object[]{name,value,getOptionValue(name)});
          return false;
        } else {
          return checkOptionDependency(`tail*);
        }
      }
    }
    return false;
  }
  

  /**
   * 
   * @return the global options
   */
  public PlatformOptionList declaredOptions() {
    return globalOptions;
  }

  /**
   * Extracts the global options from the XML configuration file.
   * 
   * @param node the node containing the XML file
   */
  public void setGlobalOptionList(TNode node) {
    %match(TNode node) {
      <server>opt@<options></options></server> -> {
         globalOptions = xmlNodeToOptionList(opt);
       }
    }
  }


  /**
   * 
   * @return the prerequisites
   */
  public PlatformOptionList requiredOptions() {
    PlatformOptionList prerequisites = `emptyPlatformOptionList();

    if( ((Boolean)getOptionValue("debug")).booleanValue() ) {
      prerequisites = `concPlatformOption(PluginOption("jCode", "", "", BooleanValue(True()),""), prerequisites*);
      // for the moment debug is only available for Java as target language
    }

    // options destdir and output are incompatible

    if( !((String)getOptionValue("destdir")).equals(".") ) {
      prerequisites = `concPlatformOption(PluginOption("output", "", "", StringValue(""), ""), prerequisites*);
      // destdir is not set at its default value -> it has been changed
      // -> we want output at its default value
    }

    if( !((String)getOptionValue("output")).equals("") ) {
      prerequisites = `concPlatformOption(PluginOption("destdir", "", "", StringValue("."), ""), prerequisites*);
      // output is not set at its default value -> it has been changed
      // -> we want destdir at its default value
    }

    return prerequisites;
  }

  /**
   * This method takes the arguments given by the user and deduces the options to set, then sets them.
   * 
   * @param argumentList
   * @return an array containing the name of the input files
   */
  private String[] processArguments(String[] argumentList) {
    List inputFiles = new ArrayList();
    StringBuffer imports = new StringBuffer();
    boolean outputEncountered = false;
    boolean destdirEncountered = false;
    int i = 0;

    try {
      for(; i < argumentList.length; i++) {
        String s = argumentList[i];

        if(!s.startsWith("-")) {
          // input file name, should never start with '-'
          inputFiles.add(s);
        } else { // s does start with '-', thus is -or at least should be- an option
          s = s.substring(1); // crops the '-'
          if(s.startsWith("-")) {
            // if there's another one
            s = s.substring(1); // crops the second '-'
          }
          if( s.equals("help") || s.equals("h") ) {
            displayHelp();
            System.exit(0);
          }
          if( s.equals("version") || s.equals("V") ) {
            displayVersion();
            System.exit(0);
          }
          if( s.equals("X") ) {
            // if we're here, the PluginPlatform has already handled the "-X" option
            // and all errors that might occur
            // just skip it,along with its argument
            i++;
            continue;
          }
          if( s.equals("import") || s.equals("I") ) {
            imports.append(argumentList[++i] + ":");
          }
          if( s.equals("output") || s.equals("o") ) {
            if(outputEncountered) {
              logger.log(Level.SEVERE, "OutputTwice");
              return NULL_STRING_ARRAY;
            } else {
              outputEncountered = true;
            }
          }
          if( s.equals("destdir") || s.equals("d") ) {
            if(destdirEncountered) {
              logger.log(Level.SEVERE, "DestdirTwice");
              return NULL_STRING_ARRAY;
            } else {
              destdirEncountered = true;
            }
          }

          OptionOwner plugin = getOptionOwnerFromName(s);
          PlatformOption option = getOptionFromName(s);

          if(option == null || plugin == null) {// option not found
            logger.log(Level.SEVERE, "InvalidOption", argumentList[i]);
            return NULL_STRING_ARRAY;
          } else {
            %match(PlatformOption option) {
              PluginOption[value=BooleanValue[]] -> {
                plugin.setOption(s, Boolean.TRUE);
              }

              PluginOption[value=IntegerValue[]] -> {
                String t = argumentList[++i];
                plugin.setOption(s, new Integer(t));
              }

              PluginOption[value=StringValue[]] -> {
                if ( !( s.equals("import") || s.equals("I") ) ) {// "import" is handled in the end
                  String t = argumentList[++i];
                  plugin.setOption(s, t);
                }
              }

            }

          }     				
        }	
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.log(Level.SEVERE, "IncompleteOption", argumentList[--i]);
      return NULL_STRING_ARRAY;
    }

    setOptionValue("import",imports.toString());

    if(inputFiles.isEmpty()) {
      displayVersion();
      displayHelp();
      logger.log(Level.SEVERE, "NoFileToCompile");
      return NULL_STRING_ARRAY;
    } else if(inputFiles.size() > 1 && outputEncountered) {
      logger.log(Level.SEVERE, "OutputWithMultipleCompilation");
      return NULL_STRING_ARRAY;
    }
    
    return (String[])inputFiles.toArray(NULL_STRING_ARRAY);	
  }

  public static PlatformOptionList xmlToOptionList(String xmlString) {
    XmlTools xtools = new XmlTools();
    InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
    TNode node = (TNode)xtools.convertXMLToATerm(stream);
    return (new TomOptionManager()).xmlNodeToOptionList(node.getDocElem());
  }

  private PlatformOptionList xmlNodeToOptionList(TNode optionsNode) {
    PlatformOptionList list = `emptyPlatformOptionList();
    %match(TNode optionsNode) {
      <options>(_*,option,_*)</options> -> {
         %match(TNode option) {
           <OptionBoolean [name = n, altName = an, description = d, value = v] /> -> {	
              PlatformBoolean bool = Boolean.valueOf(v).booleanValue()?`True():`False();
              list = `concPlatformOption(list*, PluginOption(n, an, d, BooleanValue(bool), "")); 
            }
            <OptionInteger [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
              list = `concPlatformOption(list*, PluginOption(n, an, d, IntegerValue(Integer.parseInt(v)), at));
            }
            <OptionString [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
              list = `concPlatformOption(list*, PluginOption(n, an, d, StringValue(v), at));
            }
         }
       }
    }
    return list;
  }

}
