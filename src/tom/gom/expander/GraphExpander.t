/*
 *
 * GOM
 *
 * Copyright (c) 2006-2008, INRIA
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
import tom.gom.adt.objects.types.ClassName;
import tom.gom.tools.error.GomRuntimeException;

public class GraphExpander {

  %include {../../library/mapping/java/util/HashMap.tom}
  %include {../../library/mapping/java/util/ArrayList.tom}
  %include {../../library/mapping/java/sl.tom}
  %include {../../library/mapping/java/boolean.tom}
  %include { ../adt/gom/Gom.tom}

  %typeterm GomStreamManager { implement { GomStreamManager } }
  private static GomStreamManager streamManager;
  private static SortDecl stringSortDecl;
  private static SortDecl intSortDecl;
  // indicates if the expand method must include normalization phase
  // specific to termgraphs
  private boolean forTermgraph;

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public GraphExpander(GomStreamManager streamManager,boolean forTermgraph) {
    this.forTermgraph = forTermgraph;
    this.streamManager = streamManager;
    stringSortDecl = environment().builtinSort("String");
    intSortDecl = environment().builtinSort("int");
    //we mark them as used builtins:
    //String is used for labelling
    environment().markUsedBuiltin("String");
    //int is used for defining paths
    environment().markUsedBuiltin("int");
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
        "GraphExpander:fullClassName got a strange ClassName "+clsName);
  }

  public Pair expand(ModuleList list, HookDeclList hooks) {
    ModuleList expandedList = `ConcModule();
    ArrayList hookList = new ArrayList();
    try {
      expandedList = (ModuleList) `TopDown(ExpandSort(hookList)).visit(list);
    } catch(tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    //add a global expand method in every ModuleDecl contained in the SortList
    try {
      `TopDown(
          ExpandModule(streamManager,forTermgraph,hookList)).visit(expandedList);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    Iterator it = hookList.iterator();
    while(it.hasNext()) {
      HookDeclList hList = (HookDeclList) it.next();
      hooks = `ConcHookDecl(hList*,hooks*);
    }
    return `ModHookPair(expandedList,hooks);
  }

  %strategy ExpandModule(
      streamManager:GomStreamManager,
      forTermgraph:boolean,
      hookList:ArrayList) extends Identity() {
    visit Module {
      Module[
        MDecl=mdecl@ModuleDecl[ModuleName=moduleName],Sorts=sorts] -> {
        hookList.add(expHooksModule(`moduleName,`sorts,`mdecl,streamManager.getPackagePath(`moduleName.getName()),forTermgraph));
      }
    }
  }

  %strategy ExpandSort(hookList:ArrayList) extends Identity() {
    visit Sort {
      sort@Sort[Decl=sortdecl@SortDecl[Name=sortname],OperatorDecls=ops] -> {
         
        //We add 4 new operators Lab<Sort>,Ref<Sort>,Path<Sort>,Var<Sort>
        //the last one is only used to implement the termgraph rewriting step
        OperatorDecl labOp = `OperatorDecl("Lab"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,stringSortDecl),Slot("term"+sortname,sortdecl))));
        OperatorDecl refOp = `OperatorDecl("Ref"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,stringSortDecl))));
        OperatorDecl pathOp = `OperatorDecl("Path"+sortname,sortdecl,Variadic(intSortDecl));
        OperatorDecl varOp = `OperatorDecl("Var"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,stringSortDecl))));
        hookList.add(pathHooks(pathOp,`sortdecl));
        return `sort.setOperatorDecls(`ConcOperator(ops*,labOp,refOp,pathOp,varOp));

      }
    }
  }

  private static HookDeclList pathHooks(OperatorDecl opDecl, SortDecl sort){

    String moduleName = sort.getModuleDecl().getModuleName().getName();
    String sortName = sort.getName();

    String prefixPkg = streamManager.getPackagePath(moduleName);
    String codeImport =%[
      import @((prefixPkg=="")?"":prefixPkg+".")+moduleName.toLowerCase()@.types.*;
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
      `ConcHookDecl(
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
    ClassName abstractType = `ClassName(packagePath+"."+moduleName.toLowerCase(),moduleName+"AbstractType");

    String prefix = (("".equals(packagePath))?"":packagePath+".")+moduleName.toLowerCase();
    String codeImport =%[
    import @prefix@.types.*;
    import @prefix@.*;
    import tom.library.sl.*;
    import java.util.ArrayList;
    import java.util.HashMap;
    ]%;

    String codeStrategies = getStrategies(sorts);

    %match(sorts){
      ConcSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortName]],_*) -> {
        codeImport += %[
          import @packagePath@.@moduleName.toLowerCase()@.types.@`sortName.toLowerCase()@.Path@`sortName@;
        ]%;
      }
    }

    String codeBlockCommon =%[
    %include{java/util/HashMap.tom}
    %include{java/util/ArrayList.tom}
    %include{sl.tom}

    static int freshlabel =0; //to unexpand termgraphs
    
    %typeterm tom_Info {
      implement { Info }
      is_sort(t) { ($t instanceof Info) }
    }


    public static class Info {
      public String label;
      public Path path;
      public @fullClassName(abstractType)@ term;
    }

    public @fullClassName(abstractType)@ unexpand() {
       HashMap map = getMapFromPositionToLabel();
       try {
         return (@fullClassName(abstractType)@)`Sequence(TopDown(CollectRef(map)),BottomUp(AddLabel(map))).visit(this);
       } catch (tom.library.sl.VisitFailure e) {
         throw new RuntimeException("Unexpected strategy failure!");
       }
    }

    protected HashMap getMapFromPositionToLabel(){
      HashMap map = new HashMap();
      try {
      `TopDown(CollectPositionsOfLabels(map)).visit(this);
      return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    ]%;


    String codeBlockTermWithPointers =%[

      public @fullClassName(abstractType)@ expand(){
        HashMap map = new HashMap();
        Strategy label2path = `Sequence(RepeatId(OnceTopDownId(CollectAndRemoveLabels(map))),TopDown(Label2Path(map)));
        try {
          return (@fullClassName(abstractType)@) `label2path.visit(this);
        } catch (tom.library.sl.VisitFailure e) {
          throw new RuntimeException("Unexpected strategy failure!");
        }}
        ]%;

    String codeBlockTermGraph =%[

   public @fullClassName(abstractType)@ expand(){
       Info info = new Info();
       ArrayList marked = new ArrayList();
       HashMap map = new HashMap();
       try {
         return ((@fullClassName(abstractType)@)`InnermostIdSeq(NormalizeLabel(map)).visit(this.unexpand())).label2path();
       } catch (tom.library.sl.VisitFailure e) {
         throw new RuntimeException("Unexpected strategy failure!");
       }
     }

    protected @fullClassName(abstractType)@ label2path(){
      HashMap map = new HashMap();
      Strategy label2path = `Sequence(RepeatId(OnceTopDownId(CollectAndRemoveLabels(map))),TopDown(Label2Path(map)));
      try {
        return (@fullClassName(abstractType)@) label2path.visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }
    
    public HashMap<String,Position> getMapFromLabelToPositionAndRemoveLabels(){
      HashMap<String,Position> map = new HashMap<String,Position>();
      try {
      `TopDown(CollectAndRemoveLabels(map)).visit(this);
      return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public HashMap<String,Position> getMapFromLabelToPosition(){
      HashMap<String,Position> map = new HashMap<String,Position>();
      try {
      `TopDown(CollectLabels(map)).visit(this);
      return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @fullClassName(abstractType)@ normalize() {
      try {
         return (@fullClassName(abstractType)@)`InnermostIdSeq(Normalize()).visit(this); 
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @fullClassName(abstractType)@ applyGlobalRedirection(Position p1,Position p2) {
      try {
         return (@fullClassName(abstractType)@) globalRedirection(p1,p2).visit(this); 
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }
  
    public static Strategy globalRedirection(Position p1,Position p2) {
        return `TopDown(GlobalRedirection(p1,p2)); 
    }

    public @fullClassName(abstractType)@ swap(Position p1, Position p2) {
      try {
        @fullClassName(abstractType)@ updatedSubject =  (@fullClassName(abstractType)@ ) `TopDown(Sequence(UpdatePos(p1,p2))).visit(this);
        @fullClassName(abstractType)@ subterm_p1 = (@fullClassName(abstractType)@) p1.getSubterm().visit(updatedSubject);
        @fullClassName(abstractType)@ subterm_p2 = (@fullClassName(abstractType)@) p2.getSubterm().visit(updatedSubject);
        return (@fullClassName(abstractType)@) `Sequence(p2.getReplace(subterm_p1),p1.getReplace(subterm_p2)).visit(updatedSubject);
      } catch (VisitFailure e) { 
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

   %strategy Normalize() extends Identity() {
]%;

  %match(sorts){
      ConcSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
 codeBlockTermGraph += %[
        visit @`sortname@ {
          p@@Path@`sortname@(_*) -> {
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

   %strategy UpdatePos(source:Position,target:Position) extends Identity() {
  ]%;


   %match(sorts){
      ConcSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
        codeBlockTermGraph += %[
      visit @`sortname@ {
            p@@Path@`sortname@(_*) -> {
              Position current = getEnvironment().getPosition(); 
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              //relative pos from the source to the external
              if(current.hasPrefix(source) && !dest.hasPrefix(target) && !dest.hasPrefix(source)){
                current = current.changePrefix(source,target);
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the external to the source
              if (dest.hasPrefix(source) && !current.hasPrefix(target) && !current.hasPrefix(source)){
                dest = dest.changePrefix(source,target); 
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the target to the external
              if(current.hasPrefix(target) && !dest.hasPrefix(source) && !dest.hasPrefix(target)){
                current = current.changePrefix(target,source);
                return Path@`sortname@.make(dest.sub(current));
              }

              //relative pos from the external to the target
              if (dest.hasPrefix(target) && !current.hasPrefix(source) && !current.hasPrefix(target)){
                dest = dest.changePrefix(target,source); 
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
      ConcSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortname]],_*) -> {
        codeBlockTermGraph += %[
      visit @`sortname@ {
            p@@Path@`sortname@(_*) -> {
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

    return `ConcHookDecl(
        ImportHookDecl(CutModule(mDecl),Code(codeImport)),
        BlockHookDecl(CutModule(mDecl),Code(codeBlock)));
  }

  private static String getStrategies(SortList sorts) {
    StringBuilder strategiesCode = new StringBuilder();
    // for the CollectLabels strategy
    StringBuilder CollectLabelsCode = new StringBuilder();
    CollectLabelsCode.append(%[
    /**
     * Collect labels and their corresponding positions in a
     * map. The keys are the labels.
     * @@param map the map to collect tuples <label,position>
     */

    %strategy CollectLabels(map:HashMap) extends Identity() {
      ]%);

    // for the CollectAndRemoveLabels strategy
    StringBuilder CollectAndRemoveLabelsCode = new StringBuilder();
    CollectAndRemoveLabelsCode.append(%[
    /**
     * Collect labels and their corresponding positions in a
     * map. The keys are the labels. At the same time, replace the labelled
     * term by the term itself.
     * @@param map the map to collect tuples <label,position>
     */

    %strategy CollectAndRemoveLabels(map:HashMap) extends Identity() {
      ]%);


    // for the CollectPositionsOfLabels strategy
    StringBuilder CollectPositionsOfLabelsCode = new StringBuilder();
    CollectPositionsOfLabelsCode.append(%[
     /**
     * Collect labels of sort and their corresponding positions in a
     * map and at the same time replace the labelled term by the term itself.
     * The keys are the positions.
     * @@param map the map to collect tuples <position,label>
     */

    %strategy CollectPositionsOfLabels(map:HashMap) extends Identity() {
    ]%);

    // for the Label2Path strategy
    StringBuilder Label2PathCode = new StringBuilder();
    Label2PathCode.append(%[
    %strategy Label2Path(map:HashMap) extends Identity() {
    ]%);

    // for the CollectRef strategy
    StringBuilder CollectRefCode = new StringBuilder();
    CollectRefCode.append(%[
    %strategy CollectRef(map:HashMap) extends Identity() {
    ]%);


    // for the AddLabel strategy
    StringBuilder AddLabelCode = new StringBuilder();
    AddLabelCode.append(%[
    %strategy AddLabel(map:HashMap) extends Identity() {
    ]%);

    // for the NormalizeLabel strategy
    StringBuilder NormalizeLabelCode = new StringBuilder();
    NormalizeLabelCode.append(%[
    %strategy NormalizeLabel(map:HashMap) extends Identity() {
    ]%);



    %match(sorts){
      ConcSort(_*,Sort[Decl=sDecl@SortDecl[Name=sortName]],_*) -> {
  
        strategiesCode.append(%[

    %typeterm tom_Info@`sortName@ {
        implement { Info@`sortName@ }
        is_sort(t) { ($t instanceof Info@`sortName@) }
    }

    static class Info@`sortName@{
      public Position omegaRef;
      public @CodeGen.generateCode(`FullSortClass(sDecl))@ sharedTerm;
    }

    ]%);

        // generate the code for the CollectLabels strategy
        CollectLabelsCode.append(%[
      visit @`sortName@{
        Lab@`sortName@[label@`sortName@=label,term@`sortName@=term]-> {
          map.put(`label,getEnvironment().getPosition());
          return (@`sortName@) getEnvironment().getSubject();
        }
      }
        ]%);
        
        // generate the code for the CollectAndRemoveLabels strategy
        CollectAndRemoveLabelsCode.append(%[
      visit @`sortName@{
        Lab@`sortName@[label@`sortName@=label,term@`sortName@=term]-> {
          map.put(`label,getEnvironment().getPosition());
          return `term;
        }
      }
      ]%);

        // generate the code for the CollectPositionsOfLabels strategy
        CollectPositionsOfLabelsCode.append(%[
      visit @`sortName@{
        Lab@`sortName@[label@`sortName@=label,term@`sortName@=term]-> {
          map.put(getEnvironment().getPosition().toString(),`label);
          return (@`sortName@) getEnvironment().getSubject();
        }
      }
      ]%);

       // generate the code for the Label2Path strategy
        Label2PathCode.append(%[
      visit @`sortName@ {
        Ref@`sortName@[label@`sortName@=label] -> {
          if (map.containsKey(`label)) {
            Position target = (Position) map.get(`label);
            @CodeGen.generateCode(`FullSortClass(sDecl))@ ref = (@CodeGen.generateCode(`FullSortClass(sDecl))@) (Path@`sortName@.make(target.sub(getEnvironment().getPosition())).getCanonicalPath());
            return ref;
          }
        }
      }
      ]%);

        // generate the code for the CollectRef strategy
        CollectRefCode.append(%[
      visit @`sortName@ {
        p@@Path@`sortName@(_*) -> {
          //use String instead of Position because containskey method does
          //not use the method equals to compare values
          String target =
            getEnvironment().getPosition().add((Path)`p).getCanonicalPath().toString();
          if (map.containsKey(target)){
            String label = (String) map.get(target);
            return `Ref@`sortName@(label);
          }
          else{
            freshlabel++;
            String label = "tom_label"+freshlabel;
            map.put(target,label);
            return `Ref@`sortName@(label);
          }
        }
      }
   ]%);
   
        // generate the code for the AddLabel strategy
        AddLabelCode.append(%[
    visit @`sortName@ {
      t@@!Lab@`sortName@[] -> {
        if (map.containsKey(getEnvironment().getPosition().toString())) {
          String label = (String) map.get(getEnvironment().getPosition().toString());
          return `Lab@`sortName@(label,t);
        }
      }
    }
   ]%);
        // generate the code for the NormalizeLabel strategy
        NormalizeLabelCode.append(%[
      visit @`sortName@ {
        Ref@`sortName@[label@`sortName@=label] -> {
          if (! map.containsKey(`label)){
            Position old = getEnvironment().getPosition();
            Position rootpos = new Position(new int[]{});
            Info@`sortName@ info = new Info@`sortName@();
            info.omegaRef = old;
            getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));           
            `OnceTopDown(CollectSubterm@`sortName@(label,info)).visit(getEnvironment());            
            getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
            //test if it is not a ref to a cycle
            if (info.sharedTerm!=null) {
              map.put(`label,old);
              return `Lab@`sortName@(label,info.sharedTerm);
            }
          }
        }
        Lab@`sortName@[label@`sortName@=label] -> {
          map.put(`label,getEnvironment().getPosition());
        }
      }
    ]%);

    // generate each strategy CollectSubterm<sortname>
    strategiesCode.append(%[
    %strategy CollectSubterm@`sortName@(label:String,info:tom_Info@`sortName@) extends Fail() {
      visit @`sortName@ {
        term@@Lab@`sortName@[label@`sortName@=label,term@`sortName@=subterm] -> {
          Position current = getEnvironment().getPosition();
          if (label.equals(`label)) {
            //test if it is not a cycle
            if (!info.omegaRef.hasPrefix(current)) {
              //return a ref
              info.sharedTerm = `subterm;
              return `Ref@`sortName@(label);
            }
            else {
              //do not return a ref and stop to collect
              return `term;  
            }
          }
        }
      }
    }
    ]%);
      }
    }


    CollectLabelsCode.append(%[
  }
  ]%);
    CollectAndRemoveLabelsCode.append(%[
  }
  ]%);
    CollectPositionsOfLabelsCode.append(%[
  }
  ]%);
    Label2PathCode.append(%[
  }
  ]%);
 
    CollectRefCode.append(%[
  }
  ]%);
    AddLabelCode.append(%[
  }
  ]%);
    NormalizeLabelCode.append(%[
  }
  ]%);
    strategiesCode.append(CollectLabelsCode);
    strategiesCode.append(CollectAndRemoveLabelsCode);
    strategiesCode.append(CollectPositionsOfLabelsCode);
    strategiesCode.append(Label2PathCode);
    strategiesCode.append(CollectRefCode);
    strategiesCode.append(AddLabelCode);
    strategiesCode.append(NormalizeLabelCode);
    return strategiesCode.toString();
  }

}
