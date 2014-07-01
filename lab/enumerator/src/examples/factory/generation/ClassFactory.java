package examples.factory.generation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import examples.factory.generation.Enumerate;

public class ClassFactory extends AbstractGeneratorFactory{

	
	
	
	@Override
	protected StringBuilder core(FieldConstructor fc,String packagePath) throws IOException{
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
			appendln(sb, makeEnumetator(c,f,packagePath));
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
			if(s.containsAnnotation(Enumerate.class)){
				stack.add(s);
			}
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


	public String makeEnumetator(Object o,FieldConstructor fc,String packagePath) throws IOException{
		
		// //@Enumerate(maxSize=4)
		// int no,
		
		StringBuilder sb=new StringBuilder();
		sb.append("final Enumeration<"+classNameWithParameter(fc)+"> "+fc.getNameField()+" = ");
		if(isPrimitive(fc.getTypeChamp())){
			sb.append("new Enumeration<"+classNameWithParameter(fc)+">("+"Combinators.make"+className(fc.getTypeChamp())+"().parts()");
			if(((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class))!=null&&
					((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()>0){
				sb.append(".take(BigInteger.valueOf("+((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()+"))");
				System.out.println("BROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOo");
			}
			sb.append(")");
		}
		else if(!o.getClass().equals(fc.getTypeChamp())){
			sb.append(className(fc.getTypeChamp())+"Factory.getEnumeration("+makeEnum2(fc.getParameter()+"")+")");
			generate(fc.getTypeChamp(),packagePath);
		}
		sb.append(";");
		return sb+"";
		
	}
	/***
	 * sert a faire ListFactory......
	 * @param t
	 * @return
	 */
	public String makeEnum2(String t){
		if(t.contains("<")){
			String nom=t.substring(t.indexOf("<"));
			t=t.substring(t.indexOf("<")+1,t.length()-1);
			int i=0;
			int ac=-1;
			while(i<t.length()){
				if(t.charAt(i)=='>'){
					ac=i;
				}
				i++;
			}
			if(ac!=-1){
				t=t.substring(0, ac-1);
			}
			System.out.println("T   "+t);
			return t+"Factory.getEnumeration("+makeEnum2(t)+")";
			
		}else{
			return "";
		}
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
