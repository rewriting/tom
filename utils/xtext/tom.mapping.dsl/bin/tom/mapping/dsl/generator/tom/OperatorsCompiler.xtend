package tom.mapping.dsl.generator.tom

import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EStructuralFeature
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.ClassOperator
import tom.mapping.model.EnumLiteralValue
import tom.mapping.model.FeatureParameter
import tom.mapping.model.JavaCodeValue
import tom.mapping.model.Mapping
import tom.mapping.model.Operator
import tom.mapping.model.SettedFeatureParameter
import tom.mapping.model.SettedValue

class OperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	
	def dispatch operator(Mapping mapping, Operator op) {
	}
	
	
	def dispatch operator(Mapping mapping, ClassOperator clop) {
		if(clop.parameters.size > 0) {
			classOperatorWithParameters(mapping, clop);
		} else {
			classOperator(mapping, clop.name, clop.class_);
		}
	}
	
	
	def classOperator(Mapping mapping, String op, EClass ecl) { // How to manage protected regions ?
	
		/* PROTECTED REGION ID(op+"_"+ecl.name+"_tom_operator") ENABLED START */
   
   val parameters = getDefaultParameters(ecl, mapping);
   
   if(mapping.getTerminal(ecl,false) != null) { // What to do with SEPARATOR ? How to manage «» correctly ?
   	'''
   	%op «mapping.getTerminal(ecl.class_,false).name» «name» 
   	«(for(EReference p : parameters) » {
   		«parameters.featureParameter(mapping, p))
   	» }
   		is_fsym(t) {$t instanceof «ecl.class_.name»
   		«for(EReference p: ecl.parameters)» {
   			«settedParameterTest(ecl.class_, p)»
   			}
   		}
   		«for(EReference p: parameters)»{
   			get_slot(«p.feature.name»,t) {«getter(ecl.class_, p.feature)»}
   		}
   		make(«for(EReference p: parameters)» {
   			«p.feature.name»
   		}
   	«tomFactoryQualifiedName(mapping».create«this.name.toFirstUpper()»(�for(EReference p:parameters)»{$_«p.feature.name»})
   	'''
   }
   
		/* PROTECTED REGION END */
	}
	
	
	def classOperatorWithParameters(Mapping mapping, ClassOperator clop) { // How to manage protected regions ?
	
		/* PROTECTED REGION ID(op+"_"+clop.name+"_tom_operator_with_param") ENABLED START */
   
   val parameters = getCustomParameters(clop);

   	'''
   	%op «mapping.getTerminal(clop.class_,false).name» «name» 
   	«(for(EReference p : parameters) » {
   		«parameters.featureParameter(mapping, p))
   	» }
   		is_fsym(t) {$t instanceof «clop.class_.name»
   		«for(EReference p: ecl.parameters)» {
   			«settedParameterTest(clop.class_, p)»
   			}
   		}
   		«for(EReference p: parameters)»{
   			get_slot(«p.feature.name»,t) {«getter(clop.class_, p.feature)»}
   		}
   		make(«for(EReference p: parameters)» {
   			«p.feature.name»
   		}
   	«tomFactoryQualifiedName(mapping».create«this.name.toFirstUpper()»(�for(EReference p:parameters)»{$_«p.feature.name»})
   	'''
   
		/* PROTECTED REGION END */
	}
	
	
	def getter(EClass c, EStructuralFeature esf) {
		if(esf.many){
			'''
			enforce(((«c.name»)$t).get«name.toFirstUpper()»()
			'''
		}
	}
	
	
	def classAttibutes(Mapping mapping, EClass ecl) {
		
		val parameters = getDefaultParameters(ecl, mapping);
		
		for(EAttribute att: ecl.EAllAttributes) {
			// parameters.feature(att); // _"att.name" ?
		}
		
		if(ecl.EAllAttributes.size > 0 && getDefaultParameters(ecl, mapping).size>0) {} // Interest ?
	}
	
	
	def javaClassAttibutes(Mapping mapping, EClass ecl) {
		
		val parameters = getDefaultParameters(ecl, mapping);
		
		for(EAttribute att: ecl.EAllAttributes) {
			// parameters.feature(att); // ?
		}
		
		if(ecl.EAllAttributes.size > 0 && getDefaultParameters(ecl, mapping).size>0) {} // Interest ?
	}
	
	
	def dispatch settedParameterTest(EClass c, FeatureParameter feature) {		
	}
	
	
	def dispatch settedParameterTest(EClass c, SettedFeatureParameter feature) {
		'''
		((«c.name»)$t).get«feature.name.toFirstUpper()»().equals(«settedValue(feature, sfp.value) »
		'''
	}
	
	
	def dispatch settedValue(EStructuralFeature feature, SettedValue sv) {}
	
	def dispatch settedValue(EStructuralFeature feature, EnumLiteralValue literal) { // litteral.name ?
		feature.EType.name;
	}
	
	
	def dispatch settedValue(EStructuralFeature feature, JavaCodeValue jcv) {
		val isString = ((feature.EType.instanceTypeName != null) && (feature.EType.instanceTypeName.contains("java.lang.String")))
		if(isString) {
			"java";
		}
	}
	
	
}