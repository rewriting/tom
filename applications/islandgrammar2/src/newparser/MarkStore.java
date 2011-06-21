package newparser;

import java.util.LinkedList;
import java.util.Queue;

public class MarkStore {

  private Queue<Integer> store = null; 
  
  // this is a singleton ===================
  private static MarkStore instance = null;
  
  public static MarkStore getInstance(){
    if(instance==null){
      instance=new MarkStore();
    }
    
    return instance;
  }
  
  private MarkStore(){
    store = new LinkedList<Integer>();
  }
  // =======================================
  

  public void storeMark(Integer mark){
    store.add(mark);
  }
  
  public Integer getMark(){
    return store.poll();
  }
  
}
