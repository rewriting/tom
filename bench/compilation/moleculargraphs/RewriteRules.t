package moleculargraphs;

public class RewriteRules {
	%include{ term/term.tom }
 	%include {sl.tom }
	
//	public RewRuleSplitOneNode splitOneNode;
	public ReactionPattern rule1;
	public ReactionPattern rule2;
	public ReactionPattern rule3;
	public ReactionPattern rule4;
	public ReactionPattern rule5;

	public RewriteRules() {  
		rule1 = new EGFDimerization();
		rule2 = new EGFDimerBindsEGFR();
		rule3 = new EGFRDimerization();
		rule4 = new EGFRPhosporylatesEGFR();
		rule5 = new	EGFRBindsSHC();
	}
	
}
