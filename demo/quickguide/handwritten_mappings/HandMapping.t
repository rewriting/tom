public class HandMapping {

  %include { int.tom }
  %include { string.tom }

  private static class Car {
    private int seats = 0;
    private String color = null;
    
    public Car(int seats, String color) {
      this.seats = seats;
      this.color = color;
    }
    public int getSeats() { return seats; }
    public String getColor() { return color; }
    public void setSeats(int s) { seats = s; }
    public void getColor(String c) { color = c; }
    public boolean equals(Car c) { 
      return c.seats == seats && c.color.equals(color); 
    }
  }

  %typeterm Car {
    implement { HandMapping.Car }
    is_sort(c) { c instanceof HandMapping.Car }
    equals(c1,c2) { c1.equals(c2) }
  }

  %op Car car(seats:int, color:String) {
    is_fsym(c)        { c instanceof Car }
    get_slot(seats,c) { c.getSeats() }
    get_slot(color,c) { c.getColor() }
    make(s,c)         { new Car(s,c) } 
  }

  public static void main(String[] args) {
    Car c1 = `car(4,"blue");
    Car c2 = `car(6,"red");
    Car c3 = `car(2,"blue");

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
  }
}

