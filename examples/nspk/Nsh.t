import aterm.*;
import java.util.*;
import jtom.runtime.*;
import adt.*;

public class Nsh {

  private TermFactory factory;
  private GenericTraversal traversal;

    %include { term.t }


    /*
  %typelist ListAgent {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("consListAgent", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2) { l1==l2 }
    get_head(l)   { l.getFirst() }
    get_tail(l)   { l.getNext() }
    is_empty(l)   { l.isEmpty() }
  }

  %oplist ListAgent consListAgent( Agent* ) {
    fsym { factory.makeAFun("consListAgent", 1, false) }
    make_empty()  { factory.makeList() }
    make_insert(e,l) { l.insert(e) }
  }
    */

  %typelist ATerm {
    implement { ATermList }
    get_fun_sym(t) { ((t instanceof ATermList)?factory.makeAFun("consListAgent", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2) { l1==l2 }
    get_head(l)   { l.getFirst() }
    get_tail(l)   { l.getNext() }
    is_empty(l)   { l.isEmpty() }
  }

  %oplist ATerm consListAgent( Agent* ) {
    fsym { factory.makeAFun("consListAgent", 1, false) }
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
      consListAgent(agent(a,SLEEP,N(a,a))),
      consListAgent(agent(b,SLEEP,N(b,b)),agent(c,SLEEP,N(c,c))),
      intruder(devil,nilListNonce,nilListMessage),
      nilListMessage);


    Collection c = new HashSet();
    long startChrono = System.currentTimeMillis();
    res = initState;


    collectOneStep(initState,c);
    
    long stopChrono = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }


  public boolean existAgent(Agent agent, ATermList list) {
      /*
    %match(Agent agent, ListAgent list) {
      x, nilListAgent       -> { return false; }
      x, consListAgent(x,l) -> { return true; }
      x, consListAgent(y,l) -> { return existAgent(x,l); }
      }
      */
    %match(Agent agent, ATerm list) {
      x, consListAgent()          -> { return false; }
      x, consListAgent(X1*,x,X2*) -> { return true; }
    }
    return false;
  }

  public boolean existMessage(Message message, ListMessage list) {
    %match(Message message, ListMessage list) {
      x, nilListMessage       -> { return false; }
      x, consListMessage(x,l) -> { return true; }
      x, consListMessage(y,l) -> { return existMessage(x,l); }
    }
    return false;
  }

  public int sizeMessage(ListMessage list) {
    %match(ListMessage list) {
      nilListMessage       -> { return 0; }
      consListMessage(x,l) -> { return 1 + sizeMessage(l); }
    }
    return 0;
  }
  
    /*
     *
     */
  public void collectOneStep(State subject, final Collection collection) {
    Collect2 collect = new Collect2() { 
        public boolean apply(ATerm t, Object arg1) {
          Collection c = (Collection) arg1;
          %match(State t) { 
            state(
              consListAgent(E1*,agent(x,SLEEP,resp),E2*),
              consListAgent(D1*,agent(y,std,init),D2*),
              I, ls)
              -> {

              System.out.println("x = " + x + "\ty = " + y);
              

            }

          }
          
          return true;
        } // end apply
      }; // end new

    collect.apply(subject,collection);
    
      //traversal.genericCollect(subject, collect, collection); 
  }

  
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

