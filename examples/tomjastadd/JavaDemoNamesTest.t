package tomjastadd;

import tomjastadd.AST.*;
import java.util.Set;
import java.util.HashSet;
import tom.library.sl.*;
import org.junit.Test;
import org.junit.Assert;

public class JavaDemoNamesTest {

  %include{ JavaDemoNames.tom }
  %include{ sl.tom }
  %include{ util/types/Set.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(JavaDemoNamesTest.class.getName());
  }

  @Test
  public void test() {
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
    System.out.println("subject ast");
    p.printTree();
    try {
      Set fields = new HashSet();
      System.out.println("collect fields");
      `TopDown(CollectField(fields)).visit(p, new LocalIntrospector());
      System.out.println(fields);
      System.out.println("rename");
      p = `TopDown(RenameField("f")).visit(p, new LocalIntrospector());
      //TODO: Test the renaming (Jatsadd does not generate a deep equal)
      p.printTree();
      Assert.assertTrue(fields.size() == 1 && fields.contains("f"));
    } catch(VisitFailure e) {
      Assert.fail("Unexpected Visit failure!");
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
