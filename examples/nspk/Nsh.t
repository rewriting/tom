import aterm.*;
import java.util.*;
import jtom.runtime.*;
import jtom.runtime.set.*;
import adt.*;

public class Nsh {

  private TermFactory factory;
  private GenericTraversal traversal;
  
  %include { term.t }

  %oplist ATermList concAgent( Agent* ) {
    fsym { null }
    is_fsym(t) { (t instanceof ATermList) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }
   
  %oplist ATermList concMessage( Message* ) {
    fsym { null }
    is_fsym(t) { (t instanceof ATermList) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }

  %oplist ATermList concNonce( Nonce* ) {
    fsym { null }
    is_fsym(t) { (t instanceof ATermList) }
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
    ATermList s = `concAgent();
    for(int index=0 ; index<nbSenders ; index++) {
      Integer i = new Integer(index);
      s = s.insert(`agent(sender(i),SLEEP,N(sender(i),sender(i))));
    }
    ATermList r = `concAgent();
    for(int index=0 ; index<nbReceivers ; index++) {
      Integer i = new Integer(index);
      r = r.insert(`agent(receiver(i),SLEEP,N(receiver(i),receiver(i))));
    }
    State state =
      `state(s,r,intruder(devil,concNonce(),concMessage()),concMessage());
    return state;
  }
  
  public boolean breadthSearch(State start, State end) {
    Collection c1 = new HashSet();
    c1.add(start);

    int i = 0;
    while(!c1.isEmpty()) {
      Collection c2 = new HashSet();
      Iterator it = c1.iterator();
      while(it.hasNext()) {
        collectOneStep((State)it.next(),c2);
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

  Collection result = new HashSet();
  public boolean depthSearch(State start, State end) {
    Collection c1 = new HashSet();
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

    Iterator it = c1.iterator();
    while(it.hasNext()) {
      boolean b = depthSearch((State)it.next(),end);
      if(b) return b;
    }
    return false;
  }


  public int compareMessage(Message m1, Message m2) {
      
    %match(Message m1, Message m2) {
      msg[src=sender[]], msg[src=receiver[]] -> { return 1;  }
      msg[src=receiver[]], msg[src=sender[]] -> { return -1;  }
      msg[src=receiver[]], msg[src=devil()]  -> { return 1;  }
      msg[src=sender[]], msg[src=devil()]    -> { return 1;  }
      msg[src=devil()], msg[src=receiver[]]  -> { return -1;  }
      msg[src=devil()], msg[src=sender[]]    -> { return -1;  }

      msg[src=x,dst=sender[]], msg[src=x,dst=receiver[]] -> { return 1;  }
      msg[src=x,dst=receiver[]], msg[src=x,dst=sender[]] -> { return -1;  }
      
    }
      

    
    String s1 = m1.toString();
    String s2 = m2.toString();
    
      //System.out.println("m1 = " + s1);
      //System.out.println("m2 = " + s2);
    int res = s1.compareTo(s2);

    
    
    return res;
  }

    /*
  ATermList insertMessage(Message m,ATermList l) {
    return l.insert(m);
  }
    */
    
  ATermList insertMessage(Message m,ATermList l) {
    ATermList res = null;
    if(l.isEmpty()) {
      res = l.insert(m);
    } else if(compareMessage(m, (Message)l.getFirst()) < 0) {
      res =  l.insert(m);
        //System.out.println("res = " + res);
    } else {
      res = insertMessage(m,l.getNext()).insert(l.getFirst());
        //System.out.println("res = " + res);
    }

    return res;
  }
    
  
  public boolean depthSearch2(Collection local, State start, State end) {
    Collection c1 = new HashSet();
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

    Iterator it = c1.iterator();
    Collection c = new HashSet();
    while(it.hasNext()) {
      boolean b = depthSearch2(c,(State)it.next(),end);
      if(b) return b;
    }
    return false;
  }
  
  public Nonce DN() {
    return `N(dai,dai);
  }

  public Address DA() {
    return `A(dai);
  }

  public boolean existAgent(Agent agent, ATermList list) {
      /*
        %match(Agent agent, ATermList list) {
        x, concAgent()          -> { return false; }
        x, concAgent(X1*,x,X2*) -> { return true; }
        }
        return false;
      */
    return list.indexOf(agent,0) >= 0;
  }

  public boolean existMessage(Message message, ATermList list) {
      /*
        %match(Message message, ATermList list) {
        x, concMessage            -> { return false; }
        x, concMessage(X1*,x,X2*) -> { return true; }
        }
        return false;
      */
    return list.indexOf(message,0) >= 0;
  }

  public int sizeMessage(ATermList list) {
    return list.getLength();
  }
  
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
              concAgent(E1*,agent(x,SLEEP(),_),E2*),
              dst@concAgent(D1*,agent(y,_,_),D2*),
              I,
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                State state = `state(
                  concAgent(E1*,agent(x,WAIT,N(x,y)),E2*),
                  dst,I,
                  insertMessage(msg(x,y,K(y),N(x,y),DN(),A(x)),M));
                c.add(state);
                fire[1]++;
              }
            } 
              // initiator 1
            state(
              concAgent(E1*,agent(x,SLEEP(),_),E2*),
              D, 
              intru@intruder(w,_,_),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                State state = `state(
                  concAgent(E1*,agent(x,WAIT(),N(x,w)),E2*),
                  D,intru,
                  concMessage(msg(x,w,K(w),N(x,w),DN(),A(x)),M*));
                c.add(state);
                fire[2]++;
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
              
              if(x==n1 && v==n3 && (!authVerif || v==z)) { // lowe
                State state = `state(
                  concAgent(E1*,agent(x,COMMIT(),N(x,v)),E2*),
                  D,
                  intru,
                  insertMessage(msg(x,v,K(v),N(n2,n4),DN(),DA()),concMessage(M1*,M2*)));
                c.add(state);
                fire[4]++;
              }

              if(x!=n1 || v!=n3 && (!authVerif || v!=z)) { // lowe
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
              concMessage(M1*,msg(w,y,K(y),N(n1,n3),N(_,_),A(v)),M2*)) -> {
              if(x==n3 && y==n1) {
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
              concMessage(M1*,msg(z,x,K(w),N(n1,n3),N(n2,n4),A(v)),M2*)) -> {
              if(w!=z) {
                State state = `state(
                  E, D,
                  intruder(w,simplify(concNonce(N(n1,n3),N(n2,n4),l*)),ll),
                  concMessage(M1*,M2*));
                c.add(state);
                fire[8]++;
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
                  intruder(w,l,insertMessage(message,ll)),
                  concMessage(M1*,M2*));
                c.add(state);
                fire[9]++;
              } 
            }

              //-----------------------
              // intruder sends (random) stored messages to
              //		  (random) agents from E and D
              // !! allow dupplicates in the network (in the list version)

            state(
              E, D,
              intru@intruder(w,l,concMessage(L1*,msg(x,y,K(z),N(n1,n3),N(n2,n4),A(v)),L2*)),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                ATermList subjectList = `concAgent(E*,D*);
                %match(ATermList subjectList) {
                  concAgent(A1*,agent(t,_,_),A2*) -> {
                    Message message = `msg(w,t,K(z),N(n1,n3),N(n2,n4),A(v));
                    if(!existMessage(message,M)) {
                      State state = `state(E,D,intru,insertMessage(message,M));
                      c.add(state);
                      fire[10]++;
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
              intru@intruder(w,listNonce@concNonce(L1*,resp,L2*),ll),
              M) -> {
              if(sizeMessage(M) < MaxMessagesInNetwork) {
                ATermList subjectED = `concAgent(E*,D*);
                %match(ATermList subjectED,
                       ATermList subjectED,
                       ATermList listNonce) {
                  concAgent(A1*,agent(y,_,_),A2*),
                  concAgent(A3*,agent(xadd,_,_),A4*),
                  concNonce(L3*,init,L4*) -> {
                    Message message = `msg(w,y,K(y),resp,init,A(xadd));
                    if(!existMessage(message,M)) {
                      State state = `state(E,D,intru,insertMessage(message,M));
                      c.add(state);
                      fire[11]++;
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
              pool1@concAgent(E1*,agent(x,COMMIT(),N(x,y)),E2*),
              D,intruder(w,l,ll),M) -> {
              if(y!=w &&
                 !existAgent(`agent(y,WAIT(),N(y,x)),D) &&
                 !existAgent(`agent(y,COMMIT(),N(y,x)),D) ) {
                State state = `ATTACK();
                c.add(state);
                fire[12]++;
              } 
            }

              //---------------------------
              // attack on the sender
            state(
              E,
              pool1@concAgent(D1*,agent(y,COMMIT(),N(y,x)),D2*),
              intruder(w,l,ll),M) -> {
              if(x!=w && !existAgent(`agent(x,COMMIT(),N(x,y)),E) ) {
                State state = `ATTACK();
                c.add(state);
                fire[13]++;
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
  private static boolean authVerif = true;
  private static int fire[] = new int[20];

  public final static void main(String[] args) {
    Nsh test = new Nsh(new TermFactory(16));
    int nbAgent = 1;
    try {
      nbAgent = Integer.parseInt(args[0]);
      MaxMessagesInNetwork = Integer.parseInt(args[1]);
    } catch (Exception e) {
      System.out.println("Usage: java Nsh <nbAgent> <maxMsg>");
      return;
    }

    test.run(nbAgent);

      //for(int i=1 ; i<15 ; i++) {
      //System.out.println("fire[" + i + "] = " + fire[i]);
      //}
    
  }

  
}
