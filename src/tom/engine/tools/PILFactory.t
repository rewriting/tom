/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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
 * **/

package jtom.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jtom.TomBase;
import jtom.adt.tomsignature.*;
import jtom.adt.tomsignature.types.*;
import jtom.tools.TomGenericPlugin;
import tom.library.traversal.Collect2;
import tom.library.traversal.Replace1;
import aterm.ATerm;

public class PILFactory extends TomBase {
  
  %include{ adt/TomSignature.tom }

  /**
   * level specifies the level of details of the output
   * 0 is identity
   * 1 removes options
   */
  private int level = 0;
  
  public PILFactory() {
    super();
    init(1);
  }

  void init (int level) {
    this.level = level;
  }

  public TomTerm reduce(TomTerm subject) {
    TomTerm res = subject;
    res = remove(res);

    return res;
  }

  TomTerm remove(TomTerm subject) {
    return (TomTerm) replace_remove.apply(subject);
  }

  Replace1 replace_remove = new Replace1() {
      public ATerm apply(ATerm subject) {
        // removes options
        if (subject instanceof OptionList) {
          return `concOption();
        }
        else if (subject instanceof Option) {
          return `noOption();
        }

        // removes TargetLanguage
        else if (subject instanceof TargetLanguage) {
          return `noTL();
        }

        // removes Type
        else if (subject instanceof TomType) {
          %match(TomType subject) {
            Type[] -> { return `EmptyType();}
          }
        }

        // clean Expressions
        else if (subject instanceof Expression) {
          %match(Expression subject) {
            Cast[source=e]          -> { return this.apply(`e); }
            Or[arg1=e,arg2=FalseTL()] -> { return this.apply(`e); }
            EqualFunctionSymbol(type,t1,appl@Appl[args=concTomTerm(x,_*)]) -> {
              return this.apply(`EqualFunctionSymbol(type,t1,appl.setArgs(concTomTerm())));
            } 
          }
        }


        /* Default case : Traversal */
        return traversal().genericTraversal(subject,this);
      }
    };

  public String prettyPrintCompiledMatch(TomTerm subject) {
    String res = "";
    Collection matches = collectMatch(subject);
    Iterator it = matches.iterator();
    while(it.hasNext()) {
      Instruction cm = (Instruction) it.next();
      res += prettyPrint(cm);
      res += "\n";
    }
    return res;
  }
  String prettyPrint(ATerm subject) {
    if (subject instanceof Instruction) {
      %match(Instruction subject) {
        CompiledMatch(automata,_) -> { return prettyPrint(`automata); }
        Let(variable,src,body) -> {
          return "let " + prettyPrint(variable) + " = " + prettyPrint(src) + " in \n" + prettyPrint(body) + "";
        }
        IfThenElse(cond,success,failure) -> {
          return "if " + prettyPrint(cond) + " then \n\t" + prettyPrint(success) + "\n\telse " + prettyPrint(failure) + "\n";
        }
      }
    }
    else if (subject instanceof TomTerm) {
      %match(TomTerm subject) {
        Variable[astName=Name(name)] -> {
          return `name;
        }
      }
    }
    return subject.toString();
  }

  private Collect2 collect_match = new Collect2() {
      public boolean apply(ATerm subject, Object astore) {
        Collection store = (Collection)astore;
        if (subject instanceof Instruction) {
          %match(Instruction subject) {
            CompiledMatch[automataInst=automata]  -> {
              store.add(subject);
            }
            _ -> { return true; }
          }
        } else { return true; }
      }
    };
  
  public Collection collectMatch(TomTerm subject) {
    Collection result = new HashSet();
    traversal().genericCollect(subject,collect_match,result);
    return result;
  }


}
