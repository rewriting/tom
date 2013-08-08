//TODO: I think that proposing to users which property they want to test is a good idea. Or even better, we should propose which files they want to test and afterwards, which property(ies).

//TODO: il me semble que l'on ne peut pas choisir seule la méthode smallcheck pour vérifier un fichier, soit il fait appel aux deux méthodes soit qu'à quickcheck

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

  // Carriage return
  public static final String CR = "\r";
  // Tab
  public static final String TAB = "\t";

	%include{ proplang/PropLang.tom }

	%include { sl.tom }
	%typeterm Map {
		implement      { java.util.Map<String,String> }
		is_sort(t)     { $t instanceof java.util.Map }
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


	// ---------------- Variable Set ----------------------------------------------------------------
	/*
	* Compute the set of distinct variables occuring in a given Property
	* @param p: a Property that may contain some variables
	* @return a Map(name:type) of all Variables occuring in p
	*/
	public static Map<String,String> variableSet(Property p) {
		Map<String,String> bag = new HashMap<String,String>();
		try {
			`BottomUp(CollectVariables(bag)).visitLight(p);
		} catch(VisitFailure e) { }
		return bag;
	}

	//TODO: improve indentation
	// ---------------- Generate Code ---------------------------------------------------------------
	public static String generateCode(PropList properties, int index, Map<String,String> methodCalls) {

    StringBuffer methods = new StringBuffer();

    %match (properties){
      PropList(property,t*) -> {
      	index++;
        methods.append(generateMethod(`property, index));
        //TODO: maybe I can modify prettyPrint, and remove the second parameter
        methodCalls.put("checkProperty" + index, prettyProperty(`property, null));
        if(!`t.isEmptyPropList()){
          methods.append(generateCode(`t, index, methodCalls));
        }
      }
    }
		return methods.toString();
	}

	// ---------------- Generate Method -------------------------------------------------------------
	public static String generateMethod(Property property, int index) {
	
    Map<String,String> varSet = variableSet(property);
    Map<String,String> projs = new HashMap<String,String>();
    int i = 1;
    for(String varName: varSet.keySet()){
      //TODO: maybe I can delete projs. It contains a set view of the keys contained in bag
	  	//TODO : projs is not a good name
      projs.put(varName, "proj" + (i));
	  	i++;
    }

    String product = generateProductType(varSet);

		return
			%[
				@CR+TAB@ //@`prettyProperty(property,null)@
				@CR+TAB@ public static void checkProperty@index@(){
				@CR+TAB+TAB@ Set<F< @product@, Boolean>> condBag = new HashSet<F< @product@, Boolean>>();
				@CR+TAB+TAB@ @generateBodyMethod(`property, varSet, projs, 0, product)@
			}; 
			]%;
 	}

	// ---------------- Generate Body Method --------------------------------------------------------
	public static String generateBodyMethod(Property property, Map<String,String> varSet,
																					Map<String,String> projs, int index, String product) {

    String body = "";
    //TODO: it seems that existential is never used, because even if it's set to true, it never reaches the last return. Therefore, maybe I can delete the second parameter from generateEnumerations function
    Boolean existential = false;
    
    %match(property) {
			Check(c) -> {
				body += generateFClass(`c, varSet, projs, 0, product);
			}
		  Implies(c,p) -> {
		  	index++;
	  		return  generateFClass(`c, varSet, projs, index, product) +"\n\n\t\t"+
	  						generateBodyMethod(`p, varSet, projs, index, product);
  		}
		  Exists(_,p) -> {
		  	existential = true;
		  	return generateBodyMethod(`p, varSet, projs, index, product);
	  	}
		}
		return body + "\n" + generateEnumerations(varSet, existential, product);
	}

	// ---------------- Generate FClass ---------------------------------------------------------------
  // Generate the function corresponding to the property
	public static String generateFClass(Cond cond, Map<String,String> bag, Map<String,String> projs,
																			int index, String product) {

    // Function name
    String funcName = index == 0 ? " prop" : " cond" + index;

    StringBuffer result = new StringBuffer("");

    // Pretty comment: e.g. swap(swap)) = e
    result.append(
			%[ @CR+TAB+TAB@ F< @product@, Boolean> @funcName@ = new F< @product@, Boolean>() {
				 @CR+TAB+TAB+TAB@ public Boolean apply(@product@ args) {
				 @CR+TAB+TAB+TAB+TAB@ return @`prettyCond(cond, projs)@;
				 @CR+TAB+TAB+TAB@ }
				 @CR+TAB+TAB@ };
			]%
   	);

    if(!funcName.contains("prop")) {
    	result.append(
    		%[ @CR+TAB+TAB@ condBag.add(@funcName@); ]%
      );
    }

    return result.toString();
	}

	// ---------------- Generate Enumerations -------------------------------------------------------
  // Generate the enumerations for the property and the corresponding check calls
	public static String generateEnumerations(Map<String,String> varSet, boolean existential,
																						String product) {

    int numOfType = varSet.size();
    StringBuffer result = new StringBuffer("");

    // Declaration of enumeration
    result.append(
    	%[ @CR+TAB+TAB@ Enumeration< @product@ > params = @product.substring(0,8)@.enumerate(]%
   	);
   	
    // Generate the enumerations
    for(String var: varSet.keySet()) {
    	String type = varSet.get(var);
      if(type.compareTo("String") == 0){
        result.append(
        	%[ Combinators.makeString(),]%);
      }else if(type.compareTo("Integer") == 0 || type.compareTo("int") == 0) {
        result.append(
        	%[ Combinators.makeInt(),]%);
      }else if(type.compareTo("Boolean") == 0 || type.compareTo("boolean") == 0) {
        result.append(
        	%[ Combinators.makeBoolean(),]%);
      }else if(type.compareTo("Character") == 0 || type.compareTo("char") == 0) {
        result.append(
        	%[ Combinators.makeCharacter(),]%);
      }else {
    	  result.append(
        	%[ @type@.getEnumeration(),]%);
      }
    }

    // If no arguments then fake String
    if(numOfType == 0){
      result.append(
	    	%[ Enumeration.singleton(new String()),]%);
      numOfType = 1;
    }

    int end = result.length();
    int start = end-1;
    result.replace(start, end,");");

    int verbose = options.v;
    int shrink = options.s;
    int numberOfTest = options.numOfTest;
    int quotient = options.q;

    //e.g. quickCheck(15, enumeration1, enumeration2 etc.)
    if(options.quick){
      result.append(%[
				@CR+TAB+TAB+TAB@ try{
					@CR+TAB+TAB+TAB+TAB@ System.out.println("--Random Check--");
					@CR+TAB+TAB+TAB+TAB@ Checker.quickCheckProd@numOfType@(@options.level@, params, prop, @verbose@, @shrink@, @quotient@, condBag, BigInteger.valueOf(@numberOfTest@));
				@CR+TAB+TAB+TAB@ }catch(AssertionError err){
					@CR+TAB+TAB+TAB+TAB@ System.out.println(err.getMessage());
				@CR+TAB+TAB+TAB@ } ]%);
    }
    
    if(options.small){
      result.append(%[                        
				@CR+TAB+TAB+TAB@ try{
					@CR+TAB+TAB+TAB+TAB@ System.out.println("--Exhaustive Check--");
					@CR+TAB+TAB+TAB+TAB@ Checker.smallCheckProd@numOfType@(@options.level@, params, prop, @verbose@, condBag, @existential@);
				@CR+TAB+TAB+TAB@ }catch(AssertionError err){
				@CR+TAB+TAB+TAB+TAB@ System.out.println(err.getMessage());
				@CR+TAB+TAB+TAB@ } ]%);
    }
    return result.toString();
	}

	// ---------------- Generate Product Type -------------------------------------------------------
  public static String generateProductType(Map<String,String> varSet) {
    int numOfType = varSet.size();

    StringBuffer product = new StringBuffer("Product");
    product.append(numOfType == 0 ? 1 : numOfType);
    product.append("<");
    for(String var: varSet.keySet()){
      product.append(varSet.get(var) + ",");
		}
    if(numOfType == 0){// if no parameters fake a String one
      product.append("String"+",");
    }
    product.replace(product.length()-1, product.length(),">");

    return product.toString();
  }


	// ---------------- Pretty Print PropList -------------------------------------------------------
	/**
	* Pretty-print the property
	* @param prop: the Property to be printed
	* @param code: if true, print the corresponding Java; if false print human-readable text
	* @return the corresponding String
	*/
	public static String prettyPropList(PropList propList, Map<String,String> projs) {
		%match(propList) {
			PropList(p) -> { return prettyProperty(`p,projs); }
			PropList(p,l*) -> { return prettyProperty(`p,projs) + "\n\t" + prettyPropList(`l,projs); }
		}
		return "";
	}

	// ---------------- Pretty Print Property -------------------------------------------------------
	public static String prettyProperty(Property property, Map<String,String> projs) {
		%match(property) {
			Check(cond) -> {
				return prettyCond(`cond,projs);
			}
			Implies(cond, prop) -> {
				return prettyCond(`cond,projs) + " ==> ( " + prettyProperty(`prop,projs) + " )";
			}
			Exists(exp, prop) -> {
				return "Exists " + prettyExpr(`exp,projs) + " . ( " + prettyProperty(`prop,projs) + " )";
			}
		}
		return "";
	}

	// ---------------- Pretty Print Cond -----------------------------------------------------------
	public static String prettyCond(Cond c, Map<String,String> projs) { 
		%match(c) {
		
      Same(e1, e2) -> {
      	return prettyExpr(`e1,projs) + " == " + prettyExpr(`e2,projs);
  		}
  		
      Diff(e1, e2) -> {
      	return prettyExpr(`e1,projs) + " != " + prettyExpr(`e2,projs);
    	}
    	
      Eq(e1, e2) -> {
      	if(projs == null){
      		return prettyExpr(`e1,projs) + " EQ " + prettyExpr(`e2,projs);
      	}else{
      		return prettyExpr(`e1,projs) + ".equals(" + prettyExpr(`e2,projs)+")";
      	}
    	}
    	
      Neq(e1, e2) -> {
				if(projs == null){
					return prettyExpr(`e1,projs) + " NEQ " + prettyExpr(`e2,projs);
				}else{
					return "!"+prettyExpr(`e1,projs) + ".equals(" + prettyExpr(`e2,projs)+")";
				}
    	}
    	
      And(c1, c2) -> {
				if(projs == null){
					return "(" + prettyCond(`c1,projs) + " AND " + prettyCond(`c2,projs) + ")";
				}else{
					return "(" + prettyCond(`c1,projs) + " && " + prettyCond(`c2,projs) + ")";
				}
    	}
    	
      Or(c1, c2) -> {
				if(projs == null){
					return "(" + prettyCond(`c1,projs) + " OR " + prettyCond(`c2,projs) + ")" ;
				}else{
					return "(" +prettyCond(`c1,projs) + " || " + prettyCond(`c2,projs) + ")";
				}
  		}
  		
      Neg(c1) -> {
				if(projs == null){
					return "NOT(" + prettyCond(`c1,projs) + ")";
				}else{
					return "!(" + prettyCond(`c1,projs) + ")" ;
				}
    	}
    	
      Gre(e1,e2) -> {
      	return prettyExpr(`e1,projs) + " > " + prettyExpr(`e2,projs);
    	}
    	
      Geq(e1,e2) -> {
      	return prettyExpr(`e1,projs) + " >= " + prettyExpr(`e2,projs);
    	}
    	
      Less(e1,e2) -> {
      	return prettyExpr(`e1,projs) + " < " + prettyExpr(`e2,projs);
    	}
    	
      Leq(e1,e2) -> {
      	return prettyExpr(`e1,projs) + " <= " + prettyExpr(`e2,projs);
    	}
      	
  	}
		return "";
	}

	// ---------------- Pretty Print Expr -----------------------------------------------------------
	public static String prettyExpr(Expr e, Map<String,String> projs) {
		%match(e) {
      Func(name, arg) -> {
      	return `name + "(" + prettyArgs(`arg,projs) + ")";
    	}
      Var(name, type) -> {
	      // if a real pretty print
        if(projs == null){
          return `name + ":" + `type ; 
        } else {
          return "args." + projs.get(`name); 
        }
      }
      Null() -> { return "null"; }
      True() -> { return "true"; }
      False() -> { return "false"; }
      Len(name) -> { return `name + %[.length()]% ; }
      Nat(i) -> { return Integer.toString(`i); }
    }
		return "";
	}

	// ---------------- Pretty Print Args -----------------------------------------------------------
	public static String prettyArgs(Args arg, Map<String,String> projs) {
		%match (arg){
			Arg(e) -> { return prettyExpr(`e,projs); }
			Arg(e,t*) -> { return prettyExpr(`e,projs) + "," + prettyArgs(`t,projs); }
		}
		return "";
	}

	// ---------------- Property Check --------------------------------------------------------------
	/*
	* Generate the program to check the property; compile and run it
	* @param property the Property to be tested
	* @param typesPath imports for types
	* @param programPath imports for functions
	*/	
	public static void propertyCheck(PropList properties, List<String> typesPath,
																	 List<String> programPath) throws IOException {
 
		//Constructing the content of check file
    String pkg = options.pkg == null ? "testedFile" : options.pkg;

		//TODO: it's a good idea to retrieve the imports from the original tom file, for each program
		String testingContent = 
			%[ @CR@ package @pkg@;
				 @CR@ import tom.library.enumerator.*;
				 @CR@ import java.util.Random;
				 @CR@ import java.util.HashSet;
				 @CR@ import java.util.Set;
				 @CR@ import java.math.BigInteger;
				 @CR@ import tomchecker.*; ]%;

		for(String imp: typesPath) {
			testingContent +=
				%[ @CR@ import @imp@;]%;
		}

		for(String imp: programPath) {
			testingContent +=
				%[ @CR@ import @imp@; ]%;
		}

		testingContent += %[
			@CR@ public class Testing { ]%;

    // Generate code
    Map<String,String> methodCalls = new HashMap<String,String>();
		testingContent += generateCode(properties,0,methodCalls);

		testingContent += %[ 
			@CR+TAB@ public static void main(String args[]) {
			@CR+TAB+TAB@ System.out.println("START CHECKING"); ]%;

    for(String call: methodCalls.keySet()){
		  testingContent += %[ 
				@CR+TAB+TAB@ System.out.println("====================================================");
				@CR+TAB+TAB@ System.out.println("@methodCalls.get(call)@");
				@CR+TAB+TAB@ @call@(); ]%;
  	}

    testingContent += %[
			@CR+TAB@ }
		 	@CR@ }
	 	]%;

		// Print generated code
  	if(options.printj) {
  		System.out.println(testingContent);
  	}
		
    // Start to write the generated code to Testing.java file
		System.out.println("CREATE " + pkg +"/Testing.java");

		// Create the tested directory in ../../build
		try{
			boolean success = ( new File(pkg)).mkdir();
			if (success) {
				System.out.println("Directory: " + pkg + " created");
			}
		}catch (Exception e){
			System.err.println("Error: Failed to create directory " + pkg);
	  }
	  
	  // Efficient way of writing to a file
		File testing = new File(pkg +"/Testing.java");
		testing.createNewFile();
		FileWriter fw = new FileWriter(testing.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(testingContent);
		fw.flush();
		bw.flush();
		fw.close();
		bw.close();
        
		// Compile and run the generated testing file
  	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
  	int compilationResult = compiler.run(null, null, null, testing.getPath());

  	if(compilationResult == 0){
	    System.out.println("Compilation is successful");
    	Process proc = Runtime.getRuntime().exec("java -ea " + pkg + "/Testing");

    	// Get testing file output
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader out = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			while ((line = out.readLine()) != null) {
				System.out.println(line);
			}
    }else{
	    System.out.println("Compilation Failed");
    }

	}

	// ---------------- Command Line Options --------------------------------------------------------
	protected static Options options = new Options();

	// ---------------- Main ------------------------------------------------------------------------
	public static void main(String[] args)  {

		// command line options
		CmdLineParser optionParser = new CmdLineParser(options);
		optionParser.setUsageWidth(80);

		System.out.println("\n------------------------------------------");

		try {
			// Parse the arguments
			optionParser.parseArgument(args);

			if( options.help || options.h ) {
				throw new CmdLineException("Help");
			}
      
		}catch( CmdLineException e ) {
			// If there's a problem in the command line, this will report an error message.
			System.err.println(e.getMessage());
			System.err.println("java Main [options...] arguments ...");
			// Print the list of available options
			optionParser.printUsage(System.err);
			System.err.println();
			return;
		}

		// Print current options
		System.out.println("OPTIONS:");
		try {
			Class c = options.getClass();
			Field[] fields = c.getDeclaredFields();
			for(Field f : fields){
				if( !f.getName().startsWith("h") ){
					System.out.print(f.getName() + ":\n\t" );
					if( f.getType() != Class.forName("java.util.List") ){
						System.out.println(f.get(options));
					}else {
						for( Object s : (List)f.get(options) ) {
							System.out.println(s);
						}
					}
				}
			}
		} catch (java.lang.ClassNotFoundException ec) {
			System.err.println("No class: " + ec);
		} catch (java.lang.IllegalAccessException ef) {
			System.err.println("No field: " + ef);
		}
		
		System.out.println("\n------------------------------------------");

		// Parse and test property
		try {
			InputStream fileInput = System.in;

			if(options.in != null) {
				fileInput = new FileInputStream(options.in);
			}
			
			PropLangLexer lexer = new PropLangLexer(new ANTLRInputStream(fileInput));
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PropLangParser parser = new PropLangParser(tokens);

			// Parse the input expression 
			Tree b = (Tree) parser.top().getTree();
			PropList properties = (PropList) PropLangAdaptor.getTerm(b);

			System.out.println("PROPERTY:\n\t" + prettyPropList(properties,null));
			System.out.println("ABSTRACT TREE:\n\t" + properties);
			if(options.printj) {
				System.out.println("Generated CODE:\n---------------------------");
			}
			
			/* 
			* If arrays are empty, then there is no import. Without import, there cannot be a check file
			* We can have none if the tested class is in the same package
  		*/ 
			propertyCheck(properties, options.importTypes, options.importPackage);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
