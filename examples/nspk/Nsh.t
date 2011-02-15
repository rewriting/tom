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
 */

package nspk;

import java.util.*;
import nspk.term.types.*;
import tom.library.sl.VisitFailure;

public class Nsh {
  private static boolean authVerif = true;

// ------------------------------------------------------------  
  %include { term/term.tom }
  %include { sl.tom }
  %include { int.tom }
  %typeterm StateCollection {
    implement     { java.util.Collection<State> }
    is_sort(t)    { $t instanceof java.util.Collection }
    equals(l1,l2) { $l1.equals($l2) }
  }
// ------------------------------------------------------------  
 
  public void run(int nbAgent) {
    State initState = query(nbAgent,nbAgent);
    State search    = `ATTACK();

    long startChrono = System.currentTimeMillis();
      boolean res      = breadthSearch(initState,search);
      //boolean res      = depthSearch(initState,search);
      //boolean res      = depthSearch2(new HashSet(),initState,search);
    long stopChrono = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public State query(int nbSenders, int nbReceivers) {
    ListAgent s = `concAgent();
    for(int index=0 ; index<nbSenders ; index++) {
        s = `concAgent(agent(sender(index),SLEEP(),N(sender(index),sender(index))),s*);
    }
    ListAgent r = `concAgent();
    for(int index=0 ; index<nbReceivers ; index++) {
        r = `concAgent(agent(receiver(index),SLEEP(),N(receiver(index),receiver(index))),r*);
    }
    State state =
      `state(s,r,intruder(devil(),concNonce(),concMessage()),concMessage());
    return state;
  }
  
  public boolean breadthSearch(State start, State end) {
    Collection<State> c1 = new HashSet<State>();
    c1.add(start);

    int i = 0;
    while(!c1.isEmpty()) {
      Collection<State> c2 = new HashSet<State>();
      for (State state : c1) {
        collectOneStep(state,c2);
      }

      System.out.print("iteration " + i + ":");
      System.out.print("\tc2: " + c2.size());
        //System.out.print("\tresult.size = " + result.size());
      System.out.println();
      c1 = c2;
      if(c2.contains(end)) {
        return true;
      }
      i++;
    }
    return false;
  }

  Collection<State> result = new HashSet<State>();
  public boolean depthSearch(State start, State end) {
    Collection<State> c1 = new HashSet<State>();
    collectOneStep(start,c1);

      c1.removeAll(result);
      result.addAll(c1);
        //System.out.println("\tresult.size = " + result.size());
    
    if(c1.size() > 0) {
        //System.out.println("c1.size = " + c1.size());
    } else {
      return false;
    }
    
    if(c1.contains(end)) {
      System.out.println("ATTACK");
      return true;
    }

    for (State state : c1) {
      boolean b = depthSearch(state,end);
      if(b) return b;
    }
    return false;
  }

 public boolean depthSearch2(Collection<State> local, State start, State end) {
    Collection<State> c1 = new HashSet<State>();
    collectOneStep(start,c1);

      c1.removeAll(local);
      local.addAll(c1);
        //System.out.println("\tlocal.size = " + local.size());
    
    if(c1.size() > 0) {
        //System.out.println("c1.size = " + c1.size());
    } else {
      return false;
    }
    
    if(c1.contains(end)) {
      System.out.println("ATTACK");
      return true;
    }

    Collection<State> c = new HashSet<State>();
    for (State state : c1) {
      boolean b = depthSearch2(c,state,end);
      if(b) return b;
    }
    return false;
  }

  public static int compareMessage(Message m1, Message m2) {
      
    %match(m1, m2) {
      msg[src=sender[]],   msg[src=receiver[]] -> { return 1;  }
      msg[src=receiver[]], msg[src=sender[]] -> { return -1;  }
      msg[src=receiver[]], msg[src=devil()]  -> { return 1;  }
      msg[src=sender[]],   msg[src=devil()]    -> { return 1;  }
      msg[src=devil()],    msg[src=receiver[]]  -> { return -1;  }
      msg[src=devil()],    msg[src=sender[]]    -> { return -1;  }

      msg[src=x,dst=sender[]],    msg[src=x,dst=receiver[]]  -> { return 1;  }
      msg[src=x,dst=receiver[]],  msg[src=x,dst=sender[]]    -> { return -1;  }
     
      //msg[src=sender(i)],         msg[src=sender(j)]         -> { return j-i;  } 
      //msg[src=receiver(i)],       msg[src=receiver(j)]       -> { return j-i;  } 
      //msg[src=x,dst=sender(i)],   msg[src=x,dst=sender(j)]   -> { return j-i;  } 
      //msg[src=x,dst=receiver(i)], msg[src=x,dst=receiver(j)] -> { return j-i;  } 

    }
    
      //System.out.println("m1 = " + s1);
      //System.out.println("m2 = " + s2);
    int res = m1.compareTo(m2);
    
    return res;
  }

  public static int compareNonce(Nonce m1, Nonce m2) {
      
    %match(m1, m2) {
      N[id1=sender[]],   N[id1=receiver[]] -> { return 1;  }
      N[id1=receiver[]], N[id1=sender[]] -> { return -1;  }
      N[id1=receiver[]], N[id1=devil()]  -> { return 1;  }
      N[id1=sender[]],   N[id1=devil()]    -> { return 1;  }
      N[id1=devil()],    N[id1=receiver[]]  -> { return -1;  }
      N[id1=devil()],    N[id1=sender[]]    -> { return -1;  }

      N[id1=x,id2=sender[]],    N[id1=x,id2=receiver[]]  -> { return 1;  }
      N[id1=x,id2=receiver[]],  N[id1=x,id2=sender[]]    -> { return -1;  }
 
      //N[id1=sender(i)],         N[id1=sender(j)]         -> { return j-i;  } 
      //N[id1=receiver(i)],       N[id1=receiver(j)]       -> { return j-i;  } 
      //N[id1=x,id2=sender(i)],   N[id1=x,id2=sender(j)]   -> { return j-i;  } 
      //N[id1=x,id2=receiver(i)], N[id1=x,id2=receiver(j)] -> { return j-i;  } 
     
    }
    
      //System.out.println("m1 = " + s1);
      //System.out.println("m2 = " + s2);
    int res = m1.compareTo(m2);
    
    return res;
  }
  static ListMessage insertMessage(Message m,ListMessage l) {
    ListMessage res = null;
    if(l.isEmptyconcMessage()) {
      res = `concMessage(m,l*);
    } else if(compareMessage(m, l.getHeadconcMessage()) < 0) {
      res = `concMessage(m,l*);
    } else {
      ListMessage newTail = insertMessage(m,l.getTailconcMessage());
      res = `concMessage(l.getHeadconcMessage(),newTail*);
    }

      //System.out.println("res = " + res);
    return res;
  }
    
  static ListNonce insertNonce(Nonce m,ListNonce l) {
    ListNonce res = null;
    %match(m) {
      N(dai(),dai()) -> { return l; }
    }
 
    if(l.isEmptyconcNonce()) {
      res = `concNonce(m,l*);
    } else if(m == l.getHeadconcNonce()) {
      res = l;
    } else if(compareNonce(m, l.getHeadconcNonce()) < 0) {
      res = `concNonce(m,l*);
    } else {
      ListNonce newTail = insertNonce(m,l.getTailconcNonce());
      res = `concNonce(l.getHeadconcNonce(),newTail*);
    }

      //System.out.println("res = " + res);
    return res;
  }
     
  public static Nonce DN() {
    return `N(dai(),dai());
  }

  public static Address DA() {
    return `A(dai());
  }

  public static boolean existAgent(Agent agent, ListAgent list) {
    %match(list) {
      concAgent() -> { return false; }
      concAgent(_*,x,_*) && x << Agent agent -> { return true; }
    }
    return false;
    /* return list.indexOf(agent,0) >= 0; */
  }

  public static boolean existMessage(Message message, ListMessage list) {
    %match(list) {
      concMessage() -> { return false; }
      concMessage(_*,x,_*) && x<<Message message -> { return true; }
    }
    return false;
    /* return list.indexOf(message,0) >= 0; */
  }

  public static int sizeMessage(ListMessage list) {
    return ((nspk.term.types.listmessage.concMessage)list).length();
  }
  
  public void collectOneStep(State state, Collection<State> col) {
    try {
      `OneStep(col).visitLight(state);
    } catch (VisitFailure f) {
      throw new RuntimeException("VisitFailure for "+state);
    }
  }
  %strategy OneStep(c:StateCollection) extends `Identity() {
    visit State {
      // 3 (A --> B)
      // sender creates message
      // sender waits a message from y(or I) with the nonce N(x,y)(or N(x,I))

      // initiator 1
      /*
      state(
          concAgent(E1*,agent(x,SLEEP(),_),E2*),
          dst@concAgent(_*,agent(y,_,_),_*),
          I,
          M) && maxMessagesInNetwork > int sizeMessage(M) 
      */
      // Without explicit type declaration "int" for sizeMessage
      state(
          concAgent(E1*,agent(x,SLEEP(),_),E2*),
          dst@concAgent(_*,agent(y,_,_),_*),
          I,
          M) && maxMessagesInNetwork > sizeMessage(M)
      -> {        
          State state = `state(
              concAgent(E1*,agent(x,WAIT(),N(x,y)),E2*),
              dst,I,
              insertMessage(msg(x,y,K(y),N(x,y),DN(),A(x)),M));
          c.add(state);
          fire[1]++;        
      } 
      // initiator 1
      /*
      state(
          concAgent(E1*,agent(x,SLEEP(),_),E2*),
          D, 
          intru@intruder(w,_,_),
          M) && maxMessagesInNetwork > int sizeMessage(M)
          */
      // Without explicit type declaration "int" for sizeMessage
     state(
          concAgent(E1*,agent(x,SLEEP(),_),E2*),
          D, 
          intru@intruder(w,_,_),
          M) && maxMessagesInNetwork > sizeMessage(M)
       -> {
          State state = `state(
              concAgent(E1*,agent(x,WAIT(),N(x,w)),E2*),
              D,intru,
              concMessage(msg(x,w,K(w),N(x,w),DN(),A(x)),M*));
          c.add(state);
          fire[2]++;
      }

      // 3/6 (--> B --> A)
      // receiver checks if 
      //  - y is the dest
      //  - message encrypted with K(y)
      //  - message type is MNA
      // receiver sends
      //  - to n2 (identifier of sender; normally N(n1,n3)==N(n2,n4))
      //  - received nonce (N(n1,n3)) and its nonce (N(y,n1))
      // receiver waits a message from n1 with the nonce N(y,n1)

      state(
          E,
          concAgent(D1*,agent(y,SLEEP(),_),D2*),
          intru@intruder(w,_,_),
          concMessage(M1*,msg(w,y,K(y),N(n1,n3),N(_,_),A(z)),M2*)) -> {
        State state = `state(
            E,
            concAgent(D1*,agent(y,WAIT(),N(y,z)),D2*),
            intru,
            insertMessage(msg(y,z,K(z),N(n1,n3),N(y,z),A(y)),concMessage(M1*,M2*)));
        c.add(state);
        fire[3]++;
      }

      // 6/7 (--> A --> B)
      // sender checks if 
      //  - x is the dest
      //  - message encrypted with K(x)
      //  - message type is MNNA
      //  - nonce is N(x,v) --> if not: ERROR
      //  ?? (possibly checks address : A(v) ) ??

      // sender sends a message 
      //   - to the the initial receiver (stored in N(x,v)->v)
      //   - with the received nonce

      state(
          concAgent(E1*,agent(x,WAIT(),N(x,v)),E2*),
          D,
          intru@intruder(w,_,_),
          concMessage(M1*,msg(w,x,K(x),N(n1,n3),N(n2,n4),A(z)),M2*)) -> {

        if(`x==`n1 && `v==`n3 && (!authVerif || `v==`z)) { // lowe
          State state = `state(
              concAgent(E1*,agent(x,COMMIT(),N(x,v)),E2*),
              D,
              intru,
              insertMessage(msg(x,v,K(v),N(n2,n4),DN(),DA()),concMessage(M1*,M2*)));
          c.add(state);
          fire[4]++;
        }

        if(`x!=`n1 || `v!=`n3 && (!authVerif || `v!=`z)) { // lowe
          c.add(`ERROR());
          fire[5]++;
        }
      }

      // 7 (B --> A)

      // receiver checks if 
      //  - y is the dest
      //  - message encrypted with K(y)
      //  - message type is MN
      //  - nonce is N(y,x) --> if not: ERROR

      // receiver sends
      //  - to n2 (identifier of sender; normally N(n1,n3)==N(n2,n4))
      //  - received nonce (N(n1,n3)) and its nonce (N(y,n1))
      // receiver waits a message from n1 with the nonce N(y,n1)

      state(
          E,
          concAgent(D1*,agent(y,WAIT(),N(y,x)),D2*),
          intru@intruder(w,_,_),
          concMessage(M1*,msg(w,y,K(y),N(n1,n3),N(_,_),A(_)),M2*)) -> {
        if(`x==`n3 && `y==`n1) {
          State state = `state(
              E,
              concAgent(D1*,agent(y,COMMIT(),N(y,x)),D2*),
              intru,
              concMessage(M1*,M2*));
          c.add(state);
          fire[6]++;    
        } else {
          c.add(`ERROR());
          fire[7]++;
        }
      }

      //----------------------------
      // intruder intercepts message
      //----------------------------
      // intruder intercepts message encrypted with its key
      // we may replace N(n1,n3) with a variable Nonce:n13
      // 		  N(n2,n4) with a variable Nonce:n24
      // 		  A(v) with a variable Address:add

      state(
          E, D,
          intruder(w,l,ll),
          concMessage(M1*,msg(!w,_,K(w),N1,N2,_),M2*)) -> {        
        State state = `state(
            E, D,
            intruder(w,insertNonce(N1,insertNonce(N2,l)),ll),
            concMessage(M1*,M2*));
        c.add(state);
        fire[8]++;         
      }

      //------------------------
      // learn messages
      // intruder should check if has not already intercepted it
      // (with nonamed rules --> we might get infinite loops ???)
      // (with insUnic)
      state(
          E, D,
          intruder(w,l,ll),
          concMessage(M1*,message@msg(!w,_,K(!w),N(_,_),N(_,_),A(_)),M2*)) -> {        
        State state = `state(
            E, D,
            intruder(w,l,insertMessage(message,ll)),
            concMessage(M1*,M2*));
        c.add(state);
        fire[9]++;         
      }

      //-----------------------
      // intruder sends (random) stored messages to
      //		  (random) agents from E and D
      // !! allow dupplicates in the network (in the list version)

      /*
      state(
          E, D,
          intru@intruder(w,_,concMessage(_*,msg(_,_,K(z),N(n1,n3),N(n2,n4),A(v)),_*)),
          M) && maxMessagesInNetwork > int sizeMessage(M) &&  concAgent(_*,agent(t,_,_),_*) << concAgent(E*,D*)
      */
      // Without explicit type declaration "int" for sizeMessage
      state(
          E, D,
          intru@intruder(w,_,concMessage(_*,msg(_,_,K(z),N(n1,n3),N(n2,n4),A(v)),_*)),
          M) && maxMessagesInNetwork > sizeMessage(M) &&  concAgent(_*,agent(t,_,_),_*) << concAgent(E*,D*)
      -> {
          //RK
        Message message = `msg(w,t,K(z),N(n1,n3),N(n2,n4),A(v));
        if(!existMessage(message,`M)) {
          State state = `state(E,D,intru,insertMessage(message,M));
          c.add(state);
          fire[10]++;
        } 
      }

      //---------------------------
      // intruder sends mnessages starting from the 
      //		(random) nonces it knows with --> resp, init
      //		  (random) destination   --> y
      //		  (random) message type  --> 3 rules
      //		  (random) nonce1 --> resp
      //		  (random) nonce2 --> init
      //		  (random) address --> A(xadd)
      // !! allow dupplicates in the network (in the list version)

      /*
      state(
          E, D,
          intru@intruder(w,listNonce,_),
          M) && maxMessagesInNetwork > int sizeMessage(M) 
             && subjectED << concAgent(E*,D*)
             && concAgent(_*,agent(y,_,_),_*) <<  subjectED
             && concAgent(_*,agent(xadd,_,_),_*) <<  subjectED
             && concNonce(_*,init,_*) << listNonce
             && concNonce(_*,resp,_*) << listNonce      
      */
      // Without explicit type declaration "int" for sizeMessage
      state(
          E, D,
          intru@intruder(w,listNonce,_),
          M) && maxMessagesInNetwork > sizeMessage(M) 
             && subjectED << concAgent(E*,D*)
             && concAgent(_*,agent(y,_,_),_*) <<  subjectED
             && concAgent(_*,agent(xadd,_,_),_*) <<  subjectED
             && concNonce(_*,init,_*) << listNonce
             && concNonce(_*,resp,_*) << listNonce
          -> {
//        if(sizeMessage(`M) < maxMessagesInNetwork) {
//          ListAgent subjectED = `concAgent(E*,D*);
//          %match(ListAgent subjectED, ListAgent subjectED,
//              ListNonce `listNonce, ListNonce `listNonce) {
//            concAgent(_*,agent(y,_,_),_*),
//            concAgent(_*,agent(xadd,_,_),_*),
//            concNonce(_*,init,_*),
//            concNonce(_*,resp,_*) -> {
              Message message = `msg(w,y,K(y),resp,init,A(xadd));
              if(!existMessage(message,`M)) {
                State state = `state(E,D,intru,insertMessage(message,M));
                c.add(state);
                fire[11]++;
              } 
//            }
//          }
//        }
      }

      //------------------------------
      // the ATTACK
      //------------------------------
      // attack on the receiver
      state(
          concAgent(_*,agent(x,COMMIT(),N(x,y@!w)),_*),
          D,intruder(w,_,_),_) -> {
        if( !existAgent(`agent(y,WAIT(),N(y,x)),`D) &&
            !existAgent(`agent(y,COMMIT(),N(y,x)),`D) ) {
          State state = `ATTACK();
          c.add(state);
          fire[12]++;
        } 
      }

      //---------------------------
      // attack on the sender
      state(
          E,
          concAgent(_*,agent(y,COMMIT(),N(y,x)),_*),
          intruder(!x,_,_),_) -> {
        if(!existAgent(`agent(x,COMMIT(),N(x,y)),`E) ) {
          State state = `ATTACK();
          c.add(state);
          fire[13]++;
        } 
      }
    }
  }

  public ListNonce simplify(ListNonce list) {
    %match(ListNonce list) {
      concNonce(N1*,x,N2*,x,N3*) -> {
        return `simplify(concNonce(N1*,x,N2*,N3*));
      }

      concNonce(N1*,N(dai(),dai()),N2*) -> {
          return `simplify(concNonce(N1*,N2*));
      }
      
    }
    return list;
  }

  
  private static int maxMessagesInNetwork = 1;
  private static int fire[] = new int[20];

  public final static void main(String[] args) {
    Nsh test = new Nsh();
    int nbAgent = 1;
    //System.out.println("agent = " + `paul());
    try {
      nbAgent = Integer.parseInt(args[0]);
      maxMessagesInNetwork = Integer.parseInt(args[1]);
    } catch (Exception e) {
      System.out.println("Usage: java Nsh <nbAgent> <maxMsg>");
      return;
    }

    test.run(nbAgent);
  }
}
