/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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

package tom.engine.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.*;
import tom.engine.tools.TomGenericPlugin;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.library.sl.*;

public class PILFactory {

  %include{ ../adt/tomsignature/TomSignature.tom }
  %include{ ../../library/mapping/java/util/types/Collection.tom }
  %include{ ../../library/mapping/java/sl.tom }

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

  public tom.library.sl.Visitable remove(tom.library.sl.Visitable subject) {
    try {
      return `TopDown(replaceRemove()).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return subject;
  }

  public TomTerm remove(TomTerm subject) {
    try {
      return (TomTerm) `TopDown(replaceRemove()).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return subject;
  }

  %strategy replaceRemove() extends `Identity() {
    visit OptionList {
      // removes options
      _ -> { return `concOption(); }
    }
    visit Option {
      _ -> { return `noOption(); }
    }
    visit TargetLanguage {
      // removes TargetLanguage
      _ -> { return `noTL(); }
    }
    visit Expression {
      // clean Expressions
      Cast[Source=e] -> { return (Expression) `TopDown(replaceRemove()).visitLight(`e); }
      Or[Arg1=e,Arg2=FalseTL()] -> { return (Expression) `TopDown(replaceRemove()).visitLight(`e); }
    }
  }

  public String prettyPrintCompiledMatch(tom.library.sl.Visitable subject) {
    StringBuilder res = new StringBuilder();
    Collection matches = collectMatch(subject);
    Iterator it = matches.iterator();
    while(it.hasNext()) {
      Instruction cm = (Instruction) it.next();
      res.append(prettyPrint(cm));
      res.append("\n");
    }
    return res.toString();
  }

  public String prettyPrint(tom.library.sl.Visitable subject) {
    %match(Instruction subject) {
      CompiledMatch(automata,_) -> { 
	return prettyPrint(`automata); 
      }

      Let(variable,src,body) -> {
	return "let " + prettyPrint(`variable) + " = " + prettyPrint(`src) + " in\n\t" + prettyPrint(`body).replace("\n","\n\t");
      }

      LetRef(variable,src,body) -> {
	return "letRef " + prettyPrint(`variable) + " = " + prettyPrint(`src) + " in\n\t" + prettyPrint(`body).replace("\n","\n\t");
      }

      LetAssign(variable,src,body) -> {
	return "letAssign " + prettyPrint(`variable) + " = " + prettyPrint(`src) + " in\n\t" + prettyPrint(`body).replace("\n","\n\t");
      }

      LetAssignArray(variable,index,value,body) -> {
	return "letAssignArray " + prettyPrint(`variable) + "["+prettyPrint(`index)+"] = " + prettyPrint(`value) + " in\n\t" + prettyPrint(`body).replace("\n","\n\t");
      }

      Assign(variable,src) -> {
	return "Assign " + prettyPrint(`variable) + " = " + prettyPrint(`src) ;
      }


      DoWhile(doInst,condition) ->{
	return "do\n\t " + prettyPrint(`doInst).replace("\n","\n\t") +"while "+ prettyPrint(`condition);
      }

      WhileDo(condition,doInst) ->{
	return "while "+ prettyPrint(`condition)+" do\n\t " + prettyPrint(`doInst).replace("\n","\n\t");
      }


      If(cond,success,Nop()) -> {
	return  "if " + prettyPrint(`cond) + " then \n\t" + prettyPrint(`success).replace("\n","\n\t"); 
      }

      If(cond,success,failure) -> {
	return "if " + prettyPrint(`cond) + " then \n\t" + prettyPrint(`success).replace("\n","\n\t") + "\n\telse " + prettyPrint(`failure).replace("\n","\n\t")+"\n";
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
	return `name + " : " + prettyPrint(`instList);
      }


      TypedAction(_,_,_) -> {
	return "targetLanguageInstructions";
      }

      CompiledPattern(_,automata) -> { 
	return prettyPrint(`automata); 
      }

    }

    %match(Expression subject) {
      TomTermToExpression(astTerm) -> {
	return prettyPrint(`astTerm);
      }

      IsSort[] -> {
	return "isSort\n\t";
      }

      IsFsym(name,term) -> {
        return "is_fun_sym(" + prettyPrint(`name) + "," + prettyPrint(`term) + ")";
      }

      Negation(exp) -> {
	return "not " + prettyPrint(`exp);
      }

       And(Arg1,Arg2) -> {
         return prettyPrint(`Arg1) + " && " + prettyPrint(`Arg2) ;
       }
       
       Or(Arg1,Arg2) -> {
         return prettyPrint(`Arg1) + " || " + prettyPrint(`Arg2) ;
       }

      IsEmptyList[Variable=kid1] -> {
	return "is_empty(" + prettyPrint(`kid1) + ")";
      }

      EqualTerm(_,kid1,kid2) -> {
	return prettyPrint(`kid1) + "==" + prettyPrint(`kid2) + ")";
      }

      GetSliceList(astName,variableBeginAST,variableEndAST,tail) -> {
	return "getSliceList("+prettyPrint(`astName)+","+prettyPrint(`variableBeginAST)+","+prettyPrint(`variableEndAST)+"," + prettyPrint(`tail) + ")";
      }

      GetHead[Variable=variable] -> {
	return "getHead("+prettyPrint(`variable)+")";
      }

      GetTail[Variable=variable] -> {
	return "getTail("+prettyPrint(`variable)+")";
      }

      GetSlot(_,astName,slotNameString,variable) -> {
	return "get_slot_"+prettyPrint(`astName)+"_"+`slotNameString+"("+prettyPrint(`variable)+")";
      }
           
      GetElement[Variable=variable,Index=index] -> {
        return prettyPrint(`variable)+"["+prettyPrint(`index)+"]";
      }

      GetSize[Variable=variable] -> {
        return "size("+prettyPrint(`variable)+")";
      }

      GreaterOrEqualThan(e1, e2) -> {
        return prettyPrint(`e1)+" >= "+prettyPrint(`e2);
      }
      GreaterThan(e1, e2) -> {
        return prettyPrint(`e1)+" > "+prettyPrint(`e2);
      }
      Integer(i) -> {
        return ""+`i;
      }
      AddOne(Variable) -> {
        return prettyPrint(`Variable)+"+1";
      }
      SubstractOne(Variable) -> {
        return prettyPrint(`Variable)+"-1";
      }
    }

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

      RecordAppl(_,nameList,_,_) ->{
        return prettyPrint(`nameList); 
      }
      ExpressionToTomTerm(astTerm) -> {
        return prettyPrint(`astTerm);
      }
      FunctionCall(AstName,AstType,Args) -> {
        String s = "";
        %match(Args) {
          concTomTerm(_*,x,_*) -> {
            s += ","+prettyPrint(`x);
          }
        }
        return prettyPrint(`AstName)+"("+s.substring(1, s.length())+")";
      }
      BuildEmptyArray[AstName=name,Size=size] -> {
        return "new "+prettyPrint(`name)+"["+prettyPrint(`size)+"]";
      }
    }

    %match(TomName subject) {
      PositionName(number_list) -> {
        return "t"+ TomBase.tomNumberListToString(`number_list);
      }
      Name(string) -> {
        return `string;
      }

    }

    %match(TomNumber subject) {
      Position(i) -> {
        return "" + `i;
      }

      NameNumber(name) -> {
        return prettyPrint(`name);
      }

      ListNumber(number) -> {
        return "listNumber"+`number;
      }

      Begin(number) -> {
        return "begin"+`number;
      }

      End(number) -> {
        return "end"+`number;
      }

    }

    if(subject instanceof InstructionList) {
      InstructionList list = (InstructionList)subject;
      if(list.isEmptyconcInstruction()) {
        return "";
      } else {
        return prettyPrint(list.getHeadconcInstruction()) + "\n" + prettyPrint(list.getTailconcInstruction());
      }
    }  else if(subject instanceof TomNumberList) {
      TomNumberList list = (TomNumberList)subject;
      if(list.isEmptyconcTomNumber()) {
        return "";
      } else {
        return prettyPrint(list.getTailconcTomNumber()) + prettyPrint(list.getTailconcTomNumber());
      }
    }
    return subject.toString();
  }

  %strategy collectMatch(c:Collection) extends `Identity() {
    visit Instruction {
      m@CompiledMatch[AutomataInst=_]  -> {
        c.add(`m);
      }
    }
  } 

  public Collection collectMatch(tom.library.sl.Visitable subject) {
    Collection result = new HashSet();
    try {
      `TopDown(collectMatch(result)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return result;
  }

}
