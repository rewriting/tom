package tom.mapping.dsl.generator

import com.google.inject.Inject
import tom.mapping.model.Mapping
import tom.mapping.dsl.java.CustomOperatorsCompiler

class MappingCustomAccessorCompiler {
	
	@Inject CustomOperatorsCompiler injcusopco
	
	def main(Mapping m) {
		injcusopco.main(m);
	}
	
}