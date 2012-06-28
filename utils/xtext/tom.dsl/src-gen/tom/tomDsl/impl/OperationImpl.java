/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tom.tomDsl.ARG;
import tom.tomDsl.JavaBody;
import tom.tomDsl.Operation;
import tom.tomDsl.TomDslPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tom.tomDsl.impl.OperationImpl#getArg <em>Arg</em>}</li>
 *   <li>{@link tom.tomDsl.impl.OperationImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link tom.tomDsl.impl.OperationImpl#getMake <em>Make</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationImpl extends ArrayOperationImpl implements Operation
{
  /**
   * The cached value of the '{@link #getArg() <em>Arg</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArg()
   * @generated
   * @ordered
   */
  protected EList<ARG> arg;

  /**
   * The cached value of the '{@link #getSlot() <em>Slot</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSlot()
   * @generated
   * @ordered
   */
  protected EList<JavaBody> slot;

  /**
   * The cached value of the '{@link #getMake() <em>Make</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMake()
   * @generated
   * @ordered
   */
  protected JavaBody make;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperationImpl()
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
    return TomDslPackage.Literals.OPERATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ARG> getArg()
  {
    if (arg == null)
    {
      arg = new EObjectContainmentEList<ARG>(ARG.class, this, TomDslPackage.OPERATION__ARG);
    }
    return arg;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<JavaBody> getSlot()
  {
    if (slot == null)
    {
      slot = new EObjectContainmentEList<JavaBody>(JavaBody.class, this, TomDslPackage.OPERATION__SLOT);
    }
    return slot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getMake()
  {
    return make;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMake(JavaBody newMake, NotificationChain msgs)
  {
    JavaBody oldMake = make;
    make = newMake;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION__MAKE, oldMake, newMake);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMake(JavaBody newMake)
  {
    if (newMake != make)
    {
      NotificationChain msgs = null;
      if (make != null)
        msgs = ((InternalEObject)make).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION__MAKE, null, msgs);
      if (newMake != null)
        msgs = ((InternalEObject)newMake).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION__MAKE, null, msgs);
      msgs = basicSetMake(newMake, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION__MAKE, newMake, newMake));
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
      case TomDslPackage.OPERATION__ARG:
        return ((InternalEList<?>)getArg()).basicRemove(otherEnd, msgs);
      case TomDslPackage.OPERATION__SLOT:
        return ((InternalEList<?>)getSlot()).basicRemove(otherEnd, msgs);
      case TomDslPackage.OPERATION__MAKE:
        return basicSetMake(null, msgs);
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
      case TomDslPackage.OPERATION__ARG:
        return getArg();
      case TomDslPackage.OPERATION__SLOT:
        return getSlot();
      case TomDslPackage.OPERATION__MAKE:
        return getMake();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TomDslPackage.OPERATION__ARG:
        getArg().clear();
        getArg().addAll((Collection<? extends ARG>)newValue);
        return;
      case TomDslPackage.OPERATION__SLOT:
        getSlot().clear();
        getSlot().addAll((Collection<? extends JavaBody>)newValue);
        return;
      case TomDslPackage.OPERATION__MAKE:
        setMake((JavaBody)newValue);
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
      case TomDslPackage.OPERATION__ARG:
        getArg().clear();
        return;
      case TomDslPackage.OPERATION__SLOT:
        getSlot().clear();
        return;
      case TomDslPackage.OPERATION__MAKE:
        setMake((JavaBody)null);
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
      case TomDslPackage.OPERATION__ARG:
        return arg != null && !arg.isEmpty();
      case TomDslPackage.OPERATION__SLOT:
        return slot != null && !slot.isEmpty();
      case TomDslPackage.OPERATION__MAKE:
        return make != null;
    }
    return super.eIsSet(featureID);
  }

} //OperationImpl
