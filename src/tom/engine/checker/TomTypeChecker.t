/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.adt.tomsignature.types.*;
import tom.library.traversal.Collect1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker {

  %include { adt/tomsignature/TomSignature.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='noTypeCheck' altName='' description='Do not perform type checking' value='false'/></options>";

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomTypeChecker.DECLARED_OPTIONS);
  }

  /** Constructor */
  public TomTypeChecker() {
    super("TomTypeChecker");
  }
  
  public void run() {
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        checkTypeInference( (TomTerm)getWorkingTerm() );
        // verbose
        getLogger().log( Level.INFO, TomMessage.tomTypeCheckingPhase.getMessage(),
                         new Integer((int)(System.currentTimeMillis()-startChrono)) );
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                         new Object[]{getClass().getName(), getStreamManager().getInputFileName(),e.getMessage()} );
        e.printStackTrace();
      }
    } else {
      // type checker desactivated    
      getLogger().log(Level.INFO, TomMessage.typeCheckerInactivated.getMessage());
    }
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("noTypeCheck");
  }
  
  /**
   * Main type checking entry point:
   * We check all Match and RuleSet instructions
   */
  private void checkTypeInference(TomTerm expandedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
      public boolean apply(ATerm term) {
        %match(Instruction term) {
          Match(_, patternInstructionList, oplist) -> {  
            currentTomStructureOrgTrack = findOriginTracking(`oplist);
            verifyMatchVariable(`patternInstructionList);
            return false;
          }
          RuleSet(list, orgTrack) -> {
            currentTomStructureOrgTrack = `orgTrack;
            verifyRuleVariable(`list);
            return false;
          }
        }
        %match(Declaration term) {
          Strategy(_,visitList,orgTrack) -> {
            currentTomStructureOrgTrack = `orgTrack;
            verifyStrategyVariable(`visitList);
            return false;
          }
        }
        return true;
      }// end apply
    }; // end new
    traversal().genericCollect(expandedTerm, collectAndVerify);
  } //checkTypeInference

  /* 
   * Collect unknown (not in symbol table) appls without ()
   */
  private void collectUnknownsAppls(ArrayList unknownsApplsInWhen, TomList guards) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm term) {
					%match(TomTerm term) {
						app@TermAppl[option=opts,args=[]] -> {
							boolean isConstructor = false;
							%match(OptionList opts) {
								(_*,Constructor[],_*) -> {
									isConstructor = true;
								}
							}
							if(!isConstructor) {
								if((symbolTable().getSymbolFromName(getName(`app)))==null) {
									messageError(findOriginTrackingLine(`app.getOption()),
											TomMessage.unknownVariableInWhen.getMessage(),
											new Object[]{getName(`app)});
								}
								// else, it's actually app()
							} // else, it's a unknown (ie : java) function
							return true;
						}
          } 
          return true;
        }// end apply
      }; // end new
    traversal().genericCollect(guards, collectAndVerify);
  }
  
  private void verifyMatchVariable(PatternInstructionList patternInstructionList) {
    while(!patternInstructionList.isEmpty()) {
      PatternInstruction pa = patternInstructionList.getHead();
      Pattern pattern = pa.getPattern();
        // collect variables
      ArrayList variableList = new ArrayList();
      collectVariable(variableList, pattern);
      verifyVariableTypeListCoherence(variableList);
      // verify variables in WHEN instruction
      ArrayList unknownsApplsInWhen = new ArrayList();
      // collect unknown variables
      collectUnknownsAppls(unknownsApplsInWhen, pattern.getGuards());
      //System.out.println("vars in guard "+unknownsApplsInWhen);

      patternInstructionList = patternInstructionList.getTail();
    }
  } //verifyMatchVariable
  
  private void verifyStrategyVariable(TomVisitList list) {
    TomForwardType visitorFwd = null;
    TomForwardType currentVisitorFwd = null;
    while(!list.isEmpty()) {
      TomVisit visit = list.getHead();
      %match(TomVisit visit) {
        VisitTerm(visitType,patternInstructionList) -> {

          //check that all visitType have same visitorFwd
          currentVisitorFwd = symbolTable().getForwardType(`visitType.getTomType().getString());
          //noVisitorFwd defined for visitType
          if (currentVisitorFwd == null || currentVisitorFwd == `EmptyForward()){ 
            messageError(`visitType.getTlType().getTl().getStart().getLine(),
                TomMessage.noVisitorForward.getMessage(),
                new Object[]{`visitType.getTomType().getString()});
          }
          else if (visitorFwd == null) {//first visit 
            visitorFwd = currentVisitorFwd;
          }
          else {//check if current visitor equals to previous visitor
            if (currentVisitorFwd != visitorFwd){ 
              messageError(`visitType.getTlType().getTl().getStart().getLine(),
                  TomMessage.differentVisitorForward.getMessage(),
                  new Object[]{visitorFwd.getString(),currentVisitorFwd.getString()});
            }
          }
          verifyMatchVariable(`patternInstructionList);
        }      
      }
      // next visit
      list = list.getTail();
    }
  }

  /**
   * The notion of conditional rewrite rule can be generalised with a sequence of conditions
   * as in lhs -> rhs where P1:=C1 ... where Pn:=Cn if Qj==Dj 
   * (i) Var(Pi) inter (var(lhs) U var(P1) U ... U var(Pi-1)) = empty
   * => introduced variables in Pi are "fresh"
   * (ii) var(ci) included in (var(lhs) U var(P1) U ... U var(Pi-1))
   * no new fresh variables in Ci
   * (iii) Var(rhs) included in (var(lhs) U U(var(Pi)))
   * => no new fresh variables in r
   * (iv) the condition Qj==Dj shall never lead to the declaration of a new variable
   */
  private void verifyRuleVariable(TomRuleList list) {
    while(!list.isEmpty()) {
      TomRule rewriteRule = list.getHead();
      TomTerm ruleLhs = rewriteRule.getLhs();
      TomTerm ruleRhs = rewriteRule.getRhs();
      InstructionList condList = rewriteRule.getCondList();
      Option orgTrack = findOriginTracking(rewriteRule.getOption());
           
      // the accumulator for defined variables
      Hashtable variableTable = new Hashtable();
      // collect lhs variable 
      ArrayList freshLhsVariableList = new ArrayList();
      collectVariable(freshLhsVariableList, ruleLhs);

      // fill the table with found variables in lhs
      if(!appendToTable(variableTable, freshLhsVariableList)) {
        // there are already some coherence issues: same name but not same type
        break;
      }
      
      %match(InstructionList condList) {
        (_*, cond, _*) -> {
          Instruction condition = `cond ;
          %match(Instruction condition) {
            MatchingCondition(p@lhs, c@rhs) -> {
              // (i)
              ArrayList pVar = new ArrayList();
              collectVariable(pVar, `p);
              if(!areAllFreshVariableTest(pVar, variableTable)) {
                // at least one no fresh variable
                break;
              }
              // (ii)
              ArrayList cVar = new ArrayList();
              collectVariable(cVar, `c);
              if(!areAllExistingVariableTest(cVar, variableTable, TomMessage.declaredVariableIssueInWhere)) {
                // there is a fresh variable
                break;
              }
              
              // fill the table
              if(!appendToTable(variableTable, pVar)) {
                // there are some coherence issues: same name but not same type
                break;
              }
            }
            TypedEqualityCondition(_, p@lhs, c@rhs) -> {
               // (iv)
              ArrayList pVar = new ArrayList();
              collectVariable(pVar, `p);
              if(!areAllExistingVariableTest(pVar, variableTable, TomMessage.declaredVariableIssueInIf)) {
                // there is a fresh variable
                break;
              }
              // (iv)
              ArrayList cVar = new ArrayList();
              collectVariable(cVar, `c);
              if(!areAllExistingVariableTest(cVar, variableTable, TomMessage.declaredVariableIssueInIf)) {
                // there is a fresh variable
                break;
              }
              
              // fill the table
              if(!appendToTable(variableTable, pVar)) {
                // there are some coherence issues: same name but not same type
                break;
              }
            }
          }
        }
      }
      
      // (iii)
      ArrayList variableRhs = new ArrayList();
      collectVariable(variableRhs, ruleRhs);
      areAllExistingVariableTest(variableRhs, variableTable, TomMessage.unknownRuleRhsVariable);
      
      // next rewrite rule
      list = list.getTail();
    }
  } //verifyRuleVariable
  
  private void verifyVariableTypeListCoherence(ArrayList list) {
    // compute multiplicities
    HashSet set = new HashSet();
    HashMap map = new HashMap();
    Iterator it = list.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      
      if(set.contains(name.getString())) {
        TomTerm var = (TomTerm)map.get(name);
        TomType type = var.getAstType();
        TomType type2 = variable.getAstType();

        if(!(type==type2)) {
          messageError(findOriginTrackingLine(variable.getOption()),
                       TomMessage.incoherentVariable.getMessage(),
                       new Object[]{name.getString(), type.getTomType().getString(), type2.getTomType().getString()});
        }
      } else {
        map.put(name, variable);
        set.add(name.getString());
      }
    }
  }  //verifyVariableTypeListCoherence  

  /**
   * Append variables to table 
   * if variable name already exists, it ensures it is coherent with the previous found type
   * return true if OK else false
   */
  private boolean appendToTable(Hashtable variableTable, List freshLhsVariableList) {
    Iterator it = freshLhsVariableList.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName nameVar = variable.getAstName();
      TomType typeVar = variable.getAstType();
        
      if(variableTable.containsKey(nameVar)) {
        // this is an issue
        //TomTerm var = (TomTerm)variableTable.get(nameVar);
        //TomType type = var.getAstType();
        TomType type = (TomType)variableTable.get(nameVar);
        if(!(type==typeVar)) {
          messageError(findOriginTrackingLine(variable.getOption()),
                       TomMessage.incoherentVariable.getMessage(),
                       new Object[]{nameVar.getString(), type.getTomType().getString(), typeVar.getTomType().getString()});
          return false;
        }
      } else {
        // append to table
        variableTable.put(nameVar, typeVar);
        //System.out.println("Adding "+nameVar+" "+typeVar);
      }
    }
    return true;
  }
  
  /** (i) condition */
  private boolean areAllFreshVariableTest(ArrayList pVar, Hashtable variableTable) {
    Iterator it = pVar.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(variableTable.containsKey(name)) {
        messageError(findOriginTrackingLine(variable.getOption()),
                     TomMessage.freshVariableIssue.getMessage(),
                     new Object[]{name.getString()});
         
        return false;
      }
    }
    return true;
  }

  /** (ii) condition and (iii) at the end when varaibleTable is full */
  private boolean areAllExistingVariableTest(ArrayList cVar, Hashtable variableTable, TomMessage message) {
    Iterator it = cVar.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(!variableTable.containsKey(name)) {
        messageError(findOriginTrackingLine(variable.getOption()),
                     message.getMessage(),
                     new Object[]{name.getString()});             
        return false;
      }
    }
    return true;
  }
  
} // class TomTypeChecker
