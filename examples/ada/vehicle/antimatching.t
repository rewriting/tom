with System; use System;
with Ada.Text_IO; use Ada.Text_IO;

package body AntiMatching is


	type Colors is tagged null record;

	type Types is tagged null record;

	type Ecological is new Types with null record;

	type Polluting is new Types with null record;

	type Red is new Colors with null record;

	type Green is new Colors with null record;

	type Vehicle is tagged null record;

	type Car is new Vehicle with
	record
		interior: access Colors;
		exterior: access Colors;
		ecotype: access Types;
	end record;

	type Bike is new Vehicle with null record;

	function image(t: access Colors'Class) return String is
	begin
	if (t.all in Red) then
	return "red";
	else
	return "green";
	end if;
	end;

	function image(t: access Types'Class) return String is
	begin
	if (t.all in Polluting) then
	return "polluting";
	else
	return "ecological";
	end if;
	end;


	function image(t: access Vehicle'Class) return String is
	begin
	if t.all in Car then
		declare
		c: Car := Car(t.all);
		begin
			return "car(" & image(c.interior) & "," & image(c.exterior) & "," & image(c.ecotype) & ")";
		end;
	else
		return "bike";
	end if;
	end;

	%typeterm TomVehicle {
	  implement { tagged: access Vehicle }
	  is_sort(t) { $t.all in Vehicle'Class }
	  equals(t1,t2) { ($t1.all=$t2.all) }

	}

	%typeterm TomTypes {
	  implement { tagged: access Types }
	  is_sort(t) { $t.all in Types'Class }
	  equals(t1,t2) { ($t1.all=$t2.all) }

	}

	%typeterm TomColors {
	  implement { tagged: access Colors }
	  is_sort(t) { $t.all in Colors'Class }
	  equals(t1,t2) { ($t1.all=$t2.all) }

	}

	%op TomVehicle car(interior:TomColors, exterior:TomColors, Types:TomTypes) {
	  is_fsym(t) { $t.all in Car }
	  get_slot(interior, t) { Car($t.all).interior }
	  get_slot(exterior, t) { Car($t.all).exterior }
	  get_slot(Types, t) { Car($t.all).ecotype }
	  make(t0, t1, t2) { new Car'($t0, $t1, $t2) }
	}

	%op TomVehicle bike() {
	  is_fsym(t) { $t.all in Bike }
	  make() {new Bike}
	}

	%op TomTypes ecological() {
	  is_fsym(t) { $t.all in Ecological }
	  make() {new Ecological}
	}

	%op TomTypes polluting() {
	  is_fsym(t) { $t.all in Polluting }
	  make() {new Polluting}
	}

	%op TomColors red() {
	  is_fsym(t) { $t.all in Red }
	  make() {new Red}
	}

	%op TomColors green() {
	  is_fsym(t) { $t.all in Green }
	  make() {new Green}
	}

 

  procedure searchCars(subject: access Vehicle'Class) is
  begin
    %match(subject){
      !car(x,x,_) -> {
        put_line("- Not a car, or car with different colors: " & image(`subject));
      }
      car(x,!x,!ecological()) -> {
        put_line("- Car that is not ecological and that does not have the same interior and exterior colors: " & image(`subject));
      }
      car(x@!green(),x,ecological()) -> {
        put_line("- Ecological car and that has the same interior and exterior colors, but different from green: " & image(`subject));
      }
    }
  end searchCars;
 
  procedure main is
	veh1,veh2,veh3,veh4,veh5, veh6: access Vehicle'Class;
  begin
    veh1 := `bike();  
    veh2 := `car(red(),green(),ecological());
    veh3 := `car(red(),red(),ecological());
    veh4 := `car(green(),green(),ecological());
    veh5 := `car(green(),red(),polluting());
    veh6 := `car(red(),red(),ecological());
 
    searchCars(veh1);
    searchCars(veh2);
    searchCars(veh3);
    searchCars(veh4);
    searchCars(veh5);
    -- put_line(Boolean'Image(veh6.all = veh3.all));
  end main;
end AntiMatching;
