/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import tom.tomDsl.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see tom.tomDsl.TomDslPackage
 * @generated
 */
public class TomDslAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static TomDslPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TomDslAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = TomDslPackage.eINSTANCE;
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
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
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
  protected TomDslSwitch<Adapter> modelSwitch =
    new TomDslSwitch<Adapter>()
    {
      @Override
      public Adapter caseTomFile(TomFile object)
      {
        return createTomFileAdapter();
      }
      @Override
      public Adapter caseArrayOperation(ArrayOperation object)
      {
        return createArrayOperationAdapter();
      }
      @Override
      public Adapter caseInclude(Include object)
      {
        return createIncludeAdapter();
      }
      @Override
      public Adapter caseOperation(Operation object)
      {
        return createOperationAdapter();
      }
      @Override
      public Adapter caseOperationArray(OperationArray object)
      {
        return createOperationArrayAdapter();
      }
      @Override
      public Adapter caseJavaBody(JavaBody object)
      {
        return createJavaBodyAdapter();
      }
      @Override
      public Adapter caseARG(ARG object)
      {
        return createARGAdapter();
      }
      @Override
      public Adapter caseTypeTerm(TypeTerm object)
      {
        return createTypeTermAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
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
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.TomFile <em>Tom File</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.TomFile
   * @generated
   */
  public Adapter createTomFileAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.ArrayOperation <em>Array Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.ArrayOperation
   * @generated
   */
  public Adapter createArrayOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.Include <em>Include</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.Include
   * @generated
   */
  public Adapter createIncludeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.Operation
   * @generated
   */
  public Adapter createOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.OperationArray <em>Operation Array</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.OperationArray
   * @generated
   */
  public Adapter createOperationArrayAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.JavaBody <em>Java Body</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.JavaBody
   * @generated
   */
  public Adapter createJavaBodyAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.ARG <em>ARG</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.ARG
   * @generated
   */
  public Adapter createARGAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link tom.tomDsl.TypeTerm <em>Type Term</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see tom.tomDsl.TypeTerm
   * @generated
   */
  public Adapter createTypeTermAdapter()
  {
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
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //TomDslAdapterFactory
