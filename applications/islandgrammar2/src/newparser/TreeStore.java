package newparser;

import java.util.LinkedList;
import java.util.Queue;

import org.antlr.runtime.tree.Tree;

public class TreeStore {

  private Queue<Tree> store = null; 
  
  // this is a singleton ===================
  private static TreeStore instance = null;
  
  public static TreeStore getInstance(){
    if(instance==null){
      instance=new TreeStore();
    }
    
    return instance;
  }
  
  private TreeStore(){
    store = new LinkedList<Tree>();
  }
  // =======================================
  

  public void storeTree(Tree tree){
    store.add(tree);
  }
  
  public Tree getTree(){
    return store.poll();
  }
}
