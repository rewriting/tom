/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

import tom.platform.BasicPlatformMessage;

import java.lang.reflect.*;
/**
 * The TomMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class TomMessage extends BasicPlatformMessage {
  private TomMessage(String message) {
    super(message);
  }

  /*
   * in a first step the TomMessage class is initialized (each fieldName field is set to null)
   * the initMessageName() method iterates over the static fields
   * and for each of them we set the slot "fieldName" of the corresponding TomMessage
   *
   * this method is called from a static block (end of file)
   */
  public static void initMessageName() {
    try {
      Field[] fields = java.lang.Class.forName("tom.engine.TomMessage").getDeclaredFields();
      for(Field f:fields) {
        int mod=f.getModifiers();
        if(Modifier.isStatic(mod)) {
          Object o = f.get(null);
          if(o instanceof TomMessage) {
            TomMessage msg = (TomMessage) o;
            msg.setMessageName(f.getName());
            //System.out.println(" --> " + msg.getMessageName());
          }
        }
      }
    } catch(java.lang.Exception e) {
      throw new tom.engine.exception.TomRuntimeException(e.getMessage());
    }
  }

  public static final TomMessage loggingInitializationFailure =
    new TomMessage("Error during Tom logging module initialization:{0}");

  public static final TomMessage simpleMessage         =
    new TomMessage("{0}:{1,number,integer}: {2}");
  public static final TomMessage exceptionMessage      =
    new TomMessage("{0}: Unhandled exception\n");
  public static final TomMessage taskErrorMessage      =
    new TomMessage("{0}: Encountered {1,number,integer} error(s) and {2,number,integer} warning(s)\nNo file generated.\n");
  public static final TomMessage taskWarningMessage    =
    new TomMessage("{0}: Encountered {1,number,integer} warning(s).\n");

  // TomOptionManager
  // Error messages
  public static final TomMessage optionNotFound        =
      new TomMessage("TomOptionManager: Option ''{0}'' not found");
  public static final TomMessage prerequisitesIssue    =
      new TomMessage("TomOptionManager: ''{0}'' can''t run : prerequisites are not met");
  public static final TomMessage incorrectOptionValue  =
      new TomMessage("TomOptionManager: Option ''{0}'' was expected with value ''{1}'', but ''{2}'' was found instead");
  public static final TomMessage outputTwice           =
      new TomMessage("TomOptionManager: Output filename specified twice");
  public static final TomMessage destdirTwice          =
      new TomMessage("TomOptionManager: Destination directory specified twice");
  public static final TomMessage invalidOption         =
      new TomMessage("TomOptionManager: ''{0}'' is not a valid option");
  public static final TomMessage incompleteOption      =
      new TomMessage("TomOptionManager: Expecting information after option ''{0}''");
  public static final TomMessage noFileToCompile       =
      new TomMessage("TomOptionManager: No file to compile");
  public static final TomMessage outputWithMultipleCompilation =
      new TomMessage("TomOptionManager: Cannot specify --output with multiple compilation inputs");
  public static final TomMessage notReturnedPluginOption =
    new TomMessage("TomOptionManager: getOptionFromName did not return a PluginOption");
  // Debug messages
  public static final TomMessage setValue              =
      new TomMessage("TomOptionManager: Set ''{0}'' to ''{1}'' (old value : ''{2}'')");
  // Warnings
  public static final TomMessage optimizerModifiesLineNumbers              =
    new TomMessage("The optimizer has activated the option pretty and line numbers are not preserved in the generated code." +
                " Please disable the optimizer if you need correct line numbers.");

  public static final TomMessage optimizerNotActive              =
    new TomMessage("The optimizer is not activated and thus WILL NOT RUN");

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
  public static final TomMessage codeExpected       =
    new TomMessage("{0}: A Code was expected by the plugin");

  // Plugings common
  public static final TomMessage invalidPluginArgument =
    new TomMessage("{0}.setArg expecting {1} but {2} argument");

  //TomStreamManager
  public static final TomMessage expectingOOptionWhenStdin =
    new TomMessage("Expecting use of \"-o file\" when using stdin");

  // parser.TomParserPlugin
  public static final TomMessage fileNotFound          =
      new TomMessage("File ''{0}'' not found");
  public static final TomMessage unamedTransformationRule =
      new TomMessage("A transformation rule has not been named in {0} transformation");
  public static final TomMessage tokenStreamException  =
      new TomMessage("TokenStreamException catched: {0}");
  public static final TomMessage recognitionException  =
      new TomMessage("RecognitionException catched: {0}");

  public static final TomMessage parserNotUsed =
    new TomMessage("The parser is not in use.");
  public static final TomMessage newParserNotUsed =
    new TomMessage("The new parser is not in use.");

  // parser.TomParser
  // TODO : simplify the message in using PlatformLogRecord with detail
  // As these messages are propagated via an exception in TomLanguage.g.t, it is not trivial
  public static final TomMessage malformedXMLTerm      =
      new TomMessage("In file ''{0}'' at line {1,number,integer}: In structure {2}, malformed XML pattern: expecting {3} but got {4}");
  public static final TomMessage malformedStrategy=
      new TomMessage("In file ''{0}'' at line {1,number,integer}: In structure {2}, malformed strategy: expecting {3} but got {4}");
  public static final TomMessage errorTwoSameSlotDecl=
    new TomMessage("{0}:{1,number,integer}: In structure {2} declared line {3,number,integer}, get_slot({4},...) is repeated.");
  public static final TomMessage errorIncompatibleSlotDecl =
    new TomMessage("{0}:{1,number,integer}: In structure {2} declared line {3,number,integer}, get_slot: {4} is not known");
  public static final TomMessage errorIncompatibleDefaultDecl =
    new TomMessage("{0}:{1,number,integer}: In structure {2} declared line {3,number,integer}, get_default: {4} is not known");
  public static final TomMessage slotIncompatibleTypes =
    new TomMessage("Slot ''{0}'' has sort {1} but has already been declared with sort {2}");
  // used with TomMessage to format exception messages
  public static final TomMessage missingIncludedFile   =
      new TomMessage("Missing file name in %include structure in file ''{0}'' at line {1,number,integer}");
  public static final TomMessage missingIncludeSL   =
      new TomMessage("Missing %include { sl.tom }");
  public static final TomMessage includedFileNotFound  =
      new TomMessage("Included file ''{0}'' not found in file {1} at line {2,number,integer}");
  public static final TomMessage includedFileCycle     =
      new TomMessage("Included file ''{0}'' found at line {1,number,integer} in file ''{2}'' forms a cycle");
  public static final TomMessage includedFileAlreadyParsed=
      new TomMessage("Included file ''{0}'' has already been parsed");
  public static final TomMessage errorWhileIncludingFile=
      new TomMessage("Exception ''{0}'' occurs in parsing file ''{1}'' included in file {2} at line {3,number,integer}: See stacktrace\n\n{4}");
//TODO
  public static final TomMessage mmFileNotFound  =
      new TomMessage("{0} metamodel file ''{1}'' not found in file {2} at line {3,number,integer}");
//
  public static final TomMessage gomFailure =
      new TomMessage("An error occured dealing with %gom input in ''{0}'' at line {1,number,integer}");
  public static final TomMessage gomInitFailure =
      new TomMessage("An error occured initializing gom in ''{0}'' at line {1,number,integer}: {2}");
  public static final TomMessage emptyGomSignature =
      new TomMessage("A %gom block is empty");
  public static final TomMessage vasPlatformFailure    =
      new TomMessage("Fail to obtain a Vas PluginPlatform in ''{0}'' at line {1,number,integer}");
  public static final TomMessage vasFailure            =
      new TomMessage("An error occured dealing with vas input in ''{0}'' at line {1,number,integer}");
  public static final TomMessage iOExceptionWithGeneratedTomFile=
      new TomMessage("Generated file ''{0}'' from vas in file ''{1}'' caused an IOException: {2}");
  public static final TomMessage exceptionWithGeneratedTomFile =
      new TomMessage("Generated file ''{0}'' from vas in file ''{1}'' caused an IOException: See stackTrace\n\n{2}");
  public static final TomMessage repeatedSlotName      =
      new TomMessage("Repeated slot ''{0}'' in symbol declaration");
  public static final TomMessage braceExpected         =
      new TomMessage("'')'' expected line {0,number,integer}: EOF encountered ");
  public static final TomMessage invalidBackQuoteTerm  =
      new TomMessage("Invalid backquote term started line #{0,number,integer}: EOF encountered");
  public static final TomMessage badNumberOfAt  =
      new TomMessage("Bad number of '@' in %[...]%");

  public static final TomMessage backendInactivated    =
      new TomMessage("The BackendPlugin is not activated and thus WILL NOT RUN:No output will be generated ");
  public static final TomMessage backendIOException    =
      new TomMessage("BackendPlugin : IOException occurs writting to file ''{0}'': {1} ");
  public static final TomMessage deprecatedDisjunction =
      new TomMessage("Disjunction of patterns is deprecated and may be removed in a future version. Try to use disjunction of symbols instead ");
  public static final TomMessage invalidConstraintType =
      new TomMessage("Invalid Constraint type ");
  public static final TomMessage typetermNotDefined =
      new TomMessage("%typeterm is not defined for {0}");

  // checker.TomChecker
  public static final TomMessage findOTL               =
    new TomMessage("{0}: findOriginTrackingLine : not found (TomChecker)");
  public static final TomMessage syntaxCheckerInactivated=
      new TomMessage("The syntax checker is not activated and thus WILL NOT RUN.");
  public static final TomMessage typeCheckerInactivated=
      new TomMessage("The type checker is not activated and thus WILL NOT RUN.");

  public static final TomMessage freshVariableIssue    =
      new TomMessage("Expecting a fresh variable in WHERE condition left hand side instead of ''{0}''");
  public static final TomMessage declaredVariableIssueInIf=
      new TomMessage("Expecting an already declared variable in IF condition instead of ''{0}''");
  public static final TomMessage declaredVariableIssueInWhere=
      new TomMessage("Expecting an already declared variable in WHERE condition instead of ''{0}''");
  public static final TomMessage unknownRuleRhsVariable=
      new TomMessage("Rule right hand side uses an undeclared variable ''{0}''");

  public static final TomMessage illegalAnnotationInAntiPattern =
      new TomMessage("It is forbidden to make annotations under an anti symbol");
  public static final TomMessage freeVarNotPresentInOr =
    new TomMessage("Any variable used in the action should appear in all the members of a disjunction. Variable ''{0}'' not found in all disjunctions");

  // compiler
  public static final TomMessage errorMissingSlotDecl =
      new TomMessage("In symbol ''{0}'', get_slot for {1} is missing.");
  public static final TomMessage noCodeGeneration =
      new TomMessage("The match with ''{0}'' is always unsuccessful. No code for the correspondent action will be generated");
  public static final TomMessage circularReferences =
      new TomMessage("A circular reference was detected for the variable ''{0}''. For instance x << Context[x] is a circular reference.");
  public static final TomMessage cannotCompileACPattern =
      new TomMessage("Cannot compile this AC pattern: ''{0}''. There should be at least one linear variable");

  // optimizer.TomOptimizer
  public static final TomMessage tomOptimizationType =
    new TomMessage("optimization of type ''{0}''");
  public static final TomMessage unusedVariable        =
    new TomMessage("Variable ''{0}'' is never used");
  public static final TomMessage remove                =
    new TomMessage("{0} -> remove:     {1}");
  public static final TomMessage inline                =
    new TomMessage("{0} -> inline:     {1}");
  public static final TomMessage noInline              =
    new TomMessage("{0} -> no inline:  {1}");
  public static final TomMessage doNothing             =
    new TomMessage("{0} -> do nothing: {1}");
  public static final TomMessage cannotInline              =
    new TomMessage("    -> cannot inline {1} (modified value)");

  // verifier
  public static final TomMessage verifierInactivated   =
      new TomMessage("The verifier is not activated and thus WILL NOT RUN.");
  public static final TomMessage verifierNotCompatibleWithOptimize   =
      new TomMessage("The verification cannot be performed when optimizing code with level>=2");

  // verbose messages
  public static final TomMessage tomExec =
      new TomMessage("TOM exec:  {0}");
  public static final TomMessage tomParsingPhase =
      new TomMessage("TOM parsing phase ({0,number,integer} ms)");
  public static final TomMessage tomSyntaxCheckingPhase =
      new TomMessage("TOM syntax checking phase ({0,number,integer} ms)");
  public static final TomMessage tomTransformingPhase =
      new TomMessage("TOM transforming phase ({0,number,integer} ms)");
  public static final TomMessage tomDesugaringPhase =
      new TomMessage("TOM desugaring phase ({0,number,integer} ms)");
  public static final TomMessage tomTypingPhase =
      new TomMessage("TOM typing phase ({0,number,integer} ms)");
  public static final TomMessage tomTypingPhaseComplement =
      new TomMessage("TOM typing phase ({0} constraint(s) and {1} fresh type variable(s))");
  public static final TomMessage tomFormattingPhase =
      new TomMessage("TOM formatting phase ({0,number,integer} ms)");
  public static final TomMessage tomExpandingPhase =
      new TomMessage("TOM expanding phase ({0,number,integer} ms)");
  public static final TomMessage tomTypeCheckingPhase =
      new TomMessage("TOM type checking phase ({0,number,integer} ms)");
  public static final TomMessage tomCompilationPhase =
      new TomMessage("TOM compilation phase ({0,number,integer} ms)");
  public static final TomMessage tomOptimizationPhase =
      new TomMessage("TOM optimization phase ({0,number,integer} ms)");
  public static final TomMessage tomVerificationPhase =
      new TomMessage("TOM verification phase ({0,number,integer} ms)");
  public static final TomMessage tomGenerationPhase =
      new TomMessage("TOM generation phase ({0,number,integer} ms)");

  // Main messages
  public static final TomMessage mainErrorMessage      =
    new TomMessage("{0}:{1,number,integer}:Error:{2}");
  public static final TomMessage mainWarningMessage    =
    new TomMessage("{0}:{1,number,integer}:Warning:{2}");

  // Tom.java
  public static final TomMessage iOException           =
      new TomMessage("IO Exception reading file ''{0}''\n{1}");

  // Tom parser
  public static final TomMessage apigenClassNotFound     =
      new TomMessage("ClassNotFoundException: You need apigen to use the %vas construct");
  public static final TomMessage apigenInvocationIssue   =
      new TomMessage("Exception occurs while dealing with Apigen: ''{0}''");
  public static final TomMessage multipleUpperTypes =
      new TomMessage("Type ''{0}'' cannot have two upper types simultaneously: ''{1}'' and ''{2}''");

  // Error messages linked to operator and type definitions
  public static final TomMessage multipleSortDefinitionError=
      new TomMessage("Multiple definition of Sort ''{0}''");
  public static final TomMessage multipleSymbolDefinitionError=
      new TomMessage("Multiple definition of Symbol ''{0}''");
  public static final TomMessage symbolCodomainError     =
      new TomMessage("Symbol ''{0}'' has an unknown return type: ''{1}''");
  public static final TomMessage symbolDomainError       =
      new TomMessage("Slot {0,number,integer} of symbol ''{1}'' declaration has an unknown type: ''{2}''");
  public static final TomMessage macroFunctionRepeated   =
      new TomMessage("Repeated macro-function ''{0}''");
  public static final TomMessage nonLinearMacroFunction  =
      new TomMessage("Arguments must be linear in macro-function ''{0}'': Variable ''{1}'' is repeated");
  public static final TomMessage missingMacroFunctions   =
      new TomMessage("Missing macro-function(s) [ ''{0}'' ]");
  public static final TomMessage badMakeDefinition       =
      new TomMessage("Bad number of arguments in ''make(...)'' macro-function: {0,number,integer} arguments found but {1,number,integer} expected in symbol definition");
  public static final TomMessage listSymbolDomainError   =
      new TomMessage("List symbol ''{0}'' has an unknown parameter type: ''{1}''");
  public static final TomMessage termOrVariableNumericLeft   =
    new TomMessage("The left hand side of a boolean constraint should be a variable or a term (implicit notation with ''[]'' is forbidden): ''{0}''");
  public static final TomMessage termOrVariableNumericRight   =
    new TomMessage("The right hand side of a boolean constraint should be a variable or a term (implicit notation with ''[]'' is forbidden): ''{0}''");
  public static final TomMessage invalidTypesNumeric   =
    new TomMessage("Found type ''{0}'' for symbol ''{1}'' and ''{2}'' for symbol ''{3}''. The two symbols should have the same type.");
  public static final TomMessage inconsistentTypes   =
    new TomMessage("The symbol ''{0}'' already had type ''{1}''. Type ''{2}'' is invalid");
  public static final TomMessage forbiddenAntiTermInNumeric   =
    new TomMessage("Anti-patterns are forbidden in boolean constraints: ''{0}''");
  public static final TomMessage forbiddenAnonymousInNumeric   =
    new TomMessage("Anonymous symbols are forbidden in boolean constraints: ''{0}''");
  public static final TomMessage forbiddenImplicitNumeric   =
    new TomMessage("Implicit notation with ''[]'' is forbidden in boolean constraints: ''{0}''");
  public static final TomMessage forbiddenAnnotationsNumeric   =
    new TomMessage("Annotations are forbidden in boolean constraints: ''{0}''");

  // Error messages linked to slot declaration during parsing
  public static final TomMessage badSlotName             =
      new TomMessage("Slot Name ''{0}'' is not correct for symbol ''{1}''. Possible slot names are : {2}");
  public static final TomMessage unknownMatchArgumentTypeInSignature=
      new TomMessage("''{0}'' has an unknown type: ''{1}''");
  public static final TomMessage repeatedMatchArgumentName=
      new TomMessage("Repeated variable ''{0}'' in ''match'' declaration");
  public static final TomMessage invalidMatchSubject=
      new TomMessage("Type ''{0}'' and Subject ''{1}'' are not correct");
  public static final TomMessage cannotGuessMatchType=
      new TomMessage("Cannot guess the type of ''{0}'', use at least one constructor or specify the type");
  public static final TomMessage badMatchNumberArgument  =
      new TomMessage("Bad number of arguments: {0,number,integer} argument(s) required by match signature but {1,number,integer} found");
  public static final TomMessage incorrectVariableStarInMatch=
      new TomMessage("Single list variable ''{0}'' is not allowed if not under a list operator");
  public static final TomMessage wrongMatchArgumentTypeInPattern=
      new TomMessage("Wrong type for slot {0,number,integer}:Type ''{1}'' required but Type ''{2}'' found");
  public static final TomMessage unknownSymbolInDisjunction=
      new TomMessage("Unknown symbol ''{0}'' not allowed in disjunction");
  public static final TomMessage unknownUnamedList       =
      new TomMessage("Not able to found a list symbol of type: ''{0}''");
  public static final TomMessage ambigousUnamedList      =
      new TomMessage("Too many list symbols with returned type ''{0}'': {1}");
  public static final TomMessage invalidCodomain         =
      new TomMessage("The symbol ''{0}'' has type ''{1}'' but type ''{2}'' was required");
  public static final TomMessage invalidDisjunctionCodomain=
      new TomMessage("The symbol ''{0}'' has type ''{1}'' but type ''{2}'' was required in disjunction");
  public static final TomMessage invalidDisjunctionDomain=
      new TomMessage("The symbols ''{0}'' and ''{1}'' do not have the same domain");
  public static final TomMessage invalidDisjunctionSlotName =
      new TomMessage("The symbol ''{0}'' does not have a slot named ''{1}''");
  public static final TomMessage bracketOnListSymbol     =
      new TomMessage("[] are not allowed on lists or arrays, see ''{0}''");
  public static final TomMessage slotRepeated=
      new TomMessage("Same slot names can not be used several times in symbol ''{0}'': Repeated slot Name : ''{1}''");
  public static final TomMessage invalidVariableStarArgument=
    new TomMessage("{0} is not allowed in non list symbol ");
  public static final TomMessage ambigousSymbolWithoutConstructor=
      new TomMessage("{0} is a constructor and cannot be a variable. Add () to denote the constructor.");
  public static final TomMessage IsSortNotDefined =
      new TomMessage("IsSort(t) is not defined for {0}");

  //typer.NewKernelTyper
  public static final TomMessage incompatibleTypes   =
    new TomMessage("Incompatible types ''{0}'' and ''{1}'' for symbol ''{2}''.");
  public static final TomMessage unknownSymbol=
      new TomMessage("Unknown symbol ''{0}''");
  public static final TomMessage unknownType=
      new TomMessage("Unknown type ''{0}''");
  public static final TomMessage symbolNumberArgument    =
      new TomMessage("Bad number of arguments for symbol ''{0}'': {1,number,integer} argument(s) required but {2,number,integer} found");

  //strategy
  public static final TomMessage invalidStrategyName =
      new TomMessage("{0} is not a valid strategy name. Maybe it is already used as a symbol.");
  public static final TomMessage differentVisitorForward =
      new TomMessage("All visited sorts in same %strategy must have same visitorForward: ''{0}'' is different from ''{1}''");
  public static final TomMessage noVisitorForward =
      new TomMessage("no visitorForward defined for type ''{0}''");
  public static final TomMessage unknownVisitedType =
      new TomMessage("Visited Type ''{0}'' is unknown.");
  public static final TomMessage emptyStrategy =
      new TomMessage("empty %strategy construct : at least one \"visit\" should be declared");

  //transformation
  public static final TomMessage emptyTransformation =
      new TomMessage("empty %transformation construct : at least one \"<with> -> <to>\" should be declared");
  public static final TomMessage invalidTransformationName =
      new TomMessage("{0} is not a valid transformation name. Maybe it is already used as a symbol.");

  // rule
  public static final TomMessage incorrectRuleLHSClass   =
      new TomMessage("''{0}'': Impossible in rule left hand side");
  public static final TomMessage incorrectRuleRHSClass   =
      new TomMessage("''{0}'': Impossible in rule right hand side");
  public static final TomMessage differentRuleConstructor=
      new TomMessage("Rule head symbol name ''{0}'' expected, but ''{1}'' found");
  public static final TomMessage noRuleMakeDecl          =
      new TomMessage("Rule head symbol ''{0}'' has no ''make'' method: It is necessary to define one!!");
  public static final TomMessage incorrectRuleRHSType    =
      new TomMessage("Bad right hand side type: ''{0}'' instead of ''{1}''");
  // Cant do further analyses
  public static final TomMessage unknownPermissiveSymbol =
      new TomMessage("Unknown symbol ''{0}'': go through without analyses");
  public static final TomMessage impossiblePermissiveAndDisjunction=
      new TomMessage("Disjunction is not allowed after backquote ('') call");
  public static final TomMessage unknownVariable         =
      new TomMessage("Unknown variable(s) ''{0}''");
  public static final TomMessage badVariableType         =
      new TomMessage("Variable ''{0}'' has type ''{1}'' instead of type ''{2}''");
  public static final TomMessage incoherentVariable      =
      new TomMessage("Bad variable type for ''{0}'': it has both type ''{1}'' and ''{2}''");
  public static final TomMessage incoherentVariableStar =
      new TomMessage("Variable ''{0}*'' is rooted by ''{1}'' and cannot be put under a ''{2}''");
  public static final TomMessage multipleRuleDefinition  =
      new TomMessage("Rule ''{0}'' is repeated");

  // when
  public static final TomMessage unknownVariableInWhen   =
      new TomMessage("''{0}'' is not a variable and is not a constructor");

  public static final TomMessage ioExceptionTempGom=
      new TomMessage("IO Exception when creating gom temp file: ''{0}''");

  public static final TomMessage writingExceptionTempGom=
      new TomMessage("Writing temp file for gom: ''{0}''");

  public static final TomMessage writingFailureTempGom=
      new TomMessage("Failed writing gom temp file: ''{0}''");
  
  public static final TomMessage typerNotUsed =
      new TomMessage("The default typer is not in use");
  public static final TomMessage newTyperNotUsed =
    new TomMessage("The new typer is not in use.");

  /*
   * FINER
   */
  public static final TomMessage failGetCanonicalPath =
      new TomMessage("Failed to get canonical path for ''{0}''");

  // Message level
  public static final int TOM_INFO = 0;

  /*
   * static block: should stay at the end of the file  (after the initialization of static fields)
   */
  static {
    TomMessage.initMessageName();
  }
}
