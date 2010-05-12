/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import java.util.Collection;

import ligneproduitstelephones.LigneProduitsTelephones;
import ligneproduitstelephones.LigneproduitstelephonesPackage;
import ligneproduitstelephones.Marque;
import ligneproduitstelephones.Telephone;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ligne Produits Telephones</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl#getTelephones <em>Telephones</em>}</li>
 *   <li>{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl#getMarques <em>Marques</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LigneProduitsTelephonesImpl extends EObjectImpl implements LigneProduitsTelephones {
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
	 * The cached value of the '{@link #getTelephones() <em>Telephones</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTelephones()
	 * @generated
	 * @ordered
	 */
	protected EList<Telephone> telephones;

	/**
	 * The cached value of the '{@link #getMarques() <em>Marques</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarques()
	 * @generated
	 * @ordered
	 */
	protected EList<Marque> marques;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LigneProduitsTelephonesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LigneproduitstelephonesPackage.Literals.LIGNE_PRODUITS_TELEPHONES;
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
			eNotify(new ENotificationImpl(this, Notification.SET, LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Telephone> getTelephones() {
		if (telephones == null) {
			telephones = new EObjectContainmentEList<Telephone>(Telephone.class, this, LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES);
		}
		return telephones;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Marque> getMarques() {
		if (marques == null) {
			marques = new EObjectContainmentEList<Marque>(Marque.class, this, LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES);
		}
		return marques;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES:
				return ((InternalEList<?>)getTelephones()).basicRemove(otherEnd, msgs);
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES:
				return ((InternalEList<?>)getMarques()).basicRemove(otherEnd, msgs);
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
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__NAME:
				return getName();
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES:
				return getTelephones();
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES:
				return getMarques();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__NAME:
				setName((String)newValue);
				return;
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES:
				getTelephones().clear();
				getTelephones().addAll((Collection<? extends Telephone>)newValue);
				return;
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES:
				getMarques().clear();
				getMarques().addAll((Collection<? extends Marque>)newValue);
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
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__NAME:
				setName(NAME_EDEFAULT);
				return;
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES:
				getTelephones().clear();
				return;
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES:
				getMarques().clear();
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
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__TELEPHONES:
				return telephones != null && !telephones.isEmpty();
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES__MARQUES:
				return marques != null && !marques.isEmpty();
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
		result.append(')');
		return result.toString();
	}

} //LigneProduitsTelephonesImpl
