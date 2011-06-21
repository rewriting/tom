package newparser;

import java.util.LinkedList;
import java.util.Queue;

import org.antlr.runtime.tree.Tree;

/**
 * Used to store tree from lexer and get them back from parser.
 * TODO : it uses a Queue, that idea comes from islandgrammar (first episode),
 * and it seems to work well. Will it ALWAYS work ?
 */
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
