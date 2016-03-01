/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2009-2016, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 * Antoine Floch
 * Nicolas Henry
 *
 **/

package tom.library.emf;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.common.util.EList;

import tom.library.sl.Introspector;

public class EcoreContainmentIntrospector implements Introspector {
  
  @SuppressWarnings("unchecked")
  private <O extends Object> List<O> getList(Object o){
    if(o instanceof List<?>) {
      return (List<O>) o;
    } else if(o instanceof EObject) {
      EObject eo = (EObject) o;
      return (List<O>) eo.eContents();
    }
    return null;
  }
  
  public Object getChildAt(Object o, int i) {
    List<Object> l=getList(o);
    if(l!=null) {
      return l.get(i);
    }
    return null;
  }

  public int getChildCount(Object o) {
    List<Object> l=getList(o);
    if(l!=null) {
      return l.size();
    }
    return 0;
  }

  public Object[] getChildren(Object o) {
    List<Object> l=getList(o);
    if(l!=null) {
      return l.toArray();
    }
    return null;
  }

  //Original code
  /*@Override
  public <T> T setChildAt(T o, int i, Object obj) {
    List<Object> l=getList(o);
    if(l!=null) {
      l.set(i,o);
      return o;
    }
    return null;
  }*/


  // Antoine Floch's version
	@SuppressWarnings("unchecked")
	// XXX: original tom implementation fixed by antoine (need to be extensively
	// tested)
	public <T> T setChildAt(T o, int i, Object obj) {
		EObject eo = (EObject) o;

		// 1) Get the structural feature which has to be modified
		EStructuralFeature feature = getFeature(eo, i);

		// 2) If feature is a list
		if(feature.getUpperBound() == -1) {
			EList<EObject> list = (EList<EObject>) eo.eGet(feature);

			if(list.size() > 0) {
				// find the position in the list and set it
				int pos = getPositionInTheContainingList(i, eo, feature);
				if(pos > list.size()) {
					throw new RuntimeException("Relative position isn't allowed.");
        } else {
					list.set(pos, (EObject) obj);
        }
			} else {
				list.add((EObject) obj);
			}

		} else {
			// else just set the feature
			eo.eSet(feature, obj);
		}

		return o;
	}

	/**
	 * Get a position in a feature list from global position in all contents of
	 * the referencing object.
	 * 
	 * @param global position of the object in all contents
	 * @param referencing
	 * @param feature target feature
	 * @return the relative position
	 */
	private int getPositionInTheContainingList(int global, EObject referencing,
			EStructuralFeature feature) {
		int offset = 0;
		List<EObject> contents = getList(referencing);
		while (offset < contents.size()
				&& contents.get(offset).eContainingFeature() != feature) {
			offset++;
    }
		return global - offset;
	}

	private EStructuralFeature getFeature(EObject o, int i) {
		List<EObject> l = getList(o);
		return l.get(i).eContainingFeature();
	}
///

  //Original code
  /*@Override
  public <T> T setChildren(T o, Object[] objs) {
    List<Object> l=getList(o);
    if(l!=null) {
      for(int i = 0; i < objs.length; i++) {
        l.set(i, objs[i]);
      }
      return o;
    }
    return null;
  }*/


  // Antoine Floch's version
	@SuppressWarnings("unchecked")
	// XXX: original tom implementation fixed by antoine (need to be extensively
	// tested)
	public <T> T setChildren(T o, Object[] objs) {
		List<EObject> l = getList(o);

		EObject eo = (EObject) o;

		// Memorize all setted features
		List<EStructuralFeature> settedFeatures = new ArrayList<EStructuralFeature>();
		for(EObject el : l) {
			settedFeatures.add(el.eContainingFeature());
		}

		// Clear list ones
		clear(eo, new HashSet<EStructuralFeature>(settedFeatures));

		for(int i = 0; i < settedFeatures.size(); i++) {
			EStructuralFeature feature = settedFeatures.get(i);
			if(feature.getUpperBound() == -1) {
				// in case of a list feature: add each object to its list
				List<EObject> list = (List<EObject>) eo.eGet(feature);
        if(objs[i]!=null) {
          list.add((EObject) objs[i]);
        }
			} else {
				eo.eSet(l.get(i).eContainingFeature(), objs[i]);
      }
		}

		return o;
	}

	/**
	 * Clear list features of an {@link EObject}.
	 * 
	 * @param eo
	 * @param features
	 */
	@SuppressWarnings({ "unchecked" })
	private void clear(EObject eo, Set<EStructuralFeature> features) {
		for(EStructuralFeature feature : features) {
			if(feature.getUpperBound() == -1) {
				List<EObject> list = (List<EObject>) eo.eGet(feature);
				list.clear();
			}
		}
	}

}
