package iptables;

import iptables.ast.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Analyser {
	public static final int NOT_COMPARABLE = -2;

	%include {iptables/ast/Ast.tom}
	%include {sl.tom}

	public static Rules checkIntegrity(Rules rs) {
		%match(rs) {
			rules(
				_*,
				r1@rule(action1,srcaddr1,dstaddr1,srcport,dstport),
				_*,
				r2@rule(action2,srcaddr2,dstaddr2,srcport,dstport),
				_*
			) -> {
				/* looking for equivalence in the rules */
				if (isEquiv(`srcaddr1,`srcaddr2) && isEquiv(`dstaddr1,`dstaddr2)) {
					if (`action1 == `action2)
						System.out.println("doubloon: " + `r1);
					else
						System.out.println("conflicting rules:" + `r1 + " / " + `r2);
					/* >>> TODO: need to remove r1 or r2 from rs */
				}
			}
		}
		return rs;
	}

	public static Rules checkOptimization(Rules rs) {
		%match(rs) {
			rules(_*,r1,_*,r2,_*) -> {
				/* looking for inclusions optimizations */
				int i = compareTo(`r1,`r2);
				if (i == 1) {
					/* >>> TODO: remove r2 */
					System.out.println("optimization: " + `r2);
				} else if (i == -1) {
					/* >>> TODO: remove r1 */
					System.out.println("optimization: " + `r1);
				} else if (i == 0)
					System.out.println("optimization-doubloon: " + `r1);
			}
		}
		return rs;
	}

	public static boolean isEquiv(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return true; }
			Addr(ip,smask),AddrAny() && ((ip == 0) && (smask == 0xffffffff))
				-> { return true; }
			AddrAny(),Addr(ip,smask) && ((ip == 0) && (smask == 0xffffffff))
				-> { return true; }
			Addr(ip1,smask1),Addr(ip2,smask2) && (smask1 == smask2) -> {
				if ((`ip1 & `smask1) == (`ip2 & `smask2))
					return true;
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
	public static int compareTo(Rule r1, Rule r2) {
		%match(r1,r2) {
			rule(action,srcaddr1,dstaddr1,srcport,dstport),
			rule(action,srcaddr2,dstaddr2,srcport,dstport) -> {
				int i1 = compareTo(`srcaddr1,`srcaddr2),
					i2 = compareTo(`dstaddr1,`dstaddr2);
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
	public static int compareTo(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return 0; }
			Addr(ip1,smask1),Addr(ip2,smask2) -> {
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
		}
		return NOT_COMPARABLE;
	}

	public static void main(String[] args) {
		Rule r1 = `rule(
			Accept(),
			AddrAny(),
			Addr((16+256+4096+65536),0xff000000),
			PortAny(),
			Port(80)
		);
		Rule r2 = `rule(
			Drop(),
			AddrAny(),
			Addr((16+256+4096+65536),0xff000000),
			PortAny(),
			Port(80)
		);
		Rule r3 = `rule(
			Accept(),
			AddrAny(),
			Addr((4096+65536),0xffff0000),
			PortAny(),
			Port(80)
		);

		Rules rs = 	`rules(r1,r2,r3);

		/* printing tests */
		System.out.println("#printing test: " +rs);

		/* isEquivAddress tests */
		Address a1,a2;
		a1 = `Addr(256,0xffffff00);
		a2 = `Addr(312,0xffffff00);
		System.out.println("# isEquivAddress test: isEquivaddr(" + `a1 + "," + `a2 + "):" + isEquiv(a1,a2));

		/* checkIntegrity tests */

		System.out.println("# checkIntegrity test: doubloon");
		checkIntegrity(`rules(r1,r1));
		System.out.println("# checkIntegrity test: conflict");
		checkIntegrity(`rules(r1,r2));
		System.out.println("# checkIntegrity test: nothing wrong");
		checkIntegrity(`rules(r1,r3));
		System.out.println("# checkIntegrity test: doubloon & conflict");
		checkIntegrity(`rules(r1,r2,r3,r1));

		/* checkOptimization tests */
		System.out.println("# checkOptimization test");
		checkOptimization(rs);
		
	}
}
