/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2005, INRIA
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
import aterm.pure.*;
import java.util.*;
import tom.library.traversal.*;
import jtom.adt.tomsignature.types.*;
import jtom.verifier.il.*;
import jtom.verifier.il.types.*;

public class StatOutput {

	// ------------------------------------------------------------
	%include { il/il.tom }
	// ------------------------------------------------------------

	protected jtom.verifier.il.ilFactory factory;
  private GenericTraversal traversal;
	private TomVerifier verifier;

	public StatOutput(TomVerifier verifier) {
		factory = ilFactory.getInstance(SingletonFactory.getInstance());
    this.traversal = new GenericTraversal();
		this.verifier = verifier;
	}

  public GenericTraversal traversal() {
    return this.traversal;
  }
  
	protected final ilFactory getIlFactory() {
		return factory;
	}

	public String build_stats(Collection derivationSet) {
		String result = "\nStatistics for this run: \n";

		// collecting derivations size, number of constraints
		Map sizes = new HashMap();

		Iterator it = derivationSet.iterator();
		while(it.hasNext()) {
			DerivTree tree = (DerivTree) it.next();
			MyCounter count = count(tree);
			sizes.put(tree,count);
		}

		// compute global statistics, and print stats for each tree
		int count = 0;
		int globalnbderiv = 0;
		int globalnbconstraints = 0;
		int globalnbexprconst = 0;
		int globalnbdedstep = 0;
		it = sizes.values().iterator();
		while(it.hasNext()) {
			count += 1;
			MyCounter counter = (MyCounter) it.next();
			result += "Derivation " + count + "\n";
			result += "  Number of rules: " + counter.nbDerivRule + "\n";
			globalnbderiv += counter.nbDerivRule;
			result += "  Number of constraints: " + counter.nbConstraints + "\n";
			globalnbconstraints += counter.nbConstraints;
			result += "  Number of interesting constraints: " + counter.nbExprConstraints + "\n";
			globalnbexprconst += counter.nbExprConstraints;
			result += "  Number of deduction steps: " + counter.nbDedStep + "\n";
			globalnbdedstep += counter.nbDedStep;
		}

		result += "\nGlobal stats:\n";
		result += "  Number of derivations = " + derivationSet.size() + "\n";
		
		result += "  Number of rules: " + globalnbderiv + "\n";
		result += "  Number of constraints: " + globalnbconstraints + "\n";
		result += "  Number of interesting constraints: " + globalnbexprconst + "\n";
		result += "  Number of deduction steps: " + globalnbdedstep + "\n";
		
		result += "\n";
		return result;
	}

	private Collect2 count_derivrules = new Collect2() {
	    public boolean apply(ATerm subject, Object astore) {
        MyCounter store = (MyCounter)astore;
        if (subject instanceof DerivTree) {
          %match(DerivTree subject) {
            derivrule(name,post,pre,cond)  -> {
							store.nbDerivRule += 1;
            }
          }//end match
        } else if (subject instanceof Seq) { 
					%match(Seq subject) {
						seq() -> {
							// i font count the empty conditions
						}
						dedterm(termlist) -> {
							store.nbConstraints += 1;
							store.nbDedStep += `termlist.getLength();
						}
						dedexpr(termlist) -> {
							store.nbConstraints += 1;
							store.nbExprConstraints += 1;
							store.nbDedStep += `termlist.getLength();
						}
					}
        }
				return true;
	    }//end apply
    }; //end new
  
  public MyCounter count(DerivTree subject) {
    MyCounter result = new MyCounter();
    traversal().genericCollect(subject,count_derivrules,result);
    return (MyCounter) result;
  }

	class MyCounter {
		public int nbDerivRule;
		public int nbConstraints;
		public int nbExprConstraints;
		public int nbDedStep;

		public MyCounter() {
			nbDerivRule = 0;
			nbConstraints = 0;
			nbExprConstraints = 0;
			nbDedStep = 0;
		}
	}

}
