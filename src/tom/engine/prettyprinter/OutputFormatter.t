package tom.engine.prettyprinter;

import ppeditor.*;
import java.lang.StringBuffer;
import java.util.*;
import java.util.Comparator;

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

  public void printAll() {

    Iterator<Node> iteratorOnNode= this.theNodeList.iterator();
    while (iteratorOnNode.hasNext()){
      System.out.println("->"+iteratorOnNode.next().getText());
    }
  }

  public void stock(String text, int ligne, int colonne, int type) { 
    
    theNodeList.add(new Node(text, new PPTextPosition(ligne, colonne), type));
  }

  public StringBuffer dump() {

    Iterator<Node> iteratorOnNode= this.theNodeList.iterator();
    Node aNode;
    boolean type;
    while (iteratorOnNode.hasNext()){
      aNode=iteratorOnNode.next();
      type=(aNode.getType()==1);
//      System.out.println(aNode.getBegin().getColumn());
/*      if(aNode.getBegin().getLine() <= this.theCursor.getPosition().getLine()+1 && aNode.getBegin().getColumn() < this.theCursor.getPosition().getColumn()+1) {
          System.out.println("Position curseur : "+(this.theCursor.getPosition().getLine()+1)+" et "+(this.theCursor.getPosition().getColumn()+1));
          System.out.println("Position node : "+(aNode.getBegin().getLine())+" et "+(aNode.getBegin().getColumn())+"\n");
          this.write(aNode.getText(),this.theCursor.getPosition().getLine()+1, this.theCursor.getPosition().getColumn()+1,type);
      } else {
        this.write(aNode.getText(),aNode.getBegin().getLine(), aNode.getBegin().getColumn(),type);
      }i*/
     this.write(aNode.getText(),aNode.getBegin().getLine(), aNode.getBegin().getColumn(),type);
     System.out.println("Position début :"+aNode.getBegin().getLine()+" et "+aNode.getBegin().getColumn());
     }
    return theCursor.dump("./"+this.fileName);
  }

  public class Node {
  
    private PPTextPosition begin;
//    private PPTextPosition end; //endPosition
    private int type;
    private String text;

    public Node (String txt, PPTextPosition b, int t) {
//    public Node (String txt, PPTextPosition b, PPTextPosition e, int t) { //endPosition
      
      this.begin= b;
//      this.end= e; //endPosition
      this.type= t;
      this.text= txt;
    } 

    public PPTextPosition getBegin() {
      return this.begin;
    }
/*    public PPTextPosition getEnd() { //endPosition
      return this.end;
    }*/
    public int getType() {
      return this.type;
    }
    public String getText() {
      return this.text;
    }
   
  }

  public class ComparatorOnNode implements Comparator<Node> {

    public int compare(Node n1, Node n2) {
      if (n1.getBegin().getLine()!=n2.getBegin().getLine()){
        return n1.getBegin().getLine()-n2.getBegin().getLine();
      }
      else if(n1.getBegin().getColumn()!=-n2.getBegin().getColumn()){
        return n1.getBegin().getColumn()-n2.getBegin().getColumn();
      }
      else{ return -1;}
    }
    public boolean equals(Object o) {
      return false;
    }
  }
} 
