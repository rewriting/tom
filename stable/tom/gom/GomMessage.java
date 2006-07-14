/*
 * Gom
 * 
 * Copyright (c) 2005-2006, INRIA
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

import tom.platform.PlatformMessage;

/**
 * The GomMessage class is a container for error messages, using the
 * typesafe enum pattern
 */

public class GomMessage implements PlatformMessage {
  private final String message;

  private GomMessage(String message) {
    this.message = message;
  }

  public static final GomMessage loggingInitializationFailure =
    new GomMessage("Error during Gom logging module initialization:{0}");

  public static final GomMessage simpleMessage =
    new GomMessage("{0}:{1,number,integer}: {2}");
  public static final GomMessage exceptionMessage =
    new GomMessage("{0}: Unhandled exception occurs with input {1}: See stacktrace\n+++++++++++++++++++++++++\n{2}+++++++++++++++++++++++++");

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
    new GomMessage("no slot declaration");
  public static final GomMessage deprecatedSyntax =
    new GomMessage("The Vas compatibility syntax is deprecated, "+
        "and should be converted to plain Gom syntax");

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

  // GomExpander
  public static final GomMessage moduleNotFound =
    new GomMessage("Module {0} not found. Breaking expansion phase");
  public static final GomMessage expansionIssue =
    new GomMessage("Problems encountered expanding module {0}");

  // GomTypeExpander
  public static final GomMessage orphanedHook =
    new GomMessage("The operator {0} is not declared, but used for a hook");
  public static final GomMessage mismatchedMakeArguments =
    new GomMessage("Arguments list {0} does not match expected arguments {1}");
  public static final GomMessage discardedHook =
    new GomMessage("Hook for operator {0} discarded");
  public static final GomMessage unsupportedHookAlgebraic =
    new GomMessage("Hook type {0} not supported for non algebraic operator");
  public static final GomMessage badMakeInsertArguments =
    new GomMessage("Bad arguments for make_insert: expecting 2, but got {0}");
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
    new GomMessage("Incompatible types for slot {0}: {1}, {2}. Two slots with the same name in the same sort should have same type");

  // GomCompiler
  public static final GomMessage compilationIssue =
    new GomMessage("Problems encountered compiling module {0}");

  // GomBackend
  public static final GomMessage generationIssue =
    new GomMessage("Problems encountered in code generation for module {0}");
  public static final GomMessage tomFailure =
    new GomMessage("An error occured running Tom when generating {0}");

  public String toString() {
    return message;
  }

  // Message level
  public static final int GOM_INFO = 0;
  // Default error line
  public static final int DEFAULT_ERROR_LINE_NUMBER = 1;

  public String getMessage() {
    return message;
  }
}
