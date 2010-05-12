/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import ligneproduitstelephones.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LigneproduitstelephonesFactoryImpl extends EFactoryImpl implements LigneproduitstelephonesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LigneproduitstelephonesFactory init() {
		try {
			LigneproduitstelephonesFactory theLigneproduitstelephonesFactory = (LigneproduitstelephonesFactory)EPackage.Registry.INSTANCE.getEFactory("http:///ligneproduitstelephones.ecore"); 
			if (theLigneproduitstelephonesFactory != null) {
				return theLigneproduitstelephonesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new LigneproduitstelephonesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LigneproduitstelephonesFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case LigneproduitstelephonesPackage.OS_TELEPHONE: return createOSTelephone();
			case LigneproduitstelephonesPackage.IPHONE_OS: return createIphoneOS();
			case LigneproduitstelephonesPackage.ANDROID: return createAndroid();
			case LigneproduitstelephonesPackage.MARQUE: return createMarque();
			case LigneproduitstelephonesPackage.TELEPHONE: return createTelephone();
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES: return createLigneProduitsTelephones();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OSTelephone createOSTelephone() {
		OSTelephoneImpl osTelephone = new OSTelephoneImpl();
		return osTelephone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IphoneOS createIphoneOS() {
		IphoneOSImpl iphoneOS = new IphoneOSImpl();
		return iphoneOS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Android createAndroid() {
		AndroidImpl android = new AndroidImpl();
		return android;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Marque createMarque() {
		MarqueImpl marque = new MarqueImpl();
		return marque;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Telephone createTelephone() {
		TelephoneImpl telephone = new TelephoneImpl();
		return telephone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LigneProduitsTelephones createLigneProduitsTelephones() {
		LigneProduitsTelephonesImpl ligneProduitsTelephones = new LigneProduitsTelephonesImpl();
		return ligneProduitsTelephones;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LigneproduitstelephonesPackage getLigneproduitstelephonesPackage() {
		return (LigneproduitstelephonesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static LigneproduitstelephonesPackage getPackage() {
		return LigneproduitstelephonesPackage.eINSTANCE;
	}

} //LigneproduitstelephonesFactoryImpl
