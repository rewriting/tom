/*
 * Gom
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom;

import tom.platform.BasicPlatformMessage;

/**
 * The GomMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class GomMessage extends BasicPlatformMessage {

  private GomMessage(String message) {
    super(message);
  }

  public static final GomMessage loggingInitializationFailure =
    new GomMessage("Error during Gom logging module initialization:{0}");

  public static final GomMessage simpleMessage =
    new GomMessage("{0}:{1,number,integer}: {2}");
  public static final GomMessage exceptionMessage =
    new GomMessage("{0}: Unhandled exception occurs in class {1}: See stacktrace\n+++++++++++++++++++++++++\n{2}+++++++++++++++++++++++++");

  // GomOptionManager error messages
  public static final GomMessage optionNotFound =
    new GomMessage("GomOptionManager: Option `{0}` not found");
  public static final GomMessage prerequisitesIssue =
    new GomMessage("GomOptionManager: {0} can't run : prerequisites not met");
  public static final GomMessage noFileToCompile =
    new GomMessage("GomOptionManager: No file to compile");
  public static final GomMessage incompleteOption =
    new GomMessage("GomOptionManager: Expecting information after option `{0}`");
  public static final GomMessage incorrectOptionValue =
    new GomMessage("GomOptionManager: Option `{0}` was expected with value `{1}`, but `{2}` was found instead");
  public static final GomMessage destdirTwice =
    new GomMessage("GomOptionManager: Destination directory specified twice");
  public static final GomMessage invalidOption =
    new GomMessage("GomOptionManager: `{0}` is not a valid option");

  public static final GomMessage setValue =
    new GomMessage("GomOptionManager: Set `{0}` to `{1}` (old value : `{2}`)");
  // GomPluginFactory
  public static final GomMessage classNotFound =
    new GomMessage("{0}: Class {1} not found");
  public static final GomMessage instantiationError =
    new GomMessage("{0}: An error occured during the instantiation of class {1}");

  // Plugins
  public static final GomMessage invalidPluginArgument =
    new GomMessage("{0}.setArg expecting {1} but got {2} as argument");

  // parser.GomParserPlugin
  public static final GomMessage fileNotFound =
    new GomMessage("File {0} not found");
  public static final GomMessage tokenStreamException  =
    new GomMessage("TokenStreamException catched: See stacktrace\n\n{0}");
  public static final GomMessage recognitionException  =
    new GomMessage("RecognitionException catched: See stacktrace\n\n{0}");
  // used with GomMessage to format exception messages
  public static final GomMessage missingIncludedFile   =
    new GomMessage("Missing file name in %include structure in file {0} at line {1,number,integer}");
  public static final GomMessage includedFileNotFound  =
    new GomMessage("Included file `{0}` not found in file {1} at line {2,number,integer}");
  public static final GomMessage includedFileCycle     =
    new GomMessage("Included file `{0}` found at line {1,number,integer} in file `{2}` forms a cycle");
  public static final GomMessage errorWhileIncludingFile=
    new GomMessage("Exception {0} occurs in parsing file `{1}` included in file {2} at line {3,number,integer}: See stacktrace\n\n{4}");
  public static final GomMessage detailedParseException =
    new GomMessage("Parse problem \"{0}\"");
  public static final GomMessage unknownHookKind =
    new GomMessage("Unknown hook kind \"{0}\"");
  public static final GomMessage noSlotDeclaration =
    new GomMessage("No slot declaration");
  public static final GomMessage deprecatedSyntax =
    new GomMessage("The Vas compatibility syntax is deprecated, "+
        "and should be converted to plain Gom syntax");
  public static final GomMessage unableToCloseReaderMessage =
    new GomMessage("An error occured when closing reader");
  public static final GomMessage unableToUseReaderMessage =
    new GomMessage("An error occured when initializing reader");
  public static final GomMessage parsedModules =
    new GomMessage("Parsed Module:\n{0}");
  public static final GomMessage parsingPhase =
    new GomMessage("GOM Parsing phase ({0} ms)");


  // verbose messages
  public static final GomMessage gomParsingPhase =
    new GomMessage("Gom parsing phase ({0,number,integer} ms)");

  // Main messages
  public static final GomMessage mainErrorMessage =
    new GomMessage("{0}:{1,number,integer}:Error:{2}");
  public static final GomMessage mainWarningMessage =
    new GomMessage("{0}:{1,number,integer}:Warning:{2}");

  public static final GomMessage iOException =
    new GomMessage("IO Exception reading file `{0}`\n{1}");
  public static final GomMessage iOExceptionManipulation =
    new GomMessage("IOExceptionManipulation");
  public static final GomMessage iOExceptionManipulationInputReader =
    new GomMessage("getInputReader:IOExceptionManipulation {0} : {1}");
  public static final GomMessage iOExceptionManipulationInputParent =
    new GomMessage("getInputParent:IOExceptionManipulation {0} : {1}");

  //Gom Antlr Adapter
  public static final GomMessage gomAntlrAdapterGenerationPhase =
    new GomMessage("GOM Antlr Adapter generation phase ({0} ms)");

  // GomExpander
  public static final GomMessage moduleNotFound =
    new GomMessage("Module {0} not found. Breaking expansion phase");
  public static final GomMessage expansionIssue =
    new GomMessage("Problems encountered expanding module {0}");
  public static final GomMessage hookExpansionIssue =
    new GomMessage("Problems encountered expanding hooks for module {0}");
  public static final GomMessage moduleToAnalyse =
    new GomMessage("GomExpander:moduleToAnalyse {0}");
  public static final GomMessage fileSeeking =
    new GomMessage("Seeking for file {0}");
  public static final GomMessage gomExpansionPhase = 
    new GomMessage("GOM Expansion phase ({0} ms)");
  public static final GomMessage viewerToTreeFailure = 
    new GomMessage("Viewer.toTree failed {0}");
  public static final GomMessage importedModules = 
    new GomMessage("Imported Modules:\n{0}");

  // GomTypeExpander
  public static final GomMessage operatorOnBuiltin =
    new GomMessage("Builtin type {0} cannot have operators");
  public static final GomMessage orphanedHook =
    new GomMessage("The operator {0} is not declared, but used for a hook");
  public static final GomMessage mismatchedMakeArguments =
    new GomMessage("Arguments list {0} does not match expected arguments {1}");
  public static final GomMessage discardedHook =
    new GomMessage("Hook for operator {0} discarded");
  public static final GomMessage unsupportedHookAlgebraic =
    new GomMessage("Hook type {0} not supported for non algebraic operator");
  public static final GomMessage badHookArguments =
    new GomMessage("Bad arguments for {0}: expecting 2, but got {1}");
  public static final GomMessage unsupportedHookVariadic =
    new GomMessage("Hook type {0} not supported for non variadic operator");
  public static final GomMessage undeclaredSorts =
    new GomMessage("Some sorts were not declared: {0}");
  public static final GomMessage emptySorts =
    new GomMessage("Some sorts are empty: {0}");
  public static final GomMessage malformedProduction =
    new GomMessage("Ill formed production, invalid fields: {0}");
  public static final GomMessage unknownSort =
    new GomMessage("Sort {0} not found: missing include ?");
  public static final GomMessage slotIncompatibleTypes =
    new GomMessage("Incompatible slot types in sort {0}: slot {1} has sort {2} and {3}. Two slots with the same name in the same sort should have same type");
  public static final GomMessage typedModules =
    new GomMessage("Typed Modules:\n{0}");
  public static final GomMessage gomTypeExpansionPhase = 
    new GomMessage("GOM Type Expansion phase ({0} ms)");
  public static final GomMessage typedHooks =
    new GomMessage("Typed Hooks:\n{0}");
  public static final GomMessage gomHookExpansionPhase = 
    new GomMessage("GOM Hook Expansion phase ({0} ms)");

  //FreshExpander
  public static final GomMessage gomFreshGomExpansionPhase = 
    new GomMessage("GOM Expansion of freshgom phase ({0} ms)");
  public static final GomMessage freshExpandedModules = 
    new GomMessage("Fresh expanded Modules:\n{0}");

  //GraphExpander
  public static final GomMessage signatureExtension = 
    new GomMessage("Extend the signature");
  public static final GomMessage referencedModules = 
    new GomMessage("Referenced Modules: {0}");
  public static final GomMessage signatureExtensionSuccess = 
    new GomMessage("Signature extension succeeds");

  // RuleExpander
  public static final GomMessage variadicRuleStartingWithStar =
    new GomMessage("Rule for variadic operator {0} should not have list variable {1} in head position");
  public static final GomMessage discardRuleWarning =
    new GomMessage("Discard rule \"{0}\"");
  public static final GomMessage multipleRulesForEmpty =
    new GomMessage("Multiple rules for empty {0}");
  public static final GomMessage unknownConstructor =
    new GomMessage("Unknown constructor {0}");

  //HookTypeExpander
  public static final GomMessage graphRulesHooksAuthOnSorts  =
    new GomMessage("Graphrules hooks are authorised only on sorts");
  public static final GomMessage moduleHooksAuthOnCurrentModule =
    new GomMessage("Hooks on module are authorised only on the current module");
  public static final GomMessage mustSpecifyAssociatedTheoryForVarOp =
    new GomMessage("As you use make_insert, make_empty or rules, specify the associated theory for the variadic operator {0}");
  public static final GomMessage differentDomainCodomain =
    new GomMessage("Different domain and codomain");
  public static final GomMessage hookFLAUACACUOnlyOnVarOp =
    new GomMessage("FL/AU/AC/ACU hook can only be used on a variadic operator");
  public static final GomMessage graphrulHookAuthorizedStrat =
    new GomMessage("In graphrules hooks, the default strategies authorized are only Fail and Identity");
  public static final GomMessage neutralElmtDefNotAllowed =
    new GomMessage("FL hook does not allow the definition of a neutral element");

  //GraphRuleExpander
  public static final GomMessage rulesParsingFailure =
    new GomMessage("Cannot parse rules");

  // GomCompiler
  public static final GomMessage compilationIssue =
    new GomMessage("Problems encountered compiling module {0}");

  // GomBackend
  public static final GomMessage generationIssue =
    new GomMessage("Problems encountered in code generation for module {0}");
  public static final GomMessage tomFailure =
    new GomMessage("An error occured running Tom when generating {0}");
  public static final GomMessage gomChoiceWarning =
    new GomMessage("There were many possibilities ({0}) in {1} but the first one was chosen : {2}");
  public static final GomMessage gomGenerationPhase = 
    new GomMessage("GOM generation phase ({0} ms)");
  public static final GomMessage impossibleToDeleteTmpFile = 
    new GomMessage("Could not delete temporary file {0}");
  public static final GomMessage tomCodeGenerationFailure = 
    new GomMessage("Failed to generate Tom code: {0}");
  public static final GomMessage importListComputationFailure = 
    new GomMessage("Failed compute import list: {0}");
  public static final GomMessage codeGenerationFailure = 
    new GomMessage("Failed to generate code for {0}");
  public static final GomMessage expectingVariadicButGot = 
    new GomMessage("{Empty,Cons}: expecting variadic, but got {0}");

  // SymbolTable
  public static final GomMessage nonExhaustiveMatch =
    new GomMessage("Non exhaustive match");
  public static final GomMessage nonExhaustivePattern =
    new GomMessage("Non exhaustive pattern");
  public static final GomMessage shouldNeverHappen =
    new GomMessage("Should never happen ({0}:{1}})");
  public static final GomMessage nonVariadicOperator =
    new GomMessage("Non variadic operator");
  public static final GomMessage cannotAccessToChildConstructor =
    new GomMessage("Cannot access to the {0}ith child of the constuctor {1}");
  public static final GomMessage undeclaredSortException =
    new GomMessage("The sort {0} is not declared");
  public static final GomMessage undeclaredConstructorException =
    new GomMessage("The constructor {0} is not declared");
  public static final GomMessage sortException =
    new GomMessage("Sort exception : {0}");
  public static final GomMessage constructorName =
    new GomMessage("Constructor exception : {0}");

  //FINE & FINER
  public static final GomMessage systemInAsInput = 
    new GomMessage("Gom will use System.in as input");
  public static final GomMessage processingArgument = 
    new GomMessage("GomOptionManager: processing argument {0} \"{1}\"");
  public static final GomMessage compiledModules = 
    new GomMessage("Compiled Modules:\n{0}");
  public static final GomMessage gomCompilationPhase = 
    new GomMessage("GOM Compilation phase ({0} ms)");
  public static final GomMessage getCanonicalPathFailure = 
    new GomMessage("Failed to get canonical path for {0}");

  // Message level
  public static final int GOM_INFO = 0;
}
