/*
 * Gom
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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

  %include { ../../adt/objects/Objects.tom}

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
    %match(gomClass) {
      OperatorClass[AbstractType=abstractType,
                    ExtendsType=extendsType,
                    SortName=sortName,
                    SlotFields=slots,
                    Comments=comments] -> {
        this.abstractType = `abstractType;
        this.extendsType = `extendsType;
        this.sortName = `sortName;
        this.variadicconstructor =
          // add a condition to detect variadic operators with the same name as their sort
          ! sortName.equals(extendsType) &&
          gomEnvironment.getSymbolTable().isVariadic(extendsType.getName());
        this.slotList = `slots;
        this.comments = `comments;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for OperatorTemplate: " + gomClass);
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
@generateComments()@
public final class @className()@ extends @fullClassName(extendsType)@ implements tom.library.sl.Visitable @generateInterface()@ {
  @generateBlock()@
  private static String symbolName = "@className()@";
]%);


  if(slotList.length() > 0) {
    writer.write(%[

  private @className()@() {}
  private int hashCode;
  private static @className()@ gomProto = new @className()@();
  ]%); 
    if(variadicconstructor) {
      /* get the domain type of the constructor */
      %match(slotList) {
        ConcSlotField(SlotField[Domain=domainclass],_*) -> {
          if (!getGomEnvironment().isBuiltinClass(`domainclass)) {
            writer.write(%[
   private @fullClassName(`domainclass)@[] children;
                ]%);
          } else {
            writer.write(%[
   private tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(fullClassName(`domainclass))@>[] children;
   ]%);
          }
        }
      }
    } 
  } else {
    writer.write(%[

  private @className()@() {}
  private static int hashCode = hashFunction();
  private static @className()@ gomProto = (@className()@) factory.build(new @className()@());
  ]%);
  }
} else {
  writer.write(
%[
@generateComments()@
public final class @className()@ extends @fullClassName(extendsType)@ implements Cloneable, tom.library.sl.Visitable @generateInterface()@ {
  @generateBlock()@
  private static String symbolName = "@className()@";
]%);
    if(variadicconstructor) {
      /* get the domain type of the constructor */
      %match(slotList) {
        ConcSlotField(SlotField[Domain=domainclass],_*) -> {
       if (!getGomEnvironment().isBuiltinClass(`domainclass)) {
          writer.write(%[
   private @fullClassName(`domainclass)@[] children;
          ]%);
        } else {
          writer.write(%[
   private tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(fullClassName(`domainclass))@>[] children;
          ]%);
        }
        }
      }
    }

// generate a private constructor that takes as arguments the operator arguments
  writer.write(%[

  private @className()@(@childListWithType(slotList)@) {
  ]%);
  generateMembersInit(writer);
  writer.write(%[
  }
  ]%);
}

  if (hooks.containsTomCode()) {
    mapping.generate(writer);
  }
  generateMembers(writer);
  generateBody(writer);
  writer.write(%[
}
]%);
  }

  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    if (!comments.equals("")) {
      writer.write(%[
  @generateComments()@
        ]%);
    } else {
    writer.write(%[
  /**
   * Constructor that builds a term rooted by @className()@
   *
   * @@return a term rooted by @className()@
   */
]%);
    }
generateConstructor(writer);

if(slotList.length()>0) {

if (maximalsharing) {
writer.write(%[
  /**
   * Initializes attributes and hashcode of the class
   *
   * @@param @childListOnePerLine(slotList)@
   * @@param hashCode hashCode of @className()@
   */
  private void init(@childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":", ") @int hashCode) {
]%);
generateMembersInit(writer);
writer.write(%[
    this.hashCode = hashCode;
  }

  /**
   * Initializes attributes and hashcode of the class
   *
   * @@param @childListOnePerLine(slotList)@
   */
  private void initHashCode(@childListWithType(slotList)@) {
]%);
generateMembersInit(writer);
writer.write(%[
    this.hashCode = hashFunction();
  }
]%);
}

writer.write(%[
  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @@return the name of the symbol
   */
  @@Override
  public String symbolName() {
    return "@className()@";
  }

  /**
   * Returns the arity of the symbol
   *
   * @@return the arity of the symbol
   */
  private int getArity() {
    return @slotList.length()@;
  }
]%);


if (maximalsharing) {
  writer.write(%[
  /**
   * Copy the object and returns the copy
   *
   * @@return a clone of the SharedObject
   */]%);
if(multithread) {
  writer.write(%[
  public shared.SharedObject duplicate() {
    // the proto is a fresh object: no need to clone it again
    return this;
  }
  ]%);
} else {
  writer.write(%[
  public shared.SharedObject duplicate() {
    @className()@ clone = new @className()@();
    clone.init(@childList(slotList) + (slotList.isEmptyConcSlotField()?"":", ") @hashCode);
    return clone;
  }
  ]%);
 }
}

} else {
    // case: constant
writer.write(%[
  /* name and arity */

  /**
   * Returns the name of the symbol
   *
   * @@return the name of the symbol
   */
  @@Override
  public String symbolName() {
    return "@className()@";
  }

  /**
   * Returns the arity of the symbol
   *
   * @@return arity of the symbol
   */
  private static int getArity() {
    return 0;
  }

]%);

if (maximalsharing) {
writer.write(%[
  /**
   * Copy the object and returns the copy
   *
   * @@return a clone of the SharedObject
   */
  public shared.SharedObject duplicate() {
    // the proto is a constant object: no need to clone it
    return this;
    //return new @className()@();
  }
]%);
}

  }

  /*
   * Generate a toStringBuilder method if the operator is not associative
   */
  if (sortName == extendsType) {
writer.write(%[
  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @@param buffer the buffer to which a string represention of this term is appended.
   */
  @@Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("@className()@(");
    @toStringChildren("buffer")@
    buffer.append(")");
  }
]%);
  }

writer.write(%[

  /**
   * Compares two terms. This functions implements a total lexicographic path ordering.
   *
   * @@param o object to which this term is compared
   * @@return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @@throws ClassCastException in case of invalid arguments
   * @@throws RuntimeException if unable to compare children
   */
  @@Override
  public int compareToLPO(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    @fullClassName(abstractType)@ ao = (@fullClassName(abstractType)@) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* compare the symbols */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* compare the children */
    @genCompareChildren("ao","compareToLPO")@
    throw new RuntimeException("Unable to compare");
  }
]%);

if (maximalsharing) {
writer.write(%[
 /**
   * Compares two terms. This functions implements a total order.
   *
   * @@param o object to which this term is compared
   * @@return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @@throws ClassCastException in case of invalid arguments
   * @@throws RuntimeException if unable to compare children
   */
  @@Override
  public int compareTo(Object o) {
    /*
     * We do not want to compare with any object, only members of the module
     * In case of invalid argument, throw a ClassCastException, as the java api
     * asks for it
     */
    @fullClassName(abstractType)@ ao = (@fullClassName(abstractType)@) o;
    /* return 0 for equality */
    if (ao == this) { return 0; }
    /* use the hash values to discriminate */

    if(hashCode != ao.hashCode()) { return (hashCode < ao.hashCode())?-1:1; }

    /* If not, compare the symbols : back to the normal order */
    int symbCmp = this.symbolName().compareTo(ao.symbolName());
    if (symbCmp != 0) { return symbCmp; }
    /* last resort: compare the children */
    @genCompareChildren("ao","compareTo")@
    throw new RuntimeException("Unable to compare");
  }

 //shared.SharedObject
  /**
   * Returns hashCode
   *
   * @@return hashCode
   */
  @@Override
  public final int hashCode() {
    return hashCode;
  }

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @@param obj SharedObject to test
   * @@return true if obj is a @className()@ and its members are equal, else false
   */
  public final boolean equivalent(shared.SharedObject obj) {
    if(obj instanceof @className()@) {
@generateMembersEqualityTest("peer")@
    }
    return false;
  }

]%);
} else {
  //XXX: compareTo must be correctly implemented
writer.write(%[
  /**
   * Compares two terms. This functions implements a total order.
   *
   * @@param o object to which this term is compared
   * @@return a negative integer, zero, or a positive integer as this
   *         term is less than, equal to, or greater than the argument
   * @@throws ClassCastException in case of invalid arguments
   * @@throws RuntimeException if unable to compare children
   */
  @@Override
  public int compareTo(Object o) {
    throw new UnsupportedOperationException("Unable to compare");
  }

  /**
   * Clones the object
   *
   * @@return the copy
   */
  @@Override
  public Object clone() {
]%);

SlotFieldList slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write(%[
      return new @className()@();
  }
      ]%);
} else {

SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
  writer.write(%[
      return new @className()@(@getMethod(head)@()]%);

} else {
  writer.write(%[
      return new @className()@( (@fullClassName(head.getDomain())@) @getMethod(head)@().clone()]%);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
   writer.write(%[,@getMethod(head)@()]%);
  } else {
  writer.write(%[,(@fullClassName(head.getDomain())@) @getMethod(head)@().clone()]%);
  }
}
writer.write(");\n}");
}

writer.write(%[
  /**
   * Checks if an object is strictly equal to the current object
   *
   * @@param o object to compare
   * @@return true if each member is equal, else false
   */
  @@Override
  public final boolean deepEquals(Object o) {
    if (o instanceof @className()@) {
      @className()@ typed_o = (@className()@) o;
]%);

slots = slotList;
if(slots.isEmptyConcSlotField()) {
  writer.write(%[
      return true;
      ]%);
} else {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();
if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
  writer.write(%[
      return @fieldName(head.getName())@ == typed_o.@getMethod(head)@()
      ]%);

} else {
  writer.write(%[
      return @fieldName(head.getName())@.deepEquals(typed_o.@getMethod(head)@())
      ]%);
}

while(!slots.isEmptyConcSlotField()) {
  head = slots.getHeadConcSlotField();
  slots = slots.getTailConcSlotField();
  if (getGomEnvironment().isBuiltinClass(head.getDomain())) {
    writer.write(%[
        && @fieldName(head.getName())@ == typed_o.@getMethod(head)@()
        ]%);

  } else {
    writer.write(%[
      && @fieldName(head.getName())@.deepEquals(typed_o.@getMethod(head)@())
      ]%);
  }
}
writer.write(";");
}
writer.write(%[
    }
    return false;
    }
  ]%);
}

writer.write(%[
   //@className(sortName)@ interface
  /**
   * Returns true if the term is rooted by the symbol @className.getName()@
   *
   * @@return true, because this is rooted by @className.getName()@
   */
  @@Override
  public boolean @isOperatorMethod(className)@() {
    return true;
  }
  ]%);

generateGetters(writer);

    writer.write(%[
  /* AbstractType */
  /**
   * Returns an ATerm representation of this term.
   *
   * @@return an ATerm representation of this term.
   */
  @@Override
  public aterm.ATerm toATerm() {
    aterm.ATerm res = super.toATerm();
    if(res != null) {
      // the super class has produced an ATerm (may be a variadic operator)
      return res;
    }
    return atermFactory.makeAppl(
      atermFactory.makeAFun(symbolName(),getArity(),false),
      new aterm.ATerm[] {@generateToATermChildren()@});
  }

  /**
   * Apply a conversion on the ATerm contained in the String and returns a @fullClassName(sortName)@ from it
   *
   * @@param trm ATerm to convert into a Gom term
   * @@param atConv ATerm Converter used to convert the ATerm
   * @@return the Gom term
   */
  public static @fullClassName(sortName)@ fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {
    trm = atConv.convert(trm);
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if(symbolName.equals(appl.getName()) && !appl.getAFun().isQuoted()) {
        return make(
@generatefromATermChildren("appl","atConv")@
        );
      }
    }
    return null;
  }
]%);

    writer.write(%[
  /* Visitable */
  /**
   * Returns the number of children of the term
   *
   * @@return the number of children of the term
   */
  public int getChildCount() {
    return @visitableCount()@;
  }

  /**
   * Returns the child at the specified index
   *
   * @@param index index of the child to return; must be
             nonnegative and less than the childCount
   * @@return the child at the specified index
   * @@throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable getChildAt(int index) {
    @getchildat()@
 }

  /**
   * Set the child at the specified index
   *
   * @@param index index of the child to set; must be
             nonnegative and less than the childCount
   * @@param v child to set at the specified index
   * @@return the child which was just set
   * @@throws IndexOutOfBoundsException if the index out of range
   */
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable v) {
    @setchildat("v")@
  }

  /**
   * Set children to the term
   *
   * @@param children array of children to set
   * @@return an array of children which just were set
   * @@throws IndexOutOfBoundsException if length of "children" is different than @slotList.length()@
   */
  @@SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    if (children.length == getChildCount() @arrayCheck("children")@) {
      @arrayMake("children")@
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Returns the whole children of the term
   *
   * @@return the children of the term
   */
  public tom.library.sl.Visitable[] getChildren() {
    @getchildren(slotList)@
  }
]%);

if(maximalsharing) {
  // OLD VERSION
    writer.write(%[
    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @@return a hash value
     */
  protected@((slotList.length()==0)?" static":"")@ int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    b = (@shared.HashFunctions.stringHashFunction(fullClassName(),slotList.length())@<<8);
    c = getArity();
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
]%);
generateHashArgs(writer);
writer.write(%[
    a -= b; a -= c; a ^= (c >> 13);
    b -= c; b -= a; b ^= (a << 8);
    c -= a; c -= b; c ^= (b >> 13);
    a -= b; a -= c; a ^= (c >> 12);
    b -= c; b -= a; b ^= (a << 16);
    c -= a; c -= b; c ^= (b >> 5);
    a -= b; a -= c; a ^= (c >> 3);
    b -= c; b -= a; b ^= (a << 10);
    c -= a; c -= b; c ^= (b >> 15);
    /* ------------------------------------------- report the result */
    return c;
  }
]%);
}

if(false && maximalsharing) {
  // NEW VERSION: http://burtleburtle.net/bob/c/lookup3.c
  // seems to be a bit slower than the OLD version
  int length = slotList.length();
    writer.write(%[
    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @@return a hash value
     */
  protected@((length==0)?" static":"")@ int hashFunction() {
    int a, b, c;
    /* Set up the internal state */
    a = b = c =
    0xdeadbeef + (getArity()<<2) +
    (@shared.HashFunctions.stringHashFunction(fullClassName(),length)@<<8);
    /* -------------------------------------- handle most of the key */
    /* ------------------------------------ handle the last 11 bytes */
]%);
generateHashArgsLookup3(writer);
  if(length>0 && (length%3)>0 ) {
writer.write(%[
    // final(a,b,c)
    c ^= b; c -= (((b)<<(14)) | ((b)>>(32-(14))));
    a ^= c; a -= (((c)<<(11)) | ((c)>>(32-(11))));
    b ^= a; b -= (((a)<<(25)) | ((a)>>(32-(25))));
    c ^= b; c -= (((b)<<(16)) | ((b)>>(32-(16))));
    a ^= c; a -= (((c)<<(4)) | ((c)>>(32-(4))));
    b ^= a; b -= (((a)<<(14)) | ((a)>>(32-(14))));
    c ^= b; c -= (((b)<<(24)) | ((b)>>(32-(24))));
]%);
  }

writer.write(%[
    /* ------------------------------------------- report the result */
    return c;
  }
]%);
}

  generateEnum(writer);

}

private void generateEnum(java.io.Writer writer) throws java.io.IOException {
  final String P = "tom.library.enumerator.";
  if(slotList.length() == 0) {
    String E = "tom.library.enumerator.Enumeration<" + fullClassName(sortName) + ">";
    writer.write(
%[
  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static @P@F<@E@, @E@> funMake() {
    return new @P@F<@E@, @E@>() {
      public @E@ apply(final @E@ e) {
        return @P@Enumeration.singleton((@fullClassName(sortName)@) make());
      }
    };
  }
]%);
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
        for(int i=1 ; i<index ; i++) { sapply = %[@P@Enumeration.apply(@sapply@,t@i@)]%; }
        if(index>0) {
          sapply = sapply + ".pay()";
        }
        return sapply;
      }
    };

    writer.write(
%[
  /**
    * function that returns functional version of the current operator
    * need for initializing the Enumerator
    */
  public static @buildFunctionDomain(true,slotList,sortName)@ funMake() {
    return @buildNestedFunction(true,slotList,sortName,1,genBody)@;
  }
]%);
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
      return %[
        new @buildFunctionDomain(withEnumeration,slots,codomain)@() {
          public @buildFunctionDomain(withEnumeration,tail,codomain)@ apply(final @domain@ t@index@) {
            return @buildNestedFunction(withEnumeration,tail, codomain, index+1, genBody)@;
          }
        }]%;
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
    if (!comments.equals("") && comments.contains("@param")) {
      // "@param chaine " -> "@param _chaine "
      return comments.replaceAll("@param ","@param _");
    } else {
      return comments;
    }
  }

  private void generateMembers(java.io.Writer writer) throws java.io.IOException {
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domainClass],_*) -> {
        writer.write("  private ");
        writer.write(fullClassName(`domainClass));
        writer.write(" ");
        writer.write(fieldName(`fieldName));
        writer.write(";\n");
      }
    }
  }

  private void generateMembersInit(java.io.Writer writer) throws java.io.IOException {
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        writer.write("    this.");
        writer.write(fieldName(`fieldName));
        writer.write(" = ");
        writer.write(fieldName(`fieldName));
        if (getGomEnvironment().isBuiltinClass(`domain) && `domain.equals(`ClassName("","String"))) {
          writer.write(".intern()");
        }
        writer.write(";\n");
      }
    }
  }

  private void generateGetters(java.io.Writer writer) throws java.io.IOException {
    SlotFieldList slots = slotList;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      writer.write(%[
  /**
   * Returns the attribute @slotDomain(head)@
   *
   * @@return the attribute @slotDomain(head)@
   */
  @@Override
  public @slotDomain(head)@ @getMethod(head)@() {
    return @fieldName(head.getName())@;
  }

  /**
   * Sets and returns the attribute @fullClassName(sortName)@
   *
   * @@param set_arg the argument to set
   * @@return the attribute @slotDomain(head)@ which just has been set
   */]%);
      if (maximalsharing) {
        writer.write(%[
  @@Override
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    return make(@generateMakeArgsFor(head,"set_arg")@);
  }
  ]%);
      } else {
        writer.write(%[
  @@Override
  public @fullClassName(sortName)@ @setMethod(head)@(@slotDomain(head)@ set_arg) {
    @fieldName(head.getName())@ = set_arg;
    return this;
  }]%);
      }

      if(jmicompatible) {
        // generate getters and setters where slot-names are normalized
        if(!getMethod(head,true).equals(getMethod(head))) {
      writer.write(%[
  public @slotDomain(head)@ @getMethod(head,true)@() {
    return @getMethod(head)@();
  }
  ]%);
        }
        if(!setMethod(head,true).equals(setMethod(head))) {
      writer.write(%[
  public @fullClassName(sortName)@ @setMethod(head,true)@(@slotDomain(head)@ set_arg) {
    return @setMethod(head)@(set_arg);
  }
  ]%);
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
      %match(head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(fullClassName(`domain));
          res.append(" ");
          res.append(fieldName(`name));
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
      %match(head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(fullClassName(`domain));
          res.append(" ");
          res.append(`name);
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
      %match(head) {
        SlotField[Name=name] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(fieldName(`name));
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
      %match(head) {
        SlotField[Name=name] -> {
          if (res.length()!=0) {
            res.append(", ");
          }
          res.append(" ");
          res.append(`name);
        }
      }
    }
    return res.toString();
  }
  private String generateMembersEqualityTest(String peer) {
    StringBuilder res = new StringBuilder();
    if(!slotList.isEmptyConcSlotField()) {
      res.append(%[
      @className()@ peer = (@className()@) obj;]%);;
    }
    res.append(%[
      return ]%);
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName],_*) -> {
        res.append(fieldName(`fieldName));
        res.append("==");
        res.append(peer);
        res.append(".");
        res.append(fieldName(`fieldName));
        res.append(" && ");
      }
    }
    res.append("true;"); // to handle the "no children" case
    return res.toString();
  }

  private String getchildat() {
    if (variadicconstructor) {
      // use the methods of the Collection interface
      return "return getChildren()[index];";
    } else {
      StringBuilder res = new StringBuilder("    switch(index) {\n");
      int index = 0;
      %match(slotList) {
        ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
            res.append("      case ");
            res.append(index);
            res.append(": return ");
            res.append(fieldName(`fieldName));
            res.append(";\n");
            index++;
          } else {
            res.append("      case ");
            res.append(index);
            res.append(": return new tom.library.sl.VisitableBuiltin<");
            res.append(primitiveToReferenceType(fullClassName(`domain)));
            res.append(">(");
            res.append(fieldName(`fieldName));
            res.append(");\n");
            index++;
          }
        }
      }
      res.append("      default: throw new IndexOutOfBoundsException();\n }");
      return res.toString();
    }
  }

  private String getchildren(SlotFieldList slots) {
    if (variadicconstructor) {
      // use the methods of the Collection interface
      if (className.getName().startsWith("Cons")) {
        %match(slotList) {
          ConcSlotField(SlotField[Domain=domainclass],_*) -> {
            if (!getGomEnvironment().isBuiltinClass(`domainclass)) {
              return %[
        if (children == null) {
          children = toArray(new @fullClassName(`domainclass)@[]{});
        }
        return java.util.Arrays.copyOf(children,children.length);
      ]%;
            } else {
              String reference_type = primitiveToReferenceType(fullClassName(`domainclass));
              return %[
        if (children == null) {
          @reference_type@[] object_children = toArray(new @reference_type@[]{});
          children = new tom.library.sl.VisitableBuiltin[object_children.length];
          for (int i=0; i<object_children.length; i++) {
              children[i] = new tom.library.sl.VisitableBuiltin(object_children[i]);
          }
        }
        return java.util.Arrays.copyOf(children,children.length);
      ]%;
            }
          }
        }
      } 
      // default case: empty constructor
      return %[
        return new tom.library.sl.Visitable[]{};
      ]%;
    } else {
      StringBuilder res = new StringBuilder();
      res.append("return new tom.library.sl.Visitable[] {");
      boolean is_first_slot = true;
      while(!slots.isEmptyConcSlotField()) {
        SlotField head = slots.getHeadConcSlotField();
        slots = slots.getTailConcSlotField();
        %match(head) {
          SlotField[Domain=domain,Name=name] -> {
            if (is_first_slot) {
              is_first_slot =  false;
            } else {
              res.append(", ");
            }
            res.append(" ");
            if (!getGomEnvironment().isBuiltinClass(`domain)) {
              res.append(fieldName(`name));
            } else {
              res.append("new tom.library.sl.VisitableBuiltin<");
              res.append(primitiveToReferenceType(fullClassName(`domain)));
              res.append(">(");
              res.append(fieldName(`name));
              res.append(")");
            }
          }
        }
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
      if (className.getName().startsWith("Cons")) {
        /* get the domain type of the constructor */
        %match(slotList) {
          ConcSlotField(SlotField[Domain=domainclass],_*) -> {
            if (!getGomEnvironment().isBuiltinClass(`domainclass)) {
              return %[
               @fullClassName(`domainclass)@[] typed_children = new @fullClassName(`domainclass)@[children.length];
              for (int i=0; i<children.length; i++) {
                typed_children[i] = (@fullClassName(`domainclass)@) children[i]; 
              }
              return fromArray(typed_children);
              ]%;
            } else {
              String builtin_type = fullClassName(`domainclass);
              return %[
               @builtin_type@[] builtin_children = new @builtin_type@[children.length];
               for (int i=0; i<children.length; i++) {
                 builtin_children[i] = ((tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(builtin_type)@>) children[i]).getBuiltin(); 
               }
               return fromArray(builtin_children);
              ]%;
            }
          }
        }
      } 
      /* Empty constructor */
      return "return this;";
    } else { 
      StringBuilder res = new StringBuilder("return make(");
      int index = 0;
      %match(slotList) {
        ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
          if(index>0) { res.append(", "); }
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
            res.append("(");
            res.append(fullClassName(`domain));
            res.append(") ");
            res.append(arrayName);
            res.append("[");
            res.append(index);
            res.append("]");
          } else {
            res.append("((tom.library.sl.VisitableBuiltin<");
            res.append(primitiveToReferenceType(fullClassName(`domain)));
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
      res.append(");");
      return res.toString();
    }
  }

  private String arrayCheck(String arrayName) {
    StringBuilder res = new StringBuilder();
    if(variadicconstructor) {
      if (className.getName().startsWith("Cons")) {
        /* get the domain type of the constructor */
        %match(slotList) {
          ConcSlotField(SlotField[Domain=domain],_*) -> {
              //TODO test that each child is of type domain
              // this has to be done dynamically (we do not know statically the
              //number of children)
              return "";
            }
          }
      } else {
        /* empty constructor */ 
        return "";
      }
    } else {
      int index = 0;
      %match(slotList) {
        ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
          if (!getGomEnvironment().isBuiltinClass(`domain)) {
            res.append(%[ && @arrayName@[@index@] instanceof @fullClassName(`domain)@]%);
          } else {
            res.append(%[ && @arrayName@[@index@] instanceof tom.library.sl.VisitableBuiltin]%);
          }
          index++;
        }
      }
    }
    return res.toString();
  }

private String setchildat(String argName) {
  if(variadicconstructor) {
    if (className.getName().startsWith("Cons")) {
    /* get the domain type of the constructor */
     %match(slotList) {
      ConcSlotField(SlotField[Domain=domainclass],_*) -> {
        String domain = fullClassName(`domainclass); 
        StringBuilder res = new StringBuilder();
        String class_name = fullClassName(`domainclass);
            if (!getGomEnvironment().isBuiltinClass(`domainclass)) {
              res.append(%[
      tom.library.sl.Visitable[] children = getChildren();
      @class_name@[] new_children = new @class_name@[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((@class_name@) children[i]); 
      }
     new_children[index] = (@domain@) @argName@;
     return fromArray(new_children);
                  ]%);
            } else {
              res.append(%[
      tom.library.sl.Visitable[] children = getChildren();
      @class_name@[] new_children = new @class_name@[children.length];
      for(int i =0; i<children.length; i++) {
        new_children[i] = ((tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(class_name)@>) children[i]).getBuiltin(); 
      }
      new_children[index] = ((tom.library.sl.VisitableBuiltin<@primitiveToReferenceType(class_name)@>) @argName@).getBuiltin();
      return fromArray(new_children);
                  ]%);
            }
            return res.toString();
      }
     }
    }
    /* Empty constructor */
    return "throw new IndexOutOfBoundsException();";
  } else {
    StringBuilder res = new StringBuilder("    switch(index) {\n");
    int index = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[],_*) -> {
        res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
        index++;
      }
    }
    res.append("      default: throw new IndexOutOfBoundsException();\n }");
    return res.toString();
  }
}

private String generateMakeArgsFor(int argIndex, String argName) {
  StringBuilder res = new StringBuilder();
  int index = 0;
  %match(slotList) {
    ConcSlotField(_*,slot@SlotField[Name=fieldName,Domain=domain],_*) -> {
      if(index>0) { res.append(", "); }
      if (getGomEnvironment().isBuiltinClass(`domain)) {
        res.append(getMethod(`slot));
        res.append("()");
      } else {
        if (index != argIndex) {
          res.append(fieldName(`fieldName));
        } else {
          res.append("(");
          res.append(fullClassName(`domain));
          res.append(") ");
          res.append(argName);
        }
      }
      index++;
    }
  }
  return res.toString();
}
private String generateMakeArgsFor(SlotField slot, String argName) {
  StringBuilder res = new StringBuilder();
  int fullindex = 0;
  %match(slotList) {
    ConcSlotField(_*,itslot@SlotField[Name=fieldName],_*) -> {
      if(fullindex>0) { res.append(", "); }
      if (`itslot == slot) {
        res.append(argName);
      } else {
        res.append(fieldName(`fieldName));
      }
      fullindex++;
    }
  }
  return res.toString();
}

  private String toStringChildren(String buffer) {
    if (0 == slotList.length()) {
      return "";
    }
    StringBuilder res = new StringBuilder();
    SlotFieldList slots = slotList;
		while(!slots.isEmptyConcSlotField()) {
			if(res.length()!=0) {
				res.append(%[@buffer@.append(",");
    ]%);
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
    res.append(%[@className()@ @other@ = (@className()@) @oldOther@;]%);
    }
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        if (getGomEnvironment().isBuiltinClass(`domain)) {
         if (`domain.equals(`ClassName("","int"))
             || `domain.equals(`ClassName("","long"))
             || `domain.equals(`ClassName("","double"))
             || `domain.equals(`ClassName("","float"))
             || `domain.equals(`ClassName("","char"))) {
           res.append(%[
    if( this.@fieldName(`slotName)@ != @other@.@fieldName(`slotName)@) {
      return (this.@fieldName(`slotName)@ < @other@.@fieldName(`slotName)@)?-1:1;
    }
]%);
         } else if (`domain.equals(`ClassName("","boolean"))) {
           res.append(%[
    if( this.@fieldName(`slotName)@ != @other@.@fieldName(`slotName)@) {
      return (!this.@fieldName(`slotName)@ && @other@.@fieldName(`slotName)@)?-1:1;
    }
]%);
         } else if (`domain.equals(`ClassName("","String"))) {
           res.append(%[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).compareTo(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0) {
      return @fieldName(`slotName)@Cmp;
    }

]%);
         } else if (`domain.equals(`ClassName("aterm","ATerm"))
             ||`domain.equals(`ClassName("aterm","ATermList"))) {
           res.append(%[
    /* Inefficient total order on ATerm */
    int @fieldName(`slotName)@Cmp = ((this.@fieldName(`slotName)@).toString()).compareTo((@other@.@fieldName(`slotName)@).toString());
    if(@fieldName(`slotName)@Cmp != 0) {
      return @fieldName(`slotName)@Cmp;
    }
]%);
         } else {
            throw new GomRuntimeException("Builtin "+`domain+" not supported");
         }
        } else {
          res.append(%[
    int @fieldName(`slotName)@Cmp = (this.@fieldName(`slotName)@).@compareFun@(@other@.@fieldName(`slotName)@);
    if(@fieldName(`slotName)@Cmp != 0) {
      return @fieldName(`slotName)@Cmp;
    }
]%);
        }
      }
    }
    return res.toString();
  }

  private void generateHashArgs(java.io.Writer writer) throws java.io.IOException {
    int index = slotList.length() - 1;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        int shift = (index % 4) * 8;
        String accum = ""+"aaaabbbbcccc".toCharArray()[index % 12];
        writer.write("    "+accum+" += (");
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          writer.write(fieldName(`slotName)+".hashCode()");
        } else {
          if (`domain.equals(`ClassName("","int"))
              || `domain.equals(`ClassName("","long"))
              || `domain.equals(`ClassName("","float"))
              || `domain.equals(`ClassName("","char"))) {
            writer.write(fieldName(`slotName));
          } else if (`domain.equals(`ClassName("","boolean"))) {
            writer.write("("+fieldName(`slotName)+"?1:0)");
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")");
          } else if (`domain.equals(`ClassName("","double"))) {
            writer.write("(int)(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")^(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")>>>32");
            writer.write("))");
          } else if (`domain.equals(`ClassName("aterm","ATerm"))||`domain.equals(`ClassName("aterm","ATermList"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write(fieldName(`slotName)+".hashCode()");
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + `domain + " not supported");
          }
        }
        if (shift!=0) { writer.write(" << "+(shift)); }
        writer.write(");\n");
        index--;
      }
    }
  }

  private void generateHashArgsLookup3(java.io.Writer writer) throws java.io.IOException {
    int k=0;
    int index = slotList.length() - 1;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=slotName,Domain=domain],_*) -> {
        k++;
        switch(k % 3) {
          case 1: writer.write("    a += ("); break;
          case 2: writer.write("    b += ("); break;
          case 0: writer.write("    c += ("); break;
          default:
        }
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          writer.write(fieldName(`slotName)+".hashCode()");
        } else {
          if (`domain.equals(`ClassName("","int"))
              || `domain.equals(`ClassName("","long"))
              || `domain.equals(`ClassName("","float"))
              || `domain.equals(`ClassName("","char"))) {
            writer.write(fieldName(`slotName));
          } else if (`domain.equals(`ClassName("","boolean"))) {
            writer.write("("+fieldName(`slotName)+"?1:0)");
          } else if (`domain.equals(`ClassName("","String"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write("shared.HashFunctions.stringHashFunction("+fieldName(`slotName)+", "+index+")");
          } else if (`domain.equals(`ClassName("","double"))) {
            writer.write("(int)(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")^(java.lang.Double.doubleToLongBits(");
            writer.write(fieldName(`slotName));
            writer.write(")>>>32");
            writer.write("))");
          } else if (`domain.equals(`ClassName("aterm","ATerm"))||`domain.equals(`ClassName("aterm","ATermList"))) {
            // Use the string hashFunction for Strings, and pass index as arity
            writer.write(fieldName(`slotName)+".hashCode()");
          }  else {
            throw new GomRuntimeException("generateHashArgs: Builtin " + `domain + " not supported");
          }
        }
        writer.write(");\n");
        if(k % 3 == 0) {
          writer.write(%[
    // mix(a,b,c)
    a -= c;  a ^= (((c)<<(4))  | ((c)>>(32-(4))));  c += b;
    b -= a;  b ^= (((a)<<(6))  | ((a)>>(32-(6))));  a += c;
    c -= b;  c ^= (((b)<<(8))  | ((b)>>(32-(8))));  b += a;
    a -= c;  a ^= (((c)<<(16)) | ((c)>>(32-(16)))); c += b;
    b -= a;  b ^= (((a)<<(19)) | ((a)>>(32-(19)))); a += c;
    c -= b;  c ^= (((b)<<(4))  | ((b)>>(32-(4))));  b += a;
]%);
        }
        index--;
      }
    }
  }

  public void generateConstructor(java.io.Writer writer) throws java.io.IOException {
    boolean hasHooks = false;
    %match(hooks) {
      /* If there is at least one MakeHook */
lbl:ConcHook(_*,MakeHook[HookArguments=args],_*) -> {
      hasHooks = true;
      writer.write(%[
    public static @fullClassName(sortName)@ make(@unprotectedChildListWithType(`args)@) {
  ]%);
        SlotFieldList bargs = generateMakeHooks(hooks,null,writer);
        writer.write(%[
      return realMake(@unprotectedChildList(bargs)@);
    }
  ]%);
        break lbl;
      }
    }

    String makeName = "make";
    String visibility = "public";
    if (hasHooks) {
      makeName = "realMake";
      visibility = "private";
    }
    writer.write(%[
  @visibility@ static @className()@ @makeName@(@childListWithType(slotList)@) {
]%);

    if (! maximalsharing) {
        writer.write(%[
    return new @className()@(@childList(slotList)@);
    ]%);
    } else {
    if(slotList.length()>0) {
      if(multithread) {
        writer.write(%[
    // allocate and object and make duplicate equal identity
    @className()@ newProto = new @className()@();
    newProto.initHashCode(@childList(slotList)@);
    return (@className()@) factory.build(newProto);
]%);
      } else {
        writer.write(%[
    // use the proto as a model
    gomProto.initHashCode(@childList(slotList)@);
    return (@className()@) factory.build(gomProto);
]%);
      }
    } else {
        writer.write(%[
    return gomProto;
]%);
    }
    }
    writer.write(%[
  }
]%);


  }


  public SlotFieldList generateMakeHooks(
      HookList other,
      SlotFieldList oArgs, /* will be null if it is the first hook */
      java.io.Writer writer)
    throws java.io.IOException {
    %match(other) {
      ConcHook(!MakeHook[],tail*) -> {
        /* skip non Make hooks */
        return generateMakeHooks(`tail, oArgs, writer);
      }
      ConcHook(MakeHook[HookArguments=args, Code=code],tail*) -> {
        /* Rename the previous arguments according to new, if needed */
        if(oArgs != null && oArgs != `args) {
          recVarNameRemap(oArgs,`args, writer);
        }
        /* Make sure we defeat java dead code detection */
        writer.write("if (true) {");
        CodeGen.generateCode(`code,writer);
        writer.write("}");
        return generateMakeHooks(`tail, `args, writer);
      }
    }
    return oArgs;
  }

  private void recVarNameRemap(
      SlotFieldList oargs,
      SlotFieldList nargs,
      java.io.Writer writer)
  throws java.io.IOException {
    %match(oargs, nargs) {
      ConcSlotField(),ConcSlotField() -> {
        return ;
      }
      ConcSlotField(SlotField[Name=oargName,Domain=odomain],to*),
      ConcSlotField(SlotField[Name=nargName,Domain=ndomain],tn*) -> {
        if (!(`odomain==`ndomain)) {
          throw new GomRuntimeException(
              "OperatorTemplate: incompatible args "+
              "should be rejected by typechecker");
        } else if (!`oargName.equals(`nargName)) {
          /* XXX: the declaration should be omitted if nargName was previously
           * used */
          writer.write(%[
    @fullClassName(`ndomain)@ @`nargName@ = @`oargName@;
]%);
        } /* else nothing to rename */
        recVarNameRemap(`to,`tn, writer);
        return;
      }
    }
    throw new GomRuntimeException(
        "OperatorTemplate:recVarNameRemap failed " + oargs + " " + nargs);
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    %match(hooks) {
      !ConcHook(_*,MappingHook[],_*) -> {
        writer.write("%op "+className(sortName)+" "+className()+"(");
        slotDecl(writer,slotList);
        writer.write(") {\n");
        writer.write("  is_fsym(t) { ($t instanceof "+fullClassName()+") }\n");
        %match(slotList) {
          ConcSlotField(_*,slot@SlotField[Name=slotName],_*) -> {
            writer.write("  get_slot("+`slotName+", t) ");
            writer.write("{ $t."+getMethod(`slot)+"() }\n");
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
      ConcHook(_*,MappingHook[Code=code],_*) -> {
        CodeGen.generateCode(`code,writer);
      }
    }
    return;
  }
}
