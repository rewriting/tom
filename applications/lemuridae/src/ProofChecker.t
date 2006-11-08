import sequents.*;
import sequents.types.*;

import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.Position;
import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;

import java.io.*;
import antlr.*;
import antlr.collections.*;

public class ProofChecker {
	%include { sequents/sequents.tom }

public static boolean proofcheck (Tree term) {
	%match(Tree term) {
	/* rule(NOM,PREMISSES,CONCLUSIONS,ACTIVEPROP) */
	rule(
		"axiom",
		 _,
		 sequent((_*,x,_*),(_*,x,_*)),
		 _
	)
			 -> {return true;}
	rule(
		"and L",
		 (p@rule(_,_,sequent((g1*,A,B,g2*), d),_)),
		 sequent((g1*,a,g2*),d),
		 a@and(A,B)
	)
			 -> { return proofcheck(`p);}
	rule(
		"and R",
		 (p1@rule(_,_,sequent(g,(d1*,A,d2*)),_),p2@rule(_,_,sequent(g,(d1*,B,d2*)),_)),
		 sequent(g,(d1*,a,d2*)),
		 a@and(A,B)
	)
			 -> { return (proofcheck(`p1) && proofcheck(`p2)); }
	rule(
		"or R",
		 (p@rule(_,_,sequent(g, (d1*,A,B,d2*)),_)),
		 sequent(g, (d1*,a,d2*)),
		 a@or(A,B)
	)
			 -> { return proofcheck(`p);}
	rule(
		"or L",
		 (p1@rule(_,_,sequent((g1*,A,g2*), d),_),p2@rule(_,_,sequent((g1*,B,g2*), d),_)),
		 sequent((g1*,a,g2*), d),
		 a@or(A,B)
	)
			 -> { return (proofcheck(`p1) && proofcheck(`p2)); }
	rule(
		"implies R",
		(p@rule(_,_,sequent((g*,A), (d1*, B, d2*)),_)),
		sequent(g, (d1*,a,d2*)),
		a@implies(A,B)
	)
			-> { return proofcheck(`p);}
	rule(
		"implies L",
/*		(p1@rule(_,_,sequent((g1*,g2*),(A,d*)),_), p2@rule(_,_,sequent((g1*,B,g2*),d),_)), */
		(p1@rule(_,_,sequent(x,(A,d*)),_), p2@rule(_,_,sequent((g1*,B,g2*),d),_)),
		sequent(y@(g1*,a,g2*),d),
		a@implies(A,B)
	)
/*			-> {return (proofcheck(`p1) && proofcheck(`p2));} */
			-> {return (proofcheck(`p1) && proofcheck(`p2) && `x == `context(g1*,g2*));}
	x -> {System.out.println(`x); try {PrettyPrinter.display(`x);} catch (Exception e) {}; return false;}
	}
	return false;
}

}
