package examples.factory.generation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

public abstract class AbstractEnumeratorGenerator {

	public static String INTEGER = Integer.class.getSimpleName();
	public static String LONG = Long.class.getSimpleName();
	public static String BYTE = Byte.class.getSimpleName();
	public static String FLOAT = Float.class.getSimpleName();
	public static String DOUBLE = Double.class.getSimpleName();
	public static String BOOLEAN = Boolean.class.getSimpleName();
	public static String STRING = String.class.getSimpleName();

	public static String ENDL = System.getProperty("line.separator");
	public static String TAB = "\t";

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
	 */
	public StringBuilder generateEnumerator(Class<?> targetClass, String packagePath, Map<Class<?>, StringBuilder> existingFactories)
			throws IOException, ClassNotFoundException {
		// if it has been already generated just return it
		if (existingFactories.containsKey(targetClass)) {
			return existingFactories.get(targetClass);
		}
		
		// if other classes need it they shouldn't try to build it
		existingFactories.put(targetClass, null);
		// build the enumerator code
		StringBuilder enumeratorCode = new StringBuilder();
		enumeratorCode.append(appendImports(targetClass, packagePath));
		enumeratorCode.append(appendClass(new FieldConstructor(null, targetClass, null,null), packagePath, existingFactories));
		// add the code to the map
		existingFactories.put(targetClass, enumeratorCode);
		return enumeratorCode;
	}

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

	/***
	 * generate the list of all import class that the Factory needs
	 * 
	 * @param c
	 * @return
	 */
	protected List<Class> generateListImport(Class<?> c) {
		List<FieldConstructor> list = MyIntrospection
				.getAllParameterFieldFromConstructorEnumerator(c);
		List<Class> sortie = new ArrayList<Class>();
		for (FieldConstructor fc : list) {
			if (!isPrimitive(fc.getTypeChamp())) {
				Class cla = fc.getTypeChamp();
				if (!sortie.contains(cla)) {
					sortie.add(cla);
				}
				sortie.addAll(generateListImport(fc.getTypeChamp()));
			}
		}
		return sortie;
	}

	/***
	 * create the import line for the class that we want to generate
	 * 
	 * @param c
	 *            : return the StringBuilder that contains the line to generate
	 * @return
	 */
	protected StringBuilder appendImports(Class<?> c, String packagePath) {
		StringBuilder sb = new StringBuilder();
		appendln(sb, "package " + packagePath + ";");

		appendln(sb, makeImport(c));
		for (Class cla : generateListImport(c)) {
			appendln(sb, makeImport(cla));
		}

		// Default Import
		appendln(sb, makeImport(Combinators.class));
		appendln(sb, makeImport(Enumeration.class));
		appendln(sb, makeImport(F.class));
		appendln(sb, makeImport(BigInteger.class));
		return sb;
	}

	/***************************** CLASS *********************************************************/

	/**
	 * create the structur of the class in parameter and call core to fill it.
	 * 
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected StringBuilder appendClass(FieldConstructor fc,
			String packagePath, Map<Class<?>, StringBuilder> classLists)
			throws IOException, ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		appendln(sb, "public class " + fc.getTypeChamp().getSimpleName()
				+ "Factory{");
		sb.append(core(fc, packagePath, classLists));
		appendln(sb, "}");
		return sb;
	}

	/**
	 * allow the subclasses to create the core of the "Class"Factory
	 * 
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected abstract StringBuilder core(FieldConstructor fc,
			String packagePath, Map<Class<?>, StringBuilder> classLists)
			throws IOException, ClassNotFoundException;

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

	public static String classNameWithParameter(FieldConstructor fc) {
		Type l = fc.getParameter();

		StringBuilder sb = new StringBuilder();

		if (l != null && (l + "").contains("<")) {
			sb.append(l);
		} else {
			sb.append(className(fc.getTypeChamp()));
		}

		System.out.println("SB  ��  " + sb);

		return sb + "";
	}

	/**
	 * allow to obtain the class names for example the primitve "int" will be
	 * return like the class "int"
	 * 
	 * @param c
	 * @return
	 */
	public static String className(Class<?> c) {
		String str[] = { "int", "long", "byte", "float", "double", "boolean",
				"String" };
		String str2[] = { INTEGER, LONG, BYTE, FLOAT, DOUBLE, BOOLEAN, STRING };
		for (int i = 0; i < str.length; i++) {
			String s = str[i];
			if (c != null
					&& c.getSimpleName().toUpperCase().equals(s.toUpperCase())) {
				return str2[i];
			}
		}
		return c.getSimpleName();
	}

	/**
	 * can be call to know if the class belong to the primitive set return True
	 * if that is right
	 * 
	 * @param canonicalName
	 * @return
	 */
	public static boolean isPrimitive(Class c) {
		String str[] = { "int", "long", "byte", "float", "double", "boolean",
				"String" };
		for (int i = 0; i < str.length; i++) {
			String s = str[i];
			if (c != null
					&& c.getSimpleName().toUpperCase().equals(s.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

}
