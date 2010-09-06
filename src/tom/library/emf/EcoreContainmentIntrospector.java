/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2009-2010, INPL, INRIA
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
 * Nicolas Henry
 *
 **/

package tom.library.emf;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

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
  
  @Override
  public Object getChildAt(Object o, int i) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.get(i);
    }
    return null;
  }

  @Override
  public int getChildCount(Object o) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.size();
    }
    return 0;
  }

  @Override
  public Object[] getChildren(Object o) {
    List<Object> l=getList(o);
    if(l!=null){
      return l.toArray();
    }
    return null;
  }

  @Override
  public <T> T setChildAt(T o, int i, Object obj) {
    List<Object> l=getList(o);
    if(l!=null){
      l.set(i,o);
      return o;
    }
    return null;
  }

  @Override
  public <T> T setChildren(T o, Object[] objs) {
    List<Object> l=getList(o);
    if(l!=null){
      for(int i = 0; i < objs.length; i++) {
        l.set(i, objs[i]);
      }
      return o;
    }
    return null;
  }

}
