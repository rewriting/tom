package iptables;

import iptables.ast.types.*;
import iptables.analyserwrapper.types.*;
import tom.library.sl.*; 
import java.util.*;

public class AnalyserWrapper {
	%include {iptables/analyserwrapper/AnalyserWrapper.tom}
	%include {iptables/ast/Ast.tom}
	%include {sl.tom}

	public static final String
		REGEX_IPv4_NUM = "([0-9]{1,2}|[01][0-9]{2}|2[0-4][0-9]|25[0-5])",
		REGEX_IPv4_NUM_STAR = "([0-9]{1,2}|[01][0-9]{2}|2[0-4][0-9]|25[0-5]|\\*)",
		REGEX_IPv4_CIDR_MASKLEN = "([0-2]?[0-9]|3[0-2])",
		REGEX_IPv4_DOTDEC = "^(" + REGEX_IPv4_NUM_STAR + "\\.){3}"
			+ REGEX_IPv4_NUM_STAR + "$",
		REGEX_IPv4_CIDR = "^(" + REGEX_IPv4_NUM + "\\.){0,3}" + REGEX_IPv4_NUM + "/"
			+ REGEX_IPv4_CIDR_MASKLEN + "$",
		REGEX_IPv4_ANYWHERE = "^anywhere$";


	public static Address addressWrapper(String s) {
		return addressWrapper(addressRawWrapper(s));
	}

	public static Address addressWrapper(AddressRaw ar) {
		%match(ar) {
			AddrAnyRaw() -> { return `AddrAny(); }
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
				return `Addr4(ip,smask);
			}
			AddrStringCIDR4(ipStr) -> {
				String[] strs = `ipStr.split("/");
				int tmp = Integer.parseInt(strs[1]),
					ip = 0, smask = (~0 << (32 - tmp));
				strs = strs[0].split("\\.");
				for (int i = 0; i < (tmp/8) ;i++)
					ip |= (Integer.parseInt(strs[i]) << (8*(4 - i - 1)));
				return `Addr4(ip,smask);
			}
		}
		return null;
	}

	public static AddressRaw addressRawWrapper(String s) {
		if (s.matches(REGEX_IPv4_ANYWHERE))
			return `AddrAnyRaw();
		else if (s.matches(REGEX_IPv4_DOTDEC))
			return `AddrStringDotDecimal4(s);
		else if (s.matches(REGEX_IPv4_CIDR))
			return `AddrStringCIDR4(s);
		return null;
	}

	public static void main(String[] args) {
		String s;
		s = "1.0.*.*";
		System.out.println(s + " : " + addressWrapper(`AddrStringDotDecimal4(s)));
		s = "1.0.1.1/16";
		System.out.println(s + " : " + addressWrapper(`AddrStringCIDR4(s)));
		s = "1.0.*.*";
		System.out.println(s + " : " + addressWrapper(s));
		s = "1.0.1.1/16";
		System.out.println(s + " : " + addressWrapper(s));
	}
}