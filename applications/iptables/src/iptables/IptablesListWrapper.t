package iptables;

import iptables.iptableslist.types.*;
import iptables.analyser.types.*;
import iptables.addressparser.types.*;
import iptables.firewall.types.*;
import tom.library.sl.*; 
import java.util.*;

public class IptablesListWrapper implements Wrapper {
	%include { iptables/firewall/Firewall.tom }
	%include { sl.tom }

	public Rules wrap(FirewallRules fr) {
		%match (fr) {
			FirewallRulesIptablesList(ilb) -> { 
				return wrapBlocks(`ilb);
			}
		}
		return null;
	}

	private Rules wrapBlocks(IptablesListBlocks ilbs) {
		%match(ilbs) {
			IptablesListBlocks(ilb,X*) -> {
				return `RulesA(
					wrapBlock(ilb),
					wrapBlocks(X*));
			}
		}
		return `Rules();
	}

	private Rules wrapBlock(IptablesListBlock ilb) {
		%match(ilb) {
			IptablesListBlock(t,a,is,in) -> {
				return `RulesL(
					Rule(a,IfaceAny(),ProtoAny(),t,
						AddrAny(),AddrAny(),
						PortAny(),PortAny(),NoOpt(),in),
					wrapRule(is,t)
				);
			}
		}
		return `Rules();
	}

	private Rules wrapRule(IptablesListRules ilrs,Target t) {
		%match(ilrs) {
		IptablesListRules(
			IptablesListRule(
				act,
				p,
				asrc,
				adst,
				o,
				in),
			X*
		) -> {
			boolean opt = false;

			Port sport = `PortAny(), dport = `PortAny();

			Options options = `NoOpt();
			States states = `States(StateAny());
			GlobalOptions globalOpt = `GlobalOpts(NoGlobalOpt());
			ProtocolOptions protoOpt = `ProtoOpts(NoProtoOpt());

			%match(o) {
				IptablesListOptions(
					_*,
					IptablesListPortSrc(ns),
					_*) -> {
					sport = `Port(ns);
				}

				IptablesListOptions(
					_*,
					IptablesListPortDest(nd),
					_*) -> { 
					dport = `Port(nd);
				}
				IptablesListOptions(_*,IptablesListStates(s),_*) -> {
					states = `s;
					opt = true;
				}
			}
			
			if (opt)
				options = `Opt(globalOpt,protoOpt,states);

			return `RulesL(Rule(act,IfaceAny(),p,t,
					AddressParser.addressWrapper(asrc),
					AddressParser.addressWrapper(adst),
					sport,dport,options,in),
				wrapRule(X*,t));
		}
	}
		return `Rules();
	}
}
