package tom.library.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import examples.factory.Car;
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.ListStack;
//import examples.factory.Garage;
import examples.factory.Room;
//import examples.factory.Room;
import examples.factory.Student;
import examples.factory.handwritten.recursive1.Friend;
import examples.factory.handwritten.recursive3.User;
import examples.factory.inheritance.*;

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
     * the factories that have been generated so far of the form
     * <"nameOfClassToEnumerate", "nameOfFactoryClass">
     */
    private Map<Class<?>, EnumerableType> generatedFactories;

    /**
     * velocity Engine
     */
    private VelocityEngine velocityEngine;

    /**
     * initiates generator and sets all paths to default values TODO: could be
     * read from configurations file
     */
    private FactoryGenerator() {
        // init Apache Velocity
        velocityEngine = new VelocityEngine();
        velocityEngine.init();

        this.templatePath = "./src/tom/library/factory/templates/";
        this.generationPath = "./src/examples/factory/";
        this.compilationPath = "./src/examples/factory/";
        this.generatedFactories = new HashMap<Class<?>, EnumerableType>();
    }

    /**
     * returning a reference to the single instance
     * 
     * @return the single FactoryGenerator instance
     */
    public static FactoryGenerator getInstance() {
        if (generator == null) {
            generator = new FactoryGenerator();
        }
        return generator;
    }

    /**
     * generates source code files for factories starting from the specified
     * class then its dependencies
     * 
     * @param classToGenerateFactoriesFor
     *            entry point for factories generation
     */
    public <T> void generateSources(Class<T> classToGenerateFactoriesFor) {
        Queue<EnumerableType> typesToEnumerate = new LinkedList<EnumerableType>();
        typesToEnumerate.add(new EnumerableType(classToGenerateFactoriesFor, null));
        while (!typesToEnumerate.isEmpty()) {
            EnumerableType enumerableType = typesToEnumerate.poll();
            enumerableType.parse();
            generatedFactories.put(enumerableType.getTypeClass(), enumerableType);
            // handle dependencies
            for (EnumerableType dependency : enumerableType.getDependencies()) {
                if (!generatedFactories.containsKey(dependency.getTypeClass())) {
                    typesToEnumerate.add(dependency);
                }
            }
        }
    }

    public void outputSourceForClasses() {
        // generate code using velocity
        VelocityContext context = new VelocityContext();

        for (EnumerableType enumerableType : generatedFactories.values()) {
            
            StringWriter writer = new StringWriter();
            String templateName = null;
            switch (enumerableType.getDependencyType()) {
                case SIMPLE:
                    templateName = templatePath + "SimpleTypeFactory.vm";
                    context.put("parsedClass", enumerableType.getParsedClass());
                    break;
                case SUPERTYPE:
                    templateName = templatePath + "SuperTypeFactory.vm";
                    context.put("enumerableType", enumerableType);
                    break;
                case RECURSIVE:
                    templateName = templatePath + "RecursiveTypeFactory.vm";
                    context.put("parsedClass", enumerableType.getParsedClass());
                    break;
                case MUTUALLY_RECURSIVE:
                    templateName = templatePath + "MutuallyRecursiveTypeFactory.vm";
                    context.put("enumerableType", enumerableType);
                    context.put("parsedClass", enumerableType.getParsedClass());
                    break;
            }
            Template template = velocityEngine.getTemplate(templateName);
            template.merge(context, writer);

            // save code to a file
            try {
                PrintWriter pw = new PrintWriter(generationPath
                    + enumerableType.getFactoryName() + ".java");
                pw.print(writer.toString());
                pw.flush();
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("factory generated for: " + enumerableType.getCanonicalName());
        }
    }

    /**
     * gets the name of classToEnemerate, look it up in the map, and get name of
     * corresponding factory class then loads and instantiates the factory
     * 
     * @param nameOfClassToEnumerate
     * @return an instance of the corresponding factory class
     */
    public Class<?> getFactoryClass(String nameOfClassToEnumerate) {
        // String factoryClassName = generatedFactories.get(nameOfClassToEnumerate);
        String factoryClassName = nameOfClassToEnumerate + "Factory";
        Class<?> factoryClass = null;
        try {
            File compilationDir = new File(compilationPath);
            URL[] urls = new URL[] { compilationDir.toURI().toURL() };
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);
            factoryClass = classLoader.loadClass(factoryClassName);
            System.out.println("class loaded: " + factoryClass.getCanonicalName());
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return factoryClass;
    }

    public static void main(String[] args) {
        FactoryGenerator generator = FactoryGenerator.getInstance();
        generator.generateSources(User.class);
        generator.outputSourceForClasses();

    }

}
