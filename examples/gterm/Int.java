package gterm;

public class Int extends Element {
  private static Int proto = new Int();
	private int hashCode;

	private int value;

  private Int() {}

	public boolean isInt() {
		return true;
	}

	public static Int make(int value) {
      proto.initHashCode(value);
      return (Int) shared.SingletonSharedObjectFactory.getInstance().build(proto);
	}

  private void init(int value,int hashCode) {
		this.value = value;
    this.hashCode = hashCode;
  }

  private void initHashCode(int value) {
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

	public aterm.ATerm toATerm() {
		return aterm.pure.SingletonFactory.getInstance().makeAppl(
				aterm.pure.SingletonFactory.getInstance().makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {
					aterm.pure.SingletonFactory.getInstance().makeInt(getValue()) // special case for builtin argument
        });
	}
	
	public static Element fromTerm(aterm.ATerm trm) {
		if(trm instanceof aterm.ATermAppl) {
			aterm.ATermAppl appl = (aterm.ATermAppl) trm;
			if(proto.getName().equals(appl.getName())) {
				return make(
						((aterm.ATermInt)appl.getArgument(0)).getInt() // special case for builtin argument
						);
			}
		}
		return null;
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
