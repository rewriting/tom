/*
*
* TOM - To One Matching Compiler
*
* Copyright (c) 2000-2011, INPL, INRIA
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
  
  private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {
    if( l1.isEmptyconcPlatformOption() ) {
      return l2;
    } else if( l2.isEmptyconcPlatformOption() ) {
      return l1;
    } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {
      return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;
    } else {
      return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;
    }
  }
  private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;
  }
  

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
PlatformOptionList list = 
 tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ;

{
{
if ( (optionsNode instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )optionsNode) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch675_3= (( tom.library.adt.tnode.types.TNode )optionsNode).getChildList() ;
if ( "options".equals( (( tom.library.adt.tnode.types.TNode )optionsNode).getName() ) ) {
if ( (( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch675_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch675_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch675__end__11=tomMatch675_3;
do {
{
if (!( tomMatch675__end__11.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tom_option= tomMatch675__end__11.getHeadconcTNode() ;
{
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_2= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676_3= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "boolean".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch676_2 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_2 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__9=tomMatch676_2;
do {
{
if (!( tomMatch676__end__9.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_28= tomMatch676__end__9.getHeadconcTNode() ;
if ( (tomMatch676_28 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch676_28.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_10= tomMatch676__end__9.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__13=tomMatch676_10;
do {
{
if (!( tomMatch676__end__13.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_33= tomMatch676__end__13.getHeadconcTNode() ;
if ( (tomMatch676_33 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch676_33.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_14= tomMatch676__end__13.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__17=tomMatch676_14;
do {
{
if (!( tomMatch676__end__17.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_38= tomMatch676__end__17.getHeadconcTNode() ;
if ( (tomMatch676_38 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch676_38.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_18= tomMatch676__end__17.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__21=tomMatch676_18;
do {
{
if (!( tomMatch676__end__21.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_43= tomMatch676__end__21.getHeadconcTNode() ;
if ( (tomMatch676_43 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch676_43.getName() ) ) {
if ( ((tomMatch676_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch676_3.isEmptyconcTNode() ) {

PlatformBoolean bool = Boolean.valueOf(
 tomMatch676_43.getValue() ).booleanValue()?
 tom.platform.adt.platformoption.types.platformboolean.True.make() :
 tom.platform.adt.platformoption.types.platformboolean.False.make() ;
list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch676_38.getValue() ,  tomMatch676_28.getValue() ,  tomMatch676_33.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch676__end__21.isEmptyconcTNode() ) {
tomMatch676__end__21=tomMatch676_18;
} else {
tomMatch676__end__21= tomMatch676__end__21.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__21==tomMatch676_18) ));
}
}
}
if ( tomMatch676__end__17.isEmptyconcTNode() ) {
tomMatch676__end__17=tomMatch676_14;
} else {
tomMatch676__end__17= tomMatch676__end__17.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__17==tomMatch676_14) ));
}
}
}
if ( tomMatch676__end__13.isEmptyconcTNode() ) {
tomMatch676__end__13=tomMatch676_10;
} else {
tomMatch676__end__13= tomMatch676__end__13.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__13==tomMatch676_10) ));
}
}
}
if ( tomMatch676__end__9.isEmptyconcTNode() ) {
tomMatch676__end__9=tomMatch676_2;
} else {
tomMatch676__end__9= tomMatch676__end__9.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__9==tomMatch676_2) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_47= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676_48= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "integer".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch676_47 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_47 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__54=tomMatch676_47;
do {
{
if (!( tomMatch676__end__54.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_77= tomMatch676__end__54.getHeadconcTNode() ;
if ( (tomMatch676_77 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch676_77.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_55= tomMatch676__end__54.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__58=tomMatch676_55;
do {
{
if (!( tomMatch676__end__58.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_82= tomMatch676__end__58.getHeadconcTNode() ;
if ( (tomMatch676_82 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch676_82.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_59= tomMatch676__end__58.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__62=tomMatch676_59;
do {
{
if (!( tomMatch676__end__62.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_87= tomMatch676__end__62.getHeadconcTNode() ;
if ( (tomMatch676_87 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch676_87.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_63= tomMatch676__end__62.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__66=tomMatch676_63;
do {
{
if (!( tomMatch676__end__66.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_92= tomMatch676__end__66.getHeadconcTNode() ;
if ( (tomMatch676_92 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch676_92.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_67= tomMatch676__end__66.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__70=tomMatch676_67;
do {
{
if (!( tomMatch676__end__70.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_97= tomMatch676__end__70.getHeadconcTNode() ;
if ( (tomMatch676_97 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch676_97.getName() ) ) {
if ( ((tomMatch676_48 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_48 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch676_48.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch676_92.getValue() ,  tomMatch676_77.getValue() ,  tomMatch676_87.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(Integer.parseInt( tomMatch676_97.getValue() )) ,  tomMatch676_82.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch676__end__70.isEmptyconcTNode() ) {
tomMatch676__end__70=tomMatch676_67;
} else {
tomMatch676__end__70= tomMatch676__end__70.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__70==tomMatch676_67) ));
}
}
}
if ( tomMatch676__end__66.isEmptyconcTNode() ) {
tomMatch676__end__66=tomMatch676_63;
} else {
tomMatch676__end__66= tomMatch676__end__66.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__66==tomMatch676_63) ));
}
}
}
if ( tomMatch676__end__62.isEmptyconcTNode() ) {
tomMatch676__end__62=tomMatch676_59;
} else {
tomMatch676__end__62= tomMatch676__end__62.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__62==tomMatch676_59) ));
}
}
}
if ( tomMatch676__end__58.isEmptyconcTNode() ) {
tomMatch676__end__58=tomMatch676_55;
} else {
tomMatch676__end__58= tomMatch676__end__58.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__58==tomMatch676_55) ));
}
}
}
if ( tomMatch676__end__54.isEmptyconcTNode() ) {
tomMatch676__end__54=tomMatch676_47;
} else {
tomMatch676__end__54= tomMatch676__end__54.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__54==tomMatch676_47) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_101= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676_102= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "string".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch676_101 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_101 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__108=tomMatch676_101;
do {
{
if (!( tomMatch676__end__108.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_131= tomMatch676__end__108.getHeadconcTNode() ;
if ( (tomMatch676_131 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch676_131.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_109= tomMatch676__end__108.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__112=tomMatch676_109;
do {
{
if (!( tomMatch676__end__112.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_136= tomMatch676__end__112.getHeadconcTNode() ;
if ( (tomMatch676_136 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch676_136.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_113= tomMatch676__end__112.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__116=tomMatch676_113;
do {
{
if (!( tomMatch676__end__116.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_141= tomMatch676__end__116.getHeadconcTNode() ;
if ( (tomMatch676_141 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch676_141.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_117= tomMatch676__end__116.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__120=tomMatch676_117;
do {
{
if (!( tomMatch676__end__120.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_146= tomMatch676__end__120.getHeadconcTNode() ;
if ( (tomMatch676_146 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch676_146.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch676_121= tomMatch676__end__120.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch676__end__124=tomMatch676_121;
do {
{
if (!( tomMatch676__end__124.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch676_151= tomMatch676__end__124.getHeadconcTNode() ;
if ( (tomMatch676_151 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch676_151.getName() ) ) {
if ( ((tomMatch676_102 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch676_102 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch676_102.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch676_146.getValue() ,  tomMatch676_131.getValue() ,  tomMatch676_141.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make( tomMatch676_151.getValue() ) ,  tomMatch676_136.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch676__end__124.isEmptyconcTNode() ) {
tomMatch676__end__124=tomMatch676_121;
} else {
tomMatch676__end__124= tomMatch676__end__124.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__124==tomMatch676_121) ));
}
}
}
if ( tomMatch676__end__120.isEmptyconcTNode() ) {
tomMatch676__end__120=tomMatch676_117;
} else {
tomMatch676__end__120= tomMatch676__end__120.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__120==tomMatch676_117) ));
}
}
}
if ( tomMatch676__end__116.isEmptyconcTNode() ) {
tomMatch676__end__116=tomMatch676_113;
} else {
tomMatch676__end__116= tomMatch676__end__116.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__116==tomMatch676_113) ));
}
}
}
if ( tomMatch676__end__112.isEmptyconcTNode() ) {
tomMatch676__end__112=tomMatch676_109;
} else {
tomMatch676__end__112= tomMatch676__end__112.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__112==tomMatch676_109) ));
}
}
}
if ( tomMatch676__end__108.isEmptyconcTNode() ) {
tomMatch676__end__108=tomMatch676_101;
} else {
tomMatch676__end__108= tomMatch676__end__108.getTailconcTNode() ;
}

}
} while(!( (tomMatch676__end__108==tomMatch676_101) ));
}
}
}
}

}


}



}
if ( tomMatch675__end__11.isEmptyconcTNode() ) {
tomMatch675__end__11=tomMatch675_3;
} else {
tomMatch675__end__11= tomMatch675__end__11.getTailconcTNode() ;
}

}
} while(!( (tomMatch675__end__11==tomMatch675_3) ));
}
}
}
}
}

}

}

return list;
}

}
