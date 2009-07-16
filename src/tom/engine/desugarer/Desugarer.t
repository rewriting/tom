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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.desugarer;

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
import tom.engine.adt.code.types.*;
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
 * The Desugarer plugin.
 * Perform syntax expansion and more.
 */
public class Desugarer extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom }

  public Desugarer() {
    super("Desugarer");
  }

  public void run(Map informationTracker) {
     // long startChrono = System.currentTimeMillis();
     try {

       Code syntaxExpandedTerm = (Code) getWorkingTerm();

       // replace underscores by fresh variables
       syntaxExpandedTerm = 
         `TopDown(DesugarUnderscore(this)).visitLight(syntaxExpandedTerm);

       setWorkingTerm(syntaxExpandedTerm);      

    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{
            getClass().getName(), 
            getStreamManager().getInputFileName(), 
            e.getMessage()} );
      e.printStackTrace();
      return;
    }
  }

  // FIXME : generate truly fresh variables
  private int freshCounter = 0;
  private TomName getFreshVariable() {
    freshCounter++;
    return `Name("_f_r_e_s_h_v_a_r_" + freshCounter);
  }

  %typeterm Desugarer { implement { tom.engine.desugarer.Desugarer }}

  /* replaces  _  by a fresh variable
               _* by a fresh varstar    */
  %strategy DesugarUnderscore(desugarer:Desugarer) extends Identity() {
    visit TomTerm {
       UnamedVariable[Option=opts,AstType=ty,Constraints=constr] -> {
         return `Variable(opts,desugarer.getFreshVariable(),ty,constr);
       }
       UnamedVariableStar[Option=opts,AstType=ty,Constraints=constr] -> {
         return `VariableStar(opts,desugarer.getFreshVariable(),ty,constr);
       }
    }
  }
}
