/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ARG</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.ARG#getName <em>Name</em>}</li>
 *   <li>{@link tom.tomDsl.ARG#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getARG()
 * @model
 * @generated
 */
public interface ARG extends EObject
{
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
   * @see tom.tomDsl.TomDslPackage#getARG_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link tom.tomDsl.ARG#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see tom.tomDsl.TomDslPackage#getARG_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link tom.tomDsl.ARG#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

} // ARG
