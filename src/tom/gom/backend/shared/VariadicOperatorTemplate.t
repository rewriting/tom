/*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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

  %include { ../../adt/objects/Objects.tom}

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  TemplateClass mapping) {
    super(gomClass,manager,tomHomePath,importList,mapping);
    %match(gomClass) {
      VariadicOperatorClass[AbstractType=abstractType,
                            SortName=sortName,
                            Empty=empty,
                            Cons=cons] -> {
        this.abstractType = `abstractType;
        this.sortName = `sortName;
        this.empty = `empty;
        this.cons = `cons;
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
public abstract class @className()@ extends @fullClassName(sortName)@ @generateInterface()@{
@generateBlock()@
]%);
generateBody(writer);
writer.write(%[
}
]%);
  }

  protected String generateInterface() {
    String interfaces = super.generateInterface();
    if (! interfaces.equals("")) {
      return "implements "+interfaces.substring(1);
    } else {
      return interfaces;
    }
  }


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    String domainClassName = fullClassName(
        cons.getSlots().getHeadconcSlotField().getDomain());
    writer.write(%[
  public int length() {
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ tl = ((@fullClassName(cons.getClassName())@)this).getTail@className()@();
      if (tl instanceof @className()@) {
        return 1+((@className()@)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
    }


  public static @fullClassName(sortName)@ fromArray(@domainClassName@[] array) {
    @fullClassName(sortName)@ res = @fullClassName(empty.getClassName())@.make();
    for(int i = array.length; i>0;) {
      res = @fullClassName(cons.getClassName())@.make((@domainClassName@)array[--i],res);
    }
    return res;
  }

  public @fullClassName(sortName)@ reverse() {
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      @fullClassName(sortName)@ rev = @fullClassName(empty.getClassName())@.make();
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        rev = @fullClassName(cons.getClassName())@.make(((@fullClassName(cons.getClassName())@)cur).getHead@className()@(),rev);
        cur = ((@fullClassName(cons.getClassName())@)cur).getTail@className()@();
      }
      return rev;
    } else {
      return this;
    }
  }

  public @fullClassName(sortName)@ add(@domainClassName@ element) {
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ tl = ((@fullClassName(cons.getClassName())@)this).getTail@className()@();
      if (tl instanceof @className()@) {
        return @fullClassName(cons.getClassName())@.make(this.getHead@className()@(),((@className()@)tl).add(element));
      } else {
]%);
    if(fullClassName(sortName)==domainClassName) {
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

  public void toStringBuffer(java.lang.StringBuffer buffer) {
    buffer.append("@className()@(");
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @domainClassName@ elem = ((@fullClassName(cons.getClassName())@)cur).getHead@className()@();
        cur = ((@fullClassName(cons.getClassName())@)cur).getTail@className()@();
        @toStringChild("buffer","elem")@
        if(cur instanceof @fullClassName(cons.getClassName())@) {
          buffer.append(",");
        }
      }
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) {
        buffer.append(",");
        cur.toStringBuffer(buffer);
      }
    }
    buffer.append(")");
  }

  public static @fullClassName(sortName)@ fromTerm(aterm.ATerm trm) {
    if(trm instanceof aterm.ATermAppl) {
      aterm.ATermAppl appl = (aterm.ATermAppl) trm;
      if("@className()@".equals(appl.getName())) {
        @fullClassName(sortName)@ res = @fullClassName(empty.getClassName())@.make();

        aterm.ATerm array[] = appl.getArgumentArray();
        for(int i = array.length-1; i>=0; --i) {
          @domainClassName@ elem = @fromATermElement("array[i]","elem")@;
          res = @fullClassName(cons.getClassName())@.make(elem,res);
        }
        return res;
      }
    }
    return null;
  }

  /*
   * methods from Collection
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

  public boolean contains(Object o) {
    @fullClassName(sortName)@ cur = this;
    if(cur instanceof @fullClassName(cons.getClassName())@) {
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        if( ((@fullClassName(cons.getClassName())@)cur).getHead@className()@() == o ) {
          return true;
        }
        cur = ((@fullClassName(cons.getClassName())@)cur).getTail@className()@();
      }
    }
    return false;
  }

  //public boolean equals(Object o) { return this == o; }

  //public int hashCode() { return hashCode(); }

  public boolean isEmpty() { return isEmpty@className()@() ; }

  public java.util.Iterator<@primitiveToReferenceType(domainClassName)@> iterator() {
    return new java.util.Iterator<@primitiveToReferenceType(domainClassName)@>() {
      @fullClassName(sortName)@ list = @className()@.this;

      public boolean hasNext() {
        return list.isCons@className()@();
      }

      public @primitiveToReferenceType(domainClassName)@ next() {
        if(list.isEmpty@className()@()) {
          throw new java.util.NoSuchElementException();
        }
        @primitiveToReferenceType(domainClassName)@ head = ((@fullClassName(cons.getClassName())@)list).getHead@className()@();
        list = ((@fullClassName(cons.getClassName())@)list).getTail@className()@();
        return head;
      }

      public void remove() {
        throw new UnsupportedOperationException("Not yet implemented");
      }
    };

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
    int size = this.length();
    Object[] array = new Object[size];
    int i=0;
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @primitiveToReferenceType(domainClassName)@ elem = ((@fullClassName(cons.getClassName())@)cur).getHead@className()@();
        array[i] = elem;
        cur = ((@fullClassName(cons.getClassName())@)cur).getTail@className()@();
        i++;
      }
    }
    return array;
  }

  public Object[] toArray(Object[] array) {
    int size = this.length();
    if (array.length < size) {
      array = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
    } else if (array.length > size) {
      array[size] = null;
    }
    int i=0;
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @fullClassName(sortName)@ cur = this;
      while(cur instanceof @fullClassName(cons.getClassName())@) {
        @primitiveToReferenceType(domainClassName)@ elem = ((@fullClassName(cons.getClassName())@)cur).getHead@className()@();
        array[i] = elem;
        cur = ((@fullClassName(cons.getClassName())@)cur).getTail@className()@();
        i++;
      }
    }
    return array;
  }

  /*
   * to get a Collection for an immutable list
   */
  public java.util.Collection<@primitiveToReferenceType(domainClassName)@> getCollection@className()@() {
    return new Collection@className()@(this);
  }

  /*
   * private static class
   */
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

  public boolean contains(Object o) {
    return get@className(sortName)@().contains(o);
  }

  public boolean containsAll(java.util.Collection<?> c) {
    return get@className(sortName)@().containsAll(c);
  }

  public boolean equals(Object o) { return get@className(sortName)@().equals(o); }

  public int hashCode() { return get@className(sortName)@().hashCode(); }

  public java.util.Iterator<@primitiveToReferenceType(domainClassName)@> iterator() {
    return get@className(sortName)@().iterator();
  }

  public int size() { return get@className(sortName)@().size(); }

  public Object[] toArray() { return get@className(sortName)@().toArray(); }

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

    /**
     * Collection
     */

    public boolean add(@primitiveToReferenceType(domainClassName)@ o) {
      if(o instanceof @primitiveToReferenceType(domainClassName)@) {
        list = (@className()@) @fullClassName(cons.getClassName())@.make((@domainClassName@)o,list);
        return true;
      }
      return false;
    }

    public void clear() {
      list = (@className()@) @fullClassName(empty.getClassName())@.make();
    }

    public boolean isEmpty() { return list.isEmpty@className()@(); }

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
    if (! hooks.isEmptyconcHook()) {
      mapping.generate(writer); 
    }

  }

  private String toStringChild(String buffer, String element) {
    SlotField head = cons.getSlots().getHeadconcSlotField();
    StringBuffer res = new StringBuffer();
    toStringSlotField(res,head,element,buffer);
    return res.toString();
  }

  private String fromATermElement(String term, String element) {
    SlotField slot = cons.getSlots().getHeadconcSlotField();
    StringBuffer buffer = new StringBuffer();
    fromATermSlotField(buffer,slot,term);
    return buffer.toString();
  }

  public void generateTomMapping(Writer writer, ClassName basicStrategy)
      throws java.io.IOException {
    boolean hasHook = false;
    %match(HookList hooks) {
      concHook(_*,MappingHook[Code=code],_*) -> {
        CodeGen.generateCode(`code,writer);
        hasHook = true;
      }
    }

    %match(cons) {
      OperatorClass[
        Slots=concSlotField(head@SlotField[Domain=headDomain], tail)
      ] -> {
    ClassName emptyClass = empty.getClassName();
    ClassName consClass = cons.getClassName();
    writer.write(%[
%oplist @className(sortName)@ @className()@(@className(`headDomain)@*) {
  is_fsym(t) { t instanceof @fullClassName(consClass)@ || t instanceof @fullClassName(emptyClass)@ }
  make_empty() { @fullClassName(emptyClass)@.make() }
  make_insert(e,l) { @fullClassName(consClass)@.make(e,l) }
  get_head(l) { l.@getMethod(`head)@() }
  get_tail(l) { l.@getMethod(`tail)@() }
  is_empty(l) { l.@isOperatorMethod(emptyClass)@() }
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
