package examples.factory.generation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public 	abstract class ParaType{
	private List<ParaType> list;
	
	public ParaType(){
		
		this.list=new ArrayList<ParaType>();
	}
	public void addParaType(ParaType pt){
		this.list.add(pt);
	}
	
	public boolean isLeaf(){
		return this.list.isEmpty();
	}
	@Override
	public String toString() {
		return getStringClass()+" ListParaType = "+list;
	}
	
	public abstract String getStringClass();
	
	/**
	 * factory method to create paraType
	 * @param t
	 * @return
	 */
	public static ParaType createParaType(Type t){
		ParaType paraType=null;
		if(t instanceof ParameterizedTypeImpl){
			paraType=new ParaTypeClass(((ParameterizedTypeImpl)t).getRawType());
			for(Type t2:((ParameterizedTypeImpl)t).getActualTypeArguments()){
				paraType.addParaType(createParaType(t2));
			}
		}else if(t instanceof TypeVariableImpl){
			paraType=new ParaTypeGeneric(t+"");
		}else{
			paraType=new ParaTypeClass(((Class<?>)t));
		}
		return paraType;
	}
	
	public static List<ParaType> createParaTypeforConstructorParameter(Constructor<?> cons){
		List<ParaType> list=new ArrayList<ParaType>();
		for(Type t:cons.getGenericParameterTypes()){
			list.add(createParaType(t));
		}
		return list;
	}
	
	public static List<ParaType> createParaTypeforClassWithEnumerateGenerator(Class<?> c){
		Constructor<?>cons=MyIntrospection.extractConstructorWithEnumerateGenerator(c, EnumerateGenerator.class);
		List<ParaType> list=new ArrayList<ParaType>();
		for(Type t:cons.getGenericParameterTypes()){
			list.add(createParaType(t));
		}
		return list;
	}

}

class ParaTypeClass extends ParaType{
	private Class classType;
	public ParaTypeClass(Class classType){
		this.classType=classType;
	}
	@Override
	public String getStringClass() {
		// TODO Auto-generated method stub
		return classType.getCanonicalName();
	}
}

class ParaTypeGeneric extends ParaType{
	private String stringClassType;
	public ParaTypeGeneric(String stringClassType){
		this.stringClassType=stringClassType;
	}
	@Override
	public String getStringClass() {
		return stringClassType;
	}
}
