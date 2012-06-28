package tom.mapping.dsl.generator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.mwe2.runtime.workflow.Workflow;
import org.eclipse.xpand2.Generator;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xtext.ISetup;
import org.eclipse.xtext.mwe.UriBasedReader;

import tom.mapping.dsl.TomMappingStandaloneSetupGenerated;
import tom.mapping.dsl.ui.internal.TomMappingActivator;

import com.google.inject.Injector;

import fr.irisa.cairn.xtext.utils.AbstractMWE2WorkflowRunner;

public class TomMappingGenerator extends AbstractMWE2WorkflowRunner {

	private final static String slot = "tmaps";
	private final static String type = "Mapping";
	private final static String nsURI = "tom.mapping.model";
	
	private static final String defaultTargetDir = "src-gen/";
	private static final String defaultTomDir = "src-tom/";
	private static final String defaultFileEncoding = "UTF-8";
	
	private IFile file;
	
	private static class MyAdapterStandAloneSetup extends TomMappingStandaloneSetupGenerated {
		@Override
		public Injector createInjector() {
			return TomMappingActivator.getInstance().getInjector("tom.mapping.dsl.TomMapping");
		}
	}
	
	public TomMappingGenerator(IFile file) {
		this.file = file;
		new Path(getGeneratedPath()).toFile().mkdirs();
		new Path(getTomPath()).toFile().mkdirs();
	}

	@Override
	protected Workflow buildWorkflow() {
		final Workflow workflow = new Workflow();
		final UriBasedReader reader = this.getReader(TomMappingGenerator.slot, TomMappingGenerator.type, TomMappingGenerator.nsURI);
		workflow.addComponent(reader);
		
		Generator generator = getXpandGenerator("templates::Factory::main FOR tmaps", getGeneratedPath());
		generator.setPrSrcPaths(getGeneratedPath());
		generator.setPrDefaultExcludes(true);
		workflow.addComponent(generator);
		
		generator = getXpandGenerator("templates::TomTemplate::main FOR tmaps", getTomPath());
		generator.setPrSrcPaths(getTomPath());
		generator.setPrDefaultExcludes(true);
		workflow.addComponent(generator);
		
		generator = getXpandGenerator("templates::Introspector::main FOR tmaps", getGeneratedPath());
		generator.setPrSrcPaths(getGeneratedPath());
		generator.setPrDefaultExcludes(true);
		workflow.addComponent(generator);
		
		generator = getXpandGenerator("templates::MappingCustomAccessors::main FOR tmaps", getGeneratedPath());
		generator.setPrSrcPaths(getGeneratedPath());
		workflow.addComponent(generator);
		return workflow;
	}

	@Override
	protected ISetup getXtextRegister() {
		return new MyAdapterStandAloneSetup();
	}

	@Override
	protected String getGeneratedPath() {
		return file.getProject().getLocation().toOSString() + "/" + defaultTargetDir + "/";
	}
	
	protected String getTomPath() {
		return file.getProject().getLocation().toOSString() + "/" + defaultTomDir + "/";
	}

	/**
	 * Get default XPand generator
	 * 
	 * @param rule
	 * @return
	 */
	@Override
	public Generator getXpandGenerator(final String rule) {
		final Generator generator = new Generator();
		generator.setExpand(rule);
		final Outlet outlet = new Outlet();
		outlet.setOverwrite(true);
		outlet.setPath(this.getGeneratedPath());
		generator.addOutlet(outlet);
		generator.setFileEncoding(defaultFileEncoding);
		return generator;
	}
	
	public Generator getXpandGenerator(final String rule, final String path) {
		final Generator generator = new Generator();
		generator.setExpand(rule);
		final Outlet outlet = new Outlet();
		outlet.setOverwrite(true);
		outlet.setPath(path);
		generator.addOutlet(outlet);
		generator.setFileEncoding(defaultFileEncoding);
		return generator;
	}
}