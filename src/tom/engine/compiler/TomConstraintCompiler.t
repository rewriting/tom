package tom.engine.compiler;

import tom.engine.tools.TomGenericPlugin;

/**
 * Tom compiler based on constraints.
 * 
 * It controls different phases of compilation:
 * - propagation of constraints
 * - instruction generation from constraints
 * - ...   
 */
public class TomConstraintCompiler extends TomGenericPlugin {

	/**  
	 * Constructor - just passes the name to the platform 
	 */
	public TomConstraintCompiler() {
		super("TomConstraintCompiler");
	}

	/**
	 * Main function
	 */
	public void run() {
		
	}
	
}