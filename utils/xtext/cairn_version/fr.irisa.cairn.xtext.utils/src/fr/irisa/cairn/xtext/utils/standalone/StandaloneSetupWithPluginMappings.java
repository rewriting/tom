package fr.irisa.cairn.xtext.utils.standalone;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.mwe.utils.StandaloneSetup;


public class StandaloneSetupWithPluginMappings extends StandaloneSetup {

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public void addPluginMapping(PluginMapping m) {
		EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(
				m.nsuri);
		resourceSet.getPackageRegistry().put(m.plugin, ePackage);
	}
}