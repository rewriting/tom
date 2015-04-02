package tom.library.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import examples.factory.Car;
import examples.factory.ListStack;
import examples.factory.Student;

/**
 * sample client for the generator api it initializes Apache Velocity template
 * engine, call the parse method on a class and output the template filled with
 * the information from parsed class
 * 
 * @author Ahmad
 * 
 */
public class FactoryGenerator {
    
    /**
     * path to the templates directory
     */
    private String templatePath;
    
    /**
     * path to directory where generated XFactory.java files should be saved
     */
    private String generationPath;
    
    /**
     * path to directory where compiled XFactory.class files should be saved
     */
    private String compilationPath;
    
    /**
     * initiates generator and sets all paths to default values
     * TODO: could be read from configurations file
     */
    public FactoryGenerator() {
        this.templatePath = "./src/tom/library/factory/templates/";
        this.generationPath = "./src/examples/factory/tests/";
        this.compilationPath = "./src/examples/factory/tests/";
        
    }
    
    /**
     * generate corresponding source code of factory for a class
     * @param classToGenerateFactoriesFor
     */
    public <T> void generateSources(Class<T> classToGenerateFactoriesFor) {

        ParsedClass parsedClass = Parser.parse(classToGenerateFactoriesFor);

        VelocityEngine ve = new VelocityEngine();
        ve.init();
        Template t = ve.getTemplate(templatePath + "FactoryTemplate.vm");

        VelocityContext context = new VelocityContext();
        context.put("parsedClass", parsedClass);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        try {
            PrintWriter pw = new PrintWriter(generationPath + parsedClass.getFactoryClassName()
                + ".java");
            pw.print(writer.toString());
            pw.flush();
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("sources generated");

    }
    
    public static void main(String[] args) {
        FactoryGenerator generator = new FactoryGenerator();
        generator.generateSources(ListStack.class);
        
    }

}
