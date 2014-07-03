package examples.factory.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorFactory {

	private enum FactoryType {
		ListFactory, StaticFactory, ClassFactory;
	}

	private static Map<FactoryType, AbstractEnumeratorGenerator> factories = new HashMap<FactoryType, AbstractEnumeratorGenerator>();
	static{
		factories.put(FactoryType.ListFactory, new ListFactory());
		factories.put(FactoryType.ClassFactory, new ClassFactory());
		factories.put(FactoryType.StaticFactory, new StaticFactory());
	}

	public static AbstractEnumeratorGenerator getGenerator(Class<?> c) {
		// should handle potential errors here:
		// -- no annotation?
		// -- no constructor with an annotation
		// ...
		FactoryType factoryType = null;
		if (List.class.isAssignableFrom(c)) {
			factoryType = FactoryType.ListFactory;
		} else if (c.getAnnotation(EnumerateStatic.class) != null) {
			factoryType = FactoryType.StaticFactory;
		} else {
			factoryType = FactoryType.ClassFactory;
		}
		System.out.println("CLASS " + c + " ENUM " + factoryType);
		
		if (!factories.containsKey(factoryType)) {
				throw new UnsupportedOperationException("Must not be here");
		}
		return factories.get(factoryType);
	}

}
