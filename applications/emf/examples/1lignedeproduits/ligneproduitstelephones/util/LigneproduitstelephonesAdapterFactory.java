/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package ligneproduitstelephones.util;

import ligneproduitstelephones.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see ligneproduitstelephones.LigneproduitstelephonesPackage
 * @generated
 */
public class LigneproduitstelephonesAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static LigneproduitstelephonesPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LigneproduitstelephonesAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = LigneproduitstelephonesPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LigneproduitstelephonesSwitch<Adapter> modelSwitch =
		new LigneproduitstelephonesSwitch<Adapter>() {
			@Override
			public Adapter caseOSTelephone(OSTelephone object) {
				return createOSTelephoneAdapter();
			}
			@Override
			public Adapter caseIphoneOS(IphoneOS object) {
				return createIphoneOSAdapter();
			}
			@Override
			public Adapter caseAndroid(Android object) {
				return createAndroidAdapter();
			}
			@Override
			public Adapter caseMarque(Marque object) {
				return createMarqueAdapter();
			}
			@Override
			public Adapter caseTelephone(Telephone object) {
				return createTelephoneAdapter();
			}
			@Override
			public Adapter caseLigneProduitsTelephones(LigneProduitsTelephones object) {
				return createLigneProduitsTelephonesAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.OSTelephone <em>OS Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.OSTelephone
	 * @generated
	 */
	public Adapter createOSTelephoneAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.IphoneOS <em>Iphone OS</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.IphoneOS
	 * @generated
	 */
	public Adapter createIphoneOSAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.Android <em>Android</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.Android
	 * @generated
	 */
	public Adapter createAndroidAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.Marque <em>Marque</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.Marque
	 * @generated
	 */
	public Adapter createMarqueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.Telephone <em>Telephone</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.Telephone
	 * @generated
	 */
	public Adapter createTelephoneAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ligneproduitstelephones.LigneProduitsTelephones <em>Ligne Produits Telephones</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ligneproduitstelephones.LigneProduitsTelephones
	 * @generated
	 */
	public Adapter createLigneProduitsTelephonesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //LigneproduitstelephonesAdapterFactory
