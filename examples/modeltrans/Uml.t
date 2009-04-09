/*
 * Copyright (c) 2004-2009, INRIA
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

import modeltrans.uml.metamodel.types.*;

public class Uml {

  // signature -> meta-model UML
  // algebraic terms -> models UML
  %gom(--termgraph) {

    module MetaModel
      imports int String

      abstract syntax

      ModelElementList = List(ModelElement*)
      
      ModelElement = ModelElement(name:String, constraint:ModelElement, type:ModelElementType)
                   | Empty()

      ModelElementType = Attribute()
                       | AssociationEnd(low:Mult, high:Mult, participant:ModelElement)
                       | ConstraintElt(constrainedElement: ModelElement, body:Constraint)
                       | Class(features: ModelElementList)
                       | Association(connections: ModelElementList)
                       | AssociationClass(features: ModelElementList, connections: ModelElementList)

     Mult = Mult(value:String)

     Constraint = Constraint(value: String)
                          
  }

  %include{ sl.tom }

  /**
   * transform an association class to a ternary association
   * The association class A on the left-hand side is split up into a class A
   * and a ternary association RA on the right-hand side.
   */


  %strategy AssociationClass2TernaryAssociation() extends Identity() {
    visit ModelElement {

      ModelElement[name=name, type=AssociationClass[connections=List(X1*,end1,X2*,end2,X3*)]]
      && ModelElement[name=n_end1, type=AssociationEnd(low_end1,high_end1, c_end1)] << end1
      && ModelElement[name=n_end2, type=AssociationEnd(low_end2,high_end2, c_end2)] << end2 -> {
         
        String c1 = `n_end1+"->size>=" + `low_end1+" and "+`n_end1+"->size<="+`high_end1;
        String c2 = `n_end2+"->size>=" + `low_end2+" and "+`n_end2+"->size<="+`high_end2;
        String c3 = `n_end1+"->size=1 and "+`n_end2+"->size=1";

        ModelElement newend1 = `ModelElement(n_end1, Empty(), AssociationEnd(Mult("0"),Mult("1"), LabModelElement("l1",c_end1.setconstraint(ModelElement("c1", Empty(), ConstraintElt(RefModelElement("l1"), Constraint(c2)))))));

        ModelElement newend2 = `ModelElement(n_end2, Empty(), AssociationEnd(Mult("0"),Mult("1"), LabModelElement("l2", c_end2.setconstraint(ModelElement("c2", Empty(), ConstraintElt(RefModelElement("l2"), Constraint(c1)))))));

        ModelElement end3 = `ModelElement(name.toLowerCase(), Empty(), AssociationEnd(Mult("0"),Mult("1"),LabModelElement("l3",ModelElement(name, ModelElement("c3", Empty(), ConstraintElt(RefModelElement("l3"),Constraint(c3))), Class(List(X1,X2,X3))))));

        return `ModelElement("R"+name, Empty(), Association(List(newend1,newend2,end3)));

      }
    }

  }


  public static void main(String[] args) {
    ModelElement example = `ModelElement("Job", Empty(), 
        AssociationClass(
          List(
            ModelElement("salary", Empty(), Attribute())
            ),
          List(
            ModelElement("employer", Empty(), AssociationEnd(Mult("1"), Mult("3"), ModelElement("Compagny", Empty(), Class(List())))),
            ModelElement("employee", Empty(), AssociationEnd(Mult("0"), Mult("infinity"), ModelElement("Person", Empty(), Class(List()))))
            )));
    try {
      tom.library.utils.Viewer.display(`AssociationClass2TernaryAssociation().visitLight(example).expand());
      //System.out.println(`AssociationClass2TernaryAssociation().visitLight(example).expand());
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("Unexpected strategy failure!");
    }
  }

}
