/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Jean-Christophe Bach e-mail: jeanchristophe.bach@inria.fr
 *
 **/

package tom.library.utils;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.emf.ecore.EObject;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class LinkClass {
  private ConcurrentMap<EObject,ReferenceClass> table;
  /* 
   * Test a new way of implementing resolution phase
   * If this approach is kept, we should rename the class, the functions and
   * the attributes.
   * Implementation and data structures might also be changed
   * */
  //private Set<EObject> tracedReferences;
  //private Set<EObject> resolveObjects;
  
  private HashMap<EObject,Set<EObject>> map;

  public LinkClass() {
    this.table = new ConcurrentHashMap<EObject,ReferenceClass>();
    //this.tracedReferences = new HashSet<EObject>();
    //this.resolveObjects = new HashSet<EObject>();
    this.map = new HashMap<EObject,Set<EObject>>();
  }

  /* tracedReferences related functions */
  public Set<EObject> getTraced(EObject resolveNode) {
    Set<EObject> s = map.get(resolveNode);
    if(s == null) {
      s = new HashSet<EObject>();
    }
    return s;
  }

  public boolean keepTrace(EObject resolveNode, EObject traced) {
    boolean res = false;
    Set<EObject> s = map.get(resolveNode);
    if(s == null) {
      s = new HashSet<EObject>();
    } 
    res = s.add(traced);
    map.put(resolveNode,s);
    return res;
  }

  //public boolean remove(EObject o) {
  //  return this.tracedReferences.remove(o);
  //}

  //public boolean hasBeenReferenced(EObject o) {
  //  return this.tracedReferences.contains(o);
  //}

  //public int tracedSize() {
  //  return this.tracedReferences.size();
  //}

  /* resolveObjects related functions */

  public Set<EObject> getResolve() {
    return map.keySet();
  }

  //public boolean traceResolve(EObject resolve) {
  //  return this.resolveObjects.add(resolve);
  //}

  //public boolean resolveExists(EObject resolve) {
  //  return this.resolveObjects.contains(resolve);
  //}

  //public int resolveObjectsSize() {
  //  return this.resolveObjects.size();
  //}

////////////////

  public ReferenceClass put(EObject key, ReferenceClass value) {
    return this.table.put(key,value);
  }

  public boolean containsKey(EObject key) {
    return this.table.containsKey(key);
  }

  public ReferenceClass get(EObject key) {
    return this.table.get(key);
  }
}
