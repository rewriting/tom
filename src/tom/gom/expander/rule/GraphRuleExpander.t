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

    %typeterm Position{
        implement {Position}
        is_sort(t)     { t instanceof Position }
    }

    private static @sortname@ Swap(Position p1, Position p2, @sortname@ subject) {
        try {
          @sortname@ updatedSubject =  (@sortname@) `TopDown(Sequence(UpdatePos(p1,p2),UpdatePos2(p1,p2))).visit(subject);
          @sortname@ subterm_p1 = (@sortname@) p1.getSubterm().visit(updatedSubject);
          @sortname@ subterm_p2 = (@sortname@) p2.getSubterm().visit(updatedSubject);
          return (@sortname@) `Sequence(p2.getReplace(subterm_p1),p1.getReplace(subterm_p2)).visit(updatedSubject);
          } catch (VisitFailure e) { return null; }
    }


   %strategy Normalize() extends Identity(){
        visit @sortname@ {
          p@getArobase()@Path@sortname@(_*) -> {
            Position current = getEnvironment().getPosition(); 
            Position dest = (Position) current.add((Path)`p).getCanonicalPath();
            if(current.compare(dest)== -1) {
                getEnvironment().followPath((Path)`p);
                Position realDest = getEnvironment().getPosition(); 
            if(!realDest.equals(dest)) {
                //the subterm pointed was a pos (in case of previous switch) 
                //and we must only update the relative position
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return Path@sortname@.make(realDest.sub(current));
            }  else {
                //switch the rel position and the pointed subterm

                // 1. construct the new relative position
                @sortname@ relref = Path@sortname@.make(current.sub(dest));

                // 2. update the part to change 
                `TopDown(UpdatePos(dest,current)).visit(getEnvironment());

                // 3. save the subterm updated 
                @sortname@ subterm = (@sortname@) getEnvironment().getSubject(); 

                // 4. replace at dest the subterm by the new relative pos
                getEnvironment().setSubject(relref);
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return subterm; 
            }
          }
        }
      }
    }

   %strategy UpdatePos(source:Position,target:Position) extends Identity() {
          visit @sortname@ {
            p@getArobase()@Path@sortname@(_*) -> {
              Position current = getEnvironment().getPosition(); 
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              if(current.hasPrefix(source) && !dest.hasPrefix(target) && !dest.hasPrefix(source)){
                //update this relative pos from the redex to the external
                current = current.changePrefix(source,target);
                return Path@sortname@.make(dest.sub(current));
              }

              if (dest.hasPrefix(source)  && !current.hasPrefix(target) && !current.hasPrefix(source)){
                //update this relative pos from the external to the redex
                dest = dest.changePrefix(source,target); 
                return Path@sortname@.make(dest.sub(current));
              }
            }
          }
   }

   %strategy UpdatePos2(p1:Position,p2:Position) extends Identity() {
          visit @sortname@ {
            p@getArobase()@Path@sortname@(_*) -> {
              Position src = getEnvironment().getPosition(); 
              Position dest = (Position) src.add((Path)`p).getCanonicalPath();
              if(src.hasPrefix(p1) && dest.hasPrefix(p2)){
                //update this relative pos from the subterm at p1 to the subterm at p2
                Position newsrc = src.changePrefix(p1,p2);
                Position newdest = dest.changePrefix(p2,p1);
                return Path@sortname@.make(newdest.sub(newsrc));
              }

              if(src.hasPrefix(p2) && dest.hasPrefix(p1)){
                //update this relative pos from the subterm at p2 to the subterm at p1
                Position newsrc = src.changePrefix(p2,p1);
                Position newdest = dest.changePrefix(p1,p2);
                return Path@sortname@.make(newdest.sub(newsrc));
              }
            }
          }
   }


  private static @sortname@ computeRhsWithPath(@sortname@ lhs, @sortname@ rhs, Position posRedex) {
    try {
      return (@sortname@) `TopDown(FromVarToPath(lhs,posRedex)).visit(`rhs);
    } catch(VisitFailure e) { return null; }
  }

  %strategy FromVarToPath(lhs:@sortname@,posRedex:Position) extends Identity() {
    visit @sortname@ {
      Var@sortname@(name) -> { 
        Position wl = getVarPos(lhs,`name);
        Position wr = getEnvironment().getPosition();
        Position wwl = (Position) (new Position(new int[]{1})).add(posRedex).add(wl); 
        Position wwr = (Position) (new Position(new int[]{2})).add(wr); 
        Position res = (Position) wwl.sub(wwr);
        return Path@sortname@.make(res);
      }
    }
  }

  private static Position getVarPos(@sortname@ term, String varname) {
    Position p = new Position();
    try {
      `OnceTopDown(GetVarPos(p,varname)).visit(term);
      return p;
    } catch (VisitFailure e) { return null; }
  }

  %strategy GetVarPos(Position p, String varname) extends Fail() {
    visit @sortname@ {
      v@getArobase()@Var@sortname@(name) -> { 
        if (`name.equals(varname)) { 
          p.setValue(getEnvironment().getPosition().toArray()); 
          return `v; } 
      } 
    }
  }

   ]%);

  String imports = %[
import tom.library.sl.*;
import java.util.*;
   ]%;

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

                /*  2. go to the root and get the global term-graph */
                getEnvironment().followPath(omega.inverse());

                /*3. construct tt=SubstTerm(t,r) */
                @sortname@ expandedLhs = (@sortname@) @modulename@AbstractType.expand(`@genTermWithExplicitVar(`lhs)@);
                @sortname@ expandedRhs = (@sortname@) @modulename@AbstractType.expand(`@genTermWithExplicitVar(`rhs)@);
                @sortname@ r = computeRhsWithPath(expandedLhs,expandedRhs,omega);
                @sortname@ tt = `Subst@sortname@((@sortname@)getEnvironment().getSubject(),r);
                
                /* 4. set the global term to norm(swap(tt,1.w,2))|1 i */
                @sortname@ ttt = Swap((Position) posFinal.add(omega),posRhs,tt); 
                @sortname@ res = (@sortname@) `InnermostIdSeq(Normalize()).visit(ttt); 
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

  private String genTermWithExplicitVar(Term term) {
    StringBuffer output = new StringBuffer();
    %match(term) {
      LabTerm(label,term) -> {
        output.append("Lab"+sortname);
        output.append("(");
        output.append("\""+`label+"\"");
        output.append(",");
        output.append(genTermWithExplicitVar(`term));
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
        output.append(genTermListWithExplicitVar(`args));
        output.append(")");
      }
      Var(name) -> {
        output.append("Var"+sortname+"(\""+`name+"\")");
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

  private String genTermListWithExplicitVar(TermList list) {
    StringBuffer output = new StringBuffer();
    %match(list) {
      TermList() -> { return ""; }
      TermList(h,t*) -> {
        output.append(genTermWithExplicitVar(`h));
        if (!`t.isEmptyTermList()) {
          output.append(", ");
        }
        output.append(genTermListWithExplicitVar(`t*));
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


  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
