/*
 * 
 * TOM - To One Matching Expander
 * 
 * Copyright (c) 2000-2010, INPL, INRIA
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

package tom.engine.prettyprinter;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomsignature.types.tomsymbollist.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.engine.adt.tominstruction.types.constraintinstructionlist.concConstraintInstruction;
import tom.engine.adt.tomslot.types.slotlist.concSlot;
import tom.engine.adt.tomsignature.types.tomvisitlist.concTomVisit;
import tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass;
import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;

import tom.library.sl.*;

/**
 * The PrettyPrinter plugin.
 */
public class PrettyPrinterPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}
  %include { string.tom }
  %typeterm PrettyPrinterPlugin { implement { PrettyPrinterPlugin } }

  private static Logger logger = Logger.getLogger("tom.engine.prettyprinter.PrettyPrinterPlugin");
  /** some output suffixes */
  public static final String PRETTYPRINTER_SUFFIX = ".pretty";

  /** the declared options string */
  public static final String DECLARED_OPTIONS = 
    "<options>" +
    "<boolean name='prettyTOM' altName='pit' description='Pretty print original Tom code (not activated by default)' value='false'/>" +
    "</options>";

  /** Constructor */
  public PrettyPrinterPlugin() {
    super("PrettyPrinterPlugin");
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean prettyTOM = getOptionBooleanValue("prettyTOM");    
    try {
      //reinit the variable for intropsector generation
      Code code = (Code) getWorkingTerm();

      if(prettyTOM) {
	prettyPrinter(code);
      }

      // verbose
      TomMessage.info(logger,null,0,TomMessage.tomExpandingPhase, Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
      setWorkingTerm(code);
    } catch(Exception e) {
      TomMessage.error(logger,getStreamManager().getInputFileName(),0,TomMessage.exceptionMessage, e.getMessage());
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(PrettyPrinterPlugin.DECLARED_OPTIONS);
  }

  private void prettyPrinter(Code code) throws IOException {
    System.out.println("PrettyPrinter active");
    generate(0,code,"");
  }
  
  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  // ------------------------------------------------------------
  
  static void generateBQTerm(int deep, BQTerm subject, String moduleName) throws IOException {
    System.out.println("In generateBQTerm");
    %match (subject) {
      BQAppl(anOptionList, aTomName, aBQTermList) -> {
        return;
      }
      BuildConstant[AstName=Name(name)] -> {
      /*  if(`name.charAt(0)=='\'' && `name.charAt(`name.length()-1)=='\'') {
          String substring = `name.substring(1,`name.length()-1);
          //System.out.println("BuildConstant: " + substring);
          substring = substring.replace("\\","\\\\"); // replace backslash by backslash-backslash
          substring = substring.replace("'","\\'"); // replace quote by backslash-quote
          output.write("'" + substring + "'");
          return;
        }
        output.write(`name);*/
	System.out.println(`name);
        return;
      }

      BuildTerm(Name(name), argList, myModuleName) -> {
     //   `buildTerm(deep, name, argList, myModuleName);
        return;
      }

      l@(BuildEmptyList|BuildEmptyArray|BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
      //  buildListOrArray(deep, `l, moduleName);
        return;
      }

      FunctionCall[AstName=Name(name), Args=argList] -> {
       // buildFunctionCall(deep,`name, `argList, moduleName);
        return;
      }

      var@(BQVariable|BQVariableStar)[] -> {
        //output.write(deep,getVariableName(`var));
        return;
      }

      ExpressionToBQTerm(t) -> {
        //generateExpression(deep,`t, moduleName);
        return;
      }

      Composite(_*,t,_*) -> {
        %match(t) {
         /* CompositeTL(target) -> {
            generateTargetLanguage(deep,`target, moduleName);
          }
          CompositeBQTerm(term) -> {
            generateBQTerm(deep,`term, moduleName);
          }*/
        }
      }
      Composite(CompositeBQTerm(BuildTerm(Name(t),_,_)))->{
	System.out.println(`t);
      }
      t@!Composite(_*) -> {
        throw new TomRuntimeException("Cannot generate code for bqterm "+`t);
      }
      
    }
  }
  

  
  static void generate(int deep, Code subject, String moduleName) throws IOException {    
    %match(subject) {
      Tom(l) -> {
        generateList(deep,`l, moduleName);
        return;
      }

      TomInclude(l) -> {
        //generateListInclude(deep,`l, moduleName);
        return;
      }

      BQTermToCode(t) -> {
        generateBQTerm(deep,`t, moduleName);
        return;
      }

      TargetLanguageToCode(t) -> {
        generateTargetLanguage(deep,`t, moduleName);
        return;
      }

      InstructionToCode(t) -> {
        //generateInstruction(deep,`t, moduleName);
        return;
      }

      DeclarationToCode(t) -> {
        //generateDeclaration(deep,`t, moduleName);
        return;
      }

      t -> {
        System.out.println("Cannot generate code for: " + `t);
        throw new TomRuntimeException("Cannot generate code for: " + `t);
      }
    }
  }
  
  static void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {    
    System.out.println("In generateTargetLanguage");
    %match(subject) {
      TL(t,TextPosition[Line=startLine], TextPosition[Line=endLine]) -> {
        //output.write(deep, `t, `startLine, `endLine - `startLine);
        return;
      }
      ITL(t) -> {
        //output.write(`t);
        return;
      }
      Comment(t) -> {
        //`buildComment(deep,t);
        return;
      }
      t -> {
        System.out.println("Cannot generate code for TL: " + `t);
        throw new TomRuntimeException("Cannot generate code for TL: " + `t);
      }
    }
  }
  
  static void generateList(int deep, CodeList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcCode()) {
        generate(deep, subject.getHeadconcCode(), moduleName);
        subject = subject.getTailconcCode();
      }
    }
    
}
