/*
 * Created on Nov 13, 2003
 */
package jtom.checker;

public final class TomCheckerMessage {
    // Message level
  public static int TOM_ERROR = 0;
  public static int TOM_WARNING = 1;
  public static int DEFAULT_LOCATION = 1;
    // Main messages
  public static String MainErrorMessage = "-- Error occured line: {0,number,integer} in structure `{1}` declared line: {2,number,integer} in file: `{3}`\n---> {4}\n" ;
  public static String MainWarningMessage = "-- Warning occured line: {0,number,integer} in structure `{1}` declared line: {2,number,integer} in file: `{3}`\n---> {4}\n" ;
  
    //Error messages linked to operator and type definitions
  public static String MultipleSymbolDefinitionError = "Multiple definition of Symbol `{0}`";
  public static String SymbolCodomainError = "Symbol `{0}` has an unknown return type: `{1}`";
  public static String SymbolDomainError = "Slot {0,number,integer} of symbol `{1}` declaration has an unknown type: `{2}`";
  public static String MacroFunctionRepeated = "Repeated macro-function `{0}`";
  public static String NonLinearMacroFunction = "Arguments must be linear in macro-function `{0}`: Variable `{1}` is repeated" ;
  public static String MissingMacroFunctions = "Missing macro-function(s) [ {0} ]";
  public static String BadMakeDefinition = "Bad number of arguments in 'make(...)' macro-function: {0,number,integer} arguments found but {1,number,integer} expected in symbol definition";
  public static String ListSymbolDomainError = "List symbol `{0}` has an unknown parameter type: `{1}`";
  
    //Error messages linked to slot declaration during parsing
  public static String RepeatedSlotName = "Repeated slot `{0}` in symbol declaration";
  public static String WarningTwoSameSlotDecl = "Warning: GetSlot declaration `get_slot({0},...)` is repeated. Consider only the first one!!";
  public static String WarningMissingSlotDecl = "Warning: GetSlot declaration `get_slot({0},...)` is missing. Generic getsubterm macro will be used!!";
  public static String WarningIncompatibleSlotDecl = "Warning: Incompatible GetSlot declaration: `{0}` does not appears in symbol declaration";
  
  
  public static String BadSlotName = "Slot Name `{0}` is not correct for symbol `{1}`. Possible slot names are : {2}";
  
  public static String UnknownMatchArgumentTypeInSignature = "Variable `{0}` has an unknown type: `{1}`";
  
  public static String RepeatedMatchArgumentName = "Repeated variable `{0}` in `match` declaration";
  public static String BadMatchNumberArgument = "Bad number of arguments: {0,number,integer} argument(s) required by match signature but {1,number,integer} found";
  public static String IncorrectVariableStar = "Single list variable `{0}` is not allowed on top of `match` pattern";
  public static String WrongMatchArgumentTypeInPattern = "Wrong type for slot {0,number,integer}:Type `{1}` required but Type `{2}` found";
  
  public static String UnknownSymbol = "Unknown symbol `{0}`";
  public static String UnknownSymbolInDisjunction = "Unknown symbol `{0}` not allowed in disjunction";
  
  public static String UnknowUnamedList = "Not able to found a list symbol of type: `{0}`";
  public static String AmbigousUnamedList = "Too many list symbols with returned type `{0}`: {1}";
  public static String InvalidCodomain = "The symbol `{0}` has type `{1}` but type `{2}` was required";
  public static String InvalidDisjunctionCodomain = "The symbol `{0}` has type `{1}` but type `{2}` was required in disjunction";
  public static String InvalidDisjunctionDomain = "The symbol `{0}` has a different domain as first symbol in disjunction";
  
  public static String BracketOnListSymbol = "[] are not allowed on lists or arrays, see `{0}`";
  
  public static String SlotRepeated = "Same slot names can not be used several times in symbol `{0}`: Repeated slot Name : `{1}`";
  public static String SymbolNumberArgument = "Bad number of arguments for symbol `{0}`: {1,number,integer} argument(s) required but {2,number,integer} found";;
  
  
  public static String InvalidVariableStarArgument = "{0} is not allowed in non list symbol ";
  public static String AmbigousSymbolWithoutConstructor = "Warning: Ambiguous symbol name. Is`{0}` a variable or a constructor? Prefer `{0}`() if it is a constructor";
    // rule
  public static String IncorrectRuleLHSClass = "`{0}`: Impossible in rule left hand side";
  public static String IncorrectRuleRHSClass = "`{0}`: Impossible in rule right hand side";
    //public static String UnknownRuleSymbol = "Symbol `{0}` has no been declared";
  public static String DifferentRuleConstructor = "Rule head symbol name `{0}` expected, but `{1}` found";
  public static String NoRuleMakeDecl = "Rule head symbol `{0}` has no `make` method: It is necessary to define one!!";
  public static String IncorrectRuleRHSType = "Bad right hand side type: `{0} instead of `{1}`";
  public static String UnknownPermissiveSymbol = "Unknown symbol `{0}`: Can't do further analyses";
  
  public static String ImpossiblePermissiveAndDisjunction = "Disjunction is not allowed after backquote (`) call";
  public static String UnknownVariable = "Unknown variable(s) `{0}`";
  public static String BadVariableType = "Variable `{0}` has type `{1}` instead of type `{2}`";
  public static String IncoherentVariable = "Bad variable type for `{0}`: it has both type `{1}` and `{2}`";
  
  public static String MultipleRuleDefinition = "Rule `{0}` is repeated";
  public static String MalformedXMLTerm = "Malformed XML pattern: expecting `{0}` but got `{1}`";
  
}
