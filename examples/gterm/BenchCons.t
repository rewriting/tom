public class BenchCons {
  private Factory factory;

  %include { int.tom }

  public BenchCons(Factory factory) {
    this.factory = factory;
  }

	public Factory getFactory() {
		return factory;
	}

  %typeterm TomList {
    implement { List }
    equals(l1,l2) { l1==l2 }
  }

  %oplist TomList conc( int* ) {
    is_fsym(t) { t instanceof Cons }
    make_empty()  { factory.makeEmpty() }
    make_insert(e,l) { factory.makeCons(factory.makeInt(e),l) }
    get_head(l)   { ((Int)((Cons)l).getHead()).getValue() }
    get_tail(l)   { ((Cons)l).getTail() }
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

  public List elim(List l) {
    %match(TomList l) {
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
      %match(TomList l) {
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

    //System.out.println(" l = " + elim(reverse(genere(100))));
    elim(reverse(genere(max)));
    
    System.out.println(max + " " + (System.currentTimeMillis()-startChrono));
  }

  public final static void main(String[] args) {
    BenchCons test = new BenchCons(new Factory());
    int max = 100;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java list.BenchCons <max>");
      return;
    }
    test.run(max);
  }

}

