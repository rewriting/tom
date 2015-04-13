package tom.library.factory;

import java.lang.reflect.Constructor;

/**
 * the class represents a parser which is given a class to parse in order to
 * generate the factory for it it loads the parsed information into the
 * ParsedClass both of the ParsedClass and the classToParse should be given as
 * input to its static parse method
 * 
 * @author Ahmad
 * 
 */
public class Parser {

    /**
     * parses classToParse into parsedClass
     * 
     * @param classToParse
     *            the class to be parsed
     * @return parsedClass object holding the parsed information (constructors,
     *         annotations...)
     */
    public static <T> ParsedClass parse(Class<T> classToParse) {
        ParsedClass parsedClass = new ParsedClass(classToParse);
        // load all constructors having EnumerateGenerator annotations
        for (Constructor cons : classToParse.getDeclaredConstructors()) {
            if (cons.isAnnotationPresent(EnumerateGenerator.class)) {
                if (cons.getParameterTypes().length == 0) {
                    // no args cons
                    parsedClass.addNoArgsConstructor(cons);
                } else {
                    // cons with parameters
                    parsedClass.addConstructor(cons);
                }

            }
        }
        return parsedClass;
    }
}
