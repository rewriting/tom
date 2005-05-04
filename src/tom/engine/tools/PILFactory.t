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
import aterm.ATermList;

public class PILFactory extends TomBase {
  
  %include{ adt/tomsignature/TomSignature.tom }

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

 public ATerm remove(ATerm subject) {
    return replace_remove.apply(subject);
  }

  Replace1 replace_remove = new Replace1() {
      public ATerm apply(ATerm subject) {
        // removes options
        if (subject instanceof OptionList) {
          return `concOption();
        } else if (subject instanceof Option) {
          return `noOption();
        } else if (subject instanceof TargetLanguage) {
          // removes TargetLanguage
          return `noTL();
        } else if (subject instanceof TomType) {
          // removes Type
          %match(TomType subject) {
            Type[] -> { return `EmptyType();}
          }
        } else if (subject instanceof Expression) {
        // clean Expressions
          %match(Expression subject) {
            Cast[source=e]          -> { return this.apply(`e); }
            Or[arg1=e,arg2=FalseTL()] -> { return this.apply(`e); }
            EqualFunctionSymbol(type,t1,appl@RecordAppl[slots=concSlot(x,_*)]) -> {
              return this.apply(`EqualFunctionSymbol(type,t1,appl.setSlots(concSlot())));
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
  public String prettyPrint(ATerm subject) {
    if (subject instanceof Instruction) {
      %match(Instruction subject) {
        CompiledMatch(automata,_) -> { 
          return prettyPrint(`automata); 
        }
        
     
        Let(variable,src,body) -> {
          return "let " + prettyPrint(variable) + " = " + prettyPrint(src) + " in\n\t" + prettyPrint(body).replaceAll("\n","\n\t");
        }

        LetRef(variable,src,body) -> {
          return "letRef " + prettyPrint(variable) + " = " + prettyPrint(src) + " in\n\t" + prettyPrint(body).replaceAll("\n","\n\t");
        }

        LetAssign(variable,src,body) -> {
          return "letAssign " + prettyPrint(variable) + " = " + prettyPrint(src) + " in\n\t" + prettyPrint(body).replaceAll("\n","\n\t");
        }

        Assign(variable,src) -> {
          return "Assign " + prettyPrint(variable) + " = " + prettyPrint(src) ;
        }
        

        DoWhile(doInst,condition) ->{
          return "do\n\t " + prettyPrint(doInst).replaceAll("\n","\n\t") +"while "+ prettyPrint(condition);
        }

        WhileDo(condition,doInst) ->{
          return "while "+ prettyPrint(condition)+" do\n\t " + prettyPrint(doInst).replaceAll("\n","\n\t");
        }
  

        If(cond,success,Nop()) -> {
          return  "if " + prettyPrint(cond) + " then \n\t" + prettyPrint(success).replaceAll("\n","\n\t"); 
        }

        If(cond,success,failure) -> {
          return "if " + prettyPrint(cond) + " then \n\t" + prettyPrint(success).replaceAll("\n","\n\t") + "\n\telse " + prettyPrint(failure).replaceAll("\n","\n\t")+"\n";
        }

      
        AbstractBlock(concInstruction(x*,Nop(),y*)) -> {
          return prettyPrint(`AbstractBlock(concInstruction(x*,y*)));
        }
  
        AbstractBlock(instList) -> {
          return prettyPrint(`instList);
        }

        UnamedBlock(instList) -> {
          return prettyPrint(`instList);
        }

        NamedBlock(name,instList) -> {
          return name+" : "+prettyPrint(`instList);
        }
        

       TypedAction(_,_,_) -> {
         return "targetLanguageInstructions";
        }

        CompiledPattern(_,automata) -> { 
          return prettyPrint(`automata); 
        }
        
        CheckStamp(_) -> {
          return "";
        }
                   
      }


    } else if (subject instanceof Expression) {
      %match(Expression subject) {
        TomTermToExpression(astTerm) -> {
          return prettyPrint(astTerm);
        }

        EqualFunctionSymbol(astType,exp1,exp2) -> {
          return "is_fun_sym("+prettyPrint(exp1)+","+prettyPrint(exp2)+")";
        }

        Negation(exp) -> {
          return "not "+prettyPrint(exp);
        }
        
        IsEmptyList[variable=kid1] -> {
          return "is_empty("+prettyPrint(kid1)+")";
        }

        EqualTerm(_,kid1,kid2) -> {
          return "equal("+prettyPrint(kid1)+","+prettyPrint(kid2)+")";
        }

        GetSliceList(astName,variableBeginAST,variableEndAST) -> {
          return "getSliceList("+prettyPrint(astName)+","+prettyPrint(variableBeginAST)+","+prettyPrint(variableEndAST)+")";
        }


        GetHead[variable=variable] -> {
          return "getHead("+prettyPrint(variable)+")";
        }
        
        GetTail[variable=variable] -> {
          return "getTail("+prettyPrint(variable)+")";
        }

        GetSlot(codomain,astName,slotNameString,variable) -> {
          return "get_slot_"+prettyPrint(astName)+"_"+slotNameString+"("+prettyPrint(variable)+")";
        }
      }

    } else if (subject instanceof TomTerm) {
      %match(TomTerm subject) {
        Variable(_,name,_,_) -> {
          return prettyPrint(`name);
        }

        VariableStar(_,name,_,_) -> {
          return prettyPrint(`name);
        }

        Ref(term) -> {
          return prettyPrint(`term);
        }

        RecordAppl(optionList,nameList,args,constraints) ->{
          return prettyPrint(nameList); 
        }
      }
    } else if (subject instanceof TomName) {
      %match(TomName subject) {
        PositionName(number_list) -> {
          return "t" + numberListToIdentifier(number_list);
        }
        Name(string) -> {
          return string;
        }
      
      }
    } else if (subject instanceof TomNumber) {
      %match(TomNumber subject) {
        Number(n) -> {
          return ""+n;
        }

        NameNumber(name) -> {
          return prettyPrint(`name);
        }
        
        ListNumber(number) -> {
          return "listNumber"+prettyPrint(number);
        }

        Begin(number) -> {
          return "begin"+prettyPrint(number);
        }

        End(number) -> {
          return "end"+prettyPrint(number);
        }

      }
      
    } else if(subject instanceof InstructionList) {
      ATermList list = (ATermList)subject;
      if(list.isEmpty()) {
        return "";
      } else {
        return prettyPrint(list.getFirst()) + "\n" + prettyPrint(list.getNext());
      }
    }  else if(subject instanceof TomNumberList) {
      ATermList list = (ATermList)subject;
      if(list.isEmpty()) {
        return "";
      } else {
        return prettyPrint(list.getFirst()) + prettyPrint(list.getNext());
      }
    }
    else if(subject instanceof ATermList) {
      ATermList list = (ATermList)subject;
      if(list.isEmpty()) {
        return "";
      } else {
        return prettyPrint(list.getFirst()) + " " + prettyPrint(list.getNext());
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
