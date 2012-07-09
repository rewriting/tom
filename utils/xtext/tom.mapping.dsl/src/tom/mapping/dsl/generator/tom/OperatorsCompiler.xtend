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
import tom.mapping.dsl.generator.NamingCompiler
import com.google.inject.Inject

class OperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	extension NamingCompiler = new NamingCompiler()
	
	@Inject ParametersCompiler injpa
	
	
	def dispatch operator(Mapping mapping, Operator op) {
	}
	
	
	def dispatch operator(Mapping mapping, ClassOperator clop) {
		if(clop.parameters.size > 0) {
			classOperatorWithParameters(mapping, clop);
		} else {
			classOperator(mapping, clop.name, clop.class_);
		}
	}
	
	
	def classOperator(Mapping mapping, String op, EClass ecl) {
	
		/* PROTECTED REGION ID(op+"_"+ecl.name+"_tom_operator") ENABLED START */
   
   val parameters = getDefaultParameters(ecl, mapping);
   
   if(mapping.getTerminal(ecl,false) != null) {
   	'''
		%op «mapping.getTerminal(ecl.class_,false).name» «op» («mapping.classAttributes(ecl))» 
		«(for(p : parameters SEPARATOR ",") » {
		«injpa.defaultFeatureParameter(mapping, p))
		» }
		is_fsym(t) {$t instanceof «ecl.name»}
		«for(attribute: ecl.EAllAttribute)» {
		get_slot(«attribute.name»,t) {
		((«ecl.name»)$t).get«attribute.name.toFirstUpper()»()
		}
		}
		}
		«for(p: parameters)»{
		get_slot(«p.name»,t) {«getter(ecl, p)»}
		}
		make(«for(att: ecl.EAllAttribute SEPARATOR ",")» {
		«att.name»
		«if(ecl.EAllAttributes.size > 0 && ecl.getDefaultParameters(mapping).size > 0)» {
			for(param: parameters SEPARATOR ","") {
				«param.name») {
					«tomFactoryQualifiedName(mapping)».create«ecl.name.toFirstUpper()»(«for(att: ecl.EAllAttributes SEPARATOR ",")» {$«att.name»}
					«if(ecl.EAllAttributes.size > 0 && ecl.getDefaultParameters(mapping).size > 0)»{,}
					«for(param: parameters SEPARATOR ",")» {$«param.name»})
				}
			}
		}
	}
   	'''
   }
   
		/* PROTECTED REGION END */
	}
	
	
	def classOperatorWithParameters(Mapping mapping, ClassOperator clop) { // How to manage protected regions ?
	
		/* PROTECTED REGION ID(op+"_"+clop.name+"_tom_operator_with_param") ENABLED START */
   
   val parameters = getCustomParameters(clop);

   	'''
   	%op «mapping.getTerminal(clop.class_,false).name» «name» 
   	«(for(p : parameters SEPARATOR ",") » {
   		«injpa.featureParameter(mapping, p)»
   		}) {
   		is_fsym(t) {$t instanceof «clop.class_.name»
   		«for(p: ecl.parameters)» {
   			«settedParameterTest(clop.class_, p)»
   			}
   		}
   		«for(p: parameters)»{
   			get_slot(«p.feature.name»,t) {«getter(clop.class_, p.feature)»}
   		}
   		make(«for(p: parameters SEPARATOR ",")» {
   			_«p.feature.name»
   		}) {
   	«tomFactoryQualifiedName(mapping».create«this.name.toFirstUpper()»(�for(p:parameters SEPARATOR ",")»{$_«p.feature.name»})}
   	'''
   
		/* PROTECTED REGION END */
	}
	
	
	def getter(EClass c, EStructuralFeature esf) {
		if(esf.many){
			'''enforce(''' }
			'''((«c.name»)$t).get«name.toFirstUpper()»()'''
		if(esf.many) {''')'''}
	}
	
	
	def classAttributes(Mapping mapping, EClass ecl) {
				
		for(att: ecl.EAllAttributes SEPARATOR ",") {
			'''
			«att.name» = «injpa.feature(att)»
			'''
		}
		
		if(ecl.EAllAttributes.size > 0 && getDefaultParameters(ecl, mapping).size>0) {''','''}
	}
	
	
	def javaClassAttibutes(Mapping mapping, EClass ecl) {
		
		for(att: ecl.EAllAttributes SEPARATOR ",") {
			'''
			«injpa.feature(att)» _«att.name»
			'''
		}
		
		if(ecl.EAllAttributes.size > 0 && getDefaultParameters(ecl, mapping).size>0) {''','''}
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
		'''
		«feature.EType.name».«literal.name»
		'''
	}
	
	
	def dispatch settedValue(EStructuralFeature feature, JavaCodeValue jcv) {
		val isString = ((feature.EType.instanceTypeName != null) && (feature.EType.instanceTypeName.contains("java.lang.String")))
		if(isString) {'''"'''}
		'''«java»'''
		if(isString) {'''"'''}
	}
	
	
}