package firewall;

import firewall.ast.types.*;
import java.util.*;
import java.io.*;

public class Compiler {
  %include { ast/Ast.tom  }

  public static void pretty(Instruction inst) {
    %match(inst) {
      	Accept() -> {
     		System.out.println("ACCEPT");
      	}
     	Classify() -> {
	       	System.out.println("CLASSIFY");
       	}
        ClusterIp() -> {
        	System.out.println("CLUSTERIP");
        }
        ConnMark() ->{
        	System.out.println("ACCEPT");
        }
        ConnSecMark() ->{
        	System.out.println("CONNSECMARK");
        }
        Dnat() -> {
        	System.out.println("DNAT");
        }
        Drop() -> {
        	System.out.println("DROP");
        }
        Dscp() -> {
        	System.out.println("DSCP");
        }
        Ecn() -> {
        	System.out.println("ECN");
        }
        Log() -> {
        	System.out.println("LOG");
        }
        Mark() -> {
        	System.out.println("MARK");
        }
        Masquerade() -> {
        	System.out.println("MASQUERADE");
        }
        Mirror() -> {
        	System.out.println("MIRROR");
        }
        NetMap() -> {
        	System.out.println("NETMAP");
        }
        NfLog() -> {
        	System.out.println("NFLOG");
        }
        NfQueue() -> {
        	System.out.println("NFQUEUE");
        }
        NoTrack() -> {
        	System.out.println("NOTRACK");
        }
        Queue() -> {
        	System.out.println("QUEUE");
        }
        Redirect() -> {
	        System.out.println("REDIRECT");
        }
        Reject() -> {
        	System.out.println("REJECT");
        }
        Return() -> {
        	System.out.println("RETURN");
        }
        User_Rule(user_rule:String) -> {//RAJOUTER USER_RULE
        	System.out.println("USER_RULE");
        }
        Same() -> {
        	System.out.println("SAME");
        }
        SecMark() -> {
        	System.out.println("SECMARK");
        }
        Snat() -> {
        	System.out.println("SNAT");
        }
        TcpMss() -> {
        	System.out.println("TCPMSS");
        }
        Tos() -> {
        	System.out.println("TOS");
        }
        Trace() -> {
        	System.out.println("TRACE");
        }
        Ttl() -> {
        	System.out.println("TTL");
        }
        Ulog() -> {
        	System.out.println("ULOG");
        }
        Ipv4OptSstRip() -> {
        	System.out.println("IPV40PTSSTRIP");
        }
        Set() -> {
        	System.out.println("SET");
        }
        Tarpit() -> {
        	System.out.println("TARPIT");
        }
    }
  }
}
