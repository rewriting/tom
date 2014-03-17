/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2014, Universite de Lorraine, Inria
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
 * Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

import tom.engine.adt.tomtype.types.*;

public class Substitution {
  private HashMap<TomType,TomType> substitutions;

  public Substitution() {
    this.substitutions = new HashMap<TomType,TomType>();
  }

  public TomType get(TomType key) {
    return substitutions.get(key);
  }

  public boolean containsKey(TomType key) {
    return substitutions.containsKey(key);
  }

  /**
   * The method <code>addSubstitution</code> adds a substitutions (i.e. a pair
   * (type1,type2) where type2 is the substitution for type1) into the
   * global list "substitutions" and saturate it.
   * For example, to add a pair (X,Y) where X is a type variable and Y is a type
   * which can be a type variable or a ground type, we follow two steps:
   * <p>
   * STEP 1:  a) put(X,Z) if (Y,Z) is in substitutions or
   *          b) put(X,Y) otherwise
   * <p>
   * STEP 2:  a) put(W,Z) after step 1.a or put(W,Y) after step 1.b
   *             if there exist (W,X) in substitutions for each (W,X) in substitutions
   *          b) do nothing otherwise
   * @param key   the first argument of the pair to be inserted (i.e. the type1) 
   * @param value the second argument of the pair to be inserted (i.e. the type2) 
   */
  public void addSubstitution(TomType key, TomType value) {
    /* STEP 1 */

    TomType newValue = substitutions.get(value);
    if(newValue == null) {
      newValue = value;
    }
    //TomType newValue = value;
    //if (substitutions.containsKey(value)) {
    //  newValue = substitutions.get(value); 
    //} 
    substitutions.put(key,newValue);

    /* STEP 2 */
    for (TomType currentKey : substitutions.keySet()) {
      if (substitutions.get(currentKey) == key) {
          substitutions.put(currentKey,newValue);
        }
      }
  }
}

