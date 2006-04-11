import aterm.*;
import aterm.pure.*;
import example.house.*;
import example.house.types.*;

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

  class RewriteSystem extends example.house.HouseVisitableFwd {
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
  
  class BedExchange extends example.house.HouseVisitableFwd {
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
