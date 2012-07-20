package tom.mapping.dsl.generator.tom

import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EPackage
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Mapping
import tom.mapping.model.Terminal

class TerminalsCompiler {

extension TomMappingExtensions = new TomMappingExtensions()

	def terminal(Mapping m, Terminal t) {
		if  (t.many) {
			m.listTerminal(t);
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
		val name  = t.name(m);
	
	'''
	%typeterm «name»{
		 implement { EList<«t.class_.name»> }
	     is_sort(t) «t.listTest()»
		equals(l1,l2) 	{($l1!=null && $l1.equals($l2)) || $l1==$l2}
	}
	
	%oparray «name» «name» («m.getTerminal(t.class_,false).name»*) {
	 	 is_fsym(t) «t.listTest()»
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
	
	def dispatch primitiveTerminal(EPackage c)'''
		«FOR classifier :c.eAllContents.filter(typeof(EClassifier)).toList»
			«classifier.primitiveTerminal()»
		«ENDFOR»
	'''
	
	def dispatch primitiveTerminal(EClassifier c)''''''
	
	
	def dispatch primitiveTerminal(EEnum c)'''
		%typeterm «c.name» {
			implement 		{«c.name»}
			is_sort(t) 		{$t instanceof «c.name»}
			equals(l1,l2) 	{$l1==$l2}
		}
	'''
	
	def dispatch primitiveTerminal(EDataType c)'''
		«val primitive = c.instanceTypeName.primitive»
		%typeterm «c.name» {
			implement 		{«c.instanceTypeName»}
			is_sort(t) 		{«IF primitive»true«ELSE»$t instanceof «c.instanceTypeName»«ENDIF»}
			equals(l1,l2) 	{«IF primitive»$l1==$l2«ELSE»$l1.equals($l2)«ENDIF»}
		}
	'''
}
