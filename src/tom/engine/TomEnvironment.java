/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2003 INRIA
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

import jtom.tools.*;
import jtom.adt.tomsignature.*;
import jtom.exception.TomRuntimeException;
import jtom.adt.tomsignature.types.*;

public class TomEnvironment {
  private ASTFactory astFactory;
  private Factory tomSignatureFactory;
  private SymbolTable symbolTable;
  private TomTerm term;
  private TomErrorList errors;

  /*
   * Singleton pattern
   */
  private static TomEnvironment instance = null;
  protected TomEnvironment() {
    // exisits to defeat instantiation
  }

  public static TomEnvironment getInstance() {
    if(instance == null) {
      throw new TomRuntimeException(new Throwable("cannot get the instance of an unitialized TomEnvironment"));
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
      throw new TomRuntimeException(new Throwable("cannot create two instances of TomEnvironment"));
    }
  }

  public void init() {
    symbolTable.init();
    errors = tomSignatureFactory.makeTomErrorList();
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
  
  public TomErrorList getErrors() {
    return errors;
  }
  public void setErrors(TomErrorList list) {
    errors = list;
  }

  public boolean checkNoErrors(String taskName,
                  boolean eclipseMode,
                  boolean warningAll,
                  boolean noWarning) {
    boolean res = true; 
    TomErrorList errors = getErrors();
      //System.out.println(errors);
    int nbTotalError = errors.getLength();
    int nbWarning = 0, nbError=0;
    if(nbTotalError > 0 ) {
      while(!errors.isEmpty()) {
        TomError error = errors.getHead();
        if (error.getLevel() == 1) {
          nbWarning++;
          if (/*!noWarning || */warningAll && !eclipseMode) {
            System.out.println(error.getMessage());
          }
        } else if (error.getLevel() == 0) {
          if(!eclipseMode){
            System.out.println(error.getMessage());
          }
          res = false;
          nbError++;
        }
        errors= errors.getTail();
      }
      if (nbError>0 && !eclipseMode) {
        String msg = taskName+":  Encountered " + nbError + " errors and "+ nbWarning+" warnings.";
        msg += "No file generated.";
        System.out.println(msg);
      } else if (nbWarning>0 && !eclipseMode && !noWarning) {
        String msg = taskName+":  Encountered "+ nbWarning+" warnings.";
        System.out.println(msg);
      }
      if(!eclipseMode) {
        setErrors(getTomSignatureFactory().makeTomErrorList());
      }
    }
    return res;
  }

}
