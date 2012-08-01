package tom.mapping.dsl.generator.tom

import com.google.inject.Inject
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EStructuralFeature
import tom.mapping.dsl.generator.TomMappingExtensions
import model.ClassOperator
import model.EnumLiteralValue
import model.FeatureParameter
import model.JavaCodeValue
import model.Mapping
import model.Operator
import model.SettedFeatureParameter
import model.SettedValue
import tom.mapping.dsl.generator.tom.ParametersCompiler

class OperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	@Inject ParametersCompiler injpa
	
	
	def dispatch operator(Mapping mapping, Operator op) {
		''''''
	}
	
	
	def dispatch operator(Mapping mapping, ClassOperator clop) {
		if(clop.parameters.size > 0) {
			mapping.classOperatorWithParameters(clop);
		} else {
			mapping.classOperator(clop.name, clop.class_);
		}
	}
	
	
	def classOperator(Mapping mapping, String op, EClass ecl) {
	'''
	«val parameters = ecl.getDefaultParameters(mapping)»
	
	«IF mapping.getTerminal(ecl,false) != null»
		%op «mapping.getTerminal(ecl,false).name» «op» («mapping.classAttributes(ecl)»«FOR p: parameters SEPARATOR ","»«injpa.defaultFeatureParameter(mapping, p)»«ENDFOR») {
			
		is_fsym(t) {$t instanceof «ecl.name»}
		
		«FOR attribute: ecl.EAllAttributes»
		get_slot(«attribute.name»,t) {((«ecl.name»)$t).get«attribute.name.toFirstUpper()»()}
		«ENDFOR»
		
		«FOR p: parameters»
		get_slot(«p.name»,t) {«ecl.getter(p)»}
		«ENDFOR»
		
		make(«FOR att: ecl.EAllAttributes SEPARATOR ","» «att.name»«ENDFOR»«IF (ecl.EAllAttributes.size > 0) && (ecl.getDefaultParameters(mapping).size > 0)»,«ENDIF»«FOR param: parameters SEPARATOR ","»«param.name»«ENDFOR»){«mapping.tomFactoryQualifiedName()».create«op.toFirstUpper()»(«FOR att: ecl.EAllAttributes SEPARATOR ","»$«att.name»«ENDFOR»«IF ecl.EAllAttributes.size > 0 && ecl.getDefaultParameters(mapping).size > 0»,«ENDIF»«FOR param: parameters SEPARATOR ","» $«param.name»«ENDFOR»)}«ENDIF»}
   	'''
	}
	
	
	def classOperatorWithParameters(Mapping mapping, ClassOperator clop) {
	'''
   «val parameters = clop.getCustomParameters()»

		%op «mapping.getTerminal(clop.class_,false).name» «clop.name» 
		(«FOR p : parameters SEPARATOR ","»«injpa.featureParameter(mapping, p)»«ENDFOR») {
		is_fsym(t) {$t instanceof «clop.class_.name»«FOR p: clop.parameters»«clop.class_.settedParameterTest(p)»«ENDFOR»}
		
   		«FOR p: parameters»
   		get_slot(«p.feature.name»,t) {«clop.class_.getter(p.feature)»}
   		«ENDFOR»
		
		make(«FOR p: parameters SEPARATOR ","»_«p.feature.name»«ENDFOR») {«mapping.tomFactoryQualifiedName()».create«clop.name.toFirstUpper()»(«FOR p:parameters SEPARATOR ","»$_«p.feature.name»«ENDFOR»)}
   	'''
	}
	
	
	def getter(EClass c, EStructuralFeature esf) {
		'''
		«IF esf.many»enforce«ENDIF»
			((«c.name»)$t).get«esf.name.toFirstUpper()»()
		«IF esf.many»)«ENDIF»
		'''
	}
	
	
	def classAttributes(Mapping mapping, EClass ecl) {		
		'''		
		«FOR att: ecl.EAllAttributes SEPARATOR ","»
		«att.name» : «injpa.feature(att)»
		«ENDFOR»
		«IF (ecl.EAllAttributes.size() > 0) && (ecl.getDefaultParameters(mapping).size() >0)»,«ENDIF»
		'''
	}
	
	
	def javaClassAttributes(Mapping mapping, EClass ecl) {
		'''		
		«FOR att: ecl.EAllAttributes SEPARATOR ","»
		 «injpa.feature(att)» _«att.name»
		«ENDFOR»
		«IF (ecl.EAllAttributes.size > 0) && (ecl.getDefaultParameters(mapping).size>0)»,«ENDIF»
		'''
	}
	
	
	def dispatch settedParameterTest(EClass c, FeatureParameter feature) {''''''}
	
	
	def dispatch settedParameterTest(EClass c, SettedFeatureParameter sfp) {
		'''
		&& ((«c.name»)$t).get«sfp.feature.name.toFirstUpper()»().equals(«sfp.feature.settedValue(sfp.value)»
		'''
	}
	
	
	def dispatch settedValue(EStructuralFeature feature, SettedValue sv) {''''''}
	
	def dispatch settedValue(EStructuralFeature feature, EnumLiteralValue elv) {
		'''
		«feature.EType.name».«elv.literal.name»
		'''
	}
	
	
	def dispatch settedValue(EStructuralFeature feature, JavaCodeValue jcv) {
		'''
		«val isString = ((feature.EType.instanceTypeName != null) && (feature.EType.instanceTypeName.contains("java.lang.String")))»
		«IF isString»"«ENDIF»
		«jcv.java»
		«IF isString»"«ENDIF»
		'''
	}
	
	
}