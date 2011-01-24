import gates.logic.types.*;
  public class Gates {
    %gom {
      module Logic
        imports int
        abstract syntax
        Bool = Input(n:int)
             | True()
             | False()
             | Not(b:Bool)
             | Or(b1:Bool, b2:Bool)
             | And(b1:Bool, b2:Bool)
             | Nand(b1:Bool, b2:Bool)
             | Xor(b1:Bool, b2:Bool)
 
        module Logic:rules() {
          Not(a)   -> Nand(a,a)
          Or(a,b)  -> Nand(Not(a),Not(b))
          And(a,b) -> Not(Nand(a,b))
          Xor(a,b) -> Or(And(a,Not(b)),And(Not(a),b))
          Nand(False(),_)     -> True()
          Nand(_,False())     -> True()
          Nand(True(),True()) -> False()
        }
    }
    public final static void main(String[] args) {
    Bool b = `Xor(True(),False());
    System.out.println("b = " + b);
  }
}

