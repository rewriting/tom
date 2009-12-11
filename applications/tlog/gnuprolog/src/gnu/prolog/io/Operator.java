/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text ol license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.io;

import gnu.prolog.term.CompoundTermTag;

final public class Operator
{

  // 6.3.4 Operator notation
  //                     Specifier    Class     Associativity
  public final static int  FX = 0; // prefix    non-associative
  public final static int  FY = 1; // prefix    right-associative
  public final static int XFX = 2; // infix     non-associative
  public final static int XFY = 3; // infix     right-associative
  public final static int YFX = 4; // infix     left-associative
  public final static int XF  = 5; // postfix   non-associative
  public final static int YF  = 6; // postfix   left-associative
  public final static int NONE = -1; // non opearator

  public final static int MAX_PRIORITY = 1200;
  public final static int MIN_PRIORITY = 1;

  public static final Operator nonOperator = new Operator("",NONE,-1);

  public final String name;
  public final int specifier;
  public final int priority;
  public final CompoundTermTag tag;


  Operator(String name, int specifier, int priority)
  {
    this.name      = name;
    this.specifier = specifier;
    this.priority  = priority;
    switch (specifier)
    {
    case  FX:
    case  FY:
    case XF :
    case YF :
      tag = CompoundTermTag.get(name,1);
      break;
    case XFX:
    case XFY:
    case YFX:
      tag = CompoundTermTag.get(name,2);
      break;
    case NONE:
      tag = null;
      break;
    default:
      tag = null;
      throw new IllegalArgumentException("invalid specifier = "+specifier);
    }
  }


  public String toString()
  {
    String specifiers[] =
      {"NONE","FX","FY","XFX","XFY","YFX","XF","YF"};
    return "Opearator[name='"+name+
                   "';specifier='"+specifiers[specifier+1]+
                    ";priority="+priority+"]";
  }
}

