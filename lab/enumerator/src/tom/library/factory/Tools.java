package tom.library.factory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.regex.Pattern;

public class Tools {

	public static final String INTEGER = Integer.class.getSimpleName();
	public static final String LONG = Long.class.getSimpleName();
	public static final String BYTE = Byte.class.getSimpleName();
	public static final String FLOAT = Float.class.getSimpleName();
	public static final String DOUBLE = Double.class.getSimpleName();
	public static final String BOOLEAN = Boolean.class.getSimpleName();
	public static final String STRING = String.class.getSimpleName();

	public static final String PRIMITIVESTRING[] = { "int", "long", "byte",
			"float", "double", "boolean", "String" };
	public static final String PRIMITIVESTRINGSIMPLE[] = { INTEGER, LONG, BYTE,
			FLOAT, DOUBLE, BOOLEAN, STRING };

	public static boolean isBasicType(Type type) {
		if (type.equals(int.class) || type.equals(long.class)
				|| type.equals(byte.class) || type.equals(float.class)
				|| type.equals(double.class) || type.equals(boolean.class)
				|| type.equals(short.class) || type.equals(char.class)) {
			return true;
		}
		return false;
	}

	public static String name4PrimitiveType(Type type) {
		String name = "";
		if (type.equals(int.class)) {
			name = "Integer";
		} else if (type.equals(long.class)) {
			name = "Long";
		} else if (type.equals(byte.class)) {
			name = "Byte";
		} else if (type.equals(float.class)) {
			name = "Float";
		} else if (type.equals(double.class)) {
			name = "Double";
		} else if (type.equals(boolean.class)) {
			name = "Boolean";
		} else if (type.equals(short.class)) {
			name = "Short";
		} else if (type.equals(char.class)) {
			name = "Character";
		}
		return name;
	}

	/**
	 * allow to obtain the class names for example the primitve "int" will be
	 * return like the class "int"
	 * 
	 * @param c
	 * @return
	 */
	public static String className(Class<?> c) {
		String refactoring = refactoringOfPrimitive(c);
		if (refactoring != null) {
			return refactoring;
		}
		return c.getCanonicalName()/*
									 * +AbstractEnumeratorGenerator.
									 * getStringTypeParametersOfTheClass(c)
									 */;// (modifier le abstract car mal place
	}

	public static String refactoringOfPrimitive(Class<?> c) {
		for (int i = 0; i < PRIMITIVESTRING.length; i++) {
			String s = PRIMITIVESTRING[i];
			if (c != null
					&& c.getSimpleName().toUpperCase().equals(s.toUpperCase())) {
				return PRIMITIVESTRINGSIMPLE[i];
			}
		}
		return null;
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

	public static boolean isLanguage(Class c) {
		return c.getCanonicalName().contains("java.lang.");
	}

	/****
	 * transform the packagePath to the final url where we want to create the
	 * factory
	 * 
	 * @param packagePath
	 * @return
	 */
	public static String getUrl(String packagePath) {
		StringBuilder sb = new StringBuilder("src/");
		String li[] = packagePath.split(Pattern.quote("."));
		int i = 0;
		for (String s : li) {
			sb.append(s);
			sb.append("/");
			i++;
		}

		return sb + "";
	}

	protected static String getStringTypeParametersOfTheClass(Class<?> c) {
		StringBuilder typeParameters = new StringBuilder();
		for (TypeVariable<?> tv : c.getTypeParameters()) {
			typeParameters = typeParameters.append(tv.getName()).append(",");
		}
		// if there are some types
		if (typeParameters.length() > 0) {
			typeParameters.insert(0,'<');
			typeParameters.replace(typeParameters.length()-1,typeParameters.length(),">");
		}
		return typeParameters + "";
	}
}