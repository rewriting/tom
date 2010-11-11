/*
* 
* TOM - To One Matching Compiler
* 
* Copyright (c) 2000-2010, INPL, INRIA
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

package tom.platform;

import java.util.*;
import java.util.logging.*;

import aterm.*;
import aterm.pure.*;

import tom.engine.TomMessage;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.library.xml.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;

/**
* This class is a wrapper for the platform XML configuration files.
* It extracts the plugins information and create an ordered list of
* of instances. Extracts the Option Management information and based
* on it create and initialize the corresponding OptionManager.
* The instantiation of a Configuration is not sufficient since it need to
* be initialized with an execution commandLine.
*
*/
public class ConfigurationManager {

/** Used to analyse xml configuration file*/


  private static   tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList  l2) {
    if( l1.isEmptyconcTNode() ) {
      return l2;
    } else if( l2.isEmptyconcTNode() ) {
      return l1;
    } else if(  l1.getTailconcTNode() .isEmptyconcTNode() ) {
      return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,l2) ;
    } else {
      return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,tom_append_list_concTNode( l1.getTailconcTNode() ,l2)) ;
    }
  }
  private static   tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end, tom.library.adt.tnode.types.TNodeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTNode()  ||  (end== tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( begin.getHeadconcTNode() ,( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode( begin.getTailconcTNode() ,end,tail)) ;
  }
  

/** configuration file name */
private String xmlConfigurationFileName;

/** The plugins instance list*/
private List<Plugin> pluginsList;

/** The OptionManager */
private OptionManager optionManager;

private static Logger logger = Logger.getLogger("tom.platform.ConfigurationManager");
/**
* Basic Constructor
* constructing a configurationManager that needs to be initialized
*/
public ConfigurationManager(String xmlConfigurationFileName) {
this.xmlConfigurationFileName = xmlConfigurationFileName;
this.pluginsList = new ArrayList<Plugin>();
}

/**
* initialize analyse the XML file and extract plugins and option management
*
* @return  an error code :
* <ul>
* <li>0 if no error was encountered</li>
* <li>1 if something went wrong</li>
* </ul>
*/
public int initialize(String[] commandLine) {    
XmlTools xtools = new XmlTools();
TNode configurationNode = xtools.convertXMLToTNode(xmlConfigurationFileName);
if(configurationNode == null) {
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.configFileNotXML, xmlConfigurationFileName);
return 1;
}
if(createPlugins(configurationNode.getDocElem())==1) {
return 1;
}    
if(createOptionManager(configurationNode.getDocElem()) == 1) {     
if( ((Boolean)optionManager.getOptionValue("optimize2")).booleanValue()
&& !(optionManager.getInputToCompileList().size() == 1 && "-".equals((String)optionManager.getInputToCompileList().get(0))) ) {
TomMessage.warning(logger, null, 0, TomMessage.optimizerModifiesLineNumbers);
}
return 1;
}
return optionManager.initialize(this, commandLine);
}

/** Accessor method */
public List<Plugin> getPluginsList() {
return pluginsList;
}

/** Accessor method */
public OptionManager getOptionManager() {
return optionManager;
}

/** 
* Initialize the plugins list based on information extracted
* from the XML conf file converted in TNode
*
* @return  an error code :
* <ul>
* <li>0 if no error was encountered</li>
* <li>1 if something went wrong</li>
* </ul>
*/
private int createPlugins(TNode configurationNode) {
List<String> pluginsClassList = extractClassPaths(configurationNode);
// if empty list this means there is a problem somewhere
if(pluginsClassList.isEmpty()) {
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.noPluginFound, xmlConfigurationFileName);
pluginsList = null;
return 1;
}
// creates an instance of each plugin
for (String pluginClass : pluginsClassList) {
try { 
Object pluginInstance = Class.forName(pluginClass).newInstance();
if(pluginInstance instanceof Plugin) {
pluginsList.add((Plugin)pluginInstance);
} else {
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.classNotAPlugin, pluginClass);
pluginsList = null;
return 1;
}
} catch(ClassNotFoundException cnfe) {
PluginPlatformMessage.warning(logger, null, 0, 
PluginPlatformMessage.classNotFound, pluginClass);
return 1;
} catch(Exception e) {
// adds the error message. this is too cryptic otherwise
e.printStackTrace();
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.instantiationError, pluginClass);
pluginsList = null;
return 1;
}
}
return 0;
}

/**
* Extracts the plugins' class name from the XML configuration file.
* 
* @param node the node containing the XML document
* @return the List of plugins class path
*/
private List<String> extractClassPaths(TNode node) {
List<String> res = new ArrayList<String>();

{
{
if ( (node instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )node) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637_3= (( tom.library.adt.tnode.types.TNode )node).getChildList() ;
if ( "platform".equals( (( tom.library.adt.tnode.types.TNode )node).getName() ) ) {
if ( (( (( tom.library.adt.tnode.types.TNode )node).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )node).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch637_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch637_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637__end__11=tomMatch637_3;
do {
{
if (!( tomMatch637__end__11.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch637_17= tomMatch637__end__11.getHeadconcTNode() ;
if ( (tomMatch637_17 instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637_16= tomMatch637_17.getChildList() ;
if ( "plugins".equals( tomMatch637_17.getName() ) ) {
if ( (( tomMatch637_17.getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( tomMatch637_17.getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch637_16 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch637_16 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637__end__24=tomMatch637_16;
do {
{
if (!( tomMatch637__end__24.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch637_30= tomMatch637__end__24.getHeadconcTNode() ;
if ( (tomMatch637_30 instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637_28= tomMatch637_30.getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch637_29= tomMatch637_30.getChildList() ;
if ( "plugin".equals( tomMatch637_30.getName() ) ) {
if ( ((tomMatch637_28 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch637_28 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch637__end__35=tomMatch637_28;
do {
{
if (!( tomMatch637__end__35.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch637_42= tomMatch637__end__35.getHeadconcTNode() ;
if ( (tomMatch637_42 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "class".equals( tomMatch637_42.getName() ) ) {
 String  tom_cp= tomMatch637_42.getValue() ;
if ( ((tomMatch637_29 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch637_29 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch637_29.isEmptyconcTNode() ) {

res.add(
tom_cp);
PluginPlatformMessage.finer(logger, null, 0, 
PluginPlatformMessage.classPathRead, 
tom_cp);


}
}
}
}
}
if ( tomMatch637__end__35.isEmptyconcTNode() ) {
tomMatch637__end__35=tomMatch637_28;
} else {
tomMatch637__end__35= tomMatch637__end__35.getTailconcTNode() ;
}

}
} while(!( (tomMatch637__end__35==tomMatch637_28) ));
}
}
}
}
if ( tomMatch637__end__24.isEmptyconcTNode() ) {
tomMatch637__end__24=tomMatch637_16;
} else {
tomMatch637__end__24= tomMatch637__end__24.getTailconcTNode() ;
}

}
} while(!( (tomMatch637__end__24==tomMatch637_16) ));
}
}
}
}
}
if ( tomMatch637__end__11.isEmptyconcTNode() ) {
tomMatch637__end__11=tomMatch637_3;
} else {
tomMatch637__end__11= tomMatch637__end__11.getTailconcTNode() ;
}

}
} while(!( (tomMatch637__end__11==tomMatch637_3) ));
}
}
}
}
}

}

}

return res;
}

/**
* Initialize the option manager based on information extracted
* from the XML conf file converted in TNode
* 
* @param node the node containing the XML file
* @return  an error code :
* <ul>
* <li>0 if no error was encountered</li>
* <li>1 if something went wrong</li>
* </ul>
*/
private int createOptionManager(TNode node) {

{
{
if ( (node instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )node) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638_3= (( tom.library.adt.tnode.types.TNode )node).getChildList() ;
if ( "platform".equals( (( tom.library.adt.tnode.types.TNode )node).getName() ) ) {
if ( (( (( tom.library.adt.tnode.types.TNode )node).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )node).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch638_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch638_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638__end__11=tomMatch638_3;
do {
{
if (!( tomMatch638__end__11.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch638_17= tomMatch638__end__11.getHeadconcTNode() ;
if ( (tomMatch638_17 instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638_15= tomMatch638_17.getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch638_16= tomMatch638_17.getChildList() ;
if ( "optionmanager".equals( tomMatch638_17.getName() ) ) {
if ( ((tomMatch638_15 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch638_15 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638__end__22=tomMatch638_15;
do {
{
if (!( tomMatch638__end__22.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch638_34= tomMatch638__end__22.getHeadconcTNode() ;
if ( (tomMatch638_34 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "class".equals( tomMatch638_34.getName() ) ) {
 String  tom_omclass= tomMatch638_34.getValue() ;
if ( ((tomMatch638_16 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch638_16 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638__end__28=tomMatch638_16;
do {
{
if (!( tomMatch638__end__28.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch638_39= tomMatch638__end__28.getHeadconcTNode() ;
if ( (tomMatch638_39 instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch638_38= tomMatch638_39.getChildList() ;
if ( "options".equals( tomMatch638_39.getName() ) ) {
if ( (( tomMatch638_39.getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( tomMatch638_39.getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch638_38 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch638_38 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {

try {
Object omInstance = Class.forName(
tom_omclass).newInstance();
if(omInstance instanceof OptionManager) {
optionManager = (OptionManager)omInstance;
} else {
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.classNotOptionManager, 
tom_omclass);
return 1;
}
} catch(ClassNotFoundException cnfe) {
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.classNotFound, 
tom_omclass);
optionManager = null;
return 1;
} catch (Exception e) {
e.printStackTrace();
System.out.println(e.getMessage());
PluginPlatformMessage.error(logger, null, 0, 
PluginPlatformMessage.instantiationError, 
tom_omclass);
optionManager = null;
return 1;
}

TNode optionX = 
 tom.library.adt.tnode.types.tnode.ElementNode.make("string",  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("altName", "true", "X") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("attrName", "true", "file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("description", "true", "Defines an alternate XML configuration file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("name", "true", "config") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("value", "true", xmlConfigurationFileName) , tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) ) ) ) ) ,  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) ;
TNode opt = 
 tom.library.adt.tnode.types.tnode.ElementNode.make("options",  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ,  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make(optionX,tom_append_list_concTNode(tomMatch638_38, tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() )) ) ;
PlatformOptionList globalOptions = OptionParser.xmlNodeToOptionList(
opt);
optionManager.setGlobalOptionList(globalOptions);
return 0;


}
}
}
}
}
if ( tomMatch638__end__28.isEmptyconcTNode() ) {
tomMatch638__end__28=tomMatch638_16;
} else {
tomMatch638__end__28= tomMatch638__end__28.getTailconcTNode() ;
}

}
} while(!( (tomMatch638__end__28==tomMatch638_16) ));
}
}
}
}
if ( tomMatch638__end__22.isEmptyconcTNode() ) {
tomMatch638__end__22=tomMatch638_15;
} else {
tomMatch638__end__22= tomMatch638__end__22.getTailconcTNode() ;
}

}
} while(!( (tomMatch638__end__22==tomMatch638_15) ));
}
}
}
}
if ( tomMatch638__end__11.isEmptyconcTNode() ) {
tomMatch638__end__11=tomMatch638_3;
} else {
tomMatch638__end__11= tomMatch638__end__11.getTailconcTNode() ;
}

}
} while(!( (tomMatch638__end__11==tomMatch638_3) ));
}
}
}
}
}

}

}

return 1;
}

/** logger accessor in case of logging needs*/
private Logger getLogger() {
return Logger.getLogger(getClass().getName());
}

}
