package examples.factory.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

	private List<Class<?>> traite;
	private List<Class<?>> imports;
	
	
	public AbstractGeneratorFactory(){
		super();
		this.traite=new ArrayList<Class<?>>();
		this.imports=new ArrayList<Class<?>>();
	}
	
	protected StringBuilder generer(Class<?> c) throws IOException{
		StringBuilder sb=new StringBuilder();
		sb.append(lesImports(c));
		sb.append(laClasse(c));
		
		return sb;
	}
	
	protected StringBuilder laClasse(Class<?>c) throws IOException{
		StringBuilder sb=new StringBuilder();
		appendLine(sb,"public class "+c.getSimpleName()+"Factory{");
		sb.append(core(c));
		appendLine(sb, "}");
		return sb;
	}
	
	
	protected abstract StringBuilder core(Class<?>c) throws IOException;

	
	protected StringBuilder lesImports(Class<?>c){
		StringBuilder sb=new StringBuilder();
		appendLine(sb, "package "+c.getPackage().getName()+";");
		for(String s:genererListImport(c)){
			appendLine(sb, "import "+s+";"+ENDL);
		}
		appendLine(sb, makeImport(Combinators.class));
		appendLine(sb, makeImport(Enumeration.class));
		appendLine(sb, makeImport(F.class));
		appendLine(sb, makeImport(BigInteger.class));
		return sb;
	}
	
	public List<String>genererListImport(Class<?>c){
		List<FieldConstructor>list=MyIntrospection.recupererTousChampConstructorEnumerator(c);
		List<String>sortie=new ArrayList<String>();
		for(FieldConstructor fc:list){
			if(!isPrimitive(fc.getTypeChamp().getSimpleName())){
				String s=fc.getTypeChamp().getCanonicalName();
				if(!sortie.contains(s)){
					sortie.add(s);
				}
				sortie.addAll(genererListImport(fc.getTypeChamp()));
			}
		}
		return sortie;
	}
	
	public void generateFile(Class<?> c) throws IOException{
		if(!traite.contains(c)){
			traite.add(c);
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(new File("src/examples/factory/tests/"+nomClasse(c)+"Factory.java"))));
			pw.print(generer(c));
			pw.close();
		}
	}
	
	public void generate(Class<?> c) throws IOException{
		AbstractGeneratorFactory agf=null;
		if(c.getAnnotation(EnumerateStatic.class)!=null){
			System.out.println("STATIC "+c);
			agf=new StaticFactory();
		}else{
			agf=new ClassFactory();
		}
		agf.generateFile(c);
	}
	
	

	public static void appendLine(StringBuilder sb,String texte){
		sb.append(texte+ENDL);
	}
	public static String getTab(int number){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<number;i++){
			sb.append(TAB);
		}
		return sb+"";
	}
	
	/**
	 * permet d'ecrire les nom des classes, sert a transformer int en Integer ...
	 * @param c
	 * @return
	 */
	public static String nomClasse(Class<?> c){
		return traiterPrimitive(c.getSimpleName());
	}

	public static String traiterPrimitive(String canonicalName){
		String str[]={"int","long","byte","float","double","boolean","String"};
		String str2[]={INTEGER,
				LONG,
				BYTE,
				FLOAT,
				DOUBLE,
				BOOLEAN,
				STRING};
		for(int i=0;i<str.length;i++){
			String s=str[i];
			if(canonicalName!=null&&canonicalName.toUpperCase().equals(s.toUpperCase())){
				return str2[i];
			}
		}
		return canonicalName;
	}
	/**
	 * sert a savoir si le string canonicalName est le nom d'une classe primitive ou non
	 * @param canonicalName
	 * @return
	 */
	public static boolean isPrimitive(String canonicalName){
		String str[]={"int","long","byte","float","double","boolean","String"};
		for(int i=0;i<str.length;i++){
			String s=str[i];
			if(canonicalName!=null&&canonicalName.toUpperCase().equals(s.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	public String makeImport(Class c){
		return "import "+c.getCanonicalName()+";";
	}
	
}
