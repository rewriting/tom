package moleculargraphs;

//import moleculargraphs.term.*;
import moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;

//import tom.library.sl.Strategy; 

public class EGFRPhosporylatesEGFR extends ReactionPattern {  
	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

	public EGFRPhosporylatesEGFR() {
		super();
	}

	public Structure apply(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			`EGFRPhosporylatesEGFRStrat(c).visit(structure); 
			Iterator it = c.iterator();
			while( it.hasNext() ) {
				result = `struct((Structure)it.next(), result*);
			}
			return result;
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRPhosporylatesEGFRStrat on " + structure + " with the message " + e);
		}
		return structure;
	}

/*	public Structure apply(Structure structure) {
		try {
			return (Structure)`EGFRPhosporylatesEGFRStrat().visitLight(structure); 
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRPhosporylatesEGFRStrat on " + structure + " with the message " + e);
		}
		return structure;
	}
*/
   %strategy EGFRPhosporylatesEGFRStrat(c:Collection) extends `Identity() {
  	visit Structure {
		Nodes(concN(	
			nl3*, 
			labNode(k, node(uid(k, "EGFR"), concP(p1,
												  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2")))), b())),	
												  labPort("s3", port("s3", concNG(), h())),
												  p4))),
			nl4*,
			labNode(l, node(uid(l, "EGFR"), concP(r1,
												  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2")))), b())),	
												  r3,
												  r4))),
			nl5*))
				 -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();
										 
				NodeList rhs = `concN(	
									nl3*,
									labNode(k, node(uid(k, "EGFR"), concP(p1,
																		  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2")))), b())),	
																		  labPort("s3", port("s3", concNG(), v())),
																		  p4))),
									nl4*,
									labNode(l, node(uid(l, "EGFR"), concP(r1,
																		  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2")))), b())),	
																		  r3,
																		  r4))),
									nl5*);
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));			
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));	
			}

	
		Nodes(concN(	
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(p1,
												  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2")))), b())),	
												  p3,
												  p4))),
			nl4*,
			labNode(l, node(uid(l, "EGFR"), concP(r1,
												  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2")))), b())),	
												  labPort("s3", port("s3", concNG(), h())),
												  r4))),
			nl5*))
				 -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();
										 
				NodeList rhs = `concN(	
									nl3*,
									labNode(k, node(uid(k, "EGFR"), concP(p1,
																		  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2")))), b())),	
																		  p3,
																		  p4))),
									nl4*,
									labNode(l, node(uid(l, "EGFR"), concP(r1,
																		  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2")))), b())),	
																		  labPort("s3", port("s3", concNG(), v())),
																		  r4))),
									nl5*);
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));			
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));	
			}

  		}
    }   // end strategy 
  
}
