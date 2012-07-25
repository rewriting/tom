/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package tom.mapping.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Operator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.mapping.model.UserOperator#getType <em>Type</em>}</li>
 *   <li>{@link tom.mapping.model.UserOperator#getParameters <em>Parameters</em>}</li>
 *   <li>{@link tom.mapping.model.UserOperator#getAccessors <em>Accessors</em>}</li>
 *   <li>{@link tom.mapping.model.UserOperator#getMake <em>Make</em>}</li>
 *   <li>{@link tom.mapping.model.UserOperator#getTest <em>Test</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.mapping.model.ModelPackage#getUserOperator()
 * @model
 * @generated
 */
public interface UserOperator extends Operator {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(Terminal)
	 * @see tom.mapping.model.ModelPackage#getUserOperator_Type()
	 * @model required="true"
	 * @generated
	 */
	Terminal getType();

	/**
	 * Sets the value of the '{@link tom.mapping.model.UserOperator#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(Terminal value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link tom.mapping.model.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see tom.mapping.model.ModelPackage#getUserOperator_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Accessors</b></em>' containment reference list.
	 * The list contents are of type {@link tom.mapping.model.Accessor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accessors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accessors</em>' containment reference list.
	 * @see tom.mapping.model.ModelPackage#getUserOperator_Accessors()
	 * @model containment="true"
	 * @generated
	 */
	EList<Accessor> getAccessors();

	/**
	 * Returns the value of the '<em><b>Make</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Make</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Make</em>' attribute.
	 * @see #setMake(String)
	 * @see tom.mapping.model.ModelPackage#getUserOperator_Make()
	 * @model required="true"
	 * @generated
	 */
	String getMake();

	/**
	 * Sets the value of the '{@link tom.mapping.model.UserOperator#getMake <em>Make</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Make</em>' attribute.
	 * @see #getMake()
	 * @generated
	 */
	void setMake(String value);

	/**
	 * Returns the value of the '<em><b>Test</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Test</em>' attribute.
	 * @see #setTest(String)
	 * @see tom.mapping.model.ModelPackage#getUserOperator_Test()
	 * @model required="true"
	 * @generated
	 */
	String getTest();

	/**
	 * Sets the value of the '{@link tom.mapping.model.UserOperator#getTest <em>Test</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Test</em>' attribute.
	 * @see #getTest()
	 * @generated
	 */
	void setTest(String value);

} // UserOperator
