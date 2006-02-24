public abstract class List extends AbstractType {

	public boolean isEmpty() {
		return false;
	}
  
	public boolean isCons() {
		return false;
	}

	public Element getHead() {
    throw new UnsupportedOperationException("This List has no head");
	}

	public List getTail() {
    throw new UnsupportedOperationException("This List has no tail");
	}
}
