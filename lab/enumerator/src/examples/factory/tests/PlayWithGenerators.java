package examples.factory.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import tom.library.factory.GeneratorFactory;
import tom.library.factory.GeneratorFactoryException;
import tom.library.factory.Tools;
import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Student;
import examples.factory.Tree;


public class PlayWithGenerators {

	public static void generateFactoryFiles(Class<?> targetClass, String packagePath) throws ClassNotFoundException, GeneratorFactoryException, IOException{
		if(!Collection.class.isAssignableFrom(targetClass)){
			Map<Class<?>,StringBuilder> existingdCode = new HashMap<Class<?>, StringBuilder>();

			Map<Class<?>,StringBuilder>  generatedCode=GeneratorFactory.getGenerator(targetClass).generateEnumerator(packagePath,existingdCode);

			// TODO: handle existing files (e.g. if we generate Car + Student, the code in Student is duplicated)
			// create files with the generated code
			for(Class<?> c:generatedCode.keySet()){
				// TODO: change hard-coded with classList
				System.out.println("--- "+c);
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File(Tools.getUrl(packagePath)+c.getSimpleName()+"Factory.java"))));
				pw.print(existingdCode.get(c));
				pw.close();
			}
		}
	}

	public static void main(String args[]) throws IOException, ClassNotFoundException, GeneratorFactoryException{
		String packagePath="examples.factory.tests";
//		PlayWithGenerators.generateFactoryFiles(Car.class,packagePath);
//		PlayWithGenerators.generateFactoryFiles(Garage.class,packagePath);
//		PlayWithGenerators.generateFactoryFiles(Garage2.class,packagePath);
		PlayWithGenerators.generateFactoryFiles(Car.class,packagePath);

		////MainFactoryGenerator.generateFactoryFiles(T3.class,"examples.factory.tests");
		//		
		//PlayWithGenerators.generateFactoryFiles(Tree.class,packagePath);		

		// TODO: check it
//		MainFactoryGenerator.generateFactoryFiles(TrickyTree.class,packagePath);
	}

}
