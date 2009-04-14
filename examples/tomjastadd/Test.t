package tomjastadd;

import tomjastadd.AST.*;
import java.util.Set;
import java.util.HashSet;
import tom.library.sl.*;

public class Test {

  %include{ JavaDemoNames.tom }
  %include{ sl.tom }
  %include{ util/types/Set.tom }

  public static void main(String[] args) {
    System.out.println("Building a small AST with generic names which are resolved");
    System.out.println("The tree structure is printed to show the resulting tree nodes");
    /* use Tom backquote construct for creating ASTs */
    Prog p = (Prog) `Prog(
        List(
          CompUnit(
            "comp",
            List(
              ClassDecl("Object", TypeName("Object"), 
                List(
                  FieldDecl(Dot("dot", ParseName("comp"), ParseName("Object")), "f", 
                    Dot("dot", ParseName("comp"), Dot("dot", ParseName("Object"), ParseName("f")))
                    )
                  )  
                )
              )
            ) 
          )
        );
    p.printTree();
    try {
      Set fields = new HashSet();
      System.out.println("collect fields");
      `TopDown(CollectField(fields)).visit(p, new LocalIntrospector());
      System.out.println(fields);
      System.out.println("rename the field f");
      p = `TopDown(RenameField("f")).visit(p, new LocalIntrospector());
      p.printTree();
    } catch(VisitFailure e) {
      System.out.println("Unexpected failure!");
    }
  }

  %strategy CollectField(set:Set) extends Identity() {
    visit ASTNode {
      FieldDecl[name=name] -> {
        set.add(`name);
      }
    }
  }

  %strategy RenameField(name:String) extends Identity() {
    visit ASTNode {
      FieldDecl(type,n,value) && n==name -> {
        return `FieldDecl(type,"new_"+name,value);
      }
    }
  }

}
