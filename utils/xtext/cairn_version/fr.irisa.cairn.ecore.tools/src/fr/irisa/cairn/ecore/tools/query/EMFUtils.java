package fr.irisa.cairn.ecore.tools.query;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EMFUtils<T> {

	private EClass eclass;

	public EMFUtils(EClass eclass) {
		this.eclass = eclass;
	}


	public T eContainerTypeSelect(EObject current) {

		while (current != null) {
			if (eclass.isSuperTypeOf(current.eClass())) {
				@SuppressWarnings("unchecked")
				T current2 = (T) current;
				return current2;
			}
			current = current.eContainer();
		}
		return null;
	}

	public static EObject eContainerTypeSelect(EObject current, EClass eclass) {

		while (current != null) {
			if (eclass.isSuperTypeOf(current.eClass())) {
				return current;
			}
			current = current.eContainer();
		}
		return null;
	}

	// FIXME : untested
	public static void replaceAllRefsInContainement(EObject root, EObject old,
			EClass newref) {
		TreeIterator<EObject> i = root.eAllContents();
		while (i.hasNext()) {
			EObject current = i.next();
			for (EObject crossref : current.eCrossReferences()) {
				if (crossref == old) {
					for (EReference feature : current.eClass()
							.getEAllReferences()) {
						if (feature.isMany()) {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							EList<EObject> eGet = (EList) current.eGet(feature);
							if (eGet.contains(old)) {
								int indexOf = eGet.indexOf(old);
								eGet.set(indexOf, newref);
							}
						} else {
							EObject ref = (EObject) current.eGet(feature);
							if (ref == old) {
								current.eSet(feature, newref);
							}
						}
					}
				}
			}
		}
	}

	// FIXME : untested
	public static void substituteByNewObjectInContainer(EObject oldObj,
			EObject newObj) {
		EStructuralFeature eContainingFeature = oldObj.eContainingFeature();
		EObject container = oldObj.eContainer();
		replaceFeature(oldObj, newObj, eContainingFeature, container);
	}

	private static void replaceFeature(EObject oldObj, EObject newObj,
			EStructuralFeature eContainingFeature, EObject container) {
		if (eContainingFeature.isMany()) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			EList<EObject> eGet = (EList) container.eGet(eContainingFeature);
			eGet.add(eGet.indexOf(oldObj), newObj);
			eGet.remove(oldObj);
		} else {
			container.eSet(eContainingFeature, newObj);
		}
	}

	public static EObject eRootContainer(EObject current) {
		while (current.eContainer() != null) {
			current = current.eContainer();
		}
		return current;
	}

	public EList<T> eListTypeSelect(EList<EObject> root) {
		EList<T> res = new BasicEList<T>();
		Iterator<EObject> iterator = root.iterator();
		while (iterator.hasNext()) {
			EObject obj = (EObject) iterator.next();
			if (eclass.isSuperTypeOf(obj.eClass())) {
				@SuppressWarnings("unchecked")
				T obj2 = (T) obj;
				res.add(obj2);
			}
		}
		return res;
	}

	public EList<T> eAllContentTypeSelect(EObject root) {
		EList<T> res = new BasicEList<T>();
		TreeIterator<EObject> iterator = root.eAllContents();
		while (iterator.hasNext()) {
			EObject obj = (EObject) iterator.next();
			if (eclass.isSuperTypeOf(obj.eClass())) {
				@SuppressWarnings("unchecked")
				T obj2 = (T) obj;
				res.add(obj2);
			}
		}
		return res;
	}

	/**
	 * XXX:Here we should add parameters so as to allow for a smarter search
	 * (with pruning)
	 * 
	 * @param root
	 * @param eclass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List eAllContentsFirstInstancesOf(EObject root, EClass eclass) {
		/* FIXME : Because of a hack in the containment for ComponentInstructions, refelxive 
		 * Query may gives duplicate results in some cases. We hence use an ordered 
		 * Set to collect the results and return a list 
		 */
		LinkedHashSet<EObject> res = new LinkedHashSet<EObject>();
		TreeIterator<EObject> iterator = root.eAllContents();
		while (iterator.hasNext()) {
			EObject obj = (EObject) iterator.next();
			if (eclass.isSuperTypeOf(obj.eClass())) {
				res.add(obj);
				iterator.prune();
			}
		}
		return new BasicEList<EObject>(res);
	}  
	/**
	 * XXX:Here we should add parameters so as to allow for a smarter search
	 * (with pruning)
	 * 
	 * @param root
	 * @param eclass
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List eAllContentsInstancesOf(EObject root, EClass eclass) {
		/* FIXME : Because of a hack in the containment for ComponentInstructions, refelxive 
		 * Query may gives duplicate results in some cases. We hence use an ordered 
		 * Set to collect the results and return a list 
		 */
		LinkedHashSet<EObject> res = new LinkedHashSet<EObject>();
		TreeIterator<EObject> iterator = root.eAllContents();
		while (iterator.hasNext()) {
			EObject obj = (EObject) iterator.next();
			if (eclass.isSuperTypeOf(obj.eClass())) {
				res.add(obj);
			}
		}
		return new BasicEList<EObject>(res);
	}  

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List eAllListContentsInstancesOf(List<EObject> root, EClass eclass) {
		EList<EObject> res = new BasicEList<EObject>();
		for (EObject eObject : root) {
			res.addAll(eAllContentsInstancesOf(eObject, eclass));
		}
		return res;
	}


	@SuppressWarnings("rawtypes")
	public static List searchInstancesOfInPath(EObject root, EClass eclass, EClass... paths) {
		EList<EObject> res = new BasicEList<EObject>();
		List<EClass> classes = Arrays.asList(paths);
		TreeIterator<EObject> iterator = root.eAllContents();
		while (iterator.hasNext()) {
			EObject obj = (EObject) iterator.next();
			if (eclass.isSuperTypeOf(obj.eClass())) {
				res.add(obj);
			}
			if(!classes.contains(obj.eClass())) {
				iterator.prune();
			}
		}
		return res;
	}  



	@SuppressWarnings("unchecked")
	public static <T> List<T> getAllReferencesOfType(EObject o, Class<T> c){
		List<T> refs = new UniqueEList<T>();
		for (EReference r : o.eClass().getEAllReferences()) {
			Object eGet = o.eGet(r);
			if (eGet!=null && c.isAssignableFrom(eGet.getClass())) {
				refs.add((T) eGet);
			}
			if(r.isContainment()){
				if(r.getUpperBound()==-1){
					List<EObject> eGet2 = (List<EObject>)eGet;
					for(EObject object: eGet2){
						refs.addAll(getAllReferencesOfType(object, c));
					}
				}else
					refs.addAll(getAllReferencesOfType((EObject) eGet, c));
					
			}
		}
		return refs;
	}
}
