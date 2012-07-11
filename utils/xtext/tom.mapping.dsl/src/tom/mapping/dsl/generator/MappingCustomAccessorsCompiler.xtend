// Licence
package tom.mapping.dsl.generator

import com.google.inject.Inject
import tom.mapping.dsl.generator.tom.CustomOperatorsCompiler
import tom.mapping.model.Mapping

class MappingCustomAccessorsCompiler {
	
	@Inject CustomOperatorsCompiler injcusopco
	
	def main(Mapping m) {
		injcusopco.main(m);
	}
	
}