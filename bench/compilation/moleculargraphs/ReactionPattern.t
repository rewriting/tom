package compilation.moleculargraphs;

import compilation.moleculargraphs.term.types.*;	


//import tom.library.sl.Strategy; 

public class ReactionPattern {  
	%include{ term/term.tom }
 	%include {sl.tom }
	%include {java/util/types/Collection.tom}	
	protected static NodeSubstitutionApplication nodeSubstApp;

	public ReactionPattern() {
		nodeSubstApp = new NodeSubstitutionApplication();
	}

	public Structure apply(Structure structure) {
		return structure;
	}

}
 