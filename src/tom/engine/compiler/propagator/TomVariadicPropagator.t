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

//--------------------------------------------------------	
  %include { adt/tomsignature/TomSignature.tom }	
  %include { mustrategy.tom}
//--------------------------------------------------------

  private static short freshVarCounter = 0;
  private static short beginEndCounter = 0;

  public Constraint propagate(Constraint constraint){
    return (Constraint)`InnermostId(VariadicPatternMatching()).apply(constraint);		
  }	

  %strategy VariadicPatternMatching() extends `Identity(){		
    visit Constraint{
//    [pem] I need some explanations to deeply understand the rules
//    TODO    	
      // Decompose - only if 'g' != SymbolOf 
      // conc(t1,X*,t2,Y*) = g -> conc=SymbolOf(g) /\ fresh_var = g 
      // /\ NotEmpty(fresh_Var)  /\ t1=GetHead(fresh_var) /\ fresh_var1 = GetTail(fresh_var) 
      // /\ begin1 = fresh_var1  /\ end1 = fresh_var1 /\ X* = VariableHeadList(begin1,end1) /\ fresh_var2 = end1
      // /\ NotEmpty(fresh_Var2) /\ t2=GetHead(fresh_var2)/\ fresh_var3 = GetTail(fresh_var2)  
      // /\ begin2 = fresh_var3  /\ end2 = fresh_var3 /\ Y* = VariableHeadList(begin2,end2) /\ fresh_var4 = end2
      m@MatchConstraint(t@RecordAppl(options,nameList@(name@Name(tomName),_*),slots,constraints),g@!SymbolOf[]) -> {        
        // if this is not a list, nothing to do
        if(!TomConstraintCompiler.isListOperator(TomConstraintCompiler.getSymbolTable().
            getSymbolFromName(`tomName))) {return `m;}    
        // declare fresh variable
        TomType listType = TomConstraintCompiler.getTermTypeFromTerm(`t);
        TomTerm freshVariable = getFreshVariableStar(listType);				
        Constraint freshVarDeclaration = `MatchConstraint(freshVariable,g);
//      TODO - simplify below
        Constraint l = `AndConstraint();
        match:  %match(slots){
          // last element needs special treatment - see below
          concSlot(_*,PairSlotAppl[Appl=appl],X*,_)->{                
            TomTerm newFreshVarList = getFreshVariableStar(listType);
            // if we have a variable
            if((`appl).isVariableStar() || (`appl).isUnamedVariableStar()){
              TomTerm beginSublist = getBeginVariableStar(listType);
              TomTerm endSublist = getEndVariableStar(listType);
              l = `AndConstraint(l*,
                  MatchConstraint(beginSublist,freshVariable),
                  MatchConstraint(endSublist,freshVariable),             
                  MatchConstraint(appl,VariableHeadList(name,beginSublist,endSublist)),
                  MatchConstraint(newFreshVarList,endSublist));							
            }else{	// a term or a syntactic variable
              l = `AndConstraint(l*,                      
                  Negate(EmptyListConstraint(name,freshVariable)),
                  MatchConstraint(appl,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
                  MatchConstraint(newFreshVarList,ExpressionToTomTerm(GetTail(name,freshVariable))));
            }
            freshVariable = newFreshVarList;
          }
          // the last element needs a special treatment
          // 1. if it is a VariableStar, we shouldn't generate a while, just an assignment for the variable                               
          // 2. if it is not a VariableStar, this means that we should check that the subject ends also
          // 3. if it is an UnamedVariableStar, there is nothing to do
          concSlot(_*,PairSlotAppl[Appl=appl@VariableStar[]]) ->{
            l = `AndConstraint(l*,MatchConstraint(appl,freshVariable));
            break match;
          }
          // TODO - replace with an anti-pattern when the bug is solved
          concSlot(_*,PairSlotAppl[Appl=appl@UnamedVariableStar[]]) ->{
            break match;
          }
          concSlot(_*,PairSlotAppl[Appl=appl]) ->{                
            TomTerm newFreshVarList = getFreshVariableStar(listType);
            l = `AndConstraint(l*,
                Negate(EmptyListConstraint(name,freshVariable)),
                MatchConstraint(appl,ExpressionToTomTerm(GetHead(name,listType,freshVariable))),
                MatchConstraint(newFreshVarList,ExpressionToTomTerm(GetTail(name,freshVariable))),
                EmptyListConstraint(name,newFreshVarList));                                    
          }
          concSlot() ->{
            l = `AndConstraint(l*,EmptyListConstraint(name,freshVariable));
          }
        }// end match                    
        // add head equality condition + fresh var declaration
        l = `AndConstraint(MatchConstraint(RecordAppl(options,nameList,concSlot(),constraints),SymbolOf(g)),
            freshVarDeclaration,l*);
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

  private static TomTerm getBeginVariableStar(TomType type){
    return TomConstraintCompiler.getFreshVariableStar("begin_" + (++beginEndCounter),type);
  }

  private static TomTerm getEndVariableStar(TomType type){    
    return TomConstraintCompiler.getFreshVariableStar("end_" + beginEndCounter,type);
  }

  private static TomTerm getFreshVariableStar(TomType type){    
    return TomConstraintCompiler.getFreshVariableStar("freshList_" + (++freshVarCounter),type);
  }
}
