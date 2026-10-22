

import java.lang.reflect.Method;

import javax.swing.JApplet;

import Conf.ConfString;

/**
 * Classe qui exécute la classe créer sur la machine client.
 * @author cynthia
 */
public class AppletExecutionTom extends JApplet{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2502027947276580008L;


	/**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    @Override
    public void init() {
    	//myThread = new Thread(this);
        Console cons = new Console();
        this.add(cons);
    }
    
    @Override
    public void start() {
        String name = getParameter(ConfString.nameClass);    
        if (name != null) {
            try {
                Class<?> nameclass = Class.forName(name);
                Method m = nameclass.getDeclaredMethod("main", new Class[]{String[].class});
                m.invoke(null, new Object[]{new String[]{}});
                
            } catch (Exception ex) {
            	System.err.println("Probleme de chargement de votre class. Impossible d'executer le code");
            	ex.printStackTrace(System.err);
            }
        } else {
            ExceptionApplet ea = new ExceptionApplet();
            System.err.println(ea.getMessage());
        }
    }


// TODO overwrite start(), stop() and destroy() methods
    
}
