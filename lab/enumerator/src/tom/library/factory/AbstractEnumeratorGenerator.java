package tom.library.factory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.Map;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.theory.ForSome;

public abstract class AbstractEnumeratorGenerator {

	public static final String ENDL = System.getProperty("line.separator");
	public static final String TAB = "\t";
	
	protected boolean canBeNull;

	/**
	 * Constuctor
	 */
	public AbstractEnumeratorGenerator(Class<?> class2enumerate) {
		super();
		// TODO: handle Exceptions
		for(Constructor<?> constructor: class2enumerate.getConstructors()){
			if(constructor.getAnnotation(EnumerateGenerator.class)!=null){
				this.canBeNull = constructor.getAnnotation(EnumerateGenerator.class).canBeNull();
			}
		}
	}

	/***
	 * Contain all the lines that we need to write in a file Return the text of
	 * the factory of the class
	 * 
	 * @param c
	 * @param existingFactories
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException 
	 */
	public StringBuilder generateEnumerator(Class<?> class2enumerate, String packagePath, Map<Class<?>, StringBuilder> existingFactories)
			throws IOException, ClassNotFoundException, GeneratorFactoryException {
		// if it has been already generated just return it
		if (existingFactories.containsKey(class2enumerate)) {
			return existingFactories.get(class2enumerate);
		}
		
		// if other classes need it they shouldn't try to build it
		existingFactories.put(class2enumerate, null);
		// build the enumerator code
		StringBuilder enumeratorCode = new StringBuilder();
		enumeratorCode.append("package " + packagePath + ";"+ENDL+ENDL);
		enumeratorCode.append(generateImports(class2enumerate));
		enumeratorCode.append(generateClass(new FieldConstructor(class2enumerate), packagePath, existingFactories));
		// add the code to the map
		existingFactories.put(class2enumerate, enumeratorCode);
		return enumeratorCode;
	}


	/***
	 * create the import line for the class that we want to generate
	 * 
	 * @param class2enumerate
	 *            : return the StringBuilder that contains the line to generate
	 * @return
	 */
	private StringBuilder generateImports(Class<?> class2enumerate) {
		StringBuilder generatorHeader = new StringBuilder();
		
		// all imports for enumerations (some can be unused)
		generatorHeader.append("import " + Combinators.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + Enumeration.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + F.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + BigInteger.class.getCanonicalName()+";"+ENDL);
		// import the class to be enumerated
		generatorHeader.append("import " + class2enumerate.getCanonicalName()+";"+ENDL);
		// import the classes used in the profile of the constructor to be used for the enumeration
		for (Class<?> cla : MyIntrospection.getClassFromConstructorEnumerator(class2enumerate)) {
			// TODO: isPrimitive = int, long, byte, float, double, boolean, short, char   OU   java.lang.*
			// Actually it s just int, long, byte, float, double, boolean, short, char that are considered as Primitive
			if (!Tools.isLanguage(cla) && !Tools.isPrimitive(cla)) {
				generatorHeader.append("import " + cla.getCanonicalName()+";"+ENDL);
			}
		}
		generatorHeader.append(generateParticularImports(class2enumerate));
		return generatorHeader;
	}

	protected abstract StringBuilder generateParticularImports(Class<?> class2enumerate);
	
	
	protected StringBuilder generateClass(FieldConstructor class2enumerate, String packagePath, Map<Class<?>, StringBuilder> existingFactories)
			throws IOException, ClassNotFoundException, GeneratorFactoryException {
		StringBuilder generatorClass = new StringBuilder();
		generatorClass.append( "public class " + class2enumerate.getTypeChamp().getSimpleName() + "Factory{" + ENDL);
		generatorClass.append(generateClassBody(class2enumerate, packagePath, existingFactories));
		generatorClass.append("}"+ ENDL);
		return generatorClass;
	}

	/**
	 * allow the subclasses to create the core of the "Class"Factory
	 * 
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException 
	 */
	protected abstract StringBuilder generateClassBody(FieldConstructor class2enumerate, String packagePath, Map<Class<?>, StringBuilder> existingFactories)
			throws IOException, ClassNotFoundException, GeneratorFactoryException;
	
	
	
	
	

	/************************************** USED in other classes - to remove eventually------ ******************************************************/

	/**
	 * makes the same that append and acts of sb but appends a ENDL at the end
	 * 
	 * @param sb
	 * @param texte
	 */
	protected static void appendln(StringBuilder sb, String texte) {
		sb.append(texte + ENDL);
	}




}
