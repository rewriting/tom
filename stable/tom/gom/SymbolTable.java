/*
 * Gom
 * 
 * Copyright (c) 2000-2009, INRIA
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 * 
 **/

package tom.gom;

import java.util.logging.Level;
import tom.gom.GomStreamManager;
import tom.gom.adt.symboltable.types.*;
import tom.gom.adt.symboltable.*;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import tom.gom.exception.*;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;

public class SymbolTable {

         private static   tom.gom.adt.gom.types.GomModuleList  tom_append_list_ConcGomModule( tom.gom.adt.gom.types.GomModuleList l1,  tom.gom.adt.gom.types.GomModuleList  l2) {     if( l1.isEmptyConcGomModule() ) {       return l2;     } else if( l2.isEmptyConcGomModule() ) {       return l1;     } else if(  l1.getTailConcGomModule() .isEmptyConcGomModule() ) {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( l1.getHeadConcGomModule() ,tom_append_list_ConcGomModule( l1.getTailConcGomModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GomModuleList  tom_get_slice_ConcGomModule( tom.gom.adt.gom.types.GomModuleList  begin,  tom.gom.adt.gom.types.GomModuleList  end, tom.gom.adt.gom.types.GomModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomModule()  ||  (end== tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule.make( begin.getHeadConcGomModule() ,( tom.gom.adt.gom.types.GomModuleList )tom_get_slice_ConcGomModule( begin.getTailConcGomModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AtomList  tom_append_list_ConcAtom( tom.gom.adt.gom.types.AtomList l1,  tom.gom.adt.gom.types.AtomList  l2) {     if( l1.isEmptyConcAtom() ) {       return l2;     } else if( l2.isEmptyConcAtom() ) {       return l1;     } else if(  l1.getTailConcAtom() .isEmptyConcAtom() ) {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,l2) ;     } else {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,tom_append_list_ConcAtom( l1.getTailConcAtom() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AtomList  tom_get_slice_ConcAtom( tom.gom.adt.gom.types.AtomList  begin,  tom.gom.adt.gom.types.AtomList  end, tom.gom.adt.gom.types.AtomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAtom()  ||  (end== tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( begin.getHeadConcAtom() ,( tom.gom.adt.gom.types.AtomList )tom_get_slice_ConcAtom( begin.getTailConcAtom() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.GrammarList  tom_append_list_ConcGrammar( tom.gom.adt.gom.types.GrammarList l1,  tom.gom.adt.gom.types.GrammarList  l2) {     if( l1.isEmptyConcGrammar() ) {       return l2;     } else if( l2.isEmptyConcGrammar() ) {       return l1;     } else if(  l1.getTailConcGrammar() .isEmptyConcGrammar() ) {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,l2) ;     } else {       return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( l1.getHeadConcGrammar() ,tom_append_list_ConcGrammar( l1.getTailConcGrammar() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.GrammarList  tom_get_slice_ConcGrammar( tom.gom.adt.gom.types.GrammarList  begin,  tom.gom.adt.gom.types.GrammarList  end, tom.gom.adt.gom.types.GrammarList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGrammar()  ||  (end== tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.grammarlist.ConsConcGrammar.make( begin.getHeadConcGrammar() ,( tom.gom.adt.gom.types.GrammarList )tom_get_slice_ConcGrammar( begin.getTailConcGrammar() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SectionList  tom_append_list_ConcSection( tom.gom.adt.gom.types.SectionList l1,  tom.gom.adt.gom.types.SectionList  l2) {     if( l1.isEmptyConcSection() ) {       return l2;     } else if( l2.isEmptyConcSection() ) {       return l1;     } else if(  l1.getTailConcSection() .isEmptyConcSection() ) {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( l1.getHeadConcSection() ,tom_append_list_ConcSection( l1.getTailConcSection() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SectionList  tom_get_slice_ConcSection( tom.gom.adt.gom.types.SectionList  begin,  tom.gom.adt.gom.types.SectionList  end, tom.gom.adt.gom.types.SectionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSection()  ||  (end== tom.gom.adt.gom.types.sectionlist.EmptyConcSection.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sectionlist.ConsConcSection.make( begin.getHeadConcSection() ,( tom.gom.adt.gom.types.SectionList )tom_get_slice_ConcSection( begin.getTailConcSection() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }       private static   tom.gom.adt.symboltable.types.StringList  tom_append_list_StringList( tom.gom.adt.symboltable.types.StringList l1,  tom.gom.adt.symboltable.types.StringList  l2) {     if( l1.isEmptyStringList() ) {       return l2;     } else if( l2.isEmptyStringList() ) {       return l1;     } else if(  l1.getTailStringList() .isEmptyStringList() ) {       return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,l2) ;     } else {       return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( l1.getHeadStringList() ,tom_append_list_StringList( l1.getTailStringList() ,l2)) ;     }   }   private static   tom.gom.adt.symboltable.types.StringList  tom_get_slice_StringList( tom.gom.adt.symboltable.types.StringList  begin,  tom.gom.adt.symboltable.types.StringList  end, tom.gom.adt.symboltable.types.StringList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyStringList()  ||  (end== tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( begin.getHeadStringList() ,( tom.gom.adt.symboltable.types.StringList )tom_get_slice_StringList( begin.getTailStringList() ,end,tail)) ;   }      private static   tom.gom.adt.symboltable.types.FieldDescriptionList  tom_append_list_concFieldDescription( tom.gom.adt.symboltable.types.FieldDescriptionList l1,  tom.gom.adt.symboltable.types.FieldDescriptionList  l2) {     if( l1.isEmptyconcFieldDescription() ) {       return l2;     } else if( l2.isEmptyconcFieldDescription() ) {       return l1;     } else if(  l1.getTailconcFieldDescription() .isEmptyconcFieldDescription() ) {       return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( l1.getHeadconcFieldDescription() ,l2) ;     } else {       return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( l1.getHeadconcFieldDescription() ,tom_append_list_concFieldDescription( l1.getTailconcFieldDescription() ,l2)) ;     }   }   private static   tom.gom.adt.symboltable.types.FieldDescriptionList  tom_get_slice_concFieldDescription( tom.gom.adt.symboltable.types.FieldDescriptionList  begin,  tom.gom.adt.symboltable.types.FieldDescriptionList  end, tom.gom.adt.symboltable.types.FieldDescriptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcFieldDescription()  ||  (end== tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( begin.getHeadconcFieldDescription() ,( tom.gom.adt.symboltable.types.FieldDescriptionList )tom_get_slice_concFieldDescription( begin.getTailconcFieldDescription() ,end,tail)) ;   }        private static java.util.ArrayList concArrayListAppend(Object o, java.util.ArrayList l) {   java.util.ArrayList res = (java.util.ArrayList)l.clone();   res.add(o);   return res; }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )) == null )) {         return ( (l2==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),l2) );       } else {         return ( (tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }     private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}   


  

  /** map sort-name -> SortDescription */
  private Hashtable<String,SortDescription> sorts = 
    new Hashtable<String,SortDescription>();

  /** map constructor-name -> ConstructorDescription */
  private Hashtable<String,ConstructorDescription> constructors =  
    new Hashtable<String,ConstructorDescription>();
  
  private Graph<String> sortDependences = new Graph<String>();
 
  private GomEnvironment gomEnvironment;
  
  public SymbolTable(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void fill(GomModuleList gml) {
    {{if ( (gml instanceof tom.gom.adt.gom.types.GomModuleList) ) {if ( (((( tom.gom.adt.gom.types.GomModuleList )gml) instanceof tom.gom.adt.gom.types.gommodulelist.ConsConcGomModule) || ((( tom.gom.adt.gom.types.GomModuleList )gml) instanceof tom.gom.adt.gom.types.gommodulelist.EmptyConcGomModule)) ) { tom.gom.adt.gom.types.GomModuleList  tomMatch354NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);do {{if (!( tomMatch354NameNumber_end_4.isEmptyConcGomModule() )) {fillFromGomModule( tomMatch354NameNumber_end_4.getHeadConcGomModule() )
; }if ( tomMatch354NameNumber_end_4.isEmptyConcGomModule() ) {tomMatch354NameNumber_end_4=(( tom.gom.adt.gom.types.GomModuleList )gml);} else {tomMatch354NameNumber_end_4= tomMatch354NameNumber_end_4.getTailConcGomModule() ;}}} while(!( (tomMatch354NameNumber_end_4==(( tom.gom.adt.gom.types.GomModuleList )gml)) ));}}}}

    computeSortDependences();
    isolateFreshSorts();
    //System.out.println("sorts concerned by freshgom: " + getFreshSorts());
    //fillRefreshPoints();
    fillAccessibleAtoms();
    generateConsAndNils();
  }

  public void addConstructor(String symbol, String codomain, FieldDescriptionList fields) {
    constructors.put(symbol, tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codomain, fields,  tom.gom.adt.symboltable.types.generationinfo.No.make() ) );
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(tom_append_list_StringList(l, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(symbol, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )));
  }

  public void addVariadicConstructor(String symbol, String domain, String codomain) {
    constructors.put(symbol, tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription.make(codomain, domain, false) );
    SortDescription s = sorts.get(codomain);
    StringList l = s.getConstructors();
    sorts.put(codomain,s.setConstructors(tom_append_list_StringList(l, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(symbol, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) )));
  }

  /**
   * returns the set of all constructor symbols
   **/
  public Set<String> getConstructors() {
    return new HashSet(constructors.keySet());
  }

  /**
   * returns the set of all sort symbols
   **/
  public Set<String> getSorts() {
    return new HashSet(sorts.keySet());
  }

  /**
   * clear all entries (sorts and constructors)
   */
  public void clear() {
    sorts.clear();
    constructors.clear();
  }

  public String getFullModuleName(String moduleName) {
    String packageName = gomEnvironment.getStreamManager().getPackagePath(moduleName);
    return (packageName.equals("") ? "" : packageName + ".")+moduleName.toLowerCase();
  }

  public String getFullAbstractTypeClassName(String moduleName) {
    return getFullModuleName(moduleName)+"."+moduleName+"AbstractType";
  }

  public String getFullSortClassName(String sort) {
    SortDescription desc = sorts.get(sort);
    if(desc==null) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return null;
    }
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom_m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;
 
        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom_m);
        return (packageName.equals("") ? "" : packageName + ".") 
          + tom_m.toLowerCase() + ".types." + sort; 
      }}}}

    throw new GomRuntimeException("Non exhaustive match");
    //getLogger().log(Level.SEVERE,GomMessage.nonExhaustiveMatch.getMessage());
    //return null;
  }

  public String getFullConstructorClassName(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if(desc==null) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return null;
    }
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons; 
      }}}{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return getFullSortClassName( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + cons; 
      }}}}

    throw new GomRuntimeException("Non exhaustive match");
    //getLogger().log(Level.SEVERE,GomMessage.nonExhaustiveMatch.getMessage());
    //return null;
  }

  public void generateConsAndNils() {
    for(String c: getConstructors()) {
      if(!isVariadic(c)) continue;
      String dom = getDomain(c);
      String codom = getCoDomain(c);
      FieldDescriptionList ConsFields = null; // head and tail
      FieldDescriptionList NilFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ;
      /* codom binds X = ... | c(dom*) | ...
         => codom binds X = ... | Consc(Headc:dom,Tailc:codom) | ... */
      if(isPatternType(codom)) {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SPattern.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SPattern.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
        /* codom = ... | c(<dom>*) | ...
           => codom = ... | Consc(Headc:<dom>,Tailc:codom) | ... */
      } else if(isRefreshPoint(c)) {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SRefreshPoint.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
        /* codom = ... | c(dom*) | ...
           => codom = ... | Consc(Headc:dom,Tailc:codom) | ... */
      } else {
        ConsFields =  tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Head"+c, dom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make( tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make("Tail"+c, codom,  tom.gom.adt.symboltable.types.status.SNone.make() ) , tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) ) 

;
      }

      // add the new constructors to constructors map
      String nilc = "Empty" + c;
      String consc = "Cons" + c;
      constructors.put(nilc,
           tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, NilFields,  tom.gom.adt.symboltable.types.generationinfo.GenNil.make(c) ) );
      constructors.put(consc,
           tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, ConsFields,  tom.gom.adt.symboltable.types.generationinfo.GenCons.make(c) ) );

      // modify the codomain description
      SortDescription sd = sorts.get(codom);
      StringList conslist = sd.getConstructors();
      conslist = tom_append_list_StringList(conslist, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(nilc, tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(consc, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) ) );
      sd = sd.setConstructors(conslist);
      sorts.put(codom,sd);
    }
  }

  public boolean isVariadic(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return false;
    }
  }

  public boolean isGenerated(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {boolean tomMatch358NameNumber_freshVar_4= false ;if ( ( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated()  instanceof tom.gom.adt.symboltable.types.generationinfo.No) ) {tomMatch358NameNumber_freshVar_4= true ;}if ( tomMatch358NameNumber_freshVar_4== false  ) {
 
          return true; 
        }}}}}

      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return false;
    }
  }

  public boolean isGeneratedCons(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {if ( ( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated()  instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {
 
          return true; 
        }}}}}

      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return false;
    }
  }

  public boolean isGeneratedNil(String cons) {
    try {
      ConstructorDescription desc = constructors.get(cons);
      {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {if ( ( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getGenerated()  instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {
 
          return true; 
        }}}}}

      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return false;
    }
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    for(Map.Entry<String,SortDescription> e: sorts.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter); 
      } catch(java.io.IOException ex) {
        ex.printStackTrace(); 
      }
      buf.append("sort " + e.getKey() + ":\n" + swriter + "\n");
    }
    for(Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
      java.io.StringWriter swriter = new java.io.StringWriter();
      try {
        tom.library.utils.Viewer.toTree(e.getValue(),swriter); 
      } catch(java.io.IOException ex) {
        ex.printStackTrace(); 
      }
      buf.append("constructor " + e.getKey() + ":\n" + swriter + "\n");
    }
    return buf.toString();
  }

  private void fillFromGomModule(GomModule m) {
    {{if ( (m instanceof tom.gom.adt.gom.types.GomModule) ) {if ( ((( tom.gom.adt.gom.types.GomModule )m) instanceof tom.gom.adt.gom.types.gommodule.GomModule) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch361NameNumber_freshVar_1= (( tom.gom.adt.gom.types.GomModule )m).getModuleName() ; tom.gom.adt.gom.types.SectionList  tomMatch361NameNumber_freshVar_2= (( tom.gom.adt.gom.types.GomModule )m).getSectionList() ;if ( (tomMatch361NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {if ( ((tomMatch361NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.ConsConcSection) || (tomMatch361NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.sectionlist.EmptyConcSection)) ) { tom.gom.adt.gom.types.SectionList  tomMatch361NameNumber_end_9=tomMatch361NameNumber_freshVar_2;do {{if (!( tomMatch361NameNumber_end_9.isEmptyConcSection() )) { tom.gom.adt.gom.types.Section  tomMatch361NameNumber_freshVar_13= tomMatch361NameNumber_end_9.getHeadConcSection() ;if ( (tomMatch361NameNumber_freshVar_13 instanceof tom.gom.adt.gom.types.section.Public) ) { tom.gom.adt.gom.types.GrammarList  tomMatch361NameNumber_freshVar_12= tomMatch361NameNumber_freshVar_13.getGrammarList() ;if ( ((tomMatch361NameNumber_freshVar_12 instanceof tom.gom.adt.gom.types.grammarlist.ConsConcGrammar) || (tomMatch361NameNumber_freshVar_12 instanceof tom.gom.adt.gom.types.grammarlist.EmptyConcGrammar)) ) { tom.gom.adt.gom.types.GrammarList  tomMatch361NameNumber_end_17=tomMatch361NameNumber_freshVar_12;do {{if (!( tomMatch361NameNumber_end_17.isEmptyConcGrammar() )) {fillFromGrammar( tomMatch361NameNumber_freshVar_1.getName() , tomMatch361NameNumber_end_17.getHeadConcGrammar() )


;
        }if ( tomMatch361NameNumber_end_17.isEmptyConcGrammar() ) {tomMatch361NameNumber_end_17=tomMatch361NameNumber_freshVar_12;} else {tomMatch361NameNumber_end_17= tomMatch361NameNumber_end_17.getTailConcGrammar() ;}}} while(!( (tomMatch361NameNumber_end_17==tomMatch361NameNumber_freshVar_12) ));}}}if ( tomMatch361NameNumber_end_9.isEmptyConcSection() ) {tomMatch361NameNumber_end_9=tomMatch361NameNumber_freshVar_2;} else {tomMatch361NameNumber_end_9= tomMatch361NameNumber_end_9.getTailConcSection() ;}}} while(!( (tomMatch361NameNumber_end_9==tomMatch361NameNumber_freshVar_2) ));}}}}}}

  }

  private void fillFromGrammar(String moduleName, Grammar g) {
    {{if ( (g instanceof tom.gom.adt.gom.types.Grammar) ) {if ( ((( tom.gom.adt.gom.types.Grammar )g) instanceof tom.gom.adt.gom.types.grammar.Grammar) ) { tom.gom.adt.gom.types.ProductionList  tomMatch362NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Grammar )g).getProductionList() ;if ( ((tomMatch362NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || (tomMatch362NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch362NameNumber_end_6=tomMatch362NameNumber_freshVar_1;do {{if (!( tomMatch362NameNumber_end_6.isEmptyConcProduction() )) {fillFromProduction(moduleName, tomMatch362NameNumber_end_6.getHeadConcProduction() )

; }if ( tomMatch362NameNumber_end_6.isEmptyConcProduction() ) {tomMatch362NameNumber_end_6=tomMatch362NameNumber_freshVar_1;} else {tomMatch362NameNumber_end_6= tomMatch362NameNumber_end_6.getTailConcProduction() ;}}} while(!( (tomMatch362NameNumber_end_6==tomMatch362NameNumber_freshVar_1) ));}}}}}

  }

  private static StringList getConstructors(ProductionList pl) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    {{if ( (pl instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )pl) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )pl) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch363NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )pl);do {{if (!( tomMatch363NameNumber_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch363NameNumber_freshVar_8= tomMatch363NameNumber_end_4.getHeadConcProduction() ;if ( (tomMatch363NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.production.Production) ) {
 
        res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch363NameNumber_freshVar_8.getName() , res) ; 
      }}if ( tomMatch363NameNumber_end_4.isEmptyConcProduction() ) {tomMatch363NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )pl);} else {tomMatch363NameNumber_end_4= tomMatch363NameNumber_end_4.getTailConcProduction() ;}}} while(!( (tomMatch363NameNumber_end_4==(( tom.gom.adt.gom.types.ProductionList )pl)) ));}}}}

    return res;
  }

  /* Methods only used by freshgom */

  /**
   * returns only sorts concerned by freshGom
   **/
  public Set<String> getFreshSorts() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while(it.hasNext()) { 
      if(!isFreshType(it.next())) {
        it.remove();
      }
    }
    return res;
  }


  /**
   * returns the set of all constructor symbols
   * concerned by freshgom
   **/
  public Set<String> getFreshConstructors() {
    HashSet<String> res = new HashSet();
    for(String c: constructors.keySet()) {
      if(isFreshType(getSort(c))) {
        res.add(c);
      }
    }
    return res;
  }


  public static String rawSort(String s) {
    return "Raw" + s; 
  }

  public String rawCons(String c) { 
    if(isGenerated(c)) {
      {{Object tomMatch364NameNumber_freshVar_0=getGenerated(c);if ( (tomMatch364NameNumber_freshVar_0 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch364NameNumber_freshVar_0) instanceof tom.gom.adt.symboltable.types.generationinfo.GenCons) ) {
 return "ConsRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch364NameNumber_freshVar_0).getBaseName() ; }}}{Object tomMatch364NameNumber_freshVar_3=getGenerated(c);if ( (tomMatch364NameNumber_freshVar_3 instanceof tom.gom.adt.symboltable.types.GenerationInfo) ) {if ( ((( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch364NameNumber_freshVar_3) instanceof tom.gom.adt.symboltable.types.generationinfo.GenNil) ) {
 return "EmptyRaw" +  (( tom.gom.adt.symboltable.types.GenerationInfo )tomMatch364NameNumber_freshVar_3).getBaseName() ; }}}}

    }
    return "Raw" + c; 
  }

  public String qualifiedRawSortId(String sort) {
    SortDescription desc = sorts.get(sort);
    if(desc==null) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return null;
    }
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) { String  tom_m= (( tom.gom.adt.symboltable.types.SortDescription )desc).getModuleSymbol() ;

        String packageName = gomEnvironment.getStreamManager().getPackagePath(tom_m);
        return (packageName.equals("") ? "" : packageName + ".") 
          + tom_m.toLowerCase() + ".types." + rawSort(sort); 
      }}}}

    throw new GomRuntimeException("Non exhaustive match");
    //getLogger().log(Level.SEVERE,GomMessage.nonExhaustiveMatch.getMessage());
    //return null;
  }

  public String qualifiedRawConstructorId(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    if(desc==null) {
      throw new UndeclaredConstructorException(cons);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), cons);
      //return null;
    }
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )desc) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) {

        return qualifiedRawSortId( (( tom.gom.adt.symboltable.types.ConstructorDescription )desc).getSortSymbol() ).toLowerCase() + "." + rawCons(cons); 
      }}}}

    throw new GomRuntimeException("Non exhaustive match");
    //getLogger().log(Level.SEVERE,GomMessage.nonExhaustiveMatch.getMessage());
    //return null;
  }

  private static StringList convertBoundAtoms(AtomList al) {
    StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
    {{if ( (al instanceof tom.gom.adt.gom.types.AtomList) ) {if ( (((( tom.gom.adt.gom.types.AtomList )al) instanceof tom.gom.adt.gom.types.atomlist.ConsConcAtom) || ((( tom.gom.adt.gom.types.AtomList )al) instanceof tom.gom.adt.gom.types.atomlist.EmptyConcAtom)) ) { tom.gom.adt.gom.types.AtomList  tomMatch367NameNumber_end_4=(( tom.gom.adt.gom.types.AtomList )al);do {{if (!( tomMatch367NameNumber_end_4.isEmptyConcAtom() )) {
 res =  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make( tomMatch367NameNumber_end_4.getHeadConcAtom() , res) ; }if ( tomMatch367NameNumber_end_4.isEmptyConcAtom() ) {tomMatch367NameNumber_end_4=(( tom.gom.adt.gom.types.AtomList )al);} else {tomMatch367NameNumber_end_4= tomMatch367NameNumber_end_4.getTailConcAtom() ;}}} while(!( (tomMatch367NameNumber_end_4==(( tom.gom.adt.gom.types.AtomList )al)) ));}}}}

    return res;
  }

  private void fillFromProduction(String moduleName, Production p) {
    {{if ( (p instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )p) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch368NameNumber_freshVar_1= (( tom.gom.adt.gom.types.Production )p).getType() ;if ( (tomMatch368NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { tom.gom.adt.gom.types.TypeSpec  tom_spe= tomMatch368NameNumber_freshVar_1.getSpecialization() ; String  tom_n= tomMatch368NameNumber_freshVar_1.getName() ; tom.gom.adt.gom.types.ProductionList  tom_pl= (( tom.gom.adt.gom.types.Production )p).getProductionList() ;


          // filling sorts (except AccessibleAtoms)
          StringList cons = getConstructors(tom_pl);
          StringList bound = convertBoundAtoms( (( tom.gom.adt.gom.types.Production )p).getBinds() );
          StringList empty =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
          FreshSortInfo info = null;
          {{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.typespec.ExpressionType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo.make(empty) ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.typespec.PatternType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo.make(bound, empty) ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.TypeSpec) ) {if ( ((( tom.gom.adt.gom.types.TypeSpec )tom_spe) instanceof tom.gom.adt.gom.types.typespec.AtomType) ) {
 info =  tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo.make() ; }}}}

          sorts.put(tom_n, tom.gom.adt.symboltable.types.sortdescription.SortDescription.make(cons, moduleName, info) );
          // filling constructors
          {{if ( (tom_pl instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )tom_pl) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )tom_pl) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch370NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_pl);do {{if (!( tomMatch370NameNumber_end_4.isEmptyConcProduction() )) {fillCons(tom_n, tomMatch370NameNumber_end_4.getHeadConcProduction() )
; }if ( tomMatch370NameNumber_end_4.isEmptyConcProduction() ) {tomMatch370NameNumber_end_4=(( tom.gom.adt.gom.types.ProductionList )tom_pl);} else {tomMatch370NameNumber_end_4= tomMatch370NameNumber_end_4.getTailConcProduction() ;}}} while(!( (tomMatch370NameNumber_end_4==(( tom.gom.adt.gom.types.ProductionList )tom_pl)) ));}}}}

        }}}}}

  }

  private void fillCons(String codom, Production p)  {
    {{if ( (p instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )p) instanceof tom.gom.adt.gom.types.production.Production) ) { String  tom_n= (( tom.gom.adt.gom.types.Production )p).getName() ; tom.gom.adt.gom.types.FieldList  tom_dl= (( tom.gom.adt.gom.types.Production )p).getDomainList() ;{{if ( (tom_dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )tom_dl) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )tom_dl) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) {if (!( (( tom.gom.adt.gom.types.FieldList )tom_dl).isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch372NameNumber_freshVar_5= (( tom.gom.adt.gom.types.FieldList )tom_dl).getHeadConcField() ;if ( (tomMatch372NameNumber_freshVar_5 instanceof tom.gom.adt.gom.types.field.StarredField) ) { tom.gom.adt.gom.types.GomType  tomMatch372NameNumber_freshVar_3= tomMatch372NameNumber_freshVar_5.getFieldType() ;if ( (tomMatch372NameNumber_freshVar_3 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {if (  (( tom.gom.adt.gom.types.FieldList )tom_dl).getTailConcField() .isEmptyConcField() ) {





            constructors.put(tom_n,
                 tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription.make(codom,  tomMatch372NameNumber_freshVar_3.getName() ,  tomMatch372NameNumber_freshVar_5.getSpecifier() .isRefresh()) );
            return; 
          }}}}}}}{if ( (tom_dl instanceof tom.gom.adt.gom.types.FieldList) ) {
 
            FieldDescriptionList fl = getFieldList(codom,tom_dl);
            constructors.put(tom_n, tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription.make(codom, fl,  tom.gom.adt.symboltable.types.generationinfo.No.make() ) );
          }}}

      }}}}

  }

  private FieldDescriptionList getFieldList(String codom, FieldList dl) {
    FieldDescriptionList res =  tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ;
    {{if ( (dl instanceof tom.gom.adt.gom.types.FieldList) ) {if ( (((( tom.gom.adt.gom.types.FieldList )dl) instanceof tom.gom.adt.gom.types.fieldlist.ConsConcField) || ((( tom.gom.adt.gom.types.FieldList )dl) instanceof tom.gom.adt.gom.types.fieldlist.EmptyConcField)) ) { tom.gom.adt.gom.types.FieldList  tomMatch373NameNumber_end_4=(( tom.gom.adt.gom.types.FieldList )dl);do {{if (!( tomMatch373NameNumber_end_4.isEmptyConcField() )) { tom.gom.adt.gom.types.Field  tomMatch373NameNumber_freshVar_10= tomMatch373NameNumber_end_4.getHeadConcField() ;if ( (tomMatch373NameNumber_freshVar_10 instanceof tom.gom.adt.gom.types.field.NamedField) ) { tom.gom.adt.gom.types.GomType  tomMatch373NameNumber_freshVar_9= tomMatch373NameNumber_freshVar_10.getFieldType() ; tom.gom.adt.gom.types.ScopeSpecifier  tom_spe= tomMatch373NameNumber_freshVar_10.getSpecifier() ;if ( (tomMatch373NameNumber_freshVar_9 instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {



        Status st = null;
        {{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.scopespecifier.Outer) ) {
 st =  tom.gom.adt.symboltable.types.status.SOuter.make() ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.scopespecifier.Inner) ) {
 st =  tom.gom.adt.symboltable.types.status.SInner.make() ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.scopespecifier.Neutral) ) {
 st =  tom.gom.adt.symboltable.types.status.SNeutral.make() ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.scopespecifier.None) ) {
 st = isPatternType(codom) ?  tom.gom.adt.symboltable.types.status.SPattern.make()  :  tom.gom.adt.symboltable.types.status.SNone.make() ; }}}{if ( (tom_spe instanceof tom.gom.adt.gom.types.ScopeSpecifier) ) {if ( ((( tom.gom.adt.gom.types.ScopeSpecifier )tom_spe) instanceof tom.gom.adt.gom.types.scopespecifier.Refresh) ) {
 st =  tom.gom.adt.symboltable.types.status.SRefreshPoint.make() ; }}}}

        FieldDescription desc =  tom.gom.adt.symboltable.types.fielddescription.FieldDescription.make( tomMatch373NameNumber_freshVar_10.getName() ,  tomMatch373NameNumber_freshVar_9.getName() , st) ;
        res = tom_append_list_concFieldDescription(res, tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription.make(desc, tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription.make() ) );
      }}}if ( tomMatch373NameNumber_end_4.isEmptyConcField() ) {tomMatch373NameNumber_end_4=(( tom.gom.adt.gom.types.FieldList )dl);} else {tomMatch373NameNumber_end_4= tomMatch373NameNumber_end_4.getTailConcField() ;}}} while(!( (tomMatch373NameNumber_end_4==(( tom.gom.adt.gom.types.FieldList )dl)) ));}}}}

    return res;
  }

  private void setAccessibleAtoms(String sort, StringList atoms) {
    SortDescription desc = sorts.get(sort);
    SortDescription ndesc = null;
    {{if ( (desc instanceof tom.gom.adt.symboltable.types.SortDescription) ) {if ( ((( tom.gom.adt.symboltable.types.SortDescription )desc) instanceof tom.gom.adt.symboltable.types.sortdescription.SortDescription) ) {

        ndesc = desc.setFreshInfo( (( tom.gom.adt.symboltable.types.SortDescription )desc).getFreshInfo() .setAccessibleAtoms(atoms));
      }}}}

    sorts.put(sort,ndesc);
  }

  private void setFreshSortInfo(String sort, FreshSortInfo i) {
    SortDescription desc = sorts.get(sort);
    sorts.put(sort,desc.setFreshInfo(i));
  }

  private GenerationInfo getGenerated(String cons) {
    ConstructorDescription desc = constructors.get(cons);
    return desc.getGenerated();
  }

  /**
   * precondition : isGenerated(cons)
   */
  public String getBaseName(String cons) {
    return getGenerated(cons).getBaseName();
  }

  public boolean isExpressionType(String sort) {
    if(getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.ExpressionTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return false;
    }
  }

  public boolean isPatternType(String sort) {
    if(getGomEnvironment().isBuiltin(sort)) {
      return false; 
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.PatternTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return false;
    }
  }

  public boolean isAtomType(String sort) {
    if(getGomEnvironment().isBuiltin(sort)) {
      return false;
    }
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.AtomTypeInfo) ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return false;
    }
  }

  /**
   * true if sort is concerned by freshgom
   **/
  public boolean isFreshType(String sort) {
    try {
      FreshSortInfo i = sorts.get(sort).getFreshInfo();
      {{if ( (i instanceof tom.gom.adt.symboltable.types.FreshSortInfo) ) {boolean tomMatch379NameNumber_freshVar_2= false ;if ( ((( tom.gom.adt.symboltable.types.FreshSortInfo )i) instanceof tom.gom.adt.symboltable.types.freshsortinfo.NoFreshSort) ) {tomMatch379NameNumber_freshVar_2= true ;}if ( tomMatch379NameNumber_freshVar_2== false  ) { return true; }}}}
      return false;
    } catch (NullPointerException e) {
      throw new UndeclaredSortException(sort);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
      //return false;
    }
  }

  public static class SetRefreshPoints extends tom.library.sl.BasicStrategy {private  tom.gom.SymbolTable  st;private  String  codom;public SetRefreshPoints( tom.gom.SymbolTable  st,  String  codom) {super(( new tom.library.sl.Identity() ));this.st=st;this.codom=codom;}public  tom.gom.SymbolTable  getst() {return st;}public  String  getcodom() {return codom;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.gom.adt.symboltable.types.FieldDescription  visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {if ( ((( tom.gom.adt.symboltable.types.FieldDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {



        if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).getSort() )) {
          return (( tom.gom.adt.symboltable.types.FieldDescription )tom__arg).setStatusValue( tom.gom.adt.symboltable.types.status.SRefreshPoint.make() );
        }
      }}}}return _visit_FieldDescription(tom__arg,introspector); }public  tom.gom.adt.symboltable.types.ConstructorDescription  visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {



        if(st.isExpressionType(codom) && st.isPatternType( (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).getDomain() )) {
          return (( tom.gom.adt.symboltable.types.ConstructorDescription )tom__arg).setIsRefreshPoint(true);
        }
      }}}}return _visit_ConstructorDescription(tom__arg,introspector); }public  tom.gom.adt.symboltable.types.FieldDescription  _visit_FieldDescription( tom.gom.adt.symboltable.types.FieldDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.gom.adt.symboltable.types.FieldDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.gom.adt.symboltable.types.ConstructorDescription  _visit_ConstructorDescription( tom.gom.adt.symboltable.types.ConstructorDescription  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.gom.adt.symboltable.types.ConstructorDescription )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.symboltable.types.FieldDescription) ) {return ((T)visit_FieldDescription((( tom.gom.adt.symboltable.types.FieldDescription )v),introspector));}if ( (v instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {return ((T)visit_ConstructorDescription((( tom.gom.adt.symboltable.types.ConstructorDescription )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_SetRefreshPoints( tom.gom.SymbolTable  t0,  String  t1) { return new SetRefreshPoints(t0,t1);}



  private void fillRefreshPoints() {
    for(Map.Entry<String,ConstructorDescription> e: constructors.entrySet()) {
      String c = e.getKey();
      ConstructorDescription cd = e.getValue();
      String codom = cd.getSortSymbol();
      try {
        ConstructorDescription ncd = tom_make_TopDown(tom_make_SetRefreshPoints(this,codom)).visit(cd);
        constructors.put(c,ncd);
      } catch(Exception ex) { throw new GomRuntimeException(); }
    }
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList 
    getConstructors(String sort) {
      return (tom.gom.adt.symboltable.types.stringlist.StringList)
        sorts.get(sort).getConstructors();
    }

  public String getSort(String constructor) {
    try { 
      return constructors.get(constructor).getSortSymbol(); 
    } catch(NullPointerException e) {
      throw new UndeclaredConstructorException(constructor);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), constructor);
      //return null;
    }
  }

  public String getChildSort(String constructor,int omega) {
    try {
      ConstructorDescription c = constructors.get(constructor);  
      int count=1;

      {{if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

          return  (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getDomain() ;
        }}}{if ( (c instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )c) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch382NameNumber_freshVar_4= (( tom.gom.adt.symboltable.types.ConstructorDescription )c).getFields() ;if ( ((tomMatch382NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || (tomMatch382NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch382NameNumber_end_9=tomMatch382NameNumber_freshVar_4;do {{if (!( tomMatch382NameNumber_end_9.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch382NameNumber_freshVar_13= tomMatch382NameNumber_end_9.getHeadconcFieldDescription() ;if ( (tomMatch382NameNumber_freshVar_13 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


          if (count==omega) {
            return  tomMatch382NameNumber_freshVar_13.getSort() ;
          } else {
            count++;
          }
        }}if ( tomMatch382NameNumber_end_9.isEmptyconcFieldDescription() ) {tomMatch382NameNumber_end_9=tomMatch382NameNumber_freshVar_4;} else {tomMatch382NameNumber_end_9= tomMatch382NameNumber_end_9.getTailconcFieldDescription() ;}}} while(!( (tomMatch382NameNumber_end_9==tomMatch382NameNumber_freshVar_4) ));}}}}}

    } catch(NullPointerException e) {
      throw new UndeclaredConstructorException(constructor);
      //getLogger().log(Level.SEVERE, GomMessage.undeclaredConstructorException.getMessage(), constructor);
      //return null;
    }
    throw new GomRuntimeException("Cannot access to the "+omega+"ith child of the constructor "+constructor);
    //getLogger().log(Level.SEVERE, GomMessage.cannotAccessToChildConstructor.getMessage(), new Object[] {omega,constructor});
    //return null;
  }

  public tom.gom.adt.symboltable.types.stringlist.StringList 
    getAccessibleAtoms(String sort) {
      try {
        return (tom.gom.adt.symboltable.types.stringlist.StringList)
          sorts.get(sort).getFreshInfo().getAccessibleAtoms();
      } catch(NullPointerException e) {
        throw new UndeclaredSortException(sort);
        //getLogger().log(Level.SEVERE, GomMessage.undeclaredSortException.getMessage(), sort);
        //return null;
      }
    }

  public tom.gom.adt.symboltable.types.stringlist.StringList getBoundAtoms(String sort) {
    return (tom.gom.adt.symboltable.types.stringlist.StringList)
      sorts.get(sort).getFreshInfo().getBoundAtoms();
  }

  private FieldDescriptionList getFieldList(String constructor) {
    return constructors.get(constructor).getFields();
  }

  public ArrayList<String> getFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch383NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch383NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch383NameNumber_freshVar_8= tomMatch383NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch383NameNumber_freshVar_8 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        result.add( tomMatch383NameNumber_freshVar_8.getFieldName() );
      }}if ( tomMatch383NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch383NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch383NameNumber_end_4= tomMatch383NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch383NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public ArrayList<String> getNeutralFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch384NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch384NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch384NameNumber_freshVar_9= tomMatch384NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch384NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch384NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        result.add( tomMatch384NameNumber_freshVar_9.getFieldName() );
      }}}if ( tomMatch384NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch384NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch384NameNumber_end_4= tomMatch384NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch384NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public ArrayList<String> getPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch385NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch385NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch385NameNumber_freshVar_9= tomMatch385NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch385NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch385NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        result.add( tomMatch385NameNumber_freshVar_9.getFieldName() );
      }}}if ( tomMatch385NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch385NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch385NameNumber_end_4= tomMatch385NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch385NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  /**
   * generates 'getfield()' except for HeadC and TailC
   * where it generates 'getHeadRawC()' and 'getTailRawC()'
   **/
  public String rawGetter(String cons, String field) {
    if(isGenerated(cons)) {
      String suffix = getBaseName(cons);
      if(field.startsWith("Head")) {
        return "getHeadRaw" + suffix + "()";
      } else if (field.startsWith("Tail")) {
        return "getTailRaw" + suffix + "()";
      }
    }
    return "get" + field + "()";
  }

  public ArrayList<String> getNonPatternFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch386NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch386NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch386NameNumber_freshVar_9= tomMatch386NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch386NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {boolean tomMatch386NameNumber_freshVar_11= false ;if ( ( tomMatch386NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {tomMatch386NameNumber_freshVar_11= true ;}if ( tomMatch386NameNumber_freshVar_11== false  ) {


        result.add( tomMatch386NameNumber_freshVar_9.getFieldName() );
      }}}if ( tomMatch386NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch386NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch386NameNumber_end_4= tomMatch386NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch386NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public ArrayList<String> getOuterFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch387NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch387NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch387NameNumber_freshVar_9= tomMatch387NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch387NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch387NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        result.add( tomMatch387NameNumber_freshVar_9.getFieldName() );
      }}}if ( tomMatch387NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch387NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch387NameNumber_end_4= tomMatch387NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch387NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public ArrayList<String> getInnerFields(String constructor) {
    ArrayList<String> result = new ArrayList<String>();
    FieldDescriptionList l = getFieldList(constructor);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch388NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch388NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch388NameNumber_freshVar_9= tomMatch388NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch388NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch388NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        result.add( tomMatch388NameNumber_freshVar_9.getFieldName() );
      }}}if ( tomMatch388NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch388NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch388NameNumber_end_4= tomMatch388NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch388NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return result;
  }

  public String getSort(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch389NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch389NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch389NameNumber_freshVar_9= tomMatch389NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch389NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {

        if ( tomMatch389NameNumber_freshVar_9.getFieldName() .equals(field)) return  tomMatch389NameNumber_freshVar_9.getSort() ;
      }}if ( tomMatch389NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch389NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch389NameNumber_end_4= tomMatch389NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch389NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    throw new GomRuntimeException("Should never happen");
    //getLogger().log(Level.SEVERE,GomMessage.shouldNeverHappen.getMessage(),new Object[]{cons,field});
    //return null;
  }

  public boolean containsRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ;
      }}}{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch390NameNumber_freshVar_4= (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getFields() ;if ( ((tomMatch390NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || (tomMatch390NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch390NameNumber_end_9=tomMatch390NameNumber_freshVar_4;do {{if (!( tomMatch390NameNumber_end_9.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch390NameNumber_freshVar_13= tomMatch390NameNumber_end_9.getHeadconcFieldDescription() ;if ( (tomMatch390NameNumber_freshVar_13 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch390NameNumber_freshVar_13.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {



          return true;
        }}}if ( tomMatch390NameNumber_end_9.isEmptyconcFieldDescription() ) {tomMatch390NameNumber_end_9=tomMatch390NameNumber_freshVar_4;} else {tomMatch390NameNumber_end_9= tomMatch390NameNumber_end_9.getTailconcFieldDescription() ;}}} while(!( (tomMatch390NameNumber_end_9==tomMatch390NameNumber_freshVar_4) ));}}}}}

    return false;
  }

  public boolean isOuter(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch391NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch391NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch391NameNumber_freshVar_9= tomMatch391NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch391NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch391NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SOuter) ) {


        if( tomMatch391NameNumber_freshVar_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch391NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch391NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch391NameNumber_end_4= tomMatch391NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch391NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isPattern(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch392NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch392NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch392NameNumber_freshVar_9= tomMatch392NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch392NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch392NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SPattern) ) {


        if( tomMatch392NameNumber_freshVar_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch392NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch392NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch392NameNumber_end_4= tomMatch392NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch392NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isInner(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch393NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch393NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch393NameNumber_freshVar_9= tomMatch393NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch393NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch393NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SInner) ) {


        if( tomMatch393NameNumber_freshVar_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch393NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch393NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch393NameNumber_end_4= tomMatch393NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch393NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isNeutral(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch394NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch394NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch394NameNumber_freshVar_9= tomMatch394NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch394NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch394NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SNeutral) ) {


        if( tomMatch394NameNumber_freshVar_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch394NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch394NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch394NameNumber_end_4= tomMatch394NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch394NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }


  /**
   * for variadic operators 
   */
  public boolean isRefreshPoint(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 
        return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getIsRefreshPoint() ; 
      }}}}

    return false;
  }

  /**
   * for variadic operators 
   */
  public String getDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getDomain() ; }}}}

    throw new GomRuntimeException("Non variadic operator");
    //getLogger().log(Level.SEVERE,GomMessage.nonVariadicOperator.getMessage());
    //return null;
  }

  /**
   * for variadic operators 
   */
  public String getCoDomain(String cons) {
    ConstructorDescription d = constructors.get(cons);
    {{if ( (d instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )d) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {
 return  (( tom.gom.adt.symboltable.types.ConstructorDescription )d).getSortSymbol() ; }}}}

    throw new GomRuntimeException("Non variadic operator");
    //getLogger().log(Level.SEVERE,GomMessage.nonVariadicOperator.getMessage());
    //return null;
  }


  public boolean isRefreshPoint(String cons, String field) {
    FieldDescriptionList l = getFieldList(cons);
    {{if ( (l instanceof tom.gom.adt.symboltable.types.FieldDescriptionList) ) {if ( (((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || ((( tom.gom.adt.symboltable.types.FieldDescriptionList )l) instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch398NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);do {{if (!( tomMatch398NameNumber_end_4.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch398NameNumber_freshVar_9= tomMatch398NameNumber_end_4.getHeadconcFieldDescription() ;if ( (tomMatch398NameNumber_freshVar_9 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {if ( ( tomMatch398NameNumber_freshVar_9.getStatusValue()  instanceof tom.gom.adt.symboltable.types.status.SRefreshPoint) ) {


        if( tomMatch398NameNumber_freshVar_9.getFieldName() .equals(field)) {
          return true;
        }
      }}}if ( tomMatch398NameNumber_end_4.isEmptyconcFieldDescription() ) {tomMatch398NameNumber_end_4=(( tom.gom.adt.symboltable.types.FieldDescriptionList )l);} else {tomMatch398NameNumber_end_4= tomMatch398NameNumber_end_4.getTailconcFieldDescription() ;}}} while(!( (tomMatch398NameNumber_end_4==(( tom.gom.adt.symboltable.types.FieldDescriptionList )l)) ));}}}}

    return false;
  }

  public boolean isBound(String cons, String field) {
    String sort = getSort(cons);
    return getBoundAtoms(sort).contains(getSort(cons,field));
  }


  public Set<String> getAtoms() {
    Set<String> res = getSorts();
    Iterator<String> it = res.iterator();
    while(it.hasNext()) {
      if(!isAtomType(it.next())) {
        it.remove();
      }
    }
    return res;
  }

  private StringList getAccessibleAtoms
    (String sort, HashSet<String> visited)  {
      if(getGomEnvironment().isBuiltin(sort) || visited.contains(sort)) {
        return  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
      }
      visited.add(sort);
      if(isAtomType(sort)) {
        return  tom.gom.adt.symboltable.types.stringlist.ConsStringList.make(sort, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ) ;
      }
      StringList res =  tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ;
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        {{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) {

            StringList tyatoms = getAccessibleAtoms( (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ,visited);
            res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
          }}}{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch399NameNumber_freshVar_4= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( ((tomMatch399NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || (tomMatch399NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch399NameNumber_end_9=tomMatch399NameNumber_freshVar_4;do {{if (!( tomMatch399NameNumber_end_9.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch399NameNumber_freshVar_13= tomMatch399NameNumber_end_9.getHeadconcFieldDescription() ;if ( (tomMatch399NameNumber_freshVar_13 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) {


              StringList tyatoms = getAccessibleAtoms( tomMatch399NameNumber_freshVar_13.getSort() ,visited);
              res = tom_append_list_StringList(tyatoms,tom_append_list_StringList(res, tom.gom.adt.symboltable.types.stringlist.EmptyStringList.make() ));
            }}if ( tomMatch399NameNumber_end_9.isEmptyconcFieldDescription() ) {tomMatch399NameNumber_end_9=tomMatch399NameNumber_freshVar_4;} else {tomMatch399NameNumber_end_9= tomMatch399NameNumber_end_9.getTailconcFieldDescription() ;}}} while(!( (tomMatch399NameNumber_end_9==tomMatch399NameNumber_freshVar_4) ));}}}}}

      }
      return res;
    }

  private void fillAccessibleAtoms() {
    for(String s: getSorts()) {
      if(isExpressionType(s) || isPatternType(s)) {
        StringList atoms = getAccessibleAtoms(s,new HashSet<String>());
        setAccessibleAtoms(s,atoms);
      }
    }
  }

  private void computeSortDependences() {
    sortDependences.clear();
    for(String sort: getSorts()) {
      if (getGomEnvironment().isBuiltin(sort)) continue;
      for(String c: getConstructors(sort)) {
        ConstructorDescription cd = constructors.get(c);
        {{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.VariadicConstructorDescription) ) { String  tom_ty= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getDomain() ;

            if(!getGomEnvironment().isBuiltin(tom_ty)) {
              sortDependences.addLink(sort,tom_ty);
            }
          }}}{if ( (cd instanceof tom.gom.adt.symboltable.types.ConstructorDescription) ) {if ( ((( tom.gom.adt.symboltable.types.ConstructorDescription )cd) instanceof tom.gom.adt.symboltable.types.constructordescription.ConstructorDescription) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch400NameNumber_freshVar_4= (( tom.gom.adt.symboltable.types.ConstructorDescription )cd).getFields() ;if ( ((tomMatch400NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.ConsconcFieldDescription) || (tomMatch400NameNumber_freshVar_4 instanceof tom.gom.adt.symboltable.types.fielddescriptionlist.EmptyconcFieldDescription)) ) { tom.gom.adt.symboltable.types.FieldDescriptionList  tomMatch400NameNumber_end_9=tomMatch400NameNumber_freshVar_4;do {{if (!( tomMatch400NameNumber_end_9.isEmptyconcFieldDescription() )) { tom.gom.adt.symboltable.types.FieldDescription  tomMatch400NameNumber_freshVar_13= tomMatch400NameNumber_end_9.getHeadconcFieldDescription() ;if ( (tomMatch400NameNumber_freshVar_13 instanceof tom.gom.adt.symboltable.types.fielddescription.FieldDescription) ) { String  tom_ty= tomMatch400NameNumber_freshVar_13.getSort() ;


              if(!getGomEnvironment().isBuiltin(tom_ty)) {
                sortDependences.addLink(sort,tom_ty);
              }
            }}if ( tomMatch400NameNumber_end_9.isEmptyconcFieldDescription() ) {tomMatch400NameNumber_end_9=tomMatch400NameNumber_freshVar_4;} else {tomMatch400NameNumber_end_9= tomMatch400NameNumber_end_9.getTailconcFieldDescription() ;}}} while(!( (tomMatch400NameNumber_end_9==tomMatch400NameNumber_freshVar_4) ));}}}}}

      }
    }
  }

  /**
   * sets sorts that are not connected to any atom at NoFreshSort
   **/
  private void isolateFreshSorts() {
    Set<String> atoms = getAtoms();
    Set<String> sorts = getSorts();
    Set<String> connected = sortDependences.connected(atoms);
    sorts.removeAll(connected);
    for(String s: sorts) { setFreshSortInfo(s, tom.gom.adt.symboltable.types.freshsortinfo.NoFreshSort.make() ); }
  }

  /* TODO: use GomMessage and the logger instead of RutimeException */
//getLogger().log(Level.SEVERE,"Failed compute import list: " + e.getMessage());

  public Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
/*
  public class SortException extends RuntimeException {
    protected String sortName;

    public SortException(String sortName) {
      super();
      this.sortName = sortName;
    }
    public String getSortName() { return sortName; } 
    public String toString() {
      return "sort exception: " + sortName;
    }
  }

  public class UndeclaredSortException extends SortException {
    public UndeclaredSortException(String n) { super(n); }
    public String toString() {
      return "undeclared sort: " + sortName;
    }
  }

  public class ConstructorException extends RuntimeException {
    protected String constructorName;
    public ConstructorException(String constructorName) {
      super();
      this.constructorName = constructorName;
    }
    public String getConstructorName() { return constructorName; } 
    public String toString() {
      return "constructor exception: " + constructorName;
    }
  }

  public class UndeclaredConstructorException extends ConstructorException {
    public UndeclaredConstructorException(String n) { super(n); }
  }

  public class InvalidConstructorException extends ConstructorException {
    public InvalidConstructorException(String n) { super(n); }
  }
*/
}
