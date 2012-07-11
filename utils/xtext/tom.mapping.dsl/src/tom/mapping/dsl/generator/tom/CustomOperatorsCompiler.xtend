package tom.mapping.dsl.generator.tom

import org.eclipse.xtext.generator.IFileSystemAccess
import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Accessor
import tom.mapping.model.Mapping
import tom.mapping.model.Operator
import tom.mapping.model.UserOperator
import com.google.inject.Inject

class CustomOperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	@Inject ParametersCompiler injpa
	
	String prefix = "tom"
	
	
	def compile(Mapping m, IFileSystemAccess fsa) {
		fsa.generateFile(prefix+"/"+m.getCustomOperatorsClass()+".java", m.main())
	}
	
	def main(Mapping map) {
		if(map.operators.filter[e | e instanceof UserOperator].size>0) {			
			'''
			public class «map.getCustomOperatorsClass()» {
				«FOR op: map.operators»
					«map.operator(op)»
				«ENDFOR»
				
			}
			'''
		}
	}
	

	def operator(Mapping map, Operator op){}
	
	def operator(Mapping map, UserOperator usop){
		for(a: usop.accessors) {
			usop.accessor(a) 
			}
			usop.test()
			usop.make()
	}
	
	
	def accessor(UserOperator op, Accessor acc) {
		'''
		public static «injpa.javaTerminalType(acc.slot.type)»
		«acc.getCustomOperatorSlotAccessorName()»
		(«injpa.javaTerminalType(op.type)» t) {
			return «acc.java»;
		}
		'''
	}


	def test(UserOperator usop) {
		'''
		public static boolean is«usop.name.toFirstUpper()»
			(«injpa.javaTerminalType(usop.type)» t) {
			return «usop.test()»;
		}
		'''
	}


	def make(UserOperator usop) {
		'''
		public static «injpa.javaTerminalType(usop.type)» make «usop.name.toFirstUpper()»
		(«FOR acc: usop.accessors SEPARATOR ","»
			«injpa.javaParameter(acc.slot)»
		«ENDFOR») {
			return «usop.make()»
			}
		'''
	}

}