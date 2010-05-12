/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import ligneproduitstelephones.LigneproduitstelephonesPackage;
import ligneproduitstelephones.Marque;
import ligneproduitstelephones.OSTelephone;
import ligneproduitstelephones.Telephone;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Telephone</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ligneproduitstelephones.impl.TelephoneImpl#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.TelephoneImpl#isTactile <em>Tactile</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.TelephoneImpl#getOSTelephone <em>OS Telephone</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.TelephoneImpl#getMarque <em>Marque</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TelephoneImpl extends EObjectImpl implements Telephone {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isTactile() <em>Tactile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTactile()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TACTILE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTactile() <em>Tactile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTactile()
	 * @generated
	 * @ordered
	 */
	protected boolean tactile = TACTILE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOSTelephone() <em>OS Telephone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOSTelephone()
	 * @generated
	 * @ordered
	 */
	protected static final OSTelephone OS_TELEPHONE_EDEFAULT = OSTelephone.ANDROID;

	/**
	 * The cached value of the '{@link #getOSTelephone() <em>OS Telephone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOSTelephone()
	 * @generated
	 * @ordered
	 */
	protected OSTelephone oSTelephone = OS_TELEPHONE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMarque() <em>Marque</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarque()
	 * @generated
	 * @ordered
	 */
	protected Marque marque;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TelephoneImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LigneproduitstelephonesPackage.Literals.TELEPHONE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.TELEPHONE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTactile() {
		return tactile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTactile(boolean newTactile) {
		boolean oldTactile = tactile;
		tactile = newTactile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.TELEPHONE__TACTILE, oldTactile, tactile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OSTelephone getOSTelephone() {
		return oSTelephone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOSTelephone(OSTelephone newOSTelephone) {
		OSTelephone oldOSTelephone = oSTelephone;
		oSTelephone = newOSTelephone == null ? OS_TELEPHONE_EDEFAULT : newOSTelephone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.TELEPHONE__OS_TELEPHONE, oldOSTelephone, oSTelephone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Marque getMarque() {
		if (marque != null && marque.eIsProxy()) {
			InternalEObject oldMarque = (InternalEObject)marque;
			marque = (Marque)eResolveProxy(oldMarque);
			if (marque != oldMarque) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LigneproduitstelephonesPackage.TELEPHONE__MARQUE, oldMarque, marque));
			}
		}
		return marque;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Marque basicGetMarque() {
		return marque;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMarque(Marque newMarque, NotificationChain msgs) {
		Marque oldMarque = marque;
		marque = newMarque;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.TELEPHONE__MARQUE, oldMarque, newMarque);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarque(Marque newMarque) {
		if (newMarque != marque) {
			NotificationChain msgs = null;
			if (marque != null)
				msgs = ((InternalEObject)marque).eInverseRemove(this, LigneproduitstelephonesPackage.MARQUE__TELEPHONES, Marque.class, msgs);
			if (newMarque != null)
				msgs = ((InternalEObject)newMarque).eInverseAdd(this, LigneproduitstelephonesPackage.MARQUE__TELEPHONES, Marque.class, msgs);
			msgs = basicSetMarque(newMarque, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.TELEPHONE__MARQUE, newMarque, newMarque));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				if (marque != null)
					msgs = ((InternalEObject)marque).eInverseRemove(this, LigneproduitstelephonesPackage.MARQUE__TELEPHONES, Marque.class, msgs);
				return basicSetMarque((Marque)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				return basicSetMarque(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__NAME:
				return getName();
			case LigneproduitstelephonesPackage.TELEPHONE__TACTILE:
				return isTactile();
			case LigneproduitstelephonesPackage.TELEPHONE__OS_TELEPHONE:
				return getOSTelephone();
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				if (resolve) return getMarque();
				return basicGetMarque();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__NAME:
				setName((String)newValue);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__TACTILE:
				setTactile((Boolean)newValue);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__OS_TELEPHONE:
				setOSTelephone((OSTelephone)newValue);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				setMarque((Marque)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__TACTILE:
				setTactile(TACTILE_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__OS_TELEPHONE:
				setOSTelephone(OS_TELEPHONE_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				setMarque((Marque)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.TELEPHONE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LigneproduitstelephonesPackage.TELEPHONE__TACTILE:
				return tactile != TACTILE_EDEFAULT;
			case LigneproduitstelephonesPackage.TELEPHONE__OS_TELEPHONE:
				return oSTelephone != OS_TELEPHONE_EDEFAULT;
			case LigneproduitstelephonesPackage.TELEPHONE__MARQUE:
				return marque != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", tactile: ");
		result.append(tactile);
		result.append(", oSTelephone: ");
		result.append(oSTelephone);
		result.append(')');
		return result.toString();
	}

} //TelephoneImpl
