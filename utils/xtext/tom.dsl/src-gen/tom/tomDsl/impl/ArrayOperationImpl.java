/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import tom.tomDsl.ArrayOperation;
import tom.tomDsl.JavaBody;
import tom.tomDsl.TomDslPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Array Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tom.tomDsl.impl.ArrayOperationImpl#getTerm <em>Term</em>}</li>
 *   <li>{@link tom.tomDsl.impl.ArrayOperationImpl#getName <em>Name</em>}</li>
 *   <li>{@link tom.tomDsl.impl.ArrayOperationImpl#getFsym <em>Fsym</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArrayOperationImpl extends MinimalEObjectImpl.Container implements ArrayOperation
{
  /**
   * The default value of the '{@link #getTerm() <em>Term</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTerm()
   * @generated
   * @ordered
   */
  protected static final String TERM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTerm() <em>Term</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTerm()
   * @generated
   * @ordered
   */
  protected String term = TERM_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getFsym() <em>Fsym</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFsym()
   * @generated
   * @ordered
   */
  protected JavaBody fsym;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArrayOperationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TomDslPackage.Literals.ARRAY_OPERATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTerm()
  {
    return term;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTerm(String newTerm)
  {
    String oldTerm = term;
    term = newTerm;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.ARRAY_OPERATION__TERM, oldTerm, term));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.ARRAY_OPERATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getFsym()
  {
    return fsym;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFsym(JavaBody newFsym, NotificationChain msgs)
  {
    JavaBody oldFsym = fsym;
    fsym = newFsym;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.ARRAY_OPERATION__FSYM, oldFsym, newFsym);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFsym(JavaBody newFsym)
  {
    if (newFsym != fsym)
    {
      NotificationChain msgs = null;
      if (fsym != null)
        msgs = ((InternalEObject)fsym).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.ARRAY_OPERATION__FSYM, null, msgs);
      if (newFsym != null)
        msgs = ((InternalEObject)newFsym).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.ARRAY_OPERATION__FSYM, null, msgs);
      msgs = basicSetFsym(newFsym, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.ARRAY_OPERATION__FSYM, newFsym, newFsym));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case TomDslPackage.ARRAY_OPERATION__FSYM:
        return basicSetFsym(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case TomDslPackage.ARRAY_OPERATION__TERM:
        return getTerm();
      case TomDslPackage.ARRAY_OPERATION__NAME:
        return getName();
      case TomDslPackage.ARRAY_OPERATION__FSYM:
        return getFsym();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TomDslPackage.ARRAY_OPERATION__TERM:
        setTerm((String)newValue);
        return;
      case TomDslPackage.ARRAY_OPERATION__NAME:
        setName((String)newValue);
        return;
      case TomDslPackage.ARRAY_OPERATION__FSYM:
        setFsym((JavaBody)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case TomDslPackage.ARRAY_OPERATION__TERM:
        setTerm(TERM_EDEFAULT);
        return;
      case TomDslPackage.ARRAY_OPERATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TomDslPackage.ARRAY_OPERATION__FSYM:
        setFsym((JavaBody)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case TomDslPackage.ARRAY_OPERATION__TERM:
        return TERM_EDEFAULT == null ? term != null : !TERM_EDEFAULT.equals(term);
      case TomDslPackage.ARRAY_OPERATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TomDslPackage.ARRAY_OPERATION__FSYM:
        return fsym != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (term: ");
    result.append(term);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //ArrayOperationImpl
