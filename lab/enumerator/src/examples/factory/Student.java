package examples.factory;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Student {

	private int no;
	private String name;

	@EnumerateGenerator(canBeNull = true)
	public Student(@Enumerate(maxSize = 8) int no,
			@Enumerate(maxSize = 2) String string) {
		this.no = no;
		this.name = string;
	}

	@Override
	public String toString() {
		return "Student [no=" + no + ", name=" + name + "]";
	}

}
