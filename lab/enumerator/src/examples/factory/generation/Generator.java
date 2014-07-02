package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import examples.adt.tree.tree.types.Tree;
import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.Student;

public class Generator {

	private static Generator instance;
	private Map<Class,StringBuilder> classLists;

	private Generator(){
		this.classLists=new HashMap<Class, StringBuilder>();
	}

	public void run(Class c,String packagePath) throws ClassNotFoundException, IOException{
		this.classLists=new HashMap<Class, StringBuilder>();
		generate(c, packagePath);
		generateFile(packagePath);
	}
	
	/**
	 * Function that to must be callto generate the factory of the class in parameter
	 * @param c
	 * @param packagePath
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void generate(Class c,String packagePath) throws ClassNotFoundException, IOException{
		if(!classLists.containsKey(c)){
			StringBuilder sb=GeneratorFactory.getInstance().getFactory(c).appendGenerateFactory(c, packagePath);
			classLists.put(c, sb);
		}
		
	}
	/**
	 * Return the static instance of Generator
	 * @return
	 */
	public static Generator getInstance(){
		if(instance==null){
			instance=new Generator();
		}
		return instance;
	}

	/***
	 * Write the Factories in text File
	 * @param c
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private void generateFile(String packagePath) throws IOException, ClassNotFoundException{
		for(Class c:classLists.keySet()){
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+AbstractGeneratorFactory.className(c)+"Factory.java"))));
			pw.print(classLists.get(c));
			pw.close();
		}
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		Generator main=Generator.getInstance();
		main.run(Car.class,"examples.factory.tests");
		main.run(Garage.class,"examples.factory.tests");
		main.run(Garage2.class,"examples.factory.tests");
		main.run(Student.class,"examples.factory.tests");
	//	main.run(Tree.class, "examples.factory.tests");
	}
}
