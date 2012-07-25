package fr.irisa.cairn.ecore.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A tool which replace an {@link EObject} by another one in all its referencing
 * {@link EObject} (Containment and CrossReferences).
 * 
 * @author antoine
 * 
 */
public class EcoreUpdater {

	/**
	 * Replace all references to an {@link EObject} by a new one.
	 * 
	 * @param original
	 *            referenced object
	 * @param update
	 *            new object
	 */
	public static void update(EObject original, EObject update) {
		// Update containment references
		EcoreUtil.replace(original, update);

		// Update cross references
		for (EObject referencing : new ArrayList<EObject>(
				original.eCrossReferences())) {
			updateCrossReferences(referencing, original, update);
		}
	}

	/**
	 * Replace all references to an {@link EObject} by a new one in a root.
	 * 
	 * @param root
	 * @param original
	 * @param update
	 */
	public static void update(EObject root, EObject original, EObject update) {
		TreeIterator<EObject> i = root.eAllContents();
		while (i.hasNext()) {
			EObject current = i.next();
			for (EObject crossref : current.eCrossReferences()) {
				if (crossref == original) {
					for (EReference feature : current.eClass()
							.getEAllReferences()) {
						if (feature.isMany()) {
							@SuppressWarnings({ "rawtypes", "unchecked" })
							EList<EObject> eGet = (EList) current.eGet(feature);
							if (eGet.contains(original)) {
								int indexOf = eGet.indexOf(original);
								eGet.set(indexOf, update);
							}
						} else {
							EObject ref = (EObject) current.eGet(feature);
							if (ref == original) {
								current.eSet(feature, update);
							}
						}
					}
				}
			}
		}
	}

	public static void updateCrossReferences(EObject referencing,
			EObject referenced, EObject update) {
		for (EReference r : referencing.eClass().getEAllReferences()) {
			if (referencing.eGet(r) == referenced) {
				referencing.eSet(r, update);
			}
		}
	}

	public static void updateCrossReferencesInHierarchy(EObject referencing,
			EObject referenced, EObject update) {

		List<EObject> allReferencingObjects = getAllReferencingObjects(
				referenced, referencing);
		for (EObject r : new ArrayList<EObject>(allReferencingObjects)) {
			updateCrossReferences(r, referenced, update);
		}
	}

	public static List<EObject> getAllReferencingObjects(EObject reference) {
		return getAllReferencingObjects(reference,EcoreUtil.getRootContainer(reference));

	}

	public static List<EObject> getAllReferencingObjects(EObject reference,
			EObject root) {
		List<EObject> referencing = new UniqueEList<EObject>();
		if (root.eCrossReferences().contains(reference))
			referencing.add(root);
		TreeIterator<EObject> eAllContents = root.eAllContents();
		while (eAllContents.hasNext()) {
			EObject o = eAllContents.next();
			if (o.eCrossReferences().contains(reference))
				referencing.add(o);
		}
		return referencing;
	}

}
