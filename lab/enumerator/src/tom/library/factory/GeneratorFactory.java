package tom.library.factory;

import java.util.HashMap;
import java.util.Map;

public class GeneratorFactory {

	private static Map<Class<?>, Generator> factories = new HashMap<Class<?>, Generator>();

	public static Generator getGenerator(Class<?> targetClass) {
		Generator classGenerator;
		if (factories.containsKey(targetClass)) {
			classGenerator=factories.get(targetClass);
		}else{
			classGenerator=new Generator(targetClass);
			factories.put(targetClass, classGenerator);
		}
		return classGenerator;
	}
}
