package fr.irisa.cairn.xtext.utils.standalone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
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
public class StandaloneGenmodelGenerator extends AbstractWorkflowComponent {
	private String modelDir;
	private String ecore;
	private String genmodel;
	private String basePackage;
	private String prefix;
	private StandaloneSetupWithPluginMappings setup;

	public StandaloneSetupWithPluginMappings getSetup() {
		return setup;
	}

	public String getModelDir() {
		return modelDir;
	}

	public void setModelDir(String modelDir) {
		this.modelDir = modelDir;
	}

	public void setSetup(StandaloneSetupWithPluginMappings setup) {
		this.setup = setup;
	}

	private List<GenPackage> usedGenPackages;

	public void checkConfiguration(Issues issues) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		// Get the resource
		try {

			Resource resource = setup.getResourceSet().getResource(
					getFileURI(ecore), true);
			EPackage p = (EPackage) resource.getContents().get(0);
			createGenPackage(p, basePackage, prefix);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private URI getFileURI(String file) {
		return URI.createURI(file);
	}

	public String getEcore() {
		return ecore;
	}

	public void setEcore(String ecore) {
		this.ecore = System.getProperty("user.dir") + "/" + ecore;
	}

	public String getGenmodel() {
		return genmodel;
	}

	public void setGenmodel(String genmodel) {
		this.genmodel = genmodel;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void addGenPackage(String p) {
		if (EcorePlugin.getWorkspaceRoot() == null) {
			p = p.replaceAll("/plugin/", "/resource/");
		}
		getUsedGenPackages().addAll(getUsedGenPackage(p));
	}

	protected List<GenPackage> getUsedGenPackages() {
		if (usedGenPackages == null) {
			usedGenPackages = new UniqueEList<GenPackage>();
		}
		return usedGenPackages;
	}

	protected List<GenPackage> getUsedGenPackage(String genmodel) {
		List<GenPackage> genPackages = new ArrayList<GenPackage>();
		URI uri = getFileURI(genmodel);

		Resource genModelResource = setup.getResourceSet().getResource(uri,
				true);
		GenModel gm = (GenModel) genModelResource.getContents().get(0);
		genPackages.addAll(gm.getGenPackages());
		return genPackages;
	}

	private GenPackage createGenPackage(EPackage ePackage, String basePackage,
			String prefix) throws IOException {
		GenModel genModel = ecore2GenModel(ePackage, basePackage, prefix);

		URI ecoreURI = setup.getResourceSet().getURIConverter()
				.normalize(getFileURI(ecore));
		URI genModelURI = ecoreURI.trimFileExtension().appendFileExtension(
				"genmodel");

		Resource ecoreResource = setup.getResourceSet()
				.createResource(ecoreURI);
		ecoreResource.getContents().add(ePackage);

		Resource genModelResource = setup.getResourceSet().createResource(
				genModelURI);
		genModelResource.getContents().add(genModel);
		genModelResource.save(new HashMap<Object, Object>());
		return (GenPackage) genModel.getGenPackages().get(0);
	}

	private GenModel ecore2GenModel(EPackage ePackage, String basePackage,
			String prefix) {

		GenModel genModel = GenModelFactory.eINSTANCE.createGenModel();
		// EcoreUtil.resolveAll(ePackage);

		genModel.setModelName(ePackage.getName());
		genModel.setValidateModel(false);
		genModel.setForceOverwrite(true);
		genModel.setCanGenerate(true);
		genModel.setFacadeHelperClass(null);
		genModel.setBundleManifest(true);
		genModel.setUpdateClasspath(false);
		genModel.setContainmentProxies(false);
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		genModel.setModelDirectory(modelDir);
		for (GenPackage gp : getUsedGenPackages()) {
			genModel.getUsedGenPackages().add(gp);
		}
		genModel.initialize(Collections.singleton(ePackage));
		GenPackage genPackage = (GenPackage) genModel.getGenPackages().get(0);

		if (basePackage != null) {
			genPackage.setBasePackage(basePackage);
		}
		if (prefix != null) {
			genPackage.setPrefix(prefix);
		}
		

		return genModel;
	}
}
