import list.types.*;

public class BenchConsGom {
  %include { list/List.tom }

  public List genere(int n) {
    if(n>2) {
      List l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public List elim(List l) {
    %match(List l) {
      conc(x*,e1,y*,e2,z*) -> {
        if(`e2%`e1 == 0) {
          return `elim(conc(x*,e1,y*,z*));
        }
      }
    }
    return l;
  }

  public List reverse(List l) {
    List res = `conc();
    while(true) {
      %match(List l) {
        conc() -> { return res; }
        conc(h,t*) -> {
          res = `conc(h,res*);
          l = `t;
        }
      }
    }
  }

  public void run(int max) {
    //System.out.println(" l = " + genere(100));

    long startChrono = System.currentTimeMillis();
    
    elim(reverse(genere(max)));
    
    System.out.println(max + " " + (System.currentTimeMillis()-startChrono));
    System.out.println(shared.SingletonSharedObjectFactory.getInstance());
  }

  public final static void main(String[] args) {
    BenchConsGom test = new BenchConsGom();
    int max = 100;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java list.BenchConsGom <max>");
      return;
    }
    test.run(max);
  }

}
