/**
 */
package model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class Operator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link model.ClassOperator#getClass_ <em>Class</em>}</li>
 *   <li>{@link model.ClassOperator#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see model.ModelPackage#getClassOperator()
 * @model
 * @generated
 */
public interface ClassOperator extends Operator {
	/**
	 * Returns the value of the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Class</em>' reference.
	 * @see #setClass(EClass)
	 * @see model.ModelPackage#getClassOperator_Class()
	 * @model required="true"
	 * @generated
	 */
	EClass getClass_();

	/**
	 * Sets the value of the '{@link model.ClassOperator#getClass_ <em>Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Class</em>' reference.
	 * @see #getClass_()
	 * @generated
	 */
	void setClass(EClass value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link model.FeatureParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see model.ModelPackage#getClassOperator_Parameters()
	 * @model type="model.FeatureParameter" containment="true"
	 * @generated
	 */
	EList<FeatureParameter> getParameters();

} // ClassOperator
