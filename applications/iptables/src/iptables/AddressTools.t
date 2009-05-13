package iptables;

import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;
import java.io.*;

public class AddressTools {
	%include {iptables/analyser/Analyser.tom}

	public static final int NOT_COMPARABLE = Integer.MAX_VALUE;

	/* 	returns:
			0 if Addresses are equals, 
			1 if a2 is included in a1,
			-1 if a1 include in a2
			NOT_COMPARABLE if the 2 addresses are not comparable
	*/
	public static int isInclude(Address a1, Address a2) {
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
}
