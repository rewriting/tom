/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRST, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

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
        default:
          System.out.println("Extend Collect.apply to " + length + " arguments");
          System.exit(1);
    }
    return false;
  }
}

  
