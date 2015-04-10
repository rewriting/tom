package examples.factory;

import java.util.ArrayList;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class StudentWithCar {

    private int no;
    private String name;
    private StudentCar car;

    @EnumerateGenerator(canBeNull = true)
    public StudentWithCar(
        @Enumerate(maxSize = 8) int no,
        @Enumerate(maxSize = 4) String name,
        @Enumerate(maxSize = 6) StudentCar car
    ) {
        this.no = no;
        this.name = name;
        this.car = car;
        new ArrayList<Integer>();
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    
    public StudentCar getCar() {
        return car;
    }

    @Override
    public String toString() {
        return "StudentWithCar [no=" + no + ", name=" + name + ", studentcar="+car+ "]";
    }

}
