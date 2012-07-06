// Licence
package tom.mapping.dsl.generator

import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EClass
import tom.mapping.dsl.generator.NamingCompiler

class ChildrenGetterSetter {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	extension NamingCompiler = new NamingCompiler()
	
	NamingCompiler nam
	
	// Getter
	
	def dispatch getter(Mapping mapping, EPackage ep) {
		'''
		private static class «getChildrenGetterName(ep)» extends «ep.name.toFirstUpper()»Switch<Object[]> implements IChildrenGetter{
			public final static «getChildrenGetterName(ep)» INSTANCE = new «getChildrenGetterName(ep)»();
			
			private «getChildrenGetterName(ep)»(){}
			
			public Object[] children(Object i) {				
				Object[] children = doSwitch((EObject) i);
				if(children !=null) {
					return children
				} else { return new Object[0]; }
			}
			«for(EClassifier c: ep.EClassifiers) {
				getter(mapping, c);
			}»
		}
		'''
}

	
	def getter(Mapping mapping, EClassifier ecf) {
		'''
		'''
	}
	
	
	def dispatch getter(Mapping mapping, EClass ec) {
		val parameters = getDefaultParameters(ec, mapping);
		if(parameters.size() > 0) {
			'''
			public Object[] case«ec.name.toFirstUpper()»(�ec.name» o) {
				List<Object> l = new ArrayList<Object>();
				«for(EReference param: parameters)» {
					if(o.get«param.name.toFirstUpper()»() != null)
					l.add(o.get«param.name.toFirstUpper()»());
					}
				return l.toArray();
			}
			
			'''	
		}
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
			«for(EClassifier c: ep.EClassifiers) {
				setter(mapping, c);
			}»
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
			'''
			public Object[] case«ec.name.toFirstUpper()»(�ec.name» o) {
				«for(EReference p: parameters)» {
					o.set«p.name.toFirstUpper()»((«p.EReferenceType.name»)children[«parameters.indexOf(p)»]);
					}
				return o;
			}
			
			'''	
		}
	}
	
}