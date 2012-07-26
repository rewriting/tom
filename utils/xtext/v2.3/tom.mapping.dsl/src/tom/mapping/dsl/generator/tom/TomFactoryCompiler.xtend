//Licence
package tom.mapping.dsl.generator.tom

import com.google.inject.Inject
import java.util.ArrayList
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.xtext.generator.IFileSystemAccess
import tom.mapping.dsl.generator.ImportsCompiler
import tom.mapping.dsl.generator.TomMappingExtensions
import model.ClassOperator
import model.FeatureParameter
import model.Mapping
import model.Operator

class TomFactoryCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	String prefix = "tom"
	
	@Inject OperatorsCompiler injop
	@Inject ImportsCompiler injco
	@Inject ParametersCompiler injpa
	
	def compile(Mapping m, IFileSystemAccess fsa) {
		fsa.generateFile(prefix+"/"+m.name.toFirstLower()+"/internal/"+m.tomFactoryName()+".java", m.main());
	}
	
	def ArrayList<EPackage> intersectName(ArrayList<EPackage> listBase) { // Defined because of the intersections of "packageList" needed in function "main"
		var listDestination = new ArrayList<EPackage>();
		
		for(eltB : listBase) {
			var sameName = false;
			for(eltD : listDestination) {
				sameName = sameName || eltB.name == eltD.name;
				} 
			if(!sameName) {
				listDestination.add(eltB);
				}
			}
		return listDestination;
		}
		
	
	def main(Mapping map){
	'''
	 package �prefix.getPackagePrefix()��map.name.toFirstLower()�.internal;
	 
	 /* PROTECTED REGION ID(map.name+"_tom_factory_imports") ENABLED START */
	 // protected imports, you should add here required imports that won't be removed after regeneration of the maping code
	  
	 import java.util.List 
	  
	 �injco.imports(map)�
	 /* ENDPROTECT */
	 
	 /**
	 * Factory used by TOM for �map.name� mapping.
	 * It shouldn't be visible outside of the plugin
	 * -- Generated by TOM mapping EMF generator --
	 */
	 
	 
	 public class �map.tomFactoryName()� {
	 	
	 	�/* PROTECTED REGION ID(map.name+"_tom_factory_instances") ENABLED START */�
	 	
	 	�var packageList = new ArrayList<EPackage>()�
	    �FOR elt : map.operators.filter(typeof(ClassOperator))�
	    �packageList.add(elt.class_.EPackage)�
		�ENDFOR�

	 	�FOR pack: packageList.intersectName()�
	 		public static �pack.name.toFirstUpper()�Factory �pack.name�Factory = �pack.name.toFirstUpper()�Factory.eINSTANCE;
	 	�ENDFOR�
	 	
	 	�var packageList2 = new ArrayList<EPackage>()�
		 	�FOR elt : map.allDefaultOperators�
		 	�packageList2.add(elt.EPackage)�
		 	�ENDFOR�

	 	�FOR pack: packageList2.intersectName()�
	 		public static �pack.name.toFirstUpper()�Factory �pack.name�Factory = �pack.name.toFirstUpper()�Factory.eINSTANCE;
	 	�ENDFOR�
	 }
	 	�/* ENDPROTECT */�
	 
	 
	 // User operators �map.operators�
	 
	 �FOR module: map.modules�
	 	/** Module �module.name� **/
	 	�FOR op: module.operators�
	 		// Operator �op.name�
	 		�map.operator(op)�
	 	�ENDFOR�
	 �ENDFOR�
	 
	 /*
	 * Default TOM operators for �map.name� mapping. Each class that has a terminal type has aloso a default create function.
	 */
	 
	 �FOR op: map.allDefaultOperators�
	 	�IF !op.instanceClassName.contains("java.util.Map$Entry")�
	 		�map.javaFactoryCreateDefaultOperator(op.name,op)�
	 	�ENDIF�
	 �ENDFOR�
	 '''
	} 


	def operator(Mapping mapping, Operator op) {
		'''// �op.eClass().name�'''
	}

	
	def operator(Mapping mapping, ClassOperator clop) {
		if(clop.parameters.size()>0) {
			val parameters = clop.getCustomParameters();
			parameters.javaFactoryCreateOperatorWithParameters(mapping, clop);
		} else {
			mapping.javaFactoryCreateDefaultOperator(clop.name, clop.class_);
		}
	}


	def javaFactoryCreateOperatorWithParameters(Iterable<FeatureParameter> parameters, Mapping mapping, ClassOperator clop) {
	'''	
	// CreateOperatorWithParameters �clop.name�
	public static �clop.class_.name� create�clop.name.toFirstUpper()�(�FOR p: parameters SEPARATOR ","��mapping.typeOfParameter(p.feature)� _�p.feature.name��ENDFOR�) {
		�clop.class_.name� o = �clop.class_.EPackage.name�Factory.create�clop.class_.name.toFirstUpper()�();
		�FOR p: parameters�
			�p.feature.structureFeatureSetter()�
		�ENDFOR�
		�FOR p: clop.getSettedCustomParameters()�
			o.set�p.feature.name.toFirstUpper()�(�injop.settedValue(p.feature, p.value)�
		�ENDFOR�
		return o;
	}
	'''
	}
	
	
	def javaFactoryCreateDefaultOperator(Mapping mapping, String name, EClass ecl) {
		val parameters = ecl.getDefaultParameters(mapping);
		'''
		// CreateDefaultOperator �ecl.name�
		public static �ecl.name� create�ecl.name.toFirstUpper()�(�injop.javaClassAttributes(mapping, ecl)�
		�FOR p: parameters SEPARATOR ","��mapping.typeOfParameter(p)� _�p.name��ENDFOR�) {
			�ecl.name� o = �ecl.EPackage.name�Factory create�ecl.name.toFirstUpper()�();
			�FOR attribute: ecl.EAllAttributes�
				�attribute.structureFeatureSetter()�
			�ENDFOR�
		�FOR param: parameters�
			�param.structureFeatureSetter()�
		�ENDFOR�
		return o;
		}
		'''
	}
	
	
	def structureFeatureSetter(EStructuralFeature esf) {
		if(esf.many) {
			'''
			for(int i=0; i<_�esf.name�.size(); i++) {
				o.get�esf.name.toFirstUpper()�().add(�esf.featureAccess()�.get(i));
			}
			'''
		} else {
			'''
			o.set�esf.name.toFirstUpper()�(�esf.featureAccess()�);
			'''
		}
	}
	
	
	def featureAccess(EStructuralFeature esf) {
		'''
		�IF esf.EType.instanceTypeName == null�
			(�esf.EType.name�)
			�ENDIF�
			_�esf.name�
			'''
	}
	
	
	def typeOfParameter(Mapping mapping, EStructuralFeature esf) {}
	
	def typeOfParameter(Mapping mapping, EReference eref) {mapping.terminalTypeName(eref.many, eref.EType);}
	
	def typeOfParameter(Mapping mapping, EEnum enu) {enu.name;}
	
	def  typeOfParameters(Mapping mapping, EAttribute eat) {injpa.primitiveType(eat.EAttributeType);}
	
	
	def dispatch terminalTypeName(Mapping mapping, boolean many, EClassifier ecl) {
		'''�IF many�List<�ENDIF��ecl.name��IF many�>�ENDIF�'''
	}
	
	def dispatch terminalTypeName(Mapping mapping, boolean many, EClass ecl) {
		'''�IF many�List<�ENDIF��mapping.getTerminal(ecl,false).class_.name��IF many�>�ENDIF�'''
	}


	def javaClassAttributes(Mapping mapping, EClass ecl) {
		'''
		�FOR att: ecl.EAllAttributes SEPARATOR ","�
			�mapping.typeOfParameter(att)� _�att.name�
		�ENDFOR�
		�IF ecl.EAllAttributes.size() > 0 && ecl.getDefaultParameters(mapping).size() > 0�,�ENDIF�
		'''
	}
	
}