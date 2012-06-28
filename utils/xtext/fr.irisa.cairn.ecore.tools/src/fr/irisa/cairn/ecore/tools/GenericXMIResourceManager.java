package fr.irisa.cairn.ecore.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * Generic XMI load/save utility. EPackage and required extensions of the
 * input/output model have to be registered.
 * 
 * @author antoine
 * 
 * @param <Model>
 *            Root model type
 */
@SuppressWarnings("unchecked")
public class GenericXMIResourceManager<Model extends EObject> {
	protected List<EPackage> packages;
	protected List<String> extensions;
	private ResourceSet resourceSet;

	public GenericXMIResourceManager(EPackage... packages) {
		this.extensions = new ArrayList<String>();
		this.packages = new ArrayList<EPackage>();
		for (EPackage p : packages) {
			this.packages.add(p);
		}
	}

	public GenericXMIResourceManager(List<EPackage> packages) {
		this();
		for (EPackage p : packages) {
			this.packages.add(p);
		}
	}

	public Model load(URI uri) {
		ResourceSet rs = getResourceSet();
		Resource resource = rs.getResource(uri, true);
		EcoreUtil.resolveAll(rs);
		Model loaded = ((Model) resource.getContents().get(0));
		return loaded;
	}

	public ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = buildResourceSet();
			initialize(resourceSet);
		}
		return resourceSet;
	}

	protected ResourceSet buildResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		return resourceSet;
	}

	protected void initialize(ResourceSet resourceSet) {
		resourceSet.setPackageRegistry(EPackage.Registry.INSTANCE);
		resourceSet
				.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
		Resource.Factory.Registry f = resourceSet.getResourceFactoryRegistry();
		Map<String, Object> m = f.getExtensionToFactoryMap();
		for (String extension : extensions) {
			m.put(extension, new XMIResourceFactoryImpl());
		}
		for (EPackage p : packages) {
			Registry rsreg = resourceSet.getPackageRegistry();
			if (!rsreg.containsKey(p.getNsURI()))
				rsreg.put(p.getNsURI(), p);
		}
	}

	public void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
	}

	public Model load(String file) {
		return load(URI.createURI(file));

	}

	public void save(String file, Model model) {
		ResourceSet rs = getResourceSet();
		Resource.Factory.Registry f = rs.getResourceFactoryRegistry();
		Map<String, Object> m = f.getExtensionToFactoryMap();
		for (String extension : extensions) {
			m.put(extension, new XMIResourceFactoryImpl());
		}
		for (EPackage p : packages) {
			rs.getPackageRegistry().put(p.getNsURI(), p);
		}
		URI uri = URI.createURI(file);
		Resource resource = rs.createResource(uri);
		if(resource ==null)
			throw new RuntimeException("Unable to create the resource: "+file+". You should check file extension and/or registered extensions.");
		resource.getContents().add(model);

		try {
			HashMap<String, Object> options = new HashMap<String, Object>();
			options.put(XMIResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
			resource.save(options);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Register a supported XMI resource extension.
	 * 
	 * @param extension
	 */
	public void register(String extension) {
		this.extensions.add(extension);
	}

	/**
	 * Register a XMI resource {@link EPackage}.
	 * 
	 * @param p
	 */
	public void register(EPackage p) {
		this.packages.add(p);
	}
}
