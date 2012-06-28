/**
 * <copyright>
 * </copyright>
 *
 */
package tom.tomDsl;

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
 * @see tom.tomDsl.TomDslFactory
 * @model kind="package"
 * @generated
 */
public interface TomDslPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "tomDsl";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.TomDsl.tom";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "tomDsl";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  TomDslPackage eINSTANCE = tom.tomDsl.impl.TomDslPackageImpl.init();

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.TomFileImpl <em>Tom File</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.TomFileImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getTomFile()
   * @generated
   */
  int TOM_FILE = 0;

  /**
   * The feature id for the '<em><b>Ops</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TOM_FILE__OPS = 0;

  /**
   * The feature id for the '<em><b>Terms</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TOM_FILE__TERMS = 1;

  /**
   * The feature id for the '<em><b>Inc</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TOM_FILE__INC = 2;

  /**
   * The feature id for the '<em><b>Locals</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TOM_FILE__LOCALS = 3;

  /**
   * The number of structural features of the '<em>Tom File</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TOM_FILE_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.ArrayOperationImpl <em>Array Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.ArrayOperationImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getArrayOperation()
   * @generated
   */
  int ARRAY_OPERATION = 1;

  /**
   * The feature id for the '<em><b>Term</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_OPERATION__TERM = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_OPERATION__NAME = 1;

  /**
   * The feature id for the '<em><b>Fsym</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_OPERATION__FSYM = 2;

  /**
   * The number of structural features of the '<em>Array Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARRAY_OPERATION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.IncludeImpl <em>Include</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.IncludeImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getInclude()
   * @generated
   */
  int INCLUDE = 2;

  /**
   * The feature id for the '<em><b>Path</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUDE__PATH = 0;

  /**
   * The number of structural features of the '<em>Include</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INCLUDE_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.OperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.OperationImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getOperation()
   * @generated
   */
  int OPERATION = 3;

  /**
   * The feature id for the '<em><b>Term</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__TERM = ARRAY_OPERATION__TERM;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__NAME = ARRAY_OPERATION__NAME;

  /**
   * The feature id for the '<em><b>Fsym</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__FSYM = ARRAY_OPERATION__FSYM;

  /**
   * The feature id for the '<em><b>Arg</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__ARG = ARRAY_OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Slot</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__SLOT = ARRAY_OPERATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Make</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__MAKE = ARRAY_OPERATION_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_FEATURE_COUNT = ARRAY_OPERATION_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.OperationArrayImpl <em>Operation Array</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.OperationArrayImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getOperationArray()
   * @generated
   */
  int OPERATION_ARRAY = 4;

  /**
   * The feature id for the '<em><b>Term</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__TERM = ARRAY_OPERATION__TERM;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__NAME = ARRAY_OPERATION__NAME;

  /**
   * The feature id for the '<em><b>Fsym</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__FSYM = ARRAY_OPERATION__FSYM;

  /**
   * The feature id for the '<em><b>Size</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__SIZE = ARRAY_OPERATION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Element</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__ELEMENT = ARRAY_OPERATION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Empty</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__EMPTY = ARRAY_OPERATION_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Append</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY__APPEND = ARRAY_OPERATION_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Operation Array</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_ARRAY_FEATURE_COUNT = ARRAY_OPERATION_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.JavaBodyImpl <em>Java Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.JavaBodyImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getJavaBody()
   * @generated
   */
  int JAVA_BODY = 5;

  /**
   * The feature id for the '<em><b>Body</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_BODY__BODY = 0;

  /**
   * The number of structural features of the '<em>Java Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JAVA_BODY_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.ARGImpl <em>ARG</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.ARGImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getARG()
   * @generated
   */
  int ARG = 6;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARG__NAME = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARG__TYPE = 1;

  /**
   * The number of structural features of the '<em>ARG</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ARG_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link tom.tomDsl.impl.TypeTermImpl <em>Type Term</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see tom.tomDsl.impl.TypeTermImpl
   * @see tom.tomDsl.impl.TomDslPackageImpl#getTypeTerm()
   * @generated
   */
  int TYPE_TERM = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TERM__NAME = 0;

  /**
   * The feature id for the '<em><b>Implement</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TERM__IMPLEMENT = 1;

  /**
   * The feature id for the '<em><b>Sort</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TERM__SORT = 2;

  /**
   * The feature id for the '<em><b>Equals</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TERM__EQUALS = 3;

  /**
   * The number of structural features of the '<em>Type Term</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPE_TERM_FEATURE_COUNT = 4;


  /**
   * Returns the meta object for class '{@link tom.tomDsl.TomFile <em>Tom File</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Tom File</em>'.
   * @see tom.tomDsl.TomFile
   * @generated
   */
  EClass getTomFile();

  /**
   * Returns the meta object for the containment reference list '{@link tom.tomDsl.TomFile#getOps <em>Ops</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Ops</em>'.
   * @see tom.tomDsl.TomFile#getOps()
   * @see #getTomFile()
   * @generated
   */
  EReference getTomFile_Ops();

  /**
   * Returns the meta object for the containment reference list '{@link tom.tomDsl.TomFile#getTerms <em>Terms</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Terms</em>'.
   * @see tom.tomDsl.TomFile#getTerms()
   * @see #getTomFile()
   * @generated
   */
  EReference getTomFile_Terms();

  /**
   * Returns the meta object for the containment reference list '{@link tom.tomDsl.TomFile#getInc <em>Inc</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Inc</em>'.
   * @see tom.tomDsl.TomFile#getInc()
   * @see #getTomFile()
   * @generated
   */
  EReference getTomFile_Inc();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.TomFile#getLocals <em>Locals</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Locals</em>'.
   * @see tom.tomDsl.TomFile#getLocals()
   * @see #getTomFile()
   * @generated
   */
  EAttribute getTomFile_Locals();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.ArrayOperation <em>Array Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Array Operation</em>'.
   * @see tom.tomDsl.ArrayOperation
   * @generated
   */
  EClass getArrayOperation();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.ArrayOperation#getTerm <em>Term</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Term</em>'.
   * @see tom.tomDsl.ArrayOperation#getTerm()
   * @see #getArrayOperation()
   * @generated
   */
  EAttribute getArrayOperation_Term();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.ArrayOperation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see tom.tomDsl.ArrayOperation#getName()
   * @see #getArrayOperation()
   * @generated
   */
  EAttribute getArrayOperation_Name();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.ArrayOperation#getFsym <em>Fsym</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Fsym</em>'.
   * @see tom.tomDsl.ArrayOperation#getFsym()
   * @see #getArrayOperation()
   * @generated
   */
  EReference getArrayOperation_Fsym();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.Include <em>Include</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Include</em>'.
   * @see tom.tomDsl.Include
   * @generated
   */
  EClass getInclude();

  /**
   * Returns the meta object for the attribute list '{@link tom.tomDsl.Include#getPath <em>Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Path</em>'.
   * @see tom.tomDsl.Include#getPath()
   * @see #getInclude()
   * @generated
   */
  EAttribute getInclude_Path();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see tom.tomDsl.Operation
   * @generated
   */
  EClass getOperation();

  /**
   * Returns the meta object for the containment reference list '{@link tom.tomDsl.Operation#getArg <em>Arg</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Arg</em>'.
   * @see tom.tomDsl.Operation#getArg()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_Arg();

  /**
   * Returns the meta object for the containment reference list '{@link tom.tomDsl.Operation#getSlot <em>Slot</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Slot</em>'.
   * @see tom.tomDsl.Operation#getSlot()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_Slot();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.Operation#getMake <em>Make</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Make</em>'.
   * @see tom.tomDsl.Operation#getMake()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_Make();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.OperationArray <em>Operation Array</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation Array</em>'.
   * @see tom.tomDsl.OperationArray
   * @generated
   */
  EClass getOperationArray();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.OperationArray#getSize <em>Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Size</em>'.
   * @see tom.tomDsl.OperationArray#getSize()
   * @see #getOperationArray()
   * @generated
   */
  EReference getOperationArray_Size();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.OperationArray#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Element</em>'.
   * @see tom.tomDsl.OperationArray#getElement()
   * @see #getOperationArray()
   * @generated
   */
  EReference getOperationArray_Element();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.OperationArray#getEmpty <em>Empty</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Empty</em>'.
   * @see tom.tomDsl.OperationArray#getEmpty()
   * @see #getOperationArray()
   * @generated
   */
  EReference getOperationArray_Empty();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.OperationArray#getAppend <em>Append</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Append</em>'.
   * @see tom.tomDsl.OperationArray#getAppend()
   * @see #getOperationArray()
   * @generated
   */
  EReference getOperationArray_Append();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.JavaBody <em>Java Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Java Body</em>'.
   * @see tom.tomDsl.JavaBody
   * @generated
   */
  EClass getJavaBody();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.JavaBody#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Body</em>'.
   * @see tom.tomDsl.JavaBody#getBody()
   * @see #getJavaBody()
   * @generated
   */
  EAttribute getJavaBody_Body();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.ARG <em>ARG</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ARG</em>'.
   * @see tom.tomDsl.ARG
   * @generated
   */
  EClass getARG();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.ARG#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see tom.tomDsl.ARG#getName()
   * @see #getARG()
   * @generated
   */
  EAttribute getARG_Name();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.ARG#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see tom.tomDsl.ARG#getType()
   * @see #getARG()
   * @generated
   */
  EAttribute getARG_Type();

  /**
   * Returns the meta object for class '{@link tom.tomDsl.TypeTerm <em>Type Term</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Type Term</em>'.
   * @see tom.tomDsl.TypeTerm
   * @generated
   */
  EClass getTypeTerm();

  /**
   * Returns the meta object for the attribute '{@link tom.tomDsl.TypeTerm#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see tom.tomDsl.TypeTerm#getName()
   * @see #getTypeTerm()
   * @generated
   */
  EAttribute getTypeTerm_Name();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.TypeTerm#getImplement <em>Implement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Implement</em>'.
   * @see tom.tomDsl.TypeTerm#getImplement()
   * @see #getTypeTerm()
   * @generated
   */
  EReference getTypeTerm_Implement();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.TypeTerm#getSort <em>Sort</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Sort</em>'.
   * @see tom.tomDsl.TypeTerm#getSort()
   * @see #getTypeTerm()
   * @generated
   */
  EReference getTypeTerm_Sort();

  /**
   * Returns the meta object for the containment reference '{@link tom.tomDsl.TypeTerm#getEquals <em>Equals</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Equals</em>'.
   * @see tom.tomDsl.TypeTerm#getEquals()
   * @see #getTypeTerm()
   * @generated
   */
  EReference getTypeTerm_Equals();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  TomDslFactory getTomDslFactory();

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
  interface Literals
  {
    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.TomFileImpl <em>Tom File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.TomFileImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getTomFile()
     * @generated
     */
    EClass TOM_FILE = eINSTANCE.getTomFile();

    /**
     * The meta object literal for the '<em><b>Ops</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TOM_FILE__OPS = eINSTANCE.getTomFile_Ops();

    /**
     * The meta object literal for the '<em><b>Terms</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TOM_FILE__TERMS = eINSTANCE.getTomFile_Terms();

    /**
     * The meta object literal for the '<em><b>Inc</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TOM_FILE__INC = eINSTANCE.getTomFile_Inc();

    /**
     * The meta object literal for the '<em><b>Locals</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TOM_FILE__LOCALS = eINSTANCE.getTomFile_Locals();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.ArrayOperationImpl <em>Array Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.ArrayOperationImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getArrayOperation()
     * @generated
     */
    EClass ARRAY_OPERATION = eINSTANCE.getArrayOperation();

    /**
     * The meta object literal for the '<em><b>Term</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARRAY_OPERATION__TERM = eINSTANCE.getArrayOperation_Term();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARRAY_OPERATION__NAME = eINSTANCE.getArrayOperation_Name();

    /**
     * The meta object literal for the '<em><b>Fsym</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ARRAY_OPERATION__FSYM = eINSTANCE.getArrayOperation_Fsym();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.IncludeImpl <em>Include</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.IncludeImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getInclude()
     * @generated
     */
    EClass INCLUDE = eINSTANCE.getInclude();

    /**
     * The meta object literal for the '<em><b>Path</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INCLUDE__PATH = eINSTANCE.getInclude_Path();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.OperationImpl <em>Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.OperationImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getOperation()
     * @generated
     */
    EClass OPERATION = eINSTANCE.getOperation();

    /**
     * The meta object literal for the '<em><b>Arg</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__ARG = eINSTANCE.getOperation_Arg();

    /**
     * The meta object literal for the '<em><b>Slot</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__SLOT = eINSTANCE.getOperation_Slot();

    /**
     * The meta object literal for the '<em><b>Make</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__MAKE = eINSTANCE.getOperation_Make();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.OperationArrayImpl <em>Operation Array</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.OperationArrayImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getOperationArray()
     * @generated
     */
    EClass OPERATION_ARRAY = eINSTANCE.getOperationArray();

    /**
     * The meta object literal for the '<em><b>Size</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION_ARRAY__SIZE = eINSTANCE.getOperationArray_Size();

    /**
     * The meta object literal for the '<em><b>Element</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION_ARRAY__ELEMENT = eINSTANCE.getOperationArray_Element();

    /**
     * The meta object literal for the '<em><b>Empty</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION_ARRAY__EMPTY = eINSTANCE.getOperationArray_Empty();

    /**
     * The meta object literal for the '<em><b>Append</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION_ARRAY__APPEND = eINSTANCE.getOperationArray_Append();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.JavaBodyImpl <em>Java Body</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.JavaBodyImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getJavaBody()
     * @generated
     */
    EClass JAVA_BODY = eINSTANCE.getJavaBody();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JAVA_BODY__BODY = eINSTANCE.getJavaBody_Body();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.ARGImpl <em>ARG</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.ARGImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getARG()
     * @generated
     */
    EClass ARG = eINSTANCE.getARG();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARG__NAME = eINSTANCE.getARG_Name();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ARG__TYPE = eINSTANCE.getARG_Type();

    /**
     * The meta object literal for the '{@link tom.tomDsl.impl.TypeTermImpl <em>Type Term</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see tom.tomDsl.impl.TypeTermImpl
     * @see tom.tomDsl.impl.TomDslPackageImpl#getTypeTerm()
     * @generated
     */
    EClass TYPE_TERM = eINSTANCE.getTypeTerm();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TYPE_TERM__NAME = eINSTANCE.getTypeTerm_Name();

    /**
     * The meta object literal for the '<em><b>Implement</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_TERM__IMPLEMENT = eINSTANCE.getTypeTerm_Implement();

    /**
     * The meta object literal for the '<em><b>Sort</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_TERM__SORT = eINSTANCE.getTypeTerm_Sort();

    /**
     * The meta object literal for the '<em><b>Equals</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TYPE_TERM__EQUALS = eINSTANCE.getTypeTerm_Equals();

  }

} //TomDslPackage
