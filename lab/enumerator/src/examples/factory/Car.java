package examples.factory;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Car {
	private int no;
	private Color color;

	@EnumerateGenerator(canBeNull = true)
	public Car(@Enumerate(maxSize = 8) int no, @Enumerate Color color) {
		this.no = no;
		this.color = color;
	}

	@Override
	public String toString() {
		return "Car [no=" + no + ", color=" + color + "]";
	}

}
