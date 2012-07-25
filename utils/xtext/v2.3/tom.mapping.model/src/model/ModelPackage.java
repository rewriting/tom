/**
 */
package model;

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
 * @see model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "tom.mapping.model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link model.impl.MappingImpl <em>Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.MappingImpl
	 * @see model.impl.ModelPackageImpl#getMapping()
	 * @generated
	 */
	int MAPPING = 0;

	/**
	 * The feature id for the '<em><b>Imports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__IMPORTS = 0;

	/**
	 * The feature id for the '<em><b>Terminals</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__TERMINALS = 1;

	/**
	 * The feature id for the '<em><b>External Terminals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__EXTERNAL_TERMINALS = 2;

	/**
	 * The feature id for the '<em><b>Operators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__OPERATORS = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__NAME = 4;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__PREFIX = 5;

	/**
	 * The feature id for the '<em><b>Modules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING__MODULES = 6;

	/**
	 * The number of structural features of the '<em>Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link model.impl.TerminalImpl <em>Terminal</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.TerminalImpl
	 * @see model.impl.ModelPackageImpl#getTerminal()
	 * @generated
	 */
	int TERMINAL = 1;

	/**
	 * The feature id for the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__CLASS = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__NAME = 1;

	/**
	 * The feature id for the '<em><b>Many</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL__MANY = 2;

	/**
	 * The number of structural features of the '<em>Terminal</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TERMINAL_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link model.Operator <em>Operator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.Operator
	 * @see model.impl.ModelPackageImpl#getOperator()
	 * @generated
	 */
	int OPERATOR = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATOR__NAME = 0;

	/**
	 * The number of structural features of the '<em>Operator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATOR_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link model.impl.ClassOperatorImpl <em>Class Operator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.ClassOperatorImpl
	 * @see model.impl.ModelPackageImpl#getClassOperator()
	 * @generated
	 */
	int CLASS_OPERATOR = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_OPERATOR__NAME = OPERATOR__NAME;

	/**
	 * The feature id for the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_OPERATOR__CLASS = OPERATOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_OPERATOR__PARAMETERS = OPERATOR_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Class Operator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASS_OPERATOR_FEATURE_COUNT = OPERATOR_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link model.impl.UserOperatorImpl <em>User Operator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.UserOperatorImpl
	 * @see model.impl.ModelPackageImpl#getUserOperator()
	 * @generated
	 */
	int USER_OPERATOR = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__NAME = OPERATOR__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__TYPE = OPERATOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__PARAMETERS = OPERATOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Accessors</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__ACCESSORS = OPERATOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Make</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__MAKE = OPERATOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Test</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR__TEST = OPERATOR_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>User Operator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USER_OPERATOR_FEATURE_COUNT = OPERATOR_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link model.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.ParameterImpl
	 * @see model.impl.ModelPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = 1;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link model.impl.ImportImpl <em>Import</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.ImportImpl
	 * @see model.impl.ModelPackageImpl#getImport()
	 * @generated
	 */
	int IMPORT = 6;

	/**
	 * The feature id for the '<em><b>Import URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT__IMPORT_URI = 0;

	/**
	 * The number of structural features of the '<em>Import</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link model.impl.AccessorImpl <em>Accessor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.AccessorImpl
	 * @see model.impl.ModelPackageImpl#getAccessor()
	 * @generated
	 */
	int ACCESSOR = 7;

	/**
	 * The feature id for the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCESSOR__SLOT = 0;

	/**
	 * The feature id for the '<em><b>Java</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCESSOR__JAVA = 1;

	/**
	 * The number of structural features of the '<em>Accessor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ACCESSOR_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link model.impl.FeatureParameterImpl <em>Feature Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.FeatureParameterImpl
	 * @see model.impl.ModelPackageImpl#getFeatureParameter()
	 * @generated
	 */
	int FEATURE_PARAMETER = 9;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_PARAMETER__FEATURE = 0;

	/**
	 * The number of structural features of the '<em>Feature Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_PARAMETER_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link model.impl.FeatureExceptionImpl <em>Feature Exception</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.FeatureExceptionImpl
	 * @see model.impl.ModelPackageImpl#getFeatureException()
	 * @generated
	 */
	int FEATURE_EXCEPTION = 8;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_EXCEPTION__FEATURE = FEATURE_PARAMETER__FEATURE;

	/**
	 * The number of structural features of the '<em>Feature Exception</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_EXCEPTION_FEATURE_COUNT = FEATURE_PARAMETER_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link model.impl.SettedFeatureParameterImpl <em>Setted Feature Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.SettedFeatureParameterImpl
	 * @see model.impl.ModelPackageImpl#getSettedFeatureParameter()
	 * @generated
	 */
	int SETTED_FEATURE_PARAMETER = 10;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTED_FEATURE_PARAMETER__FEATURE = FEATURE_PARAMETER__FEATURE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTED_FEATURE_PARAMETER__VALUE = FEATURE_PARAMETER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Setted Feature Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTED_FEATURE_PARAMETER_FEATURE_COUNT = FEATURE_PARAMETER_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link model.impl.SettedValueImpl <em>Setted Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.SettedValueImpl
	 * @see model.impl.ModelPackageImpl#getSettedValue()
	 * @generated
	 */
	int SETTED_VALUE = 11;

	/**
	 * The number of structural features of the '<em>Setted Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SETTED_VALUE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link model.impl.JavaCodeValueImpl <em>Java Code Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.JavaCodeValueImpl
	 * @see model.impl.ModelPackageImpl#getJavaCodeValue()
	 * @generated
	 */
	int JAVA_CODE_VALUE = 12;

	/**
	 * The feature id for the '<em><b>Java</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_CODE_VALUE__JAVA = SETTED_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Java Code Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA_CODE_VALUE_FEATURE_COUNT = SETTED_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link model.impl.EnumLiteralValueImpl <em>Enum Literal Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.EnumLiteralValueImpl
	 * @see model.impl.ModelPackageImpl#getEnumLiteralValue()
	 * @generated
	 */
	int ENUM_LITERAL_VALUE = 13;

	/**
	 * The feature id for the '<em><b>Literal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_VALUE__LITERAL = SETTED_VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Literal Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_VALUE_FEATURE_COUNT = SETTED_VALUE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link model.impl.AliasOperatorImpl <em>Alias Operator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.AliasOperatorImpl
	 * @see model.impl.ModelPackageImpl#getAliasOperator()
	 * @generated
	 */
	int ALIAS_OPERATOR = 14;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALIAS_OPERATOR__NAME = OPERATOR__NAME;

	/**
	 * The feature id for the '<em><b>Op</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALIAS_OPERATOR__OP = OPERATOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALIAS_OPERATOR__NODES = OPERATOR_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Alias Operator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALIAS_OPERATOR_FEATURE_COUNT = OPERATOR_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link model.impl.AliasNodeImpl <em>Alias Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.AliasNodeImpl
	 * @see model.impl.ModelPackageImpl#getAliasNode()
	 * @generated
	 */
	int ALIAS_NODE = 15;

	/**
	 * The number of structural features of the '<em>Alias Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALIAS_NODE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link model.impl.FeatureNodeImpl <em>Feature Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.FeatureNodeImpl
	 * @see model.impl.ModelPackageImpl#getFeatureNode()
	 * @generated
	 */
	int FEATURE_NODE = 16;

	/**
	 * The feature id for the '<em><b>Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_NODE__FEATURE = ALIAS_NODE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Feature Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_NODE_FEATURE_COUNT = ALIAS_NODE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link model.impl.OperatorNodeImpl <em>Operator Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.OperatorNodeImpl
	 * @see model.impl.ModelPackageImpl#getOperatorNode()
	 * @generated
	 */
	int OPERATOR_NODE = 17;

	/**
	 * The feature id for the '<em><b>Op</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATOR_NODE__OP = ALIAS_NODE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATOR_NODE__NODES = ALIAS_NODE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Operator Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATOR_NODE_FEATURE_COUNT = ALIAS_NODE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link model.impl.ModuleImpl <em>Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see model.impl.ModuleImpl
	 * @see model.impl.ModelPackageImpl#getModule()
	 * @generated
	 */
	int MODULE = 18;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Operators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__OPERATORS = 1;

	/**
	 * The number of structural features of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link model.Mapping <em>Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mapping</em>'.
	 * @see model.Mapping
	 * @generated
	 */
	EClass getMapping();

	/**
	 * Returns the meta object for the containment reference list '{@link model.Mapping#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Imports</em>'.
	 * @see model.Mapping#getImports()
	 * @see #getMapping()
	 * @generated
	 */
	EReference getMapping_Imports();

	/**
	 * Returns the meta object for the containment reference list '{@link model.Mapping#getTerminals <em>Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Terminals</em>'.
	 * @see model.Mapping#getTerminals()
	 * @see #getMapping()
	 * @generated
	 */
	EReference getMapping_Terminals();

	/**
	 * Returns the meta object for the reference list '{@link model.Mapping#getExternalTerminals <em>External Terminals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>External Terminals</em>'.
	 * @see model.Mapping#getExternalTerminals()
	 * @see #getMapping()
	 * @generated
	 */
	EReference getMapping_ExternalTerminals();

	/**
	 * Returns the meta object for the containment reference list '{@link model.Mapping#getOperators <em>Operators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Operators</em>'.
	 * @see model.Mapping#getOperators()
	 * @see #getMapping()
	 * @generated
	 */
	EReference getMapping_Operators();

	/**
	 * Returns the meta object for the attribute '{@link model.Mapping#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.Mapping#getName()
	 * @see #getMapping()
	 * @generated
	 */
	EAttribute getMapping_Name();

	/**
	 * Returns the meta object for the attribute '{@link model.Mapping#getPrefix <em>Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see model.Mapping#getPrefix()
	 * @see #getMapping()
	 * @generated
	 */
	EAttribute getMapping_Prefix();

	/**
	 * Returns the meta object for the containment reference list '{@link model.Mapping#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modules</em>'.
	 * @see model.Mapping#getModules()
	 * @see #getMapping()
	 * @generated
	 */
	EReference getMapping_Modules();

	/**
	 * Returns the meta object for class '{@link model.Terminal <em>Terminal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Terminal</em>'.
	 * @see model.Terminal
	 * @generated
	 */
	EClass getTerminal();

	/**
	 * Returns the meta object for the reference '{@link model.Terminal#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Class</em>'.
	 * @see model.Terminal#getClass_()
	 * @see #getTerminal()
	 * @generated
	 */
	EReference getTerminal_Class();

	/**
	 * Returns the meta object for the attribute '{@link model.Terminal#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.Terminal#getName()
	 * @see #getTerminal()
	 * @generated
	 */
	EAttribute getTerminal_Name();

	/**
	 * Returns the meta object for the attribute '{@link model.Terminal#isMany <em>Many</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Many</em>'.
	 * @see model.Terminal#isMany()
	 * @see #getTerminal()
	 * @generated
	 */
	EAttribute getTerminal_Many();

	/**
	 * Returns the meta object for class '{@link model.Operator <em>Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operator</em>'.
	 * @see model.Operator
	 * @generated
	 */
	EClass getOperator();

	/**
	 * Returns the meta object for the attribute '{@link model.Operator#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.Operator#getName()
	 * @see #getOperator()
	 * @generated
	 */
	EAttribute getOperator_Name();

	/**
	 * Returns the meta object for class '{@link model.ClassOperator <em>Class Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Class Operator</em>'.
	 * @see model.ClassOperator
	 * @generated
	 */
	EClass getClassOperator();

	/**
	 * Returns the meta object for the reference '{@link model.ClassOperator#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Class</em>'.
	 * @see model.ClassOperator#getClass_()
	 * @see #getClassOperator()
	 * @generated
	 */
	EReference getClassOperator_Class();

	/**
	 * Returns the meta object for the containment reference list '{@link model.ClassOperator#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see model.ClassOperator#getParameters()
	 * @see #getClassOperator()
	 * @generated
	 */
	EReference getClassOperator_Parameters();

	/**
	 * Returns the meta object for class '{@link model.UserOperator <em>User Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>User Operator</em>'.
	 * @see model.UserOperator
	 * @generated
	 */
	EClass getUserOperator();

	/**
	 * Returns the meta object for the reference '{@link model.UserOperator#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see model.UserOperator#getType()
	 * @see #getUserOperator()
	 * @generated
	 */
	EReference getUserOperator_Type();

	/**
	 * Returns the meta object for the containment reference list '{@link model.UserOperator#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see model.UserOperator#getParameters()
	 * @see #getUserOperator()
	 * @generated
	 */
	EReference getUserOperator_Parameters();

	/**
	 * Returns the meta object for the containment reference list '{@link model.UserOperator#getAccessors <em>Accessors</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Accessors</em>'.
	 * @see model.UserOperator#getAccessors()
	 * @see #getUserOperator()
	 * @generated
	 */
	EReference getUserOperator_Accessors();

	/**
	 * Returns the meta object for the attribute '{@link model.UserOperator#getMake <em>Make</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Make</em>'.
	 * @see model.UserOperator#getMake()
	 * @see #getUserOperator()
	 * @generated
	 */
	EAttribute getUserOperator_Make();

	/**
	 * Returns the meta object for the attribute '{@link model.UserOperator#getTest <em>Test</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Test</em>'.
	 * @see model.UserOperator#getTest()
	 * @see #getUserOperator()
	 * @generated
	 */
	EAttribute getUserOperator_Test();

	/**
	 * Returns the meta object for class '{@link model.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see model.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the reference '{@link model.Parameter#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see model.Parameter#getType()
	 * @see #getParameter()
	 * @generated
	 */
	EReference getParameter_Type();

	/**
	 * Returns the meta object for the attribute '{@link model.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for class '{@link model.Import <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Import</em>'.
	 * @see model.Import
	 * @generated
	 */
	EClass getImport();

	/**
	 * Returns the meta object for the attribute '{@link model.Import#getImportURI <em>Import URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Import URI</em>'.
	 * @see model.Import#getImportURI()
	 * @see #getImport()
	 * @generated
	 */
	EAttribute getImport_ImportURI();

	/**
	 * Returns the meta object for class '{@link model.Accessor <em>Accessor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Accessor</em>'.
	 * @see model.Accessor
	 * @generated
	 */
	EClass getAccessor();

	/**
	 * Returns the meta object for the reference '{@link model.Accessor#getSlot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Slot</em>'.
	 * @see model.Accessor#getSlot()
	 * @see #getAccessor()
	 * @generated
	 */
	EReference getAccessor_Slot();

	/**
	 * Returns the meta object for the attribute '{@link model.Accessor#getJava <em>Java</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Java</em>'.
	 * @see model.Accessor#getJava()
	 * @see #getAccessor()
	 * @generated
	 */
	EAttribute getAccessor_Java();

	/**
	 * Returns the meta object for class '{@link model.FeatureException <em>Feature Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Exception</em>'.
	 * @see model.FeatureException
	 * @generated
	 */
	EClass getFeatureException();

	/**
	 * Returns the meta object for class '{@link model.FeatureParameter <em>Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Parameter</em>'.
	 * @see model.FeatureParameter
	 * @generated
	 */
	EClass getFeatureParameter();

	/**
	 * Returns the meta object for the reference '{@link model.FeatureParameter#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Feature</em>'.
	 * @see model.FeatureParameter#getFeature()
	 * @see #getFeatureParameter()
	 * @generated
	 */
	EReference getFeatureParameter_Feature();

	/**
	 * Returns the meta object for class '{@link model.SettedFeatureParameter <em>Setted Feature Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Setted Feature Parameter</em>'.
	 * @see model.SettedFeatureParameter
	 * @generated
	 */
	EClass getSettedFeatureParameter();

	/**
	 * Returns the meta object for the containment reference '{@link model.SettedFeatureParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see model.SettedFeatureParameter#getValue()
	 * @see #getSettedFeatureParameter()
	 * @generated
	 */
	EReference getSettedFeatureParameter_Value();

	/**
	 * Returns the meta object for class '{@link model.SettedValue <em>Setted Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Setted Value</em>'.
	 * @see model.SettedValue
	 * @generated
	 */
	EClass getSettedValue();

	/**
	 * Returns the meta object for class '{@link model.JavaCodeValue <em>Java Code Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java Code Value</em>'.
	 * @see model.JavaCodeValue
	 * @generated
	 */
	EClass getJavaCodeValue();

	/**
	 * Returns the meta object for the attribute '{@link model.JavaCodeValue#getJava <em>Java</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Java</em>'.
	 * @see model.JavaCodeValue#getJava()
	 * @see #getJavaCodeValue()
	 * @generated
	 */
	EAttribute getJavaCodeValue_Java();

	/**
	 * Returns the meta object for class '{@link model.EnumLiteralValue <em>Enum Literal Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Literal Value</em>'.
	 * @see model.EnumLiteralValue
	 * @generated
	 */
	EClass getEnumLiteralValue();

	/**
	 * Returns the meta object for the reference '{@link model.EnumLiteralValue#getLiteral <em>Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Literal</em>'.
	 * @see model.EnumLiteralValue#getLiteral()
	 * @see #getEnumLiteralValue()
	 * @generated
	 */
	EReference getEnumLiteralValue_Literal();

	/**
	 * Returns the meta object for class '{@link model.AliasOperator <em>Alias Operator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alias Operator</em>'.
	 * @see model.AliasOperator
	 * @generated
	 */
	EClass getAliasOperator();

	/**
	 * Returns the meta object for class '{@link model.AliasNode <em>Alias Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alias Node</em>'.
	 * @see model.AliasNode
	 * @generated
	 */
	EClass getAliasNode();

	/**
	 * Returns the meta object for class '{@link model.FeatureNode <em>Feature Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Node</em>'.
	 * @see model.FeatureNode
	 * @generated
	 */
	EClass getFeatureNode();

	/**
	 * Returns the meta object for the attribute '{@link model.FeatureNode#getFeature <em>Feature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Feature</em>'.
	 * @see model.FeatureNode#getFeature()
	 * @see #getFeatureNode()
	 * @generated
	 */
	EAttribute getFeatureNode_Feature();

	/**
	 * Returns the meta object for class '{@link model.OperatorNode <em>Operator Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operator Node</em>'.
	 * @see model.OperatorNode
	 * @generated
	 */
	EClass getOperatorNode();

	/**
	 * Returns the meta object for the reference '{@link model.OperatorNode#getOp <em>Op</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Op</em>'.
	 * @see model.OperatorNode#getOp()
	 * @see #getOperatorNode()
	 * @generated
	 */
	EReference getOperatorNode_Op();

	/**
	 * Returns the meta object for the containment reference list '{@link model.OperatorNode#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see model.OperatorNode#getNodes()
	 * @see #getOperatorNode()
	 * @generated
	 */
	EReference getOperatorNode_Nodes();

	/**
	 * Returns the meta object for class '{@link model.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module</em>'.
	 * @see model.Module
	 * @generated
	 */
	EClass getModule();

	/**
	 * Returns the meta object for the attribute '{@link model.Module#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see model.Module#getName()
	 * @see #getModule()
	 * @generated
	 */
	EAttribute getModule_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link model.Module#getOperators <em>Operators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Operators</em>'.
	 * @see model.Module#getOperators()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Operators();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

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
		 * The meta object literal for the '{@link model.impl.MappingImpl <em>Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.MappingImpl
		 * @see model.impl.ModelPackageImpl#getMapping()
		 * @generated
		 */
		EClass MAPPING = eINSTANCE.getMapping();

		/**
		 * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING__IMPORTS = eINSTANCE.getMapping_Imports();

		/**
		 * The meta object literal for the '<em><b>Terminals</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING__TERMINALS = eINSTANCE.getMapping_Terminals();

		/**
		 * The meta object literal for the '<em><b>External Terminals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING__EXTERNAL_TERMINALS = eINSTANCE.getMapping_ExternalTerminals();

		/**
		 * The meta object literal for the '<em><b>Operators</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING__OPERATORS = eINSTANCE.getMapping_Operators();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING__NAME = eINSTANCE.getMapping_Name();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING__PREFIX = eINSTANCE.getMapping_Prefix();

		/**
		 * The meta object literal for the '<em><b>Modules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING__MODULES = eINSTANCE.getMapping_Modules();

		/**
		 * The meta object literal for the '{@link model.impl.TerminalImpl <em>Terminal</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.TerminalImpl
		 * @see model.impl.ModelPackageImpl#getTerminal()
		 * @generated
		 */
		EClass TERMINAL = eINSTANCE.getTerminal();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TERMINAL__CLASS = eINSTANCE.getTerminal_Class();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TERMINAL__NAME = eINSTANCE.getTerminal_Name();

		/**
		 * The meta object literal for the '<em><b>Many</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TERMINAL__MANY = eINSTANCE.getTerminal_Many();

		/**
		 * The meta object literal for the '{@link model.Operator <em>Operator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.Operator
		 * @see model.impl.ModelPackageImpl#getOperator()
		 * @generated
		 */
		EClass OPERATOR = eINSTANCE.getOperator();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATOR__NAME = eINSTANCE.getOperator_Name();

		/**
		 * The meta object literal for the '{@link model.impl.ClassOperatorImpl <em>Class Operator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.ClassOperatorImpl
		 * @see model.impl.ModelPackageImpl#getClassOperator()
		 * @generated
		 */
		EClass CLASS_OPERATOR = eINSTANCE.getClassOperator();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLASS_OPERATOR__CLASS = eINSTANCE.getClassOperator_Class();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLASS_OPERATOR__PARAMETERS = eINSTANCE.getClassOperator_Parameters();

		/**
		 * The meta object literal for the '{@link model.impl.UserOperatorImpl <em>User Operator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.UserOperatorImpl
		 * @see model.impl.ModelPackageImpl#getUserOperator()
		 * @generated
		 */
		EClass USER_OPERATOR = eINSTANCE.getUserOperator();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER_OPERATOR__TYPE = eINSTANCE.getUserOperator_Type();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER_OPERATOR__PARAMETERS = eINSTANCE.getUserOperator_Parameters();

		/**
		 * The meta object literal for the '<em><b>Accessors</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference USER_OPERATOR__ACCESSORS = eINSTANCE.getUserOperator_Accessors();

		/**
		 * The meta object literal for the '<em><b>Make</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_OPERATOR__MAKE = eINSTANCE.getUserOperator_Make();

		/**
		 * The meta object literal for the '<em><b>Test</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USER_OPERATOR__TEST = eINSTANCE.getUserOperator_Test();

		/**
		 * The meta object literal for the '{@link model.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.ParameterImpl
		 * @see model.impl.ModelPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER__TYPE = eINSTANCE.getParameter_Type();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

		/**
		 * The meta object literal for the '{@link model.impl.ImportImpl <em>Import</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.ImportImpl
		 * @see model.impl.ModelPackageImpl#getImport()
		 * @generated
		 */
		EClass IMPORT = eINSTANCE.getImport();

		/**
		 * The meta object literal for the '<em><b>Import URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMPORT__IMPORT_URI = eINSTANCE.getImport_ImportURI();

		/**
		 * The meta object literal for the '{@link model.impl.AccessorImpl <em>Accessor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.AccessorImpl
		 * @see model.impl.ModelPackageImpl#getAccessor()
		 * @generated
		 */
		EClass ACCESSOR = eINSTANCE.getAccessor();

		/**
		 * The meta object literal for the '<em><b>Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ACCESSOR__SLOT = eINSTANCE.getAccessor_Slot();

		/**
		 * The meta object literal for the '<em><b>Java</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ACCESSOR__JAVA = eINSTANCE.getAccessor_Java();

		/**
		 * The meta object literal for the '{@link model.impl.FeatureExceptionImpl <em>Feature Exception</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.FeatureExceptionImpl
		 * @see model.impl.ModelPackageImpl#getFeatureException()
		 * @generated
		 */
		EClass FEATURE_EXCEPTION = eINSTANCE.getFeatureException();

		/**
		 * The meta object literal for the '{@link model.impl.FeatureParameterImpl <em>Feature Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.FeatureParameterImpl
		 * @see model.impl.ModelPackageImpl#getFeatureParameter()
		 * @generated
		 */
		EClass FEATURE_PARAMETER = eINSTANCE.getFeatureParameter();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_PARAMETER__FEATURE = eINSTANCE.getFeatureParameter_Feature();

		/**
		 * The meta object literal for the '{@link model.impl.SettedFeatureParameterImpl <em>Setted Feature Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.SettedFeatureParameterImpl
		 * @see model.impl.ModelPackageImpl#getSettedFeatureParameter()
		 * @generated
		 */
		EClass SETTED_FEATURE_PARAMETER = eINSTANCE.getSettedFeatureParameter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SETTED_FEATURE_PARAMETER__VALUE = eINSTANCE.getSettedFeatureParameter_Value();

		/**
		 * The meta object literal for the '{@link model.impl.SettedValueImpl <em>Setted Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.SettedValueImpl
		 * @see model.impl.ModelPackageImpl#getSettedValue()
		 * @generated
		 */
		EClass SETTED_VALUE = eINSTANCE.getSettedValue();

		/**
		 * The meta object literal for the '{@link model.impl.JavaCodeValueImpl <em>Java Code Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.JavaCodeValueImpl
		 * @see model.impl.ModelPackageImpl#getJavaCodeValue()
		 * @generated
		 */
		EClass JAVA_CODE_VALUE = eINSTANCE.getJavaCodeValue();

		/**
		 * The meta object literal for the '<em><b>Java</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JAVA_CODE_VALUE__JAVA = eINSTANCE.getJavaCodeValue_Java();

		/**
		 * The meta object literal for the '{@link model.impl.EnumLiteralValueImpl <em>Enum Literal Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.EnumLiteralValueImpl
		 * @see model.impl.ModelPackageImpl#getEnumLiteralValue()
		 * @generated
		 */
		EClass ENUM_LITERAL_VALUE = eINSTANCE.getEnumLiteralValue();

		/**
		 * The meta object literal for the '<em><b>Literal</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_LITERAL_VALUE__LITERAL = eINSTANCE.getEnumLiteralValue_Literal();

		/**
		 * The meta object literal for the '{@link model.impl.AliasOperatorImpl <em>Alias Operator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.AliasOperatorImpl
		 * @see model.impl.ModelPackageImpl#getAliasOperator()
		 * @generated
		 */
		EClass ALIAS_OPERATOR = eINSTANCE.getAliasOperator();

		/**
		 * The meta object literal for the '{@link model.impl.AliasNodeImpl <em>Alias Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.AliasNodeImpl
		 * @see model.impl.ModelPackageImpl#getAliasNode()
		 * @generated
		 */
		EClass ALIAS_NODE = eINSTANCE.getAliasNode();

		/**
		 * The meta object literal for the '{@link model.impl.FeatureNodeImpl <em>Feature Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.FeatureNodeImpl
		 * @see model.impl.ModelPackageImpl#getFeatureNode()
		 * @generated
		 */
		EClass FEATURE_NODE = eINSTANCE.getFeatureNode();

		/**
		 * The meta object literal for the '<em><b>Feature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_NODE__FEATURE = eINSTANCE.getFeatureNode_Feature();

		/**
		 * The meta object literal for the '{@link model.impl.OperatorNodeImpl <em>Operator Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.OperatorNodeImpl
		 * @see model.impl.ModelPackageImpl#getOperatorNode()
		 * @generated
		 */
		EClass OPERATOR_NODE = eINSTANCE.getOperatorNode();

		/**
		 * The meta object literal for the '<em><b>Op</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATOR_NODE__OP = eINSTANCE.getOperatorNode_Op();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATOR_NODE__NODES = eINSTANCE.getOperatorNode_Nodes();

		/**
		 * The meta object literal for the '{@link model.impl.ModuleImpl <em>Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see model.impl.ModuleImpl
		 * @see model.impl.ModelPackageImpl#getModule()
		 * @generated
		 */
		EClass MODULE = eINSTANCE.getModule();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODULE__NAME = eINSTANCE.getModule_Name();

		/**
		 * The meta object literal for the '<em><b>Operators</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__OPERATORS = eINSTANCE.getModule_Operators();

	}

} //ModelPackage
