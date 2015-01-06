/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package sdfgom.converter;

import java.lang.*;
import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.ATermList;
import aterm.pure.PureFactory;
import aterm.pure.SingletonFactory;
import tom.library.utils.ATermConverter;

public class MeptConverter implements ATermConverter {

  private static PureFactory factory = SingletonFactory.getInstance();
  private static LRUCache<ATerm,ATerm> cache = new LRUCache<ATerm,ATerm>(1024);

  /**
   * Method from ATermConverter interface
   */
  public ATerm convert(ATerm at) {
    ATerm result = cache.get(at);
    if(result!=null) {
      return result;
    }

    switch(at.getType()) {
      case ATerm.APPL:
        //System.out.print("convert: " + at + " --> ");
        ATermAppl appl = renameAppl((ATermAppl) at);
        String name = appl.getName();
        result = appl;

        if(name.equals("char_class") && appl.getArity()==1) { // subcase : "char_class([CharRange])"
          ATerm arg = appl.getArgument(0);
          if(arg instanceof ATermList) {
            result = appl.setArgument(encodeIntList((ATermList)arg,"character"),0);
          }

        } else if(name.equals("amb") && appl.getArity()==1) { // subcase : "amb([Tree])"
          ATerm arg = appl.getArgument(0);
          if(arg instanceof ATermList) {
            result = appl.setArgument(encodeIntList((ATermList)arg,"my_char"),0);
          }

        } else if(name.equals("appl") && appl.getArity()==2) { // subcase : "appl(Production,[Tree])"
          ATerm arg = appl.getArgument(1);
          if(arg instanceof ATermList) {
            result = appl.setArgument(encodeIntList((ATermList)arg,"my_char"),1);
          }

        } else if(name.equals("term") && appl.getArity()==1) { // "term(cons(x)) -> cons(x)"
          ATerm arg = appl.getArgument(0);
          if(arg instanceof ATermAppl) {
            if(((ATermAppl)arg).getName().equals("cons")) {
              result = (ATermAppl)arg;
            }
          }

        } 
        // default case: perform classical renaming
        //System.out.println(result);
        cache.put(at,result);
        return result;

      default:
        cache.put(at,at);
        return at;
    }

  } //convert

  /** 
   * wrap the integers contained in an ATermList
   * @param l the ATermList that needs to be transformed
   * @param constructorName the constructor to introduce
   * @return the list where each integer is replaced by constructorName(int)
   */
  private ATermList encodeIntList(ATermList l,String constructorName) {
    if(l.isEmpty()) {
      return l;
    } else {
      ATerm elt = l.getFirst();
      if(elt.getType() == ATerm.INT) {
        elt = factory.makeAppl(factory.makeAFun(constructorName,1,false),elt);
      } 
      return factory.makeList(elt,encodeIntList(l.getNext(),constructorName));
    }
  }

  /**
    * rename into a valid Java identifier the name of an ATermAppl
    * @param the ATerm to rename
    * @return the renamed ATerm
    * '-' are replaced by '_'
    * the constant 'assoc' is renamed into 'my_assoc'
    * the constructor 'sort' is renamed into 'my_sort'
    */
  private ATermAppl renameAppl(ATermAppl appl) {
    AFun fun = appl.getAFun();
    if(!fun.isQuoted()) {
      String name = fun.getName().replaceAll("-","_");
      if(name.equals("sort")) {
        name = "my_sort";
      } else if(name.equals("assoc") && appl.getArity()==0) {
        name = "my_assoc";
      }
      appl = factory.makeAppl(factory.makeAFun(name,fun.getArity(),false),appl.getArgumentArray());
    }
    return appl; 
  }

}
