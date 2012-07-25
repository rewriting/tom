
package tom.mapping.dsl;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class TomMappingStandaloneSetup extends TomMappingStandaloneSetupGenerated{

	public static void doSetup() {
		new TomMappingStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

