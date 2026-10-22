





import java.io.PrintStream;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cynthia
 */

public class System {
    public static PrintStream 	err = java.lang.System.err;
    public static PrintStream 	out = java.lang.System.out;

    public static void setErr(PrintStream 	e){
        err=e;
    }

    public static void setOut(PrintStream 	o){
        out=o;
    }
    
    public static void exit(){
    	System.exit();
    }
    
    

}
