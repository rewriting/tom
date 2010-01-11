import prologterms.types.*;
import java.util.*;

public class Arcs {

	%include {prologterms/PrologTerms.tom}
	
	public final static void main(String[] args) {
	
		Term A = `constant("A");
		Term B = `constant("B");
		Term C = `constant("C");
		Term D = `constant("D");
		
		Term X = `variable("X");
		Term Y = `variable("Y");
		Term Z = `variable("Z");
		
		Fact AB = `fact("arc",2,tList(A,B));
		Fact AC = `fact("arc",2,tList(A,C));
		Fact BC = `fact("arc",2,tList(B,C));
		Fact BD = `fact("arc",2,tList(B,D));
		
		Fact AD = `fact("arc",2,tList(A,D));
		Fact AX = `fact("arc",2,tList(A,X));
		Fact XY = `fact("arc",2,tList(X,Y));
		
		Rule arc = `rule(fact("arc",2,tList(X,Y)),fList(fact("arc",2,tList(X,Z)),fact("arc",2,tList(Z,Y))));
		
		RuleList rules = `rList(rule(AB,fList()),rule(AC,fList()),rule(BC,fList()),rule(BD,fList()),arc);
		
		Context context = new Context(rules,2000);
		
		FactList factsToTest = `fList(AX);
		context.ask(factsToTest);
	
		}

}
