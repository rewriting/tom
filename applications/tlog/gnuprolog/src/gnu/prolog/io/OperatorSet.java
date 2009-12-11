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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import gnu.prolog.term.CompoundTermTag;

final public class OperatorSet
{
  static class OperatorLevel
  {
    OperatorLevel(int p)
    {
      priority = p;
      //usage = 0;
    }
    int priority;
    //int usage;
  }
  ArrayList priorityLevels = new ArrayList();
  HashMap xfOps = new HashMap();
  HashMap fxOps = new HashMap();

  public synchronized Operator lookupXf(String value)
  {
    Operator op = (Operator)xfOps.get(value);
    return op != null? op: Operator.nonOperator;
  }

  public synchronized Operator lookupFx(String value)
  {
    Operator op = (Operator)fxOps.get(value);
    return op != null? op: Operator.nonOperator;
  }
  
  /** get all oprators currently in the set */
  public synchronized HashSet getOperators()
  {
    HashSet rc = new HashSet();
    rc.addAll(fxOps.values());
    rc.addAll(xfOps.values());
    return rc;
  }

  /** remove operator from oprator set*/
  public synchronized void remove(int specifier, String name)
  {
    switch (specifier)
    {
    case Operator. FX:
    case Operator. FY:
      fxOps.remove(name);
      break;
    case Operator.XF :
    case Operator.YF :
    case Operator.XFX:
    case Operator.XFY:
    case Operator.YFX:
      xfOps.remove(name);
      break;
    }
  }
  
  /** add operator to oprator set*/
  public synchronized Operator add(int priority, int specifier, String name)
  {
    int i,n = priorityLevels.size();
    //System.out.println(name+" prio:" + priority+ " priolvs:"+n);
    OperatorLevel ol = null;
    int nlv = 0;
    for (i = n-1; i>=0; i--)
    {
      ol = (OperatorLevel)priorityLevels.get(i);
      if (ol.priority == priority)
      {
        nlv = i;
        break;
      }
      else if(ol.priority < priority)
      {
        nlv = i + 1;
        ol = new OperatorLevel(priority);
        if (nlv == n) // w/a
        {
          priorityLevels.add(ol);
        }
        else
        {
          priorityLevels.add(nlv,ol);
        }
        break;
      }
    }
    if(i<0)
    {
      nlv = 0;
      ol = new OperatorLevel(priority);
      if (priorityLevels.size() == 0) //w/a
      {
        priorityLevels.add(ol);
      }
      else
      {
        priorityLevels.add(nlv,ol);
      }
    }
    //ol.usage++;

    Operator op = new Operator(name,specifier,priority);

    switch (specifier)
    {
    case Operator. FX:
    case Operator. FY:
      fxOps.put(name,op);
      break;
    case Operator.XF :
    case Operator.YF :
    case Operator.XFX:
    case Operator.XFY:
    case Operator.YFX:
      xfOps.put(name,op);
      break;
    }
    return op;
  }

  public synchronized int getNextLevel(int priority)
  {
    int i,n = priorityLevels.size();
    for (i = n-1; i>=0; i--)
    {
      int p = ((OperatorLevel)priorityLevels.get(i)).priority;
      if (p<=priority)
      {
        return p;
      }
    }
    return 0;
  }

  public synchronized int getCommaLevel()
  {
    return 1000;
  }

  public synchronized int getMaxLevel()
  {
    return 1200;
  }

  public OperatorSet(boolean defaultSet)
  {
    if (defaultSet)
    {
      initDefault();
    }
  }

  public OperatorSet()
  {
    this(true);
  }

  private void initDefault()
  {
    add(1200, Operator.XFX, "-->");
    add(1200, Operator.XFX, ":-" );
    add(1200, Operator.FX , ":-" );
    add(1200, Operator.FX , "?-" );
    add(1100, Operator.XFY, ";"  );
    add(1050, Operator.XFY, "->" );
    add(1000, Operator.XFY, ","  );
    add( 900, Operator.FX,  "\\+");
    add( 700, Operator.XFX, "="  );
    add( 700, Operator.XFX, "\\=" );
    add( 700, Operator.XFX, "==" );
    add( 700, Operator.XFX, "\\==");
    add( 700, Operator.XFX, "@<" );
    add( 700, Operator.XFX, "@=<");
    add( 700, Operator.XFX, "@>" );
    add( 700, Operator.XFX, "@>=");
    add( 700, Operator.XFX, "=..");
    add( 700, Operator.XFX, "is" );
    add( 700, Operator.XFX, "=:=");
    add( 700, Operator.XFX, "=\\=");
    add( 700, Operator.XFX, "<"  );
    add( 700, Operator.XFX, "=<" );
    add( 700, Operator.XFX, ">"  );
    add( 700, Operator.XFX, ">=" );
    add( 600, Operator.XFX, ":"  );
    add( 500, Operator.YFX, "+"  );
    add( 500, Operator.YFX, "-"  );
    add( 500, Operator.YFX, "/\\" );
    add( 500, Operator.YFX, "\\/" );
    add( 400, Operator.YFX, "*"  );
    add( 400, Operator.YFX, "/"  );
    add( 400, Operator.YFX, "//" );
    add( 400, Operator.YFX, "rem");
    add( 400, Operator.YFX, "mod");
    add( 400, Operator.YFX, "<<" );
    add( 400, Operator.YFX, ">>" );
    add( 200, Operator.XFX, "**" );
    add( 200, Operator.XFY, "^"  );
    add( 200, Operator.FY , "-"  );
    add( 200, Operator.FY , "\\"  );
    add( 100, Operator.XFX, "@"  );
  }

  public synchronized Operator getOperatorForTag(CompoundTermTag tag)
  {
    if (tag.arity == 1)
    {
      Operator op = lookupFx(tag.functor.value);
      if (op == Operator.nonOperator)
      {
        op = lookupXf(tag.functor.value);
      }
      if (op.tag != tag)
      {
        op = Operator.nonOperator;
      }
      return op;
    }
    if (tag.arity == 2)
    {
      Operator op = lookupXf(tag.functor.value);
      if (op.tag != tag)
      {
        op = Operator.nonOperator;
      }
      return op;
    }
    return Operator.nonOperator;
  }
}

