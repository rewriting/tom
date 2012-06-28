package templates

import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import tom.mapping.model.Mapping

class TomTemplate {

	def public static  main(Mapping m) {
	'''
		%include { string.tom }
		%include { boolean.tom }
		%include { int.tom }
		%include { long.tom }
		%include { float.tom }
		%include { double.tom }
		
		private static <O> EList<O> enforce(EList l) {
				return l;
		}
		
		private static <O> EList<O> append(O e,EList<O> l) {
		       l.add(e);
		       return l;
		}
	'''
	}


//«DEFINE primitiveTerminal FOR EClassifier-»«ENDDEFINE»
def dispatch primitiveTerminal(EEnum e) {
'''
%typeterm «e.name» {
	implement 		{«e.name»}
	is_sort(t) 		{$t instanceof «e.name»}
	equals(l1,l2) 	{$l1==$l2}
}
'''
}
//«DEFINE primitiveTerminal FOR EDataType»
//«LET isPrimitive(instanceTypeName) AS primitive»
//%typeterm «name» {
//	implement 		{«instanceTypeName»}
//	is_sort(t) 		{«IF primitive»true«ELSE»$t instanceof «instanceTypeName»«ENDIF»}
//	equals(l1,l2) 	{«IF primitive»$l1==$l2«ELSE»$l1.equals($l2)«ENDIF»}
//}
//«ENDLET»
//«ENDDEFINE»

	def dispatch primitiveTerminal(EClassifier e) {}


//	def dispatch primitiveTerminal (EDataType e) {
//		val primitive = isPrimitive(instanceTypeName);
//		'''
//			%typeterm «e.name» {
//				implement 		{«instanceTypeName»}
//				is_sort(t) 		{«IF primitive»true«ELSE»$t instanceof «instanceTypeName»«ENDIF»}
//				equals(l1,l2) 	{«IF primitive»$l1==$l2«ELSE»$l1.equals($l2)«ENDIF»}
//			}
//		'''	
//	}
	
	def dispatch primitiveTerminal (EPackage e) {
		e.EClassifiers.map[c|primitiveTerminal(c)]
		e.ESubpackages.map[c|primitiveTerminal(c)]
	}

//«DEFINE primitiveTerminals FOR EPackage-»
//«FOREACH EClassifiers AS c-»«EXPAND primitiveTerminal FOR c-»«ENDFOREACH-»
//«FOREACH ESubpackages AS subp-»«EXPAND primitiveTerminals FOR subp-»«ENDFOREACH-»
//«ENDDEFINE»

def termFile() {
	'''
	// Primitive terminals (enum and data types)
	'''
	//getAllRootPackages().map[e|primitiveTerminals(e)]
	'''
	// Terminals
	'''
	//terminals.map[e|tom::Terminals::terminal(e)]
	'''
	// List Terminals
	'''
	//allListTerminals.map[e|tom::Terminals::listTerminal(e)]
}
//«FILE this.name+"_terms.tom"-»
//// Primitive terminals (enum and data types)
//«FOREACH getAllRootPackages() AS p»«EXPAND primitiveTerminals FOR p»
//«ENDFOREACH»    
//// Terminals
//«FOREACH terminals AS t-»
//«EXPAND tom::Terminals::terminal(this) FOR t-»
//«ENDFOREACH-»
//// List Terminals
//«FOREACH allListTerminals AS lt»
//«EXPAND tom::Terminals::listTerminal(this) FOR lt»
//«ENDFOREACH»
//«ENDFILE»

	
}