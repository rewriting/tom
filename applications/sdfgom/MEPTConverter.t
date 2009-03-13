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

public class MEPTConverter implements ATermConverter {

  %include { mept/Mept.tom }

  public static PureFactory factory = SingletonFactory.getInstance();

  /** Subroutine called by the convert method 
   * @param at the ATerm that needs to be transformed
   * @return an ATerm based on the "my_char(42)" template, using the ATerm parameter as argument
   */
  protected ATermAppl getMyCharATerm(ATerm arg) {
    return factory.makeAppl(factory.makeAFun("my_char",1,false),arg);
  }

  /** Subroutine called by the convert method 
   * @param at the ATerm that needs to be transformed
   * @return an ATerm based on the "character(42)" template, using the ATerm parameter as argument
   */
  protected ATermAppl getCharacterATerm(ATerm arg) {
    return factory.makeAppl(factory.makeAFun("character",1,false),arg);
  }

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

        if(name.equals("char-class")) { // subcase : "char_class([CharRange])"
          ATermList char_args = (ATermList)args.getFirst();
          if(!char_args.isEmpty()) {
            for(int i=0 ; i<char_args.getLength() ; i++) {
              ATerm elt = char_args.elementAt(i);
              if(elt.getType() == ATerm.INT) {
                char_args = char_args.replace(getCharacterATerm(elt),i);
              } 
            }
            at = factory.makeAppl(factory.makeAFun("char_class",1,false),char_args);
          }

        } else if(name.equals("term")) { // "term(cons(x)) -> cons(x)"
          ATermAppl term_arg = (ATermAppl)appl.getArgument(0);
          //System.out.println("term_arg = " + term_arg);
          if(term_arg.getName().equals("cons")) {
            at = term_arg;
          }

        } else if(name.equals("amb")) { // subcase : "amb([Tree])"
          ATermList amb_args = (ATermList)args.getFirst();
          if(!amb_args.isEmpty()) {
            for(int i=0 ; i<amb_args.getLength() ; i++) {
              ATerm elt = amb_args.elementAt(i);
              if(elt.getType() == ATerm.INT) {
                amb_args = amb_args.replace(getMyCharATerm(elt),i);
              }
            }
            at = factory.makeAppl(factory.makeAFun("amb",1,false),amb_args);
          }

        } else if(name.equals("appl")) { // subcase : "appl(Production,[Tree])"
          ATermList appl_args = (ATermList)args.elementAt(1);
          if(!appl_args.isEmpty()) {
            for(int i=0 ; i<appl_args.getLength() ; i++) {
              ATerm elt = appl_args.elementAt(i);
              if(elt.getType() == ATerm.INT) {
                appl_args = appl_args.replace(getMyCharATerm(elt),i);
              }
            }
            // doesn't work -> bug with args
            // ATermList newArgs = factory.makeList(args.getFirst(),appl_args);
            // makeAFun(String name, int arity, boolean isQuoted)
            // at = factory.makeApplList(factory.makeAFun("appl",1,false),newArgs);
            // correction :
            at = factory.makeAppl(factory.makeAFun("appl",2,false),args.getFirst(),appl_args);
          }

        } else if(name.equals("sort")) { // subcase : "sort -> my_sort" / arity = 1
          ATerm sort_arg = args.getFirst();
          AFun myAFun = factory.makeAFun("my_sort",1,false);
          at = factory.makeAppl(myAFun,sort_arg);

        } else if(name.equals("no-attrs")) { // subcase : "no-attrs -> no_attrs" / arity = 0
          AFun myAFun = factory.makeAFun("no_attrs",0,false);
          at = factory.makeAppl(myAFun);

        } else if(name.equals("assoc")) { // subcase : "assoc -> my_assoc" && arity =0
          if(appl.getArity() == 0) { // test needed because there are 2 "assoc", the one whose arity equal to 0 is the good one
            AFun myAFun = factory.makeAFun("my_assoc",0,false);
            at = factory.makeAppl(myAFun);
          }

        } else if(name.equals("non-assoc")) { // subcase : "non-assoc -> non_assoc" / arity = 0
          AFun myAFun = factory.makeAFun("non_assoc",0,false);
          at = factory.makeAppl(myAFun);

        } else if(name.equals("iter-sep")) { // subcase : "iter-sep -> iter_sep" / arity = 2
          at = factory.makeApplList(factory.makeAFun("iter_sep",2,false),args);

        } else if(name.equals("iter-sep-n")) { // subcase : "iter-sep-n -> iter_sep_n" / arity = 3
          at = factory.makeApplList(factory.makeAFun("iter_sep_n",3,false),args);

        } else if(name.equals("iter-plus")) { // subcase : "iter_plus -> iter_plus" / arity = 1
          at = factory.makeApplList(factory.makeAFun("iter_plus",1,false),args);

        } else if(name.equals("iter-plus-sep")) { // subcase : "iter-plus-sep -> iter_sep" / arity = 2
          at = factory.makeApplList(factory.makeAFun("iter_sep",2,false),args);


        } else if(name.equals("iter-star")) { // subcase : "iter-star -> iter_star" / arity = 1
          ATerm iter_arg = args.getFirst();
          AFun myAFun = factory.makeAFun("iter_star",1,false);
          at = factory.makeAppl(myAFun,iter_arg);

        } else if(name.equals("iter-star-sep")) { // subcase : "iter-star-sep -> iter_star_sep" / arity = 2
          at = factory.makeApplList(factory.makeAFun("iter_star_sep",2,false),args);

        } else if(name.equals("iter-n")) { // subcase : "iter-n -> iter_n" / arity = 2
          at = factory.makeApplList(factory.makeAFun("iter_n",2,false),args);

        } else {
          return at;
        }
    }
    return at;

  } //convert


} //MEPTConverter
