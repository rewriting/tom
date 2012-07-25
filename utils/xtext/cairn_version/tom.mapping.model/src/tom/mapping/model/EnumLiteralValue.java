/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package tom.mapping.model;

import org.eclipse.emf.ecore.EEnumLiteral;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Literal Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.mapping.model.EnumLiteralValue#getLiteral <em>Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.mapping.model.ModelPackage#getEnumLiteralValue()
 * @model
 * @generated
 */
public interface EnumLiteralValue extends SettedValue {
	/**
	 * Returns the value of the '<em><b>Literal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literal</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literal</em>' reference.
	 * @see #setLiteral(EEnumLiteral)
	 * @see tom.mapping.model.ModelPackage#getEnumLiteralValue_Literal()
	 * @model
	 * @generated
	 */
	EEnumLiteral getLiteral();

	/**
	 * Sets the value of the '{@link tom.mapping.model.EnumLiteralValue#getLiteral <em>Literal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literal</em>' reference.
	 * @see #getLiteral()
	 * @generated
	 */
	void setLiteral(EEnumLiteral value);

} // EnumLiteralValue
