package examples.factory.generation;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

public class Tools {
	
	public static final String INTEGER = Integer.class.getSimpleName();
	public static final String LONG = Long.class.getSimpleName();
	public static final String BYTE = Byte.class.getSimpleName();
	public static final String FLOAT = Float.class.getSimpleName();
	public static final String DOUBLE = Double.class.getSimpleName();
	public static final String BOOLEAN = Boolean.class.getSimpleName();
	public static final String STRING = String.class.getSimpleName();

	public static final String PRIMITIVESTRING[] = { "int", "long", "byte", "float", "double", "boolean",
	"String" };
	public static final String PRIMITIVESTRINGSIMPLE[] = { INTEGER, LONG, BYTE, FLOAT, DOUBLE, BOOLEAN, STRING };
	
	/**
	 * allow to obtain the class names for example the primitve "int" will be
	 * return like the class "int"
	 * 
	 * @param c
	 * @return
	 */
	public static String className(Class<?> c) {
		for (int i = 0; i < PRIMITIVESTRING.length; i++) {
			String s = PRIMITIVESTRING[i];
			if (c != null
					&& c.getSimpleName().toUpperCase().equals(s.toUpperCase())) {
				return PRIMITIVESTRINGSIMPLE[i];
			}
		}
		return c.getCanonicalName()/*+AbstractEnumeratorGenerator.getStringTypeParametersOfTheClass(c)*/;//(modifier le abstract car mal place
	}

	/**
	 * can be call to know if the class belong to the primitive set return True
	 * if that is right
	 * 
	 * @param canonicalName
	 * @return
	 */
	public static boolean isPrimitive(Class c) {
		for (int i = 0; i < PRIMITIVESTRING.length; i++) {
			String s = PRIMITIVESTRING[i];
			if (c != null
					&& c.getSimpleName().toUpperCase().equals(s.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	

	
	/****
	 * transform the packagePath to the final url where we want to create the factory
	 * @param packagePath
	 * @return
	 */
	public static String getUrl(String packagePath){
		StringBuilder sb=new StringBuilder("src/");
		String li[]=packagePath.split(Pattern.quote("."));
		int i=0;
		for(String s:li){
			sb.append(s);
			sb.append("/");
			i++;
		}
		
		return sb+"";
	}
}
