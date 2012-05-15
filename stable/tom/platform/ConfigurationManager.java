/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2012, INPL, INRIA
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
    {{if ( (((Object)node) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )((Object)node)) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )((Object)node))) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch723_1= (( tom.library.adt.tnode.types.TNode )((Object)node)).getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch723_3= (( tom.library.adt.tnode.types.TNode )((Object)node)).getChildList() ;if ( true ) {if ( "platform".equals(tomMatch723_1) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )((Object)node)).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )((Object)node)).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch723_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch723_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch723__end__13=tomMatch723_3;do {{if (!( tomMatch723__end__13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch723_19= tomMatch723__end__13.getHeadconcTNode() ;if ( (tomMatch723_19 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch723_19) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch723_16= tomMatch723_19.getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch723_18= tomMatch723_19.getChildList() ;if ( true ) {if ( "plugins".equals(tomMatch723_16) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) tomMatch723_19.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) tomMatch723_19.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch723_18) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch723_18) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch723__end__28=tomMatch723_18;do {{if (!( tomMatch723__end__28.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch723_34= tomMatch723__end__28.getHeadconcTNode() ;if ( (tomMatch723_34 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch723_34) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch723_31= tomMatch723_34.getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch723_32= tomMatch723_34.getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch723_33= tomMatch723_34.getChildList() ;if ( true ) {if ( "plugin".equals(tomMatch723_31) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch723_32) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch723_32) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch723__end__41=tomMatch723_32;do {{if (!( tomMatch723__end__41.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch723_48= tomMatch723__end__41.getHeadconcTNode() ;if ( (tomMatch723_48 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch723_48) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) { String  tomMatch723_45= tomMatch723_48.getName() ;if ( true ) {if ( "class".equals(tomMatch723_45) ) { String  tom_cp= tomMatch723_48.getValue() ;if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch723_33) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch723_33) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch723_33.isEmptyconcTNode() ) {

         res.add(tom_cp);
         PluginPlatformMessage.finer(logger, null, 0, 
             PluginPlatformMessage.classPathRead, tom_cp);
       }}}}}}}if ( tomMatch723__end__41.isEmptyconcTNode() ) {tomMatch723__end__41=tomMatch723_32;} else {tomMatch723__end__41= tomMatch723__end__41.getTailconcTNode() ;}}} while(!( (tomMatch723__end__41==tomMatch723_32) ));}}}}}}if ( tomMatch723__end__28.isEmptyconcTNode() ) {tomMatch723__end__28=tomMatch723_18;} else {tomMatch723__end__28= tomMatch723__end__28.getTailconcTNode() ;}}} while(!( (tomMatch723__end__28==tomMatch723_18) ));}}}}}}}if ( tomMatch723__end__13.isEmptyconcTNode() ) {tomMatch723__end__13=tomMatch723_3;} else {tomMatch723__end__13= tomMatch723__end__13.getTailconcTNode() ;}}} while(!( (tomMatch723__end__13==tomMatch723_3) ));}}}}}}}}}

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
    {{if ( (((Object)node) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )((Object)node)) instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )(( tom.library.adt.tnode.types.TNode )((Object)node))) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch724_1= (( tom.library.adt.tnode.types.TNode )((Object)node)).getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch724_3= (( tom.library.adt.tnode.types.TNode )((Object)node)).getChildList() ;if ( true ) {if ( "platform".equals(tomMatch724_1) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )((Object)node)).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )((Object)node)).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch724_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch724_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch724__end__13=tomMatch724_3;do {{if (!( tomMatch724__end__13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch724_19= tomMatch724__end__13.getHeadconcTNode() ;if ( (tomMatch724_19 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch724_19) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch724_16= tomMatch724_19.getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch724_17= tomMatch724_19.getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch724_18= tomMatch724_19.getChildList() ;if ( true ) {if ( "optionmanager".equals(tomMatch724_16) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch724_17) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch724_17) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch724__end__26=tomMatch724_17;do {{if (!( tomMatch724__end__26.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch724_38= tomMatch724__end__26.getHeadconcTNode() ;if ( (tomMatch724_38 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch724_38) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) { String  tomMatch724_35= tomMatch724_38.getName() ;if ( true ) {if ( "class".equals(tomMatch724_35) ) { String  tom_omclass= tomMatch724_38.getValue() ;if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch724_18) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch724_18) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch724__end__32=tomMatch724_18;do {{if (!( tomMatch724__end__32.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch724_45= tomMatch724__end__32.getHeadconcTNode() ;if ( (tomMatch724_45 instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tomMatch724_45) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { String  tomMatch724_42= tomMatch724_45.getName() ; tom.library.adt.tnode.types.TNodeList  tomMatch724_44= tomMatch724_45.getChildList() ;if ( true ) {if ( "options".equals(tomMatch724_42) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) tomMatch724_45.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) tomMatch724_45.getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch724_44) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch724_44) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {

        try {
          Object omInstance = Class.forName(tom_omclass).newInstance();
          if(omInstance instanceof OptionManager) {
            optionManager = (OptionManager)omInstance;
          } else {
            PluginPlatformMessage.error(logger, null, 0, 
                PluginPlatformMessage.classNotOptionManager, tom_omclass);
            return 1;
          }
        } catch(ClassNotFoundException cnfe) {
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.classNotFound, tom_omclass);
          optionManager = null;
          return 1;
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e.getMessage());
          PluginPlatformMessage.error(logger, null, 0, 
              PluginPlatformMessage.instantiationError, tom_omclass);
          optionManager = null;
          return 1;
        }

        TNode optionX =  tom.library.adt.tnode.types.tnode.ElementNode.make("string",  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("altName", "true", "X") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("attrName", "true", "file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("description", "true", "Defines an alternate XML configuration file") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("name", "true", "config") , tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( tom.library.adt.tnode.types.tnode.AttributeNode.make("value", "true", xmlConfigurationFileName) , tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) ) ) ) ) ,  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) 


;
        TNode opt =  tom.library.adt.tnode.types.tnode.ElementNode.make("options",  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ,  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make(optionX,tom_append_list_concTNode(tomMatch724_44, tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() )) ) ;
        PlatformOptionList globalOptions = OptionParser.xmlNodeToOptionList(opt);
        optionManager.setGlobalOptionList(globalOptions);
        return 0;
      }}}}}}}if ( tomMatch724__end__32.isEmptyconcTNode() ) {tomMatch724__end__32=tomMatch724_18;} else {tomMatch724__end__32= tomMatch724__end__32.getTailconcTNode() ;}}} while(!( (tomMatch724__end__32==tomMatch724_18) ));}}}}}}if ( tomMatch724__end__26.isEmptyconcTNode() ) {tomMatch724__end__26=tomMatch724_17;} else {tomMatch724__end__26= tomMatch724__end__26.getTailconcTNode() ;}}} while(!( (tomMatch724__end__26==tomMatch724_17) ));}}}}}}if ( tomMatch724__end__13.isEmptyconcTNode() ) {tomMatch724__end__13=tomMatch724_3;} else {tomMatch724__end__13= tomMatch724__end__13.getTailconcTNode() ;}}} while(!( (tomMatch724__end__13==tomMatch724_3) ));}}}}}}}}}

    return 1;
  }

  /** logger accessor in case of logging needs*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
