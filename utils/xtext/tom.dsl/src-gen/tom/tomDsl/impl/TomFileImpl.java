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
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import tom.tomDsl.ArrayOperation;
import tom.tomDsl.Include;
import tom.tomDsl.TomDslPackage;
import tom.tomDsl.TomFile;
import tom.tomDsl.TypeTerm;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tom File</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tom.tomDsl.impl.TomFileImpl#getOps <em>Ops</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TomFileImpl#getTerms <em>Terms</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TomFileImpl#getInc <em>Inc</em>}</li>
 *   <li>{@link tom.tomDsl.impl.TomFileImpl#getLocals <em>Locals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TomFileImpl extends MinimalEObjectImpl.Container implements TomFile
{
  /**
   * The cached value of the '{@link #getOps() <em>Ops</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOps()
   * @generated
   * @ordered
   */
  protected EList<ArrayOperation> ops;

  /**
   * The cached value of the '{@link #getTerms() <em>Terms</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTerms()
   * @generated
   * @ordered
   */
  protected EList<TypeTerm> terms;

  /**
   * The cached value of the '{@link #getInc() <em>Inc</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInc()
   * @generated
   * @ordered
   */
  protected EList<Include> inc;

  /**
   * The default value of the '{@link #getLocals() <em>Locals</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocals()
   * @generated
   * @ordered
   */
  protected static final String LOCALS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocals() <em>Locals</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocals()
   * @generated
   * @ordered
   */
  protected String locals = LOCALS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TomFileImpl()
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
    return TomDslPackage.Literals.TOM_FILE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ArrayOperation> getOps()
  {
    if (ops == null)
    {
      ops = new EObjectContainmentEList<ArrayOperation>(ArrayOperation.class, this, TomDslPackage.TOM_FILE__OPS);
    }
    return ops;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<TypeTerm> getTerms()
  {
    if (terms == null)
    {
      terms = new EObjectContainmentEList<TypeTerm>(TypeTerm.class, this, TomDslPackage.TOM_FILE__TERMS);
    }
    return terms;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Include> getInc()
  {
    if (inc == null)
    {
      inc = new EObjectContainmentEList<Include>(Include.class, this, TomDslPackage.TOM_FILE__INC);
    }
    return inc;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocals()
  {
    return locals;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocals(String newLocals)
  {
    String oldLocals = locals;
    locals = newLocals;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, TomDslPackage.TOM_FILE__LOCALS, oldLocals, locals));
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
      case TomDslPackage.TOM_FILE__OPS:
        return ((InternalEList<?>)getOps()).basicRemove(otherEnd, msgs);
      case TomDslPackage.TOM_FILE__TERMS:
        return ((InternalEList<?>)getTerms()).basicRemove(otherEnd, msgs);
      case TomDslPackage.TOM_FILE__INC:
        return ((InternalEList<?>)getInc()).basicRemove(otherEnd, msgs);
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
      case TomDslPackage.TOM_FILE__OPS:
        return getOps();
      case TomDslPackage.TOM_FILE__TERMS:
        return getTerms();
      case TomDslPackage.TOM_FILE__INC:
        return getInc();
      case TomDslPackage.TOM_FILE__LOCALS:
        return getLocals();
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
      case TomDslPackage.TOM_FILE__OPS:
        getOps().clear();
        getOps().addAll((Collection<? extends ArrayOperation>)newValue);
        return;
      case TomDslPackage.TOM_FILE__TERMS:
        getTerms().clear();
        getTerms().addAll((Collection<? extends TypeTerm>)newValue);
        return;
      case TomDslPackage.TOM_FILE__INC:
        getInc().clear();
        getInc().addAll((Collection<? extends Include>)newValue);
        return;
      case TomDslPackage.TOM_FILE__LOCALS:
        setLocals((String)newValue);
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
      case TomDslPackage.TOM_FILE__OPS:
        getOps().clear();
        return;
      case TomDslPackage.TOM_FILE__TERMS:
        getTerms().clear();
        return;
      case TomDslPackage.TOM_FILE__INC:
        getInc().clear();
        return;
      case TomDslPackage.TOM_FILE__LOCALS:
        setLocals(LOCALS_EDEFAULT);
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
      case TomDslPackage.TOM_FILE__OPS:
        return ops != null && !ops.isEmpty();
      case TomDslPackage.TOM_FILE__TERMS:
        return terms != null && !terms.isEmpty();
      case TomDslPackage.TOM_FILE__INC:
        return inc != null && !inc.isEmpty();
      case TomDslPackage.TOM_FILE__LOCALS:
        return LOCALS_EDEFAULT == null ? locals != null : !LOCALS_EDEFAULT.equals(locals);
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
    result.append(" (locals: ");
    result.append(locals);
    result.append(')');
    return result.toString();
  }

} //TomFileImpl
