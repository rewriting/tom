package tom.mapping.dsl.generator

import tom.mapping.model.Accessor
import tom.mapping.model.UserOperator
import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Terminal
import org.eclipse.emf.ecore.EClass
import tom.mapping.model.ClassOperator
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EReference
import tom.mapping.model.SettedFeatureParameter
import tom.mapping.model.FeatureParameter
import tom.mapping.model.FeatureException
import java.util.ArrayList

class TomMappingExtensions {
	
	def  String getCustomOperatorSlotAccessorName(Accessor a){
		return "get"+(a as UserOperator).name.toFirstUpper()+"Slot"+a.slot.name.toUpperCase(); 
	}
	
	def  String getCustomOperatorsClass(Mapping mapping) {
		return mapping.name.toFirstUpper()+"CustomAccessors";
	}
	
	def  String getChildrenGetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenGetter";
	}
	
	def String getChildrenSetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenSetter";
	}

	
	def String name(Terminal t, Mapping m){
		if(t.many&&t.isInferedList(m)){
			return t.name+"List";
		
		}
		else{
			return t.name;
			
		}
	}
	

	def boolean isInferedList(Terminal t, Mapping m){
		t.many&&!(m.terminals.contains(t)||m.externalTerminals.contains(t));
	}
	
	def String factoryName(Mapping m){
		m.name.toFirstUpper()+"UserFactory";
	}
	
	def String tomFactoryName(Mapping m){
		m.name.toFirstUpper()+"TomFactory";
	}
	def String tomFactoryQualifiedName(Mapping m){
		m.prefix+"."+m.name+"."+"internal"+"."+tomFactoryName(m);
	}

	def String getPackagePrefix( String prefix){
		if(prefix==null || prefix.compareTo("")==0){
			return "";
		}else{
			return prefix+".";
		}
	}
	
		
	def getDefaultParameters(EClass c, Mapping mapping){
		c.EAllReferences.filter([e|e.isParameterFeature(mapping)]).toList;
	}
	

	def getCustomParameters(ClassOperator c){
		c.parameters.filter([e|isExplicitParameter(e)]);
		
	}
		
	def getSettedCustomParameters(ClassOperator c){
		c.parameters.filter(typeof(SettedFeatureParameter)).toList;
		
	}
	
	def private dispatch boolean isParameterFeature(EStructuralFeature f,Mapping mapping){
		true;
	
	}
		
	def private dispatch boolean isParameterFeature(EReference f,Mapping mapping){
		 f.derived==false 
		&& f.volatile==false 
		&& (f.containment|| f.EOpposite==null)
		&& (mapping.getTerminal(f.EReferenceType,false)!=null || mapping.getTerminal(f.EReferenceType,true)!=null);
	}
	
	def private dispatch boolean isExplicitParameter(FeatureParameter pf){
		true; 
	
	}
	
	def private dispatch boolean  isExplicitParameter(SettedFeatureParameter pf){
		false; 
	}
		
	def private dispatch boolean  isExplicitParameter(FeatureException pf){
		false; 
	}	
		
	def private getAllSubPackages(EPackage p){
		p.eAllContents.filter(typeof(EPackage)).toList
	}
	
	def getAllPackages(Mapping m){
		val selected = new ArrayList<EPackage>()
		for(mp: m.metamodelPackages){
			selected.addAll(mp.allSubPackages.filter([e | e.isSelected(m)]));
		}
		return selected
	}
		
	def boolean isSelected(EPackage p, Mapping m){
		p.EClassifiers.filter(typeof(EClass)).filter([e|m.getTerminal(e as EClass,false)!=null]).size >0; 
	}	
		
	def  packageToPath(String s){
		if(s!=null){
			return s.replaceAll("\\.", "/")
		} else{
			return ""
		}
	}	
	
	def getAllRootPackages(Mapping mapping){
		mapping.metamodelPackages;
	
	}
		
	def boolean isPrimitive(String type){
		type.compareTo("int")==0 || type.compareTo("long")==0 || type.compareTo("float")==0|| type.compareTo("double")==0 || type.compareTo("boolean")==0 || type.compareTo("char")==0;
	}

}