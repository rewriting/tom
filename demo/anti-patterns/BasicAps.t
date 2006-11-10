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
		Vehicle veh = `car(white(),ecological());
		searchCars(veh);		
		
		veh = `car(white(),poluting());
		searchCars(veh);
		
		veh = `car(red(),ecological());
		searchCars(veh);
		
		veh = `car(green(),poluting());
		searchCars(veh);
		
		veh = `bike();
		searchCars(veh);				
	}
	
	private static void searchCars(Vehicle subject){
		%match(subject){
			car(!white(),ecological()) ->{
				System.out.println("Car that is ecological and not white:" + `subject);				
			}
			car(white(),!ecological()) ->{
				System.out.println("Car that is white and not ecological:" + `subject);
			}
			!car(white(),!ecological()) ->{
				System.out.println("Not a white car or a white ecological one:" + `subject);
			}
		}
	}
}
