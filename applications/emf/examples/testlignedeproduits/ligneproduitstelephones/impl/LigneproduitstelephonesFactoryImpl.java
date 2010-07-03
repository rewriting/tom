/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.impl;

import ligneproduitstelephones.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
			case LigneproduitstelephonesPackage.LIGNE_PRODUITS_TELEPHONES: return createLigneProduitsTelephones();
			case LigneproduitstelephonesPackage.MARQUE: return createMarque();
			case LigneproduitstelephonesPackage.TELEPHONE: return createTelephone();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case LigneproduitstelephonesPackage.OS_TELEPHONE:
				return createOSTelephoneFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case LigneproduitstelephonesPackage.OS_TELEPHONE:
				return convertOSTelephoneToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
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
	public OSTelephone createOSTelephoneFromString(EDataType eDataType, String initialValue) {
		OSTelephone result = OSTelephone.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOSTelephoneToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
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
