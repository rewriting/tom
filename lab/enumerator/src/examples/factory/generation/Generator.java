package examples.factory.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;


public class Generator {

	protected Class<?> class2enumerate;

	protected Constructor<?> constructor2Class2Enumerate;


	protected StringBuilder enumeratorCode;

	protected boolean canBeNull;

	//is used to have a schema of stringClassName
	protected List<ParaType> paraType;

	protected List<Class<?>> listParameterType;

	public static final String ENDL = System.getProperty("line.separator");

	protected Annotation annotationConstructor;//Not null if the annotation is EnumerateGenerator

	protected List<List<Annotation>> listAnnotationParameterType;

	protected boolean isParametrized;

	public final static String PACKAGELIBRARY="examples.factory";
	
	public Generator(Class<?>c){

		this.class2enumerate=c;

		this.enumeratorCode=new StringBuilder();
		this.isParametrized=class2enumerate.getTypeParameters().length!=0;

		this.annotationConstructor=null;
		chooseConstructor();
		createListParameterType();

		this.paraType=ParaType.createParaTypeforConstructorParameter(constructor2Class2Enumerate);

		this.canBeNull=annotationConstructor!=null&&((EnumerateGenerator)annotationConstructor).canBeNull();


	}

	private void createListParameterType(){
		this.listParameterType=new ArrayList<Class<?>>();
		this.listAnnotationParameterType=new ArrayList<List<Annotation>>();

		Class<?> tclass[]=constructor2Class2Enumerate.getParameterTypes();


		for(int i=0;i<tclass.length;i++){
			listParameterType.add(tclass[i]);

			List<Annotation> annotations=new ArrayList<Annotation>();
			for(Annotation annot:constructor2Class2Enumerate.getParameterAnnotations()[i]){
				annotations.add(annot);
			}
			listAnnotationParameterType.add(annotations);

		}

	}

	private void chooseConstructor(){
		int i=0;
		int maxLenght=-1;
		int indexMaxLenght=-1;//index of the constructor with the longest list of Parameter Type
		Constructor<?> cons[]=class2enumerate.getConstructors();// array of all constructor of the class
		while(i<cons.length&&constructor2Class2Enumerate==null){
			Constructor<?> currentCons=cons[i];//current constructor
			if(currentCons.getAnnotation(EnumerateGenerator.class)!=null){
				constructor2Class2Enumerate=currentCons;
				annotationConstructor=currentCons.getAnnotation(EnumerateGenerator.class);
			}
			if(maxLenght<currentCons.getParameterTypes().length){
				indexMaxLenght=i;
				maxLenght=currentCons.getParameterTypes().length;
			}
			i++;
		}
		if(constructor2Class2Enumerate==null&&indexMaxLenght!=-1){
			constructor2Class2Enumerate=cons[indexMaxLenght];
		}

		System.out.println(maxLenght+" "+indexMaxLenght+" "+cons);
	}

	/*********************************END ---- initialization----**************************************/


	private void generatePackage(String packagePath){
		enumeratorCode.append("package " + packagePath + ";"+ENDL+ENDL);
	}

	private void generateImport(){
		// all imports for enumerations (some can be unused)
		enumeratorCode.append("import " + Enumeration.class.getCanonicalName()+";"+ENDL);
		enumeratorCode.append("import " + F.class.getCanonicalName()+";"+ENDL);


		if(true){//waiting for the condition but actually it is set true
			enumeratorCode.append("import " + Combinators.class.getCanonicalName()+";"+ENDL);
			enumeratorCode.append("import " + BigInteger.class.getCanonicalName()+";"+ENDL);
		}

		// import the class to be enumerated
		enumeratorCode.append("import " + class2enumerate.getCanonicalName()+";"+ENDL);
		// import the classes used in the profile of the constructor to be used for the enumeration

		for (Class<?> cla : listParameterType) {
			// TODO: isPrimitive = int, long, byte, float, double, boolean, short, char   OU   java.lang.*
			// Actually it s just int, long, byte, float, double, boolean, short, char that are considered as Primitive
			if (!Tools.isLanguage(cla)&&!Tools.isPrimitive(cla)) {
				enumeratorCode.append("import " + cla.getCanonicalName()+";"+ENDL);
			}
		}
		generateParticularImports();
	}
	


	protected void generateParticularImports(){
	/*	boolean containsCollection=false;
		Class<?> cla=null;
		int i=0;
		while(i<listParameterType.size()&&!containsCollection){
			containsCollection=List.class.isAssignableFrom(listParameterType.get(i));
			if(containsCollection){
				cla=listParameterType.get(i);
			}
			i++;
		}
		if(containsCollection){
			enumeratorCode.append("import "+cla.getSimpleName()+"Factory;"+ENDL);
		}*/
	}


	protected StringBuilder generateClass(String packagePath)
			throws ClassNotFoundException, GeneratorFactoryException {
		enumeratorCode.append( "public class " + class2enumerate.getSimpleName() + "Factory{" + ENDL);
		generateClassBody(packagePath);
		enumeratorCode.append("}"+ ENDL);
		return enumeratorCode;
	}


	/**
	 * allow the subclasses to create the core of the "Class"Factory
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws GeneratorFactoryException 
	 */
	protected void generateClassBody(String packagePath) throws ClassNotFoundException, GeneratorFactoryException{
		//class without TypeParameters
		String plus="";
		if(isParametrized){
			plus="<";
			int i=0;
			for(TypeVariable<?> tv:class2enumerate.getTypeParameters()){
				plus=plus+tv.getName();
				if(i<class2enumerate.getTypeParameters().length-1){
					plus+=",";
				}
				i++;
			}
			plus=plus+">";
		}
		enumeratorCode.append("public static final "+plus+"Enumeration<"+ParaType.createParaType(class2enumerate).getStringClass()+"> getEnumeration(");
		if(isParametrized){
			enumeratorCode.append("final Enumeration"+plus+" enumeration");
		}
		enumeratorCode.append("){"+ENDL);
		if(class2enumerate.getAnnotation(EnumerateStatic.class)!=null){
			generateEnumerationBodyStatic();
		}else{
			generateEnumerationBody(packagePath);
		}

		enumeratorCode.append("}"+ENDL);
		//	}else{

		//System.out.println("In Progress");
		//throw new NotImplementedException();
		//	}

	}

	protected void generateEnumerationBody(String packagePath) throws ClassNotFoundException, GeneratorFactoryException{
		if(canBeNull||isParametrized){
			String cl=ParaType.createParaType(class2enumerate).getStringClass();
			enumeratorCode.append("final Enumeration<"+cl+"> emptyEnum = Enumeration.singleton(null);"+ENDL);

		}
		enumeratorCode.append("Enumeration<"+ParaType.createParaType(class2enumerate).getStringClass()+"> enumRes = null;"+ENDL);
		generateEnumerationFunction();
		generateEnum(packagePath);
		enumeratorCode.append("return enumRes;"+ENDL);
	}

	protected void generateEnumerationFunction(){
		enumeratorCode.append("final "+getParameterList(0)+" "+variableNameRefactoring(class2enumerate.getCanonicalName().toLowerCase())+" = new "+getParameterList(0)+"() {"+ENDL);
		enumeratorCode.append(generateEnumerationFunction(1));
		enumeratorCode.append("};"+ENDL);
	}

	public String generateEnumerationFunction(int index){
		StringBuilder sb=new StringBuilder();
		if(index<paraType.size()){
			sb.append( "public "+getParameterList(index)+" apply (final "+paraType.get(index-1).getStringClass()+""+" arg"+(index-1)+"){"+ENDL);
			sb.append("return new "+getParameterList(index)+"() {"+ENDL);
			sb.append(generateEnumerationFunction(index+1));
			sb.append("};"+ENDL);
			sb.append("}"+ENDL);
		}else{
			sb.append( "public "+ParaType.createParaType(class2enumerate).getStringClass()+" apply (final "+paraType.get(index-1).getStringClass()+""+" arg"+(index-1)+"){"+ENDL);
			sb.append("return new "+ParaType.createParaType(class2enumerate).getStringClass()+"("+listArgs()+");"+ENDL);
			sb.append("}"+ENDL);
		}


		return sb+"";
	}

	public static String variableNameRefactoring(String canonicalName){
		StringBuilder sb=new StringBuilder();
		String l[]=canonicalName.split(Pattern.quote("."));
		for(String s:l){
			sb.append("_"+s);
		}
		return sb+"";
	}

	private String listArgs(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<paraType.size();i++){
			sb.append("arg"+i);
			if(i<paraType.size()-1){
				sb.append(", ");
			}
		}
		return sb+"";
	}


	private String getParameterList(int index){
		StringBuilder sb=new StringBuilder();

		if(index<paraType.size()){
			sb.append("F<"+paraType.get(index).getStringClass()+",");
			sb.append(getParameterList(index+1));
			sb.append(">");
		}else{
			sb.append(ParaType.createParaType(class2enumerate).getStringClass());
		}
		return sb+"";
	}




	/******************************************END ---- GenerateFunction----**********************************************************/


	/*************************************GENERATE ENUMERATOR**************************************/



	protected void generateEnum(String packagePath){

		if(listParameterType.contains(class2enumerate)){
			recursiveCall();
		}

		for(int i=0;i<paraType.size();i++){
			ParaType pt=paraType.get(i);
			Class<?> cla=listParameterType.get(i);
			Enumerate annotation=getEnumerateAnnotation(i);

			//case : the Parameter is the class that we want to generate the Factory

			if(cla!=class2enumerate){


				if(Tools.isPrimitive(cla)){
					enumeratorCode.append("final Enumeration<"+pt.getStringClass()+"> arg"+i+" = ");
					enumeratorCode.append("new Enumeration <"+pt.getStringClass()+">(");
					enumeratorCode.append("Combinators.make"+paraType.get(i).getStringClass()+"().parts()");

					if(annotation!=null&&annotation.maxSize()>0){//
						//make anything
						enumeratorCode.append(".take(BigInteger.valueOf("+annotation.maxSize()+"))");
					}
					enumeratorCode.append(");"+ENDL);
				}else if(List.class.isAssignableFrom(cla)){
					//PACKAGELIBRARY
					
					enumeratorCode.append("Enumeration<"+pt.getStringClass()+"> arg"+i+" = ");
					enumerateList(pt);
					enumeratorCode.append(";"+ENDL);
					
					
				}else if(pt instanceof ParaTypeClass){
					enumeratorCode.append("final Enumeration<"+pt.getStringClass()+"> arg"+i+" = ");
					enumeratorCode.append(packagePath+"."+cla.getSimpleName()+"Factory.getEnumeration();"+ENDL);
				}
			}
		}

		enumeratorCode.append("enumRes = ");
		enumeratorCode.append(generateEnumerationApply()+";"+ENDL);

		if(canBeNull){
			//enumeratorCode.append("final Enumeration<"+class2enumerate.getCanonicalName()+"> emptyEnum = Enumeration.singleton(null);"+ENDL);
			enumeratorCode.append("enumRes = emptyEnum.plus(enumRes);"+ENDL);

		}

	}
	
	public void enumerateList(ParaType pt){
		System.out.println("RID "+pt.getStringClass());
		if(!pt.isLeaf()){
			
			enumeratorCode.append(PACKAGELIBRARY+"."+pt.getSimpleStringClass()+"Factory.getEnumeration(");
			ParaType pt2=pt.getParaTypes().get(0);
			enumerateList(pt2);
			enumeratorCode.append(")");
		}else{
			enumeratorCode.append(PACKAGELIBRARY+"."+pt.getSimpleStringClass()+"Factory.getEnumeration(false)");
		}
		
		
	}

	private void recursiveCall(){
		String cl=ParaType.createParaType(class2enumerate).getStringClass();

		enumeratorCode.append("// recursive call"+ENDL);

		enumeratorCode.append("F<Enumeration<"+cl+">, Enumeration<"+cl+">> "+variableNameRefactoring(class2enumerate.getCanonicalName()).toLowerCase()+"_Enum = new F<Enumeration<"+cl+">, Enumeration<"+cl+">>() {"+ENDL);
		enumeratorCode.append("	public Enumeration<"+cl+"> apply(final Enumeration<"+cl+"> t) {"+ENDL);
		enumeratorCode.append("		return emptyEnum.plus("+ENDL);
		enumeratorCode.append("				Enumeration.apply(Enumeration.apply(Enumeration.apply("+ENDL);
		enumeratorCode.append("						Enumeration.singleton("+variableNameRefactoring(class2enumerate.getCanonicalName().toLowerCase())+"), enumeration), t), t))"+ENDL);
		enumeratorCode.append("				.pay();"+ENDL);
		enumeratorCode.append("	}"+ENDL);
		enumeratorCode.append("};"+ENDL);
	}

	private Enumerate getEnumerateAnnotation(int index){
		for(Annotation an:listAnnotationParameterType.get(index)){
			if(an.annotationType().getSimpleName().equals(Enumerate.class.getSimpleName())){
				return (Enumerate)an;
			}
		}
		return null;
	}

	private String generateEnumerationApply(){
		String codeEnumerationApply="";


		int countReelEnumerator=0;
		for(Class<?> c:listParameterType){
			if(c!=class2enumerate&&c!=Object.class){
				countReelEnumerator++;
			}
		}

		if(listParameterType.contains(class2enumerate)){
			codeEnumerationApply="Enumeration.fix("+variableNameRefactoring(class2enumerate.getCanonicalName().toLowerCase())+"_Enum)";
		}else{
			codeEnumerationApply="Enumeration.singleton("+variableNameRefactoring(class2enumerate.getCanonicalName().toLowerCase())+")";
		}








		for(int i=0;i<countReelEnumerator;i++){
			codeEnumerationApply="Enumeration.apply("+codeEnumerationApply+", arg"+i+")";
		}

		return codeEnumerationApply+"";
	}



	/******************************************END ----GENERATE ENUMERATOR--------**************************************/


	/************************************************GENERATE ENUMERATOR STATIC******************************************************/
	private void generateEnumerationBodyStatic(){

		List<String>stringField=new ArrayList<String>();
		for(Field f:class2enumerate.getFields()){
			int mod=f.getModifiers();
			if(Modifier.isFinal(mod)&&Modifier.isStatic(mod)&&Modifier.isPublic(mod)){
				stringField.add(f.getName());
			}
		}

		enumeratorCode.append("Enumeration<Color> enumRes = null;"+ENDL);
		enumeratorCode.append("final Enumeration<Color> emptyEnum = Enumeration.singleton(null);"+ENDL);
		for(int i=0;i<stringField.size();i++){
			enumeratorCode.append("final Enumeration<Color> "+stringField.get(i)+"Enum = Enumeration.singleton("+class2enumerate.getCanonicalName()+"."+stringField.get(i)+");"+ENDL);
		}

		for(int i=stringField.size()-1;i>=0;i--){
			if(i==stringField.size()-1){
				enumeratorCode.append("enumRes = "+stringField.get(i)+"Enum;");
			}else{
				enumeratorCode.append("enumRes =enumRes.pay().plus("+stringField.get(i)+"Enum);");
			}
		}
		enumeratorCode.append("return enumRes;");
		



	}


	/************************************************END -- GENERATE ENUMERATOR STATIC******************************************************/




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
	public void generateEnumerator(String packagePath,Map<Class<?>, StringBuilder> existingFactories)
			throws ClassNotFoundException, GeneratorFactoryException {
		// if it has been already generated just return it
		/*	if (existingFactories.containsKey(class2enumerate)) {
			return existingFactories.get(class2enumerate);
		}*/
		
		// if other classes need it they shouldn't try to build it
		existingFactories.put(class2enumerate, null);
		// build the enumerator code
		generatePackage(packagePath);
		generateImport();
		generateClass(packagePath);

		// add the code to the map
		System.out.println("PUT "+class2enumerate);
		existingFactories.put(class2enumerate, enumeratorCode);

		generateDependancies(packagePath,existingFactories);

	}

	public void generateDependancies(String packagePath,Map<Class<?>, StringBuilder> existingFactories) throws ClassNotFoundException, GeneratorFactoryException{
		for(int i=0;i<listParameterType.size();i++){
			Class<?> c=listParameterType.get(i);
			if(paraType.get(i) instanceof ParaTypeClass &&!existingFactories.containsKey(c)&&!Tools.isPrimitive(c)){
				if(!Collection.class.isAssignableFrom(c)){
					GeneratorFactory.getGenerator(c).generateEnumerator(packagePath,existingFactories);
				}
			}
		}

	}




}
