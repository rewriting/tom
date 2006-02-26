public class Int extends Element {
	private int hashCode;

	private int value;

	public boolean isInt() {
		return true;
	}

  protected void init(int value,int hashCode) {
		this.value = value;
    this.hashCode = hashCode;
  }

  protected void initHashCode(int value) {
		this.value = value;
    this.hashCode = this.hashFunction();
  }

	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    Int clone = new Int();
    clone.init(value,hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof Int) {
			Int peer = (Int) obj;
			return value == peer.getValue();
		}
		return false;
  }

	public int getValue() {
		return value;
	}

  public String getName() {
    return "Int";
  }

  public int getArity() {
    return 1;
  }

  public int getChildCount() {
    return 0;
  }

	/*
	 * meme remarque que dans Factory: construire le constructeur Int ici
	 */
	public aterm.ATerm toATerm() {
		return ATFactory.makeInt(getValue());
	}
	
	public jjtraveler.Visitable getChildAt(int index) {
		switch(index) {
			// skip arg0:builtin
			default: throw new IndexOutOfBoundsException();
		}
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
		switch(index) {
			// skip arg0:builtin
			default: throw new IndexOutOfBoundsException();
		}
  }
	
	public AbstractType accept(VisitorForward v) throws jjtraveler.VisitFailure {
    return v.visit_Element_Int(this);
  }

  protected int hashFunction() {
    int a, b, c;

    /* Set up the internal state */
    a = b = 0x9e3779b9; /* the golden ratio; an arbitrary value */
    c = getArity();
    /*---------------------------------------- handle most of the key */

    /*------------------------------------- handle the last 11 bytes */
    //b += (stringHashFunction(getName(),getArity()) << 8);
    
		a += value; // special case for builtin argument

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
