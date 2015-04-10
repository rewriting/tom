package tom.library.factory.old;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import tom.library.factory.Enumerate;
import tom.library.factory.EnumerateGenerator;

public class Generator {
	public static final String ENDL = System.getProperty("line.separator");
	public static final int CLASS = Integer.MAX_VALUE;

	private final Class<?> class2enumerate;
	private String classTypeParameters;

	private Constructor<?> constructor4Enumerate;
	private final List<Class<?>> constructorParameters;
	private final List<Annotation[]> constructorParametersAnnotations;
	private final List<Type> constructorParametersTypes;

	protected boolean canBeNull;

	// is used to have a schema of stringClassName
	protected List<ParaType> paraType;
	protected boolean isParametrized;
	private StringBuilder enumeratorCode;
	// ???
	public final static String PACKAGELIBRARY = "examples.factory";

	public Generator(Class<?> class2enumerate) {
		this.class2enumerate = class2enumerate;

		// build type parameters (if any)
		this.classTypeParameters=Tools.getStringTypeParametersOfTheClass(class2enumerate);
		
		// choose the constructor used for generating the instances
		// TODO: what if the selected constructor has an argument of type interface/abstract class? 
		//    --> specify a concrete class?
		int maxLenght = -1;
		for (Constructor<?> constr : class2enumerate.getConstructors()) {
			if (constr.getAnnotation(EnumerateGenerator.class) != null) {
				this.constructor4Enumerate = constr;
				this.canBeNull = this.constructor4Enumerate.getAnnotation(
						EnumerateGenerator.class).canBeNull();
				break;
			} else {
				if (maxLenght < constr.getParameterTypes().length) {
					maxLenght = constr.getParameterTypes().length;
					this.constructor4Enumerate = constr;
				}
			}
		}

		// store the parameters of the constructor and their annotations
		this.constructorParameters = new ArrayList<Class<?>>();
		this.constructorParametersAnnotations = new ArrayList<Annotation[]>();
		int i = 0;
		for (Class<?> param : constructor4Enumerate.getParameterTypes()) {
			this.constructorParameters.add(param);
			this.constructorParametersAnnotations.add(constructor4Enumerate.getParameterAnnotations()[i++]);
		}
		this.paraType = new ArrayList<ParaType>();
		for (Type t : constructor4Enumerate.getGenericParameterTypes()) {
			this.paraType.add(ParaType.createParaType(t));
		}
		this.constructorParametersTypes = new ArrayList<Type>();
		for (Type t : constructor4Enumerate.getGenericParameterTypes()) {
			this.constructorParametersTypes.add(t);
		}

		// TODO
		// Allow methods to be enumerate generators (if they return an object of the type of the class)?
		
		// TODO
		this.enumeratorCode = new StringBuilder();
		this.isParametrized = class2enumerate.getTypeParameters().length != 0;

		System.out.println("CLASS:"+class2enumerate);	
		System.out.println("Params:"+constructorParameters);
		System.out.println("Params types:"+constructorParametersTypes);
	}

	/***
	 * Contain all the lines that we need to write in a file Return the text of
	 * the factory of the class
	 * 
	 * @param c
	 * @param existingFactories
	 * @return
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException
	 */
	public Map<Class<?>, StringBuilder> generateEnumerator(String packagePath,
			Map<Class<?>, StringBuilder> existingFactories)
			throws ClassNotFoundException, GeneratorFactoryException {
		// if it has been already generated just return it
		if (existingFactories.containsKey(this.class2enumerate)) {
			return existingFactories;
		}

		// if other classes need it they shouldn't try to build it
		existingFactories.put(class2enumerate, null);
		// build the enumerator code
		StringBuilder enumeratorCode = new StringBuilder();
		enumeratorCode.append("package " + packagePath + ";" + ENDL + ENDL);
		enumeratorCode.append(generateImports());
		enumeratorCode.append(generateClass(packagePath));

		// add the code to the map
		existingFactories.put(class2enumerate, enumeratorCode);

		// ???
		generateDependancies(packagePath, existingFactories);
		return existingFactories;
	}

	private StringBuilder generateImports() {
		StringBuilder generatorHeader = new StringBuilder();

		// all imports for enumerations (some can be unused)
		generatorHeader.append("import " + Enumeration.class.getCanonicalName()
				+ ";" + ENDL);
		generatorHeader.append("import " + F.class.getCanonicalName() + ";"
				+ ENDL);
		generatorHeader.append("import " + Combinators.class.getCanonicalName()
				+ ";" + ENDL);
		generatorHeader.append("import " + BigInteger.class.getCanonicalName()
				+ ";" + ENDL);

		// import the class to be enumerated
		// really needed?
		generatorHeader.append("import " + class2enumerate.getCanonicalName()
				+ ";" + ENDL);

		// import the classes used in the profile of the constructor to be used
		// for the enumeration
//		for (Class<?> cla : constructorParameters) {
//			// TODO: isPrimitive = int, long, byte, float, double, boolean,
//			// short, char OU java.lang.*
//			// Actually it s just int, long, byte, float, double, boolean,
//			// short, char that are considered as Primitive
//			if (!Tools.isLanguage(cla) && !Tools.isPrimitive(cla)) {
//				generatorHeader.append("import " + cla.getCanonicalName() + ";"
//						+ ENDL);
//			}
//		}
		return generatorHeader;
	}

	protected StringBuilder generateClass(String packagePath)
			throws ClassNotFoundException, GeneratorFactoryException {
		StringBuilder generatorClass = new StringBuilder();
		generatorClass.append("public class " + class2enumerate.getSimpleName()
				+ "Factory{" + ENDL);
		generatorClass.append(generateClassBody(packagePath));
		generatorClass.append("}" + ENDL);
		return generatorClass;
	}

	/**
	 * allow the subclasses to create the core of the "Class"Factory
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException
	 */
	protected StringBuilder generateClassBody(String packagePath)
			throws ClassNotFoundException, GeneratorFactoryException {
		
		enumeratorCode.append("public static final " + this.classTypeParameters
				+ "Enumeration<"
				+ class2enumerate.getCanonicalName()+this.classTypeParameters
//				+ ParaType.createParaType(class2enumerate).getStringClass()
				+ "> getEnumeration(");
		// TODO - if several type parameters then several arguments (as for classTypeParameters in constructor)
		if (isParametrized) {
			enumeratorCode.append("final Enumeration" + this.classTypeParameters
					+ " enumeration");
		}
		enumeratorCode.append("){" + ENDL);
		if (class2enumerate.getAnnotation(EnumerateStatic.class) != null) {
			generateEnumerationBodyStatic();
		} else {
			generateEnumerationBody(packagePath);
		}

		enumeratorCode.append("}" + ENDL);
		
		return enumeratorCode;
	}

	protected void generateEnumerationBody(String packagePath)
			throws ClassNotFoundException, GeneratorFactoryException {
//		if (canBeNull || isParametrized) {
//			String cl = 
					// ParaType.createParaType(class2enumerate).getStringClass();
			enumeratorCode.append("final Enumeration<" + class2enumerate.getCanonicalName()+this.classTypeParameters
					+ "> emptyEnum = Enumeration.singleton(null);" + ENDL);

//		}
		enumeratorCode.append("Enumeration<"
//				+ ParaType.createParaType(class2enumerate).getStringClass()
				+ class2enumerate.getCanonicalName()+this.classTypeParameters
				+ "> enumRes = null;" + ENDL);
		generateEnumerationFunction();
		generateEnum(packagePath);
		enumeratorCode.append("return enumRes;" + ENDL);
	}

	protected void generateEnumerationFunction() {
		enumeratorCode.append("final "
				+ getParameterList(0)
				+ " "
				+ variableName4Class() + " = new " + getParameterList(0)
				+ "() {" + ENDL);
		enumeratorCode.append(generateEnumerationFunction(1));
		enumeratorCode.append("};" + ENDL);
	}

	public String generateEnumerationFunction(int index) {
		StringBuilder sb = new StringBuilder();
		if (index < this.constructorParameters.size()) {
			sb.append("public " + getParameterList(index) + " apply (final "
//					+ paraType.get(index - 1).getStringClass() + "" + " arg"
					+ typeName4Parameter(index - 1) + "" + " arg"
					+ (index - 1) + "){" + ENDL);
			sb.append("return new " + getParameterList(index) + "() {" + ENDL);
			sb.append(generateEnumerationFunction(index + 1));
			sb.append("};" + ENDL);
			sb.append("}" + ENDL);
		} else {
			sb.append("public "
//					+ ParaType.createParaType(class2enumerate).getStringClass()
					+ class2enumerate.getCanonicalName()+this.classTypeParameters
					+ " apply (final "
//					+ paraType.get(index - 1).getStringClass() + "" + " arg"
					+ typeName4Parameter(index - 1) + "" + " arg"
					+ (index - 1) + "){" + ENDL);
			sb.append("return new "
//					+ ParaType.createParaType(class2enumerate).getStringClass()
					+ class2enumerate.getCanonicalName()+this.classTypeParameters
					+ "(" + listArgs() + ");" + ENDL);
			sb.append("}" + ENDL);
		}

		return sb + "";
	}

	
	private String variableName4Class() {
		String canonicalName=this.class2enumerate.getCanonicalName().toLowerCase();
		StringBuilder sb = new StringBuilder();
		String l[] = canonicalName.split(Pattern.quote("."));
		for (String s : l) {
			sb.append("_" + s);
		}
		return sb + "";
	}

	private String listArgs() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.constructorParameters.size(); i++) {
			sb.append("arg" + i);
			if (i < paraType.size() - 1) {
				sb.append(", ");
			}
		}
		return sb + "";
	}

//	private String getParameterList(int index) {
//		StringBuilder sb = new StringBuilder();
//
//		if (index < paraType.size()) {
//			sb.append("F<" + paraType.get(index).getStringClass() + ",");
//			sb.append(getParameterList(index + 1));
//			sb.append(">");
//		} else {
//			sb.append(ParaType.createParaType(class2enumerate).getStringClass());
//		}
//		return sb + "";
//	}


	/****************************************** END ---- GenerateFunction---- **********************************************************/

	private String getParameterList(int index) {
		StringBuilder sb = new StringBuilder();

		if (index < this.constructorParametersTypes.size()) {
			sb.append("F<" + typeName4Parameter(index) + ",");
			sb.append(getParameterList(index + 1));
			sb.append(">");
		} else {
//			sb.append(class2enumerate.getCanonicalName()+this.classTypeParameters);
			sb.append(typeName4Parameter(CLASS));
		}

		return sb + "";
	}

	private  String typeName4Parameter(int index){
		String res="";
		Type paraType=class2enumerate;
		if (index < this.constructorParametersTypes.size()) {
			 paraType=this.constructorParametersTypes.get(index);
		}else{
			return class2enumerate.getCanonicalName()+this.classTypeParameters;
		}
		// TODO: misses GenericArrayType (http://www.artima.com/weblogs/viewpost.jsp?thread=208860)
		if(paraType instanceof TypeVariable || paraType instanceof ParameterizedType){
			res+=paraType;
		}else if(paraType instanceof Class){
			if(Tools.isBasicType(paraType)){
				// System.out.println("PRIMITIVE = "+paraType);
				res=Tools.name4PrimitiveType(paraType);
			}else{
				res=this.constructorParameters.get(index).getCanonicalName();
				// System.out.println("CLASS = "+paraType);
			}
		}else {
			res="ERROR";
		}
		
		return res;
	}
	
	/************************************* GENERATE ENUMERATOR **************************************/

	protected void generateEnum(String packagePath) {

		// TODO: constructor contains other type parameters than the class
		if (constructorParameters.contains(class2enumerate)) {
			recursiveCall();
		}

		for (int i = 0; i < this.constructorParameters.size(); i++) {
			ParaType pt = paraType.get(i);
			Class<?> cla = this.constructorParameters.get(i);
			Type paramType = this.constructorParametersTypes.get(i);
			
			Enumerate annotation = getEnumerateAnnotation(i);

			// case : the Parameter is the class that we want to generate the
			// Factory

//			if (cla != class2enumerate) {
		    if (!class2enumerate.equals(cla)) {

//				if (Tools.isPrimitive(cla)) {
			    if (Tools.isBasicType(paramType)) {
					enumeratorCode.append("final Enumeration<"
//							+ pt.getStringClass() + "> arg" + i + " = ");
							+ Tools.name4PrimitiveType(paramType) + "> arg" + i + " = ");
					enumeratorCode.append("new Enumeration <"
//							+ pt.getStringClass() + ">(");
							+ Tools.name4PrimitiveType(paramType) + ">(");
					enumeratorCode.append("Combinators.make"
//							+ paraType.get(i).getStringClass() + "().parts()");
							+ Tools.name4PrimitiveType(paramType) + "().parts()");

					if (annotation != null && annotation.maxSize() > 0) {//
						// make anything
						enumeratorCode.append(".take(BigInteger.valueOf("
								+ annotation.maxSize() + "))");
					}
					enumeratorCode.append(");" + ENDL);
				} else if(cla.equals(String.class)){
					enumeratorCode.append("final Enumeration<"
							+ typeName4Parameter(i) + "> arg" + i + " = ");
					enumeratorCode.append("new Enumeration <"
							+ typeName4Parameter(i) + ">(");
					enumeratorCode.append("Combinators.make"
							+ "String" + "().parts()");

					if (annotation != null && annotation.maxSize() > 0) {//
						// make anything
						enumeratorCode.append(".take(BigInteger.valueOf("
								+ annotation.maxSize() + "))");
					}
					enumeratorCode.append(");" + ENDL);
					
				} else if (List.class.isAssignableFrom(cla)) {
				
					// PACKAGELIBRARY

					enumeratorCode.append("Enumeration<" + pt.getStringClass()
							+ "> arg" + i + " = ");
					enumerateList(pt);
					enumeratorCode.append(";" + ENDL);

				} else if (pt instanceof ParaTypeClass) {
					enumeratorCode.append("final Enumeration<"
//							+ pt.getStringClass() + "> arg" + i + " = ");
							+ typeName4Parameter(i) + "> arg" + i + " = ");
					enumeratorCode.append(packagePath + "."
							+ this.constructorParameters.get(i).getSimpleName() + "Factory.getEnumeration();"
							+ ENDL);
				}
			}
		}

		enumeratorCode.append("enumRes = ");
		enumeratorCode.append(generateEnumerationApply() + ";" + ENDL);

		if (canBeNull) {
			// enumeratorCode.append("final Enumeration<"+class2enumerate.getCanonicalName()+"> emptyEnum = Enumeration.singleton(null);"+ENDL);
			enumeratorCode.append("enumRes = emptyEnum.plus(enumRes);" + ENDL);

		}

	}

	public void enumerateList(ParaType pt) {
		System.out.println("RID " + pt.getStringClass());
		if (!pt.isLeaf()) {

			enumeratorCode.append(PACKAGELIBRARY + "."
					+ pt.getSimpleStringClass() + "Factory.getEnumeration(");
			ParaType pt2 = pt.getParaTypes().get(0);
			enumerateList(pt2);
			enumeratorCode.append(")");
		} else {
			enumeratorCode.append(PACKAGELIBRARY + "."
					+ pt.getSimpleStringClass()
					+ "Factory.getEnumeration(false)");
		}

	}

	private void recursiveCall() {
//		String cl = ParaType.createParaType(class2enumerate).getStringClass();
		String cl = class2enumerate.getCanonicalName()+this.classTypeParameters;

		enumeratorCode.append("// recursive call" + ENDL);

		enumeratorCode.append("F<Enumeration<"
				+ cl
				+ ">, Enumeration<"
				+ cl
				+ ">> "
				+ variableName4Class()
						.toLowerCase() + "_Enum = new F<Enumeration<" + cl
				+ ">, Enumeration<" + cl + ">>() {" + ENDL);
		enumeratorCode.append("	public Enumeration<" + cl
				+ "> apply(final Enumeration<" + cl + "> t) {" + ENDL);
		enumeratorCode.append("		return emptyEnum.plus(" + ENDL);
		enumeratorCode
				.append("				Enumeration.apply(Enumeration.apply(Enumeration.apply("
						+ ENDL);
		enumeratorCode.append("						Enumeration.singleton("
				+ variableName4Class() + "), enumeration), t), t))" + ENDL);
		enumeratorCode.append("				.pay();" + ENDL);
		enumeratorCode.append("	}" + ENDL);
		enumeratorCode.append("};" + ENDL);
	}

	private Enumerate getEnumerateAnnotation(int index) {
		for (Annotation an : constructorParametersAnnotations.get(index)) {
			if (an.annotationType().getSimpleName()
					.equals(Enumerate.class.getSimpleName())) {
				return (Enumerate) an;
			}
		}
		return null;
	}

	private String generateEnumerationApply() {
		String codeEnumerationApply = "";

		int countReelEnumerator = 0;
		for (Class<?> c : constructorParameters) {
			if (c != class2enumerate && c != Object.class) {
				countReelEnumerator++;
			}
		}

		if (constructorParameters.contains(class2enumerate)) {
			codeEnumerationApply = "Enumeration.fix("
					+ variableName4Class() + "_Enum)";
		} else {
			codeEnumerationApply = "Enumeration.singleton("
					+ variableName4Class() + ")";
		}

		for (int i = 0; i < countReelEnumerator; i++) {
			codeEnumerationApply = "Enumeration.apply(" + codeEnumerationApply
					+ ", arg" + i + ")";
		}

		return codeEnumerationApply + "";
	}

	/****************************************** END ----GENERATE ENUMERATOR-------- **************************************/

	/************************************************ GENERATE ENUMERATOR STATIC ******************************************************/
	private void generateEnumerationBodyStatic() {

		List<String> stringField = new ArrayList<String>();
		for (Field f : class2enumerate.getFields()) {
			int mod = f.getModifiers();
			if (Modifier.isFinal(mod) && Modifier.isStatic(mod)
					&& Modifier.isPublic(mod)) {
				stringField.add(f.getName());
			}
		}

		enumeratorCode.append("Enumeration<Color> enumRes = null;" + ENDL);
		enumeratorCode
				.append("final Enumeration<Color> emptyEnum = Enumeration.singleton(null);"
						+ ENDL);
		for (int i = 0; i < stringField.size(); i++) {
			enumeratorCode.append("final Enumeration<Color> "
					+ stringField.get(i) + "Enum = Enumeration.singleton("
					+ class2enumerate.getCanonicalName() + "."
					+ stringField.get(i) + ");" + ENDL);
		}

		for (int i = stringField.size() - 1; i >= 0; i--) {
			if (i == stringField.size() - 1) {
				enumeratorCode.append("enumRes = " + stringField.get(i)
						+ "Enum;");
			} else {
				enumeratorCode.append("enumRes =enumRes.pay().plus("
						+ stringField.get(i) + "Enum);");
			}
		}
		enumeratorCode.append("return enumRes;");

	}

	/************************************************ END -- GENERATE ENUMERATOR STATIC ******************************************************/

	public void generateDependancies(String packagePath,
			Map<Class<?>, StringBuilder> existingFactories)
			throws ClassNotFoundException, GeneratorFactoryException {
		for (int i = 0; i < constructorParameters.size(); i++) {
			Class<?> c = constructorParameters.get(i);
			if (paraType.get(i) instanceof ParaTypeClass
					&& !existingFactories.containsKey(c)
					&& !Tools.isPrimitive(c)) {
				if (!Collection.class.isAssignableFrom(c)) {
					GeneratorFactory.getGenerator(c).generateEnumerator(
							packagePath, existingFactories);
				}
			}
		}

	}

	@Override
	public String toString() {
		return "Generator [class2enumerate=" + class2enumerate
				+ ", constructor4Enumerate=" + constructor4Enumerate
				+ ", constructorParameters=" + constructorParameters
				+ ", constructorParametersAnnotations="
				+ constructorParametersAnnotations + ", canBeNull=" + canBeNull
				+ ", paraType=" + paraType + "]";
	}

}
