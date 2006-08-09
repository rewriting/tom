
package strategy.graphterm.types.graphterm;

public abstract class refGraphTerm extends strategy.graphterm.types.GraphTerm implements tom.library.strategy.mutraveler.MuReference{


  public int length() {
    int count = 0;
    if(this instanceof strategy.graphterm.types.graphterm.ConsrefGraphTerm) {
      strategy.graphterm.types.GraphTerm tl = ((strategy.graphterm.types.graphterm.ConsrefGraphTerm)this).getTailrefGraphTerm();
      if (tl instanceof refGraphTerm) {
        return 1+((refGraphTerm)tl).length();
      } else {
        return 2;
      }
    } else {
      return 0;
    }
  }

 public int[] toArray() {
    int[] array;
    if(this instanceof strategy.graphterm.types.graphterm.ConsrefGraphTerm) {
      int h = ((strategy.graphterm.types.graphterm.ConsrefGraphTerm)this).getHeadrefGraphTerm();
      strategy.graphterm.types.GraphTerm tl = ((strategy.graphterm.types.graphterm.ConsrefGraphTerm)this).getTailrefGraphTerm();
      if (tl instanceof refGraphTerm) {
        int[] tailArray =((refGraphTerm)tl).toArray();
        array = new int[1+tailArray.length];
        array[0]=h;     
        for(int i =0;i<tailArray.length;i++){
          array[i+1]=tailArray[i];
        }
      } else {
        array = new int[1];
        array[0]=h;  
      }
    } else {
        array = new int[0];
    }
    return array;
  }

  public strategy.graphterm.types.GraphTerm reverse() {
    if(this instanceof strategy.graphterm.types.graphterm.ConsrefGraphTerm) {
      strategy.graphterm.types.GraphTerm cur = this;
      strategy.graphterm.types.GraphTerm rev = strategy.graphterm.types.graphterm.EmptyrefGraphTerm.make();
      while(cur instanceof strategy.graphterm.types.graphterm.ConsrefGraphTerm) {
        rev = strategy.graphterm.types.graphterm.ConsrefGraphTerm.make(((strategy.graphterm.types.graphterm.ConsrefGraphTerm)cur).getHeadrefGraphTerm(),rev);
        cur = ((strategy.graphterm.types.graphterm.ConsrefGraphTerm)cur).getTailrefGraphTerm();
      }
      return rev;
    } else {
      return this;
    }
  }


}
