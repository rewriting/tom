package examples.factory.generation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import tom.library.enumerator.Enumeration;
import examples.factory.Color;

public class StaticFactory extends AbstractGeneratorFactory{

	protected StringBuilder lesImports(Class<?>c){
		StringBuilder sb=super.lesImports(c);
		appendLine(sb, makeImport(List.class));
		appendLine(sb, makeImport(Field.class));
		appendLine(sb, makeImport(MyIntrospection.class));
		appendLine(sb, makeImport(FieldConstructor.class));
		
		
		return sb;
	}
	
	
	
	@Override
	protected StringBuilder core(Class<?> c) throws IOException {
		// TODO Auto-generated method stub
		String CLASSNAME=c.getSimpleName();
		
		StringBuilder sb=new StringBuilder();
		
		appendLine(sb, "public static final Enumeration<"+CLASSNAME+"> getEnumeration() {");
		appendLine(sb, "Enumeration<"+CLASSNAME+"> enumRes = null;");
		appendLine(sb, "try{");
		appendLine(sb, "final Enumeration<"+CLASSNAME+"> emptyEnum = Enumeration.singleton(null);");
		int a=0;
		List<Field> listFC=MyIntrospection.recupererTousChampEnumerateStatic(c);
		Map<String,Object>map=MyIntrospection.getValueStatic(c);
		
		try {
			sb.append(lesStatics(c));
		} catch (NoSuchFieldException | SecurityException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Field fc:listFC){
			appendLine(sb, "final Enumeration<"+CLASSNAME+"> "+fc.getName()+"Enum = Enumeration.singleton("+fc.getName()+");");
			a++;
		}
		
		if(!listFC.isEmpty())appendLine(sb, "enumRes = "+listFC.get(a-1).getName()+"Enum;");
		for(int i=a-2;i>=0;i--){
			appendLine(sb, "enumRes = enumRes.pay().plus("+listFC.get(i).getName()+"Enum);");
		}
		
		appendLine(sb, "}catch(ClassNotFoundException | IllegalArgumentException| IllegalAccessException e){");
		
		appendLine(sb, "}");
		appendLine(sb, "return enumRes;");
		appendLine(sb, "}");
		
		return sb;
	}
	
	public StringBuilder lesStatics(Class c) throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		StringBuilder sb=new StringBuilder();
		appendLine(sb, "Object obj=Class.forName(\""+c.getCanonicalName()+"\");");
		appendLine(sb, "List<Field> listFC=MyIntrospection.recupererTousChampEnumerateStatic("+c.getCanonicalName()+".class);");
		List<Field> listFC=MyIntrospection.recupererTousChampEnumerateStatic(c);
		
		appendLine(sb, "Field f;");
		for(Field fc:listFC){
			appendLine(sb, "f=MyIntrospection.getField("+fc.getType().getCanonicalName()+".class,\""+fc.getName()+"\");");
		//c.getCLASS() n'est peut etre pas bon faudrait peut etre faire plus de reflection
			String clas=fc.getType().getCanonicalName();
			appendLine(sb, clas+" "+fc.getName()+"=("+clas+")f.get(obj);");
		}
		
		return sb;
	}


}
