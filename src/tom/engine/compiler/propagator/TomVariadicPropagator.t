package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;

/**
 * Syntactic propagator
 */
public class TomVariadicPropagator implements TomIBasePropagator{

// --------------------------------------------------------	
	%include { adt/tomsignature/TomSignature.tom }	
	%include { mustrategy.tom}
// --------------------------------------------------------
	
	private static short freshVarCounter = 0;
	private static short beginEndCounter = 0;
	
	public Constraint propagate(Constraint constraint){
		return (Constraint)`InnermostId(VariadicPatternMatching()).apply(constraint);		
	}	

	%strategy VariadicPatternMatching() extends `Identity(){		
		visit Constraint{
//[pem] I need some explanations to deeply understand the rules
			// Decompose - only if 'g' != SymbolOf 
			// conc(t1,X*,t2,Y*) = g -> conc=SymbolOf(g) /\ fresh_var = g 
			// /\ NotEmpty(fresh_Var)  /\ t1=GetHead(fresh_var) /\ fresh_var1 = GetTail(fresh_var) 
			// /\ begin1 = fresh_var1  /\ end1 = fresh_var1 /\	X* = VariableHeadList(begin1,end1) /\ fresh_var2 = end1
			// /\ NotEmpty(fresh_Var2) /\ t2=GetHead(fresh_var2)/\ fresh_var3 = GetTail(fresh_var2)  
			// /\ begin2 = fresh_var3  /\ end2 = fresh_var3 /\	Y* = VariableHeadList(begin2,end2) /\ fresh_var4 = end2
			m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots
					,constraints),g@!SymbolOf[]) -> {
				// if this is not a list, nothing to do
				if(!TomConstraintCompiler.isListOperator(TomConstraintCompiler.getSymbolTable().
						getSymbolFromName(`tomName))) {return `m;}				
				// declare fresh variable
				TomType listType = TomConstraintCompiler.getTermTypeFromTerm(`t);
				TomTerm freshVariable = getFreshVariableStar(listType);				
				Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
				
				ConstraintList l = `concConstraint();		
				TomTerm lastElement = null;
				// SlotList sList = `slots;
				%match(slots){
					p:concSlot(_*,PairSlotAppl[Appl=appl],X*)->{
//[pem] I do not understand this trick: no MatchConstraint is generated if the last element is an appl?
						if (`X.length() == 0) {
							lastElement = `appl;
							break p;
						}						
						TomTerm newFreshVarList = getFreshVariableStar(listType);
						// if we have a variable
//[pem] can't we split the rule by dupplicating the pattern?
						if(((`appl) instanceof VariableStar) || ((`appl) instanceof UnamedVariableStar)){
							TomTerm beginSublist = getBeginVariableStar(listType);
							TomTerm endSublist = getEndVariableStar(listType);							
							// we put them in the inverse order in the list because later on we do a 'reverse' 
							l = `concConstraint(MatchConstraint(newFreshVarList,endSublist),
									MatchConstraint(appl,VariableHeadList(name,beginSublist,endSublist)),
									MatchConstraint(endSublist,freshVariable),
									MatchConstraint(beginSublist,freshVariable),l*);							
						}else{	// a term or a syntactic variable
							// we put them in the inverse order in the list because later on we do a 'reverse'
							l = `concConstraint(MatchConstraint(newFreshVarList,ExpressionToTomTerm(GetTail(name,freshVariable))),
									MatchConstraint(appl,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
									Negate(EmptyListConstraint(name,freshVariable)),l*);
						}
						freshVariable = newFreshVarList;
					}					 
					concSlot() ->{
						l = `concConstraint(EmptyListConstraint(name,freshVariable),l*);
					}
				}// end match
				// the last element needs a special treatment
				// 1. if it is a VariableStar, we shouldn't generate a while, just an assignment for the variable				
				// 2. if it is not a VariableStar, this means that we should check that the subject ends also
				// 3. if it is an UnamedVariableStar, there is nothing to do
				TomTerm newFreshVarList = getFreshVariableStar(listType);
//[pem] can't we do a post-treatment to inspect the last element, instead of using the lastElement variable?
//[pem] do not use instanceof to implement pattern matching
//[pem] use concSlot(_*,VariableStar[]) or concSlot(_*,UnamedVariableStar[]) instead
				if (lastElement != null){
					if(lastElement instanceof VariableStar){
						l = `concConstraint(MatchConstraint(lastElement,freshVariable),l*);
					}else if (!(lastElement instanceof UnamedVariableStar)){
						l = `concConstraint(EmptyListConstraint(name,newFreshVarList),
								MatchConstraint(newFreshVarList,ExpressionToTomTerm(GetTail(name,freshVariable))),
								MatchConstraint(lastElement,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
								Negate(EmptyListConstraint(name,freshVariable)),l*);						
					}
				}
				l = l.reverse();
				// add head equality condition + fresh var declaration
				l = `concConstraint(MatchConstraint(SymbolOf(g),RecordAppl(options,nameList,concSlot(),constraints)),
						freshVarDeclaration,l*);
				
				return `AndConstraint(l);
			}					
			// Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
			// X* = p1 /\ X* = p2 -> X* = p1 /\ freshVar = p2 /\ freshVar == X*
//[pem] make AndConstraint A*, this would simplify the code a lot
			andC@AndConstraint(concConstraint(X*,eq@MatchConstraint(v@VariableStar[AstName=x@!PositionName[]],p1),Y*)) ->{
				Constraint toApplyOn = `AndConstraint(concConstraint(Y*));
				TomNumberList path = TomConstraintCompiler.getRootpath();
				TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshVar_" + (++freshVarCounter)))));
				TomTerm freshVar = `v.setAstName(freshVarName);
				Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).apply(toApplyOn);
				if (res != toApplyOn){					
					return `AndConstraint(concConstraint(X*,eq,res));
				}
			}
		}
	}// end %strategy
	
	%strategy ReplaceMatchConstraint(varName:TomName, freshVar:TomTerm) extends `Identity(){
		visit Constraint {
			MatchConstraint(v@VariableStar[AstName=name],p) -> {
				if (`name == varName) {					
					return `AndConstraint(concConstraint(MatchConstraint(freshVar,p),MatchConstraint(TestVarStar(freshVar),v)));
				}				  
			}
		}
	}
	
	private static TomTerm getBeginVariableStar(TomType type){
		TomNumberList path = TomConstraintCompiler.getRootpath();
		TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("begin_" + (++beginEndCounter)))));
		return `VariableStar(concOption(),freshVarName,type,concConstraint());
	}
	
	private static TomTerm getEndVariableStar(TomType type){
		TomNumberList path = TomConstraintCompiler.getRootpath();
		TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("end_" + beginEndCounter))));
		return `VariableStar(concOption(),freshVarName,type,concConstraint());
	}
	
	private static TomTerm getFreshVariableStar(TomType type){
		TomNumberList path = TomConstraintCompiler.getRootpath();
		TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshList_" + (++freshVarCounter)))));
		return `VariableStar(concOption(),freshVarName,type,concConstraint());
	}
}
