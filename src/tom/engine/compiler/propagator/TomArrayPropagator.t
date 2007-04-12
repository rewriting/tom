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
public class TomArrayPropagator implements TomIBasePropagator{

//--------------------------------------------------------	
  %include { adt/tomsignature/TomSignature.tom }	
  %include { mustrategy.tom}
//--------------------------------------------------------

  private static short freshVarCounter = 0;
  private static short beginEndCounter = 0;

  public Constraint propagate(Constraint constraint){
    return (Constraint)`InnermostId(ArrayPatternMatching()).apply(constraint);		
  }	

  %strategy ArrayPatternMatching() extends `Identity(){		
    visit Constraint{      
      // Decompose - only if 'g' != SymbolOf 
      // array[t1,X*,t2,Y*] = g -> array=SymbolOf(g) /\ fresh_index = 0 
      // /\ HasElement(fresh_index,g)  /\ t1=GetElement(fresh_index,g) /\ fresh_index1 = fresh_index + 1 
      // /\ begin1 = fresh_index1  /\ end1 = fresh_index1 /\ X* = VariableHeadArray(begin1,end1) /\ fresh_index2 = end1
      // /\ HasElement(fresh_index2,g) /\ t2=GetElement(fresh_index2,g)/\ fresh_index3 = fresh_index2 + 1  
      // /\ begin2 = fresh_index3  /\ end2 = fresh_index3 /\ Y* = VariableHeadArray(begin2,end2) /\ fresh_index4 = end2
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {      
            // if this is not an array, nothing to do
            if(!TomConstraintCompiler.isArrayOperator(TomConstraintCompiler.getSymbolTable().
                getSymbolFromName(`tomName))) {return `m;}        
            TomType termType = TomConstraintCompiler.getTermTypeFromTerm(`t);
            // declare fresh index = 0            
            TomTerm freshIndex = getFreshIndex();				
            Constraint freshIndexDeclaration = `MatchConstraint(freshIndex,TargetLanguageToTomTerm(ITL("0")));
            Constraint l = `AndConstraint();
    match:  %match(slots){
              // last element needs special treatment - see below
              concSlot(_*,PairSlotAppl[Appl=appl],X*,_)->{                
                TomTerm newFreshIndex = getFreshIndex();
                // if we have a star variable
                if(((`appl) instanceof VariableStar) || ((`appl) instanceof UnamedVariableStar)){
                  TomTerm beginIndex = getBeginIndex();
                  TomTerm endIndex = getEndIndex();
                  l = `AndConstraint(l*,
                      MatchConstraint(beginIndex,freshIndex),
                      MatchConstraint(endIndex,freshIndex),
                      MatchConstraint(appl,VariableHeadArray(name,g,beginIndex,endIndex)),
                      MatchConstraint(newFreshIndex,endIndex));							
                }else{	// a term or a syntactic variable
                  l = `AndConstraint(l*,                      
                      Negate(EmptyArrayConstraint(name,g,freshIndex)),                      
                      MatchConstraint(appl,ExpressionToTomTerm(GetElement(name,termType,g,Ref(freshIndex)))),
                      MatchConstraint(newFreshIndex,ExpressionToTomTerm(AddOne(Ref(freshIndex)))));
                }
                freshIndex = newFreshIndex;
              }
              // the last element needs a special treatment
              // 1. if it is a VariableStar, we shouldn't generate a while, just an assignment for the variable                               
              // 2. if it is not a VariableStar, this means that we should check that the subject ends also
              // 3. if it is an UnamedVariableStar, there is nothing to do
              concSlot(_*,PairSlotAppl[Appl=appl@VariableStar[]]) ->{
                l = `AndConstraint(l*,MatchConstraint(appl,ExpressionToTomTerm(
                    GetSliceArray(name,g,freshIndex,ExpressionToTomTerm(GetSize(name,g))))));
                break match;
              }
              // TODO - replace with an anti-pattern when the bug is solved
              concSlot(_*,PairSlotAppl[Appl=appl@UnamedVariableStar[]]) ->{
                break match;
              }
              concSlot(_*,PairSlotAppl[Appl=appl]) ->{                
                TomTerm newFreshIndex = getFreshIndex();
                l = `AndConstraint(l*,
                    Negate(EmptyArrayConstraint(name,g,freshIndex)),
                    MatchConstraint(appl,ExpressionToTomTerm(GetElement(name,termType,g,Ref(freshIndex)))),
                    MatchConstraint(newFreshIndex,ExpressionToTomTerm(AddOne(Ref(freshIndex)))),
                    EmptyArrayConstraint(name,g,newFreshIndex));                                    
              }
              concSlot() ->{
                l = `AndConstraint(l*,EmptyArrayConstraint(name,g,freshIndex));
              }
            }// end match                        
            // add head equality condition + fresh var declaration
            l = `AndConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
                freshIndexDeclaration,l*);
            return l;
          }					
          // Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
          // X* = p1 /\ X* = p2 -> X* = p1 /\ freshVar = p2 /\ freshVar == X*
          andC@AndConstraint(X*,eq@MatchConstraint(v@VariableStar[AstName=x@!PositionName[]],p1),Y*) ->{
            Constraint toApplyOn = `AndConstraint(Y*);
            TomNumberList path = TomConstraintCompiler.getRootpath();
            TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshVar_" + (++freshVarCounter)))));
            TomTerm freshVar = `v.setAstName(freshVarName);
            Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).apply(toApplyOn);
            if (res != toApplyOn){					
              return `AndConstraint(X*,eq,res);
            }
          }
    }
  }// end %strategy

  %strategy ReplaceMatchConstraint(varName:TomName, freshVar:TomTerm) extends `Identity(){
    visit Constraint {
      MatchConstraint(v@VariableStar[AstName=name],p) -> {
        if (`name == varName) {					
          return `AndConstraint(MatchConstraint(freshVar,p),MatchConstraint(TestVarStar(freshVar),v));
        }				  
      }
    }
  }

  private static TomTerm getBeginIndex(){
    TomNumberList path = TomConstraintCompiler.getRootpath();
    TomName freshIndexName  = `PositionName(concTomNumber(path*,NameNumber(Name("begin_" + (++beginEndCounter)))));
    return `Variable(concOption(),freshIndexName,TomConstraintCompiler.getIntType(),concConstraint());
  }

  private static TomTerm getEndIndex(){
    TomNumberList path = TomConstraintCompiler.getRootpath();
    TomName freshIndexName  = `PositionName(concTomNumber(path*,NameNumber(Name("end_" + beginEndCounter))));
    return `Variable(concOption(),freshIndexName,TomConstraintCompiler.getIntType(),concConstraint());
  }

  private static TomTerm getFreshIndex(){
    TomNumberList path = TomConstraintCompiler.getRootpath();
    TomName freshIndexName  = `PositionName(concTomNumber(path*,NameNumber(Name("freshIndex_" + (++freshVarCounter)))));
    return `Variable(concOption(),freshIndexName,TomConstraintCompiler.getIntType(),concConstraint());    
  }
}
