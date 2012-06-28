package fr.irisa.cairn.ecore.tools;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Observe modifications of the hierarchy of an {@link EObject}. If the object
 * is modified, then a exception is thrown.
 * 
 * @author antoine
 * 
 */
public class UnmodifiableObjectObserver extends AdapterImpl {
	private EObject observable;

	public UnmodifiableObjectObserver(EObject o) {
		this.observable = o;
		initialize(o);
	}

	protected void initialize(EObject o) {
		o.eAdapters().add(this);
		TreeIterator<EObject> eAllContents = o.eAllContents();
		while (eAllContents.hasNext()) {
			eAllContents.next().eAdapters().add(this);
		}
	}

	@Override
	public void notifyChanged(Notification msg) {
		if (!msg.isTouch()) {
			EObject target = (EObject) msg.getNotifier();
			Object old = msg.getOldValue();
			EStructuralFeature feature = (EStructuralFeature) msg.getFeature();
			Object update = msg.getNewValue();
			throw new ModificationException(old, update, target, feature);
		}
		super.notifyChanged(msg);
	}

	@Override
	public String toString() {
		return "Unmodifiable observer for " + observable.toString();
	}

	@SuppressWarnings("serial")
	public static class ModificationException extends RuntimeException {
		private Object old;
		private Object update;
		private Object object;
		private EStructuralFeature feature;

		public ModificationException(Object old, Object update, Object object,
				EStructuralFeature feature) {
			this.old = old;
			this.update = update;
			this.object = object;
			this.feature = feature;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer(object.toString());
			sb.append(" Unauthorized modification ::").append(old).append("->")
					.append(update).append("(feature::")
					.append(feature.getName()).append(")").append("in ");
			return sb.toString();
		}

	}
}
