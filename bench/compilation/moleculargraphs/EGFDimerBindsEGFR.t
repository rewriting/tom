package compilation.moleculargraphs;

//import moleculargraphs.term.*;
import compilation.moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;

//import tom.library.sl.Strategy; 

public class EGFDimerBindsEGFR extends ReactionPattern {  
	%include{ term/term.tom }  
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	
	
	public EGFDimerBindsEGFR() {
		super();
	}

	public Structure apply(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			`EGFDimerBindsEGFRStrat(c).visit(structure); 
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
	
/*	public Structure apply(Structure structure) {
		try {
			return (Structure)`EGFDimerBindsEGFRStrat().visitLight(structure); 
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFDimerBindsEGFRStrat on " + structure + " with the message " + e);
		}
		return structure;
	}
*/
   %strategy EGFDimerBindsEGFRStrat(c:Collection) extends `Identity() {
  	visit Structure {
		Nodes(concN(	
			nl1*,
			labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
												p))),
			nl2*,
			labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), v()))))),
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(), v())),
												  pl1*,
												  labPort("s4", port("s4", concNG(), h()))
												  ))),
			nl4*))
				 -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();						 
										 
				NodeList rhs = `concN(	
									nl1*,
									labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
																		p))),
									nl2*,
									labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
																		labPort("s2", port("s2", concNG(), b()))))),
									nl3*,
									labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s2")))), b())),
																		  pl1*,
																		  labPort("s4", port("s4", concNG(), v()))
																		  ))),
									nl4*);
				//System.out.println("test1:");
				//c.add((Nodes)nodeSubstApp.applyAll(rhs, nodeSubsts));			
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));	
			}

		Nodes(concN(	
			nl1*,
			labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), v()))))),
			nl2*,
			labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
												 p))),
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(), v())),
												  pl1*,
												  labPort("s4", port("s4", concNG(), h()))
												  ))),
			nl4*))
				 -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();			
										 
				NodeList rhs = `concN(	
									nl1*,
									labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
																		labPort("s2", port("s2", concNG(), b()))))),
									nl2*,
									labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
																		p))),
									nl3*,
									labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s2")))), b())),
																		  pl1*,
																		  labPort("s4", port("s4", concNG(), v()))
																		  ))),
									nl4*);
				//System.out.println("test2:");
				//c.add((Nodes)nodeSubstApp.applyAll(rhs, nodeSubsts));			
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));	
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));	
			}
	
  		}
    }   // end strategy 
  
}
