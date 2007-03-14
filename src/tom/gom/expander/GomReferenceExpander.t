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
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class GomReferenceExpander {

  %include{java/util/HashMap.tom}
  %include{java/util/ArrayList.tom}
  %include{java/sl.tom}
  %include{java/boolean.tom}
  %include { ../adt/gom/Gom.tom}

  // indicates if the expand method must include normalization phase
  // specific to termgraphs
  private static String packagePath;

  private static SortDecl stringSortDecl,intSortDecl;
  private boolean forTermgraph;

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public GomReferenceExpander(String packagePath,boolean forTermgraph) {
    this.forTermgraph = forTermgraph;
    this.packagePath = packagePath;
    stringSortDecl = environment().builtinSort("String");
    intSortDecl = environment().builtinSort("int");
    //we mark them as used builtins
    environment().markUsedBuiltin("String");
    environment().markUsedBuiltin("int");
  }

  public Pair expand(ModuleList list, HookDeclList hooks) {
    ModuleList expandedList = `concModule();
    ArrayList hookList = new ArrayList();
    expandedList = (ModuleList) `TopDown(ExpandSort(hookList)).fire(list);
    //add a global expand method in every ModuleDecl contained in the SortList
    `TopDown(
        ExpandModule(packagePath,forTermgraph,hookList)).fire(expandedList);
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
      hookList:ArrayList) extends `Identity(){
    visit Module {
      module@Module[
        MDecl=mdecl@ModuleDecl[ModuleName=modName],Sorts=sorts] -> {
        hookList.add(expHooksModule(`modName,`sorts,`mdecl,packagePath,forTermgraph));
      }
    }
  }

  %strategy ExpandSort(hookList:ArrayList) extends `Identity() {
    visit Sort {
      x -> { return expandSort(`x,`hookList); }
    }
  }

  private static Sort expandSort(Sort sort, ArrayList hookList) {
    OperatorDeclList l1 = sort.getOperators();
    OperatorDeclList l2 = getRefOperators(sort.getDecl(),hookList);
    return sort.setOperators(`concOperator(l1*,l2*));
  }

  /*
     We add 4 new operators for every sort
     lab<Sort>,ref<Sort>,pos<Sort>,exp<Sort>
     and the corresponding hooks
   */
  private static OperatorDeclList getRefOperators(
      SortDecl sort,
      ArrayList hookList) {
    OperatorDecl labOp = `OperatorDecl("lab"+sort.getName(),sort,Slots(concSlot(Slot("label",stringSortDecl),Slot("term",sort))));

    OperatorDecl refOp = `OperatorDecl("ref"+sort.getName(),sort,Slots(concSlot(Slot("label",stringSortDecl))));

    String posOpName = "pos"+sort.getName();
    OperatorDecl posOp = `OperatorDecl(posOpName,sort,Variadic(intSortDecl));
    hookList.add(posHooks(posOp,sort));

    return `concOperator(labOp,refOp,posOp);
  }

  private static HookDeclList posHooks(OperatorDecl opDecl, SortDecl sort) {
    
    String moduleName = sort.getModuleDecl().getModuleName().getName();
    String sortName = sort.getName();

    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.*;
    import tom.library.sl.*;
    ]%;

    String codeBlock =%[

    public Position toPos(){
      return Position.makeRelativePosition(toArray());
    }
     
    /**
     * constructs the reference from the relative position given in parameter
     */
    public static @sortName@ fromPos(Position pos){
      //available only for relative positions
      if(! pos.isRelative()) return null;
      @sortName@ ref = `pos@sortName@();
      int[] array = pos.toArray();
      for(int i=0;i<pos.depth();i++){
        ref = `pos@sortName@(ref*,array[i]);
      }
      return ref; 
    }

      ]%;

    return 
      `concHookDecl(
          ImportHookDecl(CutOperator(opDecl),"{"+codeImport+"}"),
          InterfaceHookDecl(CutOperator(opDecl),"{tom.library.sl.Reference}"),
          BlockHookDecl(CutOperator(opDecl),"{"+codeBlock+"}"));
  }

  private static HookDeclList expHooksModule(GomModuleName gomModuleName,
                                             SortList sorts,
                                             ModuleDecl mDecl,
                                             String packagePath,
                                             boolean forTermgraph) {
    String moduleName = gomModuleName.getName();

    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.*;
    import @packagePath@.@moduleName.toLowerCase()@.*;
    import tom.library.sl.*;
    import java.util.*;
   ]%;

    String codeBlockCommon =%[
    %include{java/util/HashMap.tom}
    %include{java/util/ArrayList.tom}
    %include{sl.tom}

    %typeterm Info{
      implement {Info}
    }


    public static class Info{
      public String label;
      public Position pos;
      public @moduleName@AbstractType term;
    }
    ]%;

    String codeStrategies = "";
    String CollectLabels= "Fail()",Collect = "Fail()";
    String Min= "Identity()",Switch= "Identity()",ClearMarked= "Identity()",Label2Pos = "Identity()";

    %match(sorts){
      concSort(_*,Sort[Decl=SortDecl[Name=sortName]],_*) -> {
        codeStrategies += getStrategies(`sortName,moduleName);
        Min = "Sequence(Min"+`sortName+"(info),"+Min+")";
        Switch = "Sequence(Switch"+`sortName+"(info),"+Switch+")";
        ClearMarked = "Sequence(ClearMarked"+`sortName+"(marked),"+ClearMarked+")";
        Label2Pos = "Sequence(Label2Pos"+`sortName+"(map),"+Label2Pos+")";
        CollectLabels = "ChoiceV(CollectLabels"+`sortName+"(map),"+CollectLabels+")";
        Collect = "ChoiceV(Collect"+`sortName+"(marked,info),"+Collect+")";
      }
    }

    String codeBlockTermWithPointers =%[

      public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        HashMap map = new HashMap();
        Strategy label2pos = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Pos@));
        return (@moduleName@AbstractType) `label2pos.fire(t);
      }
    ]%;



    String codeBlockTermGraph =%[

      public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        Info info = new Info();
        ArrayList marked = new ArrayList();
        HashMap map = new HashMap();
        Strategy normalization = `RepeatId(Sequence(Repeat(Sequence(OnceTopDown(@Collect@),BottomUp(@Min@),TopDown(@Switch@))),@ClearMarked@));
        Strategy label2pos = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Pos@));
        return (@moduleName@AbstractType) `Sequence(normalization,label2pos).fire(t);
      }

      public static @moduleName@AbstractType label2pos(@moduleName@AbstractType t){
        HashMap map = new HashMap();
        Strategy label2pos = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Pos@));
        return (@moduleName@AbstractType) label2pos.fire(t);
      }

    ]%;

    String codeBlock = "{" +codeBlockCommon + codeStrategies + (forTermgraph?codeBlockTermGraph:codeBlockTermWithPointers) + "}";

    return `concHookDecl(
        ImportHookDecl(CutModule(mDecl),"{"+codeImport+"}"),
        BlockHookDecl(CutModule(mDecl),codeBlock));
  }

  private static String getStrategies(String sortName, String moduleName){

    String strategies =%[

      %strategy Collect@sortName@(marked:ArrayList,info:Info) extends Fail(){
        visit @sortName@{
          lab@sortName@[label=label,term=term]-> {
            if(! marked.contains(`label)){
              info.label=`label;
              info.term=`lab@sortName@(label,term);
              info.pos=getEnvironment().getPosition();
              marked.add(`label);
              return `lab@sortName@(label,term);
            }
          }
        }
      }

    %strategy Min@sortName@(info:Info) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if(`label.equals(info.label)){
            if(getEnvironment().getPosition().compare(info.pos)==-1){
              info.pos=getEnvironment().getPosition();
            }
          }
        }
      }
    }

    %strategy Switch@sortName@(info:Info) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (info.pos.equals(getEnvironment().getPosition())){
            return (@sortName@) info.term;
          }
        }
        lab@sortName@[label=label,term=term] -> {
          if(`label.equals(info.label)){
            if (! info.pos.equals(getEnvironment().getPosition())){
              return `ref@sortName@(label);
            }
          }
        }
      }
    }


    %strategy ClearMarked@sortName@(list:ArrayList) extends Identity(){
      visit @sortName@{
        _ -> {
          list.clear();
        }
      }
    }

    %strategy CollectLabels@sortName@(map:HashMap) extends Fail(){
      visit @sortName@{
        lab@sortName@[label=label,term=term]-> {
          map.put(`label,getEnvironment().getPosition());
          return `term;
        }
      }
    }

    %strategy Label2Pos@sortName@(map:HashMap) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (! map.containsKey(`label)) {
            // ref with an unexistent label
            throw new RuntimeException("Term-graph with a null reference at"+getEnvironment().getPosition());
          }
          else {
            Position target = (Position) map.get(`label);
            Position pos =
              getEnvironment().getPosition().getRelativePosition(target);
            @sortName@ ref = `pos@sortName@();
            int[] array = `pos.toArray();
            for(int i=0;i<`pos.depth();i++){
              ref = `pos@sortName@(ref*,array[i]);
            }
            return ref;
          }
        }
      }
    }
    ]%;

    return strategies;
  }
}
