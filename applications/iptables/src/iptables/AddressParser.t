package iptables;

import iptables.analyser.types.*;
import iptables.addressparser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class AddressParser {
	%include {iptables/addressparser/AddressParser.tom}
	%include {iptables/analyser/Analyser.tom}
	%include {sl.tom}

	public static final String
		REGEX_HEXA_BYTE = "([0-9a-fA-F]){2}",
		REGEX_IPv4_NUM = "([0-9]{1,2}|[01][0-9]{2}|2[0-4][0-9]|25[0-5])",
		REGEX_IPv4_NUM_STAR = "(" + REGEX_IPv4_NUM + "|\\*)",
		REGEX_IPv4_CIDR_MASKLEN = "([0-2]?[0-9]|3[0-2])",

		REGEX_IPv4_DOTDEC = "^(" + REGEX_IPv4_NUM_STAR + "\\.){3}"
			+ REGEX_IPv4_NUM_STAR + "$",
		REGEX_IPv4_CIDR = "^(" + REGEX_IPv4_NUM + "\\.){0,3}" + REGEX_IPv4_NUM + "/"
			+ REGEX_IPv4_CIDR_MASKLEN + "$",
		REGEX_IPv4_ANYWHERE = "^anywhere$",

		REGEX_IPv6_NUM = "(" + REGEX_HEXA_BYTE + "){2}",
		REGEX_IPv6_NUM_IPv4 = "(" + REGEX_IPv6_NUM + "|(" + REGEX_IPv4_NUM + "\\.){3}"
			+ REGEX_IPv4_NUM + ")",
		REGEX_IPv6_HEXA_NUM = "(((" + REGEX_IPv6_NUM + ":){7}" + REGEX_IPv6_NUM
			+ ")|(" + REGEX_IPv6_NUM + ":){6}(" + REGEX_IPv4_NUM + "\\.){3}"
			+ REGEX_IPv4_NUM + ")",
		REGEX_IPv6_HEXA_START = "((" + REGEX_IPv6_NUM + ":){1,7}:)",
		REGEX_IPv6_HEXA_END = "((::(" + REGEX_IPv6_NUM + ":){0,6})"
			+ REGEX_IPv6_NUM_IPv4 + ")",
		REGEX_IPv6_CIDR_MASKLEN = "(0?[0-9]{1,2}|1[01][0-9]|12[0-8])",

		REGEX_IPv6_HEXA = "^(" + REGEX_IPv6_HEXA_NUM + "|" + REGEX_IPv6_HEXA_START
			+ "|" + REGEX_IPv6_HEXA_END + ")$",
		REGEX_IPv6_CIDR = "^(" + REGEX_IPv6_HEXA_NUM + "/" + REGEX_IPv6_CIDR_MASKLEN
			+ "|" + REGEX_IPv6_HEXA_START + "/" + REGEX_IPv6_CIDR_MASKLEN
			+ "|" + REGEX_IPv6_HEXA_END + "/" + REGEX_IPv6_CIDR_MASKLEN + ")$";


	public static Address addressWrapper(String s) {
		return addressWrapper(addressRawWrapper(s));
	}

	public static Address addressWrapper(AddressRaw ar) {
		%match(ar) {
			AddrAnyRaw() -> { return `AddrAny(); }
			AddrStringDotDecimal4(concString('anywhere')) -> { 
				return `AddrAny();
			}
			AddrStringDotDecimal4(concString('localhost')) -> { 
				return addressWrapper(`AddrStringDotDecimal4("127.0.0.1"));
			}
			AddrStringDotDecimal4(ipStr) -> {
				String[] strs = `ipStr.split("\\.");
				int ip = 0,smask = ~0;
				for (int i = strs.length-1; i >= 0 ;i--) {
					if (strs[i].matches("\\*")) {
						ip <<= 8;
						smask <<= 8;
					} else {
						ip |= (Integer.parseInt(strs[i]) << (8*(strs.length - i - 1)));
					}
				}
				return `Addr4(ip,smask,ipStr);
			}
			AddrStringHexadecimal6(ipStr) -> {
				/* >>> TODO: use address6Wrapper() */
				String[] strs = `ipStr.split(":");
				long ipms = 0,ipls = 0,smaskms = ~0, smaskls = ~0;
				int startoffset = 0, endoffset = 0, midoffset = 4;

				if (strs[strs.length - 1].matches(REGEX_IPv4_DOTDEC)) {
					System.out.println("aaa " +strs[strs.length - 1]);
					String[] strs2 = strs[strs.length - 1].split("\\.");
					for (int k = strs2.length-1; k >= 0 ;k--)
						ipls |= (Integer.parseInt(strs2[k]) << (8*(strs2.length - k - 1)));
					endoffset = 1;
				} else
					endoffset = 0;

				if (`ipStr.matches("^" + REGEX_IPv6_HEXA_END + "$")) {
					/* "::xxxx:xxxx..." case */
					/* The two first strings are empty */
					startoffset = 2;
					if ((strs.length - 2) > 4)
						midoffset = (strs.length - 4);
					else
						midoffset = 2;
				} else if (`ipStr.matches("^" + REGEX_IPv6_HEXA_START + "$")) {
					/* "...xxxx:xxxx::" case */
					/* String.split() removes the ending empty strings from 
						the returned array */
					endoffset = -1;
					if (strs.length > 4)
						midoffset = 4;
					else
						midoffset = strs.length;
				}

				//System.out.println("soff: " + startoffset + " eoff:" + endoffset + " moff:" + midoffset + " len:" + strs.length);
				for (int i = startoffset; i < midoffset; i++) {
					ipms |= (((long)Integer.parseInt(strs[i],16)) 
							<< (16 * ((endoffset>=0 ? midoffset:4)-i-1)));
				}
				for (int i = midoffset; i < (strs.length-(endoffset>=0?endoffset:0)); i++) {
					ipls |= (((long)Integer.parseInt(strs[i],16)) 
							<< (16 * (endoffset>=0 ? (endoffset>0 ? 
											strs.length-endoffset+1-i 
											: strs.length-endoffset-1-i) 
										: (3 - (i - midoffset)))));
				}
				return `Addr6(ipms,ipls,smaskms,smaskls,ipStr);
			}
			AddrStringCIDR4(ipStr) -> {
				String[] strs = `ipStr.split("/");
				int tmp = Integer.parseInt(strs[1]),
					ip = 0, smask = (~0 << (32 - tmp));
				strs = strs[0].split("\\.");
				for (int i = 0; i < (tmp/8) ;i++)
					ip |= (Integer.parseInt(strs[i]) << (8*(4 - i - 1)));
				return `Addr4(ip,smask,ipStr);
			}
			AddrStringCIDR6(ipStr) -> {
				String[] strs = `ipStr.split("/");
				int tmp = Integer.parseInt(strs[1]);
				long ipms = 0,ipls = 0,smaskms = ~0, smaskls = ~0;
				strs = `ipStr.split(":");
			}
		}
		return null;
	}

	private static long address6Wrapper(String s) { return 0L; }

	public static AddressRaw addressRawWrapper(String s) {
		if (s.matches(REGEX_IPv4_ANYWHERE))
			return `AddrAnyRaw();
		else if (s.matches(REGEX_IPv4_DOTDEC))
			return `AddrStringDotDecimal4(s);
		else if (s.matches(REGEX_IPv4_CIDR))
			return `AddrStringCIDR4(s);
		else if (s.matches(REGEX_IPv6_HEXA))
			return `AddrStringHexadecimal6(s);
		else if (s.matches(REGEX_IPv6_CIDR))
			return `AddrStringCIDR6(s);
		return null;
	}

	public static void main(String[] args) {
		String s;
		s = "1.0.*.*";
		System.out.println(s + " = " + addressWrapper(`AddrStringDotDecimal4(s)));
		s = "1.0.1.1/16";
		System.out.println(s + " = " + addressWrapper(`AddrStringCIDR4(s)));
		s = "1.0.*.*";
		System.out.println(s + " = " + addressWrapper(s));
		s = "1.0.1.1/16";
		System.out.println(s + " = " + addressWrapper(s));
		s = "::0001:0.0.1.0";
		System.out.println(s + " = " + addressWrapper(s));
		s = "::0001:0000:0100";
		System.out.println(s + " = " + addressWrapper(s));
		s = "0001::";
		System.out.println(s + " = " + addressWrapper(s));
		s = "0000:0000:0000:0100:0000:0000:0000:0100";
		System.out.println(s + " = " + addressWrapper(s));
	}
}
