/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
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

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;     }   }      private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;     }   }    

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  TemplateClass mapping) {
    super(gomClass,manager,tomHomePath,importList,mapping);
    {if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {{  tom.gom.adt.objects.types.GomClass  tomMatch405NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if ( (tomMatch405NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {{  tom.gom.adt.objects.types.ClassName  tomMatch405NameNumber_freshVar_0= tomMatch405NameNumberfreshSubject_1.getAbstractType() ;{  tom.gom.adt.objects.types.ClassName  tomMatch405NameNumber_freshVar_1= tomMatch405NameNumberfreshSubject_1.getSortName() ;{  tom.gom.adt.objects.types.GomClass  tomMatch405NameNumber_freshVar_2= tomMatch405NameNumberfreshSubject_1.getEmpty() ;{  tom.gom.adt.objects.types.GomClass  tomMatch405NameNumber_freshVar_3= tomMatch405NameNumberfreshSubject_1.getCons() ;if ( true ) {




        this.abstractType = tomMatch405NameNumber_freshVar_0;
        this.sortName = tomMatch405NameNumber_freshVar_1;
        this.empty = tomMatch405NameNumber_freshVar_2;
        this.cons = tomMatch405NameNumber_freshVar_3;
        return;
      }}}}}}}}}

    throw new GomRuntimeException(
        "Wrong argument for VariadicOperatorTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write("\npackage "/* Generated by TOM (version 2.6alpha): Do not edit this file */+getPackage()+";\n"/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateImport()+"\npublic abstract class "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" extends "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" "/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateInterface()+" {\n"/* Generated by TOM (version 2.6alpha): Do not edit this file */+generateBlock()+"\n"




);
generateBody(writer);
writer.write("\n}\n"

);
  }

  protected String generateInterface() {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    return 
      "implements java.util.Collection<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> "/* Generated by TOM (version 2.6alpha): Do not edit this file */+super.generateInterface()+"";
  }


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    writer.write("\n  @Override\n  public int length() {\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" tl = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")this).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n      if (tl instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+") {\n        return 1+(("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+")tl).length();\n      } else {\n        return 2;\n      }\n    } else {\n      return 0;\n    }\n    }\n\n\n  public static "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" fromArray("/* Generated by TOM (version 2.6alpha): Do not edit this file */+domainClassName+"[] array) {\n    "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" res = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+".make();\n    for(int i = array.length; i>0;) {\n      res = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(array[--i],res);\n    }\n    return res;\n  }\n\n  @Override\n  public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" reverse() {\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" cur = this;\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" rev = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+".make();\n      while(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n        rev = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make((("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(),rev);\n        cur = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n      }\n"
































);
    if(fullClassName(sortName).equals(domainClassName)) { /* domain = codomain */
      writer.write("\n      if(!(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+")) { \n        rev = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(cur,rev);\n      }\n"



);
    }
    writer.write("\n      return rev;\n    } else {\n      return this;\n    }\n  }\n\n  public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" append("/* Generated by TOM (version 2.6alpha): Do not edit this file */+domainClassName+" element) {\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" tl = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")this).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n      if (tl instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+") {\n        return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(this.getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(),(("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+")tl).append(element));\n      } else {\n"












);
    if(fullClassName(sortName).equals(domainClassName)) {
      writer.write("\n        return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(this.getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(),"/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(tl,element));\n"

);
    } else {
      writer.write("\n        return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(this.getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(),"/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(element,tl));\n"

);
    }
    writer.write("\n      }\n    } else {\n      return "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(element,this);\n    }\n  }\n\n  @Override\n  public void toStringBuilder(java.lang.StringBuilder buffer) {\n    buffer.append(\""/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(\");\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n        "/* Generated by TOM (version 2.6alpha): Do not edit this file */+domainClassName+" elem = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        cur = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        "/* Generated by TOM (version 2.6alpha): Do not edit this file */+toStringChild("buffer","elem")+"\n        if(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n          buffer.append(\",\");\n        }\n      }\n      if(!(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+")) {\n        buffer.append(\",\");\n        cur.toStringBuilder(buffer);\n      }\n    }\n    buffer.append(\")\");\n  }\n\n  public static "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" fromTerm(aterm.ATerm trm) {\n    if(trm instanceof aterm.ATermAppl) {\n      aterm.ATermAppl appl = (aterm.ATermAppl) trm;\n      if(\""/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"\".equals(appl.getName())) {\n        "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" res = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+".make();\n\n        aterm.ATerm array[] = appl.getArgumentArray();\n        for(int i = array.length-1; i>=0; --i) {\n          "/* Generated by TOM (version 2.6alpha): Do not edit this file */+domainClassName+" elem = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fromATermElement("array[i]","elem")+";\n          res = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(elem,res);\n        }\n        return res;\n      }\n    }\n    return null;\n  }\n\n  /*\n   * methods from Collection\n   */\n  public boolean containsAll(java.util.Collection c) {\n    java.util.Iterator it = c.iterator();\n    while(it.hasNext()) {\n      if(!this.contains(it.next())) {\n        return false;\n      }\n    }\n    return true;\n  }\n\n  public boolean contains(Object o) {\n    "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" cur = this;\n    if(o==null) { return false; }\n    if(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      while(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n        if( o.equals((("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"()) ) {\n          return true;\n        }\n        cur = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n      }\n      if(!(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+")) { \n        if( o.equals(cur) ) {\n          return true;\n        }\n      }\n\n    }\n    return false;\n  }\n\n  //public boolean equals(Object o) { return this == o; }\n\n  //public int hashCode() { return hashCode(); }\n\n  public boolean isEmpty() { return isEmpty"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"() ; }\n\n  public java.util.Iterator<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> iterator() {\n    return new java.util.Iterator<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+">() {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" list = "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+".this;\n\n      public boolean hasNext() {\n        return list!=null && !list.isEmpty"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n      }\n\n      public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" next() {\n        if(list.isEmpty"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"()) {\n          throw new java.util.NoSuchElementException();\n        }\n        if(list.isCons"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"()) {\n          "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" head = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")list).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n          list = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")list).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n          return head;\n        } else {\n          // we are in this case only if domain=codomain\n          // thus, the cast is safe\n          Object res = list;\n          list = null;\n          return ("/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+")res;\n        }\n      }\n\n      public void remove() {\n        throw new UnsupportedOperationException(\"Not yet implemented\");\n      }\n    };\n\n  }\n\n  public boolean add("/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" o) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n\n  public boolean addAll(java.util.Collection<? extends "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean remove(Object o) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public void clear() {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean removeAll(java.util.Collection c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean retainAll(java.util.Collection c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public int size() { return length(); }\n\n  public Object[] toArray() {\n    int size = this.length();\n    Object[] array = new Object[size];\n    int i=0;\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n        "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" elem = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        array[i] = elem;\n        cur = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        i++;\n      }\n      if(!(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+")) {\n        array[i] = cur;\n      }\n    }\n    return array;\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T[] toArray(T[] array) {\n    int size = this.length();\n    if (array.length < size) {\n      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);\n    } else if (array.length > size) {\n      array[size] = null;\n    }\n    int i=0;\n    if(this instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n      "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+") {\n        "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" elem = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getHead"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        array[i] = (T)elem;\n        cur = (("/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+")cur).getTail"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"();\n        i++;\n      }\n      if(!(cur instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+")) {\n        array[i] = (T)cur;\n      }\n    }\n    return array;\n  }\n\n  /*\n   * to get a Collection for an immutable list\n   */\n  public java.util.Collection<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> getCollection() {\n    return new Collection"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(this);\n  }\n\n  public java.util.Collection<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> getCollection"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"() {\n    return new Collection"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(this);\n  }\n\n  /************************************************************\n   * private static class\n   ************************************************************/\n  private static class Collection"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" implements java.util.Collection<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> {\n    private "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" list;\n\n    public "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"() {\n      return list; \n    }\n\n    public Collection"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+" list) {\n      this.list = list;\n    }\n\n    /**\n     * generic\n     */\n  public boolean addAll(java.util.Collection<? extends "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> c) {\n    boolean modified = false;\n    java.util.Iterator<? extends "/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> it = c.iterator();\n    while(it.hasNext()) {\n      modified = modified || add(it.next());\n    }\n    return modified;\n  }\n\n  public boolean contains(Object o) {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().contains(o);\n  }\n\n  public boolean containsAll(java.util.Collection<?> c) {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().containsAll(c);\n  }\n\n  @Override\n  public boolean equals(Object o) { \n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().equals(o); \n  }\n\n  @Override\n  public int hashCode() {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().hashCode(); \n  }\n\n  public java.util.Iterator<"/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+"> iterator() {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().iterator();\n  }\n\n  public int size() { \n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().size(); \n  }\n\n  public Object[] toArray() {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().toArray();\n  }\n\n  public <T> T[] toArray(T[] array) {\n    return get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().toArray(array);\n  }\n\n/*\n  public <T> T[] toArray(T[] array) {\n    int size = get"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+"().length();\n    if (array.length < size) {\n      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);\n    } else if (array.length > size) {\n      array[size] = null;\n    }\n    int i=0;\n    for(java.util.Iterator it=iterator() ; it.hasNext() ; i++) {\n        array[i] = (T)it.next();\n    }\n    return array;\n  }\n*/\n    /**\n     * Collection\n     */\n\n    public boolean add("/* Generated by TOM (version 2.6alpha): Do not edit this file */+primitiveToReferenceType(domainClassName)+" o) {\n      list = ("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+")"/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(cons.getClassName())+".make(o,list);\n      return true;\n    }\n\n    public void clear() {\n      list = ("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+")"/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(empty.getClassName())+".make();\n    }\n\n    public boolean isEmpty() { \n      return list.isEmpty"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"(); \n    }\n\n    public boolean remove(Object o) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n    public boolean removeAll(java.util.Collection<?> c) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n    public boolean retainAll(java.util.Collection<?> c) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n  }\n\n"














































































































































































































































































































);
    if (! hooks.isEmptyConcHook()) {
      mapping.generate(writer); 
    }

  }

  private String toStringChild(String buffer, String element) {
    SlotField head = cons.getSlotFields().getHeadConcSlotField();
    StringBuilder res = new StringBuilder();
    toStringSlotField(res,head,element,buffer);
    return res.toString();
  }

  private String fromATermElement(String term, String element) {
    SlotField slot = cons.getSlotFields().getHeadConcSlotField();
    StringBuilder buffer = new StringBuilder();
    fromATermSlotField(buffer,slot,term);
    return buffer.toString();
  }

  public void generateTomMapping(Writer writer)
      throws java.io.IOException {
    boolean hasHook = false;
    {if ( (hooks instanceof tom.gom.adt.objects.types.HookList) ) {{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.HookList )hooks);if ( ((tomMatch406NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || (tomMatch406NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) {{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumber_freshVar_0=tomMatch406NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumber_begin_2=tomMatch406NameNumber_freshVar_0;{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumber_end_3=tomMatch406NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumber_freshVar_1=tomMatch406NameNumber_end_3;if (!( tomMatch406NameNumber_freshVar_1.isEmptyConcHook() )) {if ( ( tomMatch406NameNumber_freshVar_1.getHeadConcHook()  instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {{  tom.gom.adt.code.types.Code  tomMatch406NameNumber_freshVar_6=  tomMatch406NameNumber_freshVar_1.getHeadConcHook() .getCode() ;{  tom.gom.adt.objects.types.HookList  tomMatch406NameNumber_freshVar_4= tomMatch406NameNumber_freshVar_1.getTailConcHook() ;if ( true ) {

        CodeGen.generateCode(tomMatch406NameNumber_freshVar_6,writer);
        hasHook = true;
      }}}}}}if ( tomMatch406NameNumber_end_3.isEmptyConcHook() ) {tomMatch406NameNumber_end_3=tomMatch406NameNumber_begin_2;} else {tomMatch406NameNumber_end_3= tomMatch406NameNumber_end_3.getTailConcHook() ;}}} while(!( tomMatch406NameNumber_end_3.equals(tomMatch406NameNumber_begin_2) ));}}}}}}}{if ( (cons instanceof tom.gom.adt.objects.types.GomClass) ) {{  tom.gom.adt.objects.types.GomClass  tomMatch407NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )cons);if ( (tomMatch407NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch407NameNumber_freshVar_0= tomMatch407NameNumberfreshSubject_1.getSlotFields() ;if ( ((tomMatch407NameNumber_freshVar_0 instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || (tomMatch407NameNumber_freshVar_0 instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch407NameNumber_freshVar_1=tomMatch407NameNumber_freshVar_0;if (!( tomMatch407NameNumber_freshVar_1.isEmptyConcSlotField() )) {if ( ( tomMatch407NameNumber_freshVar_1.getHeadConcSlotField()  instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {{  tom.gom.adt.objects.types.ClassName  tomMatch407NameNumber_freshVar_4=  tomMatch407NameNumber_freshVar_1.getHeadConcSlotField() .getDomain() ;{  tom.gom.adt.objects.types.SlotFieldList  tomMatch407NameNumber_freshVar_2= tomMatch407NameNumber_freshVar_1.getTailConcSlotField() ;if (!( tomMatch407NameNumber_freshVar_2.isEmptyConcSlotField() )) {{  tom.gom.adt.objects.types.SlotFieldList  tomMatch407NameNumber_freshVar_3= tomMatch407NameNumber_freshVar_2.getTailConcSlotField() ;if ( tomMatch407NameNumber_freshVar_3.isEmptyConcSlotField() ) {if ( true ) {






    ClassName emptyClass = empty.getClassName();
    ClassName consClass = cons.getClassName();
    writer.write("\n%oplist "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(sortName)+" "/* Generated by TOM (version 2.6alpha): Do not edit this file */+className()+"("/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tomMatch407NameNumber_freshVar_4)+"*) {\n  is_fsym(t) { (($t instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(consClass)+") || ($t instanceof "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(emptyClass)+")) }\n  make_empty() { "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(emptyClass)+".make() }\n  make_insert(e,l) { "/* Generated by TOM (version 2.6alpha): Do not edit this file */+fullClassName(consClass)+".make($e,$l) }\n  get_head(l) { $l."/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMethod( tomMatch407NameNumber_freshVar_1.getHeadConcSlotField() )+"() }\n  get_tail(l) { $l."/* Generated by TOM (version 2.6alpha): Do not edit this file */+getMethod( tomMatch407NameNumber_freshVar_2.getHeadConcSlotField() )+"() }\n  is_empty(l) { $l."/* Generated by TOM (version 2.6alpha): Do not edit this file */+isOperatorMethod(emptyClass)+"() }\n}\n"








);
      }}}}}}}}}}}}}}}

    return;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
