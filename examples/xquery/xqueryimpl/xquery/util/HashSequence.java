package xquery.util;

import java.util.*;

public class HashSequence extends Sequence {
  private Hashtable hashtable = null; 

  public HashSequence() 
  {
	super();
	this.hashtable = new Hashtable(); 
	
  }

  public boolean add(Object o)
  {
	if (hashtable.put(o, o)==null){
	  super.add(o);
	}
	  
	return true;
  }
  
  public boolean addAll(Collection c)
  {
	Iterator i = c.iterator(); 
	boolean changed =false; 

	while (i.hasNext()) {
	  Object o = i.next(); 
	  if (hashtable.put(o,o)==null) {
		changed = true; 
		super.add(o);
	  }
	}

	return changed;
  }
}