// Licence
package tom.mapping.dsl.generator

import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EClass
import templates.Naming
import templates.Extensions

class ChildrenGetterSetter {
	
	Naming nam
	
	// Getter
	
	def getter(Mapping mapping, EPackage ep) {
		'''
		private static class «nam::getChildrenGetterName(ep)» extends «ep.name.toFirstUpper()» Switch<Object[]> implements IChildrenGetter{
			public final static «nam::getChildrenGetterName(ep)» INSTANCE = new «nam::getChildrenGetterName(ep)»();
			
			private «nam::getChildrenGetterName(ep)»(){}
			
			public Object[] children(Object i) {				
				Object[] children = doSwitch((EObject) i);
				return children !=null ? children: new Object[0];
			}
			«for(c: ep.EClassifiers) {
				getter(mapping, c);
			}»
		}
		'''
	}
	
	
	def getter(Mapping mapping, EClassifier ecf) {
		'''
		'''
	}
	
	
	def getter(Mapping mapping, EClass ec) {
		val ext = new Extensions();
		val parameters = ext.getDefaultParameters(ec, mapping);
		if(parameters.size() > 0) {
			'''
			public Object[] case«ec.name.toFirstUpper()»(�ec.name» o) {
				List<Object> l = new ArrayList<Object>();
				«for(param: parameters) {»
					if(o.get«param.name.toFirstUpper()»() != null)
					l.add(o.get«param.name.toFirstUpper()»());
					«}»
				return l.toArray();
			}
			
			'''	
		}
	}
	
	// Setter
	
	def setter(Mapping mapping, EPackage ep) {
		'''
		private static class «nam::getChildrenSetterName(ep)» extends «ep.name.toFirstUpper()» Switch<Object> implements IChildrenSetter{
			public final static «nam::getChildrenSetterName(ep)» INSTANCE = new «nam::getChildrenSetterName(ep)»();
			private Object[] children;
			
			private «nam::getChildrenSetterName(ep)»(){}
			
			public Object[] set(Object i, Object[] children) {				
				this.children = children;
				return doSwitch((EObject) i);
			}
			
			«for(c: ep.EClassifiers) {
				setter(mapping, c);
			}»
		}
		'''
	}
	
	
	def setter(Mapping mapping, EClassifier ecf) {
		'''
		'''
	}
	
	
	def setter(Mapping mapping, EClass ec) {
		val ext = new Extensions();
		val parameters = ext.getDefaultParameters(ec, mapping).filter(e|e.many);
		if(parameters.size() > 0) {
			'''
			public Object case«ec.name.toFirstUpper()»(�ec.name» o) {
				«for(p: parameters) {»
					o.set«p.name.toFirstUpper()»((«p.EReferenceType.name»)children[«parameters.indexOf(p)»]);
					«}»
				return o;
			}
			'''	
		}
	}
	
}