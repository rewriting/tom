// Licence
package tom.mapping.dsl.generator

import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Mapping
import tom.mapping.model.Accessor
import tom.mapping.model.UserOperator
import tom.mapping.model.Terminal


class TomMappingNaming {
	
	def String getCustomOperatorSlotAccessorName(Accessor accessor){
		"get"+(accessor.eContainer() as UserOperator).name.toFirstUpper()+"Slot"+accessor.slot.name.toUpperCase();
	}
	
	def String getCustomOperatorsClass(Mapping mapping){
		mapping.name.toFirstUpper()+"CustomAccessors";
	}
	
	def String getChildrenGetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenGetter";
	}
	
	def String getChildrenSetterName(EPackage p){
		p.name.toFirstUpper()+"ChildrenSetter";
	}
	
	def name(Terminal t, Mapping m) {
		if(t.many && t.isInferedList(m)) {
			t.name+"List"
		} else {
			t.name;
		}
	}
	
	def factoryName(Mapping m) {
		m.name.toFirstUpper()+"UserFactory";
	}

	def tomFactoryName(Mapping m) {
		m.name.toFirstUpper()+"TomFactory";
	}
	
	def String tomFactoryQualifiedName(Mapping m) {
		m.prefix+"."+m.name+"."+"internal"+"."+m.tomFactoryName();
	}
	
	def String getPackagePrefix(String prefix) {
		if(prefix==null || prefix.compareTo("") == 0) {
			"";
		} else {
			prefix+".";
		}
	}
	
	def private Boolean isInferedList(Terminal t, Mapping m) {
		(t.many && !(m.terminals.contains(t) || m.externalTerminals.contains(t)))
	}
	
}