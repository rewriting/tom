package examples.factory;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Room {
    private int no;
    private Student student;

     @EnumerateGenerator(canBeNull = true)
    public Room(@Enumerate(maxSize = 8) int no, @Enumerate Student student) {
        this.no = no;
        this.student = student;
    }

	@Override
	public String toString() {
		return "Room [no=" + no + ", student=" + student + "]";
	}


}
