/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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

package sdfgom;

import java.lang.*;
import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import mept.*;
import mept.types.*;
import tom.library.utils.ATermConverter;

public class SDFConverter implements ATermConverter {

  %include { sdf/Sdf.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  /**
   * Method from ATermConverter interface
   */
  public ATerm convert(ATerm at) {
    // is this switch really useful ? a simple if (at.getType() == ATerm.APPL) {} would be enough
    switch(at.getType()) {
      case ATerm.APPL:
        ATermAppl appl = (ATermAppl) at;
        String name = appl.getName();
        ATermList args = appl.getArguments();
        //int arity = appl.getArity();

        if(name.equals("sort")) { // subcase : "sort -> my_sort" / arity = 1
          at = factory.makeApplList(factory.makeAFun("my_sort",1,false),args);

        } else if(name.equals("more-chars")) { // subcase : "more-chars -> more_chars" / arity = 1
          at = factory.makeApplList(factory.makeAFun("more_chars",1,false),args);

        } else if(name.equals("context-free-syntax")) { // subcase : "context-free-syntax -> context_free_syntax" / arity = 1
          at = factory.makeApplList(factory.makeAFun("context_free_syntax",1,false),args);

        } else if(name.equals("no-attrs")) { // subcase : "no-attrs -> no_attrs" / arity = 0
          at = factory.makeAppl(factory.makeAFun("no_attrs",0,false));

        } else if(name.equals("default")) { // subcase : "default -> lit_default" / arity = 0
          at = factory.makeApplList(factory.makeAFun("lit_default",1,false),args);

        } else if(name.equals("assoc")) { // subcase : "assoc -> my_assoc" && arity =0
          if(appl.getArity() == 0) { // test needed because there are 2 "assoc", the one whose arity equal to 0 is the good one
            at = factory.makeAppl(factory.makeAFun("my_assoc",0,false));
          }

        } else {
          return at;
        }
    }
    return at;

  } //convert


} //SDFConverter
