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

  %include { ../../adt/objects/Objects.tom }
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
    %match(gomClass) {
      AbstractTypeClass[SortList=sortList] -> {
        this.sortList = `sortList;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for AbstractTypeTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write(
%[
package @getPackage()@;
@generateImport()@

/**
  * This class provides a skeletal implementation of <i>terms</i>
  * When implementing the interface <tt>shared.SharedObjectWithID</tt>,
  * the objects are immutable and can be compared in constant time, using
  * <tt>==</tt>.
  */
]%);

    String implementsInterface;
    if(maximalsharing) {
      implementsInterface = "shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable";
    } else {
      implementsInterface = "tom.library.sl.Visitable, Cloneable, Comparable";
    }
    writer.write(
%[
public abstract class @className()@ implements @implementsInterface@ @generateInterface()@ {
  /**
   * Sole constructor.  (For invocation by subclass
   * constructors, typically implicit.)
   */
  protected @className()@() {}
]%);

    if(hooks.containsTomCode()) {
      mapping.generate(writer);
    }
    writer.write(
%[
@generateBlock()@
]%);

    if(maximalsharing) {
      writer.write(
%[
  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();
]%);
    }

writer.write(
%[
  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();

  /**
    * Returns an ATerm representation of this term.
    *
    * @@return an ATerm representation of this term.
    */
  public abstract aterm.ATerm toATerm();

  /**
    * Returns a string representation of the top symbol of this term.
    *
    * @@return a string representation of the top symbol of this term.
    */
  public abstract String symbolName();

  /**
    * Returns a string representation of this term.
    *
    * @@return a string representation of this term.
    */
  @@Override
  public String toString() {
    java.lang.StringBuilder buffer = new java.lang.StringBuilder();
    toStringBuilder(buffer);
    return buffer.toString();
  }

  /**
    * Appends a string representation of this term to the buffer given as argument.
    *
    * @@param buffer the buffer to which a string represention of this term is appended.
    */
  public abstract void toStringBuilder(java.lang.StringBuilder buffer);

  /**
    * Compares two terms. This functions implements a total order.
    *
    * @@param o object to which this term is compared
    * @@return a negative integer, zero, or a positive integer as this
    *         term is less than, equal to, or greater than the argument
    */
  public abstract int compareTo(Object o);

  /**
    * Compares two terms. This functions implements a total lexicographic path ordering.
    *
    * @@param o object to which this term is compared
    * @@return a negative integer, zero, or a positive integer as this
    *         term is less than, equal to, or greater than the argument
    */
  public abstract int compareToLPO(Object o);

  /**
    * Converts an ATerm into a string
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a string
    */
  protected static String convertATermToString(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) at;
      aterm.AFun fun = appl.getAFun();
      if (fun.isQuoted() && fun.getArity() == 0 ) { // this test ensures that we have a String and not an "exotic" ATerm like this one : "f"(a)
        return fun.getName();
      }
    }
    throw new RuntimeException("Not a String : " + at);
  }

  /**
    * Converts an ATerm into an int
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return an int
    */
  protected static int convertATermToInt(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      return atint.getInt();
    }
    throw new RuntimeException("Not an Int : " + at);
  }

  /**
    * Converts an ATerm into a float
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a float
    */
  protected static float convertATermToFloat(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atfloat = (aterm.ATermReal) at;
      return (float) atfloat.getReal();
    }
    throw new RuntimeException("Not a Float : " + at);
  }

  /**
    * Converts an ATerm into a double
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a double
    */
  protected static double convertATermToDouble(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atdouble = (aterm.ATermReal) at;
      return atdouble.getReal();
    }
    throw new RuntimeException("Not a Double : " + at);
  }

  /**
    * Converts an ATerm into a long
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a long
    */
  protected static long convertATermToLong(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermLong) {
      aterm.ATermLong atlong = (aterm.ATermLong) at;
      return atlong.getLong();
    }
    throw new RuntimeException("Not a Long : " + at);
  }

  /**
    * Converts an ATerm into a boolean
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a boolean
    */
  protected static boolean convertATermToBoolean(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      return (atint.getInt()==0?false:true);
    }
    throw new RuntimeException("Not a Boolean : " + at);
  }

  /**
    * Converts an ATerm into a char
    *
    * @@param at ATerm to convert
    * @@param atConv converter applied to the ATerm before coersion
    * @@return a char
    */
  protected static char convertATermToChar(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      int atint = ((aterm.ATermInt)at).getInt();
      atint = atint + (int)'0';
      return (char) atint;
    }
    throw new RuntimeException("Not a Char : " + at);
  }

]%);

  if(maximalsharing) {
   writer.write(
%[
  /**
    * from SharedObjectWithID
    */
  private int uniqueID;

  /**
    * Returns an integer that identifies this term in a unique way.
    * <tt>t1.getUniqueIdentifier()==t2.getUniqueIdentifier()</tt> iff <tt>t1==t2</tt>.
    *
    * @@return an integer that identifies this term in a unique way.
    */
  public int getUniqueIdentifier() {
    return uniqueID;
  }

  /**
    * Modifies the integer that identifies this term in a unique way.
    *
    * @@param uniqueID integer that identifies this term in a unique way.
    */
  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }
]%);
  } else {
    //implement the Cloneable interface and decalre a public method clone()
    writer.write(
%[
  public abstract Object clone();
]%);
  }
  writer.write("}\n"); // end of class
}

}

