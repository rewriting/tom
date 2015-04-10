package tom.library.factory.old;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import tom.library.enumerator.Enumeration;
import examples.factory.Color;

public class StaticFactory extends AbstractEnumeratorGenerator{

	public StaticFactory(Class<?> class2enumerate) {
		super(class2enumerate);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected StringBuilder generateParticularImports(Class<?>c){
		StringBuilder sb = new StringBuilder();
		appendln(sb, makeImport(List.class));
		appendln(sb, makeImport(Field.class));
		appendln(sb, makeImport(MyIntrospection.class));
		appendln(sb, makeImport(FieldConstructor.class));
		
		return sb;
	}
	
	
	
	@Override
	protected StringBuilder generateClassBody(FieldConstructor fc,String packagePath, Map<Class<?>,StringBuilder> classLists) throws IOException {
		// TODO Auto-generated method stub
		Class<?>c=fc.getTypeChamp();
		String CLASSNAME=c.getSimpleName();
		
		StringBuilder sb=new StringBuilder();
		
		appendln(sb, "public static final Enumeration<"+CLASSNAME+"> getEnumeration() {");
		appendln(sb, "Enumeration<"+CLASSNAME+"> enumRes = null;");
		appendln(sb, "try{");
		appendln(sb, "final Enumeration<"+CLASSNAME+"> emptyEnum = Enumeration.singleton(null);");
		int a=0;
		List<Field> listFC=MyIntrospection.getAllFieldFromEnumerateStaticClass(c);
		Map<String,Object>map=MyIntrospection.getValueStatic(c);
		
		try {
			sb.append(lesStatics(c));
		} catch (NoSuchFieldException | SecurityException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Field f:listFC){
			appendln(sb, "final Enumeration<"+CLASSNAME+"> "+f.getName()+"Enum = Enumeration.singleton("+f.getName()+");");
			a++;
		}
		
		if(!listFC.isEmpty())appendln(sb, "enumRes = "+listFC.get(a-1).getName()+"Enum;");
		for(int i=a-2;i>=0;i--){
			appendln(sb, "enumRes = enumRes.pay().plus("+listFC.get(i).getName()+"Enum);");
		}
		
		appendln(sb, "}catch(ClassNotFoundException | IllegalArgumentException| IllegalAccessException e){");
		
		appendln(sb, "}");
		appendln(sb, "return enumRes;");
		appendln(sb, "}");
		
		return sb;
	}
	
	public StringBuilder lesStatics(Class c) throws NoSuchFieldException, SecurityException, ClassNotFoundException{
		StringBuilder sb=new StringBuilder();
		appendln(sb, "Object obj=Class.forName(\""+c.getCanonicalName()+"\");");
		appendln(sb, "List<Field> listFC=MyIntrospection.getAllFieldFromEnumerateStaticClass("+c.getCanonicalName()+".class);");
		List<Field> listFC=MyIntrospection.getAllFieldFromEnumerateStaticClass(c);
		
		appendln(sb, "Field f;");
		for(Field fc:listFC){
			appendln(sb, "f=MyIntrospection.getField("+fc.getType().getCanonicalName()+".class,\""+fc.getName()+"\");");
		//c.getCLASS() n'est peut etre pas bon faudrait peut etre faire plus de reflection
			String clas=fc.getType().getCanonicalName();
			appendln(sb, clas+" "+fc.getName()+"=("+clas+")f.get(obj);");
		}
		
		return sb;
	}


	/************************************** IMPORTS ************************************/
	/**
	 * return the line to write to import this class
	 * 
	 * @param c
	 * @return
	 */
	public String makeImport(Class c) {
		return "import " + c.getCanonicalName() + ";";
	}

}
