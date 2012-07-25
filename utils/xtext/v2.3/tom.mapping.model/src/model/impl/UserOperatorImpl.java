/**
 */
package model.impl;

import java.util.Collection;

import model.Accessor;
import model.ModelPackage;
import model.Parameter;
import model.Terminal;
import model.UserOperator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>User Operator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.UserOperatorImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.impl.UserOperatorImpl#getType <em>Type</em>}</li>
 *   <li>{@link model.impl.UserOperatorImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link model.impl.UserOperatorImpl#getAccessors <em>Accessors</em>}</li>
 *   <li>{@link model.impl.UserOperatorImpl#getMake <em>Make</em>}</li>
 *   <li>{@link model.impl.UserOperatorImpl#getTest <em>Test</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UserOperatorImpl extends EObjectImpl implements UserOperator {
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
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected Terminal type;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList parameters;

	/**
	 * The cached value of the '{@link #getAccessors() <em>Accessors</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccessors()
	 * @generated
	 * @ordered
	 */
	protected EList accessors;

	/**
	 * The default value of the '{@link #getMake() <em>Make</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMake()
	 * @generated
	 * @ordered
	 */
	protected static final String MAKE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMake() <em>Make</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMake()
	 * @generated
	 * @ordered
	 */
	protected String make = MAKE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTest() <em>Test</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTest()
	 * @generated
	 * @ordered
	 */
	protected static final String TEST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTest() <em>Test</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTest()
	 * @generated
	 * @ordered
	 */
	protected String test = TEST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UserOperatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ModelPackage.Literals.USER_OPERATOR;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.USER_OPERATOR__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Terminal getType() {
		if (type != null && type.eIsProxy()) {
			InternalEObject oldType = (InternalEObject)type;
			type = (Terminal)eResolveProxy(oldType);
			if (type != oldType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.USER_OPERATOR__TYPE, oldType, type));
			}
		}
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Terminal basicGetType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(Terminal newType) {
		Terminal oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.USER_OPERATOR__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList(Parameter.class, this, ModelPackage.USER_OPERATOR__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAccessors() {
		if (accessors == null) {
			accessors = new EObjectContainmentEList(Accessor.class, this, ModelPackage.USER_OPERATOR__ACCESSORS);
		}
		return accessors;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMake() {
		return make;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMake(String newMake) {
		String oldMake = make;
		make = newMake;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.USER_OPERATOR__MAKE, oldMake, make));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTest() {
		return test;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTest(String newTest) {
		String oldTest = test;
		test = newTest;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.USER_OPERATOR__TEST, oldTest, test));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.USER_OPERATOR__PARAMETERS:
				return ((InternalEList)getParameters()).basicRemove(otherEnd, msgs);
			case ModelPackage.USER_OPERATOR__ACCESSORS:
				return ((InternalEList)getAccessors()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.USER_OPERATOR__NAME:
				return getName();
			case ModelPackage.USER_OPERATOR__TYPE:
				if (resolve) return getType();
				return basicGetType();
			case ModelPackage.USER_OPERATOR__PARAMETERS:
				return getParameters();
			case ModelPackage.USER_OPERATOR__ACCESSORS:
				return getAccessors();
			case ModelPackage.USER_OPERATOR__MAKE:
				return getMake();
			case ModelPackage.USER_OPERATOR__TEST:
				return getTest();
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
			case ModelPackage.USER_OPERATOR__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.USER_OPERATOR__TYPE:
				setType((Terminal)newValue);
				return;
			case ModelPackage.USER_OPERATOR__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection)newValue);
				return;
			case ModelPackage.USER_OPERATOR__ACCESSORS:
				getAccessors().clear();
				getAccessors().addAll((Collection)newValue);
				return;
			case ModelPackage.USER_OPERATOR__MAKE:
				setMake((String)newValue);
				return;
			case ModelPackage.USER_OPERATOR__TEST:
				setTest((String)newValue);
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
			case ModelPackage.USER_OPERATOR__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.USER_OPERATOR__TYPE:
				setType((Terminal)null);
				return;
			case ModelPackage.USER_OPERATOR__PARAMETERS:
				getParameters().clear();
				return;
			case ModelPackage.USER_OPERATOR__ACCESSORS:
				getAccessors().clear();
				return;
			case ModelPackage.USER_OPERATOR__MAKE:
				setMake(MAKE_EDEFAULT);
				return;
			case ModelPackage.USER_OPERATOR__TEST:
				setTest(TEST_EDEFAULT);
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
			case ModelPackage.USER_OPERATOR__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.USER_OPERATOR__TYPE:
				return type != null;
			case ModelPackage.USER_OPERATOR__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case ModelPackage.USER_OPERATOR__ACCESSORS:
				return accessors != null && !accessors.isEmpty();
			case ModelPackage.USER_OPERATOR__MAKE:
				return MAKE_EDEFAULT == null ? make != null : !MAKE_EDEFAULT.equals(make);
			case ModelPackage.USER_OPERATOR__TEST:
				return TEST_EDEFAULT == null ? test != null : !TEST_EDEFAULT.equals(test);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", make: ");
		result.append(make);
		result.append(", test: ");
		result.append(test);
		result.append(')');
		return result.toString();
	}

} //UserOperatorImpl
