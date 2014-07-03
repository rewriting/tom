package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import examples.factory.Student;

public class Generator {

	public static void generateFactoryFiles(Class<?> targetClass,String packagePath) throws ClassNotFoundException, IOException{
		Map<Class<?>,StringBuilder> classLists = generateFactoryCode(targetClass, packagePath, new HashMap<Class<?>, StringBuilder>());
		generateFile(classLists, packagePath);
	}
	
	/**
	 * Function that to must be callto generate the factory of the class in parameter
	 * @param ctargetClass
	 * @param packagePath
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Map<Class<?>,StringBuilder> generateFactoryCode(Class<?> ctargetClass, String packagePath, Map<Class<?>,StringBuilder> existingFactories) throws ClassNotFoundException, IOException{
		if(!existingFactories.containsKey(ctargetClass)){
			// if other classes need it they shouldn't try to build it
			existingFactories.put(ctargetClass, null);
			StringBuilder sb=GeneratorFactory.getGenerator(ctargetClass).appendGenerateFactory(ctargetClass, packagePath, existingFactories);
			existingFactories.put(ctargetClass, sb);
		}
		return existingFactories;
	}
	
	/***
	 * Write the Factories in text File
	 * @param c
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private static void generateFile(Map<Class<?>,StringBuilder> classLists, String packagePath) throws IOException, ClassNotFoundException{
		for(Class<?> c:classLists.keySet()){
			// TODO: change hard-coded with classList
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+c.getSimpleName()+"Factory.java"))));
			//			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+AbstractGeneratorFactory.className(c)+"Factory.java"))));
			pw.print(classLists.get(c));
			pw.close();
		}
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
//		Generator.generateFactoryFiles(Car.class,"examples.factory.tests");
//		Generator.generateFactoryFiles(Garage.class,"examples.factory.tests");
//		Generator.generateFactoryFiles(Garage2.class,"examples.factory.tests");
		Generator.generateFactoryFiles(Student.class,"examples.factory.tests");
	}
}
