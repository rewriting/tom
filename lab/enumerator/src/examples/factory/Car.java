package examples.factory;

public class Car {
	private int no;
	private Color color;

	// @Generator(canBeNull=true)
	public Car(
			//@Enumerate(maxSize=4)
			int no, 
			//@Enumerate
			Color color) {
		this.no = no;
		this.color = color;
	}

	@Override
	public String toString() {
		return "Car [no=" + no + ", color=" + color + "]";
	}

}
