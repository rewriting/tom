/*
 * Created on Nov 13, 2003
 */
package jtom.checker;

public final class TomCheckerMessage {
	  // Error messages linked to 
	public static String MainErrorMessage = "\n-- Error occured line: {0,number,integer} in structure `{1}` declared line: {2,number,integer} in file: {3}\n---> {4}\n" ;
	public static String MultipleSymbolDefinitionError = "Multiple definition of Symbol `{0}`";
	public static String SymbolCodomainError = "Symbol `{0}` has an unknown return type: `{1}`";
	public static String SymbolDomainError = "Slot {0,number,integer} of symbol `{1}` declaration has an unknown type: `{2}`";
	public static String MacroFunctionRepeated = "Repeated macro-function `{0}`";
	public static String NonLinearMacroFunction = "Arguments must be linear in macro-function `{0}`: Variable `{1}` is repeated" ;
	public static String MissingMacroFunctions = "Missing macro-function(s) [ {0} ]";
	public static String BadMakeDefinition = "Bad number of arguments in 'make(...)' macro-function: {0,number,integer} arguments found but {1,number,integer} expected in symbol definition";
	public static String ListSymbolDomainError = "List symbol `{0}` has an unknown parameter type: `{1}`";
	
	public static String WarningTwoSameSlotDecl = "Warning: GetSlot declaration `get_slot({0},...)` is repeated";
	public static String WarningIncompatibleSlotDecl = "Incompatible GetSlot declaration: `{0}` does not appears in symbol declaration";

  public static String RepeatedSlotName = "Repeated slot `{0}` in symbol declaration";
	
	public static String UnknownMatchArgumentTypeInSignature = "Variable `{0}` has an unknown type: `{1}`";
	
	public static String RepeatedMatchArgumentName = "Repeated variable `{0}` in match declaration";
	public static String BadMatchNumberArgument = "Bad number of arguments: {0,number,integer} argument(s) required by match signature but {1,number,integer} found";
	public static String MatchVariableStar = "Single list variable `{0}` is not allowed";
	public static String WrongMatchArgumentTypeInPattern = "Wrong type for slot {0,number,integer}:Type `{1}` required but Type `{2}` found";

	public static String UnknownSymbol = "Unknown symbol {0}";
	
	
	public static String UnknowUnamedList = "Not able to found a list symbol of type: `{0}`";
	public static String AmbigousUnamedList = "Too many list symbols with returned type `{0}`: {1}";
	public static String InvalidDisjunctionCodomain = "The symbol {0} has type `{1}` but type `{2}` was required";
	
	
	public static String BracketOnListSymbol = "[] are not allowed on lists or arrays nor constants, see `{0}`";
	public static String BadSlotName = "Slot Name `{0}` is not correct for symbol `{1}`. Possible slot names are : {3}";
	
	public static String SlotRepeated = "Same slot names can not be used several times in symbol `{0}`: Repeated slot Name : `{1}`";
	public static String SymbolNumberArgument = "Bad number of arguments for symbol `{0}`: {1,number,integer} argument(s) required but {2,number,integer} found";;
	
	
	public static String InvalidVariableStarArgument = "{0} is not allowed in non list symbol ";
	public static String VariableWithConstructorName = "Warning: Ambiguous symbol name. Is`{0}` a variable or a constructor? Prefer `{0}`() if it is a constructor";
	public static String RepeatedMatchArgumentName3 = " ";
	public static String RepeatedMatchArgumentName4 = " ";
	public static String RepeatedMatchArgumentName5 = " ";
	public static String RepeatedMatchArgumentName6 = " ";
	public static String RepeatedMatchArgumentName7 = " ";
	
	
	/*Object[] arguments = {
			new Integer(7),
			new Date(System.currentTimeMillis()),
			"a disturbance in the Force"
	};

	String result = MessageFormat.format(
			"At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.",
			arguments);

	output: At 12:30 PM on Jul 3, 2053, there was a disturbance
						in the Force on planet 7.*/
}
