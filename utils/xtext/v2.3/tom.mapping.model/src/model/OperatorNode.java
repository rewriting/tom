/**
 */
package model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operator Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link model.OperatorNode#getOp <em>Op</em>}</li>
 *   <li>{@link model.OperatorNode#getNodes <em>Nodes</em>}</li>
 * </ul>
 * </p>
 *
 * @see model.ModelPackage#getOperatorNode()
 * @model
 * @generated
 */
public interface OperatorNode extends AliasNode {
	/**
	 * Returns the value of the '<em><b>Op</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Op</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Op</em>' reference.
	 * @see #setOp(Operator)
	 * @see model.ModelPackage#getOperatorNode_Op()
	 * @model
	 * @generated
	 */
	Operator getOp();

	/**
	 * Sets the value of the '{@link model.OperatorNode#getOp <em>Op</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Op</em>' reference.
	 * @see #getOp()
	 * @generated
	 */
	void setOp(Operator value);

	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link model.AliasNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' containment reference list.
	 * @see model.ModelPackage#getOperatorNode_Nodes()
	 * @model type="model.AliasNode" containment="true"
	 * @generated
	 */
	EList<AliasNode> getNodes();

} // OperatorNode
