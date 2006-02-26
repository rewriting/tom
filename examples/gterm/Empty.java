public class Empty extends List {
	private int hashCode;

	public boolean isEmpty() {
		return true;
	}
  public boolean isCons() {
    return true;
  }

  protected void init(int hashCode) {
    this.hashCode = hashCode;
  }

  protected void initHashCode() {
    this.hashCode = this.hashFunction();
  }

	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    Empty clone = new Empty();
    clone.init(hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof Empty) {
			return true;
		}
		return false;
  }

  public String getName() {
    return "Empty";
  }

  public int getArity() {
    return 0;
  }

	public aterm.ATerm toATerm() {
		return ATFactory.makeAppl(
				ATFactory.makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {});
	}

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
	
	public AbstractType accept(VisitorForward v) throws jjtraveler.VisitFailure {
    return v.visit_List_Empty(this);
  }
	
  protected int hashFunction() {
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
