/**
 */
package model.impl;


import model.Accessor;
import model.ModelPackage;
import model.Parameter;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Accessor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.AccessorImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link model.impl.AccessorImpl#getJava <em>Java</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AccessorImpl extends EObjectImpl implements Accessor {
	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Parameter slot;

	/**
	 * The default value of the '{@link #getJava() <em>Java</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJava()
	 * @generated
	 * @ordered
	 */
	protected static final String JAVA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJava() <em>Java</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJava()
	 * @generated
	 * @ordered
	 */
	protected String java = JAVA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AccessorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ModelPackage.Literals.ACCESSOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Parameter)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.ACCESSOR__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlot(Parameter newSlot) {
		Parameter oldSlot = slot;
		slot = newSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ACCESSOR__SLOT, oldSlot, slot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getJava() {
		return java;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJava(String newJava) {
		String oldJava = java;
		java = newJava;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ACCESSOR__JAVA, oldJava, java));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.ACCESSOR__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case ModelPackage.ACCESSOR__JAVA:
				return getJava();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.ACCESSOR__SLOT:
				setSlot((Parameter)newValue);
				return;
			case ModelPackage.ACCESSOR__JAVA:
				setJava((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.ACCESSOR__SLOT:
				setSlot((Parameter)null);
				return;
			case ModelPackage.ACCESSOR__JAVA:
				setJava(JAVA_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.ACCESSOR__SLOT:
				return slot != null;
			case ModelPackage.ACCESSOR__JAVA:
				return JAVA_EDEFAULT == null ? java != null : !JAVA_EDEFAULT.equals(java);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (java: ");
		result.append(java);
		result.append(')');
		return result.toString();
	}

} //AccessorImpl
