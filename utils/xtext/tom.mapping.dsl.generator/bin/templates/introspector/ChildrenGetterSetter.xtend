package templates.introspector

import tom.mapping.model.Mapping
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EClass
import templates.Naming
import templates.Extensions

class ChildrenGetterSetter {
	Naming a
	def getter(EPackage p, Mapping mapping) {
		
		'''
		private static class «Naming::getChildrenGetterName(p)» extends «p.name.toFirstUpper()»Switch<Object[]> implements IChildrenGetter{
				public final static «Naming::getChildrenGetterName(p)» INSTANCE = new «Naming::getChildrenGetterName(p)»();
				
				private «Naming::getChildrenGetterName(p)»(){}
				
				public Object[] children(Object i) {
					Object[] children = doSwitch((EObject) i);
					return children!=null ? children: new Object[0];
				}
				
				«FOR c:p.EClassifiers»«getter(c,mapping)»«ENDFOR»
			
		}
		'''
}

def getter(EClassifier c,Mapping mapping) {
'''
'''
}

def getter(EClass c, Mapping mapping) { 
	val hack = new Extensions()
	val parameters = hack.getDefaultParameters(c,mapping)
	if(parameters.size>0)
'''
		public Object[] case«c.name.toFirstUpper()»(«c.name» o){
			List<Object> l = new ArrayList<Object>();
			«FOR param:parameters »
			if(o.get«param.name.toFirstUpper()»() !=null)
				l.add(o.get«param.name.toFirstUpper()»());
			«ENDFOR»
			return l.toArray();
		}
'''
		
}

	
}