package examples.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ParametrizedClass<A,B,C,D,E,F> {
	A a;
	B b;
	C c;
	D d;
	E e;
	F f;
	int toto;
	Car car;
	public ParametrizedClass(A ta,B tb,int R){
		
	}
	
	public static void main(String args[]){
		ParametrizedClass<Integer,String,Long,Float,Double,Byte> pt=new ParametrizedClass<Integer,String,Long,Float,Double,Byte>(10,"toto",5);
		/*for(Field f:pt.getClass().getDeclaredFields()){
			System.out.println(f.getGenericType());
		}*/
		Constructor<?> cons=pt.getClass().getConstructors()[0];
		System.out.println(cons.getGenericParameterTypes()[0]);
		
	}
}
