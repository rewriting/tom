package gterm;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;

public class Strat {
  %include { int.tom }
  %include { mutraveler.tom }

  %typeterm TomList {
    implement { List }
    equals(l1,l2) { l1==l2 }
  }

  %oplist TomList conc( int* ) {
    is_fsym(t) { t instanceof ConsInt || t instanceof Empty }
    make_empty()  { Empty.make() }
    make_insert(e,l) { ConsInt.make(e,l) }
    get_head(l)   { l.getHeadInt() }
    get_tail(l)   { l.getTail() }
    is_empty(l)   { l.isEmpty() }
  }
  
  public List genere(int n) {
    if(n>2) {
      List l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public void run(int max) {
		List subject = genere(max);
    VisitableVisitor rule = new RewriteSystem();
		try {
      System.out.println("subject       = " + subject);
      System.out.println("onceBottomUp  = " + MuTraveler.init(`OnceBottomUp(rule)).visit(subject));
      System.out.println("BottomUp  = " + MuTraveler.init(`BottomUp(Try(rule))).visit(subject));
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    } catch (Exception e) {
			System.out.println(e);
		}
  }

  public final static void main(String[] args) {
    Strat test = new Strat();
    try {
      int max = Integer.parseInt(args[0]);
			test.run(max);
    } catch (Exception e) {
      System.out.println("Usage: java list.Strat <max>");
      return;
    }
  }
  
	class RewriteSystem extends BasicStrategy {
    public RewriteSystem() {
      super(`Fail());
    }
    
    public List visit_List(List arg) throws VisitFailure { 
      %match(TomList arg) {
				conc(h,t*) -> {
					int v = `h+1;
					return `conc(v,t*);
				}
      }
      return (List)`Fail().visit(arg);
    }
  }

}

