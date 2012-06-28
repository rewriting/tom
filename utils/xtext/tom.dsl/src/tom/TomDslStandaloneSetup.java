
package tom;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class TomDslStandaloneSetup extends TomDslStandaloneSetupGenerated{

	public static void doSetup() {
		new TomDslStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	// contributed by org.eclipse.xtext.generator.parser.antlr.XtextAntlrGeneratorFragment
	public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
		return tom.lexer.CustomTomMappingLexer.class;
	}

}

