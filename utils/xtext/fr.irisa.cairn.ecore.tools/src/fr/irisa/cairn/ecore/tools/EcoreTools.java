package fr.irisa.cairn.ecore.tools;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EcoreTools {
	/**
	 * Copy an {@link EObject}. Only attributes and non contained references are
	 * copied.
	 * 
	 * @param <T>
	 * @param o
	 * @return
	 */
	public static <T extends EObject> T copyWithoutContainment(T o) {
		T copy = copy(o);

		for (EReference ref : copy.eClass().getEAllContainments()) {
			copy.eUnset(ref);
		}
		return copy;
	}

	/**
	 * Returns a self-contained copy of the eObject. Some
	 * {@link EStructuralFeature} can be exceptions and won't be copied.
	 * 
	 * @param <T>
	 *            type of the eObject
	 * @param o
	 *            the eObject to copy
	 * @param exceptions
	 *            structure features that won't be copied
	 * @return the copy
	 * @see Copier
	 */
	public static <T extends EObject> T copy(T o,
			EStructuralFeature... exceptions) {
		T copy = EcoreUtil.copy(o);
		for (EStructuralFeature attribute : o.eClass().getEAllAttributes()) {
			copy.eSet(attribute, o.eGet(attribute));
		}
		for (EStructuralFeature ref : exceptions) {
			copy.eUnset(ref);
		}
		return copy;
	}
}
