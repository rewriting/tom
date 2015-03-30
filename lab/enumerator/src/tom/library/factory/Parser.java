package tom.library.factory;

import java.lang.reflect.Constructor;
import tom.library.factory.EnumerateGenerator;

/**
 * the class represents a parser which is given a class to parse in order to generate the factory for it
 * it loads the parsed information into the ParsedClass
 * both of the ParsedClass and the classToParse should be given as input to its static parse method
 * @author Ahmad
 *
 */
public class Parser {
	
	/**
	 * parses classToParse into parsedClass
	 * @param classToParse the class to be parsed
	 * @param parsedClass  object holding the parsed information (constructors, annotations...)
	 */
	public static <T> void parse(Class<T> classToParse, ParsedClass parsedClass) {
		
		// set class properties
		parsedClass.setSimpleName(classToParse.getSimpleName());
		parsedClass.setCanonicalName(classToParse.getCanonicalName());
		
		// load all constructors having EnumerateGenerator annotations
		for (Constructor cons:classToParse.getDeclaredConstructors()) {
			if (cons.isAnnotationPresent(EnumerateGenerator.class)) {
				if (cons.getParameterTypes().length == 0) {
					// no args cons
					parsedClass.setNoArgsConstructor(cons);
				} else {
					// cons with parameters
					parsedClass.addConstructor(cons);
				}
				
			}
		}
	}
}
