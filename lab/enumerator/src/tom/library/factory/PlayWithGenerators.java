package tom.library.factory;

import examples.factory.Car;

public class PlayWithGenerators {
	public static void main(String[] args) {
		Generator generator = new Generator(Car.class);
		
		try {
			generator.generateClass("examples.factory.tests");
		} catch (ClassNotFoundException | GeneratorFactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
