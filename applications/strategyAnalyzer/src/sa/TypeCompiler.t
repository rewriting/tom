package sa;

import sa.rule.types.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import tom.library.sl.*;
import aterm.*;
import aterm.pure.*;

public class TypeCompiler {
  %include { rule/Rule.tom }
  %include { sl.tom }
//   %include { java/util/types/Map.tom }
//   %include { java/util/types/List.tom }
//   %include { java/util/types/ArrayList.tom }

  %typeterm Signature { implement { Signature } }

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The typed signature
  private Signature typedSignature;
  // The generated (ordered) TRS
  private RuleList generatedRules;

  public TypeCompiler(Signature extractedSignature) {
    this.extractedSignature = extractedSignature;
    this.typedSignature = new Signature(extractedSignature);
    this.generatedRules = `ConcRule();
  }

  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }

  public Signature getTypedSignature() {
    return this.typedSignature;
  }

  public RuleList getGeneratedRules() {
    return this.generatedRules;
  }

  /**
   * Transform each rewrite rule to a list of well-typed rules with the same behaviour. 
   **/
  public void typeRules(RuleList untypedRules) {
    this.generatedRules = `ConcRule();
    Map<String,GomType> env = new HashMap<String,GomType>();

    %match(untypedRules) {
      ConcRule(_*,rule,_*) -> {
        %match(rule) {
          Rule(lhs@Appl(stratOp,TermList(arg,_*)), rhs) -> {
            // get the possible codomain(s) for the head operator of the LHS
            // - boolean operators: Bool
            // - operators of the form ALL-f... : codomain of f
            // - operators of the form CHOICE... (no ...-f) : codomain of its first argument
            Set<GomType> types = new HashSet<GomType>();
            if(getExtractedSignature().isBooleanOperator(`stratOp)) {
              // if head opearator of the rule is EQ or AND then the codomain should be BOOL 
              types.add(Signature.TYPE_BOOLEAN);
            } else { 
              if(Tools.getOperatorName(`stratOp) != null) {
                // if symbol of the form ALL-f... the codomain is given by the codomain of f
                types = this.getTypes(env,`lhs);
              } else { 
                // otherwise the codomain is given by the codomain of its argument
                types = this.getTypes(env,`arg);
              }
            }
            // normally shouldn't happen 
            if(types.size() == 0) {
              throw new UntypableTermException("RULE OMITTED for " + `stratOp + "  because no possible codomain");
            }

            // for each potential codomain generate a new rule
            // (the potential codomain is unique for all operators but for BOTTOM
            // which can be of any type of the declared (ie extracted) signature
            for(GomType type: types) {
              try {
                env = new HashMap<String,GomType>(); // start with fresh env for each rule generation
                Term typedLhs = this.propagateType(env,`lhs,type); // type of all variables is inferred in env
                // head symbol of LHS is added to the typed signature
                Term typedRhs = this.propagateType(env,`rhs,type);
                Rule newRule = `Rule(typedLhs, typedRhs);
                //                 this.generatedRules.add(newRule);
                generatedRules = `ConcRule(generatedRules*, newRule); // must preserve the order
              } catch(TypeMismatchException typeExc) {
                System.err.println("RULE OMITTED for " + `stratOp + "  because of " + typeExc.getMessage());
              }
            }
          }
        }
      }
    }
  }

  /** Get the potential types of a term by looking at its head symbol
   *  The head symbol can be a symbol from the extracted signature or Bottom or a boolean operator
   *  (symbol can't be a generated)
   */  
  private Set<GomType> getTypes(Map<String,GomType> env, Term term) {
    Set<GomType> types = new HashSet<GomType>();
    Signature eSig = this.getExtractedSignature();
    %match(term) {
        Appl(symbol,_) -> {
          if(`symbol == Signature.BOTTOM) { // for BOTTOM add all possible types
            types.addAll(eSig.getCodomains());
          } else if(eSig.isBooleanOperator(`symbol)) {
            // for boolean ops (in fact only TRUE or FALSE can occur) add BOOLEAN
            types.add(Signature.TYPE_BOOLEAN);
          } else if(Tools.getOperatorName(`symbol) != null) { 
            // for symbol of the form ALL-f add the type of f
            types.add(eSig.getCodomain(Tools.getOperatorName(`symbol)));
          } else if(eSig.getCodomain(`symbol) != null) {
            // for symbols of the original signature add their type
            types.add(eSig.getCodomain(`symbol));
          }
        }

        Var(name) -> { // for VAR get the type from the environment
          if(env.get(`name) != null) {
            types.add(env.get(`name));
          } else { // if we don't know it's type than it could be any type (eg for mainstrat)
            types.addAll(eSig.getCodomains());
          }
        }
    }
    return types;
  }
   
  /**
   * Propagate the type information in the names of the symbols. Term t of the form:
   * - all_39(s1)
   * - all_39-f(s1,...,sn,s) with n=ar(f)
   * - one_42(s1)
   * - one_42-f(s1,...,sn) with n=ar(f)
   * - ...
   */
  private Term propagateType(Map<String,GomType> env, Term t, GomType type) {
    Term typedTerm = t;
    Signature eSig = getExtractedSignature();

    %match(t) {
      Appl(name,args) -> {
        if(eSig.isBooleanOperatorExceptEQ(`name)) {
          // for AND, TRUE, FALSE we override the "type" imposed by propagateType: always BOOLEAN
          type = Signature.TYPE_BOOLEAN;
        } else if(eSig.isBooleanOperatorEQ(`name)) {
          // for EQ we override the "type" imposed by propagateType: the type of its first argument
          %match(args) {
            TermList(arg,_*) -> {
              if(this.getTypes(env,`arg).size() == 1) { // at least one type (ie well-typed) and only one (ie no Bottom)
                //                 type = this.getTypes(env,`arg).get(0);
                type = this.getTypes(env,`arg).iterator().next(); // get the "first" (and only) element 
              } else {  // normally shouldn't happen 
                throw new UntypableTermException("No possible type for "+`arg+" in term "+t);
              }
            }
          }
        }

        GomTypeList domain = null;
        String fun = null;
        String typedName = null;

        // build the symbol name by adding the type information
        if(eSig.getCodomain(`name) != null) {
          // if term in the extracted signature then first check if it's well-typed
          fun = `name;
          if(eSig.getCodomain(`name) != type) {
            // if type mismatch than the rule should be eventually removed
            throw new TypeMismatchException("BAD ARG: " + `name + " TRY TYPE " + type + " IN TERM "+t);
          }
          // don't change its name if symbol from original (extracted) signature
          typedName = `name;
        } else {
          // retrieve fun from name of the form symbolName-fun_typeName
          fun = Tools.getOperatorName(`name);
          typedName = Tools.addTypeName(`name,type.getName()); // add type information to symbol name
        }

        String nameMain = Tools.getSymbolNameMain(`name);
        // build the domain of the typed symbol
        if(fun != null) {
          // if a composite symbol (e.g. all-f_...)  or symbol from original signature (eg  f, g, ...)
          domain = eSig.getDomain(fun);
          if(domain == null) {
            // normally should'n happen (unless wrongly built symbol name)
            throw new UntypableTermException("Type of symbol "+ fun +"cannot be determined");
          }
          // for all_f add the type of f(...) at the end
          if(StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.ALL) {
            domain = `ConcGomType(domain*, type); 
          }
        } else {
          // if Boolean or Generated symbol or Bottom
          domain = `ConcGomType();
          if(!nameMain.equals(Signature.TRUE) && !nameMain.equals(Signature.FALSE)) { // if any arguments
            domain = `ConcGomType(type); // first argument has always the same type as the term
            if(eSig.isBooleanOperator(`name) || 
               (Tools.isSymbolNameAux(`name) && StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.SEQ)) {
              // if boolean operator (EQ or AND) or if aux symbol for SEQ (ie seqAux_...) then there is a second parameter of the same type
              domain = `ConcGomType(type,type);
            } else if(Tools.isSymbolNameAux(`name) && StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.RULE) {
              // if aux symbol for RULE (originating from a non-linear rule compilation) then there is a second parameter of type Bool
              domain = `ConcGomType(type,Signature.TYPE_BOOLEAN);
            }
          }
        }

        // propagate the type to the subterms
        TermList args = `args;
        TermList newArgs = `TermList();
        GomTypeList tl = domain;
        while(!args.isEmptyTermList()) {
          Term arg = args.getHeadTermList();
          GomType arg_type = tl.getHeadConcGomType();
          Term typedArg = propagateType(env, arg, arg_type);
          if(typedArg != null) {
            newArgs = `TermList(newArgs*, typedArg);
          } else {
            throw new UntypableTermException("Can't type argument " + arg +" in "+t);
          }
          args = args.getTailTermList();
          tl = tl.getTailConcGomType();
        }
        typedTerm = `Appl(typedName,newArgs);

        // add info into typed signature
        if(`name == Signature.EQ) {
          typedSignature.addSymbol(typedName,domain,Signature.TYPE_BOOLEAN);
        } else {
          typedSignature.addSymbol(typedName,domain,type);
        }
        // TODO: build domain exactly as it should be (problem for TRUE, ruleAUX, ...?)
      }

      s@Var(name) -> {
        // set the type in the environment
        GomType varType = env.get(`name);
        if(varType != null) { // if type already set
          if(varType != type) { // if try to assign a different type
            throw new RuntimeException("Attemp to type variable with different type");
          }
        } else { // set type if not already done
          env.put(`name,type);
        }
        // dont't change it
        typedTerm = `s;
      }
    }
    return typedTerm;
  }

}
 
  
  /********************************************************************************
   *     END
   ********************************************************************************/
