package examples.factory;

import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Car {
    private int no;
    private Color color;

    @Enumerate(canBeNull = true)
    public Car(@Enumerate(maxSize = 8) int no, @Enumerate Color color) {
        this.no = no;
        this.color = color;
    }

}
