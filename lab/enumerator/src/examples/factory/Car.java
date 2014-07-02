package examples.factory;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Car {
	private int no;
	private Color color;
	private Student driver;

<<<<<<< HEAD
//	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no, @Enumerate Color color) {
=======
	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no,Color color) {
>>>>>>> 5526a6d0a1cc7b0935b6966a33295bf3b9ddff19
		this.no = no;
		this.color = color;
	}

	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no, @Enumerate Student driver) {
		this.no = no;
		this.driver = driver;
	}

	@Override
	public String toString() {
		return "Car [no=" + no + ", color=" + color + ", driver=" + driver
				+ "]";
	}

}
