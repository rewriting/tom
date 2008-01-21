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

package tom.gom.backend.strategy;

import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.backend.MappingTemplateClass;
import java.io.*;
import tom.gom.adt.objects.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class StratMappingTemplate extends MappingTemplateClass {
  GomClassList operatorClasses;

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.gom.adt.objects.types.GomClassList  tom_append_list_ConcGomClass( tom.gom.adt.objects.types.GomClassList l1,  tom.gom.adt.objects.types.GomClassList  l2) {     if( l1.isEmptyConcGomClass() ) {       return l2;     } else if( l2.isEmptyConcGomClass() ) {       return l1;     } else if(  l1.getTailConcGomClass() .isEmptyConcGomClass() ) {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,l2) ;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( l1.getHeadConcGomClass() ,tom_append_list_ConcGomClass( l1.getTailConcGomClass() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.GomClassList  tom_get_slice_ConcGomClass( tom.gom.adt.objects.types.GomClassList  begin,  tom.gom.adt.objects.types.GomClassList  end, tom.gom.adt.objects.types.GomClassList  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass.make( begin.getHeadConcGomClass() ,( tom.gom.adt.objects.types.GomClassList )tom_get_slice_ConcGomClass( begin.getTailConcGomClass() ,end,tail)) ;     }   }    

  public StratMappingTemplate(GomClass gomClass) {
    super(gomClass);
    {if ( (gomClass instanceof tom.gom.adt.objects.types.GomClass) ) {{  tom.gom.adt.objects.types.GomClass  tomMatch428NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClass )gomClass);if ( (tomMatch428NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclass.TomMapping) ) {{  tom.gom.adt.objects.types.GomClassList  tomMatch428NameNumber_freshVar_0= tomMatch428NameNumberfreshSubject_1.getOperatorClasses() ;if ( true ) {

        this.operatorClasses = tomMatch428NameNumber_freshVar_0;
        return;
      }}}}}}

    throw new GomRuntimeException(
        "Wrong argument for MappingTemplate: " + gomClass);
  }
  
  public void generateTomMapping(java.io.Writer writer) throws java.io.IOException {
    generate(writer);
  }

  public void generate(java.io.Writer writer) throws java.io.IOException {
    writer.write("\n   /*\n   %include { mustrategy.tom }\n   */\n"



);
    {if ( (operatorClasses instanceof tom.gom.adt.objects.types.GomClassList) ) {{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);if ( ((tomMatch429NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || (tomMatch429NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) {{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_0=tomMatch429NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_begin_2=tomMatch429NameNumber_freshVar_0;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_end_3=tomMatch429NameNumber_freshVar_0;do {{{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_1=tomMatch429NameNumber_end_3;if (!( tomMatch429NameNumber_freshVar_1.isEmptyConcGomClass() )) {if ( ( tomMatch429NameNumber_freshVar_1.getHeadConcGomClass()  instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {{  tom.gom.adt.objects.types.GomClass  tom_op= tomMatch429NameNumber_freshVar_1.getHeadConcGomClass() ;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_4= tomMatch429NameNumber_freshVar_1.getTailConcGomClass() ;if ( true ) {

        writer.write(
            (new tom.gom.backend.strategy.SOpTemplate(tom_op)).generateMapping());
        writer.write(
            (new tom.gom.backend.strategy.IsOpTemplate(tom_op)).generateMapping());
        writer.write(
            (new tom.gom.backend.strategy.MakeOpTemplate(tom_op)).generateMapping());
      }}}}}}if ( tomMatch429NameNumber_end_3.isEmptyConcGomClass() ) {tomMatch429NameNumber_end_3=tomMatch429NameNumber_begin_2;} else {tomMatch429NameNumber_end_3= tomMatch429NameNumber_end_3.getTailConcGomClass() ;}}} while(!( tomMatch429NameNumber_end_3.equals(tomMatch429NameNumber_begin_2) ));}}}}}}if ( (operatorClasses instanceof tom.gom.adt.objects.types.GomClassList) ) {{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumberfreshSubject_1=(( tom.gom.adt.objects.types.GomClassList )operatorClasses);if ( ((tomMatch429NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclasslist.ConsConcGomClass) || (tomMatch429NameNumberfreshSubject_1 instanceof tom.gom.adt.objects.types.gomclasslist.EmptyConcGomClass)) ) {{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_6=tomMatch429NameNumberfreshSubject_1;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_begin_8=tomMatch429NameNumber_freshVar_6;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_end_9=tomMatch429NameNumber_freshVar_6;do {{{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_7=tomMatch429NameNumber_end_9;if (!( tomMatch429NameNumber_freshVar_7.isEmptyConcGomClass() )) {if ( ( tomMatch429NameNumber_freshVar_7.getHeadConcGomClass()  instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) {{  tom.gom.adt.objects.types.ClassName  tomMatch429NameNumber_freshVar_12=  tomMatch429NameNumber_freshVar_7.getHeadConcGomClass() .getClassName() ;{  tom.gom.adt.objects.types.GomClass  tomMatch429NameNumber_freshVar_13=  tomMatch429NameNumber_freshVar_7.getHeadConcGomClass() .getEmpty() ;{  tom.gom.adt.objects.types.GomClass  tomMatch429NameNumber_freshVar_14=  tomMatch429NameNumber_freshVar_7.getHeadConcGomClass() .getCons() ;{  tom.gom.adt.objects.types.ClassName  tom_vopName=tomMatch429NameNumber_freshVar_12;if ( (tomMatch429NameNumber_freshVar_13 instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {{  tom.gom.adt.objects.types.ClassName  tomMatch429NameNumber_freshVar_15= tomMatch429NameNumber_freshVar_13.getClassName() ;if ( (tomMatch429NameNumber_freshVar_14 instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {{  tom.gom.adt.objects.types.ClassName  tomMatch429NameNumber_freshVar_16= tomMatch429NameNumber_freshVar_14.getClassName() ;{  tom.gom.adt.objects.types.GomClassList  tomMatch429NameNumber_freshVar_10= tomMatch429NameNumber_freshVar_7.getTailConcGomClass() ;if ( true ) {





        writer.write("\n            %op Strategy _"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"(sub:Strategy) {\n            is_fsym(t) { false }\n            make(sub)  { `mu(MuVar(\"x_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"\"),Choice(_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tomMatch429NameNumber_freshVar_16)+"(sub,MuVar(\"x_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tom_vopName)+"\")),_"/* Generated by TOM (version 2.6alpha): Do not edit this file */+className(tomMatch429NameNumber_freshVar_15)+"())) }\n            }\n            "




);
      }}}}}}}}}}}}}if ( tomMatch429NameNumber_end_9.isEmptyConcGomClass() ) {tomMatch429NameNumber_end_9=tomMatch429NameNumber_begin_8;} else {tomMatch429NameNumber_end_9= tomMatch429NameNumber_end_9.getTailConcGomClass() ;}}} while(!( tomMatch429NameNumber_end_9.equals(tomMatch429NameNumber_begin_8) ));}}}}}}}

      }

  protected String fileName() {
    return fullClassName().replace('.',File.separatorChar)+".tom";
  }

}
