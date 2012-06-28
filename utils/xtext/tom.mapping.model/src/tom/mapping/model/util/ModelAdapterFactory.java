/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package tom.mapping.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import tom.mapping.model.Accessor;
import tom.mapping.model.AliasNode;
import tom.mapping.model.AliasOperator;
import tom.mapping.model.ClassOperator;
import tom.mapping.model.EnumLiteralValue;
import tom.mapping.model.FeatureException;
import tom.mapping.model.FeatureNode;
import tom.mapping.model.FeatureParameter;
import tom.mapping.model.Import;
import tom.mapping.model.JavaCodeValue;
import tom.mapping.model.Mapping;
import tom.mapping.model.ModelPackage;
import tom.mapping.model.Module;
import tom.mapping.model.Operator;
import tom.mapping.model.OperatorNode;
import tom.mapping.model.Parameter;
import tom.mapping.model.SettedFeatureParameter;
import tom.mapping.model.SettedValue;
import tom.mapping.model.Terminal;
import tom.mapping.model.UserOperator;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see tom.mapping.model.ModelPackage
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
	protected ModelSwitch<Adapter> modelSwitch =
		new ModelSwitch<Adapter>() {
			@Override
			public Adapter caseMapping(Mapping object) {
				return createMappingAdapter();
			}
			@Override
			public Adapter caseTerminal(Terminal object) {
				return createTerminalAdapter();
			}
			@Override
			public Adapter caseOperator(Operator object) {
				return createOperatorAdapter();
			}
			@Override
			public Adapter caseClassOperator(ClassOperator object) {
				return createClassOperatorAdapter();
			}
			@Override
			public Adapter caseUserOperator(UserOperator object) {
				return createUserOperatorAdapter();
			}
			@Override
			public Adapter caseParameter(Parameter object) {
				return createParameterAdapter();
			}
			@Override
			public Adapter caseImport(Import object) {
				return createImportAdapter();
			}
			@Override
			public Adapter caseAccessor(Accessor object) {
				return createAccessorAdapter();
			}
			@Override
			public Adapter caseFeatureException(FeatureException object) {
				return createFeatureExceptionAdapter();
			}
			@Override
			public Adapter caseFeatureParameter(FeatureParameter object) {
				return createFeatureParameterAdapter();
			}
			@Override
			public Adapter caseSettedFeatureParameter(SettedFeatureParameter object) {
				return createSettedFeatureParameterAdapter();
			}
			@Override
			public Adapter caseSettedValue(SettedValue object) {
				return createSettedValueAdapter();
			}
			@Override
			public Adapter caseJavaCodeValue(JavaCodeValue object) {
				return createJavaCodeValueAdapter();
			}
			@Override
			public Adapter caseEnumLiteralValue(EnumLiteralValue object) {
				return createEnumLiteralValueAdapter();
			}
			@Override
			public Adapter caseAliasOperator(AliasOperator object) {
				return createAliasOperatorAdapter();
			}
			@Override
			public Adapter caseAliasNode(AliasNode object) {
				return createAliasNodeAdapter();
			}
			@Override
			public Adapter caseFeatureNode(FeatureNode object) {
				return createFeatureNodeAdapter();
			}
			@Override
			public Adapter caseOperatorNode(OperatorNode object) {
				return createOperatorNodeAdapter();
			}
			@Override
			public Adapter caseModule(Module object) {
				return createModuleAdapter();
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
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Mapping <em>Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Mapping
	 * @generated
	 */
	public Adapter createMappingAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Terminal
	 * @generated
	 */
	public Adapter createTerminalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Operator <em>Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Operator
	 * @generated
	 */
	public Adapter createOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.ClassOperator <em>Class Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.ClassOperator
	 * @generated
	 */
	public Adapter createClassOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.UserOperator <em>User Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.UserOperator
	 * @generated
	 */
	public Adapter createUserOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Parameter
	 * @generated
	 */
	public Adapter createParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Import <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Import
	 * @generated
	 */
	public Adapter createImportAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Accessor <em>Accessor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Accessor
	 * @generated
	 */
	public Adapter createAccessorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.FeatureException <em>Feature Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.FeatureException
	 * @generated
	 */
	public Adapter createFeatureExceptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.FeatureParameter <em>Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.FeatureParameter
	 * @generated
	 */
	public Adapter createFeatureParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.SettedFeatureParameter <em>Setted Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.SettedFeatureParameter
	 * @generated
	 */
	public Adapter createSettedFeatureParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.SettedValue <em>Setted Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.SettedValue
	 * @generated
	 */
	public Adapter createSettedValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.JavaCodeValue <em>Java Code Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.JavaCodeValue
	 * @generated
	 */
	public Adapter createJavaCodeValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.EnumLiteralValue <em>Enum Literal Value</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.EnumLiteralValue
	 * @generated
	 */
	public Adapter createEnumLiteralValueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.AliasOperator <em>Alias Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.AliasOperator
	 * @generated
	 */
	public Adapter createAliasOperatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.AliasNode <em>Alias Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.AliasNode
	 * @generated
	 */
	public Adapter createAliasNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.FeatureNode <em>Feature Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.FeatureNode
	 * @generated
	 */
	public Adapter createFeatureNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.OperatorNode <em>Operator Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.OperatorNode
	 * @generated
	 */
	public Adapter createOperatorNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link tom.mapping.model.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see tom.mapping.model.Module
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
