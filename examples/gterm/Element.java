public abstract class Element extends AbstractType {
  
  public AbstractType accept(Visitor v) throws jjtraveler.VisitFailure {
    return v.visit_Element(this);
  }

	public boolean isPlop() {
		return false;
	}

  public boolean isInt() {
		return false;
	}
  
	public int getValue() {
    throw new UnsupportedOperationException("This Element has no value");
	}
	
  public static Element fromTerm(aterm.ATerm trm) {
    Element tmp;
    tmp = Plop.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    tmp = Int.fromTerm(trm);
    if (tmp != null) {
      return tmp;
    }

    throw new IllegalArgumentException("This is not a Element: " + trm);
  }
}
