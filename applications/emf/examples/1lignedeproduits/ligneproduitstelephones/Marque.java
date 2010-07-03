/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marque</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ligneproduitstelephones.Marque#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.Marque#getTelephones <em>Telephones</em>}</li>
 * </ul>
 * </p>
 *
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getMarque()
 * @model
 * @generated
 */
public interface Marque extends EObject {
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
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getMarque_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.Marque#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Telephones</b></em>' reference list.
	 * The list contents are of type {@link ligneproduitstelephones.Telephone}.
	 * It is bidirectional and its opposite is '{@link ligneproduitstelephones.Telephone#getMarque <em>Marque</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Telephones</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Telephones</em>' reference list.
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getMarque_Telephones()
	 * @see ligneproduitstelephones.Telephone#getMarque
	 * @model opposite="marque"
	 * @generated
	 */
	EList<Telephone> getTelephones();

} // Marque
