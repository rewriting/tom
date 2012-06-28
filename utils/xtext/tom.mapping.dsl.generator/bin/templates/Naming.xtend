package templates

import tom.mapping.model.Accessor
import tom.mapping.model.UserOperator
import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Terminal

class Naming {
	
	def public static  String getCustomOperatorSlotAccessorName(Accessor accessor) {
		return "get"+((accessor.eContainer() as UserOperator)).name.toFirstUpper()+"Slot"+accessor.slot.name.toUpperCase();
	} 
	
	def public static String getCustomOperatorsClass(Mapping mapping) {
		mapping.name.toFirstUpper()+"CustomAccessors";
	}
	def public static String getChildrenGetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenGetter";
	}
		
	def public static String getChildrenSetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenSetter";
		
	}
	def public static String name(Terminal t, Mapping m){
		if(t.many&&isInferedList(t,m)) t.name+"List" else t.name;
	}
		
	def public static String factoryName(Mapping m){
		m.name.toFirstUpper()+"UserFactory";
	}
	
	def public static String tomFactoryName(Mapping m){
		m.name.toFirstUpper()+"TomFactory";
	}
		
	def public static String tomFactoryQualifiedName(Mapping m){
		m.prefix+"."+m.name+"."+"internal"+"."+tomFactoryName(m);
	}
	
	def public static String getPackagePrefix(String prefix){
		if (prefix==null || prefix.compareTo("")==0) "" else prefix+".";
	}
		
	def public static Boolean isInferedList(Terminal t, Mapping m){
		t.many&&!(m.terminals.contains(t)||m.externalTerminals.contains(t));
	}
}
