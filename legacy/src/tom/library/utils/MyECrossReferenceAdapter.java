/**
 * Copyright (c) 2005-2012 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 */


package tom.library.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;


/**
 * An adapter that maintains itself as an adapter for all contained objects.
 * It can be installed for an {@link EObject}, a {@link Resource}, or a {@link ResourceSet}.
 * @since 2.2
 */
public class MyECrossReferenceAdapter extends org.eclipse.emf.ecore.util.ECrossReferenceAdapter
{

  public Collection<EStructuralFeature.Setting> getInverseReferences(EObject eObject)
  {
    return getInverseReferences(eObject, !resolve());
  }

  public Collection<EStructuralFeature.Setting> getInverseReferences(EObject eObject, boolean resolve)
  {
    Collection<EStructuralFeature.Setting> result = new ArrayList<EStructuralFeature.Setting>();
    
    if (resolve)
    {
      //System.out.println("DEBUG cross - will resolveAll");
      resolveAll(eObject);
    }
    
    EObject eContainer = eObject.eContainer();
    if (eContainer != null)
    {
      //System.out.println("DEBUG cross - 1st add");
      result.add(((InternalEObject)eContainer).eSetting(eObject.eContainmentFeature()));
    }
    
    Collection<EStructuralFeature.Setting> nonNavigableInverseReferences = inverseCrossReferencer.get(eObject);
    if (nonNavigableInverseReferences != null)
    {
      //System.out.println("DEBUG cross - addAll");
      result.addAll(nonNavigableInverseReferences);
    }
    
    for (EReference eReference : eObject.eClass().getEAllReferences())
    {
      EReference eOpposite = eReference.getEOpposite();
      if (eOpposite != null && !eReference.isContainer() && eObject.eIsSet(eReference))
      {
        //always in my example
        if (eReference.isMany())
        {
          Object collection = eObject.eGet(eReference);
          for (@SuppressWarnings("unchecked") Iterator<EObject> j = 
                 resolve() ? 
                   ((Collection<EObject>)collection).iterator() : 
                   ((org.eclipse.emf.ecore.util.InternalEList<EObject>)collection).basicIterator(); 
               j.hasNext(); )
          {
            InternalEObject referencingEObject = (InternalEObject)j.next();
            //System.out.println("DEBUG cross - 2nd add");
            result.add(referencingEObject.eSetting(eOpposite));
          }
        }
        else//never in my example
        {
          //System.out.println("DEBUG cross - 3rd add");
          result.add(((InternalEObject)eObject.eGet(eReference, resolve())).eSetting(eOpposite));
        }
      }
    }
    
    return result;
  }
}
