package examples.factory.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyIntrospection {

	/*********************************************************************************/















	public static Constructor extraireConstructor(Class c){
		int i=0;
		while(i<c.getConstructors().length){
			if(c.getConstructors()[i].getAnnotation(Generator.class)!=null){
				return c.getConstructors()[i];
			}
			i++;
		}
		return null;
	}


	public static List<FieldConstructor> recupererTousChampConstructorEnumerator(Class<?> c){
		List<FieldConstructor>f=new ArrayList<FieldConstructor>();
		int r=0;
		//rechercher le constructeur concerne( le premier avec annotation
		Constructor cons=extraireConstructor(c);
		if(cons!=null){//manque les enumerator
			cons.getParameterAnnotations();
			for(int i=0;i<cons.getParameterTypes().length;i++){
				Class type=cons.getParameterTypes()[i];

				List<Annotation> annotations=new ArrayList<Annotation>();
				for(Annotation annot:cons.getParameterAnnotations()[i]){
					annotations.add(annot);
				}
				//Annotation annot[]=new Annotation[]
				f.add(new FieldConstructor(annotations, type,"arg"+r++));

			}
		}

		return f;
	}

	public static List<Field> recupererTousChampEnumerateStatic(Class<?>c){
		List<Field>fc=new ArrayList<Field>();

		Map<String,Object> value=getValueStatic(c);
		System.out.println("MAP");
		System.out.println(value);

		for(Field f:c.getDeclaredFields()){

			if(Modifier.isStatic(f.getModifiers())){
				System.out.println("F = "+f.getName());
				List<Annotation> annotations=new ArrayList<Annotation>();
				for(Annotation annot:f.getAnnotations()){
					annotations.add(annot);
				}
			//	fc.add(new FieldConstructor(annotations, f.getType(), f.getName()));
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
