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
                            Mapping=mapping,
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

  public @domainClassName@[] toArray() {
    @domainClassName@[] array;
    if(this instanceof @fullClassName(cons.getClassName())@) {
      @domainClassName@ h = ((@fullClassName(cons.getClassName())@)this).getHead@className()@();
      @fullClassName(sortName)@ tl = ((@fullClassName(cons.getClassName())@)this).getTail@className()@();
      if (tl instanceof @className()@) {
        @domainClassName@[] tailArray =((@className()@)tl).toArray();
        array = new @domainClassName@[1+tailArray.length];
        array[0]=h;
        for(int i =0;i<tailArray.length;i++){
          array[i+1]=tailArray[i];
        }
      } else {
        array = new @domainClassName@[1];
        array[0]=h;
      }
    } else {
      array = new @domainClassName@[0];
    }
    return array;
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
