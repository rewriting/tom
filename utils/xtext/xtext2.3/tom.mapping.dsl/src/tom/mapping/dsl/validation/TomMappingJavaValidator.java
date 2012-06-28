package tom.mapping.dsl.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.Check;

import tom.mapping.dsl.scoping.TomMappingScopeProvider;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.EnumLiteralValue;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Mapping;
import tom.mapping.model.ModelPackage;
import tom.mapping.model.Operator;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.Terminal;

public class TomMappingJavaValidator extends AbstractTomMappingJavaValidator {

	// @Check
	// public void checkGreetingStartsWithCapital(Greeting greeting) {
	// if (!Character.isUpperCase(greeting.getName().charAt(0))) {
	// warning("Name should start with a capital", MyDslPackage.GREETING__NAME);
	// }
	// }

	@Check
	public void checkOperatorException(ClassOperator o) {
		Set<EStructuralFeature> features = new HashSet<EStructuralFeature>();

		for (FeatureParameter ex : o.getParameters()) {
			if (features.contains(ex.getFeature())) {
				String type = ex.getFeature().getEType().getName();
				if (ex.getFeature().isMany())
					type += "*";
				error("Feature " + ex.getFeature().getName() + "::" + type
						+ " is already in parameters",
						ModelPackage.eINSTANCE.getClassOperator_Parameters());
			} else
				features.add(ex.getFeature());
		}
		if (features.size() > 0) {
			int n = getAllSelectableFeatures(o).size();
			if (features.size() < n) {
				error("Not enough parameters",
						ModelPackage.eINSTANCE.getClassOperator_Parameters());
			} else if (features.size() > n) {
				error("Too much parameters",
						ModelPackage.eINSTANCE.getClassOperator_Parameters());
			}
		}
	}
	
	@Check
	public void checkTerminals(Terminal t){
		Mapping mapping  = (Mapping) t.eContainer();
		for(Terminal term: mapping.getTerminals()){
			if(term.getName().compareTo(t.getName())==0 && t!=term)
				error("Duplicate Terminal ID: "+term.getName(),ModelPackage.eINSTANCE.getTerminal_Name());
			if(term.getClass_()==t.getClass_() && term.isMany()==t.isMany()&& term!=t)
				warning("A terminal for "+t.getClass_().getName()+ " already exist",ModelPackage.eINSTANCE.getTerminal_Class());
		}
	}

	@Check
	public void checkClassOperator(ClassOperator op){
		if(op.eContainer() instanceof Mapping) {
			Mapping mapping  = (Mapping) op.eContainer();
			boolean hasSettedParameters = hasSettedParameters(op);
			for(Operator p: mapping.getOperators()){
				if(p!=op){
					if(p.getName().compareTo(op.getName())==0){
						error("Duplicate Operator ID: "+op.getName(),ModelPackage.eINSTANCE.getClassOperator_Parameters());
					}
					if(p instanceof ClassOperator){
						ClassOperator cp = (ClassOperator)p;
						if(cp.getClass_()==op.getClass_()){
							if(!hasSettedParameters && !hasSettedParameters(cp))
								error("There is already a Class Operator for "+op.getClass_().getName(), ModelPackage.eINSTANCE.getClassOperator_Class());
						}
					}
				}
			}
		}
	}
	
	private boolean hasSettedParameters(ClassOperator op){
		for(FeatureParameter p: op.getParameters()){
			if(p instanceof SettedFeatureParameter)
				return true;
		}
		return false;
	}
//	@Check
//	public void checkImport(Import i) {
//		try {
//			// Obtain a new resource set
//			ResourceSet resSet = new ResourceSetImpl();
//			// Get the resource
//			Resource resource = resSet.getResource(
//					URI.createURI(i.getImportURI()), true);
//			resource.getContents().get(0);
//		} catch (Exception e) {
//			error("Unable to load the metamodel: "+e.getMessage(),ModelPackage.IMPORT);
//		}
//	}

	private List<EStructuralFeature> getAllSelectableFeatures(ClassOperator op) {
		List<EStructuralFeature> selected = new ArrayList<EStructuralFeature>();
		for (EStructuralFeature f : op.getClass_().getEAllStructuralFeatures()) {
			if (TomMappingScopeProvider.isSelectedFeature(f,op))
				selected.add(f);
		}
		return selected;
	}
	
	@Check
	public void checkSettedFeatureParameter(SettedFeatureParameter f){
		if(f.getFeature().getEType() instanceof EEnum){
			if(! (f.getValue() instanceof EnumLiteralValue)){
				error("Feature "+f.getFeature().getName()+" is an Enum type, its value has to be a Literal",ModelPackage.eINSTANCE.getSettedFeatureParameter_Value());
			}
		}
	}

}
