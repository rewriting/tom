/*
 * Gom
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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

package tom.gom.backend.shared;

import java.io.*;
import java.util.*;

import tom.gom.backend.TemplateClass;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.platform.OptionManager;

public class SortTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassNameList operatorList;
  ClassNameList variadicOperatorList;
  GomClassList operatorClasses;
  SlotFieldList slotList;
  boolean maximalsharing;

         private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyConcGomClass() ) {       return l2;     } else if( l2.isEmptyConcGomClass() ) {       return l1;     } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomClass()  ||  (end== tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.ClassNameList  tom_append_list_ConcClassName( tom.gom.adt.objects.types.ClassNameList l1,  tom.gom.adt.objects.types.ClassNameList  l2) {     if( l1.isEmptyConcClassName() ) {       return l2;     } else if( l2.isEmptyConcClassName() ) {       return l1;     } else if(  l1.getTailConcClassName() .isEmptyConcClassName() ) {       return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,l2) ;     } else {       return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( l1.getHeadConcClassName() ,tom_append_list_ConcClassName( l1.getTailConcClassName() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.ClassNameList  tom_get_slice_ConcClassName( tom.gom.adt.objects.types.ClassNameList  begin,  tom.gom.adt.objects.types.ClassNameList  end, tom.gom.adt.objects.types.ClassNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcClassName()  ||  (end== tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.classnamelist.ConsConcClassName.make( begin.getHeadConcClassName() ,( tom.gom.adt.objects.types.ClassNameList )tom_get_slice_ConcClassName( begin.getTailConcClassName() ,end,tail)) ;   }    

  public SortTemplate(File tomHomePath,
                      OptionManager manager,
                      boolean maximalsharing,
                      List importList, 	
                      GomClass gomClass,
                      TemplateClass mapping,
                      GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.maximalsharing = maximalsharing;
    {{if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )gomClass)) instanceof tom.gom.adt.objects.types.gomclass.SortClass) ) {





        this.abstractType =  (( tom.gom.adt.objects.types.GomClass )gomClass).getAbstractType() ;
        this.operatorList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getOperators() ;
        this.variadicOperatorList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getVariadicOperators() ;
        this.operatorClasses =  (( tom.gom.adt.objects.types.GomClass )gomClass).getOperatorClasses() ;
        this.slotList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
        return;
      }}}}}

    throw new GomRuntimeException(
        "Bad argument for SortTemplate: " + gomClass);
  }

  protected String generateInterface() {
    String interfaces =  super.generateInterface();
    if (interfaces.equals("")) return "";
    else return "implements "+interfaces.substring(1)+"";
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write("\npackage "+getPackage()+";\n"+generateImport()+"\n\npublic abstract class "+className()+" extends "+fullClassName(abstractType)+" "+generateInterface()+" {\n  /**\n   * Sole constructor.  (For invocation by subclass\n   * constructors, typically implicit.)\n   */\n  protected "+className()+"() {}\n\n"+generateBlock()+"\n"











);
generateBody(writer);
writer.write("\n}\n"

);
  }

  public void generateBody(java.io.Writer writer) throws java.io.IOException {
    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();

      writer.write("\n  /**\n   * Returns true if the term is rooted by the symbol "+operatorName.getName()+"\n   *\n   * @return true if the term is rooted by the symbol "+operatorName.getName()+"\n   */\n  public boolean "+isOperatorMethod(operatorName)+"() {\n    return false;\n  }\n"








);
    }
    // methods for each slot
    SlotFieldList sl = slotList;
    while (!sl.isEmptyConcSlotField()) {
      SlotField slot = sl.getHeadConcSlotField();
      sl = sl.getTailConcSlotField();

      writer.write("\n  /**\n   * Returns the subterm corresponding to the slot "+slot.getName()+"\n   *\n   * @return the subterm corresponding to the slot "+slot.getName()+"\n   */\n  public "+slotDomain(slot)+" "+getMethod(slot)+"() {\n    throw new UnsupportedOperationException(\"This "+className()+" has no "+slot.getName()+"\");\n  }\n\n  /**\n   * Returns a new term where the subterm corresponding to the slot "+slot.getName()+"\n   * is replaced by the term given in argument.\n   * Note that there is no side-effect: a new term is returned and the original term is left unchanged\n   *\n   * @param _arg the value of the new subterm\n   * @return a new term where the subterm corresponding to the slot "+slot.getName()+" is replaced by _arg\n   */\n  public "+className()+" "+setMethod(slot)+"("+slotDomain(slot)+" _arg) {\n    throw new UnsupportedOperationException(\"This "+className()+" has no "+slot.getName()+"\");\n  }\n"




















);

    }

    /* fromTerm method, dispatching to operator classes */
    writer.write("\n  protected static tom.library.utils.IdConverter idConv = new tom.library.utils.IdConverter();\n\n  /**\n   * Returns an ATerm representation of this term.\n   *\n   * @return null to indicate to sub-classes that they have to work\n   */\n  public aterm.ATerm toATerm() {\n    // returns null to indicate sub-classes that they have to work\n    return null;\n  }\n\n  /**\n   * Returns a "+fullClassName()+" from an ATerm without any conversion\n   *\n   * @param trm ATerm to handle to retrieve a Gom term\n   * @return the term from the ATerm\n   */\n  public static "+fullClassName()+" fromTerm(aterm.ATerm trm) {\n    return fromTerm(trm,idConv);\n  }\n\n  /**\n   * Returns a "+fullClassName()+" from a String without any conversion\n   *\n   * @param s String containing the ATerm\n   * @return the term from the String\n   */\n  public static "+fullClassName()+" fromString(String s) {\n    return fromTerm(atermFactory.parse(s),idConv);\n  }\n\n  /**\n   * Returns a "+fullClassName()+" from a Stream without any conversion\n   *\n   * @param stream stream containing the ATerm\n   * @return the term from the Stream\n   * @throws java.io.IOException if a problem occurs with the stream\n   */\n  public static "+fullClassName()+" fromStream(java.io.InputStream stream) throws java.io.IOException {\n    return fromTerm(atermFactory.readFromFile(stream),idConv);\n  }\n\n  /**\n   * Apply a conversion on the ATerm and returns a "+fullClassName()+"\n   *\n   * @param trm ATerm to convert into a Gom term\n   * @param atConv ATermConverter used to convert the ATerm\n   * @return the Gom term\n   * @throws IllegalArgumentException\n   */\n  public static "+fullClassName()+" fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {\n    aterm.ATerm convertedTerm = atConv.convert(trm);\n    "+fullClassName()+" tmp;\n    java.util.ArrayList<"+fullClassName()+"> results = new java.util.ArrayList<"+fullClassName()+">();\n"























































);
    ClassNameList constructor = tom_append_list_ConcClassName(operatorList,tom_append_list_ConcClassName(variadicOperatorList, tom.gom.adt.objects.types.classnamelist.EmptyConcClassName.make() ));
    while(!constructor.isEmptyConcClassName()) {
      ClassName operatorName = constructor.getHeadConcClassName();
      constructor = constructor.getTailConcClassName();
      writer.write("\n    tmp = "+fullClassName(operatorName)+".fromTerm(convertedTerm,atConv);\n    if(tmp!=null) {\n      results.add(tmp);\n    }"



);
    }

    writer.write("\n    switch(results.size()) {\n      case 0:\n        throw new IllegalArgumentException(trm + \" is not a "+className()+"\");\n      case 1:\n        return results.get(0);\n      default:\n        java.util.logging.Logger.getLogger(\""+className()+"\").log(java.util.logging.Level.WARNING,\"There were many possibilities ({0}) in {1} but the first one was chosen: {2}\",new Object[] {results.toString(), \""+fullClassName()+"\", results.get(0).toString()});\n        return results.get(0);\n    }\n  }\n\n  /**\n   * Apply a conversion on the ATerm contained in the String and returns a "+fullClassName()+" from it\n   *\n   * @param s String containing the ATerm\n   * @param atConv ATerm Converter used to convert the ATerm\n   * @return the Gom term\n   */\n  public static "+fullClassName()+" fromString(String s, tom.library.utils.ATermConverter atConv) {\n    return fromTerm(atermFactory.parse(s),atConv);\n  }\n\n  /**\n   * Apply a conversion on the ATerm contained in the Stream and returns a "+fullClassName()+" from it\n   *\n   * @param stream stream containing the ATerm\n   * @param atConv ATerm Converter used to convert the ATerm\n   * @return the Gom term\n   */\n  public static "+fullClassName()+" fromStream(java.io.InputStream stream, tom.library.utils.ATermConverter atConv) throws java.io.IOException {\n    return fromTerm(atermFactory.readFromFile(stream),atConv);\n  }\n"
































);

    /* abstract method to compare two terms represented by objects without maximal sharing */
    /* used in the mapping */
    if(!maximalsharing) {
      writer.write("\n  /**\n   * Abstract method to compare two terms represented by objects without maximal sharing\n   *\n   * @param o Object used to compare\n   * @return true if the two objects are equal\n   */\n  public abstract boolean deepEquals(Object o);\n\n\n"









);
    }

    if(maximalsharing) {
      writer.write("\n  /**\n   * Checks if an object is equal\n   *\n   * @param o object which is compared\n   * @return true if objects are equal, false otherwise\n   */\n  @Override\n  public boolean equals(Object o) { return this == o; }\n"








);
    } else {
      writer.write("\n  /**\n   * Checks if an object is equal\n   *\n   * @param o object which is compared\n   * @return true if objects are equal, false otherwise\n   */\n  @Override\n  public boolean equals(Object o) { return this.deepEquals(o); }\n"








);
    }

    /* length and reverse prototypes, only usable on lists */
    writer.write("\n  /**\n   * Returns the length of the list\n   *\n   * @return the length of the list\n   * @throws IllegalArgumentException if the term is not a list\n   */\n  public int length() {\n    throw new IllegalArgumentException(\n      \"This \"+this.getClass().getName()+\" is not a list\");\n  }\n\n  /**\n   * Returns an inverted term\n   *\n   * @return the inverted list\n   * @throws IllegalArgumentException if the term is not a list\n   */\n  public "+fullClassName()+" reverse() {\n    throw new IllegalArgumentException(\n      \"This \"+this.getClass().getName()+\" is not a list\");\n  }\n\n  "






















);

    /*
     * generate a getCollection<OpName>() method for all variadic operators
     */
    ClassNameList varopList = variadicOperatorList;
    while (!varopList.isEmptyConcClassName()) {
      ClassName operatorName = varopList.getHeadConcClassName();
      varopList = varopList.getTailConcClassName();

      String varopName = operatorName.getName();
      SlotFieldList tmpsl = slotList;
      while (!tmpsl.isEmptyConcSlotField()) {
        SlotField slot = tmpsl.getHeadConcSlotField();
        tmpsl = tmpsl.getTailConcSlotField();
        if(slot.getName().equals("Head" + varopName)) {
          String domainClassName = fullClassName(slot.getDomain());
          writer.write("\n  /**\n   * Returns a Collection extracted from the term\n   *\n   * @return the collection\n   * @throws UnsupportedOperationException if the term is not a list\n   */\n  public java.util.Collection<"+primitiveToReferenceType(domainClassName)+"> getCollection"+varopName+"() {\n    throw new UnsupportedOperationException(\"This "+className()+" cannot be converted into a Collection\");\n  }\n          "









);
        }
      }
    }

  /*
    // methods for each variadic operator
    consum = variadicOperatorList;
    while(!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();
      // look for the corresponding domain
matchblock: {
      %match(slotList) {
        ConcSlotField(_*,slot@SlotField[Name=opname,Domain=domain],_*) -> {
          if(`opname.equals("Head"+operatorName.getName())) {
      writer.write(%[
  public java.util.Collection<@primitiveToReferenceType(slotDomain(`slot))@> @getCollectionMethod(operatorName)@() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }
]%);
      break matchblock;
          }
        }
      }
      }
    }
  */
    if(hooks.containsTomCode()) {
      mapping.generate(writer);
    }

    /*
     * generate code for Enumerator
     */
    generateEnum(writer);
  }

  public void generateTomMapping(Writer writer) throws java.io.IOException {
    writer.write("\n%typeterm "+className()+" {\n  implement { "+fullClassName()+" }\n  is_sort(t) { ($t instanceof "+fullClassName()+") }\n"



);
    if(maximalsharing) {
      writer.write("\n  equals(t1,t2) { ($t1==$t2) }\n"

);
    } else {
      writer.write("\n  equals(t1,t2) { ((("+fullClassName()+")$t1).deepEquals($t2)) }\n"

);
    }
    writer.write("\n}\n"

);
 }

private void generateEnum(java.io.Writer writer) throws java.io.IOException {
  String P = "tom.library.enumerator.";
  String E = "tom.library.enumerator.Enumeration";

  writer.write(
"\n  /*\n   * Initialize the (cyclic) data-structure\n   * in order to generate/enumerate terms\n   */\n\n  protected static "+E+"<"+fullClassName()+"> enum"+className()+" = null;\n  public static final "+E+"<"+fullClassName()+"> tmpenum"+className()+" = new "+E+"<"+fullClassName()+">(("+P+"LazyList<"+P+"Finite<"+fullClassName()+">>) null);\n\n  public static "+E+"<"+fullClassName()+"> getEnumeration() {\n    if(enum"+className()+" == null) { \n      enum"+className()+" = "+generateSum()+"\n      tmpenum"+className()+".p1 = new "+P+"P1<"+P+"LazyList<"+P+"Finite<"+fullClassName()+">>>() {\n        public "+P+"LazyList<"+P+"Finite<"+fullClassName()+">> _1() { return enum"+className()+".parts(); }\n      };\n\n    }\n    return enum"+className()+";\n  }\n"


















);

}

/*
 * build a.plus(b).plus(f);
 */
private String generateSum() {
  String res = null;
  Set<String> toInitialize = new HashSet<String>();

  //System.out.println("operatorClasses = " + operatorClasses);
  //System.out.println("variadicOperatorList = " + variadicOperatorList);

  {{if ( (operatorClasses instanceof tom.gom.adt.objects.types.GomClassList) ) {if ( (((( tom.gom.adt.objects.types.GomClassList )(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || ((( tom.gom.adt.objects.types.GomClassList )(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) { tom.gom.adt.objects.types.GomClassList  tomMatch584_end_4=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);do {{if (!( tomMatch584_end_4.isEmptyConcGomClass() )) { tom.gom.adt.objects.types.GomClass  tomMatch584_10= tomMatch584_end_4.getHeadConcGomClass() ;if ( (tomMatch584_10 instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tomMatch584_10) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) { tom.gom.adt.objects.types.ClassName  tomMatch584_8= tomMatch584_10.getSortName() ;if ( (tomMatch584_8 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch584_8) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { tom.gom.adt.objects.types.SlotFieldList  tom_slotList= tomMatch584_10.getSlotFields() ;if (  tomMatch584_8.getName() .equals(className()) ) {

      String exp = fullClassName( tomMatch584_10.getClassName() ) + ".funMake()";
      {{if ( (tom_slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if ( (( tom.gom.adt.objects.types.SlotFieldList )tom_slotList).isEmptyConcSlotField() ) {

          exp += ".apply(" + fullClassName() + ".tmpenum" + className() + ")";
        }}}}{if ( (tom_slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch585_end_6=(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList);do {{if (!( tomMatch585_end_6.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch585_10= tomMatch585_end_6.getHeadConcSlotField() ;if ( (tomMatch585_10 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch585_10) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tomMatch585_9= tomMatch585_10.getDomain() ;if ( (tomMatch585_9 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch585_9) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { String  tom_domainName= tomMatch585_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain=tomMatch585_9;

          if(getGomEnvironment().isBuiltinClass(tom_domain)) {
            exp += ".apply(tom.library.enumerator.Combinators.make" + tom_domainName+ "())";
          } else {
            exp += ".apply(" + fullClassName(tom_domain) + ".tmpenum" + tom_domainName+ ")";
            if(!fullClassName().equals(fullClassName(tom_domain))) {
              toInitialize.add(fullClassName(tom_domain));
            }
          }
        }}}}}if ( tomMatch585_end_6.isEmptyConcSlotField() ) {tomMatch585_end_6=(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList);} else {tomMatch585_end_6= tomMatch585_end_6.getTailConcSlotField() ;}}} while(!( (tomMatch585_end_6==(( tom.gom.adt.objects.types.SlotFieldList )tom_slotList)) ));}}}}

      res = (res==null)?exp:res+"\n        .plus(" + exp + ")";
    }}}}}}if ( tomMatch584_end_4.isEmptyConcGomClass() ) {tomMatch584_end_4=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);} else {tomMatch584_end_4= tomMatch584_end_4.getTailConcGomClass() ;}}} while(!( (tomMatch584_end_4==(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) ));}}}{if ( (operatorClasses instanceof tom.gom.adt.objects.types.GomClassList) ) {if ( (((( tom.gom.adt.objects.types.GomClassList )(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || ((( tom.gom.adt.objects.types.GomClassList )(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) { tom.gom.adt.objects.types.GomClassList  tomMatch584_end_19=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);do {{if (!( tomMatch584_end_19.isEmptyConcGomClass() )) { tom.gom.adt.objects.types.GomClass  tomMatch584_26= tomMatch584_end_19.getHeadConcGomClass() ;if ( (tomMatch584_26 instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tomMatch584_26) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) { tom.gom.adt.objects.types.ClassName  tomMatch584_23= tomMatch584_26.getSortName() ; tom.gom.adt.objects.types.GomClass  tomMatch584_24= tomMatch584_26.getEmpty() ; tom.gom.adt.objects.types.GomClass  tomMatch584_25= tomMatch584_26.getCons() ;if ( (tomMatch584_23 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch584_23) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {if ( (tomMatch584_24 instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tomMatch584_24) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) { tom.gom.adt.objects.types.ClassName  tomMatch584_32= tomMatch584_24.getSortName() ;if ( (tomMatch584_32 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch584_32) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {if ( (tomMatch584_25 instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tomMatch584_25) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) { tom.gom.adt.objects.types.ClassName  tomMatch584_40= tomMatch584_25.getSortName() ;if ( (tomMatch584_40 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch584_40) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { tom.gom.adt.objects.types.SlotFieldList  tom_consSlotList= tomMatch584_25.getSlotFields() ;if (  tomMatch584_23.getName() .equals(className()) ) {





      String exp = fullClassName( tomMatch584_24.getClassName() ) + ".funMake().apply(" + fullClassName() + ".tmpenum" + className() + ")";
      res = (res==null)?exp:res+"\n        .plus(" + exp + ")";
      
      exp = fullClassName( tomMatch584_25.getClassName() ) + ".funMake()";
      {{if ( (tom_consSlotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if ( (( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList).isEmptyConcSlotField() ) {

          exp += ".apply(" + fullClassName() + ".tmpenum" + className() + ")";
        }}}}{if ( (tom_consSlotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch586_end_6=(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList);do {{if (!( tomMatch586_end_6.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch586_10= tomMatch586_end_6.getHeadConcSlotField() ;if ( (tomMatch586_10 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch586_10) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tomMatch586_9= tomMatch586_10.getDomain() ;if ( (tomMatch586_9 instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )tomMatch586_9) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { String  tom_domainName= tomMatch586_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain=tomMatch586_9;

          if(getGomEnvironment().isBuiltinClass(tom_domain)) {
            exp += ".apply(tom.library.enumerator.Combinators.make" + tom_domainName+ "())";
          } else {
            exp += ".apply(" + fullClassName(tom_domain) + ".tmpenum" + tom_domainName+ ")";
            if(!fullClassName().equals(fullClassName(tom_domain))) {
              toInitialize.add(fullClassName(tom_domain));
            }
          }
        }}}}}if ( tomMatch586_end_6.isEmptyConcSlotField() ) {tomMatch586_end_6=(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList);} else {tomMatch586_end_6= tomMatch586_end_6.getTailConcSlotField() ;}}} while(!( (tomMatch586_end_6==(( tom.gom.adt.objects.types.SlotFieldList )tom_consSlotList)) ));}}}}

      res = (res==null)?exp:res+"\n        .plus(" + exp + ")";
    }}}}}}}}}}}}}}if ( tomMatch584_end_19.isEmptyConcGomClass() ) {tomMatch584_end_19=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);} else {tomMatch584_end_19= tomMatch584_end_19.getTailConcGomClass() ;}}} while(!( (tomMatch584_end_19==(( tom.gom.adt.objects.types.GomClassList )operatorClasses)) ));}}}}



  res += ";\n\n";
  for(String className:toInitialize) {
    res += "      " + className + ".getEnumeration();\n";
  }

  return res;
}




}
