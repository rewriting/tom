package examples.factory.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import examples.factory.Car;
import examples.factory.Student;
import examples.factory.generation.GeneratorFactory;

public class PlayWithGenerators {

	public static void generateFactoryFiles(Class<?> targetClass, String packagePath) throws ClassNotFoundException, IOException{
		Map<Class<?>,StringBuilder> generatedCode = new HashMap<Class<?>, StringBuilder>();
		GeneratorFactory.getGenerator(targetClass).generateEnumerator(targetClass, packagePath, generatedCode);
		
		// create files with the generated code
		for(Class<?> c:generatedCode.keySet()){
			// TODO: change hard-coded with classList
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+c.getSimpleName()+"Factory.java"))));
			pw.print(generatedCode.get(c));
			pw.close();
		}
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		PlayWithGenerators.generateFactoryFiles(Car.class,"examples.factory.tests");
//		PlayWithGenerators.generateFactoryFiles(Garage.class,"examples.factory.tests");
//		PlayWithGenerators.generateFactoryFiles(Garage2.class,"examples.factory.tests");
		PlayWithGenerators.generateFactoryFiles(Student.class,"examples.factory.tests");
	}
	
}
