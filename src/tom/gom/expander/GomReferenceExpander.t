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

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class GomReferenceExpander {

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
        ModuleDecl module = sortDecl.getModuleDecl();
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
    //add a global expand method in every ModuleDecl for each sort
     %match(expandedList){
      concSort(_*,sort,_*) -> {
        SortDecl sortDecl = `sort.getDecl();
        ModuleDecl module = sortDecl.getModuleDecl();
        SortDeclList sorts = (SortDeclList) mapModuleSorts.get(module);
        HookDeclList hooks = module.getHooks();
        HookDeclList expHooks = expHooksModule(module,sorts);
        module = module.setHooks(`concHookDecl(hooks*,expHooks*));
        sortDecl = sortDecl.setModuleDecl(module);
       `sort = `sort.setDecl(sortDecl); 
        expandedList = `concSort(expandedList*,sort);
      }
     }
     return expandedList;
  }

  private Sort expandSort(Sort sort) {
    OperatorDeclList l1 = sort.getOperators();
    OperatorDeclList l2 = getRefOperators(sort.getDecl());
    Sort extendSort = sort.setOperators(`concOperator(l1*,l2*));
    //add an expand method in every sort
    HookDeclList expHooks = expHooksSort(extendSort.getDecl());
    HookDeclList oldHooks = extendSort.getHooks();
    extendSort = extendSort.setHooks(`concHookDecl(oldHooks*,expHooks*));
    return extendSort;
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
  
  private HookDeclList expHooksModule(ModuleDecl module,SortDeclList sorts){
    String moduleName = module.getModuleName().getName();
    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.*;
    ]%;

    String codeBlock =%[
    public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
      ]%;

    %match(sorts){
      concSortDecl(_*,SortDecl[Name=name],_*) -> {
        codeBlock += "t="+`name+".expand(t);\n";
      }
    }

    codeBlock += %[
      return t;
    }
    ]%;  
    return `concHookDecl(ImportHookDecl(codeImport),BlockHookDecl(codeBlock));
  }
    
  private HookDeclList expHooksSort(SortDecl sortDecl){
    String sortName = sortDecl.getName();
    String moduleName = sortDecl.getModuleDecl().getModuleName().getName();

    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.@sortName.toLowerCase()@.*;
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
      public @sortName@ term;
    }


    %strategy Collect(marked:ArrayList,info:Info) extends Fail(){
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

    %strategy Min(info:Info) extends Identity(){
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

    %strategy Switch(info:Info) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (info.pos.equals(getPosition())){
            return info.term; 
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


    %strategy ClearMarked(list:ArrayList) extends Identity(){
      visit @sortName@{
        _ -> {
          list.clear();
        }
      }
    }

    %strategy CollectLabels(map:HashMap) extends Fail(){
      visit @sortName@{
        lab@sortName@[label=label,term=term]-> {
          map.put(`label,getPosition());
          return `term;
        }
      }
    }

    %strategy Label2Pos(map:HashMap) extends Identity(){
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


    String codeBlockTermWithPointers =%[
      
    public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
      HashMap map = new HashMap();
      MuStrategy label2pos = `Sequence(Repeat(OnceTopDown(CollectLabels(map))),TopDown(Label2Pos(map)));
      return (@moduleName@AbstractType) `label2pos.apply(t);
    }
    ]%;

    String codeBlockTermGraph =%[
    
    public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        Info info = new Info();
        ArrayList marked = new ArrayList();
        HashMap map = new HashMap();
        MuStrategy normalization = `RepeatId(Sequence(Repeat(Sequence(OnceTopDown(Collect(marked,info)),BottomUp(Min(info)),TopDown(Switch(info)))),ClearMarked(marked)));
        MuStrategy label2pos = `Sequence(Repeat(OnceTopDown(CollectLabels(map))),TopDown(Label2Pos(map)));
        return (@moduleName@AbstractType) `Sequence(normalization,label2pos).apply(t);
      }
    ]%;

    String codeBlock = codeBlockCommon + (forTermgraph?codeBlockTermGraph:codeBlockTermWithPointers);

    return `concHookDecl(ImportHookDecl(codeImport),BlockHookDecl(codeBlock));
}


}
