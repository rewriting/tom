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

	%strategy wrapBlocks() extends Identity() {
		visit IptablesBlocks {
			IptablesBlocks(b@IptablesBlock,X*) -> {
				return `IptablesBlocks(
					OutermostId(wrapBlock()).visit(b),
					X*);
			}
		}
	}

	%strategy wrapBlock() extends Identity() {
		visit IptablesBlock {
			IptablesBlock(t@Target,a@Action,is@IptablesRules) -> {
				return `Rules(
					Rule(a,IfaceAny(),ProtoAny(),t,
						AddrAny(),AddrAny(),
						PortAny(),PortAny(), NoOpt()),
					OutermostId(wrapRule(t)).visit(is)
				);
			}
		}
	}

	%strategy wrapRule(t:Target) extends Identity() {
	visit IptablesRules {
		IptablesRules(
			r@IptablesRule(
				a@Action,
				p@Protocol,
				asrc@Address,
				adest@Address,
				o@IptablesOptions),
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

			return `Rules(	Rule(a,IfaceAny(),p,t,asrc,adest,sport,
						dport,options),
					X*);
		}
	}
	}
}
