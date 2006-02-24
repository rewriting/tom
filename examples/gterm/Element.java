public abstract class Element extends AbstractType {

	public boolean isPlop() {
		return false;
	}
	public boolean isInt() {
		return false;
	}
  
	public int getValue() {
    throw new UnsupportedOperationException("This Element has no value");
	}
}
