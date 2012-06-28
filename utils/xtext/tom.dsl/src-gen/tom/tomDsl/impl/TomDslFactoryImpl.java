/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import tom.tomDsl.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TomDslFactoryImpl extends EFactoryImpl implements TomDslFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TomDslFactory init()
  {
    try
    {
      TomDslFactory theTomDslFactory = (TomDslFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.TomDsl.tom"); 
      if (theTomDslFactory != null)
      {
        return theTomDslFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new TomDslFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TomDslFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case TomDslPackage.TOM_FILE: return createTomFile();
      case TomDslPackage.ARRAY_OPERATION: return createArrayOperation();
      case TomDslPackage.INCLUDE: return createInclude();
      case TomDslPackage.OPERATION: return createOperation();
      case TomDslPackage.OPERATION_ARRAY: return createOperationArray();
      case TomDslPackage.JAVA_BODY: return createJavaBody();
      case TomDslPackage.ARG: return createARG();
      case TomDslPackage.TYPE_TERM: return createTypeTerm();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TomFile createTomFile()
  {
    TomFileImpl tomFile = new TomFileImpl();
    return tomFile;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ArrayOperation createArrayOperation()
  {
    ArrayOperationImpl arrayOperation = new ArrayOperationImpl();
    return arrayOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Include createInclude()
  {
    IncludeImpl include = new IncludeImpl();
    return include;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operation createOperation()
  {
    OperationImpl operation = new OperationImpl();
    return operation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OperationArray createOperationArray()
  {
    OperationArrayImpl operationArray = new OperationArrayImpl();
    return operationArray;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JavaBody createJavaBody()
  {
    JavaBodyImpl javaBody = new JavaBodyImpl();
    return javaBody;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ARG createARG()
  {
    ARGImpl arg = new ARGImpl();
    return arg;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TypeTerm createTypeTerm()
  {
    TypeTermImpl typeTerm = new TypeTermImpl();
    return typeTerm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TomDslPackage getTomDslPackage()
  {
    return (TomDslPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static TomDslPackage getPackage()
  {
    return TomDslPackage.eINSTANCE;
  }

} //TomDslFactoryImpl
