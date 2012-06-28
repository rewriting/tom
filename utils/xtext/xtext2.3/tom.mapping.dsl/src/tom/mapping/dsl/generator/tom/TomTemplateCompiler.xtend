package tom.mapping.dsl.generator.tom

import tom.mapping.model.Mapping
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Module
import org.eclipse.xtext.generator.IFileSystemAccess
import com.google.inject.Inject

class TomTemplateCompiler {
	extension TomMappingExtensions = new TomMappingExtensions()
	@Inject TerminalsCompiler terminals
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
	
	def main(Mapping m)'''
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
	'''
	
	def defaultOperators(Mapping m)'''
	'''
	
	def module(Module m)'''
	'''
}