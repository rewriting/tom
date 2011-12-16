package futil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.antlr.runtime.tree.DOTTreeGenerator;
import org.antlr.runtime.tree.Tree;

/**
 * Provide a function that generate png files from ANTLRTrees.
 */
public class ANTLRTreeDrawer {

  private ANTLRTreeDrawer(){;}
  
  public static void draw(String filename, Tree tree) throws IOException{
    
    String dotFilename = filename+".dot";
    BufferedWriter dotWriter =
      new BufferedWriter(new FileWriter(new File(dotFilename)));
    
    DOTTreeGenerator dotTreeGen = new DOTTreeGenerator();
    String dotContent = dotTreeGen.toDOT(tree).toString();
    
    dotWriter.write(dotContent);
    dotWriter.flush();
    dotWriter.close();
    
    Runtime.getRuntime().exec("dot -Tpng "+dotFilename+" -o "+filename);
    //Runtime.getRuntime().exec("rm "+dotFilename);
  }
  
}
