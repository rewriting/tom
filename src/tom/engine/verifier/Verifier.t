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
 * Antoine Reilles
 *
 **/

package jtom.verifier;

import jtom.*;
import aterm.*;
import java.util.*;
import jtom.tools.*;
import jtom.runtime.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.verifier.il.*;
import jtom.verifier.verifier.il.types.*;
import jtom.TomMessage;

public class Verifier extends TomBase {

	// ------------------------------------------------------------
 	%include { ../adt/TomSignature.tom }
	// ------------------------------------------------------------

	%vas {
		module il
			
		public sorts 
			Symbol Representation Variable Term Expr Instr 
			Substitution SubstitutionList Environment
			Seq
			Deriv DerivTree

		abstract syntax
			fsymbol(name:String)                    -> Symbol
			
			//repr(term:String)                     -> Representation

			var(name:String)                        -> Variable

			repr(term:String)                       -> Term
			subterm(symbol:Symbol,t:Term,index:Int) -> Term

			true                                    -> Expr
			false                                   -> Expr
			isfsym(t:Term,symbol:Symbol)            -> Expr
			eq(lt:Term,rt:Term)                     -> Expr

			accept                                  -> Instr
			refuse                                  -> Instr
			ITE(e:Expr,ift:Instr,iff:Instr)         -> Instr
			ILLet(var:Variable,t:Term,body:Instr)   -> Instr

			is(var:Variable,term:Term)              -> Substitution
			subs(Substitution *)                    -> SubstitutionList
			env(subs:Substitution,i:Instr)          -> Environment

			ebs(lhs:Environment,rhs:Environment)    -> Deriv
			iftrue(post:Deriv,pre:Deriv,cond:Seq)   -> DerivTree

			// to be completed
			seq()                                   -> Seq
	}

	protected jtom.verifier.verifier.il.Factory factory;

	public Verifier() {
		super();
		factory = new Factory(getTomSignatureFactory().getPureFactory());
	}

	protected final Factory getIlFactory() {
		return factory;
	}

	public DerivTree build_tree(Instruction automata) {
		DerivTree tree = null;
		System.out.println("Build derivation tree for : " + automata);

		return tree;
	}

}


