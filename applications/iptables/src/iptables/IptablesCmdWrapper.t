package iptables;

import iptables.iptablescmd.types.*;
import iptables.analyser.types.*;
import iptables.addressparser.types.*;
import iptables.firewall.types.*;
import tom.library.sl.*; 
import java.util.*;

public class IptablesCmdWrapper implements Wrapper {
	%include { iptables/firewall/Firewall.tom }
	%include { sl.tom }

	public Rules wrap(FirewallRules fr) {
		%match (fr) {
			FirewallRulesIptablesCmd(icr) -> { 
				return wrapRules(`icr);
			}
		}
		return null;
	}

	private Rules wrapRules(IptablesCmdRules icrs) {
		%match(icrs) {
		IptablesCmdRules(IptablesCmdPolicy(tar,act,input),X*) -> {
			return `RulesL(Rule(act,IfaceAny(),ProtoAny(),tar,
					AddrAny(),AddrAny(),PortAny(),PortAny(),
					NoOpt(),input),
				wrapRules(X*));
		}

		IptablesCmdRules(IptablesCmdAppend(tar,opts,act,in),X*) -> {
			boolean opt = false;

			Port sport = `PortAny(), dport = `PortAny();
			AddressRaw asrc = `AddrAnyRaw(), adst = `AddrAnyRaw();
			Protocol proto = `ProtoAny();
			Iface iface = `IfaceAny();

			Options options = `NoOpt();
			States states = `States(StateAny());
			GlobalOptions globalOpt = `GlobalOpts(NoGlobalOpt());
			ProtocolOptions protoOpt = `ProtoOpts(NoProtoOpt());

			%match(opts) {
				IptablesCmdOpts(_*,IptablesCmdProto(p),_*)
				-> {
					proto = `p;
				}
				IptablesCmdOpts(_*,IptablesCmdAddressSrc(as),_*)
				-> {
					asrc = `as;
				}

				IptablesCmdOpts(_*,IptablesCmdAddressDest(ad),_*)
				-> {
					adst = `ad;
				}

				IptablesCmdOpts(_*,IptablesCmdPortSrc(sp),_*)
				-> {
					sport = `sp;
				}

				IptablesCmdOpts(_*,IptablesCmdPortDest(dp),_*)
				 -> {
					dport = `dp;
				}

				IptablesCmdOpts(_*,IptablesCmdIfaceOpt(i),_*)
				-> {
					iface = `i;
				}
				IptablesCmdOpts(_*,IptablesCmdStatesOpt(s),_*)
				-> {
					states = `s;
					opt = true;
				}
			}
			
			if (opt)
				options = `Opt(globalOpt,protoOpt,states);

			return `RulesL(Rule(act,iface,proto,tar,
					AddressParser.addressWrapper(asrc),
					AddressParser.addressWrapper(adst),
					sport,dport,options,in),
				wrapRules(X*));
		}
	}
		return `Rules();
	}
}
