/**
 */
package model.util;

import java.util.List;


import model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see model.ModelPackage
 * @generated
 */
public class ModelSwitch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelSwitch() {
		if (modelPackage == null) {
			modelPackage = ModelPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<?> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ModelPackage.MAPPING: {
				Mapping mapping = (Mapping)theEObject;
				Object result = caseMapping(mapping);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.TERMINAL: {
				Terminal terminal = (Terminal)theEObject;
				Object result = caseTerminal(terminal);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.OPERATOR: {
				Operator operator = (Operator)theEObject;
				Object result = caseOperator(operator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.CLASS_OPERATOR: {
				ClassOperator classOperator = (ClassOperator)theEObject;
				Object result = caseClassOperator(classOperator);
				if (result == null) result = caseOperator(classOperator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.USER_OPERATOR: {
				UserOperator userOperator = (UserOperator)theEObject;
				Object result = caseUserOperator(userOperator);
				if (result == null) result = caseOperator(userOperator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.PARAMETER: {
				Parameter parameter = (Parameter)theEObject;
				Object result = caseParameter(parameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.IMPORT: {
				Import import_ = (Import)theEObject;
				Object result = caseImport(import_);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.ACCESSOR: {
				Accessor accessor = (Accessor)theEObject;
				Object result = caseAccessor(accessor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.FEATURE_EXCEPTION: {
				FeatureException featureException = (FeatureException)theEObject;
				Object result = caseFeatureException(featureException);
				if (result == null) result = caseFeatureParameter(featureException);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.FEATURE_PARAMETER: {
				FeatureParameter featureParameter = (FeatureParameter)theEObject;
				Object result = caseFeatureParameter(featureParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.SETTED_FEATURE_PARAMETER: {
				SettedFeatureParameter settedFeatureParameter = (SettedFeatureParameter)theEObject;
				Object result = caseSettedFeatureParameter(settedFeatureParameter);
				if (result == null) result = caseFeatureParameter(settedFeatureParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.SETTED_VALUE: {
				SettedValue settedValue = (SettedValue)theEObject;
				Object result = caseSettedValue(settedValue);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.JAVA_CODE_VALUE: {
				JavaCodeValue javaCodeValue = (JavaCodeValue)theEObject;
				Object result = caseJavaCodeValue(javaCodeValue);
				if (result == null) result = caseSettedValue(javaCodeValue);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.ENUM_LITERAL_VALUE: {
				EnumLiteralValue enumLiteralValue = (EnumLiteralValue)theEObject;
				Object result = caseEnumLiteralValue(enumLiteralValue);
				if (result == null) result = caseSettedValue(enumLiteralValue);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.ALIAS_OPERATOR: {
				AliasOperator aliasOperator = (AliasOperator)theEObject;
				Object result = caseAliasOperator(aliasOperator);
				if (result == null) result = caseOperator(aliasOperator);
				if (result == null) result = caseOperatorNode(aliasOperator);
				if (result == null) result = caseAliasNode(aliasOperator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.ALIAS_NODE: {
				AliasNode aliasNode = (AliasNode)theEObject;
				Object result = caseAliasNode(aliasNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.FEATURE_NODE: {
				FeatureNode featureNode = (FeatureNode)theEObject;
				Object result = caseFeatureNode(featureNode);
				if (result == null) result = caseAliasNode(featureNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.OPERATOR_NODE: {
				OperatorNode operatorNode = (OperatorNode)theEObject;
				Object result = caseOperatorNode(operatorNode);
				if (result == null) result = caseAliasNode(operatorNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ModelPackage.MODULE: {
				Module module = (Module)theEObject;
				Object result = caseModule(module);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseMapping(Mapping object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Terminal</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Terminal</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseTerminal(Terminal object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOperator(Operator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Class Operator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Class Operator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseClassOperator(ClassOperator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>User Operator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>User Operator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUserOperator(UserOperator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseParameter(Parameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Import</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseImport(Import object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Accessor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Accessor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAccessor(Accessor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Exception</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Exception</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureException(FeatureException object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureParameter(FeatureParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Setted Feature Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Setted Feature Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSettedFeatureParameter(SettedFeatureParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Setted Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Setted Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseSettedValue(SettedValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Java Code Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Java Code Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseJavaCodeValue(JavaCodeValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Literal Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Literal Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseEnumLiteralValue(EnumLiteralValue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alias Operator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alias Operator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAliasOperator(AliasOperator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Alias Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Alias Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAliasNode(AliasNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFeatureNode(FeatureNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operator Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operator Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseOperatorNode(OperatorNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseModule(Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //ModelSwitch
