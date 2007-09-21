package moleculargraphs;

import moleculargraphs.term.*;
import moleculargraphs.term.types.*;	

import tom.library.sl.VisitFailure;

public class Cleaning {
	%include{ term/term.tom }
 	%include {sl.tom }

	public static NodeList apply(NodeList rhs) {
		try {
			return (NodeList)`InnermostId(DeleteLinkToBH()).visitLight(rhs);
		} catch (VisitFailure e) {
			System.out.println("Error at apply cleaning strategy: " + e);
			return rhs;
		}
	}

	//delete a link to a black hole node
	%strategy DeleteLinkToBH() extends Identity() {
		visit NeighbourList {
   			concNG(l1*, neighbour(refNode("BH"),_), l2*) -> {
				return `concNG(l1*, l2*);
			}
		}
	}

}
