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
   		Blocks(_*,Block(UserRuleDef(name),_,InstructionList(_*,Ins(_,Drop(),prot,_,src,dest,_),_*)),_*) -> {
   			if ((`name).startsWith("fail2ban")) System.out.println(`name+" : "+Pretty.toString(`src)+" -> "+Pretty.toString(`dest)+" for protocol : "+Pretty.toString(`prot)) ;
   		}
   	}
   }

   // Display shadowed anomalies
	%strategy dispShadowedAnomalies() extends `Identity() {
		visit IpTablesOutput {
			Blocks(_*,Block(r,_,InstructionList(_*,Ins(n1,t1,p1,op1,s1,d1,o1),_*,Ins(n2,t2,p2,op2,s2,d2,o2),_*)),_*) -> {
				%match {
					t1!=t2 -> {
						if (`p1.equals(`p2) || `p1.equals(`All_())) {
							if (`s1.equals(`s2) || isIncludeIn(`s2,`s1)) {
								if (`d1.equals(`d2) || isIncludeIn(`d2,`d1))
									dispSA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
							}
						}
					}
				}
			}
		}
	}

	private static void dispSA(Rule r , Instruction i1 , Instruction i2) {
		System.out.println("WARNING : on rule "+Pretty.toString(r));
		System.out.println("Instruction line "+Pretty.toString(i2));
		System.out.println("is shadowed by");
		System.out.println("Instruction line "+Pretty.toString(i1));
		System.out.println();
	}


	// Display redondant anomalies
	%strategy dispRedondantAnomalies() extends `Identity() {
		visit IpTablesOutput {
			Blocks(_*,Block(r,_,InstructionList(_*,Ins(n1,t1,p1,op1,s1,d1,o1),_*,Ins(n2,t2,p2,op2,s2,d2,o2),_*)),_*) -> {
				%match {
					t1==t2 -> {
						%match {
							p1==p2 -> {
								if (`s1.equals(`s2)) {
									if (isIncludeIn(`d1,`d2) || isIncludeIn(`d2,`d1))
										dispRA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
								}
								else {
									if (isIncludeIn(`s1 ,`s2)) {
										if (`d1.equals(`d2) || isIncludeIn(`d2,`d1))
											dispRA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
									}
								}
							}
							p1==All_() -> {
								if (`s1.equals(`s2) || isIncludeIn(`s2,`s1)) {
									if (`d1.equals(`d2) || isIncludeIn(`d2,`d1))
										dispRA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
								}
							}
							p2==All_() -> {
								if (`s1.equals(`s2) || isIncludeIn(`s1,`s2)) {
									if (`d1.equals(`d2) || isIncludeIn(`d1,`d2))
										dispRA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
								}
							}
						}
					}
				}
			}
		}
	}

	private static void dispRA(Rule r , Instruction i1 , Instruction i2) {
		System.out.println("WARNING : on rule "+Pretty.toString(r));
		System.out.println("Instruction line "+Pretty.toString(i1));
		System.out.println("and");
		System.out.println("Instruction line "+Pretty.toString(i2));
		System.out.println("are redondant.");
		System.out.println();
	}

	// Display generalization anomalies
	%strategy dispGeneralizationAnomalies() extends `Identity() {
		visit IpTablesOutput {
			Blocks(_*,Block(r,_,InstructionList(_*,Ins(n1,t1,p1,op1,s1,d1,o1),_*,Ins(n2,t2,p2,op2,s2,d2,o2),_*)),_*) -> {
				%match {
					t1!=t2 -> {
						%match {
							p1==p2 -> {
								if (`s1.equals(`s2)) {
									if (isIncludeIn(`d1,`d2))
									dispGA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
								}
								else {
									if (isIncludeIn(`s1,`s2)) {
										if (`d1.equals(`d2) || isIncludeIn(`d1,`d2))
										dispGA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
									}
								}
							}
							p2==All_() -> {
								if (`s1.equals(`s2) || isIncludeIn(`s1,`s2)) {
									if (`d1.equals(`d2) || isIncludeIn(`d1,`d2))
										dispGA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
								}
							}
						}
					}
				}
			}
		}
	}

	private static void dispGA(Rule r , Instruction i1 , Instruction i2) {
		System.out.println("WARNING : on rule "+Pretty.toString(r));
		System.out.println("Instruction line "+Pretty.toString(i2));
		System.out.println("is a generalization of");
		System.out.println("Instruction line "+Pretty.toString(i1));
		System.out.println();
	}


	// Display correlated anomalies
	%strategy dispCorrelatedAnomalies() extends `Identity() {
		visit IpTablesOutput {
			Blocks(_*,Block(r,_,InstructionList(_*,Ins(n1,t1,p1,op1,s1,d1,o1),_*,Ins(n2,t2,p2,op2,s2,d2,o2),_*)),_*) -> {
				%match {
					p1==p2 -> {
						if (isIncludeIn(`s2,`s1)) {
							if (isIncludeIn(`d1,`d2))
								dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
						}
						else {
							if (isIncludeIn(`s1,`s2)) {
								if (isIncludeIn(`d2,`d1))
									dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
							}
						}
					}
					p1==All_() -> {
						if (`s1.equals(`s2) || isIncludeIn(`s2,`s1)) {
							if (isIncludeIn(`d1,`d2))
								dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
						}
						else {
							if (isIncludeIn(`s1,`s2)) {
								if (`d1.equals(`d2) || isIncludeIn(`d2,`d1) || isIncludeIn(`d1,`d2))
									dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
							}
						}
					}
					p2==All_() -> {
						if (`s1.equals(`s2) || isIncludeIn(`s1,`s2)) {
							if (isIncludeIn(`d2,`d1))
								dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
						}
						else {
							if (isIncludeIn(`s2,`s1)) {
								if (`d1.equals(`d2) || isIncludeIn(`d2,`d1) || isIncludeIn(`d1,`d2))
									dispCA(`r,`Ins(n1,t1,p1,op1,s1,d1,o1),`Ins(n2,t2,p2,op2,s2,d2,o2));
							}
						}
					}
				}
			}
		}
	}

	private static void dispCA(Rule r , Instruction i1 , Instruction i2) {
		System.out.println("WARNING : on rule "+Pretty.toString(r));
		System.out.println("Instruction line "+Pretty.toString(i1));
		System.out.println("and");
		System.out.println("Instruction line "+Pretty.toString(i2));
		System.out.println("are correlated.");
		System.out.println();
	}


	/**
	 * This method tests if c1<c2.
	 * @param c1 first communication
	 * @param c2 second communication
	 * @return true if c1<c2, false otherwise
	 */
	private static boolean isIncludeIn(Communication c1 , Communication c2) {
		%match {
			c2==Anywhere() && c1!=Anywhere() -> {return true;}
			Ipv4_Addr(Ipv4(i11,i12,i13,i14),m1)<<c1 && Ipv4_Addr(Ipv4(i21,i22,i23,i24),m2)<<c2 -> {
				return Operation_IP.areSubNetworks(`i11,`i12,`i13,`i14,`m1,`i21,`i22,`i23,`i24,`m2);
			}
		}
		return false;
	}

	private String nw ;

  public static void main(String[] args) {
     IpTablesOutput t = `Blocks(
		Block(Input(),PolicyDrop(),InstructionList(
			Ins(1,Drop(),All_(),None(),Anywhere(),Anywhere(),Opts("blablabla")),
			Ins(2,Accept(),All_(),None(),Anywhere(),Anywhere(),Opts("le")),
			Ins(3,Accept(),All_(),None(),Localhost(),Localhost(),Opts("citron")),
			Ins(4,Accept(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),12),Ipv4_Addr(Ipv4(5,6,7,80),17),Opts("dit :")),
			Ins(5,Drop(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),12),Ipv4_Addr(Ipv4(5,6,7,80),17),Opts("dit :"))
			)
		),
		Block(Forward(),PolicyAccept(),InstructionList(
			Ins(1,Accept(),All_(),None(),Anywhere(),Anywhere(),Opts("blablabla")),
			Ins(2,UserRuleCall("Rule 1"),Icmp(),None(),Localhost(),Anywhere(),Opts("plus")),
			Ins(3,Drop(),Tcp(),None(),Anywhere(),Localhost(),Opts("un"))
			)
		),
		Block(Postrouting(),PolicyDrop(),InstructionList(
			Ins(1,Mirror(),Udp(),None(),Ipv6_Addr("...9.10.11.12..."),Anywhere(),Opts("zeste"))
			)
		),
		Block(UserRuleDef("fail2ban-apache"),Ref(0),InstructionList(
			Ins(1,Drop(),All_(),None(),Ipv4_Addr(Ipv4(1,2,3,40),24),Localhost(),Opts("blablabla")),
			Ins(2,Log(),All_(),None(),Anywhere(),Anywhere(),Opts("!!!!!!"))
			)     			
		)
	       ) ;
     
     System.out.println(Pretty.toString(t));
     try{
			System.out.println(Pretty.toString(`RepeatId(dispBanishments()).visit(t)));
			`RepeatId(dispShadowedAnomalies()).visit(t) ;
     }catch(tom.library.sl.VisitFailure e){
       System.out.println("echec : " + e.getMessage());
     }
  }

}
