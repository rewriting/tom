/*
 * Gom
 *
 * Copyright (c) 2006-2009, INRIA
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.antlradapter;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.GomMessage;
import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.backend.CodeGen;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.library.sl.VisitFailure;
import tom.library.sl.Strategy;

public class AdapterGenerator {

  /* Attributes needed to call tom properly */
  private File tomHomePath;
  // Here, we use a GomEnvironment in spite of the fact that only a GomStreamManager is needed
  // But as other classes have a GomEnvironment, we keep the same model for the moment
  // It is better to stay with one single model whatever class is used
  private GomEnvironment gomEnvironment;
  private String grammarPkg = "";
  private String grammarName = "";

  AdapterGenerator(File tomHomePath, GomEnvironment gomEnvironment, String grammar) {
    this.tomHomePath = tomHomePath;
    this.gomEnvironment = gomEnvironment;
    int lastDot = grammar.lastIndexOf('.');
    if (-1 != lastDot) {
      // the grammar is in a package different from the gom file
      this.grammarPkg = grammar.substring(0,lastDot);
      this.grammarName = grammar.substring(lastDot+1,grammar.length());
    } else {
      this.grammarPkg = getStreamManager().getDefaultPackagePath();
      this.grammarName = grammar;
    }
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public GomStreamManager getStreamManager() {
    return getGomEnvironment().getStreamManager();
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

         private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if(  l1.getTailCodeList() .isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}    






  public void generate(ModuleList moduleList, HookDeclList hookDecls) {
    writeTokenFile(moduleList);
    writeAdapterFile(moduleList);
    //writeTreeFile(moduleList);
  }

  public int writeTokenFile(ModuleList moduleList) {
    try {
       File output = tokenFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       generateTokenFile(moduleList, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  public int writeAdapterFile(ModuleList moduleList) {
    try {
       File output = adaptorFileToGenerate();
       // make sure the directory exists
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateAdapterFile(moduleList, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  private String adapterPkg() {
    String packagePrefix = getStreamManager().getDefaultPackagePath();
    return ((packagePrefix=="")?filename():packagePrefix+"."+filename()).toLowerCase();
  }

  public void generateAdapterFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    Collection operatorset = new HashSet();
    try {
      tom_make_TopDown(tom_make_CollectOperators(operatorset)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperators should not fail");
    }
    writer.write(
    "\npackage "+adapterPkg()+";\n\nimport org.antlr.runtime.Token;\nimport org.antlr.runtime.tree.Tree;\n"




);
    if (!"".equals(grammarPkg)) {
    writer.write("\nimport "+grammarPkg+"."+grammarName+"Parser;\n"

);
    }
    writer.write("\npublic class "+grammarName+filename()+"Adaptor {\n  public static shared.SharedObject getTerm(Tree tree) {\n    shared.SharedObject res = null;\n    if(tree.isNil()) {\n      throw new RuntimeException(\"nil term\");\n    }\n    if(tree.getType()==Token.INVALID_TOKEN_TYPE) {\n      throw new RuntimeException(\"bad type\");\n    }\n    \n    switch (tree.getType()) {\n"











);

    Iterator it = operatorset.iterator();
    while(it.hasNext()) {
      OperatorDecl opDecl = (OperatorDecl) it.next();
      {{if ( (opDecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )opDecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch53_2= (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getProd() ;if ( (tomMatch53_2 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) { tom.gom.adt.gom.types.SortDecl  tom_domainSort= tomMatch53_2.getSort() ; tom.gom.adt.gom.types.OperatorDecl  tom_op=(( tom.gom.adt.gom.types.OperatorDecl )opDecl);


          Code cast = genGetTerm(tom_domainSort,"tree.getChild(i)");
          Code code =
             tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("      case "+grammarName+"Parser.") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make( (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getName() ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(":\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        {\n") , tom.gom.adt.code.types.code.ConsCodeList.make(/* createemptylist*/
                 tom.gom.adt.code.types.code.Code.make("          res = ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Empty.make(tom_op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(".make();\n") , tom.gom.adt.code.types.code.ConsCodeList.make(/* addelements*/
                 tom.gom.adt.code.types.code.Code.make("          for(int i = 0; i < tree.getChildCount(); i++) {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(tom_domainSort) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" elem = ") ,tom_append_list_CodeList(cast, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(";\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make(tom_op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" list = (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make(tom_op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") res;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("res = list.append(elem);\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          break;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        }\n") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) )) ) ) ) ) ) ) ) ) ) ) 
























;
          CodeGen.generateCode(code,writer);
        }}}}{if ( (opDecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )opDecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch53_8= (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getProd() ; tom.gom.adt.gom.types.TypedProduction  tom_prod=tomMatch53_8;boolean tomMatch53_12= false ;if ( (tomMatch53_8 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {if ( (tom_prod==tomMatch53_8) ) {tomMatch53_12= true ;}}if (!(tomMatch53_12)) {


        Code code =
           tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("      case "+grammarName+"Parser.") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make( (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getName() ) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(":\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        {\n") 
              , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) 




;
        {{if ( (tom_prod instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tom_prod) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tom_slotList= (( tom.gom.adt.gom.types.TypedProduction )tom_prod).getSlots() ;

            int idx = 0;
            SlotList sList = tom_slotList;
            int length = sList.length();
            String sCode = "\n          if(tree.getChildCount()!="+length+") {\n            throw new RuntimeException(\"Node \" + tree + \": "+length+" child(s) expected, but \" + tree.getChildCount() + \" found\");\n          }\n"



;
            code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(sCode) , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ;

            while(sList.isConsConcSlot()) {
              Slot slot = sList.getHeadConcSlot();
              sList = sList.getTailConcSlot();
              Code cast = genGetTerm(slot.getSort(),"tree.getChild("+idx+")");
              code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(slot.getSort()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" field" + idx+ " = ") ,tom_append_list_CodeList(cast, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(";\n") 
                  , tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) ) ) ) 





;
              idx++;
            }
            code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          res = ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make((( tom.gom.adt.gom.types.OperatorDecl )opDecl)) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(".make(") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(genArgsList(tom_slotList)) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(");\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          break;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        }\n") 
                , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) 







;
              }}}}


          CodeGen.generateCode(code,writer);
        }}}}}


    }

    writer.write("\n    }\n    return res;\n  }\n}\n"




);
  }

  public void generateTokenFile(ModuleList moduleList, Writer writer)
    throws java.io.IOException {
    Collection bag = new HashSet();
    try {
      tom_make_TopDown(tom_make_CollectOperatorNames(bag)).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperatorNames should not fail");
    }
    Iterator it = bag.iterator();
    while(it.hasNext()) {
      String op = (String) it.next();
      writer.write(op + ";\n");
    }
  }

  public static class CollectOperators extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  bag;public CollectOperators( java.util.Collection  bag) {super(( new tom.library.sl.Identity() ));this.bag=bag;}public  java.util.Collection  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom__arg) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {


        bag.add((( tom.gom.adt.gom.types.OperatorDecl )tom__arg));
      }}}}return _visit_OperatorDecl(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  _visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.OperatorDecl )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.OperatorDecl) ) {return ((T)visit_OperatorDecl((( tom.gom.adt.gom.types.OperatorDecl )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectOperators( java.util.Collection  t0) { return new CollectOperators(t0);}public static class CollectOperatorNames extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  bag;public CollectOperatorNames( java.util.Collection  bag) {super(( new tom.library.sl.Identity() ));this.bag=bag;}public  java.util.Collection  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom__arg) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {






        bag.add( (( tom.gom.adt.gom.types.OperatorDecl )tom__arg).getName() );
      }}}}return _visit_OperatorDecl(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  _visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.OperatorDecl )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.OperatorDecl) ) {return ((T)visit_OperatorDecl((( tom.gom.adt.gom.types.OperatorDecl )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectOperatorNames( java.util.Collection  t0) { return new CollectOperatorNames(t0);}



  protected Code genGetTerm(SortDecl sort, String tree) {
    Code code =  tom.gom.adt.code.types.code.EmptyCodeList.make() ;
    {{if ( (sort instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )sort) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("(") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(sort) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(")" + grammarName+filename() + "Adaptor.getTerm(" + tree+ ")") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) 


;
      }}}{if ( (sort instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )sort) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) { String  tom_name= (( tom.gom.adt.gom.types.SortDecl )sort).getName() ;

        if("int".equals(tom_name)) {
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Integer.parseInt(" + tree+ ".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if("long".equals(tom_name)) {
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Long.parseLong(" + tree+ ".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if ("String".equals(tom_name)) {
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(tree+ ".getText()") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if ("boolean".equals(tom_name)) {
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Boolean.valueOf(" + tree+ ".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else {
          throw new RuntimeException("Unsupported builtin "+tom_name);
        }
      }}}}

    return code;
  }

  protected String genArgsList(SlotList slots) {
    String res = "";
    SlotList sList = slots;
    int idx=0;
    while(sList.isConsConcSlot()) {
      Slot slot = sList.getHeadConcSlot();
      sList = sList.getTailConcSlot();
      res += "field" + idx;
      if(sList.isConsConcSlot()) {
        res += ", ";
      }
      idx++;
    }
    return res;
  }

  protected String fullFileName() {
    return (adapterPkg() + "." + grammarName+filename()).replace('.',File.separatorChar);
  }

  protected String filename() {
    String filename = (new File(getStreamManager().getOutputFileName())).getName();
    int dotidx = filename.indexOf('.');
    if(-1 != dotidx) {
      filename = filename.substring(0,dotidx);
    }
    return filename;
  }

  protected File tokenFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"TokenList.txt");
    return output;
  }

  protected File adaptorFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"Adaptor.java");
    return output;
  }

}
