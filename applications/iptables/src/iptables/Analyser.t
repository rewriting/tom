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

		%match(rules) {
			rules(_*,rule(Accept(),Addr(srcip,srcsmask),Addr(dstip,dstsmask),Port(srcport),Port(dstport)),_*) -> {
				System.out.println("ACCEPT: " +`srcip + ":" + `srcport + "->" + `dstip + ":" + `dstport);
			}
		}

		System.out.println(rules);

		Address a1,a2;
		a1 = `Addr(16,0xfffffffe);
		a2 = `Addr(17,0xfffffffe);

		System.out.println("equiv(" + `a1 + "," + `a2 + "):" + equivAddress(a1,a2));
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
