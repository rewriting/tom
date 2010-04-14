/*
 *   
 * TOM - To One Matching Compiler
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
 * Julien Guyon
 *
 **/

package tom.engine.checker;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.TomGenericPlugin;
import tom.platform.PlatformLogRecord;


abstract public class CheckerPlugin extends TomGenericPlugin {
  
    // ------------------------------------------------------------
        private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }    
    // ------------------------------------------------------------
  
    // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int UNAMED_APPL             = 1;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int XML_APPL                = 5;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int UNAMED_VARIABLE_STAR    = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int VARIABLE                = 9;
  
  protected boolean strictType = false;
  protected Option currentTomStructureOrgTrack;
    
  public CheckerPlugin(String name) {
    super(name);
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
  }

  public int getClass(TomTerm term) {
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch1_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch1_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch1_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch1_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch1_6= tomMatch1_1.getHeadconcTomName() ;if ( (tomMatch1_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "".equals( tomMatch1_6.getString() ) ) {if (  tomMatch1_1.getTailconcTomName() .isEmptyconcTomName() ) {
 return UNAMED_APPL;}}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch1_9= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch1_9 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch1_9 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch1_9.isEmptyconcTomName() )) {if ( ( tomMatch1_9.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch1_9.getTailconcTomName() .isEmptyconcTomName() ) {
 return TERM_APPL;}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch1_16= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch1_16 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch1_16 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch1_16.isEmptyconcTomName() )) {if ( ( tomMatch1_16.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return APPL_DISJUNCTION;}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch1_24= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch1_24 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch1_24 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch1_24.isEmptyconcTomName() )) {if ( ( tomMatch1_24.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch1_24.getTailconcTomName() .isEmptyconcTomName() ) {
 return RECORD_APPL;}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch1_31= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch1_31 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch1_31 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch1_31.isEmptyconcTomName() )) {if ( ( tomMatch1_31.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return RECORD_APPL_DISJUNCTION;}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {
 return XML_APPL;}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {
 return UNAMED_VARIABLE;}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 return VARIABLE_STAR;}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 return VARIABLE;}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {
 return UNAMED_VARIABLE_STAR;}}}}

    throw new TomRuntimeException("Invalid Term");
  }
  
  public String getName(TomTerm term) {
    String dijunctionName = "";
    {{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch2_1= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch2_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch2_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch2_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch2_6= tomMatch2_1.getHeadconcTomName() ;if ( (tomMatch2_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch2_1.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch2_6.getString() ;}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String head;
        dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch2_11= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch2_11 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch2_11 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch2_11.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch2_16= tomMatch2_11.getHeadconcTomName() ;if ( (tomMatch2_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch2_11.getTailconcTomName() .isEmptyconcTomName() ) {
 return  tomMatch2_16.getString() ;}}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String head;
        dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch2_21= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;if ( ((tomMatch2_21 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch2_21 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch2_21.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch2_27= tomMatch2_21.getHeadconcTomName() ;if ( (tomMatch2_27 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch2_27.getString() ;}}}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList= (( tom.engine.adt.tomterm.types.TomTerm )term).getNameList() ;

        String head;
        dijunctionName = tom_nameList.getHeadconcTomName().getString();
        while(!tom_nameList.isEmptyconcTomName()) {
          head = tom_nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          tom_nameList= tom_nameList.getTailconcTomName();
        }
        return dijunctionName;
      }}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch2_32= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( (tomMatch2_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch2_32.getString() ;}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch2_37= (( tom.engine.adt.tomterm.types.TomTerm )term).getAstName() ;if ( (tomMatch2_37 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 return  tomMatch2_37.getString() +"*";}}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {
 return "_";}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {
 return "_*";}}}{if ( (term instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )term) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 return getName( (( tom.engine.adt.tomterm.types.TomTerm )term).getTomTerm() ); }}}}

    throw new TomRuntimeException("Invalid Term:" + term);
  }
  
  /**
   * Shared Functions 
   */
  protected String extractType(TomSymbol symbol) {
    TomType type = TomBase.getSymbolCodomain(symbol);
    return TomBase.getTomType(type);
  }
  
  protected String findOriginTrackingFileName(OptionList optionList) {
    {{if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch3__end__4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{if (!( tomMatch3__end__4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch3_8= tomMatch3__end__4.getHeadconcOption() ;if ( (tomMatch3_8 instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch3_8.getFileName() ; }}if ( tomMatch3__end__4.isEmptyconcOption() ) {tomMatch3__end__4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch3__end__4= tomMatch3__end__4.getTailconcOption() ;}}} while(!( (tomMatch3__end__4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    {{if ( (optionList instanceof tom.engine.adt.tomoption.types.OptionList) ) {if ( (((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || ((( tom.engine.adt.tomoption.types.OptionList )optionList) instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch4__end__4=(( tom.engine.adt.tomoption.types.OptionList )optionList);do {{if (!( tomMatch4__end__4.isEmptyconcOption() )) { tom.engine.adt.tomoption.types.Option  tomMatch4_8= tomMatch4__end__4.getHeadconcOption() ;if ( (tomMatch4_8 instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return  tomMatch4_8.getLine() ; }}if ( tomMatch4__end__4.isEmptyconcOption() ) {tomMatch4__end__4=(( tom.engine.adt.tomoption.types.OptionList )optionList);} else {tomMatch4__end__4= tomMatch4__end__4.getTailconcOption() ;}}} while(!( (tomMatch4__end__4==(( tom.engine.adt.tomoption.types.OptionList )optionList)) ));}}}}

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
  protected void messageError(String fileName, int errorLine, TomMessage msg, Object[] msgArgs) {
    TomMessage.error(getLogger(),fileName,errorLine,msg,msgArgs);
    //getLogger().log(new PlatformLogRecord(Level.SEVERE, msg, msgArgs,fileName, errorLine));
  }
  
  protected void messageWarning(String fileName, int errorLine, TomMessage msg, Object[] msgArgs) {
    TomMessage.warning(getLogger(),fileName,errorLine,msg,msgArgs);
    //getLogger().log(new PlatformLogRecord(Level.WARNING,msg,msgArgs,fileName, errorLine));
  }
  
  public static void messageError(String className,String fileName, int errorLine, TomMessage msg, Object[] msgArgs) {
    TomMessage.error(Logger.getLogger(className),fileName,errorLine,msg,msgArgs);
    //Logger.getLogger(className).log(new PlatformLogRecord(Level.SEVERE, msg, msgArgs,fileName, errorLine));
  }
  
  public static void messageWarning(String className,String fileName, int errorLine, TomMessage msg, Object[] msgArgs) {
    TomMessage.warning(Logger.getLogger(className),fileName,errorLine,msg,msgArgs);
    //Logger.getLogger(className).log(new PlatformLogRecord(Level.WARNING, msg, msgArgs,fileName, errorLine));
  }
  
}
