public class ConsInt extends List  {
	private int hashCode;

	private int headint;
	private List tail;

	public boolean isConsInt() {
		return true;
	}

  protected void init(int headint, List tail,int hashCode) {
		this.headint = headint;
		this.tail = tail;
    this.hashCode = hashCode;
  }

  protected void initHashCode(int headint, List tail) {
		this.headint = headint;
		this.tail = tail;
    this.hashCode = this.hashFunction();
  }

	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    ConsInt clone = new ConsInt();
    clone.init(headint,tail,hashCode);
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof ConsInt) {
			ConsInt peer = (ConsInt) obj;
			//return headint.equals(peer.getHeadInt()) && tail.equals(peer.getTail());
			return headint==peer.getHeadInt() && tail==peer.getTail();
		}
		return false;
  }

  public String getName() {
    return "ConsInt";
  }

  public int getArity() {
    return 2;
  }

	public int getHeadInt() {
		return headint;
	}

	public List getTail() {
		return tail;
	}
	
	public aterm.ATerm toATerm() {
		return ATFactory.makeAppl(
				ATFactory.makeAFun(getName(),getArity(),false), 
				new aterm.ATerm[] {ATFactory.makeInt(getHeadInt()), getTail().toATerm()});
	}

  public int getChildCount() {
    return getArity();
  }

	public jjtraveler.Visitable getChildAt(int index) {
		switch(index) {
			case 0:	throw new RuntimeException("no children!");
			case 1: return getTail();
			default: throw new IndexOutOfBoundsException();
		}
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
		switch(index) {
			case 0:	throw new RuntimeException("no children!");
			case 1: return Factory.getInstance().makeConsInt(getHeadInt(),(List)v);
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

		a += (headint << 8);
		a += (tail.hashCode());
    
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
