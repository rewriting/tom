public class Cons extends List  {
	private int hashCode;

	private Element head;
	private List tail;

	public boolean isCons() {
		return true;
	}

  protected void init(Element head, List tail,int hashCode) {
		this.head = head;
		this.tail = tail;
    this.hashCode = hashCode;
  }

  protected void initHashCode(Element head, List tail) {
		this.head = head;
		this.tail = tail;
    this.hashCode = this.hashFunction();
  }

	public int hashCode() {
		return this.hashCode;
	}
	
  public shared.SharedObject duplicate() {
    Cons clone = new Cons();
    clone.init(head,tail,hashCode());
    return clone;
  }
  
	public boolean equivalent(shared.SharedObject obj) {
		if(obj instanceof Cons) {
			Cons peer = (Cons) obj;
			//return head.equals(peer.getHead()) && tail.equals(peer.getTail());
			return head==peer.getHead() && tail==peer.getTail();
		}
		return false;
  }

  public String getName() {
    return "Cons";
  }

  public int getArity() {
    return 2;
  }

	public Element getHead() {
		return head;
	}

	public List getTail() {
		return tail;
	}

  public int getChildCount() {
    return getArity();
  }

	public jjtraveler.Visitable getChildAt(int index) {
		switch(index) {
			case 0: return getHead();
			case 1: return getTail();
			default: throw new IndexOutOfBoundsException();
		}
  }

  public jjtraveler.Visitable setChildAt(int index, jjtraveler.Visitable v) {
		switch(index) {
			case 0: return Factory.getInstance().makeCons((Element)v,getTail());
			case 1: return Factory.getInstance().makeCons(getHead(),(List)v);
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

		a += (head.hashCode() << 8);
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
