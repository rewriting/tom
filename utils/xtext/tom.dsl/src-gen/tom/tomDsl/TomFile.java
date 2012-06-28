/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tom File</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tom.tomDsl.TomFile#getOps <em>Ops</em>}</li>
 *   <li>{@link tom.tomDsl.TomFile#getTerms <em>Terms</em>}</li>
 *   <li>{@link tom.tomDsl.TomFile#getInc <em>Inc</em>}</li>
 *   <li>{@link tom.tomDsl.TomFile#getLocals <em>Locals</em>}</li>
 * </ul>
 * </p>
 *
 * @see tom.tomDsl.TomDslPackage#getTomFile()
 * @model
 * @generated
 */
public interface TomFile extends EObject
{
  /**
   * Returns the value of the '<em><b>Ops</b></em>' containment reference list.
   * The list contents are of type {@link tom.tomDsl.ArrayOperation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ops</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ops</em>' containment reference list.
   * @see tom.tomDsl.TomDslPackage#getTomFile_Ops()
   * @model containment="true"
   * @generated
   */
  EList<ArrayOperation> getOps();

  /**
   * Returns the value of the '<em><b>Terms</b></em>' containment reference list.
   * The list contents are of type {@link tom.tomDsl.TypeTerm}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Terms</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Terms</em>' containment reference list.
   * @see tom.tomDsl.TomDslPackage#getTomFile_Terms()
   * @model containment="true"
   * @generated
   */
  EList<TypeTerm> getTerms();

  /**
   * Returns the value of the '<em><b>Inc</b></em>' containment reference list.
   * The list contents are of type {@link tom.tomDsl.Include}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Inc</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Inc</em>' containment reference list.
   * @see tom.tomDsl.TomDslPackage#getTomFile_Inc()
   * @model containment="true"
   * @generated
   */
  EList<Include> getInc();

  /**
   * Returns the value of the '<em><b>Locals</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Locals</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Locals</em>' attribute.
   * @see #setLocals(String)
   * @see tom.tomDsl.TomDslPackage#getTomFile_Locals()
   * @model
   * @generated
   */
  String getLocals();

  /**
   * Sets the value of the '{@link tom.tomDsl.TomFile#getLocals <em>Locals</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Locals</em>' attribute.
   * @see #getLocals()
   * @generated
   */
  void setLocals(String value);

} // TomFile
