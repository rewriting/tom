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
      intruder(devil,nilListNonce,concMessage()),
      concMessage());


    Collection c = new HashSet();
    long startChrono = System.currentTimeMillis();
    res = initState;


    collectOneStep(initState,c);
    
    long stopChrono = System.currentTimeMillis();

      //System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
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
        public boolean apply(ATerm t, Object arg1) {
          Collection c = (Collection) arg1;
          %match(State t) {
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
              }
            }
            
            
          }
          
          return true;
        } // end apply
      }; // end new

    collect.apply(subject,collection);
    
      //traversal.genericCollect(subject, collect, collection); 
  }

  private final int MaxMessagesInNetwork = 1;
  public final static void main(String[] args) {
    Nsh test = new Nsh(new TermFactory(16));
    int size;
    try {
      size = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java Nsh <size>");
      return;
    }
    
    test.run(size);
  }

  
}

