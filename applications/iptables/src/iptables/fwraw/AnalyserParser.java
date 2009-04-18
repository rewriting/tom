package iptables.fwraw;

import iptables.*;
import iptables.analyser.types.*;
import iptables.analyserwrapper.types.*;

public interface AnalyserParser {
	public Action parseAction(String s);
	public Protocol parseProto(String s);
	public Target parseTarget(String s);
	public Address parseAddrSrc(String s);
	public Address parseAddrDst(String s);
	public Port parsePortSrc(String s);
	public Port parsePortDst(String s);
	public Options parseOptions(String s);
}