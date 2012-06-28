/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package tom.mapping.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Accessor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.mapping.model.Accessor#getSlot <em>Slot</em>}</li>
 *   <li>{@link tom.mapping.model.Accessor#getJava <em>Java</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.mapping.model.ModelPackage#getAccessor()
 * @model
 * @generated
 */
public interface Accessor extends EObject {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(Parameter)
	 * @see tom.mapping.model.ModelPackage#getAccessor_Slot()
	 * @model
	 * @generated
	 */
	Parameter getSlot();

	/**
	 * Sets the value of the '{@link tom.mapping.model.Accessor#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(Parameter value);

	/**
	 * Returns the value of the '<em><b>Java</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Java</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Java</em>' attribute.
	 * @see #setJava(String)
	 * @see tom.mapping.model.ModelPackage#getAccessor_Java()
	 * @model
	 * @generated
	 */
	String getJava();

	/**
	 * Sets the value of the '{@link tom.mapping.model.Accessor#getJava <em>Java</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Java</em>' attribute.
	 * @see #getJava()
	 * @generated
	 */
	void setJava(String value);

} // Accessor
