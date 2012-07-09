package tom.mapping.dsl.generator.tom

import tom.mapping.model.Mapping
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Module
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.inject.Inject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EDataType

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
			fsa.generateFile(prefix+m.name+"_"+module.name+".tom",module.module())
		}
	}
	
	def main(Mapping m) '''
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
	
	def terminals(Mapping m)'''
// Primitive terminals (enum and data types)
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
	
	def operators(Mapping m)'''
	// User operators
	«FOR op: m.operators()»
	«injop.operator(m, op)»
	«ENDFOR»
	'''
	
	def defaultOperators(Mapping m)'''
	// Default operators
	«FOR op: m.allDefaultOperators»
	«injop.classOperator(m, op.name, op)»
	«ENDFOR»
	/* PROTECTED REGION ID(op.name+"_mapping_user") ENABLED START */
	// Protected user region
	/* PROTECTED REGION END */
	'''

	
	def module(Module m)'''
	/* PROTECTED REGION ID(module.name+"_mapping_user") ENABLED START */
	// Protected user region
	/* PROTECTED REGION END */
	
	«FOR op: m.operators»
	«injop.operator(m,op)»
	«ENDFOR»
	'''
	
	
	def primitiveTerminals(EPackage epa) {
		
		for(c: epa.EClassifiers) {
		primitiveTerminal(c);
		}
		
		for(subp: epa.ESubpackages) {
			primitiveTerminals(subp);
		}
	}
		
		
	def dispatch primitiveTerminal(EClassifier ecl) {
		''''''
	}	
		
	def dispatch primitiveTerminal(EEnum enum) {
		'''
		%typeterm «enum.name» {
			implement {«enum.name»}
			is_sort(t) {$t instanceof «enum.name»}
			equals(l1,l2) {$l1==$l2}
		}	
		'''
	}	
		
	def dispatch primitiveTerminal(EDataType edaty){
		val primitive = isPrimitive(edaty.instanceTypeName);
		'''
		%typeterm «edaty.name» {
			implement {«edaty.instanceTypeName»}
			is_sort(t) {«if (primitive)»{true} «else {$t instanceof «edaty.instanceTypeName»} }
			equals(l1,l2) {«if(primitive)»{$l1=$l2} «else» {l1.equals($l2)} }
		}
		'''	
		}	
			
}