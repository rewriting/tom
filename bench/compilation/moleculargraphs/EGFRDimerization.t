package compilation.moleculargraphs;

//import moleculargraphs.term.*;
import compilation.moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;

//import tom.library.sl.Strategy; 

public class EGFRDimerization extends ReactionPattern {  
	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

	public EGFRDimerization() {
		super();
	}

	public Structure apply(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			`EGFRDimerizationStrat(c).visit(structure); 
			Iterator it = c.iterator();
			while( it.hasNext() ) {
				result = `struct((Structure)it.next(), result*);
			}
			return result;
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRDimerizationStrat on " + structure + " with the message " + e);
		}
		return structure;
	}

/*	public Structure apply(Structure structure) {
		try {
			return (Structure)`EGFRDimerizationStrat().visitLight(structure); 
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRDimerizationStrat on " + structure + " with the message " + e);
		}
		return structure;
	}
*/
   %strategy EGFRDimerizationStrat(c:Collection) extends `Identity() {
  	visit Structure {
		Nodes(concN(	
			nl1*,
			labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), b())) ))),
			nl2*,
			labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), b()))))),
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s2")))), b())),
												  labPort("s2", port("s2", concNG(), v())),	
												  p31,
												  labPort("s4", port("s4", concNG(), v()))
												  ))),
			nl4*,
			labNode(l, node(uid(l, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s2")))), b())),
												  labPort("s2", port("s2", concNG(), v())),	
												  p32,
												  labPort("s4", port("s4", concNG(), v()))
												  ))),
			nl5*))
				 -> {
				
				//NodeSubstitutionList nodeSubsts = `concNS();
										 
				NodeList rhs = `concN(	
								nl1*,
								labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
																	labPort("s2", port("s2", concNG(), b())) ))),
								nl2*,
								labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
																	labPort("s2", port("s2", concNG(), b()))))),
								nl3*,
								labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s2")))), b())),
																	  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2"))) ), b())),	
																	  p31,
																	  labPort("s4", port("s4", concNG(), h()))
																	  ))),
								nl4*,
								labNode(l, node(uid(l, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s2")))), b())),
																	  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2"))) ), b())),	
																	  p32,
																	  labPort("s4", port("s4", concNG(), h()))
																	  ))),
								nl5*);
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));
				System.out.println("Aici1: " + rhs);
				c.add(`Nodes(rhs));	
			}

		Nodes(concN(	
			nl1*,
			labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), b())) ))),
			nl2*,
			labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
												labPort("s2", port("s2", concNG(), b()))))),
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s2")))), b())),
												  labPort("s2", port("s2", concNG(), v())),	
												  p31,
												  labPort("s4", port("s4", concNG(), v()))
												  ))),
			nl4*,
			labNode(l, node(uid(l, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s2")))), b())),
												  labPort("s2", port("s2", concNG(), v())),	
												  p32,
												  labPort("s4", port("s4", concNG(), v()))
												  ))),
			nl5*))
				 -> {
				
				//NodeSubstitutionList nodeSubsts = `concNS();
										 
				NodeList rhs = `concN(	
								nl1*,
								labNode(i, node(uid(i,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s1")))), b())), 
																	labPort("s2", port("s2", concNG(), b())) ))),
								nl2*,
								labNode(j, node(uid(j,"EGF"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s1")))), b())), 
																	labPort("s2", port("s2", concNG(), b()))))),
								nl3*,
								labNode(k, node(uid(k, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(j), concP(refPort("s2")))), b())),
																	  labPort("s2", port("s2", concNG(neighbour(refNode(l), concP(refPort("s2"))) ), b())),	
																	  p31,
																	  labPort("s4", port("s4", concNG(), h()))
																	  ))),
								nl4*,
								labNode(l, node(uid(l, "EGFR"), concP(labPort("s1", port("s1", concNG(neighbour(refNode(i), concP(refPort("s2")))), b())),
																	  labPort("s2", port("s2", concNG(neighbour(refNode(k), concP(refPort("s2"))) ), b())),	
																	  p32,
																	  labPort("s4", port("s4", concNG(), h()))
																	  ))),
								nl5*);
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));
				System.out.println("Sau aici: " + rhs);
				c.add(`Nodes(rhs));	
			}


	
  		}
    }   // end strategy 
  
}
