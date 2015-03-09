package tom.library.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import examples.factory.Student;

public class MyIntrospection {

	/*********************************************************************************/

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

	
	public static List<Class<?>> getClassFromConstructorEnumerator(Class<?> c){
		List<Class<?>>listClass=new ArrayList<Class<?>>();
		Constructor<?> cons=extractConstructorWithEnumerateGenerator(c,EnumerateGenerator.class);
		if(cons!=null){
			for(Class<?> nc:cons.getParameterTypes()){
				listClass.add(nc);
			}
		}
		
		return listClass;
	}
	

	public static List<FieldConstructor> getAllParameterFieldFromConstructorEnumerator(Class<?> c){
		List<FieldConstructor>f=new ArrayList<FieldConstructor>();
		//rechercher le constructeur concerne( le premier avec annotation
		Constructor<?> cons=extractConstructorWithEnumerateGenerator(c,EnumerateGenerator.class);
		if(cons!=null){//manque les enumerator
			List<Type>genericType=getGenericTypeFieldClass(cons);
			cons.getParameterAnnotations();
			for(int i=0;i<cons.getParameterTypes().length;i++){
				Class<?> type=cons.getParameterTypes()[i];
				Type genericParameterType=cons.getGenericParameterTypes()[i];
				List<Annotation> annotations=new ArrayList<Annotation>();
				for(Annotation annot:cons.getParameterAnnotations()[i]){
					annotations.add(annot);
				}
				f.add(new FieldConstructor(annotations, type,genericType.get(i),genericParameterType));
			}
		}
		return f;
	}
	
	public static void main(String args[]) throws NoSuchMethodException, SecurityException{
		List<FieldConstructor> l=getAllParameterFieldFromConstructorEnumerator(Student.class);
		for(FieldConstructor fc:l){
			System.out.println(fc);
		}
	}

	
	public static List<Type> getGenericTypeFieldClass(Constructor c){
		List<Type> liste=new ArrayList<Type>();
		for(Type t:c.getGenericParameterTypes()){
			liste.add(t);
		}
		return liste;
	}
	

	/*public static List<Field> getAllFieldClass(Class<?> c){
		List<Field>f=new ArrayList<Field>();
		for(Field fi:c.getDeclaredFields()){
			if(!AlreadyContainFieldWithName(f,fi))f.add(fi);
		}
		if(c.getSuperclass()!=null&&!c.getSuperclass().equals(Object.class)){
			List<Field> newf=getAllFieldClass(c.getSuperclass());
			for(Field fi:newf){
				if(!AlreadyContainFieldWithName(f,fi))f.add(fi);
			}
		}
		return f;
	}*/

	public static boolean AlreadyContainFieldWithName(List<Field> l,Field nom){
		for(Field f:l){
			if(f.getName().equals(nom.getName())){
				return true;
			}
		}
		return false;
	}

	public static List<Field> getAllFieldFromEnumerateStaticClass(Class<?>c){
		List<Field>fc=new ArrayList<Field>();
		for(Field f:c.getDeclaredFields()){
			if(Modifier.isStatic(f.getModifiers())){
				List<Annotation> annotations=new ArrayList<Annotation>();
				for(Annotation annot:f.getAnnotations()){
					annotations.add(annot);
				}
				fc.add(f);
			}
		}
		return fc;
	}

	public static Map<String,Object> getValueStatic(Class c){
		Map<String, Object>map=new HashMap<String, Object>();
		try {
			Object obj=Class.forName(c.getCanonicalName());
			for(Field f:c.getDeclaredFields()){
				if(Modifier.isStatic(f.getModifiers())){
					map.put(f.getName(),f.get(obj));
				}
			}
		} catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	public static boolean containsAnnotation(List<Annotation> annotation,Class annot){
		boolean b=false;
		int i=0;
		while(!b&&i<annotation.size()){
			b=annotation.get(i).annotationType().getSimpleName().equals(annot.getSimpleName());
			i++;
		}
		return b;
	}






	public static Field getField(Class c,String nom){
		int i=0;
		Field listF[]=c.getFields();
		Field f=null;
		while(i<listF.length&&f==null){
			if(listF[i].getName().equals(nom)){
				f=listF[i];
			}
			i++;
		}

		return f;

	}










}
