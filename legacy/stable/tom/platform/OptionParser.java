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

import java.io.*;

import aterm.*;
import aterm.pure.*;

import tom.library.adt.tnode.*;
import tom.library.adt.tnode.types.*;
import tom.platform.adt.platformoption.*;
import tom.platform.adt.platformoption.types.*;
import tom.library.xml.*;

/**
 * Helper class to parse OptionOwner options.
 * The options have to comply with the following this DTD
 *
 * <PRE><CODE>
 * < !ELEMENT options (boolean*,integer*,string*) >
 *
 * < !ELEMENT boolean EMPTY >
 * < !ATTLIST boolean
 *   name CDATA #REQUIRED
 *   altName CDATA ""
 *   description CDATA ""
 *   value (true|false) #REQUIRED >
 *
 * < !ELEMENT integer EMPTY >
 * < !ATTLIST integer
 *   name CDATA #REQUIRED
 *   altName CDATA ""
 *   description CDATA ""
 *   value CDATA #REQUIRED
 *   attrName CDATA #REQUIRED >
 *
 * < !ELEMENT string EMPTY >
 * < !ATTLIST string
 *   name CDATA #REQUIRED
 *   altName CDATA ""
 *   description CDATA ""
 *   value CDATA #REQUIRED
 *   attrName CDATA #REQUIRED >
 * </CODE></PRE>
 */
public class OptionParser {

       private static   tom.library.adt.tnode.types.TNodeList  tom_append_list_concTNode( tom.library.adt.tnode.types.TNodeList l1,  tom.library.adt.tnode.types.TNodeList  l2) {     if( l1.isEmptyconcTNode() ) {       return l2;     } else if( l2.isEmptyconcTNode() ) {       return l1;     } else if(  l1.getTailconcTNode() .isEmptyconcTNode() ) {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,l2) ;     } else {       return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( l1.getHeadconcTNode() ,tom_append_list_concTNode( l1.getTailconcTNode() ,l2)) ;     }   }   private static   tom.library.adt.tnode.types.TNodeList  tom_get_slice_concTNode( tom.library.adt.tnode.types.TNodeList  begin,  tom.library.adt.tnode.types.TNodeList  end, tom.library.adt.tnode.types.TNodeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTNode()  ||  (end== tom.library.adt.tnode.types.tnodelist.EmptyconcTNode.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.adt.tnode.types.tnodelist.ConsconcTNode.make( begin.getHeadconcTNode() ,( tom.library.adt.tnode.types.TNodeList )tom_get_slice_concTNode( begin.getTailconcTNode() ,end,tail)) ;   }        private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }    



  /**
   * An XMLTools for doing the stuff
   */
    // non static XmlTools
  //private static XmlTools xtools = new XmlTools();

  /**
   * @return a PlatformOptionList extracted from the a String
   */
  public static PlatformOptionList xmlToOptionList(String xmlString) {
    InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
    // non static XmlTools
    XmlTools xtools = new XmlTools();
    TNode node = xtools.convertXMLToTNode(stream);
    return xmlNodeToOptionList(node.getDocElem());
  }

  /**
   * @return a PlatformOptionList extracted from a TNode
   */
  public static PlatformOptionList xmlNodeToOptionList(TNode optionsNode) {
    PlatformOptionList list =  tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;
    { /* unamed block */{ /* unamed block */if ( (optionsNode instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )optionsNode) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch790_3= (( tom.library.adt.tnode.types.TNode )optionsNode).getChildList() ;if ( "options".equals( (( tom.library.adt.tnode.types.TNode )optionsNode).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList ) (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList() ) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch790_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch790_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch790_end_13=tomMatch790_3;do {{ /* unamed block */if (!( tomMatch790_end_13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tom___option= tomMatch790_end_13.getHeadconcTNode() ;{ /* unamed block */{ /* unamed block */if ( (tom___option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom___option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_2= (( tom.library.adt.tnode.types.TNode )tom___option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_3= (( tom.library.adt.tnode.types.TNode )tom___option).getChildList() ;if ( "boolean".equals( (( tom.library.adt.tnode.types.TNode )tom___option).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_2) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_2) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_end_11=tomMatch791_2;do {{ /* unamed block */if (!( tomMatch791_end_11.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_30= tomMatch791_end_11.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_30) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch791_30.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_12= tomMatch791_end_11.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_15=tomMatch791_12;do {{ /* unamed block */if (!( tomMatch791_end_15.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_37= tomMatch791_end_15.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_37) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch791_37.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_16= tomMatch791_end_15.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_19=tomMatch791_16;do {{ /* unamed block */if (!( tomMatch791_end_19.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_44= tomMatch791_end_19.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_44) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch791_44.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_20= tomMatch791_end_19.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_23=tomMatch791_20;do {{ /* unamed block */if (!( tomMatch791_end_23.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_51= tomMatch791_end_23.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_51) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch791_51.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_3) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_3) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch791_3.isEmptyconcTNode() ) {



            PlatformBoolean bool = Boolean.valueOf( tomMatch791_51.getValue() ).booleanValue()? tom.platform.adt.platformoption.types.platformboolean.True.make() : tom.platform.adt.platformoption.types.platformboolean.False.make() ;
            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch791_44.getValue() ,  tomMatch791_30.getValue() ,  tomMatch791_37.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch791_end_23.isEmptyconcTNode() ) {tomMatch791_end_23=tomMatch791_20;} else {tomMatch791_end_23= tomMatch791_end_23.getTailconcTNode() ;}}} while(!( (tomMatch791_end_23==tomMatch791_20) ));}}}if ( tomMatch791_end_19.isEmptyconcTNode() ) {tomMatch791_end_19=tomMatch791_16;} else {tomMatch791_end_19= tomMatch791_end_19.getTailconcTNode() ;}}} while(!( (tomMatch791_end_19==tomMatch791_16) ));}}}if ( tomMatch791_end_15.isEmptyconcTNode() ) {tomMatch791_end_15=tomMatch791_12;} else {tomMatch791_end_15= tomMatch791_end_15.getTailconcTNode() ;}}} while(!( (tomMatch791_end_15==tomMatch791_12) ));}}}if ( tomMatch791_end_11.isEmptyconcTNode() ) {tomMatch791_end_11=tomMatch791_2;} else {tomMatch791_end_11= tomMatch791_end_11.getTailconcTNode() ;}}} while(!( (tomMatch791_end_11==tomMatch791_2) ));}}}}}{ /* unamed block */if ( (tom___option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom___option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_57= (( tom.library.adt.tnode.types.TNode )tom___option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_58= (( tom.library.adt.tnode.types.TNode )tom___option).getChildList() ;if ( "integer".equals( (( tom.library.adt.tnode.types.TNode )tom___option).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_57) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_57) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_end_66=tomMatch791_57;do {{ /* unamed block */if (!( tomMatch791_end_66.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_89= tomMatch791_end_66.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_89) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch791_89.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_67= tomMatch791_end_66.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_70=tomMatch791_67;do {{ /* unamed block */if (!( tomMatch791_end_70.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_96= tomMatch791_end_70.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_96) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "attrName".equals( tomMatch791_96.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_71= tomMatch791_end_70.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_74=tomMatch791_71;do {{ /* unamed block */if (!( tomMatch791_end_74.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_103= tomMatch791_end_74.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_103) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch791_103.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_75= tomMatch791_end_74.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_78=tomMatch791_75;do {{ /* unamed block */if (!( tomMatch791_end_78.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_110= tomMatch791_end_78.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_110) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch791_110.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_79= tomMatch791_end_78.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_82=tomMatch791_79;do {{ /* unamed block */if (!( tomMatch791_end_82.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_117= tomMatch791_end_82.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_117) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch791_117.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_58) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_58) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch791_58.isEmptyconcTNode() ) {

            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch791_110.getValue() ,  tomMatch791_89.getValue() ,  tomMatch791_103.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(Integer.parseInt( tomMatch791_117.getValue() )) ,  tomMatch791_96.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch791_end_82.isEmptyconcTNode() ) {tomMatch791_end_82=tomMatch791_79;} else {tomMatch791_end_82= tomMatch791_end_82.getTailconcTNode() ;}}} while(!( (tomMatch791_end_82==tomMatch791_79) ));}}}if ( tomMatch791_end_78.isEmptyconcTNode() ) {tomMatch791_end_78=tomMatch791_75;} else {tomMatch791_end_78= tomMatch791_end_78.getTailconcTNode() ;}}} while(!( (tomMatch791_end_78==tomMatch791_75) ));}}}if ( tomMatch791_end_74.isEmptyconcTNode() ) {tomMatch791_end_74=tomMatch791_71;} else {tomMatch791_end_74= tomMatch791_end_74.getTailconcTNode() ;}}} while(!( (tomMatch791_end_74==tomMatch791_71) ));}}}if ( tomMatch791_end_70.isEmptyconcTNode() ) {tomMatch791_end_70=tomMatch791_67;} else {tomMatch791_end_70= tomMatch791_end_70.getTailconcTNode() ;}}} while(!( (tomMatch791_end_70==tomMatch791_67) ));}}}if ( tomMatch791_end_66.isEmptyconcTNode() ) {tomMatch791_end_66=tomMatch791_57;} else {tomMatch791_end_66= tomMatch791_end_66.getTailconcTNode() ;}}} while(!( (tomMatch791_end_66==tomMatch791_57) ));}}}}}{ /* unamed block */if ( (tom___option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom___option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_123= (( tom.library.adt.tnode.types.TNode )tom___option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_124= (( tom.library.adt.tnode.types.TNode )tom___option).getChildList() ;if ( "string".equals( (( tom.library.adt.tnode.types.TNode )tom___option).getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_123) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_123) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_end_132=tomMatch791_123;do {{ /* unamed block */if (!( tomMatch791_end_132.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_155= tomMatch791_end_132.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_155) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch791_155.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_133= tomMatch791_end_132.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_136=tomMatch791_133;do {{ /* unamed block */if (!( tomMatch791_end_136.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_162= tomMatch791_end_136.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_162) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "attrName".equals( tomMatch791_162.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_137= tomMatch791_end_136.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_140=tomMatch791_137;do {{ /* unamed block */if (!( tomMatch791_end_140.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_169= tomMatch791_end_140.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_169) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch791_169.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_141= tomMatch791_end_140.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_144=tomMatch791_141;do {{ /* unamed block */if (!( tomMatch791_end_144.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_176= tomMatch791_end_144.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_176) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch791_176.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch791_145= tomMatch791_end_144.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch791_end_148=tomMatch791_145;do {{ /* unamed block */if (!( tomMatch791_end_148.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch791_183= tomMatch791_end_148.getHeadconcTNode() ;if ( ((( tom.library.adt.tnode.types.TNode )tomMatch791_183) instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch791_183.getName() ) ) {if ( (((( tom.library.adt.tnode.types.TNodeList )tomMatch791_124) instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ((( tom.library.adt.tnode.types.TNodeList )tomMatch791_124) instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch791_124.isEmptyconcTNode() ) {

            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch791_176.getValue() ,  tomMatch791_155.getValue() ,  tomMatch791_169.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make( tomMatch791_183.getValue() ) ,  tomMatch791_162.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch791_end_148.isEmptyconcTNode() ) {tomMatch791_end_148=tomMatch791_145;} else {tomMatch791_end_148= tomMatch791_end_148.getTailconcTNode() ;}}} while(!( (tomMatch791_end_148==tomMatch791_145) ));}}}if ( tomMatch791_end_144.isEmptyconcTNode() ) {tomMatch791_end_144=tomMatch791_141;} else {tomMatch791_end_144= tomMatch791_end_144.getTailconcTNode() ;}}} while(!( (tomMatch791_end_144==tomMatch791_141) ));}}}if ( tomMatch791_end_140.isEmptyconcTNode() ) {tomMatch791_end_140=tomMatch791_137;} else {tomMatch791_end_140= tomMatch791_end_140.getTailconcTNode() ;}}} while(!( (tomMatch791_end_140==tomMatch791_137) ));}}}if ( tomMatch791_end_136.isEmptyconcTNode() ) {tomMatch791_end_136=tomMatch791_133;} else {tomMatch791_end_136= tomMatch791_end_136.getTailconcTNode() ;}}} while(!( (tomMatch791_end_136==tomMatch791_133) ));}}}if ( tomMatch791_end_132.isEmptyconcTNode() ) {tomMatch791_end_132=tomMatch791_123;} else {tomMatch791_end_132= tomMatch791_end_132.getTailconcTNode() ;}}} while(!( (tomMatch791_end_132==tomMatch791_123) ));}}}}}}

      }if ( tomMatch790_end_13.isEmptyconcTNode() ) {tomMatch790_end_13=tomMatch790_3;} else {tomMatch790_end_13= tomMatch790_end_13.getTailconcTNode() ;}}} while(!( (tomMatch790_end_13==tomMatch790_3) ));}}}}}}}

    return list;
  }

}
