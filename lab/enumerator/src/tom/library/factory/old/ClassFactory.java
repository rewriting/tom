package tom.library.factory.old;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClassFactory extends AbstractEnumeratorGenerator{


	public ClassFactory(Class<?> class2enumerate) {
		super(class2enumerate);
	}


	@Override
	protected  StringBuilder generateParticularImports(Class<?> class2enumerate){
		return new StringBuilder("");
	}
	
	
	@Override
	protected StringBuilder generateClassBody(FieldConstructor fc,String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, ClassNotFoundException, GeneratorFactoryException{
		StringBuilder sb=new StringBuilder();
		//faire un cas si c'est un type primitif ou non.
		//debut classe



		//debut methode enumeration
		appendln(sb,"public static final "+preBuiltEnumeration1(fc.getTypeChamp())+" getEnumeration(){");
		appendln(sb,"boolean canBeNull = false;");
		// TODO: generate "canBeNull = true;" if  canBeNull==true in the annotation Enumerate
		if(canBeNull){
			//		appendln(sb,"// if(@MainFactoryGenerator(canBeNull)){");
			appendln(sb,"    canBeNull = true;");
			//		appendln(sb,"// }");
		}
		appendln(sb,"return getEnumeration(canBeNull);");
		appendln(sb,"}");
		//fin methode enumeration

		appendln(sb, makeEnumerator(fc,packagePath,classLists));

		//fin classe
		return sb;
	}
	
	public String makeEnumerator(FieldConstructor fc,String packagePath, Map<Class<?>,StringBuilder> classLists) throws ClassNotFoundException, IOException, GeneratorFactoryException{
		StringBuilder sb=new StringBuilder();
		
		Class<?> c=fc.getTypeChamp();
		String typeParameters=Tools.getStringTypeParametersOfTheClass(c);
		
		appendln(sb,"public static final "+typeParameters+preBuiltEnumeration1(fc.getTypeChamp())+" getEnumeration(boolean canBeNull) {");
		appendln(sb, preBuiltEnumeration1(fc.getTypeChamp())+" enumRes = null;");
		appendln(sb,makeF(c));
		appendln(sb, makePostEnumerator(c, packagePath, classLists));
		appendln(sb, makeEnd(fc));
		sb.append("}"+ENDL);
		
		return sb+"";
	}
	
	public static String classNameWithParameter(FieldConstructor fc) {
		Type l = fc.getParameter();
		String typeParameters=/*getStringTypeParametersOfTheClass(fc.getTypeChamp())*/"";
		StringBuilder sb = new StringBuilder();

		if (l != null && (l + "").contains("<")) {
			System.out.println("A");
			sb.append(l);
		}else {
			System.out.println("AA");
			sb.append(Tools.className(fc.getTypeChamp())+typeParameters);
		}
		System.out.println(" BBB "+sb);
		return sb + "";
	}
	
	public String makeEnd(FieldConstructor fc){
		StringBuilder sb=new StringBuilder();
		
		
		List<Class<?>>list=MyIntrospection.getClassFromConstructorEnumerator(fc.getTypeChamp());
		int numberOfParameters=list.size();
		
		Collections.reverse(list);
		appendln(sb, "enumRes = "+makeEnumeratorFinal(fc.getTypeChamp(),numberOfParameters,  1)+";");

		appendln(sb, "if (canBeNull) {");
		appendln(sb, "final Enumeration<"+classNameWithParameter(fc)+"> emptyEnum = Enumeration.singleton(null);");
		appendln(sb, "enumRes = emptyEnum.plus(enumRes);");
		appendln(sb, "}");

		appendln(sb, "return enumRes;");
		
		return sb+"";
	}
	
	public String makePostEnumerator(Class<?> c,String packagePath,Map<Class<?>,StringBuilder> classLists) throws ClassNotFoundException, IOException, GeneratorFactoryException{
		StringBuilder sb=new StringBuilder();
		List<FieldConstructor>list=MyIntrospection.getAllParameterFieldFromConstructorEnumerator(c);
		for(int i=0;i<list.size();i++){
			if(!list.get(i).getTypeChamp().equals(c)){
				appendln(sb, makeEnumetator(c,list,i,packagePath, classLists));
			}
		}
		return sb+"";
	}

	public String makeEnumeratorFinal(Class c,int numberOfParameters,int index){
		StringBuilder sb=new StringBuilder();
		if(index-1<numberOfParameters){
			sb.append("Enumeration.apply("+makeEnumeratorFinal(c,numberOfParameters,index+1)+", arg"+(numberOfParameters-index)+")");
		}else{
			sb.append("Enumeration.singleton(_"+c.getSimpleName().toLowerCase()+")");
		}

		return sb+"";
	}


	public String listArgs(int numberArgs){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<numberArgs;i++){
			sb.append("arg"+i);
			if(i<numberArgs-1){
				sb.append(", ");
			}
		}
		return sb+"";
	}



	public String makeF(Class<?> c){
		StringBuilder sb=new StringBuilder();
		List<Class<?>>list=MyIntrospection.getClassFromConstructorEnumerator(c);
		String F1=ecrireF0(c,0);
		sb.append("final "+F1+" _"+c.getSimpleName().toLowerCase()+"= new "+F1+"()");
		if(list.size()>0){
			appendln(sb,"{");
			makeF(sb,c,list,1);
			appendln(sb,"};");
		}else{
			appendln(sb,";");
		}
		return sb+"";

	}

	public String ecrireF0(Class c,int index){
		List<String> listClass=new ArrayList<String>();
		for(FieldConstructor fc:MyIntrospection.getAllParameterFieldFromConstructorEnumerator(c)){
			listClass.add(classNameWithParameter(fc));
		}
		listClass.add(c.getCanonicalName());//+getStringTypeParametersOfTheClass(c));
		
		return ecrireF1(listClass, index);
	}
	
	

	public void makeF(StringBuilder sb,Class<?> c,List<Class<?>>list,int index){
		Class<?> nextClass=list.get(index-1);
		appendln(sb, "public "+ecrireF0(c,index)+" apply (final "+Tools.className(nextClass)+/*getStringTypeParametersOfTheClass(nextClass)*/""+" arg"+(index-1)+"){");
		makeG(sb, c, list, index);
		appendln(sb, "}");
		//DANGER ICI avec simpleNAME

	}

	public void makeG(StringBuilder sb,Class c,List<Class<?>>list,int index){
		if(index<list.size()){
			appendln(sb, " return new "+ecrireF0(c,index)+"(){");
			makeF(sb, c,list, index+1);
			appendln(sb, "};");

		}else{
			appendln(sb,"return new "+Tools.className(c)+/*getStringTypeParametersOfTheClass(c)*/""+"("+listArgs(list.size())+");");
		}
	}

	/***
	 * permet d'ecrire F<arg0 ,F<arg1,F<..........
	 * @param list
	 * @param index
	 * @return
	 */
	public String ecrireF1(List<String>list,int index){
		if(index+1<list.size()){
			return "F<"+list.get(index)+", "+ecrireF1(list,index+1)+">";
		}else{
			return list.get(index);
		}
	}

	

	public String makeEnumetator(Object o,List<FieldConstructor> listField, int index, String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, ClassNotFoundException, GeneratorFactoryException{

		// //@Enumerate(maxSize=4)
		// int no,
		
		
		//Annotation ann[]=
		
		FieldConstructor fc=listField.get(index);

		StringBuilder sb=new StringBuilder();
		sb.append("final Enumeration<"+classNameWithParameter(fc)+/*getStringTypeParametersOfTheClass(fc.getTypeChamp())*/""+"> arg"+index+" = ");
		if(Tools.isPrimitive(fc.getTypeChamp())){
			sb.append("new Enumeration<"+classNameWithParameter(fc)+">("+"Combinators.make"+Tools.className(fc.getTypeChamp())+"().parts()");
			if(((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class))!=null&&
					((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()>0){
				sb.append(".take(BigInteger.valueOf("+((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()+"))");
			}
			sb.append(")");
		}
		else if(!o.getClass().equals(fc.getTypeChamp())){
			sb.append(treatClassNameWithGenericType(fc.getParameter()+"",packagePath, classLists));
			GeneratorFactory.getGenerator(fc.getTypeChamp()).generateEnumerator(packagePath, classLists);
		}
		sb.append(";");
		return sb+"";

	}



	protected static String preBuiltEnumeration1(Class c){
		String NOMCLASSE=c.getSimpleName();
		String typeParameters=/*getStringTypeParametersOfTheClass(c)*/"";
		return " Enumeration<"+NOMCLASSE+typeParameters+">";
	}

	/***
	 * sert a faire ListFactory......
	 * @param t
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws GeneratorFactoryException 
	 */
	public String treatClassNameWithGenericType(String type, String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, GeneratorFactoryException{

		String genericType=getStringGenericType(type);
		String classType=getStringClassFromType(type);
		String classTypeSimpleName=classType;
		try{
			Class<?> nc=Class.forName(classType+"");
			if(!nc.isPrimitive()){
				GeneratorFactory.getGenerator(nc).generateEnumerator(packagePath, classLists);

			}
			classTypeSimpleName=nc.getSimpleName();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		if(genericType==null){
			return classTypeSimpleName+"Factory.getEnumeration()";
		}
		return packagePath+"."+classTypeSimpleName+"Factory.getEnumeration("+treatClassNameWithGenericType(genericType,packagePath,classLists)+")";//t => nom
	}

	/**
	 * get the String between < > for example "List<Car>" will return "Car"
	 * @param s
	 * @return
	 */
	public String getStringGenericType(String t){
		int i=0;
		int ac=-1;
		while(i<t.length()){
			if(t.charAt(i)=='>'){
				ac=i;
			}
			i++;
		}
		if(ac!=-1){
			t=t.substring(t.indexOf("<")+1, ac);
			return t+"";
		}else{
			return null;
		}
	}
	/***
	 * Return the class string from a Type cast to String for example List<Car> will return List
	 * This function complete the getStringGenericType function 
	 * @param type
	 * @return
	 */
	public String getStringClassFromType(String type){
		if(type.contains("<")){
			return removeWordClass(type.substring(0, type.indexOf("<")));
		}else{
			return removeWordClass(type);
		}
	}
	private String removeWordClass(String name){
		if(name.contains("class ")){
			return name.substring(name.indexOf("class ")+"class ".length(), name.length());
		}
		return name;
	}

	public Annotation getAnnotation(List<Annotation>list,Class annot){
		for(Annotation an:list){
			if(an.annotationType().getSimpleName().equals(annot.getSimpleName())){
				return an;
			}
		}
		return null;
	}
	
	

}
