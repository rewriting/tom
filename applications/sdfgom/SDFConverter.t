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
        } else if(name.equals("short")) { 
          at = factory.makeApplList(factory.makeAFun("my_short",1,false),args);

        } else if(name.equals("more-chars")) { // subcase : "more-chars -> more_chars" / arity = 1
          at = factory.makeApplList(factory.makeAFun("more_chars",1,false),args);
        } else if(name.equals("one-char")) { // subcase : "one-char -> one_char" / arity = 1
          at = factory.makeApplList(factory.makeAFun("one_char",1,false),args);

        } else if(name.equals("lexical-syntax")) {
          at = factory.makeApplList(factory.makeAFun("lexical_syntax",1,false),args);
        } else if(name.equals("context-free-syntax")) { // subcase : "context-free-syntax -> context_free_syntax" / arity = 1
          at = factory.makeApplList(factory.makeAFun("context_free_syntax",1,false),args);

        } else if(name.equals("no-attrs")) { // subcase : "no-attrs -> no_attrs" / arity = 0Ar
          at = factory.makeAppl(factory.makeAFun("no_attrs",0,false));

        } else if(name.equals("term") 
            || name.equals("quoted") 
            || name.equals("unquoted") 
            || name.equals("lit")) { // "term(cons(x)) -> cons(x)"
          ATermAppl term_arg = (ATermAppl)appl.getArgument(0);
          if(term_arg.getName().equals("default")) {
            at = factory.makeApplList(factory.makeAFun(name,1,false),term_arg.getArguments());
          }
        //} else if(name.equals("default")) { // subcase : "default(foo) -> foo" / arity = 1
        //  System.out.println("defaut args = " + args);
        // at = args.getFirst(); 
        
    } else if(name.equals("char-class")) { 
          at = factory.makeApplList(factory.makeAFun("char_class",1,false),args);
    } else if(name.equals("simple-charclass")) { 
          at = factory.makeApplList(factory.makeAFun("simple_charclass",1,false),args);
    } else if(name.equals("conc-grammars")) { 
          at = factory.makeApplList(factory.makeAFun("conc_grammars",1,false),args);
    } else if(name.equals("iter-star")) { 
          at = factory.makeApplList(factory.makeAFun("iter_star",1,false),args);

    } else if(name.equals("iter-sep")) { 
          at = factory.makeApplList(factory.makeAFun("iter_sep",2,false),args);
    } else if(name.equals("iter-star-sep")) { 
          at = factory.makeApplList(factory.makeAFun("iter_star_sep",2,false),args);

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
