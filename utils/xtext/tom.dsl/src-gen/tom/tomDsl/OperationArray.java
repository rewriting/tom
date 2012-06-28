/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Array</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.OperationArray#getSize <em>Size</em>}</li>
 *   <li>{@link tom.tomDsl.OperationArray#getElement <em>Element</em>}</li>
 *   <li>{@link tom.tomDsl.OperationArray#getEmpty <em>Empty</em>}</li>
 *   <li>{@link tom.tomDsl.OperationArray#getAppend <em>Append</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getOperationArray()
 * @model
 * @generated
 */
public interface OperationArray extends ArrayOperation
{
  /**
   * Returns the value of the '<em><b>Size</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Size</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Size</em>' containment reference.
   * @see #setSize(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getOperationArray_Size()
   * @model containment="true"
   * @generated
   */
  JavaBody getSize();

  /**
   * Sets the value of the '{@link tom.tomDsl.OperationArray#getSize <em>Size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Size</em>' containment reference.
   * @see #getSize()
   * @generated
   */
  void setSize(JavaBody value);

  /**
   * Returns the value of the '<em><b>Element</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Element</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' containment reference.
   * @see #setElement(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getOperationArray_Element()
   * @model containment="true"
   * @generated
   */
  JavaBody getElement();

  /**
   * Sets the value of the '{@link tom.tomDsl.OperationArray#getElement <em>Element</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' containment reference.
   * @see #getElement()
   * @generated
   */
  void setElement(JavaBody value);

  /**
   * Returns the value of the '<em><b>Empty</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Empty</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Empty</em>' containment reference.
   * @see #setEmpty(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getOperationArray_Empty()
   * @model containment="true"
   * @generated
   */
  JavaBody getEmpty();

  /**
   * Sets the value of the '{@link tom.tomDsl.OperationArray#getEmpty <em>Empty</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Empty</em>' containment reference.
   * @see #getEmpty()
   * @generated
   */
  void setEmpty(JavaBody value);

  /**
   * Returns the value of the '<em><b>Append</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Append</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Append</em>' containment reference.
   * @see #setAppend(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getOperationArray_Append()
   * @model containment="true"
   * @generated
   */
  JavaBody getAppend();

  /**
   * Sets the value of the '{@link tom.tomDsl.OperationArray#getAppend <em>Append</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Append</em>' containment reference.
   * @see #getAppend()
   * @generated
   */
  void setAppend(JavaBody value);

} // OperationArray
