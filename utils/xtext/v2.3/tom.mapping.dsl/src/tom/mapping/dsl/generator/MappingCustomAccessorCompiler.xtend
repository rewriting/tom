package tom.mapping.dsl.generator

import com.google.inject.Inject
import model.Mapping
import tom.mapping.dsl.generator.java.CustomOperatorsCompiler

class MappingCustomAccessorCompiler {
	
	@Inject CustomOperatorsCompiler injcusopco
	
	def main(Mapping m) {
		injcusopco.main(m);
	}
	
}