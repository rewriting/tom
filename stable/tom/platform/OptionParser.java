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
 tom.library.adt.tnode.types.TNodeList  tomMatch686_3= (( tom.library.adt.tnode.types.TNode )optionsNode).getChildList() ;
if ( "options".equals( (( tom.library.adt.tnode.types.TNode )optionsNode).getName() ) ) {
if ( (( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch686_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch686_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch686__end__11=tomMatch686_3;
do {
{
if (!( tomMatch686__end__11.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tom_option= tomMatch686__end__11.getHeadconcTNode() ;
{
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_2= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687_3= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "boolean".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch687_2 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_2 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__9=tomMatch687_2;
do {
{
if (!( tomMatch687__end__9.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_28= tomMatch687__end__9.getHeadconcTNode() ;
if ( (tomMatch687_28 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch687_28.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_10= tomMatch687__end__9.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__13=tomMatch687_10;
do {
{
if (!( tomMatch687__end__13.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_33= tomMatch687__end__13.getHeadconcTNode() ;
if ( (tomMatch687_33 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch687_33.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_14= tomMatch687__end__13.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__17=tomMatch687_14;
do {
{
if (!( tomMatch687__end__17.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_38= tomMatch687__end__17.getHeadconcTNode() ;
if ( (tomMatch687_38 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch687_38.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_18= tomMatch687__end__17.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__21=tomMatch687_18;
do {
{
if (!( tomMatch687__end__21.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_43= tomMatch687__end__21.getHeadconcTNode() ;
if ( (tomMatch687_43 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch687_43.getName() ) ) {
if ( ((tomMatch687_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch687_3.isEmptyconcTNode() ) {

PlatformBoolean bool = Boolean.valueOf(
 tomMatch687_43.getValue() ).booleanValue()?
 tom.platform.adt.platformoption.types.platformboolean.True.make() :
 tom.platform.adt.platformoption.types.platformboolean.False.make() ;
list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch687_38.getValue() ,  tomMatch687_28.getValue() ,  tomMatch687_33.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch687__end__21.isEmptyconcTNode() ) {
tomMatch687__end__21=tomMatch687_18;
} else {
tomMatch687__end__21= tomMatch687__end__21.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__21==tomMatch687_18) ));
}
}
}
if ( tomMatch687__end__17.isEmptyconcTNode() ) {
tomMatch687__end__17=tomMatch687_14;
} else {
tomMatch687__end__17= tomMatch687__end__17.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__17==tomMatch687_14) ));
}
}
}
if ( tomMatch687__end__13.isEmptyconcTNode() ) {
tomMatch687__end__13=tomMatch687_10;
} else {
tomMatch687__end__13= tomMatch687__end__13.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__13==tomMatch687_10) ));
}
}
}
if ( tomMatch687__end__9.isEmptyconcTNode() ) {
tomMatch687__end__9=tomMatch687_2;
} else {
tomMatch687__end__9= tomMatch687__end__9.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__9==tomMatch687_2) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_47= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687_48= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "integer".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch687_47 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_47 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__54=tomMatch687_47;
do {
{
if (!( tomMatch687__end__54.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_77= tomMatch687__end__54.getHeadconcTNode() ;
if ( (tomMatch687_77 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch687_77.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_55= tomMatch687__end__54.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__58=tomMatch687_55;
do {
{
if (!( tomMatch687__end__58.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_82= tomMatch687__end__58.getHeadconcTNode() ;
if ( (tomMatch687_82 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch687_82.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_59= tomMatch687__end__58.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__62=tomMatch687_59;
do {
{
if (!( tomMatch687__end__62.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_87= tomMatch687__end__62.getHeadconcTNode() ;
if ( (tomMatch687_87 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch687_87.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_63= tomMatch687__end__62.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__66=tomMatch687_63;
do {
{
if (!( tomMatch687__end__66.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_92= tomMatch687__end__66.getHeadconcTNode() ;
if ( (tomMatch687_92 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch687_92.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_67= tomMatch687__end__66.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__70=tomMatch687_67;
do {
{
if (!( tomMatch687__end__70.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_97= tomMatch687__end__70.getHeadconcTNode() ;
if ( (tomMatch687_97 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch687_97.getName() ) ) {
if ( ((tomMatch687_48 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_48 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch687_48.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch687_92.getValue() ,  tomMatch687_77.getValue() ,  tomMatch687_87.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(Integer.parseInt( tomMatch687_97.getValue() )) ,  tomMatch687_82.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch687__end__70.isEmptyconcTNode() ) {
tomMatch687__end__70=tomMatch687_67;
} else {
tomMatch687__end__70= tomMatch687__end__70.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__70==tomMatch687_67) ));
}
}
}
if ( tomMatch687__end__66.isEmptyconcTNode() ) {
tomMatch687__end__66=tomMatch687_63;
} else {
tomMatch687__end__66= tomMatch687__end__66.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__66==tomMatch687_63) ));
}
}
}
if ( tomMatch687__end__62.isEmptyconcTNode() ) {
tomMatch687__end__62=tomMatch687_59;
} else {
tomMatch687__end__62= tomMatch687__end__62.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__62==tomMatch687_59) ));
}
}
}
if ( tomMatch687__end__58.isEmptyconcTNode() ) {
tomMatch687__end__58=tomMatch687_55;
} else {
tomMatch687__end__58= tomMatch687__end__58.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__58==tomMatch687_55) ));
}
}
}
if ( tomMatch687__end__54.isEmptyconcTNode() ) {
tomMatch687__end__54=tomMatch687_47;
} else {
tomMatch687__end__54= tomMatch687__end__54.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__54==tomMatch687_47) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_101= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687_102= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "string".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch687_101 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_101 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__108=tomMatch687_101;
do {
{
if (!( tomMatch687__end__108.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_131= tomMatch687__end__108.getHeadconcTNode() ;
if ( (tomMatch687_131 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch687_131.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_109= tomMatch687__end__108.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__112=tomMatch687_109;
do {
{
if (!( tomMatch687__end__112.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_136= tomMatch687__end__112.getHeadconcTNode() ;
if ( (tomMatch687_136 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch687_136.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_113= tomMatch687__end__112.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__116=tomMatch687_113;
do {
{
if (!( tomMatch687__end__116.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_141= tomMatch687__end__116.getHeadconcTNode() ;
if ( (tomMatch687_141 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch687_141.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_117= tomMatch687__end__116.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__120=tomMatch687_117;
do {
{
if (!( tomMatch687__end__120.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_146= tomMatch687__end__120.getHeadconcTNode() ;
if ( (tomMatch687_146 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch687_146.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch687_121= tomMatch687__end__120.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch687__end__124=tomMatch687_121;
do {
{
if (!( tomMatch687__end__124.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch687_151= tomMatch687__end__124.getHeadconcTNode() ;
if ( (tomMatch687_151 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch687_151.getName() ) ) {
if ( ((tomMatch687_102 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch687_102 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch687_102.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch687_146.getValue() ,  tomMatch687_131.getValue() ,  tomMatch687_141.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make( tomMatch687_151.getValue() ) ,  tomMatch687_136.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch687__end__124.isEmptyconcTNode() ) {
tomMatch687__end__124=tomMatch687_121;
} else {
tomMatch687__end__124= tomMatch687__end__124.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__124==tomMatch687_121) ));
}
}
}
if ( tomMatch687__end__120.isEmptyconcTNode() ) {
tomMatch687__end__120=tomMatch687_117;
} else {
tomMatch687__end__120= tomMatch687__end__120.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__120==tomMatch687_117) ));
}
}
}
if ( tomMatch687__end__116.isEmptyconcTNode() ) {
tomMatch687__end__116=tomMatch687_113;
} else {
tomMatch687__end__116= tomMatch687__end__116.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__116==tomMatch687_113) ));
}
}
}
if ( tomMatch687__end__112.isEmptyconcTNode() ) {
tomMatch687__end__112=tomMatch687_109;
} else {
tomMatch687__end__112= tomMatch687__end__112.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__112==tomMatch687_109) ));
}
}
}
if ( tomMatch687__end__108.isEmptyconcTNode() ) {
tomMatch687__end__108=tomMatch687_101;
} else {
tomMatch687__end__108= tomMatch687__end__108.getTailconcTNode() ;
}

}
} while(!( (tomMatch687__end__108==tomMatch687_101) ));
}
}
}
}

}


}



}
if ( tomMatch686__end__11.isEmptyconcTNode() ) {
tomMatch686__end__11=tomMatch686_3;
} else {
tomMatch686__end__11= tomMatch686__end__11.getTailconcTNode() ;
}

}
} while(!( (tomMatch686__end__11==tomMatch686_3) ));
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
