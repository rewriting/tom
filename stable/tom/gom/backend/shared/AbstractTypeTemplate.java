/*
 * Gom
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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

public class AbstractTypeTemplate extends TemplateHookedClass {
  ClassNameList sortList;

  


  boolean maximalsharing;

  public AbstractTypeTemplate(File tomHomePath,
                              OptionManager manager,
                              List importList,
                              GomClass gomClass,
                              TemplateClass mapping,
                              boolean maximalsharing,
                              GomEnvironment gomEnvironment){
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.maximalsharing = maximalsharing;
    { /* unamed block */{ /* unamed block */if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass) ) {


        this.sortList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSortList() ;
        return;
      }}}}


    throw new GomRuntimeException(
        "Bad argument for AbstractTypeTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write(
"\npackage "+getPackage()+";\n"+generateImport()+"\n\n/**\n  * This class provides a skeletal implementation of <i>terms</i>\n  * When implementing the interface <tt>shared.SharedObjectWithID</tt>,\n  * the objects are immutable and can be compared in constant time, using\n  * <tt>==</tt>.\n  */\n"









);

    String implementsInterface;
    if(maximalsharing) {
      implementsInterface = "shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable";
    } else {
      implementsInterface = "tom.library.sl.Visitable, Cloneable, Comparable";
    }
    writer.write(
"\npublic abstract class "+className()+" implements "+implementsInterface+" "+generateInterface()+" {\n  /**\n   * Sole constructor.  (For invocation by subclass\n   * constructors, typically implicit.)\n   */\n  protected "+className()+"() {}\n"






);

    if(hooks.containsTomCode()) {
      mapping.generate(writer);
    }
    writer.write(
"\n"+generateBlock()+"\n"

);

    if(maximalsharing) {
      writer.write(
"\n  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();\n"

);
    }

writer.write(
"\n  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();\n\n  /**\n    * Returns an ATerm representation of this term.\n    *\n    * @return an ATerm representation of this term.\n    */\n  public abstract aterm.ATerm toATerm();\n\n  /**\n    * Returns a string representation of the top symbol of this term.\n    *\n    * @return a string representation of the top symbol of this term.\n    */\n  public abstract String symbolName();\n\n  /**\n    * Returns a string representation of this term.\n    *\n    * @return a string representation of this term.\n    */\n  @Override\n  public String toString() {\n    java.lang.StringBuilder buffer = new java.lang.StringBuilder();\n    toStringBuilder(buffer);\n    return buffer.toString();\n  }\n\n  /**\n    * Appends a string representation of this term to the buffer given as argument.\n    *\n    * @param buffer the buffer to which a string represention of this term is appended.\n    */\n  public abstract void toStringBuilder(java.lang.StringBuilder buffer);\n\n  /**\n    * Compares two terms. This functions implements a total order.\n    *\n    * @param o object to which this term is compared\n    * @return a negative integer, zero, or a positive integer as this\n    *         term is less than, equal to, or greater than the argument\n    */\n  public abstract int compareTo(Object o);\n\n  /**\n    * Compares two terms. This functions implements a total lexicographic path ordering.\n    *\n    * @param o object to which this term is compared\n    * @return a negative integer, zero, or a positive integer as this\n    *         term is less than, equal to, or greater than the argument\n    */\n  public abstract int compareToLPO(Object o);\n\n  /**\n    * Converts an ATerm into a string\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a string\n    */\n  protected static String convertATermToString(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermAppl) {\n      aterm.ATermAppl appl = (aterm.ATermAppl) at;\n      aterm.AFun fun = appl.getAFun();\n      if (fun.isQuoted() && fun.getArity() == 0 ) { // this test ensures that we have a String and not an \"exotic\" ATerm like this one : \"f\"(a)\n        return fun.getName();\n      }\n    }\n    throw new RuntimeException(\"Not a String : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into an int\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return an int\n    */\n  protected static int convertATermToInt(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermInt) {\n      aterm.ATermInt atint = (aterm.ATermInt) at;\n      return atint.getInt();\n    }\n    throw new RuntimeException(\"Not an Int : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into a float\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a float\n    */\n  protected static float convertATermToFloat(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermReal) {\n      aterm.ATermReal atfloat = (aterm.ATermReal) at;\n      return (float) atfloat.getReal();\n    }\n    throw new RuntimeException(\"Not a Float : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into a double\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a double\n    */\n  protected static double convertATermToDouble(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermReal) {\n      aterm.ATermReal atdouble = (aterm.ATermReal) at;\n      return atdouble.getReal();\n    }\n    throw new RuntimeException(\"Not a Double : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into a long\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a long\n    */\n  protected static long convertATermToLong(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermLong) {\n      aterm.ATermLong atlong = (aterm.ATermLong) at;\n      return atlong.getLong();\n    }\n    throw new RuntimeException(\"Not a Long : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into a boolean\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a boolean\n    */\n  protected static boolean convertATermToBoolean(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermInt) {\n      aterm.ATermInt atint = (aterm.ATermInt) at;\n      return (atint.getInt()==0?false:true);\n    }\n    throw new RuntimeException(\"Not a Boolean : \" + at);\n  }\n\n  /**\n    * Converts an ATerm into a char\n    *\n    * @param at ATerm to convert\n    * @param atConv converter applied to the ATerm before coersion\n    * @return a char\n    */\n  protected static char convertATermToChar(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {\n    at = atConv.convert(at);\n    if(at instanceof aterm.ATermInt) {\n      int atint = ((aterm.ATermInt)at).getInt();\n      atint = atint + (int)'0';\n      return (char) atint;\n    }\n    throw new RuntimeException(\"Not a Char : \" + at);\n  }\n\n"









































































































































































);

  if(maximalsharing) {
   writer.write(
"\n  /**\n    * from SharedObjectWithID\n    */\n  private int uniqueID;\n\n  /**\n    * Returns an integer that identifies this term in a unique way.\n    * <tt>t1.getUniqueIdentifier()==t2.getUniqueIdentifier()</tt> iff <tt>t1==t2</tt>.\n    *\n    * @return an integer that identifies this term in a unique way.\n    */\n  public int getUniqueIdentifier() {\n    return uniqueID;\n  }\n\n  /**\n    * Modifies the integer that identifies this term in a unique way.\n    *\n    * @param uniqueID integer that identifies this term in a unique way.\n    */\n  public void setUniqueIdentifier(int uniqueID) {\n    this.uniqueID = uniqueID;\n  }\n"























);
  } else {
    //implement the Cloneable interface and decalre a public method clone()
    writer.write(
"\n  public abstract Object clone();\n"

);
  }
  writer.write("}\n"); // end of class
}

}

