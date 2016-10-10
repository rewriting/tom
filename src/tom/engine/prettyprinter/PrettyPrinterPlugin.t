/*
 * 
 * TOM - To One Matching Expander
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
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
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

  public static final PlatformOptionList PLATFORM_OPTIONS =
    `concPlatformOption(
        PluginOption("prettyTOM", "pit", "Pretty print original Tom code (not activated by default)", BooleanValue(False()), "")
        );

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
    //return PLATFORM_OPTIONS; 
  }

  private void prettyPrinter(Code code) {
//    %include { string.tom }
    System.out.println("PrettyPrinter active");
/*
    Map<Float,String> anOrganizer = new TreeMap<Float,String>();
    try {
      FileWriter log = new FileWriter("log.t");
      FileWriter txt = new FileWriter("log.txt");
      txt.write(code.toString());
      txt.close();
    
      %match(code) {
        Tom(concCode(_*,TargetLanguageToCode(TL(a,e,_)),_*))-> { anOrganizer.put(getPosition(`e),`a); }
        Tom(concCode(_*,TargetLanguageToCode(TL(_,_,e)),BQTermToCode(Composite(CompositeBQTerm(BuildTerm(Name(a),_,_)))),_*))-> { anOrganizer.put(getPosition(`e),"`"+`a+"()"); }
//          Tom(concCode(t*)) -> { System.out.println(`(t*)); }
      }
    for (Iterator<Float> i = anOrganizer.keySet().iterator() ; i.hasNext() ; ){
      log.write(anOrganizer.get(i.next()));
    }
    
    log.close();
    }
    catch(Exception e) {
      System.out.println("little problem while writing the log file");
    }
*/
    //System.out.println(intermediaryCode);
    //System.out.println(codeToGive);
  }
 
/* 
  private float getPosition(TextPosition tp) {
    float f;
    %match(tp) {
      TextPosition(line,column) -> { f = Float.parseFloat(`line+"."+`column); return f; }
    }
  return -1;
  }
*/
}
