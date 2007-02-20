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

import tom.library.strategy.mutraveler.*;
import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class GomReferenceExpander {

  %include{java/util/HashMap.tom}
  %include{java/mustrategy.tom}
  %include{java/boolean.tom}
  %include { ../adt/gom/Gom.tom}

  // indicates if the expand method must include normalization phase 
  // specific to termgraphs
  private String packagePath;
  
  private SortDecl stringSortDecl,intSortDecl;
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

  public SortList expand(SortList list) {
    SortList expandedList = `concSort();
    HashMap mapModuleSorts = new HashMap();
    %match(list){
      concSort(_*,sort,_*) -> {
        SortDecl sortDecl = `sort.getDecl();
        GomModuleName module = sortDecl.getModuleDecl().getModuleName();
        SortDeclList sorts = (SortDeclList) mapModuleSorts.get(module);
        if(sorts !=null) {
          mapModuleSorts.put(module,`concSortDecl(sorts*,sortDecl));
        }
        else {
          mapModuleSorts.put(module,`concSortDecl(sortDecl));
        }
        expandedList = `concSort(expandedList*,expandSort(sort));
      }
    }
    //return expandedList;
    //add a global expand method in every ModuleDecl contained in the SortList
    return (SortList) `TopDown(ExpandModule(mapModuleSorts,packagePath,forTermgraph)).apply(expandedList);
  }

  %strategy ExpandModule(map:HashMap,packagePath:String,forTermgraph:boolean) extends `Identity(){
    visit ModuleDecl {
      module -> {
        SortDeclList sorts = (SortDeclList) map.get(`module.getModuleName());
        return (ModuleDecl) `module.setHooks(expHooksModule(`module,sorts,packagePath,forTermgraph));
      }
    }
  }


  private Sort expandSort(Sort sort) {
    OperatorDeclList l1 = sort.getOperators();
    OperatorDeclList l2 = getRefOperators(sort.getDecl());
    return sort.setOperators(`concOperator(l1*,l2*));
  }

  /*
     We add 4 new operators for every sort
     lab<Sort>,ref<Sort>,pos<Sort>,exp<Sort>
     and the corresponding hooks
   */
  private OperatorDeclList getRefOperators(SortDecl sort){
    OperatorDecl labOp = `OperatorDecl("lab"+sort.getName(),sort,Slots(concSlot(Slot("label",stringSortDecl),Slot("term",sort))),concHookDecl());

    OperatorDecl refOp = `OperatorDecl("ref"+sort.getName(),sort,Slots(concSlot(Slot("label",stringSortDecl))),concHookDecl());

    String posOpName = "pos"+sort.getName();
    OperatorDecl posOp = `OperatorDecl(posOpName,sort,Variadic(intSortDecl),posHooks());

    return `concOperator(labOp,refOp,posOp);
  }

  //TODO remove MuReference when sl is operational
  private HookDeclList posHooks(){
    return 
      `concHookDecl(InterfaceHookDecl("{tom.library.strategy.mutraveler.MuReference,tom.library.sl.Reference}"));
  }

  private static HookDeclList expHooksModule(ModuleDecl module,SortDeclList sorts,String packagePath,boolean forTermgraph){
    String moduleName = module.getModuleName().getName();
    
    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.*;
    import @packagePath@.@moduleName.toLowerCase()@.*;
    import tom.library.strategy.mutraveler.*;
    import java.util.*;
    ]%;

    String codeBlockCommon =%[
    %include{java/util/HashMap.tom}
    %include{java/util/ArrayList.tom}
    %include{java/mustrategy.tom}

    %typeterm Info{
      implement {Info}
    }


    public static class Info{
      public String label;
      public tom.library.strategy.mutraveler.Position pos;
      public @moduleName@AbstractType term;
    }
    ]%;

    String codeStrategies = "";
    String CollectLabels= "Fail()",Collect = "Fail()";
    String Min= "Identity()",Switch= "Identity()",ClearMarked= "Identity()",Label2Pos = "Identity()";

    %match(sorts){
      concSortDecl(_*,SortDecl[Name=sortName],_*) -> {
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
        MuStrategy label2pos = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Pos@));
        return (@moduleName@AbstractType) `label2pos.apply(t);
      }
    ]%;



    String codeBlockTermGraph =%[

      public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        Info info = new Info();
        ArrayList marked = new ArrayList();
        HashMap map = new HashMap();
        MuStrategy normalization = `RepeatId(Sequence(Repeat(Sequence(OnceTopDown(@Collect@),BottomUp(@Min@),TopDown(@Switch@))),@ClearMarked@));
        MuStrategy label2pos = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Pos@));
        return (@moduleName@AbstractType) `Sequence(normalization,label2pos).apply(t);
      }
    ]%;

    String codeBlock = codeBlockCommon + codeStrategies + (forTermgraph?codeBlockTermGraph:codeBlockTermWithPointers);

    return `concHookDecl(ImportHookDecl(codeImport),BlockHookDecl(codeBlock));
  }

  private static String getStrategies(String sortName, String moduleName){

    String strategies =%[

      %strategy Collect@sortName@(marked:ArrayList,info:Info) extends Fail(){
        visit @sortName@{
          lab@sortName@[label=label,term=term]-> {
            if(! marked.contains(`label)){
              info.label=`label;
              info.term=`lab@sortName@(label,term);
              info.pos=getPosition();
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
            if(getPosition().compare(info.pos)==-1){
              info.pos=getPosition(); 
            }
          }
        }
      }
    }

    %strategy Switch@sortName@(info:Info) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (info.pos.equals(getPosition())){
            return (@sortName@) info.term; 
          }
        }
        lab@sortName@[label=label,term=term] -> {
          if(`label.equals(info.label)){
            if (! info.pos.equals(getPosition())){
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
          map.put(`label,getPosition());
          return `term;
        }
      }
    }

    %strategy Label2Pos@sortName@(map:HashMap) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (! map.containsKey(`label)) {
            // ref with an unexistent label
            throw new RuntimeException("Term-graph with a null reference at"+getPosition());
          }
          else {
            Position target = (Position) map.get(`label);
            RelativePosition pos = 
              RelativePosition.make(getPosition(),target);
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
