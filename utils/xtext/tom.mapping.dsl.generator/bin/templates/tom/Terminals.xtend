
package templates.tom

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
import templates.Naming

class Terminals {


def terminal(Mapping m, Terminal t) {
	if  (t.many) {
		listTerminal(m,t);
	} else {
	'''
	%typeterm «t.name» {
		implement 		{«t.class_.name»}
		is_sort(t) 		{$t instanceof «t.class_.name»}
		equals(l1,l2) 	{($l1!=null && $l1.equals($l2)) || $l1==$l2}
	}
	'''
	}
}



def listTerminal(Mapping m, Terminal t) {
	val name  = Naming::name(t,m);

'''
%typeterm «name»{
	 implement { EList<«t.class_.name»> }
     is_sort(t) «listTest (t)»
	equals(l1,l2) 	{($l1!=null && $l1.equals($l2)) || $l1==$l2}
}

%oparray «name» «name» («m.getTerminal(t.class_,false).name»*) {
 	 is_fsym(t) «listTest(t)»
     make_empty(n) { new BasicEList<«t.class_.name»>($n) }
     make_append(e,l) { append($e,$l) }
     get_element(l,n) { $l.get($n) }
     get_size(l)      { $l.size() }
}
'''
}

def listTest(Terminal t) {
	'''
	{ $t instanceof EList<?> &&  
                           (((EList<«t.class_.name»>)$t).size() == 0 
                         || (((EList<«t.class_.name»>)$t).size()>0 && ((EList<«t.class_.name»>)$t).get(0) instanceof «t.class_.name»))} 
	}
	'''
}

}
