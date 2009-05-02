package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class IptablesOutput {
	%include { analyser/Analyser.tom }
	%include { sl.tom }

	public final static String 
		CMD = "iptables",
		OPT_FLUSH = "--flush",
		OPT_APPEND = "--append",
		OPT_POLICY = "--policy",
		OPT_TABLE = "--table",
		OPT_PROTOCOL = "--protocol",
		OPT_IFACE_IN = "--in-interface",
		OPT_IFACE_OUT = "--out-interface",
		OPT_IPSRC = "--source",
		OPT_IPDST = "--destination",
		OPT_PORTSRC = "--source-port",
		OPT_PORTDST = "--destination-port",
		OPT_ACTION = "--jump",

		ACTION_ACCEPT = "ACCEPT",
		ACTION_DROP = "DROP",
		ACTION_REJECT = "REJECT",
		ACTION_LOG = "LOG",

		CHAIN_INPUT = "INPUT",
		CHAIN_OUTPUT = "OUTPUT",
		CHAIN_FORWARD = "FORWARD",

		TABLE_FILTER = "filter",
		TABLE_NAT = "nat";
		
	public static String wrapAction(Action a) {
		%match(a) {
			Accept() 	-> { return ACTION_ACCEPT; }
			Drop() 		-> { return ACTION_DROP; }
			Reject() 	-> { return ACTION_REJECT; }
			Log() 		-> { return ACTION_LOG; }
		}
		return null;
	}

	public static String wrapTarget(Target t) {
		%match(t) {
			In()		-> { return CHAIN_INPUT; }
			Out()		-> { return CHAIN_OUTPUT; }
			Forward()	-> { return CHAIN_FORWARD; }
		}
		return null;
	}

	public static String wrapProtocol(Protocol p) {
		%match(p) {
			TCP()	-> { return "tcp"; }
			UDP()	-> { return "udp"; }
			IPv4()	-> { return "ip4"; }
			IPv6()	-> { return "ip6"; }
			ICMP()	-> { return "icmp"; }
			ARP()	-> { return "arp"; }
			RIP()	-> { return "rip"; }
			Ethernet()	-> { return "eth"; }
		}
		return null;
	}

	public static String wrapPort(Port p) {
		%match(p) {
			Port(i) -> { return `i + ""; }
		}
		return null;
	}

	public static void printCmdAppend(String tab, Target t) {
		System.out.print(CMD + " " + OPT_TABLE + " " + tab + " "
			+ OPT_APPEND + " " + wrapTarget(`t) + " ");
	}

	public static void printCmdPolicy(String tab,Action a,Target t) {
		System.out.println(CMD + " " + OPT_TABLE + " " + tab + " " 
			+ OPT_POLICY + " " + wrapAction(`a) + " " 
			+ wrapTarget(`t));
	}

	public static void print2Opt(String optname, String arg) {
		System.out.print(optname + " " + arg + " ");
	}

	public static String wrapAddress(Address a) {
		%match(a) {
			Addr4(_,_,str) -> { return `str; }
			Addr6(_,_,_,_,str) -> { return `str; }
		}
		return null;
	}

	public static void printNewLine() { System.out.println(""); }

	public static void printTranslation(Rules rs) {
		%match(rs) {
			Rules(
				Rule(action,iface,proto,tar,srcaddr,
					dstaddr,srcport,dstport,opts,_),
				X*
			) -> {
				printCmdAppend(TABLE_FILTER,`tar);
				%match(proto) {
					!ProtoAny() -> {
						print2Opt(OPT_PROTOCOL,
							wrapProtocol(`proto));
					}
				}

				%match(iface) {
					Iface(name) -> {
						print2Opt(OPT_IFACE_IN,`name);
						print2Opt(OPT_IFACE_OUT,`name);
					}
				}

				%match(srcport) {
					!PortAny() -> {
						print2Opt(OPT_PORTSRC,
							wrapPort(`srcport));
					}
				}
				%match(dstport) {
					!PortAny() -> {
						print2Opt(OPT_PORTDST,
							wrapPort(`dstport));
					}
				}
				%match(srcaddr) {
					!AddrAny() -> {
						print2Opt(OPT_IPSRC,
							wrapAddress(`srcaddr));
					}
				}
				%match(dstaddr) {
					!AddrAny() -> {
						print2Opt(OPT_IPDST,
							wrapAddress(`dstaddr));
					}
				}
				print2Opt(OPT_ACTION,wrapAction(`action));
				printNewLine();
				printTranslation(`X*);
			}

			Rules(Rule(action,IfaceAny(),ProtoAny(),target,
					AddrAny(),AddrAny(),PortAny(),PortAny(),

					NoOpt(),_),
				X*
			) -> {
				printCmdPolicy(TABLE_FILTER,`action,`target);
				printTranslation(`X*);
			}
		}
	}
}
