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

	public static Rule parse(String input) {
		%match (input) {
			concString(action*,' ',proto*,' ',source*,' ',dest*) -> {
				return `rule(
						parseAction(action),
						IfaceAny(),
						parseProto(proto),
						parseTarget(""),
						parseAddrSrc(source),
						parseAddrDst(dest),
						parsePortSrc(""),
						parsePortDst(""),
						parseOptions(""));
			}
		}
		return null;
	}

	public static Action parseAction(String s) {
		return `Accept();
	}

	public static Protocol parseProto(String s) {
		return `ProtoAny();
	}

	public static Target parseTarget(String s) {
		return `In();
	}

	public static Address parseAddrSrc(String s) {
		return AnalyserWrapper.addressWrapper(s);
	}

	public static Address parseAddrDst(String s) {
		return AnalyserWrapper.addressWrapper(s);
	}

	public static Port parsePortSrc(String s) {
		return `PortAny();
	}

	public static Port parsePortDst(String s) {
		return `PortAny();
	}

	public static Options parseOptions(String s) {
		return `NoOpt();
	}

	public static void main(String[] argv) {
		System.out.println(parse("ACCEPT all 127.0.0.1 anywhere"));
	}
}
