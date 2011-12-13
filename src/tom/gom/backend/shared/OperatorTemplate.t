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

public class OperatorTemplate extends TemplateHookedClass {
  ClassName abstractType;
  ClassName extendsType;
  ClassName sortName;
  SlotFieldList slotList;
  String comments;
  boolean multithread;
  boolean maximalsharing;
  boolean jmicompatible;

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
        this.slotList = `slots;
        this.comments = `comments;
        return;
      }
    }
    throw new GomRuntimeException(
        "Bad argument for OperatorTemplate: " + gomClass);
  }

  public void generateSpec(java.io.Writer writer) throws java.io.IOException {

writer.write(
%[
with @fullClassName(extendsType)@; use @fullClassName(extendsType)@;
@generateImport()@

package @fullClassName(extendsType)@.@className()@ is
]%);

if(slotList.length()>0){
writer.write(%[
type @className()@ is new @className(extendsType)@ with 
  record
@childListWithType(slotList)@;
  end record;
]%);
} else {
writer.write(%[
type @className()@ is new @className(extendsType)@ with null record;
]%); }

writer.write(%[
type @className()@Ptr is access all @className()@;
]%);


if(slotList.length()>0) {
writer.write(%[
function make(@childListWithType(slotList)@) return @className()@Ptr;
]%);
} else {
writer.write(%[
function make return @className()@Ptr;
]%);
}


writer.write(%[

procedure init(this: in out @className()@; @childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":"; ")@ hash: Integer);
]%);

if(slotList.length()>0) {
writer.write(%[
procedure initHashCode(this: in out @className()@; @childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":"")@);
]%);
} else {
writer.write(%[
procedure initHashCode(this: in out @className()@);
]%);
}


writer.write(%[
function symbolName(this: @className()@) return String;

overriding
function toString(this: @className()@) return String;

function getArity(this: @className()@) return Integer;

overriding
function duplicate(this: @className()@) return SharedObject''Class;

function equivalent(this: @className()@; o: SharedObject''Class) return boolean;

function @isOperatorMethod(className)@(this: @className()@) return Boolean

function hashFunction(this: @className()@) return Integer

	@className()@GomProto : @className()@Ptr := new @className()@ ;

end @fullClassName(extendsType)@.@className()@; 

-- Spec End
 
]%);

}

  public void generate(java.io.Writer writer) throws java.io.IOException {

writer.write(
%[
with @getPackage()@; use @getPackage()@;
with @fullClassName(extendsType)@; use @fullClassName(extendsType)@;
@generateImport()@
]%);

  writer.write(
%[
package body @fullClassName(extendsType)@.@className()@ is
  /*@generateBlock()@*/
]%);


  if (hooks.containsTomCode()) {
    mapping.generate(writer);
  }
  generateMembers(writer);
  generateBody(writer);

}

  private void generateBody(java.io.Writer writer) throws java.io.IOException {
generateConstructor(writer);

if(slotList.length()>0) {

writer.write(%[

procedure init(this: in out @className()@; @childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":"; ")@ hash: Integer) is
	begin
]%);

generateMembersInit(writer);

writer.write(%[
    this.hashCode = hashCode;
  end;

	procedure initHashCode(this: in out @className()@; @childListWithType(slotList) + (slotList.isEmptyConcSlotField()?"":"")@) is
	begin
]%);
generateMembersInit(writer);
writer.write(%[
    this.hashCode = this.hashFunction;
 end; 
]%);

writer.write(%[
	-- Name & Arity

function toString(this: @className()@) return String is
begin
return this.symbolName&"()";
-- TODO, opt. 
end;

function symbolName(this: @className()@) return String is
begin
    return "@className()@";
end;

function getArity(this: @className()@) return String is
begin
    return @slotList.length()@;
end;

	-- Stop - Name & Arity

]%);


writer.write(%[
  /**
   * Copy the object and returns the copy
   *
   * @@return a clone of the SharedObject
   */
		-- Duplicate
	function duplicate(this: @className()@) return SharedObject''Class is
		clone : access @className()@ := new @className()@ ;
	begin
    		clone.init(@childList(slotList) + (slotList.isEmptyConcSlotField()?"":", ") @hashCode);
		return clone.all;
	end;

		-- End duplicate
  ]%);

} else {
    
writer.write(%[
	-- case: constant
	-- Name & Arity  

 function toString(this: @className()@) return String is
begin
return "@className()@";
end;

 function symbolName(this: @className()@) return String is
begin
    return "@className()@";
end;


	function getArity(this: plus) return Integer is
	begin
		return 2;
	end;

	-- Stop - Name & Arity

]%);

writer.write(%[
    -- the proto is a constant object: no need to clone it

	function duplicate(this: zero) return zero is
	begin
		return this;
	end;
  
]%);

  }


writer.write(%[

/* TODO: Comparaisons */

 //SharedObject extension

  /**
   * Checks if a SharedObject is equivalent to the current object
   *
   * @@param obj SharedObject to test
   * @@return true if obj is a @className()@ and its members are equal, else false
   */
	function equivalent(this: plus; o: SharedObject''Class) return Boolean is
begin
	    if(o in @className()@) then 
@generateMembersEqualityTest("peer")@
	end if; 

    return false;


]%);

writer.write(%[
   //@className(sortName)@ interface
  /**
   * Returns true if the term is rooted by the symbol @className.getName()@
   *
   * @@return true, because this is rooted by @className.getName()@
   */

	function @isOperatorMethod(className)@(this: @className()@) return Boolean is
	begin
		return true;
	end ;

  ]%);

/* TODO: Unnecessary in Ada? generateGetters(writer);*/

/* Hereby lies ATerm interface */


/*TODO: Handling Visitable Interface*/
if (false) {
    writer.write(%[
  /* Visitable */
  /**
   * Returns the number of childs of the term
   *
   * @@return the number of childs of the term
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
    switch(index) {
@getCases()@
      default: throw new IndexOutOfBoundsException();
    }
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
    switch(index) {
@makeCases("v")@
      default: throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Set children to the term
   *
   * @@param childs array of children to set
   * @@return an array of children which just were set
   * @@throws IndexOutOfBoundsException if length of "childs" is different than @slotList.length()@
   */
  @@SuppressWarnings("unchecked")
  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] childs) {
    if (childs.length == @slotList.length()@ @arrayCheck("childs")@) {
      return @arrayMake("childs")@;
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
    return new tom.library.sl.Visitable[] { @visitableList(slotList)@ };
  }
]%);

}


    writer.write(%[
    /**
     * Compute a hashcode for this term.
     * (for internal use)
     *
     * @@return a hash value
     */

function hashFunction(this: @className()@) return Integer is
begin 
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
  end;

]%);
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

  private String fieldName(String fieldName) {
    return "tommy_"+fieldName;
  }


  private String childListWithType(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      %match(head) {
        SlotField[Name=name, Domain=domain] -> {
          if (res.length()!=0) {
            res.append("; ");
          }
          res.append(fieldName(`name));
	  res.append(": ");
          res.append(className(`domain));
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
       peer := @className()@(o)  ;]%);;
    }
    res.append(%[
      return ]%);
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName],_*) -> {
        res.append("this.");
	res.append(fieldName(`fieldName));
        res.append("=");
        res.append(peer);
        res.append(".");
        res.append(fieldName(`fieldName));
        res.append(" and ");
      }
    }
    res.append("true;"); // to handle the "no childs" case
    return res.toString();
  }

  private String getCases() {
    StringBuilder res = new StringBuilder();
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
    return res.toString();
  }

  private String visitableList(SlotFieldList slots) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      %match(head) {
        SlotField[Domain=domain,Name=name] -> {
          if (res.length()!=0) {
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
    return res.toString();
  }


  private String visitableCount() {
    if(className().equals("ConsPath"+sortName.getName())) {
      return "0";
    } else {
      return ""+slotList.length();
    }
  }

  private String arrayMake(String arrayName) {
    StringBuilder res = new StringBuilder("make(");
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
    res.append(")");
    return res.toString();
  }

  private String arrayCheck(String arrayName) {
    StringBuilder res = new StringBuilder();
    int index = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
       if (!getGomEnvironment().isBuiltinClass(`domain)) {
         res.append(%[ && @arrayName@[@index@] in @fullClassName(`domain)@]%);
       } else {
         res.append(%[ && @arrayName@[@index@] in tom.library.sl.VisitableBuiltin]%);
       }
       index++;
      }
    }
    return res.toString();
  }
private String makeCases(String argName) {
  StringBuilder res = new StringBuilder();
  int index = 0;
  %match(slotList) {
    ConcSlotField(_*,SlotField[],_*) -> {
      res.append("      case "+index+": return make("+generateMakeArgsFor(index, argName)+");\n");
      index++;
    }
  }
  return res.toString();
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

  private String toStringChilds(String buffer) {
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

  private String genCompareChilds(String oldOther, String compareFun) {
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
    if (hasHooks) {
      makeName = "realMake";
    }
    writer.write(%[
  function @makeName@(@childListWithType(slotList)@) return @className()@Ptr is 
	this : @className()@Ptr;
 begin

]%);

    if(slotList.length()>0) {
        writer.write(%[
this := new @className()@;
@className()@GomProto.all.initHashCode(x1,x2);
this.all := @className()@(build(factory.all,GomProto.all,SharedObjectPtr(this)));
    
return this; 
]%);
    } else {
        writer.write(%[
    return @className()@GomProto;
]%);
    }
    writer.write(%[
	end;
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
        writer.write("  is_fsym(t) { ($t in "+fullClassName()+") }\n");
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
