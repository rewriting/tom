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

package jtom;

import java.text.*;
import jtom.tools.*;
import jtom.adt.tomsignature.*;
import jtom.exception.TomRuntimeException;
import jtom.adt.tomsignature.types.*;
import jtom.TomMessage;

public class TomEnvironment {
  private ASTFactory astFactory;
  private Factory tomSignatureFactory;
  private SymbolTable symbolTable;
  private TomTerm term;
  private TomAlertList errors;
  private TomAlertList warnings;

  /*
   * Singleton pattern
   */
  private static TomEnvironment instance = null;
  protected TomEnvironment() {
    // exisits to defeat instantiation
  }

  public static TomEnvironment getInstance() {
    if(instance == null) {
      throw new TomRuntimeException(TomMessage.getString("GetInitializedTomEnvInstance"));
    }
    return instance;
  }

  public static TomEnvironment create(Factory tomSignatureFactory,
                                      ASTFactory astFactory,
                                      SymbolTable symbolTable) {
    if(instance == null) {
      instance = new TomEnvironment();
      instance.tomSignatureFactory = tomSignatureFactory;
      instance.astFactory = astFactory;
      instance.symbolTable = symbolTable;
      return instance;
    } else {
      throw new TomRuntimeException(TomMessage.getString("TwoTomEnvInstance"));
    }
  }

  public void init() {
    symbolTable.init();
    errors = tomSignatureFactory.makeTomAlertList();
    warnings = tomSignatureFactory.makeTomAlertList();
    term = null;
  }

  public ASTFactory getASTFactory() {
    return astFactory;
  }

  public Factory getTomSignatureFactory() {
    return tomSignatureFactory;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public void setTerm(TomTerm term) {
    this.term = term;
  }
  public TomTerm getTerm() {
    return term;
  }

  private static TomTaskInput getInput() {
    return TomTaskInput.getInstance();
  }

  public TomAlertList getErrors() {
    return errors;
  }

  public TomAlertList getWarnings() {
    return warnings;
  }

  public void clearErrors() {
  	errors = tomSignatureFactory.makeTomAlertList();
  }
  
  public void clearWarnings() {
  	warnings = tomSignatureFactory.makeTomAlertList();
  }
  
  private void setErrors(TomAlertList list) {
    errors = list;
  }

  public void setWarnings(TomAlertList list) {
    warnings = list;
  }

  public boolean hasError() {
    return getErrors().getLength()>0;
  }

  public boolean hasWarning() {
    return getWarnings().getLength()>0;
  }

  public void printErrorMessage() {
    if(!getInput().isEclipseMode()) {
      TomAlertList errors = getErrors();
      while(!errors.isEmpty()) {
        TomAlert error = errors.getHead();
        System.out.println(MessageFormat.format(TomMessage.getString("MainErrorMessage"), new Object[]{error.getFile(), new Integer(error.getLine()), error.getMessage()}));
      errors= errors.getTail();
      }
    }
  }

  public void printWarningMessage() {
    if(!getInput().isEclipseMode() && !getInput().isNoWarning()) {
      TomAlertList warnings = getWarnings();
      while(!warnings.isEmpty()) {
        TomAlert warning = warnings.getHead();
        System.out.println(MessageFormat.format(TomMessage.getString("MainWarningMessage"), new Object[]{warning.getFile(), new Integer(warning.getLine()), warning.getMessage()}));
      warnings= warnings.getTail();
      }
    }
  }

  public void printAlertMessage(String taskName) {
    if(!getInput().isEclipseMode()) {
      printErrorMessage();
      printWarningMessage();
      if(hasError()) {
      	System.out.println(MessageFormat.format(TomMessage.getString("TaskErrorMessage"),
        		new Object[]{taskName, new Integer(getErrors().getLength()), new Integer(getWarnings().getLength())}));
      } else if(hasWarning()) {
      	System.out.println(MessageFormat.format(TomMessage.getString("TaskWarningMessage"),
        		new Object[]{taskName, new Integer(getWarnings().getLength())}));
      }
    }
  }


  public void messageError(int errorLine,
                           String fileName,
                           String structInfo,
                           int structInfoLine,
                           String msg,
                           Object[] msgArg) {
    String formatedMessage = 
      MessageFormat.format(
                           TomMessage.getString("DetailErrorMessage"), 
                           new Object[]{
                             structInfo, 
                             new Integer(structInfoLine), 
                             MessageFormat.format(msg, msgArg)
                           });
    messageError(formatedMessage,fileName, errorLine);
  }
         
    
  public void messageError(String msg, Object[] args, String fileName, int errorLine) {
    String formatedMessage = MessageFormat.format(msg, args);
    messageError(formatedMessage,fileName, errorLine);
  }
  
  public void messageError(String formatedMessage, String file, int line) {
    TomAlert err = getTomSignatureFactory().makeTomAlert_Error(formatedMessage,file,line);
    setErrors(getTomSignatureFactory().makeTomAlertList(err, getErrors()));
  }

  public void messageWarning(int warningLine,
                           String fileName,
                           String structInfo,
                           int structInfoLine,
                           String msg,
                           Object[] msgArg) {
    String formatedMessage = 
      MessageFormat.format(
                           TomMessage.getString("DetailWarningMessage"), 
                           new Object[]{
                             structInfo, 
                             new Integer(structInfoLine), 
                             MessageFormat.format(msg, msgArg)
                           });
    messageWarning(formatedMessage,fileName, warningLine);
  }

  public void messageWarning(String msg, Object[] args, String fileName, int errorLine) {
    String formatedMessage = MessageFormat.format(msg, args);
    messageWarning(formatedMessage,fileName, errorLine);
  }

  public void messageWarning(String formatedMessage, String file, int line) {
    TomAlert err = getTomSignatureFactory().makeTomAlert_Warning(formatedMessage,file,line);
    setWarnings(getTomSignatureFactory().makeTomAlertList(err, getWarnings()));
  }

  
}
