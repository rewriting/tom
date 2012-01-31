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

public class OperatorTemplate extends tom.gom.backend.ada.TemplateHookedClass {
  ClassName abstractType;
  ClassName extendsType;
  ClassName sortName;
  SlotFieldList slotList;
  String comments;
  boolean multithread;
  boolean maximalsharing;
  boolean jmicompatible;

  %include { ../../../adt/objects/Objects.tom}

  public OperatorTemplate(File tomHomePath,
                          OptionManager manager,
                          List importList, 	
                          GomClass gomClass,
                          tom.gom.backend.TemplateClass mapping,
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
]%);


  if (hooks.containsTomCode()) {
    mapping.generate(writer);
  }
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


	function getArity(this: @className()@) return Integer is
	begin
		return 2;
	end;

	-- Stop - Name & Arity

]%);

writer.write(%[
    -- the proto is a constant object: no need to clone it

	function duplicate(this: @className()@) return @className()@ is
	begin
		return this;
	end;
]%);
  }

// TODO: Comparing childs/symbolName?
writer.write(%[
function compareTo(this: @className()@, o: SharedObject''Class) return Integer is
	begin

		-- Compare directly
		if (o in @className()@) then
		  if (ao = this) then return 0; 
		  end if;
		end if; 

		-- Compare by hashCode
		if this.hashCode != ao.hashCode then return (this.hashCode < ao.hashCode())?-1:1; end if;

		Ada.Text_IO.Put("Unable to compare @className()@");
	end;
)%];


writer.write(%[

 --SharedObject extension

	function equivalent(this: plus; o: SharedObject''Class) return Boolean is
begin
	    if(o in @className()@) then 
@generateMembersEqualityTest("peer")@
	end if; 

    return false;


]%);

writer.write(%[
   -- @className(sortName)@ interface

	function @isOperatorMethod(className)@(this: @className()@) return Boolean is
	begin
		return true;
	end ;

  ]%);

/* TODO: Visitable interface */


    writer.write(%[

function hashFunction(this: @className()@) return Integer is
begin 
    return @shared.HashFunctions.stringHashFunction(fullClassName(),slotList.length())@;
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
/*
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
*/

  private void generateMembersInit(java.io.Writer writer) throws java.io.IOException {
    %match(slotList) {
      ConcSlotField(_*,SlotField[Name=fieldName,Domain=domain],_*) -> {
        writer.write("    this.");
        writer.write(fieldName(`fieldName));
        writer.write(" = ");
        writer.write(fieldName(`fieldName));
        if (getGomEnvironment().isBuiltinClass(`domain) && `domain.equals(`ClassName("","String"))) {
          writer.write(".intern");
        }
        writer.write(";\n");
      }
    }
  }

  private String fieldName(String fieldName) {
    return "gomada_"+fieldName;
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

  public void generateConstructor(java.io.Writer writer) throws java.io.IOException {
    boolean hasHooks = false;
    %match(hooks) {
      /* If there is at least one MakeHook */
lbl:ConcHook(_*,MakeHook[HookArguments=args],_*) -> {
      hasHooks = true;
      writer.write(%[
    function make(@unprotectedChildListWithType(`args)@) return @className()@Ptr is 
  ]%);

        SlotFieldList bargs = generateMakeHooks(hooks,null,writer);
        writer.write(%[
      return realMake(@unprotectedChildList(bargs)@);
    end;
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
        CodeGen.generateCode(`code,writer);
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
            writer.write("{ $t.get"+`slotName+"}\n");
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
