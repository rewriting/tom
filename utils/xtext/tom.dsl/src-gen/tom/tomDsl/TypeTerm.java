/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.TypeTerm#getName <em>Name</em>}</li>
 *   <li>{@link tom.tomDsl.TypeTerm#getImplement <em>Implement</em>}</li>
 *   <li>{@link tom.tomDsl.TypeTerm#getSort <em>Sort</em>}</li>
 *   <li>{@link tom.tomDsl.TypeTerm#getEquals <em>Equals</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getTypeTerm()
 * @model
 * @generated
 */
public interface TypeTerm extends EObject
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
   * @see tom.tomDsl.TomDslPackage#getTypeTerm_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link tom.tomDsl.TypeTerm#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Implement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Implement</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Implement</em>' containment reference.
   * @see #setImplement(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getTypeTerm_Implement()
   * @model containment="true"
   * @generated
   */
  JavaBody getImplement();

  /**
   * Sets the value of the '{@link tom.tomDsl.TypeTerm#getImplement <em>Implement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Implement</em>' containment reference.
   * @see #getImplement()
   * @generated
   */
  void setImplement(JavaBody value);

  /**
   * Returns the value of the '<em><b>Sort</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sort</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sort</em>' containment reference.
   * @see #setSort(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getTypeTerm_Sort()
   * @model containment="true"
   * @generated
   */
  JavaBody getSort();

  /**
   * Sets the value of the '{@link tom.tomDsl.TypeTerm#getSort <em>Sort</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sort</em>' containment reference.
   * @see #getSort()
   * @generated
   */
  void setSort(JavaBody value);

  /**
   * Returns the value of the '<em><b>Equals</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Equals</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Equals</em>' containment reference.
   * @see #setEquals(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getTypeTerm_Equals()
   * @model containment="true"
   * @generated
   */
  JavaBody getEquals();

  /**
   * Sets the value of the '{@link tom.tomDsl.TypeTerm#getEquals <em>Equals</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Equals</em>' containment reference.
   * @see #getEquals()
   * @generated
   */
  void setEquals(JavaBody value);

} // TypeTerm
