package templates

import tom.mapping.model.Accessor
import tom.mapping.model.UserOperator
import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Terminal
import org.eclipse.emf.ecore.EReference
import java.util.List
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EStructuralFeature
import tom.mapping.model.FeatureParameter
import tom.mapping.model.SettedFeatureParameter
import tom.mapping.model.FeatureException
import tom.mapping.model.ClassOperator

class Extensions {
	  

def Iterable<EReference> getDefaultParameters(EClass c, Mapping mapping) {	
	c.EAllReferences.filter([e|isParameterFeature(e,mapping)]);
}

def Iterable<FeatureParameter> getCustomParameters(ClassOperator c) {
	c.parameters.filter([e|isExplicitParameter(e)]);
}

def Iterable<SettedFeatureParameter> getSettedCustomParameters(ClassOperator c) {
	c.parameters.filter([e| e instanceof SettedFeatureParameter]) as Iterable<SettedFeatureParameter>;
}

def  boolean isParameterFeature(EStructuralFeature f,Mapping mapping) {
	true;
}

def  boolean isParameterFeature(EReference f,Mapping mapping) {
	 f.derived==false 
	&& f.volatile==false 
	&& (f.containment|| f.EOpposite==null)
	&& (mapping.getTerminal(f.EReferenceType,false)!=null || mapping.getTerminal(f.EReferenceType,true)!=null);
}

def dispatch boolean isExplicitParameter(FeatureParameter pf) {
	true; 
}
def dispatch boolean isExplicitParameter(SettedFeatureParameter pf) {
	false; 
	
}
def dispatch boolean isExplicitParameter(FeatureException pf) {
	false; 
	
}

def Iterable<EPackage> getAllSubPackages(EPackage p) {
	val f= p.ESubpackages.map[e|getAllSubPackages(e)];
	return f.flatten();
}

def boolean isSelected(EPackage p, Mapping m) {
	p.EClassifiers.filter[e|e instanceof EClass].filter[e|m.getTerminal(e as EClass,false)!=null].size >0;
}

def Iterable<EPackage> getAllPackages(Mapping m) {
	m.metamodelPackages.map[e|getAllSubPackages(e)].flatten().filter[e|isSelected(e,m)];
}

def String packageToPath(String s) {
	if(s!=null) s.replaceAll("\\.", "/") else "";
	

}
def List<EPackage> getAllRootPackages(Mapping mapping) {
	mapping.metamodelPackages;
	
}
def boolean isPrimitive(String type) {
	type.compareTo("int")==0 || type.compareTo("long")==0 || type.compareTo("float")==0|| type.compareTo("double")==0 || type.compareTo("boolean")==0 || type.compareTo("char")==0;
}
}