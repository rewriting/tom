/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage
 * @generated
 */
public interface LigneproduitstelephonesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LigneproduitstelephonesFactory eINSTANCE = ligneproduitstelephones.impl.LigneproduitstelephonesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Ligne Produits Telephones</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ligne Produits Telephones</em>'.
	 * @generated
	 */
	LigneProduitsTelephones createLigneProduitsTelephones();

	/**
	 * Returns a new object of class '<em>Marque</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Marque</em>'.
	 * @generated
	 */
	Marque createMarque();

	/**
	 * Returns a new object of class '<em>Telephone</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Telephone</em>'.
	 * @generated
	 */
	Telephone createTelephone();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	LigneproduitstelephonesPackage getLigneproduitstelephonesPackage();

} //LigneproduitstelephonesFactory
