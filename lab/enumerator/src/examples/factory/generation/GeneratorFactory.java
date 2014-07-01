package examples.factory.generation;

import java.io.IOException;

import examples.factory.Garage;
import examples.factory.Student;

public class GeneratorFactory extends AbstractGeneratorFactory{
	
	public GeneratorFactory(){
		super();
	}


	@Override
	protected StringBuilder core(Class<?> c) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String args[]) throws IOException{
		GeneratorFactory gf=new GeneratorFactory();
		gf.generate(Student.class);
		
	}

}
