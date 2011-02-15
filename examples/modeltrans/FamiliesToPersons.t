/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce:w
 the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the INRIA nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package modeltrans;

import modeltrans.familiestopersons.metamodel.types.*;

public class FamiliesToPersons {

  // signature -> meta-model
  // algebraic terms -> models
  %gom {

    module MetaModel
      imports int String

      abstract syntax
      
      Families = Families(Family*)

      Family = Family(Name:String,Father:String,Mother:String,Sons:StringList,Daughters:StringList) 

      StringList = StringList(String*)

      Persons = Persons(Person*)

      Person = Male(Name:String)
             | Female(Name:String)

  }

  // transformations from Families model to Persons model
  public static Persons FamiliesToPersons(Families source) {
    Persons target =`Persons();
    %match(source) {
      Families(_*,Family(name,father,mother,sons,daughters),_*) -> {
        target = `Persons(target*,Male(name+" "+father),Female(name+" "+mother));
        %match(sons) {
          StringList(_*,s,_*) -> {
            target = `Persons(target*,Male(name+" "+s));
          }
        }
        %match(daughters) {
          StringList(_*,s,_*) -> {
            target = `Persons(target*,Female(name+" "+s));
          }
        }
      }
    }
    return target;
  }

}
