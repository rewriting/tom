/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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

package tom.platform;

import aterm.pure.*;
import java.util.logging.*;

import tom.platform.adt.platformalert.*;
import tom.platform.adt.platformalert.types.*;

public class RuntimeAlert {
  
  %include { adt/platformalert/PlatformAlert.tom }

  private AlertList errors;
  private AlertList warnings;
  private int nbErrors;
  private int nbWarnings;

  public RuntimeAlert() {
    errors = `concAlert();
    warnings = `concAlert();
    nbErrors = 0;
    nbWarnings = 0;
  }

  /**
   * Add the warning only if it is not already in the list 
   */
  public void addWarning(String message, String file, int line) {
    Alert entry = `Warning(message, file, line);
    %match(Alert entry,warnings){
      x, !concAlert(_*,x,_*) -> {
        warnings = `concAlert(entry,warnings*);
        nbWarnings++;   
      }
    }    
  }
  
  /**
   * Add the error only if it is not already in the list 
   */
  public void addError(String message, String file, int line) {
    Alert entry = `Error(message, file, line);
    %match(Alert entry,errors){
      x, !concAlert(_*,x,_*) -> {
        errors = `concAlert(entry,errors*);
        nbErrors++;    
      }
    }    
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
    if(newErrors.getErrors() != `concAlert()) {
      AlertList newAlerts = newErrors.getErrors();
      errors = `concAlert(newAlerts*,errors*);
      nbErrors += newErrors.getNbErrors();      
    }
    if(newErrors.getWarnings() != `concAlert()) {
      AlertList newAlerts = newErrors.getWarnings();
      warnings = `concAlert(newAlerts*,warnings*);
      nbWarnings += newErrors.getNbWarnings();
    }
  }

  /**
   * @param record
   */
  public void add(PlatformLogRecord record) {
    
	PlatformFormatter formatter = new PlatformFormatter();   
	  
	if(record.getLevel() == Level.SEVERE) {
      addError(formatter.formatMessage(record), record.getFilePath(), record.getLine());
    } else if(record.getLevel() == Level.WARNING) {
      addWarning(formatter.formatMessage(record), record.getFilePath(), record.getLine());
    }
  }

} //class RuntimeAlert
