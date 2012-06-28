 package fr.irisa.cairn.xtext.utils;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.ISetup;

import com.google.inject.Injector;

import fr.irisa.cairn.ecore.tools.GenericXMIResourceManager;

/**
 * Generic XText DSL model load/save utility.
 * 
 * @see GenericXMIResourceManager
 * @author antoine
 * 
 * @param <Model>
 *            Root model type
 */
public class GenericXtextResourceManager<Model extends EObject> extends
		GenericXMIResourceManager<Model> {
	private ISetup setup;

	public GenericXtextResourceManager(ISetup setup, EPackage... packages) {
		super(packages);
		this.setup = setup;

	}

	public GenericXtextResourceManager(ISetup setup, List<EPackage> packages) {
		super(packages);
		this.setup = setup;
	}

	/**
	 * Register a supported Xtext DSL.
	 * 
	 * @param setup
	 */
	public void registerXtextDSL(ISetup setup) {
		setup.createInjectorAndDoEMFRegistration();
	}

	@Override
	public Model load(String file) {
		Model m= super.load(file);
		return m;
	}

	public void save(String file, Model model) {
		super.save(file, model);
	};

	@Override
	protected ResourceSet buildResourceSet() {
		Injector injector = setup.createInjectorAndDoEMFRegistration();
		return injector.getInstance(ResourceSet.class);
	}

	
}
