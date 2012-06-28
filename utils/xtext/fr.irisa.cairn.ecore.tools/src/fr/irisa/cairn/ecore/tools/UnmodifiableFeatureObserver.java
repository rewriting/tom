package fr.irisa.cairn.ecore.tools;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public class UnmodifiableFeatureObserver extends UnmodifiableObjectObserver {

	private EStructuralFeature feature;

	public UnmodifiableFeatureObserver(EObject o, EStructuralFeature feature) {
		super(o);
		this.feature = feature;
	}

	@Override
	protected void initialize(EObject o) {
		o.eAdapters().add(this);
	}

	@Override
	public void notifyChanged(Notification msg) {
		if (!msg.isTouch()) {
			EStructuralFeature feature = (EStructuralFeature) msg.getFeature();
			if (feature == this.feature) {
				EObject target = (EObject) msg.getNotifier();
				Object old = msg.getOldValue();
				Object update = msg.getNewValue();
				throw new ModificationException(old, update, target, feature);
			}
		}
		super.notifyChanged(msg);
	}
}
