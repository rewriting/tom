/*
* Gom
*
* Copyright (c) 2006-2010, INPL, INRIA
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

package tom.gom.backend.strategy;

import java.io.*;
import java.util.logging.*;
import tom.gom.backend.TemplateClass;
import tom.gom.tools.GomEnvironment;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.objects.types.*;

public class MakeOpTemplate extends TemplateClass {
ClassName operator;
SlotFieldList slotList;



  private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {
    if( l1.isEmptyConcSlotField() ) {
      return l2;
    } else if( l2.isEmptyConcSlotField() ) {
      return l1;
    } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;
    } else {
      return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;
    }
  }
  private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;
  }
  

/**
* The argument is an operator class, and this template generates the
* assotiated MakeOp strategy
*/
public MakeOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
super(gomClass,gomEnvironment);
ClassName clsName = this.className;

{
{
if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {
if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

String newpkg = 
 (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() .replaceFirst(".types.",".strategy.");
String newname = "Make_"+
 (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;
this.className = 
 tom.gom.adt.objects.types.classname.ClassName.make(newpkg, newname) ;


}
}

}

}
{
{
if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {
if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {

this.operator = 
 (( tom.gom.adt.objects.types.GomClass )gomClass).getClassName() ;
this.slotList = 
 (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
return;


}
}

}

}

throw new GomRuntimeException(
"Wrong argument for MakeOpTemplate: " + gomClass);
}

public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write(
"\npackage "+getPackage()+
";\n\npublic class "+className()+
" implements tom.library.sl.Strategy {\n\n  protected tom.library.sl.Environment environment;\n  public void setEnvironment(tom.library.sl.Environment env) {\n    this.environment = env;\n  }\n\n  public tom.library.sl.Environment getEnvironment() {\n    if(environment!=null) {\n      return environment;\n    } else {\n      throw new RuntimeException(\"environment not initialized\");\n    }\n  }\n\n"+generateMembers()+
"\n\n  public int getChildCount() {\n    return "+nonBuiltinChildCount()+
";\n  }\n  public tom.library.sl.Visitable getChildAt(int index) {\n    switch(index) {\n"+nonBuiltinsGetCases()+
"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {\n    switch(index) {\n"+nonBuiltinMakeCases("child")+
"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n  public tom.library.sl.Visitable[] getChildren() {\n    return new tom.library.sl.Visitable[]{"+generateMembersList()+
"};\n  }\n\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    "+generateMembersSetChildren("children")+
"\n    return this;\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{\n    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {\n    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,envt);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getSubject();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());\n    getEnvironment().setRoot(any);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return (T) getEnvironment().getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public "+className()+
"("+childListWithType(slotList)+
") {\n"+generateMembersInit()+
"\n  }\n\n  /**\n    * Builds a new "+className(operator)+
"\n    * If one of the sub-strategies application fails, throw a VisitFailure\n    */\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n"+computeNewChilds(slotList,"any","i")+
"\n    return (T) "+fullClassName(operator)+
".make("+genMakeArguments(slotList,false)+
");\n  }\n\n  public int visit(tom.library.sl.Introspector i) {\n"+computeSLNewChilds(slotList,"i")+
"\n    getEnvironment().setSubject("+fullClassName(operator)+
".make("+genMakeArguments(slotList,false)+
"));\n    return tom.library.sl.Environment.SUCCESS;\n  }\n}\n");
}

public String generateMapping() {
return 
"\n  %op Strategy "+className()+
"("+genStratArgs(slotList,"arg")+
") {\n    is_fsym(t) { (($t!=null) && ($t instanceof "+fullClassName()+
")) }\n"+genGetSlot(slotList.length(),"arg")+
"\n    make("+genMakeArguments(slotList,false)+
") { new "+fullClassName()+
"("+genMakeArguments(slotList,true)+
") }\n  }\n";
}

private String genGetSlot(int count, String arg) {
StringBuilder out = new StringBuilder();
for (int i = 0; i < count; ++i) {
out.append(
"\n        get_slot("+arg+i+
", t) { (tom.library.sl.Strategy)(("+fullClassName()+
")$t).getChildAt("+i+
") }");
}
return out.toString();
}

private String genStratArgs(SlotFieldList slots,String arg) {
StringBuilder args = new StringBuilder();
int i = 0;
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();


{
{
if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )head).getDomain() ;

args.append((i==0?"":", "));
args.append(arg);
args.append(i);
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
args.append(":Strategy");
} else {
args.append(":");
args.append(fullClassName(
tom_domain));
}


}
}

}

}

i++;
}
return args.toString();
}

private String genNonBuiltin() {
StringBuilder out = new StringBuilder();

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch454__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch454__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch454_8= tomMatch454__end__4.getHeadConcSlotField() ;
if ( (tomMatch454_8 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch454_8.getDomain() )) {
out.append("true, ");
} else {
out.append("false, ");
}


}
}
if ( tomMatch454__end__4.isEmptyConcSlotField() ) {
tomMatch454__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch454__end__4= tomMatch454__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch454__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

if (out.length()!=0) {
return out.substring(0,out.length()-2);
} else {
return out.toString();
}
}

private int nonBuiltinChildCount() {
int count = 0;

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch455__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch455__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch455_8= tomMatch455__end__4.getHeadConcSlotField() ;
if ( (tomMatch455_8 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch455_8.getDomain() )) {
count++;
}


}
}
if ( tomMatch455__end__4.isEmptyConcSlotField() ) {
tomMatch455__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch455__end__4= tomMatch455__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch455__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return count;
}

/**
* Store a strategy for each non builtin child, the builtin value otherwise
*/
private String generateMembers() {
String res="";

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch456__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch456__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch456_9= tomMatch456__end__4.getHeadConcSlotField() ;
if ( (tomMatch456_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch456_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch456_9.getDomain() ;

if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res += "  private tom.library.sl.Strategy "+fieldName(
tom_fieldName)+";\n";
} else {
res += "  private "+fullClassName(
tom_domain)+" "+fieldName(
tom_fieldName)+";\n";
}


}
}
if ( tomMatch456__end__4.isEmptyConcSlotField() ) {
tomMatch456__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch456__end__4= tomMatch456__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch456__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return res;
}

private String generateMembersList() {
String res="";

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch457__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch457__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch457_9= tomMatch457__end__4.getHeadConcSlotField() ;
if ( (tomMatch457_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch457_9.getDomain() )) {
res += fieldName(
 tomMatch457_9.getName() ) + ", ";
}
// else : Skip builtin childs


}
}
if ( tomMatch457__end__4.isEmptyConcSlotField() ) {
tomMatch457__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch457__end__4= tomMatch457__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch457__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

if (res.length() != 0) {
return res.substring(0,res.length()-2);
} else {
return res;
}
}

/**
* Generate "case: " instructions for each non builtin child
* XXX: this code in duplicated from OperatorTemplate, need to be factorized
*/
private String nonBuiltinsGetCases() {
String res = "";
int index = 0;

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch458__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch458__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch458_9= tomMatch458__end__4.getHeadConcSlotField() ;
if ( (tomMatch458_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch458_9.getDomain() )) {
res += "      case "+index+": return "+fieldName(
 tomMatch458_9.getName() )+";\n";
index++;
}


}
}
if ( tomMatch458__end__4.isEmptyConcSlotField() ) {
tomMatch458__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch458__end__4= tomMatch458__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch458__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return res;
}

private String nonBuiltinMakeCases(String argName) {
String res = "";
int index = 0;

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch459__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch459__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch459_9= tomMatch459__end__4.getHeadConcSlotField() ;
if ( (tomMatch459_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch459_9.getDomain() )) {
res += 
"      case "+index+
": "+fieldName(
 tomMatch459_9.getName() )+
" = (tom.library.sl.Strategy) "+argName+
"; return this;\n";
index++;
}


}
}
if ( tomMatch459__end__4.isEmptyConcSlotField() ) {
tomMatch459__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch459__end__4= tomMatch459__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch459__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return res;
}

/**
* Generate the child list to be used as function parameter declaration
* Each non builtin child has type VisitableVisitor
*/
private String childListWithType(SlotFieldList slots) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_name= (( tom.gom.adt.objects.types.SlotField )head).getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= (( tom.gom.adt.objects.types.SlotField )head).getDomain() ;

if (res.length()!=0) {
res.append(", ");
}
if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res.append("tom.library.sl.Strategy ");
res.append(fieldName(
tom_name));
} else {
res.append(fullClassName(
tom_domain));
res.append(" ");
res.append(fieldName(
tom_name));
}


}
}

}

}

}
return res.toString();
}

/**
* Generate code to initialize all members of the strategy
*/
private String computeNewChilds(SlotFieldList slots, String argName, String introspectorName) {
String res = "";

{
{
if ( (slots instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch461__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slots);
do {
{
if (!( tomMatch461__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch461_9= tomMatch461__end__4.getHeadConcSlotField() ;
if ( (tomMatch461_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch461_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch461_9.getDomain() ;

if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res += 
"\n    Object tmp"+fieldName(
tom_fieldName)+
" = "+fieldName(
tom_fieldName)+
".visit("+argName+
","+introspectorName+
");\n    if (! (tmp"+fieldName(
tom_fieldName)+
" instanceof "+fullClassName(
tom_domain)+
")) {\n      throw new tom.library.sl.VisitFailure();\n    }\n    "+fullClassName(
tom_domain)+
" new"+fieldName(
tom_fieldName)+
" = ("+fullClassName(
tom_domain)+
") tmp"+fieldName(
tom_fieldName)+
";\n";
}


}
}
if ( tomMatch461__end__4.isEmptyConcSlotField() ) {
tomMatch461__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slots);
} else {
tomMatch461__end__4= tomMatch461__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch461__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slots)) ));
}
}

}

}

return res;
}

/**
* Generate code to initialize all members of the strategy with the sl scheme
*/
private String computeSLNewChilds(SlotFieldList slots, String introspectorName) {
String res = "";

{
{
if ( (slots instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch462__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slots);
do {
{
if (!( tomMatch462__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch462_9= tomMatch462__end__4.getHeadConcSlotField() ;
if ( (tomMatch462_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_fieldName= tomMatch462_9.getName() ;
 tom.gom.adt.objects.types.ClassName  tom_domain= tomMatch462_9.getDomain() ;

if (!getGomEnvironment().isBuiltinClass(
tom_domain)) {
res += 
"\n    ("+fieldName(
tom_fieldName)+
").visit("+introspectorName+
");\n    if (! (getEnvironment().getSubject() instanceof "+fullClassName(
tom_domain)+
")) {\n      return tom.library.sl.Environment.FAILURE;\n    }\n    "+fullClassName(
tom_domain)+
" new"+fieldName(
tom_fieldName)+
" = ("+fullClassName(
tom_domain)+
") getEnvironment().getSubject();\n";
}


}
}
if ( tomMatch462__end__4.isEmptyConcSlotField() ) {
tomMatch462__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slots);
} else {
tomMatch462__end__4= tomMatch462__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch462__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slots)) ));
}
}

}

}

return res;
}

/**
* Generate the computation of all new childs for the target
*/
private String generateMembersInit() {
String res = "";

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch463__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch463__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch463_8= tomMatch463__end__4.getHeadConcSlotField() ;
if ( (tomMatch463_8 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_name= tomMatch463_8.getName() ;

res += "    this."+fieldName(
tom_name)+" = "+fieldName(
tom_name)+";\n";


}
}
if ( tomMatch463__end__4.isEmptyConcSlotField() ) {
tomMatch463__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch463__end__4= tomMatch463__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch463__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return res;
}

private String generateMembersSetChildren(String array) {
String res = "";
int index = 0;

{
{
if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {
if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) {
 tom.gom.adt.objects.types.SlotFieldList  tomMatch464__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
do {
{
if (!( tomMatch464__end__4.isEmptyConcSlotField() )) {
 tom.gom.adt.objects.types.SlotField  tomMatch464_9= tomMatch464__end__4.getHeadConcSlotField() ;
if ( (tomMatch464_9 instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

if (!getGomEnvironment().isBuiltinClass(
 tomMatch464_9.getDomain() )) {
res += "    this."+fieldName(
 tomMatch464_9.getName() )+" = (tom.library.sl.Strategy)"+array+"["+index+"];\n";
index++;
}


}
}
if ( tomMatch464__end__4.isEmptyConcSlotField() ) {
tomMatch464__end__4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);
} else {
tomMatch464__end__4= tomMatch464__end__4.getTailConcSlotField() ;
}

}
} while(!( (tomMatch464__end__4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));
}
}

}

}

return res;
}

/**
* Generate the argument list for the operator construction, using the
* values computed by computeNewChilds
*/
private String genMakeArguments(SlotFieldList slots, boolean withDollar) {
StringBuilder res = new StringBuilder();
while(!slots.isEmptyConcSlotField()) {
SlotField head = slots.getHeadConcSlotField();
slots = slots.getTailConcSlotField();

{
{
if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {
if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {
 String  tom_name= (( tom.gom.adt.objects.types.SlotField )head).getName() ;

if (res.length()!=0) {
res.append(", ");
}
if (!getGomEnvironment().isBuiltinClass(
 (( tom.gom.adt.objects.types.SlotField )head).getDomain() )) {
res.append(" ");
if(withDollar) {
res.append("$");
}
res.append("new");
res.append(fieldName(
tom_name));
} else {
res.append(" ");
if(withDollar) {
res.append("$");
}
res.append(fieldName(
tom_name));
}


}
}

}

}

}
return res.toString();
}
private String fieldName(String fieldName) {
return "_"+fieldName;
}

/** the class logger instance*/
private Logger getLogger() {
return Logger.getLogger(getClass().getName());
}
}
