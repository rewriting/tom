package tom.engine.prettyprinter;

import ppeditor.*;
import java.lang.StringBuffer;
import java.util.*;

public class OutputFormatter{

  private String fileName;
  private PPCursor theCursor;
  private TreeSet theNodeList;
  
  public OutputFormatter(String n) {

    this.fileName=n;
    this.theCursor=new PPCursor(0,0);
    this.theCursor.setInsertion(false);
    this.theNodeList=new TreeSet(new ComparatorOnNode()); 
  }

  public void write(String text, int ligne, int colonne, boolean insertion) { 

    theCursor.setPosition(new PPTextPosition(ligne-1,colonne-1));
    theCursor.setInsertion(insertion);
    theCursor.write(text);
  }

  public void stock(String text, int ligne, int colonne, int type) { 
    
  }

  public StringBuffer dump() {

    return theCursor.dump("./"+this.fileName);
  }

  public class Node {
  
    private PPTextPosition begin;
    private int type;

    public Node (PPTextPosition b) {
      
      this.begin=b;
    } 

    public PPTextPosition getBegin() {
      return this.begin;
    }
    public int getType() {
      return this.type;
    }
  
  }

  public class ComparatorOnNode implements Comparator<Node> {

    public int compare(Node n1, Node n2) {
      if (n1.getBegin().getLine()!=n2.getBegin().getLine()){
        return n1.getBegin().getLine()-n2.getBegin().getLine();
      }else{
        return n1.getBegin().getColumn()-n2.getBegin().getColumn();
      }
    }
    public boolean equals(Object o) {
      return false;
    }
  }
} 
