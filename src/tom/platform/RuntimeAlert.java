/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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

import tom.platform.adt.platformalert.*;
import tom.platform.adt.platformalert.types.*;

public class RuntimeAlert {
  
  private static PlatformAlertFactory platformAlertFactory = PlatformAlertFactory.getInstance(SingletonFactory.getInstance());
  public static AlertList EMPTY_ALERT_LIST = platformAlertFactory.makeAlertList();

  private AlertList errors;
  private AlertList warnings;
  private int nbErrors;
  private int nbWarnings;

  public RuntimeAlert() {
    errors = EMPTY_ALERT_LIST;
    warnings = EMPTY_ALERT_LIST;
    nbErrors = 0;
    nbWarnings = 0;
  }
  
  /*  public void addWarning(String message, String file, int line) {
    Alert entry = platformErrorFactory.makeErrorEntry_Warning(message, file, line);
    warnings = platformAlertFactory.append(warnings, entry);
    nbWarnings++;
  }
  
  public void addError(String message, String file, int line) {
    Alert entry = platformErrorFactory.makeAlert_Error(message, file, line);
    errors = platformErrorFactory.append(errors, entry);
    nbErrors++;
    }*/

  public void addWarning(String message, String file, int line) {
    Alert entry = platformAlertFactory.makeAlert_Warning(message, file, line);
    warnings = platformAlertFactory.append(warnings, entry);
    nbWarnings++;
  }
  
  public void addError(String message, String file, int line) {
    Alert entry = platformAlertFactory.makeAlert_Error(message, file, line);
    errors = platformAlertFactory.append(errors, entry);
    nbErrors++;
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
    if(newErrors.getErrors() != EMPTY_ALERT_LIST) {
      errors = platformAlertFactory.concat(errors, newErrors.getErrors());
      nbErrors += newErrors.getNbErrors();      
    }
    if(newErrors.getWarnings() != EMPTY_ALERT_LIST) {
      warnings = platformAlertFactory.concat(warnings, newErrors.getWarnings());
      nbWarnings += newErrors.getNbWarnings();
    }
  }

} //class RuntimeAlert
