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
 tom.library.adt.tnode.types.TNodeList  tomMatch630_3= (( tom.library.adt.tnode.types.TNode )optionsNode).getChildList() ;
if ( "options".equals( (( tom.library.adt.tnode.types.TNode )optionsNode).getName() ) ) {
if ( (( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || ( (( tom.library.adt.tnode.types.TNode )optionsNode).getAttrList()  instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( ((tomMatch630_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch630_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch630__end__11=tomMatch630_3;
do {
{
if (!( tomMatch630__end__11.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tom_option= tomMatch630__end__11.getHeadconcTNode() ;
{
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_2= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631_3= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "boolean".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch631_2 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_2 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__9=tomMatch631_2;
do {
{
if (!( tomMatch631__end__9.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_28= tomMatch631__end__9.getHeadconcTNode() ;
if ( (tomMatch631_28 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch631_28.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_10= tomMatch631__end__9.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__13=tomMatch631_10;
do {
{
if (!( tomMatch631__end__13.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_33= tomMatch631__end__13.getHeadconcTNode() ;
if ( (tomMatch631_33 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch631_33.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_14= tomMatch631__end__13.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__17=tomMatch631_14;
do {
{
if (!( tomMatch631__end__17.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_38= tomMatch631__end__17.getHeadconcTNode() ;
if ( (tomMatch631_38 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch631_38.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_18= tomMatch631__end__17.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__21=tomMatch631_18;
do {
{
if (!( tomMatch631__end__21.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_43= tomMatch631__end__21.getHeadconcTNode() ;
if ( (tomMatch631_43 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch631_43.getName() ) ) {
if ( ((tomMatch631_3 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_3 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch631_3.isEmptyconcTNode() ) {

PlatformBoolean bool = Boolean.valueOf(
 tomMatch631_43.getValue() ).booleanValue()?
 tom.platform.adt.platformoption.types.platformboolean.True.make() :
 tom.platform.adt.platformoption.types.platformboolean.False.make() ;
list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch631_38.getValue() ,  tomMatch631_28.getValue() ,  tomMatch631_33.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make(bool) , "") , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch631__end__21.isEmptyconcTNode() ) {
tomMatch631__end__21=tomMatch631_18;
} else {
tomMatch631__end__21= tomMatch631__end__21.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__21==tomMatch631_18) ));
}
}
}
if ( tomMatch631__end__17.isEmptyconcTNode() ) {
tomMatch631__end__17=tomMatch631_14;
} else {
tomMatch631__end__17= tomMatch631__end__17.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__17==tomMatch631_14) ));
}
}
}
if ( tomMatch631__end__13.isEmptyconcTNode() ) {
tomMatch631__end__13=tomMatch631_10;
} else {
tomMatch631__end__13= tomMatch631__end__13.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__13==tomMatch631_10) ));
}
}
}
if ( tomMatch631__end__9.isEmptyconcTNode() ) {
tomMatch631__end__9=tomMatch631_2;
} else {
tomMatch631__end__9= tomMatch631__end__9.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__9==tomMatch631_2) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_47= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631_48= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "integer".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch631_47 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_47 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__54=tomMatch631_47;
do {
{
if (!( tomMatch631__end__54.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_77= tomMatch631__end__54.getHeadconcTNode() ;
if ( (tomMatch631_77 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch631_77.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_55= tomMatch631__end__54.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__58=tomMatch631_55;
do {
{
if (!( tomMatch631__end__58.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_82= tomMatch631__end__58.getHeadconcTNode() ;
if ( (tomMatch631_82 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch631_82.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_59= tomMatch631__end__58.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__62=tomMatch631_59;
do {
{
if (!( tomMatch631__end__62.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_87= tomMatch631__end__62.getHeadconcTNode() ;
if ( (tomMatch631_87 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch631_87.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_63= tomMatch631__end__62.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__66=tomMatch631_63;
do {
{
if (!( tomMatch631__end__66.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_92= tomMatch631__end__66.getHeadconcTNode() ;
if ( (tomMatch631_92 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch631_92.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_67= tomMatch631__end__66.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__70=tomMatch631_67;
do {
{
if (!( tomMatch631__end__70.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_97= tomMatch631__end__70.getHeadconcTNode() ;
if ( (tomMatch631_97 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch631_97.getName() ) ) {
if ( ((tomMatch631_48 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_48 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch631_48.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch631_92.getValue() ,  tomMatch631_77.getValue() ,  tomMatch631_87.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.IntegerValue.make(Integer.parseInt( tomMatch631_97.getValue() )) ,  tomMatch631_82.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch631__end__70.isEmptyconcTNode() ) {
tomMatch631__end__70=tomMatch631_67;
} else {
tomMatch631__end__70= tomMatch631__end__70.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__70==tomMatch631_67) ));
}
}
}
if ( tomMatch631__end__66.isEmptyconcTNode() ) {
tomMatch631__end__66=tomMatch631_63;
} else {
tomMatch631__end__66= tomMatch631__end__66.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__66==tomMatch631_63) ));
}
}
}
if ( tomMatch631__end__62.isEmptyconcTNode() ) {
tomMatch631__end__62=tomMatch631_59;
} else {
tomMatch631__end__62= tomMatch631__end__62.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__62==tomMatch631_59) ));
}
}
}
if ( tomMatch631__end__58.isEmptyconcTNode() ) {
tomMatch631__end__58=tomMatch631_55;
} else {
tomMatch631__end__58= tomMatch631__end__58.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__58==tomMatch631_55) ));
}
}
}
if ( tomMatch631__end__54.isEmptyconcTNode() ) {
tomMatch631__end__54=tomMatch631_47;
} else {
tomMatch631__end__54= tomMatch631__end__54.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__54==tomMatch631_47) ));
}
}
}
}

}
{
if ( (tom_option instanceof tom.library.adt.tnode.types.TNode) ) {
if ( ((( tom.library.adt.tnode.types.TNode )tom_option) instanceof tom.library.adt.tnode.types.tnode.ElementNode) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_101= (( tom.library.adt.tnode.types.TNode )tom_option).getAttrList() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631_102= (( tom.library.adt.tnode.types.TNode )tom_option).getChildList() ;
if ( "string".equals( (( tom.library.adt.tnode.types.TNode )tom_option).getName() ) ) {
if ( ((tomMatch631_101 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_101 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__108=tomMatch631_101;
do {
{
if (!( tomMatch631__end__108.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_131= tomMatch631__end__108.getHeadconcTNode() ;
if ( (tomMatch631_131 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "altName".equals( tomMatch631_131.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_109= tomMatch631__end__108.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__112=tomMatch631_109;
do {
{
if (!( tomMatch631__end__112.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_136= tomMatch631__end__112.getHeadconcTNode() ;
if ( (tomMatch631_136 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "attrName".equals( tomMatch631_136.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_113= tomMatch631__end__112.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__116=tomMatch631_113;
do {
{
if (!( tomMatch631__end__116.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_141= tomMatch631__end__116.getHeadconcTNode() ;
if ( (tomMatch631_141 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "description".equals( tomMatch631_141.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_117= tomMatch631__end__116.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__120=tomMatch631_117;
do {
{
if (!( tomMatch631__end__120.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_146= tomMatch631__end__120.getHeadconcTNode() ;
if ( (tomMatch631_146 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "name".equals( tomMatch631_146.getName() ) ) {
 tom.library.adt.tnode.types.TNodeList  tomMatch631_121= tomMatch631__end__120.getTailconcTNode() ;
 tom.library.adt.tnode.types.TNodeList  tomMatch631__end__124=tomMatch631_121;
do {
{
if (!( tomMatch631__end__124.isEmptyconcTNode() )) {
 tom.library.adt.tnode.types.TNode  tomMatch631_151= tomMatch631__end__124.getHeadconcTNode() ;
if ( (tomMatch631_151 instanceof tom.library.adt.tnode.types.tnode.AttributeNode) ) {
if ( "value".equals( tomMatch631_151.getName() ) ) {
if ( ((tomMatch631_102 instanceof tom.library.adt.tnode.types.tnodelist.ConsconcTNode) || (tomMatch631_102 instanceof tom.library.adt.tnode.types.tnodelist.EmptyconcTNode)) ) {
if ( tomMatch631_102.isEmptyconcTNode() ) {

list = 
tom_append_list_concPlatformOption(list, tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make( tomMatch631_146.getValue() ,  tomMatch631_131.getValue() ,  tomMatch631_141.getValue() ,  tom.platform.adt.platformoption.types.platformvalue.StringValue.make( tomMatch631_151.getValue() ) ,  tomMatch631_136.getValue() ) , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) );


}
}
}
}
}
if ( tomMatch631__end__124.isEmptyconcTNode() ) {
tomMatch631__end__124=tomMatch631_121;
} else {
tomMatch631__end__124= tomMatch631__end__124.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__124==tomMatch631_121) ));
}
}
}
if ( tomMatch631__end__120.isEmptyconcTNode() ) {
tomMatch631__end__120=tomMatch631_117;
} else {
tomMatch631__end__120= tomMatch631__end__120.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__120==tomMatch631_117) ));
}
}
}
if ( tomMatch631__end__116.isEmptyconcTNode() ) {
tomMatch631__end__116=tomMatch631_113;
} else {
tomMatch631__end__116= tomMatch631__end__116.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__116==tomMatch631_113) ));
}
}
}
if ( tomMatch631__end__112.isEmptyconcTNode() ) {
tomMatch631__end__112=tomMatch631_109;
} else {
tomMatch631__end__112= tomMatch631__end__112.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__112==tomMatch631_109) ));
}
}
}
if ( tomMatch631__end__108.isEmptyconcTNode() ) {
tomMatch631__end__108=tomMatch631_101;
} else {
tomMatch631__end__108= tomMatch631__end__108.getTailconcTNode() ;
}

}
} while(!( (tomMatch631__end__108==tomMatch631_101) ));
}
}
}
}

}


}



}
if ( tomMatch630__end__11.isEmptyconcTNode() ) {
tomMatch630__end__11=tomMatch630_3;
} else {
tomMatch630__end__11= tomMatch630__end__11.getTailconcTNode() ;
}

}
} while(!( (tomMatch630__end__11==tomMatch630_3) ));
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
