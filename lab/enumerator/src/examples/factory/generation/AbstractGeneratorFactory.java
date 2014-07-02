package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import tom.library.enumerator.Combinators;
import tom.library.enumerator.Enumeration;
import tom.library.enumerator.F;

import examples.factory.generation.EnumerateStatic;

public abstract class AbstractGeneratorFactory {
	
	public static String INTEGER=Integer.class.getSimpleName();
	public static String LONG=Long.class.getSimpleName();
	public static String BYTE=Byte.class.getSimpleName();
	public static String FLOAT=Float.class.getSimpleName();
	public static String DOUBLE=Double.class.getSimpleName();
	public static String BOOLEAN=Boolean.class.getSimpleName();
	public static String STRING=String.class.getSimpleName();
	
	
	public static String ENDL=System.getProperty("line.separator");
	public static String TAB="\t";

	// contains the classes that are already been treated
	private List<Class<?>> traite;
	
	/**
	 * Constuctor
	 */
	public AbstractGeneratorFactory(){
		super();
		this.traite=new ArrayList<Class<?>>();
	}
	
	/***
	 * Contain all the lines that we need to write in a file 
	 * Return the text of the factory of the class
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private StringBuilder appendGenerateFactory(FieldConstructor fc,String packagePath) throws IOException, ClassNotFoundException{
		StringBuilder sb=new StringBuilder();
		sb.append(appendImports(fc.getTypeChamp(),packagePath));
		sb.append(appendClass(fc,packagePath));
		return sb;
	}
	
	/**
	 * create the structur of the class in parameter and call core to fill it.
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	protected StringBuilder appendClass(FieldConstructor fc,String packagePath) throws IOException, ClassNotFoundException{
		StringBuilder sb=new StringBuilder();
		appendln(sb,"public class "+fc.getTypeChamp().getSimpleName()+"Factory{");
		sb.append(core(fc,packagePath));
		appendln(sb, "}");
		return sb;
	}
	
	/**
	 * allow the subclasses to create the core of the "Class"Factory 
	 * @param c
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	protected abstract StringBuilder core(FieldConstructor fc,String packagePath) throws IOException, ClassNotFoundException;

	/***
	 * create the import line for the class that we want to generate
	 * @param c : return the StringBuilder that contains the line to generate
	 * @return
	 */
	protected StringBuilder appendImports(Class<?>c,String packagePath){
		StringBuilder sb=new StringBuilder();
		appendln(sb, "package "+packagePath+";");
		appendln(sb, "import "+c.getCanonicalName()+";"+ENDL);
		for(String s:generateListImport(c)){
			appendln(sb, "import "+s+";"+ENDL);
		}
		appendln(sb, makeImport(Combinators.class));
		appendln(sb, makeImport(Enumeration.class));
		appendln(sb, makeImport(F.class));
		appendln(sb, makeImport(BigInteger.class));
		return sb;
	}
	/***
	 * generate the list of all import class that the Factory needs
	 * @param c
	 * @return
	 */
	public List<String>generateListImport(Class<?>c){
		List<FieldConstructor>list=MyIntrospection.getAllParameterFieldFromConstructorEnumerator(c);
		List<String>sortie=new ArrayList<String>();
		for(FieldConstructor fc:list){
			if(!isPrimitive(fc.getTypeChamp())){
				String s=fc.getTypeChamp().getCanonicalName();
				if(!sortie.contains(s)){
					sortie.add(s);
				}
				sortie.addAll(generateListImport(fc.getTypeChamp()));
			}
		}
		return sortie;
	}
	/***
	 * 
	 * @param c
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	protected void generateFile(FieldConstructor fc,String packagePath) throws IOException, ClassNotFoundException{
		if(!traite.contains(fc.getTypeChamp())){
			traite.add(fc.getTypeChamp());
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+className(fc.getTypeChamp())+"Factory.java"))));
			pw.print(appendGenerateFactory(fc,packagePath));
			pw.close();
		}
	}
	/**
	 * Function that to must be call to generate the factory of the class in parameter
	 * @param c
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void generate(Class<?> c,String packagePath) throws IOException, ClassNotFoundException{
		AbstractGeneratorFactory agf=null;
		System.out.println(c);
		if(List.class.isAssignableFrom(c)){
			System.out.println("BIOOOo");
			agf=new ListFactory();
		}else if(c.getAnnotation(EnumerateStatic.class)!=null){
			agf=new StaticFactory();
		}else{
			agf=new ClassFactory();
		}
		agf.generateFile(new FieldConstructor(null,c,null,null),packagePath);
	}
	
	
	/**
	 * makes the same that append and acts of sb but appends a ENDL at the end
	 * @param sb
	 * @param texte
	 */
	protected static void appendln(StringBuilder sb,String texte){
		sb.append(texte+ENDL);
	}
	/**
	 * return "number" tabulations
	 * @param number : number of tabulations that we want
	 * @return
	 */
	protected static String createTabulation(int number){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<number;i++){
			sb.append(TAB);
		}
		return sb+"";
	}
	
	public static String classNameWithParameter(FieldConstructor fc){
		Type l=fc.getParameter();
		
		StringBuilder sb=new StringBuilder();
		
		
		
		if(l!=null&&(l+"").contains("<")){
			sb.append(l);
		}else{
			sb.append(className(fc.getTypeChamp()));
		}

		System.out.println("SB  °  "+sb);
		
		return sb+"";
	}
	
	/**
	 * allow to obtain the class names for example the primitve "int" will be return like the class "int"
	 * @param c
	 * @return
	 */
	protected static String className(Class<?> c){
		String str[]={"int","long","byte","float","double","boolean","String"};
		String str2[]={INTEGER,LONG,BYTE,FLOAT,DOUBLE,BOOLEAN,STRING};
		for(int i=0;i<str.length;i++){
			String s=str[i];
			if(c!=null&&c.getSimpleName().toUpperCase().equals(s.toUpperCase())){
				return str2[i];
			}
		}
		return c.getSimpleName();
	}
	/**
	 * can be call to know if the class belong to the primitive set
	 * return True if that is right
	 * @param canonicalName
	 * @return
	 */
	public static boolean isPrimitive(Class c){
		String str[]={"int","long","byte","float","double","boolean","String"};
		for(int i=0;i<str.length;i++){
			String s=str[i];
			if(c!=null&&c.getSimpleName().toUpperCase().equals(s.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	/**
	 * return the line to write to import this class
	 * @param c
	 * @return
	 */
	protected static String makeImport(Class c){
		return "import "+c.getCanonicalName()+";";
	}
	
}
