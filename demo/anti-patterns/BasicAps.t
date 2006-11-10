import basicaps.cars.types.*;

class BasicAps {  

	%gom {
		module Cars		
		abstract syntax

		Vehicle = car(color:CarColors,type:CarType)
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

        veh = `car(white(),ecological());
		searchCars(veh);		
		
		veh = `car(white(),poluting());
		searchCars(veh);
		
		veh = `car(red(),ecological());
		searchCars(veh);
		
		veh = `car(green(),poluting());
		searchCars(veh);
		
	}
	
	private static void searchCars(Vehicle subject) {
        //System.out.println("searchCars: " + subject);
		%match(subject){
			car(!white(),ecological()) ->{
				System.out.println("Car that is not white but ecological: " + `subject);				
			}
			car(white(),!ecological()) ->{
				System.out.println("Car that is white but not ecological: " + `subject);
			}
			!car(white(),!ecological()) ->{
				System.out.println("Not a white car or a white ecological one: " + `subject);
			}
		}
		System.out.println();
	}
}
