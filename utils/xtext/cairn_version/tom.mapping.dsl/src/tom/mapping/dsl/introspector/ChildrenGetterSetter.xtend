// Licence
package tom.mapping.dsl.introspector

import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EPackage
import tom.mapping.model.Mapping
import java.util.ArrayList
import org.eclipse.emf.ecore.EReference
import tom.mapping.dsl.generator.TomMappingExtensions

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
	
	def dispatch getter(Mapping mapping, EClassifier ecf) {}
	
	
	def dispatch getter(Mapping mapping, EClass ec) {
		val parameters = ec.getDefaultParameters(mapping);
		'''
		«IF parameters.size() > 0»
			public Object[] case«ec.name.toFirstUpper()»(«ec.name» o) {
				List<Object> l = new ArrayList<Object>();
				«FOR param: parameters»
					if(o.get«param.name.toFirstUpper()»() != null) { 
						l.add(o.get«param.name.toFirstUpper()»());
					}
				«ENDFOR»
				return l.toArray();
		«ENDIF»'''	
	}
	
	// Setter
	
	def dispatch setter(Mapping mapping, EPackage ep) {
		'''
		private static class «ep.getChildrenSetterName()» extends «ep.name.toFirstUpper()»Switch<Object[]> implements IChildrenSetter{
			public final static «ep.getChildrenSetterName()» INSTANCE = new «ep.getChildrenSetterName()»();
			
			private «ep.getChildrenSetterName()»(){}
			
			public Object set(Object i, Object[] children) {				
				ep.children = children;
				return doSwitch((EObject) i);
			}
			«FOR c: ep.EClassifiers»
				«mapping.setter(c)»
			«ENDFOR»
		}
		'''
}

	
	def dispatch setter(Mapping mapping, EClassifier ecf) {
	}
	
	
	def dispatch setter(Mapping mapping, EClass ec) {
		val parameters = ec.getDefaultParameters(mapping).filter(e | !e.many)  as ArrayList<EReference>;
		'''
		«IF parameters.size() > 0»
			public Object[] case«ec.name.toFirstUpper()»(«ec.name» o) { 
				«FOR p: parameters»
					o.set«p.name.toFirstUpper()»((«p.EReferenceType.name»)children[«parameters.indexOf(p)»]);
					«ENDFOR»
				return o;
		«ENDIF»
		'''
	}
	

}