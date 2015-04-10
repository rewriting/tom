package tom.library.factory.old;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

public 	abstract class ParaType{
	protected List<ParaType> paraTypes;

	public ParaType(){

		this.paraTypes=new ArrayList<ParaType>();
	}
	public void addParaType(ParaType pt){
		this.paraTypes.add(pt);
	}

	public boolean isLeaf(){
		return this.paraTypes.isEmpty();
	}

	public List<ParaType> getParaTypes() {
		return paraTypes;
	}
	public void setParaTypes(List<ParaType> paraTypes) {
		this.paraTypes = paraTypes;
	}
	@Override
	public String toString() {
		return getStringClass()+" ListParaType = "+paraTypes;
	}

	public abstract String getStringClass();


	public String getSimpleStringClass(){
		String r=getStringClass();
		if(r.indexOf("<")!=-1){
			r=r.substring(0,r.indexOf("<"));
		}

		String tab[]=r.split(Pattern.quote("."));
		r=tab[tab.length-1];
		return r;
	}

	/**
	 * factory method to create paraType
	 * @param t
	 * @return
	 */
	public static ParaType createParaType(Type t){
		ParaType paraType=null;
		if(t instanceof TypeVariableImpl){
			paraType=new ParaTypeGeneric(t+"");
		}else if(t instanceof ParameterizedTypeImpl){
			paraType=new ParaTypeClass(((ParameterizedTypeImpl)t).getRawType());
			for(Type t2:((ParameterizedTypeImpl)t).getActualTypeArguments()){
				paraType.addParaType(createParaType(t2));
			}
		}else{
			paraType=new ParaTypeClass(((Class<?>)t));
		}
		return paraType;
	}

	public static ParaType createParaType(Class<?> c){
		ParaType paraType=new ParaTypeClass(((Class<?>)c));
		for(TypeVariable<?> tvi:c.getTypeParameters()){
			paraType.addParaType(new ParaTypeGeneric(tvi.getName()+""));
		}


		return paraType;
	}

	public static void main(String args[]){
		//		List<ParaType> l=ParaType.createParaTypeforClassWithEnumerateGenerator(Garage.class);
		//		for(ParaType pt:l){
		//			System.out.println(pt.getStringClass());
		//		}
		//	System.out.println(ParaType.createParaType(ParametrizedClass2.class));
		System.out.println(ParaType.createParaType(List.class).getStringClass());
	}


	public static List<ParaType> createParaTypeforClassWithEnumerateGenerator(Class<?> c){
		Constructor<?>cons=extractConstructorWithEnumerateGenerator(c, EnumerateGenerator.class);
		List<ParaType> list=new ArrayList<ParaType>();
		for(Type t:cons.getGenericParameterTypes()){
			list.add(createParaType(t));
		}
		return list;
	}


	public static Constructor extractConstructorWithEnumerateGenerator(Class c,Class enumerator){
		int i=0;
		while(i<c.getConstructors().length){
			if(c.getConstructors()[i].getAnnotation(enumerator)!=null){
				return c.getConstructors()[i];
			}
			i++;
		}
		return null;
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