/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr

*/

package jtom;

import jtom.tools.*;
import jtom.adt.*;

public class TomEnvironment {

    private ASTFactory astFactory;
    private TomSignatureFactory tomSignatureFactory;
    private SymbolTable symbolTable;
    private Statistics statistics;
    
    public TomEnvironment(TomSignatureFactory tomSignatureFactory,
                          ASTFactory astFactory,
                          SymbolTable symbolTable,
                          Statistics statistics) {
        this.tomSignatureFactory = tomSignatureFactory;
	this.astFactory   = astFactory;
	this.symbolTable  = symbolTable;
        this.statistics   = statistics;
    }

    public ASTFactory getASTFactory() {
	return astFactory;
    }

    public TomSignatureFactory getTomSignatureFactory() {
	return tomSignatureFactory;
    }
  
    public SymbolTable getSymbolTable() {
	return symbolTable;
    }

    public Statistics getStatistics() {
	return statistics;
    }

}



