package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import iptables.analyserwrapper.types.*;
import iptables.firewall.types.*;
import tom.library.sl.*; 
import java.util.*;

public class IptablesWrapper implements Wrapper {
	%include { iptables/firewall/Firewall.tom }
	%include { sl.tom }

	public Rules wrap(FirewallRules fr) {
		%match (fr) {
			FirewallRules(ib@IptablesBlock) -> { 
				return wrapBlocks(`ib);
			}
		}
		return null;
	}

	private Rules wrapBlocks(IptablesBlocks ibs) {
		%match(ibs) {
			IptablesBlocks(ib,X*) -> {
				return `RulesA(
					wrapBlock(ib),
					wrapBlocks(X*));
			}
		}
		return `Rules();
	}

	private Rules wrapBlock(IptablesBlock ib) {
		%match(ib) {
			IptablesBlock(t,a,is,in) -> {
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

	private Rules wrapRule(IptablesRules irs,Target t) {
		%match(irs) {
		IptablesRules(
			IptablesRule(
				act,
				p,
				asrc,
				adst@Address,
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
				IptablesOptions(_*,SourcePort(ns),_*) -> {
					sport = `Port(ns);
				}

				IptablesOptions(_*,DestPort(nd),_*) -> { 
					dport = `Port(nd);
				}
				IptablesOptions(_*,IptablesStates(s),_*) -> {
					states = `s;
					opt = true;
				}
			}
			
			if (opt)
				options = `Opt(globalOpt,protoOpt,states);

			return `RulesL(Rule(act,IfaceAny(),p,t,
					AnalyserWrapper.addressWrapper(asrc),
					AddrAny(),sport,dport,options,in),
				wrapRule(X*,t));
		}
	}
		return `Rules();
	}
}
