/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.ArrayOperation#getTerm <em>Term</em>}</li>
 *   <li>{@link tom.tomDsl.ArrayOperation#getName <em>Name</em>}</li>
 *   <li>{@link tom.tomDsl.ArrayOperation#getFsym <em>Fsym</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getArrayOperation()
 * @model
 * @generated
 */
public interface ArrayOperation extends EObject
{
  /**
   * Returns the value of the '<em><b>Term</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Term</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Term</em>' attribute.
   * @see #setTerm(String)
   * @see tom.tomDsl.TomDslPackage#getArrayOperation_Term()
   * @model
   * @generated
   */
  String getTerm();

  /**
   * Sets the value of the '{@link tom.tomDsl.ArrayOperation#getTerm <em>Term</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Term</em>' attribute.
   * @see #getTerm()
   * @generated
   */
  void setTerm(String value);

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
   * @see tom.tomDsl.TomDslPackage#getArrayOperation_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link tom.tomDsl.ArrayOperation#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Fsym</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Fsym</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fsym</em>' containment reference.
   * @see #setFsym(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getArrayOperation_Fsym()
   * @model containment="true"
   * @generated
   */
  JavaBody getFsym();

  /**
   * Sets the value of the '{@link tom.tomDsl.ArrayOperation#getFsym <em>Fsym</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fsym</em>' containment reference.
   * @see #getFsym()
   * @generated
   */
  void setFsym(JavaBody value);

} // ArrayOperation
