/*
 *
 * GOM
 *
 * Copyright (C) 2006 INRIA
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

  private String packagePath;
  private SortDecl stringSortDecl,intSortDecl;

  private GomEnvironment environment() {
    return GomEnvironment.getInstance();
  }

  public GomReferenceExpander(String packagePath) {
    this.packagePath = packagePath;
    stringSortDecl = environment().builtinSort("String");
    intSortDecl = environment().builtinSort("int");
    //we mark them as used builtins
    environment().markUsedBuiltin("String");
    environment().markUsedBuiltin("int");
  }

  public SortList expand(SortList typedModuleList) {
    //return (SortList) `_concSort(ExpandSort()).apply(typedModuleList);
    SortList expandedList = `concSort();
    %match(typedModuleList){
      concSort(_*,sort,_*) -> {
        expandedList = `concSort(expandedList*,expandSort(sort));
      }
    }
    return expandedList;
  }

  /*
     %strategy ExpandSort() extends `Identity() {
     visit Sort{
     sort@Sort[Decl=Decl,Operators=Operators]-> {
     return `sort.setOperators(concOperator(Operators*,getRefOperators(Decl)));
     }
     }
     }
   */

  private Sort expandSort(Sort sort) {
    OperatorDeclList l1 = sort.getOperators();
    OperatorDeclList l2 = getRefOperators(sort.getDecl());
    return `sort.setOperators(`concOperator(l1*,l2*));
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

    String expOpName = "exp"+sort.getName();
    OperatorDecl expOp = `OperatorDecl(expOpName,sort,Slots(concSlot(Slot("term",sort))),expHooks(sort));

    return `concOperator(labOp,refOp,posOp,expOp);
  }

  private HookDeclList posHooks(){
    return 
      `concHookDecl(InterfaceHookDecl(" tom.library.strategy.mutraveler.MuReference "));
  }

  private HookDeclList expHooks(SortDecl sortDecl){
    String sortName = sortDecl.getName();
    String moduleName = sortDecl.getModuleDecl().getModuleName().getName();
    String codeMake =%[
    @sortName@ termWithPos = expand(term);
    //to avoid unaccessible real_make statement
    if(! termWithPos.equals(term)){
      return termWithPos;
    }
    if(termWithPos.equals(term)){
      return term;
    }]%;

    String codeImport =%[
    import @packagePath@.@moduleName.toLowerCase()@.types.@sortName@;
    import tom.library.strategy.mutraveler.*;
    import java.util.HashMap;
    ]%;

    String codeBlock =%[
    %include{java/util/HashMap.tom}
    %include{java/mustrategy.tom}

    %strategy Collectlabels(tableMin:HashMap,tableRef:HashMap) extends `Identity() {
      visit @sortName@{
        lab@sortName@[label=label,term=term]-> {
          tableRef.put(`label,`term);
          if(!tableMin.containsKey(`label) || getPosition().compare(tableMin.get(`label))==-1){
            tableMin.put(`label,getPosition());
          }
        }
        ref@sortName@[label=label] -> {
          if(!tableMin.containsKey(`label) || getPosition().compare(tableMin.get(`label))==-1){
            tableMin.put(`label,getPosition());
          }
        }
      }
    }

    %strategy Replacelabels(tableMin:HashMap,tableRef:HashMap) extends `Identity() {
      visit @sortName@{

        ref@sortName@[label=label] -> {
          if (!tableRef.containsKey(`label)) throw new RuntimeException("The label "+`label+" is not referenced");
          if(getPosition().equals(tableMin.get(`label))){
            return (@sortName@) tableRef.get(`label);
          }
          else{ 
            RelativePosition pos = 
              RelativePosition.make(getPosition(),(Position) tableMin.get(`label));
            @sortName@ ref = `pos@sortName@();
            int[] array = pos.toArray();
            for(int i=0;i<pos.depth();i++){
              ref = `pos@sortName@(ref*,array[i]);
            }
            return ref; 
          }
        }
        lab@sortName@[label=label] -> {
          if (!tableRef.containsKey(`label)) throw new RuntimeException("The label "+`label+" is not referenced");
          if(getPosition().equals(tableMin.get(`label))){
            return (@sortName@) tableRef.get(`label);
          }
          else{ 
            RelativePosition pos = 
              RelativePosition.make(getPosition(),(Position) tableMin.get(`label));
            @sortName@ ref = `pos@sortName@();
            int[] array = pos.toArray();
            for(int i=0;i<pos.depth();i++){
              ref = `pos@sortName@(ref*,array[i]);
            }
            return ref; 
          }
        }
      }
    }

    public static @sortName@ expand(@sortName@ t){
      HashMap tableMin = new HashMap();
      HashMap tableRef = new HashMap();
      return (@sortName@) `Sequence(RepeatId(TopDown(Collectlabels(tableMin,tableRef))),TopDown(Replacelabels(tableMin,tableRef))).apply(t);
    }
    ]%;
    return `concHookDecl(MakeHookDecl(concSlot(Slot("term",sortDecl)),codeMake),ImportHookDecl(codeImport),BlockHookDecl(codeBlock));
  }


}
