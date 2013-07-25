package tomchecker;

import tomchecker.proplang.PropLangAdaptor;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.tree.Tree;

import javax.tools.JavaCompiler;
import java.lang.reflect.Method;
import javax.tools.ToolProvider;

import java.io.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import java.util.HashMap; 

import org.kohsuke.args4j.*;

import tom.library.sl.VisitFailure;

import tomchecker.proplang.types.*;

public class Translator{

	%include{ proplang/PropLang.tom }

	%include { sl.tom }
	%typeterm Map {
		implement      { java.util.Map<String,String> }
		is_sort(t)      { $t instanceof java.util.Map }
		equals(l1,l2)  { $l1.equals($l2) }
	}

	%strategy CollectVariables(bag:Map) extends Identity() {
		visit Expr {
			Var(name,type) -> {
				if(bag.get(`name) != null) {
					if(!bag.get(`name).equals(`type)) {
						throw new RuntimeException("Same variable with two different types :  " + `name);
					}
				}else {
					bag.put(`name,`type);
				}
			}
		}
	}

	/**
	* Compute the set of distinct variables occuring in a given Property.
	* @param p a Property that may contain some variables
	* @return a Map(name:type) of all Variables occuring in p
	*/
	public static Map<String,String> variableSet(Property p) {
		Map<String,String> bag = new HashMap<String,String>();
		try {
			`BottomUp(CollectVariables(bag)).visitLight(p);
		} catch(VisitFailure e) { }
		return bag;
	}

	public static String generateMethods(PropList properties, int index, Map<String,String> methodCalls) {
    StringBuffer methods = new StringBuffer();

    %match (properties){
      PropList(property,t*) -> {  
        methods.append(generateMethod(`property, ++index));        
        methodCalls.put("checkProperty" + index, prettyPrint(`property, null));
        if(!`t.isEmptyPropList()){
          methods.append(generateMethods(`t, index, methodCalls));        
        }
      }
    }
		return new String(methods);
	}


	public static String generateMethod(Property property,int index) {
    Map<String,String> bag = variableSet(property);
    Map<String,String> projs = new HashMap<String,String>();
    int i = 1;
    for(String var: bag.keySet()){
      projs.put(var, "proj" + (i++));
    }
    StringBuffer product = generateProductType(bag);
		return %[
              //@`prettyPrint(property,null)@
              public static void checkProperty@index@(){
            	  Set<F< @product@, Boolean>> condBag = new HashSet<F< @product@, Boolean>>();
                   @generateCode(`property,bag,projs,0)@
              };

              ]%;
 	}


	public static String generateCode(Property property, Map<String,String> bag, Map<String,String> projs, int index) {
    String code = "";	
    Boolean existential = false;
    %match(property) {
	  Check(c) -> { code += generateFClass(`c,bag,projs,0); }  
      Implies(c, p) -> { return  generateFClass(`c,bag,projs,++index) + "\n\n\t\t" + generateCode(`p,bag,projs,index); }
      Exists(e, p) -> {  existential = true; return generateCode(`p,bag,projs,index); }  
	}
		return code + "\n" + generateEnumerations(bag, existential);
	}

    // Generate the function corresponding to the property
	public static String generateFClass(Cond cond, Map<String,String> bag, Map<String,String> projs, int index) {
    // function profile
    StringBuffer product = generateProductType(bag);
    // function name
    String funName = index == 0 ? " prop" : " cond" + index;

    StringBuffer result = new StringBuffer("");
    //pretty comment: e.g. swap(swap)) = e 
    result.append(%[
                   F< @product@, Boolean> @funName@ = new F< @product@, Boolean>() {
                        public Boolean apply(@product@ args) {
                          return @`prettyPrint(cond, projs)@;
                        }
                   };]%);
     if(!funName.contains("prop")) {
    	result.append(%[
    	                condBag.add(@funName@);]%);
    }              

    return new String(result);
	}


    // Generate the enumerations for the property and the corresponding check calls
	public static String generateEnumerations(Map<String,String> bag, boolean existential) {
    int numOfType = bag.size();
    StringBuffer product = generateProductType(bag);

    StringBuffer result = new StringBuffer("");

    // Declaration of enumeration
    result.append(%[
                   Enumeration< @product@ > params = @product.substring(0,8)@.enumerate(]%);
    // Generate the enumerations
    for(String var: bag.keySet()){
      if(bag.get(var).compareTo("String") == 0){
        result.append(%[
                        Combinators.makeString(),]%);
      } else if(bag.get(var).compareTo("Integer") == 0 || bag.get(var).compareTo("int") == 0){
        result.append(%[
                        Combinators.makeInt(),]%);
      }else if(bag.get(var).compareTo("Boolean") == 0 || bag.get(var).compareTo("boolean") == 0){
        result.append(%[
                        Combinators.makeBoolean(),]%);
      }else if(bag.get(var).compareTo("Character") == 0 || bag.get(var).compareTo("char") == 0){
        result.append(%[
                        Combinators.makeCharacter(),]%);
      }else {
    	  result.append(%[
                        @bag.get(var)@.getEnumeration(),]%);
      } 
    }
    if(numOfType == 0){ // if no arguments then fake String
      result.append(%[
                         Enumeration.singleton(new String()),]%);
      numOfType = 1;
    }
    result.replace(result.length()-1, result.length(),");");

    int verbose = options.v;
    int shrink = options.s;
    int numberOfTest = options.numOfTest;
    int quotient = options.q;
    //e.g. quickCheck(15, enumeration1, enumeration2 etc.)
    if(options.quick){
      result.append(%[                        
                   try{
                     System.out.println("--Random Check--");
                     Checker.quickCheckProd@numOfType@(@options.level@, params, prop, @verbose@, @shrink@, @quotient@, condBag, BigInteger.valueOf(@numberOfTest@));
                   }catch(AssertionError err){
                     System.out.println(err.getMessage());
                   } ]%);
    }
    if(options.small){
      result.append(%[                        
                   try{
                     System.out.println("--Exhaustive Check--");
                     Checker.smallCheckProd@numOfType@(@options.level@, params, prop, @verbose@, condBag, @existential@);
                   }catch(AssertionError err){
                     System.out.println(err.getMessage());
                   }
                    ]%);
    }
    return new String(result);
	}

  // Generate the  product type
  public static StringBuffer generateProductType(Map<String,String> bag) {
    int numOfType = bag.size();

    StringBuffer product = new StringBuffer("Product");
    product.append(numOfType == 0 ? 1 : numOfType);
    product.append("<");
    for(String var: bag.keySet()){
      product.append(bag.get(var) + ",");
		}
    if(numOfType == 0){// if no parameters fake a String one
      product.append("String"+",");
    }
    product.replace(product.length()-1, product.length(),">");

    return product;
  }


	/**
	* Pretty-print the property
	* @param prop the Property to be printed
	* @param code if true print the corresponding Java; if false print human-readable text
	* @return the corresponding String 
	*/
	public static String prettyPrint(PropList propList, Map<String,String> projs) { 
		%match(propList) {
			PropList(p) -> { return prettyPrint(`p,projs); }  
			PropList(p,l*) -> { return prettyPrint(`p,projs) +"\n     "+prettyPrint(`l,projs); }  
		}
		return ""; 
	}

	/**
	* Pretty-print the property
	* @param prop the Property to be printed
	* @param code if true print the corresponding Java; if false print human-readable text
	* @return the corresponding String 
	*/
	public static String prettyPrint(Property prop, Map<String,String> projs) { 
		%match(prop) {
			Check(c) -> { return prettyPrint(`c,projs); }  
			Implies(c, p) -> { return prettyPrint(`c,projs) + " ==> ( " + prettyPrint(`p,projs) + " )"; }
			Exists(e, p) -> { return "Exists " + prettyPrint(`e,projs) + " . ( " + prettyPrint(`p,projs) + " )"; }
		}
		return ""; 
	}

	public static String prettyPrint(Cond c, Map<String,String> projs) { 
		%match(c) {
	      Same(e1, e2) -> { return prettyPrint(`e1,projs) + " == " + prettyPrint(`e2,projs); }  
	      Diff(e1, e2) -> { return prettyPrint(`e1,projs) + " != " + prettyPrint(`e2,projs); }  
	      Eq(e1, e2) -> { return projs == null ? prettyPrint(`e1,projs) + " EQ " + prettyPrint(`e2,projs) : prettyPrint(`e1,projs) + ".equals(" + prettyPrint(`e2,projs)+")"; }  
	      Neq(e1, e2) -> { return projs == null ? prettyPrint(`e1,projs) + " NEQ " + prettyPrint(`e2,projs) : "!"+prettyPrint(`e1,projs) + ".equals(" + prettyPrint(`e2,projs)+")"; }  
	      And(c1, c2) -> { return projs == null ? "(" + prettyPrint(`c1,projs) + " AND " + prettyPrint(`c2,projs) +")" : "("+prettyPrint(`c1,projs) + " && " + prettyPrint(`c2,projs)+")"; }  
	      Or(c1, c2) -> { return projs == null ? "(" + prettyPrint(`c1,projs) + " OR " + prettyPrint(`c2,projs) +")" : "("+prettyPrint(`c1,projs) + " || " + prettyPrint(`c2,projs)+")"; }  
	      Neg(c1) -> { return projs == null ? "NOT(" + prettyPrint(`c1,projs) +")" : "!("+prettyPrint(`c1,projs)+")"; }
	      Gre(e1,e2) -> { return prettyPrint(`e1,projs) + " > " + prettyPrint(`e2,projs); }
	      Geq(e1,e2) -> { return prettyPrint(`e1,projs) + " >= " + prettyPrint(`e2,projs); }
	      Less(e1,e2) -> { return prettyPrint(`e1,projs) + " < " + prettyPrint(`e2,projs); }
	      Leq(e1,e2) -> { return prettyPrint(`e1,projs) + " <= " + prettyPrint(`e2,projs); }  
    	}
		return ""; 
	}

	public static String prettyPrint(Expr e, Map<String,String> projs) {
		%match(e) {
      Func(name, arg) -> { return `name + "(" + prettyPrint(`arg,projs) + ")"; }
      Var(name, type) -> { 
        if(projs == null){ // if a real pretty print
          return `name + ":" + `type ; 
        } else {
          return "args." + projs.get(`name); 
        }
      }
      Null() -> { return "null"; }
      True() -> { return "true"; }
      False() -> { return "false"; }
      Len(name) -> { return `name + %[.length()]%; }
      Nat(i) -> { return Integer.toString(`i); }
    }
		return "";
	}

	public static String prettyPrint(Args arg, Map<String,String> projs) {
		%match (arg){
			Arg(e) -> { return prettyPrint(`e,projs); }
			Arg(e,t*) -> { return prettyPrint(`e,projs) + "," + prettyPrint(`t,projs); }
		}
		return "";
	}

	/**
	* Generate the program to check the property; compile and run it
	* @param property the Property to be tested
	* @param typesPath imports for types
	* @param programPath imports for functions
	*/	
	public static void propertyCheck(PropList properties, List<String> typesPath, List<String> programPath) throws IOException {
 
	//Constructing the content of check file
    String pkg = options.pkg == null ? "checker" : options.pkg;

	String checkContent = %[package @pkg@; 

import tom.library.enumerator.*;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;
import tomchecker.*;
]%;
		for(String imp: typesPath) {
			checkContent += %[import @imp@;
]%;
		}
		for(String imp: programPath) {
			checkContent += %[import @imp@;
]%;
		}
		checkContent += %[
public class Check {
                        ]%;

    // generate methods
    Map<String,String> methodCalls = new HashMap<String,String>();
		checkContent += generateMethods(properties,0,methodCalls);

		checkContent += %[
              public static void main(String args[]) {
                        System.out.println("START CHECKING");
                   ]%;

      // checkContent += generateCode(property,bag,0);
      for(String call: methodCalls.keySet()){
      checkContent += %[
                        System.out.println("====================================================");
                        System.out.println("@methodCalls.get(call)@");
                        @call@();
                        ]%;
    }
    checkContent += %[
              }
 }]%;
    	if(options.printj) {
    		System.out.println(checkContent);
    	} 
		
    //Write the generated check file
		System.out.println("CREATE " + pkg +"/Check.java");

// 		This part of the code has been replaced with the following Try/Catch block
//		Runtime runtime = Runtime.getRuntime();
//    runtime.exec("mkdir " + pkg);

		// Create the tester directory in ~/exampleQcSc/build
		try{
			boolean success = ( new File(pkg)).mkdir(); 
											if (success) { System.out.println("Directory: " + pkg + " created"); }
	
		}catch (Exception e){ // Catch exception if any
			System.err.println("Error: Failed to create directory " + pkg);
	  }
		File Check = new File(pkg +"/Check.java");
		Check.createNewFile();
		FileWriter fw = new FileWriter(Check.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(checkContent);
		fw.flush();
		bw.flush();
        
		//Compile and run the generated check file
    	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    	compiler.run(null, null, null, Check.getPath()); 
    	Process pro = Runtime.getRuntime().exec("java -ea " + pkg + "/Check");
		BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));  
		BufferedReader out = new BufferedReader(new InputStreamReader(pro.getErrorStream()));  
		String line = null;  
		while ((line = in.readLine()) != null) {  
			System.out.println(line);  
		}  
		while ((line = out.readLine()) != null) {  
			System.out.println(line);  
		}  
	}
	
 	// command line options
	protected static Options options = new Options();

	public static void main(String[] args)  {
		// command line options
		CmdLineParser optionParser = new CmdLineParser(options);
		optionParser.setUsageWidth(80);
		try {
			// parse the arguments.
			optionParser.parseArgument(args);
			if( options.help || options.h ) {
				throw new CmdLineException("Help");
			}
		} catch( CmdLineException e ) {
			// if there's a problem in the command line,
			// this will report an error message.
			System.err.println(e.getMessage());
			System.err.println("java Main [options...] arguments ...");
			// print the list of available options
			optionParser.printUsage(System.err);
			System.err.println();
			return;
		}
		// print current options
		System.out.println("OPTIONS:");
		try {
			Class c = options.getClass();
			Field[] fields = c.getDeclaredFields();
			for(Field f : fields){
				if( !f.getName().startsWith("h") ){
					System.out.print(f.getName() + ": \n     " );
					if( f.getType() != Class.forName("java.util.List") ){
						System.out.println(f.get(options));
					} else {
						for( Object s : (List)f.get(options) ) {
							System.out.println("     "+s);
						}
					}
				}
			}
		} catch (java.lang.ClassNotFoundException ec) {
			System.err.println("No class: " + ec);
		} catch (java.lang.IllegalAccessException ef) {
			System.err.println("No field: " + ef);
		}
		System.out.println("\n------------------------------------------   ");

		// Parse and test property
		try {
			InputStream fileInput = System.in;
			if(options.in != null) {
				fileInput = new FileInputStream(options.in);
			}
			PropLangLexer lexer = new PropLangLexer(new ANTLRInputStream(fileInput));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PropLangParser parser = new PropLangParser(tokens);
			//Parse the input expression 
			Tree b = (Tree) parser.top().getTree();
			PropList properties = (PropList) PropLangAdaptor.getTerm(b);
			//PropList properties = `PropList(Implies(Same(Func("MyProgram.isBigger",Arg(Func("MyProgram.size", Arg(Var("l","MyList"))),Nat(1))),True()),Check(Same(Func("MyProgram.get",Arg(Var("l","MyList"),Nat(0))),Var("v","Value")))));
			System.out.println("PROPERTY: \n     " + prettyPrint(properties,null));
			System.out.println("ABSTRACT TREE: \n    " + properties);
			if(options.printj) {
				System.out.println("Generated CODE: \n-----------------\n");
			}
			
			// if arrays are empty, then there is no import
      		// without import, there cannot be a check file
      		// --> we can have none if the tested class is in the same package
			propertyCheck(properties, options.importTypes, options.importPackage);
      		//System.out.println("pretty exist");
      		//Exist e:Foo . MyProgram.setElement(MyProgram.setElement(e:Foo,h:Hoo),h:Hoo) == MyProgram.setElement(e:Foo,h:Hoo)
      		//Property p = `Exists(Var("e","Foo"), Check(Same(Func("MyProgram.setElement", Arg(Var("e","Foo"), Var("h","Hoo"))) , Var("h","Hoo"))));
      		//System.out.println(prettyPrint(p, null));

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
