package ted;

import aterm.*;
import aterm.pure.PureFactory;
import java.io.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.*;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.Position;
 

public class VisitableViewer {

  %include { atermmapping.tom }
  %include { mutraveler.tom }

  // for parsing
  private static ATermFactory atermFactory = new PureFactory();


  public static void visitableToDotStdout(jjtraveler.Visitable v) {
    visitableToDotStdout(v,null,"");
  }
  
  public static void 
    visitableToDotStdout(jjtraveler.Visitable v, Position hilight, String color)
    {
      try {
        Writer w = new BufferedWriter(new OutputStreamWriter(System.out)); 
        visitableToDot(v,w,hilight,color);
        w.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  public static void visitableToDot(jjtraveler.Visitable v, Writer w) 
    throws java.io.IOException {
      visitableToDot(v,w,null,"");
    }


  public static void 
    visitableToDot(jjtraveler.Visitable v, Writer w, Position hilight, String color) 
    throws java.io.IOException {
      ATerm at = atermFactory.parse(v.toString());
      ATermToDot(at, w, hilight, color);
    }

  public static 
    void ATermToDot(ATerm term, Writer w, Position hilight, String color) 
    throws java.io.IOException 
    {
      w.write("digraph g { graph [ordering=out];\n");
      ATermToDotRec(term, w, new Position(), hilight, color);
      w.write("}\n");
    }

  private static String posToId(Position p) {
    int[] a = p.toArray();
    StringBuffer buf = new StringBuffer("pos_");
    for(int i=0; i<p.depth(); i++) {
      buf.append(a[i]);
    }
    return buf.toString();
  }

  private static void ATermToDotRec
    (ATerm term, Writer w, Position pos, Position hilight, String color)
    throws java.io.IOException {
      %match(ATerm term) {
        a@ATermAppl(AFun[name=name],list) -> {
          // node id declaration
          if (pos.equals(hilight)) {
            w.write(%[@posToId(pos)@ [label="@`name@",fillcolor="@color@",style=filled];]%);
          } else {
            w.write(%[@posToId(pos)@ [label="@`name@"];]%);
          }

          // arrows 
          int i=0;
          for(; !`list.isEmpty(); `list = `list.getNext()) {
            ATerm t = `list.getFirst(); i++;
            w.write(%[@posToId(pos)@ -> ]%);
            pos.down(i);
            w.write(%[@posToId(pos)@;]%);
            ATermToDotRec(t,w,pos,hilight,color);
            pos.up();
          }
        }
      }
    }
}
