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

import tom.tomDsl.JavaBody;
import tom.tomDsl.OperationArray;
import tom.tomDsl.TomDslPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Array</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tom.tomDsl.impl.OperationArrayImpl#getSize <em>Size</em>}</li>
 *   <li>{@link tom.tomDsl.impl.OperationArrayImpl#getElement <em>Element</em>}</li>
 *   <li>{@link tom.tomDsl.impl.OperationArrayImpl#getEmpty <em>Empty</em>}</li>
 *   <li>{@link tom.tomDsl.impl.OperationArrayImpl#getAppend <em>Append</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationArrayImpl extends ArrayOperationImpl implements OperationArray
{
  /**
   * The cached value of the '{@link #getSize() <em>Size</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSize()
   * @generated
   * @ordered
   */
  protected JavaBody size;

  /**
   * The cached value of the '{@link #getElement() <em>Element</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected JavaBody element;

  /**
   * The cached value of the '{@link #getEmpty() <em>Empty</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEmpty()
   * @generated
   * @ordered
   */
  protected JavaBody empty;

  /**
   * The cached value of the '{@link #getAppend() <em>Append</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAppend()
   * @generated
   * @ordered
   */
  protected JavaBody append;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperationArrayImpl()
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
    return TomDslPackage.Literals.OPERATION_ARRAY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getSize()
  {
    return size;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSize(JavaBody newSize, NotificationChain msgs)
  {
    JavaBody oldSize = size;
    size = newSize;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__SIZE, oldSize, newSize);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSize(JavaBody newSize)
  {
    if (newSize != size)
    {
      NotificationChain msgs = null;
      if (size != null)
        msgs = ((InternalEObject)size).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__SIZE, null, msgs);
      if (newSize != null)
        msgs = ((InternalEObject)newSize).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__SIZE, null, msgs);
      msgs = basicSetSize(newSize, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__SIZE, newSize, newSize));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getElement()
  {
    return element;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetElement(JavaBody newElement, NotificationChain msgs)
  {
    JavaBody oldElement = element;
    element = newElement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__ELEMENT, oldElement, newElement);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setElement(JavaBody newElement)
  {
    if (newElement != element)
    {
      NotificationChain msgs = null;
      if (element != null)
        msgs = ((InternalEObject)element).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__ELEMENT, null, msgs);
      if (newElement != null)
        msgs = ((InternalEObject)newElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__ELEMENT, null, msgs);
      msgs = basicSetElement(newElement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__ELEMENT, newElement, newElement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getEmpty()
  {
    return empty;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEmpty(JavaBody newEmpty, NotificationChain msgs)
  {
    JavaBody oldEmpty = empty;
    empty = newEmpty;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__EMPTY, oldEmpty, newEmpty);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEmpty(JavaBody newEmpty)
  {
    if (newEmpty != empty)
    {
      NotificationChain msgs = null;
      if (empty != null)
        msgs = ((InternalEObject)empty).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__EMPTY, null, msgs);
      if (newEmpty != null)
        msgs = ((InternalEObject)newEmpty).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__EMPTY, null, msgs);
      msgs = basicSetEmpty(newEmpty, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__EMPTY, newEmpty, newEmpty));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getAppend()
  {
    return append;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAppend(JavaBody newAppend, NotificationChain msgs)
  {
    JavaBody oldAppend = append;
    append = newAppend;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__APPEND, oldAppend, newAppend);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAppend(JavaBody newAppend)
  {
    if (newAppend != append)
    {
      NotificationChain msgs = null;
      if (append != null)
        msgs = ((InternalEObject)append).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__APPEND, null, msgs);
      if (newAppend != null)
        msgs = ((InternalEObject)newAppend).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.OPERATION_ARRAY__APPEND, null, msgs);
      msgs = basicSetAppend(newAppend, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.OPERATION_ARRAY__APPEND, newAppend, newAppend));
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
      case TomDslPackage.OPERATION_ARRAY__SIZE:
        return basicSetSize(null, msgs);
      case TomDslPackage.OPERATION_ARRAY__ELEMENT:
        return basicSetElement(null, msgs);
      case TomDslPackage.OPERATION_ARRAY__EMPTY:
        return basicSetEmpty(null, msgs);
      case TomDslPackage.OPERATION_ARRAY__APPEND:
        return basicSetAppend(null, msgs);
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
      case TomDslPackage.OPERATION_ARRAY__SIZE:
        return getSize();
      case TomDslPackage.OPERATION_ARRAY__ELEMENT:
        return getElement();
      case TomDslPackage.OPERATION_ARRAY__EMPTY:
        return getEmpty();
      case TomDslPackage.OPERATION_ARRAY__APPEND:
        return getAppend();
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
      case TomDslPackage.OPERATION_ARRAY__SIZE:
        setSize((JavaBody)newValue);
        return;
      case TomDslPackage.OPERATION_ARRAY__ELEMENT:
        setElement((JavaBody)newValue);
        return;
      case TomDslPackage.OPERATION_ARRAY__EMPTY:
        setEmpty((JavaBody)newValue);
        return;
      case TomDslPackage.OPERATION_ARRAY__APPEND:
        setAppend((JavaBody)newValue);
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
      case TomDslPackage.OPERATION_ARRAY__SIZE:
        setSize((JavaBody)null);
        return;
      case TomDslPackage.OPERATION_ARRAY__ELEMENT:
        setElement((JavaBody)null);
        return;
      case TomDslPackage.OPERATION_ARRAY__EMPTY:
        setEmpty((JavaBody)null);
        return;
      case TomDslPackage.OPERATION_ARRAY__APPEND:
        setAppend((JavaBody)null);
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
      case TomDslPackage.OPERATION_ARRAY__SIZE:
        return size != null;
      case TomDslPackage.OPERATION_ARRAY__ELEMENT:
        return element != null;
      case TomDslPackage.OPERATION_ARRAY__EMPTY:
        return empty != null;
      case TomDslPackage.OPERATION_ARRAY__APPEND:
        return append != null;
    }
    return super.eIsSet(featureID);
  }

} //OperationArrayImpl
