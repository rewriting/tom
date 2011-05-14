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

import ppeditor.*;

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

//  public static PPOutputFormatter aPPOutputFormatter = new PPOutputFormatter();

  /** Constructor */
  public PrettyPrinterPlugin() {
    super("PrettyPrinterPlugin");
  }

    static OutputFormatter theFormatter = new OutputFormatter("TestOutput.txt");

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
    FileWriter textFile = new FileWriter("code.txt");
    textFile.write(code.toString());
    textFile.close();
    printTL(code);
    theFormatter.dump();
    System.out.println ("===========================================================");
    theFormatter.printAll();
  }
  
  public static void printTL(Code code) {

    try {
      `TopDownStopOnSuccess(stratPrintTL()).visitLight(code);
    } catch (VisitFailure e) {
      System.out.println("strategy failed");
    }
  }
  
  %strategy stratPrintTL() extends Fail(){

    visit TargetLanguage {
      TL[Code=x, Start=TextPosition[Line=startLine, Column=startColumn], End=TextPosition[Line=endLine, Column=endColumn]] -> {
        theFormatter.stock(`x,`startLine,`startColumn, 2); System.out.println ("==>"+`x+"l "+`startLine+ " &&& c "+`startColumn);
      }
    }

    visit BQTerm {
      //BuildConstant[Options=concOption(_*,OriginTracking[Line=line, Column=column],_*),AstName=Name(name)] -> {theFormatter.stock(`name+ "depth= "+getEnvironment().depth(), `line, `column,2);}
      //BQAppl[Options=concOption(_*,OriginTracking[Line=line, Column=column],_*),AstName=Name(name)] -> {theFormatter.stock(`name +"depth= "+getEnvironment().depth() ,`line, `column,2);}
    //  x@BQVariable[Options=concOption(_*,OriginTracking[Line=line, Column=column],_*),AstName=Name(name)] -> {theFormatter.stock("`"+`name, `line,`column-1,1); return `x;}
      //BuildTerm[Options=concOption(_*, OriginTracking[Line=line, Column=column] ,_*), AstName=Name(name), Args=concBQTerm()] -> {compteur+=100;theFormatter.stock(`name+"()" +compteur,`line,`column,2);}
    //  x@BuildTerm[Options=concOption(_*, OriginTracking[Line=line, Column=column] ,_*), AstName=Name(name)] -> {theFormatter.stock(generateBuildTerm(`x, `name, true),`line,`column-1,1); return `x;}

/*
      BuildTerm[Options=concOption(_*,OriginTracking[Line=line, Column=column],_*), AstName=Name(name), argList, myModuleName] -> {
        `buildTerm(deep, name, argList, myModuleName);
        return;
      }

      l@(BuildEmptyList|BuildEmptyArray|BuildConsList|BuildAppendList|BuildConsArray|BuildAppendArray)[] -> {
        buildListOrArray(deep, `l, moduleName);
        return;
      }

      FunctionCall[AstName=Name(name), Args=argList] -> {
        buildFunctionCall(deep,`name, `argList, moduleName);
        return;
      }

      var@(BQVariable|BQVariableStar)[] -> { // utile ?
        output.write(deep,getVariableName(`var));
        return;
      }

      ExpressionToBQTerm(t) -> {
        generateExpression(deep,`t, moduleName);
        return;
      }

      Composite(_*,t,_*) -> {
        %match(t) {
          CompositeTL(target) -> {
            // on ne fait rien ici
          }
          CompositeBQTerm(term) -> {
            generateBQTerm(deep,`term, moduleName);
          }
        }
      }
      t@!Composite(_*) -> {
        throw new TomRuntimeException("Cannot generate code for bqterm "+`t);
      }
*/
    }
  }
//  public void buildTerm(int deep, String name, BQTermList argList, String moduleName) throws IOException;
//  public void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException;
//  public void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName)  throws IOException;
  public static String generateBuildTerm(BQTerm bq, String name, boolean trunk) {

    String result;
    if (trunk){
      result = "`"+name+"(";
    }else{
      result = name+"(";
    }
    boolean isNotFirst = false;
    %match(bq) {

      BuildTerm[Args=concBQTerm(_*,Composite(CompositeBQTerm(bq@BuildTerm[AstName=Name(btName)])),_*)] -> {
       // System.out.println("Bla");
        if(isNotFirst) {
          result+=",";
        }
        result+=generateBuildTerm(`bq, `btName,false);
        isNotFirst = true;
      }    
    }
    result+=")";
   // if (trunk) {
   //   result+=";";
   // }
    return result;
  }
}
