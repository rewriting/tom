package examples.factory.generation.reforge;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;
import examples.factory.Car;
import examples.factory.Color;
import examples.factory.Garage;
import examples.factory.Student;
import examples.factory.generation.AbstractEnumeratorGenerator;
import examples.factory.generation.Enumerate;
import examples.factory.generation.FieldConstructor;
import examples.factory.generation.GeneratorFactory;
import examples.factory.generation.GeneratorFactoryException;
import examples.factory.generation.MyIntrospection;
import examples.factory.generation.ParaType;
import examples.factory.generation.Tools;

abstract class T{
	public abstract String built();
}

abstract class T1 extends T{
	
	public static final String ENDL = System.getProperty("line.separator");
	
	protected ParaType paraType;
	
	protected int index;
	
	protected T1 father;
	
	public T1(ParaType paraType){
		this.paraType=paraType;
	}
	
	public void setIndex(int index){
		this.index=index;
	}
	public int getIndex(){
		return index;
	}
	
	public String getClassName(){
		return paraType.getStringClass();
	}
	
	public String getParameterList(){
		return getClassName();
	}
	
	public T1 getFather() {
		return father;
	}
	public void setFather(T1 father) {
		this.father = father;
	}
}

class T1Leaf extends T1{
	public T1Leaf(ParaType paraType){
		super(paraType);
		
	}
	@Override
	public String built() {
		// TODO Auto-generated method stub
		return "return new "+getClassName()+"("+listArgs()+");"+ENDL;
	}

	private String listArgs(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<getIndex();i++){
			sb.append("arg"+i);
			if(i<getIndex()-1){
				sb.append(", ");
			}
		}
		return sb+"";
	}
}




abstract class T1Decorator extends T1{
	protected T1 sohn;
	protected T1 father;
	
	public T1Decorator(T1 sohn,ParaType paraType){
		super(paraType);
		setSohn(sohn);
		sohn.setFather(this);
		setIndex(0);
	}
	public void setIndex(int index) {
		// TODO Auto-generated method stub
		this.index=index;
		this.sohn.setIndex(index+1);
	}
	
	public String getParameterList(){
		StringBuilder sb=new StringBuilder();
		sb.append("<"+getClassName());
		sb.append(sohn.getParameterList());
		sb.append(">");
		return sb+"";
	}
	
	protected static String variableRemove(String canonicalName){
		StringBuilder sb=new StringBuilder();
		String l[]=canonicalName.split(Pattern.quote("."));
		for(String s:l){
			sb.append("_"+s);
		}
		return sb+"";
	}
	public T1 getSohn() {
		return sohn;
	}
	public void setSohn(T1 sohn) {
		this.sohn = sohn;
	}

}

class T1NodeNormalDecorator extends T1Decorator{
	
	public T1NodeNormalDecorator(T1 sohn,ParaType paraType){
		super(sohn,paraType);
	}

	@Override
	public String built() {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		
		sb.append( "public "+"F"+getParameterList()+" apply (final "+getClassName()+""+" arg"+(index)+"){"+ENDL);
		sb.append("return new F"+getParameterList()+"() {");
		sb.append(sohn.built());
		sb.append("}");
		sb.append("}"+ENDL);
		
		return sb+"";
	}
	
	public String getParameterList(){
		StringBuilder sb=new StringBuilder();
		sb.append("<"+getClassName()+", ");
		sb.append(sohn.getParameterList());
		sb.append(">");
		return sb+"";
	}
	
}

class T1TopNormalDecorator extends T1Decorator{
	
	public T1TopNormalDecorator(T1 sohn,ParaType paraType){
		super(sohn,paraType);
	}

	@Override
	public String built() {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		
		sb.append("final F"+getParameterList()+" "+variableRemove(paraType.getStringClass().toLowerCase())+" = new F"+getParameterList()+"() {"+ENDL);
	//	makeG(sb, c, list, index);
		sb.append(sohn.built());
		sb.append("}"+ENDL);
		
		return sb+"";
	}
	


	
}

public abstract class ClassFactory extends AbstractEnumeratorGenerator{

	private T1 t1;
	
	public abstract String r1();
	
	public static final String ENDL = System.getProperty("line.separator");
	
	public String r2(){
		StringBuilder sb=new StringBuilder();
		//ecrire
		appendln(sb, t1.built());
		
		//ecrire
		
		return sb+"";
	}
	
	
	
	@Override
	protected StringBuilder core(FieldConstructor fc,String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException, ClassNotFoundException, GeneratorFactoryException{
		// TODO Auto-generated method stub

		StringBuilder sb=new StringBuilder();

		if(r1()!=null)appendln(sb, r1());

		if(r2()!=null)appendln(sb,r2());

		//fin classe
		return sb;
	}
	

	public static void main(String args[]){
		
		List<ParaType>list=ParaType.createParaTypeforClassWithEnumerateGenerator(Car.class);
		
		T1 t=new T1Leaf(ParaType.createParaType(Car.class));
		
		t=new T1NodeNormalDecorator(t,list.get(2));
		t=new T1NodeNormalDecorator(t,list.get(1));
		t=new T1TopNormalDecorator(t, list.get(0));
		
		System.out.println(t.built());
		
		
	}

}
