/*
 * Gom
 *
 * Copyright (c) 2006-2008, INRIA
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
  SlotFieldList slotList;
  boolean maximalsharing;

  %include { ../../adt/objects/Objects.tom}

  public SortTemplate(File tomHomePath,
                      OptionManager manager,
                      boolean maximalsharing,
                      List importList, 	
                      GomClass gomClass,
                      TemplateClass mapping,
                      GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    this.maximalsharing = maximalsharing;
    %match(gomClass) {
      SortClass[AbstractType=abstractType,
                Operators=ops,
                VariadicOperators=variops,
                SlotFields=slots] -> {
        this.abstractType = `abstractType;
        this.operatorList = `ops;
        this.variadicOperatorList = `variops;
        this.slotList = `slots;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for SortTemplate: " + gomClass);
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  protected String generateInterface() {
    String interfaces =  super.generateInterface();
    if (interfaces.equals("")) return "";
    else return %[implements @interfaces.substring(1)@]%;
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write(%[
package @getPackage()@;        
@generateImport()@
import tom.gom.GomMessage;
import tom.library.utils.ATermConverter;
import tom.library.utils.IdConverter;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
//import @getPackage()@.@className().toLowerCase()@.*;
//import @getPackage().substring(0,getPackage().lastIndexOf("."))@.*;
public abstract class @className()@ extends @fullClassName(abstractType)@ @generateInterface()@ {

@generateBlock()@
]%);
generateBody(writer);
writer.write(%[
}
]%);
  }

  public void generateBody(java.io.Writer writer) throws java.io.IOException {
    // methods for each operator
    ClassNameList consum = operatorList;
    while (!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();

      writer.write(%[
  public boolean @isOperatorMethod(operatorName)@() {
    return false;
  }
]%);
    }
    // methods for each slot
    SlotFieldList sl = slotList;
    while (!sl.isEmptyConcSlotField()) {
      SlotField slot = sl.getHeadConcSlotField();
      sl = sl.getTailConcSlotField();

      writer.write(%[
  public @slotDomain(slot)@ @getMethod(slot)@() {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getName()@");
  }

  public @className()@ @setMethod(slot)@(@slotDomain(slot)@ _arg) {
    throw new UnsupportedOperationException("This @className()@ has no @slot.getName()@");
  }
]%);

    }

    /* fromTerm method, dispatching to operator classes */
    writer.write(%[

  public static IdConverter idConv = new IdConverter();

  public aterm.ATerm toATerm() {
    // returns null to indicates sub-classes that they have to work
    return null;
  }

  public static @fullClassName()@ fromTerm(aterm.ATerm trm) {
    return fromTerm(trm,idConv);
  }

  public static @fullClassName()@ fromString(String s) {
    return fromTerm(atermFactory.parse(s),idConv);
  }

  public static @fullClassName()@ fromStream(java.io.InputStream stream) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),idConv);
  }

  public static @fullClassName()@ fromTerm(aterm.ATerm trm, ATermConverter atConv) {
    Vector<@fullClassName()@> tmp = new Vector<@fullClassName()@>();
    aterm.ATerm convertedTerm = atConv.convert(trm);
    int nbr = 0;
    @fullClassName()@ res = null;
]%);
    generateFromTerm(writer,"convertedTerm","atConv","tmp");
    writer.write(%[
    for(int i=0;i<tmp.size();i++) {
      if(tmp.get(i) != null) {
        nbr++;
        if (res == null) {
          res = tmp.get(i);
        }
      }
    }
    switch(nbr) {
      case 0:
        throw new IllegalArgumentException("This is not a @className()@ " + trm);
      case 1:
        return res;
      default:
        Logger.getLogger("@className()@").log(Level.WARNING,GomMessage.gomChoiceWarning.getMessage(),new Object[] {"@fullClassName()@", res.toString()});
        //System.out.println("WARNING: There were many possibilities in @fullClassName()@ but the first one was chosen : " + res.toString());
        break;
    }
    return res;
  }

  public static @fullClassName()@ fromString(String s, ATermConverter atConv) {
    return fromTerm(atermFactory.parse(s),atConv);
  }

  public static @fullClassName()@ fromStream(java.io.InputStream stream, ATermConverter atConv) throws java.io.IOException {
    return fromTerm(atermFactory.readFromFile(stream),atConv);
  }
]%);

    
    /* abstract method to compare two terms represented by objects without maximal sharing */
    /* used in the mapping */
    if(!maximalsharing) {
      writer.write(%[
  public abstract boolean deepEquals(Object o);
]%);
    } 

    /* length and reverse prototypes, only usable on lists */
    writer.write(%[
  public int length() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public @fullClassName()@ reverse() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  /**
   * Collection
   */
  /*
  public boolean add(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean containsAll(java.util.Collection c) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean contains(Object o) {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean equals(Object o) { return this == o; }

  public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return false; }

  public java.util.Iterator iterator() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public int size() { return length(); }

  public Object[] toArray() {
    throw new IllegalArgumentException(
      "This "+this.getClass().getName()+" is not a list");
  }

  public Object[] toArray(Object[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
  */
  ]%);
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
  }

  private void generateFromTerm(java.io.Writer writer, String trm, String conv,String tmp) throws java.io.IOException {
    ClassNameList consum = `ConcClassName(operatorList*,variadicOperatorList*);
    while(!consum.isEmptyConcClassName()) {
      ClassName operatorName = consum.getHeadConcClassName();
      consum = consum.getTailConcClassName();
      writer.write(%[
    tmp.addElement(@fullClassName(operatorName)@.fromTerm(@trm@,@conv@));
]%);
    }
  }

  public void generateTomMapping(Writer writer) throws java.io.IOException {
    writer.write(%[
%typeterm @className()@ {
  implement { @fullClassName()@ }
  is_sort(t) { ($t instanceof @fullClassName()@) }
]%);
    if(maximalsharing) {
      writer.write(%[
  equals(t1,t2) { ($t1==$t2) }
]%);
    } else {
      writer.write(%[
  equals(t1,t2) { (((@fullClassName()@)$t1).deepEquals($t2)) }
]%);
    }
    writer.write(%[
}
]%);
 }
}
