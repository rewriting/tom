package jtom.checker;

import java.util.logging.*;
import java.util.*;

import aterm.*;
import jtom.*;
import jtom.adt.tomsignature.types.*;
import tom.platform.adt.platformoption.types.*;
import tom.platform.*;

import tom.library.traversal.Collect1;

/**
 * The TomTypeChecker plugin.
 */
public class TomTypeChecker extends TomChecker {

  %include { adt/TomSignature.tom }

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
  
  public RuntimeAlert run() {
    RuntimeAlert result = new RuntimeAlert();
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      //int errorsAtStart = getStatusHandler().nbOfErrors();
      //int warningsAtStart = getStatusHandler().nbOfWarnings();
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        checkTypeInference( (TomTerm)getWorkingTerm() );
        // verbose
        getLogger().log( Level.INFO, "TomTypeCheckingPhase",
                         new Integer((int)(System.currentTimeMillis()-startChrono)) );
        //printAlertMessage(errorsAtStart, warningsAtStart);
      } catch (Exception e) {
        getLogger().log( Level.SEVERE, "ExceptionMessage",
                         new Object[]{getClass().getName(), getStreamManager().getInputFile().getName(),e.getMessage()} );
        e.printStackTrace();
        // result.addError();
      }
    } else {
      // type checker desactivated    
      getLogger().log(Level.INFO, "TypeCheckerInactivated");
    }
    return result;
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
          if(term instanceof Instruction) {
            %match(Instruction term) {
              Match(_, PatternList(list), oplist) -> {  
                currentTomStructureOrgTrack = findOriginTracking(`oplist);
                verifyMatchVariable(`list);
                return false;
              }
              RuleSet(list, orgTrack) -> {
                currentTomStructureOrgTrack = `orgTrack;
                verifyRuleVariable(`list);
                return false;
              }
            }
          } 
          return true;
        }// end apply
      }; // end new
    traversal().genericCollect(expandedTerm, collectAndVerify);
  } //checkTypeInference
  
  private void verifyMatchVariable(TomList patternList) {
    while(!patternList.isEmpty()) {
      TomTerm pa = patternList.getHead();
      TomTerm patterns = pa.getTermList();
        // collect variables
      ArrayList variableList = new ArrayList();
      collectVariable(variableList, patterns);      
      verifyVariableTypeListCoherence(variableList);
      patternList = patternList.getTail();
    }
  } //verifyMatchVariable
  
  /**
   * The notion of conditional rewrite rule can be generalised with a sequence of conditions
   * as in lhs -> rhs where P1:=C1 ... where Pn:=Cn if Qj==Dj 
   * (i) Var(Pi) inter (var(lhs) U var(P1) U ... U var(Pi-1)) = vide
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
              if(!areAllExistingVariableTest(cVar, variableTable, "DeclaredVariableIssueInWhere")) {
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
              if(!areAllExistingVariableTest(pVar, variableTable, "DeclaredVariableIssueInIf")) {
                // there is a fresh variable
                break;
              }
              // (iv)
              ArrayList cVar = new ArrayList();
              collectVariable(cVar, `c);
              if(!areAllExistingVariableTest(cVar, variableTable, "DeclaredVariableIssueInIf")) {
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
      areAllExistingVariableTest(variableRhs, variableTable, "UnknownRuleRhsVariable");
      
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
                       TomMessage.getMessage("IncoherentVariable"),
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
                       TomMessage.getMessage("IncoherentVariable"),
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
                     TomMessage.getMessage("FreshVariableIssue"),
                       new Object[]{name.getString()});
         
        return false;
      }
    }
    return true;
  }

  /** (ii) condition and (iii) at the end when varaibleTable is full */
  private boolean areAllExistingVariableTest(ArrayList cVar, Hashtable variableTable, String message) {
    Iterator it = cVar.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(!variableTable.containsKey(name)) {
        messageError(findOriginTrackingLine(variable.getOption()),
                     TomMessage.getMessage(message),
                     new Object[]{name.getString()});             
        return false;
      }
    }
    return true;
  }
  
} // class TomTypeChecker
