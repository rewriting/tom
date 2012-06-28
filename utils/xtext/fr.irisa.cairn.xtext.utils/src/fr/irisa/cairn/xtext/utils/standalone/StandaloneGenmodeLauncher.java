package fr.irisa.cairn.xtext.utils.standalone;

import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;


/**
 * XXX: to clean  (inheritance from Runtime Component)
 * 
 * @author antoine
 * 
 */
public class StandaloneGenmodeLauncher extends AbstractWorkflowComponent {
	private GenModel genmodel;
	private StandaloneSetupWithPluginMappings setup;

	public void checkConfiguration(Issues issues) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {

		// Globally register the default generator adapter factory for GenModel
		// elements (only needed in standalone).
		//
		GeneratorAdapterFactory.Descriptor.Registry.INSTANCE.addDescriptor(
				GenModelPackage.eNS_URI,
				GenModelGeneratorAdapterFactory.DESCRIPTOR);

		// Create the generator and set the model-level input object.
		//
		Generator generator = new Generator();
		generator.setInput(genmodel);

		// Generator model code.
		//
		generator.generate(genmodel,
				GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE,
				new BasicMonitor.Printing(System.out));
	}

	public void setGenModel(String g) {
		URI uri = URI.createURI(g);

		// resourceSet.setPackageRegistry(EPackage.Registry.INSTANCE);
		Resource res = setup.getResourceSet().getResource(uri, true);
		GenModel gm = (GenModel) res.getContents().get(0);
		genmodel = gm;
		genmodel.setForceOverwrite(false);
		genmodel.setCanGenerate(true);
		genmodel.setFacadeHelperClass(null);
	}

	public void setSetup(StandaloneSetupWithPluginMappings s) {
		this.setup = s;
	}
}
