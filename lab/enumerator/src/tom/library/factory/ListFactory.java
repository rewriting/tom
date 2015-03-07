package tom.library.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListFactory extends AbstractEnumeratorGenerator{

	public ListFactory(Class<?> class2enumerate) {
		super(class2enumerate);
		// TODO Auto-generated constructor stub
	}

	protected StringBuilder generateParticularImports(Class<?>c){
		StringBuilder sb = new StringBuilder();
		appendln(sb, "import " + ArrayList.class.getCanonicalName() + ";" );
		appendln(sb, "import " + List.class.getCanonicalName() + ";" );
		
		return sb;
	}
	
	@Override
	protected StringBuilder generateClassBody(FieldConstructor fc, String packagePath, Map<Class<?>,StringBuilder> classLists)
			throws IOException {
		// TODO Auto-generated method stub
		StringBuilder sb=new StringBuilder();
		/*appendln(sb, "			public static final Enumeration<Garage> getEnumeration() {");
		appendln(sb, "		boolean canBeNull = false;");
		appendln(sb, "		// if(@MainFactoryGenerator(canBeNull))");
		appendln(sb, "		// canBeNull = true;");
		appendln(sb, "		return getEnumeration(canBeNull);");
		appendln(sb, "	}");*/
		
		appendln(sb, "public static final <T> Enumeration<List<T>> getEnumeration(");
		appendln(sb, "		final Enumeration<T> enumeration) {");
		appendln(sb, "			Enumeration<List<T>> enumRes = null;");
		appendln(sb, "			final Enumeration<List<T>> emptyEnum = Enumeration.singleton((List<T>) new ArrayList<T>());");
		appendln(sb, "			final F<T, F<List<T>, List<T>>> _list = new F<T, F<List<T>, List<T>>>() {");
		appendln(sb, "						public F<List<T>, List<T>> apply(final T e) {");
		appendln(sb, "							return new F<List<T>, List<T>>(){");
		appendln(sb, "								public List<T> apply(final List<T> l) {");
		appendln(sb, "									List<T> list=new ArrayList<T>();");
		appendln(sb, "									list.add(e);");
		appendln(sb, "									list.addAll(l);");
		appendln(sb, "									return list;");
		appendln(sb, "								}");
		appendln(sb, "							};");
		appendln(sb, "				}");
		
		appendln(sb, "			};");
		appendln(sb, "			F<Enumeration<List<T>>, Enumeration<List<T>>> listEnum = new F<Enumeration<List<T>>, Enumeration<List<T>>>() {");
		appendln(sb, "				public Enumeration<List<T>> apply(final Enumeration<List<T>> t) {");
		appendln(sb, "					return emptyEnum.plus(");
		appendln(sb, "							Enumeration.apply(Enumeration.apply(");
		appendln(sb, "									Enumeration.singleton(_list), enumeration), t))");
		appendln(sb, "							.pay();");
		appendln(sb, "				}");
		appendln(sb, "			};");
		appendln(sb, "			enumRes=Enumeration.fix(listEnum);");
		appendln(sb, "			return enumRes;");
		appendln(sb, "		}");



		
		
		
		return sb;
	}


}
