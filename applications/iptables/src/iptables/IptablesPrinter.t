package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import tom.library.sl.*; 
import java.util.*;

public class IptablesPrinter extends Printer {
	%include { analyser/Analyser.tom }
	%include { sl.tom }

	private final String 
		CMD = "iptables",
		OPT_FLUSH = "--flush",
		OPT_APPEND = "--append",
		OPT_POLICY = "--policy",
		OPT_TABLE = "--table",
		OPT_STATE = "--state",
		OPT_PROTOCOL = "--protocol",
		OPT_IFACE_IN = "--in-interface",
		OPT_IFACE_OUT = "--out-interface",
		OPT_IPSRC = "--source",
		OPT_IPDST = "--destination",
		OPT_PORTSRC = "--source-port",
		OPT_PORTDST = "--destination-port",
		OPT_ACTION = "--jump",
		OPT_LOAD_STATE = "-m state",

		ACTION_ACCEPT = "ACCEPT",
		ACTION_DROP = "DROP",
		ACTION_REJECT = "REJECT",
		ACTION_LOG = "LOG",

		CHAIN_INPUT = "INPUT",
		CHAIN_OUTPUT = "OUTPUT",
		CHAIN_FORWARD = "FORWARD",

		TABLE_FILTER = "filter",
		TABLE_NAT = "nat",

		STATE_NEW = "NEW",
		STATE_RELATED = "RELATED",
		STATE_ESTABLISHED = "ESTABLISHED",
		STATE_INVALID = "INVALID";
		
	private String wrapAction(Action a) {
		%match(a) {
			Accept() 	-> { return ACTION_ACCEPT; }
			Drop() 		-> { return ACTION_DROP; }
			Reject() 	-> { return ACTION_REJECT; }
			Log() 		-> { return ACTION_LOG; }
		}
		return null;
	}

	private String wrapTarget(Target t) {
		%match(t) {
			In()		-> { return CHAIN_INPUT; }
			Out()		-> { return CHAIN_OUTPUT; }
			Forward()	-> { return CHAIN_FORWARD; }
		}
		return null;
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
		}
		return "";
	}

	private String wrapPort(Port p, String opt) {
		%match(p) {
			Port(i) -> { return opt + " " + `i; }
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
				String opt = OPT_STATE + " ";
				%match(s) {
					New() -> { opt += STATE_NEW + " "; }
					Related() -> { opt += STATE_RELATED + " "; }
					Established() -> { opt += STATE_ESTABLISHED + " "; }
					Invalid() -> { opt += STATE_INVALID + " "; }
					StateAny() -> { opt = ""; }
				}
				return opt + wrapOptStates(`X*);
			}
		}
		return "";
	}

	private String wrapOptions(Options opts) {
		%match(opts) {
			Opt(glob,proto,states) -> {
				String states = wrapOptStates(`states);
				return wrapOptGlob(`glob) 
					+ wrapOptProto(`proto)
					+ (states.length() > 0 ?
						(OPT_LOAD_STATE + " " + states)
						: "");
			}
		}
		return "";
	}

	private void printCmdAppend(String tab, Target t) {
		printStr(CMD + " " + OPT_TABLE + " " + tab + " " + OPT_APPEND 
			+ " " + wrapTarget(`t) + " ");
	}

	private void printCmdPolicy(String tab,Action a,Target t) {
		printStr(CMD + " " + OPT_TABLE + " " + tab + " " + OPT_POLICY 
			+ " " + wrapAction(`a) + " " + wrapTarget(`t) + "\n");
	}


	public void prettyPrinter(Rules rs) {
		%match(rs) {
			Rules(Rule(action,IfaceAny(),ProtoAny(),target,
					AddrAny(),AddrAny(),PortAny(),PortAny(),
					NoOpt(),_),
				X*
			) -> {
				printCmdPolicy(TABLE_FILTER,`action,`target);
				prettyPrinter(`X*);
				return;
			}

			Rules(Rule(action,iface,proto,tar,srcaddr,dstaddr,
					srcport,dstport,opts,_),
				X*
			) -> {
				printCmdAppend(TABLE_FILTER,`tar);

				%match(iface) {
					Iface(name) -> {
						print2Opt(OPT_IFACE_IN,`name);
						print2Opt(OPT_IFACE_OUT,`name);
					}
				}

				printOpt(wrapProtocol(`proto));
				printOpt(wrapPort(`srcport,OPT_PORTSRC));
				printOpt(wrapPort(`dstport,OPT_PORTDST));
				printOpt(wrapAddress(`srcaddr,OPT_IPSRC));
				printOpt(wrapAddress(`dstaddr,OPT_IPDST));
				printOpt(wrapOptions(`opts));
				print2Opt(OPT_ACTION,wrapAction(`action));
				printNewLine();
				prettyPrinter(`X*);
			}
		}
	}
}
