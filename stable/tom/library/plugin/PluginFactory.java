/* Generated by TOM (version 2.5alpha): Do not edit this file *//*
 *
 * Copyright (c) 2000-2007, Pierre-Etienne Moreau
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

  /* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) {  return  (t1.equals(t2))  ;}private static boolean tom_is_sort_String(String t) {  return  t instanceof String  ;}  /* Generated by TOM (version 2.5alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) {  return  (t1==t2)  ;} /* Generated by TOM (version 2.5alpha): Do not edit this file */ /* Generated by TOM (version 2.5alpha): Do not edit this file */ /* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_TNodeList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_TNodeList(Object t) {  return  t instanceof tom.library.adt.tnode.types.TNodeList  ;}private static boolean tom_equal_term_TNode(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_TNode(Object t) {  return  t instanceof tom.library.adt.tnode.types.TNode  ;}private static boolean tom_is_fun_sym_ElementNode( tom.library.adt.tnode.types.TNode  t) {  return  t instanceof tom.library.adt.tnode.types.tnode.ElementNode  ;}private static  String  tom_get_slot_ElementNode_Name( tom.library.adt.tnode.types.TNode  t) {  return  t.getName()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_slot_ElementNode_AttrList( tom.library.adt.tnode.types.TNode  t) {  return  t.getAttrList()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_slot_ElementNode_ChildList( tom.library.adt.tnode.types.TNode  t) {  return  t.getChildList()  ;}private static boolean tom_is_fun_sym_AttributeNode( tom.library.adt.tnode.types.TNode  t) {  return  t instanceof tom.library.adt.tnode.types.tnode.AttributeNode  ;}private static  String  tom_get_slot_AttributeNode_Name( tom.library.adt.tnode.types.TNode  t) {  return  t.getName()  ;}private static  String  tom_get_slot_AttributeNode_Specified( tom.library.adt.tnode.types.TNode  t) {  return  t.getSpecified()  ;}private static  String  tom_get_slot_AttributeNode_Value( tom.library.adt.tnode.types.TNode  t) {  return  t.getValue()  ;}private static boolean tom_is_fun_sym_concTNode( tom.library.adt.tnode.types.TNodeList  t) {  return  t instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode || t instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_empty_list_concTNode() { return  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ; }private static  tom.library.adt.tnode.types.TNodeList  tom_cons_list_concTNode( tom.library.adt.tnode.types.TNode  e,  tom.library.adt.tnode.types.TNodeList  l) { return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make(e,l) ; }private static  tom.library.adt.tnode.types.TNode  tom_get_head_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.getHeadconcTNode()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_tail_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.getTailconcTNode()  ;}private static boolean tom_is_empty_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.isEmptyconcTNode()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList  l1,  tom.library.adt.tnode.types.TNodeList  l2) {    if(tom_is_empty_concTNode_TNodeList(l1)) {     return l2;    } else if(tom_is_empty_concTNode_TNodeList(l2)) {     return l1;    } else if(tom_is_empty_concTNode_TNodeList(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(l1))) {     return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(l1),l2);    } else {      return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(l1),tom_append_list_concTNode(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(l1),l2));    }   }  private static  tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end) {    if(tom_equal_term_TNodeList(begin,end)) {      return ( tom.library.adt.tnode.types.TNodeList )tom_empty_list_concTNode();    } else {      return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(begin),( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(begin),end));    }   }   /* Generated by TOM (version 2.5alpha): Do not edit this file */private static boolean tom_equal_term_PlatformOption(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_PlatformOption(Object t) {  return  t instanceof tom.platform.adt.platformoption.types.PlatformOption  ;}private static boolean tom_equal_term_PlatformOptionList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_PlatformOptionList(Object t) {  return  t instanceof tom.platform.adt.platformoption.types.PlatformOptionList  ;}private static boolean tom_equal_term_PlatformBoolean(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_PlatformBoolean(Object t) {  return  t instanceof tom.platform.adt.platformoption.types.PlatformBoolean  ;}private static boolean tom_equal_term_PlatformValue(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_PlatformValue(Object t) {  return  t instanceof tom.platform.adt.platformoption.types.PlatformValue  ;}private static  tom.platform.adt.platformoption.types.PlatformOption  tom_make_PluginOption( String  t0,  String  t1,  String  t2,  tom.platform.adt.platformoption.types.PlatformValue  t3,  String  t4) { return  tom.platform.adt.platformoption.types.platformoption.PluginOption.make(t0, t1, t2, t3, t4); }private static  tom.platform.adt.platformoption.types.PlatformBoolean  tom_make_False() { return  tom.platform.adt.platformoption.types.platformboolean.False.make(); }private static  tom.platform.adt.platformoption.types.PlatformValue  tom_make_BooleanValue( tom.platform.adt.platformoption.types.PlatformBoolean  t0) { return  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(t0); }private static boolean tom_is_fun_sym_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  t) {  return  t instanceof tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption || t instanceof tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption  ;}private static  tom.platform.adt.platformoption.types.PlatformOptionList  tom_empty_list_concPlatformOption() { return  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ; }private static  tom.platform.adt.platformoption.types.PlatformOptionList  tom_cons_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOption  e,  tom.platform.adt.platformoption.types.PlatformOptionList  l) { return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make(e,l) ; }private static  tom.platform.adt.platformoption.types.PlatformOption  tom_get_head_concPlatformOption_PlatformOptionList( tom.platform.adt.platformoption.types.PlatformOptionList  l) {  return  l.getHeadconcPlatformOption()  ;}private static  tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_tail_concPlatformOption_PlatformOptionList( tom.platform.adt.platformoption.types.PlatformOptionList  l) {  return  l.getTailconcPlatformOption()  ;}private static boolean tom_is_empty_concPlatformOption_PlatformOptionList( tom.platform.adt.platformoption.types.PlatformOptionList  l) {  return  l.isEmptyconcPlatformOption()  ;}private static  tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {    if(tom_is_empty_concPlatformOption_PlatformOptionList(l1)) {     return l2;    } else if(tom_is_empty_concPlatformOption_PlatformOptionList(l2)) {     return l1;    } else if(tom_is_empty_concPlatformOption_PlatformOptionList(( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_tail_concPlatformOption_PlatformOptionList(l1))) {     return ( tom.platform.adt.platformoption.types.PlatformOptionList )tom_cons_list_concPlatformOption(( tom.platform.adt.platformoption.types.PlatformOption )tom_get_head_concPlatformOption_PlatformOptionList(l1),l2);    } else {      return ( tom.platform.adt.platformoption.types.PlatformOptionList )tom_cons_list_concPlatformOption(( tom.platform.adt.platformoption.types.PlatformOption )tom_get_head_concPlatformOption_PlatformOptionList(l1),tom_append_list_concPlatformOption(( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_tail_concPlatformOption_PlatformOptionList(l1),l2));    }   }  private static  tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end) {    if(tom_equal_term_PlatformOptionList(begin,end)) {      return ( tom.platform.adt.platformoption.types.PlatformOptionList )tom_empty_list_concPlatformOption();    } else {      return ( tom.platform.adt.platformoption.types.PlatformOptionList )tom_cons_list_concPlatformOption(( tom.platform.adt.platformoption.types.PlatformOption )tom_get_head_concPlatformOption_PlatformOptionList(begin),( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption(( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_tail_concPlatformOption_PlatformOptionList(begin),end));    }   }   


  private PlatformOptionList allDeclaredOptions;
  private PlatformOptionList allRequiredOptions;
  private Map flagOwners;
  private Object[] argToRelay;
  private OptionManager optionManager; // it is never written!

  private String pluginName;
  private Logger logger;

  protected Logger getLogger() { return logger; }

  private OptionManager getOM() {
    return optionManager;
  }

  public PluginFactory(String name, String xmlFile) {
    allDeclaredOptions = tom_empty_list_concPlatformOption();
    allRequiredOptions = tom_empty_list_concPlatformOption();
    flagOwners = new HashMap();

    pluginName = name;
    logger = Logger.getLogger(getClass().getName());

    List classPaths = new ArrayList();
    List plugins = new ArrayList();

    fillClassPathsList(classPaths, xmlFile);

    // creates an instance of each plugin
    Iterator it = classPaths.iterator();
    while( it.hasNext() ) {
      Object instance;
      String path = (String)it.next();
      try {
        instance = Class.forName(path).newInstance();
        if(instance instanceof Plugin) {
          plugins.add(instance);
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

    it = plugins.iterator();
    while( it.hasNext() ) {
      Plugin plugin = (Plugin)it.next();

      PlatformOptionList declaredList = plugin.getDeclaredOptionList();
      allDeclaredOptions = tom_append_list_concPlatformOption(allDeclaredOptions,tom_append_list_concPlatformOption(declaredList,tom_empty_list_concPlatformOption()));
      String flagName = declaredList.getHeadconcPlatformOption().getName();
      flagOwners.put(flagName, plugin);

      PlatformOptionList requiredList = plugin.getRequiredOptionList();
      allRequiredOptions = tom_append_list_concPlatformOption(allRequiredOptions,tom_append_list_concPlatformOption(requiredList,tom_empty_list_concPlatformOption()));
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
  public void run() {
    Plugin activatedPlugin = null;
    Iterator it = flagOwners.keySet().iterator();
    while(it.hasNext()) {
      String flagName = (String)it.next();
      if( ((Boolean)getOM().getOptionValue(flagName)).booleanValue() ) {
        activatedPlugin = (Plugin)flagOwners.get(flagName);
      }
    }
    try{
      activatedPlugin.setArgs(argToRelay);
      activatedPlugin.run();
      argToRelay = activatedPlugin.getArgs();
    } catch(NullPointerException npe) {
      System.out.println("Error : No plugin was activated.");
      // TODO: when error management has changed, change this
    }
  }

  /**
   * From OptionOwner interface inherited from Plugin interface
   */
  public void setOptionManager(OptionManager optionManager) {}

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
    Iterator it = flagOwners.keySet().iterator();
    while(it.hasNext()) { // for all plugins
      String flagName = (String)it.next();
      if(((Boolean)getOM().getOptionValue(flagName)).booleanValue()) {
        // if this plugin is activated
        it = flagOwners.keySet().iterator();

        while( it.hasNext() ) {
          String name = (String)it.next();
          if( !name.equals(flagName) ) // require that the other aren't
            allRequiredOptions = tom_cons_list_concPlatformOption(tom_make_PluginOption(name,"","",tom_make_BooleanValue(tom_make_False()),""),tom_append_list_concPlatformOption(allRequiredOptions,tom_empty_list_concPlatformOption()));
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
      if( flagOwners.keySet().contains(optionName) ) {
        // if the flag just set is an activation flag...
        Iterator it = flagOwners.keySet().iterator();
        while( it.hasNext() ) {
          String flagName = (String)it.next();
          if( !flagName.equals(optionName) ) {
            getOM().setOptionValue(flagName, Boolean.FALSE);
            // ...desactivate the other flags
            //System.out.println(flagName + " desactivated");
          }
        }
        //System.out.println(optionName + " activated");
      }
    }
  }

  private void fillClassPathsList(List classPaths, String xmlFile) {
    XmlTools xtools = new XmlTools();
    TNode docNode = ( xtools.convertXMLToTNode(xmlFile) ).getDocElem();

     if (tom_is_sort_TNode(docNode)) { { tom.library.adt.tnode.types.TNode  tomMatch1Position1=(( tom.library.adt.tnode.types.TNode )docNode); if ( ( tom_is_fun_sym_ElementNode(tomMatch1Position1) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch1Position1); { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch1Position1); if ( ( tom_equal_term_String("factory", tom_get_slot_ElementNode_Name(tomMatch1Position1)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberAttrList) ||  false  ) ) { {int tomMatch1Position1NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberAttrListList1=tomMatch1Position1NameNumberAttrList; if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListList1=tomMatch1Position1NameNumberChildList; { tom.library.adt.tnode.types.TNode  tom_fact=tomMatch1Position1; if ( true ) { if (tom_is_sort_TNode(tom_fact)) { { tom.library.adt.tnode.types.TNode  tomMatch2Position1=(( tom.library.adt.tnode.types.TNode )tom_fact); if ( ( tom_is_fun_sym_ElementNode(tomMatch2Position1) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tom_cl=tom_get_slot_ElementNode_ChildList(tomMatch2Position1); if ( true ) {




            while(!(tom_cl.isEmptyconcTNode())) {
              TNode pluginNode = tom_cl.getHeadconcTNode();

               if (tom_is_sort_TNode(pluginNode)) { { tom.library.adt.tnode.types.TNode  tomMatch3Position1=(( tom.library.adt.tnode.types.TNode )pluginNode); if ( ( tom_is_fun_sym_ElementNode(tomMatch3Position1) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch3Position1); { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch3Position1); if ( ( tom_equal_term_String("plugin", tom_get_slot_ElementNode_Name(tomMatch3Position1)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch3Position1NameNumberAttrList) ||  false  ) ) { {int tomMatch3Position1NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberAttrListList1=tomMatch3Position1NameNumberAttrList; { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberAttrListBegin1=tomMatch3Position1NameNumberAttrListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberAttrListEnd1=tomMatch3Position1NameNumberAttrListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch3Position1NameNumberAttrListEnd1))) {tomMatch3Position1NameNumberAttrListList1=tomMatch3Position1NameNumberAttrListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberAttrListSave2=tomMatch3Position1NameNumberAttrListList1; { { tom.library.adt.tnode.types.TNode  tomMatch3Position1NameNumberAttrListPosition2=tom_get_head_concTNode_TNodeList(tomMatch3Position1NameNumberAttrListList1);tomMatch3Position1NameNumberAttrListIndex1=tomMatch3Position1NameNumberAttrListIndex1 + 1;tomMatch3Position1NameNumberAttrListList1=tom_get_tail_concTNode_TNodeList(tomMatch3Position1NameNumberAttrListList1); if ( ( tom_is_fun_sym_AttributeNode(tomMatch3Position1NameNumberAttrListPosition2) ||  false  ) ) { if ( ( tom_equal_term_String("classpath", tom_get_slot_AttributeNode_Name(tomMatch3Position1NameNumberAttrListPosition2)) ||  false  ) ) { { String  tom_cp=tom_get_slot_AttributeNode_Value(tomMatch3Position1NameNumberAttrListPosition2); if ( ( tom_is_fun_sym_concTNode(tomMatch3Position1NameNumberChildList) ||  false  ) ) { {int tomMatch3Position1NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch3Position1NameNumberChildListList1=tomMatch3Position1NameNumberChildList; if (tom_is_empty_concTNode_TNodeList(tomMatch3Position1NameNumberChildListList1)) { if ( true ) {
 classPaths.add(tom_cp);/*System.out.println(cp);*/  } } } } } } } } }tomMatch3Position1NameNumberAttrListList1=tomMatch3Position1NameNumberAttrListSave2; } }tomMatch3Position1NameNumberAttrListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch3Position1NameNumberAttrListEnd1); } }tomMatch3Position1NameNumberAttrListList1=tomMatch3Position1NameNumberAttrListBegin1; } } } } } } } } } } } }tom_cl

= tom_cl.getTailconcTNode();
            }
           } } } } }

       } } } } } } } } } } } } } }

  }

}
