package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class PacketFilterPrinter extends Printer {
	%include { analyser/Analyser.tom }
	%include { sl.tom }

	private final String 
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
		
	private String wrapAction(Action a) {
		%match(a) {
			Accept() 	-> { return ACTION_ACCEPT; }
			Drop() 		-> { return ACTION_DROP; }
			Reject() 	-> { return ACTION_REJECT; }
		}
		return "";
	}

	private String wrapTarget(Target t) {
		%match(t) {
			In()		-> { return DIR_INPUT; }
			Out()		-> { return DIR_OUTPUT; }
		}
		return "";
	}

	private String wrapProtocol(Protocol p) {
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

	private String wrapAddress(Address a,String opt) {
		%match(a) {
			(Addr4|Addr6)[str=str] -> { return opt + " " + `str; }
			AddrAny() -> { return opt + " " + ADDR_ANY; }
		}
		return "";
	}

	private String wrapAddressFamily(Address src,Address dest) {
		%match(src,dest) {
			Addr4[],Addr4[] -> { return  AF_IPV4; }
			Addr6[],Addr6[] -> { return  AF_IPV6; }
			Addr6[],Addr4[] -> { return  AF_IPV6; }
			Addr4[],Addr6[] -> { return  AF_IPV6; }
			AddrAny(),Addr6[] -> { return  AF_IPV6; }
			Addr6[],AddrAny() -> { return  AF_IPV6; }
			AddrAny(),Addr4[] -> { return  AF_IPV4; }
			Addr4[],AddrAny() -> { return  AF_IPV4; }
		}
		return "";
	}

	private String wrapPort(Port p) {
		%match(p) {
			Port(i) -> { return OPT_PORT + " " + `i; }
		}
		return "";
	}

	private String wrapOptGlob(GlobalOptions gopts) {
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

	private String wrapOptProto(ProtocolOptions popts) {
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

	private String wrapOptStates(States states) {
		%match(states) {
			States(s@!StateAny(),X*) -> {
				return STATE_KEEP + " ";
			}
		}
		return "";
	}

	private String wrapOptions(Options opts) {
		%match(opts) {
			Opt(glob,proto,states) -> {
				return wrapOptGlob(`glob) 
					+ wrapOptProto(`proto)
					+ wrapOptStates(`states);
			}
		}
		return "";
	}

	private void printCmdPolicy(Action a,Target t) {
		printStr( wrapAction(`a) + " " + wrapTarget(`t) + " " 
			+ DIR_ALL + "\n");
	}

	public void prettyPrinter(Rules rs) {
		%match(rs) {
			Rules(Rule(action,IfaceAny(),ProtoAny(),target,
					AddrAny(),AddrAny(),PortAny(),PortAny(),
					NoOpt(),_),
				X*
			) -> {
				printCmdPolicy(`action,`target);
				prettyPrinter(`X*);
				return;
			}
			Rules(Rule(action,iface,proto,tar,srcaddr,dstaddr,
					srcport,dstport,opts,_),
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
				prettyPrinter(`X*);
			}
		}
	}
}

