import aterm.*;
import java.util.*;
import jtom.runtime.*;
import adt.*;

public class Nsh {

  private TermFactory factory;
  private GenericTraversal traversal;

  %include { term.t }

  %oplist ATermList concAgent( Agent* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }
   
  %oplist ATermList concMessage( Message* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }

  %oplist ATermList concNonce( Nonce* ) {
    fsym { factory.makeAFun("conc", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }

  public Nsh(TermFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public TermFactory getTermFactory() {
    return factory;
  }
  
  public void run(int size) {
    ATerm res = null;

      // A+SLEEP+N(A,A) * nnl <> B+SLEEP+N(B,B) * nnl <> II#nl#nill <> nill   end

    State initState = `state(
      concAgent(agent(alice,SLEEP,N(alice,alice))),
      concAgent(agent(bob,SLEEP,N(bob,bob))),
      intruder(devil,concNonce(),concMessage()),
      concMessage());

    Collection c = new HashSet();
    long startChrono = System.currentTimeMillis();
    res = initState;


    collectOneStep(initState,c);
    
    long stopChrono = System.currentTimeMillis();

      //System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public Nonce DN() {
    return `N(dai,dai);
  }

  public Address DA() {
    return `A(dai);
  }

  public boolean existAgent(Agent agent, ATermList list) {
    %match(Agent agent, ATermList list) {
      x, concAgent()          -> { return false; }
      x, concAgent(X1*,x,X2*) -> { return true; }
    }
    return false;
  }

  public boolean existMessage(Message message, ATermList list) {
    %match(Message message, ATermList list) {
      x, concMessage            -> { return false; }
      x, concMessage(X1*,x,X2*) -> { return true; }
    }
    return false;
  }

  public int sizeMessage(ATermList list) {
    return list.getLength();
  }
  
    /*
     *
     */
  public void collectOneStep(State subject, final Collection collection) {
    Collect2 collect = new Collect2() { 
        public boolean apply(ATerm subject, Object arg1) {
          Collection c = (Collection) arg1;
          %match(State subject) {
              // 3 (A --> B)
              // sender creates message
              // sender waits a message from y(or I) with the nonce N(x,y)(or N(x,I))

              // initiator 1
            state(
              concAgent(E1*,agent(x,SLEEP(),resp),E2*),
              dst@concAgent(D1*,agent(y,std,init),D2*),
              I,
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                State state = `state(
                  concAgent(E1*,agent(x,WAIT,N(x,y)),E2*),
                  dst,I,
                  concMessage(msg(x,y,K(y),N(x,y),DN(),A(x)),M*));
                c.add(state);
                System.out.println("state = " + state);
              }
            } 
              // initiator 1
            state(
              concAgent(E1*,agent(x,SLEEP(),resp),E2*),
              D, 
              intru@intruder(w,l,ll),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                State state = `state(
                  concAgent(E1*,agent(x,WAIT(),N(x,w)),E2*),
                  D,intru,
                  concMessage(msg(x,w,K(w),N(x,w),DN(),A(x)),M*));
                c.add(state);
              }
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
              concAgent(D1*,agent(y,SLEEP(),init),D2*),
              intru@intruder(w,l,ll),
              concMessage(M1*,msg(w,y,K(y),N(n1,n3),N(n2,n4),A(z)),M2*)) -> {
              State state = `state(
                E,
                concAgent(D1*,agent(y,WAIT(),N(y,z)),D2*),
                intru,
                concMessage(M1*,msg(y,z,K(z),N(n1,n3),N(y,z),A(y)),M2*));
              c.add(state);
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
              intru@intruder(w,l,ll),
              concMessage(M1*,msg(w,x,K(x),N(n1,n3),N(n2,n4),A(z)),M2*)) -> {
              if(x==n1 && v==n3) {
                  // lowe
                State state = `state(
                  concAgent(E1*,agent(x,COMMIT(),N(x,v)),E2*),
                  D,
                  intru,
                  concMessage(M1*,msg(x,v,K(z),N(n2,n4),DN(),DA()),M2*));
                c.add(state);
              } else {
                c.add(`ERROR());
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
              intru@intruder(w,l,ll),
              concMessage(M1*,msg(w,y,K(y),N(n1,n3),N(n2,n4),A(v)),M2*)) -> {
              if(x==n3 && y==n1) {
                  // lowe
                State state = `state(
                  E,
                  concAgent(D1*,agent(y,COMMIT(),N(y,x)),D2*),
                  intru,
                  concMessage(M1*,M2*));
                c.add(state);
              } else {
                c.add(`ERROR());
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
              concMessage(M1*,msg(z,x,K(w),N(n1,n3),N(n2,n4),A(v)),M2*)) -> {
              if(w!=z) {
                State state = `state(
                  E, D,
                  intruder(w,simplify(concNonce(N(n1,n3),N(n2,n4),l*)),ll),
                  concMessage(M1*,M2*));
                c.add(state);
              } 
            }

              //------------------------
              // learn messages
              // intruder should check if has not already intercepted it
              // (with nonamed rules --> we might get infinite loops ???)
              // (with insUnic)
            state(
              E, D,
              intruder(w,l,ll),
              concMessage(M1*,message@msg(z,x,K(y),N(n1,n3),N(n2,n4),A(v)),M2*)) -> {
              if(w!=z && w!=y) {
                State state = `state(
                  E, D,
                  intruder(w,l,concMessage(message,ll*)),
                  concMessage(M1*,M2*));
                c.add(state);
              } 
            }

              //-----------------------
              // intruder sends (random) stored messages to
              //		  (random) agents from E and D
              // !! allow dupplicates in the network (in the list version)

            state(
              E, D,
              intru@intruder(w,l,concMessage(msg(x,y,K(z),N(n1,n3),N(n2,n4),A(v)),ll*)),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                ATermList subjectList = `concAgent(E*,D*);
                %match(ATermList subjectList) {
                  concAgent(A1*,agent(t,_,_),A2*) -> {
                    Message message = `msg(w,t,K(z),N(n1,n3),N(n2,n4),A(v));
                    if(!existMessage(message,M)) {
                      State state = `state(E,D,intru,concMessage(message,M*));
                      c.add(state);
                    } 
                  }
                }
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

            state(
              E, D,
              intru@intruder(w,concNonce(resp,l*),ll),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                ATermList subjectList = `concAgent(E*,D*);
                %match(ATermList subjectList,ATermList subjectList) {
                  concAgent(A1*,agent(y,_,_),A2*),
                  concAgent(A3*,agent(xadd,_,_),A4*) -> {
                    Message message = `msg(w,y,K(y),resp,resp,A(xadd));
                    if(!existMessage(message,M)) {
                      State state = `state(E,D,intru,concMessage(message,M*));
                      c.add(state);
                    } 
                  }
                }
              }
            }

            state(
              E, D,
              intru@intruder(w,concNonce(resp,init,l*),ll),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                ATermList subjectList = `concAgent(E*,D*);
                %match(ATermList subjectList,ATermList subjectList) {
                  concAgent(A1*,agent(y,_,_),A2*),
                  concAgent(A3*,agent(xadd,_,_),A4*) -> {
                    Message message = `msg(w,y,K(y),resp,init,A(xadd));
                    if(!existMessage(message,M)) {
                      State state = `state(E,D,intru,concMessage(message,M*));
                      c.add(state);
                    } 
                  }
                }
              }
            }

              //------------------------------
              // the ATTACK
              //------------------------------
              // attack on the receiver
            state(
              concAgent(E1*,agent(x,COMMIT(),N(x,y)),E2*),
              D,intruder(w,l,ll),M) -> {
              if(!existAgent(`agent(y,WAIT(),N(y,x)),D) &&
                 !existAgent(`agent(y,COMMIT(),N(y,x)),D) ) {
                State state = `ATTACK();
                c.add(state);
              } 
            }

              //---------------------------
              // attack on the sender
            state(
              E,
              concAgent(D1*,agent(y,COMMIT(),N(y,x)),D2*),
              intruder(w,l,ll),M) -> {
              if(x!=w && !existAgent(`agent(y,COMMIT(),N(x,y)),E) ) {
                State state = `ATTACK();
                c.add(state);
              } 
            }

            
            
          }
          
          return true;
        } // end apply
      }; // end new

    collect.apply(subject,collection);
    
      //traversal.genericCollect(subject, collect, collection); 
  }

  public ATermList simplify(ATermList list) {
    %match(ATermList list) {
      concNonce(N1*,x,N2*,x,N3*) -> {
        return `simplify(concNonce(N1*,x,N2*,N3*));
      }

      concNonce(N1*,N(dai,dai),N2*) -> {
        return `simplify(concNonce(N1*,N2*));
      }
    }
    return list;
  }

  
  private static int MaxMessagesInNetwork = 1;
  public final static void main(String[] args) {
    Nsh test = new Nsh(new TermFactory(16));
    int size = 1;
    try {
      MaxMessagesInNetwork = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java Nsh <size>");
      return;
    }
    
    test.run(size);
  }

  
}

