package examples.factory;

import tom.library.factory.old.Enumerate;
import tom.library.factory.old.EnumerateGenerator;

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
