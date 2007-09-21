/** 
When a node is deleted, we replace it with the black-hole but keeping the ports. Not done the port deletion.
In a list of nodes, the elements are ordered lexicographically by the node names.
EGFR > EGF > SHC > EGF.EGF
*/

package moleculargraphs;

import tom.library.sl.Strategy;
import tom.library.sl.Visitable;
import tom.library.sl.VisitFailure;
import java.util.*;

import moleculargraphs.term.*;
import moleculargraphs.term.types.*;
import moleculargraphs.term.strategy.*;	

public class MolGraphTest {

  %include {term/term.tom}
  %include {term/_term.tom} 
 
  %include {sl.tom }
  %include {java/util/types/Collection.tom}	
	private static PrettyPrintingTools pp;  
	//private static RewriteRules rewRules;

	public MolGraphTest() {
		pp = new PrettyPrintingTools();
		//rewRules = new RewriteRules();
	}

  public static void main(String[] args) {
    MolGraphTest test = new MolGraphTest();
    test.run();
  } 

	  
  public void run() {
	Structure structure = 
			`struct(Nodes(concN(labNode("1", node(uid("1", "EGF"), concP(labPort("s1", port("s1", concNG(), v())), labPort("s2", port("s2", concNG(), h()))))),
					labNode("2", node(uid("2", "EGF"), concP(labPort("s1", port("s1", concNG(), v())), labPort("s2", port("s2", concNG(), h()))))),
					labNode("3", node(uid("3", "EGF"), concP(labPort("s1", port("s1", concNG(), v())), labPort("s2", port("s2", concNG(), h()))))),
					labNode("4", node(uid("4", "EGF"), concP(labPort("s1", port("s1", concNG(), v())), labPort("s2", port("s2", concNG(), h()))))),
					labNode("5", node(uid("5", "EGFR"), concP(labPort("s1", port("s1", concNG(), v())), 
															  labPort("s2", port("s2", concNG(), v())),
															  labPort("s3", port("s3", concNG(), h())),
															  labPort("s4", port("s4", concNG(), h()))	))),
					labNode("6", node(uid("6", "EGFR"), concP(labPort("s1", port("s1", concNG(), v())), 
															  labPort("s2", port("s2", concNG(), v())),
															  labPort("s3", port("s3", concNG(), h())),
															  labPort("s4", port("s4", concNG(), h()))	))),
					labNode("7", node(uid("7", "SHC"), concP(labPort("s1", port("s1", concNG(), v())), labPort("s2", port("s2", concNG(), h())))))
						))); 	
  
	//System.out.println("Initial configuration:\n " + pp.prettyPrint(configuration)); 
	
	//Structure structure = `struct(configuration);  
	System.out.println("Initial structure:\n " + pp.prettyPrint(structure)); 
	  
      //Nodes subject = (Nodes)termAbstractType.expand(mgraph2);
      //System.out.println("\nsubject: " + pp.prettyPrint(subject));
   
	try { 
	
		Structure temp = (Structure)`Repeat(AppRule(Rule1())).visit(structure);
		System.out.println("Result after repeat rule1:\n " + pp.prettyPrint(temp));
	
		temp = (Structure)`Repeat(AppRule(Rule2())).visit(temp);
		System.out.println("Result after rule2:\n " + pp.prettyPrint(temp));

		temp = (Structure)`Repeat(AppRule(Rule3())).visit(temp);
		System.out.println("Result after rule3:\n " + pp.prettyPrint(temp));
/*
		temp = (Structure)`Repeat(AppRule(Rule4())).visit(temp);
		System.out.println("Result after rule4:\n " + pp.prettyPrint(temp));

		temp = (Structure)`Repeat(AppRule(Rule5())).visit(temp);
		System.out.println("Result after rule5:\n " + pp.prettyPrint(temp));
*/
	} catch (VisitFailure vf) {
		System.out.println("Exception in main: " + vf);
	} 
   } // end run()

 	%strategy AppRule(strat:Strategy) extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				Structure sss = (Structure)strat.visitLight(`struct(x));
				if( `struct() == sss) {
					throw new VisitFailure("Empty Structure from a one-element structure!");
				} else return sss;
			} 
			z@struct(x, y, t*) -> {
				//try{
					Structure s1 = (Structure)strat.visitLight(`struct(x));
					Structure s2 = (Structure)this.visitLight(`struct(y, t*));
					Structure s3 = `struct(s1, s2);
					if( `struct() == s3 ) {
						throw new VisitFailure("Empty structure, hurray!");
					} else return s3;
					//return (Structure)`_Consstruct(Rule1(), Rule1()).visitLight(`z);
				//}catch (VisitFailure vf) { System.out.println("Exception in Rule1: " + vf); }
			}
		}
	}

 	%strategy Rule1() extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				ReactionPattern r = new EGFDimerization();
				return `struct(r.apply(x));
			} 
		}
	}

 	%strategy Rule2() extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				ReactionPattern r = new EGFDimerBindsEGFR();
				return `struct(r.apply(x));
			} 
		}
	}
 	%strategy Rule3() extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				ReactionPattern r = new EGFRDimerization();
				return `struct(r.apply(x));
			} 
		}
	}
 	%strategy Rule4() extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				ReactionPattern r = new EGFRPhosporylatesEGFR();
				return `struct(r.apply(x));
			} 
		}
	}
 	%strategy Rule5() extends Fail() {
		visit Structure {
			struct(x@Nodes(_)) -> {
				ReactionPattern r = new EGFRBindsSHC();
				return `struct(r.apply(x));
			} 
		}
	}

}
 
