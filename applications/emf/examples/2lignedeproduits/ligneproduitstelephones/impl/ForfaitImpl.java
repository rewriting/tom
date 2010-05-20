/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import ligneproduitstelephones.Forfait;
import ligneproduitstelephones.LigneproduitstelephonesPackage;
import ligneproduitstelephones.Operateur;
import ligneproduitstelephones.Telephone;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Forfait</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getOperateur <em>Operateur</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getHours <em>Hours</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getSMS <em>SMS</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.ForfaitImpl#getSpecificPhone <em>Specific Phone</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ForfaitImpl extends EObjectImpl implements Forfait {
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
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected float price = PRICE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOperateur() <em>Operateur</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperateur()
	 * @generated
	 * @ordered
	 */
	protected Operateur operateur;

	/**
	 * The default value of the '{@link #getHours() <em>Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHours()
	 * @generated
	 * @ordered
	 */
	protected static final int HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHours() <em>Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHours()
	 * @generated
	 * @ordered
	 */
	protected int hours = HOURS_EDEFAULT;

	/**
	 * The default value of the '{@link #getSMS() <em>SMS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSMS()
	 * @generated
	 * @ordered
	 */
	protected static final int SMS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSMS() <em>SMS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSMS()
	 * @generated
	 * @ordered
	 */
	protected int sMS = SMS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSpecificPhone() <em>Specific Phone</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecificPhone()
	 * @generated
	 * @ordered
	 */
	protected Telephone specificPhone;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ForfaitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LigneproduitstelephonesPackage.Literals.FORFAIT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(float newPrice) {
		float oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operateur getOperateur() {
		if (operateur != null && operateur.eIsProxy()) {
			InternalEObject oldOperateur = (InternalEObject)operateur;
			operateur = (Operateur)eResolveProxy(oldOperateur);
			if (operateur != oldOperateur) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LigneproduitstelephonesPackage.FORFAIT__OPERATEUR, oldOperateur, operateur));
			}
		}
		return operateur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operateur basicGetOperateur() {
		return operateur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperateur(Operateur newOperateur) {
		Operateur oldOperateur = operateur;
		operateur = newOperateur;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__OPERATEUR, oldOperateur, operateur));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHours(int newHours) {
		int oldHours = hours;
		hours = newHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__HOURS, oldHours, hours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSMS() {
		return sMS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSMS(int newSMS) {
		int oldSMS = sMS;
		sMS = newSMS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__SMS, oldSMS, sMS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Telephone getSpecificPhone() {
		if (specificPhone != null && specificPhone.eIsProxy()) {
			InternalEObject oldSpecificPhone = (InternalEObject)specificPhone;
			specificPhone = (Telephone)eResolveProxy(oldSpecificPhone);
			if (specificPhone != oldSpecificPhone) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE, oldSpecificPhone, specificPhone));
			}
		}
		return specificPhone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Telephone basicGetSpecificPhone() {
		return specificPhone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpecificPhone(Telephone newSpecificPhone) {
		Telephone oldSpecificPhone = specificPhone;
		specificPhone = newSpecificPhone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE, oldSpecificPhone, specificPhone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean hasInternet() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.FORFAIT__NAME:
				return getName();
			case LigneproduitstelephonesPackage.FORFAIT__PRICE:
				return getPrice();
			case LigneproduitstelephonesPackage.FORFAIT__OPERATEUR:
				if (resolve) return getOperateur();
				return basicGetOperateur();
			case LigneproduitstelephonesPackage.FORFAIT__HOURS:
				return getHours();
			case LigneproduitstelephonesPackage.FORFAIT__SMS:
				return getSMS();
			case LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE:
				if (resolve) return getSpecificPhone();
				return basicGetSpecificPhone();
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
			case LigneproduitstelephonesPackage.FORFAIT__NAME:
				setName((String)newValue);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__PRICE:
				setPrice((Float)newValue);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__OPERATEUR:
				setOperateur((Operateur)newValue);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__HOURS:
				setHours((Integer)newValue);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__SMS:
				setSMS((Integer)newValue);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE:
				setSpecificPhone((Telephone)newValue);
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
			case LigneproduitstelephonesPackage.FORFAIT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__OPERATEUR:
				setOperateur((Operateur)null);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__HOURS:
				setHours(HOURS_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__SMS:
				setSMS(SMS_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE:
				setSpecificPhone((Telephone)null);
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
			case LigneproduitstelephonesPackage.FORFAIT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LigneproduitstelephonesPackage.FORFAIT__PRICE:
				return price != PRICE_EDEFAULT;
			case LigneproduitstelephonesPackage.FORFAIT__OPERATEUR:
				return operateur != null;
			case LigneproduitstelephonesPackage.FORFAIT__HOURS:
				return hours != HOURS_EDEFAULT;
			case LigneproduitstelephonesPackage.FORFAIT__SMS:
				return sMS != SMS_EDEFAULT;
			case LigneproduitstelephonesPackage.FORFAIT__SPECIFIC_PHONE:
				return specificPhone != null;
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
		result.append(", price: ");
		result.append(price);
		result.append(", hours: ");
		result.append(hours);
		result.append(", sMS: ");
		result.append(sMS);
		result.append(')');
		return result.toString();
	}

} //ForfaitImpl
