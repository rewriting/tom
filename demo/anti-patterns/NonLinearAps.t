import nonlinearaps.specialcars.types.*;

class NonLinearAps {  

	%gom {
		module SpecialCars		
		abstract syntax

		Vehicle = car(interiorColor:CarColors,exteriorColor:CarColors,type:CarType)
				| bike()
				| truck()

		CarType = ecological()
				| poluting()

		CarColors = white()
				  | blue()
				  | red()
				  | green() 
	} 

	public final static void main(String[] args) {
		Vehicle veh;

		veh = `bike();  
		searchCars(veh);		 		

		veh = `car(white(),green(),ecological());
		searchCars(veh);		

		veh = `car(red(),red(),ecological());
		searchCars(veh);
		
		veh = `car(green(),green(),ecological());
		searchCars(veh);
		
		veh = `car(green(),green(),poluting());
		searchCars(veh);
		 
	}
	
	private static void searchCars(Vehicle subject){
		%match(subject){
			car(x,x,ecological()) ->{
				System.out.println("Ecological car that has the same interior and exterior colors: " + `subject);
			}
			!car(x,x,_) ->{
				System.out.println("Not a car, or car with different colors: " + `subject);
			}
			car(x,!x,!ecological()) ->{
				System.out.println("Car that is not ecological and that does not have the same interior and exterior colors: " + `subject);
			}
			car(x@!green(),x,ecological()) ->{
				System.out.println("Ecological car and that has the same interior and exterior colors, but different from green: " + `subject);
			}
		}
		System.out.println();
	}
}
