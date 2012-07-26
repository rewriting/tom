/**
 */
package model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.ClassOperator;
import model.FeatureParameter;
import model.Import;
import model.Mapping;
import model.ModelFactory;
import model.ModelPackage;
import model.Module;
import model.Operator;
import model.SettedFeatureParameter;
import model.Terminal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.MappingImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getTerminals <em>Terminals</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getExternalTerminals <em>External Terminals</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getOperators <em>Operators</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link model.impl.MappingImpl#getModules <em>Modules</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MappingImpl extends EObjectImpl implements Mapping {
	/**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected EList<Import> imports;

	/**
	 * The cached value of the '{@link #getTerminals() <em>Terminals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTerminals()
	 * @generated
	 * @ordered
	 */
	protected EList<Terminal> terminals;

	/**
	 * The cached value of the '{@link #getExternalTerminals() <em>External Terminals</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExternalTerminals()
	 * @generated
	 * @ordered
	 */
	protected EList<Terminal> externalTerminals;

	/**
	 * The cached value of the '{@link #getOperators() <em>Operators</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperators()
	 * @generated
	 * @ordered
	 */
	protected EList<Operator> operators;

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
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final String PREFIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected String prefix = PREFIX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModules() <em>Modules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModules()
	 * @generated
	 * @ordered
	 */
	protected EList<Module> modules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Import> getImports() {
		if (imports == null) {
			imports = new EObjectContainmentEList<Import>(Import.class, this, ModelPackage.MAPPING__IMPORTS);
		}
		return imports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Terminal> getTerminals() {
		if (terminals == null) {
			terminals = new EObjectContainmentEList<Terminal>(Terminal.class, this, ModelPackage.MAPPING__TERMINALS);
		}
		return terminals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Terminal> getExternalTerminals() {
		if (externalTerminals == null) {
			externalTerminals = new EObjectResolvingEList<Terminal>(Terminal.class, this, ModelPackage.MAPPING__EXTERNAL_TERMINALS);
		}
		return externalTerminals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Operator> getOperators() {
		if (operators == null) {
			operators = new EObjectContainmentEList<Operator>(Operator.class, this, ModelPackage.MAPPING__OPERATORS);
		}
		return operators;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MAPPING__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrefix(String newPrefix) {
		String oldPrefix = prefix;
		prefix = newPrefix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MAPPING__PREFIX, oldPrefix, prefix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Module> getModules() {
		if (modules == null) {
			modules = new EObjectContainmentEList<Module>(Module.class, this, ModelPackage.MAPPING__MODULES);
		}
		return modules;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<EClass> getAllDefaultOperators() {
		EList<EClass> operators = new UniqueEList<EClass>();
		for (EPackage p : getMetamodelPackages()) {
			operators.addAll(getDefaultOperatorsIn(p));
		}
		return operators;
	}

	/**
	 * Find all operators which are not defined in mapping operators for a
	 * {@link EPackage}.
	 * 
	 * @param p
	 * @return
	 */
	private EList<EClass> getDefaultOperatorsIn(EPackage p) {
		EList<EClass> ops = new BasicEList<EClass>();
		for (EClassifier c : p.getEClassifiers()) {
			if (c instanceof EClass) {
				if (isDefaultOperator((EClass) c))
					ops.add((EClass) c);
			}
		}
		for (EPackage subp : p.getESubpackages()) {
			ops.addAll(getDefaultOperatorsIn(subp));
		}
		return ops;
	}
	
	/**
	 * A class is a default operator if it has one Terminal in its inheritance
	 * top hierachy or if no terminals have been defined. A class operator with
	 * a setted feature parameter isn't a default operator.
	 * 
	 * @param c
	 * @return
	 */
	private boolean isDefaultOperator(EClass c) {
		for (Operator op : getOperators()) {
			if (op instanceof ClassOperator) {
				ClassOperator cop = (ClassOperator) op;
				if (areSameClasses(cop.getClass_(), c)
						&& !isSettedOperator(cop))
					return false;
			}
		}
		return !c.isAbstract()
				&& (getTerminal(c, false) != null || getTerminals().size() == 0);
	}
	
	private boolean areSameClasses(EClass c1, EClass c2) {

		EPackage ePackage1 = c1.getEPackage();
		EPackage ePackage2 = c2.getEPackage();
		if (ePackage1.getNsURI() == ePackage2.getNsURI()
				|| ePackage1.getNsURI().equals(ePackage2.getNsURI())) {
			return c1.getClassifierID() == c2.getClassifierID();
		}
		return false;

	}
	
	private boolean isSettedOperator(ClassOperator op) {
		for (FeatureParameter fp : op.getParameters()) {
			if (fp instanceof SettedFeatureParameter) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public Terminal getTerminal(EClass c, boolean isList) {
		Terminal terminal = searchInTerminals(c, isList);
		if (terminal == null) {
			// Find a super class terminal
			List<EClass> superclasses = c.getESuperTypes();
			if (superclasses.size() > 0) {
				terminal = searchFirstTerminal(superclasses, isList);
			}
		}
		return terminal;
	}

	/**
	 * Search terminal of an {@link EClass} in the mapping terminals list.
	 * 
	 * @param c
	 * @return
	 */
	private Terminal searchInTerminals(EClass c, boolean isList) {
		String className = c.getName();
		if (terminals != null)
			//System.out.println("Searching in local terminals for "+c.getName()+ " in "+terminals);
			for (Terminal term : terminals) {
				String termName = term.getClass_().getName();
				boolean many = term.isMany();
				if (many == isList && termName.equals(className))
					return term;
			}
		
		if (externalTerminals!= null)
			//System.out.println("Searching in local terminals for "+c.getName()+ " in external "+terminals);
			for (Terminal term : externalTerminals) {
				String termName = term.getClass_().getName();
				boolean many = term.isMany();
				if (many == isList) {
					if(termName.equals(className)) {
						return term;
					}
				}
			}
		
		if (isList && listTerminals != null) {
			for (Terminal term : listTerminals) {
				if (term.getClass_().getName().compareTo(className) == 0)
					return term;
			}
		}
		
		return null;
	}
	
	/**
	 * Search for a terminal in list of classes. If no terminals are found then
	 * search in super classes.
	 * 
	 * @param classes
	 * @return
	 */
	private Terminal searchFirstTerminal(List<EClass> classes, boolean isList) {
		if (classes.size() > 0) {
			for (EClass c : classes) {
				Terminal term = searchInTerminals(c, isList);
				if (term != null)
					return term;
				
			}
			return searchFirstTerminal(getSuperClasses(classes), isList);
		} else
			return null;
	}
	
	/**
	 * Get all super classes of a list of classes.
	 * 
	 * @param classes
	 * @return
	 */
	private List<EClass> getSuperClasses(List<EClass> classes) {
		List<EClass> superClasses = new ArrayList<EClass>();
		for (EClass eClass : classes) {
			superClasses.addAll(eClass.getESuperTypes());
		}
		return superClasses;
	}
	
	
	private EList<Terminal> listTerminals;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<Terminal> getAllListTerminals() {
		if (listTerminals == null) {
			listTerminals = new UniqueEList<Terminal>();
			for (EPackage p : getMetamodelPackages()) {
				TreeIterator<EObject> eAllContents = p.eAllContents();
				while (eAllContents.hasNext()) {
					EObject o = eAllContents.next();
					if (o instanceof EReference) {
						EReference feature = (EReference) o;
						if (feature.isMany()) {
							Terminal terminal = getTerminal(
									feature.getEReferenceType(), false);
							if (terminal != null
									&& getTerminal(feature.getEReferenceType(),
											true) == null) {
								Terminal defaultListTerminal = ModelFactory.eINSTANCE
										.createTerminal();
								defaultListTerminal.setClass(terminal
										.getClass_());
								defaultListTerminal.setMany(true);
								defaultListTerminal.setName(terminal.getName()
										+ "List");
								listTerminals.add(defaultListTerminal);
							}
						}
					}
				}
			}
		}
		return listTerminals;
	}

	private EList<EPackage> mm;
	
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public EList<EPackage> getMetamodelPackages() {
		if (mm == null) {
			mm = new BasicEList<EPackage>();
			GenericXMIResourceManager<EPackage> loader = new GenericXMIResourceManager<EPackage>(
					EcorePackage.eINSTANCE);
			// Obtain a new resource set
			ResourceSet resSet = new ResourceSetImpl();
			// Get the resource
			for (Import i : getImports()) {
				Resource resource = resSet.getResource(
						URI.createURI(i.getImportURI()), true);
				EPackage load = loader.load(i.getImportURI());

				mm.add(load);
			}

		}
		return mm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				return ((InternalEList<?>)getImports()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__TERMINALS:
				return ((InternalEList<?>)getTerminals()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__OPERATORS:
				return ((InternalEList<?>)getOperators()).basicRemove(otherEnd, msgs);
			case ModelPackage.MAPPING__MODULES:
				return ((InternalEList<?>)getModules()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				return getImports();
			case ModelPackage.MAPPING__TERMINALS:
				return getTerminals();
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				return getExternalTerminals();
			case ModelPackage.MAPPING__OPERATORS:
				return getOperators();
			case ModelPackage.MAPPING__NAME:
				return getName();
			case ModelPackage.MAPPING__PREFIX:
				return getPrefix();
			case ModelPackage.MAPPING__MODULES:
				return getModules();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				getImports().clear();
				getImports().addAll((Collection<? extends Import>)newValue);
				return;
			case ModelPackage.MAPPING__TERMINALS:
				getTerminals().clear();
				getTerminals().addAll((Collection<? extends Terminal>)newValue);
				return;
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				getExternalTerminals().clear();
				getExternalTerminals().addAll((Collection<? extends Terminal>)newValue);
				return;
			case ModelPackage.MAPPING__OPERATORS:
				getOperators().clear();
				getOperators().addAll((Collection<? extends Operator>)newValue);
				return;
			case ModelPackage.MAPPING__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.MAPPING__PREFIX:
				setPrefix((String)newValue);
				return;
			case ModelPackage.MAPPING__MODULES:
				getModules().clear();
				getModules().addAll((Collection<? extends Module>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				getImports().clear();
				return;
			case ModelPackage.MAPPING__TERMINALS:
				getTerminals().clear();
				return;
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				getExternalTerminals().clear();
				return;
			case ModelPackage.MAPPING__OPERATORS:
				getOperators().clear();
				return;
			case ModelPackage.MAPPING__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.MAPPING__PREFIX:
				setPrefix(PREFIX_EDEFAULT);
				return;
			case ModelPackage.MAPPING__MODULES:
				getModules().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ModelPackage.MAPPING__IMPORTS:
				return imports != null && !imports.isEmpty();
			case ModelPackage.MAPPING__TERMINALS:
				return terminals != null && !terminals.isEmpty();
			case ModelPackage.MAPPING__EXTERNAL_TERMINALS:
				return externalTerminals != null && !externalTerminals.isEmpty();
			case ModelPackage.MAPPING__OPERATORS:
				return operators != null && !operators.isEmpty();
			case ModelPackage.MAPPING__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.MAPPING__PREFIX:
				return PREFIX_EDEFAULT == null ? prefix != null : !PREFIX_EDEFAULT.equals(prefix);
			case ModelPackage.MAPPING__MODULES:
				return modules != null && !modules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", prefix: ");
		result.append(prefix);
		result.append(')');
		return result.toString();
	}

} //MappingImpl
