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
			IptablesBlocks(b@IptablesBlock,_*) -> {
				return `OutermostId(wrapBlock()).visit(`b);
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
			_*
		) -> {
			Port sport = `PortAny(), dport = `PortAny();

			%match(o) {
				IptablesOptions(
					_*,SourcePort(ns),
					DestPort(nd),_*
				) -> {
					sport = `Port(ns);
					dport = `Port(nd);
				}

				IptablesOptions(
					_*,DestPort(nd),
					SourcePort(ns),_*
				) -> {
					sport = `Port(ns);
					dport = `Port(nd);
				}

				IptablesOptions(_*,SourcePort(ns),_*) -> {
					sport = `Port(ns);
				}

				IptablesOptions(_*,DestPort(nd),_*) -> { 
					dport = `Port(nd);
				}
			}

			return `Rule(a,IfaceAny(),p,t,asrc,adest,sport,dport,
				NoOpt());
		}
	}
	}
}
