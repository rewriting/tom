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

	public static void printError(String errtype, Rule r1, String errmsg,
		Rule r2) {
		System.err.println("error[" + errtype + "]: " + `r1 + " <" + 
			errmsg + "> " + `r2 + "\n");
	}

	public static void printWarning(String errtype, Rule r1, String errmsg,
		Rule r2) {
		System.err.println("warning[" + errtype + "]: " + `r1 + " <"
			+ errmsg + "> " + `r2 + "\n");
	}

	public static void printReport() {
		int sum = 0;
		for (int i = 0; i < anomalies.length; i++) {
			sum += anomalies[i];
			anomalies[i] *= 100;
		}

		System.out.println("---\n" + sum + " Anomalie" 
			+ (sum > 1 ? "s" : "") + " detected.\n\n"
			+ "Shadowing:\t" + (anomalies[0]/sum) + "%\n"
			+ "Redundancy:\t" + (anomalies[1]/sum) + "%\n"
			+ "Generalization:\t" + (anomalies[2]/sum) + "%\n"
			+ "Correlation:\t" + (anomalies[3]/sum) + "%\n");
	}

	public Rule getDisplayRuleChoice(String msg, Rule r1, Rule r2) {
		System.out.print("\n[1] "+ `r1 + "\n[2] " + `r2 + "\n[0] None\n"
			+ msg + " [0/1/2] ? ");
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

				try {
					/* check shadowing */
					checkShadowing(`r1,`r2);
					/* check generalization */
					checkGeneralization(`r1,`r2);
					
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
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,_),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,_)
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
					printError("shadowing",`r1,
						"shadows",`r2);
					anomalies[0]++;
					throw new InteractiveNeededException();

				/* If the two rules are equals */
				} else if ((incs == AddressTools.EQUALS)
				&& (incd == AddressTools.EQUALS)) {
					printError("shadowing",`r1,
						"in conflict with",`r2);
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
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,_),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,_)
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
					printWarning("redundancy",`r2,
						"included in",`r1);
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
					printWarning("redundancy",`r1,
						"included in",`r2);
					anomalies[1]++;
					return `r2;
				/* If the two rules are equals */
				} else if ((incs == AddressTools.EQUALS)
				&& (incd == AddressTools.EQUALS)) {
					printWarning("redundancy",`r1,
						"equivalent to",`r2);
					anomalies[1]++;
					return `r2;
				}
			}
		}
		return null;
	}

	public static Rule checkGeneralization(Rule r1, Rule r2)
					throws InteractiveNeededException {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,_),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,_)
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
					printWarning("generalization",`r1,
						"generalized by",`r2);
					anomalies[2]++;
					throw new InteractiveNeededException();
				}
			}
		}
		return null;
	}

	/* >>>TODO: rewrite this test respecting the publication specification */
	public static Rule checkCorrelation(Rule r1, Rule r2) {
		%match(r1,r2) {
			/* The first rule has the better priority (order) */
			Rule(a1,i,p,t,srcaddr1,dstaddr1,spt,dpt,opts,_),
			Rule(a2,i,p,t,srcaddr2,dstaddr2,spt,dpt,opts,_)
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
					&& (incd == AddressTools.INCLUDED))
				|| ((incs == AddressTools.INCLUDED)
					&& (incd == AddressTools.EQUALS))
				|| ((incs == AddressTools.EQUALS)
					&& (incd == AddressTools.INCLUDED))
				) {
					printWarning("correlation",`r1,
						"correlated to",`r2);
					anomalies[3]++;
				}
			}
		}
		return null;
	}
}
