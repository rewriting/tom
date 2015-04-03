package tom.library.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import examples.factory.Car;
import examples.factory.ListStack;
import examples.factory.Student;

/**
 * It initializes Apache Velocity template engine, call the parse method on a
 * class and output the template filled with the information from parsed class
 * It implements the singleton pattern
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
     * the only FactoryGenerator instance
     */
    private static FactoryGenerator generator;
    
    /**
     * the factories that have been generated so far
     * of the form <"nameOfClassToEnumerate", "nameOfFactoryClass">
     */
    private Map<String, String> generatedFactories;

    /**
     * initiates generator and sets all paths to default values TODO: could be
     * read from configurations file
     */
    private FactoryGenerator() {
        this.templatePath = "./src/tom/library/factory/templates/";
        this.generationPath = "./src/examples/factory/tests/";
        this.compilationPath = "./src/examples/factory/tests/";
        this.generatedFactories = new HashMap<String, String>();
        generatedFactories.put("examples.factory.Student", "examples.factory.tests.StudentFactory"); // for testing

    }
    
    /**
     * returning a reference to the single instance
     * @return the single FactoryGenerator instance
     */
    public static FactoryGenerator getInstance() {
        if (generator == null) {
            generator = new FactoryGenerator();
        }
        return generator;
    }

    /**
     * generate corresponding source code of factory for a class
     * 
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
    
    /**
     * gets the name of classToEnemerate, look it up in the map, and get name of corresponding factory class
     * then loads and instantiates the factory
     * @param nameOfClassToEnumerate
     * @return an instance of the corresponding factory class
     */
    public Class<?> getFactoryClass(String nameOfClassToEnumerate) {
        System.out.println("hiii");
        String factoryClassName = generatedFactories.get(nameOfClassToEnumerate);
        Object factoryInstance = null;
        Class<?> factoryClass = null;
        try {
            File compilationDir = new File(compilationPath);
            URL[] urls = new URL[]{ compilationDir.toURI().toURL() };
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);
            factoryClass = classLoader.loadClass(factoryClassName);
            System.out.println(factoryClass.getCanonicalName());
            //factoryInstance = factoryClass.newInstance();
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return factoryClass;
    }
    
    public static void main(String[] args) {
        FactoryGenerator generator = FactoryGenerator.getInstance();
        generator.generateSources(ListStack.class);

    }

}
