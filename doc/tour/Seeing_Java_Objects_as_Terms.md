# Seeing Java Objects as Terms

## Matching java.util.LinkedList -- standard mappings

```java
import java.util.Arrays;
import java.util.LinkedList;

public class Mapping {

  /* includes the mapping for java's LinkedList
     provided with the tom distribution */
  %include{ util/LinkedList.tom }

  public static void main(String[] args) {
    LinkedList<Integer> l = new LinkedList(Arrays.asList(1,2,3,1,4,3,1,1));
    System.out.println("list = " + l);

    /* We can match linked lists like any other associative constructor.
       Since we declare the Sort (LinkedList) in the %match construct,
       we can use the implicit notation (without the constructor name)
       in the patterns and thus do not need to look at the mapping file. */
    %match(LinkedList l) {
      concLinkedList(_*,x,_*) -> { System.out.println("iterate: " + `x); }
      concLinkedList(_*,x,_*,x,_*) -> { System.out.println("appears twice: " + `x); }
    }
    %match(LinkedList l, LinkedList l) {
      concLinkedList(_*,x,_*), !concLinkedList(_*,x,_*,x,_*) -> { System.out.println("appears only once: " + `x); }
    }
  }
}
```

```
$ tom Mapping.t && javac Mapping.java && java Mapping
list = [1, 2, 3, 1, 4, 3, 1, 1]
iterate: 1
iterate: 2
iterate: 3
iterate: 1
iterate: 4
iterate: 3
iterate: 1
iterate: 1
appears twice: 1
appears twice: 1
appears twice: 1
appears twice: 3
appears twice: 1
appears twice: 1
appears twice: 1
appears only once: 2
appears only once: 4
```

## Matching cars -- Handwritten Mappings

```java
public class HandMapping {

  /* We need to include the standard mappings for
     java integers and strings since we declare
     fields of these sorts in the following mapping */
  %include { int.tom }
  %include { string.tom }

  /* This class is here for pedagogic reasons.
     We could have worked with the only 'Car' class */
  private static class Vehicle { }

  /* This demo class declares two fields with
     the corresponding getters and setters, as well
     as a custom version of equals. */
  private static class Car extends Vehicle {
    private int seats = 0;
    private String color = null;

    public Car(int seats, String color) {
      this.seats = seats;
      this.color = color;
    }
    public int getSeats() { return seats; }
    public String getColor() { return color; }
    public void setSeats(int s) { seats = s; }
    public void setColor(String c) { color = c; }
    public boolean equals(Car c) {
      return c.seats == seats && c.color.equals(color);
    }
  }

  /* We define a mapping for the Vehicle and Car classes
     so that it behave like the following gom signature:
     Vehicle = car(seats:int, color:String) */

  // typeterm defines a Sort (Vehicle here)
  %typeterm Vehicle {
    // The corresponding java type
    implement { HandMapping.Car }
    // How to check the sort
    is_sort(c) { c instanceof HandMapping.Car }
    // How to check the equality between two terms of sort Car
    equals(c1,c2) { c1.equals(c2) }
  }

  %op Vehicle car(seats:int, color:String) {
    // Is the head symbol of the tested term a car ?
    is_fsym(c)        { c instanceof Car }
    // Get the slot name 'seats'
    get_slot(seats,c) { c.getSeats() }
    get_slot(color,c) { c.getColor() }
    // For the ` (backquote) construct
    make(s,c)         { new Car(s,c) }
  }

  /* We can now perform standard term construction
     and pattern matching on vehicles */
  public static void main(String[] args) {
    Vehicle c1 = `car(4,"blue");
    Vehicle c2 = `car(6,"red");
    Vehicle c3 = `car(2,"blue");

    %match (c2) {
      car(s,"red") -> {
        System.out.println("this red car has " + `s + " seats");
      }
    }
    %match (c1,c3) {
      car(_,c), car(_,c) -> {
        System.out.println("c1 and c3 have the same " + `c + " color");
      }
    }
    %match (Vehicle c1, Vehicle c1) {
      // note that we use the definition of equality here
      x@car(s,c), x -> {
        System.out.println("This is the same "+`c +" car with "+`s +" seats");
      }
    }
  }
}
```

```
$ tom HandMapping.t && javac HandMapping.java && java HandMapping
this red car has 6 seats
c1 and c3 have the same blue color
This is the same blue car with 4 seats
```