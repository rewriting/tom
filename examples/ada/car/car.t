with System; use System;
with Ada.Text_IO; use Ada.Text_IO;
package body Car is

 
  %include { int.tom }
  %include { string.tom }
 
  type Vehicle is tagged null record;

  type Car is new Vehicle with
  record
    seats: Integer;
    color: access String;
  end record;


 -----------------------------------------------------
 -- Mapping TOM
 -----------------------------------------------------
 
  %typeterm TomCar {
    implement { tagged: access Car }  
    is_sort(c) { $c.all in Car'Class } 
    equals(c1,c2) { $c1.all = $c2.all }
  }
 
  %op TomCar car(seats:int, color:String) {
    is_fsym(c)        { $c.all in Car }
    get_slot(seats,c) { Car($c.all).seats}
    get_slot(color,c) { Car($c.all).color}
    make(s,c)         { new Car'($s, $c) } 
  }
  
 -----------------------------------------------------
 -- Example of use
 -----------------------------------------------------

  type StringPtr is access all String;

  procedure main is
    color_green : StringPtr := new String'("green");
    color_black : StringPtr := new String'("black");
    c1 : access Car := `car(4, color_black);
    c2 : access Car := `car(6, color_green);
    c3 : access Car := `car(2, color_black);
    c4 : access Car := `car(4, color_black);
  begin
   
    %match (TomCar c2) {
      car(s, "green") -> { 
      put_line("this green car has" & Integer'Image(`s) & " seats"); 
      }
    }
    %match (c1,c3) {
      car(_,c), car(_,c) -> { 
      put_line("c1 and c3 have the same " & `c.all & " color");
      }
    }
    %match (TomCar c1, TomCar c4) {
      x@car(s,c), x -> { 
      put_line("This is the same " & `c.all & " car with" & Integer'Image(`s) & " seats"); 
      }
    }
  end Main;
end Car;
