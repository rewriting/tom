/*
 *
 * GOM
 *
 * Copyright (c) 2007-2008, INRIA
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
import tom.gom.adt.objects.types.ClassName;
import tom.gom.tools.error.GomRuntimeException;
import tom.library.sl.*;

public class GraphRuleExpander {

  %include { ../../adt/gom/Gom.tom}
  %include { ../../adt/rule/Rule.tom }
  %include { ../../../library/mapping/java/sl.tom}
  %include { ../../../library/mapping/java/util/HashMap.tom}

  private ModuleList moduleList;
  private String sortname;
  private String moduleName;
  private String pkgName;

  public GraphRuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  private static String fullClassName(ClassName clsName) {
    %match(ClassName clsName) {
      ClassName[Pkg=pkgPrefix,Name=name] -> {
        if(`pkgPrefix.length()==0) {
          return `name;
        } else {
          return `pkgPrefix+"."+`name;
        }
      }
    }
    throw new GomRuntimeException(
        "GomReferenceExpander:fullClassName got a strange ClassName "+clsName);
  }

  public HookDeclList expandGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    this.sortname = sortname; 
    %match(sdecl) {
      CutSort[Sort=SortDecl[ModuleDecl=ModuleDecl(GomModuleName(moduleName),pkgName)]] -> {
        this.moduleName = `moduleName;
        this.pkgName = `pkgName;
      }
    }

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
      return `ConcHookDecl();
    }
    return expand(rulelist,stratname,defaultstrat,sdecl);
  }

  public HookDeclList expandFirstGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    HookDeclList expandedrules = expandGraphRules(sortname,stratname,defaultstrat,ruleCode, sdecl);
    HookDeclList commonpart = expandFirst(sdecl);
    return `ConcHookDecl(commonpart*,expandedrules*);  
  }

  //add the common methods, includes and imports for all graphrule strategies of a sort 
  protected HookDeclList expandFirst(Decl sdecl) {
    ClassName abstractType = `ClassName(pkgName+"."+moduleName.toLowerCase(),moduleName+"AbstractType");
    ClassName visitor = `ClassName(pkgName+"."+moduleName.toLowerCase(),moduleName+"Visitor");

    StringBuilder output = new StringBuilder();
    output.append(
        %[
%include {sl.tom }
%typeterm tom_StringList {
  implement      { java.util.List<String> }
  is_sort(t)      { $t instanceof java.util.List }  
  equals(l1,l2)  { $l1.equals($l2) }
}

%typeterm tom_SharedLabel{
  implement {SharedLabel}
  is_sort(t) { ($t instanceof SharedLabel) }
}

static class SharedLabel {
  public Position posLhs;
  public Position posRhs;
  public String label;

  public SharedLabel(String label, Position posLhs, Position posRhs) {
    this.label = label;
    this.posLhs = posLhs;
    this.posRhs = posRhs;
  }
}

%op tom_@abstractType.getName()@ Subst(global:tom_@abstractType.getName()@,subst:tom_@abstractType.getName()@) {
  is_fsym(t) {( $t instanceof Subst )}
  make(t1,t2) {( new Subst($t1,$t2) )}
}

%typeterm tom_@abstractType.getName()@ {
  implement { @fullClassName(abstractType)@ }
  is_sort(t) {( $t instanceof @fullClassName(abstractType)@ )}
}

static class Subst extends @fullClassName(abstractType)@ {
    
  @fullClassName(abstractType)@ substitution,globalterm;

  public Subst(@fullClassName(abstractType)@ globalterm, @fullClassName(abstractType)@ substitution) {
    this.substitution = substitution;
    this.globalterm = globalterm;
  }

  //abstract methods from the abstractType which are trivially implemented
  //they must never be used
  
  public aterm.ATerm toATerm() {
    return null;
  }

  public String symbolName() { return "@@"; }

  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("@@(");
    buffer.append(globalterm);
    buffer.append(",");
    buffer.append(substitution);
    buffer.append(")");
  }

  public int compareTo(Object o) { return 0; }

  public int compareToLPO(Object o) { return 0; }

  public final int hashCode() {
    return 0;
  }

  public final boolean equivalent(shared.SharedObject obj) {
    return false;
  }

  public shared.SharedObject duplicate() {
    return this;
  }

  //implementation of the Visitable interface
  public Visitable setChildren(Visitable[] children) {
    if (children.length == 2){
      return new Subst((@fullClassName(abstractType)@)children[0],(@fullClassName(abstractType)@)children[1]);
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public Visitable[] getChildren() {
    return new Visitable[]{globalterm,substitution};
  }

  public Visitable getChildAt(int i) {
    switch(i) {
      case 0: return globalterm;
      case 1: return substitution;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable setChildAt(int i, Visitable child) {
    switch(i) {
      case 0: return new Subst((@fullClassName(abstractType)@)child,substitution);
      case 1: return new Subst(globalterm,(@fullClassName(abstractType)@)child);
      default: throw new IndexOutOfBoundsException();
    }
  } 

    public int getChildCount() { 
      return 2; 
    }

}

static class Substitution {
  public Position omega;
  @fullClassName(abstractType)@ value;

  public Substitution(Position omega, @fullClassName(abstractType)@ value) {
    this.omega = omega;
    this.value = value;
  }

  public String toString() {
    return "<"+omega+","+value+">";
  }
}

  protected static java.util.List<Substitution> getSubstitutions(@fullClassName(abstractType)@ labelledLhs, @fullClassName(abstractType)@ labelledRhs, Position omega) {
    java.util.List<String> sharedlabels = new java.util.ArrayList();
    java.util.HashMap<String,Position> lhsLabels = labelledLhs.getMapFromLabelToPositionAndRemoveLabels();
    java.util.HashMap<String,Position> rhsLabels = labelledRhs.getMapFromLabelToPosition();
    for (String labelRhs: rhsLabels.keySet()) {
      if (lhsLabels.containsKey(labelRhs)) {
        sharedlabels.add(labelRhs);
      }
    }

    java.util.List<Substitution> substitutions = new java.util.ArrayList<Substitution>();
    
     // add a substitution for each shared label
    for (String sharedlabel: sharedlabels) {
      Position posLhs = lhsLabels.get(sharedlabel);
      Position posRhs = rhsLabels.get(sharedlabel);
      Substitution subst = computeSubstitution(labelledLhs,labelledRhs,new SharedLabel(sharedlabel,posLhs,posRhs),sharedlabels,omega);
      substitutions.add(subst);
    }

    // add the substitution for the root position at the end of the list
    // the root subtitution must be necessary applied at the end
    Substitution rootsubst = computeSubstitution(labelledLhs,labelledRhs,new SharedLabel("",new Position(new int[]{}),new Position(new int[]{})),sharedlabels,omega);
    substitutions.add(rootsubst);
    
    return substitutions;
  }


  %strategy ReplaceSharedLabelByVar(sharedlabels:tom_StringList,sharedlabel:tom_SharedLabel) extends Identity() {
    visit @sortname@ {
      Lab@sortname@[label@sortname@=label] -> {
        if (`label.equals(sharedlabel.label)) {
          throw new VisitFailure();
        } else {
          if(sharedlabels.contains(`label)) {
            return `Var@sortname@(label);
          }
        }
      }
    }
  }

  private static Substitution computeSubstitution(@fullClassName(abstractType)@ labelledLhs, @fullClassName(abstractType)@ labelledRhs, SharedLabel sharedlabel, java.util.List<String> sharedlabels, Position omega) {
    try {
      @fullClassName(abstractType)@ lhs = (@fullClassName(abstractType)@) `Try(BottomUp(ReplaceSharedLabelByVar(sharedlabels,sharedlabel))).visit(labelledLhs);
      @fullClassName(abstractType)@ rhs = (@fullClassName(abstractType)@) `Try(BottomUp(ReplaceSharedLabelByVar(sharedlabels,sharedlabel))).visit(labelledRhs);
      lhs = (@fullClassName(abstractType)@) lhs.expand();
      rhs = (@fullClassName(abstractType)@) sharedlabel.posRhs.getSubterm().visit(rhs);
      return new Substitution((Position) omega.add(sharedlabel.posLhs),(@fullClassName(abstractType)@) `TopDown(FromVarToPath(lhs,omega)).visit(rhs));
    } catch(VisitFailure e) { 
      throw new RuntimeException("Unexpected strategy failure!");
    }
  }


  %strategy FromVarToPath(lhs:tom_@abstractType.getName()@,omega:Position) extends Identity() {
]%);

  %match(moduleList) {
      ConcModule(_*,Module[Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
    output.append(
        %[
    visit @`name@ {
      Var@`name@(name) -> {
        Position wl = getVarPos(lhs,`name);
        Position wr = getEnvironment().getPosition();
        Position wwl = (Position) (new Position(new int[]{1})).add(omega).add(wl); 
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

  private static Position getVarPos(@fullClassName(abstractType)@ term, String varname) {
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
      ConcModule(_*,Module[Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
 output.append(
        %[
    visit @`name@ {
      v@@Var@`name@(name) -> { 
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

 output.append(
        %[
  %strategy InlinePath() extends Identity() {
]%);

  %match(moduleList) {
      ConcModule(_*,Module[Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
    output.append(
        %[
    visit @`name@ {
    //match non empty paths
      p1@@Path@`name@(_,_*) -> {
        getEnvironment().followPath((Path)`p1);
        @`name@ pointedSubterm = (@`name@) getEnvironment().getSubject();
        %match(pointedSubterm) {
          p2@@Path@`name@(_,_*) -> {
            getEnvironment().followPath(((Path)`p1).inverse());
            return (@`name@) ((Path)`p1).add((Path)`p2).getCanonicalPath();
          }
        }
        getEnvironment().followPath(((Path)`p1).inverse());
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
   ]%;

  //import all the constructors Path<Sort> of the module
  %match(moduleList) {
ConcModule(_*,Module[MDecl=ModuleDecl(GomModuleName(moduleName),pkg),Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {

  String prefix = ((`pkg=="")?"":`pkg+".")+moduleName.toLowerCase();
  imports += %[
import @prefix@.types.@`name.toLowerCase()@.Path@`name@; 
  ]%;
    }
  }

   return `ConcHookDecl(BlockHookDecl(sdecl,Code(output.toString())),ImportHookDecl(sdecl,Code(imports)));
  }

  protected HookDeclList expand(RuleList rulelist, String stratname, String defaultstrat, Decl sdecl) {
    ClassName abstractType = `ClassName(pkgName+"."+moduleName.toLowerCase(),moduleName+"AbstractType");
 
    StringBuilder output = new StringBuilder();
    output.append(%[
  public static Strategy @stratname@() {
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
                Position posFinal = new Position(new int[]{1});
                Position posRhs = new Position(new int[]{2});

                /* 2. go to the root and get the global term-graph */
                getEnvironment().followPath(omega.inverse());

                @fullClassName(abstractType)@ labelledLhs = `@genTermWithExplicitVar(`lhs,"root",0)@;
                @fullClassName(abstractType)@ labelledRhs = `@genTermWithExplicitVar(`rhs,"root",0)@;
                @fullClassName(abstractType)@ subject = (@fullClassName(abstractType)@) getEnvironment().getSubject();             
                
                //compute all the different substitutions
                java.util.List<Substitution> substitutions = getSubstitutions(labelledLhs,labelledRhs,omega);
                
                /* 3. construct tt=SubstTerm(subject',r') */
                for (Substitution subst: substitutions) {
                  @fullClassName(abstractType)@ r = subst.value;
                  //System.out.println("subst "+r);
                  Position posRedex = subst.omega;
                  //System.out.println("at the position "+posRedex);
                  @fullClassName(abstractType)@ t = `Subst(subject,r);
                  Position newomega = (Position) posFinal.add(posRedex);
                  //System.out.println("t "+t);
                  //replace in subject every pointer to the position newomega by
                  //a pointer to the position 2  and if in position 2 there is also a
                  //pointer inline the paths.
                  //(corresponds to dot(t) in the paper)
                  t = (@fullClassName(abstractType)@) posFinal.getOmega(`TopDown(Sequence(globalRedirection(newomega,posRhs),InlinePath()))).visit(t);
                  //System.out.println("t "+t);
                  //inline paths in the intermediate r
                  //(corresponds to dot(r) in the paper)
                  t = (@fullClassName(abstractType)@) posRhs.getOmega(`TopDown(InlinePath())).visit(t);
                  //System.out.println("t "+t);

                  /* 4. set the global term to norm(swap(t,1.w,2))|1 */
                  @fullClassName(abstractType)@ tt = (@fullClassName(abstractType)@) t.swap(newomega,posRhs); 
                  //System.out.println("tt "+tt);
                  @fullClassName(abstractType)@ res = (@fullClassName(abstractType)@) tt.normalize();
                  //System.out.println("res "+res);
                  subject = (@fullClassName(abstractType)@) posFinal.getSubterm().visit(res);
                  //System.out.println("subject "+subject);
                }

                //expand the subject to remove labels from the rhs
                getEnvironment().setSubject(subject.expand());

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

          return `ConcHookDecl(BlockHookDecl(sdecl,Code(output.toString())));
  }

  private String genTerm(Term term) {
    StringBuilder output = new StringBuilder();
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
    StringBuilder output = new StringBuilder();
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

  private String genTermWithExplicitVar(Term termArg, String fathersymbol, int omega) {
    StringBuilder output = new StringBuilder();
    %match(termArg) {
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
    StringBuilder output = new StringBuilder();
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
    java.util.HashMap map = new java.util.HashMap();
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
    is_sort(t) { ($t instanceof Info) }
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
    java.util.HashMap map = new java.util.HashMap();
    try {
      return (Term) `Sequence(RepeatId(OnceTopDownId(CollectLabels(map))),TopDown(Label2Path(map))).visit(t);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }  
  }

  %strategy CollectLabels(map:HashMap) extends Identity(){
    visit Term {
      LabTerm[l=label,t=term]-> {
        map.put(`label,getEnvironment().getPosition());
        return `term;
      }
    }
  }

  %strategy PathForPattern() extends Identity(){
    visit Term {
      //treatment for the first rise
      PathTerm(-1,tail*) -> {
        PathTerm newtail = (PathTerm) `PathForPattern().visit(`tail);
        return newtail;
      }
      
      //detect rises into a TermList comb and remove it
      PathTerm(sublist@!PathTerm(_*,!-2,_*),-1,tail*) && (!PathTerm()<<sublist)-> {
        int upcount = `sublist.length();
        PathTerm newtail = (PathTerm) `PathForPattern().visit(`tail);
        return `PathTerm(-upcount,newtail*);
      }

      // special treatment for the last rise into a TermList to the root
      //PathTerm()<<tail cooresponds to cycle
      p@PathTerm(sublist@!PathTerm(_*,!-2,_*),tail*) && (PathTerm(1,_*)<<tail || PathTerm(2,_*)<<tail || PathTerm()<<tail) && (!PathTerm()<<sublist) -> {
        int downcount = `sublist.length();
        PathTerm newtail = (PathTerm) `PathForPattern().visit(`tail*);
        return `PathTerm(-downcount,newtail*);
      }
      
      //detect descents into a TermList comb and remove it
      PathTerm(sublist@!PathTerm(_*,!2,_*),1,tail*) && (!PathTerm()<<sublist)-> {
        int downcount = `sublist.length();
        PathTerm newtail = (PathTerm) `PathForPattern().visit(`tail);
        return `PathTerm(downcount,newtail*);
      }
    }
  }

  %strategy Normalize() extends Identity(){
    visit Term {
      PathTerm(X*,x,y,Y*) -> {
        if (`x==-`y) {
          return `PathTerm(X*,Y*);
        }
      }
    }
  }

  %strategy Label2Path(map:HashMap) extends Identity(){
    visit Term {
      RefTerm[l=label] -> {
        if (map.containsKey(`label)) {
          Path target = (Path) map.get(`label);
          Path ref = target.sub(getEnvironment().getPosition());
          //warning: do not normalize ref because we need to transform paths that go to the root
          //construct the PathTerm corresponding to the position ref
          Term path = `PathTerm();
          int head;
          while(ref.length()!=0) {
            head = ref.getHead();
            ref  = ref.getTail();
            path = `PathTerm(path*,head);
          }
          //transform the path to obtain the corresponding one in the pattern
          Term newpath = (Term) `Sequence(PathForPattern(),RepeatId(Normalize())).visitLight(path);
          return newpath;
        }
      }
    }
  }

  private String getSort(String symbolOperator,int omega) {
    %match(moduleList) {
      ConcModule(_*,Module[Sorts=ConcSort(_*,Sort[OperatorDecls=ConcOperator(_*,OperatorDecl[Name=name,Prod=prod],_*)],_*)],_*) -> {
        if(`name.equals(symbolOperator)) {
          int count=1;
          %match(prod) {
            Variadic(sortVar) -> {
              return `sortVar.getName();
            }
            Slots(ConcSlot(_*,Slot[Sort=SortDecl[Name=type]],_*)) -> {
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
