// Licence
package tom.mapping.dsl.generator.tom

import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.FeatureParameter
import tom.mapping.model.Mapping
import tom.mapping.model.Parameter
import tom.mapping.model.Terminal
import java.util.List
import tom.mapping.dsl.generator.NamingCompiler

class ParametersCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	extension NamingCompiler = new NamingCompiler()
	
	def parameter(Parameter p){
		'''
		«p.name» : «terminalType(p.type)»;
		'''
	}
	
	
	def javaParameter(Parameter p) {
		'''
		«javaTerminalType(p.type)» «name»;
		'''
	}
	
	
	def terminalType(Terminal t) {
		'''«t.name»;'''
	}
	
	
	def javaTerminalType(Terminal t) {
		if(t.many) {
			'''List<«t.class_.name»>;'''
		} else {
			'''«t.class_.name»;''';
		}
	}
	
	// Reprendre ici. Bon week-end tout le monde !
	
	def featureParameter(Mapping mapping, FeatureParameter fp) {
		feature(mapping, fp.feature);
	}
	
	
	def javaFeatureParameter(FeatureParameter fp) { // What to do with "_this.feature.name" ?
		feature(fp.feature);
	}
	
	
	def defaultFeatureParameter(Mapping mapping, EStructuralFeature esf) {
		feature(mapping, esf);
	}
	
	
	def defaultJavaFeatureParameter(EStructuralFeature efp) { // this.name ?
		feature(efp);
	}
	
	
	def renameEcoreClasses(EAttribute eat) { // Ok for the EAttributeType ?
		if(eat.EAttributeType.name == "EInt") {
			eat.EAttributeType.name = "int"; 
		}
		else if(eat.EAttributeType.name == "EFloat") {
			eat.EAttributeType.name = "float"; 
		}
		else if(eat.EAttributeType.name == "EDouble") {
			eat.EAttributeType.name = "double"; 
		}
		else if(eat.EAttributeType.name == "EBoolean") {
			eat.EAttributeType.name = "boolean"; 
		}
		else if(eat.EAttributeType.name == "EString") {
			eat.EAttributeType.name = "String"; 
		} else {
			if(eat.EAttributeType.instanceTypeName != "null") {
				eat.EAttributeType.instanceClassName;
			} else {
				eat.EAttributeType.name;
			}
		}
	}
	
	
	def dispatch feature(Mapping mapping, EStructuralFeature esf) {
	}
	
	
	def dispatch feature(Mapping mapping, EReference er) {
		name(mapping.getTerminal(er.EReferenceType, er.many),mapping);
	}
	
	
	def dispatch feature(Mapping mapping, EAttribute eat) { // Is it ok with the List ?
		if(eat.many) {
			var List<? extends EAttribute> leat;
			leat.map[renameEcoreClasses(eat)];
		} else {renameEcoreClasses(eat);}
	}
	
	
	def dispatch feature(EStructuralFeature esf) {
	}
	
	
	def dispatch feature(EReference er) {  // Is it ok with the List ?
		if(er.many) {
			var List<String> lert;
			lert.add(er.EReferenceType.name);
		}
		er.EReferenceType.name;
	}
	
	
	def dispatch feature(EEnum ee) { // What else to put in this ?
		ee.name;
	}
	
	
	def dispatch feature(EAttribute eat) {
		primitiveType(eat.EAttributeType);
	}
	
	
	def dispatch primitiveType(EDataType edt) {
		if(edt.instanceTypeName == "java.lang.String") {
			"String";
		} else {
			edt.instanceTypeName;
		}
	}
	
	
	def dispatch primitiveType(EEnum ee) { // What else to put in this ?
		ee.name;
	}
	
	
}