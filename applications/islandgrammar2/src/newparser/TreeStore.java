package newparser;

import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.tree.Tree;

public class TreeStore {

  private Map<Object, Tree> store = null; 
  
  // this is a singleton ===================
  private static TreeStore instance = null;
  
  public static TreeStore getInstance(){
    if(instance==null){
      instance=new TreeStore();
    }
    
    return instance;
  }
  
  private TreeStore(){
    store = new HashMap<Object, Tree>();
  }
  // =======================================
  

  public void storeTree(Object key,Tree tree){
    store.put(key, tree);
  }
  
  public Tree pickTree(Object key){
    return store.get(key);
  }
}
