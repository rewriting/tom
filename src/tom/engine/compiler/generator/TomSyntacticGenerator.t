package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;

import tom.engine.compiler.*;
/**
 * Syntactic Generator
 */
public class TomSyntacticGenerator implements TomIBaseGenerator{
	
	%include { adt/tomsignature/TomSignature.tom }
	%include { sl.tom }	
	
	public Expression generate(Expression expression){		
		return  (Expression)((Strategy)`TopDown(SyntacticGenerator())).fire(expression);
	}

	// If we find ConstraintToExpression it means that this constraint was not processed	
	%strategy SyntacticGenerator() extends Identity(){
		visit Expression{
			// generate is_fsym(t,f) || is_fsym(t,g) || ...
			ConstraintToExpression(MatchConstraint(RecordAppl[Option=option,NameList=nameList@(headName,_*),Slots=l],SymbolOf(subject))) ->{
				Expression cond = null;
				TomType termType = getTermTypeFromName(`headName);
				// add condition for each name
				%match(nameList){
					concTomName(_*,name,_*) ->{
						Expression check = `EqualFunctionSymbol(termType,subject,RecordAppl(option,concTomName(name),l,concConstraint()));
				        cond = (cond == null ? check : `Or(check,cond));	
					}
				}		        
		        return cond;
			}
			// generate equality
			ConstraintToExpression(MatchConstraint(t,u@!SymbolOf(_))) ->{
				return `EqualTerm(getTermTypeFromTerm(t),t,u);		        		      
			}			
		} // end visit
	} // end strategy
	
	private static TomType getTermTypeFromName(TomName tomName){
		String stringName = ((Name)tomName).getString();
        TomSymbol tomSymbol = TomInstructionGenerationManager.getSymbolTable().getSymbolFromName(stringName);
        return tomSymbol.getTypesToType().getCodomain();
	}
	
	private static TomType getTermTypeFromTerm(TomTerm term){
		%match(term){
			RecordAppl[NameList=nameList@(headName,_*)] ->{
				return getTermTypeFromName(`headName);
			}
		}
        throw new TomRuntimeException("getTermTypeFromTerm: cannot find the type for: " + term);
	}
}