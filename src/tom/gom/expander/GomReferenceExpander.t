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
      hookList:ArrayList) extends Identity(){
    visit Module {
      module@Module[
        MDecl=mdecl@ModuleDecl[ModuleName=modName],Sorts=sorts] -> {
        hookList.add(expHooksModule(`modName,`sorts,`mdecl,packagePath,forTermgraph));
      }
    }
  }

  %strategy ExpandSort(hookList:ArrayList) extends Identity() {
    visit Sort {
      sort@Sort[Decl=sortdecl@SortDecl[Name=sortname],Operators=ops] -> {
         
        //We add 3 new operators lab<Sort>,ref<Sort>,path<Sort>     
        OperatorDecl labOp = `OperatorDecl("lab"+sortname,sortdecl,Slots(concSlot(Slot("label",stringSortDecl),Slot("term",sortdecl))));
        OperatorDecl refOp = `OperatorDecl("ref"+sortname,sortdecl,Slots(concSlot(Slot("label",stringSortDecl))));
        OperatorDecl pathOp = `OperatorDecl("path"+sortname,sortdecl,Variadic(intSortDecl));
        hookList.add(pathHooks(pathOp,`sortdecl));
        return `sort.setOperators(`concOperator(ops*,labOp,refOp,pathOp));

      }
    }
  }

  private static HookDeclList pathHooks(OperatorDecl opDecl, SortDecl sort){

    String moduleName = sort.getModuleDecl().getModuleName().getName();
    String sortName = sort.getName();

    String codeImport =%[
      import @packagePath@.@moduleName.toLowerCase()@.types.*;
    import tom.library.sl.*;
    ]%;

    String codeBlock =%[

      public Path add(Path p){
        Position pp = Position.make(this);
        return make(pp.add(p));
      }

    public Path inv(){
      Position pp = Position.make(this);
      return make(pp.inv());
    }

    public Path sub(Path p){
      Position pp = Position.make(this);
      return make(pp.sub(p));
    }

    public int getHead(){
      return getHeadpath@sortName@();
    }

    public Path getTail(){
      return (Path) getTailpath@sortName@();
    }

    public Path normalize(){
      %match(this) {
        path@sortName@(X*,x,y,Y*) -> {
          if (`x==-`y) {
            return ((Path)`path@sortName@(X*,Y*)).normalize();
          }
        }
      }
      return this;
    }

    public Path conc(int i){
      path@sortName@ current = this;
      return (Path) `Conspath@sortName@(i,current); 
    }

    public static path@sortName@ make(Path path){
      @sortName@ ref = `path@sortName@();
      Path pp = path.normalize();
      int size = pp.length();
      for(int i=0;i<size;i++){
        ref = `path@sortName@(ref*,pp.getHead());
        pp = pp.getTail();
      }
      return (path@sortName@) ref;
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
      public Path path;
      public @moduleName@AbstractType term;
    }
    ]%;

    String codeStrategies = "";
    String CollectLabels= "Fail()",Collect = "Fail()";
    String Label2Path = "Identity()",NormalizeLabel = "Identity()";

    %match(sorts){
      concSort(_*,Sort[Decl=SortDecl[Name=sortName]],_*) -> {
        codeImport += %[
          import @packagePath@.@moduleName.toLowerCase()@.types.@`sortName.toLowerCase()@.*;
        ]%;
        codeStrategies += getStrategies(`sortName,moduleName);
        Label2Path = "Sequence(Label2Path"+`sortName+"(map),"+Label2Path+")";
        CollectLabels = "ChoiceV(CollectLabels"+`sortName+"(map),"+CollectLabels+")";
        NormalizeLabel = "Sequence(NormalizeLabel"+`sortName+"(map),"+NormalizeLabel+")";
      }
    }

    String codeBlockTermWithPointers =%[

      public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        HashMap map = new HashMap();
        Strategy label2path = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Path@));
        return (@moduleName@AbstractType) `label2path.fire(t);
      }
    ]%;

    String codeBlockTermGraph =%[

      public static @moduleName@AbstractType expand(@moduleName@AbstractType t){
        Info info = new Info();
        ArrayList marked = new ArrayList();
        HashMap map = new HashMap();
        @moduleName@AbstractType tt = (@moduleName@AbstractType) `InnermostIdSeq(@NormalizeLabel@).fire(t);
        return label2path(tt);
      }

    public static @moduleName@AbstractType label2path(@moduleName@AbstractType t){
      HashMap map = new HashMap();
      Strategy label2path = `Sequence(Repeat(OnceTopDown(@CollectLabels@)),TopDown(@Label2Path@));
      return (@moduleName@AbstractType) label2path.fire(t);
    }

    ]%;

    String codeBlock = codeBlockCommon + codeStrategies + (forTermgraph?codeBlockTermGraph:codeBlockTermWithPointers);

    return `concHookDecl(
        ImportHookDecl(CutModule(mDecl),Code(codeImport)),
        BlockHookDecl(CutModule(mDecl),Code(codeBlock)));
  }

  private static String getStrategies(String sortName, String moduleName){

    String strategies =%[

    %typeterm Info@sortName@{
        implement {Info@sortName@}
    }

    static class Info@sortName@{
      public Position omega;
      public @sortName@ term;
    }
 
      %strategy Collect@sortName@(marked:ArrayList,info:Info) extends Fail(){
        visit @sortName@{
          lab@sortName@[label=label,term=term]-> {
            if(! marked.contains(`label)){
              info.label=`label;
              info.term=`lab@sortName@(label,term);
              info.path=getEnvironment().getPosition();
              marked.add(`label);
              return `lab@sortName@(label,term);
            }
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

    %strategy Label2Path@sortName@(map:HashMap) extends Identity(){
      visit @sortName@{
        ref@sortName@[label=label] -> {
          if (! map.containsKey(`label)) {
            // find a reference with an unexistent label
            throw new RuntimeException("Term-graph with a null reference at"+getEnvironment().getPosition());
          }
          else {
            Position target = (Position) map.get(`label);
            @sortName@ ref = (@sortName@) (path@sortName@.make(target.sub(getEnvironment().getPosition())).normalize());
            return ref;
          }
        }
      }
    }

    %strategy CollectSubterm@sortName@(label:String,info:Info@sortName@) extends Identity(){
      visit @sortName@ {
        lab@sortName@[label=label,term=subterm] -> {
          if(label.equals(`label)){
            info.term = `subterm;
            info.omega = getEnvironment().getPosition();
            return `ref@sortName@(label);
          }
        }
      }
    }


    %strategy NormalizeLabel@sortName@(map:HashMap) extends Identity(){
      visit @sortName@ {
        ref@sortName@[label=label] -> {
          if (! map.containsKey(`label)){
            Info@sortName@ info = new Info@sortName@();
            Position pos = new Position(new int[]{});
            Position old = getEnvironment().getPosition();
            Position rootpos = new Position(new int[]{});
            map.put(`label,old);
            getEnvironment().goTo(rootpos.sub(getEnvironment().getPosition()));
            execute(`Try(TopDown(CollectSubterm@sortName@(label,info))));
            getEnvironment().goTo(old.sub(getEnvironment().getPosition()));
            return `lab@sortName@(label,info.term);
          }
        }
        lab@sortName@[label=label] -> {
          map.put(`label,getEnvironment().getPosition());
        }
      }
    }
    ]%;

    return strategies;
  }

}
