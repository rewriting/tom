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

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    
    writer.write(
%[
package @getPackage()@;
@generateImport()@
]%);

    if (maximalsharing) {
    writer.write(
%[
public abstract class @className()@ implements shared.SharedObjectWithID, tom.library.sl.Visitable, Comparable @generateInterface()@ {
]%);
} else {
    writer.write(
%[
public abstract class @className()@ implements tom.library.sl.Visitable, Cloneable, Comparable @generateInterface()@ {
]%);
}

    if (hooks.containsTomCode()) {
      mapping.generate(writer); 
    }
    writer.write(
%[
@generateBlock()@
]%);

    if (maximalsharing) {
    writer.write(
%[
  private int uniqueID;

  protected static final shared.SharedObjectFactory factory = shared.SingletonSharedObjectFactory.getInstance();
]%);
    }

writer.write(
%[
  protected static final aterm.ATermFactory atermFactory = aterm.pure.SingletonFactory.getInstance();

  public abstract aterm.ATerm toATerm();

  public abstract String symbolName();

  @@Override
  public String toString() {
    java.lang.StringBuilder buffer = new java.lang.StringBuilder();
    toStringBuilder(buffer);
    return buffer.toString();
  }

  public abstract void toStringBuilder(java.lang.StringBuilder buffer);

  public abstract int compareTo(Object o);

  public abstract int compareToLPO(Object o);

  public static String convertATermToString(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
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

  public static int convertATermToInt(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      return atint.getInt();
    }
    throw new RuntimeException("Not an Int : " + at);
  }

  public static float convertATermToFloat(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atfloat = (aterm.ATermReal) at;
      return (float) atfloat.getReal();
    }
    throw new RuntimeException("Not a Float : " + at);
  }

  public static double convertATermToDouble(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermReal) {
      aterm.ATermReal atdouble = (aterm.ATermReal) at;
      return atdouble.getReal();
    }
    throw new RuntimeException("Not a Double : " + at);
  }

  public static long convertATermToLong(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermLong) {
      aterm.ATermLong atlong = (aterm.ATermLong) at;
      return atlong.getLong();
    }
    throw new RuntimeException("Not a Long : " + at);
  }

  public static boolean convertATermToBoolean(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      aterm.ATermInt atint = (aterm.ATermInt) at;
      boolean atbool = (atint.getInt()==0?false:true);
      return atbool;
    }
    throw new RuntimeException("Not a Boolean : " + at);
  }

  public static char convertATermToChar(aterm.ATerm at, tom.library.utils.ATermConverter atConv) {
    at = atConv.convert(at);
    if(at instanceof aterm.ATermInt) {
      int atint = ((aterm.ATermInt)at).getInt();
      atint = atint + (int)'0';
      char atchar = (char) atint;
      return atchar;
    }
    throw new RuntimeException("Not a Char : " + at);
  }

]%);

if (maximalsharing) {
 writer.write(
%[
  public int getUniqueIdentifier() {
    return uniqueID;
  }

  public void setUniqueIdentifier(int uniqueID) {
    this.uniqueID = uniqueID;
  }
}
]%);
} else {
  //implement the Cloneable interface and decalre a public method clone()
 writer.write(
%[
 public abstract Object clone();
}
]%);
}
 }

}
