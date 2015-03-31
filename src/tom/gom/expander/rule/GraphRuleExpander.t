/*
 *
 * GOM
 *
 * Copyright (c) 2007-2015, Universite de Lorraine, Inria
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

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.gom.SymbolTable;
import tom.gom.GomMessage;

import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.RuleAdaptor;
import tom.gom.adt.rule.types.*;
import tom.gom.adt.rule.types.term.*;
import tom.gom.adt.objects.types.ClassName;

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
  private GomEnvironment gomEnvironment;

  public GraphRuleExpander(ModuleList data, GomEnvironment gomEnvironment) {
    moduleList = data;
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  private static String fullClassName(ClassName clsName) {
    %match(clsName) {
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
    RuleList rulelist = `RuleList();
    try {
      Tree ast = (Tree)parser.graphruleset().getTree();
      rulelist = (RuleList) RuleAdaptor.getTerm(ast);
    } catch (org.antlr.runtime.RecognitionException e) {
      GomMessage.error(getLogger(), null, 0, GomMessage.rulesParsingFailure);
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
%include { sl.tom }
%include { java/util/ArrayList.tom}
%typeterm tom_StringList {
  implement      { java.util.List<String> }
  is_sort(t)     { $t instanceof java.util.List }
  equals(l1,l2)  { $l1.equals($l2) }
}
%typeterm tom_StringPositionMap {
  implement      { java.util.Map<String,Position> }
  is_sort(t)      { $t instanceof java.util.Map }
  equals(l1,l2)  { $l1.equals($l2) }
}

%typeterm tom_SharedLabel {
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

  protected static java.util.List<SharedLabel> getSharedLabels(@fullClassName(abstractType)@ labelledLhs, @fullClassName(abstractType)@ labelledRhs) {
    java.util.ArrayList<SharedLabel> sharedlabels = new java.util.ArrayList<SharedLabel>();
    java.util.HashMap<String,Position> lhsLabels = labelledLhs.getMapFromLabelToPositionAndRemoveLabels();
    java.util.HashMap<String,Position> rhsLabels = labelledRhs.getMapFromLabelToPositionAndRemoveLabels();
    for (String labelRhs: rhsLabels.keySet()) {
      if (lhsLabels.containsKey(labelRhs)) {
        sharedlabels.add(new SharedLabel(labelRhs,lhsLabels.get(labelRhs),rhsLabels.get(labelRhs)));
      }
    }
   return sharedlabels;
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
        Position wwl = (Position) (Position.makeFromArray(new int[]{1})).add(omega).add(wl);
        Position wwr = (Position) (Position.makeFromArray(new int[]{2})).add(wr);
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
    ArrayList<Position> list = new ArrayList<Position>();
    try {
      `OnceTopDown(GetVarPos(list,varname)).visit(term);
      return list.get(0);
    } catch (VisitFailure e) {
      throw new RuntimeException("Unexpected strategy failure!");
      }
  }

  %strategy GetVarPos(ArrayList l, String varname) extends Fail() {
]%);

 %match(moduleList) {
      ConcModule(_*,Module[Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {
 output.append(
        %[
    visit @`name@ {
      v@@Var@`name@(name) -> {
        if(`name.equals(varname)) {
          l.add(Position.makeFromPath(getEnvironment().getPosition()));
          return `v;
        }
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
import java.util.ArrayList;
   ]%;

  //import all the constructors Path<Sort> of the module
  %match(moduleList) {
ConcModule(_*,Module[MDecl=ModuleDecl(GomModuleName(_),pkg),Sorts=ConcSort(_*,Sort[Decl=SortDecl[Name=name]],_*)],_*) -> {

  String prefix = ((`pkg=="")?"":`pkg+".")+moduleName.toLowerCase();
  imports += %[
import @prefix@.types.@`name.toLowerCase()@.Path@`name@;
  ]%;
    }
  }

   return `ConcHookDecl(BlockHookDecl(sdecl,Code(output.toString()),true()),ImportHookDecl(sdecl,Code(imports)));
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

                /* 1. set needed positions */
                Position omega = getEnvironment().getPosition();
                Position posFinal = Position.makeFromArray(new int[]{1});
                Position posRhs = Position.makeFromArray(new int[]{2});
                Position newomega = (Position) posFinal.add(omega);

                /* 2. go to the root and get the global term-graph */
                getEnvironment().followPath(omega.inverse());
                @fullClassName(abstractType)@ subject = (@fullClassName(abstractType)@) getEnvironment().getSubject();

                /* 2. construct at compile-time the lhs and rhs */
                @fullClassName(abstractType)@ labelledLhs = `@genTermWithExplicitVar(`lhs,"root",0)@.normalizeWithLabels();
                @fullClassName(abstractType)@ labelledRhs = `@genTermWithExplicitVar(`rhs,"root",0)@.normalizeWithLabels();

               /* 3. construct t = SubstTerm(subject',r') */
                @fullClassName(abstractType)@ rhs = labelledRhs.label2path();
                @fullClassName(abstractType)@ lhs = labelledLhs.label2path();
                rhs = `TopDown(FromVarToPath(lhs,omega)).visit(rhs);
                @fullClassName(abstractType)@ t = `Subst(subject,rhs);
                //replace in subject every pointer to the position newomega by
                //a pointer to the position 2  and if in position 2 there is also a
                //pointer inline the paths.
                t = posFinal.getOmega(`TopDown(Sequence(globalRedirection(newomega,posRhs),InlinePath()))).visit(t);
                //inline paths in the intermediate r
                t = posRhs.getOmega(`TopDown(InlinePath())).visit(t);

                //compute the list of all shared labels
                java.util.List<SharedLabel> sharedlabels = getSharedLabels(labelledLhs,labelledRhs);

                //redirect paths for shared labels
                for (SharedLabel sharedlabel: sharedlabels) {
                  Position l = (Position) newomega.add(sharedlabel.posLhs);
                  Position r = (Position) posRhs.add(sharedlabel.posRhs);
                  t =  t.applyGlobalRedirection(l,r);
                }

                /* 4. set the global term to swap(t,1.w,2) */
                t = t.swap(newomega,posRhs);

                /* 5. set the global term to swap(t,1.w,2) */
                t = t.normalize();

                /* 6. get the first child */
                t = posFinal.getSubterm().visit(t);

                //expand the subject to remove labels from the rhs
                getEnvironment().setSubject(t.expand());

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

          return `ConcHookDecl(BlockHookDecl(sdecl,Code(output.toString()),true()));
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
          String sortvar = getGomEnvironment().getSymbolTable().getChildSort(fathersymbol,omega);
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
      tt = `InnermostIdSeq(NormalizeLabel(map)).visit(t);
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
          Position rootpos = Position.make();
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
      return `Sequence(RepeatId(OnceTopDownId(CollectLabels(map))),TopDown(Label2Path(map))).visit(t);
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
      PathTerm(x,y,tail*) -> {
        if (`x<0 && `y==-2) {
          //detect a rise in a TermList to elimnate
          Term newtail = `PathForPattern().visit(`tail);
          return `PathTerm(x,newtail*);
        } else if (`x==2 && `y>0) {
          //detect a descent in a TermList to elimnate
          Term newtail = `PathForPattern().visit(`tail);
          return `PathTerm(y,newtail*);
        } else {
          //default case
          Term newtail = `PathForPattern().visit(`tail);
          return `PathTerm(x,y,newtail*);
        }
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
          return `Sequence(PathForPattern(),RepeatId(Normalize())).visitLight(path);
        }
      }
    }
  }


  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
