package examples.factory.generation;

import java.io.IOException;

import examples.factory.Car;
import examples.factory.Color;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.Student;

public class GeneratorFactory extends AbstractGeneratorFactory{
	
	public GeneratorFactory(){
		super();
	}


	@Override
	protected StringBuilder core(FieldConstructor c,String packagePath) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException{
		GeneratorFactory gf=new GeneratorFactory();
		gf.generate(Car.class,"examples.factory.tests");
		gf.generate(Garage.class,"examples.factory.tests");
		gf.generate(Garage2.class,"examples.factory.tests");
		gf.generate(Student.class,"examples.factory.tests");
		
		
	}

}
