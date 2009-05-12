package iptables;

import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;
import java.io.*;


public class Analyser {
	public static class InteractiveNeededException extends RuntimeException { }

	%include {iptables/analyser/Analyser.tom}
	%include {sl.tom}

	public static final int NOT_COMPARABLE = Integer.MAX_VALUE;

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

	public Rule getDisplayRuleChoice(String msg, Rule r1, Rule r2) {
		System.out.print("\n[1] " + `r1 + "\n[2] " + `r2 + "\n" + msg 
			+ " [1/2] ? ");
		BufferedReader input = 
			new BufferedReader(new InputStreamReader(System.in));
	
		int i = 1;
		try {
			i = Integer.parseInt(input.readLine());
		} catch (Exception e) {}
		System.out.println("" + i);

		if (i == 1)
			return `r2;
		else
			return `r1;
	}


	public static Rules checkIntegrity(Rules rs) {
		try {
			rs = `OutermostId(checkIntegrityStrategy()).visit(rs);
		} catch (VisitFailure vf) { }
		return rs;
	}


	%strategy checkIntegrityStrategy() extends Identity() { 
		visit Rules {
			Rules(
				X*,
				r1@Rule(_,_,_,_,_,_,_,_,_,_),
				Y*,
				r2@Rule(_,_,_,_,_,_,_,_,_,_),
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
						return `Rules(X*,r1,Y*,Z*);
					else 
						return `Rules(X*,Y*,r2,Z*);
				}
			}
		}
	}

	public static Rule checkShadowing(Rule r1, Rule r2) 
					throws InteractiveNeededException {
		%match(r1,r2) {
				Rule(_,_,_,_,_,_,_,_,_,_),
				Rule(_,_,_,_,_,_,_,_,_,_)
			 -> {
				int i = isInclude(`r1,`r2);
				if (i == -2) {
					printError("shadowing",`r1,
						"shadows",`r2);
					throw new InteractiveNeededException();
				} else if (i == 8) {
					printError("shadowing",`r1,
						"in conflict with",`r2);
					throw new InteractiveNeededException();
				}
			}
		}
		return null;
	}

	/* returns englobing rule */
	public static Rule checkRedundancy(Rule r1, Rule r2) {
		%match(r1,r2) {
				Rule(_,_,_,_,_,_,_,_,_,_),
				Rule(_,_,_,_,_,_,_,_,_,_)
			 -> {
				int i = isInclude(`r1,`r2);
				if (i == 1) {
					printWarning("redundancy",`r2,
						"included in",`r1);
					return `r1;
				} else if (i == -1) {
					printWarning("redundancy",`r1,
						"included in",`r2);
					return `r2;
				} else if (i == 0) {
					printWarning("redundancy",`r1,
						"equivalent to",`r2);
					return `r1;
				}
			}
		}
		return null;
	}

	public static Rule checkGeneralization(Rule r1, Rule r2)
					throws InteractiveNeededException {
		%match(r1,r2) {
				Rule(_,_,_,_,_,_,_,_,_,_),
				Rule(_,_,_,_,_,_,_,_,_,_)
			 -> {
				int i = isInclude(`r1,`r2);
				if (i == 2) {
					printWarning("generalization",`r1,
						"generalized by",`r2);
					throw new InteractiveNeededException();
				}
			}
		}
		return null;
	}

	public static Rule checkCorrelation(Rule r1, Rule r2) {
		%match(r1,r2) {
				Rule(_,_,_,_,_,_,_,_,_,_),
				Rule(_,_,_,_,_,_,_,_,_,_)
			 -> {
				int i = isInclude(`r1,`r2);
				if ((i == -2) || (i == -2))  {
					printWarning("correlation",`r1,
						"correlated to",`r2);
				}
			}
		}
		return null;
	}

	private static boolean isEquiv(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return true; }
			Addr4(ip1,smask1,_),Addr4(ip2,smask2,_) 
			&& (smask1 == smask2) 
			-> {
				if ((`ip1 & `smask1) == (`ip2 & `smask2))
					return true;
			}
			Addr6(ipms1,ipls1,smaskms1,smaskls1,_),
			Addr6(ipms2,ipls2,smaskms2,smaskls2,_) 
			&& (smaskms1 == smaskms2) && (smaskls1 == smaskls2) -> {
				if (((`ipms1 & `smaskms1) == (`ipms2 & `smaskms2)) 
				&& ((`ipls1 & `smaskls1) == (`ipls2 & `smaskls2))) {
					return true;
				}
			}
			Addr6(ipms,ipls,_,smaskls,_),Addr4(_,_,_) 
			&& (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isEquiv(`Addr4((int)ipls,(int)smaskls,""),a2);
				}
			}
			Addr4(_,_,_),Addr6(ipms,ipls,_,smaskls,_)
			&& (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isEquiv(a1,`Addr4((int)ipls,(int)smaskls,""));
				}
			}
		}
		return false;
	}

	/* 	returns:
			0 if Rules are equals, 
			1 if r2 is included in r1,
			-1 if r1 include in r2
			NOT_COMPARABLE if the 2 rules are not comparable
	*/
	private static int isInclude(Rule r1, Rule r2) {
		%match(r1,r2) {
			Rule(action1,iface,proto,target,srcaddr1,dstaddr1,srcport,dstport,opts,_),
			Rule(action2,iface,proto,target,srcaddr2,dstaddr2,srcport,dstport,opts,_) -> {
				int i1 = isInclude(`srcaddr1,`srcaddr2),
					i2 = isInclude(`dstaddr1,`dstaddr2),ret = Integer.MAX_VALUE;
				if ((i1 != NOT_COMPARABLE) && (i2 != NOT_COMPARABLE)) {
					if (i1 == 0) {
						ret = i2;
					} else if (i2 == 0) {
						ret = i1;
					} else {
						if ((i1 > 0) && (i2 > 0))
							ret = 1;
						else if ((i1 < 0) && (i2 < 0))
							ret = -1;
					}
					if (`action1 == `action2)
						return ret;
					else
						return (ret == 0 ? 8 : ret * 2);
					
				}
			}
		}
		return NOT_COMPARABLE;
	}

	/* 	returns:
			0 if Addresses are equals, 
			1 if a2 is included in a1,
			-1 if a1 include in a2
			NOT_COMPARABLE if the 2 addresses are not comparable
	*/
	private static int isInclude(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return 0; }
			AddrAny(),Addr4(_,_,_) -> { return 1; }
			Addr4(_,_,_),AddrAny() -> { return -1; }
			AddrAny(),Addr6(_,_,_,_,_) -> { return 1; }
			Addr6(_,_,_,_,_),AddrAny() -> { return -1; }
			Addr4(ip1,smask1,_),Addr4(ip2,smask2,_) -> {
				if (Math.abs(`smask1) < Math.abs(`smask2)) {
					if ((`ip2 & `smask1) == (`ip1 & `smask1))
						return 1;
				} else if (Math.abs(`smask1) > Math.abs(`smask2)) {
					if ((`ip1 & `smask2) == (`ip2 & `smask2))
						return -1;
				} else {
					if ((`ip1 & `smask1) == (`ip2 & `smask2))
						return 0;
				}
			}
			Addr6(ipms1,ipls1,smaskms1,smaskls1,_),
			Addr6(ipms2,ipls2,smaskms2,smaskls2,_) -> {
				if (`smaskms1 == `smaskms2) {
					/* if the most significant mask is filled with 1 bits,
					compare the less significant masks */
					if (`smaskms1 == (0x0L - 0x1L)) {
						if (Math.abs(`smaskls1) < Math.abs(`smaskls2)) {
							if ((`ipls2 & `smaskls1) == (`ipls1 & `smaskls1))
								return 1;
						} else if (Math.abs(`smaskls1) > Math.abs(`smaskls2)) {
							if ((`ipls1 & `smaskls2) == (`ipls2 & `smaskls2))
								return -1;
						} else {
							if ((`ipls1 & `smaskls1) == (`ipls2 & `smaskls1))
								return 0;
						}
					} else {
						if ((`ipms1 & `smaskms1) == (`ipms2 & `smaskms1))
							return 0;
					}
				} else if (Math.abs(`smaskms1) < Math.abs(`smaskms2)) {
					if ((`ipms2 & `smaskms1) == (`ipms1 & `smaskms1))
						return 1;
				} else if (Math.abs(`smaskms1) > Math.abs(`smaskms2)) {
					if ((`ipms1 & `smaskms2) == (`ipms2 & `smaskms2))
						return -1;
				}
			}
			/* IPv4 mapped address has its first 80 bits set to zero, 
			the next 16 set to one, while its last 32 bits represent an 
			IPv4 address*/
			Addr6(ipms,ipls,_,smaskls,_),Addr4(_,_,_) 
			&& (ipms == 0)-> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(`Addr4((int)ipls,(int)smaskls,""),a2);
				}
			}
			Addr4(_,_,_),Addr6(ipms,ipls,_,smaskls,_)
			&& (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(a1,`Addr4((int)ipls,(int)smaskls,""));
				}
			}
		}
		return NOT_COMPARABLE;
	}
}
