/*
 * Gom
 *
 * Copyright (c) 2006-2011, INPL, INRIA
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

package tom.gom.backend.ada.shared;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import tom.gom.backend.ada.TemplateHookedClass;
import tom.gom.backend.ada.TemplateClass;
import tom.gom.backend.CodeGen;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;
import tom.platform.OptionManager;

public class VariadicOperatorTemplate extends tom.gom.backend.ada.TemplateHookedClass {
  ClassName abstractType;
  ClassName sortName;
  GomClass empty;
  GomClass cons;
  String comments;

  %include { ../../../adt/objects/Objects.tom}

  public VariadicOperatorTemplate(File tomHomePath,
                                  OptionManager manager,
                                  List importList, 	
                                  GomClass gomClass,
                                  tom.gom.backend.TemplateClass mapping,
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

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {
writer.write(%[
with @getPackage()@; use @getPackage()@;
@generateImport()@

package @fullClassName()@ is

function length(this: @className()@) return Integer ;

function reverse(this: @fullClassName(sortName)@) return @fullClassName(sortName)@ ;

end ;
 ]%) ;
}

  public void generate(java.io.Writer writer) throws java.io.IOException {
  writer.write(
%[
package body @fullClassName()@ is
]%);

  generateBody(writer);

writer.write(%[
end @fullClassName()@ ;
]%);

}


  private void generateBody(java.io.Writer writer) throws java.io.IOException {
    String domainClassName = fullClassName(cons.getSlotFields().getHeadConcSlotField().getDomain());

writer.write(%[

function length(this: @className()@) return Integer is
tl : @fullClassName(sortName)@;
begin
    if(this instanceof @fullClassName(cons.getClassName())@) then 
      tl = this.getTail@className()@();
      if (tl in @className()@) then
        return 1+((@className()@)tl).length();
       else 
        return 2;
     end if; 
     else 
      return 0;
    end if; 
end;

  overriding
function reverse(this: @fullClassName(sortName)@) return @fullClassName(sortName)@ is 
cur : @fullClassName(sortName)@ ;
rev : @fullClassName(sortName)@ ;
begin
    if(this in @fullClassName(cons.getClassName())@) then 
      @fullClassName(sortName)@ cur = this;
      @fullClassName(sortName)@ rev = @fullClassName(empty.getClassName())@.make();
      
      while (cur in @fullClassName(cons.getClassName())@) loop
        rev = @fullClassName(cons.getClassName())@.make(cur.getHead@className()@(),rev);
        cur = cur.getTail@className()@();
      end loop;
]%);
    if(fullClassName(sortName).equals(domainClassName)) { /* domain = codomain*/ 
      writer.write(%[
      if(!(cur in @fullClassName(empty.getClassName())@)) {
        rev = @fullClassName(cons.getClassName())@.make(cur,rev);
      }
]%);
    }
    writer.write(%[
      return rev;
     else 
      return this;
    end if;
end ;

  function append(this: @fullClassName(sortName)@, element: @domainClassName@) return @fullClassName(sortName)@ is
tl : @fullClassName(sortName)@;
begin 
    if(this instanceof @fullClassName(cons.getClassName())@) {
      tl = this.getTail@className()@();
      if (tl in @className()@) then 
        return @fullClassName(cons.getClassName())@.make(this.getHead@className()@(),((@className()@)tl).append(element));
       else 
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
     else 
      return @fullClassName(cons.getClassName())@.make(element,this);
   end if;
end ; 


function contains(this: @fullClassName(sortName)@, o: Object)
    cur : @fullClassName(sortName)@ cur;
    begin 
    cur = (@fullClassName(sortName)@) this;
    if(o==null) then return false; end if;
    if(cur instanceof @fullClassName(cons.getClassName())@) then 
      while(cur instanceof @fullClassName(cons.getClassName())@) loop 
        if( o.equals(cur.getHead@className()@()) ) then 
          return true;
        end if; 
        cur = cur.getTail@className()@();
      end loop; 
      if(!(cur instanceof @fullClassName(empty.getClassName())@)) then 
        if( o.equals(cur) ) then 
          return true;
        end if; 
      end if; 
    end if; 
    return false;
end;


function isEmpty(this: @fullClassName(sortName)@) is
begin
 return isEmpty@className()@() ;
end;
 


  
]%);
    return;
  }

  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
