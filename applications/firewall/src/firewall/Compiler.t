package firewall;

import firewall.ast.types.*;
import java.util.*;
import java.io.*;
import tom.library.sl.*;

public class Compiler {

   %include {firewall/ast/Ast.tom}
   %include {sl.tom}

   // Eliminate rules for doubles in the same chain
   %strategy elimDoubles() extends `Identity() {
      visit IpTablesOutput {
     	   Blocks(lb1*,Block(ch,policy,InstructionList(li1*,instr,li2*,instr,li3*)),lb2*) -> {
     	      return `Blocks(lb1*,Block(ch,policy,InstructionList(li1*,instr,li2*,li3*)),lb2*) ;
	   	}
      }
   }

   // Eliminate rules made unnecessary by the default policy
   // ex :
   // chain INPUT policy DROP
   //   drop ...
   %strategy elimUseless() extends `Identity() {
      visit IpTablesOutput {
         Blocks(lb1*,Block(rule,PolicyAccept(),InstructionList(li1*,Ins(Accept(),_,_,_,_,_),li2*)),lb2*) -> {
	    		return `Blocks(lb1*,Block(rule,PolicyAccept(),InstructionList(li1*,li2*)),lb2*) ;
	 		}
	 		Blocks(lb1*,Block(rule,PolicyDrop(),InstructionList(li1*,Ins(Drop(),_,_,_,_,_),li2*)),lb2*) -> {
	    		return `Blocks(lb1*,Block(rule,PolicyDrop(),InstructionList(li1*,li2*)),lb2*) ;
	 		}
      }
   }
   
   // Display a warning for unused user rules
   %strategy warnUnuse() extends `Identity() {
   	visit IpTablesOutput {
   		Blocks(_*,Block(UserRuleDef(name),Ref(0),_),_*) -> {
   			System.out.println("Rule \""+`name+"\" is unuse.") ;
   		}
   	}
   }
 
   // Display banishments from a fail2ban
   %strategy dispBanishments() extends `Identity() {
   	visit IpTablesOutput {
   		Blocks(_*,Block(UserRuleDef(name),_,InstructionList(_*,Ins(Drop(),prot,_,src,dest,_),_*)),_*) -> {
   			if ((`name).startsWith("fail2ban")) System.out.println(`name+" : "+Pretty.toString(`src)+" -> "+Pretty.toString(`dest)+" for protocol : "+Pretty.toString(`prot)) ;
   		}
   	}
   }

	// Group rules by network
	%strategy dispByNetwork() extends `Identity() {
		visit IpTablesOutput {
			Blocks() -> {
				
			}
		}
	}

	private String nw ;

  public static void main(String[] args) {
     IpTablesOutput t = `Blocks(
		Block(Input(),PolicyDrop(),InstructionList(
			Ins(Drop(),All_(),None(),Anywhere(),Anywhere(),Opts("blablabla")),
			Ins(Accept(),All_(),None(),Anywhere(),Anywhere(),Opts("le")),
			Ins(Accept(),All_(),None(),Localhost(),Localhost(),Opts("citron")),
			Ins(Accept(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),12),Ipv4_Addr(Ipv4(5,6,7,80),17),Opts("dit :")),
			Ins(Accept(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),8),Ipv4_Addr(Ipv4(5,6,7,80),16),Opts("dit :"))
			)
		),
		Block(Forward(),PolicyAccept(),InstructionList(
			Ins(Accept(),All_(),None(),Anywhere(),Anywhere(),Opts("blablabla")),
			Ins(UserRuleCall("Rule 1"),Icmp(),None(),Localhost(),Anywhere(),Opts("plus")),
			Ins(Drop(),Tcp(),None(),Anywhere(),Localhost(),Opts("un"))
			)
		),
		Block(Postrouting(),PolicyDrop(),InstructionList(
			Ins(Mirror(),Udp(),None(),Ipv6_Addr("...9.10.11.12..."),Anywhere(),Opts("zeste"))
			)
		),
		Block(UserRuleDef("fail2ban-apache"),Ref(0),InstructionList(
			Ins(Drop(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),24),Localhost(),Opts("blablabla")),
			Ins(Log(),All_(),None(),Anywhere(),Anywhere(),Opts("!!!!!!"))
			)     			
		)
	       ) ;
     
     System.out.println(Pretty.toString(t));
     try{
			System.out.println(Pretty.toString(`RepeatId(dispBanishments()).visit(t)));
			//`RepeatId(dispLocalRules()).visit(t) ;
     }catch(tom.library.sl.VisitFailure e){
       System.out.println("echec : " + e.getMessage());
     }
  }
  /*
	public void dispByNetwork(IpTablesOutput ito) {
		IpTablesOutput t = `ito;
		try {
			boolean stop = false;
			while (!stop) {
				%match(t) {
					Blocks(_*,Block(rule,policy,InstructionList(Ins(_,_,_,source,destination,_),_*)),_*) -> {
						networkFromIPv4AndMask
					}
					_ -> {stop = true ;}
				}
			}
			
			`RepeatId(dispByNetwork()).visit(t);
		}catch(tom.library.sl.VisitFailure e) {
			System.out.println("echec : "+e.getMessage());
		}
	}
*/
}
