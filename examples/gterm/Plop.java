public class Plop extends Element {
  private static Plop proto = new Plop();
	private int hashCode;
  private Plop() {}

	public static Plop make() {
    proto.initHashCode();
    return (Plop) shared.SingletonSharedObjectFactory.getInstance().build(proto);
	}

  private void init(int hashCode) {
    this.hashCode = hashCode;
  }

  private void initHashCode() {
    this.hashCode = this.hashFunction();
  }

  private String getName() {
    return "Plop";
  }

  private int getArity() {
    return 0;
  }

  /* shared.SharedObject */
	public int hashCode() {
		return this.hashCode;
	}

  public shared.SharedObject duplicate() {
    Plop clone = new Plop();
    clone.init(hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof Plop) {
			return true;
		}
		return false;
  }

  /* List */
	public boolean isPlop() {
		return true;
	}

  /* AbstractType */
	public aterm.ATerm toATerm() {
		return aterm.pure.SingletonFactory.getInstance().makeAppl(
				aterm.pure.SingletonFactory.getInstance().makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {});
	}

	public static Element fromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(proto.getName().equals(appl.getName())) {
				return make();
			}
		}
		return null;
  }

  /* jjtraveler.Visitable */
  public int getChildCount() {
    return 0;
  }

	public jjtraveler.Visitable getChildAt(int index) {
		switch(index) {
			default: throw new IndexOutOfBoundsException();
		}
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
		switch(index) {
			default: throw new IndexOutOfBoundsException();
		}
  }

  /* internal use */
  private int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */
    //b += (stringHashFunction(getName(),getArity()) << 8);
    
		/* case 0: nothing left to add */
    a -= b;
    a -= c;
    a ^= (c >> 13);
    b -= c;
    b -= a;
    b ^= (a << 8);
    c -= a;
    c -= b;
    c ^= (b >> 13);
    a -= b;
    a -= c;
    a ^= (c >> 12);
    b -= c;
    b -= a;
    b ^= (a << 16);
    c -= a;
    c -= b;
    c ^= (b >> 5);
    a -= b;
    a -= c;
    a ^= (c >> 3);
    b -= c;
    b -= a;
    b ^= (a << 10);
    c -= a;
    c -= b;
    c ^= (b >> 15);

    /*-------------------------------------------- report the result */
    return c;
  }

}
