package firewall;

import firewall.ast.types.*;
import tom.library.sl.*; 
import java.util.*;

public class Pretty {

  %include {firewall/ast/Ast.tom}
  %include {sl.tom}
  
  public String toString(File f) {
    StringBuffer sb = new StringBuffer() ;
    %match(f) {
      Blocks(_*,b,_*) -> {
        sb.append(toString(`b)+"\n\n") ;
      }
    }
    return sb.toString() ;
  }

  public String toString(Block b) {
    StringBuffer sb = new StringBuffer() ;
    %match(b) {
      Block(r,il) -> {
        return "Chain " + toString(`r)+ "\n" + toString(`il) ;
      }
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Rule r) {
    %match(r) {
      Input()       -> {return "INPUT";}
      Forward()     -> {return "FORWARD";}
      Output() 	    -> {return "OUTPUT";}
      Prerouting()  -> {return "PREROUTING";}
      Postrouting() -> {return "POSTROUTING";}
      UserRuleDef(ur)  -> {return `ur;}
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(InstructionList il) {
    StringBuffer sb = new StringBuffer() ;
    %match(il) {
      InstructionList(_*,i,_*) -> {
        sb.append(toString(`i) + "\n") ;
      }
    }
    return sb.toString() ;
  }

  public String toString(Instruction i) {
    %match(i) {
      Ins(t,p,o,s,d,lo) -> {
        return toString(`t) + "\t" + toString(`p) + " " + toString(`o) + " " + toString(`s) + "\t\t" + toString(`d) 
	       		    + "\t\t" + toString(`lo) ;
      }
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Target t) {
    %match(t) {
      Accept()        -> {return "ACCEPT";}
      Classify()      -> {return "CLASSIFY";}
      ClusterIp()     -> {return "CLUSTERIP";}
      ConnMark()      -> {return "CONN_MARK";}
      ConnSecMark()   -> {return "CONNSECMARK";}
      Dnat()	      -> {return "DNAT";}
      Drop()	      -> {return "DROP";}
      Dscp()	      -> {return "DSCP";}
      Ecn()	      -> {return "ECN";}
      Log()	      -> {return "LOG";}
      Mark()	      -> {return "MARK";}
      Masquerade()    -> {return "MASQUERADE";}
      Mirror()	      -> {return "MIRROR";}
      NetMap()	      -> {return "NETMAP";}
      NfLog()	      -> {return "NFLOG";}
      NfQueue()	      -> {return "NFQUEUE";}
      NoTrack()	      -> {return "NOTRACK";}
      Queue()	      -> {return "QUEUE";}
      Redirect()      -> {return "REDIRECT";}
      Reject()	      -> {return "REJECT";}
      Return()	      -> {return "RETURN";}
      UserRuleCall(ur)    -> {return `ur;}
      Same()	      -> {return "SAME";}
      SecMark()	      -> {return "SECMARK";}
      Snat()	      -> {return "SNAT";}
      TcpMss()	      -> {return "TCPMSS";}
      Tos()	      -> {return "TOS";}
      Trace()	      -> {return "TRACE";}
      Ttl()	      -> {return "TTL";}
      Ulog()	      -> {return "ULOG";}
      Ipv4OptSstRip() -> {return "IPV4OPTSSTRIP";}
      Set()	      -> {return "SET";}
      Tarpit()	      -> {return "TARPIT";}
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Protocol p) {
    %match(p) {
      All_()  -> {return "all ";}
      Icmp() -> {return "icmp";}
      Udp()  -> {return "udp ";}
      Tcp()  -> {return "tcp ";}
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Opts o) {
    %match(o) {
      None() -> {return "--";}
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Communication c) {
    %match(c) {
      Anywhere()  -> {return "anywhere";}
      Localhost() -> {return "localhost";}
      Ip_Addr(ip) -> {return `ip;}
    }
    throw new RuntimeException("should not be there");
  }

  public String toString(Options lo) {
    %match(lo) {
      Options(s) -> {return `s;}
    }
    throw new RuntimeException("should not be there");
  }

  public static void main(String[] args) {
    Pretty pretty = new Pretty();
    System.out.println(pretty.toString(
				Blocks(
					Block(
						Input(),
						InstructionList(
								Ins(
								    Accept(),
								    All(),
								    None(),
								    Anywhere(),
								    Anywhere(),
								    "optionsssssss"
								    )
								)
					     )
				      )
			       )
	               ) ;
  }

}
