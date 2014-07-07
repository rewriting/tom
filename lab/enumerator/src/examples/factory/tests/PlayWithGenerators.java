package examples.factory.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.ParametrizedClass;
import examples.factory.Student;
import examples.factory.Tree;
import examples.factory.generation.GeneratorFactory;
import examples.factory.generation.GeneratorFactoryException;
import examples.factory.generation.Tools;

public class PlayWithGenerators {

	public static void generateFactoryFiles(Class<?> targetClass, String packagePath) throws ClassNotFoundException, IOException, GeneratorFactoryException{
		Map<Class<?>,StringBuilder> generatedCode = new HashMap<Class<?>, StringBuilder>();
		GeneratorFactory.getGenerator(targetClass).generateEnumerator(targetClass, packagePath, generatedCode);
		
		// create files with the generated code
		for(Class<?> c:generatedCode.keySet()){
			// TODO: change hard-coded with classList
		
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File(Tools.getUrl(packagePath)+c.getSimpleName()+"Factory.java"))));
			pw.print(generatedCode.get(c));
			pw.close();
		}
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, GeneratorFactoryException{
//		PlayWithGenerators.generateFactoryFiles(Car.class,"examples.factory.tests");
//		PlayWithGenerators.generateFactoryFiles(Garage.class,"examples.factory.tests");
//		PlayWithGenerators.generateFactoryFiles(Garage2.class,"examples.factory.tests");
		PlayWithGenerators.generateFactoryFiles(Student.class,"examples.factory.tests");
//		
//		PlayWithGenerators.generateFactoryFiles(Tree.class,"examples.factory.tests");
	}
	
}
