/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package tom.mapping.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Setted Feature Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.mapping.model.SettedFeatureParameter#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.mapping.model.ModelPackage#getSettedFeatureParameter()
 * @model
 * @generated
 */
public interface SettedFeatureParameter extends FeatureParameter {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(SettedValue)
	 * @see tom.mapping.model.ModelPackage#getSettedFeatureParameter_Value()
	 * @model containment="true"
	 * @generated
	 */
	SettedValue getValue();

	/**
	 * Sets the value of the '{@link tom.mapping.model.SettedFeatureParameter#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(SettedValue value);

} // SettedFeatureParameter
