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

         private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }    

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  TemplateClass mapping,
                                  GomEnvironment gomEnvironment) {
    super(gomClass,manager,tomHomePath,importList,mapping,gomEnvironment);
    { /* unamed block */{ /* unamed block */if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {





        this.abstractType =  (( tom.gom.adt.objects.types.GomClass )gomClass).getAbstractType() ;
        this.sortName =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSortName() ;
        this.empty =  (( tom.gom.adt.objects.types.GomClass )gomClass).getEmpty() ;
        this.cons =  (( tom.gom.adt.objects.types.GomClass )gomClass).getCons() ;
        this.comments =  (( tom.gom.adt.objects.types.GomClass )gomClass).getComments() ;
        return;
      }}}}

    throw new GomRuntimeException(
        "Wrong argument for VariadicOperatorTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {

    writer.write("\npackage "+getPackage()+";\n"+generateImport()+"\n\n"+comments+"\npublic abstract class "+className()+" extends "+fullClassName(sortName)+" "+generateInterface()+" {\n"+generateBlock()+"\n"






);
generateBody(writer);
writer.write("\n}\n"

);
  }

  protected String generateInterface() {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    return
      "implements java.util.Collection<"+primitiveToReferenceType(domainClassName)+"> "+super.generateInterface()+"";
  }


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    String domainClassName = fullClassName(
        cons.getSlotFields().getHeadConcSlotField().getDomain());
    writer.write("\n  /**\n   * Returns the number of arguments of the variadic operator\n   *\n   * @return the number of arguments of the variadic operator\n   */\n  @Override\n  public int length() {\n    int res = 0;\n    "+fullClassName(sortName)+" tl = this;\n    while(tl instanceof "+fullClassName(cons.getClassName())+") {\n      res += 1;\n      tl = tl.getTail"+className()+"();\n      if(tl instanceof "+className()+" == false) {\n        res += 1;\n      }\n    }\n    return res;\n/*\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" tl = this.getTail"+className()+"();\n      if (tl instanceof "+className()+") {\n        return 1+(("+className()+")tl).length();\n      } else {\n        return 2;\n      }\n    } else {\n      return 0;\n    }\n    */\n  }\n\n  public static "+fullClassName(sortName)+" fromArray("+domainClassName+"[] array) {\n    "+fullClassName(sortName)+" res = "+fullClassName(empty.getClassName())+".make();\n    for(int i = array.length; i>0;) {\n      res = "+fullClassName(cons.getClassName())+".make(array[--i],res);\n    }\n    return res;\n  }\n\n  /**\n   * Inverses the term if it is a list\n   *\n   * @return the inverted term if it is a list, otherwise the term itself\n   */\n  @Override\n  public "+fullClassName(sortName)+" reverse() {\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" cur = this;\n      "+fullClassName(sortName)+" rev = "+fullClassName(empty.getClassName())+".make();\n      while(cur instanceof "+fullClassName(cons.getClassName())+") {\n        rev = "+fullClassName(cons.getClassName())+".make(cur.getHead"+className()+"(),rev);\n        cur = cur.getTail"+className()+"();\n      }\n"





















































);
    if(fullClassName(sortName).equals(domainClassName)) { /* domain = codomain */
      writer.write("\n      if(!(cur instanceof "+fullClassName(empty.getClassName())+")) {\n        rev = "+fullClassName(cons.getClassName())+".make(cur,rev);\n      }\n"



);
    }
    writer.write("\n      return rev;\n    } else {\n      return this;\n    }\n  }\n\n  /**\n   * Appends an element\n   *\n   * @param element element which has to be added\n   * @return the term with the added element\n   */\n  public "+fullClassName(sortName)+" append("+domainClassName+" element) {\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" tl = this.getTail"+className()+"();\n      if (tl instanceof "+className()+") {\n        return "+fullClassName(cons.getClassName())+".make(this.getHead"+className()+"(),(("+className()+")tl).append(element));\n      } else {\n"


















);
    if(fullClassName(sortName).equals(domainClassName)) {
      writer.write("\n        return "+fullClassName(cons.getClassName())+".make(this.getHead"+className()+"(),"+fullClassName(cons.getClassName())+".make(tl,element));\n"

);
    } else {
      writer.write("\n        return "+fullClassName(cons.getClassName())+".make(this.getHead"+className()+"(),"+fullClassName(cons.getClassName())+".make(element,tl));\n"

);
    }
    writer.write("\n      }\n    } else {\n      return "+fullClassName(cons.getClassName())+".make(element,this);\n    }\n  }\n\n  /**\n   * Appends a string representation of this term to the buffer given as argument.\n   *\n   * @param buffer the buffer to which a string represention of this term is appended.\n   */\n  @Override\n  public void toStringBuilder(java.lang.StringBuilder buffer) {\n    buffer.append(\""+className()+"(\");\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "+fullClassName(cons.getClassName())+") {\n        "+domainClassName+" elem = cur.getHead"+className()+"();\n        cur = cur.getTail"+className()+"();\n        "+toStringChild("buffer","elem")+"\n        if(cur instanceof "+fullClassName(cons.getClassName())+") {\n          buffer.append(\",\");\n        }\n      }\n      if(!(cur instanceof "+fullClassName(empty.getClassName())+")) {\n        buffer.append(\",\");\n        cur.toStringBuilder(buffer);\n      }\n    }\n    buffer.append(\")\");\n  }\n\n  /**\n   * Returns an ATerm representation of this term.\n   *\n   * @return an ATerm representation of this term.\n   */\n  public aterm.ATerm toATerm() {\n    aterm.ATerm res = atermFactory.makeList();\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" tail = this.getTail"+className()+"();\n      res = atermFactory.makeList("+toATermHead()+",(aterm.ATermList)tail.toATerm());\n    }\n    return res;\n  }\n\n  /**\n   * Apply a conversion on the ATerm contained in the String and returns a "+fullClassName(sortName)+" from it\n   *\n   * @param trm ATerm to convert into a Gom term\n   * @param atConv ATerm Converter used to convert the ATerm\n   * @return the Gom term\n   */\n  public static "+fullClassName(sortName)+" fromTerm(aterm.ATerm trm, tom.library.utils.ATermConverter atConv) {\n    trm = atConv.convert(trm);\n    if(trm instanceof aterm.ATermAppl) {\n      aterm.ATermAppl appl = (aterm.ATermAppl) trm;\n      if(\""+className()+"\".equals(appl.getName())) {\n        "+fullClassName(sortName)+" res = "+fullClassName(empty.getClassName())+".make();\n\n        aterm.ATerm array[] = appl.getArgumentArray();\n        for(int i = array.length-1; i>=0; --i) {\n          "+domainClassName+" elem = "+fromATermElement("array[i]","atConv")+";\n          res = "+fullClassName(cons.getClassName())+".make(elem,res);\n        }\n        return res;\n      }\n    }\n\n    if(trm instanceof aterm.ATermList) {\n      aterm.ATermList list = (aterm.ATermList) trm;\n      "+fullClassName(sortName)+" res = "+fullClassName(empty.getClassName())+".make();\n      try {\n        while(!list.isEmpty()) {\n          "+domainClassName+" elem = "+fromATermElement("list.getFirst()","atConv")+";\n          res = "+fullClassName(cons.getClassName())+".make(elem,res);\n          list = list.getNext();\n        }\n      } catch(IllegalArgumentException e) {\n        // returns null when the fromATerm call failed\n        return null;\n      }\n      return res.reverse();\n    }\n\n    return null;\n  }\n\n  /*\n   * Checks if the Collection contains all elements of the parameter Collection\n   *\n   * @param c the Collection of elements to check\n   * @return true if the Collection contains all elements of the parameter, otherwise false\n   */\n  public boolean containsAll(java.util.Collection c) {\n    java.util.Iterator it = c.iterator();\n    while(it.hasNext()) {\n      if(!this.contains(it.next())) {\n        return false;\n      }\n    }\n    return true;\n  }\n\n  /**\n   * Checks if "+fullClassName(sortName)+" contains a specified object\n   *\n   * @param o object whose presence is tested\n   * @return true if "+fullClassName(sortName)+" contains the object, otherwise false\n   */\n  public boolean contains(Object o) {\n    "+fullClassName(sortName)+" cur = this;\n    if(o==null) { return false; }\n    if(cur instanceof "+fullClassName(cons.getClassName())+") {\n      while(cur instanceof "+fullClassName(cons.getClassName())+") {\n        if( o.equals(cur.getHead"+className()+"()) ) {\n          return true;\n        }\n        cur = cur.getTail"+className()+"();\n      }\n      if(!(cur instanceof "+fullClassName(empty.getClassName())+")) {\n        if( o.equals(cur) ) {\n          return true;\n        }\n      }\n    }\n    return false;\n  }\n\n  //public boolean equals(Object o) { return this.deepEquals(o); }\n\n  //public int hashCode() { return hashCode(); }\n\n  /**\n   * Checks the emptiness\n   *\n   * @return true if empty, otherwise false\n   */\n  public boolean isEmpty() { return isEmpty"+className()+"() ; }\n\n  public java.util.Iterator<"+primitiveToReferenceType(domainClassName)+"> iterator() {\n    return new java.util.Iterator<"+primitiveToReferenceType(domainClassName)+">() {\n      "+fullClassName(sortName)+" list = "+className()+".this;\n\n      public boolean hasNext() {\n        return list!=null && !list.isEmpty"+className()+"();\n      }\n\n      public "+primitiveToReferenceType(domainClassName)+" next() {\n        if(list.isEmpty"+className()+"()) {\n          throw new java.util.NoSuchElementException();\n        }\n        if(list.isCons"+className()+"()) {\n          "+primitiveToReferenceType(domainClassName)+" head = list.getHead"+className()+"();\n          list = list.getTail"+className()+"();\n          return head;\n        } else {\n          // we are in this case only if domain=codomain\n          // thus, the cast is safe\n          Object res = list;\n          list = null;\n          return ("+primitiveToReferenceType(domainClassName)+")res;\n        }\n      }\n\n      public void remove() {\n        throw new UnsupportedOperationException(\"Not yet implemented\");\n      }\n    };\n\n  }\n\n  public boolean add("+primitiveToReferenceType(domainClassName)+" o) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean addAll(java.util.Collection<? extends "+primitiveToReferenceType(domainClassName)+"> c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean remove(Object o) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public void clear() {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean removeAll(java.util.Collection c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  public boolean retainAll(java.util.Collection c) {\n    throw new UnsupportedOperationException(\"This object \"+this.getClass().getName()+\" is not mutable\");\n  }\n\n  /**\n   * Returns the size of the collection\n   *\n   * @return the size of the collection\n   */\n  public int size() { return length(); }\n\n  /**\n   * Returns an array containing the elements of the collection\n   *\n   * @return an array of elements\n   */\n  public Object[] toArray() {\n    int size = this.length();\n    Object[] array = new Object[size];\n    int i=0;\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "+fullClassName(cons.getClassName())+") {\n        "+primitiveToReferenceType(domainClassName)+" elem = cur.getHead"+className()+"();\n        array[i] = elem;\n        cur = cur.getTail"+className()+"();\n        i++;\n      }\n      if(!(cur instanceof "+fullClassName(empty.getClassName())+")) {\n        array[i] = cur;\n      }\n    }\n    return array;\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T[] toArray(T[] array) {\n    int size = this.length();\n    if (array.length < size) {\n      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);\n    } else if (array.length > size) {\n      array[size] = null;\n    }\n    int i=0;\n    if(this instanceof "+fullClassName(cons.getClassName())+") {\n      "+fullClassName(sortName)+" cur = this;\n      while(cur instanceof "+fullClassName(cons.getClassName())+") {\n        "+primitiveToReferenceType(domainClassName)+" elem = cur.getHead"+className()+"();\n        array[i] = (T)elem;\n        cur = cur.getTail"+className()+"();\n        i++;\n      }\n      if(!(cur instanceof "+fullClassName(empty.getClassName())+")) {\n        array[i] = (T)cur;\n      }\n    }\n    return array;\n  }\n\n  /*\n   * to get a Collection for an immutable list\n   */\n  public java.util.Collection<"+primitiveToReferenceType(domainClassName)+"> getCollection() {\n    return new Collection"+className()+"(this);\n  }\n\n  public java.util.Collection<"+primitiveToReferenceType(domainClassName)+"> getCollection"+className()+"() {\n    return new Collection"+className()+"(this);\n  }\n\n  /************************************************************\n   * private static class\n   ************************************************************/\n  private static class Collection"+className()+" implements java.util.Collection<"+primitiveToReferenceType(domainClassName)+"> {\n    private "+className()+" list;\n\n    public "+className()+" get"+className(sortName)+"() {\n      return list;\n    }\n\n    public Collection"+className()+"("+className()+" list) {\n      this.list = list;\n    }\n\n    /**\n     * generic\n     */\n  public boolean addAll(java.util.Collection<? extends "+primitiveToReferenceType(domainClassName)+"> c) {\n    boolean modified = false;\n    java.util.Iterator<? extends "+primitiveToReferenceType(domainClassName)+"> it = c.iterator();\n    while(it.hasNext()) {\n      modified = modified || add(it.next());\n    }\n    return modified;\n  }\n\n  /**\n   * Checks if the collection contains an element\n   *\n   * @param o element whose presence has to be checked\n   * @return true if the element is found, otherwise false\n   */\n  public boolean contains(Object o) {\n    return get"+className(sortName)+"().contains(o);\n  }\n\n  /**\n   * Checks if the collection contains elements given as parameter\n   *\n   * @param c elements whose presence has to be checked\n   * @return true all the elements are found, otherwise false\n   */\n  public boolean containsAll(java.util.Collection<?> c) {\n    return get"+className(sortName)+"().containsAll(c);\n  }\n\n  /**\n   * Checks if an object is equal\n   *\n   * @param o object which is compared\n   * @return true if objects are equal, false otherwise\n   */\n  @Override\n  public boolean equals(Object o) {\n    return get"+className(sortName)+"().equals(o);\n  }\n\n  /**\n   * Returns the hashCode\n   *\n   * @return the hashCode\n   */\n  @Override\n  public int hashCode() {\n    return get"+className(sortName)+"().hashCode();\n  }\n\n  /**\n   * Returns an iterator over the elements in the collection\n   *\n   * @return an iterator over the elements in the collection\n   */\n  public java.util.Iterator<"+primitiveToReferenceType(domainClassName)+"> iterator() {\n    return get"+className(sortName)+"().iterator();\n  }\n\n  /**\n   * Return the size of the collection\n   *\n   * @return the size of the collection\n   */\n  public int size() {\n    return get"+className(sortName)+"().size();\n  }\n\n  /**\n   * Returns an array containing all of the elements in this collection.\n   *\n   * @return an array of elements\n   */\n  public Object[] toArray() {\n    return get"+className(sortName)+"().toArray();\n  }\n\n  /**\n   * Returns an array containing all of the elements in this collection.\n   *\n   * @param array array which will contain the result\n   * @return an array of elements\n   */\n  public <T> T[] toArray(T[] array) {\n    return get"+className(sortName)+"().toArray(array);\n  }\n\n/*\n  public <T> T[] toArray(T[] array) {\n    int size = get"+className(sortName)+"().length();\n    if (array.length < size) {\n      array = (T[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);\n    } else if (array.length > size) {\n      array[size] = null;\n    }\n    int i=0;\n    for(java.util.Iterator it=iterator() ; it.hasNext() ; i++) {\n        array[i] = (T)it.next();\n    }\n    return array;\n  }\n*/\n    /**\n     * Collection\n     */\n\n    /**\n     * Adds an element to the collection\n     *\n     * @param o element to add to the collection\n     * @return true if it is a success\n     */\n    public boolean add("+primitiveToReferenceType(domainClassName)+" o) {\n      list = ("+className()+")"+fullClassName(cons.getClassName())+".make(o,list);\n      return true;\n    }\n\n    /**\n     * Removes all of the elements from this collection\n     */\n    public void clear() {\n      list = ("+className()+")"+fullClassName(empty.getClassName())+".make();\n    }\n\n    /**\n     * Tests the emptiness of the collection\n     *\n     * @return true if the collection is empty\n     */\n    public boolean isEmpty() {\n      return list.isEmpty"+className()+"();\n    }\n\n    public boolean remove(Object o) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n    public boolean removeAll(java.util.Collection<?> c) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n    public boolean retainAll(java.util.Collection<?> c) {\n      throw new UnsupportedOperationException(\"Not yet implemented\");\n    }\n\n  }\n\n"










































































































































































































































































































































































































































);
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
    { /* unamed block */{ /* unamed block */if ( (hooks instanceof tom.gom.adt.objects.types.HookList) ) {if ( (((( tom.gom.adt.objects.types.HookList )hooks) instanceof tom.gom.adt.objects.types.hooklist.ConsConcHook) || ((( tom.gom.adt.objects.types.HookList )hooks) instanceof tom.gom.adt.objects.types.hooklist.EmptyConcHook)) ) { tom.gom.adt.objects.types.HookList  tomMatch609_end_4=(( tom.gom.adt.objects.types.HookList )hooks);do {{ /* unamed block */if (!( tomMatch609_end_4.isEmptyConcHook() )) { tom.gom.adt.objects.types.Hook  tomMatch609_8= tomMatch609_end_4.getHeadConcHook() ;if ( ((( tom.gom.adt.objects.types.Hook )tomMatch609_8) instanceof tom.gom.adt.objects.types.hook.MappingHook) ) {

        CodeGen.generateCode( tomMatch609_8.getCode() ,writer);
        hasHook = true;
        // if there is a mapping hook we stop here
        return;
      }}if ( tomMatch609_end_4.isEmptyConcHook() ) {tomMatch609_end_4=(( tom.gom.adt.objects.types.HookList )hooks);} else {tomMatch609_end_4= tomMatch609_end_4.getTailConcHook() ;}}} while(!( (tomMatch609_end_4==(( tom.gom.adt.objects.types.HookList )hooks)) ));}}}}{ /* unamed block */{ /* unamed block */if ( (cons instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )cons) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch610_1= (( tom.gom.adt.objects.types.GomClass )cons).getSlotFields() ;if ( (((( tom.gom.adt.objects.types.SlotFieldList )tomMatch610_1) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )tomMatch610_1) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {if (!( tomMatch610_1.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch610_8= tomMatch610_1.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch610_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch610_5= tomMatch610_1.getTailConcSlotField() ;if (!( tomMatch610_5.isEmptyConcSlotField() )) {if (  tomMatch610_5.getTailConcSlotField() .isEmptyConcSlotField() ) {






    ClassName emptyClass = empty.getClassName();
    ClassName consClass = cons.getClassName();
    writer.write("\n%oplist "+className(sortName)+" "+className()+"("+className( tomMatch610_8.getDomain() )+"*) {\n  is_fsym(t) { (($t instanceof "+fullClassName(consClass)+") || ($t instanceof "+fullClassName(emptyClass)+")) }\n  make_empty() { "+fullClassName(emptyClass)+".make() }\n  make_insert(e,l) { "+fullClassName(consClass)+".make($e,$l) }\n  get_head(l) { $l."+getMethod( tomMatch610_1.getHeadConcSlotField() )+"() }\n  get_tail(l) { $l."+getMethod( tomMatch610_5.getHeadConcSlotField() )+"() }\n  is_empty(l) { $l."+isOperatorMethod(emptyClass)+"() }\n}\n"








);
      }}}}}}}}}

    return;
  }

  /** the class logger instance*/
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
//@primitiveToReferenceType(domainClassName)@ elem = this.getHead@className()@();
