// Licence
package tom.mapping.dsl.generator

import org.eclipse.emf.ecore.EPackage
import model.Mapping

class ImportsCompiler {

	extension TomMappingExtensions = new TomMappingExtensions()
	
	String prefix = ""
	
	def imports(Mapping map) {
		for(EPackage p: map.getAllRootPackages()) {
			prefix.imports(p)
		}
	}
	
	
	def importsWithUtils(Mapping map) {
		for(EPackage p: map.getAllRootPackages()) {
			prefix.importsWithUtils(p)
		}
	}
	
	
	def imports(String prefix, EPackage ep) {
		'''
		«IF ep.EClassifiers.size() > 0»	
			 import «prefix.getPackagePrefix()»«ep.name».*;
		«ENDIF» 
		«FOR p : ep.ESubpackages»
			 «(prefix.getPackagePrefix()+ep.name).imports(p)»
		«ENDFOR»
		'''
	}


	def importsWithUtils(String prefix, EPackage ep) {
		'''
		«IF ep.EClassifiers.size() > 0»
			import «prefix.getPackagePrefix()»«ep.name».*; 
			import «prefix.getPackagePrefix()»«ep.name».util.*;
		«ENDIF»
		«FOR p: ep.ESubpackages»
		«(prefix.getPackagePrefix()+ep.name).importsWithUtils(p)»
		«ENDFOR»
		'''
	}


}