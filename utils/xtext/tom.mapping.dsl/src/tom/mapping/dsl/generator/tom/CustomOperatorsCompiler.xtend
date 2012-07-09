package tom.mapping.dsl.generator.tom

import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Accessor
import tom.mapping.model.Mapping
import tom.mapping.model.Operator
import tom.mapping.model.UserOperator
import org.eclipse.emf.ecore.*
import org.eclipse.xtext.generator.IFileSystemAccess

class CustomOperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	String prefix = "tom"
	
	
	def compile(Mapping m, IFileSystemAccess fsa) {
		fsa.generateFile(prefix+"/"+m.getCustomOperatorsClass()+".java", m.main())
	}
	
	def main(Mapping map) {
		if(map.operators.filter[e | UserOperator.isInstance(e)].size>0) {			
			'''
			public class «getCustomOperatorClass()» {
				«for(op: operators)» {
				«operator(map, op);
				}»
				
			}
			'''
		}
	}
	

	def operator(Mapping map, Operator op){}
	
	def operator(Mapping map, UserOperator usop){
		for(a: usop.accessors) {
			'''
			«accessor(usop,a)»
			«test(usop)»
			«make(usop)»
			'''
		}
	}
	
	
	def accessor(UserOperator op, Accessor acc) {
		var ParametersCompiler paco;
		'''
		public static «paco.javaTerminalType(acc.slot.type)»
		«getCustomOperatorSlotAccessorName(acc);»
		«(paco.javaTerminalType(op.type)» t) {
			return «java»
		}
		'''
	}


	def test(UserOperator usop) {
		var ParametersCompiler paco;
		'''
		public static boolean is«name.toFirstUpper()»
			(«paco.javaTerminalType(usop.type)» t) {
			return «test»;
		}
		'''
	}


	def make(UserOperator usop) {
		var ParametersCompiler paco;
		'''
		public static «paco.javaTerminalType(usop.type)» make «name.toFirstUpper()»
		(«for(Accessor as: accessors)») {
			«paco.javaParameter(acc.slot)» {
				return «make»
			}
		}
		'''
	}




}