package examples.factory.generation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import examples.factory.generation.Enumerate;

public class ClassFactory extends AbstractGeneratorFactory{

	
	
	
	@Override
	protected StringBuilder core(Class c) throws IOException{
		// TODO Auto-generated method stub
		
		String NOMCLASSE=c.getSimpleName();
		StringBuilder sb=new StringBuilder();
		//faire un cas si c'est un type primitif ou non.
		//debut classe
		

		//debut methode enumeration
		appendLine(sb,"public static final Enumeration<"+NOMCLASSE+"> getEnumeration(){");
		appendLine(sb,"boolean canBeNull = false;");
		appendLine(sb,"// if(@Generator(canBeNull)){");
		appendLine(sb,"    canBeNull = true;");
		appendLine(sb,"// }");
		appendLine(sb,"return getEnumeration(canBeNull);");
		appendLine(sb,"}");
		//fin methode enumeration

		appendLine(sb,"public static final Enumeration<"+NOMCLASSE+"> getEnumeration(boolean canBeNull) {");
		appendLine(sb, "Enumeration<"+NOMCLASSE+"> enumRes = null;");

		makeF(sb,c);

		for(FieldConstructor fc:makeList(c)){
			if(!fc.getTypeChamp().equals(c)){
			appendLine(sb, makeEnumetator(c,fc));
			}
		}
		List<FieldConstructor>list=makeList(c);
		
		Collections.reverse(list);
		appendLine(sb, "enumRes = "+makeEnumeratorFinal(list, c, 1)+";");
		
		appendLine(sb, "if (canBeNull) {");
		appendLine(sb, "final Enumeration<"+nomClasse(c)+"> emptyEnum = Enumeration.singleton(null);");
		appendLine(sb, "enumRes = emptyEnum.plus(enumRes);");
		appendLine(sb, "}");
		
		appendLine(sb, "return enumRes;");
		
		sb.append("}"+ENDL);
		//fin classe
		return sb;
	}
	

	
	public String makeEnumeratorFinal(List<FieldConstructor> list,Class c,int index){
		StringBuilder sb=new StringBuilder();
		if(index<list.size()){
			sb.append("Enumeration.apply("+makeEnumeratorFinal(list,c,index+1)+","+list.get(index).getNameField()+")");
		}else{
			sb.append("Enumeration.singleton(_"+nomClasse(c).toLowerCase()+")");
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
		List<FieldConstructor>list=MyIntrospection.recupererTousChampConstructorEnumerator(c);
		for(FieldConstructor s:list){
			if(s.containsAnnotation(Enumerate.class)){
				stack.add(s);
			}
		}
		stack.add(new FieldConstructor(null, c, null));

		return stack;
	}

	public void makeF(StringBuilder sb,Class c){
		List<FieldConstructor>list=makeList(c);
		String F1=ecrireF1(list,0);
		sb.append(getTab(0)+"final "+F1+" _"+nomClasse(c).toLowerCase()+"= new "+F1+"()");
		if(list.size()>1){
			appendLine(sb,"{");
			makeF(sb,list,1);
			appendLine(sb,"};");
		}else{
			appendLine(sb,getTab(0)+";");
		}
	}



	public void makeF(StringBuilder sb,List<FieldConstructor>list,int index){
		appendLine(sb, getTab(index)+"public "+ecrireF1(list,index)+" apply (final "+nomClasse(list.get(index-1).getTypeChamp())+" "+list.get(index-1).getNameField()+"){");

		if(index+1<list.size()){
			appendLine(sb, getTab(index+1)+" return new "+ecrireF1(list,index)+"(){");
			makeF(sb, list, index+1);
			appendLine(sb, getTab(index+1)+"};");

		}else{
			appendLine(sb,getTab(index+1)+"return new "+nomClasse(list.get(index).getTypeChamp())+"("+listArgs(list)+");");
		}

		appendLine(sb, getTab(index)+"}");
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
			Class actuel=list.get(index).getTypeChamp();
			List<FieldConstructor> cloner=new ArrayList<FieldConstructor>(list);
			return "F<"+nomClasse(actuel)+", "+ecrireF1(list,index+1)+">";
		}else{
			return nomClasse(list.get(index).getTypeChamp());
		}
	}


	public String makeEnumetator(Object o,FieldConstructor fc) throws IOException{
		
		// //@Enumerate(maxSize=4)
		// int no,
		final Enumeration<Integer> noEnum = new Enumeration<Integer>(
				Combinators.makeInteger().parts().take(BigInteger.valueOf(4)));
		
		StringBuilder sb=new StringBuilder();
		sb.append("final Enumeration<"+nomClasse(fc.getTypeChamp())+"> "+fc.getNameField()+" = ");
		if(isPrimitive(fc.getTypeChamp().getSimpleName())){
			sb.append("new Enumeration<"+nomClasse(fc.getTypeChamp())+">("+"Combinators.make"+nomClasse(fc.getTypeChamp())+"().parts()");
			if(((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class))!=null&&
					((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()>0){
		//TODO: a rajouter quand ca marchera		sb.append(".take(BigInteger.valueOf("+((Enumerate)getAnnotation(fc.getAnnotation(),Enumerate.class)).maxSize()+"))");
				System.out.println("BROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOo");
			}
			sb.append(")");
		}
		else if(!o.getClass().equals(fc.getTypeChamp())){
			sb.append(nomClasse(fc.getTypeChamp())+"Factory.getEnumeration()");
			generate(fc.getTypeChamp());
		}
		sb.append(";");
		return sb+"";
		
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
