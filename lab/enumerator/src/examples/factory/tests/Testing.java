package examples.factory.tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import tom.library.factory.old.EnumerateGenerator;
import tom.library.factory.old.ParaType;
import tom.library.factory.old.AbstractEnumeratorGenerator;
import tom.library.factory.old.MyIntrospection;
import examples.factory.Car;

class T1{
	@EnumerateGenerator
	public T1(int a,int b){

	}
}

class T2{
	@EnumerateGenerator
	public T2(Integer a,Integer b){

	}
}

class T3<A,B,C>{
	@EnumerateGenerator
	public T3(A a,B b){

	}
	public T3(Integer a,B b){

	}
}

class T4{
	@EnumerateGenerator
	public T4(List<Integer> l){

	}
}
class T5<E>{
	@EnumerateGenerator
	public T5(List<E> l){

	}
}
class T6<E>{
	@EnumerateGenerator
	public T6(ArrayList<E> l){

	}
}

class T7{
	@EnumerateGenerator
	public T7(Car l){

	}
}

class T8{
	@EnumerateGenerator
	public T8(AbstractEnumeratorGenerator aeg){

	}
}

class T9<A,B>{
	@EnumerateGenerator
	public T9(A a,B b){

	}
}

class T10<A,B>{
	@EnumerateGenerator
	public T10(A a,B b){

	}
}

class T11<A,B,C>{
	@EnumerateGenerator
	public T11(T10<T9<Integer,String>,T9<Long,Byte>> a,T9<T10<A,B>,Integer> b){

	}
}



public class Testing {
	public void affichage(Constructor cons){
		System.out.println("/*************************************/");
		System.out.println(cons);
		for(TypeVariable tv:cons.getTypeParameters()){
			System.out.print(tv.getName()+" - ");
		}
		System.out.println();
		for(Type t:cons.getGenericParameterTypes()){
			if( t instanceof ParameterizedTypeImpl){
				System.out.println("RawType "+((ParameterizedTypeImpl)t).getRawType() +" + ");
				System.out.println("OwnerType "+((ParameterizedTypeImpl)t).getOwnerType()+" + ");
				System.out.println("ActualTypeArguments");
				for(Type t2:((ParameterizedTypeImpl)t).getActualTypeArguments()){
					System.out.print(""+t2+" + ");
				}
				System.out.println();
				System.out.println("-----------------------");

			}else if(t instanceof TypeVariableImpl){
				System.out.print(((TypeVariableImpl)t) +" ////");
			}else{
				System.out.print(((Class)t) +" / ");
			}
			System.out.println("PARATYPE");
			System.out.println(ParaType.createParaType(t));

		}
		System.out.println();


	}






	public static void main(String args[]){
		Testing t=new Testing();
		Constructor cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T1.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T2.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T3.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T4.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T5.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T6.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T7.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T8.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T9.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T10.class, EnumerateGenerator.class);
		t.affichage(cons);
		cons=MyIntrospection.extractConstructorWithEnumerateGenerator(T11.class, EnumerateGenerator.class);
		t.affichage(cons);
		
		System.out.println("BBBBBBBBBBBBBBBBBBBBB");
//		System.out.println(ParaType.createParaTypeforConstructorParameter(cons));
	}
}
