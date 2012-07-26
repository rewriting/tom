/**
 */
package model.impl;

import java.util.Collection;


import model.AliasNode;
import model.AliasOperator;
import model.ModelPackage;
import model.Operator;
import model.OperatorNode;

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
 * An implementation of the model object '<em><b>Alias Operator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.AliasOperatorImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.impl.AliasOperatorImpl#getOp <em>Op</em>}</li>
 *   <li>{@link model.impl.AliasOperatorImpl#getNodes <em>Nodes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AliasOperatorImpl extends EObjectImpl implements AliasOperator {
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
	 * The cached value of the '{@link #getOp() <em>Op</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOp()
	 * @generated
	 * @ordered
	 */
	protected Operator op;

	/**
	 * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodes()
	 * @generated
	 * @ordered
	 */
	protected EList<AliasNode> nodes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AliasOperatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ModelPackage.Literals.ALIAS_OPERATOR;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ALIAS_OPERATOR__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operator getOp() {
		if (op != null && op.eIsProxy()) {
			InternalEObject oldOp = (InternalEObject)op;
			op = (Operator)eResolveProxy(oldOp);
			if (op != oldOp) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.ALIAS_OPERATOR__OP, oldOp, op));
			}
		}
		return op;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operator basicGetOp() {
		return op;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOp(Operator newOp) {
		Operator oldOp = op;
		op = newOp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ALIAS_OPERATOR__OP, oldOp, op));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AliasNode> getNodes() {
		if (nodes == null) {
			nodes = new EObjectContainmentEList<AliasNode>(AliasNode.class, this, ModelPackage.ALIAS_OPERATOR__NODES);
		}
		return nodes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.ALIAS_OPERATOR__NODES:
				return ((InternalEList<?>)getNodes()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.ALIAS_OPERATOR__NAME:
				return getName();
			case ModelPackage.ALIAS_OPERATOR__OP:
				if (resolve) return getOp();
				return basicGetOp();
			case ModelPackage.ALIAS_OPERATOR__NODES:
				return getNodes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.ALIAS_OPERATOR__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.ALIAS_OPERATOR__OP:
				setOp((Operator)newValue);
				return;
			case ModelPackage.ALIAS_OPERATOR__NODES:
				getNodes().clear();
				getNodes().addAll((Collection<? extends AliasNode>)newValue);
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
			case ModelPackage.ALIAS_OPERATOR__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.ALIAS_OPERATOR__OP:
				setOp((Operator)null);
				return;
			case ModelPackage.ALIAS_OPERATOR__NODES:
				getNodes().clear();
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
			case ModelPackage.ALIAS_OPERATOR__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.ALIAS_OPERATOR__OP:
				return op != null;
			case ModelPackage.ALIAS_OPERATOR__NODES:
				return nodes != null && !nodes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == AliasNode.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == OperatorNode.class) {
			switch (derivedFeatureID) {
				case ModelPackage.ALIAS_OPERATOR__OP: return ModelPackage.OPERATOR_NODE__OP;
				case ModelPackage.ALIAS_OPERATOR__NODES: return ModelPackage.OPERATOR_NODE__NODES;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == AliasNode.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == OperatorNode.class) {
			switch (baseFeatureID) {
				case ModelPackage.OPERATOR_NODE__OP: return ModelPackage.ALIAS_OPERATOR__OP;
				case ModelPackage.OPERATOR_NODE__NODES: return ModelPackage.ALIAS_OPERATOR__NODES;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(')');
		return result.toString();
	}

} //AliasOperatorImpl
