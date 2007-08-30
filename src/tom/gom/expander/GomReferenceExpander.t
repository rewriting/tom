/*
 *
 * GOM
 *
 * Copyright (C) 2006-2007, INRIA
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
 * Emilie Balland  e-mail: Emilie.Balland@loria.fr
 *
 **/

package tom.gom.expander;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.library.sl.*;
import tom.gom.backend.CodeGen;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class GomReferenceExpander {

  %include{../../library/mapping/java/util/HashMap.tom}
  %include{../../library/mapping/java/util/ArrayList.tom}
  %include{../../library/mapping/java/sl.tom}
  %include{../../library/mapping/java/boolean.tom}
  %include { ../adt/gom/Gom.tom}

  private static String packagePath;
  private static SortDecl stringSortDecl,intSortDecl;
  // indicates if the expand method must include normalization phase
  // specific to termgraphs
  private boolean forTermgraph;

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public GomReferenceExpander(String packagePath,boolean forTermgraph) {
    this.forTermgraph = forTermgraph;
    this.packagePath = packagePath;
    if (!"".equals(this.packagePath)) {
      this.packagePath = this.packagePath + ".";
    }
    stringSortDecl = environment().builtinSort("String");
    intSortDecl = environment().builtinSort("int");
    //we mark them as used builtins:
    //String is used for labelling
    environment().markUsedBuiltin("String");
    //int is used for defining paths
    environment().markUsedBuiltin("int");
  }

  public Pair expand(ModuleList list, HookDeclList hooks) {
    ModuleList expandedList = `concModule();
    ArrayList hookList = new ArrayList();
    try {
      expandedList = (ModuleList) `TopDown(ExpandSort(hookList)).visit(list);
    } catch(tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    //add a global expand method in every ModuleDecl contained in the SortList
    try {
      `TopDown(
          ExpandModule(packagePath,forTermgraph,hookList)).visit(expandedList);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    Iterator it = hookList.iterator();
    while(it.hasNext()) {
      HookDeclList hList = (HookDeclList) it.next();
      hooks = `concHookDecl(hList*,hooks*);
    }
    return `ModHookPair(expandedList,hooks);
  }

  %strategy ExpandModule(
      packagePath:String,
      forTermgraph:boolean,
      hookList:ArrayList) extends Identity(){
    visit Module {
      Module[
        MDecl=mdecl@ModuleDecl[ModuleName=modName],Sorts=sorts] -> {
        hookList.add(expHooksModule(`modName,`sorts,`mdecl,packagePath,forTermgraph));
      }
    }
  }

  private static String getArobase(){
    return "@";
  }

  %strategy ExpandSort(hookList:ArrayList) extends Identity() {
    visit Sort {
      sort@Sort[Decl=sortdecl@SortDecl[Name=sortname],Operators=ops] -> {
         
        //We add 5 new operators Lab<Sort>,Ref<Sort>,Path<Sort>,Subst<Sort>,Var<Sort>
        //the two last ones are only used to implement the termgraph rewriting step
        OperatorDecl labOp = `OperatorDecl("Lab"+sortname,sortdecl,Slots(concSlot(Slot("label"+sortname,stringSortDecl),Slot("term"+sortname,sortdecl))));
        OperatorDecl refOp = `OperatorDecl("Ref"+sortname,sortdecl,Slots(concSlot(Slot("label"+sortname,stringSortDecl))));
        OperatorDecl pathOp = `OperatorDecl("Path"+sortname,sortdecl,Variadic(intSortDecl));
        OperatorDecl substOp = `OperatorDecl("Subst"+sortname,sortdecl,Slots(concSlot(Slot("global"+sortname,sortdecl),Slot("subst"+sortname,sortdecl))));
        OperatorDecl varOp = `OperatorDecl("Var"+sortname,sortdecl,Slots(concSlot(Slot("label"+sortname,stringSortDecl))));
        hookList.add(pathHooks(pathOp,`sortdecl));
        return `sort.setOperators(`concOperator(ops*,labOp,refOp,pathOp,substOp,varOp));

      }
    }
  }

  private static HookDeclList pathHooks(OperatorDecl opDecl, SortDecl sort){

    String moduleName = sort.getModuleDecl().getModuleName().getName();
    String sortName = sort.getName();

    String codeImport =%[
      import @packagePath+moduleName.toLowerCase()@.types.*;
    import tom.library.sl.*;
    ]%;

    String codeBlock =%[

    public Path add(Path p){
        Position pp = Position.make(this);
        return make(pp.add(p));
    }

    public Path inverse(){
      Position pp = Position.make(this);
      return make(pp.inverse());
    }

    public Path sub(Path p){
      Position pp = Position.make(this);
      return make(pp.sub(p));
    }

    public int getHead(){
      return getHeadPath@sortName@();
    }

    public Path getTail(){
      return (Path) getTailPath@sortName@();
    }

    public Path getCanonicalPath(){
      %match(this) {
        Path@sortName@(X*,x,y,Y*) -> {
          if (`x==-`y) {
            return ((Path)`Path@sortName@(X*,Y*)).getCanonicalPath();
          }
        }
      }
      return this;
    }

    public Path conc(int i){
      Path@sortName@ current = this;
      return (Path) `Path@sortName@(i,current*);
    }

    public static Path@sortName@ make(Path path){
      @CodeGen.generateCode(`FullSortClass(sort))@ ref = `Path@sortName@();
      Path pp = path.getCanonicalPath();
      int size = pp.length();
      for(int i=0;i<size;i++){
        ref = `Path@sortName@(ref*,pp.getHead());
        pp = pp.getTail();
      }
      return (Path@sortName@) ref;
    }

    public int compare(Path p){
      Position p1 = Position.make(this);
      Position p2 = Position.make(p);
      return p1.compare(p2);
    }
    ]%;

    return 
      `concHookDecl(
          ImportHookDecl(CutOperator(opDecl),Code(codeImport)),
          InterfaceHookDecl(CutOperator(opDecl),
            Code("tom.library.sl.Path")),
          BlockHookDecl(CutOperator(opDecl),Code(codeBlock)));
  }

  private static HookDeclList expHooksModule(GomModuleName gomModuleName,
      SortList sorts,
      ModuleDecl mDecl,
      String packagePath,
      boolean forTermgraph) {
    String moduleName = gomModuleName.getName();

    String codeImport =%[
    import @packagePath+moduleName.toLowerCase()@.types.*;
    import @packagePath+moduleName.toLowerCase()@.*;
    import tom.library.sl.*;
    import java.util.ArrayList;
    import java.util.HashMap;
    ]%;

    String codeBlockCommon =%[
    %include{java/util/HashMap.tom}
    %include{java/util/ArrayList.tom}
    %include{sl.tom}

    %typeterm Info {
      implement { Info }
      is_sort(t) { t instanceof Info } 
    }


    public static class Info {
      public String label;
      public Path path;
      public @moduleName@AbstractType term;
    }
    ]%;

    String codeStrategies = "";
    String CollectLabels= "Fail()",Collect = "Fail()";
    String Label2Path = "Identity()",NormalizeLabel = "Identity()";

    %match(sorts){
      concSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortName]],_*) -> {
        codeImport += %[
          import @packagePath+moduleName.toLowerCase()@.types.@`sortName.toLowerCase()@.*;
        ]%;
        codeStrategies += getStrategies(`sDecl,moduleName);
        Label2Path = "Sequence(Label2Path"+`sortName+"(map),"+Label2Path+")";
        CollectLabels = "Choice(CollectLabels"+`sortName+"(map),"+CollectLabels+")";
        NormalizeLabel = "Sequence(NormalizeLabel"+`sortName+"(map),"+NormalizeLabel+")";
      }
    }

    String codeBlockTermWithPointers =%[

      public @moduleName@AbstractType expand(){
        HashMap map = new HashMap();
        Strategy label2path = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Path@));
        try {
          return (@moduleName@AbstractType) `label2path.visit(this);
        } catch (tom.library.sl.VisitFailure e) {
          throw new RuntimeException("Unexpected strategy failure!");
        }}
        ]%;

    String codeBlockTermGraph =%[

    public @moduleName@AbstractType expand(){
       Info info = new Info();
       ArrayList marked = new ArrayList();
       HashMap map = new HashMap();
       try {
         return ((@moduleName@AbstractType)`InnermostIdSeq(@NormalizeLabel@).visit(this)).label2path();
       } catch (tom.library.sl.VisitFailure e) {
         throw new RuntimeException("Unexpected strategy failure!");
       }
     }

    protected @moduleName@AbstractType label2path(){
      HashMap map = new HashMap();
      Strategy label2path = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Path@));
      try {
        return (@moduleName@AbstractType) label2path.visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @moduleName@AbstractType normalize() {
      try {
         return (@moduleName@AbstractType)`InnermostIdSeq(Normalize()).visit(this); 
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @moduleName@AbstractType applyGlobalRedirection(Position p1,Position p2) {
      try {
         return (@moduleName@AbstractType) globalRedirection(p1,p2).visit(this); 
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }
  
    public static Strategy globalRedirection(Position p1,Position p2) {
        return `TopDown(GlobalRedirection(p1,p2)); 
    }

    public @moduleName@AbstractType swap(Position p1, Position p2) {
      try {
        @moduleName@AbstractType updatedSubject =  (@moduleName@AbstractType ) `TopDown(UpdatePos(p1,p2)).visit(this);
        @moduleName@AbstractType subterm_p1 = (@moduleName@AbstractType) p1.getSubterm().visit(updatedSubject);
        @moduleName@AbstractType subterm_p2 = (@moduleName@AbstractType) p2.getSubterm().visit(updatedSubject);
        return (@moduleName@AbstractType) `Sequence(p2.getReplace(subterm_p1),p1.getReplace(subterm_p2)).visit(updatedSubject);
      } catch (VisitFailure e) { 
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

   %strategy Normalize() extends Identity(){
]%;

  %match(sorts){
      concSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
 codeBlockTermGraph += %[
        visit @`sortname@ {
          p@getArobase()@Path@`sortname@(_*) -> {
            Position current = getEnvironment().getPosition(); 
            Position dest = (Position) current.add((Path)`p).getCanonicalPath();
            if(current.compare(dest)== -1) {
                getEnvironment().followPath((Path)`p);
                Position realDest = getEnvironment().getPosition(); 
            if(!realDest.equals(dest)) {
                //the subterm pointed was a pos (in case of previous switch) 
                //and we must only update the relative position
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return Path@`sortname@.make(realDest.sub(current));
            }  else {
                //switch the rel position and the pointed subterm

                // 1. construct the new relative position
                @`sortname@ relref = Path@`sortname@.make(current.sub(dest));

                // 2. update the part to change 
                `TopDown(UpdatePos(dest,current)).visit(getEnvironment());

                // 3. save the subterm updated 
                @`sortname@ subterm = (@`sortname@) getEnvironment().getSubject(); 

                // 4. replace at dest the subterm by the new relative pos
                getEnvironment().setSubject(relref);
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return subterm; 
            }
          }
        }
      }
]%;
      }
  }

  codeBlockTermGraph += %[
    }

   %typeterm Position{
        implement {Position}
        is_sort(t)     { t instanceof Position }
    }
  
   %strategy UpdatePos(source:Position,target:Position) extends Identity() {
  ]%;


   %match(sorts){
      concSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
        codeBlockTermGraph += %[
      visit @`sortname@ {
            p@getArobase()@Path@`sortname@(_*) -> {
              Position current = getEnvironment().getPosition(); 
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              //relative pos from the source to the external
              if(current.hasPrefix(source) && !dest.hasPrefix(target) && !dest.hasPrefix(source)){
                current = current.changePrefix(source,target);
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the external to the source
              if (dest.hasPrefix(source)  && !current.hasPrefix(target) && !current.hasPrefix(source)){
                dest = dest.changePrefix(source,target); 
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the source to the target
              if(current.hasPrefix(source) && dest.hasPrefix(target)){
                current = current.changePrefix(source,target);
                dest = dest.changePrefix(target,source);
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the target to the source
              if(current.hasPrefix(target) && dest.hasPrefix(source)){
                current = current.changePrefix(target,source);
                dest = dest.changePrefix(source,target);
                return Path@`sortname@.make(dest.sub(current));
              }
   
            }
          }
    ]%;
      }
   }

   codeBlockTermGraph += %[
   }

   %strategy GlobalRedirection(source:Position,target:Position) extends Identity() {
  ]%;

   %match(sorts){
      concSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
        codeBlockTermGraph += %[
      visit @`sortname@ {
            p@getArobase()@Path@`sortname@(_*) -> {
              Position current = getEnvironment().getPosition(); 
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              if(dest.equals(source)) {
                return Path@`sortname@.make(target.sub(current));
              }
            }
          }
    ]%;
      }
   }

   codeBlockTermGraph += %[
   }
   ]%;

    String codeBlock = codeBlockCommon + codeStrategies + (forTermgraph?codeBlockTermGraph:codeBlockTermWithPointers);

    return `concHookDecl(
        ImportHookDecl(CutModule(mDecl),Code(codeImport)),
        BlockHookDecl(CutModule(mDecl),Code(codeBlock)));
  }

  private static String getStrategies(SortDecl sDecl, String moduleName){

    String sortName = sDecl.getName();
    String strategies =%[

    %typeterm Info@sortName@ {
        implement { Info@sortName@ }
        is_sort(t) { t instanceof Info@sortName@ } 
    }

    static class Info@sortName@{
      public Position omegaRef;
      public @CodeGen.generateCode(`FullSortClass(sDecl))@ sharedTerm;
    }
 
      %strategy Collect@sortName@(marked:ArrayList,info:Info) extends Fail(){
        visit @sortName@{
          Lab@sortName@[label@sortName@=label,term@sortName@=term]-> {
            if(! marked.contains(`label)){
              info.label=`label;
              info.term=`Lab@sortName@(label,term);
              info.path=getEnvironment().getPosition();
              marked.add(`label);
              return `Lab@sortName@(label,term);
            }
          }
        }
      }

    %strategy CollectLabels@sortName@(map:HashMap) extends Fail(){
      visit @sortName@{
        Lab@sortName@[label@sortName@=label,term@sortName@=term]-> {
          map.put(`label,getEnvironment().getPosition());
          return `term;
        }
      }
    }

    %strategy Label2Path@sortName@(map:HashMap) extends Identity() {
      visit @sortName@ {
        Ref@sortName@[label@sortName@=label] -> {
          if (map.containsKey(`label)) {
            Position target = (Position) map.get(`label);
            @CodeGen.generateCode(`FullSortClass(sDecl))@ ref = (@CodeGen.generateCode(`FullSortClass(sDecl))@) (Path@sortName@.make(target.sub(getEnvironment().getPosition())).getCanonicalPath());
            return ref;
          }
        }
      }
    }

    %strategy CollectSubterm@sortName@(label:String,info:Info@sortName@) extends Fail(){
      visit @sortName@ {
        term@getArobase()@Lab@sortName@[label@sortName@=label,term@sortName@=subterm] -> {
         Position current = getEnvironment().getPosition();
        if (label.equals(`label)) {
          //test if it is not a cycle
          if (!info.omegaRef.hasPrefix(current)) {
           //return a ref
            info.sharedTerm = `subterm;
            return `Ref@sortName@(label);
          }
          else {
            //do not return a ref and stop to collect
            return `term;  
          }
        }
      }
    }
  }

    %strategy NormalizeLabel@sortName@(map:HashMap) extends Identity(){
      visit @sortName@ {
        Ref@sortName@[label@sortName@=label] -> {
          if (! map.containsKey(`label)){
            Position old = getEnvironment().getPosition();
            Position rootpos = new Position(new int[]{});
            Info@sortName@ info = new Info@sortName@();
            info.omegaRef = old;
            getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));           
            `OnceTopDown(CollectSubterm@sortName@(label,info)).visit(getEnvironment());            
            getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
          //test if it is not a ref to a cycle
          if (info.sharedTerm!=null) {
            map.put(`label,old);
            return `Lab@sortName@(label,info.sharedTerm);
          }
        }
      }
      Lab@sortName@[label@sortName@=label] -> {
          map.put(`label,getEnvironment().getPosition());
        }
      }
    }
    ]%;

    return strategies;
  }

}
