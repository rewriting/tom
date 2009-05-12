package iptables;

import iptables.analyser.types.*;
import iptables.firewall.types.*;

public interface Wrapper {
	public Rules wrap(FirewallRules fr);
}
