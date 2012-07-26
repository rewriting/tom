/**
 */
package model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link model.Mapping#getImports <em>Imports</em>}</li>
 *   <li>{@link model.Mapping#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link model.Mapping#getExternalTerminals <em>External Terminals</em>}</li>
 *   <li>{@link model.Mapping#getOperators <em>Operators</em>}</li>
 *   <li>{@link model.Mapping#getName <em>Name</em>}</li>
 *   <li>{@link model.Mapping#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link model.Mapping#getModules <em>Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @see model.ModelPackage#getMapping()
 * @model
 * @generated
 */
public interface Mapping extends EObject {
	/**
	 * Returns the value of the '<em><b>Imports</b></em>' containment reference list.
	 * The list contents are of type {@link model.Import}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Imports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imports</em>' containment reference list.
	 * @see model.ModelPackage#getMapping_Imports()
	 * @model type="model.Import" containment="true" required="true"
	 * @generated
	 */
	EList<Import> getImports();

	/**
	 * Returns the value of the '<em><b>Terminals</b></em>' containment reference list.
	 * The list contents are of type {@link model.Terminal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Terminals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Terminals</em>' containment reference list.
	 * @see model.ModelPackage#getMapping_Terminals()
	 * @model type="model.Terminal" containment="true"
	 * @generated
	 */
	EList<Terminal> getTerminals();

	/**
	 * Returns the value of the '<em><b>External Terminals</b></em>' reference list.
	 * The list contents are of type {@link model.Terminal}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External Terminals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External Terminals</em>' reference list.
	 * @see model.ModelPackage#getMapping_ExternalTerminals()
	 * @model type="model.Terminal"
	 * @generated
	 */
	EList<Terminal> getExternalTerminals();

	/**
	 * Returns the value of the '<em><b>Operators</b></em>' containment reference list.
	 * The list contents are of type {@link model.Operator}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operators</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operators</em>' containment reference list.
	 * @see model.ModelPackage#getMapping_Operators()
	 * @model type="model.Operator" containment="true"
	 * @generated
	 */
	EList<Operator> getOperators();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see model.ModelPackage#getMapping_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link model.Mapping#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prefix</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prefix</em>' attribute.
	 * @see #setPrefix(String)
	 * @see model.ModelPackage#getMapping_Prefix()
	 * @model
	 * @generated
	 */
	String getPrefix();

	/**
	 * Sets the value of the '{@link model.Mapping#getPrefix <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prefix</em>' attribute.
	 * @see #getPrefix()
	 * @generated
	 */
	void setPrefix(String value);

	/**
	 * Returns the value of the '<em><b>Modules</b></em>' containment reference list.
	 * The list contents are of type {@link model.Module}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' containment reference list.
	 * @see model.ModelPackage#getMapping_Modules()
	 * @model type="model.Module" containment="true"
	 * @generated
	 */
	EList<Module> getModules();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" type="org.eclipse.emf.ecore.EClass"
	 * @generated
	 */
	EList<EClass> getAllDefaultOperators();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Terminal getTerminal(EClass c, boolean isList);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" type="model.Terminal"
	 * @generated
	 */
	EList<Terminal> getAllListTerminals();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" type="org.eclipse.emf.ecore.EPackage"
	 * @generated
	 */
	EList<EPackage> getMetamodelPackages();

} // Mapping
