// Licence
package tom.mapping.dsl.generator

import org.eclipse.emf.ecore.EPackage
import model.Mapping

class ImportsCompiler {

	extension TomMappingExtensions = new TomMappingExtensions()
	
	String prefix = ""
	
	def imports(Mapping map) {
		'''
		«FOR EPackage p: map.getAllRootPackages()»
		«prefix.imports(p)»
		«ENDFOR»
		'''
	}
	
	
	def importsWithUtils(Mapping map) {
		'''
		«FOR EPackage p: map.getAllRootPackages()»
		«prefix.importsWithUtils(p)»
		«ENDFOR»
		'''
	}
	
	
	def imports(String prefix, EPackage ep) { // Check this function
		'''
		«IF ep.EClassifiers.size() > 0»	
			 import «prefix.getPackagePrefix()»«ep.name».*;
		«ENDIF» 
		«FOR p : ep.ESubpackages»
			 «(prefix.getPackagePrefix()+ep.name).imports(p)»
		«ENDFOR»
		'''
	}


	def importsWithUtils(String prefix, EPackage ep) { // Check this function
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