package tom.mapping.dsl.generator.tom

import tom.mapping.dsl.generator.TomMappingExtensions
import tom.mapping.model.Accessor
import tom.mapping.model.Mapping
import tom.mapping.model.Operator
import tom.mapping.model.UserOperator

class CustomOperatorsCompiler {
	
	extension TomMappingExtensions = new TomMappingExtensions()
	
	
	/* def main(Mapping map) {
		if(map.operators.filter[e | UserOperator.isInstance(e)].size>0) {
			var File getCustomOperatorsClass()+".java"; // File ?
			
			'''
			public class «getCustomOperatorClass()» {
				«for(Operator op: operators)» {
				«operator(map, op);
				}»
				
			}
			'''
		}
	} */
	

	def operator(Mapping map, Operator op){}
	
	def operator(Mapping map, UserOperator usop){
		for(Accessor a: usop.accessors) {
			'''
			«accessor(usop,a);»
			«test(usop);»
			«make(usop);»
			'''
		}
	}
	
	
	// >_<' ?
	
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