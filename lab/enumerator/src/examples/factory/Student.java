package examples.factory;

import java.util.ArrayList;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Student {

	private int no;
	private String name;

	@EnumerateGenerator(canBeNull = true)
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
