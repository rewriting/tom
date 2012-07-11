// Licence
package tom.mapping.dsl.generator

import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EClass

class ChildrenGetterSetter {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	// Getter
	
	def dispatch getter(Mapping mapping, EPackage ep) {
		'''
		private static class «ep.getChildrenGetterName()» extends «ep.name.toFirstUpper()»Switch<Object[]> implements IChildrenGetter{
			public final static «ep.getChildrenGetterName()» INSTANCE = new «ep.getChildrenGetterName()»();
			
			private «ep.getChildrenGetterName()»(){}
			
			public Object[] children(Object i) {				
				Object[] children = doSwitch((EObject) i);
				if(children !=null) {
					return children
				} else { return new Object[0]; }
			}
			«FOR c: ep.EClassifiers»
				«mapping.getter(c)»
			«ENDFOR»
		}
		'''
}
// Reprendre ici
	
	def dispatch getter(Mapping mapping, EClassifier ecf) {}
	
	
	def dispatch getter(Mapping mapping, EClass ec) {
		val parameters = ec.getDefaultParameters(mapping);
		'''
		«IF parameters.size() > 0»
			public Object[] case«ec.name.toFirstUpper()»(«ec.name» o) {
				List<Object> l = new ArrayList<Object>();
				for (param: parameters) {
					if(o.get«param.name.toFirstUpper()»() != null) { 
						l.add(o.get«param.name.toFirstUpper()»());
					} '''
				}
				'''return l.toArray();'''
		«ENDFOR»'''	
	}
	
	// Setter
	
	def dispatch setter(Mapping mapping, EPackage ep) {
		'''
		private static class «getChildrenSetterName(ep)» extends «ep.name.toFirstUpper()»Switch<Object[]> implements IChildrenSetter{
			public final static «getChildrenSetterName(ep)» INSTANCE = new «getChildrenSetterName(ep)»();
			
			private «getChildrenSetterName(ep)»(){}
			
			public Object set(Object i, Object[] children) {				
				ep.children = children;
				return doSwitch((EObject) i);
			}
			«FOR c: ep.EClassifiers»
				«setter(mapping, c)»
			«ENDFOR»
		}
		'''
}

	
	def setter(Mapping mapping, EClassifier ecf) {
		'''
		'''
	}
	
	
	def dispatch setter(Mapping mapping, EClass ec) {
		val parameters = getDefaultParameters(ec, mapping).filter[e | !e.many];
		if(parameters.size() > 0) {
			'''public Object[] case«ec.name.toFirstUpper()»(«ec.name» o) { '''
				for(p: parameters) {
					'''o.set«p.name.toFirstUpper()»((«p.EReferenceType.name»)children[«parameters.indexOf(p)»]);'''
					}
				'''return o;'''
			}
		}
	
}