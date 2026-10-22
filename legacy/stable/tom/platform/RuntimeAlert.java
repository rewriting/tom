
























package tom.platform;



import aterm.pure.*;
import java.util.logging.*;



import tom.platform.adt.platformalert.*;
import tom.platform.adt.platformalert.types.*;



public class RuntimeAlert {
  
     private static   tom.platform.adt.platformalert.types.AlertList  tom_append_list_concAlert( tom.platform.adt.platformalert.types.AlertList l1,  tom.platform.adt.platformalert.types.AlertList  l2) {     if( l1.isEmptyconcAlert() ) {       return l2;     } else if( l2.isEmptyconcAlert() ) {       return l1;     } else if(  l1.getTailconcAlert() .isEmptyconcAlert() ) {       return  tom.platform.adt.platformalert.types.alertlist.ConsconcAlert.make( l1.getHeadconcAlert() ,l2) ;     } else {       return  tom.platform.adt.platformalert.types.alertlist.ConsconcAlert.make( l1.getHeadconcAlert() ,tom_append_list_concAlert( l1.getTailconcAlert() ,l2)) ;     }   }   private static   tom.platform.adt.platformalert.types.AlertList  tom_get_slice_concAlert( tom.platform.adt.platformalert.types.AlertList  begin,  tom.platform.adt.platformalert.types.AlertList  end, tom.platform.adt.platformalert.types.AlertList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcAlert()  ||  (end== tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.platform.adt.platformalert.types.alertlist.ConsconcAlert.make( begin.getHeadconcAlert() ,( tom.platform.adt.platformalert.types.AlertList )tom_get_slice_concAlert( begin.getTailconcAlert() ,end,tail)) ;   }   

  private AlertList errors;
  private AlertList warnings;
  private int nbErrors;
  private int nbWarnings;

  public RuntimeAlert() {
    errors =  tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ;
    warnings =  tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ;
    nbErrors = 0;
    nbWarnings = 0;
  }

  
  public void addWarning(String message, String file, int line) {
    Alert entry =  tom.platform.adt.platformalert.types.alert.Warning.make(message, file, line) ;
    { /* unamed block */{ /* unamed block */if ( (entry instanceof tom.platform.adt.platformalert.types.Alert) ) {if ( (warnings instanceof tom.platform.adt.platformalert.types.AlertList) ) {boolean tomMatch838_9= false ;if ( (((( tom.platform.adt.platformalert.types.AlertList )(( tom.platform.adt.platformalert.types.AlertList )warnings)) instanceof tom.platform.adt.platformalert.types.alertlist.ConsconcAlert) || ((( tom.platform.adt.platformalert.types.AlertList )(( tom.platform.adt.platformalert.types.AlertList )warnings)) instanceof tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert)) ) { tom.platform.adt.platformalert.types.AlertList  tomMatch838_end_5=(( tom.platform.adt.platformalert.types.AlertList )warnings);do {{ /* unamed block */if (!( tomMatch838_end_5.isEmptyconcAlert() )) {if ( ((( tom.platform.adt.platformalert.types.Alert )entry)== tomMatch838_end_5.getHeadconcAlert() ) ) {tomMatch838_9= true ;}}if ( tomMatch838_end_5.isEmptyconcAlert() ) {tomMatch838_end_5=(( tom.platform.adt.platformalert.types.AlertList )warnings);} else {tomMatch838_end_5= tomMatch838_end_5.getTailconcAlert() ;}}} while(!( (tomMatch838_end_5==(( tom.platform.adt.platformalert.types.AlertList )warnings)) ));}if (!(tomMatch838_9)) {

        warnings =  tom.platform.adt.platformalert.types.alertlist.ConsconcAlert.make(entry,tom_append_list_concAlert(warnings, tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() )) ;
        nbWarnings++;   
      }}}}}
    
  }
  
  
  public void addError(String message, String file, int line) {
    Alert entry =  tom.platform.adt.platformalert.types.alert.Error.make(message, file, line) ;
    { /* unamed block */{ /* unamed block */if ( (entry instanceof tom.platform.adt.platformalert.types.Alert) ) {if ( (errors instanceof tom.platform.adt.platformalert.types.AlertList) ) {boolean tomMatch839_9= false ;if ( (((( tom.platform.adt.platformalert.types.AlertList )(( tom.platform.adt.platformalert.types.AlertList )errors)) instanceof tom.platform.adt.platformalert.types.alertlist.ConsconcAlert) || ((( tom.platform.adt.platformalert.types.AlertList )(( tom.platform.adt.platformalert.types.AlertList )errors)) instanceof tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert)) ) { tom.platform.adt.platformalert.types.AlertList  tomMatch839_end_5=(( tom.platform.adt.platformalert.types.AlertList )errors);do {{ /* unamed block */if (!( tomMatch839_end_5.isEmptyconcAlert() )) {if ( ((( tom.platform.adt.platformalert.types.Alert )entry)== tomMatch839_end_5.getHeadconcAlert() ) ) {tomMatch839_9= true ;}}if ( tomMatch839_end_5.isEmptyconcAlert() ) {tomMatch839_end_5=(( tom.platform.adt.platformalert.types.AlertList )errors);} else {tomMatch839_end_5= tomMatch839_end_5.getTailconcAlert() ;}}} while(!( (tomMatch839_end_5==(( tom.platform.adt.platformalert.types.AlertList )errors)) ));}if (!(tomMatch839_9)) {

        errors =  tom.platform.adt.platformalert.types.alertlist.ConsconcAlert.make(entry,tom_append_list_concAlert(errors, tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() )) ;
        nbErrors++;    
      }}}}}
    
  }
  
  public int getNbErrors() {
    return nbErrors;
  }

  public int getNbWarnings() {
    return nbWarnings;
  }

  public boolean hasErrors() {
    return (nbErrors>0);
  }

  public boolean hasWarnings() {
    return (nbWarnings>0);
  }

  public AlertList getErrors() {
    return errors;
  }

  public AlertList getWarnings() {
    return warnings;
  }
  
  public void concat(RuntimeAlert newErrors) {
    if(newErrors.getErrors() !=  tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ) {
      AlertList newAlerts = newErrors.getErrors();
      errors = tom_append_list_concAlert(newAlerts,tom_append_list_concAlert(errors, tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ));
      nbErrors += newErrors.getNbErrors();      
    }
    if(newErrors.getWarnings() !=  tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ) {
      AlertList newAlerts = newErrors.getWarnings();
      warnings = tom_append_list_concAlert(newAlerts,tom_append_list_concAlert(warnings, tom.platform.adt.platformalert.types.alertlist.EmptyconcAlert.make() ));
      nbWarnings += newErrors.getNbWarnings();
    }
  }

  
  public void add(PlatformLogRecord record) {
    
	PlatformFormatter formatter = new PlatformFormatter();   
	  
	if(record.getLevel() == Level.SEVERE) {
      addError(formatter.formatMessage(record), record.getFilePath(), record.getLine());
    } else if(record.getLevel() == Level.WARNING) {
      addWarning(formatter.formatMessage(record), record.getFilePath(), record.getLine());
    }
  }



} 
