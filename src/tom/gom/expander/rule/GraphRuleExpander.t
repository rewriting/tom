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
import tom.gom.adt.rule.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.library.sl.*;

public class GraphRuleExpander {

  %include { ../../adt/gom/Gom.tom}
  %include { ../../adt/rule/Rule.tom }
  %include { ../../../library/mapping/java/sl.tom}

  private ModuleList moduleList;

  public GraphRuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandGraphRules(String ruleCode, Decl mdecl) {
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    parser.setTreeAdaptor(new ASTAdaptor());
    RuleList rulelist = `RuleList();
    try {
      ASTTree ast = (ASTTree)parser.ruleset().getTree();
      rulelist = (RuleList) ast.getTerm();
    } catch (org.antlr.runtime.RecognitionException e) {
      getLogger().log(Level.SEVERE, "Cannot parse rules",
          new Object[]{});
      return `concHookDecl();
    }
    return expand(rulelist,mdecl);
  }

  private String getArobase(){
    return "@";
  }

  protected HookDeclList expand(RuleList rulelist, Decl mdecl) {
    StringBuffer output = new StringBuffer();
    output.append(
        %[
        %include {sl.tom }

        %typeterm Position{
        implement {Position}
        is_sort(t)     { t instanceof Position }
        }

        private static Term Swap(Position p1, Position p2, Term subject) {
        try {
          Term updatedSubject =  (Term) `TopDown(Sequence(UpdatePos(p1,p2),UpdatePos2(p1,p2))).visit(subject);
          Term subterm_p1 = (Term) p1.getSubterm().visit(updatedSubject);
          Term subterm_p2 = (Term) p2.getSubterm().visit(updatedSubject);
          return (Term) `Sequence(p2.getReplace(subterm_p1),p1.getReplace(subterm_p2)).visit(updatedSubject);
          } catch (VisitFailure e) { return null; }
        }


        %strategy Normalize() extends Identity(){
        visit Term {
          p@getArobase()@pathTerm(_*) -> {
            Position current = getEnvironment().getPosition(); 
            Position dest = (Position) current.add((Path)`p).getCanonicalPath();
            if(current.compare(dest)== -1) {
                getEnvironment().followPath((Path)`p);
                Position realDest = getEnvironment().getPosition(); 
            if(!realDest.equals(dest)) {
                //the subterm pointed was a pos (in case of previous switch) 
                //and we must only update the relative position
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return pathTerm.make(realDest.sub(current));
            }  else {
                //switch the rel position and the pointed subterm

                // 1. construct the new relative position
                Term relref = pathTerm.make(current.sub(dest));

                // 2. update the part to change 
                `TopDown(UpdatePos(dest,current)).visit(getEnvironment());

                // 3. save the subterm updated 
                Term subterm = (Term) getEnvironment().getSubject(); 

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
          visit Term {
            p@getArobase()@pathTerm(_*) -> {
              Position current = getEnvironment().getPosition(); 
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              if(current.hasPrefix(source) && !dest.hasPrefix(target) && !dest.hasPrefix(source)){
                //update this relative pos from the redex to the external
                current = current.changePrefix(source,target);
                return pathTerm.make(dest.sub(current));
              }

              if (dest.hasPrefix(source)  && !current.hasPrefix(target) && !current.hasPrefix(source)){
                //update this relative pos from the external to the redex
                dest = dest.changePrefix(source,target); 
                return pathTerm.make(dest.sub(current));
              }
            }
          }
   }

   %strategy UpdatePos2(p1:Position,p2:Position) extends Identity() {
          visit Term {
            p@getArobase()@pathTerm(_*) -> {
              Position src = getEnvironment().getPosition(); 
              Position dest = (Position) src.add((Path)`p).getCanonicalPath();
              if(src.hasPrefix(p1) && dest.hasPrefix(p2)){
                //update this relative pos from the subterm at p1 to the subterm at p2
                Position newsrc = src.changePrefix(p1,p2);
                Position newdest = dest.changePrefix(p2,p1);
                return pathTerm.make(newdest.sub(newsrc));
              }

              if(src.hasPrefix(p2) && dest.hasPrefix(p1)){
                //update this relative pos from the subterm at p2 to the subterm at p1
                Position newsrc = src.changePrefix(p2,p1);
                Position newdest = dest.changePrefix(p1,p2);
                return pathTerm.make(newdest.sub(newsrc));
              }
            }
          }
   }


  private static Term computeRhsWithPath(Term lhs, Term rhs, Position posRedex) {
    try {
      return (Term) `TopDown(FromVarToPath(lhs,posRedex)).visit(`rhs);
    } catch(VisitFailure e) { return null; }
  }

  %strategy FromVarToPath(lhs:Term,posRedex:Position) extends Identity() {
    visit Term {
      refTerm(name) -> { 
        Position wl = getVarPos(lhs,`name);
        Position wr = getEnvironment().getPosition();
        Position wwl = (Position) (new Position(new int[]{1})).add(posRedex).add(wl); 
        Position wwr = (Position) (new Position(new int[]{2})).add(wr); 
        Position res = (Position) wwl.sub(wwr);
        return pathTerm.make(res);
      }
    }
  }

  private static Position getVarPos(Term term, String varname) {
    Position p = new Position();
    try {
      `OnceTopDown(GetVarPos(p,varname)).visit(term);
      return p;
    } catch (VisitFailure e) { return null; }
  }

  %strategy GetVarPos(Position p, String varname) extends Fail() {
    visit Term {
      v@getArobase()@refTerm(name) -> { 
        if (`name.equals(varname)) { 
          p.setValue(getEnvironment().getPosition().toArray()); 
          return `v; } 
      } 
    }
  }

  public static Strategy GraphRule(){
    return `GraphRule();
  }

  %strategy GraphRule() extends Identity() {
    visit Term {
      ]%);

        /*TODO: order the rules by sort */

        %match(rulelist) {
          RuleList(_*,(Rule|ConditionalRule)[lhs=lhs,rhs=rhs],_*) -> {
            output.append(%[
                @genTerm(`lhs)@ -> {
                /* 1. save the current pos w */
                Position omega = getEnvironment().getPosition();
                Position posRhs = new Position(new int[]{2});
                Position posFinal = new Position(new int[]{1});

                /*  2. go to the root and get the global term-graph */
                getEnvironment().followPath(omega.inverse());

                /*3. construct tt=substTerm(t,r) */
                Term r = computeRhsWithPath(`@genTermWithRef(`lhs)@,`@genTermWithRef(`rhs)@,omega);
                Term tt = `substTerm((Term)getEnvironment().getSubject(),r);
                
                /* 4. set the global term to norm(swap(tt,1.w,2))|1 i */
                Term ttt = Swap((Position) posFinal.add(omega),posRhs,tt); 
                Term res = (Term) `InnermostIdSeq(Normalize()).visit(ttt); 
                getEnvironment().setSubject(posFinal.getSubterm().visit(res));

                /* 5. go to the position w */
                getEnvironment().followPath(omega);

                return (Term) getEnvironment().getSubject();
                }
                ]%);
          }
        }

        output.append(%[
            }
            }
            ]%);

        String imports = %[
          import tom.library.sl.*;
          import java.util.*;
        ]%;

        return `concHookDecl(BlockHookDecl(mdecl,Code(output.toString())),ImportHookDecl(mdecl,Code(imports)));
  }

  private String genTerm(Term term) {
    StringBuffer output = new StringBuffer();
    %match(term) {
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

  private String genTermWithRef(Term term) {
    StringBuffer output = new StringBuffer();
    %match(term) {
      Appl(symbol,args) -> {
        output.append(`symbol);
        output.append("(");
        output.append(genTermListWithRef(`args));
        output.append(")");
      }
      Var(name) -> {
        output.append("refTerm(\""+`name+"\")");
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

  private String genTermListWithRef(TermList list) {
    StringBuffer output = new StringBuffer();
    %match(list) {
      TermList() -> { return ""; }
      TermList(h,t*) -> {
        output.append(genTermWithRef(`h));
        if (!`t.isEmptyTermList()) {
          output.append(", ");
        }
        output.append(genTermListWithRef(`t*));
      }
    }
    return output.toString();
  }



  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
