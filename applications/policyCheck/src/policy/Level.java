package policy;

import java.util.ArrayList;

public class Level {
 int identifier;
 String name;
 
 Level(int i){
	identifier=i;
 }
 
 Level(int i,String name){
		identifier=i;
		name=name;
}
 
 
 
 ArrayList<Level> smaller;
 ArrayList<Level> bigger;
 
 
}
