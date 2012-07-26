/**
 */
package model.util;


import model.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ModelPackage.eINSTANCE;
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
	protected ModelSwitch modelSwitch =
		new ModelSwitch() {
			public Object caseMapping(Mapping object) {
				return createMappingAdapter();
			}
			public Object caseTerminal(Terminal object) {
				return createTerminalAdapter();
			}
			public Object caseOperator(Operator object) {
				return createOperatorAdapter();
			}
			public Object caseClassOperator(ClassOperator object) {
				return createClassOperatorAdapter();
			}
			public Object caseUserOperator(UserOperator object) {
				return createUserOperatorAdapter();
			}
			public Object caseParameter(Parameter object) {
				return createParameterAdapter();
			}
			public Object caseImport(Import object) {
				return createImportAdapter();
			}
			public Object caseAccessor(Accessor object) {
				return createAccessorAdapter();
			}
			public Object caseFeatureException(FeatureException object) {
				return createFeatureExceptionAdapter();
			}
			public Object caseFeatureParameter(FeatureParameter object) {
				return createFeatureParameterAdapter();
			}
			public Object caseSettedFeatureParameter(SettedFeatureParameter object) {
				return createSettedFeatureParameterAdapter();
			}
			public Object caseSettedValue(SettedValue object) {
				return createSettedValueAdapter();
			}
			public Object caseJavaCodeValue(JavaCodeValue object) {
				return createJavaCodeValueAdapter();
			}
			public Object caseEnumLiteralValue(EnumLiteralValue object) {
				return createEnumLiteralValueAdapter();
			}
			public Object caseAliasOperator(AliasOperator object) {
				return createAliasOperatorAdapter();
			}
			public Object caseAliasNode(AliasNode object) {
				return createAliasNodeAdapter();
			}
			public Object caseFeatureNode(FeatureNode object) {
				return createFeatureNodeAdapter();
			}
			public Object caseOperatorNode(OperatorNode object) {
				return createOperatorNodeAdapter();
			}
			public Object caseModule(Module object) {
				return createModuleAdapter();
			}
			public Object defaultCase(EObject object) {
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
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link model.Mapping <em>Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Mapping
	 * @generated
	 */
	public Adapter createMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Terminal
	 * @generated
	 */
	public Adapter createTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Operator <em>Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Operator
	 * @generated
	 */
	public Adapter createOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.ClassOperator <em>Class Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.ClassOperator
	 * @generated
	 */
	public Adapter createClassOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.UserOperator <em>User Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.UserOperator
	 * @generated
	 */
	public Adapter createUserOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Parameter
	 * @generated
	 */
	public Adapter createParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Import <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Import
	 * @generated
	 */
	public Adapter createImportAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Accessor <em>Accessor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Accessor
	 * @generated
	 */
	public Adapter createAccessorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.FeatureException <em>Feature Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.FeatureException
	 * @generated
	 */
	public Adapter createFeatureExceptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.FeatureParameter <em>Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.FeatureParameter
	 * @generated
	 */
	public Adapter createFeatureParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.SettedFeatureParameter <em>Setted Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.SettedFeatureParameter
	 * @generated
	 */
	public Adapter createSettedFeatureParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.SettedValue <em>Setted Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.SettedValue
	 * @generated
	 */
	public Adapter createSettedValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.JavaCodeValue <em>Java Code Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.JavaCodeValue
	 * @generated
	 */
	public Adapter createJavaCodeValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.EnumLiteralValue <em>Enum Literal Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.EnumLiteralValue
	 * @generated
	 */
	public Adapter createEnumLiteralValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.AliasOperator <em>Alias Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.AliasOperator
	 * @generated
	 */
	public Adapter createAliasOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.AliasNode <em>Alias Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.AliasNode
	 * @generated
	 */
	public Adapter createAliasNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.FeatureNode <em>Feature Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.FeatureNode
	 * @generated
	 */
	public Adapter createFeatureNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.OperatorNode <em>Operator Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.OperatorNode
	 * @generated
	 */
	public Adapter createOperatorNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link model.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see model.Module
	 * @generated
	 */
	public Adapter createModuleAdapter() {
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

} //ModelAdapterFactory
