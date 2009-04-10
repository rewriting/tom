package iptables;

import iptables.ast.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Analyser {

	%include {iptables/ast/Ast.tom}
	%include {sl.tom}

	public static void main(String[] args) {
		Rules rules = 	`rules(
				rule(
					Accept(),
					Addr(0,0xffffffff),
					Addr((16+256+4096+65536),0xff),
					Port(0),
					Port(80)),
				rule(
					Drop(),
					Addr(0,0xffffffff),
					Addr((15+256+4096+65536),0xff),
					Port(0),
					Port(21))
			);
/*
		%match(rules) {
			rules(_*,rule(Accept(),Addr(srcip,srcsmask),Addr(dstip,dstsmask),Port(srcport),Port(dstport)),_*) -> {
				System.out.println("ACCEPT: " +`srcip + ":" + `srcport + "->" + `dstip + ":" + `dstport);
			}
		}
*/
		/* printing tests */
		System.out.println("#printing test: " +rules);

		/* isEquivAddress tests */
		Address a1,a2;
		a1 = `Addr(16,0xfffffffe);
		a2 = `Addr(17,0xfffffffe);
		System.out.println("# isEquivAddress test: isEquivaddr(" + `a1 + "," + `a2 + "):" + isEquivAddress(a1,a2));

		/* checkIntegrity tests */
		Rule r1 = `rule(
			Accept(),
			AddrAny(),
			Addr((16+256+4096+65536),0xff),
			PortAny(),
			Port(80)
		);
		Rule r2 = `rule(
			Drop(),
			AddrAny(),
			Addr((16+256+4096+65536),0xff),
			PortAny(),
			Port(80)
		);
		Rule r3 = `rule(
			Accept(),
			AddrAny(),
			Addr((4096+65536),0xffff),
			PortAny(),
			Port(80)
		);

		System.out.println("# checkIntegrity test: doubloon");
		checkIntegrity(`rules(r1,r1));
		System.out.println("# checkIntegrity test: conflict");
		checkIntegrity(`rules(r1,r2));
		System.out.println("# checkIntegrity test: nothing wrong");
		checkIntegrity(`rules(r1,r3));
		System.out.println("# checkIntegrity test: doubloon & conflict");
		checkIntegrity(`rules(r1,r2,r3,r1));
	}

	public static Rules checkIntegrity(Rules rs) {
		%match(rs) {
			rules(
				_*,
				r1@rule(action1,srcaddr1,dstaddr1,srcport1,dstport1),
				_*,
				r2@rule(action2,srcaddr2,dstaddr2,srcport2,dstport2),
				_*
			) -> {
				if (
					isEquivAddress(`srcaddr1,`srcaddr2) 
					&& isEquivAddress(`dstaddr1,`dstaddr2)
					&& isEquivPort(`srcport1,`srcport2)
					&& isEquivPort(`dstport1,`dstport2)
				) {
					%match(action1,action2) {
						Accept(),Accept() -> { System.out.println("ACCEPT doubloon"); }
						Drop(),Drop() -> { System.out.println("DROP doubloon"); }
						Accept(),Drop() -> { System.out.println("conflicting rules"); }
						Drop(),Accept() -> { System.out.println("conflicting rules"); }
					}
					/* >>> TODO: need to remove r1 or r2 from rs */
				}
			}
		}
		return rs;
	}
	public static boolean isEquivAddress(Address a1, Address a2) {
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

	public static boolean isEquivPort(Port p1, Port p2) {
		%match(p1,p2) {
			PortAny(),PortAny() -> { return true; }
			Port(number1),Port(number2) && (number1 == number2) -> { return true; }
		}
		return false;
	}
}
