/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ligneproduitstelephones.LigneproduitstelephonesFactory
 * @model kind="package"
 * @generated
 */
public interface LigneproduitstelephonesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ligneproduitstelephones";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///ligneproduitstelephones.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ligneproduitstelephones";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	LigneproduitstelephonesPackage eINSTANCE = ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl.init();

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl <em>Ligne Produits Telephones</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.LigneProduitsTelephonesImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getLigneProduitsTelephones()
	 * @generated
	 */
	int LIGNE_PRODUITS_TELEPHONES = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__NAME = 0;

	/**
	 * The feature id for the '<em><b>Marque</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__MARQUE = 1;

	/**
	 * The feature id for the '<em><b>Telephones</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__TELEPHONES = 2;

	/**
	 * The number of structural features of the '<em>Ligne Produits Telephones</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.MarqueImpl <em>Marque</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.MarqueImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getMarque()
	 * @generated
	 */
	int MARQUE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARQUE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Telephones</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARQUE__TELEPHONES = 1;

	/**
	 * The number of structural features of the '<em>Marque</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARQUE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.TelephoneImpl <em>Telephone</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.TelephoneImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getTelephone()
	 * @generated
	 */
	int TELEPHONE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Tactile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__TACTILE = 1;

	/**
	 * The feature id for the '<em><b>OS Telephone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__OS_TELEPHONE = 2;

	/**
	 * The feature id for the '<em><b>Marque</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__MARQUE = 3;

	/**
	 * The number of structural features of the '<em>Telephone</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.OSTelephone <em>OS Telephone</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.OSTelephone
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getOSTelephone()
	 * @generated
	 */
	int OS_TELEPHONE = 3;


	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.LigneProduitsTelephones <em>Ligne Produits Telephones</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ligne Produits Telephones</em>'.
	 * @see ligneproduitstelephones.LigneProduitsTelephones
	 * @generated
	 */
	EClass getLigneProduitsTelephones();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.LigneProduitsTelephones#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ligneproduitstelephones.LigneProduitsTelephones#getName()
	 * @see #getLigneProduitsTelephones()
	 * @generated
	 */
	EAttribute getLigneProduitsTelephones_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link ligneproduitstelephones.LigneProduitsTelephones#getMarque <em>Marque</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Marque</em>'.
	 * @see ligneproduitstelephones.LigneProduitsTelephones#getMarque()
	 * @see #getLigneProduitsTelephones()
	 * @generated
	 */
	EReference getLigneProduitsTelephones_Marque();

	/**
	 * Returns the meta object for the containment reference list '{@link ligneproduitstelephones.LigneProduitsTelephones#getTelephones <em>Telephones</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Telephones</em>'.
	 * @see ligneproduitstelephones.LigneProduitsTelephones#getTelephones()
	 * @see #getLigneProduitsTelephones()
	 * @generated
	 */
	EReference getLigneProduitsTelephones_Telephones();

	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.Marque <em>Marque</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marque</em>'.
	 * @see ligneproduitstelephones.Marque
	 * @generated
	 */
	EClass getMarque();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.Marque#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ligneproduitstelephones.Marque#getName()
	 * @see #getMarque()
	 * @generated
	 */
	EAttribute getMarque_Name();

	/**
	 * Returns the meta object for the reference list '{@link ligneproduitstelephones.Marque#getTelephones <em>Telephones</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Telephones</em>'.
	 * @see ligneproduitstelephones.Marque#getTelephones()
	 * @see #getMarque()
	 * @generated
	 */
	EReference getMarque_Telephones();

	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.Telephone <em>Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Telephone</em>'.
	 * @see ligneproduitstelephones.Telephone
	 * @generated
	 */
	EClass getTelephone();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.Telephone#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ligneproduitstelephones.Telephone#getName()
	 * @see #getTelephone()
	 * @generated
	 */
	EAttribute getTelephone_Name();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.Telephone#isTactile <em>Tactile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Tactile</em>'.
	 * @see ligneproduitstelephones.Telephone#isTactile()
	 * @see #getTelephone()
	 * @generated
	 */
	EAttribute getTelephone_Tactile();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.Telephone#getOSTelephone <em>OS Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>OS Telephone</em>'.
	 * @see ligneproduitstelephones.Telephone#getOSTelephone()
	 * @see #getTelephone()
	 * @generated
	 */
	EAttribute getTelephone_OSTelephone();

	/**
	 * Returns the meta object for the reference '{@link ligneproduitstelephones.Telephone#getMarque <em>Marque</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Marque</em>'.
	 * @see ligneproduitstelephones.Telephone#getMarque()
	 * @see #getTelephone()
	 * @generated
	 */
	EReference getTelephone_Marque();

	/**
	 * Returns the meta object for enum '{@link ligneproduitstelephones.OSTelephone <em>OS Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>OS Telephone</em>'.
	 * @see ligneproduitstelephones.OSTelephone
	 * @generated
	 */
	EEnum getOSTelephone();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	LigneproduitstelephonesFactory getLigneproduitstelephonesFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl <em>Ligne Produits Telephones</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.LigneProduitsTelephonesImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getLigneProduitsTelephones()
		 * @generated
		 */
		EClass LIGNE_PRODUITS_TELEPHONES = eINSTANCE.getLigneProduitsTelephones();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LIGNE_PRODUITS_TELEPHONES__NAME = eINSTANCE.getLigneProduitsTelephones_Name();

		/**
		 * The meta object literal for the '<em><b>Marque</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIGNE_PRODUITS_TELEPHONES__MARQUE = eINSTANCE.getLigneProduitsTelephones_Marque();

		/**
		 * The meta object literal for the '<em><b>Telephones</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIGNE_PRODUITS_TELEPHONES__TELEPHONES = eINSTANCE.getLigneProduitsTelephones_Telephones();

		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.MarqueImpl <em>Marque</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.MarqueImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getMarque()
		 * @generated
		 */
		EClass MARQUE = eINSTANCE.getMarque();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MARQUE__NAME = eINSTANCE.getMarque_Name();

		/**
		 * The meta object literal for the '<em><b>Telephones</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MARQUE__TELEPHONES = eINSTANCE.getMarque_Telephones();

		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.TelephoneImpl <em>Telephone</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.TelephoneImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getTelephone()
		 * @generated
		 */
		EClass TELEPHONE = eINSTANCE.getTelephone();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TELEPHONE__NAME = eINSTANCE.getTelephone_Name();

		/**
		 * The meta object literal for the '<em><b>Tactile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TELEPHONE__TACTILE = eINSTANCE.getTelephone_Tactile();

		/**
		 * The meta object literal for the '<em><b>OS Telephone</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TELEPHONE__OS_TELEPHONE = eINSTANCE.getTelephone_OSTelephone();

		/**
		 * The meta object literal for the '<em><b>Marque</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TELEPHONE__MARQUE = eINSTANCE.getTelephone_Marque();

		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.OSTelephone <em>OS Telephone</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.OSTelephone
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getOSTelephone()
		 * @generated
		 */
		EEnum OS_TELEPHONE = eINSTANCE.getOSTelephone();

	}

} //LigneproduitstelephonesPackage
