/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
	 * The meta object id for the '{@link ligneproduitstelephones.impl.OSTelephoneImpl <em>OS Telephone</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.OSTelephoneImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getOSTelephone()
	 * @generated
	 */
	int OS_TELEPHONE = 0;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_TELEPHONE__VERSION = 0;

	/**
	 * The number of structural features of the '<em>OS Telephone</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OS_TELEPHONE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.IphoneOSImpl <em>Iphone OS</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.IphoneOSImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getIphoneOS()
	 * @generated
	 */
	int IPHONE_OS = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPHONE_OS__VERSION = OS_TELEPHONE__VERSION;

	/**
	 * The number of structural features of the '<em>Iphone OS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IPHONE_OS_FEATURE_COUNT = OS_TELEPHONE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.AndroidImpl <em>Android</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.AndroidImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getAndroid()
	 * @generated
	 */
	int ANDROID = 2;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANDROID__VERSION = OS_TELEPHONE__VERSION;

	/**
	 * The number of structural features of the '<em>Android</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANDROID_FEATURE_COUNT = OS_TELEPHONE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.MarqueImpl <em>Marque</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.MarqueImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getMarque()
	 * @generated
	 */
	int MARQUE = 3;

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
	int TELEPHONE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Marque</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__MARQUE = 1;

	/**
	 * The feature id for the '<em><b>OS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE__OS = 2;

	/**
	 * The number of structural features of the '<em>Telephone</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TELEPHONE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link ligneproduitstelephones.impl.LigneProduitsTelephonesImpl <em>Ligne Produits Telephones</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ligneproduitstelephones.impl.LigneProduitsTelephonesImpl
	 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getLigneProduitsTelephones()
	 * @generated
	 */
	int LIGNE_PRODUITS_TELEPHONES = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__NAME = 0;

	/**
	 * The feature id for the '<em><b>Telephones</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__TELEPHONES = 1;

	/**
	 * The feature id for the '<em><b>Marques</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES__MARQUES = 2;

	/**
	 * The number of structural features of the '<em>Ligne Produits Telephones</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LIGNE_PRODUITS_TELEPHONES_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.OSTelephone <em>OS Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>OS Telephone</em>'.
	 * @see ligneproduitstelephones.OSTelephone
	 * @generated
	 */
	EClass getOSTelephone();

	/**
	 * Returns the meta object for the attribute '{@link ligneproduitstelephones.OSTelephone#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see ligneproduitstelephones.OSTelephone#getVersion()
	 * @see #getOSTelephone()
	 * @generated
	 */
	EAttribute getOSTelephone_Version();

	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.IphoneOS <em>Iphone OS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Iphone OS</em>'.
	 * @see ligneproduitstelephones.IphoneOS
	 * @generated
	 */
	EClass getIphoneOS();

	/**
	 * Returns the meta object for class '{@link ligneproduitstelephones.Android <em>Android</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Android</em>'.
	 * @see ligneproduitstelephones.Android
	 * @generated
	 */
	EClass getAndroid();

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
	 * Returns the meta object for the containment reference '{@link ligneproduitstelephones.Telephone#getOS <em>OS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>OS</em>'.
	 * @see ligneproduitstelephones.Telephone#getOS()
	 * @see #getTelephone()
	 * @generated
	 */
	EReference getTelephone_OS();

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
	 * Returns the meta object for the containment reference list '{@link ligneproduitstelephones.LigneProduitsTelephones#getMarques <em>Marques</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Marques</em>'.
	 * @see ligneproduitstelephones.LigneProduitsTelephones#getMarques()
	 * @see #getLigneProduitsTelephones()
	 * @generated
	 */
	EReference getLigneProduitsTelephones_Marques();

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
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.OSTelephoneImpl <em>OS Telephone</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.OSTelephoneImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getOSTelephone()
		 * @generated
		 */
		EClass OS_TELEPHONE = eINSTANCE.getOSTelephone();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OS_TELEPHONE__VERSION = eINSTANCE.getOSTelephone_Version();

		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.IphoneOSImpl <em>Iphone OS</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.IphoneOSImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getIphoneOS()
		 * @generated
		 */
		EClass IPHONE_OS = eINSTANCE.getIphoneOS();

		/**
		 * The meta object literal for the '{@link ligneproduitstelephones.impl.AndroidImpl <em>Android</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ligneproduitstelephones.impl.AndroidImpl
		 * @see ligneproduitstelephones.impl.LigneproduitstelephonesPackageImpl#getAndroid()
		 * @generated
		 */
		EClass ANDROID = eINSTANCE.getAndroid();

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
		 * The meta object literal for the '<em><b>Marque</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TELEPHONE__MARQUE = eINSTANCE.getTelephone_Marque();

		/**
		 * The meta object literal for the '<em><b>OS</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TELEPHONE__OS = eINSTANCE.getTelephone_OS();

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
		 * The meta object literal for the '<em><b>Telephones</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIGNE_PRODUITS_TELEPHONES__TELEPHONES = eINSTANCE.getLigneProduitsTelephones_Telephones();

		/**
		 * The meta object literal for the '<em><b>Marques</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LIGNE_PRODUITS_TELEPHONES__MARQUES = eINSTANCE.getLigneProduitsTelephones_Marques();

	}

} //LigneproduitstelephonesPackage
