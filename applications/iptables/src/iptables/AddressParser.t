package iptables;

import iptables.analyser.types.*;
import iptables.addressparser.types.*;
import tom.library.sl.*; 
import java.util.*;
import java.net.*;

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
		REGEX_IPv4_CIDR = "^(" + REGEX_IPv4_NUM + "\\.){0,3}" 
			+ REGEX_IPv4_NUM + "/" + REGEX_IPv4_CIDR_MASKLEN + "$",
		REGEX_IPv4_ANYWHERE = "^anywhere$",

		REGEX_IPv6_NUM = "(" + REGEX_HEXA_BYTE + "){2}",
		REGEX_IPv6_NUM_IPv4 = "(" + REGEX_IPv6_NUM + "|(" 
			+ REGEX_IPv4_NUM + "\\.){3}" + REGEX_IPv4_NUM + ")",
		REGEX_IPv6_HEXA_NUM = "(((" + REGEX_IPv6_NUM + ":){7}"
			+ REGEX_IPv6_NUM + ")|(" + REGEX_IPv6_NUM + ":){6}("
			+ REGEX_IPv4_NUM + "\\.){3}" + REGEX_IPv4_NUM + ")",
		REGEX_IPv6_HEXA_START = "((" + REGEX_IPv6_NUM + ":){1,7}:)",
		REGEX_IPv6_HEXA_END = "((::(" + REGEX_IPv6_NUM + ":){0,6})"
			+ REGEX_IPv6_NUM_IPv4 + ")",
		REGEX_IPv6_CIDR_MASKLEN = "(0?[0-9]{1,2}|1[01][0-9]|12[0-8])",

		REGEX_IPv6_HEXA = "^(" + REGEX_IPv6_HEXA_NUM + "|"
			+ REGEX_IPv6_HEXA_START+"|"+REGEX_IPv6_HEXA_END + ")$",
		REGEX_IPv6_CIDR = "^(" + REGEX_IPv6_HEXA_NUM + "/"
			+ REGEX_IPv6_CIDR_MASKLEN + "|" + REGEX_IPv6_HEXA_START 
			+ "/" + REGEX_IPv6_CIDR_MASKLEN + "|"
			+ REGEX_IPv6_HEXA_END + "/" + REGEX_IPv6_CIDR_MASKLEN
			+ ")$";


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
				return addressWrapper(
					`AddrStringDotDecimal4("127.0.0.1"));
			}
			AddrStringDotDecimal4(ipStr) -> {
				return addressWrapper(
					`AddrStringCIDR4(ipStr + "/32"));
			}
			AddrStringHexadecimal6(ipStr) -> {
				return addressWrapper(
					`AddrStringCIDR6(ipStr + "/64"));
			}
			AddrStringCIDR4(ipStr) -> {
				String[] strs = `ipStr.split("/");
				int ip = 0, smask = 
					(~0 << (32 - Integer.parseInt(strs[1])));
				byte[] bs={0};
				try {
					bs = (InetAddress.getByName(`ipStr))
						.getAddress();
				} catch(Exception e) {};
				
				for (int i = bs.length-1; i >= 0; i--)
					ip |= (bs[bs.length-i-1] << (8 * i));

				return `Addr4(ip,smask,ipStr);
			}
			AddrStringCIDR6(ipStr) -> {
				String[] strs = `ipStr.split("/");
				long ipms = 0, ipls = 0, smask = 
					(~0L << (32 - Integer.parseInt(strs[1])));
				byte[] bs={0};
				try {
					bs = (InetAddress.getByName(`ipStr))
						.getAddress();
				} catch(Exception e) {};
				
				for (int i = bs.length-1; i >= (bs.length/2); i--)
					ipms |= (bs[bs.length-i-1] << (8 * i));
				for (int i = (bs.length/2)-1; i >= 0; i--)
					ipls |= (bs[bs.length-i-1] << (8 * i));

				return `Addr6(ipms,ipls,smask,ipStr);
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
