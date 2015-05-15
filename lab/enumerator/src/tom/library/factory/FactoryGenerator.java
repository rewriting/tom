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
import examples.factory.Garage;
import examples.factory.Garage2;
import examples.factory.ListStack;
//import examples.factory.Garage;
import examples.factory.Room;
//import examples.factory.Room;
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
     * the factories that have been generated so far of the form
     * <"nameOfClassToEnumerate", "nameOfFactoryClass">
     */
    private Map<Class<?>, ParsedClass> generatedFactories;

    /**
     * factory template to be filled
     */
    private Template template;

    /**
     * initiates generator and sets all paths to default values TODO: could be
     * read from configurations file
     */
    private FactoryGenerator() {
        // init Apache Velocity
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        this.templatePath = "./src/tom/library/factory/templates/";
        this.template = ve.getTemplate(templatePath + "FactoryTemplate.vm");
        this.generationPath = "./src/examples/factory/";
        this.compilationPath = "./src/examples/factory/";
        this.generatedFactories = new HashMap<Class<?>, ParsedClass>();
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
        Queue<Class<?>> classesToProcess = new LinkedList<Class<?>>();
        classesToProcess.add(classToGenerateFactoriesFor);
        while (!classesToProcess.isEmpty()) {
            Class<?> class2process = classesToProcess.poll();
            ParsedClass parsedClass = this.parse(class2process);
            generatedFactories.put(class2process, parsedClass);
            // handle dependencies
            for (Class<?> dependency : parsedClass.getDependencies()) {
                if (!generatedFactories.containsKey(dependency)) {
                    classesToProcess.add(dependency);
                }
            }
        }
    }

    public void outputSourceForClasses() {
        // generate code using velocity
        VelocityContext context = new VelocityContext();

        // for (Class<?> class2generate : generatedFactories.keySet()) {
        for (ParsedClass parsedClass : generatedFactories.values()) {
            // ParsedClass parsedClass = generatedFactories.get(class2generate);
            context.put("parsedClass", parsedClass);

            StringWriter writer = new StringWriter();
            template.merge(context, writer);

            // save code to a file
            try {
                PrintWriter pw = new PrintWriter(generationPath
                    + parsedClass.getFactoryClassName() + ".java");
                pw.print(writer.toString());
                pw.flush();
                pw.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println("factory source generated:" + parsedClass.getCanonicalName());
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
            System.out.println(factoryClass.getCanonicalName());
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return factoryClass;
    }

    public static void main(String[] args) {
        FactoryGenerator generator = FactoryGenerator.getInstance();
        generator.generateSources(ListStack.class);
        generator.outputSourceForClasses();

    }

    /**
     * parses a class and stores parsed information into the ParsedClass
     * 
     * @param classToParse
     *            the class to be parsed
     * @return parsedClass object holding the parsed information (constructors,
     *         annotations...)
     */
    private <T> ParsedClass parse(Class<T> classToParse) {
        ParsedClass parsedClass = new ParsedClass(classToParse);
        // load all constructors having @EnumerateGenerator annotations
        for (Constructor<?> cons : classToParse.getDeclaredConstructors()) {
            if (cons.isAnnotationPresent(EnumerateGenerator.class)) {
                if (cons.getParameterTypes().length == 0) {
                    // no args cons
                    parsedClass.addNoArgsConstructor(cons);
                } else {
                    // cons with parameters
                    parsedClass.addConstructor(cons);
                }

            }
        }
        // load all methods annotated as @Enumerate
        for (Method method : classToParse.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Enumerate.class)) {
                parsedClass.addMethod(method);

            }
        }
        return parsedClass;
    }

}
