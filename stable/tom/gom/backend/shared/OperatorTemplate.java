/*
* Gom
*
* Copyright (c) 2006-2011, INPL, INRIA
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

public class OperatorTemplate extends TemplateHookedClass {
ClassName abstractType;
ClassName extendsType;
ClassName sortName;
SlotFieldList slotList;
String comments;
boolean multithread;
boolean maximalsharing;
boolean jmicompatible;



  private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {
    if( l1.isEmptyConcHook() ) {
      return l2;
    } else if( l2.isEmptyConcHook() ) {
      return l1;
    } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {
      return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;
  }
  
  private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {
    if( l1.isEmptyConcSlotField() ) {
      return l2;
    } else if( l2.isEmptyConcSlotField() ) {
      return l1;
    } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;
  }
  

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

{
{
if ( (((Object)gomClass) instanceof tom.gom.adt.objects.types.GomClass) ) {
if ( ((( tom.gom.adt.objects.types.GomClass )((Object)gomClass)) instanceof tom.gom.adt.objects.types.GomClass) ) {
if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )((Object)gomClass))) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {

this.abstractType = 
 (( tom.gom.adt.objects.types.GomClass )((Object)gomClass)).getAbstractType() ;
this.extendsType = 
 (( tom.gom.adt.objects.types.GomClass )((Object)gomClass)).getExtendsType() ;
this.sortName = 
 (( tom.gom.adt.objects.types.GomClass )((Object)gomClass)).getSortName() ;
this.slotList = 
 (( tom.gom.adt.objects.types.GomClass )((Object)gomClass)).getSlotFields() ;
this.comments = 
 (( tom.gom.adt.objects.types.GomClass )((Object)gomClass)).getComments() ;
return;


}
}
}

}

}

throw new GomRuntimeException(
"Bad argument for OperatorTemplate: " + gomClass);
}

public void generate(java.io.Writer writer) throws java.io.IOException {

writer.write(
"\npackage "+getPackage()+
";\n"+generateImport()+
"\n");

if (maximalsharing) {
writer.write(
"\n"+generateComments()+
"\npublic final class "+className()+
" extends "+fullClassName(extendsType)+
" implements tom.library.sl.Visitable "+generateInterface()+
" {\n  "+generateBlock()+
"\n  private static String symbolName = \""+className()+
"\";\n");


if(slotList.length() > 0) {
writer.write(
"\n\n  private "+className()+
"() {}\n  private int hashCode;\n  private static "+className()+
" gomProto = new "+className()+
"();\n  ");
} else {
writer.write(
"\n\n  private "+className()+
"() {}\n  private static int hashCode = hashFunction();\n  private static "+className()+
" gomProto = ("+className()+
") factory.build(new "+className()+
"());\n  ");
}
} else {
writer.write(
"\n"+generateComments()+
"\npublic final class "+className()+
" extends "+fullClassName(extendsType)+
" implements Cloneable, tom.library.sl.Visitable "+generateInterface()+
" {\n  "+generateBlock()+
"\n  private static String symbolName = \""+className()+
"\";\n");


// generate a private constructor that takes as arguments the operator arguments
writer.write(
"\n\n  private "+className()+
"("+childListWithType(slotList)+
") {\n  ");
generateMembersInit(writer);
writer.write(
"\n  }\n  ");
}

if (hooks.containsTomCode()) {
mapping.generate(writer);
}
generateMembers(writer);
generateBody(writer);
writer.write(
"\n}\n");
}

private void generateBody(java.io.Writer writer) throws java.io.IOException {
if (!comments.equals("")) {
writer.write(
"\n  "+generateComments()+
"\n        ");
} else {
writer.write(
"\n  /**\n   * Constructor that builds a term rooted by "+className()+
"\n   *\n   * @return a term rooted by "+className()+
"\n   */\n");
}
generateConstructor(writer);

if(slotList.length()>0) {

if (maximalsharing) {
writer.write(
"\n  /**\n   * Initializes attributes and hashcode of the class\n   *\n   * @param "+childListOnePerLine(slotList)+
"\n   * @param hashCode hashCode of "+className()+
"\n   */\n  private void init("+childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":", ") +
"int hashCode) {\n");
generateMembersInit(writer);
writer.write(
"\n    this.hashCode = hashCode;\n  }\n\n  /**\n   * Initializes attributes and hashcode of the class\n   *\n   * @param "+childListOnePerLine(slotList)+
"\n   */\n  private void initHashCode("+childListWithType(slotList)+
") {\n");
generateMembersInit(writer);
writer.write(
"\n    this.hashCode = hashFunction();\n  }\n");
}

writer.write(
"\n  /* name and arity */\n\n  /**\n   * Returns the name of the symbol\n   *\n   * @return the name of the symbol\n   */\n  @Override\n  public String symbolName() {\n    return \""+className()+
"\";\n  }\n\n  /**\n   * Returns the arity of the symbol\n   *\n   * @return the arity of the symbol\n   */\n  private int getArity() {\n    return "+slotList.length()+
";\n  }\n");


if (maximalsharing) {
writer.write(
"\n  /**\n   * Copy the object and returns the copy\n   *\n   * @return a clone of the SharedObject\n   */");
if(multithread) {
writer.write(
"\n  public shared.SharedObject duplicate() {\n    // the proto is a fresh object: no need to clone it again\n    return this;\n  }\n  ");
} else {
writer.write(
"\n  public shared.SharedObject duplicate() {\n    "+className()+
" clone = new "+className()+
"();\n    clone.init("+childList(slotList) + (slotList.isEmptyConcSlotField()?"":", ") +
"hashCode);\n    return clone;\n  }\n  ");
}
}

} else {
// case: constant
writer.write(
"\n  /* name and arity */\n\n  /**\n   * Returns the name of the symbol\n   *\n   * @return the name of the symbol\n   */\n  @Override\n  public String symbolName() {\n    return \""+className()+
"\";\n  }\n\n  /**\n   * Returns the arity of the symbol\n   *\n   * @return arity of the symbol\n   */\n  private static int getArity() {\n    return 0;\n  }\n\n");

if (maximalsharing) {
writer.write(
"\n  /**\n   * Copy the object and returns the copy\n   *\n   * @return a clone of the SharedObject\n   */\n  public shared.SharedObject duplicate() {\n    // the proto is a constant object: no need to clone it\n    return this;\n    //return new "+className()+
"();\n  }\n");
}

}

/*
* Generate a toStringBuilder method if the operator is not associative
*/
if (sortName == extendsType) {
writer.write(
"\n  /**\n   * Appends a string representation of this term to the buffer given as argument.\n   *\n   * @param buffer the buffer to which a string represention of this term is appended.\n   */\n  @Override\n  public void toStringBuilder(java.lang.StringBuilder buffer) {\n    buffer.append(\""+className()+
"(\");\n    "+toStringChilds("buffer")+
"\n    buffer.append(\")\");\n  }\n");
}

writer.write(
"\n\n  /**\n   * Compares two terms. This functions implements a total lexicographic path ordering.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare childs\n   */\n  @Override\n  public int compareToLPO(Object o) {\n    /*\n     * We do not want to compare with any object, only members of the module\n     * In case of invalid argument, throw a ClassCastException, as the java api\n     * asks for it\n     */\n    "+fullClassName(abstractType)+
" ao = ("+fullClassName(abstractType)+
") o;\n    /* return 0 for equality */\n    if (ao == this) { return 0; }\n    /* compare the symbols */\n    int symbCmp = this.symbolName().compareTo(ao.symbolName());\n    if (symbCmp != 0) { return symbCmp; }\n    /* compare the childs */\n    "+genCompareChilds("ao","compareToLPO")+
"\n    throw new RuntimeException(\"Unable to compare\");\n  }\n");

if (maximalsharing) {
writer.write(
"\n /**\n   * Compares two terms. This functions implements a total order.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare childs\n   */\n  @Override\n  public int compareTo(Object o) {\n    /*\n     * We do not want to compare with any object, only members of the module\n     * In case of invalid argument, throw a ClassCastException, as the java api\n     * asks for it\n     */\n    "+fullClassName(abstractType)+
" ao = ("+fullClassName(abstractType)+
") o;\n    /* return 0 for equality */\n    if (ao == this) { return 0; }\n    /* use the hash values to discriminate */\n\n    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }\n\n    /* If not, compare the symbols : back to the normal order */\n    int symbCmp = this.symbolName().compareTo(ao.symbolName());\n    if (symbCmp != 0) { return symbCmp; }\n    /* last resort: compare the childs */\n    "+genCompareChilds("ao","compareTo")+
"\n    throw new RuntimeException(\"Unable to compare\");\n  }\n\n //shared.SharedObject\n  /**\n   * Returns hashCode\n   *\n   * @return hashCode\n   */\n  @Override\n  public final int hashCode() {\n    return hashCode;\n  }\n\n  /**\n   * Checks if a SharedObject is equivalent to the current object\n   *\n   * @param obj SharedObject to test\n   * @return true if obj is a "+className()+
" and its members are equal, else false\n   */\n  public final boolean equivalent(shared.SharedObject obj) {\n    if(obj instanceof "+className()+
") {\n"+generateMembersEqualityTest("peer")+
"\n    }\n    return false;\n  }\n\n");
} else {
//XXX: compareTo must be correctly implemented
writer.write(
"\n  /**\n   * Compares two terms. This functions implements a total order.\n   *\n   * @param o object to which this term is compared\n   * @return a negative integer, zero, or a positive integer as this\n   *         term is less than, equal to, or greater than the argument\n   * @throws ClassCastException in case of invalid arguments\n   * @throws RuntimeException if unable to compare childs\n   */\n  @Override\n  public int compareTo(Object o) {\n    throw new UnsupportedOperationException(\"Unable to compare\");\n  }\n\n  /**\n   * Clones the object\n   *\n   * @return the copy\n   */\n  @Override\n  public Object clone() {\n");

SlotFieldList slots = slotList;
if(slots.isEmptyConcSlotField()) {
writer.write(
"\n      return new "+className()+
"();\n  }\n      ");
} else {

SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
writer.write(
"\n      return new "+className()+
"("+getMethod(head)+
"()");

} else {
writer.write(
"\n      return new "+className()+
"( ("+fullClassName(head.getDomain())+
") "+getMethod(head)+
"().clone()");
}

while(!slots.isEmptyConcSlotField()) {
head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
writer.write(
","+getMethod(head)+
"()");
} else {
writer.write(
",("+fullClassName(head.getDomain())+
") "+getMethod(head)+
"().clone()");
}
}
writer.write(");\n}");
}

writer.write(
"\n  /**\n   * Checks if an object is strictly equal to the current object\n   *\n   * @param o object to compare\n   * @return true if each member is equal, else false\n   */\n  @Override\n  public final boolean deepEquals(Object o) {\n    if (o instanceof "+className()+
") {\n      "+className()+
" typed_o = ("+className()+
") o;\n");

slots = slotList;
if(slots.isEmptyConcSlotField()) {
writer.write(
"\n      return true;\n      ");
} else {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
writer.write(
"\n      return "+fieldName(head.getName())+
" == typed_o."+getMethod(head)+
"()\n      ");

} else {
writer.write(
"\n      return "+fieldName(head.getName())+
".deepEquals(typed_o."+getMethod(head)+
"())\n      ");
}

while(!slots.isEmptyConcSlotField()) {
head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
writer.write(
"\n        && "+fieldName(head.getName())+
" == typed_o."+getMethod(head)+
"()\n        ");

} else {
writer.write(
"\n      && "+fieldName(head.getName())+
".deepEquals(typed_o."+getMethod(head)+
"())\n      ");
}
}
writer.write(";");
}
writer.write(
"\n    }\n    return false;\n    }\n  ");
}

writer.write(
"\n   //"+className(sortName)+
" interface\n  /**\n   * Returns true if the term is rooted by the symbol "+className.getName()+
"\n   *\n   * @return true, because this is rooted by "+className.getName()+
"\n   */\n  @Override\n  public boolean "+isOperatorMethod(className)+
"() {\n    return true;\n  }\n  ");

generateGetters(writer);

writer.write(
"\n  /* AbstractType */\n  /**\n   * Returns an ATerm representation of this term.\n   *\n   * @return an ATerm representation of this term.\n   */\n  @Override\n  public aterm.ATerm toATerm() {\n    aterm.ATerm res = super.toATerm();\n    if(res != null) {\n      // the super class has produced an ATerm (may be a variadic operator)\n      return res;\n    }\n    return atermFactory.makeAppl(\n      atermFactory.makeAFun(symbolName(),getArity(),false),\n      new aterm.ATerm[] {"+generateToATermChilds()+
"});\n  }\n\n  /**\n   * Apply a conversion on the ATerm contained in the String and returns a "+fullClassName(sortName)+
" from it\n   *\n   * @param trm ATerm to convert into a Gom term\n   * @param atConv ATerm Converter used to convert the ATerm\n   * @return the Gom term\n   */\n  public static "+fullClassName(sortName)+
" fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {\n    trm = atConv.convert(trm);\n    if(trm instanceof aterm.ATermAppl) {\n      aterm.ATermAppl appl = (aterm.ATermAppl) trm;\n      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {\n        return make(\n"+generatefromATermChilds("appl","atConv")+
"\n        );\n      }\n    }\n    return null;\n  }\n");

writer.write(
"\n  /* Visitable */\n  /**\n   * Returns the number of childs of the term\n   *\n   * @return the number of childs of the term\n   */\n  public int getChildCount() {\n    return "+visitableCount()+
";\n  }\n\n  /**\n   * Returns the child at the specified index\n   *\n   * @param index index of the child to return; must be\n             nonnegative and less than the childCount\n   * @return the child at the specified index\n   * @throws IndexOutOfBoundsException if the index out of range\n   */\n  public tom.library.sl.Visitable getChildAt(int index) {\n    switch(index) {\n"+getCases()+
"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n  /**\n   * Set the child at the specified index\n   *\n   * @param index index of the child to set; must be\n             nonnegative and less than the childCount\n   * @param v child to set at the specified index\n   * @return the child which was just set\n   * @throws IndexOutOfBoundsException if the index out of range\n   */\n  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {\n    switch(index) {\n"+makeCases("v")+
"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n  /**\n   * Set children to the term\n   *\n   * @param childs array of children to set\n   * @return an array of children which just were set\n   * @throws IndexOutOfBoundsException if length of \"childs\" is different than "+slotList.length()+
"\n   */\n  @SuppressWarnings(\"unchecked\")\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {\n    if (childs.length == "+slotList.length()+
" "+arrayCheck("childs")+
") {\n      return "+arrayMake("childs")+
";\n    } else {\n      throw new IndexOutOfBoundsException();\n    }\n  }\n\n  /**\n   * Returns the whole children of the term\n   *\n   * @return the children of the term\n   */\n  public tom.library.sl.Visitable[] getChildren() {\n    return new tom.library.sl.Visitable[] { "+visitableList(slotList)+
" };\n  }\n");

if(maximalsharing) {
// OLD VERSION
writer.write(
"\n    /**\n     * Compute a hashcode for this term.\n     * (for internal use)\n     *\n     * @return a hash value\n     */\n  protected"+((slotList.length()==0)?" static":"")+
" int hashFunction() {\n    int a, b, c;\n    /* Set up the internal state */\n    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */\n    b = ("+shared.HashFunctions.stringHashFunction(fullClassName(),slotList.length())+
"<<8);\n    c = getArity();\n    /* -------------------------------------- handle most of the key */\n    /* ------------------------------------ handle the last 11 bytes */\n");
generateHashArgs(writer);
writer.write(
"\n    a -= b; a -= c; a ^= (c >> 13);\n    b -= c; b -= a; b ^= (a << 8);\n    c -= a; c -= b; c ^= (b >> 13);\n    a -= b; a -= c; a ^= (c >> 12);\n    b -= c; b -= a; b ^= (a << 16);\n    c -= a; c -= b; c ^= (b >> 5);\n    a -= b; a -= c; a ^= (c >> 3);\n    b -= c; b -= a; b ^= (a << 10);\n    c -= a; c -= b; c ^= (b >> 15);\n    /* ------------------------------------------- report the result */\n    return c;\n  }\n");
}

if(false && maximalsharing) {
// NEW VERSION: http://burtleburtle.net/bob/c/lookup3.c
// seems to be a bit slower than the OLD version
int length = slotList.length();
writer.write(
"\n    /**\n     * Compute a hashcode for this term.\n     * (for internal use)\n     *\n     * @return a hash value\n     */\n  protected"+((length==0)?" static":"")+
" int hashFunction() {\n    int a, b, c;\n    /* Set up the internal state */\n    a = b = c =\n    0xdeadbeef + (getArity()<<2) +\n    ("+shared.HashFunctions.stringHashFunction(fullClassName(),length)+
"<<8);\n    /* -------------------------------------- handle most of the key */\n    /* ------------------------------------ handle the last 11 bytes */\n");
generateHashArgsLookup3(writer);
if(length>0 && (length%3)>0 ) {
writer.write(
"\n    // final(a,b,c)\n    c ^= b; c -= (((b)<<(14)) | ((b)>>(32-(14))));\n    a ^= c; a -= (((c)<<(11)) | ((c)>>(32-(11))));\n    b ^= a; b -= (((a)<<(25)) | ((a)>>(32-(25))));\n    c ^= b; c -= (((b)<<(16)) | ((b)>>(32-(16))));\n    a ^= c; a -= (((c)<<(4)) | ((c)>>(32-(4))));\n    b ^= a; b -= (((a)<<(14)) | ((a)>>(32-(14))));\n    c ^= b; c -= (((b)<<(24)) | ((b)>>(32-(24))));\n");
}

writer.write(
"\n    /* ------------------------------------------- report the result */\n    return c;\n  }\n");
}

}

private String generateComments() {
if (!comments.equals("") && comments.contains("@param")) {
// "@param chaine " -> "@param _chaine "
return comments.replaceAll("@param ","@param _");
} else {
return comments;
}
}

private void generateMembers(java.io.Writer writer) throws java.io.IOException {

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch503__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch503__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch503_9= tomMatch503__end__4.getHeadConcSlotField() ;
if ( (tomMatch503_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch503_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

writer.write("  private ");
writer.write(fullClassName(
 tomMatch503_9.getDomain() ));
writer.write(" ");
writer.write(fieldName(
 tomMatch503_9.getName() ));
writer.write(";\n");


}
}
}
if ( tomMatch503__end__4.isEmptyConcSlotField() ) {
tomMatch503__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch503__end__4= tomMatch503__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch503__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

}

private void generateMembersInit(java.io.Writer writer) throws java.io.IOException {

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch504__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch504__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch504_9= tomMatch504__end__4.getHeadConcSlotField() ;
if ( (tomMatch504_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch504_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch504_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch504_9.getDomain() ;

writer.write("    this.");
writer.write(fieldName(
tom_fieldName));
writer.write(" = ");
writer.write(fieldName(
tom_fieldName));
if (getGomEnvironment().isBuiltinClass(
tom_domain) && 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
writer.write(".intern()");
}
writer.write(";\n");


}
}
}
if ( tomMatch504__end__4.isEmptyConcSlotField() ) {
tomMatch504__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch504__end__4= tomMatch504__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch504__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

}

private void generateGetters(java.io.Writer writer) throws java.io.IOException {
SlotFieldList slots = slotList;
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
writer.write(
"\n  /**\n   * Returns the attribute "+slotDomain(head)+
"\n   *\n   * @return the attribute "+slotDomain(head)+
"\n   */\n  @Override\n  public "+slotDomain(head)+
" "+getMethod(head)+
"() {\n    return "+fieldName(head.getName())+
";\n  }\n\n  /**\n   * Sets and returns the attribute "+fullClassName(sortName)+
"\n   *\n   * @param set_arg the argument to set\n   * @return the attribute "+slotDomain(head)+
" which just has been set\n   */");
if (maximalsharing) {
writer.write(
"\n  @Override\n  public "+fullClassName(sortName)+
" "+setMethod(head)+
"("+slotDomain(head)+
" set_arg) {\n    return make("+generateMakeArgsFor(head,"set_arg")+
");\n  }\n  ");
} else {
writer.write(
"\n  @Override\n  public "+fullClassName(sortName)+
" "+setMethod(head)+
"("+slotDomain(head)+
" set_arg) {\n    "+fieldName(head.getName())+
" = set_arg;\n    return this;\n  }");
}

if(jmicompatible) {
// generate getters and setters where slot-names are normalized
if(!getMethod(head,true).equals(getMethod(head))) {
writer.write(
"\n  public "+slotDomain(head)+
" "+getMethod(head,true)+
"() {\n    return "+getMethod(head)+
"();\n  }\n  ");
}
if(!setMethod(head,true).equals(setMethod(head))) {
writer.write(
"\n  public "+fullClassName(sortName)+
" "+setMethod(head,true)+
"("+slotDomain(head)+
" set_arg) {\n    return "+setMethod(head)+
"(set_arg);\n  }\n  ");
}
}
}
}

private String generateToATermChilds() {
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

private String generatefromATermChilds(String appl, String atConv) {
StringBuilder res = new StringBuilder();
int index = 0;
SlotFieldList slots = slotList;
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (res.length()!=0) {
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

{
{
if ( (((Object)head) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )((Object)head)) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )((Object)head))) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (res.length()!=0) {
res.append(", ");
}
res.append(fullClassName(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getDomain() ));
res.append(" ");
res.append(fieldName(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getName() ));


}
}
}

}

}

}
return res.toString();
}
private String unprotectedChildListWithType(SlotFieldList slots) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (((Object)head) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )((Object)head)) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )((Object)head))) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (res.length()!=0) {
res.append(", ");
}
res.append(fullClassName(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getDomain() ));
res.append(" ");
res.append(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getName() );


}
}
}

}

}

}
return res.toString();
}
private String childList(SlotFieldList slots) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (((Object)head) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )((Object)head)) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )((Object)head))) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (res.length()!=0) {
res.append(", ");
}
res.append(" ");
res.append(fieldName(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getName() ));


}
}
}

}

}

}
return res.toString();
}
private String unprotectedChildList(SlotFieldList slots) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (((Object)head) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )((Object)head)) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )((Object)head))) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (res.length()!=0) {
res.append(", ");
}
res.append(" ");
res.append(
 (( tom.gom.adt.objects.types.SlotField )((Object)head)).getName() );


}
}
}

}

}

}
return res.toString();
}
private String generateMembersEqualityTest(String peer) {
StringBuilder res = new StringBuilder();
if(!slotList.isEmptyConcSlotField()) {
res.append(
"\n      "+className()+
" peer = ("+className()+
") obj;");;
}
res.append(
"\n      return ");

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch509__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch509__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch509_8= tomMatch509__end__4.getHeadConcSlotField() ;
if ( (tomMatch509_8 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch509_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch509_8.getName() ;

res.append(fieldName(
tom_fieldName));
res.append("==");
res.append(peer);
res.append(".");
res.append(fieldName(
tom_fieldName));
res.append(" && ");


}
}
}
if ( tomMatch509__end__4.isEmptyConcSlotField() ) {
tomMatch509__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch509__end__4= tomMatch509__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch509__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

res.append("true;"); // to handle the "no childs" case
return res.toString();
}

private String getCases() {
StringBuilder res = new StringBuilder();
int index = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch510__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch510__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch510_9= tomMatch510__end__4.getHeadConcSlotField() ;
if ( (tomMatch510_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch510_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch510_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch510_9.getDomain() ;

if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append("      case ");
res.append(index);
res.append(": return ");
res.append(fieldName(
tom_fieldName));
res.append(";\n");
index++;
} else {
res.append("      case ");
res.append(index);
res.append(": return new tom.library.sl.VisitableBuiltin<");
res.append(primitiveToReferenceType(fullClassName(
tom_domain)));
res.append(">(");
res.append(fieldName(
tom_fieldName));
res.append(");\n");
index++;
}


}
}
}
if ( tomMatch510__end__4.isEmptyConcSlotField() ) {
tomMatch510__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch510__end__4= tomMatch510__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch510__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}

private String visitableList(SlotFieldList slots) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (((Object)head) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )((Object)head)) instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )(( tom.gom.adt.objects.types.SlotField )((Object)head))) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )((Object)head)).getDomain() ;
 String  tom_name= (( tom.gom.adt.objects.types.SlotField )((Object)head)).getName() ;

if (res.length()!=0) {
res.append(", ");
}
res.append(" ");
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append(fieldName(
tom_name));
} else {
res.append("new tom.library.sl.VisitableBuiltin<");
res.append(primitiveToReferenceType(fullClassName(
tom_domain)));
res.append(">(");
res.append(fieldName(
tom_name));
res.append(")");
}


}
}
}

}

}

}
return res.toString();
}


private String visitableCount() {
if(className().equals("ConsPath"+sortName.getName())) {
return "0";
} else {
return ""+slotList.length();
}
}

private String arrayMake(String arrayName) {
StringBuilder res = new StringBuilder("make(");
int index = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch512__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch512__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch512_8= tomMatch512__end__4.getHeadConcSlotField() ;
if ( (tomMatch512_8 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch512_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch512_8.getDomain() ;

if(index>0) { res.append(", "); }
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append("(");
res.append(fullClassName(
tom_domain));
res.append(") ");
res.append(arrayName);
res.append("[");
res.append(index);
res.append("]");
} else {
res.append("((tom.library.sl.VisitableBuiltin<");
res.append(primitiveToReferenceType(fullClassName(
tom_domain)));
res.append(">)");
res.append(arrayName);
res.append("[");
res.append(index);
res.append("])");
res.append(".getBuiltin()");
}
index++;


}
}
}
if ( tomMatch512__end__4.isEmptyConcSlotField() ) {
tomMatch512__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch512__end__4= tomMatch512__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch512__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

res.append(")");
return res.toString();
}

private String arrayCheck(String arrayName) {
StringBuilder res = new StringBuilder();
int index = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch513__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch513__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch513_8= tomMatch513__end__4.getHeadConcSlotField() ;
if ( (tomMatch513_8 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch513_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch513_8.getDomain() ;

if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append(
" && "+arrayName+
"["+index+
"] instanceof "+fullClassName(
tom_domain)+
"");
} else {
res.append(
" && "+arrayName+
"["+index+
"] instanceof tom.library.sl.VisitableBuiltin");
}
index++;


}
}
}
if ( tomMatch513__end__4.isEmptyConcSlotField() ) {
tomMatch513__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch513__end__4= tomMatch513__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch513__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}
private String makeCases(String argName) {
StringBuilder res = new StringBuilder();
int index = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch514__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch514__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch514_7= tomMatch514__end__4.getHeadConcSlotField() ;
if ( (tomMatch514_7 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch514_7) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
index++;

}
}
}
if ( tomMatch514__end__4.isEmptyConcSlotField() ) {
tomMatch514__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch514__end__4= tomMatch514__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch514__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}
private String generateMakeArgsFor(int argIndex, String argName) {
StringBuilder res = new StringBuilder();
int index = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch515__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch515__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch515_9= tomMatch515__end__4.getHeadConcSlotField() ;
if ( (tomMatch515_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch515_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch515_9.getDomain() ;

if(index>0) { res.append(", "); }
if (getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append(getMethod(
 tomMatch515__end__4.getHeadConcSlotField() ));
res.append("()");
} else {
if (index != argIndex) {
res.append(fieldName(
 tomMatch515_9.getName() ));
} else {
res.append("(");
res.append(fullClassName(
tom_domain));
res.append(") ");
res.append(argName);
}
}
index++;


}
}
}
if ( tomMatch515__end__4.isEmptyConcSlotField() ) {
tomMatch515__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch515__end__4= tomMatch515__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch515__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}
private String generateMakeArgsFor(SlotField slot, String argName) {
StringBuilder res = new StringBuilder();
int fullindex = 0;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch516__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch516__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch516_8= tomMatch516__end__4.getHeadConcSlotField() ;
if ( (tomMatch516_8 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch516_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if(fullindex>0) { res.append(", "); }
if (
 tomMatch516__end__4.getHeadConcSlotField() == slot) {
res.append(argName);
} else {
res.append(fieldName(
 tomMatch516_8.getName() ));
}
fullindex++;


}
}
}
if ( tomMatch516__end__4.isEmptyConcSlotField() ) {
tomMatch516__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch516__end__4= tomMatch516__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch516__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}

private String toStringChilds(String buffer) {
if (0 == slotList.length()) {
return "";
}
StringBuilder res = new StringBuilder();
SlotFieldList slots = slotList;
while(!slots.isEmptyConcSlotField()) {
if(res.length()!=0) {
res.append(
""+buffer+
".append(\",\");\n    ");
}
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
toStringSlotField(res, head, fieldName(head.getName()), buffer);
}
return res.toString();
}

private String genCompareChilds(String oldOther, String compareFun) {
StringBuilder res = new StringBuilder();
String other = "tco";
if(!slotList.isEmptyConcSlotField()) {
res.append(
""+className()+
" "+other+
" = ("+className()+
") "+oldOther+
";");
}

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch517__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch517__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch517_9= tomMatch517__end__4.getHeadConcSlotField() ;
if ( (tomMatch517_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch517_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_slotName= tomMatch517_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch517_9.getDomain() ;

if (getGomEnvironment().isBuiltinClass(
tom_domain)) {
if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "double") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
res.append(
"\n    if( this."+fieldName(
tom_slotName)+
" != "+other+
"."+fieldName(
tom_slotName)+
")\n      return (this."+fieldName(
tom_slotName)+
" < "+other+
"."+fieldName(
tom_slotName)+
")?-1:1;\n");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
res.append(
"\n    if( this."+fieldName(
tom_slotName)+
" != "+other+
"."+fieldName(
tom_slotName)+
")\n      return (!this."+fieldName(
tom_slotName)+
" && "+other+
"."+fieldName(
tom_slotName)+
")?-1:1;\n");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
res.append(
"\n    int "+fieldName(
tom_slotName)+
"Cmp = (this."+fieldName(
tom_slotName)+
").compareTo("+other+
"."+fieldName(
tom_slotName)+
");\n    if("+fieldName(
tom_slotName)+
"Cmp != 0)\n      return "+fieldName(
tom_slotName)+
"Cmp;\n\n");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )
||
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
res.append(
"\n    /* Inefficient total order on ATerm */\n    int "+fieldName(
tom_slotName)+
"Cmp = ((this."+fieldName(
tom_slotName)+
").toString()).compareTo(("+other+
"."+fieldName(
tom_slotName)+
").toString());\n    if("+fieldName(
tom_slotName)+
"Cmp != 0)\n      return "+fieldName(
tom_slotName)+
"Cmp;\n");
} else {
throw new GomRuntimeException("Builtin "+
tom_domain+" not supported");
}
} else {
res.append(
"\n    int "+fieldName(
tom_slotName)+
"Cmp = (this."+fieldName(
tom_slotName)+
")."+compareFun+
"("+other+
"."+fieldName(
tom_slotName)+
");\n    if("+fieldName(
tom_slotName)+
"Cmp != 0)\n      return "+fieldName(
tom_slotName)+
"Cmp;\n");
}


}
}
}
if ( tomMatch517__end__4.isEmptyConcSlotField() ) {
tomMatch517__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch517__end__4= tomMatch517__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch517__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

return res.toString();
}

private void generateHashArgs(java.io.Writer writer) throws java.io.IOException {
int index = slotList.length() - 1;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch518__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch518__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch518_9= tomMatch518__end__4.getHeadConcSlotField() ;
if ( (tomMatch518_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch518_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_slotName= tomMatch518_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch518_9.getDomain() ;

int shift = (index % 4) * 8;
String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
writer.write("    "+accum+" += (");
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
writer.write(fieldName(
tom_slotName)+".hashCode()");
} else {
if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
writer.write(fieldName(
tom_slotName));
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
writer.write("("+fieldName(
tom_slotName)+"?1:0)");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
// Use the string hashFunction for Strings, and pass index as arity
writer.write("shared.HashFunctions.stringHashFunction("+fieldName(
tom_slotName)+", "+index+")");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
writer.write("(int)(java.lang.Double.doubleToLongBits(");
writer.write(fieldName(
tom_slotName));
writer.write(")^(java.lang.Double.doubleToLongBits(");
writer.write(fieldName(
tom_slotName));
writer.write(")>>>32");
writer.write("))");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )||
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
// Use the string hashFunction for Strings, and pass index as arity
writer.write(fieldName(
tom_slotName)+".hashCode()");
}  else {
throw new GomRuntimeException("generateHashArgs: Builtin " + 
tom_domain+ " not supported");
}
}
if (shift!=0) { writer.write(" << "+(shift)); }
writer.write(");\n");
index--;


}
}
}
if ( tomMatch518__end__4.isEmptyConcSlotField() ) {
tomMatch518__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch518__end__4= tomMatch518__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch518__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

}

private void generateHashArgsLookup3(java.io.Writer writer) throws java.io.IOException {
int k=0;
int index = slotList.length() - 1;

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch519__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch519__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch519_9= tomMatch519__end__4.getHeadConcSlotField() ;
if ( (tomMatch519_9 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch519_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_slotName= tomMatch519_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch519_9.getDomain() ;

k++;
switch(k % 3) {
case 1: writer.write("    a += ("); break;
case 2: writer.write("    b += ("); break;
case 0: writer.write("    c += ("); break;
default:
}
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
writer.write(fieldName(
tom_slotName)+".hashCode()");
} else {
if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "int") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "long") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "float") )
|| 
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "char") )) {
writer.write(fieldName(
tom_slotName));
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "boolean") )) {
writer.write("("+fieldName(
tom_slotName)+"?1:0)");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "String") )) {
// Use the string hashFunction for Strings, and pass index as arity
writer.write("shared.HashFunctions.stringHashFunction("+fieldName(
tom_slotName)+", "+index+")");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("", "double") )) {
writer.write("(int)(java.lang.Double.doubleToLongBits(");
writer.write(fieldName(
tom_slotName));
writer.write(")^(java.lang.Double.doubleToLongBits(");
writer.write(fieldName(
tom_slotName));
writer.write(")>>>32");
writer.write("))");
} else if (
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATerm") )||
tom_domain.equals(
 tom.gom.adt.objects.types.classname.ClassName.make("aterm", "ATermList") )) {
// Use the string hashFunction for Strings, and pass index as arity
writer.write(fieldName(
tom_slotName)+".hashCode()");
}  else {
throw new GomRuntimeException("generateHashArgs: Builtin " + 
tom_domain+ " not supported");
}
}
writer.write(");\n");
if(k % 3 == 0) {
writer.write(
"\n    // mix(a,b,c)\n    a -= c;  a ^= (((c)<<(4))  | ((c)>>(32-(4))));  c += b;\n    b -= a;  b ^= (((a)<<(6))  | ((a)>>(32-(6))));  a += c;\n    c -= b;  c ^= (((b)<<(8))  | ((b)>>(32-(8))));  b += a;\n    a -= c;  a ^= (((c)<<(16)) | ((c)>>(32-(16)))); c += b;\n    b -= a;  b ^= (((a)<<(19)) | ((a)>>(32-(19)))); a += c;\n    c -= b;  c ^= (((b)<<(4))  | ((b)>>(32-(4))));  b += a;\n");
}
index--;


}
}
}
if ( tomMatch519__end__4.isEmptyConcSlotField() ) {
tomMatch519__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch519__end__4= tomMatch519__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch519__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

}

public void generateConstructor(java.io.Writer writer) throws java.io.IOException {
boolean hasHooks = false;

{
{
lbl: {
if ( (((Object)hooks) instanceof tom.gom.adt.objects.types.HookList) ) {
if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {
 tom.gom.adt.objects.types.HookList  tomMatch520__end__4=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
do {
{
if (!( tomMatch520__end__4.isEmptyConcHook() )) {
 tom.gom.adt.objects.types.Hook  tomMatch520_8= tomMatch520__end__4.getHeadConcHook() ;
if ( (tomMatch520_8 instanceof tom.gom.adt.objects.types.Hook) ) {
if ( ((( tom.gom.adt.objects.types.Hook )tomMatch520_8) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) {

hasHooks = true;
writer.write(
"\n    public static "+fullClassName(sortName)+
" make("+unprotectedChildListWithType(
 tomMatch520_8.getHookArguments() )+
") {\n  ");
SlotFieldList bargs = generateMakeHooks(hooks,null,writer);
writer.write(
"\n      return realMake("+unprotectedChildList(bargs)+
");\n    }\n  ");
break lbl;


}
}
}
if ( tomMatch520__end__4.isEmptyConcHook() ) {
tomMatch520__end__4=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
} else {
tomMatch520__end__4= tomMatch520__end__4.getTailConcHook() ;
}

}
} while(!( (tomMatch520__end__4==(( tom.gom.adt.objects.types.HookList )((Object)hooks))) ));
}
}

}

}

}


String makeName = "make";
String visibility = "public";
if (hasHooks) {
makeName = "realMake";
visibility = "private";
}
writer.write(
"\n  "+visibility+
" static "+className()+
" "+makeName+
"("+childListWithType(slotList)+
") {\n");

if (! maximalsharing) {
writer.write(
"\n    return new "+className()+
"("+childList(slotList)+
");\n    ");
} else {
if(slotList.length()>0) {
if(multithread) {
writer.write(
"\n    // allocate and object and make duplicate equal identity\n    "+className()+
" newProto = new "+className()+
"();\n    newProto.initHashCode("+childList(slotList)+
");\n    return ("+className()+
") factory.build(newProto);\n");
} else {
writer.write(
"\n    // use the proto as a model\n    gomProto.initHashCode("+childList(slotList)+
");\n    return ("+className()+
") factory.build(gomProto);\n");
}
} else {
writer.write(
"\n    return gomProto;\n");
}
}
writer.write(
"\n  }\n");
}

public SlotFieldList generateMakeHooks(
HookList other,
SlotFieldList oArgs, /* will be null if it is the first hook */
java.io.Writer writer)
throws java.io.IOException {

{
{
if ( (((Object)other) instanceof tom.gom.adt.objects.types.HookList) ) {
if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)other))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)other))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {
if (!( (( tom.gom.adt.objects.types.HookList )((Object)other)).isEmptyConcHook() )) {
boolean tomMatch521_6= false ;
 tom.gom.adt.objects.types.Hook  tomMatch521_4= (( tom.gom.adt.objects.types.HookList )((Object)other)).getHeadConcHook() ;
if ( (tomMatch521_4 instanceof tom.gom.adt.objects.types.Hook) ) {
if ( ((( tom.gom.adt.objects.types.Hook )tomMatch521_4) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) {
tomMatch521_6= true ;
}
}
if (!(tomMatch521_6)) {

/* skip non Make hooks */
return generateMakeHooks(
 (( tom.gom.adt.objects.types.HookList )((Object)other)).getTailConcHook() , oArgs, writer);


}

}
}
}

}
{
if ( (((Object)other) instanceof tom.gom.adt.objects.types.HookList) ) {
if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)other))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)other))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {
if (!( (( tom.gom.adt.objects.types.HookList )((Object)other)).isEmptyConcHook() )) {
 tom.gom.adt.objects.types.Hook  tomMatch521_13= (( tom.gom.adt.objects.types.HookList )((Object)other)).getHeadConcHook() ;
if ( (tomMatch521_13 instanceof tom.gom.adt.objects.types.Hook) ) {
if ( ((( tom.gom.adt.objects.types.Hook )tomMatch521_13) instanceof tom.gom.adt.objects.types.hook.MakeHook) ) {
 tom.gom.adt.objects.types.SlotFieldList  tom_args= tomMatch521_13.getHookArguments() ;

/* Rename the previous arguments according to new, if needed */
if(oArgs != null && oArgs != 
tom_args) {
recVarNameRemap(oArgs,
tom_args, writer);
}
/* Make sure we defeat java dead code detection */
writer.write("if (true) {");
CodeGen.generateCode(
 tomMatch521_13.getCode() ,writer);
writer.write("}");
return generateMakeHooks(
 (( tom.gom.adt.objects.types.HookList )((Object)other)).getTailConcHook() , 
tom_args, writer);


}
}
}
}
}

}


}

return oArgs;
}

private void recVarNameRemap(
SlotFieldList oargs,
SlotFieldList nargs,
java.io.Writer writer)
throws java.io.IOException {

{
{
if ( (((Object)oargs) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
if ( (( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs)).isEmptyConcSlotField() ) {
if ( (((Object)nargs) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
if ( (( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs)).isEmptyConcSlotField() ) {

return ;

}
}
}
}
}
}

}
{
if ( (((Object)oargs) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
if (!( (( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs)).isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch522_14= (( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs)).getHeadConcSlotField() ;
if ( (tomMatch522_14 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch522_14) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_oargName= tomMatch522_14.getName() ;
if ( (((Object)nargs) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
if (!( (( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs)).isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch522_18= (( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs)).getHeadConcSlotField() ;
if ( (tomMatch522_18 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch522_18) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_nargName= tomMatch522_18.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_ndomain= tomMatch522_18.getDomain() ;

if (!(
 tomMatch522_14.getDomain() ==
tom_ndomain)) {
throw new GomRuntimeException(
"OperatorTemplate: incompatible args "+
"should be rejected by typechecker");
} else if (!
tom_oargName.equals(
tom_nargName)) {
/* XXX: the declaration should be omitted if nargName was previously
* used */
writer.write(
"\n    "+fullClassName(
tom_ndomain)+
" "+
tom_nargName+
" = "+
tom_oargName+
";\n");
} /* else nothing to rename */
recVarNameRemap(
 (( tom.gom.adt.objects.types.SlotFieldList )((Object)oargs)).getTailConcSlotField() ,
 (( tom.gom.adt.objects.types.SlotFieldList )((Object)nargs)).getTailConcSlotField() , writer);
return;


}
}
}
}
}
}
}
}
}
}

}


}

throw new GomRuntimeException(
"OperatorTemplate:recVarNameRemap failed " + oargs + " " + nargs);
}

public void generateTomMapping(Writer writer)
throws java.io.IOException {

{
{
if ( (((Object)hooks) instanceof tom.gom.adt.objects.types.HookList) ) {
boolean tomMatch523_9= false ;
if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {
 tom.gom.adt.objects.types.HookList  tomMatch523__end__4=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
do {
{
if (!( tomMatch523__end__4.isEmptyConcHook() )) {
 tom.gom.adt.objects.types.Hook  tomMatch523_7= tomMatch523__end__4.getHeadConcHook() ;
if ( (tomMatch523_7 instanceof tom.gom.adt.objects.types.Hook) ) {
if ( ((( tom.gom.adt.objects.types.Hook )tomMatch523_7) instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {
tomMatch523_9= true ;
}
}
}
if ( tomMatch523__end__4.isEmptyConcHook() ) {
tomMatch523__end__4=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
} else {
tomMatch523__end__4= tomMatch523__end__4.getTailConcHook() ;
}

}
} while(!( (tomMatch523__end__4==(( tom.gom.adt.objects.types.HookList )((Object)hooks))) ));
}
if (!(tomMatch523_9)) {

writer.write("%op "+className(sortName)+" "+className()+"(");
slotDecl(writer,slotList);
writer.write(") {\n");
writer.write("  is_fsym(t) { ($t instanceof "+fullClassName()+") }\n");

{
{
if ( (((Object)slotList) instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch524__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
do {
{
if (!( tomMatch524__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch524_8= tomMatch524__end__4.getHeadConcSlotField() ;
if ( (tomMatch524_8 instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch524_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

writer.write("  get_slot("+
 tomMatch524_8.getName() +", t) ");
writer.write("{ $t."+getMethod(
 tomMatch524__end__4.getHeadConcSlotField() )+"() }\n");


}
}
}
if ( tomMatch524__end__4.isEmptyConcSlotField() ) {
tomMatch524__end__4=(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList));
} else {
tomMatch524__end__4= tomMatch524__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch524__end__4==(( tom.gom.adt.objects.types.SlotFieldList )((Object)slotList))) ));
}
}

}

}

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


}

}

}
{
if ( (((Object)hooks) instanceof tom.gom.adt.objects.types.HookList) ) {
if ( (((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )(( tom.gom.adt.objects.types.HookList )((Object)hooks))) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {
 tom.gom.adt.objects.types.HookList  tomMatch523__end__14=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
do {
{
if (!( tomMatch523__end__14.isEmptyConcHook() )) {
 tom.gom.adt.objects.types.Hook  tomMatch523_18= tomMatch523__end__14.getHeadConcHook() ;
if ( (tomMatch523_18 instanceof tom.gom.adt.objects.types.Hook) ) {
if ( ((( tom.gom.adt.objects.types.Hook )tomMatch523_18) instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {

CodeGen.generateCode(
 tomMatch523_18.getCode() ,writer);


}
}
}
if ( tomMatch523__end__14.isEmptyConcHook() ) {
tomMatch523__end__14=(( tom.gom.adt.objects.types.HookList )((Object)hooks));
} else {
tomMatch523__end__14= tomMatch523__end__14.getTailConcHook() ;
}

}
} while(!( (tomMatch523__end__14==(( tom.gom.adt.objects.types.HookList )((Object)hooks))) ));
}
}

}


}

return;
}
}
