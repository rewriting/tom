package newparser;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class provide a way to store ANTLRCharStream's marck at lexing time and
 * to get them back parsing time.<br/>
 * TODO : the singleton aspect will be problematic if several files are parsed
 * at the same time or one after the other. Mark should maybe be identified by
 * filename to.
 */
public class MarkStore {

  private Map<String, Integer> store = null; 
  
  // this is a singleton ===================
  private static MarkStore instance = null;
  
  public static MarkStore getInstance(){
    if(instance==null){
      instance=new MarkStore();
    }
    
    return instance;
  }
  
  private MarkStore(){
    store = new TreeMap<String, Integer>();
  }
  // =======================================
  

  public void storeMark(int mark, int line, int posInLine){
    store.put(""+line+":"+posInLine, mark);
  }
  
  public int getMark(int line, int posInLine){
    return store.remove(""+line+":"+posInLine);
  }
  
}
