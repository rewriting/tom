package fr.irisa.cairn.xtext.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;

/**
 * Print out the prototype of unavailable scope functions when running an XText
 * generated editor.
 * 
 * @author antoine
 * 
 */
public class AbstractDeclarativeScopeProviderDebug extends
		AbstractDeclarativeScopeProvider {

	public IScope getScope(EObject context, EReference reference) {
		IScope scope = polymorphicFindScopeForReferenceName(context, reference);
		if (scope == null) {
			System.err
					.println("Unable to find a scope function: public IScope scope_"
							+ reference.getEContainingClass().getName()
							+ "_"
							+ reference.getName()
							+ "("
							+ context.eClass().getName() + " e,EReference ref)");
			scope = polymorphicFindScopeForClassName(context, reference);
			if (scope == null) {
				System.err
						.println("Unable to find a scope function: public IScope scope_"
								+ reference.getEType().getName()
								+ "("
								+ context.eClass().getName()
								+ " e,EReference ref)");
				scope = delegateGetScope(context, reference);
			}
		}
		return scope;
	}
}
