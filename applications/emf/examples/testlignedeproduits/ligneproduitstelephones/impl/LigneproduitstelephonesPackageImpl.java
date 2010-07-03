/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import ligneproduitstelephones.LigneProduitsTelephones;
import ligneproduitstelephones.LigneproduitstelephonesFactory;
import ligneproduitstelephones.LigneproduitstelephonesPackage;
import ligneproduitstelephones.Marque;
import ligneproduitstelephones.OSTelephone;
import ligneproduitstelephones.Telephone;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LigneproduitstelephonesPackageImpl extends EPackageImpl implements LigneproduitstelephonesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ligneProduitsTelephonesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass marqueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass telephoneEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum osTelephoneEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see ligneproduitstelephones.LigneproduitstelephonesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private LigneproduitstelephonesPackageImpl() {
		super(eNS_URI, LigneproduitstelephonesFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link LigneproduitstelephonesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static LigneproduitstelephonesPackage init() {
		if (isInited) return (LigneproduitstelephonesPackage)EPackage.Registry.INSTANCE.getEPackage(LigneproduitstelephonesPackage.eNS_URI);

		// Obtain or create and register package
		LigneproduitstelephonesPackageImpl theLigneproduitstelephonesPackage = (LigneproduitstelephonesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LigneproduitstelephonesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LigneproduitstelephonesPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theLigneproduitstelephonesPackage.createPackageContents();

		// Initialize created meta-data
		theLigneproduitstelephonesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theLigneproduitstelephonesPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(LigneproduitstelephonesPackage.eNS_URI, theLigneproduitstelephonesPackage);
		return theLigneproduitstelephonesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLigneProduitsTelephones() {
		return ligneProduitsTelephonesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLigneProduitsTelephones_Name() {
		return (EAttribute)ligneProduitsTelephonesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLigneProduitsTelephones_Marque() {
		return (EReference)ligneProduitsTelephonesEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLigneProduitsTelephones_Telephones() {
		return (EReference)ligneProduitsTelephonesEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMarque() {
		return marqueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMarque_Name() {
		return (EAttribute)marqueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMarque_Telephones() {
		return (EReference)marqueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTelephone() {
		return telephoneEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTelephone_Name() {
		return (EAttribute)telephoneEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTelephone_Tactile() {
		return (EAttribute)telephoneEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTelephone_OSTelephone() {
		return (EAttribute)telephoneEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTelephone_Marque() {
		return (EReference)telephoneEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getOSTelephone() {
		return osTelephoneEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LigneproduitstelephonesFactory getLigneproduitstelephonesFactory() {
		return (LigneproduitstelephonesFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		ligneProduitsTelephonesEClass = createEClass(LIGNE_PRODUITS_TELEPHONES);
		createEAttribute(ligneProduitsTelephonesEClass, LIGNE_PRODUITS_TELEPHONES__NAME);
		createEReference(ligneProduitsTelephonesEClass, LIGNE_PRODUITS_TELEPHONES__MARQUE);
		createEReference(ligneProduitsTelephonesEClass, LIGNE_PRODUITS_TELEPHONES__TELEPHONES);

		marqueEClass = createEClass(MARQUE);
		createEAttribute(marqueEClass, MARQUE__NAME);
		createEReference(marqueEClass, MARQUE__TELEPHONES);

		telephoneEClass = createEClass(TELEPHONE);
		createEAttribute(telephoneEClass, TELEPHONE__NAME);
		createEAttribute(telephoneEClass, TELEPHONE__TACTILE);
		createEAttribute(telephoneEClass, TELEPHONE__OS_TELEPHONE);
		createEReference(telephoneEClass, TELEPHONE__MARQUE);

		// Create enums
		osTelephoneEEnum = createEEnum(OS_TELEPHONE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(ligneProduitsTelephonesEClass, LigneProduitsTelephones.class, "LigneProduitsTelephones", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLigneProduitsTelephones_Name(), ecorePackage.getEString(), "name", null, 0, 1, LigneProduitsTelephones.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLigneProduitsTelephones_Marque(), this.getMarque(), null, "marque", null, 0, -1, LigneProduitsTelephones.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLigneProduitsTelephones_Telephones(), this.getTelephone(), null, "telephones", null, 0, -1, LigneProduitsTelephones.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(marqueEClass, Marque.class, "Marque", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMarque_Name(), ecorePackage.getEString(), "name", null, 0, 1, Marque.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMarque_Telephones(), this.getTelephone(), this.getTelephone_Marque(), "telephones", null, 0, -1, Marque.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(telephoneEClass, Telephone.class, "Telephone", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTelephone_Name(), ecorePackage.getEString(), "name", null, 0, 1, Telephone.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTelephone_Tactile(), ecorePackage.getEBoolean(), "tactile", "false", 0, 1, Telephone.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTelephone_OSTelephone(), this.getOSTelephone(), "oSTelephone", null, 0, 1, Telephone.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTelephone_Marque(), this.getMarque(), this.getMarque_Telephones(), "marque", null, 0, 1, Telephone.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(osTelephoneEEnum, OSTelephone.class, "OSTelephone");
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.ANDROID);
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.MACOS);
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.WINDOWSCE);
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.BLACKBERRY);
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.SYMBIAN);
		addEEnumLiteral(osTelephoneEEnum, OSTelephone.OTHER);

		// Create resource
		createResource(eNS_URI);
	}

} //LigneproduitstelephonesPackageImpl
