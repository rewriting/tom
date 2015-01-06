/*
 *
 * GOM
 *
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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
import tom.gom.SymbolTable;
import tom.gom.exception.*;
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
  %include { ../adt/gom/Gom.tom}
  %include { ../adt/symboltable/SymbolTable.tom}

  %typeterm GraphExpander { implement { GraphExpander } }

  private SortDecl stringSortDecl;
  private SortDecl intSortDecl;
  // indicates if the expand method must include normalization phase
  // specific to termgraphs
  private boolean forTermgraph;
  private GomEnvironment gomEnvironment;
  private SymbolTable st;

  public GraphExpander(boolean forTermgraph,GomEnvironment gomEnvironment) {
    this.forTermgraph = forTermgraph;
    this.gomEnvironment = gomEnvironment;
    st = gomEnvironment.getSymbolTable();
    stringSortDecl = gomEnvironment.builtinSort("String");
    intSortDecl = gomEnvironment.builtinSort("int");
    gomEnvironment.markUsedBuiltin("String");
    gomEnvironment.markUsedBuiltin("int");
  }

  public GomStreamManager getStreamManager() {
    return this.gomEnvironment.getStreamManager();
  }

  public SortDecl getStringSortDecl() {
    return stringSortDecl;
  }

  public SortDecl getIntSortDecl() {
    return intSortDecl;
  }

  public boolean getForTermgraph() {
    return forTermgraph;
  }

  public void setForTermgraph(boolean forTermgraph) {
    this.forTermgraph = forTermgraph;
  }

  public GomEnvironment getGomEnvironment() {
    return gomEnvironment;
  }

  public SymbolTable getSymbolTable() {
    return st;
  }

  public Pair expand(ModuleList list, HookDeclList hooks) {
    ModuleList expandedList = `ConcModule();
    ArrayList hookList = new ArrayList();
    try {
      expandedList = `TopDown(ExpandSort(hookList,this)).visit(list);
    } catch(tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    //add a global expand method in every ModuleDecl contained in the SortList
    try {
      `TopDown(
          ExpandModule(getForTermgraph(),hookList,this)).visit(expandedList);
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
      forTermgraph:boolean,
      hookList:ArrayList,
      ge:GraphExpander) extends Identity() {
    visit Module {
      Module[
        MDecl=mdecl@ModuleDecl[ModuleName=moduleName],Sorts=sorts] -> {
        hookList.add(ge.expHooksModule(`moduleName.getName(),`sorts,`mdecl,ge.getForTermgraph()));
      }
    }
  }

  %strategy ExpandSort(hookList:ArrayList,ge:GraphExpander) extends Identity() {
    visit Sort {
      sort@Sort[Decl=sortdecl@SortDecl[Name=sortname],OperatorDecls=ops] -> {

        //We add 4 new operators Lab<Sort>,Ref<Sort>,Path<Sort>,Var<Sort>
        //the last one is only used to implement the termgraph rewriting step
        // for now, we need also to fill the symbol table
        OperatorDecl labOp = `OperatorDecl("Lab"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,ge.getStringSortDecl()),Slot("term"+sortname,sortdecl))),Details("/** labOp */"));
        ge.getSymbolTable().addConstructor("Lab"+`sortname,`sortname,`concFieldDescription(FieldDescription("label"+sortname,"String",SNone()),FieldDescription("term"+sortname,sortname,SNone())));
        OperatorDecl refOp = `OperatorDecl("Ref"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,ge.getStringSortDecl()))),Details("/** refOp */"));
        ge.getSymbolTable().addConstructor("Ref"+`sortname,`sortname,`concFieldDescription(FieldDescription("label"+sortname,"String",SNone())));
        OperatorDecl pathOp = `OperatorDecl("Path"+sortname,sortdecl,Variadic(ge.getIntSortDecl()),Details("/** pathOp */"));
        ge.getSymbolTable().addVariadicConstructor("Path"+`sortname,"int",`sortname);
        OperatorDecl varOp = `OperatorDecl("Var"+sortname,sortdecl,Slots(ConcSlot(Slot("label"+sortname,ge.getStringSortDecl()))),Details("/** varOp */"));
        ge.getSymbolTable().addConstructor("Var"+`sortname,`sortname,`concFieldDescription(FieldDescription("label"+sortname,"String",SNone())));
        hookList.add(ge.pathHooks(pathOp,`sortdecl));
        return `sort.setOperatorDecls(`ConcOperator(ops*,labOp,refOp,pathOp,varOp));

      }
    }
  }

  private HookDeclList pathHooks(OperatorDecl opDecl, SortDecl sort){
    String moduleName = sort.getModuleDecl().getModuleName().getName();
    String sortName = sort.getName();
    String prefixPkg = getStreamManager().getPackagePath(moduleName);
    String codeImport =%[
    import @((prefixPkg=="")?"":prefixPkg+".")+moduleName.toLowerCase()@.types.*;
    import tom.library.sl.*;
    ]%;

    String codeBlock =%[

    public Path add(Path p) {
        Position pp = Position.makeFromPath(this);
        return make(pp.add(p));
    }

    public Path inverse() {
      Position pp = Position.makeFromPath(this);
      return make(pp.inverse());
    }

    public Path sub(Path p) {
      Position pp = Position.makeFromPath(this);
      return make(pp.sub(p));
    }

    public int getHead() {
      return getHeadPath@sortName@();
    }

    public Path getTail() {
      return (Path) getTailPath@sortName@();
    }

    public Path getCanonicalPath() {
      %match(this) {
        Path@sortName@(X*,x,y,Y*) -> {
          if (`x==-`y) {
            return ((Path)`Path@sortName@(X*,Y*)).getCanonicalPath();
          }
        }
      }
      return this;
    }

    public Path conc(int i) {
      Path@sortName@ current = this;
      return (Path) `Path@sortName@(i,current*);
    }

    public int[] toIntArray() {
      int[] array = new int[length()];
      Path p = this;
      for(int i=0; i<length(); i++) {
        array[i] = p.getHead();
        p = p.getTail();
      }
      return array;
    }

    public static Path@sortName@ make(Path path) {
      @CodeGen.generateCode(`FullSortClass(sort))@ ref = `Path@sortName@();
      Path pp = path.getCanonicalPath();
      int size = pp.length();
      for(int i=0;i<size;i++){
        ref = `Path@sortName@(ref*,pp.getHead());
        pp = pp.getTail();
      }
      return (Path@sortName@) ref;
    }

    public int compare(Path p) {
      Position p1 = Position.makeFromPath(this);
      Position p2 = Position.makeFromPath(p);
      return p1.compare(p2);
    }
    ]%;

    return
      `ConcHookDecl(
          ImportHookDecl(CutOperator(opDecl),Code(codeImport)),
          InterfaceHookDecl(CutOperator(opDecl),
            Code("tom.library.sl.Path")),
          BlockHookDecl(CutOperator(opDecl),Code(codeBlock),true()));
  }

  private HookDeclList expHooksModule(String gomModuleName,
      SortList sorts,
      ModuleDecl mDecl,
      boolean forTermgraph) {
    String fullAbstractTypeClassName = st.getFullAbstractTypeClassName(gomModuleName);
    String fullModuleName = st.getFullModuleName(gomModuleName);

    String codeImport =%[
    import @fullModuleName@.types.*;
    import @fullModuleName@.*;
    import tom.library.sl.*;
    ]%;

    String codeStrategies = getStrategies(sorts);

    %match(sorts){
      ConcSort(_*,Sort[Decl=SortDecl[Name=sortName]],_*) -> {
        codeImport += %[
          import @st.getFullConstructorClassName("Path"+`sortName)@;
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
      public @fullAbstractTypeClassName@ term;
    }

    public @fullAbstractTypeClassName@ unexpand() {
       java.util.HashMap<String,Position> map = getMapFromPositionToLabel();
       try {
         return `Sequence(TopDown(CollectRef(map)),BottomUp(AddLabel(map))).visit(this);
       } catch (tom.library.sl.VisitFailure e) {
         throw new RuntimeException("Unexpected strategy failure!");
       }
    }

    protected java.util.HashMap<String,Position> getMapFromPositionToLabel() {
      java.util.HashMap<String,Position> map = new java.util.HashMap<String,Position>();
      try {
        `TopDown(CollectPositionsOfLabels(map)).visit(this);
        return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    ]%;


    String codeBlockTermWithPointers =%[

      public @fullAbstractTypeClassName@ expand() {
        java.util.HashMap<Position,String> map = new java.util.HashMap<Position,String>();
        Strategy label2path = `Sequence(RepeatId(OnceTopDownId(CollectAndRemoveLabels(map))),TopDown(Label2Path(map)));
        try {
          return `label2path.visit(this);
        } catch (tom.library.sl.VisitFailure e) {
          throw new RuntimeException("Unexpected strategy failure!");
        }}
        ]%;

    String codeBlockTermGraph =%[

   public @fullAbstractTypeClassName@ expand() {
       java.util.HashMap<Position,String> map = new java.util.HashMap<Position,String>();
       try {
         return `InnermostIdSeq(NormalizeLabel(map)).visit(this.unexpand()).label2path();
       } catch (tom.library.sl.VisitFailure e) {
         throw new RuntimeException("Unexpected strategy failure!");
       }
     }

    public @fullAbstractTypeClassName@ normalizeWithLabels() {
      java.util.HashMap<Position,String> map = new java.util.HashMap<Position,String>();
      try {
        return `InnermostIdSeq(NormalizeLabel(map)).visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @fullAbstractTypeClassName@ label2path() {
      java.util.HashMap<Position,String> map = new java.util.HashMap<Position,String>();
      Strategy label2path = `Sequence(RepeatId(OnceTopDownId(CollectAndRemoveLabels(map))),TopDown(Label2Path(map)));
      try {
        return label2path.visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public java.util.HashMap<String,Position> getMapFromLabelToPositionAndRemoveLabels() {
      java.util.HashMap<String,Position> map = new java.util.HashMap<String,Position>();
      try {
        `TopDown(CollectAndRemoveLabels(map)).visit(this);
        return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public java.util.HashMap<String,Position> getMapFromLabelToPosition() {
      java.util.HashMap<String,Position> map = new java.util.HashMap<String,Position>();
      try {
        `TopDown(CollectLabels(map)).visit(this);
        return map;
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @fullAbstractTypeClassName@ normalize() {
      try {
        return `InnermostIdSeq(Normalize()).visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public @fullAbstractTypeClassName@ applyGlobalRedirection(Position p1,Position p2) {
      try {
         return globalRedirection(p1,p2).visit(this);
      } catch (tom.library.sl.VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

    public static Strategy globalRedirection(Position p1,Position p2) {
        return `TopDown(GlobalRedirection(p1,p2));
    }

    public @fullAbstractTypeClassName@ swap(Position p1, Position p2) {
      try {
        @fullAbstractTypeClassName@ updatedSubject =  `TopDown(Sequence(UpdatePos(p1,p2))).visit(this);
        @fullAbstractTypeClassName@ subterm_p1 = p1.getSubterm().visit(updatedSubject);
        @fullAbstractTypeClassName@ subterm_p2 = p2.getSubterm().visit(updatedSubject);
        return `Sequence(p2.getReplace(subterm_p1),p1.getReplace(subterm_p2)).visit(updatedSubject);
      } catch (VisitFailure e) {
        throw new RuntimeException("Unexpected strategy failure!");
      }
    }

   %strategy Normalize() extends Identity() {
]%;

  %match(sorts){
      ConcSort(_*,Sort[Decl=SortDecl[Name=sortname]],_*) -> {
 codeBlockTermGraph += %[
        visit @`sortname@ {
          p@@Path@`sortname@(_*) -> {
            Position current = getEnvironment().getPosition();
            Position dest = (Position) current.add((Path)`p).getCanonicalPath();
            if(current.compare(dest)== -1) {
                getEnvironment().followPath((Path)`p);
                Position realDest = getEnvironment().getPosition();
            if(!realDest.equals(dest)) {
                /* the subterm pointed was a pos (in case of previous switch) */
                /* and we must only update the relative position */
                getEnvironment().followPath(current.sub(getEnvironment().getPosition()));
                return Path@`sortname@.make(realDest.sub(current));
            }  else {
                /* switch the rel position and the pointed subterm */

                /* 1. construct the new relative position */
                @`sortname@ relref = Path@`sortname@.make(current.sub(dest));

                /* 2. update the part to change */
                `TopDown(UpdatePos(dest,current)).visit(getEnvironment());

                /* 3. save the subterm updated */
                @`sortname@ subterm = (@`sortname@) getEnvironment().getSubject();

                /* 4. replace at dest the subterm by the new relative pos */
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
      ConcSort(_*,Sort[Decl=SortDecl[Name=sortname]],_*) -> {
        codeBlockTermGraph += %[
      visit @`sortname@ {
            p@@Path@`sortname@(_*) -> {
              Position current = getEnvironment().getPosition();
              Position dest = (Position) current.add((Path)`p).getCanonicalPath();
              /* relative pos from the source to the external */
              if(current.hasPrefix(source) && !dest.hasPrefix(target) && !dest.hasPrefix(source)){
                current = current.changePrefix(source,target);
                return Path@`sortname@.make(dest.sub(current));
              }

              /* relative pos from the external to the source */
              if (dest.hasPrefix(source) && !current.hasPrefix(target) && !current.hasPrefix(source)){
                dest = dest.changePrefix(source,target);
                return Path@`sortname@.make(dest.sub(current));
              }

              /* relative pos from the target to the external */
              if(current.hasPrefix(target) && !dest.hasPrefix(source) && !dest.hasPrefix(target)){
                current = current.changePrefix(target,source);
                return Path@`sortname@.make(dest.sub(current));
              }

              /* relative pos from the external to the target */
              if (dest.hasPrefix(target) && !current.hasPrefix(source) && !current.hasPrefix(target)){
                dest = dest.changePrefix(target,source);
                return Path@`sortname@.make(dest.sub(current));
              }

              /* relative pos from the source to the target */
              if(current.hasPrefix(source) && dest.hasPrefix(target)){
                current = current.changePrefix(source,target);
                dest = dest.changePrefix(target,source);
                return Path@`sortname@.make(dest.sub(current));
              }

              /* relative pos from the target to the source */
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
      ConcSort(_*,Sort[Decl=SortDecl[Name=sortname]],_*) -> {
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

    String codeBlock = codeBlockCommon + codeStrategies + (getForTermgraph()?codeBlockTermGraph:codeBlockTermWithPointers);

    return `ConcHookDecl(
        ImportHookDecl(CutModule(mDecl),Code(codeImport)),
        BlockHookDecl(CutModule(mDecl),Code(codeBlock),true()));
  }

  private String getStrategies(SortList sorts) {
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

    %match(sorts) {
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
        Lab@`sortName@[label@`sortName@=label]-> {
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
        Lab@`sortName@[label@`sortName@=label]-> {
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
            /*@CodeGen.generateCode(`FullSortClass(sDecl))@ ref = */ return (@CodeGen.generateCode(`FullSortClass(sDecl))@) (Path@`sortName@.make(target.sub(getEnvironment().getPosition())).getCanonicalPath());
          }
        }
      }
      ]%);

        // generate the code for the CollectRef strategy
        CollectRefCode.append(%[
      visit @`sortName@ {
        p@@Path@`sortName@(_*) -> {
          /* 
           * use String instead of Position because containskey method does
           * not use the method equals to compare values
           */
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
            Position rootpos = Position.make();
            Info@`sortName@ info = new Info@`sortName@();
            info.omegaRef = old;
            getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));
            `OnceTopDown(CollectSubterm@`sortName@(label,info)).visit(getEnvironment());
            getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
            /* test if it is not a ref to a cycle */
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
            /* test if it is not a cycle */
            if (!info.omegaRef.hasPrefix(current)) {
              /* return a ref */
              info.sharedTerm = `subterm;
              return `Ref@`sortName@(label);
            }
            else {
              /* do not return a ref and stop to collect */
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
