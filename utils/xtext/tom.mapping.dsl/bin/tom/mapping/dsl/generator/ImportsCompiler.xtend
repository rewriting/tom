// Licence
package tom.mapping.dsl.generator

import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Mapping

class ImportsCompiler {

	extension TomMappingExtensions = new TomMappingExtensions()
	extension NamingCompiler = new NamingCompiler()
	
	String prefix = ""
	
	def imports(Mapping map) {
		for(EPackage p: map.getAllRootPackages) {
			imports(prefix, p);
		}
	}
	
	
	def importsWithUtils(Mapping map) {
		for(EPackage p: map.getAllRootPackages) {
			importsWithUtils(prefix, p);
		}
	}
	
	
	def imports(String prefix, EPackage ep) {
		if(ep.EClassifiers.size > 0) {
			'''
			 import «getPackagePrefix(prefix)»«ep.name».*; 
			 '''
		}
		for(EPackage p: ep.ESubpackages) {
			imports(prefix, p);
		}
	}


	def importsWithUtils(String prefix, EPackage ep) {
		if(ep.EClassifiers.size > 0) {
			'''
			import «getPackagePrefix(prefix)»«ep.name».*; 
			import «getPackagePrefix(prefix)�«ep.name».util.*; 
			'''
		}
		for(EPackage p: ep.ESubpackages) {
			importsWithUtils(getPackagePrefix(prefix)+ep.name, p);
		} 
	}


}