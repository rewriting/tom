/*
 * Copyright (c) 2004-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package tutorial;

import aterm.*;
import aterm.pure.*;
import tutorial.cookbook.house.*;
import tutorial.cookbook.house.types.*;

import java.util.*;
import tom.library.traversal.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class Cookbook {
  
  private HouseFactory factory;
  private GenericTraversal traversal;
  
  public Cookbook(HouseFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }
  public HouseFactory getHouseFactory() {
    return this.factory;
  }
  
  %vas { 
    module House
      imports str
      
    public
      sorts House Room PieceOfFurniture Furniture
      abstract syntax
      
      concRoom(Room*) -> House
      room(name: str, furniture: Furniture) -> Room
      test(name: str, furniture: Furniture) -> Room
      //testInt()->Int
      concPieceOfFurniture(PieceOfFurniture*) -> Furniture
      bed() -> PieceOfFurniture
      chair() -> PieceOfFurniture
      armchair() -> PieceOfFurniture
      fridge() -> PieceOfFurniture
  }

  %include { mutraveler.tom }

  public final static void main(String Args[]) {
    Cookbook ex = new Cookbook(HouseFactory.getInstance(new PureFactory()));
    ex.run();
  }
  private void run() {
    House myHouse = `concRoom(room("kitchen",concPieceOfFurniture(chair(),chair(),fridge())),room("bedroom",concPieceOfFurniture(bed(),chair())));
    myHouse = replaceChairs(myHouse);
    seekChairs(myHouse);
    RewriteSystem rule = new RewriteSystem();
    VisitableVisitor rule2 = new BedExchange();
    try {
      System.out.println("subject = " + myHouse);
      `BottomUp(Try(rule)).visit(myHouse);
      System.out.println("bottumup catched = " + rule.bagsize() + " armchairs");
      myHouse = (House)`BottomUp(rule2).visit(myHouse);
      System.out.println("bottumup new subject = " + myHouse);
    } catch (VisitFailure e) {
      System.out.println("failed on = " + myHouse);
    }
    seekChairs(myHouse);
  }
  
  public House replaceChairs(House subject) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm subject) {
          if(subject instanceof PieceOfFurniture) {
            %match(PieceOfFurniture subject) {
              chair() -> { return `armchair(); }
              _ -> { return subject; }
            }
          }
          return traversal.genericTraversal(subject,this);
        } // end apply
      }; // end replace

    return (House)replace.apply(subject);
  } //replaceChairs

 private void seekChairs(House subject) {
   Collect1 collect = new Collect1() { 
       public boolean apply(ATerm subject) {
         if(subject instanceof PieceOfFurniture) {
           %match(PieceOfFurniture subject) {
             chair() -> { 
              System.out.println("Chair !");
              return false;
             }
             armchair() -> { 
              System.out.println("Armchair !");
              return false;
             }
             bed() -> { 
              System.out.println("Bed !");
              return false;
             }
           }
         }
         return true;
       } // end apply
     }; // end collect
   // use a traversal to get all interesting subtree
   traversal.genericCollect(subject,collect);
 } //seekChairs

  class RewriteSystem extends tutorial.cookbook.house.HouseVisitableFwd {
    Collection bag; 
    public RewriteSystem() {
      super(`Fail());
      bag = new HashSet();
    }
    public int bagsize() {
	return bag.size();	
    }
    
    public PieceOfFurniture visit_PieceOfFurniture(PieceOfFurniture arg) throws jjtraveler.VisitFailure {
      bag.add(arg);
      return arg;
    }
  } //RewriteSystem
  
  class BedExchange extends tutorial.cookbook.house.HouseVisitableFwd {
    public BedExchange() {
      super(`Identity());
    }

    public PieceOfFurniture visit_PieceOfFurniture(PieceOfFurniture arg) throws jjtraveler.VisitFailure {
      %match(PieceOfFurniture arg) {
        armchair() -> { return `bed(); }	
      }
      return arg;
    }
  } //RewriteSystem
}
