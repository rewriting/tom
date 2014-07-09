package examples.factory;

import java.util.ArrayList;

import examples.factory.generation.Enumerate;
import examples.factory.generation.EnumerateGenerator;

public class Student {

	private int no;
	private String name;

	@EnumerateGenerator(canBeNull = false)
	public Student(@Enumerate(maxSize = 8) int no,
			@Enumerate(maxSize = 4) String string) {
		this.no = no;
		this.name = string;
		new ArrayList<Integer>();
	}

	@Override
	public String toString() {
		return "Student [no=" + no + ", name=" + name + "]";
	}

}
