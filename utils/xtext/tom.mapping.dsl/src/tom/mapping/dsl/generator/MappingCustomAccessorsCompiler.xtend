// Licence
package tom.mapping.dsl.generator

import tom.mapping.model.*
import org.eclipse.emf.ecore.*
import tom.mapping.dsl.generator.tom.CustomOperatorsCompiler
import com.google.inject.Inject

class MappingCustomAccessorsCompiler {
	
	@Inject CustomOperatorsCompiler injcusopco
	
	def main(Mapping m) {
		injcusopco.main(m);
	}
	
}