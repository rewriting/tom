/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.optimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomoption.types.option.OriginTracking;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.PILFactory;
import tom.engine.tools.Tools;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.exception.TomRuntimeException;

import tom.library.sl.*;

/**
 * The TomOptimizer plugin.
 */
public class OptimizerPlugin extends TomGenericPlugin {

      private static   String  tom_append_list_concString( String l1,  String  l2) {     if( l1.length()==0 ) {       return l2;     } else if( l2.length()==0 ) {       return l1;     } else if(  l1.substring(1) .length()==0 ) {       return   l1.charAt(0) +l2 ;     } else {       return   l1.charAt(0) +tom_append_list_concString( l1.substring(1) ,l2) ;     }   }   private static   String  tom_get_slice_concString( String  begin,  String  end, String  tail) {     if( begin.equals(end) ) {       return tail;     } else if( end.equals(tail)  && ( end.length()==0  ||  end.equals( "" ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return   begin.charAt(0) +( String )tom_get_slice_concString( begin.substring(1) ,end,tail) ;   }        private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }    private static  tom.library.sl.Strategy  tom_make_When_EmptyconcResolveStratBlock( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.resolvestratblocklist.Is_EmptyconcResolveStratBlock(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcResolveStratBlock( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.resolvestratblocklist.Is_ConsconcResolveStratBlock(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcElementaryTransformation( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.elementarytransformationlist.Is_EmptyconcElementaryTransformation(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcElementaryTransformation( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.elementarytransformationlist.Is_ConsconcElementaryTransformation(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomEntry( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomentrylist.Is_EmptyconcTomEntry(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomEntry( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomentrylist.Is_ConsconcTomEntry(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomVisit( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomvisitlist.Is_EmptyconcTomVisit(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomVisit( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomvisitlist.Is_ConsconcTomVisit(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomSymbol( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomsymbollist.Is_EmptyconcTomSymbol(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomSymbol( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.tomsymbollist.Is_ConsconcTomSymbol(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcResolveStratElement( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.resolvestratelementlist.Is_EmptyconcResolveStratElement(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcResolveStratElement( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomsignature.strategy.resolvestratelementlist.Is_ConsconcResolveStratElement(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTypeConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.typeconstraints.strategy.typeconstraintlist.Is_EmptyconcTypeConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTypeConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.typeconstraints.strategy.typeconstraintlist.Is_ConsconcTypeConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyOrExpressionDisjunction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomexpression.strategy.expression.Is_EmptyOrExpressionDisjunction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsOrExpressionDisjunction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomexpression.strategy.expression.Is_ConsOrExpressionDisjunction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyOrConnector( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomexpression.strategy.expression.Is_EmptyOrConnector(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsOrConnector( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomexpression.strategy.expression.Is_ConsOrConnector(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomterm.strategy.tomlist.Is_EmptyconcTomTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomterm.strategy.tomlist.Is_ConsconcTomTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcDeclaration( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomdeclaration.strategy.declarationlist.Is_EmptyconcDeclaration(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcDeclaration( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomdeclaration.strategy.declarationlist.Is_ConsconcDeclaration(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomType( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomtype.strategy.tomtypelist.Is_EmptyconcTomType(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomType( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomtype.strategy.tomtypelist.Is_ConsconcTomType(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTypeOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomtype.strategy.typeoptionlist.Is_EmptyconcTypeOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTypeOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomtype.strategy.typeoptionlist.Is_ConsconcTypeOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcCode( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.codelist.Is_EmptyconcCode(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcCode( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.codelist.Is_ConsconcCode(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.bqtermlist.Is_EmptyconcBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.bqtermlist.Is_ConsconcBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyComposite( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.bqterm.Is_EmptyComposite(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsComposite( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.code.strategy.bqterm.Is_ConsComposite(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcRefClassTracelinkInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.refclasstracelinkinstructionlist.Is_EmptyconcRefClassTracelinkInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcRefClassTracelinkInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.refclasstracelinkinstructionlist.Is_ConsconcRefClassTracelinkInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcRuleInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.ruleinstructionlist.Is_EmptyconcRuleInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcRuleInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.ruleinstructionlist.Is_ConsconcRuleInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.instructionlist.Is_EmptyconcInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.instructionlist.Is_ConsconcInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcConstraintInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.constraintinstructionlist.Is_EmptyconcConstraintInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcConstraintInstruction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tominstruction.strategy.constraintinstructionlist.Is_ConsconcConstraintInstruction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyCst_AndConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraint.Is_EmptyCst_AndConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsCst_AndConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraint.Is_ConsCst_AndConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyCst_OrConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraint.Is_EmptyCst_OrConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsCst_OrConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraint.Is_ConsCst_OrConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstslotlist.Is_EmptyConcCstSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstslotlist.Is_ConsConcCstSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstBlock( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstblocklist.Is_EmptyConcCstBlock(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstBlock( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstblocklist.Is_ConsConcCstBlock(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstPairSlotBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpairslotbqtermlist.Is_EmptyConcCstPairSlotBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstPairSlotBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpairslotbqtermlist.Is_ConsConcCstPairSlotBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstoptionlist.Is_EmptyConcCstOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstoptionlist.Is_ConsConcCstOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstPattern( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpatternlist.Is_EmptyConcCstPattern(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstPattern( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpatternlist.Is_ConsConcCstPattern(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstName( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstnamelist.Is_EmptyConcCstName(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstName( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstnamelist.Is_ConsConcCstName(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstOperator( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstoperatorlist.Is_EmptyConcCstOperator(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstOperator( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstoperatorlist.Is_ConsConcCstOperator(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.csttermlist.Is_EmptyConcCstTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.csttermlist.Is_ConsConcCstTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstbqtermlist.Is_EmptyConcCstBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstBQTerm( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstbqtermlist.Is_ConsConcCstBQTerm(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstConstraintAction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraintactionlist.Is_EmptyConcCstConstraintAction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstConstraintAction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraintactionlist.Is_ConsConcCstConstraintAction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstVisit( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstvisitlist.Is_EmptyConcCstVisit(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstVisit( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstvisitlist.Is_ConsConcCstVisit(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraintlist.Is_EmptyConcCstConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstconstraintlist.Is_ConsConcCstConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstPairPattern( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpairpatternlist.Is_EmptyConcCstPairPattern(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstPairPattern( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstpairpatternlist.Is_ConsConcCstPairPattern(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyConcCstSymbol( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstsymbollist.Is_EmptyConcCstSymbol(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsConcCstSymbol( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.cst.strategy.cstsymbollist.Is_ConsConcCstSymbol(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomName( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomname.strategy.tomnamelist.Is_EmptyconcTomName(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomName( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomname.strategy.tomnamelist.Is_ConsconcTomName(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcTomNumber( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomname.strategy.tomnumberlist.Is_EmptyconcTomNumber(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcTomNumber( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomname.strategy.tomnumberlist.Is_ConsconcTomNumber(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcBQSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.bqslotlist.Is_EmptyconcBQSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcBQSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.bqslotlist.Is_ConsconcBQSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.slotlist.Is_EmptyconcSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcSlot( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.slotlist.Is_ConsconcSlot(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcPairNameDecl( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.pairnamedecllist.Is_EmptyconcPairNameDecl(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcPairNameDecl( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomslot.strategy.pairnamedecllist.Is_ConsconcPairNameDecl(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomoption.strategy.optionlist.Is_EmptyconcOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcOption( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomoption.strategy.optionlist.Is_ConsconcOption(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyAndConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_EmptyAndConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsAndConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_ConsAndConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyOrConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_EmptyOrConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsOrConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_ConsOrConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyOrConstraintDisjunction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_EmptyOrConstraintDisjunction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsOrConstraintDisjunction( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraint.Is_ConsOrConstraintDisjunction(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraintlist.Is_EmptyconcConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcConstraint( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.tomconstraint.strategy.constraintlist.Is_ConsconcConstraint(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_EmptyconcElementaryTheory( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.theory.strategy.theory.Is_EmptyconcElementaryTheory(), tom.library.sl.Sequence.make(s, null ) )  ;}private static  tom.library.sl.Strategy  tom_make_When_ConsconcElementaryTheory( tom.library.sl.Strategy  s) { return  tom.library.sl.Sequence.make( new tom.engine.adt.theory.strategy.theory.Is_ConsconcElementaryTheory(), tom.library.sl.Sequence.make(s, null ) )  ;}      private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_append_list_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList l1,  tom.platform.adt.platformoption.types.PlatformOptionList  l2) {     if( l1.isEmptyconcPlatformOption() ) {       return l2;     } else if( l2.isEmptyconcPlatformOption() ) {       return l1;     } else if(  l1.getTailconcPlatformOption() .isEmptyconcPlatformOption() ) {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,l2) ;     } else {       return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( l1.getHeadconcPlatformOption() ,tom_append_list_concPlatformOption( l1.getTailconcPlatformOption() ,l2)) ;     }   }   private static   tom.platform.adt.platformoption.types.PlatformOptionList  tom_get_slice_concPlatformOption( tom.platform.adt.platformoption.types.PlatformOptionList  begin,  tom.platform.adt.platformoption.types.PlatformOptionList  end, tom.platform.adt.platformoption.types.PlatformOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcPlatformOption()  ||  (end== tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( begin.getHeadconcPlatformOption() ,( tom.platform.adt.platformoption.types.PlatformOptionList )tom_get_slice_concPlatformOption( begin.getTailconcPlatformOption() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make(v, null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make( tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) , null ) ) ) ) );}       @SuppressWarnings("unchecked") private static java.util.ArrayList concArrayListAppend(Object o, java.util.ArrayList l) {   java.util.ArrayList res = (java.util.ArrayList)l.clone();   res.add(o);   return res; } 













  /** some output suffixes */
  private static final String OPTIMIZED_SUFFIX = ".tfix.optimized";

  /** the declared options string*/
  public static final PlatformOptionList PLATFORM_OPTIONS =
     tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize", "O", "Optimize generated code: perform inlining",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.True.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("optimize2", "O2", "Optimize generated code: discrimination tree",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") , tom.platform.adt.platformoption.types.platformoptionlist.ConsconcPlatformOption.make( tom.platform.adt.platformoption.types.platformoption.PluginOption.make("prettyPIL", "pil", "PrettyPrint IL",  tom.platform.adt.platformoption.types.platformvalue.BooleanValue.make( tom.platform.adt.platformoption.types.platformboolean.False.make() ) , "") 
        , tom.platform.adt.platformoption.types.platformoptionlist.EmptyconcPlatformOption.make() ) ) ) 



;

  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  public void optionChanged(String optionName, Object optionValue) {
    if(optionName.equals("optimize2") && ((Boolean)optionValue).booleanValue() ) {
      setOptionValue("pretty", Boolean.TRUE);
    }
  }

  // this static field is necessary for %strategy instructions that generate static code
  private static PILFactory factory = new PILFactory();
  private static Logger logger = Logger.getLogger("tom.engine.optimizer.OptimizerPlugin");

  /** Constructor */
  public OptimizerPlugin() {
    super("OptimizerPlugin");
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom optimizer : TSM" + getStreamManager().toString());
    if(getOptionBooleanValue("optimize") || getOptionBooleanValue("optimize2")) {
      /* Initialize strategies */
      long startChrono = System.currentTimeMillis();
      boolean intermediate = getOptionBooleanValue("intermediate");
      try {
       Code renamedTerm = (Code)getWorkingTerm();
        if(getOptionBooleanValue("optimize2")) {
          Strategy optStrategy2 =  tom.library.sl.Sequence.make(tom_make_InnermostId( tom.library.sl.ChoiceId.make(tom_make_NormExpr(this), tom.library.sl.ChoiceId.make(tom_make_NopElimAndFlatten(this), null ) ) ), tom.library.sl.Sequence.make(tom_make_InnermostId( tom.library.sl.ChoiceId.make( tom.library.sl.Sequence.make(( new tom.library.sl.BuiltinRepeatId(tom_make_IfSwapping(this)) ), tom.library.sl.Sequence.make(( new tom.library.sl.BuiltinRepeatId( tom.library.sl.SequenceId.make( tom.library.sl.ChoiceId.make(tom_make_BlockFusion(this), tom.library.sl.ChoiceId.make(tom_make_IfFusion(this), null ) ) , tom.library.sl.SequenceId.make(tom_make_OnceTopDownId(tom_make_NopElimAndFlatten(this)), null ) ) ) ), null ) ) , tom.library.sl.ChoiceId.make( tom.library.sl.SequenceId.make(tom_make_InterBlock(this), tom.library.sl.SequenceId.make(tom_make_OnceTopDownId(( new tom.library.sl.BuiltinRepeatId(tom_make_NopElimAndFlatten(this)) )), null ) ) , null ) ) 
                )
              , null ) ) 







;

          //System.out.println("opt2 input term = " + renamedTerm);
          renamedTerm = optStrategy2.visitLight(renamedTerm);
          renamedTerm = ( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() ,this)) ).visit(renamedTerm);
          renamedTerm = tom_make_BottomUp(tom_make_CastElim()).visitLight(renamedTerm);
          renamedTerm = optStrategy2.visitLight(renamedTerm);
          //System.out.println("opt2 renamedTerm = " + renamedTerm);

        } else if(getOptionBooleanValue("optimize")) {
          Strategy optStrategy =  tom.library.sl.Sequence.make(tom_make_InnermostId( tom.library.sl.ChoiceId.make(tom_make_NormExpr(this), tom.library.sl.ChoiceId.make(tom_make_NopElimAndFlatten(this), null ) ) ), tom.library.sl.Sequence.make(( new tom.library.sl.BuiltinBottomUp(tom_make_Inline( tom.engine.adt.tomconstraint.types.constraint.TrueConstraint.make() ,this)) ), null ) ) 

;

          renamedTerm = optStrategy.visit(renamedTerm);
          renamedTerm = tom_make_BottomUp(tom_make_CastElim()).visitLight(renamedTerm);
          //System.out.println("opt renamedTerm = " + renamedTerm);
        }
        setWorkingTerm(renamedTerm);

        // verbose
        TomMessage.info(logger, getStreamManager().getInputFileName(), 0,
            TomMessage.tomOptimizationPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        TomMessage.error(logger, getStreamManager().getInputFileName(), 0,
            TomMessage.exceptionMessage, e.getMessage());
        e.printStackTrace();
         return;
      }
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + OPTIMIZED_SUFFIX,
            (Code)getWorkingTerm() );
      }
    } else {
      // not active plugin
      TomMessage.info(logger, getStreamManager().getInputFileName(), 0,
          TomMessage.optimizerNotActive);
    }
    if(getOptionBooleanValue("prettyPIL")) {
      System.out.println(factory.prettyPrintCompiledMatch(factory.remove((Code)getWorkingTerm())));
    }
  }

  public static class Inline extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomconstraint.types.Constraint  context;private  OptimizerPlugin  optimizer;public Inline( tom.engine.adt.tomconstraint.types.Constraint  context,  OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.context=context;this.optimizer=optimizer;}public  tom.engine.adt.tomconstraint.types.Constraint  getcontext() {return context;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch260_3= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;boolean tomMatch260_48= false ; tom.engine.adt.tomname.types.TomName  tomMatch260_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_10= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_11= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_3) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch260_48= true ;tomMatch260_10=tomMatch260_3;tomMatch260_8= tomMatch260_10.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_3) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch260_48= true ;tomMatch260_11=tomMatch260_3;tomMatch260_8= tomMatch260_11.getAstName() ;}}}if (tomMatch260_48) { tom.engine.adt.tomname.types.TomName  tom___name=tomMatch260_8; tom.engine.adt.tomexpression.types.Expression  tom___exp= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ; tom.engine.adt.tominstruction.types.Instruction  tom___body= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() ;boolean tomMatch260_47= false ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch260_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tomMatch260_27= tomMatch260_8.getString() ;if ( tomMatch260_27 instanceof String ) {if (!( tomMatch260_27.length()==0 )) {if ( 't'== tomMatch260_27.charAt(0)  ) { String  tomMatch260_31= tomMatch260_27.substring(1) ;if (!( tomMatch260_31.length()==0 )) {if ( 'o'== tomMatch260_31.charAt(0)  ) { String  tomMatch260_32= tomMatch260_31.substring(1) ;if (!( tomMatch260_32.length()==0 )) {if ( 'm'== tomMatch260_32.charAt(0)  ) { String  tomMatch260_33= tomMatch260_32.substring(1) ;if (!( tomMatch260_33.length()==0 )) {if ( '_'== tomMatch260_33.charAt(0)  ) {if ( (tom___name==tomMatch260_8) ) {tomMatch260_47= true ;}}}}}}}}}}}if (!(tomMatch260_47)) {if ( (tom___exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom___exp) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) { tom.engine.adt.code.types.BQTerm  tomMatch260_12= (( tom.engine.adt.tomexpression.types.Expression )tom___exp).getAstTerm() ;boolean tomMatch260_45= false ; tom.engine.adt.code.types.BQTerm  tomMatch260_16= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_17= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_12) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch260_45= true ;tomMatch260_16=tomMatch260_12;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_12) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch260_45= true ;tomMatch260_17=tomMatch260_12;}}}if (tomMatch260_45) {


















          return tom_make_TopDown(tom_make_replaceVariableByExpression(tom___name,tom___exp)).visitLight(tom___body);
      }}}if ( (tom___exp instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom___exp) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch260_18= (( tom.engine.adt.tomexpression.types.Expression )tom___exp).getSource() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch260_18) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) { tom.engine.adt.code.types.BQTerm  tomMatch260_21= tomMatch260_18.getAstTerm() ;boolean tomMatch260_46= false ; tom.engine.adt.code.types.BQTerm  tomMatch260_26= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_25= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_21) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch260_46= true ;tomMatch260_25=tomMatch260_21;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_21) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch260_46= true ;tomMatch260_26=tomMatch260_21;}}}if (tomMatch260_46) {           return tom_make_TopDown(tom_make_replaceVariableByExpression(tom___name,tom___exp)).visitLight(tom___body);       }}}}}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch260_50= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;boolean tomMatch260_59= false ; tom.engine.adt.code.types.BQTerm  tomMatch260_57= null ; tom.engine.adt.tomname.types.TomName  tomMatch260_55= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_58= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_50) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch260_59= true ;tomMatch260_57=tomMatch260_50;tomMatch260_55= tomMatch260_57.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_50) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch260_59= true ;tomMatch260_58=tomMatch260_50;tomMatch260_55= tomMatch260_58.getAstName() ;}}}if (tomMatch260_59) { tom.engine.adt.tomname.types.TomName  tom___name=tomMatch260_55; tom.engine.adt.tomexpression.types.Expression  tom___exp= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ;


        String varName = ""; // real name of the variable (i.e. without the tom_ prefix)
        { /* unamed block */{ /* unamed block */if ( (tom___name instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tom___name) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 varName = ASTFactory.extractRealNameFromTomVariableName( (( tom.engine.adt.tomname.types.TomName )tom___name).getString() ); }}}}


        //System.out.println("varName = " + varName);
        //System.out.println("body = " + `body);

        // count the occurence of name
        InfoVariable infoBody = new InfoVariable();
        infoBody.setAssignment(tom___exp,getPosition());
        getEnvironment().down(3); // 3 = body
        tom_make_computeOccurencesLet(tom___name,infoBody).visit(getEnvironment());
        getEnvironment().up();

        if(infoBody.modifiedAssignmentVariables) {
          /* do nothing */
          if(varName.length() > 0) {
            TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
          }
        } else {
          int mult = infoBody.readCount;
          //System.out.println(`name + " --> " + mult);
          if(mult == 0) { // name is not used
            // suppress the Let
            if(varName.length() > 0) {
              // varName is not empty when the variable is a user defined variable (with a name)
              // and not a variable generated by the compiler itself
              // TODO: check variable occurence in RawAction
              InfoVariable infoContext = new InfoVariable();
              tom_make_computeOccurencesLet(tom___name,infoContext).visit(context);
              if(infoContext.readCount<=1 && !varName.startsWith("_")) {
                // variables introduced by renaming starts with a '_'
                // verify linearity in case of variables from the pattern
                // warning to indicate that this var is unused in the rhs
                Option orgTrack = TomBase.findOriginTracking(tomMatch260_50.getOptions());
                TomMessage.warning(logger,(orgTrack instanceof OriginTracking)?orgTrack.getFileName():"unknown file", 
                  (orgTrack instanceof OriginTracking)?orgTrack.getLine():-1,
                  TomMessage.unusedVariable,varName);
                TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
              }
            }
            return  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() ;
          } else if(mult == 1) {
            //test if variables contained in the exp to assign have not been
            //modified between the last assignment and the read
            if(varName.length() > 0) {
              TomMessage.info(logger,
                  optimizer.getStreamManager().getInputFileName(), 0,
                  TomMessage.inline, mult, varName);
            }
            Position current = getPosition();
            getEnvironment().goToPosition(infoBody.usePosition);
            getEnvironment().setSubject( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(tom___exp) );
            getEnvironment().goToPosition(current);
            Instruction newlet = (Instruction) getEnvironment().getSubject();
            // return only the body
            return (Instruction) newlet.getChildAt(2);
          } else {
            /* do nothing */
            if(varName.length() > 0) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.doNothing,mult,varName);
            }
          }
        }
      }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) { tom.engine.adt.code.types.BQTerm  tomMatch260_61= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getVariable() ;boolean tomMatch260_72= false ; tom.engine.adt.code.types.BQTerm  tomMatch260_69= null ; tom.engine.adt.tomname.types.TomName  tomMatch260_66= null ; tom.engine.adt.code.types.BQTerm  tomMatch260_68= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_61) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch260_72= true ;tomMatch260_68=tomMatch260_61;tomMatch260_66= tomMatch260_68.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch260_61) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch260_72= true ;tomMatch260_69=tomMatch260_61;tomMatch260_66= tomMatch260_69.getAstName() ;}}}if (tomMatch260_72) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch260_66) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom___name=tomMatch260_66;






        /*
         * do not optimize Variable(TomNumber...) because LetRef X*=GetTail(X*) in ...
         * is not correctly handled
         * we must check that X notin exp
         */
        String varName = "";
        { /* unamed block */{ /* unamed block */if ( (tom___name instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tom___name) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 varName = ASTFactory.extractRealNameFromTomVariableName( (( tom.engine.adt.tomname.types.TomName )tom___name).getString() ); }}}}


        InfoVariable info = new InfoVariable();
        info.setAssignment( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSource() ,getPosition());
        getEnvironment().down(3);
        //computeOccurencesLetRef on the body
        tom_make_computeOccurencesLetRef(tom___name,info).visit(getEnvironment());
        getEnvironment().up();
        int mult = info.readCount;
        Position readPos = info.usePosition;

        if(info.assignment==null) {
          if(varName.length() > 0) {
            TomMessage.info(logger,null,0,TomMessage.cannotInline,0,varName);
          }
        } else {
          //System.out.println(`name + " --> " + mult);
          // 0 -> unused variable
          // suppress the letref and all the corresponding letassigns in the body
          if(mult == 0) {
            // why this test?
            if(varName.length() > 0) {
              // TODO: check variable occurence in RawAction
              info = new InfoVariable();
              tom_make_computeOccurencesLetRef(tom___name,info).visit(context);
              if(info.readCount<=1 && !varName.startsWith("_")) {
                // verify linearity in case of variables from the pattern
                // warning to indicate that this var is unused in the rhs
                Option orgTrack = TomBase.findOriginTracking(tomMatch260_61.getOptions());
                TomMessage.warning(logger,orgTrack.getFileName(), orgTrack.getLine(),
                    TomMessage.unusedVariable,varName);
                TomMessage.info(logger,null,0,TomMessage.remove,mult,varName);
              }
            }
            return tom_make_CleanAssign(tom___name).visitLight( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getAstInstruction() );
          } else if(mult == 1) {
            //test if variables contained in the exp to assign have not been
            //modified between the last assignment and the use
            //test if the last assignment is not in a conditional sub-block
            //relatively to the variable use
            Position src = info.usePosition;
            Position dest = info.assignmentPosition;

            // find the positive part of src-dest
            Position positivePart = (Position) dest.sub(src).getCanonicalPath();
            while(positivePart.length()>0 && positivePart.getHead()<0) {
              positivePart = (Position) positivePart.getTail();
            }
            // find the common ancestor of src and dest
            Position commonAncestor = (Position) dest.add(positivePart.inverse()).getCanonicalPath();
            Position current = getPosition();
            getEnvironment().goToPosition(commonAncestor);
            try {
              //this strategy fails if from common ancestor and along the path
              //positivePart there is an instruction If, WhileDo or DoWhile
              positivePart.getOmegaPath(( new tom.library.sl.Not( tom.library.sl.Choice.make( new tom.engine.adt.tominstruction.strategy.instruction.Is_If(), tom.library.sl.Choice.make( new tom.engine.adt.tominstruction.strategy.instruction.Is_DoWhile(), tom.library.sl.Choice.make( new tom.engine.adt.tominstruction.strategy.instruction.Is_WhileDo(), null ) ) ) ) )).visit(getEnvironment());
              if(varName.length() > 0) {
                TomMessage.info(logger,
                    optimizer.getStreamManager().getInputFileName(), 0,
                    TomMessage.inline, mult, varName);
              }
              getEnvironment().goToPosition(readPos);
              BQTerm value =  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(info.assignment) ;
              getEnvironment().setSubject(value);
              getEnvironment().goToPosition(current);
              tom_make_CleanAssign(tom___name).visit(getEnvironment());
              Instruction newletref = (Instruction) getEnvironment().getSubject();
              // return only the body
              //System.out.println("inlineletref");
              return (Instruction) newletref.getChildAt(2);
            } catch(VisitFailure e) {
              getEnvironment().goToPosition(current);
              if(varName.length() > 0) {
                TomMessage.info(logger,
                    optimizer.getStreamManager().getInputFileName(), 0,
                    TomMessage.noInline, mult, varName);
              }
            }
          } else {
            /* do nothing: traversal() */
            if(varName.length() > 0) {
              TomMessage.info(logger,
                  optimizer.getStreamManager().getInputFileName(), 0,
                  TomMessage.doNothing, mult, varName);
            }
          }
        }
      }}}}}}return _visit_Instruction(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) { tom.engine.adt.code.types.BQTerm  tomMatch263_2= (( tom.engine.adt.code.types.BQTerm )tom__arg).getHeadTerm() ; tom.engine.adt.tomname.types.TomName  tom___name= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch263_2) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch263_6= tomMatch263_2.getExp() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch263_6) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {if ( (tom___name== tomMatch263_6.getAstName() ) ) {         return  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSliceList.make(tom___name,  tomMatch263_6.getVariableBeginAST() ,  tomMatch263_6.getVariableEndAST() ,  tom.engine.adt.code.types.bqterm.BuildAppendList.make(tom___name,  tomMatch263_6.getTail() ,  (( tom.engine.adt.code.types.BQTerm )tom__arg).getTailTerm() ) ) ) ;       }}}}}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_Inline( tom.engine.adt.tomconstraint.types.Constraint  t0,  OptimizerPlugin  t1) { return new Inline(t0,t1);}






  private static class InfoVariable {

    private HashSet<TomName> assignmentVariables = new HashSet<TomName>();
    public int readCount=0;
    public boolean modifiedAssignmentVariables=false; // true when exp depends on a variable which is modified by Assign
    public Position usePosition; // position where is used the variable
    /*
     * for LetRef
     */
    private Position assignmentPosition;
    private Expression assignment;

    public InfoVariable() {}

    public void setAssignment(Expression newAssignment,Position newAssignmentPosition) {
      assignment = newAssignment;
      assignmentPosition = newAssignmentPosition;
      assignmentVariables.clear();
      try {
        tom_make_TopDownCollect(tom_make_CollectVariable(assignmentVariables)).visitLight(newAssignment);
      } catch(VisitFailure e) {
        throw new TomRuntimeException("Error during collecting variables in "+newAssignment);
      }
    }

    public Set getAssignmentVariables() {
      return assignmentVariables;
    }

    public void clear() {
      assignment = null;
      assignmentPosition = null;
      assignmentVariables.clear();
    }

  }

  public static class CollectVariable extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashSet<TomName>  set;public CollectVariable( java.util.HashSet<TomName>  set) {super(( new tom.library.sl.Identity() ));this.set=set;}public  java.util.HashSet<TomName>  getset() {return set;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch264_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch264_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch264_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch264_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch264_5= true ;tomMatch264_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch264_1= tomMatch264_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch264_5= true ;tomMatch264_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch264_1= tomMatch264_4.getAstName() ;}}}if (tomMatch264_5) {


        set.add(tomMatch264_1);
        //stop to visit this branch (like "return false" with traversal)
        throw new VisitFailure();
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVariable( java.util.HashSet<TomName>  t0) { return new CollectVariable(t0);}



  /* strategies for Let inlining (using cps) */
  // comp = AssignCase(RawActionCase(comp,BaseCase(all(comp),fail())),fail())
  private static  tom.library.sl.Strategy  tom_make_computeOccurencesLet( tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) { return tom_make_Try(( new tom.library.sl.Mu(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurenceLet_AssignCase(tom_make_computeOccurenceLet_RawActionCase(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurenceLet_BaseCase(( new tom.library.sl.All(( new tom.library.sl.MuVar("comp") )) ),variableName,info),variableName,info),info)
            ) )
          )













    ;}


  /*
   * check if a variable appearing in an expression (in Let x <- exp ...)
   * is modified (by applying Assign on the variables contained in exp)
   */
  public static class computeOccurenceLet_AssignCase extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  defaultCase;private  InfoVariable  info;public computeOccurenceLet_AssignCase( tom.library.sl.Strategy  defaultCase,  InfoVariable  info) {super(defaultCase);this.defaultCase=defaultCase;this.info=info;}public  tom.library.sl.Strategy  getdefaultCase() {return defaultCase;}public  InfoVariable  getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getdefaultCase();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);defaultCase = ( tom.library.sl.Strategy ) children[1];return this;}public int getChildCount() {return 2;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getdefaultCase();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch265_8= false ; tom.engine.adt.code.types.BQTerm  tomMatch265_1= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch265_3= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch265_4= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {{ /* unamed block */tomMatch265_8= true ;tomMatch265_3=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch265_1= tomMatch265_3.getVariable() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {{ /* unamed block */tomMatch265_8= true ;tomMatch265_4=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch265_1= tomMatch265_4.getVariable() ;}}}if (tomMatch265_8) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch265_1) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {


        if(info.getAssignmentVariables().contains( tomMatch265_1.getAstName() )) {
          info.modifiedAssignmentVariables=true;
          throw new VisitFailure();
        }
      }}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_AssignCase( tom.library.sl.Strategy  t0,  InfoVariable  t1) { return new computeOccurenceLet_AssignCase(t0,t1);}public static class computeOccurenceLet_RawActionCase extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  goOnCase;private  tom.library.sl.Strategy  cutCase;private  tom.engine.adt.tomname.types.TomName  variableName;private  InfoVariable  info;public computeOccurenceLet_RawActionCase( tom.library.sl.Strategy  goOnCase,  tom.library.sl.Strategy  cutCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {super(cutCase);this.goOnCase=goOnCase;this.cutCase=cutCase;this.variableName=variableName;this.info=info;}public  tom.library.sl.Strategy  getgoOnCase() {return goOnCase;}public  tom.library.sl.Strategy  getcutCase() {return cutCase;}public  tom.engine.adt.tomname.types.TomName  getvariableName() {return variableName;}public  InfoVariable  getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getgoOnCase();stratChildren[2] = getcutCase();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);goOnCase = ( tom.library.sl.Strategy ) children[1];cutCase = ( tom.library.sl.Strategy ) children[2];return this;}public int getChildCount() {return 3;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getgoOnCase();case 2: return getcutCase();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: goOnCase = ( tom.library.sl.Strategy )child; return this;case 2: cutCase = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {







        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        try {
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch266_11= false ; tom.engine.adt.tominstruction.types.Instruction  tomMatch266_7= null ; tom.engine.adt.code.types.BQTerm  tomMatch266_4= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch266_6= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {{ /* unamed block */tomMatch266_11= true ;tomMatch266_6=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch266_4= tomMatch266_6.getVariable() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {{ /* unamed block */tomMatch266_11= true ;tomMatch266_7=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch266_4= tomMatch266_7.getVariable() ;}}}if (tomMatch266_11) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch266_4) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {



        if(variableName.equals( tomMatch266_4.getAstName() )) {
          throw new TomRuntimeException("OptimizerPlugin: Assignment cannot be done for the variable "+variableName+" declared in a let");
        }
      }}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_RawActionCase( tom.library.sl.Strategy  t0,  tom.library.sl.Strategy  t1,  tom.engine.adt.tomname.types.TomName  t2,  InfoVariable  t3) { return new computeOccurenceLet_RawActionCase(t0,t1,t2,t3);}



  /*
   * when variableName is encountered, its position is stored
   * if it appears more than once, the computation is stopped because there is no possible inlining
   */
  public static class computeOccurenceLet_BaseCase extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  defaultCase;private  tom.engine.adt.tomname.types.TomName  variableName;private  InfoVariable  info;public computeOccurenceLet_BaseCase( tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {super(defaultCase);this.defaultCase=defaultCase;this.variableName=variableName;this.info=info;}public  tom.library.sl.Strategy  getdefaultCase() {return defaultCase;}public  tom.engine.adt.tomname.types.TomName  getvariableName() {return variableName;}public  InfoVariable  getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getdefaultCase();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);defaultCase = ( tom.library.sl.Strategy ) children[1];return this;}public int getChildCount() {return 2;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getdefaultCase();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch267_5= false ; tom.engine.adt.tomname.types.TomName  tomMatch267_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch267_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch267_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch267_5= true ;tomMatch267_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch267_1= tomMatch267_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch267_5= true ;tomMatch267_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch267_1= tomMatch267_4.getAstName() ;}}}if (tomMatch267_5) {



        if(variableName == tomMatch267_1) {
          info.usePosition = getPosition();
          info.readCount++;
          if(info.readCount==2) { throw new VisitFailure(); }
        }
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_computeOccurenceLet_BaseCase( tom.library.sl.Strategy  t0,  tom.engine.adt.tomname.types.TomName  t1,  InfoVariable  t2) { return new computeOccurenceLet_BaseCase(t0,t1,t2);}public static class Print extends tom.library.sl.AbstractStrategyBasic {private  String  name;public Print( String  name) {super(( new tom.library.sl.Identity() ));this.name=name;}public  String  getname() {return name;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {return ((T)visit_InstructionList((( tom.engine.adt.tominstruction.types.InstructionList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.InstructionList  _visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.InstructionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.InstructionList  visit_InstructionList( tom.engine.adt.tominstruction.types.InstructionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.InstructionList) ) {











        System.out.println(name + " instlist: " + (( tom.engine.adt.tominstruction.types.InstructionList )tom__arg));
      }}}return _visit_InstructionList(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {         System.out.println(name + " inst: " + (( tom.engine.adt.tominstruction.types.Instruction )tom__arg));       }}}return _visit_Instruction(tom__arg,introspector);}}



  /* strategies for LetRef inlining (using cps) */
  // comp = special( comp, Basecase(comp,fail()) )
  private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef( tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) { return 
 (
        tom_make_Try(( new tom.library.sl.Mu(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurencesLetRef_CutCase(( new tom.library.sl.MuVar("comp") ),tom_make_computeOccurencesLetRef_BaseCase(( new tom.library.sl.All(( new tom.library.sl.MuVar("comp") )) ),variableName,info),variableName,info)
            ) ))










        ) ;}


  //cases where failure is used to cut branches
  public static class computeOccurencesLetRef_CutCase extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  goOnCase;private  tom.library.sl.Strategy  defaultCase;private  tom.engine.adt.tomname.types.TomName  variableName;private  InfoVariable  info;public computeOccurencesLetRef_CutCase( tom.library.sl.Strategy  goOnCase,  tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {super(defaultCase);this.goOnCase=goOnCase;this.defaultCase=defaultCase;this.variableName=variableName;this.info=info;}public  tom.library.sl.Strategy  getgoOnCase() {return goOnCase;}public  tom.library.sl.Strategy  getdefaultCase() {return defaultCase;}public  tom.engine.adt.tomname.types.TomName  getvariableName() {return variableName;}public  InfoVariable  getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getgoOnCase();stratChildren[2] = getdefaultCase();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);goOnCase = ( tom.library.sl.Strategy ) children[1];defaultCase = ( tom.library.sl.Strategy ) children[2];return this;}public int getChildCount() {return 3;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getgoOnCase();case 2: return getdefaultCase();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: goOnCase = ( tom.library.sl.Strategy )child; return this;case 2: defaultCase = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {


        /* recursive call of the current strategy on the first child */
        Environment current = getEnvironment();
        current.down(1);
        try {
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch270_12= false ; tom.engine.adt.code.types.BQTerm  tomMatch270_4= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch270_5= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch270_8= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch270_7= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {{ /* unamed block */tomMatch270_12= true ;tomMatch270_7=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch270_4= tomMatch270_7.getVariable() ;tomMatch270_5= tomMatch270_7.getSource() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {{ /* unamed block */tomMatch270_12= true ;tomMatch270_8=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch270_4= tomMatch270_8.getVariable() ;tomMatch270_5= tomMatch270_8.getSource() ;}}}if (tomMatch270_12) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch270_4) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tom___name= tomMatch270_4.getAstName() ;


        if(variableName == tom___name) {
          info.setAssignment(tomMatch270_5,getPosition());
        } else {
          if(info.getAssignmentVariables().contains(tom___name)) {
            info.clear();
         }
        }
        /* recursive call of the current strategy on src */
        Environment current = getEnvironment();
        try {
          current.down(2);
          goOnCase.visit(current);
          current.up();
          return (Instruction) current.getSubject();
        } catch (VisitFailure e) {
          current.upLocal();
          throw new VisitFailure();
        }
      }}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef_CutCase( tom.library.sl.Strategy  t0,  tom.library.sl.Strategy  t1,  tom.engine.adt.tomname.types.TomName  t2,  InfoVariable  t3) { return new computeOccurencesLetRef_CutCase(t0,t1,t2,t3);}public static class computeOccurencesLetRef_BaseCase extends tom.library.sl.AbstractStrategyBasic {private  tom.library.sl.Strategy  defaultCase;private  tom.engine.adt.tomname.types.TomName  variableName;private  InfoVariable  info;public computeOccurencesLetRef_BaseCase( tom.library.sl.Strategy  defaultCase,  tom.engine.adt.tomname.types.TomName  variableName,  InfoVariable  info) {super(defaultCase);this.defaultCase=defaultCase;this.variableName=variableName;this.info=info;}public  tom.library.sl.Strategy  getdefaultCase() {return defaultCase;}public  tom.engine.adt.tomname.types.TomName  getvariableName() {return variableName;}public  InfoVariable  getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);stratChildren[1] = getdefaultCase();return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);defaultCase = ( tom.library.sl.Strategy ) children[1];return this;}public int getChildCount() {return 2;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);case 1: return getdefaultCase();default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);case 1: defaultCase = ( tom.library.sl.Strategy )child; return this;default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch271_5= false ; tom.engine.adt.tomname.types.TomName  tomMatch271_1= null ; tom.engine.adt.code.types.BQTerm  tomMatch271_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch271_3= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch271_5= true ;tomMatch271_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch271_1= tomMatch271_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch271_5= true ;tomMatch271_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch271_1= tomMatch271_4.getAstName() ;}}}if (tomMatch271_5) {







        if(variableName == tomMatch271_1) {
          info.readCount++;
          info.usePosition = getPosition();
          if(info.readCount==2) {
            throw new VisitFailure();
          }
        }
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_computeOccurencesLetRef_BaseCase( tom.library.sl.Strategy  t0,  tom.engine.adt.tomname.types.TomName  t1,  InfoVariable  t2) { return new computeOccurencesLetRef_BaseCase(t0,t1,t2);}



  /*
   * rename variable1 into variable2
   */
  private static  tom.library.sl.Strategy  tom_make_renameVariable( tom.engine.adt.tomname.types.TomName  variable1,  tom.engine.adt.tomname.types.TomName  variable2) { return 
 (tom_make_TopDown(tom_make_renameVariableOnce(variable1,variable2)) ) ;}public static class renameVariableOnce extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomname.types.TomName  variable1;private  tom.engine.adt.tomname.types.TomName  variable2;public renameVariableOnce( tom.engine.adt.tomname.types.TomName  variable1,  tom.engine.adt.tomname.types.TomName  variable2) {super(( new tom.library.sl.Identity() ));this.variable1=variable1;this.variable2=variable2;}public  tom.engine.adt.tomname.types.TomName  getvariable1() {return variable1;}public  tom.engine.adt.tomname.types.TomName  getvariable2() {return variable2;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch272_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch272_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch272_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch272_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch272_5= true ;tomMatch272_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch272_1= tomMatch272_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch272_5= true ;tomMatch272_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch272_1= tomMatch272_4.getAstName() ;}}}if (tomMatch272_5) {





        if(variable1 == tomMatch272_1) {
          return (( tom.engine.adt.code.types.BQTerm )tom__arg).setAstName(variable2);
        }
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_renameVariableOnce( tom.engine.adt.tomname.types.TomName  t0,  tom.engine.adt.tomname.types.TomName  t1) { return new renameVariableOnce(t0,t1);}public static class replaceVariableByExpression extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomname.types.TomName  variable;private  tom.engine.adt.tomexpression.types.Expression  exp;public replaceVariableByExpression( tom.engine.adt.tomname.types.TomName  variable,  tom.engine.adt.tomexpression.types.Expression  exp) {super(( new tom.library.sl.Identity() ));this.variable=variable;this.exp=exp;}public  tom.engine.adt.tomname.types.TomName  getvariable() {return variable;}public  tom.engine.adt.tomexpression.types.Expression  getexp() {return exp;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch273_5= false ; tom.engine.adt.code.types.BQTerm  tomMatch273_4= null ; tom.engine.adt.code.types.BQTerm  tomMatch273_3= null ; tom.engine.adt.tomname.types.TomName  tomMatch273_1= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch273_5= true ;tomMatch273_3=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch273_1= tomMatch273_3.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch273_5= true ;tomMatch273_4=(( tom.engine.adt.code.types.BQTerm )tom__arg);tomMatch273_1= tomMatch273_4.getAstName() ;}}}if (tomMatch273_5) {






        if(variable == tomMatch273_1) {
          return  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make(exp) ;
        }
      }}}}return _visit_BQTerm(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceVariableByExpression( tom.engine.adt.tomname.types.TomName  t0,  tom.engine.adt.tomexpression.types.Expression  t1) { return new replaceVariableByExpression(t0,t1);}private static  tom.library.sl.Strategy  tom_make_CleanAssign( tom.engine.adt.tomname.types.TomName  varname) { return 




 (tom_make_TopDown(tom_make_CleanAssignOnce(varname))) ;}public static class CleanAssignOnce extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.tomname.types.TomName  varname;public CleanAssignOnce( tom.engine.adt.tomname.types.TomName  varname) {super(( new tom.library.sl.Identity() ));this.varname=varname;}public  tom.engine.adt.tomname.types.TomName  getvarname() {return varname;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch274_10= false ; tom.engine.adt.tominstruction.types.Instruction  tomMatch274_4= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch274_3= null ; tom.engine.adt.code.types.BQTerm  tomMatch274_1= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {{ /* unamed block */tomMatch274_10= true ;tomMatch274_3=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch274_1= tomMatch274_3.getVariable() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {{ /* unamed block */tomMatch274_10= true ;tomMatch274_4=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch274_1= tomMatch274_4.getVariable() ;}}}if (tomMatch274_10) {boolean tomMatch274_9= false ; tom.engine.adt.tomname.types.TomName  tomMatch274_5= null ; tom.engine.adt.code.types.BQTerm  tomMatch274_8= null ; tom.engine.adt.code.types.BQTerm  tomMatch274_7= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch274_1) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch274_9= true ;tomMatch274_7=tomMatch274_1;tomMatch274_5= tomMatch274_7.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch274_1) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch274_9= true ;tomMatch274_8=tomMatch274_1;tomMatch274_5= tomMatch274_8.getAstName() ;}}}if (tomMatch274_9) {





        if(tomMatch274_5.equals(varname)) { return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ; }
      }}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CleanAssignOnce( tom.engine.adt.tomname.types.TomName  t0) { return new CleanAssignOnce(t0);}



  private static boolean compare(tom.library.sl.Visitable term1, tom.library.sl.Visitable term2) {
    return factory.remove(term1)==factory.remove(term2);
  }

  public static class CastElim extends tom.library.sl.AbstractStrategyBasic {public CastElim() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch275_2= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getSource() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch275_2) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getAstType() == tomMatch275_2.getAstType() ) ) {


        return tomMatch275_2;
      }}}}}}return _visit_Expression(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CastElim() { return new CastElim();}public static class NopElimAndFlatten extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public NopElimAndFlatten( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_end_7=tomMatch276_1;do {{ /* unamed block */if (!( tomMatch276_end_7.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch276_11= tomMatch276_end_7.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch276_11) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {







        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"flatten");
        return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch276_1,tomMatch276_end_7, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch276_11.getInstList() ,tom_append_list_concInstruction( tomMatch276_end_7.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )))) ;
      }}if ( tomMatch276_end_7.isEmptyconcInstruction() ) {tomMatch276_end_7=tomMatch276_1;} else {tomMatch276_end_7= tomMatch276_end_7.getTailconcInstruction() ;}}} while(!( (tomMatch276_end_7==tomMatch276_1) ));}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_14= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_14) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_14) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_end_20=tomMatch276_14;do {{ /* unamed block */if (!( tomMatch276_end_20.isEmptyconcInstruction() )) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch276_end_20.getHeadconcInstruction() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"nop-elim");
        return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch276_14,tomMatch276_end_20, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch276_end_20.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ))) ;
      }}if ( tomMatch276_end_20.isEmptyconcInstruction() ) {tomMatch276_end_20=tomMatch276_14;} else {tomMatch276_end_20= tomMatch276_end_20.getTailconcInstruction() ;}}} while(!( (tomMatch276_end_20==tomMatch276_14) ));}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_26= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_26) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_26) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {if ( tomMatch276_26.isEmptyconcInstruction() ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"abstractblock-elim1");
        return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch276_31= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_31) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch276_31) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {if (!( tomMatch276_31.isEmptyconcInstruction() )) {if (  tomMatch276_31.getTailconcInstruction() .isEmptyconcInstruction() ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"abstractblock-elim2");
        return  tomMatch276_31.getHeadconcInstruction() ;
      }}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSuccesInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"ifnopnop-elim");
        return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
      }}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getCondition() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"iftrue-elim");
        return  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getSuccesInst() ;
      }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getCondition() ) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {


        TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"iffalse-elim");
        return  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getFailureInst() ;
      }}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_NopElimAndFlatten( OptimizerPlugin  t0) { return new NopElimAndFlatten(t0);}












  /*
   * two expressions are incompatible when they cannot be true a the same time
   */
  private boolean incompatible(Expression c1, Expression c2) {
    try {
      Expression res = tom_make_InnermostId(tom_make_NormExpr(this)).visitLight( tom.engine.adt.tomexpression.types.expression.And.make(c2, c2) );
      return res == tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
    } catch(VisitFailure e) {
      return false;
    }
  }

  public static class IfSwapping extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public IfSwapping( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch277_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch277_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch277_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch277_end_7=tomMatch277_1;do {{ /* unamed block */if (!( tomMatch277_end_7.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch277_14= tomMatch277_end_7.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch277_14) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tomexpression.types.Expression  tom___cond1= tomMatch277_14.getCondition() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch277_14.getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch277_8= tomMatch277_end_7.getTailconcInstruction() ;if (!( tomMatch277_8.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch277_21= tomMatch277_8.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch277_21) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tomexpression.types.Expression  tom___cond2= tomMatch277_21.getCondition() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch277_21.getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        String s1 = factory.prettyPrint(factory.remove(tom___cond1));
        String s2 = factory.prettyPrint(factory.remove(tom___cond2));
        if(s1.compareTo(s2) < 0) {
          /* swap two incompatible conditions */
          if(optimizer.incompatible(tom___cond1,tom___cond2)) {
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-swapping");
            return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch277_1,tomMatch277_end_7, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ), tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch277_8.getHeadconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch277_end_7.getHeadconcInstruction() ,tom_append_list_concInstruction( tomMatch277_8.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) ) )) ;
          }
        }
      }}}}}}if ( tomMatch277_end_7.isEmptyconcInstruction() ) {tomMatch277_end_7=tomMatch277_1;} else {tomMatch277_end_7= tomMatch277_end_7.getTailconcInstruction() ;}}} while(!( (tomMatch277_end_7==tomMatch277_1) ));}}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_IfSwapping( OptimizerPlugin  t0) { return new IfSwapping(t0);}public static class BlockFusion extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public BlockFusion( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch278_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch278_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch278_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch278_end_7=tomMatch278_1;do {{ /* unamed block */ tom.engine.adt.tominstruction.types.InstructionList  tom___X1=tom_get_slice_concInstruction(tomMatch278_1,tomMatch278_end_7, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() );if (!( tomMatch278_end_7.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch278_14= tomMatch278_end_7.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch278_14) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch278_11= tomMatch278_14.getVariable() ;boolean tomMatch278_30= false ; tom.engine.adt.code.types.BQTerm  tomMatch278_18= null ; tom.engine.adt.code.types.BQTerm  tomMatch278_19= null ; tom.engine.adt.tomname.types.TomName  tomMatch278_16= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch278_11) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch278_30= true ;tomMatch278_18=tomMatch278_11;tomMatch278_16= tomMatch278_18.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch278_11) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch278_30= true ;tomMatch278_19=tomMatch278_11;tomMatch278_16= tomMatch278_19.getAstName() ;}}}if (tomMatch278_30) { tom.engine.adt.tomname.types.TomName  tom___name1=tomMatch278_16; tom.engine.adt.code.types.BQTerm  tom___var1=tomMatch278_11; tom.engine.adt.tomexpression.types.Expression  tom___term1= tomMatch278_14.getSource() ; tom.engine.adt.tominstruction.types.Instruction  tom___body1= tomMatch278_14.getAstInstruction() ; tom.engine.adt.tominstruction.types.InstructionList  tomMatch278_8= tomMatch278_end_7.getTailconcInstruction() ;if (!( tomMatch278_8.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch278_23= tomMatch278_8.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch278_23) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch278_20= tomMatch278_23.getVariable() ;boolean tomMatch278_29= false ; tom.engine.adt.code.types.BQTerm  tomMatch278_28= null ; tom.engine.adt.tomname.types.TomName  tomMatch278_25= null ; tom.engine.adt.code.types.BQTerm  tomMatch278_27= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch278_20) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch278_29= true ;tomMatch278_27=tomMatch278_20;tomMatch278_25= tomMatch278_27.getAstName() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch278_20) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch278_29= true ;tomMatch278_28=tomMatch278_20;tomMatch278_25= tomMatch278_28.getAstName() ;}}}if (tomMatch278_29) { tom.engine.adt.tominstruction.types.Instruction  tom___body2= tomMatch278_23.getAstInstruction() ; tom.engine.adt.tominstruction.types.InstructionList  tom___X2= tomMatch278_8.getTailconcInstruction() ; tom.engine.adt.tominstruction.types.Instruction  tom___block=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);









        /* Fusion de 2 blocs Let contigus instanciant deux variables egales */
        if(compare(tom___term1, tomMatch278_23.getSource() )) {
          if(compare(tom___var1,tomMatch278_20)) {
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"block-fusion1");
            return (tom___block.setInstList(tom_append_list_concInstruction(tom___X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Let.make(tom___var1, tom___term1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___body1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___body2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom___X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
          } else {
            InfoVariable info = new InfoVariable();
            tom_make_computeOccurencesLet(tom___name1,info).visit(tom___body2);
            int mult = info.readCount;
            if(mult==0) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"block-fusion2");
              Instruction newBody2 =  tom_make_renameVariable(tomMatch278_25,tom___name1).visitLight(tom___body2);
              return (tom___block.setInstList(tom_append_list_concInstruction(tom___X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.Let.make(tom___var1, tom___term1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___body1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(newBody2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom___X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
            }
          }
        }
      }}}}}}if ( tomMatch278_end_7.isEmptyconcInstruction() ) {tomMatch278_end_7=tomMatch278_1;} else {tomMatch278_end_7= tomMatch278_end_7.getTailconcInstruction() ;}}} while(!( (tomMatch278_end_7==tomMatch278_1) ));}}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_BlockFusion( OptimizerPlugin  t0) { return new BlockFusion(t0);}public static class IfFusion extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public IfFusion( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch279_22= false ; tom.engine.adt.tominstruction.types.InstructionList  tomMatch279_1= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch279_4= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch279_3= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {{ /* unamed block */tomMatch279_22= true ;tomMatch279_3=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch279_1= tomMatch279_3.getInstList() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {{ /* unamed block */tomMatch279_22= true ;tomMatch279_4=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);tomMatch279_1= tomMatch279_4.getInstList() ;}}}if (tomMatch279_22) {if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch279_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch279_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch279_end_8=tomMatch279_1;do {{ /* unamed block */ tom.engine.adt.tominstruction.types.InstructionList  tom___X1=tom_get_slice_concInstruction(tomMatch279_1,tomMatch279_end_8, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() );if (!( tomMatch279_end_8.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch279_15= tomMatch279_end_8.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch279_15) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tomexpression.types.Expression  tom___cond1= tomMatch279_15.getCondition() ; tom.engine.adt.tominstruction.types.Instruction  tom___success1= tomMatch279_15.getSuccesInst() ; tom.engine.adt.tominstruction.types.Instruction  tom___failure1= tomMatch279_15.getFailureInst() ; tom.engine.adt.tominstruction.types.InstructionList  tomMatch279_9= tomMatch279_end_8.getTailconcInstruction() ;if (!( tomMatch279_9.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch279_20= tomMatch279_9.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch279_20) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tominstruction.types.Instruction  tom___success2= tomMatch279_20.getSuccesInst() ; tom.engine.adt.tominstruction.types.Instruction  tom___failure2= tomMatch279_20.getFailureInst() ; tom.engine.adt.tominstruction.types.InstructionList  tom___X2= tomMatch279_9.getTailconcInstruction() ; tom.engine.adt.tominstruction.types.Instruction  tom___block=(( tom.engine.adt.tominstruction.types.Instruction )tom__arg);









        Expression c1 = factory.remove(tom___cond1);
        Expression c2 = factory.remove( tomMatch279_20.getCondition() );
        //System.out.println("c1 = " + c1);
        //System.out.println("c2 = " + c2);
        { /* unamed block */{ /* unamed block */if ( (c1 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (c2 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )c1)==(( tom.engine.adt.tomexpression.types.Expression )c2)) ) {

            /* Merge 2 blocks whose conditions are equals */
            if(tom___failure1.isNop() && tom___failure2.isNop()) {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion1");
              Instruction res = (tom___block.setInstList(tom_append_list_concInstruction(tom___X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom___cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ,tom_append_list_concInstruction(tom___X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
              //System.out.println(res);

              return res;
            } else {
              TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion2");
              return (tom___block.setInstList(tom_append_list_concInstruction(tom___X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom___cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___failure1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___failure2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom___X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
            }
          }}}}{ /* unamed block */if ( (c1 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )c1) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {if ( (c2 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ( (( tom.engine.adt.tomexpression.types.Expression )c1).getArg() ==(( tom.engine.adt.tomexpression.types.Expression )c2)) ) {


            /* Merge 2 blocks whose conditions are the negation of the other */
            TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"if-fusion-not");
            return (tom___block.setInstList(tom_append_list_concInstruction(tom___X1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom___cond1,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___failure2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___failure1, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(tom___success2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction(tom___X2, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )));
          }}}}}}

      }}}}if ( tomMatch279_end_8.isEmptyconcInstruction() ) {tomMatch279_end_8=tomMatch279_1;} else {tomMatch279_end_8= tomMatch279_end_8.getTailconcInstruction() ;}}} while(!( (tomMatch279_end_8==tomMatch279_1) ));}}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_IfFusion( OptimizerPlugin  t0) { return new IfFusion(t0);}public static class InterBlock extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public InterBlock( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch281_1= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch281_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch281_1) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch281_end_7=tomMatch281_1;do {{ /* unamed block */if (!( tomMatch281_end_7.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch281_14= tomMatch281_end_7.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch281_14) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tomexpression.types.Expression  tom___cond1= tomMatch281_14.getCondition() ; tom.engine.adt.tominstruction.types.InstructionList  tomMatch281_8= tomMatch281_end_7.getTailconcInstruction() ;if (!( tomMatch281_8.isEmptyconcInstruction() )) { tom.engine.adt.tominstruction.types.Instruction  tomMatch281_19= tomMatch281_8.getHeadconcInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch281_19) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tomexpression.types.Expression  tom___cond2= tomMatch281_19.getCondition() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch281_19.getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {










        if(optimizer.incompatible(tom___cond1,tom___cond2)) {
          TomMessage.info(logger,optimizer.getStreamManager().getInputFileName(),0,TomMessage.tomOptimizationType,"inter-block");
          return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch281_1,tomMatch281_end_7, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ), tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom___cond1,  tomMatch281_14.getSuccesInst() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tomMatch281_14.getFailureInst() , tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make(tom___cond2,  tomMatch281_19.getSuccesInst() ,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ,tom_append_list_concInstruction( tomMatch281_8.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() )) )) ;
        }
      }}}}}if ( tomMatch281_end_7.isEmptyconcInstruction() ) {tomMatch281_end_7=tomMatch281_1;} else {tomMatch281_end_7= tomMatch281_end_7.getTailconcInstruction() ;}}} while(!( (tomMatch281_end_7==tomMatch281_1) ));}}}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_InterBlock( OptimizerPlugin  t0) { return new InterBlock(t0);}public static class NormExpr extends tom.library.sl.AbstractStrategyBasic {private  OptimizerPlugin  optimizer;public NormExpr( OptimizerPlugin  optimizer) {super(( new tom.library.sl.Identity() ));this.optimizer=optimizer;}public  OptimizerPlugin  getoptimizer() {return optimizer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {





 return  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
 return  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
 return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
 return  (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
 return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ; }}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {


        //System.out.println("kid1 = " + `kid1);
        //System.out.println("kid2 = " + `kid2);
        if(compare( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getKid2() )) {
          return  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ;
        } else {
          return (( tom.engine.adt.tomexpression.types.Expression )tom__arg);
        }
      }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.And) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch282_63= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ; tom.engine.adt.tomexpression.types.Expression  tomMatch282_64= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch282_63) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tom___name1= tomMatch282_63.getAstName() ; tom.engine.adt.code.types.BQTerm  tom___term= tomMatch282_63.getVariable() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch282_64) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {if ( (tom___term== tomMatch282_64.getVariable() ) ) {


        if(tom___name1== tomMatch282_64.getAstName() ) {
          return  tom.engine.adt.tomexpression.types.expression.IsFsym.make(tom___name1, tom___term) ;
        }
        /*
         * may be true for list operator with domain=codomain
         * two if_sym(f)==is_fsym(g) may be true due to mapping
         */
        TomSymbol tomSymbol = optimizer.getSymbolTable().getSymbolFromName(tom___name1.getString());
        if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
          TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
          TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
          if(domain!=codomain) {
            return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
          }
        } else {
          return  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ;
        }
        return (( tom.engine.adt.tomexpression.types.Expression )tom__arg);
      }}}}}}}return _visit_Expression(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_NormExpr( OptimizerPlugin  t0) { return new NormExpr(t0);}



}
