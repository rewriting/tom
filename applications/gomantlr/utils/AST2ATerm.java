/*
 *
 * AST2ATerm
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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
 * Eric Deplagne <Eric.Deplagne@loria.fr>
 *
 **/

package utils;

import aterm.ATerm;
import aterm.ATermList;
import aterm.ATermFactory;
import aterm.pure.SingletonFactory;

import antlr.collections.AST;

public class AST2ATerm {
    public static ATerm getATerm(AST ast,String[] names, ATermFactory factory) {
        ATermList aterm;
        
        ATerm[] aterms=new ATerm[ast.getNumberOfChildren()];
        
        int i=0;
        
        for(AST child=ast.getFirstChild();child!=null;child=child.getNextSibling()) {
            aterms[i++]=getATerm(child,names);
        }
        
        aterm=factory.makeList();

        while(i>0) {
            aterm=factory.makeList(aterms[--i],aterm);
        }
        
        return factory.makeAppl(
            factory.makeAFun(names[ast.getType()],2,false),
            factory.makeAppl(
                factory.makeAFun("NodeInfo",3,false),
                factory.makeAppl(factory.makeAFun(ast.getText(),0,false)),
                factory.makeInt(ast.getLine()), 
                factory.makeInt(ast.getColumn())),
            aterm);
    }
    
    public static ATerm getATerm(AST ast,String[] names) {
        return getATerm(ast,names,SingletonFactory.getInstance());
    }
}
