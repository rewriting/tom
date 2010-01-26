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
    {{if ( (optionsNode instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )optionsNode) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch608_3= (( tom.library.adt.tnode.types.TNode )optionsNode).getChildList() ;if ( "options".equals( (( tom.library.adt.tnode.types.TNode )optionsNode).getName() ) ) {if ( (( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( ((tomMatch608_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch608_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch608__end__11=tomMatch608_3;do {{if (!( tomMatch608__end__11.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tom_option= tomMatch608__end__11.getHeadconcTNode() ;{{if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_2= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch609_3= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;if ( "boolean".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {if ( ((tomMatch609_2 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_2 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609__end__9=tomMatch609_2;do {{if (!( tomMatch609__end__9.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_28= tomMatch609__end__9.getHeadconcTNode() ;if ( (tomMatch609_28 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch609_28.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_10= tomMatch609__end__9.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__13=tomMatch609_10;do {{if (!( tomMatch609__end__13.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_33= tomMatch609__end__13.getHeadconcTNode() ;if ( (tomMatch609_33 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch609_33.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_14= tomMatch609__end__13.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__17=tomMatch609_14;do {{if (!( tomMatch609__end__17.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_38= tomMatch609__end__17.getHeadconcTNode() ;if ( (tomMatch609_38 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch609_38.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_18= tomMatch609__end__17.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__21=tomMatch609_18;do {{if (!( tomMatch609__end__21.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_43= tomMatch609__end__21.getHeadconcTNode() ;if ( (tomMatch609_43 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch609_43.getName() ) ) {if ( ((tomMatch609_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch609_3.isEmptyconcTNode() ) {



            PlatformBoolean bool = Boolean.valueOf( tomMatch609_43.getValue() ).booleanValue()? tom.platform.adt.platformoption.types.platformboolean.True.make() : tom.platform.adt.platformoption.types.platformboolean.False.make() ;
            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch609_38.getValue() ,  tomMatch609_28.getValue() ,  tomMatch609_33.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch609__end__21.isEmptyconcTNode() ) {tomMatch609__end__21=tomMatch609_18;} else {tomMatch609__end__21= tomMatch609__end__21.getTailconcTNode() ;}}} while(!( (tomMatch609__end__21==tomMatch609_18) ));}}}if ( tomMatch609__end__17.isEmptyconcTNode() ) {tomMatch609__end__17=tomMatch609_14;} else {tomMatch609__end__17= tomMatch609__end__17.getTailconcTNode() ;}}} while(!( (tomMatch609__end__17==tomMatch609_14) ));}}}if ( tomMatch609__end__13.isEmptyconcTNode() ) {tomMatch609__end__13=tomMatch609_10;} else {tomMatch609__end__13= tomMatch609__end__13.getTailconcTNode() ;}}} while(!( (tomMatch609__end__13==tomMatch609_10) ));}}}if ( tomMatch609__end__9.isEmptyconcTNode() ) {tomMatch609__end__9=tomMatch609_2;} else {tomMatch609__end__9= tomMatch609__end__9.getTailconcTNode() ;}}} while(!( (tomMatch609__end__9==tomMatch609_2) ));}}}}}{if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_47= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch609_48= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;if ( "integer".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {if ( ((tomMatch609_47 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_47 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609__end__54=tomMatch609_47;do {{if (!( tomMatch609__end__54.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_77= tomMatch609__end__54.getHeadconcTNode() ;if ( (tomMatch609_77 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch609_77.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_55= tomMatch609__end__54.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__58=tomMatch609_55;do {{if (!( tomMatch609__end__58.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_82= tomMatch609__end__58.getHeadconcTNode() ;if ( (tomMatch609_82 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "attrName".equals( tomMatch609_82.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_59= tomMatch609__end__58.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__62=tomMatch609_59;do {{if (!( tomMatch609__end__62.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_87= tomMatch609__end__62.getHeadconcTNode() ;if ( (tomMatch609_87 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch609_87.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_63= tomMatch609__end__62.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__66=tomMatch609_63;do {{if (!( tomMatch609__end__66.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_92= tomMatch609__end__66.getHeadconcTNode() ;if ( (tomMatch609_92 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch609_92.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_67= tomMatch609__end__66.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__70=tomMatch609_67;do {{if (!( tomMatch609__end__70.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_97= tomMatch609__end__70.getHeadconcTNode() ;if ( (tomMatch609_97 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch609_97.getName() ) ) {if ( ((tomMatch609_48 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_48 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch609_48.isEmptyconcTNode() ) {

            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch609_92.getValue() ,  tomMatch609_77.getValue() ,  tomMatch609_87.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(Integer.parseInt( tomMatch609_97.getValue() )) ,  tomMatch609_82.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch609__end__70.isEmptyconcTNode() ) {tomMatch609__end__70=tomMatch609_67;} else {tomMatch609__end__70= tomMatch609__end__70.getTailconcTNode() ;}}} while(!( (tomMatch609__end__70==tomMatch609_67) ));}}}if ( tomMatch609__end__66.isEmptyconcTNode() ) {tomMatch609__end__66=tomMatch609_63;} else {tomMatch609__end__66= tomMatch609__end__66.getTailconcTNode() ;}}} while(!( (tomMatch609__end__66==tomMatch609_63) ));}}}if ( tomMatch609__end__62.isEmptyconcTNode() ) {tomMatch609__end__62=tomMatch609_59;} else {tomMatch609__end__62= tomMatch609__end__62.getTailconcTNode() ;}}} while(!( (tomMatch609__end__62==tomMatch609_59) ));}}}if ( tomMatch609__end__58.isEmptyconcTNode() ) {tomMatch609__end__58=tomMatch609_55;} else {tomMatch609__end__58= tomMatch609__end__58.getTailconcTNode() ;}}} while(!( (tomMatch609__end__58==tomMatch609_55) ));}}}if ( tomMatch609__end__54.isEmptyconcTNode() ) {tomMatch609__end__54=tomMatch609_47;} else {tomMatch609__end__54= tomMatch609__end__54.getTailconcTNode() ;}}} while(!( (tomMatch609__end__54==tomMatch609_47) ));}}}}}{if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_101= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ; tom.library.adt.tnode.types.TNodeList  tomMatch609_102= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;if ( "string".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {if ( ((tomMatch609_101 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_101 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609__end__108=tomMatch609_101;do {{if (!( tomMatch609__end__108.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_131= tomMatch609__end__108.getHeadconcTNode() ;if ( (tomMatch609_131 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "altName".equals( tomMatch609_131.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_109= tomMatch609__end__108.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__112=tomMatch609_109;do {{if (!( tomMatch609__end__112.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_136= tomMatch609__end__112.getHeadconcTNode() ;if ( (tomMatch609_136 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "attrName".equals( tomMatch609_136.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_113= tomMatch609__end__112.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__116=tomMatch609_113;do {{if (!( tomMatch609__end__116.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_141= tomMatch609__end__116.getHeadconcTNode() ;if ( (tomMatch609_141 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "description".equals( tomMatch609_141.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_117= tomMatch609__end__116.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__120=tomMatch609_117;do {{if (!( tomMatch609__end__120.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_146= tomMatch609__end__120.getHeadconcTNode() ;if ( (tomMatch609_146 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "name".equals( tomMatch609_146.getName() ) ) { tom.library.adt.tnode.types.TNodeList  tomMatch609_121= tomMatch609__end__120.getTailconcTNode() ; tom.library.adt.tnode.types.TNodeList  tomMatch609__end__124=tomMatch609_121;do {{if (!( tomMatch609__end__124.isEmptyconcTNode() )) { tom.library.adt.tnode.types.TNode  tomMatch609_151= tomMatch609__end__124.getHeadconcTNode() ;if ( (tomMatch609_151 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {if ( "value".equals( tomMatch609_151.getName() ) ) {if ( ((tomMatch609_102 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch609_102 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {if ( tomMatch609_102.isEmptyconcTNode() ) {

            list = tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch609_146.getValue() ,  tomMatch609_131.getValue() ,  tomMatch609_141.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make( tomMatch609_151.getValue() ) ,  tomMatch609_136.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );
          }}}}}if ( tomMatch609__end__124.isEmptyconcTNode() ) {tomMatch609__end__124=tomMatch609_121;} else {tomMatch609__end__124= tomMatch609__end__124.getTailconcTNode() ;}}} while(!( (tomMatch609__end__124==tomMatch609_121) ));}}}if ( tomMatch609__end__120.isEmptyconcTNode() ) {tomMatch609__end__120=tomMatch609_117;} else {tomMatch609__end__120= tomMatch609__end__120.getTailconcTNode() ;}}} while(!( (tomMatch609__end__120==tomMatch609_117) ));}}}if ( tomMatch609__end__116.isEmptyconcTNode() ) {tomMatch609__end__116=tomMatch609_113;} else {tomMatch609__end__116= tomMatch609__end__116.getTailconcTNode() ;}}} while(!( (tomMatch609__end__116==tomMatch609_113) ));}}}if ( tomMatch609__end__112.isEmptyconcTNode() ) {tomMatch609__end__112=tomMatch609_109;} else {tomMatch609__end__112= tomMatch609__end__112.getTailconcTNode() ;}}} while(!( (tomMatch609__end__112==tomMatch609_109) ));}}}if ( tomMatch609__end__108.isEmptyconcTNode() ) {tomMatch609__end__108=tomMatch609_101;} else {tomMatch609__end__108= tomMatch609__end__108.getTailconcTNode() ;}}} while(!( (tomMatch609__end__108==tomMatch609_101) ));}}}}}}

      }if ( tomMatch608__end__11.isEmptyconcTNode() ) {tomMatch608__end__11=tomMatch608_3;} else {tomMatch608__end__11= tomMatch608__end__11.getTailconcTNode() ;}}} while(!( (tomMatch608__end__11==tomMatch608_3) ));}}}}}}}

    return list;
  }

}
