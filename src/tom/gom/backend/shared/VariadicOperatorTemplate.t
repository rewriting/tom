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

public class VariadicOperatorTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassName sortName;
  GomClass empty;
  GomClass cons;
  String comments;

  %include { ../../adt/objects/Objects.tom}

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  TemplateClass mapping,
                                  GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    %match(gomClass) {
      VariadicOperatorClass[AbstractType=abstractType,
                            SortName=sortName,
                            Empty=empty,
                            Cons=cons,
                            Comments=comments] -> {
        this.abstractType = `abstractType;
        this.sortName = `sortName;
        this.empty = `empty;
        this.cons = `cons;
        this.comments = `comments;
        return;
      }
    }
    throw new GomRuntimeException(
        "Wrong argument for VariadicOperatorTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write(%[
package @getPackage()@;
@generateImport()@

@comments@
public abstract class @className()@ extends @fullClassName(sortName)@ @generateInterface()@ {
@generateBlock()@
]%);
generateBody(writer);
writer.write(%[
}
]%);
  }

  protected String generateInterface() {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    return
      %[implements java.util.Collection<@primitiveToReferenceType(domainClassName)@> @super.generateInterface()@]%;
  }


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    writer.write(%[
  /**
   * Returns the number of arguments of the variadic operator
   *
   * @@return the number of arguments of the variadic operator
   */
  @@Override
  public int length() {
    int res = 0;
    @fullClassName(sortName)@ tl = this;
    while(tl instanceof @fullClassName(cons.getClassName())@) {
      res += 1;
      tl = tl.getTail@className()@();
      if(tl instanceof @className()@ == false) {
        res += 1;
      }
    }
    return res;
/*
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ tl = this.getTail@className()@();
      if (tl instanceof @className()@) {
        return 1+((@className()@)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
    */
  }

  public static @fullClassName(sortName)@ fromArray(@domainClassName@[] array) {
    @fullClassName(sortName)@ res = @fullClassName(empty.getClassName())@.make();
    for(int i = array.length; i>0;) {
      res = @fullClassName(cons.getClassName())@.make(array[--i],res);
    }
    return res;
  }

  /**
   * Inverses the term if it is a list
   *
   * @@return the inverted term if it is a list, otherwise the term itself
   */
  @@Override
  public @fullClassName(sortName)@ reverse() {
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      @fullClassName(sortName)@ rev = @fullClassName(empty.getClassName())@.make();
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        rev = @fullClassName(cons.getClassName())@.make(cur.getHead@className()@(),rev);
        cur = cur.getTail@className()@();
      }
]%);
    if(fullClassName(sortName).equals(domainClassName)) { /* domain = codomain */
      writer.write(%[
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        rev = @fullClassName(cons.getClassName())@.make(cur,rev);
      }
]%);
    }
    writer.write(%[
      return rev;
    } else {
      return this;
    }
  }

  /**
   * Appends an element
   *
   * @@param element element which has to be added
   * @@return the term with the added element
   */
  public @fullClassName(sortName)@ append(@domainClassName@ element) {
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ tl = this.getTail@className()@();
      if (tl instanceof @className()@) {
        return @fullClassName(cons.getClassName())@.make(this.getHead@className()@(),((@className()@)tl).append(element));
      } else {
]%);
    if(fullClassName(sortName).equals(domainClassName)) {
      writer.write(%[
        return @fullClassName(cons.getClassName())@.make(this.getHead@className()@(),@fullClassName(cons.getClassName())@.make(tl,element));
]%);
    } else {
      writer.write(%[
        return @fullClassName(cons.getClassName())@.make(this.getHead@className()@(),@fullClassName(cons.getClassName())@.make(element,tl));
]%);
    }
    writer.write(%[
      }
    } else {
      return @fullClassName(cons.getClassName())@.make(element,this);
    }
  }

  /**
   * Appends a string representation of this term to the buffer given as argument.
   *
   * @@param buffer the buffer to which a string represention of this term is appended.
   */
  @@Override
  public void toStringBuilder(java.lang.StringBuilder buffer) {
    buffer.append("@className()@(");
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @domainClassName@ elem = cur.getHead@className()@();
        cur = cur.getTail@className()@();
        @toStringChild("buffer","elem")@
        if(cur instanceof @fullClassName(cons.getClassName())@) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        buffer.append(",");
        cur.toStringBuilder(buffer);
      }
    }
    buffer.append(")");
  }

  /**
   * Returns an ATerm representation of this term.
   *
   * @@return an ATerm representation of this term.
   */
  public aterm.ATerm toATerm() {
    aterm.ATerm res = atermFactory.makeList();
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ tail = this.getTail@className()@();
      res = atermFactory.makeList(@toATermHead()@,(aterm.ATermList)tail.toATerm());
    }
    return res;
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
      if("@className()@".equals(appl.getName())) {
        @fullClassName(sortName)@ res = @fullClassName(empty.getClassName())@.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          @domainClassName@ elem = @fromATermElement("array[i]","atConv")@;
          res = @fullClassName(cons.getClassName())@.make(elem,res);
        }
        return res;
      }
    }

    if(trm instanceof aterm.ATermList) {
      aterm.ATermList list = (aterm.ATermList) trm;
      @fullClassName(sortName)@ res = @fullClassName(empty.getClassName())@.make();
      try {
        while(!list.isEmpty()) {
          @domainClassName@ elem = @fromATermElement("list.getFirst()","atConv")@;
          res = @fullClassName(cons.getClassName())@.make(elem,res);
          list = list.getNext();
        }
      } catch(IllegalArgumentException e) {
        // returns null when the fromATerm call failed
        return null;
      }
      return res.reverse();
    }

    return null;
  }

  /*
   * Checks if the Collection contains all elements of the parameter Collection
   *
   * @@param c the Collection of elements to check
   * @@return true if the Collection contains all elements of the parameter, otherwise false
   */
  public boolean containsAll(java.util.Collection c) {
    java.util.Iterator it = c.iterator();
    while(it.hasNext()) {
      if(!this.contains(it.next())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if @fullClassName(sortName)@ contains a specified object
   *
   * @@param o object whose presence is tested
   * @@return true if @fullClassName(sortName)@ contains the object, otherwise false
   */
  public boolean contains(Object o) {
    @fullClassName(sortName)@ cur = this;
    if(o==null) { return false; }
    if(cur instanceof @fullClassName(cons.getClassName())@) {
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        if( o.equals(cur.getHead@className()@()) ) {
          return true;
        }
        cur = cur.getTail@className()@();
      }
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        if( o.equals(cur) ) {
          return true;
        }
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this.deepEquals(o); }

  //public int hashCode() { return hashCode(); }

  /**
   * Checks the emptiness
   *
   * @@return true if empty, otherwise false
   */
  public boolean isEmpty() { return isEmpty@className()@() ; }

  public java.util.Iterator<@primitiveToReferenceType(domainClassName)@> iterator() {
    return new java.util.Iterator<@primitiveToReferenceType(domainClassName)@>() {
      @fullClassName(sortName)@ list = @className()@.this;

      public boolean hasNext() {
        return list!=null && !list.isEmpty@className()@();
      }

      public @primitiveToReferenceType(domainClassName)@ next() {
        if(list.isEmpty@className()@()) {
          throw new java.util.NoSuchElementException();
        }
        if(list.isCons@className()@()) {
          @primitiveToReferenceType(domainClassName)@ head = list.getHead@className()@();
          list = list.getTail@className()@();
          return head;
        } else {
          // we are in this case only if domain=codomain
          // thus, the cast is safe
          Object res = list;
          list = null;
          return (@primitiveToReferenceType(domainClassName)@)res;
        }
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

  }

  public boolean add(@primitiveToReferenceType(domainClassName)@ o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean addAll(java.util.Collection<? extends @primitiveToReferenceType(domainClassName)@> c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public void clear() {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean removeAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  public boolean retainAll(java.util.Collection c) {
    throw new UnsupportedOperationException("This object "+this.getClass().getName()+" is not mutable");
  }

  /**
   * Returns the size of the collection
   *
   * @@return the size of the collection
   */
  public int size() { return length(); }

  /**
   * Returns an array containing the elements of the collection
   *
   * @@return an array of elements
   */
  public Object[] toArray() {
    int size = this.length();
    Object[] array = new Object[size];
    int i=0;
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @primitiveToReferenceType(domainClassName)@ elem = cur.getHead@className()@();
        array[i] = elem;
        cur = cur.getTail@className()@();
        i++;
      }
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        array[i] = cur;
      }
    }
    return array;
  }

  @@SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] array) {
    int size = this.length();
    if (array.length < size) {
      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @primitiveToReferenceType(domainClassName)@ elem = cur.getHead@className()@();
        array[i] = (T)elem;
        cur = cur.getTail@className()@();
        i++;
      }
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        array[i] = (T)cur;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<@primitiveToReferenceType(domainClassName)@> getCollection() {
    return new Collection@className()@(this);
  }

  public java.util.Collection<@primitiveToReferenceType(domainClassName)@> getCollection@className()@() {
    return new Collection@className()@(this);
  }

  /************************************************************
   * private static class
   ************************************************************/
  private static class Collection@className()@ implements java.util.Collection<@primitiveToReferenceType(domainClassName)@> {
    private @className()@ list;

    public @className()@ get@className(sortName)@() {
      return list;
    }

    public Collection@className()@(@className()@ list) {
      this.list = list;
    }

    /**
     * generic
     */
  public boolean addAll(java.util.Collection<? extends @primitiveToReferenceType(domainClassName)@> c) {
    boolean modified = false;
    java.util.Iterator<? extends @primitiveToReferenceType(domainClassName)@> it = c.iterator();
    while(it.hasNext()) {
      modified = modified || add(it.next());
    }
    return modified;
  }

  /**
   * Checks if the collection contains an element
   *
   * @@param o element whose presence has to be checked
   * @@return true if the element is found, otherwise false
   */
  public boolean contains(Object o) {
    return get@className(sortName)@().contains(o);
  }

  /**
   * Checks if the collection contains elements given as parameter
   *
   * @@param c elements whose presence has to be checked
   * @@return true all the elements are found, otherwise false
   */
  public boolean containsAll(java.util.Collection<?> c) {
    return get@className(sortName)@().containsAll(c);
  }

  /**
   * Checks if an object is equal
   *
   * @@param o object which is compared
   * @@return true if objects are equal, false otherwise
   */
  @@Override
  public boolean equals(Object o) {
    return get@className(sortName)@().equals(o);
  }

  /**
   * Returns the hashCode
   *
   * @@return the hashCode
   */
  @@Override
  public int hashCode() {
    return get@className(sortName)@().hashCode();
  }

  /**
   * Returns an iterator over the elements in the collection
   *
   * @@return an iterator over the elements in the collection
   */
  public java.util.Iterator<@primitiveToReferenceType(domainClassName)@> iterator() {
    return get@className(sortName)@().iterator();
  }

  /**
   * Return the size of the collection
   *
   * @@return the size of the collection
   */
  public int size() {
    return get@className(sortName)@().size();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @@return an array of elements
   */
  public Object[] toArray() {
    return get@className(sortName)@().toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @@param array array which will contain the result
   * @@return an array of elements
   */
  public <T> T[] toArray(T[] array) {
    return get@className(sortName)@().toArray(array);
  }

/*
  public <T> T[] toArray(T[] array) {
    int size = get@className(sortName)@().length();
    if (array.length < size) {
      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    for(java.util.Iterator it=iterator() ; it.hasNext() ; i++) {
        array[i] = (T)it.next();
    }
    return array;
  }
*/
    /**
     * Collection
     */

    /**
     * Adds an element to the collection
     *
     * @@param o element to add to the collection
     * @@return true if it is a success
     */
    public boolean add(@primitiveToReferenceType(domainClassName)@ o) {
      list = (@className()@)@fullClassName(cons.getClassName())@.make(o,list);
      return true;
    }

    /**
     * Removes all of the elements from this collection
     */
    public void clear() {
      list = (@className()@)@fullClassName(empty.getClassName())@.make();
    }

    /**
     * Tests the emptiness of the collection
     *
     * @@return true if the collection is empty
     */
    public boolean isEmpty() {
      return list.isEmpty@className()@();
    }

    public boolean remove(Object o) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean removeAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean retainAll(java.util.Collection<?> c) {
      throw new UnsupportedOperationException("Not yet implemented");
    }

  }

]%);
    if (hooks.containsTomCode()) {
      mapping.generate(writer);
    }

  }

  private String toStringChild(String buffer, String element) {
    SlotField head = cons.getSlotFields().getHeadConcSlotField();
    StringBuilder res = new StringBuilder();
    toStringSlotField(res,head,element,buffer);
    return res.toString();
  }

  private String toATermHead() {
    SlotField head = cons.getSlotFields().getHeadConcSlotField();
    StringBuilder res = new StringBuilder();
    toATermSlotField(res,head);
    return res.toString();
  }

  private String fromATermElement(String term, String atConv) {
    SlotField slot = cons.getSlotFields().getHeadConcSlotField();
    StringBuilder buffer = new StringBuilder();
    fromATermSlotField(buffer,slot,term,atConv);
    return buffer.toString();
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    boolean hasHook = false;
    %match(hooks) {
      ConcHook(_*,MappingHook[Code=code],_*) -> {
        CodeGen.generateCode(`code,writer);
        hasHook = true;
        // if there is a mapping hook we stop here
        return;
      }
    }

    %match(cons) {
      OperatorClass[
        SlotFields=ConcSlotField(head@SlotField[Domain=headDomain], tail)
      ] -> {
    ClassName emptyClass = empty.getClassName();
    ClassName consClass = cons.getClassName();
    writer.write(%[
%oplist @className(sortName)@ @className()@(@`className(headDomain)@*) {
  is_fsym(t) { (($t instanceof @fullClassName(consClass)@) || ($t instanceof @fullClassName(emptyClass)@)) }
  make_empty() { @fullClassName(emptyClass)@.make() }
  make_insert(e,l) { @fullClassName(consClass)@.make($e,$l) }
  get_head(l) { $l.@`getMethod(head)@() }
  get_tail(l) { $l.@`getMethod(tail)@() }
  is_empty(l) { $l.@isOperatorMethod(emptyClass)@() }
}
]%);
      }
    }
    return;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
//@primitiveToReferenceType(domainClassName)@ elem = this.getHead@className()@();
