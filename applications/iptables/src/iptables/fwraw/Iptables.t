package iptables.fwraw;

import iptables.*;
import iptables.analyser.types.*;
import iptables.analyserwrapper.types.*;
import tom.library.sl.*; 
import java.util.*;

/* >>> TODO: use superclass */
public class Iptables {
	%include { ../analyserwrapper/AnalyserWrapper.tom }
	%include { ../analyser/Analyser.tom }
	%include { sl.tom }

	%strategy wrap() extends Identity() {
		visit IptablesBlocks {
			IptablesBlocks(b@IptablesBlock,_*) -> {
				return `OutermostId(wrap()).visit(b);
			}
		}
		visit IptableBlock {
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
		visit IptableRules {
			IptablesRules(r@IptableRule(
					a@Action,
					p@Protocol,
					as@Address,
					ad@Address,
					o@IptablesOptions),
				_*) -> {
					return `Rule(a,IfaceAnyp(),p,t,as,ad,
							PortAny(),PortAny(),NoOpt());
				}
			}
		}
	}
}
