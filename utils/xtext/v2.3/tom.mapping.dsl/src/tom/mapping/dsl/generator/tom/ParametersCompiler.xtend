// Licence
package tom.mapping.dsl.generator.tom

import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import tom.mapping.dsl.generator.TomMappingExtensions
import model.FeatureParameter
import model.Mapping
import model.Parameter
import model.Terminal

class ParametersCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	def parameter(Parameter p){
		'''
		«p.name» : «p.type.terminalType()»
		'''
	}
	
	
	def javaParameter(Parameter p) {
		'''
		«p.type.javaTerminalType()» «p.name»
		'''
	}
	
	
	def terminalType(Terminal t) {
		'''«t.name»'''
	}
	
	
	def javaTerminalType(Terminal t) {
		'''«IF t.many»List<«t.class_.name»>«ELSE»«t.class_.name»«ENDIF»'''
	}

	
	def featureParameter(Mapping mapping, FeatureParameter fp) {
		'''«fp.feature.name» : «mapping.feature(fp.feature)»'''
	}
	
	
	def javaFeatureParameter(FeatureParameter fp) {
		'''«fp.feature.feature()» _«fp.feature.name»'''
	}
	
	
	def defaultFeatureParameter(Mapping mapping, EStructuralFeature esf) {
		'''«esf.name» : «mapping.feature(esf)»'''
	}
	
	
	def defaultJavaFeatureParameter(EStructuralFeature efp) {
		'''«efp.feature()» _«efp.name»'''
	}
	
	
	def renameEcoreClasses(EAttribute eat) {
		'''
		«IF eat.EAttributeType.name == "EInt"»int
		«ELSEIF eat.EAttributeType.name == "ELong"»long
		«ELSEIF eat.EAttributeType.name == "EFloat"»float
		«ELSEIF eat.EAttributeType.name == "EDouble"»double
		«ELSEIF eat.EAttributeType.name == "EBoolean"»boolean
		«ELSEIF eat.EAttributeType.name == "EString"»String
		«ELSE»
		«IF eat.EAttributeType.instanceTypeName != null»
			«eat.EAttributeType.instanceClassName»
		«ELSE»«eat.EAttributeType.name»«ENDIF»
		«ENDIF»
		'''
	}
	
	
	def dispatch feature(Mapping mapping, EStructuralFeature esf) {
		''''''
	}
	
	
	def dispatch feature(Mapping mapping, EReference er) {
		'''«mapping.getTerminal(er.EReferenceType, er.many).name(mapping)»'''
	}
	
	
	def dispatch feature(Mapping mapping, EAttribute eat) {
		'''
		«IF eat.many»
			List<? extends «eat.renameEcoreClasses()»>
		«ELSE»«renameEcoreClasses(eat)»
		«ENDIF»
		'''
	}
	
    
	def dispatch feature(EStructuralFeature esf) {
		''''''
	}
	
	
	def dispatch feature(EReference er) {
    	'''
    	«IF er.many»
    	List<? extends «er.EReferenceType.name»>«ELSE»
    	«er.EReferenceType.name»«ENDIF»
		'''
	}
	
	
	def dispatch feature(EEnum ee) {
		'''«ee.name»'''
	}
	
	
	def dispatch feature(EAttribute eat) {
		'''«eat.EAttributeType.primitiveType()»'''
			}
	
	
	def dispatch primitiveType(EDataType edt) {
		'''«IF edt.instanceTypeName == "java.lang.String"»String«ELSE»«edt.instanceTypeName»«ENDIF»'''
	}
	
	
	def dispatch primitiveType(EEnum ee) {
		'''«ee.name»'''
	}
	
}