/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Telephone</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ligneproduitstelephones.Telephone#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.Telephone#getMarque <em>Marque</em>}</li>
 *   <li>{@link ligneproduitstelephones.Telephone#getOS <em>OS</em>}</li>
 * </ul>
 * </p>
 *
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getTelephone()
 * @model
 * @generated
 */
public interface Telephone extends EObject {
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
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getTelephone_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Marque</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link ligneproduitstelephones.Marque#getTelephones <em>Telephones</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Marque</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Marque</em>' reference.
	 * @see #setMarque(Marque)
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getTelephone_Marque()
	 * @see ligneproduitstelephones.Marque#getTelephones
	 * @model opposite="telephones" required="true"
	 * @generated
	 */
	Marque getMarque();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getMarque <em>Marque</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Marque</em>' reference.
	 * @see #getMarque()
	 * @generated
	 */
	void setMarque(Marque value);

	/**
	 * Returns the value of the '<em><b>OS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>OS</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>OS</em>' containment reference.
	 * @see #setOS(OSTelephone)
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getTelephone_OS()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OSTelephone getOS();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Telephone#getOS <em>OS</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>OS</em>' containment reference.
	 * @see #getOS()
	 * @generated
	 */
	void setOS(OSTelephone value);

} // Telephone
