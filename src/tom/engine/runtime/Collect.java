/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2004 INRIA
			                      Nancy, France.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
    MA 02111-1307, USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/
package jtom.runtime;

import aterm.ATerm;

    /*
     * collects something in table
     * returns false if no more traversal is needed
     * returns true  if traversal has to be continued
     */

class Collect {
  boolean apply(ATerm t, Object args[]) {
    int length = args.length;
    switch(length) {
        case 0: return ((Collect1)this).apply(t);
        case 1: return ((Collect2)this).apply(t, args[0]);
        case 2: return ((Collect3)this).apply(t, args[0], args[1]);
        case 3: return ((Collect4)this).apply(t, args[0], args[1], args[2]);    
        default:
          System.out.println("Extend Collect.apply to " + length + " arguments");
          throw new RuntimeException("Extend Collect.apply to " + length + " arguments");
    }
  }
}

  
