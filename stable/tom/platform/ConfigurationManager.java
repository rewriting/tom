/* Generated by TOM (version 2.5alpha): Do not edit this file *//*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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
  /* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file *//* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) {  return  (t1.equals(t2))  ;}private static boolean tom_is_sort_String(String t) {  return  t instanceof String  ;}  /* Generated by TOM (version 2.5alpha): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) {  return  (t1==t2)  ;} /* Generated by TOM (version 2.5alpha): Do not edit this file */ /* Generated by TOM (version 2.5alpha): Do not edit this file */ /* Generated by TOM (version 2.5alpha): Do not edit this file */ private static boolean tom_equal_term_TNodeList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_TNodeList(Object t) {  return  t instanceof tom.library.adt.tnode.types.TNodeList  ;}private static boolean tom_equal_term_TNode(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_sort_TNode(Object t) {  return  t instanceof tom.library.adt.tnode.types.TNode  ;}private static boolean tom_is_fun_sym_ElementNode( tom.library.adt.tnode.types.TNode  t) {  return  t instanceof tom.library.adt.tnode.types.tnode.ElementNode  ;}private static  tom.library.adt.tnode.types.TNode  tom_make_ElementNode( String  t0,  tom.library.adt.tnode.types.TNodeList  t1,  tom.library.adt.tnode.types.TNodeList  t2) { return  tom.library.adt.tnode.types.tnode.ElementNode.make(t0, t1, t2); }private static  String  tom_get_slot_ElementNode_Name( tom.library.adt.tnode.types.TNode  t) {  return  t.getName()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_slot_ElementNode_AttrList( tom.library.adt.tnode.types.TNode  t) {  return  t.getAttrList()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_slot_ElementNode_ChildList( tom.library.adt.tnode.types.TNode  t) {  return  t.getChildList()  ;}private static boolean tom_is_fun_sym_AttributeNode( tom.library.adt.tnode.types.TNode  t) {  return  t instanceof tom.library.adt.tnode.types.tnode.AttributeNode  ;}private static  tom.library.adt.tnode.types.TNode  tom_make_AttributeNode( String  t0,  String  t1,  String  t2) { return  tom.library.adt.tnode.types.tnode.AttributeNode.make(t0, t1, t2); }private static  String  tom_get_slot_AttributeNode_Name( tom.library.adt.tnode.types.TNode  t) {  return  t.getName()  ;}private static  String  tom_get_slot_AttributeNode_Specified( tom.library.adt.tnode.types.TNode  t) {  return  t.getSpecified()  ;}private static  String  tom_get_slot_AttributeNode_Value( tom.library.adt.tnode.types.TNode  t) {  return  t.getValue()  ;}private static boolean tom_is_fun_sym_concTNode( tom.library.adt.tnode.types.TNodeList  t) {  return  t instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode || t instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_empty_list_concTNode() { return  tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ; }private static  tom.library.adt.tnode.types.TNodeList  tom_cons_list_concTNode( tom.library.adt.tnode.types.TNode  e,  tom.library.adt.tnode.types.TNodeList  l) { return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make(e,l) ; }private static  tom.library.adt.tnode.types.TNode  tom_get_head_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.getHeadconcTNode()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_get_tail_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.getTailconcTNode()  ;}private static boolean tom_is_empty_concTNode_TNodeList( tom.library.adt.tnode.types.TNodeList  l) {  return  l.isEmptyconcTNode()  ;}private static  tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList  l1,  tom.library.adt.tnode.types.TNodeList  l2) {    if(tom_is_empty_concTNode_TNodeList(l1)) {     return l2;    } else if(tom_is_empty_concTNode_TNodeList(l2)) {     return l1;    } else if(tom_is_empty_concTNode_TNodeList(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(l1))) {     return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(l1),l2);    } else {      return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(l1),tom_append_list_concTNode(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(l1),l2));    }   }  private static  tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end) {    if(tom_equal_term_TNodeList(begin,end)) {      return ( tom.library.adt.tnode.types.TNodeList )tom_empty_list_concTNode();    } else {      return ( tom.library.adt.tnode.types.TNodeList )tom_cons_list_concTNode(( tom.library.adt.tnode.types.TNode )tom_get_head_concTNode_TNodeList(begin),( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode(( tom.library.adt.tnode.types.TNodeList )tom_get_tail_concTNode_TNodeList(begin),end));    }   }   /* Generated by TOM (version 2.5alpha): Do not edit this file */ 


  
  /** configuration file name */
  private String xmlConfigurationFileName;

  /** The plugins instance list*/
  private List pluginsList;

  /** The OptionManager */
  private OptionManager optionManager;
  
  /**
   * Basic Constructor
   * constructing a configurationManager that needs to be initialized
   */
  public ConfigurationManager(String xmlConfigurationFileName) {
    this.xmlConfigurationFileName = xmlConfigurationFileName;
    this.pluginsList = new ArrayList();
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
      getLogger().log(Level.SEVERE, PluginPlatformMessage.configFileNotXML.getMessage(), xmlConfigurationFileName);
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
  public List getPluginsList() {
    return pluginsList;
  }

  /** Accessor method */
  public OptionManager  getOptionManager() {
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
    List pluginsClassList = extractClassPaths(configurationNode);
    // if empty list this means there is a problem somewhere
    if(pluginsClassList.isEmpty()) {
      getLogger().log(Level.SEVERE, PluginPlatformMessage.noPluginFound.getMessage(), xmlConfigurationFileName);
      pluginsList = null;
      return 1;
    }
    // creates an instance of each plugin
    Iterator classPathIt = pluginsClassList.iterator();
    while(classPathIt.hasNext()) {
      String pluginClass = (String)classPathIt.next();
      try { 
        Object pluginInstance = Class.forName(pluginClass).newInstance();
        if(pluginInstance instanceof Plugin) {
          pluginsList.add(pluginInstance);
        } else {
          getLogger().log(Level.SEVERE, PluginPlatformMessage.classNotAPlugin.getMessage(), pluginClass);
          pluginsList = null;
          return 1;
        }
      } catch(ClassNotFoundException cnfe) {
        getLogger().log(Level.WARNING, PluginPlatformMessage.classNotFound.getMessage(), pluginClass);
        return 1;
      } catch(Exception e) {
        // adds the error message. this is too cryptic otherwise
        e.printStackTrace();
        getLogger().log(Level.SEVERE, PluginPlatformMessage.instantiationError.getMessage(), pluginClass);
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
  private List extractClassPaths(TNode node) {
    List res = new ArrayList();
     if (tom_is_sort_TNode(node)) { { tom.library.adt.tnode.types.TNode  tomMatch1Position1=(( tom.library.adt.tnode.types.TNode )node); if ( ( tom_is_fun_sym_ElementNode(tomMatch1Position1) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch1Position1); { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch1Position1); if ( ( tom_equal_term_String("platform", tom_get_slot_ElementNode_Name(tomMatch1Position1)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberAttrList) ||  false  ) ) { {int tomMatch1Position1NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberAttrListList1=tomMatch1Position1NameNumberAttrList; if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListList1=tomMatch1Position1NameNumberChildList; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListBegin1=tomMatch1Position1NameNumberChildListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListEnd1=tomMatch1Position1NameNumberChildListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch1Position1NameNumberChildListEnd1))) {tomMatch1Position1NameNumberChildListList1=tomMatch1Position1NameNumberChildListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListSave2=tomMatch1Position1NameNumberChildListList1; { { tom.library.adt.tnode.types.TNode  tomMatch1Position1NameNumberChildListPosition2=tom_get_head_concTNode_TNodeList(tomMatch1Position1NameNumberChildListList1);tomMatch1Position1NameNumberChildListIndex1=tomMatch1Position1NameNumberChildListIndex1 + 1;tomMatch1Position1NameNumberChildListList1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListList1); if ( ( tom_is_fun_sym_ElementNode(tomMatch1Position1NameNumberChildListPosition2) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch1Position1NameNumberChildListPosition2); { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch1Position1NameNumberChildListPosition2); if ( ( tom_equal_term_String("plugins", tom_get_slot_ElementNode_Name(tomMatch1Position1NameNumberChildListPosition2)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildListPosition2NameNumberAttrList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListPosition2NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberAttrListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberAttrList; if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildListPosition2NameNumberChildList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListPosition2NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildList; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListBegin1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListEnd1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListEnd1))) {tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListSave2=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1; { { tom.library.adt.tnode.types.TNode  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2=tom_get_head_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1);tomMatch1Position1NameNumberChildListPosition2NameNumberChildListIndex1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListIndex1 + 1;tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1); if ( ( tom_is_fun_sym_ElementNode(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2); { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2); if ( ( tom_equal_term_String("plugin", tom_get_slot_ElementNode_Name(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListBegin1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListEnd1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListEnd1))) {tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListSave2=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1; { { tom.library.adt.tnode.types.TNode  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListPosition2=tom_get_head_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1);tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListIndex1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListIndex1 + 1;tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1); if ( ( tom_is_fun_sym_AttributeNode(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListPosition2) ||  false  ) ) { if ( ( tom_equal_term_String("class", tom_get_slot_AttributeNode_Name(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListPosition2)) ||  false  ) ) { { String  tom_cp=tom_get_slot_AttributeNode_Value(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListPosition2); if ( ( tom_is_fun_sym_concTNode(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList) ||  false  ) ) { {int tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList; if (tom_is_empty_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListList1)) { if ( true ) {

         res.add(tom_cp);
         getLogger().log(Level.FINER, PluginPlatformMessage.classPathRead.getMessage(), tom_cp);
        } } } } } } } } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListSave2; } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListEnd1); } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListBegin1; } } } } } } } } } } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListSave2; } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListPosition2NameNumberChildListEnd1); } }tomMatch1Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch1Position1NameNumberChildListPosition2NameNumberChildListBegin1; } } } } } } } } } } } } } }tomMatch1Position1NameNumberChildListList1=tomMatch1Position1NameNumberChildListSave2; } }tomMatch1Position1NameNumberChildListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch1Position1NameNumberChildListEnd1); } }tomMatch1Position1NameNumberChildListList1=tomMatch1Position1NameNumberChildListBegin1; } } } } } } } } } } } } } } }

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
     if (tom_is_sort_TNode(node)) { { tom.library.adt.tnode.types.TNode  tomMatch2Position1=(( tom.library.adt.tnode.types.TNode )node); if ( ( tom_is_fun_sym_ElementNode(tomMatch2Position1) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch2Position1); { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch2Position1); if ( ( tom_equal_term_String("platform", tom_get_slot_ElementNode_Name(tomMatch2Position1)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberAttrList) ||  false  ) ) { {int tomMatch2Position1NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberAttrListList1=tomMatch2Position1NameNumberAttrList; if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberChildList) ||  false  ) ) { {int tomMatch2Position1NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListList1=tomMatch2Position1NameNumberChildList; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListBegin1=tomMatch2Position1NameNumberChildListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListEnd1=tomMatch2Position1NameNumberChildListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch2Position1NameNumberChildListEnd1))) {tomMatch2Position1NameNumberChildListList1=tomMatch2Position1NameNumberChildListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListSave2=tomMatch2Position1NameNumberChildListList1; { { tom.library.adt.tnode.types.TNode  tomMatch2Position1NameNumberChildListPosition2=tom_get_head_concTNode_TNodeList(tomMatch2Position1NameNumberChildListList1);tomMatch2Position1NameNumberChildListIndex1=tomMatch2Position1NameNumberChildListIndex1 + 1;tomMatch2Position1NameNumberChildListList1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListList1); if ( ( tom_is_fun_sym_ElementNode(tomMatch2Position1NameNumberChildListPosition2) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch2Position1NameNumberChildListPosition2); { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch2Position1NameNumberChildListPosition2); if ( ( tom_equal_term_String("optionmanager", tom_get_slot_ElementNode_Name(tomMatch2Position1NameNumberChildListPosition2)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrList) ||  false  ) ) { {int tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrList; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListBegin1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListEnd1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListEnd1))) {tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListSave2=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1; { { tom.library.adt.tnode.types.TNode  tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListPosition2=tom_get_head_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1);tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListIndex1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListIndex1 + 1;tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1); if ( ( tom_is_fun_sym_AttributeNode(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListPosition2) ||  false  ) ) { if ( ( tom_equal_term_String("class", tom_get_slot_AttributeNode_Name(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListPosition2)) ||  false  ) ) { { String  tom_omclass=tom_get_slot_AttributeNode_Value(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListPosition2); if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberChildListPosition2NameNumberChildList) ||  false  ) ) { {int tomMatch2Position1NameNumberChildListPosition2NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildList; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListBegin1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListEnd1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1; { while (!(tom_is_empty_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListEnd1))) {tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListEnd1; { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListSave2=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1; { { tom.library.adt.tnode.types.TNode  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2=tom_get_head_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1);tomMatch2Position1NameNumberChildListPosition2NameNumberChildListIndex1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListIndex1 + 1;tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1); if ( ( tom_is_fun_sym_ElementNode(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2) ||  false  ) ) { { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList=tom_get_slot_ElementNode_AttrList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2); { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList=tom_get_slot_ElementNode_ChildList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2); if ( ( tom_equal_term_String("options", tom_get_slot_ElementNode_Name(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2)) ||  false  ) ) { if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList) ||  false  ) ) { {int tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberAttrList; if ( ( tom_is_fun_sym_concTNode(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList) ||  false  ) ) { {int tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListIndex1=0; { tom.library.adt.tnode.types.TNodeList  tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildList; { tom.library.adt.tnode.types.TNodeList  tom_globalOptions=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListPosition2NameNumberChildListList1; if ( true ) {

        try {
          Object omInstance = Class.forName(tom_omclass).newInstance();
          if(omInstance instanceof OptionManager) {
            optionManager = (OptionManager)omInstance;
          } else {
            getLogger().log(Level.SEVERE, PluginPlatformMessage.classNotOptionManager.getMessage(), tom_omclass);
            return 1;
          }
        } catch(ClassNotFoundException cnfe) {
          getLogger().log(Level.SEVERE, PluginPlatformMessage.classNotFound.getMessage(), tom_omclass);
          optionManager = null;
          return 1;
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e.getMessage());
          getLogger().log(Level.SEVERE, PluginPlatformMessage.instantiationError.getMessage(), tom_omclass);
          optionManager = null;
          return 1;
        }

        TNode optionX = tom_make_ElementNode("string",tom_cons_list_concTNode(tom_make_AttributeNode("altName","true",""),tom_cons_list_concTNode(tom_make_AttributeNode("attrName","true","file"),tom_cons_list_concTNode(tom_make_AttributeNode("description","true","Tom XML file"),tom_cons_list_concTNode(tom_make_AttributeNode("name","true","X"),tom_cons_list_concTNode(tom_make_AttributeNode("value","true",xmlConfigurationFileName),tom_empty_list_concTNode()))))),tom_empty_list_concTNode())


;
        TNode opt = tom_make_ElementNode("options",tom_empty_list_concTNode(),tom_cons_list_concTNode(optionX,tom_append_list_concTNode(tom_globalOptions,tom_empty_list_concTNode())));
        PlatformOptionList globalOptions = OptionParser.xmlNodeToOptionList(opt);
        optionManager.setGlobalOptionList(globalOptions);
        return 0;
       } } } } } } } } } } } } }tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListSave2; } }tomMatch2Position1NameNumberChildListPosition2NameNumberChildListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberChildListEnd1); } }tomMatch2Position1NameNumberChildListPosition2NameNumberChildListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberChildListBegin1; } } } } } } } } } }tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListSave2; } }tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListEnd1); } }tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListList1=tomMatch2Position1NameNumberChildListPosition2NameNumberAttrListBegin1; } } } } } } } } } } }tomMatch2Position1NameNumberChildListList1=tomMatch2Position1NameNumberChildListSave2; } }tomMatch2Position1NameNumberChildListEnd1=tom_get_tail_concTNode_TNodeList(tomMatch2Position1NameNumberChildListEnd1); } }tomMatch2Position1NameNumberChildListList1=tomMatch2Position1NameNumberChildListBegin1; } } } } } } } } } } } } } } }

    return 1;
  }

  /** logger accessor in case of logging needs*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
