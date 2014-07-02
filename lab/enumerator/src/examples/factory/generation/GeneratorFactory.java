package examples.factory.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorFactory {
	private static GeneratorFactory instance;
	
	private Map<EnumFactory,AbstractGeneratorFactory> factories;
	
	private GeneratorFactory(){
		this.factories=new HashMap<EnumFactory, AbstractGeneratorFactory>();
	}
	
	public AbstractGeneratorFactory getFactory(Class c){
		EnumFactory enumFactory=whichEnumFactory(c);
		if(!this.factories.containsKey(enumFactory)){
			
			AbstractGeneratorFactory agf;
			if(enumFactory==EnumFactory.ListFactory){
				agf=new ListFactory();
			}else if(enumFactory==EnumFactory.ClassFactory){
				agf=new ClassFactory();
			}else if(enumFactory==EnumFactory.StaticFactory){
				agf=new StaticFactory();
			}else{
				throw new UnsupportedOperationException("Must not be here");
			}
			this.factories.put(enumFactory,agf);
		}
		return this.factories.get(enumFactory);
	}
	
	private EnumFactory whichEnumFactory(Class c){
		EnumFactory enumFactory=null;
		if(List.class.isAssignableFrom(c)){
			enumFactory=EnumFactory.ListFactory;
		}else if(c.getAnnotation(EnumerateStatic.class)!=null){
			enumFactory=EnumFactory.StaticFactory;
		}else{
			enumFactory=EnumFactory.ClassFactory;
		}
		System.out.println("CLASS "+c+" ENUM "+enumFactory);
		return enumFactory;
	}
	
	public static GeneratorFactory getInstance(){
		if(instance==null){
			instance=new GeneratorFactory();
		}
		return instance;
	}
}
