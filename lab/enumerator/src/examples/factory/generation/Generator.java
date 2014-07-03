package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.Student;

public class Generator {

	public static void run(Class<?> c,String packagePath) throws ClassNotFoundException, IOException{
		Map<Class<?>,StringBuilder> classLists = generate(c, packagePath, new HashMap<Class<?>, StringBuilder>());
		generateFile(classLists, packagePath);
	}
	
	/**
	 * Function that to must be callto generate the factory of the class in parameter
	 * @param c
	 * @param packagePath
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Map<Class<?>,StringBuilder> generate(Class<?> c,String packagePath, Map<Class<?>,StringBuilder> classLists) throws ClassNotFoundException, IOException{
		if(!classLists.containsKey(c)){
			// if other classes need it they shouldn't try to build it
			classLists.put(c, null);
			StringBuilder sb=GeneratorFactory.getInstance().getFactory(c).appendGenerateFactory(c, packagePath, classLists);
			classLists.put(c, sb);
		}
		return classLists;
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
//		Generator.run(Car.class,"examples.factory.tests");
//		Generator.run(Garage.class,"examples.factory.tests");
//		Generator.run(Garage2.class,"examples.factory.tests");
		Generator.run(Student.class,"examples.factory.tests");
	//	main.run(Tree.class, "examples.factory.tests");
	}
}
