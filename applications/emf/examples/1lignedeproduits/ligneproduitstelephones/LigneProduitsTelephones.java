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
 * A representation of the model object '<em><b>Ligne Produits Telephones</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ligneproduitstelephones.LigneProduitsTelephones#getName <em>Name</em>}</li>
 *   <li>{@link ligneproduitstelephones.LigneProduitsTelephones#getTelephones <em>Telephones</em>}</li>
 *   <li>{@link ligneproduitstelephones.LigneProduitsTelephones#getMarques <em>Marques</em>}</li>
 * </ul>
 * </p>
 *
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getLigneProduitsTelephones()
 * @model
 * @generated
 */
public interface LigneProduitsTelephones extends EObject {
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
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getLigneProduitsTelephones_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ligneproduitstelephones.LigneProduitsTelephones#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Telephones</b></em>' containment reference list.
	 * The list contents are of type {@link ligneproduitstelephones.Telephone}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Telephones</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Telephones</em>' containment reference list.
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getLigneProduitsTelephones_Telephones()
	 * @model containment="true"
	 * @generated
	 */
	EList<Telephone> getTelephones();

	/**
	 * Returns the value of the '<em><b>Marques</b></em>' containment reference list.
	 * The list contents are of type {@link ligneproduitstelephones.Marque}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Marques</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Marques</em>' containment reference list.
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#getLigneProduitsTelephones_Marques()
	 * @model containment="true"
	 * @generated
	 */
	EList<Marque> getMarques();

} // LigneProduitsTelephones
