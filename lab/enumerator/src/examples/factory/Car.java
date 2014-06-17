package examples.factory;

public class Car {
	private int no;
	private Color color;

	
	public Car(int no, Color color) {
		this.no = no;
		this.color = color;
	}


	@Override
	public String toString() {
		return "Car [no=" + no + ", color=" + color + "]";
	}

}
