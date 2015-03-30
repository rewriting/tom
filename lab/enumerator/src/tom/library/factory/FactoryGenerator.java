package tom.library.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import examples.factory.Student;


/**
 * sample client for the generator api
 * it initializes Apache Velocity template engine, call the parse method on a class 
 * and output the template filled with the information from parsed class 
 * 
 * @author Ahmad
 *
 */
public class FactoryGenerator {

	public static void main(String[] args) {
		
		ParsedClass parsedClass = new ParsedClass();
		Parser.parse(Student.class, parsedClass); 
		
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		Template t = ve.getTemplate("./src/tom/library/factory/templates/FactoryTemplate.vm");
		
		VelocityContext context = new VelocityContext();
		context.put("parsedClass", parsedClass);
		
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		
		try {
			PrintWriter pw = new PrintWriter("./src/examples/factory/tests/"+parsedClass.getFactoryClassName()+".java");
			pw.print(writer.toString());
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("done");

	}

}
