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
import java.util.logging.*;
import tom.gom.backend.TemplateHookedClass;
import tom.gom.backend.TemplateClass;
import tom.gom.backend.CodeGen;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;
import tom.library.enumerator.F;

public class OperatorTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassName extendsType;
  ClassName sortName;
  SlotFieldList slotList;
  String comments;
  boolean multithread;
  boolean maximalsharing;
  boolean jmicompatible;
  boolean variadicconstructor;

         private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }    

  public OperatorTemplate(File tomHomePath,
                          OptionManager manager,
                          List importList, 	
                          GomClass gomClass,
                          TemplateClass mapping,
                          boolean multithread,
                          boolean maximalsharing,
                          boolean jmicompatible,
                          GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.multithread = multithread;
    this.maximalsharing = maximalsharing;
    this.jmicompatible = jmicompatible;
    {{if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )gomClass)) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {





        this.abstractType =  (( tom.gom.adt.objects.types.GomClass )gomClass).getAbstractType() ;
        this.extendsType =  (( tom.gom.adt.objects.types.GomClass )gomClass).getExtendsType() ;
        this.sortName =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSortName() ;
        this.variadicconstructor =
          // add a condition to detect variadic operators with the same name as their sort
          ! sortName.equals(extendsType) &&
          gomEnvironment.getSymbolTable().isVariadic(extendsType.getName());
        this.slotList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
        this.comments =  (( tom.gom.adt.objects.types.GomClass )gomClass).getComments() ;
        return;
      }}}}}

    throw new GomRuntimeException(
        "Bad argument for OperatorTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

writer.write(
"\npackage "+getPackage()+";\n"+generateImport()+"\n"


);

if(maximalsharing) {
  writer.write(
"\n"+generateComments()+"\npublic final class "+className()+" extends "+fullClassName(extendsType)+" implements tom.library.sl.Visitable "+generateInterface()+" {\n  "+generateBlock()+"\n  private static String symbolName = \""+className()+"\";\n"




);


  if(slotList.length() > 0) {
    writer.write("\n\n  private "+className()+"() {}\n  private int hashCode;\n  private static "+className()+" gomProto = new "+className()+"();\n  "




); 
    if(variadicconstructor) {
      /* get the domain type of the constructor */
      {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch555_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch555_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch555_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domainclass= tomMatch555_5.getDomain() ;

          if(!getGomEnvironment().isBuiltinClass(tom_domainclass)) {
            writer.write("\n   private "+fullClassName(tom_domainclass)+"[] children;\n                "

);
          } else {
            writer.write("\n   private tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(fullClassName(tom_domainclass))+">[] children;\n   "

);
          }
        }}}}}}}

    } 
  } else {
    writer.write("\n\n  private "+className()+"() {}\n  private static int hashCode = hashFunction();\n  private static "+className()+" gomProto = ("+className()+") factory.build(new "+className()+"());\n  "




);
  }
} else {
  writer.write(
"\n"+generateComments()+"\npublic final class "+className()+" extends "+fullClassName(extendsType)+" implements Cloneable, tom.library.sl.Visitable "+generateInterface()+" {\n  "+generateBlock()+"\n  private static String symbolName = \""+className()+"\";\n"




);
    if(variadicconstructor) {
      /* get the domain type of the constructor */
      {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch556_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch556_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch556_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domainclass= tomMatch556_5.getDomain() ;

       if(!getGomEnvironment().isBuiltinClass(tom_domainclass)) {
          writer.write("\n   private "+fullClassName(tom_domainclass)+"[] children;\n          "

);
        } else {
          writer.write("\n   private tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(fullClassName(tom_domainclass))+">[] children;\n          "

);
        }
        }}}}}}}

    }

// generate a private constructor that takes as arguments the operator arguments
  writer.write("\n\n  private "+className()+"("+childListWithType(slotList)+") {\n  "


);
  generateMembersInit(writer);
  writer.write("\n  }\n  "

);
}

  if(hooks.containsTomCode()) {
    mapping.generate(writer);
  }
  generateMembers(writer);
  generateBody(writer);
  writer.write("\n}\n"

);
  }

  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    if(!comments.equals("")) {
      writer.write("\n  "+generateComments()+"\n        "

);
    } else {
    writer.write("\n  /**\n   * Constructor that builds a term rooted by "+className()+"\n   *\n   * @return a term rooted by "+className()+"\n   */\n"





);
    }
generateConstructor(writer);

if(slotList.length()>0) {

if(maximalsharing) {
writer.write("\n  /**\n   * Initializes attributes and hashcode of the class\n   *\n   * @param "+childListOnePerLine(slotList)+"\n   * @param hashCode hashCode of "+className()+"\n   */\n  private void init("+childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":", ") +"int hashCode) {\n"







);
generateMembersInit(writer);
writer.write("\n    this.hashCode = hashCode;\n  }\n\n  /**\n   * Initializes attributes and hashcode of the class\n   *\n   * @param "+childListOnePerLine(slotList)+"\n   */\n  private void initHashCode("+childListWithType(slotList)+") {\n"









);
generateMembersInit(writer);
writer.write("\n    this.hashCode = hashFunction();\n  }\n"


);
}

writer.write("\n  /* name and arity */\n\n  /**\n   * Returns the name of the symbol\n   *\n   * @return the name of the symbol\n   */\n  @Override\n  public String symbolName() {\n    return \""+className()+"\";\n  }\n\n  /**\n   * Returns the arity of the symbol\n   *\n   * @return the arity of the symbol\n   */\n  private int getArity() {\n    return "+slotList.length()+";\n  }\n"




















);


if(maximalsharing) {
  writer.write("\n  /**\n   * Copy the object and returns the copy\n   *\n   * @return a clone of the SharedObject\n   */"




);
if(multithread) {
  writer.write("\n  public shared.SharedObject duplicate() {\n    // the proto is a fresh object: no need to clone it again\n    return this;\n  }\n  "




);
} else {
  writer.write("\n  public shared.SharedObject duplicate() {\n    "+className()+" clone = new "+className()+"();\n    clone.init("+childList(slotList) + (slotList.isEmptyConcSlotField()?"":", ") +"hashCode);\n    return clone;\n  }\n  "





);
 }
}

} else {
    // case: constant
writer.write("\n  /* name and arity */\n\n  /**\n   * Returns the name of the symbol\n   *\n   * @return the name of the symbol\n   */\n  @Override\n  public String symbolName() {\n    return \""+className()+"\";\n  }\n\n  /**\n   * Returns the arity of the symbol\n   *\n   * @return arity of the symbol\n   */\n  private static int getArity() {\n    return 0;\n  }\n\n"





















);

if(maximalsharing) {
writer.write("\n  /**\n   * Copy the object and returns the copy\n   *\n   * @return a clone of the SharedObject\n   */\n  public shared.SharedObject duplicate() {\n    // the proto is a constant object: no need to clone it\n    return this;\n    //return new "+className()+"();\n  }\n"










);
}

  }

  /*
   * Generate a toStringBuilder method if the operator is not associative
   */
  if(sortName == extendsType) {
writer.write("\n  /**\n   * Appends a string representation of this term to the buffer given as argument.\n   *\n   * @param buffer the buffer to which a string represention of this term is appended.\n   */\n  @Override\n  public void toStringBuilder(java.lang.StringBuilder buffer) {\n    buffer.append(\""+className()+"(\");\n    "+toStringChildren("buffer")+"\n    buffer.append(\")\");\n  }\n"











);
  }

String equality = (maximalsharing)?"ao == this":"this.equals(ao)";
writer.write("\n\n  /**\n   * Compares two terms. This functions implements a total lexicographic path ordering.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare children\n   */\n  @Override\n  public int compareToLPO(Object o) {\n    /*\n     * We do not want to compare with any object, only members of the module\n     * In case of invalid argument, throw a ClassCastException, as the java api\n     * asks for it\n     */\n    "+fullClassName(abstractType)+" ao = ("+fullClassName(abstractType)+") o;\n    /* return 0 for equality */\n    if("+equality+") { return 0; }\n    /* compare the symbols */\n    int symbCmp = this.symbolName().compareTo(ao.symbolName());\n    if(symbCmp != 0) { return symbCmp; }\n    /* compare the children */\n    "+genCompareChildren("ao","compareToLPO")+"\n    throw new RuntimeException(\"Unable to compare\");\n  }\n"



























);

if(maximalsharing) {
writer.write("\n /**\n   * Compares two terms. This functions implements a total order.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare children\n   */\n  @Override\n  public int compareTo(Object o) {\n    /*\n     * We do not want to compare with any object, only members of the module\n     * In case of invalid argument, throw a ClassCastException, as the java api\n     * asks for it\n     */\n    "+fullClassName(abstractType)+" ao = ("+fullClassName(abstractType)+") o;\n    /* return 0 for equality */\n    if(ao == this) { return 0; }\n    /* use the hash values to discriminate */\n\n    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }\n\n    /* If not, compare the symbols : back to the normal order */\n    int symbCmp = this.symbolName().compareTo(ao.symbolName());\n    if(symbCmp != 0) { return symbCmp; }\n    /* last resort: compare the children */\n    "+genCompareChildren("ao","compareTo")+"\n    throw new RuntimeException(\"Unable to compare\");\n  }\n\n //shared.SharedObject\n  /**\n   * Returns hashCode\n   *\n   * @return hashCode\n   */\n  @Override\n  public final int hashCode() {\n    return hashCode;\n  }\n\n  /**\n   * Checks if a SharedObject is equivalent to the current object\n   *\n   * @param obj SharedObject to test\n   * @return true if obj is a "+className()+" and its members are equal, else false\n   */\n  public final boolean equivalent(shared.SharedObject obj) {\n    if(obj instanceof "+className()+") {\n"+generateMembersEqualityTest("peer")+"\n    }\n    return false;\n  }\n\n"























































);
} else {
  //XXX: compareTo must be correctly implemented
writer.write("\n  /**\n   * Compares two terms. This functions implements a total order.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare children\n   */\n  @Override\n  public int compareTo(Object o) {\n    throw new UnsupportedOperationException(\"Unable to compare\");\n  }\n\n  /**\n   * Clones the object\n   *\n   * @return the copy\n   */\n  @Override\n  public Object clone() {\n"





















);

SlotFieldList slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write("\n      return new "+className()+"();\n  }\n      "


);
} else {

SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

if(getGomEnvironment().isBuiltinClass(head.getDomain())) {
  writer.write("\n      return new "+className()+"("+getMethod(head)+"()"
);

} else {
  writer.write("\n      return new "+className()+"( ("+fullClassName(head.getDomain())+") "+getMethod(head)+"().clone()"
);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if(getGomEnvironment().isBuiltinClass(head.getDomain())) {
   writer.write(","+getMethod(head)+"()");
  } else {
  writer.write(",("+fullClassName(head.getDomain())+") "+getMethod(head)+"().clone()");
  }
}
writer.write(");\n}");
}

writer.write("\n  /**\n   * Checks if an object is strictly equal to the current object\n   *\n   * @param o object to compare\n   * @return true if each member is equal, else false\n   */\n  @Override\n  public final boolean deepEquals(Object o) {\n    if( this == o) {\n      return true;\n    }\n    if(o instanceof "+className()+") {\n      "+className()+" typed_o = ("+className()+") o;\n"













);

slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write("\n      return true;\n      "

);
} else {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if(getGomEnvironment().isBuiltinClass(head.getDomain())) {
  writer.write("\n      return "+fieldName(head.getName())+" == typed_o."+getMethod(head)+"()\n      "

);

} else {
  writer.write("\n      return "+fieldName(head.getName())+".deepEquals(typed_o."+getMethod(head)+"())\n      "

);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if(getGomEnvironment().isBuiltinClass(head.getDomain())) {
    writer.write("\n        && "+fieldName(head.getName())+" == typed_o."+getMethod(head)+"()\n        "

);

  } else {
    writer.write("\n      && "+fieldName(head.getName())+".deepEquals(typed_o."+getMethod(head)+"())\n      "

);
  }
}
writer.write(";");
}
writer.write("\n    }\n    return false;\n    }\n  "



);
}

writer.write("\n   //"+className(sortName)+" interface\n  /**\n   * Returns true if the term is rooted by the symbol "+className.getName()+"\n   *\n   * @return true, because this is rooted by "+className.getName()+"\n   */\n  @Override\n  public boolean "+isOperatorMethod(className)+"() {\n    return true;\n  }\n  "










);

generateGetters(writer);

    writer.write("\n  /* AbstractType */\n  /**\n   * Returns an ATerm representation of this term.\n   *\n   * @return an ATerm representation of this term.\n   */\n  @Override\n  public aterm.ATerm toATerm() {\n    aterm.ATerm res = super.toATerm();\n    if(res != null) {\n      // the super class has produced an ATerm (may be a variadic operator)\n      return res;\n    }\n    return atermFactory.makeAppl(\n      atermFactory.makeAFun(symbolName(),getArity(),false),\n      new aterm.ATerm[] {"+generateToATermChildren()+"});\n  }\n\n  /**\n   * Apply a conversion on the ATerm contained in the String and returns a "+fullClassName(sortName)+" from it\n   *\n   * @param trm ATerm to convert into a Gom term\n   * @param atConv ATerm Converter used to convert the ATerm\n   * @return the Gom term\n   */\n  public static "+fullClassName(sortName)+" fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {\n    trm = atConv.convert(trm);\n    if(trm instanceof aterm.ATermAppl) {\n      aterm.ATermAppl appl = (aterm.ATermAppl) trm;\n      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {\n        return make(\n"+generatefromATermChildren("appl","atConv")+"\n        );\n      }\n    }\n    return null;\n  }\n"





































);

    writer.write("\n  /* Visitable */\n  /**\n   * Returns the number of children of the term\n   *\n   * @return the number of children of the term\n   */\n  public int getChildCount() {\n    return "+visitableCount()+";\n  }\n\n  /**\n   * Returns the child at the specified index\n   *\n   * @param index index of the child to return; must be\n             nonnegative and less than the childCount\n   * @return the child at the specified index\n   * @throws IndexOutOfBoundsException if the index out of range\n   */\n  public tom.library.sl.Visitable getChildAt(int index) {\n    "+getchildat()+"\n }\n\n  /**\n   * Set the child at the specified index\n   *\n   * @param index index of the child to set; must be\n             nonnegative and less than the childCount\n   * @param v child to set at the specified index\n   * @return the child which was just set\n   * @throws IndexOutOfBoundsException if the index out of range\n   */\n  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {\n    "+setchildat("v")+"\n  }\n\n  /**\n   * Set children to the term\n   *\n   * @param children array of children to set\n   * @return an array of children which just were set\n   * @throws IndexOutOfBoundsException if length of \"children\" is different than "+slotList.length()+"\n   */\n  @SuppressWarnings(\"unchecked\")\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    if(children.length == getChildCount() "+arrayCheck("children")+") {\n      "+arrayMake("children")+"\n    } else {\n      throw new IndexOutOfBoundsException();\n    }\n  }\n\n  /**\n   * Returns the whole children of the term\n   *\n   * @return the children of the term\n   */\n  public tom.library.sl.Visitable[] getChildren() {\n    "+getchildren(slotList)+"\n  }\n"



























































);

if(maximalsharing) {
  // OLD VERSION
    writer.write("\n    /**\n     * Compute a hashcode for this term.\n     * (for internal use)\n     *\n     * @return a hash value\n     */\n  protected"+((slotList.length()==0)?" static":"")+" int hashFunction() {\n    int a, b, c;\n    /* Set up the internal state */\n    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */\n    b = ("+shared.HashFunctions.stringHashFunction(fullClassName(),slotList.length())+"<<8);\n    c = getArity();\n    /* -------------------------------------- handle most of the key */\n    /* ------------------------------------ handle the last 11 bytes */\n"














);
generateHashArgs(writer);
writer.write("\n    a -= b; a -= c; a ^= (c >> 13);\n    b -= c; b -= a; b ^= (a << 8);\n    c -= a; c -= b; c ^= (b >> 13);\n    a -= b; a -= c; a ^= (c >> 12);\n    b -= c; b -= a; b ^= (a << 16);\n    c -= a; c -= b; c ^= (b >> 5);\n    a -= b; a -= c; a ^= (c >> 3);\n    b -= c; b -= a; b ^= (a << 10);\n    c -= a; c -= b; c ^= (b >> 15);\n    /* ------------------------------------------- report the result */\n    return c;\n  }\n"












);
}

if(false && maximalsharing) {
  // NEW VERSION: http://burtleburtle.net/bob/c/lookup3.c
  // seems to be a bit slower than the OLD version
  int length = slotList.length();
    writer.write("\n    /**\n     * Compute a hashcode for this term.\n     * (for internal use)\n     *\n     * @return a hash value\n     */\n  protected"+((length==0)?" static":"")+" int hashFunction() {\n    int a, b, c;\n    /* Set up the internal state */\n    a = b = c =\n    0xdeadbeef + (getArity()<<2) +\n    ("+shared.HashFunctions.stringHashFunction(fullClassName(),length)+"<<8);\n    /* -------------------------------------- handle most of the key */\n    /* ------------------------------------ handle the last 11 bytes */\n"














);
generateHashArgsLookup3(writer);
  if(length>0 && (length%3)>0 ) {
writer.write("\n    // final(a,b,c)\n    c ^= b; c -= (((b)<<(14)) | ((b)>>(32-(14))));\n    a ^= c; a -= (((c)<<(11)) | ((c)>>(32-(11))));\n    b ^= a; b -= (((a)<<(25)) | ((a)>>(32-(25))));\n    c ^= b; c -= (((b)<<(16)) | ((b)>>(32-(16))));\n    a ^= c; a -= (((c)<<(4)) | ((c)>>(32-(4))));\n    b ^= a; b -= (((a)<<(14)) | ((a)>>(32-(14))));\n    c ^= b; c -= (((b)<<(24)) | ((b)>>(32-(24))));\n"








);
  }

writer.write("\n    /* ------------------------------------------- report the result */\n    return c;\n  }\n"



);
}

  generateEnum(writer);

}

private void generateEnum(java.io.Writer writer) throws java.io.IOException {
  final String P = "tom.library.enumerator.";
  if(slotList.length() == 0) {
    String E = "tom.library.enumerator.Enumeration<" + fullClassName(sortName) + ">";
    writer.write(
"\n  /**\n    * function that returns functional version of the current operator\n    * need for initializing the Enumerator\n    */\n  public static "+P+"F<"+E+", "+E+"> funMake() {\n    return new "+P+"F<"+E+", "+E+">() {\n      public "+E+" apply(final "+E+" e) {\n        return "+P+"Enumeration.singleton(("+fullClassName(sortName)+") make());\n      }\n    };\n  }\n"











);
  } else {
    final F<Integer,String> genMake = new F<Integer,String>() {
      public String apply(Integer index) {
        SlotField[] array = slotList.getCollectionConcSlotField().toArray(new SlotField[1]);
        String res = "make(";
        for(int i=1 ; i<index ; i++) { 
          String arg = "t"+i;
          ClassName domain = array[i-1].getDomain();
          String sort = fullClassName(domain);
          if(getGomEnvironment().isBuiltinClass(domain) && !sort.equals(convertBuiltinType(sort))) { // to avoid the aterm case
            arg = convertBuiltinType(sort)+".valueOf(" + arg + ")";
          }
          res += arg;
          if(i<index-1) { res += ","; }
        }
        res += ")";
        return res; 
      }
    };

    F<Integer,String> genBody = new F<Integer,String>() {
      public String apply(Integer index) {
        String f = "(" + buildFunctionDomain(false,slotList,sortName) + ") "+ buildNestedFunction(false,slotList,sortName,1,genMake);
        String sapply = P+"Enumeration.singleton("+f+")";
        for(int i=1 ; i<index ; i++) { sapply = ""+P+"Enumeration.apply("+sapply+",t"+i+")"; }
        if(index>0) {
          sapply = sapply + ".pay()";
        }
        return sapply;
      }
    };

    writer.write(
"\n  /**\n    * function that returns functional version of the current operator\n    * need for initializing the Enumerator\n    */\n  public static "+buildFunctionDomain(true,slotList,sortName)+" funMake() {\n    return "+buildNestedFunction(true,slotList,sortName,1,genBody)+";\n  }\n"







);
  }
}

  /*
   * given a domain and a codmain
   * generate  F<A, F<TTree, F<TTree, TTree>>>
   *        or F<E<A>, F<E<TTree>, F<E<TTree>, E<TTree>>>>
   */
  private String buildFunctionDomain(boolean withEnumeration, SlotFieldList slots,ClassName codomain) {
    // [A,Tree,Tree] Tree
    // F<A, F<TTree, F<TTree, TTree>>>
    String F = "tom.library.enumerator.F<";
    String E = "tom.library.enumerator.Enumeration<";
    if(slots.isEmptyConcSlotField()) {
      if(withEnumeration) {
        return E+fullClassName(codomain)+">";
      } else {
        return fullClassName(codomain);
      }
    } else {
      SlotField head = slots.getHeadConcSlotField();
      SlotFieldList tail = slots.getTailConcSlotField();
      String domain = convertBuiltinType(fullClassName(head.getDomain()));
      if(withEnumeration) {
        return F+E+domain+">,"+buildFunctionDomain(withEnumeration,tail,codomain)+">";
      } else {
        return F+domain+","+buildFunctionDomain(withEnumeration,tail,codomain)+">";
      }
    }
  }

  /*
   * given a domain and a codmain
   * generate nested new F() { apply() { ... } }
   */
  private String buildNestedFunction(boolean withEnumeration,SlotFieldList slots,ClassName codomain, int index, F<Integer,String> genBody) {
    // [A,Tree,Tree] Tree
    // F<A, F<TTree, F<TTree, TTree>>>
    if(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      SlotFieldList tail = slots.getTailConcSlotField();
      String domain = convertBuiltinType(fullClassName(head.getDomain()));
      String E = "tom.library.enumerator.Enumeration<";
      if(withEnumeration) {
        domain = E+domain+">";
      }
      return "\n        new "+buildFunctionDomain(withEnumeration,slots,codomain)+"() {\n          public "+buildFunctionDomain(withEnumeration,tail,codomain)+" apply(final "+domain+" t"+index+") {\n            return "+buildNestedFunction(withEnumeration,tail, codomain, index+1, genBody)+";\n          }\n        }"




;
    } else {
      return genBody.apply(index);
    }
  }

  private String convertBuiltinType(String domain) {
    if(domain.equals("int")) {
      return "java.lang.Integer";
    } else if(domain.equals("long")) {
      return "java.lang.Long";
    } else if(domain.equals("char")) {
      return "java.lang.Character";
    } else if(domain.equals("boolean")) {
      return "java.lang.Boolean";
    } else if(domain.equals("float")) {
      return "java.lang.Float";
    } else if(domain.equals("double")) {
      return "java.lang.Double";
    }

    return domain;
  }
  private String generateComments() {
    if(!comments.equals("") && comments.contains("@param")) {
      // "@param chaine " -> "@param _chaine "
      return comments.replaceAll("@param ","@param _");
    } else {
      return comments;
    }
  }

  private void generateMembers(java.io.Writer writer) throws java.io.IOException {
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch557_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch557_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch557_9= tomMatch557_end_4.getHeadConcSlotField() ;if ( (tomMatch557_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch557_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        writer.write("  private ");
        writer.write(fullClassName( tomMatch557_9.getDomain() ));
        writer.write(" ");
        writer.write(fieldName( tomMatch557_9.getName() ));
        writer.write(";\n");
      }}}if ( tomMatch557_end_4.isEmptyConcSlotField() ) {tomMatch557_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch557_end_4= tomMatch557_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch557_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  }

  private void generateMembersInit(java.io.Writer writer) throws java.io.IOException {
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch558_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch558_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch558_9= tomMatch558_end_4.getHeadConcSlotField() ;if ( (tomMatch558_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch558_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_fieldName= tomMatch558_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch558_9.getDomain() ;

        writer.write("    this.");
        writer.write(fieldName(tom_fieldName));
        writer.write(" = ");
        writer.write(fieldName(tom_fieldName));
        if(getGomEnvironment().isBuiltinClass(tom_domain) && tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
          writer.write(".intern()");
        }
        writer.write(";\n");
      }}}if ( tomMatch558_end_4.isEmptyConcSlotField() ) {tomMatch558_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch558_end_4= tomMatch558_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch558_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  }

  private void generateGetters(java.io.Writer writer) throws java.io.IOException {
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      writer.write("\n  /**\n   * Returns the attribute "+slotDomain(head)+"\n   *\n   * @return the attribute "+slotDomain(head)+"\n   */\n  @Override\n  public "+slotDomain(head)+" "+getMethod(head)+"() {\n    return "+fieldName(head.getName())+";\n  }\n\n  /**\n   * Sets and returns the attribute "+fullClassName(sortName)+"\n   *\n   * @param set_arg the argument to set\n   * @return the attribute "+slotDomain(head)+" which just has been set\n   */"















);
      if(maximalsharing) {
        writer.write("\n  @Override\n  public "+fullClassName(sortName)+" "+setMethod(head)+"("+slotDomain(head)+" set_arg) {\n    return make("+generateMakeArgsFor(head,"set_arg")+");\n  }\n  "




);
      } else {
        writer.write("\n  @Override\n  public "+fullClassName(sortName)+" "+setMethod(head)+"("+slotDomain(head)+" set_arg) {\n    "+fieldName(head.getName())+" = set_arg;\n    return this;\n  }"




);
      }

      if(jmicompatible) {
        // generate getters and setters where slot-names are normalized
        if(!getMethod(head,true).equals(getMethod(head))) {
      writer.write("\n  public "+slotDomain(head)+" "+getMethod(head,true)+"() {\n    return "+getMethod(head)+"();\n  }\n  "



);
        }
        if(!setMethod(head,true).equals(setMethod(head))) {
      writer.write("\n  public "+fullClassName(sortName)+" "+setMethod(head,true)+"("+slotDomain(head)+" set_arg) {\n    return "+setMethod(head)+"(set_arg);\n  }\n  "



);
        }
      }
    }
  }

  private String generateToATermChildren() {
    StringBuilder res = new StringBuilder();
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      if(res.length()!=0) {
        res.append(", ");
      }
      toATermSlotField(res,head);
    }
    return res.toString();
  }

  private String generatefromATermChildren(String appl, String atConv) {
    StringBuilder res = new StringBuilder();
    int index = 0;
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      if(res.length()!=0) {
        res.append(", ");
      }
      fromATermSlotField(res,head, appl+".getArgument("+index+")",atConv);
      index++;
    }
    return res.toString();
  }

  private String fieldName(String fieldName) {
    return "_"+fieldName;
  }

  /**
   * This method is used to generate a part of comments of init method
   *
   * @param slots fields of the class being generated
   * @return a String composed of one line per field
   */
  private String childListOnePerLine(SlotFieldList slots) {
    String str = childList(slots);
    return str.replaceAll(", ", "\n   * @param");
  }


  private String childListWithType(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      {{if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )head)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

          if(res.length()!=0) {
            res.append(", ");
          }
          res.append(fullClassName( (( tom.gom.adt.objects.types.SlotField )head).getDomain() ));
          res.append(" ");
          res.append(fieldName( (( tom.gom.adt.objects.types.SlotField )head).getName() ));
        }}}}}

    }
    return res.toString();
  }
  private String unprotectedChildListWithType(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      {{if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )head)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

          if(res.length()!=0) {
            res.append(", ");
          }
          res.append(fullClassName( (( tom.gom.adt.objects.types.SlotField )head).getDomain() ));
          res.append(" ");
          res.append( (( tom.gom.adt.objects.types.SlotField )head).getName() );
        }}}}}

    }
    return res.toString();
  }
  private String childList(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      {{if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )head)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

          if(res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(fieldName( (( tom.gom.adt.objects.types.SlotField )head).getName() ));
        }}}}}

    }
    return res.toString();
  }
  private String unprotectedChildList(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      {{if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )head)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

          if(res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append( (( tom.gom.adt.objects.types.SlotField )head).getName() );
        }}}}}

    }
    return res.toString();
  }
  private String generateMembersEqualityTest(String peer) {
    StringBuilder res = new StringBuilder();
    if(!slotList.isEmptyConcSlotField()) {
      res.append("\n      "+className()+" peer = ("+className()+") obj;"
);;
    }
    res.append("\n      return "
);
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch563_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch563_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch563_8= tomMatch563_end_4.getHeadConcSlotField() ;if ( (tomMatch563_8 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch563_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_fieldName= tomMatch563_8.getName() ;

        res.append(fieldName(tom_fieldName));
        res.append("==");
        res.append(peer);
        res.append(".");
        res.append(fieldName(tom_fieldName));
        res.append(" && ");
      }}}if ( tomMatch563_end_4.isEmptyConcSlotField() ) {tomMatch563_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch563_end_4= tomMatch563_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch563_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    res.append("true;"); // to handle the "no children" case
    return res.toString();
  }

  private String getchildat() {
    if(variadicconstructor) {
      // use the methods of the Collection interface
      return "return getChildren()[index];";
    } else {
      StringBuilder res = new StringBuilder("    switch(index) {\n");
      int index = 0;
      {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch564_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch564_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch564_9= tomMatch564_end_4.getHeadConcSlotField() ;if ( (tomMatch564_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch564_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_fieldName= tomMatch564_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch564_9.getDomain() ;

          if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
            res.append("      case ");
            res.append(index);
            res.append(": return ");
            res.append(fieldName(tom_fieldName));
            res.append(";\n");
            index++;
          } else {
            res.append("      case ");
            res.append(index);
            res.append(": return new tom.library.sl.VisitableBuiltin<");
            res.append(primitiveToReferenceType(fullClassName(tom_domain)));
            res.append(">(");
            res.append(fieldName(tom_fieldName));
            res.append(");\n");
            index++;
          }
        }}}if ( tomMatch564_end_4.isEmptyConcSlotField() ) {tomMatch564_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch564_end_4= tomMatch564_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch564_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

      res.append("      default: throw new IndexOutOfBoundsException();\n }");
      return res.toString();
    }
  }

  private String getchildren(SlotFieldList slots) {
    if(variadicconstructor) {
      // use the methods of the Collection interface
      if(className.getName().startsWith("Cons")) {
        {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch565_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch565_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch565_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domainclass= tomMatch565_5.getDomain() ;

            if(!getGomEnvironment().isBuiltinClass(tom_domainclass)) {
              return "\n        if(children == null) {\n          children = toArray(new "+fullClassName(tom_domainclass)+"[]{});\n        }\n        return java.util.Arrays.copyOf(children,children.length);\n      "




;
            } else {
              String reference_type = primitiveToReferenceType(fullClassName(tom_domainclass));
              return "\n        if(children == null) {\n          "+reference_type+"[] object_children = toArray(new "+reference_type+"[]{});\n          children = new tom.library.sl.VisitableBuiltin[object_children.length];\n          for (int i=0; i<object_children.length; i++) {\n              children[i] = new tom.library.sl.VisitableBuiltin(object_children[i]);\n          }\n        }\n        return java.util.Arrays.copyOf(children,children.length);\n      "








;
            }
          }}}}}}}

      } 
      // default case: empty constructor
      return "\n        return new tom.library.sl.Visitable[]{};\n      "

;
    } else {
      StringBuilder res = new StringBuilder();
      res.append("return new tom.library.sl.Visitable[] {");
      boolean is_first_slot = true;
      while(!slots.isEmptyConcSlotField()) {
        SlotField head = slots.getHeadConcSlotField();
        slots = slots.getTailConcSlotField();
        {{if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )head)) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )head).getDomain() ; String  tom_name= (( tom.gom.adt.objects.types.SlotField )head).getName() ;

            if(is_first_slot) {
              is_first_slot =  false;
            } else {
              res.append(", ");
            }
            res.append(" ");
            if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
              res.append(fieldName(tom_name));
            } else {
              res.append("new tom.library.sl.VisitableBuiltin<");
              res.append(primitiveToReferenceType(fullClassName(tom_domain)));
              res.append(">(");
              res.append(fieldName(tom_name));
              res.append(")");
            }
          }}}}}

      }
      res.append("};");
      return res.toString();
    }
  }


  private String visitableCount() {
    if(className().equals("ConsPath"+sortName.getName())) {
      return "0";
    } else if(variadicconstructor) {
      // use the methods of the Collection interface
      return "getChildren().length";
    } else { return ""+slotList.length();
    }
  }

  private String arrayMake(String arrayName) {
    if(variadicconstructor) {
      if(className.getName().startsWith("Cons")) {
        /* get the domain type of the constructor */
        {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch567_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch567_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch567_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domainclass= tomMatch567_5.getDomain() ;

            if(!getGomEnvironment().isBuiltinClass(tom_domainclass)) {
              return "\n               "+fullClassName(tom_domainclass)+"[] typed_children = new "+fullClassName(tom_domainclass)+"[children.length];\n              for (int i=0; i<children.length; i++) {\n                typed_children[i] = ("+fullClassName(tom_domainclass)+") children[i]; \n              }\n              return fromArray(typed_children);\n              "





;
            } else {
              String builtin_type = fullClassName(tom_domainclass);
              return "\n               "+builtin_type+"[] builtin_children = new "+builtin_type+"[children.length];\n               for (int i=0; i<children.length; i++) {\n                 builtin_children[i] = ((tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(builtin_type)+">) children[i]).getBuiltin(); \n               }\n               return fromArray(builtin_children);\n              "





;
            }
          }}}}}}}

      } 
      /* Empty constructor */
      return "return this;";
    } else { 
      StringBuilder res = new StringBuilder("return make(");
      int index = 0;
      {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch568_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch568_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch568_8= tomMatch568_end_4.getHeadConcSlotField() ;if ( (tomMatch568_8 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch568_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch568_8.getDomain() ;

          if(index>0) { res.append(", "); }
          if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
            res.append("(");
            res.append(fullClassName(tom_domain));
            res.append(") ");
            res.append(arrayName);
            res.append("[");
            res.append(index);
            res.append("]");
          } else {
            res.append("((tom.library.sl.VisitableBuiltin<");
            res.append(primitiveToReferenceType(fullClassName(tom_domain)));
            res.append(">)");
            res.append(arrayName);
            res.append("[");
            res.append(index);
            res.append("])");
            res.append(".getBuiltin()");
          }
          index++;
        }}}if ( tomMatch568_end_4.isEmptyConcSlotField() ) {tomMatch568_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch568_end_4= tomMatch568_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch568_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

      res.append(");");
      return res.toString();
    }
  }

  private String arrayCheck(String arrayName) {
    StringBuilder res = new StringBuilder();
    if(variadicconstructor) {
      if(className.getName().startsWith("Cons")) {
        /* get the domain type of the constructor */
        {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch569_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch569_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch569_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

              //TODO test that each child is of type domain
              // this has to be done dynamically (we do not know statically the
              //number of children)
              return "";
            }}}}}}}

      } else {
        /* empty constructor */ 
        return "";
      }
    } else {
      int index = 0;
      {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch570_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch570_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch570_8= tomMatch570_end_4.getHeadConcSlotField() ;if ( (tomMatch570_8 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch570_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch570_8.getDomain() ;

          if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
            res.append(" && "+arrayName+"["+index+"] instanceof "+fullClassName(tom_domain)+"");
          } else {
            res.append(" && "+arrayName+"["+index+"] instanceof tom.library.sl.VisitableBuiltin");
          }
          index++;
        }}}if ( tomMatch570_end_4.isEmptyConcSlotField() ) {tomMatch570_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch570_end_4= tomMatch570_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch570_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    }
    return res.toString();
  }

private String setchildat(String argName) {
  if(variadicconstructor) {
    if(className.getName().startsWith("Cons")) {
    /* get the domain type of the constructor */
     {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )slotList).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch571_5= (( tom.gom.adt.objects.types.SlotFieldList )slotList).getHeadConcSlotField() ;if ( (tomMatch571_5 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch571_5) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domainclass= tomMatch571_5.getDomain() ;

        String domain = fullClassName(tom_domainclass); 
        StringBuilder res = new StringBuilder();
        String class_name = fullClassName(tom_domainclass);
            if(!getGomEnvironment().isBuiltinClass(tom_domainclass)) {
              res.append("\n      tom.library.sl.Visitable[] children = getChildren();\n      "+class_name+"[] new_children = new "+class_name+"[children.length];\n      for(int i =0; i<children.length; i++) {\n        new_children[i] = (("+class_name+") children[i]); \n      }\n     new_children[index] = ("+domain+") "+argName+";\n     return fromArray(new_children);\n                  "







);
            } else {
              res.append("\n      tom.library.sl.Visitable[] children = getChildren();\n      "+class_name+"[] new_children = new "+class_name+"[children.length];\n      for(int i =0; i<children.length; i++) {\n        new_children[i] = ((tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(class_name)+">) children[i]).getBuiltin(); \n      }\n      new_children[index] = ((tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(class_name)+">) "+argName+").getBuiltin();\n      return fromArray(new_children);\n                  "







);
            }
            return res.toString();
      }}}}}}}

    }
    /* Empty constructor */
    return "throw new IndexOutOfBoundsException();";
  } else {
    StringBuilder res = new StringBuilder("    switch(index) {\n");
    int index = 0;
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch572_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch572_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch572_7= tomMatch572_end_4.getHeadConcSlotField() ;if ( (tomMatch572_7 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch572_7) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
        index++;
      }}}if ( tomMatch572_end_4.isEmptyConcSlotField() ) {tomMatch572_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch572_end_4= tomMatch572_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch572_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    res.append("      default: throw new IndexOutOfBoundsException();\n }");
    return res.toString();
  }
}

private String generateMakeArgsFor(int argIndex, String argName) {
  StringBuilder res = new StringBuilder();
  int index = 0;
  {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch573_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch573_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch573_9= tomMatch573_end_4.getHeadConcSlotField() ;if ( (tomMatch573_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch573_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch573_9.getDomain() ;

      if(index>0) { res.append(", "); }
      if(getGomEnvironment().isBuiltinClass(tom_domain)) {
        res.append(getMethod( tomMatch573_end_4.getHeadConcSlotField() ));
        res.append("()");
      } else {
        if(index != argIndex) {
          res.append(fieldName( tomMatch573_9.getName() ));
        } else {
          res.append("(");
          res.append(fullClassName(tom_domain));
          res.append(") ");
          res.append(argName);
        }
      }
      index++;
    }}}if ( tomMatch573_end_4.isEmptyConcSlotField() ) {tomMatch573_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch573_end_4= tomMatch573_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch573_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  return res.toString();
}
private String generateMakeArgsFor(SlotField slot, String argName) {
  StringBuilder res = new StringBuilder();
  int fullindex = 0;
  {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch574_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch574_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch574_8= tomMatch574_end_4.getHeadConcSlotField() ;if ( (tomMatch574_8 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch574_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

      if(fullindex>0) { res.append(", "); }
      if( tomMatch574_end_4.getHeadConcSlotField() == slot) {
        res.append(argName);
      } else {
        res.append(fieldName( tomMatch574_8.getName() ));
      }
      fullindex++;
    }}}if ( tomMatch574_end_4.isEmptyConcSlotField() ) {tomMatch574_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch574_end_4= tomMatch574_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch574_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  return res.toString();
}

  private String toStringChildren(String buffer) {
    if(0 == slotList.length()) {
      return "";
    }
    StringBuilder res = new StringBuilder();
    SlotFieldList slots = slotList;
		while(!slots.isEmptyConcSlotField()) {
			if(res.length()!=0) {
				res.append(""+buffer+".append(\",\");\n    "
);
			}
			SlotField head = slots.getHeadConcSlotField();
			slots = slots.getTailConcSlotField();
      toStringSlotField(res, head, fieldName(head.getName()), buffer);
		}
    return res.toString();
  }

  private String genCompareChildren(String oldOther, String compareFun) {
    StringBuilder res = new StringBuilder();
    String other = "tco";
    if(!slotList.isEmptyConcSlotField()) {
    res.append(""+className()+" "+other+" = ("+className()+") "+oldOther+";");
    }
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch575_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch575_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch575_9= tomMatch575_end_4.getHeadConcSlotField() ;if ( (tomMatch575_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch575_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_slotName= tomMatch575_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch575_9.getDomain() ;

        if(getGomEnvironment().isBuiltinClass(tom_domain)) {
         if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
             || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
             || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )
             || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
             || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
           res.append("\n    if( this."+fieldName(tom_slotName)+" != "+other+"."+fieldName(tom_slotName)+") {\n      return (this."+fieldName(tom_slotName)+" < "+other+"."+fieldName(tom_slotName)+")?-1:1;\n    }\n"



);
         } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
           res.append("\n    if( this."+fieldName(tom_slotName)+" != "+other+"."+fieldName(tom_slotName)+") {\n      return (!this."+fieldName(tom_slotName)+" && "+other+"."+fieldName(tom_slotName)+")?-1:1;\n    }\n"



);
         } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
           res.append("\n    int "+fieldName(tom_slotName)+"Cmp = (this."+fieldName(tom_slotName)+").compareTo("+other+"."+fieldName(tom_slotName)+");\n    if("+fieldName(tom_slotName)+"Cmp != 0) {\n      return "+fieldName(tom_slotName)+"Cmp;\n    }\n\n"





);
         } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )
             ||tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
           res.append("\n    /* Inefficient total order on ATerm */\n    int "+fieldName(tom_slotName)+"Cmp = ((this."+fieldName(tom_slotName)+").toString()).compareTo(("+other+"."+fieldName(tom_slotName)+").toString());\n    if("+fieldName(tom_slotName)+"Cmp != 0) {\n      return "+fieldName(tom_slotName)+"Cmp;\n    }\n"





);
         } else {
            throw new GomRuntimeException("Builtin "+tom_domain+" not supported");
         }
        } else {
          res.append("\n    int "+fieldName(tom_slotName)+"Cmp = (this."+fieldName(tom_slotName)+")."+compareFun+"("+other+"."+fieldName(tom_slotName)+");\n    if("+fieldName(tom_slotName)+"Cmp != 0) {\n      return "+fieldName(tom_slotName)+"Cmp;\n    }\n"




);
        }
      }}}if ( tomMatch575_end_4.isEmptyConcSlotField() ) {tomMatch575_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch575_end_4= tomMatch575_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch575_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    return res.toString();
  }

  private void generateHashArgs(java.io.Writer writer) throws java.io.IOException {
    int index = slotList.length() - 1;
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch576_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch576_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch576_9= tomMatch576_end_4.getHeadConcSlotField() ;if ( (tomMatch576_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch576_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_slotName= tomMatch576_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch576_9.getDomain() ;

        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        writer.write("    "+accum+" += (");
        if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
          writer.write(fieldName(tom_slotName)+".hashCode()");
        } else {
          if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
            writer.write(fieldName(tom_slotName));
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
            writer.write("("+fieldName(tom_slotName)+"?1:0)");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(tom_slotName)+", "+index+")");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
            writer.write("(int)(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(tom_slotName));
            writer.write(")^(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(tom_slotName));
            writer.write(")>>>32");
            writer.write("))");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )||tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write(fieldName(tom_slotName)+".hashCode()");
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + tom_domain+ " not supported");
          }
        }
        if(shift!=0) { writer.write(" << "+(shift)); }
        writer.write(");\n");
        index--;
      }}}if ( tomMatch576_end_4.isEmptyConcSlotField() ) {tomMatch576_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch576_end_4= tomMatch576_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch576_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  }

  private void generateHashArgsLookup3(java.io.Writer writer) throws java.io.IOException {
    int k=0;
    int index = slotList.length() - 1;
    {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch577_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch577_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch577_9= tomMatch577_end_4.getHeadConcSlotField() ;if ( (tomMatch577_9 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch577_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_slotName= tomMatch577_9.getName() ; tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch577_9.getDomain() ;

        k++;
        switch(k % 3) {
          case 1: writer.write("    a += ("); break;
          case 2: writer.write("    b += ("); break;
          case 0: writer.write("    c += ("); break;
          default:
        }
        if(!getGomEnvironment().isBuiltinClass(tom_domain)) {
          writer.write(fieldName(tom_slotName)+".hashCode()");
        } else {
          if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
              || tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
            writer.write(fieldName(tom_slotName));
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
            writer.write("("+fieldName(tom_slotName)+"?1:0)");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(tom_slotName)+", "+index+")");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
            writer.write("(int)(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(tom_slotName));
            writer.write(")^(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(tom_slotName));
            writer.write(")>>>32");
            writer.write("))");
          } else if(tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )||tom_domain.equals( tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write(fieldName(tom_slotName)+".hashCode()");
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + tom_domain+ " not supported");
          }
        }
        writer.write(");\n");
        if(k % 3 == 0) {
          writer.write("\n    // mix(a,b,c)\n    a -= c;  a ^= (((c)<<(4))  | ((c)>>(32-(4))));  c += b;\n    b -= a;  b ^= (((a)<<(6))  | ((a)>>(32-(6))));  a += c;\n    c -= b;  c ^= (((b)<<(8))  | ((b)>>(32-(8))));  b += a;\n    a -= c;  a ^= (((c)<<(16)) | ((c)>>(32-(16)))); c += b;\n    b -= a;  b ^= (((a)<<(19)) | ((a)>>(32-(19)))); a += c;\n    c -= b;  c ^= (((b)<<(4))  | ((b)>>(32-(4))));  b += a;\n"







);
        }
        index--;
      }}}if ( tomMatch577_end_4.isEmptyConcSlotField() ) {tomMatch577_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch577_end_4= tomMatch577_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch577_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

  }

  public void generateConstructor(java.io.Writer writer) throws java.io.IOException {
    boolean hasHooks = false;
    {{lbl: {if ( (hooks instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch578_end_4=(( tom.gom.adt.objects.types.HookList )hooks);do {{if (!( tomMatch578_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch578_8= tomMatch578_end_4.getHeadConcHook() ;if ( (tomMatch578_8 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch578_8) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) {


      hasHooks = true;
      writer.write("\n    public static "+fullClassName(sortName)+" make("+unprotectedChildListWithType( tomMatch578_8.getHookArguments() )+") {\n  "

);
        SlotFieldList bargs = generateMakeHooks(hooks,null,writer);
        writer.write("\n      return realMake("+unprotectedChildList(bargs)+");\n    }\n  "


);
        break lbl;
      }}}if ( tomMatch578_end_4.isEmptyConcHook() ) {tomMatch578_end_4=(( tom.gom.adt.objects.types.HookList )hooks);} else {tomMatch578_end_4= tomMatch578_end_4.getTailConcHook() ;}}} while(!( (tomMatch578_end_4==(( tom.gom.adt.objects.types.HookList )hooks)) ));}}}}}


    String makeName = "make";
    String visibility = "public";
    if(hasHooks) {
      makeName = "realMake";
      visibility = "private";
    }
    writer.write("\n  "+visibility+" static "+className()+" "+makeName+"("+childListWithType(slotList)+") {\n"

);

    if(! maximalsharing) {
        writer.write("\n    return new "+className()+"("+childList(slotList)+");\n    "

);
    } else {
    if(slotList.length()>0) {
      if(multithread) {
        writer.write("\n    // allocate and object and make duplicate equal identity\n    "+className()+" newProto = new "+className()+"();\n    newProto.initHashCode("+childList(slotList)+");\n    return ("+className()+") factory.build(newProto);\n"




);
      } else {
        writer.write("\n    // use the proto as a model\n    gomProto.initHashCode("+childList(slotList)+");\n    return ("+className()+") factory.build(gomProto);\n"



);
      }
    } else {
        writer.write("\n    return gomProto;\n"

);
    }
    }
    writer.write("\n  }\n"

);


  }


  public SlotFieldList generateMakeHooks(
      HookList other,
      SlotFieldList oArgs, /* will be null if it is the first hook */
      java.io.Writer writer)
    throws java.io.IOException {
    {{if ( (other instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )other)) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )other)) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {if (!( (( tom.gom.adt.objects.types.HookList )other).isEmptyConcHook() )) {boolean tomMatch579_6= false ; tom.gom.adt.objects.types.Hook  tomMatch579_4= (( tom.gom.adt.objects.types.HookList )other).getHeadConcHook() ;if ( (tomMatch579_4 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch579_4) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) {tomMatch579_6= true ;}}if (!(tomMatch579_6)) {

        /* skip non Make hooks */
        return generateMakeHooks( (( tom.gom.adt.objects.types.HookList )other).getTailConcHook() , oArgs, writer);
      }}}}}{if ( (other instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )other)) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )other)) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {if (!( (( tom.gom.adt.objects.types.HookList )other).isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch579_13= (( tom.gom.adt.objects.types.HookList )other).getHeadConcHook() ;if ( (tomMatch579_13 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch579_13) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) { tom.gom.adt.objects.types.SlotFieldList  tom_args= tomMatch579_13.getHookArguments() ;

        /* Rename the previous arguments according to new, if needed */
        if(oArgs != null && oArgs != tom_args) {
          recVarNameRemap(oArgs,tom_args, writer);
        }
        /* Make sure we defeat java dead code detection */
        writer.write("if(true) {");
        CodeGen.generateCode( tomMatch579_13.getCode() ,writer);
        writer.write("}");
        return generateMakeHooks( (( tom.gom.adt.objects.types.HookList )other).getTailConcHook() , tom_args, writer);
      }}}}}}}

    return oArgs;
  }

  private void recVarNameRemap(
      SlotFieldList oargs,
      SlotFieldList nargs,
      java.io.Writer writer)
  throws java.io.IOException {
    {{if ( (oargs instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )oargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )oargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if ( (( tom.gom.adt.objects.types.SlotFieldList )oargs).isEmptyConcSlotField() ) {if ( (nargs instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )nargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )nargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if ( (( tom.gom.adt.objects.types.SlotFieldList )nargs).isEmptyConcSlotField() ) {

        return ;
      }}}}}}}{if ( (oargs instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )oargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )oargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )oargs).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch580_14= (( tom.gom.adt.objects.types.SlotFieldList )oargs).getHeadConcSlotField() ;if ( (tomMatch580_14 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch580_14) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_oargName= tomMatch580_14.getName() ;if ( (nargs instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )nargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )nargs)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( (( tom.gom.adt.objects.types.SlotFieldList )nargs).isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch580_18= (( tom.gom.adt.objects.types.SlotFieldList )nargs).getHeadConcSlotField() ;if ( (tomMatch580_18 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch580_18) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom_nargName= tomMatch580_18.getName() ; tom.gom.adt.objects.types.ClassName  tom_ndomain= tomMatch580_18.getDomain() ;


        if(!( tomMatch580_14.getDomain() ==tom_ndomain)) {
          throw new GomRuntimeException(
              "OperatorTemplate: incompatible args "+
              "should be rejected by typechecker");
        } else if(!tom_oargName.equals(tom_nargName)) {
          /* XXX: the declaration should be omitted if nargName was previously
           * used */
          writer.write("\n    "+fullClassName(tom_ndomain)+" "+tom_nargName+" = "+tom_oargName+";\n"

);
        } /* else nothing to rename */
        recVarNameRemap( (( tom.gom.adt.objects.types.SlotFieldList )oargs).getTailConcSlotField() , (( tom.gom.adt.objects.types.SlotFieldList )nargs).getTailConcSlotField() , writer);
        return;
      }}}}}}}}}}}}

    throw new GomRuntimeException(
        "OperatorTemplate:recVarNameRemap failed " + oargs + " " + nargs);
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    {{if ( (hooks instanceof tom.gom.adt.objects.types.HookList) ) {boolean tomMatch581_9= false ;if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch581_end_4=(( tom.gom.adt.objects.types.HookList )hooks);do {{if (!( tomMatch581_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch581_7= tomMatch581_end_4.getHeadConcHook() ;if ( (tomMatch581_7 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch581_7) instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {tomMatch581_9= true ;}}}if ( tomMatch581_end_4.isEmptyConcHook() ) {tomMatch581_end_4=(( tom.gom.adt.objects.types.HookList )hooks);} else {tomMatch581_end_4= tomMatch581_end_4.getTailConcHook() ;}}} while(!( (tomMatch581_end_4==(( tom.gom.adt.objects.types.HookList )hooks)) ));}if (!(tomMatch581_9)) {

        writer.write("%op "+className(sortName)+" "+className()+"(");
        slotDecl(writer,slotList);
        writer.write(") {\n");
        writer.write("  is_fsym(t) { ($t instanceof "+fullClassName()+") }\n");
        {{if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )slotList)) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch582_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{if (!( tomMatch582_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch582_8= tomMatch582_end_4.getHeadConcSlotField() ;if ( (tomMatch582_8 instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch582_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

            writer.write("  get_slot("+ tomMatch582_8.getName() +", t) ");
            writer.write("{ $t."+getMethod( tomMatch582_end_4.getHeadConcSlotField() )+"() }\n");
          }}}if ( tomMatch582_end_4.isEmptyConcSlotField() ) {tomMatch582_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch582_end_4= tomMatch582_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch582_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

        writer.write("  make(");
        slotArgs(writer,slotList);
        writer.write(") { ");
        writer.write(fullClassName());
        writer.write(".make(");
        slotArgsWithDollar(writer,slotList);
        writer.write(") }\n");
        writer.write("}\n");
        writer.write("\n");
        return;
      }}}{if ( (hooks instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )hooks)) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch581_end_14=(( tom.gom.adt.objects.types.HookList )hooks);do {{if (!( tomMatch581_end_14.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch581_18= tomMatch581_end_14.getHeadConcHook() ;if ( (tomMatch581_18 instanceof tom.gom.adt.objects.types.Hook) ) {if ( ((( tom.gom.adt.objects.types.Hook )tomMatch581_18) instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {

        CodeGen.generateCode( tomMatch581_18.getCode() ,writer);
      }}}if ( tomMatch581_end_14.isEmptyConcHook() ) {tomMatch581_end_14=(( tom.gom.adt.objects.types.HookList )hooks);} else {tomMatch581_end_14= tomMatch581_end_14.getTailConcHook() ;}}} while(!( (tomMatch581_end_14==(( tom.gom.adt.objects.types.HookList )hooks)) ));}}}}

    return;
  }
}
