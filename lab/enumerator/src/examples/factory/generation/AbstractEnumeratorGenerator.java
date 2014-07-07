package examples.factory.generation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public abstract class AbstractEnumeratorGenerator {

	public static final String ENDL = System.getProperty("line.separator");
	public static final String TAB = "\t";
	

	/**
	 * Constuctor
	 */
	public AbstractEnumeratorGenerator() {
		super();
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
	public StringBuilder generateEnumerator(Class<?> targetClass, String packagePath, Map<Class<?>, StringBuilder> existingFactories)
			throws IOException, ClassNotFoundException, GeneratorFactoryException {
		// if it has been already generated just return it
		if (existingFactories.containsKey(targetClass)) {
			return existingFactories.get(targetClass);
		}
		
		// if other classes need it they shouldn't try to build it
		existingFactories.put(targetClass, null);
		// build the enumerator code
		StringBuilder enumeratorCode = new StringBuilder();
		enumeratorCode.append("package " + packagePath + ";"+ENDL+ENDL);
		enumeratorCode.append(generateImports(targetClass));
		enumeratorCode.append(appendClass(new FieldConstructor(targetClass), packagePath, existingFactories));
		// add the code to the map
		existingFactories.put(targetClass, enumeratorCode);
		return enumeratorCode;
	}


	/***
	 * create the import line for the class that we want to generate
	 * 
	 * @param c
	 *            : return the StringBuilder that contains the line to generate
	 * @return
	 */
	protected StringBuilder generateImports(Class<?> c) {
		StringBuilder generatorHeader = new StringBuilder();
		
		// all imports for enumerations (some can be unused)
		generatorHeader.append("import " + Combinators.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + Enumeration.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + F.class.getCanonicalName()+";"+ENDL);
		generatorHeader.append("import " + BigInteger.class.getCanonicalName()+";"+ENDL);
		// import the class to be enumerated
		generatorHeader.append("import " + c.getCanonicalName()+";"+ENDL);
		// import the classes used in the profile of the constructor to be used for the enumeration
		// TODO: how to import the other classes potentially used in the constructor? another annotation?
		// I don't know if it s possible to import the classes that are used in the core of the constructor.
		// I have never seen this possibility in the reflect package.
		// Actually I thought that if I have the parameter classes that will not need the rest.
		// But if we need if that can be a problem
		for (Class<?> cla : MyIntrospection.getClassFromConstructorEnumerator(c)) {
			// TODO: isPrimitive = int, long, byte, float, double, boolean, short, char   OU   java.lang.*
			// Actually it s just int, long, byte, float, double, boolean, short, char that are considered as Primitive
			if (!Tools.isLanguage(cla)) {
				generatorHeader.append("import " + cla.getCanonicalName()+";"+ENDL);
			}
		}
//		for (Class<?> cla : generateListImport(c)) {
//			generatorHeader.append("import " + cla.getCanonicalName()+";"+ENDL);
//		}
		return generatorHeader;
	}

	/***
	 * generate the list of all import class that the Factory needs
	 * 
	 * @param c
	 * @return
	 */
//	protected List<Class<?>> generateListImport(Class<?> c) {
//		List<Class<?>> usedClasses = MyIntrospection.getClassFromConstructorEnumerator(c);
////		List<Class<?>> existingImport=new ArrayList<Class<?>>();
//		for (Class<?> cla : usedClasses) {
//			// TODO: isPrimitive = int, long, byte, float, double, boolean, short, char   OU   java.lang.*
//			if (Tools.isPrimitive(cla)) {
//				usedClasses.remove(cla);
//			}
//			
////			if (!Tools.isPrimitive(cla)) {
////				if (!existingImport.contains(cla)) {
////					existingImport.add(cla);
////				}
////			}
//		}
//		return usedClasses;
////		return existingImport;
//
//	}

	

	/************************************** IMPORTS ************************************/
	/**
	 * return the line to write to import this class
	 * 
	 * @param c
	 * @return
	 */
	public String makeImport(Class c) {
		return "import " + c.getCanonicalName() + ";";
	}

	
	/***************************** CLASS *********************************************************/

	/**
	 * create the structur of the class in parameter and call core to fill it.
	 * 
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException 
	 */
	protected StringBuilder appendClass(FieldConstructor fc,
			String packagePath, Map<Class<?>, StringBuilder> classLists)
			throws IOException, ClassNotFoundException, GeneratorFactoryException {
		StringBuilder generatorClass = new StringBuilder();
		appendln(generatorClass, "public class " + fc.getTypeChamp().getSimpleName()
				+ "Factory{");
		generatorClass.append(core(fc, packagePath, classLists));
		appendln(generatorClass, "}");
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
	protected abstract StringBuilder core(FieldConstructor fc,
			String packagePath, Map<Class<?>, StringBuilder> classLists)
			throws IOException, ClassNotFoundException, GeneratorFactoryException;
	
	
	
	
	
	protected static String getStringTypeParametersOfTheClass(Class<?> c){
		StringBuilder typeParameters=new StringBuilder();
		if(c.getTypeParameters().length>0){
			typeParameters.append("<");
			for(int i=0;i<c.getTypeParameters().length;i++){
				typeParameters.append(c.getTypeParameters()[i]);
				if(i<c.getTypeParameters().length-1){
					typeParameters.append(",");
				}
			}
			typeParameters.append(">");
		}
		;
		return typeParameters+"";
//		return "";
	}

	/************************************** ------ ******************************************************/

	/**
	 * makes the same that append and acts of sb but appends a ENDL at the end
	 * 
	 * @param sb
	 * @param texte
	 */
	protected static void appendln(StringBuilder sb, String texte) {
		sb.append(texte + ENDL);
	}

	/**
	 * return "number" tabulations
	 * 
	 * @param number
	 *            : number of tabulations that we want
	 * @return
	 */
	protected static String createTabulation(int number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number; i++) {
			sb.append(TAB);
		}
		return sb + "";
	}



}
