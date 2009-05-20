package iptables;

import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;
import java.io.*;


public class Analyser {
	public static class InteractiveNeededException extends RuntimeException { }

	%include {iptables/analyser/Analyser.tom}
	%include {sl.tom}

	public static int[] anomalies = { 0,0,0,0 };

	public static void printError(String errtype, String r1, String errmsg,
		String r2) {
		System.err.println("error[" + errtype + "]: \n---\n'" + r1 
			+ "'\n\t<" + errmsg + "> \n'" + r2 + "'\n---");
	}

	public static void printWarning(String errtype, String r1,
		String errmsg, String r2) {
		System.err.println("warning[" + errtype + "] \n---\n'" + r1 
			+ "'\n\t<"+ errmsg + "> \n'" + r2 + "'\n---");
	}

	public static void printReport() {
		int sum = 0;
		for (int i = 0; i < anomalies.length; i++) {
			sum += anomalies[i];
			anomalies[i] *= 100;
		}

		System.out.println("---\n" + sum + " Anomalie" 
			+ (sum > 1 ? "s" : "") + " detected.\n\n"
			+ "Shadowing:\t"+(sum>0?(anomalies[0]/sum):0)+"%\n"
			+ "Redundancy:\t"+(sum>0?(anomalies[1]/sum):0)+"%\n"
			+ "Generalization:\t"+(sum>0?(anomalies[2]/sum):0)+"%\n"
			+ "Correlation:\t"+(sum>0?(anomalies[3]/sum):0)+"%\n");
	}

	public Rule getDisplayRuleChoice(String msg, Rule r1, Rule r2) {
		%match(r1,r2) {
			Rule[input=s1],Rule[input=s2] -> {
				System.out.print("[1] "+ `s1 + "\n[2] " + `s2 
					+ "\n[0] None\n" + msg + " [0/1/2] ? ");
			}
		}
		BufferedReader input = 
			new BufferedReader(new InputStreamReader(System.in));
	
		int i = 1;
		try {
			i = Integer.parseInt(input.readLine());
		} catch (Exception e) {}
		System.out.println("");

		if (i == 0)
			return null;
		else if (i == 1)
			return `r2;
		else
			return `r1;
	}


	public static Rules checkIntegrity(Rules rs) {
		try {
			rs = `OutermostId(checkIntegrityStrategy()).visit(rs);
		} catch (VisitFailure vf) { }
		printReport();
		return rs;
	}


	%strategy checkIntegrityStrategy() extends Identity() { 
		visit Rules {
			Rules(
				r1@Rule[],
				Y*,
				r2@Rule[],
				Z*
			) -> {
				Rule del = null;

				/* check redundancies */
				del = checkRedundancy(`r1,`r2);
				/* check correlations */
				checkCorrelation(`r1,`r2);
				/* check generalization */
				checkGeneralization(`r1,`r2);

				try {
					/* check shadowing */
					checkShadowing(`r1,`r2);
					
				} catch (InteractiveNeededException ine) {
					del = (new Analyser()).getDisplayRuleChoice(
						"Which rule do you want to delete"
						,`r1,`r2);
				}

				if (del != null) {
					if (del == `r1)
						return `Rules(r1,Y*,Z*);
					else 
						return `Rules(Y*,r2,Z*);
				}
			}
		}
	}

	public static Rule checkShadowing(Rule r1, Rule r2) 
					throws InteractiveNeededException {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,s1),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,s2)
			 -> {
				/* No problems if the actions are equals */
				if (`a1 == `a2)
					return null;

				int incs = AddressTools.isInclude(
					`srcaddr1,`srcaddr2),
				    incd = AddressTools.isInclude(
					`dstaddr1,`dstaddr2);

				/* If the first rule is included in the second */
				if (((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.INCLUDED))
				|| ((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.EQUALS))
				|| ((incs == AddressTools.EQUALS)
					&& (incd == AddressTools.INCLUDED))
				) {
					printError("shadowing",`s1,
						"shadows",`s2);
					anomalies[0]++;
					throw new InteractiveNeededException();

				/* If the two rules are equals */
				} else if ((incs == AddressTools.EQUALS)
				&& (incd == AddressTools.EQUALS)) {
					printError("shadowing",`s1,
						"in conflict with",`s2);
					anomalies[0]++;
					throw new InteractiveNeededException();
				}
			}
		}
		return null;
	}

	/* returns englobing rule */
	public static Rule checkRedundancy(Rule r1, Rule r2) {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,s1),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,s2)
			 -> {
				/* No problems if actions are differents */
				if (`a1 != `a2)
					return null;

				int incs = AddressTools.isInclude(
					`srcaddr1,`srcaddr2),
				    incd = AddressTools.isInclude(
					`dstaddr1,`dstaddr2);

				/* If the second rule is included in the first */
				if (((incs == AddressTools.INCLUDES)
					&& (incd == AddressTools.INCLUDES))
				|| ((incs == AddressTools.INCLUDES)
					&& (incd == AddressTools.EQUALS))
				|| ((incs == AddressTools.EQUALS)
					&& (incd == AddressTools.INCLUDES))
				) {
					printWarning("redundancy",`s2,
						"included in",`s1);
					anomalies[1]++;
					return `r1;
				/* If the first rule is included in the second */
				} else if (((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.INCLUDED))
				|| ((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.EQUALS))
				|| ((incs == AddressTools.EQUALS)
					&& (incd == AddressTools.INCLUDED))
				) {
					printWarning("redundancy",`s1,
						"included in",`s2);
					anomalies[1]++;
					return `r2;
				/* If the two rules are equals */
				} else if ((incs == AddressTools.EQUALS)
				&& (incd == AddressTools.EQUALS)) {
					printWarning("redundancy",`s1,
						"equivalent to",`s2);
					anomalies[1]++;
					return `r2;
				}
			}
		}
		return null;
	}

	public static Rule checkGeneralization(Rule r1, Rule r2) {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,s1),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,s2)
			 -> {
				/* No problems if actions are equals */
				if (`a1 == `a2)
					return null;

				int incs = AddressTools.isInclude(
					`srcaddr1,`srcaddr2),
				    incd = AddressTools.isInclude(
					`dstaddr1,`dstaddr2);

				/* If the second rule is included in the first */
				if (((incs == AddressTools.INCLUDES)
					&& (incd == AddressTools.INCLUDES))
				|| ((incs == AddressTools.INCLUDES)
					&& (incd == AddressTools.EQUALS))
				|| ((incs == AddressTools.EQUALS)
					&& (incd == AddressTools.INCLUDES))
				) {
					printWarning("generalization",`s1,
						"generalized by",`s2);
					anomalies[2]++;
				}
			}
		}
		return null;
	}

	/* >>>TODO: rewrite this test respecting the publication specification */
	public static Rule checkCorrelation(Rule r1, Rule r2) {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,s1),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,s2)
			-> {
				/* No problems if actions are equals */
				if (`a1 == `a2)
					return null;

				int incs = AddressTools.isInclude(
					`srcaddr1,`srcaddr2),
				    incd = AddressTools.isInclude(
					`dstaddr1,`dstaddr2);

				/* If the first rule is included in the second */
				if (((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.INCLUDES))
				|| ((incs == AddressTools.INCLUDES)
					&& (incd == AddressTools.INCLUDED))
				) {
					printWarning("correlation",`s1,
						"correlated to",`s2);
					anomalies[3]++;
				}
			}
		}
		return null;
	}
}
