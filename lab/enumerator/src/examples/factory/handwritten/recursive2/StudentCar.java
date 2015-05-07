package examples.factory.handwritten.recursive2;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class StudentCar {
    
    private int no;
    private StudentWithCar driver;


    @EnumerateGenerator(canBeNull = true)
    public StudentCar(@Enumerate(maxSize = 8) int no, @Enumerate(maxSize = 4) StudentWithCar driver) {
        this.no = no;
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "StudentCar [no=" + no + ", driver=" + driver + "]";
    }

}
