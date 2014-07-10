package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.Student;
import examples.factory.Tree;

public class PlayWithGenerators {

	public static void generateFactoryFiles(Class<?> targetClass, String packagePath) throws ClassNotFoundException, GeneratorFactoryException, IOException{
		if(!Collection.class.isAssignableFrom(targetClass)){
			Map<Class<?>,StringBuilder> generatedCode = new HashMap<Class<?>, StringBuilder>();

			GeneratorFactory.getGenerator(targetClass).generateEnumerator(packagePath,generatedCode);

			System.out.println("REDDDDFDD");
			// create files with the generated code
			for(Class<?> c:generatedCode.keySet()){
				// TODO: change hard-coded with classList
				System.out.println("--- "+c);
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File(Tools.getUrl(packagePath)+c.getSimpleName()+"Factory.java"))));
				pw.print(generatedCode.get(c));
				pw.close();
			}
		}
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException, GeneratorFactoryException{
		String packagePath="examples.factory.tests";
		PlayWithGenerators.generateFactoryFiles(Car.class,packagePath);
		PlayWithGenerators.generateFactoryFiles(Garage.class,packagePath);
		PlayWithGenerators.generateFactoryFiles(Garage2.class,packagePath);
		PlayWithGenerators.generateFactoryFiles(Student.class,packagePath);

		////PlayWithGenerators.generateFactoryFiles(T3.class,"examples.factory.tests");
		//		
		PlayWithGenerators.generateFactoryFiles(Tree.class,packagePath);
		//		PlayWithGenerators.generateFactoryFiles(ParametrizedClass2.class,"examples.factory.tests");
	}

}
