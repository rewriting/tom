package iptables;

import iptables.iptables.types.*;
import iptables.analyser.types.*;
import iptables.analyserwrapper.types.*;
import tom.library.sl.*; 
import java.util.*;

/* >>> TODO: use superclass */
public class Iptables {
	%include { analyserwrapper/AnalyserWrapper.tom }
	%include { iptables/Iptables.tom }
	%include { sl.tom }

	public static Rules wrapBlocks(IptablesBlocks ibs) {
		%match(ibs) {
			IptablesBlocks(ib,X*) -> {
				return `RulesA(
					wrapBlock(ib),
					wrapBlocks(X*));
			}
		}
		return null;
	}

	public static Rules wrapBlock(IptablesBlock ib) {
		%match(ib) {
			IptablesBlock(t,a,is) -> {
				return `RulesL(
					Rule(a,IfaceAny(),ProtoAny(),t,
						AddrAny(),AddrAny(),
						PortAny(),PortAny(), NoOpt()),
					wrapRule(is,t)
				);
			}
		}
		return null;
	}

	public static Rules wrapRule(IptablesRules irs,Target t) {
		%match(irs) {
		IptablesRules(
			IptablesRule(
				act,
				p,
				asrc,
				/* adst@Address, */
				o),
			X*
		) -> {
			boolean opt = false;

			Port sport = `PortAny(), dport = `PortAny();

			Options options = `NoOpt();
			States states = `States(StateAny());
			GlobalOptions globalOpt = `GlobalOpts();
			ProtocolOptions protoOpt = `ProtoOpts();

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

			return `RulesL(Rule(act,IfaceAny(),p,t,asrc,AddrAny(),sport,
						dport,options),
					wrapRule(X*,t));
		}
	}
		return null;
	}
}
