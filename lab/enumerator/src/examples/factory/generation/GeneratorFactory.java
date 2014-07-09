package examples.factory.generation;

import java.util.HashMap;
import java.util.Map;

public class GeneratorFactory {

//	private enum FactoryType {
//		ListFactory, StaticFactory, ClassFactory;
//	}

	private static Map<Class<?>, AbstractEnumeratorGenerator> factories = new HashMap<Class<?>, AbstractEnumeratorGenerator>();

	public static AbstractEnumeratorGenerator getGenerator(Class<?> targetClass) {
//			throws GeneratorFactoryException {
//		// should handle potential errors here:
//		// -- no annotation?
//		// -- no constructor with an annotation
//		// ...
//		FactoryType factoryType = null;
//		if (List.class.isAssignableFrom(targetClass)) {
//			factoryType = FactoryType.ListFactory;
//		} else if (targetClass.getAnnotation(EnumerateStatic.class) != null) {
//			factoryType = FactoryType.StaticFactory;
//		} else if (constructorContainsAnnotation(targetClass,
//				EnumerateGenerator.class)) {
//			factoryType = FactoryType.ClassFactory;
//		} else {
//			throw new GeneratorFactoryException("Class target : "
//					+ targetClass);
//		}
//		System.out.println("CLASS " + targetClass + " ENUM " + factoryType);
		
		AbstractEnumeratorGenerator classGenerator;
		if (factories.containsKey(targetClass)) {
			classGenerator=factories.get(targetClass);
		}else{
			classGenerator=new ClassFactory(targetClass);
			factories.put(targetClass, classGenerator);
		}
		return classGenerator;
	}

//	public static boolean constructorContainsAnnotation(Class target,
//			Class enumerator) {
//
//		return MyIntrospection.extractConstructorWithEnumerateGenerator(target,
//				enumerator) != null;// TODO : to do
//	}

}
