/**
 * 
 */
package fr.irisa.cairn.ecore.tools;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;

/**
 * Analyze references which are dangling from a reference {@link EObject}.
 * 
 * @author antoine
 * 
 */
public class DanglingAnalyzer {
	private EObject reference;

	public DanglingAnalyzer(EObject reference) {
		this.reference = reference;
	}

	/**
	 * An object is dangling if isn't contained or if its container isn't in
	 * reference containment hierarchy.
	 * 
	 * @param object
	 * @return
	 */
	public boolean isDangling(EObject object) {
		boolean noContainer = object.eContainer() == null;
		boolean notSameRootContainer = !isInReferenceContainmentHierarchy(object);
		if(noContainer) {
			return true;
		}
		if( notSameRootContainer) {
			return true;
		}
		return false;
	}

	/**
	 * Test if object has some dangling references. All contained references are
	 * recursively analyzed.
	 * 
	 * @param object
	 * @return true if object has no dangling reference
	 * @throws DanglingException
	 */
	public boolean hasDangling(EObject object) throws DanglingException {
		boolean d = false;

		Iterator<EObject> iterator = object.eContents().iterator();
		while (iterator.hasNext() && !d) {
			EObject c = iterator.next();
			d = hasDangling(c);
		}
		iterator = object.eCrossReferences().iterator();
		while (iterator.hasNext() && !d) {
			EObject c = iterator.next();
			d = isDangling(c);
			if (d) {
				throw new DanglingException(c);
			}
		}

		return d;
	}

	private boolean isInReferenceContainmentHierarchy(EObject o) {
		if(o != reference) {
			boolean contained = o.eContainer() != null;
			if (contained) {
				return isInReferenceContainmentHierarchy(o.eContainer());
			} else {
				return false;
			}
		} else {
			return true;
		}
			
	}

	@SuppressWarnings("serial")
	public static class DanglingException extends Exception {
		private EObject dangling;

		public DanglingException(EObject dangling) {
			super(dangling + " is dangling.");
			this.dangling = dangling;
		}

		public EObject getDangling() {
			return dangling;
		}
	}

}