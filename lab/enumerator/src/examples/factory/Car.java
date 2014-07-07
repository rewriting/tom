package examples.factory;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Car {
	private int no;
	private Color color;
	private Student driver;

//	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no, @Enumerate Color color) {
		this.no = no;
		this.color = color;
	}
	
	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no, @Enumerate Color color,Student driver) {
		this.no = no;
		this.color = color;
		this.driver=driver;
	}

//	@EnumerateGenerator(canBeNull = true)
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
