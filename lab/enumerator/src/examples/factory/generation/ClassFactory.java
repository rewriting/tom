package examples.factory.generation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import examples.factory.Car;
import examples.factory.Color;
import examples.factory.generation.Enumerate;

public class ClassFactory extends AbstractGeneratorFactory{




	@Override
	protected StringBuilder core(FieldConstructor fc,String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, ClassNotFoundException{
		// TODO Auto-generated method stub
		Class c=fc.getTypeChamp();
		String NOMCLASSE=c.getSimpleName();
		StringBuilder sb=new StringBuilder();
		//faire un cas si c'est un type primitif ou non.
		//debut classe


		//debut methode enumeration
		appendln(sb,"public static final Enumeration<"+NOMCLASSE+"> getEnumeration(){");
		appendln(sb,"boolean canBeNull = false;");
		appendln(sb,"// if(@Generator(canBeNull)){");
		appendln(sb,"    canBeNull = true;");
		appendln(sb,"// }");
		appendln(sb,"return getEnumeration(canBeNull);");
		appendln(sb,"}");
		//fin methode enumeration

		appendln(sb,"public static final Enumeration<"+NOMCLASSE+"> getEnumeration(boolean canBeNull) {");
		appendln(sb, "Enumeration<"+NOMCLASSE+"> enumRes = null;");

		makeF(sb,c);

		for(FieldConstructor f:makeList(c)){
			if(!f.getTypeChamp().equals(c)){
				appendln(sb, makeEnumetator(c,f,packagePath, classLists));
			}
		}
		List<FieldConstructor>list=makeList(c);

		Collections.reverse(list);
		appendln(sb, "enumRes = "+makeEnumeratorFinal(list, fc, 1)+";");

		appendln(sb, "if (canBeNull) {");
		appendln(sb, "final Enumeration<"+classNameWithParameter(fc)+"> emptyEnum = Enumeration.singleton(null);");
		appendln(sb, "enumRes = emptyEnum.plus(enumRes);");
		appendln(sb, "}");

		appendln(sb, "return enumRes;");

		sb.append("}"+ENDL);
		//fin classe
		return sb;
	}


	public String makeEnumeratorFinal(List<FieldConstructor> list,FieldConstructor fc,int index){
		StringBuilder sb=new StringBuilder();
		if(index<list.size()){
			sb.append("Enumeration.apply("+makeEnumeratorFinal(list,fc,index+1)+","+list.get(index).getNameField()+")");
		}else{
			sb.append("Enumeration.singleton(_"+classNameWithParameter(fc).toLowerCase()+")");
		}

		return sb+"";
	}


	public String listArgs(List<FieldConstructor>list){
		StringBuilder sb=new StringBuilder();
		int i=0;;
		for(FieldConstructor f:list){
			if(f.getNameField()!=null){
				sb.append(f.getNameField());
				if(i<list.size()-2){
					sb.append(", ");
				}
			}
			i++;
		}
		return sb+"";
	}

	/**
	 * ne renvoie que la list des FieldConstructeor possedant l'annotation Enumerate
	 * @param o
	 * @return
	 */
	public List<FieldConstructor> makeList(Class c){
		List<FieldConstructor>stack=new ArrayList<FieldConstructor>();
		List<FieldConstructor>list=MyIntrospection.getAllParameterFieldFromConstructorEnumerator(c);
		for(FieldConstructor s:list){
			stack.add(s);
		}
		stack.add(new FieldConstructor(null, c, null,null));

		return stack;
	}

	public void makeF(StringBuilder sb,Class c){
		List<FieldConstructor>list=makeList(c);
		String F1=ecrireF1(list,0);
		sb.append(createTabulation(0)+"final "+F1+" _"+className(c).toLowerCase()+"= new "+F1+"()");
		if(list.size()>1){
			appendln(sb,"{");
			makeF(sb,list,1);
			appendln(sb,"};");
		}else{
			appendln(sb,createTabulation(0)+";");
		}
	}



	public void makeF(StringBuilder sb,List<FieldConstructor>list,int index){
		appendln(sb, createTabulation(index)+"public "+ecrireF1(list,index)+" apply (final "+className(list.get(index-1).getTypeChamp())+" "+list.get(index-1).getNameField()+"){");

		if(index+1<list.size()){
			appendln(sb, createTabulation(index+1)+" return new "+ecrireF1(list,index)+"(){");
			makeF(sb, list, index+1);
			appendln(sb, createTabulation(index+1)+"};");

		}else{
			appendln(sb,createTabulation(index+1)+"return new "+className(list.get(index).getTypeChamp())+"("+listArgs(list)+");");
		}

		appendln(sb, createTabulation(index)+"}");
		//DANGER ICI avec simpleNAME

	}

	/***
	 * permet d'ecrire F<arg0 ,F<arg1,F<..........
	 * @param list
	 * @param index
	 * @return
	 */
	public String ecrireF1(List<FieldConstructor>list,int index){
		if(index+1<list.size()){
			return "F<"+classNameWithParameter(list.get(index))+", "+ecrireF1(list,index+1)+">";
		}else{
			return className(list.get(index).getTypeChamp());
		}
	}


	public String makeEnumetator(Object o, FieldConstructor fc, String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, ClassNotFoundException{

		// //@Enumerate(maxSize=4)
		// int no,

		StringBuilder sb=new StringBuilder();
		sb.append("final Enumeration<"+classNameWithParameter(fc)+"> "+fc.getNameField()+" = ");
		if(isPrimitive(fc.getTypeChamp())){
			sb.append("new Enumeration<"+classNameWithParameter(fc)+">("+"Combinators.make"+className(fc.getTypeChamp())+"().parts()");
			if(((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class))!=null&&
					((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()>0){
				sb.append(".take(BigInteger.valueOf("+((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()+"))");
			}
			sb.append(")");
		}
		else if(!o.getClass().equals(fc.getTypeChamp())){
			System.out.println("PARAM "+fc.getParameter());
			sb.append(treatClassNameWithGenericType(fc.getParameter()+"",packagePath, classLists));
			System.out.println("AFTER PARAM "+treatClassNameWithGenericType(fc.getParameter()+"",packagePath, classLists));
			Generator.generate(fc.getTypeChamp(), packagePath, classLists);
		}
		sb.append(";");
		return sb+"";

	}
	/***
	 * sert a faire ListFactory......
	 * @param t
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public String treatClassNameWithGenericType(String type, String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException{

		String genericType=getStringGenericType(type);
		String classType=getStringClassFromType(type);
		String classTypeSimpleName=classType;
		try{
			Class<?> nc=Class.forName(classType+"");
			if(!nc.isPrimitive()){
				Generator.generate(nc, packagePath, classLists);
			}
			classTypeSimpleName=nc.getSimpleName();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		System.out.println("TT SS "+classTypeSimpleName);
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
		//	System.out.println("T1 "+t);
		int i=0;
		int ac=-1;
		while(i<t.length()){
			if(t.charAt(i)=='>'){
				ac=i;
			}
			i++;
		}
		if(ac!=-1){
			//	System.out.println("AC");
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
			System.out.println("RWC   == "+name+"   "+name.indexOf("class ")+" "+name.substring(name.indexOf("class ")+"class ".length(), name.length()));
			return name.substring(name.indexOf("class ")+"class ".length(), name.length());
		}
		return name;
	}

	public Annotation getAnnotation(List<Annotation>list,Class annot){
		for(Annotation an:list){
			System.out.println("ANNOOO "+an);
			if(an.annotationType().getSimpleName().equals(annot.getSimpleName())){
				return an;
			}
		}
		return null;
	}

}
