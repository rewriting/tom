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
import aterm.pure.SingletonFactory;
import java.util.*;
import tom.library.traversal.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.il.*;
import jtom.verifier.il.types.*;

public class LatexOutput {

	// ------------------------------------------------------------
	%include { il.tom }
	// ------------------------------------------------------------


	protected jtom.verifier.il.ilFactory factory;

	public LatexOutput() {
		factory = ilFactory.getInstance(SingletonFactory.getInstance());
	}

	protected final ilFactory getIlFactory() {
		return factory;
	}

	public String build_latex_from_tree(DerivTree tree) {
		String result = "";
		%match(DerivTree tree) {
			derivrule(name,post,pre,condition) -> {
				result = "\\infer[" + condition + "]{" + pre + "}{" + post + "}";
			}
			endderiv() -> {
// do nothing
			}
		}
		return result;
	}
	
}