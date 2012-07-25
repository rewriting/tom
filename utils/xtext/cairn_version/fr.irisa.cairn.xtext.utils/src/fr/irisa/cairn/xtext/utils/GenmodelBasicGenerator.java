package fr.irisa.cairn.xtext.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenJDKLevel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;

import fr.irisa.cairn.ecore.tools.GenericXMIResourceManager;

public class GenmodelBasicGenerator implements IGenerator {
	private String modelPath;
	private String pluginID;
	private String basePackage;
	private URI ecore;
	private List<GenPackage> usedGenPackages;
	private ResourceSet resourceSet;

	public GenmodelBasicGenerator(String modelPath, String pluginID, String base) {
		this.modelPath = modelPath;
		this.pluginID = pluginID;
		this.basePackage = base;
		this.resourceSet = new ResourceSetImpl();
	}

	public List<GenPackage> getUsedGenPackages() {
		if (usedGenPackages == null) {
			usedGenPackages = new UniqueEList<GenPackage>();
		}
		return usedGenPackages;
		
	}

	private List<GenPackage> getUsedGenPackage(String genmodel) {
		List<GenPackage> genPackages = new ArrayList<GenPackage>();
		URI uri = getExternalURI(genmodel);

		Resource genModelResource = resourceSet.getResource(uri, true);
		GenModel gm = (GenModel) genModelResource.getContents().get(0);
		genPackages.addAll(gm.getGenPackages());

		return genPackages;
	}

	protected GenModel createGenPackage(EPackage ePackage) throws IOException {
		GenModel genModel = ecore2GenModel(ePackage);

		URI genModelURI = getProducedGenmodeURI();

	
//		ecoreResource.getContents().add(ePackage);
		GenericXMIResourceManager<GenModel> serializer = new GenericXMIResourceManager<GenModel>(GenModelPackage.eINSTANCE);
		serializer.register("genmodel");
//		Resource ecoreResource = serializer.getResourceSet().getResource(ecore,true);
		serializer.save(genModelURI.toString(), genModel);
//		Resource genModelResource = resourceSet.createResource(genModelURI);
//		genModelResource.getContents().add(genModel);
//		genModelResource.save(new HashMap<Object, Object>());
		return genModel;
	}

	private GenModel ecore2GenModel(EPackage ePackage) {

		GenModel genModel = GenModelFactory.eINSTANCE.createGenModel();
		// EcoreUtil.resolveAll(ePackage);

		genModel.setModelName(ePackage.getName());
		genModel.setValidateModel(false);
		genModel.setForceOverwrite(true);
		genModel.setCanGenerate(true);
		genModel.setFacadeHelperClass(null);
		genModel.setBundleManifest(true);
		genModel.setContainmentProxies(false);
		genModel.setComplianceLevel(GenJDKLevel.JDK50_LITERAL);
		for (GenPackage gp : getUsedGenPackages()) {
			genModel.getUsedGenPackages().add(gp);
		}
		genModel.initialize(Collections.singleton(ePackage));

		genModel.setModelDirectory(modelPath);
		genModel.setImporterID("org.eclipse.emf.importer.ecore");
		genModel.setModelPluginID(pluginID);
		GenPackage genPackage = (GenPackage) genModel.getGenPackages().get(0);
		genPackage.setBasePackage(basePackage);

		return genModel;
	}

	private URI getProducedGenmodeURI() {
		return ecore.trimFileExtension().appendFileExtension("genmodel");
	}

	private URI getExternalURI(String name) {
		return URI.createURI(name);
	}

	public void addGenPackage(String uri) {
		getUsedGenPackages().addAll(getUsedGenPackage(uri));
	}

	public URI getEcore() {
		return ecore;
	}

	public void setEcore(URI ecore) {
		this.ecore = ecore;
	}
	
	private GenModel genModel;
	public void doGenerate(Resource input, IFileSystemAccess fsa) {
		genModel = null;
		EPackage p = (EPackage) input.getContents().get(0);
		try {
			genModel = createGenPackage(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public GenModel getLastGenModel() {
		return genModel;
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}
	
	

}