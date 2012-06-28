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

import tom.tomDsl.JavaBody;
import tom.tomDsl.TomDslPackage;
import tom.tomDsl.TypeTerm;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Term</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tom.tomDsl.impl.TypeTermImpl#getName <em>Name</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TypeTermImpl#getImplement <em>Implement</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TypeTermImpl#getSort <em>Sort</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TypeTermImpl#getEquals <em>Equals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TypeTermImpl extends MinimalEObjectImpl.Container implements TypeTerm
{
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
   * The cached value of the '{@link #getImplement() <em>Implement</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getImplement()
   * @generated
   * @ordered
   */
  protected JavaBody implement;

  /**
   * The cached value of the '{@link #getSort() <em>Sort</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSort()
   * @generated
   * @ordered
   */
  protected JavaBody sort;

  /**
   * The cached value of the '{@link #getEquals() <em>Equals</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEquals()
   * @generated
   * @ordered
   */
  protected JavaBody equals;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TypeTermImpl()
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
    return TomDslPackage.Literals.TYPE_TERM;
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
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getImplement()
  {
    return implement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetImplement(JavaBody newImplement, NotificationChain msgs)
  {
    JavaBody oldImplement = implement;
    implement = newImplement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__IMPLEMENT, oldImplement, newImplement);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setImplement(JavaBody newImplement)
  {
    if (newImplement != implement)
    {
      NotificationChain msgs = null;
      if (implement != null)
        msgs = ((InternalEObject)implement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__IMPLEMENT, null, msgs);
      if (newImplement != null)
        msgs = ((InternalEObject)newImplement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__IMPLEMENT, null, msgs);
      msgs = basicSetImplement(newImplement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__IMPLEMENT, newImplement, newImplement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getSort()
  {
    return sort;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSort(JavaBody newSort, NotificationChain msgs)
  {
    JavaBody oldSort = sort;
    sort = newSort;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__SORT, oldSort, newSort);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSort(JavaBody newSort)
  {
    if (newSort != sort)
    {
      NotificationChain msgs = null;
      if (sort != null)
        msgs = ((InternalEObject)sort).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__SORT, null, msgs);
      if (newSort != null)
        msgs = ((InternalEObject)newSort).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__SORT, null, msgs);
      msgs = basicSetSort(newSort, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__SORT, newSort, newSort));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody getEquals()
  {
    return equals;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEquals(JavaBody newEquals, NotificationChain msgs)
  {
    JavaBody oldEquals = equals;
    equals = newEquals;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__EQUALS, oldEquals, newEquals);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEquals(JavaBody newEquals)
  {
    if (newEquals != equals)
    {
      NotificationChain msgs = null;
      if (equals != null)
        msgs = ((InternalEObject)equals).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__EQUALS, null, msgs);
      if (newEquals != null)
        msgs = ((InternalEObject)newEquals).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TomDslPackage.TYPE_TERM__EQUALS, null, msgs);
      msgs = basicSetEquals(newEquals, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.TYPE_TERM__EQUALS, newEquals, newEquals));
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
      case TomDslPackage.TYPE_TERM__IMPLEMENT:
        return basicSetImplement(null, msgs);
      case TomDslPackage.TYPE_TERM__SORT:
        return basicSetSort(null, msgs);
      case TomDslPackage.TYPE_TERM__EQUALS:
        return basicSetEquals(null, msgs);
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
      case TomDslPackage.TYPE_TERM__NAME:
        return getName();
      case TomDslPackage.TYPE_TERM__IMPLEMENT:
        return getImplement();
      case TomDslPackage.TYPE_TERM__SORT:
        return getSort();
      case TomDslPackage.TYPE_TERM__EQUALS:
        return getEquals();
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
      case TomDslPackage.TYPE_TERM__NAME:
        setName((String)newValue);
        return;
      case TomDslPackage.TYPE_TERM__IMPLEMENT:
        setImplement((JavaBody)newValue);
        return;
      case TomDslPackage.TYPE_TERM__SORT:
        setSort((JavaBody)newValue);
        return;
      case TomDslPackage.TYPE_TERM__EQUALS:
        setEquals((JavaBody)newValue);
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
      case TomDslPackage.TYPE_TERM__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TomDslPackage.TYPE_TERM__IMPLEMENT:
        setImplement((JavaBody)null);
        return;
      case TomDslPackage.TYPE_TERM__SORT:
        setSort((JavaBody)null);
        return;
      case TomDslPackage.TYPE_TERM__EQUALS:
        setEquals((JavaBody)null);
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
      case TomDslPackage.TYPE_TERM__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TomDslPackage.TYPE_TERM__IMPLEMENT:
        return implement != null;
      case TomDslPackage.TYPE_TERM__SORT:
        return sort != null;
      case TomDslPackage.TYPE_TERM__EQUALS:
        return equals != null;
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
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //TypeTermImpl
