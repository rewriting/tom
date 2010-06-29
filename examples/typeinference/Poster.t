import poster.puzzle.types.*;
import tom.library.sl.*;

public class Poster {
  %include { sl.tom } 
  %gom {
    module Puzzle
    abstract syntax
    Color = Green()
           | Yellow()
           | Blue()

    Shape = Circle()
           | Star()
           | Triangle()
           | Pentagon()
           | Plus()
           | Square()

    Block = Rectangle(c: Color, s1: Shape, s2: Shape)

    Puzzle = concBlock(Block*)
  }

  //public Poster 
  public static boolean isValidBlock(Block b) {
    %match(b) {
      Rectangle(Yellow(),Circle(),Star()) -> { return true; }
      Rectangle(Green(),Triangle(),Pentagon()) -> { return true; }
      Rectangle(Blue(),Plus(),Square()) -> { return true; }
    }
    return false;
  }

  %strategy TransformBlock() extends Identity() {
    visit Block {
      Rectangle(Yellow(),Circle(),Star())
        -> { return `Rectangle(Yellow(),Circle(),Circle()); }
      Rectangle(Green(),Triangle(),Pentagon()) 
        -> { return `Rectangle(Green(),Triangle(),Triangle()); }
      Rectangle(Blue(),Plus(),Square()) 
        -> { return `Rectangle(Blue(),Square(),Square()); }
    }
  }
/*
  public static void main(String[] args) {
    Block rect1 = `Rectangle(Yellow(),Circle(),Star());
    Block rect2 = `Rectangle(Green(),Triangle(),Pentagon());
    Block rect3 = `Rectangle(Blue(),Plus(),Square());

    Puzzle rects = `concBlock(rect1,rect2,rect3); 
    
    if (isValidBlock(rect1)) {
      try {
        Puzzle new_rects = `One(TransformBlock()).visit(rects);
        System.out.println(rects);
        System.out.println(new_rects);

        new_rects = `TopDown(TransformBlock()).visit(rects);
        System.out.println("\n" + rects);
        System.out.println(new_rects);

      } catch (VisitFailure e) {
        System.out.println("reduction failed on: " + rects);
      }
    }
  }
  */
  public static void main(String[] args) {
    Puzzle puzzle1 = `concBlock(Rectangle(Yellow(),Circle(),Star()),
                               Rectangle(Green(),Triangle(),Pentagon()),
                               Rectangle(Blue(),Plus(),Square())); 

    Puzzle puzzle2 = `concBlock(Rectangle(Green(),Circle(),Star()),
                               Rectangle(Blue(),Triangle(),Pentagon()),
                               Rectangle(Yellow(),Plus(),Square())); 
      try {
        Puzzle applyOne = `One(TransformBlock()).visit(puzzle1);
        System.out.println("\n" + applyOne);
        Puzzle applyTopDown = `TopDown(TransformBlock()).visit(puzzle1);
        System.out.println("\n" + applyTopDown);
        Puzzle applyIdentity= `One(TransformBlock()).visit(puzzle2);
        System.out.println("\n" + applyIdentity);
    } catch (VisitFailure e) { System.out.println("Strategy failed!"); }
  }
}
