/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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

  %include{ adt/tnode/TNode.tom }

  %include{ adt/platformoption/PlatformOption.tom }

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
    PlatformOptionList list = `concPlatformOption();
    %match(optionsNode) {
      <options>(_*,option,_*)</options> -> {
        %match(option) {
          <boolean [name = n, altName = an, description = d, value = v] /> -> {
            PlatformBoolean bool = Boolean.valueOf(`v).booleanValue()?`True():`False();
            list = `concPlatformOption(list*, PluginOption(n, an, d, BooleanValue(bool), ""));
          }
          <integer [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, IntegerValue(Integer.parseInt(v)), at));
          }
          <string [name = n, altName = an, description = d, value = v, attrName = at] /> -> {
            list = `concPlatformOption(list*, PluginOption(n, an, d, StringValue(v), at));
          }
        }
      }
    }
    return list;
  }

}
