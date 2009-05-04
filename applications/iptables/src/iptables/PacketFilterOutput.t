package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class PacketFilterOutput {
	%include { analyser/Analyser.tom }
	%include { sl.tom }

	public final static String 
		CMD = "",
		OPT_PROTOCOL = "proto",
		OPT_IFACE = "on",
		OPT_ADDRFAM = "af",
		OPT_IPSRC = "from",
		OPT_IPDST = "to",
		OPT_PORT = "port",
		OPT_FLAG = "flag",

		ACTION_ACCEPT = "pass",
		ACTION_DROP = "block drop",
		ACTION_REJECT = "block return",
		ACTION_REDIR = "rdr",
		ACTION_LOG = "log",

		DIR_INPUT = "in",
		DIR_OUTPUT = "out",
		DIR_ALL = "all",

		AF_IPV4 = "inet",
		AF_IPV6 = "inet6",

		STATE_KEEP = "keep state",
		STATE_MODULATE = "modulate state",
		STATE_SYNPROXY = "synproxy state",

		ADDR_ANY = "any";
		
	public static String wrapAction(Action a) {
		%match(a) {
			Accept() 	-> { return ACTION_ACCEPT; }
			Drop() 		-> { return ACTION_DROP; }
			Reject() 	-> { return ACTION_REJECT; }
		}
		return "";
	}

	public static String wrapTarget(Target t) {
		%match(t) {
			In()		-> { return DIR_INPUT; }
			Out()		-> { return DIR_OUTPUT; }
		}
		return "";
	}

	public static String wrapProtocol(Protocol p) {
		String s = OPT_PROTOCOL + " ";
		%match(p) {
			TCP()	-> { return s + "tcp"; }
			UDP()	-> { return s + "udp"; }
			IPv4()	-> { return s + "ip4"; }
			IPv6()	-> { return s + "ip6"; }
			ICMP()	-> { return s + "icmp"; }
			ARP()	-> { return s + "arp"; }
			RIP()	-> { return s + "rip"; }
			Ethernet()	-> { return s + "eth"; }
		}
		return "";
	}

	public static String wrapAddress(Address a,String opt) {
		%match(a) {
			Addr4(_,_,str) -> { return opt + " " + `str; }
			Addr6(_,_,_,_,str) -> { return opt + " " + `str; }
			AddrAny() -> { return opt + " " + ADDR_ANY; }
		}
		return "";
	}

	public static String wrapAddressFamily(Address src,Address dest) {
		%match(src,dest) {
			Addr4(_,_,_),Addr4(_,_,_) -> { return  AF_IPV4 + " "; }
			Addr6(_,_,_,_,_),Addr6(_,_,_,_,_) -> { return  AF_IPV6 + " "; }
			Addr6(_,_,_,_,_),Addr4(_,_,_) -> { return  AF_IPV6 + " "; }
			Addr4(_,_,_),Addr6(_,_,_,_,_) -> { return  AF_IPV6 + " "; }
			AddrAny(),Addr6(_,_,_,_,_) -> { return  AF_IPV6 + " "; }
			Addr6(_,_,_,_,_),AddrAny() -> { return  AF_IPV6 + " "; }
			AddrAny(),Addr4(_,_,_) -> { return  AF_IPV4 + " "; }
			Addr4(_,_,_),AddrAny() -> { return  AF_IPV4 + " "; }
		}
		return "";
	}

	public static String wrapPort(Port p) {
		%match(p) {
			Port(i) -> { return OPT_PORT + " " + `i; }
		}
		return "";
	}

	public static String wrapOptGlob(GlobalOptions gopts) {
		%match(gopts) {
			GlobalOpts(o@!NoGlobalOpt(),X*) -> {
				String opt = "";
				%match(o) {
				}
				return opt + wrapOptGlob(`X*);
			}
		}
		return "";
	}

	public static String wrapOptProto(ProtocolOptions popts) {
		%match(popts) {
			ProtoOpts(o@!NoProtoOpt(),X*) -> {
				String opt = "";
				%match(o) {
				}
				return opt + wrapOptProto(`X*);
			}
		}
		return "";
	}

	public static String wrapOptStates(States states) {
		%match(states) {
			States(s@!StateAny(),X*) -> {
				return STATE_KEEP + " ";
			}
		}
		return "";
	}

	public static String wrapOptions(Options opts) {
		%match(opts) {
			Opt(glob,proto,states) -> {
				return wrapOptGlob(`glob) 
					+ wrapOptProto(`proto)
					+ wrapOptStates(`states);
			}
		}
		return "";
	}

	public static void printCmdPolicy(Action a,Target t) {
		System.out.println( wrapAction(`a) + " " + wrapTarget(`t) + " " 
			+ DIR_ALL);
	}

	public static void printOpt(String optname) {
		if (optname.length() > 0)
			System.out.print(optname + " ");
	}

	public static void print2Opt(String optname, String arg) {
		if ((optname.length() > 0) && (arg.length() > 0))
			System.out.print(optname + " " + arg + " ");
	}

	public static void printNewLine() { System.out.println(""); }

	public static void printTranslation(Rules rs) {
		%match(rs) {
			Rules(Rule(action,IfaceAny(),ProtoAny(),target,
					AddrAny(),AddrAny(),PortAny(),PortAny(),
					NoOpt(),_),
				X*
			) -> {
				printCmdPolicy(`action,`target);
				printTranslation(`X*);
			}

			Rules(
				Rule(action,iface,proto,tar,srcaddr,
					dstaddr,srcport,dstport,opts,_),
				X*
			) -> {
				%match(action) {
					Log() && Rules(Rule(action2,iface,proto,
					tar,srcaddr,dstaddr,srcport,dstport,
					opts,_)) << rs -> {
						printOpt(wrapAction(`action2));
						printOpt(wrapTarget(`tar));
						printOpt(ACTION_LOG);
					}
					!Log() -> {
						printOpt(wrapAction(`action));
						printOpt(wrapTarget(`tar));
					}
				}


				%match(iface) {
					Iface(name) -> {
						print2Opt(OPT_IFACE,`name);
					}
				}

				printOpt(wrapAddressFamily(`srcaddr,`dstaddr));
				printOpt(wrapProtocol(`proto));
				printOpt(wrapAddress(`srcaddr,OPT_IPSRC));
				printOpt(wrapPort(`srcport));
				printOpt(wrapAddress(`dstaddr,OPT_IPDST));
				printOpt(wrapPort(`dstport));
				printOpt(wrapOptions(`opts));
				printNewLine();
				printTranslation(`X*);
			}
		}
	}
}

