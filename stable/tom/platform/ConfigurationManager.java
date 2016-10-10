/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
       private static   tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList  l2) {     if( l1.isEmptyconcTNode() ) {       return l2;     } else if( l2.isEmptyconcTNode() ) {       return l1;     } else if(  l1.getTailconcTNode() .isEmptyconcTNode() ) {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,l2) ;     } else {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,tom_append_list_concTNode( l1.getTailconcTNode() ,l2)) ;     }   }   private static   tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end, tom.library.adt.tnode.types.TNodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTNode()  ||  (end== tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( begin.getHeadconcTNode() ,( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode( begin.getTailconcTNode() ,end,tail)) ;   }    
  
  //%include{ adt/platformoption/PlatformConfig.tom }
    
  
  /** configuration file name */
  private String configurationFileName;

  /** The plugins instance list*/
  private List<Plugin> pluginsList;

  /** The OptionManager */
  private OptionManager optionManager;
  
  private static Logger logger = Logger.getLogger("tom.platform.ConfigurationManager");
  /**
   * Basic Constructor
   * constructing a configurationManager that needs to be initialized
   */
  public ConfigurationManager(String configurationFileName) {
    this.configurationFileName = configurationFileName;
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
    TNode configurationNode = xtools.convertXMLToTNode(configurationFileName);
    if(configurationNode == null) {
      PluginPlatformMessage.error(logger, null, 0, 
          PluginPlatformMessage.configFileNotXML, configurationFileName);
      return 1;
    }
    if(createPlugins(configurationNode.getDocElem())==1) {
      return 1;
    }    
    if(createOptionManager(configurationNode.getDocElem()) == 1) {     
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
          PluginPlatformMessage.noPluginFound, configurationFileName);
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
    { /* unamed block */{ /* unamed block */if ( (node instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )node) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_3= (( tom.library.adt.tnode.types.TNode )node).getChildList() ;if ( "platform".equals( (( tom.library.adt.tnode.types.TNode )node).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )node).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )node).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch788_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch788_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_end_13=tomMatch788_3;do {{ /* unamed block */if (!( tomMatch788_end_13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch788_19= tomMatch788_end_13.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch788_19) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_18= tomMatch788_19.getChildList() ;if ( "plugins".equals( tomMatch788_19.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) tomMatch788_19.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) tomMatch788_19.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch788_18) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch788_18) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_end_28=tomMatch788_18;do {{ /* unamed block */if (!( tomMatch788_end_28.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch788_34= tomMatch788_end_28.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch788_34) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_32= tomMatch788_34.getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch788_33= tomMatch788_34.getChildList() ;if ( "plugin".equals( tomMatch788_34.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch788_32) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch788_32) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch788_end_41=tomMatch788_32;do {{ /* unamed block */if (!( tomMatch788_end_41.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch788_48= tomMatch788_end_41.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch788_48) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "class".equals( tomMatch788_48.getName() ) ) { String  tom___cp= tomMatch788_48.getValue() ;if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch788_33) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch788_33) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch788_33.isEmptyconcTNode() ) {

         res.add(tom___cp);
         PluginPlatformMessage.finer(logger, null, 0, 
             PluginPlatformMessage.classPathRead, tom___cp);
       }}}}}if ( tomMatch788_end_41.isEmptyconcTNode() ) {tomMatch788_end_41=tomMatch788_32;} else {tomMatch788_end_41= tomMatch788_end_41.getTailconcTNode() ;}}} while(!( (tomMatch788_end_41==tomMatch788_32) ));}}}}if ( tomMatch788_end_28.isEmptyconcTNode() ) {tomMatch788_end_28=tomMatch788_18;} else {tomMatch788_end_28= tomMatch788_end_28.getTailconcTNode() ;}}} while(!( (tomMatch788_end_28==tomMatch788_18) ));}}}}}if ( tomMatch788_end_13.isEmptyconcTNode() ) {tomMatch788_end_13=tomMatch788_3;} else {tomMatch788_end_13= tomMatch788_end_13.getTailconcTNode() ;}}} while(!( (tomMatch788_end_13==tomMatch788_3) ));}}}}}}}

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
    { /* unamed block */{ /* unamed block */if ( (node instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )node) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_3= (( tom.library.adt.tnode.types.TNode )node).getChildList() ;if ( "platform".equals( (( tom.library.adt.tnode.types.TNode )node).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )node).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )node).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch789_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch789_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_end_13=tomMatch789_3;do {{ /* unamed block */if (!( tomMatch789_end_13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch789_19= tomMatch789_end_13.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch789_19) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_17= tomMatch789_19.getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch789_18= tomMatch789_19.getChildList() ;if ( "optionmanager".equals( tomMatch789_19.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch789_17) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch789_17) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_end_26=tomMatch789_17;do {{ /* unamed block */if (!( tomMatch789_end_26.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch789_38= tomMatch789_end_26.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch789_38) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "class".equals( tomMatch789_38.getName() ) ) { String  tom___omclass= tomMatch789_38.getValue() ;if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch789_18) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch789_18) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_end_32=tomMatch789_18;do {{ /* unamed block */if (!( tomMatch789_end_32.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch789_45= tomMatch789_end_32.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch789_45) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch789_44= tomMatch789_45.getChildList() ;if ( "options".equals( tomMatch789_45.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) tomMatch789_45.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) tomMatch789_45.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch789_44) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch789_44) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {

        try {
          Object omInstance = Class.forName(tom___omclass).newInstance();
          if(omInstance instanceof OptionManager) {
            optionManager = (OptionManager)omInstance;
          } else {
            PluginPlatformMessage.error(logger, null, 0, 
                PluginPlatformMessage.classNotOptionManager, tom___omclass);
            return 1;
          }
        } catch(ClassNotFoundException cnfe) {
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.classNotFound, tom___omclass);
          optionManager = null;
          return 1;
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e.getMessage());
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.instantiationError, tom___omclass);
          optionManager = null;
          return 1;
        }

        TNode optionX =  tom.library.adt.tnode.types.tnode.ElementNode.make("string",  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("altName", "true", "X") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("attrName", "true", "file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("description", "true", "Defines an alternate XML configuration file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("name", "true", "config") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("value", "true", configurationFileName) , tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) ) ) ) ) ,  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) 


;
        TNode opt =  tom.library.adt.tnode.types.tnode.ElementNode.make("options",  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ,  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make(optionX,tom_append_list_concTNode(tomMatch789_44, tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() )) ) ;
        PlatformOptionList globalOptions = OptionParser.xmlNodeToOptionList(opt);
        optionManager.setGlobalOptionList(globalOptions);
        return 0;
      }}}}}if ( tomMatch789_end_32.isEmptyconcTNode() ) {tomMatch789_end_32=tomMatch789_18;} else {tomMatch789_end_32= tomMatch789_end_32.getTailconcTNode() ;}}} while(!( (tomMatch789_end_32==tomMatch789_18) ));}}}}if ( tomMatch789_end_26.isEmptyconcTNode() ) {tomMatch789_end_26=tomMatch789_17;} else {tomMatch789_end_26= tomMatch789_end_26.getTailconcTNode() ;}}} while(!( (tomMatch789_end_26==tomMatch789_17) ));}}}}if ( tomMatch789_end_13.isEmptyconcTNode() ) {tomMatch789_end_13=tomMatch789_3;} else {tomMatch789_end_13= tomMatch789_end_13.getTailconcTNode() ;}}} while(!( (tomMatch789_end_13==tomMatch789_3) ));}}}}}}}

    return 1;
  }

  /*
  private int createOptionManager(PlatformConfig node) {
    %match(node) {
      PlatformConfig(plugins,OptionManager(omclass,options)) -> {
        try {
          Object omInstance = Class.forName(`omclass).newInstance();
          if(omInstance instanceof OptionManager) {
            optionManager = (OptionManager)omInstance;
          } else {
            PluginPlatformMessage.error(logger, null, 0, 
                PluginPlatformMessage.classNotOptionManager, `omclass);
            return 1;
          }
        } catch(ClassNotFoundException cnfe) {
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.classNotFound, `omclass);
          optionManager = null;
          return 1;
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e.getMessage());
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.instantiationError, `omclass);
          optionManager = null;
          return 1;
        }

        Platform optionX = `PluginOption("config","X", "Defines an alternate XML configuration file",
            StringValue(configurationFileName), "");
        PlatformOptionList globalOptions = `concPlatformOption(optionX, options*);
        optionManager.setGlobalOptionList(globalOptions);
        return 0;
      }
    }
    return 1;
  }
  */

  /** logger accessor in case of logging needs*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
