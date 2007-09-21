package compilation.moleculargraphs;

import compilation.moleculargraphs.term.*;
import compilation.moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;

//import tom.library.sl.Strategy; 

public class EGFDimerization extends ReactionPattern {  
	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

	public EGFDimerization() {
		super(); 
	}
	
	public Structure apply(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			`EGFDimerizationStrat(c).visit(structure); 
			Iterator it = c.iterator();
			while( it.hasNext() ) {
				result = `struct((Structure)it.next(), result*);
			}
			return result;
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFDimerizationStrat on " + structure + " with the message " + e);
		}
		return structure;
	}

/*	public Structure applyAll(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			Iterator it;
			%match(structure) {
				struct(_*, x, _*) -> {
					c.clear();
					`EGFDimerizationStrat(c).visit(`x); 
					it = c.iterator();
					while( it.hasNext() ) {
						result = `struct((Structure)it.next(), result*);
					}
				}
			}
			return result;
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFDimerizationStrat on " + structure + " with the message " + e);
		}
		return structure;
	}
*/

// we should use a here a non-empty node-substitution, but we can do without since the two merging nodes are isolated
   %strategy EGFDimerizationStrat(c:Collection) extends Identity() {
  	visit Structure {
  		Nodes(concN(
  			nl1*,
			labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(), v())), 
												labPort("s2", port("s2", concNG(), h()))))),
			nl2*,
			labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(), v())), 
												labPort("s2", port("s2", concNG(), h()))))),
			nl3*)) -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();						 

				NodeList rhs = `concN(	
						  			nl1*,
									labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
																		labPort("s2", port("s2", concNG(), v()))))),
									nl2*,
									labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
																		labPort("s2", port("s2", concNG(), v()))))),
									nl3*);
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));		
			}
	 
  		}
    }   // end strategy 
  
}
