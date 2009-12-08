package iptables;

import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;
import java.io.*;

public class AddressTools {
	%include {iptables/analyser/Analyser.tom}

	public static final int 
		NOT_COMPARABLE = Integer.MAX_VALUE,
		INCLUDES = 1,
		INCLUDED = -1,
		EQUALS = 0;

	/* 	returns:
			EQUALS if Addresses are equals, 
			INCLUDES if a2 is included in a1,
			INCLUDED if a1 included in a2
			NOT_COMPARABLE if the 2 addresses are not comparable
	*/
	public static int isInclude(Address a1, Address a2) {
		%match(a1,a2) {
			AddrAny(),AddrAny() -> { return EQUALS; }
			AddrAny(),Addr4[] -> { return INCLUDES; }
			Addr4[],AddrAny() -> { return INCLUDED; }
			AddrAny(),Addr6[] -> { return INCLUDES; }
			Addr6[],AddrAny() -> { return INCLUDED; }
			Addr4(ip1,smask1,_),Addr4(ip2,smask2,_) -> {
				/* A smaller mask implies a smaller sub-network,
				 * then we compare the address&mask
				 */
				if ((Math.abs(`smask1) < Math.abs(`smask2)) 
				&& ((`ip2 & `smask1) == (`ip1 & `smask1)))
						return INCLUDES;
				else if ((Math.abs(`smask1) > Math.abs(`smask2))
				&& ((`ip1 & `smask2) == (`ip2 & `smask2)))
						return INCLUDED;
				else if ((`ip1 & `smask1) == (`ip2 & `smask2))
						return EQUALS;
			}
			/* Useless to compare the host part in IPv6 */
			Addr6(ipnet1,_,smask1,_),Addr6(ipnet2,_,smask2,_) -> {
				/* A smaller mask implies a smaller sub-network,
				 * then we compare the address&mask
				 */
				if ((Math.abs(`smask1) < Math.abs(`smask2)) 
				&& ((`ipnet2 & `smask1) == (`ipnet1 & `smask1)))
						return INCLUDES;
				else if ((Math.abs(`smask1) > Math.abs(`smask2))
				&& ((`ipnet1 & `smask2) == (`ipnet2 & `smask2)))
						return INCLUDED;
				else if ((`ipnet1 & `smask1) == (`ipnet2 & `smask2))
						return EQUALS;
			}
			/* IPv4 mapped address has its first 80 bits set to zero, 
			 * the next 16 set to one, while its last 32 bits
			 * represent an IPv4 address
			 */
			Addr6(ipms,ipls,smask,_),Addr4[] && (ipms == 0)-> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(
					`Addr4((int)ipls,(int)smask,""),a2);
				}
			}
			Addr4[],Addr6(ipms,ipls,smask,_) && (ipms == 0) -> {
				if ((`ipls & (0xffffL << 32)) == (0xffffL << 32)) {
					return isInclude(a1,
					`Addr4((int)ipls,(int)smask,""));
				}
			}
		}
		return NOT_COMPARABLE;
	}

	private static boolean isEquiv(Address a1, Address a2) {
		return (isInclude(a1,a2) == 0);
	}
}
