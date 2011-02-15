/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Florent Garnier   e-mail: Florent.Garnier@loria.fr
 * Zhong Yiyang      e-mail: yiyangzhong@yahoo.fr
 *
 */
package csmaca; 

import csmaca.simulator.network.types.*;
import java.util.Random;

public class Simulator {

  %gom {
    module network
    imports int 
      
    abstract syntax      
      Sender = sender(eta:int,tatt:int,nbmess:int,cback:int,write:int)
      List = concSender(Sender*)
      Listes = concList(x:List,y:List)
      Etat = etat(x:int, t:int,y:Listes)
  }
 
  public final static void main(String[] args) {
    Simulator test = new Simulator();
    test.run();
  }

  public void testSimul() {
    Etat e1= `etat(0,0,concList(concSender(sender(1,2,3,4,5)),concSender(sender(2,3,4,5,6),sender(3,4,5,6,7))));
    Etat e2= `etat(1,0,concList(concSender(sender(1,2,0,4,5)),concSender(sender(2,3,4,5,6),sender(3,4,5,6,7))));
    Etat e3= `etat(50,0,concList(concSender(sender(0,2,1,1,0)),concSender(sender(0,2,1,1,0),sender(0,3,1,1,0))));
//  Listes l= `concList(concSender(sender(0,2,1,1,0)),concSender(sender(0,2,1,1,0),sender(0,3,1,1,0)));

    System.out.println("Avant simulation:");
//  System.out.println("simul e1= "); afficheEtat(e1);
//  System.out.println("simul e1= "); afficheEtat(e2);
    System.out.println("simul e3= "); afficheEtat(e3);

    System.out.println("Apres simulation:");
//  System.out.println("simul e1= " + simul(e1) );
//  System.out.println("simul e2= " + simul(e2) );
    System.out.println("simul e3= " + simul(e3) );
                         
//    System.out.println("resultat      prendrefirst =" + prendrefirst(sortfirstdatelist(majlist(l))));
//    System.out.println("resultat sortfirstdatelist =" + sortfirstdatelist(majlist(l)));
//    System.out.println("resultat             majlist =" + majlist(l));
//    System.out.println("resultat            nextstep =" + nextstep(majdatelist(2,l)));
//    System.out.println("resultat         majdatelist =" + majdatelist(2, l));

}

  public void run() {
    testSimul();
  }

  public int min(int x,int y) {
    if(x < y) return x;
      else return y;
  }  

  public int backoff(int x) {
    Random a = new Random();
    return a.nextInt(min(10,x));
  }
	
  public int getchamp(Sender s,int i) {
   %match (Sender s) {sender(x1,x2,x3,x4,x5) -> {if(i==1) return `x1;}
                      sender(x1,x2,x3,x4,x5) -> {if(i==2) return `x2;}
	              sender(x1,x2,x3,x4,x5) -> {if(i==3) return `x3;}
                      sender(x1,x2,x3,x4,x5) -> {if(i==4) return `x4;}
		      sender(x1,x2,x3,x4,x5) -> {if(i==5) return `x5;}
	             }
    return 0;
  }

  public Sender putvalue(Sender s,int champ,int val) {
    %match (Sender s) {sender(x1,x2,x3,x4,x5) -> {if(champ==1) return `sender(val,x2,x3,x4,x5);}
		       sender(x1,x2,x3,x4,x5) -> {if(champ==2) return `sender(x1,val,x3,x4,x5);}
                       sender(x1,x2,x3,x4,x5) -> {if(champ==3) return `sender(x1,x2,val,x4,x5);}
                       sender(x1,x2,x3,x4,x5) -> {if(champ==4) return `sender(x1,x2,x3,val,x5);}
                       sender(x1,x2,x3,x4,x5) -> {if(champ==5) return `sender(x1,x2,x3,x4,val);}
                      }
    return `sender(0,0,0,0,0);
  }

  public boolean  mediumfree(Listes l) {
    %match(Listes l) { 
       concList(concSender(),concSender())                            -> {return true; } 
       concList(concSender(sender(x1,x2,x3,x4,x5)),concSender())      -> { return (`x5==0);
						                            			
              			                                           }
       concList(concSender(sender(x1,x2,x3,x4,x5)),concSender(C1,C2*))-> 
           {
             return ( (`x5==0) && (`mediumfree(concList(concSender(C1),concSender(C2*)))) ); 
             }
       }
    return true;
  }


  public Sender argmindate(Etat e) {
    %match (Etat e) {etat(t,x,l) -> {return argmindatelist(`l);}
                    }
    return `sender(0,0,0,0,0);
  }


  public Sender argmindatelist(Listes l) {
    %match (Listes l) { 
       concList(concSender(C@sender(eta,tatt,nbmess,cback,write)),concSender())       -> {return `C;}

       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*))->{
          if(`tatt<= `getchamp( argmindatelist(concList(concSender(C2),concSender(C3*))),2 )  ) return `C1;}

       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*))  -> {
          return `argmindatelist(concList( concSender(C2),concSender(C3*) ));}      
		       }
     return `sender(-1,-1,-1,-1,-1); 
  }
   	
//getminTnonNul test:
//fonction 
  public int getminTnonNul(Listes l) {
    %match (Listes l) { 
      concList(concSender(),concSender()) -> {return 0;}
      concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2*))    -> {if(`tatt > 0) return `tatt;}
      concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*)) -> {
        if(`tatt==0) return `getminTnonNul(concList(concSender(C2),concSender(C3*)));	       
                   }
      }
  return -1;
  }
		     
//insertsenderdate test:
//fonction bien
public Etat insertsenderdate(Sender s,Etat e){
  %match (Sender s,Etat e) { 
    C1@sender(eta,tatt,nbmess,cback,write),etat(t,x,concList(concSender(),concSender())) -> 
      {
       return `etat(t,x,concList( concSender(C1),concSender() ));}

    C1@sender(eta1,tatt1,nbmess1,cback1,write1),
    etat(t,x,concList(concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2)),concSender(C3*))) -> {
      if((`tatt1 < `tatt2) && (`nbmess1>0) && (`nbmess2>0)) 
        return `etat(t,x,concList(concSender(C1),concSender(C2,C3*)));}

    C1@sender(eta1,tatt1,nbmess1,cback1,write1),
    etat(t,x,concList(concSender(C2@sender(eta2,tatt,nbmess2,cback2,write2)),concSender(C3*))) -> {
      if((`nbmess1==0) && (`nbmess2==0)) 
        return `etat(t,x,concList(concSender(C1),concSender(C2,C3*)));}

    C1@sender(eta1,tatt1,nbmess1,cback1,write1),
    etat(t,x,concList(concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2)),concSender(C3*))) ->{ 
      if((`tatt1 >= `tatt2) && (`nbmess1>0) && (`nbmess2>0))
            return `etat(t,x,
                    concList(concSender(C2),
                    insertsenderdatelist(C1,C3*)
                    ));}
        }
    return `etat(0,0,concList(concSender(),concSender()));
  
}

  public List con(List x,List y) {
    %match(List x,List y) {
      concSender(C1*),concSender(C2*) -> {return `concSender(C1*,C2*);}
      }
	    return `concSender();
  }

//les methodes  
  public List insertsenderdatelist(Sender s,List l) {
    %match(Sender s,List l) {
       C1@sender(eta,tatt,nbmess,cback,write),concSender() -> {return `concSender(C1);}
    
       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) ->
          {if((`tatt1<`tatt2)&&(`nbmess1>0)&&(`nbmess2>0))     return `concSender(C1,C2,C3*); }
    

       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) ->
          {if((`tatt1>=`tatt2)&&(`nbmess1>0)&&(`nbmess2>0)) return `con(concSender(C2) ,insertsenderdatelist(C1,C3*)   );}
       
       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) ->{
           if((`nbmess1>0)&&(`nbmess2==0))  return `concSender(C1,C2,C3*);}
       
       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) ->
          {if((`nbmess1==0)&&(`nbmess2>0)) return `con(concSender(C2) ,insertsenderdatelist(C1,C3*));}

       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) ->
          {if((`nbmess1==0)&&(`nbmess2==0)) return   `concSender(C1,C2,C3*);}
}
   return `concSender();
   }

//exemple teste
  public Etat simul (Etat e) {
    %match (Etat e) {
       etat(t,x,concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2*))) -> {
         afficheEtat(e);
         if((`nbmess>0) && (`t>0))
           return `simul(etat(
                         t-1,x+tatt,
                         prendrefirst(
                         sortfirstdatelist( majlist( concList(concSender(C1),
                                                              concSender(C2*)
                                                             ) 
                                                   ) 
                                          )
                        )));
         }
       etat(t,x,concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2*))) -> {
         if((`nbmess==0)||(`t==0))
           return `etat(t,x+tatt,
                        concList(concSender(C1),concSender(C2*)));
         }
       }
    return `etat(0,0,concList(concSender(sender(-1,-2,-3,-4,-5)),concSender(sender(-1,-2,-3,-4,-5))));
  }      


//pour la fonction simul
  public Listes prendrefirst(List l) {
    %match(List l) {
       concSender(C1,C2*) -> {
         return `concList(concSender(C1),concSender(C2*));}
     }
    return `concList(concSender(sender(-1,-1,-1,-1,-1)),concSender(sender(-1,-1,-1,-1,-1)));
}


  public List sortfirstdatelist(Listes l) {
    %match (Listes l) {
       
       concList(C1@concSender(A1),C2@concSender(A2*)) -> {
         return  `insertsenderdatelist(A1,C2);}
     }
  return `concSender();
}


//les methodes 
  public Listes majlist(Listes l) {
    %match (Listes l) {
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*)) -> {
         return `nextstep(majdatelist(tatt,
                                      concList(concSender(C1),concSender(C2,C3*))
                                     ) 
                         );
       }
     }
   return `concList(concSender(sender(-1,-1,-1,-1,-1)),concSender());
  }
 	
//les methodes
  public Listes majdatelist(int x,Listes l) {
    %match( Listes l) {
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender())	 -> {
         return `concList(concSender(sender(eta,tatt - x,nbmess,cback,write)),concSender());}
      
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*)) -> {
         return `converLLsLs(concSender(sender(eta,tatt - x,nbmess,cback,write)),
                          majdatelist(x,concList(concSender(C2),concSender(C3*))) );}
       }
    return `concList(concSender(),concSender());
  }
  
  public Listes converLLsLs(List x,Listes y) {
    %match(List x,Listes y) {
       C1@concSender(A1*),concList(concSender(C2),concSender(C3*)) -> {
         return `concList(C1,concSender(C2,C3*));}
       }
    return `concList(concSender(),concSender());
  }


//les methodes
  public boolean jobleft(Etat e) {
    %match (Etat e) {
       etat(t,x,concList(concSender(sender(eta,tatt,nbmess,cback,write)),concSender())) -> { if(`nbmess != 0) return true;}

       etat(t,x,concList(concSender(sender(eta,tatt,nbmess,cback,write)),concSender())) -> { if(`nbmess == 0) return false;}

       etat(t,x,concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*))) -> { 
           if((`nbmess != 0) || (jobleft( `etat(t,x,concList(concSender(C2),concSender(C3*)))))) return true;}

        etat(t,x,concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*))) -> { 
           if((`nbmess == 0) && (jobleft( `etat(t,x,concList(concSender(C2),concSender(C3*))))==false  )) return false;}

       etat(t,x,concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2*))) -> { if(`nbmess == 0) return true;}
          }
  return false;
  }


//fonction bien
  public List markwritten(Listes l) {
    %match (Listes l) {
       concList(concSender(),concSender()) -> {return `concSender( );}
 
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender()) -> {
           return `concSender(sender(eta,tatt,nbmess,cback,2));}

       concList(concSender(C1@sender(eta1,tatt1,nbmess1,cback1,write1)),concSender(C2,C3*)) -> {
           return `converSLL(sender(eta1,tatt1,nbmess1,cback1,2),
                          markwritten(concList(concSender(C2),
                                               concSender(C3*)
                                              )
                                     )
                          );}
     }
    return `concSender();
  }

//pour la fonction markwritten:
//fonction bien
  public List converSLL(Sender s, List l) {
    %match (Sender s,List l) {
       C1@sender(eta1,tatt1,nbmess1,cback1,write1),concSender(C2@sender(eta2,tatt2,nbmess2,cback2,write2),C3*) -> {
         return `concSender(C1,C2,C3*);}
       }
    return `concSender();
  }
        
//fonction bien 
  public int recivects(Listes l) {
    %match (Listes l) {
       concList(concSender(),concSender()) -> {return 1;}
       concList(concSender(sender(eta,tatt,nbmess,cback,write)),concSender())-> {if((`eta==0) ||(`eta==1) || (`eta==2)) return 1;}
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*)) -> {
           return `(recivects(concList(concSender(C1),concSender()))*recivects(concList(concSender(C2),concSender(C3*))));
         }
       }
    return 0;
  }


//fonction bien sauf regle 3, 5, 10
  public Listes nextstep(Listes l) {
    %match (Listes l) {
       concList(concSender(C1@sender(eta,tatt,nbmess,cback,write)),concSender(C2,C3*)) -> {
//regle 1
  if((`write==0)&&(`eta==0)) 
    return `concList(concSender(sender(1,3+backoff(cback),nbmess,cback,write)),concSender(C2,C3*));
//regle 2 
  if((`eta==0)&&`(write!=0)) 
    return 
      `concList(concSender(sender(eta,
                                  getminTnonNul(concList(concSender(C2),concSender(C3*))),
                                  nbmess,
                                  cback+1,
                                  0) 
                           ),                                                     
                concSender(C2,C3*)
               );
//regle 3
  if((`eta==1)&&(`write==0)) 
    return `concList(concSender(sender(2,1,nbmess,cback,1)),
                     markwritten(concList(concSender(C2),concSender(C3*)))
                    );
//regle 4
  if((`eta==1)&&(`write==2)) 
    return `concList(concSender(sender(0,
                                       getminTnonNul(concList(concSender(C2),concSender(C3*))
                                                     ),
                                       nbmess,
                                       cback+1,
                                       0)
                               ),
                     concSender(C2,C3*)
                    );
//regle 5
  if((`eta==2)&&(`write==1)) 
    return `concList(concSender(sender(3,
                                       1,
                                       nbmess,
                                       cback,
                                       recivects(concList(concSender(C2),
							  concSender(C3*)
                                                          )
                                                )
                                       )
                                 ),
                     concSender(C2,C3*)
                    );
//regle 6
  if((`eta==2)&&(`write==2)) 
    return `concList(concSender(sender(0,0,nbmess,cback+1,0)),
                     concSender(C2,C3*));
//regle 7
  if((`eta==3)&&(`write==1)) 
    return `concList(concSender(sender(4,0,nbmess,cback,0)),
                     concSender(C2,C3*)
                    );
//regle 8
  if((`eta==3)&&(`write==0)) 
    return `concList(concSender(sender(0,2,nbmess,cback+1,0)),
                     concSender(C2,C3*)
                    );
//regle 9
  if((`eta==3)&&(`write==2)) 
    return `concList(concSender(sender(0,0,nbmess,cback+1,0)),
                     concSender(C2,C3*)
                    );
//regle 10
  if(`eta==4) 
    return `concList(concSender(sender(5,5,nbmess,cback,1)),
                     markwritten(concList(concSender(C2),concSender(C3*)))
                    );
//regle 11
  if(`eta==5) 
    return `concList(concSender(sender(6,1,nbmess,cback,0)),
                     concSender(C2,C3*));
//regle 12
  if((`eta==6)&&(`write==2)) 
    return `concList(concSender(sender(0,0,nbmess,cback+1,write)),
                     concSender(C2,C3*));
//regle 13 
  if((`eta==6)&&(`write==0)) 
    return `concList(concSender(sender(0,0,nbmess-1,0,0)),
                     concSender(C2,C3*));
//regle 14
  if((`eta==6)&&(`write==3)) 
    return `concList(concSender(sender(0,2,nbmess,cback+1,0)),
                     concSender(C2,C3*));}
    }
  return `concList(concSender(sender(-1,-1,-1,-1,-1)),concSender(sender(-1,-1,-1,-1,-1)));

}





//afficher un vecteur			   
   public void afficheElement (Sender s){
     %match(Sender s){
        sender(x1,x2,x3,x4,x5) -> {System.out.println("("+`x1+","+`x2+","+`x3+","+`x4+","+`x5+")");}
	             }
   }
//afficher une list de vexteur
  public void afficheList(List s) {
   %match(List s) {
//      concSender(sender(eta,tatt,nbmess,cback,write)) -> {    
//        System.out.println("(" + `eta + "," + `tatt + "," + `nbmess + "," + `cback + "," + `write + ")");
//        }

      concSender(C1@sender(eta,tatt,nbmess,cback,write),C2*) -> {
        System.out.println("(" + `eta + "," + `tatt + "," + `nbmess + "," + `cback + "," + `write + ")");
        `afficheList(C2*);}
      }
  }
//afficher une listes de vecteur 
   public void afficheListes(Listes s){
     %match(Listes s) {
       concList(concSender(C1@sender(eta1,tatt1,nbmess1,cback1,write1),C2*),concSender()) -> {
         System.out.println("(" + `eta1 + "," + `tatt1 + "," + `nbmess1 + "," + `cback1 + "," + `write1 + ")" ); `afficheList(C2*); 
         }

       concList(concSender(C1@sender(eta1,tatt1,nbmess1,cback1,write1),C2*),
                 concSender(C3@sender(eta2,tatt2,nbmess2,cback2,write2),C4*)) -> {
         System.out.println("(" + `eta1 + "," + `tatt1 + "," + `nbmess1 + "," + `cback1 + "," + `write1 + ")" ); `afficheList(C2*); 
         System.out.println("(" + `eta2 + "," + `tatt2 + "," + `nbmess2 + "," + `cback2 + "," + `write2 + ")" ); `afficheList(C4*); 
      } 	
   }
}
//afficher une etat
  public void  afficheEtat(Etat e) {
    %match (Etat e ) { 
       etat(x,t,l) -> {
          System.out.println( "(" + `x + "," + `t + "," ); 
          afficheListes(`l);
          System.out.println(  ")" ); 
   }
          }
  }
}
