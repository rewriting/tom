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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.text.MessageFormat;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.adt.tomsignature.types.Option;
import tom.engine.adt.tomsignature.types.OptionList;
import tom.engine.adt.tomsignature.types.TomSymbol;
import tom.engine.adt.tomsignature.types.TomTerm;
import tom.engine.adt.tomsignature.types.TomType;
import tom.engine.exception.TomRuntimeException;
import tom.engine.tools.TomGenericPlugin;
import tom.platform.PlatformLogRecord;


abstract public class TomChecker extends TomGenericPlugin {
  
    // ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
    // ------------------------------------------------------------
  
  static protected class TermDescription {
    int termClass, decLine;
    String name ="";
    TomType tomType = null;
    public TermDescription(int termClass, String name, int decLine, TomType tomType) {
      this.termClass = termClass;
      this.decLine = decLine;
      this.name = name;
      this.tomType = tomType;
    }
    public String type() {
      if(tomType != null && !tomType.isEmptyType()) {
        return tomType.getString();
      } else {
        return null;
      }
    }
  }
    // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int UNAMED_APPL             = 1;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int XML_APPL                = 5;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int UNAMED_VARIABLE_STAR    = 7;
  protected final static int PLACE_HOLDER            = 8;
  protected final static int VARIABLE                = 9;
  
  protected boolean strictType = false;
  protected Option currentTomStructureOrgTrack;
    
  public TomChecker(String name) {
    super(name);
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
  }
 
  public int getClass(TomTerm term) {
    %match(TomTerm term) {
      TermAppl[nameList=(Name(""))] -> { return UNAMED_APPL;}
      TermAppl[nameList=(Name(_))] -> { return TERM_APPL;}
      TermAppl[nameList=(Name(_), _*)] -> { return APPL_DISJUNCTION;}
      RecordAppl[nameList=(Name(_))] -> { return RECORD_APPL;}
      RecordAppl[nameList=(Name(_), _*)] -> { return RECORD_APPL_DISJUNCTION;}
      XMLAppl[] -> { return XML_APPL;}
      Placeholder[] -> { return PLACE_HOLDER;}
      VariableStar[] -> { return VARIABLE_STAR;}
      Variable[] -> { return VARIABLE;}
      UnamedVariableStar[] -> { return UNAMED_VARIABLE_STAR;}
    }
    throw new TomRuntimeException("Invalid Term");
  }
  
  public String getName(TomTerm term) {
    String dijunctionName = "";
    %match(TomTerm term) {
      TermAppl[nameList=(Name(name))] -> { return `name;}
      TermAppl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      RecordAppl[nameList=(Name(name))] -> { return `name;}
      RecordAppl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      XMLAppl[nameList=(Name(name), _*)] ->{ return `name;}
      XMLAppl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      Placeholder[]               ->{ return "_";}
      (Variable|VariableStar)[astName=Name(name)]              ->{ return `name+"*";}
      UnamedVariableStar[] ->{ return "_*";}
    }
    throw new TomRuntimeException("Invalid Term");
  }
  
  /**
   * Shared Functions 
   */
  protected String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }
  
  protected int findOriginTrackingLine(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,OriginTracking[line=line],_*) -> { return `line; }
    }
    return -1;
  }

  protected void ensureOriginTrackingLine(int line) {
    if(line < 0) {
      getLogger().log(Level.SEVERE,
                      TomMessage.findOTL.getMessage(),
                      getStreamManager().getInputFileName());
      //System.out.println("findOriginTrackingLine: not found ");
    }
  }

  /**
   * Message Functions
   */
  protected void messageError(int errorLine, TomMessage msg, Object[] msgArgs) {
    String structName = currentTomStructureOrgTrack.getAstName().getString();
    messageError(errorLine, structName, msg, msgArgs);
  }
  
  protected void messageError(int errorLine, String structInfo, TomMessage msg, Object[] msgArgs) {
    String fileName = currentTomStructureOrgTrack.getFileName().getString();
    int structDeclLine = currentTomStructureOrgTrack.getLine();
    getLogger().log(new PlatformLogRecord(Level.SEVERE, msg, msgArgs,fileName, errorLine, structDeclLine, structInfo));
  }
  
  protected void messageWarning(int errorLine, TomMessage msg, Object[] msgArgs) {
    String structName = currentTomStructureOrgTrack.getAstName().getString();
    messageWarning(errorLine, structName, msg, msgArgs);
  }
  
  protected void messageWarning(int errorLine, String structInfo, TomMessage msg, Object[] msgArgs) {
    String fileName = currentTomStructureOrgTrack.getFileName().getString();
    int structDeclLine = currentTomStructureOrgTrack.getLine();
    getLogger().log(new PlatformLogRecord(Level.WARNING,msg,msgArgs,fileName, errorLine,structDeclLine,structInfo));
  }
  
}  //Class TomChecker
