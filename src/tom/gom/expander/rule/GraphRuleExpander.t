/*
 *
 * GOM
 *
 * Copyright (C) 2007, INRIA
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
 * Emilie Balland e-mail: Emilie.Balland@loria.fr
 *
 **/

package tom.gom.expander.rule;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.RuleTree;
import tom.gom.adt.rule.RuleAdaptor;
import tom.gom.adt.rule.types.*;
import tom.gom.adt.rule.types.term.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.library.sl.*;

public class GraphRuleExpander {

  %include { ../../adt/gom/Gom.tom}
  %include { ../../adt/rule/Rule.tom }
  %include { ../../../library/mapping/java/sl.tom}
  %include{../../../library/mapping/java/util/HashMap.tom}

  private ModuleList moduleList;
  private String sortname;
  private String modulename;

  public GraphRuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    this.sortname = sortname; 
    this.modulename = sdecl.getSort().getModuleDecl().getModuleName().getName();
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    parser.setTreeAdaptor(new RuleAdaptor());
    RuleList rulelist = `RuleList();
    try {
      RuleTree ast = (RuleTree)parser.graphruleset().getTree();
      rulelist = (RuleList) ast.getTerm();
    } catch (org.antlr.runtime.RecognitionException e) {
      getLogger().log(Level.SEVERE, "Cannot parse rules",
          new Object[]{});
      return `concHookDecl();
    }
    return expand(rulelist,stratname,defaultstrat,sdecl);
  }

  public HookDeclList expandFirstGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    HookDeclList expandedrules = expandGraphRules(sortname,stratname,defaultstrat,ruleCode, sdecl);
    HookDeclList commonpart = expandFirst(sdecl);
    return `concHookDecl(commonpart*,expandedrules*);  
  }

  private String getArobase(){
    return "@";
  }


  //add the common methods, includes and imports for all graphrule strategies of a sort 
  protected HookDeclList expandFirst(Decl sdecl) {
  StringBuffer output = new StringBuffer();
    output.append(
        %[
   %include {sl.tom }

 
  private static @sortname@ computeRhsWithPath(@sortname@ lhs, @sortname@ rhs, Position posRedex) {
    try {
      return (@sortname@) `TopDown(FromVarToPath(lhs,posRedex)).visit(`rhs);
    } catch(VisitFailure e) { 
      throw new RuntimeException("Unexpected strategy failure!");
    }
  }

  %typeterm Position{
      implement {Position}
      is_sort(t)     { t instanceof Position }
  }
 
  %strategy FromVarToPath(lhs:@sortname@,posRedex:Position) extends Identity() {
]%);

  %match(moduleList) {
      concModule(_*,Module[Sorts=concSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
    output.append(
        %[
    visit @`name@ {
      Var@`name@(name) -> { 
        Position wl = getVarPos(lhs,`name);
        Position wr = getEnvironment().getPosition();
        Position wwl = (Position) (new Position(new int[]{1})).add(posRedex).add(wl); 
        Position wwr = (Position) (new Position(new int[]{2})).add(wr); 
        Position res = (Position) wwl.sub(wwr);
        return Path@`name@.make(res);
      }
    }
]%);
    }
  }

  output.append(%[
  }

  private static Position getVarPos(@sortname@ term, String varname) {
    Position p = new Position();
    try {
      `OnceTopDown(GetVarPos(p,varname)).visit(term);
      return p;
    } catch (VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
      }
  }

  %strategy GetVarPos(Position p, String varname) extends Fail() {
]%);

 %match(moduleList) {
      concModule(_*,Module[Sorts=concSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
 output.append(
        %[
    visit @`name@ {
      v@getArobase()@Var@`name@(name) -> { 
        if (`name.equals(varname)) { 
          p.setValue(getEnvironment().getPosition().toArray()); 
          return `v; } 
      } 
    }
   ]%);
   }
 } 
  
  output.append(%[
  }
]%);

  String imports = %[
import tom.library.sl.*;
import java.util.*;
   ]%;

  //import all the constructors Path<Sort> of the module
  %match(moduleList) {
concModule(_*,Module[MDecl=ModuleDecl(GomModuleName(modulename),pkg),Sorts=concSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
  imports += %[
import @`pkg@.@`modulename.toLowerCase()@.types.@`name.toLowerCase()@.Path@`name@; 
  ]%;
    }
  }

   return `concHookDecl(BlockHookDecl(sdecl,Code(output.toString())),ImportHookDecl(sdecl,Code(imports)));
  }

  protected HookDeclList expand(RuleList rulelist, String stratname, String defaultstrat, Decl sdecl) {
    StringBuffer output = new StringBuffer();
    output.append(%[
  public static Strategy @stratname@(){
    return `@stratname@();
  }

  %strategy @stratname@() extends @defaultstrat@() {
    visit @sortname@ {
      ]%);

        %match(rulelist) {
          RuleList(_*,(Rule|ConditionalRule)[lhs=lhs,rhs=rhs],_*) -> {
            //TODO: verify that the lhs of the rules are of the good sort  
            //TODO: verify the linearity of lhs and rhs
            output.append(%[
                @genTerm(`lhs)@ -> {

                /* 1. save the current pos w */
                Position omega = getEnvironment().getPosition();
                Position posRhs = new Position(new int[]{2});
                Position posFinal = new Position(new int[]{1});
                Position newomega = (Position) posFinal.add(omega);

                /* 2. go to the root and get the global term-graph */
                getEnvironment().followPath(omega.inverse());

                /* 3. construct tt=SubstTerm(subject,r) */
                @sortname@ expandedLhs = (@sortname@) `@genTermWithExplicitVar(`lhs,"root",0)@.expand();
                @sortname@ expandedRhs = (@sortname@) `@genTermWithExplicitVar(`rhs,"root",0)@.expand();
                @sortname@ r = computeRhsWithPath(expandedLhs,expandedRhs,omega);
                @sortname@ t = `Subst@sortname@((@sortname@) getEnvironment().getSubject(),r);
                //all pointers to omega must be replaced by pointers to posRhs in the redex
                @sortname@ tt = (@sortname@) newomega.getOmega(globalRedirection(newomega,posRhs)).visit(t);
                
                /* 4. set the global term to norm(swap(tt,1.w,2))|1 */
                @sortname@ ttt = (@sortname@) tt.swap(newomega,posRhs); 
                @sortname@ res = (@sortname@) ttt.normalize();
                getEnvironment().setSubject(posFinal.getSubterm().visit(res));

                /* 5. go to the position w */
                getEnvironment().followPath(omega);

                return (@sortname@) getEnvironment().getSubject();
                }
                ]%);
          }
        }

        output.append(%[
    }
  }
            ]%);

          return `concHookDecl(BlockHookDecl(sdecl,Code(output.toString())));
  }

  private String genTerm(Term term) {
    StringBuffer output = new StringBuffer();
    term = expand(term);
    %match(term) {
      PathTerm(i,tail*) -> {
        output.append("Path"+sortname);
        output.append("(");
        output.append(`i);
        %match(tail) {
          PathTerm(_*,j,_*) -> {
            output.append(",");
            output.append(`j);
          }
        }
        output.append(")");
      }
      Appl(symbol,args) -> {
        output.append(`symbol);
        output.append("(");
        output.append(genTermList(`args));
        output.append(")");
      }
      Var(name) -> {
        output.append(`name);
      }
      BuiltinInt(i) -> {
        output.append(`i);
      }
      BuiltinString(s) -> {
        output.append(`s);
      }
    }
    return output.toString();
  }

  private String genTermList(TermList list) {
    StringBuffer output = new StringBuffer();
    %match(list) {
      TermList() -> { return ""; }
      TermList(h,t*) -> {
        output.append(genTerm(`h));
        if (!`t.isEmptyTermList()) {
          output.append(", ");
        }
        output.append(genTermList(`t*));
      }
    }
    return output.toString();
  }

  private String genTermWithExplicitVar(Term term, String fathersymbol, int omega) {
    StringBuffer output = new StringBuffer();
    %match(term) {
      LabTerm(label,term) -> {
        output.append("Lab"+sortname);
        output.append("(");
        output.append("\""+`label+"\"");
        output.append(",");
        output.append(genTermWithExplicitVar(`term,fathersymbol,omega));
        output.append(")");
      }
      RefTerm(label) -> {
        output.append("Ref"+sortname);
        output.append("(");
        output.append("\""+`label+"\"");
        output.append(")");
      }
      Appl(symbol,args) -> {
        output.append(`symbol);
        output.append("(");
        output.append(genTermListWithExplicitVar(`symbol,`args));
        output.append(")");
      }
      Var(name) -> {
        //the variable must be replaced by a meta representation of the var
        //in the signature of the corresponding  sort
        //test if the variable is not at the root position 
        if(omega!=0) {
          String sortvar = getSort(fathersymbol,omega);
          output.append("Var"+sortvar+"(\""+`name+"\")");
        } else {
          //it is necessary of the sort declared for the strategy
          output.append("Var"+sortname+"(\""+`name+"\")");
        }
      }
      BuiltinInt(i) -> {
        output.append(`i);
      }
      BuiltinString(s) -> {
        output.append(`s);
      }
    }
    return output.toString();
  }

  private String genTermListWithExplicitVar(String fathersymbol, TermList list) {
    StringBuffer output = new StringBuffer();
    int omega=1;
    %match(list) {
      TermList() -> { return ""; }
      TermList(_*,h,t*) -> {
        output.append(genTermWithExplicitVar(`h,fathersymbol,omega));
        omega++;
        if (!`t.isEmptyTermList()) {
          output.append(", ");
        }
      }
    }
    return output.toString();
  }


  public static Term expand(Term t) {
    HashMap map = new HashMap();
    Term tt = null;
    try {
      tt = (Term) `InnermostIdSeq(NormalizeLabel(map)).visit(t);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    return label2path(tt);
  }

  %typeterm Info {
    implement {Info}
    is_sort(t) { t instanceof Info } 
  }

  static class Info {
    public Position omegaRef;
    public Term sharedTerm;
  }

  %strategy CollectSubterm(label:String,info:Info) extends Fail(){
    visit Term {
      term@LabTerm[l=label,t=subterm] -> {
        Position current = getEnvironment().getPosition();
        if (label.equals(`label)) {
          //test if it is not a cycle
          if (!info.omegaRef.hasPrefix(current)) {
            //return a ref
            info.sharedTerm = `subterm;
            return `RefTerm(label);
          }
          else {
            //do not return a ref and stop to collect
            return `term;  
          }
        }
      }
    }
  }

  %strategy NormalizeLabel(map:HashMap) extends Identity(){
    visit Term {
      RefTerm[l=label] -> {
        if (! map.containsKey(`label)){
          Position old = getEnvironment().getPosition();
          Position rootpos = new Position(new int[]{});
          Info info = new Info();
          info.omegaRef = old;
          getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));           
          `OnceTopDown(CollectSubterm(label,info)).visit(getEnvironment());            
          getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
          //test if is is not a ref to a cycle
          if (info.sharedTerm!=null) {
            map.put(`label,old);
            return `LabTerm(label,info.sharedTerm);
          }
        }
      }
      LabTerm[l=label] -> {
        map.put(`label,getEnvironment().getPosition());
      }
    }
  }

  public static Term label2path(Term t) {
    HashMap map = new HashMap();
    try {
      return (Term) `Sequence(Repeat(OnceTopDown(CollectLabels(map))),TopDown(Label2Path(map))).visit(t);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }  
  }

  %strategy CollectLabels(map:HashMap) extends Fail(){
    visit Term {
      LabTerm[l=label,t=term]-> {
        map.put(`label,getEnvironment().getPosition());
        return `term;
      }
    }
  }

  %strategy PathForPattern() extends Identity(){
    visit Term {
      PathTerm(X*,PathTerm(-1,sublist@!PathTerm(_*,!-2,_*),-1),Y*) -> {
        int k = 1+`sublist.length();
        PathTerm newX = (PathTerm) `PathForPattern().visit(`X);
        PathTerm newY = (PathTerm) `PathForPattern().visit(`Y);
        return `PathTerm(newX*,-k,newY*);
      }
      PathTerm(X*,PathTerm(1,sublist@!PathTerm(_*,!2,_*),1),Y*) -> {
        int k = 1+`sublist.length();
        PathTerm newX = (PathTerm) `PathForPattern().visit(`X);
        PathTerm newY = (PathTerm) `PathForPattern().visit(`Y);
        return `PathTerm(newX*,k,newY*);
      }
    }
  }

  %strategy Label2Path(map:HashMap) extends Identity(){
    visit Term {
      RefTerm[l=label] -> {
        if (map.containsKey(`label)) {
          Path target = (Path) map.get(`label);
          Path ref = (target.sub(getEnvironment().getPosition()));
          //transform the path to obtain the corresponding one in the pattern
          Term path = `PathTerm();
          int head;
          while(ref.length()!=0) {
            head = ref.getHead();
            ref  = ref.getTail();
            path = `PathTerm(path*,head);
          }
          Term newpath = normalize((Term)`PathForPattern().visitLight(path));
          return newpath;
        }
      }
    }
  }

  public static Term normalize(Term path){
    %match(path) {
      PathTerm(X*,x,y,Y*) -> {
        if (`x==-`y) {
          return normalize(`PathTerm(X*,Y*));
        }
      }
    }
    return path;
  }

  private String getSort(String symbolOperator,int omega) {
    %match(moduleList) {
      concModule(_*,Module[Sorts=concSort(_*,Sort[Operators=concOperator(_*,  OperatorDecl[Name=name,Prod=prod],_*)],_*)],_*) -> {
        if(`name.equals(symbolOperator)) {
          int count=1;
          %match(prod) {
            Variadic(sortVar) -> {
              return `sortVar.getName();
            }
            Slots(concSlot(_*,Slot[Sort=SortDecl[Name=type]],_*)) -> {
              if (count==omega) {
                return `type;
              } else {
                count++;
              }
            }
          }
        }
      }
    }
    throw new RuntimeException("cannot determine the sort of the "+omega+"th child of the constructor "+symbolOperator);
  }

  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
