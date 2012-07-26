/**
 */
package model.impl;


import model.*;

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
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelFactory init() {
		try {
			ModelFactory theModelFactory = (ModelFactory)EPackage.Registry.INSTANCE.getEFactory("tom.mapping.model"); 
			if (theModelFactory != null) {
				return theModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ModelPackage.MAPPING: return createMapping();
			case ModelPackage.TERMINAL: return createTerminal();
			case ModelPackage.CLASS_OPERATOR: return createClassOperator();
			case ModelPackage.USER_OPERATOR: return createUserOperator();
			case ModelPackage.PARAMETER: return createParameter();
			case ModelPackage.IMPORT: return createImport();
			case ModelPackage.ACCESSOR: return createAccessor();
			case ModelPackage.FEATURE_EXCEPTION: return createFeatureException();
			case ModelPackage.FEATURE_PARAMETER: return createFeatureParameter();
			case ModelPackage.SETTED_FEATURE_PARAMETER: return createSettedFeatureParameter();
			case ModelPackage.SETTED_VALUE: return createSettedValue();
			case ModelPackage.JAVA_CODE_VALUE: return createJavaCodeValue();
			case ModelPackage.ENUM_LITERAL_VALUE: return createEnumLiteralValue();
			case ModelPackage.ALIAS_OPERATOR: return createAliasOperator();
			case ModelPackage.ALIAS_NODE: return createAliasNode();
			case ModelPackage.FEATURE_NODE: return createFeatureNode();
			case ModelPackage.OPERATOR_NODE: return createOperatorNode();
			case ModelPackage.MODULE: return createModule();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Mapping createMapping() {
		MappingImpl mapping = new MappingImpl();
		return mapping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Terminal createTerminal() {
		TerminalImpl terminal = new TerminalImpl();
		return terminal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ClassOperator createClassOperator() {
		ClassOperatorImpl classOperator = new ClassOperatorImpl();
		return classOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserOperator createUserOperator() {
		UserOperatorImpl userOperator = new UserOperatorImpl();
		return userOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Import createImport() {
		ImportImpl import_ = new ImportImpl();
		return import_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Accessor createAccessor() {
		AccessorImpl accessor = new AccessorImpl();
		return accessor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureException createFeatureException() {
		FeatureExceptionImpl featureException = new FeatureExceptionImpl();
		return featureException;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureParameter createFeatureParameter() {
		FeatureParameterImpl featureParameter = new FeatureParameterImpl();
		return featureParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SettedFeatureParameter createSettedFeatureParameter() {
		SettedFeatureParameterImpl settedFeatureParameter = new SettedFeatureParameterImpl();
		return settedFeatureParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SettedValue createSettedValue() {
		SettedValueImpl settedValue = new SettedValueImpl();
		return settedValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaCodeValue createJavaCodeValue() {
		JavaCodeValueImpl javaCodeValue = new JavaCodeValueImpl();
		return javaCodeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteralValue createEnumLiteralValue() {
		EnumLiteralValueImpl enumLiteralValue = new EnumLiteralValueImpl();
		return enumLiteralValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AliasOperator createAliasOperator() {
		AliasOperatorImpl aliasOperator = new AliasOperatorImpl();
		return aliasOperator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AliasNode createAliasNode() {
		AliasNodeImpl aliasNode = new AliasNodeImpl();
		return aliasNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureNode createFeatureNode() {
		FeatureNodeImpl featureNode = new FeatureNodeImpl();
		return featureNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperatorNode createOperatorNode() {
		OperatorNodeImpl operatorNode = new OperatorNodeImpl();
		return operatorNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Module createModule() {
		ModuleImpl module = new ModuleImpl();
		return module;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPackage getModelPackage() {
		return (ModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static ModelPackage getPackage() {
		return ModelPackage.eINSTANCE;
	}

} //ModelFactoryImpl
