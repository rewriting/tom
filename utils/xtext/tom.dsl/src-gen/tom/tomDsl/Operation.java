/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.Operation#getArg <em>Arg</em>}</li>
 *   <li>{@link tom.tomDsl.Operation#getSlot <em>Slot</em>}</li>
 *   <li>{@link tom.tomDsl.Operation#getMake <em>Make</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends ArrayOperation
{
  /**
   * Returns the value of the '<em><b>Arg</b></em>' containment reference list.
   * The list contents are of type {@link tom.tomDsl.ARG}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Arg</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Arg</em>' containment reference list.
   * @see tom.tomDsl.TomDslPackage#getOperation_Arg()
   * @model containment="true"
   * @generated
   */
  EList<ARG> getArg();

  /**
   * Returns the value of the '<em><b>Slot</b></em>' containment reference list.
   * The list contents are of type {@link tom.tomDsl.JavaBody}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Slot</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Slot</em>' containment reference list.
   * @see tom.tomDsl.TomDslPackage#getOperation_Slot()
   * @model containment="true"
   * @generated
   */
  EList<JavaBody> getSlot();

  /**
   * Returns the value of the '<em><b>Make</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Make</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Make</em>' containment reference.
   * @see #setMake(JavaBody)
   * @see tom.tomDsl.TomDslPackage#getOperation_Make()
   * @model containment="true"
   * @generated
   */
  JavaBody getMake();

  /**
   * Sets the value of the '{@link tom.tomDsl.Operation#getMake <em>Make</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Make</em>' containment reference.
   * @see #getMake()
   * @generated
   */
  void setMake(JavaBody value);

} // Operation
