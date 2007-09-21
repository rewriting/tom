package moleculargraphs;

//import moleculargraphs.term.*;
import moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

import java.util.*;

//import tom.library.sl.Strategy; 

public class EGFRBindsSHC extends ReactionPattern {  
	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	

	public EGFRBindsSHC() {
		super();
	}

	public Structure apply(Structure structure) {
		try {
			Collection c = new HashSet();
			Structure result = `struct();
			`EGFRBindsSHCStrat(c).visit(structure); 
			Iterator it = c.iterator();
			while( it.hasNext() ) {
				result = `struct((Structure)it.next(), result*);
			}
			return result;
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRBindsSHCStrat on " + structure + " with the message " + e);
		}
		return structure;
	}

/*	public Structure apply(Structure structure) {
		try {
			return (Structure)`EGFRBindsSHCStrat().visitLight(structure); 
		} catch(VisitFailure e) {
			System.out.println("Error at applying EGFRBindsSHCStrat on " + structure + " with the message " + e);
		}
		return structure;
	}
*/
   %strategy EGFRBindsSHCStrat(c:Collection) extends `Identity() {
  	visit Structure {
		Nodes(concN(	
			nl3*,
			labNode(k, node(uid(k, "EGFR"), concP(p1,
												  p2,
												  labPort("s3", port("s3", concNG(), v())),
												  p4))),
			nl4*,
			labNode(l, node(uid(l, "SHC"), concP(labPort("s1", port("s1", concNG(), v())),	
												 labPort("s2", port("s2", concNG(), h()))))),
			nl5*))
				 -> {
				
				NodeSubstitutionList nodeSubsts = `concNS();
										 
				NodeList rhs = `concN(	
									nl3*,
									labNode(k, node(uid(k, "EGFR"), concP(p1,
																		  p2,
																		  labPort("s3", port("s3", concNG(neighbour(refNode(l), concP(refPort("s1"))) ), b())),
																		  p4))),
									nl4*,
									labNode(l, node(uid(l, "SHC"), concP(labPort("s1", port("s1", concNG(), b())),	
																		 labPort("s2", port("s2", concNG(), v()))))),
									nl5*);
				//return `Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts));
				c.add(`Nodes(nodeSubstApp.applyAll(rhs, nodeSubsts)));
			}

  		}
    }   // end strategy 
  
}
