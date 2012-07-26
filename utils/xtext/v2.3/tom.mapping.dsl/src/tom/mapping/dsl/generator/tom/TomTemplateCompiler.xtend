package tom.mapping.dsl.generator.tom

import com.google.inject.Inject
import model.Mapping
import model.Module
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EPackage
import org.eclipse.xtext.generator.IFileSystemAccess
import tom.mapping.dsl.generator.TomMappingExtensions

class TomTemplateCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	@Inject TerminalsCompiler terminals
	@Inject OperatorsCompiler injop
	String prefix="tom/"
	
	
	def compile(Mapping m, IFileSystemAccess fsa){
	
		fsa.generateFile(prefix+"common.tom",m.main())
		fsa.generateFile(prefix+m.name+"_terminals.tom",m.terminals())
		fsa.generateFile(prefix+m.name+"_operators.tom",m.operators())
		fsa.generateFile(prefix+m.name+"_defaultOperators.tom",m.defaultOperators())
		for(module: m.modules){
			fsa.generateFile(prefix+m.name+"_"+module.name+".tom",m.module(module))
		}
	}
	
	def main(Mapping m){ '''
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
	
	def terminals(Mapping m) { '''
// Primitive terminals (enu and data types)
«FOR p: m.getAllRootPackages()»
«terminals.primitiveTerminal(p)»
«ENDFOR»    
// Terminals
«FOR t: m.terminals»
«terminals.terminal(m,t)»
«ENDFOR»
// List Terminals
«FOR lt:m.allListTerminals»
«terminals.listTerminal(m,lt)»
«ENDFOR»
	'''
	}
	
	def operators(Mapping m) {'''
	// User operators
	«FOR op: m.operators»
	«injop.operator(m, op)»
	«ENDFOR»
	'''
	}
	
	def defaultOperators(Mapping m) { '''
	// Default operators
	«FOR op: m.allDefaultOperators»
	«injop.classOperator(m, op.name, op)»
	«ENDFOR»
	/* PROTECTED REGION ID(op.name+"_mapping_user") ENABLED START */
	// Protected user region
	/* PROTECTED REGION END */
	'''
	}

	
	def module(Mapping m, Module mod) {'''
	/* PROTECTED REGION ID(module.name+"_mapping_user") ENABLED START */
	// Protected user region
	/* PROTECTED REGION END */
	
	«FOR op: mod.operators»
	«injop.operator(m,op)»
	«ENDFOR»
	'''
	}
	
	
	def primitiveTerminals(EPackage epa) {
		
		for(c: epa.EClassifiers) {
		c.primitiveTerminal();
		}
		
		for(subp: epa.ESubpackages) {
			subp.primitiveTerminals();
		}
	}
		
		
	def dispatch primitiveTerminal(EClassifier ecl) {
	}	
		
	def dispatch primitiveTerminal(EEnum enu) {
		'''
		%typeterm «enu.name» {
			implement {«enu.name»}
			is_sort(t) {$t instanceof «enu.name»}
			equals(l1,l2) {$l1==$l2}
		}	
		'''
	}	
		
	def dispatch primitiveTerminal(EDataType edaty){
		val primitive = edaty.instanceTypeName.isPrimitive();
		'''
		%typeterm «edaty.name» {
			implement {«edaty.instanceTypeName»}
			is_sort(t) {«IF primitive»true«ELSE»{$t instanceof «edaty.instanceTypeName»«ENDIF»}
			equals(l1,l2) {«IF primitive»{$l1=$l2}«ELSE»{l1.equals($l2)«ENDIF»}
		}'''	
	}	
			
}