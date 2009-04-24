package iptables;

import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Analyser {
	%include {iptables/analyser/Analyser.tom}
	%include {sl.tom}

	public static final int NOT_COMPARABLE = -2;

	public static Rules checkIntegrity(Rules rs) {
		try {
			rs = `OutermostId(checkIntersect()).visit(rs);
			
		} catch (VisitFailure vf) { }
		return rs;
	}
 
	public static Rules checkOptimization(Rules rs) {
		try {
			rs = `OutermostId(checkInclusion()).visit(rs);
			
		} catch (VisitFailure vf) { }
		return rs;
	}

	%strategy checkIntersect() extends Identity() {
		visit Rules {
			rs@Rules(
				X*,
				r1@Rule(action1,iface,proto,target,srcaddr1,
					dstaddr1,srcport,dstport,opts),
				Y*,
				r2@Rule(action2,iface,proto,target,srcaddr2,
					dstaddr2,srcport,dstport,opts),
				Z*
			) -> {
				/* looking for equivalence in the rules */
				if (isEquiv(`srcaddr1,`srcaddr2) 
				&& isEquiv(`dstaddr1,`dstaddr2)) {
					if (`action1 == `action2) {
						System.out.println("doubloon: " + `r1);
						return `Rules(X*,r1,Y*,Z*);
					} else {
						System.err.println(
						"conflicting rules:" 
						+ `r1 + "\t/\t" + `r2 + 
						" => removing " + `r2);
						
						return `Rules(X*,r1,Y*,Z*);
					}
				}
			}
		}
	}

	%strategy checkInclusion() extends Identity() { 
		visit Rules {
			rs@Rules(X*,r1,Y*,r2,Z*) -> {
				/* looking for inclusions optimizations */
				int i = isInclude(`r1,`r2);
				if (i == 1) {
					System.out.println("optimization: " + `r2);
					return `Rules(X*,r1,Y*,Z*);
				} else if (i == -1) {
					System.out.println("optimization: " + `r1);
					return `Rules(X*,Y*,r2,Z*);
				} else if (i == 0) {
					System.out.println("optimization-doubloon: " + `r1);
					return `Rules(X*,r1,Y*,Z*);
				}
			}
		}
	}

	public static boolean isEquiv(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return true; }
			Addr4(ip1,smask1),Addr4(ip2,smask2) && (smask1 == smask2) -> {
				if ((`ip1 & `smask1) == (`ip2 & `smask2))
					return true;
			}
			Addr6(ipms1,ipls1,smaskms1,smaskls1),Addr6(ipms2,ipls2,smaskms2,smaskls2) 
			&& (smaskms1 == smaskms2) && (smaskls1 == smaskls2) -> {
				if (((`ipms1 & `smaskms1) == (`ipms2 & `smaskms2)) 
				&& ((`ipls1 & `smaskls1) == (`ipls2 & `smaskls2))) {
					return true;
				}
			}
			Addr6(ipms,ipls,smaskms,smaskls),Addr4(ip,smask) && (ipms == 0)-> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isEquiv(`Addr4((int)ipls,(int)smaskls),a2);
				}
			}
			Addr4(ip,smask),Addr6(ipms,ipls,smaskms,smaskls) && (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isEquiv(a1,`Addr4((int)ipls,(int)smaskls));
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
	public static int isInclude(Rule r1, Rule r2) {
		%match(r1,r2) {
			Rule(action,iface,proto,target,srcaddr1,dstaddr1,srcport,dstport,opts),
			Rule(action,iface,proto,target,srcaddr2,dstaddr2,srcport,dstport,opts) -> {
				int i1 = isInclude(`srcaddr1,`srcaddr2),
					i2 = isInclude(`dstaddr1,`dstaddr2);
				if ((i1 != NOT_COMPARABLE) && (i2 != NOT_COMPARABLE)) {
					if (i1 == 0) {
						return i2;
					} else if (i2 == 0) {
						return i1;
					} else {
						if ((i1 > 0) && (i2 > 0))
							return 1;
						else if ((i1 < 0) && (i2 < 0))
							return -1;
					}
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
	public static int isInclude(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return 0; }
			AddrAny(),Addr4(_,_) -> { return 1; }
			Addr4(_,_),AddrAny() -> { return -1; }
			AddrAny(),Addr6(_,_,_,_) -> { return 1; }
			Addr6(_,_,_,_),AddrAny() -> { return -1; }
			Addr4(ip1,smask1),Addr4(ip2,smask2) -> {
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
			Addr6(ipms1,ipls1,smaskms1,smaskls1),Addr6(ipms2,ipls2,smaskms2,smaskls2) -> {
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
			Addr6(ipms,ipls,smaskms,smaskls),Addr4(ip,smask) && (ipms == 0)-> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(`Addr4((int)ipls,(int)smaskls),a2);
				}
			}
			Addr4(ip,smask),Addr6(ipms,ipls,smaskms,smaskls) && (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(a1,`Addr4((int)ipls,(int)smaskls));
				}
			}
		}
		return NOT_COMPARABLE;
	}

	public static void main(String[] args) {
		Rule r1 = `Rule(
			Accept(),
			Iface("eth0"),
			TCP(),
			In(),
			AddrAny(),
			Addr4((16+256+4096+65536),(~0 << 24)),
			PortAny(),
			Port(80),
			NoOpt()
		);
		Rule r2 = `Rule(
			Drop(),
			Iface("eth0"),
			TCP(),
			In(),
			AddrAny(),
			Addr4((16+256+4096+65536),(~0 << 24)),
			PortAny(),
			Port(80),
			NoOpt()
		);
		Rule r3 = `Rule(
			Accept(),
			Iface("eth0"),
			TCP(),
			In(),
			AddrAny(),
			Addr4((4096+65536),(~0 << 16)),
			PortAny(),
			Port(80),
			NoOpt()
		);

		Rules rs = 	`Rules(r1,r2,r3),rsn;

		/* printing tests */
		System.out.println("\n#printing test: " +rs);

		/* isEquivAddress tests */
		Address a1,a2;
		a1 = `Addr4(256,(~0 << 8));
		a2 = `Addr4(312,(~0 << 8));
		System.out.println("\n# isEquivAddress test: isEquivaddr(" + `a1 + "," 
			+ `a2 + "):" + isEquiv(a1,a2));
		a1 = `Addr6(256,256,~0L,(~0L << 8));
		a2 = `Addr6(256,312,~0L,(~0L << 8));
		System.out.println("\n# isEquivAddress test: isEquivaddr(" + `a1 + "," 
			+ `a2 + "):" + isEquiv(a1,a2));
		a1 = `Addr4(256,(~0 << 8));
		a2 = `Addr6(0,(256 | (0xffffL << 32)),~0L,(~0L << 8));
		System.out.println("\n# isEquivAddress test: isEquivaddr(" + `a1 + "," 
			+ `a2 + "):" + isEquiv(a1,a2));

		/* checkIntegrity tests */
		System.out.println("\n# checkIntegrity test: doubloon");
		rsn = checkIntegrity(`Rules(r1,r1));
		System.out.println("RSN: " + rsn);
		System.out.println("\n# checkIntegrity test: conflict");
		checkIntegrity(`Rules(r1,r2));
		System.out.println("\n# checkIntegrity test: nothing wrong");
		checkIntegrity(`Rules(r1,r3));
		System.out.println("\n# checkIntegrity test: doubloon & conflict");
		checkIntegrity(`Rules(r1,r2,r3,r1));

		/* checkOptimization tests */
		System.out.println("\n# checkOptimization test");
		System.out.println("[[Rules: " + rs + "]]\n");
		rs = checkOptimization(rs);
		System.out.println("\n[[Rules: " + rs + "]]");
	}
}
