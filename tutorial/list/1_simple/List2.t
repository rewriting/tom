import aterm.*;
import aterm.pure.*;
import java.util.*;

public class List2 {
  private ATermFactory factory;

  public List2(ATermFactory factory) {
    this.factory = factory;
  }

  %typearray TomList {
    implement { ArrayList }
    get_fun_sym(t)   { ((t instanceof ArrayList)?factory.makeAFun("conc", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)      { l1.equals(l2) }
    get_element(l,n)   { l.get(n) }
    get_size(l)        { l.size() }
  }

  %oparray TomList conc( TomTerm* ) {
    fsym            { factory.makeAFun("conc", 1, false) }
    make_empty(n)   { myEmpty(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
  }

  private ArrayList myAdd(Object e,ArrayList l) {
    l.add(e);
    return l;
  }
  
  private ArrayList myEmpty(int n) {
    ArrayList res = new ArrayList(n);
    return res;
  }
  
  %typeterm TomTerm {
    implement { ATermAppl }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    get_fun_sym(t)     { t.getAFun() }
    get_subterm(t, n)  { t.getArgument(n) }
    equals(t1, t2)     { t1==t2 }
  }

  %op TomTerm a {
    fsym { factory.makeAFun("a", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("a", 0, false)) }
  }
  
  %op TomTerm b {
    fsym { factory.makeAFun("b", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("b", 0, false)) }
  }

  %op TomTerm c {
    fsym { factory.makeAFun("c", 0, false) }
    make() { factory.makeAppl(factory.makeAFun("c", 0, false)) }
  }
  
  public ArrayList swapSort(ArrayList l) {
    %match(TomList l) {
      conc(X1*,x,X2*,y,X3*) -> {
        String xname = x.getName();
        String yname = y.getName();
        if(xname.compareTo(yname) > 0) {
            //return `swapSort(conc(X1*,y,X2*,x,X3*));
          System.out.println("x -> " + x + "\ty -> " + y);
          
        }
      }

      _ -> { return l; }
    }
  }

  public ArrayList removeDouble(ArrayList l) {
    %match(TomList l) {
      conc(X1*,x,x,X2*) -> {
        return `removeDouble(conc(X1*,x,X2*));
        
      }

      _ -> { return l; }
    }
  }

  public void run() {
    ArrayList l    = `conc(a,b,c,a,b,c,a);
    ArrayList res1 = swapSort(l);
    ArrayList res2 = removeDouble(res1);
    System.out.println(" l       = " + l);
    System.out.println("sorted l = " + res1);
    System.out.println("single l = " + res2);
  }

  public final static void main(String[] args) {
    List2 test = new List2(new PureFactory(16));
    test.run();
  }

}

