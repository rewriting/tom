public abstract class List extends AbstractType {

  public AbstractType accept(Visitor v) throws jjtraveler.VisitFailure {
    return v.visit_List(this);
  }

	public boolean isEmpty() {
		return false;
	}
  
	public boolean isCons() {
		return false;
	}
	
	public boolean isConsInt() {
		return false;
	}

	public Element getHead() {
    throw new UnsupportedOperationException("This List has no head");
	}

	public List getTail() {
    throw new UnsupportedOperationException("This List has no tail");
	}
	
	public int getHeadInt() {
    throw new UnsupportedOperationException("This List has no headint");
	}
	
  public static List fromTerm(aterm.ATerm trm) {
    List tmp;
    tmp = Empty.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Cons.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }
    
		tmp = ConsInt.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a List: " + trm);
  }
}
