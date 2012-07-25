/**
 */
package model.impl;

import java.util.Collection;

import model.Import;
import model.Mapping;
import model.ModelPackage;
import model.Module;
import model.Operator;
import model.Terminal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.MappingImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getExternalTerminals <em>External Terminals</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getOperators <em>Operators</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getModules <em>Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MappingImpl extends EObjectImpl implements Mapping {
	/**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected EList imports;

	/**
	 * The cached value of the '{@link #getTerminals() <em>Terminals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTerminals()
	 * @generated
	 * @ordered
	 */
	protected EList terminals;

	/**
	 * The cached value of the '{@link #getExternalTerminals() <em>External Terminals</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExternalTerminals()
	 * @generated
	 * @ordered
	 */
	protected EList externalTerminals;

	/**
	 * The cached value of the '{@link #getOperators() <em>Operators</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperators()
	 * @generated
	 * @ordered
	 */
	protected EList operators;

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
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected String prefix = PREFIX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EList modules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getImports() {
		if (imports == null) {
			imports = new EObjectContainmentEList(Import.class, this, ModelPackage.MAPPING__IMPORTS);
		}
		return imports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTerminals() {
		if (terminals == null) {
			terminals = new EObjectContainmentEList(Terminal.class, this, ModelPackage.MAPPING__TERMINALS);
		}
		return terminals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getExternalTerminals() {
		if (externalTerminals == null) {
			externalTerminals = new EObjectResolvingEList(Terminal.class, this, ModelPackage.MAPPING__EXTERNAL_TERMINALS);
		}
		return externalTerminals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getOperators() {
		if (operators == null) {
			operators = new EObjectContainmentEList(Operator.class, this, ModelPackage.MAPPING__OPERATORS);
		}
		return operators;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MAPPING__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrefix(String newPrefix) {
		String oldPrefix = prefix;
		prefix = newPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MAPPING__PREFIX, oldPrefix, prefix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getModules() {
		if (modules == null) {
			modules = new EObjectContainmentEList(Module.class, this, ModelPackage.MAPPING__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAllDefaultOperators() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Terminal getTerminal(EClass c, boolean isList) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getAllListTerminals() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getMetamodelPackages() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				return ((InternalEList)getImports()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__TERMINALS:
				return ((InternalEList)getTerminals()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__OPERATORS:
				return ((InternalEList)getOperators()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__MODULES:
				return ((InternalEList)getModules()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.MAPPING__IMPORTS:
				return getImports();
			case ModelPackage.MAPPING__TERMINALS:
				return getTerminals();
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				return getExternalTerminals();
			case ModelPackage.MAPPING__OPERATORS:
				return getOperators();
			case ModelPackage.MAPPING__NAME:
				return getName();
			case ModelPackage.MAPPING__PREFIX:
				return getPrefix();
			case ModelPackage.MAPPING__MODULES:
				return getModules();
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
			case ModelPackage.MAPPING__IMPORTS:
				getImports().clear();
				getImports().addAll((Collection)newValue);
				return;
			case ModelPackage.MAPPING__TERMINALS:
				getTerminals().clear();
				getTerminals().addAll((Collection)newValue);
				return;
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				getExternalTerminals().clear();
				getExternalTerminals().addAll((Collection)newValue);
				return;
			case ModelPackage.MAPPING__OPERATORS:
				getOperators().clear();
				getOperators().addAll((Collection)newValue);
				return;
			case ModelPackage.MAPPING__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.MAPPING__PREFIX:
				setPrefix((String)newValue);
				return;
			case ModelPackage.MAPPING__MODULES:
				getModules().clear();
				getModules().addAll((Collection)newValue);
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
			case ModelPackage.MAPPING__IMPORTS:
				getImports().clear();
				return;
			case ModelPackage.MAPPING__TERMINALS:
				getTerminals().clear();
				return;
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				getExternalTerminals().clear();
				return;
			case ModelPackage.MAPPING__OPERATORS:
				getOperators().clear();
				return;
			case ModelPackage.MAPPING__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.MAPPING__PREFIX:
				setPrefix(PREFIX_EDEFAULT);
				return;
			case ModelPackage.MAPPING__MODULES:
				getModules().clear();
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
			case ModelPackage.MAPPING__IMPORTS:
				return imports != null && !imports.isEmpty();
			case ModelPackage.MAPPING__TERMINALS:
				return terminals != null && !terminals.isEmpty();
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				return externalTerminals != null && !externalTerminals.isEmpty();
			case ModelPackage.MAPPING__OPERATORS:
				return operators != null && !operators.isEmpty();
			case ModelPackage.MAPPING__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.MAPPING__PREFIX:
				return PREFIX_EDEFAULT == null ? prefix != null : !PREFIX_EDEFAULT.equals(prefix);
			case ModelPackage.MAPPING__MODULES:
				return modules != null && !modules.isEmpty();
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
		result.append(", prefix: ");
		result.append(prefix);
		result.append(')');
		return result.toString();
	}

} //MappingImpl
