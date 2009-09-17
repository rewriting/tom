/*
 *
 * Copyright (c) 2000-2009, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 **/

package tom.library.plugin;

import java.util.*;
import java.util.logging.*;

import tom.library.xml.*;
import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;

import tom.platform.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;


/**
 * This Factory, when given a proper XML file, can manage a set of plugins.
 * A plugin managed by this Factory MUST respect two very important conditions:
 * <ul>
 * <li>It MUST have an activation flag, which MUST be the first element in the
 * option list it declares;</li>
 * <li>In case it has other options, it MUST precise in its getRequiredOptions
 * () method that each of these options needs the activation flag to be set to
 * true to be set to another value than the default value.</li>
 * </ul>
 * Of course, the plugin must also implement the Plugin interface,
 * and the XML file must respect this DTD :
 * <!DOCTYPE factory [
 *
 * <!ELEMENT factory (plugin*)>
 *
 * <!ELEMENT plugin EMPTY>
 * <!ATTLIST plugin
 *   name CDATA #IMPLIED
 *   version CDATA #IMPLIED
 *   description CDATA #IMPLIED
 *   classpath CDATA #REQUIRED>
 * ]>
 * Please note that if you put two or more plugins in the set that
 * have their activation flag set to true by default, it will lead to
 * an error if the user doesn't activate manually (i.e. with the
 * command line) one of the plugins.  So all the benefit of having an
 * activation flag set to true by default (that is avoiding to set one
 * manually) will be gone.
 *
 * @author Gr&eacute;gory ANDRIEN
 */

public class PluginFactory implements Plugin {

       private static   tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList  l2) {     if( l1.isEmptyconcTNode() ) {       return l2;     } else if( l2.isEmptyconcTNode() ) {       return l1;     } else if(  l1.getTailconcTNode() .isEmptyconcTNode() ) {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,l2) ;     } else {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,tom_append_list_concTNode( l1.getTailconcTNode() ,l2)) ;     }   }   private static   tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end, tom.library.adt.tnode.types.TNodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTNode()  ||  (end== tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( begin.getHeadconcTNode() ,( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode( begin.getTailconcTNode() ,end,tail)) ;   }        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    


  private PlatformOptionList allDeclaredOptions;
  private PlatformOptionList allRequiredOptions;
  private Map<String,Plugin> flagOwners;
  private Object[] argToRelay;
  private OptionManager optionManager;

  private String pluginName;
  private Logger logger;

  protected Logger getLogger() { return logger; }

  private OptionManager getOM() {
    return optionManager;
  }

  public PluginFactory(String name, String xmlFile) {
    allDeclaredOptions =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
    allRequiredOptions =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
    flagOwners = new HashMap<String,Plugin>();

    pluginName = name;
    logger = Logger.getLogger(getClass().getName());

    List<String> classPaths = new ArrayList<String>();
    List<Plugin> plugins = new ArrayList<Plugin>();

    fillClassPathsList(classPaths, xmlFile);

    // creates an instance of each plugin
    for (String path : classPaths) {
      Object instance;
      try {
        instance = Class.forName(path).newInstance();
        if(instance instanceof Plugin) {
          plugins.add((Plugin)instance);
        } else {
          logger.log(Level.SEVERE, "ClassNotAPlugin",
                     new Object[]{pluginName, path});
        }
      } catch(ClassNotFoundException cnfe) {
        logger.log(Level.WARNING, "ClassNotFound",
                   new Object[]{pluginName, path});
      } catch(Exception e) {
        logger.log(Level.SEVERE, "InstantiationError",
                   new Object[]{pluginName, path});
      }
    }

    for (Plugin plugin : plugins) {
      PlatformOptionList declaredList = plugin.getDeclaredOptionList();
      allDeclaredOptions = tom_append_list_concPlatformOption(allDeclaredOptions,tom_append_list_concPlatformOption(declaredList, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ));
      String flagName = declaredList.getHeadconcPlatformOption().getName();
      flagOwners.put(flagName, plugin);

      PlatformOptionList requiredList = plugin.getRequiredOptionList();
      allRequiredOptions = tom_append_list_concPlatformOption(allRequiredOptions,tom_append_list_concPlatformOption(requiredList, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ));
    }
  }

  /**
   * From Plugin interface
   */
  public void setArgs(Object arg[]) {
    argToRelay = (Object[]) arg.clone();
  }

  /**
   * From Plugin interface
   */
  public Object[] getArgs() {
    return (Object[]) argToRelay.clone();
  }

  /**
   * From Plugin interface
   */
  public boolean isGomPlugin() {
    return false;
  }

  /**
   * From Plugin interface
   */
  public void run(Map<String,String> informationTracker) {
    Plugin activatedPlugin = null;
    for (String flagName : flagOwners.keySet()) {
      if( ((Boolean)getOM().getOptionValue(flagName)).booleanValue() ) {
        activatedPlugin = flagOwners.get(flagName);
      }
    }
    try {
      activatedPlugin.setArgs(argToRelay);
      activatedPlugin.run(informationTracker);
      argToRelay = activatedPlugin.getArgs();
    } catch (NullPointerException npe) {
      System.out.println("Error : No plugin was activated.");
      // TODO: when error management has changed, change this
    }
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public void setOptionManager(OptionManager optionManager) {
    this.optionManager = optionManager;
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public PlatformOptionList getDeclaredOptionList() {
    return allDeclaredOptions;
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public PlatformOptionList getRequiredOptionList() {
    Iterator<String> it = flagOwners.keySet().iterator();
    while(it.hasNext()) { // for all plugins
      String flagName = it.next();
      if(((Boolean)getOM().getOptionValue(flagName)).booleanValue()) {
        // if this plugin is activated
        it = flagOwners.keySet().iterator();

        while( it.hasNext() ) {
          String name = it.next();
          if( !name.equals(flagName) ) // require that the other aren't
            allRequiredOptions =  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make(name, "", "",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") ,tom_append_list_concPlatformOption(allRequiredOptions, tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() )) ;
        }
      }
    }

    return allRequiredOptions;
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public void optionChanged(String optionName, Object optionValue) {
    getOM().setOptionValue(optionName, optionValue);

    if(optionValue.equals(Boolean.TRUE)) {
      // no more than 1 plugin can be activated at a time
      if(flagOwners.keySet().contains(optionName) ) {
        // if the flag just set is an activation flag...
        for (String flagName : flagOwners.keySet()) {
          if( !flagName.equals(optionName) ) {
            // ...desactivate the other flags
            getOM().setOptionValue(flagName, Boolean.FALSE);
          }
        }
      }
    }
  }

  private void fillClassPathsList(List<String> classPaths, String xmlFile) {
    XmlTools xtools = new XmlTools();
    TNode docNode = ( xtools.convertXMLToTNode(xmlFile) ).getDocElem();

    {{if ( (docNode instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )docNode) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {if ( "factory".equals( (( tom.library.adt.tnode.types.TNode )docNode).getName() ) ) {if ( (( (( tom.library.adt.tnode.types.TNode )docNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )docNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (( (( tom.library.adt.tnode.types.TNode )docNode).getChildList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )docNode).getChildList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNode  tom_fact=(( tom.library.adt.tnode.types.TNode )docNode);{{if ( (tom_fact instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom_fact) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tom_cl= (( tom.library.adt.tnode.types.TNode )tom_fact).getChildList() ;




            while(!(tom_cl.isEmptyconcTNode())) {
              TNode pluginNode = tom_cl.getHeadconcTNode();

              {{if ( (pluginNode instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )pluginNode) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch575NameNumber_freshVar_2= (( tom.library.adt.tnode.types.TNode )pluginNode).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch575NameNumber_freshVar_3= (( tom.library.adt.tnode.types.TNode )pluginNode).getChildList() ;if ( "plugin".equals( (( tom.library.adt.tnode.types.TNode )pluginNode).getName() ) ) {if ( ((tomMatch575NameNumber_freshVar_2 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch575NameNumber_freshVar_2 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch575NameNumber_end_9=tomMatch575NameNumber_freshVar_2;do {{if (!( tomMatch575NameNumber_end_9.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch575NameNumber_freshVar_16= tomMatch575NameNumber_end_9.getHeadconcTNode() ;if ( (tomMatch575NameNumber_freshVar_16 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "classpath".equals( tomMatch575NameNumber_freshVar_16.getName() ) ) {if ( ((tomMatch575NameNumber_freshVar_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch575NameNumber_freshVar_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch575NameNumber_freshVar_3.isEmptyconcTNode() ) {
 classPaths.add( tomMatch575NameNumber_freshVar_16.getValue() ); }}}}}if ( tomMatch575NameNumber_end_9.isEmptyconcTNode() ) {tomMatch575NameNumber_end_9=tomMatch575NameNumber_freshVar_2;} else {tomMatch575NameNumber_end_9= tomMatch575NameNumber_end_9.getTailconcTNode() ;}}} while(!( (tomMatch575NameNumber_end_9==tomMatch575NameNumber_freshVar_2) ));}}}}}}tom_cl

= tom_cl.getTailconcTNode();
            }
          }}}}

      }}}}}}}

  }

}
