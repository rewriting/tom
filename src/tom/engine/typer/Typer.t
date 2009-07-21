/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
 * Cláudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The Typer plugin.
 * Perform type inference.
 */
public class Typer extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  %typeterm Typer { implement { Typer } }

  /** some output suffixes */
  private static final String freshTypeVarPrefix = "_freshTypeVar_";
  public static final String TYPED_SUFFIX       = ".tfix.typed";
  public static final String TYPED_TABLE_SUFFIX = ".tfix.typed.table";

  /** the declared options string */
  public static final String DECLARED_OPTIONS =
    "<options>" +
    "<boolean name='type' altName='' description='Typer (activated by default)' value='true'/>" +
    "</options>";

  private TyperEnvironment typerEnvironment;

  public TyperEnvironment getTyperEnvironment() {
    return typerEnvironment;
  }

  /** Constructor */
  public Typer() {
    super("Typer");
    typerEnvironment = new TyperEnvironment();
  }

  private class TyperEnvironment {
    
    /** few attributes */
    private SymbolTable symbolTable;
    private int freshTypeVarCounter = 0;
 
    public TyperEnvironment() {
      super();
    }

    /** Accessor methods */
    public SymbolTable getSymbolTable() {
      return this.symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
      this.symbolTable = symbolTable;
    }

    public int getFreshTypeVarCounter() {
      return this.freshTypeVarCounter;
    }
    
    public void setFreshTypeVarCounter(int freshVarCounter) {
      this.freshTypeVarCounter = freshTypeVarCounter;
    }
    
    public TomSymbol getSymbolFromName(String tomName) {
      return TomBase.getSymbolFromName(tomName, getSymbolTable());
    }

    public TomSymbol getSymbolFromType(TomType type) {
      %match(type) {
        TypeWithSymbol[TomType=tomType, TlType=tlType] -> {
          return TomBase.getSymbolFromType(`Type(tomType,tlType), getSymbolTable()); 
        }
      }
      return TomBase.getSymbolFromType(type, getSymbolTable()); 
    }
   
  } // class TyperEnvironment

  public void run(Map informationTracker) {
    //TODO
  }

}
