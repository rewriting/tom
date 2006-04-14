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
 *
 **/

package tom.engine;

import tom.platform.PlatformMessage;

/**
 * The TomMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class TomMessage implements PlatformMessage {
  private final String message;

  private TomMessage(String message) {
    this.message = message;
  }
    
  public static final TomMessage loggingInitializationFailure = 
    new TomMessage("Error during Tom logging module initialization:{0}");

  public static final TomMessage simpleMessage         =
    new TomMessage("{0}:{1,number,integer}: {2}");
  public static final TomMessage exceptionMessage      =
    new TomMessage("{0}: Unhandled exception occurs with input {1}: See stacktrace\n+++++++++++++++++++++++++\n{2}+++++++++++++++++++++++++");
  public static final TomMessage taskErrorMessage      =
    new TomMessage("{0}: Encountered {1,number,integer} error(s) and {2,number,integer} warning(s)\nNo file generated.\n");
  public static final TomMessage taskWarningMessage    =
    new TomMessage("{0}: Encountered {1,number,integer} warning(s).\n");

  // TomOptionManager
  // Error messages
  public static final TomMessage optionNotFound        =
      new TomMessage("TomOptionManager: Option `{0}` not found");
  public static final TomMessage prerequisitesIssue    =
      new TomMessage("TomOptionManager: {0} can't run : prerequisites are not met");
  public static final TomMessage incorrectOptionValue  =
      new TomMessage("TomOptionManager: Option `{0}` was expected with value `{1}`, but `{2}` was found instead");
  public static final TomMessage outputTwice           =
      new TomMessage("TomOptionManager: Output filename specified twice");
  public static final TomMessage destdirTwice          =
      new TomMessage("TomOptionManager: Destination directory specified twice");
  public static final TomMessage invalidOption         =
      new TomMessage("TomOptionManager: `{0}` is not a valid option");
  public static final TomMessage incompleteOption      =
      new TomMessage("TomOptionManager: Expecting information after option `{0}`");
  public static final TomMessage noFileToCompile       =
      new TomMessage("TomOptionManager: No file to compile");
  public static final TomMessage outputWithMultipleCompilation =
      new TomMessage("TomOptionManager: Cannot specify --output with multiple compilation inputs");
  // Debug messages
  public static final TomMessage setValue              =
      new TomMessage("TomOptionManager: Set `{0}` to `{1}` (old value : `{2}`)");

  // TomPluginFactory
  public static final TomMessage classNotAPlugin       =
    new TomMessage("{0}: Class {1} does not implement the tom.engine.TomPlugin interface as required");
  public static final TomMessage classNotFound         =
    new TomMessage("{0}: Class {1} not found");
  public static final TomMessage instantiationError    =
    new TomMessage("{0}: An error occured during the instantiation of class {1}");

  // TomGenericPlugin
  public static final TomMessage tomTermExpected       =
    new TomMessage("{0}: A TomTerm was expected by the plugin");

  // Plugings common
  public static final TomMessage invalidPluginArgument =
    new TomMessage("{0}.setArg expecting {1} but {2} argument");

  // parser.TomParserPlugin
  public static final TomMessage fileNotFound          =
      new TomMessage("File {0} not found");
  public static final TomMessage tokenStreamException  =
      new TomMessage("TokenStreamException catched: See stacktrace\n\n{0}");
  public static final TomMessage recognitionException  =
      new TomMessage("RecognitionException catched: See stacktrace\n\n{0}");
  // parser.TomParser
  // TODO : simplify the message in using PlatformLogRecord with detail
  // As these messages are propagated via an exception in TomLanguage.g.t, it is not trivial
  public static final TomMessage malformedXMLTerm      =
      new TomMessage("In file {0} at line {1,number,integer}: In structure {2}, malformed XML pattern: expecting {3} but got {4}");
  public static final TomMessage malformedStrategy=
      new TomMessage("In file {0} at line {1,number,integer}: In structure {2}, malformed strategy: expecting {3} but got {4}");
  public static final TomMessage errorTwoSameSlotDecl=
    new TomMessage("{0}:{1,number,integer}: In structure {2} declared line {3,number,integer}, get_slot({4},...) is repeated.");
  public static final TomMessage errorIncompatibleSlotDecl =
    new TomMessage("{0}:{1,number,integer}: In structure {2} declared line {3,number,integer}, get_slot: {4} is not known");
  // used with TomMessage to format exception messages
  public static final TomMessage missingIncludedFile   =
      new TomMessage("Missing file name in %include structure in file {0} at line {1,number,integer}");
  public static final TomMessage includedFileNotFound  =
      new TomMessage("Included file `{0}` not found in file {1} at line {2,number,integer}");
  public static final TomMessage includedFileCycle     =
      new TomMessage("Included file `{0}` found at line {1,number,integer} in file `{2}` forms a cycle");
  public static final TomMessage includedFileAlreadyParsed=
      new TomMessage("Included file {0} has already been parsed");
  public static final TomMessage errorWhileIncludingFile=
      new TomMessage("Exception {0} occurs in parsing file `{1}` included in file {2} at line {3,number,integer}: See stacktrace\n\n{4}");
  public static final TomMessage gomFailure =
      new TomMessage("An error occurs dealing with %gom input in {0} at line {1,number,integer}");
  public static final TomMessage vasPlatformFailure    =
      new TomMessage("Fail to obtain a Vas PluginPlatform in {0} at line {1,number,integer}");
  public static final TomMessage vasFailure            =
      new TomMessage("An error occurs dealing with vas input in {0} at line {1,number,integer}");
  public static final TomMessage iOExceptionWithGeneratedTomFile=
      new TomMessage("Generated file `{0}` from vas in file `{1}` caused an IOException: {2}");
  public static final TomMessage exceptionWithGeneratedTomFile =
      new TomMessage("Generated file `{0}` from vas in file `{1}` caused an IOException: See stackTrace\n\n{2}");
  public static final TomMessage repeatedSlotName      =
      new TomMessage("Repeated slot `{0}` in symbol declaration");
  public static final TomMessage braceExpected         =
      new TomMessage("')' expected line {0,number,integer}: EOF encountered ");
  public static final TomMessage invalidBackQuoteTerm  =
      new TomMessage("Invalid backquote term started line #{0,number,integer}: EOF encountered ");

  public static final TomMessage backendInactivated    =
      new TomMessage("The TomBackend plugin is not activated and thus WILL NOT RUN:No output will be generated ");
  public static final TomMessage backendIOException    =
      new TomMessage("TomBackend: IOException occurs writting to file {0}: {1} ");
  public static final TomMessage deprecatedDisjunction =
      new TomMessage("Disjunction of patterns is deprecated and may be removed in a future version. Try to use disjunction of symbols instead ");

  // checker.TomChecker
  public static final TomMessage findOTL               =
    new TomMessage("{0}: findOriginTrackingLine : not found (TomChecker)");
  public static final TomMessage syntaxCheckerInactivated=
      new TomMessage("The syntax checker is not activated and thus WILL NOT RUN.");
  public static final TomMessage typeCheckerInactivated=
      new TomMessage("The type checker is not activated and thus WILL NOT RUN.");

  public static final TomMessage freshVariableIssue    =
      new TomMessage("Expecting a fresh variable in WHERE condition left hand side instead of {0}");
  public static final TomMessage declaredVariableIssueInIf=
      new TomMessage("Expecting an already declared variable in IF condition instead of {0}");
  public static final TomMessage declaredVariableIssueInWhere=
      new TomMessage("Expecting an already declared variable in WHERE condition instead of {0}");
  public static final TomMessage unknownRuleRhsVariable=
      new TomMessage("Rule right hand side uses an undeclared variable {0}");

  // compiler
  public static final TomMessage errorMissingSlotDecl=
      new TomMessage("In symbol {0}, get_slot for {1} is missing.");

  // optimizer.TomOptimizer
  public static final TomMessage tomOptimizationType =
    new TomMessage("optimization of type {0}");
  public static final TomMessage unusedVariable        =
    new TomMessage("{0}:{1,number,integer}: Variable `{2}` is never used");
  public static final TomMessage remove                =
    new TomMessage("{0} -> remove:     {1}");
  public static final TomMessage inline                =
    new TomMessage("{0} -> inline:     {1}");
  public static final TomMessage noInline              =
    new TomMessage("{0} -> no inline:  {1}");
  public static final TomMessage doNothing             =
    new TomMessage("{0} -> do nothing: {1}");

  // verifier
  public static final TomMessage verifierInactivated   =
      new TomMessage("The verifier is not activated and thus WILL NOT RUN.");

  // verbose messages
  public static final TomMessage tomParsingPhase       =
      new TomMessage("TOM parsing phase ({0,number,integer} ms)");
  public static final TomMessage tomSyntaxCheckingPhase=
      new TomMessage("TOM syntax checking phase ({0,number,integer} ms)");
  public static final TomMessage tomExpandingPhase     =
      new TomMessage("TOM expanding phase ({0,number,integer} ms)");
  public static final TomMessage tomTypeCheckingPhase  =
      new TomMessage("TOM type checking phase ({0,number,integer} ms)");
  public static final TomMessage tomCompilationPhase   =
      new TomMessage("TOM compilation phase ({0,number,integer} ms)");
  public static final TomMessage tomOptimizationPhase  =
      new TomMessage("TOM optimization phase ({0,number,integer} ms)");
  public static final TomMessage tomVerificationPhase  =
      new TomMessage("TOM verification phase ({0,number,integer} ms)");
  public static final TomMessage tomGenerationPhase    =
      new TomMessage("TOM generation phase ({0,number,integer} ms)");

  // Main messages
  public static final TomMessage mainErrorMessage      =
    new TomMessage("{0}:{1,number,integer}:Error:{2}");
  public static final TomMessage mainWarningMessage    =
    new TomMessage("{0}:{1,number,integer}:Warning:{2}");

  // Tom.java
  public static final TomMessage iOException           =
      new TomMessage("IO Exception reading file `{0}`\n{1}");

  // Tom parser
  public static final TomMessage apigenClassNotFound     =
      new TomMessage("ClassNotFoundException: You need apigen to use the %vas construct");
  public static final TomMessage apigenInvocationIssue   =
      new TomMessage("Exception occurs while dealing with Apigen: {0}");

  // Error messages linked to operator and type definitions
  public static final TomMessage multipleSymbolDefinitionError=
      new TomMessage("Multiple definition of Symbol `{0}`");
  public static final TomMessage symbolCodomainError     =
      new TomMessage("Symbol `{0}` has an unknown return type: `{1}`");
  public static final TomMessage symbolDomainError       =
      new TomMessage("Slot {0,number,integer} of symbol `{1}` declaration has an unknown type: `{2}`");
  public static final TomMessage macroFunctionRepeated   =
      new TomMessage("Repeated macro-function `{0}`");
  public static final TomMessage nonLinearMacroFunction  =
      new TomMessage("Arguments must be linear in macro-function `{0}`: Variable `{1}` is repeated");
  public static final TomMessage missingMacroFunctions   =
      new TomMessage("Missing macro-function(s) [ {0} ]");
  public static final TomMessage badMakeDefinition       =
      new TomMessage("Bad number of arguments in 'make(...)' macro-function: {0,number,integer} arguments found but {1,number,integer} expected in symbol definition");
  public static final TomMessage listSymbolDomainError   =
      new TomMessage("List symbol `{0}` has an unknown parameter type: `{1}`");
  
  // Error messages linked to slot declaration during parsing
  public static final TomMessage badSlotName             =
      new TomMessage("Slot Name `{0}` is not correct for symbol `{1}`. Possible slot names are : {2}");
  public static final TomMessage unknownMatchArgumentTypeInSignature=
      new TomMessage("Variable `{0}` has an unknown type: `{1}`");
  public static final TomMessage repeatedMatchArgumentName=
      new TomMessage("Repeated variable `{0}` in `match` declaration");
  public static final TomMessage badMatchNumberArgument  =
      new TomMessage("Bad number of arguments: {0,number,integer} argument(s) required by match signature but {1,number,integer} found");
  public static final TomMessage incorrectVariableStarInMatch=
      new TomMessage("Single list variable `{0}` is not allowed on top of `match` pattern");
  public static final TomMessage wrongMatchArgumentTypeInPattern=
      new TomMessage("Wrong type for slot {0,number,integer}:Type `{1}` required but Type `{2}` found");
  public static final TomMessage unknownSymbol=
      new TomMessage("Unknown symbol `{0}`");
  public static final TomMessage unknownSymbolInDisjunction=
      new TomMessage("Unknown symbol `{0}` not allowed in disjunction");
  public static final TomMessage unknownUnamedList       =
      new TomMessage("Not able to found a list symbol of type: `{0}`");
  public static final TomMessage ambigousUnamedList      =
      new TomMessage("Too many list symbols with returned type `{0}`: {1}");
  public static final TomMessage invalidCodomain         =
      new TomMessage("The symbol `{0}` has type `{1}` but type `{2}` was required");
  public static final TomMessage invalidDisjunctionCodomain=
      new TomMessage("The symbol `{0}` has type `{1}` but type `{2}` was required in disjunction");
  public static final TomMessage invalidDisjunctionDomain=
      new TomMessage("The symbol `{0}` has a different domain as first symbol in disjunction");
  public static final TomMessage bracketOnListSymbol     =
      new TomMessage("[] are not allowed on lists or arrays, see `{0}`");
  public static final TomMessage slotRepeated=
      new TomMessage("Same slot names can not be used several times in symbol `{0}`: Repeated slot Name : `{1}`");
  public static final TomMessage symbolNumberArgument    =
      new TomMessage("Bad number of arguments for symbol `{0}`: {1,number,integer} argument(s) required but {2,number,integer} found");
  public static final TomMessage invalidVariableStarArgument=
    new TomMessage("{0} is not allowed in non list symbol ");
  public static final TomMessage ambigousSymbolWithoutConstructor=
      new TomMessage("{0} is a constructor and cannot be a variable. Add () to denote the constructor.");

  //strategy
  public static final TomMessage invalidStrategyName =
      new TomMessage("{0} is not a valid strategy name. Maybe it is already used as a symbol.");
  public static final TomMessage differentVisitorForward =
      new TomMessage("All visited sorts in same %strategy must have same visitorForward: `{0}` is different from `{1}`");
  public static final TomMessage noVisitorForward =
      new TomMessage("no visitorForward defined for type `{0}`");
  public static final TomMessage unknownVisitedType =
      new TomMessage("Visited Type `{0}` is unknown.");
  
  // rule
  public static final TomMessage incorrectRuleLHSClass   =
      new TomMessage("`{0}`: Impossible in rule left hand side");
  public static final TomMessage incorrectRuleRHSClass   =
      new TomMessage("`{0}`: Impossible in rule right hand side");
  public static final TomMessage differentRuleConstructor=
      new TomMessage("Rule head symbol name `{0}` expected, but `{1}` found");
  public static final TomMessage noRuleMakeDecl          =
      new TomMessage("Rule head symbol `{0}` has no `make` method: It is necessary to define one!!");
  public static final TomMessage incorrectRuleRHSType    =
      new TomMessage("Bad right hand side type: `{0}` instead of `{1}`");
  // Cant do further analyses
  public static final TomMessage unknownPermissiveSymbol =
      new TomMessage("Unknown symbol `{0}`: go through without analyses");
  public static final TomMessage impossiblePermissiveAndDisjunction=
      new TomMessage("Disjunction is not allowed after backquote (`) call");
  public static final TomMessage unknownVariable         =
      new TomMessage("Unknown variable(s) `{0}`");
  public static final TomMessage badVariableType         =
      new TomMessage("Variable `{0}` has type `{1}` instead of type `{2}`");
  public static final TomMessage incoherentVariable      =
      new TomMessage("Bad variable type for `{0}`: it has both type `{1}` and `{2}`");
  public static final TomMessage multipleRuleDefinition  =
      new TomMessage("Rule `{0}` is repeated");

  // when
  public static final TomMessage unknownVariableInWhen   =
      new TomMessage("`{0}` is not a variable and is not a constructor");

  public String toString() {
    return message;
  }


  // Message level
  public static final int TOM_INFO = 0;
  // Default error line
  public static final int DEFAULT_ERROR_LINE_NUMBER = 1; 
  
  public String getMessage() {
    return message;
  }
  
} // class TomMessage
