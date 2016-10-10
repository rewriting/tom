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

         private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }    

  /**
   * The argument is an operator class, and this template generates the
   * assotiated MakeOp strategy
   */
  public MakeOpTemplate(GomClass gomClass, GomEnvironment gomEnvironment) {
    super(gomClass,gomEnvironment);
    ClassName clsName = this.className;
    { /* unamed block */{ /* unamed block */if ( (clsName instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )clsName) instanceof tom.gom.adt.objects.types.classname.ClassName) ) {

        String newpkg =  (( tom.gom.adt.objects.types.ClassName )clsName).getPkg() .replaceFirst(".types.",".strategy.");
        String newname = "Make_"+ (( tom.gom.adt.objects.types.ClassName )clsName).getName() ;
        this.className =  tom.gom.adt.objects.types.classname.ClassName.make(newpkg, newname) ;
      }}}}{ /* unamed block */{ /* unamed block */if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )gomClass) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {



        this.operator =  (( tom.gom.adt.objects.types.GomClass )gomClass).getClassName() ;
        this.slotList =  (( tom.gom.adt.objects.types.GomClass )gomClass).getSlotFields() ;
        return;
      }}}}

    throw new GomRuntimeException(
        "Wrong argument for MakeOpTemplate: " + gomClass);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
writer.write("\npackage "+getPackage()+";\n\npublic class "+className()+" implements tom.library.sl.Strategy {\n\n  protected tom.library.sl.Environment environment;\n  public void setEnvironment(tom.library.sl.Environment env) {\n    this.environment = env;\n  }\n\n  public tom.library.sl.Environment getEnvironment() {\n    if(environment!=null) {\n      return environment;\n    } else {\n      throw new RuntimeException(\"environment not initialized\");\n    }\n  }\n\n"+generateMembers()+"\n\n  public int getChildCount() {\n    return "+nonBuiltinChildCount()+";\n  }\n  public tom.library.sl.Visitable getChildAt(int index) {\n    switch(index) {\n"+nonBuiltinsGetCases()+"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {\n    switch(index) {\n"+nonBuiltinMakeCases("child")+"\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n  public tom.library.sl.Visitable[] getChildren() {\n    return new tom.library.sl.Visitable[]{"+generateMembersList()+"};\n  }\n\n  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {\n    "+generateMembersSetChildren("children")+"\n    return this;\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {\n    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{\n    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {\n    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());\n  }\n\n  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,envt);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return environment.getSubject();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());\n    getEnvironment().setRoot(any);\n    int status = visit(i);\n    if(status == tom.library.sl.Environment.SUCCESS) {\n      return (T) getEnvironment().getRoot();\n    } else {\n      throw new tom.library.sl.VisitFailure();\n    }\n  }\n\n  public "+className()+"("+childListWithType(slotList)+") {\n"+generateMembersInit()+"\n  }\n\n  /**\n    * Builds a new "+className(operator)+"\n    * If one of the sub-strategies application fails, throw a VisitFailure\n    */\n\n  @SuppressWarnings(\"unchecked\")\n  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {\n"+computeNewChildren(slotList,"any","i")+"\n    return (T) "+fullClassName(operator)+".make("+genMakeArguments(slotList,false)+");\n  }\n\n  public int visit(tom.library.sl.Introspector i) {\n"+computeSLNewChildren(slotList,"i")+"\n    getEnvironment().setSubject("+fullClassName(operator)+".make("+genMakeArguments(slotList,false)+"));\n    return tom.library.sl.Environment.SUCCESS;\n  }\n}\n"




































































































);
  }

  public String generateMapping() {
    String prefix = className();
    return "\n  %op Strategy "+className()+"("+genStratArgs(prefix,slotList,"arg")+") {\n    is_fsym(t) { (($t!=null) && ($t instanceof "+fullClassName()+")) }\n"+genGetSlot(prefix,slotList,"arg")+"\n    make("+genMakeArguments(slotList,false)+") { new "+fullClassName()+"("+genMakeArguments(slotList,true)+") }\n  }\n"





;
  }

private String genGetSlot(String prefix,SlotFieldList slots,String arg) {
  StringBuilder out = new StringBuilder();
  int i = 0;
  { /* unamed block */{ /* unamed block */if ( (slots instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch625_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);do {{ /* unamed block */if (!( tomMatch625_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch625_9= tomMatch625_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch625_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom___domain= tomMatch625_9.getDomain() ;

      if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
        out.append("\n        get_slot("+prefix+arg+i+", t) { (tom.library.sl.Strategy)(("+fullClassName()+")$t).getChildAt("+i+") }"
);
      } else {
        out.append("\n        get_slot("+prefix+arg+i+", t) { ((tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(fullClassName(tom___domain))+">)((("+fullClassName()+")$t).getChildAt("+i+"))).getBuiltin() }"
); 
      }
      i++; 
    }}if ( tomMatch625_end_4.isEmptyConcSlotField() ) {tomMatch625_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);} else {tomMatch625_end_4= tomMatch625_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch625_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slots)) ));}}}}

  return out.toString();
}

private String genStratArgs(String prefix,SlotFieldList slots,String arg) {
    StringBuilder args = new StringBuilder();
    int i = 0;
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();

      { /* unamed block */{ /* unamed block */if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { tom.gom.adt.objects.types.ClassName  tom___domain= (( tom.gom.adt.objects.types.SlotField )head).getDomain() ;

          args.append((i==0?"":", "));
          args.append(prefix);
          args.append(arg);
          args.append(i);
          if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
            args.append(":Strategy");
          } else {
            args.append(":");
            args.append(fullClassName(tom___domain));
          }
        }}}}

      i++;
    }
    return args.toString();
  }

  private String genNonBuiltin() {
    StringBuilder out = new StringBuilder();
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch627_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch627_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch627_8= tomMatch627_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch627_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        if (!getGomEnvironment().isBuiltinClass( tomMatch627_8.getDomain() )) {
          out.append("true, ");
        } else {
          out.append("false, ");
        }
      }}if ( tomMatch627_end_4.isEmptyConcSlotField() ) {tomMatch627_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch627_end_4= tomMatch627_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch627_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    if (out.length()!=0) {
      return out.substring(0,out.length()-2);
    } else {
      return out.toString();
    }
  }

  private int nonBuiltinChildCount() {
    /**
    int count = 0;
    %match(slotList) {
      ConcSlotField(_*,SlotField[Domain=domain],_*) -> {
        if (!getGomEnvironment().isBuiltinClass(`domain)) {
          count++;
        }
      }
    }
    return count;
    */
    return slotList.length();
  }

  /**
    * Store a strategy for each non builtin child, the builtin value otherwise
    */
  private String generateMembers() {
    String res="";
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch628_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch628_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch628_9= tomMatch628_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch628_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch628_9.getName() ; tom.gom.adt.objects.types.ClassName  tom___domain= tomMatch628_9.getDomain() ;

        if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
          res += "  private tom.library.sl.Strategy "+fieldName(tom___fieldName)+";\n";
        } else {
          res += "  private "+fullClassName(tom___domain)+" "+fieldName(tom___fieldName)+";\n";
        }
      }}if ( tomMatch628_end_4.isEmptyConcSlotField() ) {tomMatch628_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch628_end_4= tomMatch628_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch628_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    return res;
  }

  private String generateMembersList() {
    String res="";
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch629_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch629_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch629_9= tomMatch629_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch629_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch629_9.getName() ;

        if (!getGomEnvironment().isBuiltinClass( tomMatch629_9.getDomain() )) {
          res += fieldName(tom___fieldName) + ", ";
        } else {
          res += "new tom.library.sl.VisitableBuiltin("+ fieldName(tom___fieldName) + "), ";
        }
      }}if ( tomMatch629_end_4.isEmptyConcSlotField() ) {tomMatch629_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch629_end_4= tomMatch629_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch629_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

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
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch630_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch630_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch630_9= tomMatch630_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch630_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch630_9.getName() ;

        if (!getGomEnvironment().isBuiltinClass( tomMatch630_9.getDomain() )) {
          res += "      case "+index+": return "+fieldName(tom___fieldName)+";\n";
        } else {
          res += "      case "+index+": return new tom.library.sl.VisitableBuiltin("+fieldName(tom___fieldName)+");\n";
        }
        index++;
      }}if ( tomMatch630_end_4.isEmptyConcSlotField() ) {tomMatch630_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch630_end_4= tomMatch630_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch630_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    return res;
  }

  private String nonBuiltinMakeCases(String argName) {
    String res = "";
    int index = 0;
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch631_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch631_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch631_9= tomMatch631_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch631_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch631_9.getName() ; tom.gom.adt.objects.types.ClassName  tom___domain= tomMatch631_9.getDomain() ;

        if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
          res += "      case "+index+": "+fieldName(tom___fieldName)+" = (tom.library.sl.Strategy) "+argName+"; return this;\n"
;
        } else {
          res += "      case "+index+": "+fieldName(tom___fieldName)+" =\n            ((tom.library.sl.VisitableBuiltin<"+primitiveToReferenceType(fullClassName(tom___domain))+">) "+argName+").getBuiltin(); return this;"
;
        }
        index++;
      }}if ( tomMatch631_end_4.isEmptyConcSlotField() ) {tomMatch631_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch631_end_4= tomMatch631_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch631_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

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
      { /* unamed block */{ /* unamed block */if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___name= (( tom.gom.adt.objects.types.SlotField )head).getName() ; tom.gom.adt.objects.types.ClassName  tom___domain= (( tom.gom.adt.objects.types.SlotField )head).getDomain() ;

          if (res.length()!=0) {
            res.append(", ");
          }
          if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
            res.append("tom.library.sl.Strategy ");
            res.append(fieldName(tom___name));
          } else {
            res.append(fullClassName(tom___domain));
            res.append(" ");
            res.append(fieldName(tom___name));
          }
        }}}}

    }
    return res.toString();
  }

  /**
   * Generate code to initialize all members of the strategy
   */
  private String computeNewChildren(SlotFieldList slots, String argName, String introspectorName) {
    String res = "";
    { /* unamed block */{ /* unamed block */if ( (slots instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch633_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);do {{ /* unamed block */if (!( tomMatch633_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch633_9= tomMatch633_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch633_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch633_9.getName() ; tom.gom.adt.objects.types.ClassName  tom___domain= tomMatch633_9.getDomain() ;

        if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
          res += "\n    Object tmp"+fieldName(tom___fieldName)+" = "+fieldName(tom___fieldName)+".visitLight("+argName+","+introspectorName+");\n    if (! (tmp"+fieldName(tom___fieldName)+" instanceof "+fullClassName(tom___domain)+")) {\n      throw new tom.library.sl.VisitFailure();\n    }\n    "+fullClassName(tom___domain)+" new"+fieldName(tom___fieldName)+" = ("+fullClassName(tom___domain)+") tmp"+fieldName(tom___fieldName)+";\n"





;
        }
      }}if ( tomMatch633_end_4.isEmptyConcSlotField() ) {tomMatch633_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);} else {tomMatch633_end_4= tomMatch633_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch633_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slots)) ));}}}}

    return res;
  }

  /**
   * Generate code to initialize all members of the strategy with the sl scheme
   */
  private String computeSLNewChildren(SlotFieldList slots, String introspectorName) {
    String res = "";
    { /* unamed block */{ /* unamed block */if ( (slots instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slots) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch634_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);do {{ /* unamed block */if (!( tomMatch634_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch634_9= tomMatch634_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch634_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___fieldName= tomMatch634_9.getName() ; tom.gom.adt.objects.types.ClassName  tom___domain= tomMatch634_9.getDomain() ;

        if (!getGomEnvironment().isBuiltinClass(tom___domain)) {
          res += "\n    ("+fieldName(tom___fieldName)+").visit("+introspectorName+");\n    if (! (getEnvironment().getSubject() instanceof "+fullClassName(tom___domain)+")) {\n      return tom.library.sl.Environment.FAILURE;\n    }\n    "+fullClassName(tom___domain)+" new"+fieldName(tom___fieldName)+" = ("+fullClassName(tom___domain)+") getEnvironment().getSubject();\n"





;
        }
      }}if ( tomMatch634_end_4.isEmptyConcSlotField() ) {tomMatch634_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slots);} else {tomMatch634_end_4= tomMatch634_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch634_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slots)) ));}}}}

    return res;
  }

  /**
    * Generate the computation of all new children for the target
    */
  private String generateMembersInit() {
    String res = "";
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch635_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch635_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch635_8= tomMatch635_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch635_8) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___name= tomMatch635_8.getName() ;

        res += "    this."+fieldName(tom___name)+" = "+fieldName(tom___name)+";\n";
      }}if ( tomMatch635_end_4.isEmptyConcSlotField() ) {tomMatch635_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch635_end_4= tomMatch635_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch635_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    return res;
  }

  private String generateMembersSetChildren(String array) {
    String res = "";
    int index = 0;
    { /* unamed block */{ /* unamed block */if ( (slotList instanceof tom.gom.adt.objects.types.SlotFieldList) ) {if ( (((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField) || ((( tom.gom.adt.objects.types.SlotFieldList )slotList) instanceof tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField)) ) { tom.gom.adt.objects.types.SlotFieldList  tomMatch636_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);do {{ /* unamed block */if (!( tomMatch636_end_4.isEmptyConcSlotField() )) { tom.gom.adt.objects.types.SlotField  tomMatch636_9= tomMatch636_end_4.getHeadConcSlotField() ;if ( ((( tom.gom.adt.objects.types.SlotField )tomMatch636_9) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) {

        if (!getGomEnvironment().isBuiltinClass( tomMatch636_9.getDomain() )) {
          res += "    this."+fieldName( tomMatch636_9.getName() )+" = (tom.library.sl.Strategy)"+array+"["+index+"];\n";
          index++;
        }
      }}if ( tomMatch636_end_4.isEmptyConcSlotField() ) {tomMatch636_end_4=(( tom.gom.adt.objects.types.SlotFieldList )slotList);} else {tomMatch636_end_4= tomMatch636_end_4.getTailConcSlotField() ;}}} while(!( (tomMatch636_end_4==(( tom.gom.adt.objects.types.SlotFieldList )slotList)) ));}}}}

    return res;
  }

  /**
    * Generate the argument list for the operator construction, using the
    * values computed by computeNewChildren
    */
  private String genMakeArguments(SlotFieldList slots, boolean withDollar) {
    StringBuilder res = new StringBuilder();
    while(!slots.isEmptyConcSlotField()) {
      SlotField head = slots.getHeadConcSlotField();
      slots = slots.getTailConcSlotField();
      { /* unamed block */{ /* unamed block */if ( (head instanceof tom.gom.adt.objects.types.SlotField) ) {if ( ((( tom.gom.adt.objects.types.SlotField )head) instanceof tom.gom.adt.objects.types.slotfield.SlotField) ) { String  tom___name= (( tom.gom.adt.objects.types.SlotField )head).getName() ;

          if (res.length()!=0) {
            res.append(", ");
          }
          if (!getGomEnvironment().isBuiltinClass( (( tom.gom.adt.objects.types.SlotField )head).getDomain() )) {
            res.append(" ");
            if(withDollar) {
              res.append("$");
            }
            res.append("new");
            res.append(fieldName(tom___name));
          } else {
            res.append(" ");
            if(withDollar) {
              res.append("$");
            }
            res.append(fieldName(tom___name));
          }
        }}}}

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
